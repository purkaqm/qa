package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;

import static com.powersteeringsoftware.libs.enums.elements_locators.TabsDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 30.09.2010
 * Time: 16:32:10
 */
public abstract class TabsDialog extends Dialog {
    public TabsDialog(ILocatorable locator) {
        super(locator);
    }

    protected TabsDialog(String locator) {
        super(locator);
    }


    public TabsDialog(Element e) {
        super(e);
    }

    public TabsDialog() {
        super("");
    }

    protected void openTab(ILocatorable name) {
        Element tab = getPopup().getChildByXpath(getTabLocator(name));
        if (tab.getAttribute(TAB_PRESSED_ATTRIBUTE) != null && tab.getAttribute(TAB_PRESSED_ATTRIBUTE).
                equalsIgnoreCase(TAB_PRESSED_ATTRIBUTE_TRUE.getLocator())) {
            PSLogger.info("Tab '" + name.getLocator() + "' is current now");
            return;
        }
        PSLogger.info("Open tab '" + name.getLocator() + "'");
        try {
            tab.click(false);
        } catch (SeleniumException e) {
            if (!CoreProperties.getBrowser().isIE()) {
                throw e;
            }
            // ignore exception in case ie
        }
        tab.waitForAttribute(TAB_PRESSED_ATTRIBUTE, TAB_PRESSED_ATTRIBUTE_TRUE);
        getPopup().setDefaultElement();
        PSLogger.save("After opening tab '" + name.getLocator() + "'");
    }

}
