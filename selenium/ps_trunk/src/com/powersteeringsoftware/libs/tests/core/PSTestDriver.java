package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.annotations.BeforeTest;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 16.08.12
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
public abstract class PSTestDriver {
    public static boolean doLogIn = true;

    @BeforeTest(timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void doLogin() {
        if (!doLogIn) return;
        PSLogger.info("Do log-in");
        BasicCommons.logIn();
    }

    public abstract PSTestData getTestData();
}
