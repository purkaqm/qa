package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.DiscussionAddPage;
import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;
import com.powersteeringsoftware.libs.pages.DiscussionsPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of parallel using. Checking deadlocks like in bug #74487, #81373
 */
public class TollgateTestDriver extends PSParallelTestDriver {
    private static final int COUNT = 15;

    private static final int SUMMARY_OPEN_COUNT = 150;
    private static final int DISCUSSION_COUNT = 10;

    private String login;
    private String passwd;

    @BeforeTest
    public void before() {
        SeleniumDriverFactory.setParallel(true);
        CoreProperties.loadProperties();
        SummaryWorkPage.setUseDirectLink(false);
        SummaryWorkPage.setCheckBlankPage(true);
        User u = CoreProperties.getDefaultUser();
        login = u.getLogin();
        passwd = u.getPassword();
    }

    private class Data extends AbstractData {
        private Work work;
        private int action;


        public String toString() {
            String s;
            if (action == 0) {
                s = "Open summary thread";
            } else if (action == 1) {
                s = "Set status thread";
            } else {
                s = "Add/Delete discussion thread";
            }
            return user + ", " + s;
        }
    }

    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT][0];
        int s1 = COUNT / 5;
        int s2 = s1 + (COUNT - s1) / 2;
        for (int i = 0; i < COUNT; i++) {
            Data dt = new Data();
            dt.user = new User(login, passwd);
            dt.work = getTestData().getSGPWork().copy();
            dt.action = i < s1 ? 0 : (i < s2 ? 1 : 2);
            res[i] = new Object[]{dt};
        }
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.action == 2) {
            addDeleteDiscussionItems(data);
        } else if (data.action == 1) {
            setStatus(data);
        } else {
            openSummary(data);
        }
    }

    private void addDeleteDiscussionItems(Data data) throws Exception {
        try {
            list.add(data);
            data.isRunning = true;
            check();
            BasicCommons.logIn(data.user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
            SummaryWorkPage sum = SummaryWorkPage.openWork(data.work, false);
            DiscussionsPage tab = sum.openDiscussionsTab();
            for (int i = 0; i < DISCUSSION_COUNT; i++) {
                String test = "test " + i;
                DiscussionAddPage add = tab.pushAddNew();
                add.setSubject(test);
                add.setDescription(test);
                DiscussionIssueViewPage view = add.submit();
                tab = view.getBlock(test).delete().ok();
                check();
            }
            tab.openHome();
            check();
        } finally {
            data.isRunning = false;
        }
    }

    private void setStatus(Data data) throws Exception {
        try {
            list.add(data);
            data.isRunning = true;
            check();
            BasicCommons.logIn(data.user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
            SummaryWorkPage sum = SummaryWorkPage.openWork(data.work, false);
            List<Work.Status> list = new ArrayList<Work.Status>(Arrays.asList(Work.Status.values()));
            TestData.mixList(list);
            PSLogger.info("Statuses to test: " + list);
            for (Work.Status st : list) {
                try {
                    sum.setStatus(st);
                } catch (AssertionError se) {
                    PSLogger.error(se);
                }
                check();
            }
            sum.openHome();
            check();
        } finally {
            data.isRunning = false;
        }

    }


    private void openSummary(Data data) throws Exception {
        check();
        BasicCommons.logIn(data.user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
        for (int i = 0; i < SUMMARY_OPEN_COUNT; i++) {
            check();
            WorkManager.open(data.work);
            if (!isAnyAlive()) return;
        }
    }
}
