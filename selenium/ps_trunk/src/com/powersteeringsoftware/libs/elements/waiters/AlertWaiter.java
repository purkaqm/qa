package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.Wait;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 08.12.2010
 * Time: 15:22:13
 */
public class AlertWaiter extends AbstractElementWaiter {
    private static final String ERROR_MESSAGE =
            "Alert does not appeared";
    private static String alert;

    AlertWaiter() {
        super(null, ERROR_MESSAGE);
    }

    public static String getAlert() {
        return alert;
    }

    public boolean until() {
        return SeleniumDriverFactory.getDriver().isAlertPresent();
    }

    public static void waitForAlert() {
        waitForAlert(CoreProperties.getWaitForElementToLoad());
    }

    public static void waitForAlert(long timeout) {
        Wait alertWaiter = new AlertWaiter();
        alertWaiter.wait(alertWaiter.toString(), timeout);
        PSLogger.info("Alert : " + (alert = SeleniumDriverFactory.getDriver().getAlert()));
    }


}
