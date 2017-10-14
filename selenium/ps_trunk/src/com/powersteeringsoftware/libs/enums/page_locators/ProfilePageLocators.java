package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.05.2010
 * Time: 13:30:08
 * To change this template use File | Settings | File Templates.
 */
public enum ProfilePageLocators implements ILocatorable {
    URL("person/settings/Profile.epage"),
    USER_SETTINGS_LINK(BasicCommonsLocators.USER_NAME_LINK_100.getLocator()),
    EDIT_PROFILE_LINK("//a/div[text()='Edit Profile']"),
    LOGIN_INPUT("userName"),
    PASSWORD_INPUT("password"),
    CONFIRM_PASSWORD_INPUT("confirmPassword"),
    EXPIRATION_DATE_PICKER("id=widget_PSDatePicker"),
    ACCESS_CHECKBOX("NoAccessCheckBox"),
    FIRST_NAME_INPUT("firstName"),
    LAST_NAME_INPUT("lastName"),
    SUBMIT("Submit_0", "id=save1"),
    CANCEL("//input[@value='Cancel' and @name='Submit']"),
    DAYS_OFF_SELECTOR("//div[@widgetid='nonWorkDaysSelector']"),
    CALENDAR_CHOOSER("calendarChooser"),
    CALENDAR_CHOOSER_LABEL("label=" + LOCATOR_REPLACE_PATTERN),;

    String locator;

    ProfilePageLocators(String loc) {
        locator = loc;
    }

    ProfilePageLocators(String loc90, String loc91) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1) ? loc91 : loc90;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }
}
