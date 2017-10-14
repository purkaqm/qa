package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AbstractWorkPage;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 04.06.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class Issue85789Test1 extends PSParallelTestDriver {

    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
        AbstractWorkPage.setUseDirectLink(false);
    }


    private static final Map<String, String> contexts = new HashMap<String, String>() {
        {
            put("ppx_94/test", "ROOT WORK FOR AUTOTESTS");
            put("ppx_94/test2", "ROOT WORK FOR AUTOTESTS 2");
            put("ppx_94/test3", "ROOT WORK FOR AUTOTESTS");
        }
    };
    private static final int COUNT = 10;

    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[contexts.size() * 2][0];
        int i = 0;
        for (String context : contexts.keySet()) {
            User user = (User) getTestData().getDefaultUser().clone();
            Work work = new Work(contexts.get(context));

            Data d1 = new Data(user, work, context);
            Data d2 = new Data(user, null, context);
            d1.user = (User) CoreProperties.getDefaultUser().clone();
            res[i++] = new Object[]{d1};
            res[i++] = new Object[]{d2};
            list.add(d1);
        }
        return res;
    }


    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());

        BasicCommons.logIn(data.user, !TestSession.isVersionPresent(), data.context);
        if (data.isClearCache) {
            clearCache(true, true);
        } else {
            _test(data);
        }
    }

    private void _test(Data data) {
        try {
            data.isRunning = true;
            for (int i = 0; i < COUNT; i++) {
                if (DeadlockParallelListener.hasFailure()) return;
                WorkManager.openWBS(data.work).getOptions().showLevel(8);
                new HomePage().open();
            }
        } finally {
            data.isRunning = false;
        }
    }

    private class Data extends AbstractData {
        private Boolean isClearCache;
        private User user;
        private Work work;
        private String context;

        private Data(User u, Work w, String c) {
            user = u;
            work = w;
            context = c;
            isClearCache = work == null;
        }

        public String toString() {
            return user + ", " + (isClearCache ? "Clear cache thread" : "Open thread " + work.getName());
        }
    }
}
