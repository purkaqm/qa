package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 17:13:33
 */
public enum NonWorkDaysSelectorLocators implements ILocatorable {
    ROW("//tr[" + LOCATOR_REPLACE_PATTERN + "]"),
    INPUT_CELL("/td[1]"),
    DATE_PICKER_CELL("/td[2]"),
    DATE_PICKER_CELL_2("/td[3]"),
    FIRST_ROW(ROW.getLocator().replace(LOCATOR_REPLACE_PATTERN, "2")),
    DATE_PICKER(DATE_PICKER_CELL.getLocator() + "/div"),
    DATE_PICKER_2(DATE_PICKER_CELL_2.getLocator() + "/div"),
    INPUT_NAME(INPUT_CELL.getLocator() + "/input"),
    ADD("//img[@alt='add']"),
    DELETE("//img[@alt='delete']"),
    WARNING_DIALOG("//div[@class='dijitDialog psDialog']"),
    WARNING_DIALOG_OK("//input[@type='button']"),
    WARNING_DIALOG_MESSAGE("//div[@class='shaded']"),;
    String locator;

    NonWorkDaysSelectorLocators(String s) {
        locator = s;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String str) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, str);
    }

    public String replace(int i) {
        return replace(String.valueOf(i));
    }
}
