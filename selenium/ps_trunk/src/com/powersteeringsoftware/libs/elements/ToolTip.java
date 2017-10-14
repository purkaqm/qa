package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.ToolTipLocators;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 31.05.2010
 * Time: 13:44:12
 */
public class ToolTip extends Element {
    private Element div;

    public ToolTip(ILocatorable locator) {
        this(locator.getLocator());
    }

    public ToolTip(String locator) {
        super(locator);
        div = new Element(ToolTipLocators.DIV.getLocator());
    }

    public void open() {
        BrowserTypes br = getDriver().getType();
        if (br.isIE() && !(br.isWebDriverIE(6) || br.isWebDriverIE(7))) {
            mouseMove();
        } else {
            mouseOver();
        }
        div.waitForVisible();
    }

    public void close() {
        if (CoreProperties.getBrowser().isIE()) {
            return;
        } else {
            mouseOut();
        }
        div.waitForDisapeared();
    }

    public String getLabel() {
        return div.getText();
    }
}
