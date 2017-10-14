package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 03.10.11
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public enum MetricsPageLocators implements ILocatorable {
    URL("/project/Metrics"),

    ATTACH("id=templateSelectDialogShow"),
    ATTACH_DIALOG("//div[@id='templateSelectDialog']"),
    ATTACH_DIALOG_SELECT(ATTACH_DIALOG.loc + "//select"),
    ATTACH_DIALOG_SUBMIT("//input[@value='Attach']"),

    TABLE_ROW_DELETE("//td[contains(@class, 'deleteMetricColumnValue')]//input"),

    DELETE("//input[@value='Delete' and @type='button' and @class='btn-white']"),
    DELETE_DIALOG("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    DELETE_ID_SUFFIX("Show"),
    DELETE_OK("//input[@value='Ok']"),
    INSTANCE_LINK("link=" + LOCATOR_REPLACE_PATTERN);
    String loc;

    MetricsPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }

    public String replace(Object s) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(s));
    }
}
