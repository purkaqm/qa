package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 04.12.13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class Issue88500Test extends PSParallelTestDriver {

    private static final int COUNT_2 = 100;

    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
        SummaryWorkPage.setWaitGrid(false);
    }

    @DataProvider(parallel = true)
    public Object[][] data() {
        List<Work> works = Arrays.asList(
                getTestData().getSGPWork()
                , getTestData().getNSGPWork()
                , Work.create("pr1", getTestData().getSGPTemplate(), "o2k05fg0000k5gi8p2n0000000") // 11.0
                , Work.create("pr2", getTestData().getSGPTemplate(), "o2k05fg0000k5gia7cbg000000") // 11.0
                , Work.create("pr3", getTestData().getSGPTemplate(), "o2k05fg0000k5gian7kg000000") // 11.0
                //, Work.create("pr4", getTestData().getSGPTemplate(), "o2k05fg0000k5gib8rug000000") // 11.0

                //, Work.create("pr4", getTestData().getSGPTemplate(), "o2k05f80000k5gf2mbs0000000") // 10.0
                //, Work.create("pr3", getTestData().getSGPTemplate(), "o2k05f80000k5gf2d5b0000000") // 10.0
                //, Work.create("pr2", getTestData().getSGPTemplate(), "o2k05f80000k5gf22op0000000") // 10.0
                //,Work.create("pr1", getTestData().getSGPTemplate(), "o2k05f80000k5gf1o240000000") // 10.0
        );
        Object[][] res = new Object[works.size()][0];
        for (int i = 0; i < works.size(); i++) {
            toData(works.get(i), res, i);
        }
        return res;
    }

    private void toData(Work w, Object[][] res, int i) {
        Data d = new Data(i + 1, w,
                (User) CoreProperties.getDefaultUser().clone(),
                (User) CoreProperties.getDefaultUser().clone(),
                (User) getTestData().getFirstUser().clone());
        res[i] = new Object[]{d};
        list.add(d);
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        try {
            Thread.currentThread().setName(data.toString());
            BasicCommons.logIn(data.user, !TestSession.isVersionPresent());
            Work work = data.work;
            User user1 = data.user1;
            User user2 = data.user2;
            try {
                String fromPage = WorkManager.open(work).getOwner();
                work.setOwner(user1.getFullName().equals(fromPage) ? user1 : user2);
            } catch (RuntimeException re) {
                PSLogger.save("Exception on page"); //: ignore
                PSLogger.fatal(re);
            }
            User newOwner = work.getOwner().equals(user1) ? user2 : user1;
            for (int i = 0; i < COUNT_2; i++) {
                PSLogger.info("Iteration #" + i + "(" + newOwner.getFullName() + ")");
                if (DeadlockParallelListener.hasFailure()) return;
                try {
                    WorkManager.setOwner(work, newOwner);
                } catch (SeleniumException se) {
                    PSLogger.save("Exception on page");
                    PSLogger.fatal(se);
                    if (se.getCause() != null && se.getCause() instanceof PSApplicationException) {
                        new HomePage().open();
                    } else {
                        //throw se; //: ignore
                    }
                }
                newOwner = newOwner.equals(user1) ? user2 : user1;
                check();
            }
        } finally {
            data.isRunning = false;
        }
    }

    private class Data extends AbstractData {
        private Work work;
        private User user1;
        private User user2;
        private int id;

        private Data(int i, Work w, User u, User u1, User u2) {
            id = i;
            work = w;
            user = u;
            user1 = u1;
            user2 = u2;
        }

        public String toString() {
            return work.getName() + "(" + id + ")";
        }
    }
}
