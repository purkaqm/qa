package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.CopyMoveWorkPage;
import com.powersteeringsoftware.libs.pages.CreateWorkPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 17.04.13
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class CopyTest extends PSTestDriver {
    private Work parent;
    private Work init;

    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
        parent = getTestData().getRootWork();
        init = getTestData().getSGPWork(); // has attached measures
    }

    private static final int COUNT = 10;
    private static final int COUNT_2 = 5;
    private static final String NAME = "COPY";

    public PSTestData getTestData() {
        return new PSTestData() {
        };
    }

    @Test(
            threadPoolSize = COUNT,
            invocationCount = COUNT, groups = TestSettings.FIREFOX_ONLY_GROUP)
    public void test() {
        BasicCommons.logIn(!TestSession.isVersionPresent());

        for (int j = 0; j < COUNT_2; j++) {
            if (DeadlockParallelListener.hasFailure()) return;
            Work res = new Work(NAME + "_" + CoreProperties.getTestTemplate() + "_" + Thread.currentThread().getId() + "(" + j + ")");

            SummaryWorkPage sum = WorkManager.open(init, false, false);
            CopyMoveWorkPage copy = (CopyMoveWorkPage) sum.moveWork();
            copy.setLocation(parent, PSTestData.getRandom().nextBoolean());
            CreateWorkPage create = copy.copy();

            res.setParent(parent);
            create.setName(res.getName());
            SummaryWorkPage sum2 = (SummaryWorkPage) create.finish(false);
            sum2.validate();
            Assert.assertTrue(sum2.checkTitle(res));
            Assert.assertTrue(sum2.checkParent(res));
        }
    }
}
