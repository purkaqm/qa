package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.06.2010
 * Time: 15:59:56
 */
public enum ComboBoxLocators implements ILocatorable {
    CLASS_ATTR("class"),
    CLASS_ERROR_VALUE("dijitError"),
    CLASS_DISABLE_VALUE("dijitComboBoxDisabled"),
    ARROW("//div[@class='dijitArrowButtonInner']"),
    ITEM(".//ul/li", ".//div[contains(@class, 'dijitMenuItem')]"),
    EMPTY_ITEM("", new String(new char[]{160})),
    INPUT("//input[@type='text']"),
    VALUE_INPUT("//input[@type='hidden']"),;

    String locator;
    String alternativeLocator;

    ComboBoxLocators(String s) {
        locator = s;
    }

    ComboBoxLocators(String loc82, String loc90) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) ? loc90 : loc82;
        alternativeLocator = loc82;
    }

    public String getLocator() {
        return locator;
    }

    public String getAlternativeLocator() {
        return alternativeLocator;
    }

}
