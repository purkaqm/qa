package com.powersteeringsoftware.tests.parallel_loading;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.mail.PSResultProperties;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 13.01.12
 * Time: 18:40
 */
public class HomeLoadingTest {
    public static int count = 80;
    public static boolean useRandom = true;
    public static long timeOutForRefreshing = 10 * 60 * 1000;

    private static List<Result> results = new ArrayList<Result>();
    private static long start;
    private static final TimerWaiter BEFORE_REFRESH = new TimerWaiter(100);

    private class Result {
        private User user;
        private Long start;
        private Long finish;
        private String id;
        private Long click;
        //long wait;
        private List<Long> refreshTimeouts = new ArrayList<Long>();

        Result(String id, User user) {
            this.id = id;
            this.user = user;
        }

        public String toString() {
            return id + "(" + user.getLogin() + ")" + ": login time " + //click + "," + wait + "," +
                    getLoadingTimeInS() + "s, refresh time " + getRefreshTimeInS() + "s";
        }

        Float getLoadingTimeInS() {
            if (click == null) return null;
            return click / 1000F;
            //return (click + wait) / 1000F;
        }

        String testDuration() {
            if (start == null || finish == null) return null;
            return "Start: " + ((start - HomeLoadingTest.start) / 1000F) + "s, duration: " + ((finish - start) / 1000F) + "s";
        }

        Float getRefreshTimeInS() {
            float res = 0;
            int count = 0;
            for (Long t : refreshTimeouts) {
                if (t == null) continue;
                res += t;
                count++;
            }
            if (count == 0) return null;
            return res / 1000F / count;
        }
    }


    @BeforeTest
    @Parameters({
            "count",
            "refresh-timeout",
            "random"})
    public void before(@Optional String sCount, @Optional String sTimeout, @Optional String sRandom) {
        if (sCount != null && sCount.matches("\\d+")) {
            count = Integer.parseInt(sCount);
        }
        if (sTimeout != null && sTimeout.matches("\\d+")) { // timeout in min
            timeOutForRefreshing = Integer.parseInt(sTimeout) * 60 * 1000L;
        }
        if (sRandom != null) {
            useRandom = Boolean.parseBoolean(sRandom);
        }
        SeleniumDriverFactory.setParallel(true);
        CoreProperties.loadProperties();
        CoreProperties.setBrowser("ZERO");
        start = System.currentTimeMillis();
        //BasicCommons.logIn();
    }

    @DataProvider(parallel = true)
    public Object[][] users() {
        return TestData.getData(useRandom, count);
    }

    @AfterTest
    public void after() {
        PSLogger.info("After tests");
        SeleniumDriverFactory.setParallel(false);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        PSLogger.info(results);
        int logInCount = 0;
        int count = 0;
        int refreshCount = 0;
        int failed = 0;
        float averageLogInTime = 0;
        float averageRefreshTime = 0;
        for (Result res : results) {
            if (res.finish == null) failed++;
            PSLogger.debug(res.id + "{" + res.user.getLogin() + "}, " + res.testDuration());
            Float logInTime = res.getLoadingTimeInS();
            if (logInTime != null) {
                averageLogInTime += logInTime;
                logInCount++;
            }
            Float refreshTime = res.getRefreshTimeInS();
            if (refreshTime != null) {
                averageRefreshTime += refreshTime;
                refreshCount++;
            }
            //average += (res.wait + res.click) / 1000F;
            count++;
        }
        averageLogInTime /= logInCount;
        averageRefreshTime /= refreshCount;
        PSLogger.info("Average Result : " + count + ", " + averageLogInTime + ", " + averageRefreshTime);
        PSLogger.perTestInfo("Average Result : count=" + count + ", logInAverageTime=" + averageLogInTime + ", refreshAverageTime=" + averageRefreshTime);
        PSResultProperties.BODY_TEST_LOADING_HEADER.setValues("users", "failed", "login(s)", "refresh(s)");
        PSResultProperties.BODY_TEST_LOADING_TABLE.addValues(count, failed, averageLogInTime, averageRefreshTime);
        results.clear();
    }


    //@Test(threadPoolSize = COUNT, invocationCount = COUNT)
    @Test(dataProvider = "users")
    public void test(User user) throws Exception {
        String id = String.valueOf(Thread.currentThread().getId());
        Thread.currentThread().setName("User: " + user.getLogin());
        String expected = user.getFullName();
        String fromPage = null;
        Result res = new Result(id, user);
        res.start = System.currentTimeMillis();
        results.add(res);

        PSLogger.info("Test: " + id + "(" + user + ")");
        HomePage page = BasicCommons.logIn(user, false);
        long click = page.getLastClickSubmitTime();
        //long wait = page.getLoadingTime();
        res.click = click;
        //res.wait = wait;
        page.validate();

        fromPage = page.getFullUserName();
        PSLogger.info("User name from page: " + fromPage);
        Assert.assertEquals(fromPage, expected, "incorrect name for user " + user);

        long s = System.currentTimeMillis();
        while (System.currentTimeMillis() - s < timeOutForRefreshing) {
            BEFORE_REFRESH.waitTime();
            page.doRefresh();
            long time = page.getLoadingTime();
            res.refreshTimeouts.add(time);
            PSLogger.info("Refresh timeout " + time / 1000F + "s");
            fromPage = page.getFullUserName();
            PSLogger.info("User name from page after refresh : " + fromPage);
            Assert.assertEquals(fromPage, expected, "incorrect name for user " + user);
            page.validate();
        }
        res.finish = System.currentTimeMillis();
    }
}