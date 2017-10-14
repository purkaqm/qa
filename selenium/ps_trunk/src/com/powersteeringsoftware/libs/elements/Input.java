package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

import java.io.File;

import static com.powersteeringsoftware.libs.enums.elements_locators.InputLocators.PARENT_DIV_CLASS_ERROR_VALUE;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 21:41:17
 * To change this template use File | Settings | File Templates.
 */
public class Input extends Element {
    private String regExp;
    private String max;
    private String min;
    private Element div;
    private static final long TIMEOUT_FOR_CLASS = 3000;

    public Input(ILocatorable locator) {
        super(locator);
    }

    public Input(String locator, Frame fr) {
        super(locator, fr);
    }

    public Input(String locator) {
        super(locator);
    }

    public Input(Element e) {
        super(e);
    }

    public void type(String text) {
        super.type(text);
    }

    public void clear() {
        if (getValue().isEmpty()) return;
        PSLogger.debug("Clear input " + this);
        super.type("");
    }

    public String getValue() {
        return getDriver().getValue(locator);
    }

    public void attachFile(String file) {
        PSLogger.debug("Attach file " + file);
        File f = new File(file);
        if (!f.exists()) {
            PSLogger.warn("Can't find file " + f.getAbsolutePath());
        }
        getDriver().attachFile(locator, f.getAbsolutePath());
    }

    private static boolean useMouseForClickOut = CoreProperties.getBrowser().isIE();
    /**
     * @return true if there is error input
     */
    public boolean isWrongInput() {
        if (div == null) {
            div = getParent().getParent().getParent();
            div.setSimpleLocator();
        }
        if (useMouseForClickOut) {
            mouseDownAndUp();
            div.getParent().mouseDownAndUp(); // <-- use mouse down and up instead focus because of web-driver
            //div.getParent().focus();
        } else {
            try {
                div.getParent().click(false);
            } catch (SeleniumException se) {
                PSLogger.warn(se); // ignore for web-driver (chrome, v21+, 23.0.1240)
                useMouseForClickOut = true;
                mouseDownAndUp();
                div.getParent().mouseDownAndUp();
            }
        }
        try {
            div.waitForClass(PARENT_DIV_CLASS_ERROR_VALUE, TIMEOUT_FOR_CLASS);
            return true;
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.debug("Class is " + div.getElementClass());
            return false;
        }
    }

    public void setParentDiv(Element div) {
        this.div = div;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(ILocatorable regExp) {
        this.regExp = regExp.getLocator();
    }

    public String getMax() {
        return max;
    }

    public void setMax(ILocatorable max) {
        this.max = max.getLocator();
    }

    public String getMin() {
        return min;
    }

    public void setMin(ILocatorable min) {
        this.min = min.getLocator();
    }
}
