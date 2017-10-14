package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.ToggleButtonLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 13:40:52
 */
public class ToggleButton extends Button {
    private Element parent;

    public ToggleButton(ILocatorable locator) {
        super(locator);
    }

    public ToggleButton(Element e) {
        super(e);
    }

    public ToggleButton(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void click() {
        try {
            super.click(false);
        } catch (SeleniumException e) { // for ie
            PSLogger.warn(e.getMessage());
        }
    }

    public boolean isChecked() {
        PSLogger.debug("isChecked for " + locator);
        if (parent == null) {
            parent = getChildByXpath(ToggleButtonLocators.PARENT);
        }
        String attr = parent.getAttribute(ToggleButtonLocators.PARENT_CLASS);
        PSLogger.debug("toggle class: " + attr);
        return attr.contains(ToggleButtonLocators.PARENT_VALUE.getLocator());
    }


}
