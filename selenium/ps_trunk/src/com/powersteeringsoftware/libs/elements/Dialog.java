package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.thoughtworks.selenium.Wait;

import static com.powersteeringsoftware.libs.enums.elements_locators.DialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 18:04:27
 */
public class Dialog extends Element implements IDialog {
    protected String popupLocator;
    protected Element popup;
    private static final int TIMEOUT = 30000;

    public Dialog(ILocatorable locator) {
        super(locator);
    }

    protected Dialog() {
        super();
    }

    public Dialog(String locator) {
        super(locator);
    }

    public Dialog(Element e) {
        super(e);
    }

    public void setPopup(ILocatorable loc) {
        setPopup(loc.getLocator());
    }

    public void setPopup(String loc) {
        popup = new Element(popupLocator = loc);
    }

    public Element getPopup() {
        return popup;
    }

    public void open() {
        click(false);
        popup.waitForVisible();
    }

    public void close() {
        close(true);
    }

    private void _close(boolean wait) {
        Element close = getCloseElement();
        close.waitForVisible();
        close.mouseDownAndUp(); //its for web-driver
        close.click(false);
        if (wait && popup != null) {
            popup.waitForUnvisible(TIMEOUT);
        }
    }

    public void close(boolean wait) {
        try {
            _close(wait); // for stabilization. chrome-web-driver
        } catch (Wait.WaitTimedOutException ww) {
            PSLogger.warn(ww);
            _close(wait);
        }
    }

    protected Element getCloseElement() {
        return popup == null ? new Element(CLOSE) : popup.getChildByXpath(CLOSE);
    }

    public boolean isPresent() {
        return popup != null && popup.exists() && popup.isVisible();
    }

    public String getTitle() {
        return getTitleElement().getText();
    }

    protected Element getTitleElement() {
        return popup.getChildByXpath(TITLE);
    }

    protected Element getBodyElement() {
        return popup.getChildByXpath(BODY);
    }

    public String getBody() {
        Element e = getBodyElement().getChildByXpath(NEXT);
        if (e.exists()) return e.getText();
        return getBodyElement().getText();
    }

}
