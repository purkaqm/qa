package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 09.10.12
 * Time: 14:21
 * To change this template use File | Settings | File Templates.
 */
public class Issue81484Test {


    static {
        CoreProperties.loadProperties();
    }

    private static final int COUNT = 5;
    private static final int LOGIN_LOGOUT_NUM = 100;

    private static User nobody;
    private static User test;

    @BeforeTest
    public void prepare() {// westlake db
        nobody = new User("nobody", "qwer");
        test = new User("nobody", "qwer");
        //new User("dlarkins", "qwer", null, "fs000080000j73ofl500000000");
        BasicCommons.logIn(nobody, true);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    private class Data {
        private User user;
        private int num;

        private Data(User u, int i) {
            user = u;
            num = i;
        }

        public String toString() {
            return user + ", #" + num;
        }
    }

    @DataProvider(parallel = true)
    @Test(dependsOnMethods = "prepare", alwaysRun = false)
    public Object[][] data() {
        Object[][] res = new Object[COUNT][0];
        for (int i = 0; i < COUNT; i++) {
            Data dt2 = new Data((User) (PSTestData.getRandom().nextBoolean() ? nobody : test).clone(), i);
            res[i] = new Object[]{dt2};
        }
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) {
        Thread.currentThread().setName(data.toString());
        int i = 0;
        while (!DeadlockParallelListener.hasFailure() && i < LOGIN_LOGOUT_NUM) {
            PSLogger.info(data + "; iter #" + i);
            BasicCommons.logIn(data.user);
            BasicCommons.validatePage();
            BasicCommons.logOut();
            BasicCommons.validatePage();
            i++;
        }
    }


}
