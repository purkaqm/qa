package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

/**
 * Waiter for element disappear.
 * <p/>
 * ("not present on page" or "present, but is invisible")
 */
public class ElementBecomeDisappearedWaiter extends AbstractElementWaiter {

    public static final String ERROR_MESSAGE_DISSAPEARED = "Element hasn't dissapeared. Element locator = ";

    public ElementBecomeDisappearedWaiter(String _locator) {
        super(_locator, ERROR_MESSAGE_DISSAPEARED + _locator);
    }

    public boolean until() {
        if (SeleniumDriverFactory.getDriver().isElementPresent(getLocator())) {
            try {
                return !SeleniumDriverFactory.getDriver().isVisible(getLocator());
            } catch (SeleniumException se) {
                // Element not found ==> Disappear;
                // Will return 'true' as default value
            }
        }
        return true;
    }

    public static void waitElementDissapeared(String _locator) {
        Wait presentWaiter = new ElementBecomeDisappearedWaiter(_locator);
        presentWaiter.wait(presentWaiter.toString(), CoreProperties.getWaitForElementToLoad());
    }
}
