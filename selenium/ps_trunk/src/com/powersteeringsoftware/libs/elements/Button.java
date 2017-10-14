package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 21:42:03
 * To change this template use File | Settings | File Templates.
 */
public class Button extends Element {
    private static final long DEBUG_TIMEOUT = 30000;
    private long lastClickTime;
    private PSPage page;

    public Button(ILocatorable locator) {
        this(locator, null);
    }

    public Button(ILocatorable locator, PSPage p) {
        super(locator);
        setResultPage(p);
    }

    public Button(Element e) {
        this(e, null);
    }

    public Button(Element e, PSPage p) {
        super(e);
        setResultPage(p);
    }

    public Button(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public Button(String loc) {
        super(loc);
    }

    public void submit() {
        long start = System.currentTimeMillis();
        click();
        lastClickTime = System.currentTimeMillis() - start;
    }

    public void click(boolean wait) {
        long start = System.currentTimeMillis();
        if (wait && page != null) {
            super.click(false);
            page.initJsErrorChecker();
            page.waitForPageToLoad();
            page.testUrl();
        } else {
            super.click(wait);
        }
        lastClickTime = System.currentTimeMillis() - start;
    }

    public void clickAndWaitForDialog(ILocatorable dialog) {
        Element popup = new Element(dialog);
        waitForVisible();
        focus(); // its debug for ie7
        try {
            // debug for ie7:
            click(false);
            popup.waitForVisible(DEBUG_TIMEOUT);
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.warn(w);
            click(false);
            popup.waitForVisible();
        }
    }

    public void setResultPage(PSPage p) {
        page = p;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }
}
