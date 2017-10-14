package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.InboxPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 06.05.13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class Issue85281Test extends PSParallelTestDriver {

    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
    }

    private static final int COUNT_1 = 50;
    private static final int COUNT_2 = 4;
    private static final int COUNT_3 = 7;


    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_2 + COUNT_3][0];

        for (int i = 0; i < COUNT_2; i++) {
            Data loginLogout = new Data();
            loginLogout.user = (User) CoreProperties.getDefaultUser().clone();
            loginLogout.isLoginLogout = true;
            res[i] = new Object[]{loginLogout};
            list.add(loginLogout);
        }

        for (int i = COUNT_2; i < COUNT_2 + COUNT_3; i++) {
            Data questionSubmit = new Data();
            questionSubmit.user = (User) CoreProperties.getDefaultUser().clone();
            questionSubmit.isLoginLogout = false;
            res[i] = new Object[]{questionSubmit};
        }

        return res;
    }


    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isLoginLogout) {
            loginLogoutTest(data);
        } else {
            questionSubmit(data.user);
        }
    }

    private void loginLogoutTest(Data data) {
        try {
            data.isRunning = true;
            HomePage home = BasicCommons.logIn(data.user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
            for (int i = 0; i < COUNT_1; i++) {
                home.doLogout();
                check();
                home = BasicCommons.logIn(data.user, false);
                //check();
            }
        } finally {
            data.isRunning = false;
        }
    }

    private void questionSubmit(User user) {
        HomePage home = BasicCommons.logIn(user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
        InboxPage inbox = home.openInbox();
        while (isAnyAlive()) {
            inbox.submit();
            //check();
            //inbox = home.openInbox();
        }
    }

    private class Data extends AbstractData {
        private boolean isLoginLogout;

        public String toString() {
            return user + ", " + (isLoginLogout ? "Login/Logout thread" : "Submit question thread");
        }
    }

}
