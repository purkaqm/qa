package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.MetricInstancePage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.MetricManager;
import com.powersteeringsoftware.libs.tests.actions.UserManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 05.12.13
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
public class Issue88535Test extends PSParallelTestDriver {

    private static final int COUNT_1 = 14;
    private static final int COUNT_2 = 20;
    private static final int COUNT_3 = 0;
    private static int action = 0;

    @BeforeTest
    public void prepare() {
        PSPage.setCheckBlankPage(true);
        SummaryWorkPage.setWaitGrid(false);
        List<MetricInstance> mis = MetricManager.collectInstances(getTestData().getSGPWork());
        PSLogger.info(mis);
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < COUNT_3; i++) {
            String login = "test_" + i + "_" + CoreProperties.getTestTemplate();
            String pwd = "qwer";
            String email = login + "@teat.domain.local";
            String f = "F(" + i + ")" + CoreProperties.getTestTemplate();
            String l = "L(" + i + ")" + CoreProperties.getTestTemplate();
            User u = new User(login, pwd, f, l, email);
            UserManager.createUser(u);
            users.add(u);
        }
        WorkManager.setUser(getTestData().getSGPWork(), BuiltInRole.CONTRIBUTOR, users);
        WorkManager.setUser(getTestData().getSGPWork(), BuiltInRole.CHAMPION, users);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);

    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_1][0];
        for (int i = 0; i < COUNT_1; i++) {
            toData(getTestData().getSGPWork(), res, i);
        }
        return res;
    }

    private synchronized void toData(Work w, Object[][] res, int i) {
        List<MetricInstance> instances = new ArrayList<MetricInstance>();
        for (MetricInstance mi : TestSession.getMetricList()) {
            if (w.equals(mi.getWork()))
                instances.add(mi);
        }
        MetricInstance mi = instances.get(0);//PSTestData.getRandomFromList(instances);
        Work _w = w.copy();
        MetricInstance _mi = mi.copy();
        _mi.setWorkTypes();
        Data d = new Data(i + 1,
                (User) CoreProperties.getDefaultUser().clone(),
                _w,
                _mi);
        res[i] = new Object[]{d};
        list.add(d);
    }


    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        try {
            Thread.currentThread().setName(data.toString());
            BasicCommons.logIn(data.user, !TestSession.isVersionPresent());
            for (int i = 0; i < COUNT_2; i++) {
                PSLogger.info("Iteration #" + i + "(" + data.mi + ")");
                if (DeadlockParallelListener.hasFailure()) return;
                try {
                    if (action == 0) {
                        MetricManager.setLock(data.mi, !data.mi.getLocked());
                    } else if (action == 1) {
                        MetricManager.setReadyForRollup(data.mi, !data.mi.getReadyForRollup());
                    } else {
                        MetricInstancePage page = MetricManager.open(data.mi, true);
                        MetricInstancePage.Grid grid = page.getGrid();
                        MetricInstancePage.Grid.Row.Cell cell = grid.getRows().get(0).getCell(0);
                        cell.setTxt(String.valueOf(PSTestData.getRandom().nextInt(5000)));
                        grid.save();
                    }
                } catch (SeleniumException se) {
                    PSLogger.save("Exception on page");
                    PSLogger.fatal(se);
                    if (se.getCause() != null && se.getCause() instanceof PSApplicationException) {
                        new HomePage().open();
                    } else {
                        //throw se; //: ignore
                    }
                }
                check();
            }
        } finally {
            data.isRunning = false;
        }
    }

    private class Data extends AbstractData {
        private Work work;
        private MetricInstance mi;
        private int id;

        private Data(int i, User user, Work w, MetricInstance mi) {
            id = i;
            work = w;
            this.user = user;
            this.mi = mi;
        }

        public String toString() {
            return work.getName() + "," + mi.getName() + "(" + id + ")";
        }
    }

}
