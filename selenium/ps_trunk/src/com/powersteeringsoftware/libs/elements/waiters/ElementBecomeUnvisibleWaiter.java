package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

public class ElementBecomeUnvisibleWaiter extends AbstractElementWaiter {
    public static final String ERROR_MESSAGE_BECOME_UNVISIBLE = "Element hasn't set unvisible. Element locator = ";

    public ElementBecomeUnvisibleWaiter(String _locator) {
        super(_locator, ERROR_MESSAGE_BECOME_UNVISIBLE + _locator);
    }

    public boolean until() {
        try {
            return !SeleniumDriverFactory.getDriver().isVisible(getLocator());
        } catch (SeleniumException e) {
            PSLogger.warn(getClass().getSimpleName() + ":" + e.getMessage());
            return true;
        }
    }

    public static void waitElementBecomeUnvisible(String _locator) {
        waitElementBecomeUnvisible(_locator, CoreProperties.getWaitForElementToLoad());
    }

    public static void waitElementBecomeUnvisible(String _locator, long timeout) {
        PSLogger.debug("Wait while element " + _locator + " will be unvisible");
        Wait presentWaiter = new ElementBecomeUnvisibleWaiter(_locator);
        presentWaiter.wait(presentWaiter.toString(), timeout);
    }

    public static void waitWithoutThrow(String locator, long timeout) {
        try {
            waitElementBecomeUnvisible(locator, timeout);
        } catch (WaitTimedOutException e) {
            PSLogger.warn(e.getMessage()); //hotfix for ie
        } catch (SeleniumException e) {
            PSLogger.warn(e.getMessage()); //hotfix for ie
        }
    }

}
