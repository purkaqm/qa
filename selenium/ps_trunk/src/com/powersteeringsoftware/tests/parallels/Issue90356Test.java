package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by admin on 05.05.2014.
 * DB: sz_100_def_deadlocks
 */
public class Issue90356Test extends PSParallelTestDriver {

    private static final int COUNT_1 = 10; // number of threads
    private static final int COUNT_2 = 20; // number of iterations


    @BeforeTest
    public void prepare() {
        PSPage.setCheckBlankPage(true);
        SummaryWorkPage.setWaitGrid(false);
        WBSPage.setWaitGrid(false);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    @Test(threadPoolSize = COUNT_1,
            invocationCount = COUNT_1, groups = TestSettings.FIREFOX_ONLY_GROUP)
    public void test() {
        BasicCommons.logIn(!TestSession.isVersionPresent());
        Work org = getTestData().getOrg90356();
        for (int j = 0; j < COUNT_2; j++) {
            PSLogger.info("Iter #" + (j + 1));
            if (DeadlockParallelListener.hasFailure()) return;
            check();
            try {
                WorkManager.openWBS(org);
                PSLogger.save();
                clearCache();
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
        }
    }


}
