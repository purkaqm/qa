package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

public class ElementBecomeVisibleWaiter extends AbstractElementWaiter {

    public static final String ERROR_MESSAGE_VISIBLE = "Element hasn't become visible. Element locator = ";

    public ElementBecomeVisibleWaiter(String _locator) {
        super(_locator, ERROR_MESSAGE_VISIBLE + _locator);
    }

    public boolean until() {
        try {
            return //SeleniumDriverFactory.getDriver().isElementPresent(getLocator()) &&
                    SeleniumDriverFactory.getDriver().isVisible(getLocator());
        } catch (SeleniumException se) {
            return false;
        }
    }

    public static void waitElementBecomeVisible(String _locator) {
        waitElementBecomeVisible(_locator, CoreProperties.getWaitForElementToLoad());
    }


    public static void waitElementBecomeVisible(String _locator, long timeout) {
        PSLogger.debug("Wait " + timeout + " ms while " + _locator + " become visible");
        Wait presentWaiter = new ElementBecomeVisibleWaiter(_locator);
        presentWaiter.wait(presentWaiter.toString(), timeout);
    }

}
