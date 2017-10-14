package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.10.11
 * Time: 12:42
 */
public class LastVisibleElementWaiter extends Wait {
    private static final int FIRST_INTERVAL = 1000; // ms
    private static final int FIRST_MAX_ITERATIONS_NUM = 10;
    private static final int FIRST_TIMEOUT = FIRST_INTERVAL * FIRST_MAX_ITERATIONS_NUM;
    private static final int SECOND_MAX_ITERATIONS_NUM = 5;
    private Element element;

    public LastVisibleElementWaiter(Element e) {
        element = e;
        element.setDynamic();
    }

    @Override
    public boolean until() {
        // get document hear, do not select frame :
        List<Element> list = Element.searchElementsByXpath(element.getDriver().getDocument(null), element.getLocator());
        PSLogger.debug2("DijitPopups: " + list.size());
        Element last = null;
        for (int i = list.size(); i > 0; i--) { // hotfix for comboboxes under ie on WBS 9.2 :
            if (!list.get(i - 1).getDEStyle().toLowerCase().contains("visibility: hidden")) {
                last = list.get(i - 1);
                break;
            }
        }
        try {
            if (last != null && last.isVisible()) {
                // reinit this popup.
                element.initFromOther(last);
                return true;
            }
        } catch (SeleniumException se) {
            // ignore.
        }
        return false;
    }


    public void wait(String message, long timeoutInMilliseconds, long intervalInMilliseconds) {
        try {
            Thread.sleep(intervalInMilliseconds); // sleep before
        } catch (InterruptedException e) {
            // ignore.
        }
        if (intervalInMilliseconds > timeoutInMilliseconds) {
            if (!until()) {
                throw new WaitTimedOutException(message);
            } else {
                return;
            }
        }
        super.wait(message, timeoutInMilliseconds - intervalInMilliseconds, intervalInMilliseconds);
    }

    public void wait(String message, long timeout) {
        if (timeout < FIRST_TIMEOUT) {
            this.wait(message, timeout, FIRST_INTERVAL);
            return;
        }
        try {
            this.wait(null, FIRST_TIMEOUT, FIRST_INTERVAL);
        } catch (WaitTimedOutException w) {
            long step = Math.max(FIRST_INTERVAL, (timeout - FIRST_TIMEOUT) / SECOND_MAX_ITERATIONS_NUM);
            this.wait(message, timeout - FIRST_TIMEOUT, step);
        }
    }

}
