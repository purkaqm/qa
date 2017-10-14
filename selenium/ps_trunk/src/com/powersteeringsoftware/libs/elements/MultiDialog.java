package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.DialogLocators.NEXT;
import static com.powersteeringsoftware.libs.enums.elements_locators.DialogLocators.POPUP;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 15:41:23
 */
public class MultiDialog extends Dialog {
    private int index;

    public MultiDialog(ILocatorable locator) {
        super(locator);
    }

    public MultiDialog(String locator) {
        super(locator);
    }

    public MultiDialog(Element e) {
        super(e);
    }

    private void setPopup(int i) {
        setPopup(POPUP.replace(i));
    }

    public Element getPopup() {
        if (index == 0) index = 1;
        setPopup(index);
        return popup;
    }

    public void open() {
        click(false);
        setPopup(++index);
        popup.waitForVisible();
        popup.setDefaultElement();
    }

    private Element getNextElement() {
        return getBodyElement().getChildByXpath(NEXT);
    }

    public MultiDialog getNext() {
        PSLogger.info("Open next dialog popup");
        getTitleElement().mouseDownAndUp(); // its for web-driver
        getNextElement().click(false);
        MultiDialog next = new MultiDialog(getBodyElement());
        next.setPopup(++index);
        next.popup.waitForPresent();
        next.popup.setDefaultElement();
        return next;
    }
}
