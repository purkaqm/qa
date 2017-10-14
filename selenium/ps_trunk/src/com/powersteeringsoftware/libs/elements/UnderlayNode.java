package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.elements.waiters.ScriptWaiter;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import static com.powersteeringsoftware.libs.enums.elements_locators.UnderlayNodeLocators.LOADING_WIDGET_SCRIPT;
import static com.powersteeringsoftware.libs.enums.elements_locators.UnderlayNodeLocators.WAITING_UNDERLAY_NODE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.06.2010
 * Time: 16:35:44
 */
public class UnderlayNode extends Element {
    public UnderlayNode(ILocatorable locator) {
        super(locator);
    }

    public UnderlayNode(String locator) {
        super(locator);
    }

    public UnderlayNode() {
        super(WAITING_UNDERLAY_NODE);
    }

    public void waiting(boolean flag) {
        Element e = getChildByXpath(WAITING_UNDERLAY_NODE);
        if (flag) {
            if (CoreProperties.getBrowser().isIE())
                e.waitForPresent();
            else
                e.waitForVisible();
        } else {
            if (CoreProperties.getBrowser().isIE())
                e.waitForDisapeared();
            else
                e.waitForUnvisible();
        }
    }

    public void waitWhileVisible() {
        doWait(CoreProperties.getWaitForElementToLoad(), false);
    }

    public void waitForVisible() {
        doWait(CoreProperties.getWaitForElementToLoad(), true);
    }

    public void doWait(long timeout, boolean visible) {
        new ScriptWaiter(LOADING_WIDGET_SCRIPT.replace("result_" + System.currentTimeMillis()), visible).defaultWait(timeout);
    }

}
