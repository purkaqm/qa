package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getVersion;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.06.2010
 * Time: 17:34:37
 */
public class TextArea extends Element {
    public TextArea(ILocatorable locator) {
        super(locator);
    }

    public TextArea(Element e) {
        super(e);
    }


    public TextArea(String locator) {
        super(locator);
    }

    public void setText(String text) {
        PSLogger.info("Set text '" + text + "' to textarea " + locator);
        if (getVersion().isTextAreaBroken(getDriver()))
            clear(); // hotfix for ff5.0 web-driver (selenium version 2.0rc3)
        getDriver().type(locator, text);
    }

    public void clear() {
        if (getValue().isEmpty()) return;
        PSLogger.debug("Clear textarea " + this);
        if (getVersion().isTextAreaBroken(getDriver())) {
            setValue("");
        } else {
            getDriver().type(locator, "");
        }
    }

    public String getText() {
        PSLogger.debug("get text from textarea " + initialLocator);
        return getValue();
    }

}
