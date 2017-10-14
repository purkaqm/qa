package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.08.2010
 * Time: 12:01:00
 */
public class ElementBecomeValueWaiter extends AbstractElementWaiter {

    public ElementBecomeValueWaiter(String locator) {
        super(locator, "Element hasn't any value after timeout. Element locator " + locator);
    }

    public boolean until() {
        try {
            return !SeleniumDriverFactory.getDriver().superGetValue(locator).isEmpty();
        } catch (SeleniumException se) {
            return false;
        }
    }
}
