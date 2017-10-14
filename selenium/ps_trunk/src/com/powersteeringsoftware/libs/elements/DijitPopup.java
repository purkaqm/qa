package com.powersteeringsoftware.libs.elements;


import com.powersteeringsoftware.libs.elements.waiters.ElementBecomeVisibleWaiter;
import com.powersteeringsoftware.libs.elements.waiters.LastVisibleElementWaiter;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.DijitPopupLocators.*;

/**
 * hotfix class for ie
 * <p/>
 * User: szuev
 * Date: 04.10.11
 * Time: 17:25
 */
public class DijitPopup extends Element {

    private boolean parseHtml;

    public DijitPopup() {
        super(THIS);
        node = NODE.getLocator();
        parseHtml = getDriver().getType().isIE();
    }

    private DijitPopup(Element parent) {
        super(parent);
        node = NODE.getLocator();
        parseHtml = getDriver().getType().isIE();
    }

    public void waitForVisible(long timeout) {
        PSLogger.debug("Wait (" + timeout + "ms) while dijit popup become visible");
        if (!parseHtml) {
            new ElementBecomeVisibleWaiter(locator).defaultWait(timeout);
        } else {
            new LastVisibleElementWaiter(this).wait("Can't find visible element " + this + " after " + timeout + " ms", timeout);
        }
    }

/*
    public void setDefaultElement() {
        if (!parseHtml || !isDEPresent()) {
            super.setDefaultElement();
        }
        // DE is set by LastVisibleElementWaiter, do not nothing here
    }
*/

    public void waitForUnvisible(long timeout) {
        super.waitForUnvisible(timeout);
        setDynamic();
    }

    public static DijitPopup getParent(Element e) {
        return new DijitPopup(e.getParent(NODE.getLocator(), CLASS.getLocator()));
    }

    public void setDynamic() {
        locator = THIS.getLocator();
        defaultElement = null;
    }
}
