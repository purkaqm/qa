package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.07.2010
 * Time: 13:51:02
 */
public enum CustomFields82PageLocators implements ILocatorable {
    ADD_NEW_BUTTON("//input[@value='Add New']"),
    NAME_INPUT("addNewFieldDialogName"),
    DESCRIPTION_TEXT_AREA("addNewFieldDialogDsc"),
    DIALOG_TYPE_SELECT("selectFieldDataType"),
    DIALOG_TYPE_SELECT_BUTTONS_YES_NO_LABEL("label=Yes/No"),
    DIALOG_TYPE_SELECT_CHECKBOXES_LABEL("label=Checkboxes"),
    DAILOG_CHECKBOXES_DESCRIPTION("extFieldDsc"),
    DIALOG("addNewFieldDialog"),
    DIALOG_SUBMIT("Submit_0", "SaveBtn"),
    DIALOG_WORK_ACCOS_TAG_CHOOSER("//div[contains(@config,'TYPE_FILTER')]"),
    DIALOG_OTHER_ACCOS_TAG_CHOOSER("//div[contains(@config,'Other Assoc')]"),
    DELETE_DIALOG("//div[@class='dijitDialog psDialog']"),
    DELETE_DIALOG_YES("//input[@value='Yes']"),
    DELETE_IMG("//img[@alt='Remove Field']"),
    DELETE_IMG_ONCLICK_ATTR("onclick"),
    CUSTOM_FIELD_NAME("//div[@class='link' and contains(@onclick, '" + LOCATOR_REPLACE_PATTERN + "')]"),
    CUSTOM_FIELD_NAME_CELL("//td[@class='nameColumnValue']/div"),;
    private String locator;

    CustomFields82PageLocators(String s) {
        locator = s;
    }

    CustomFields82PageLocators(String s1, String s2) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ? s2 : s1;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }
}
