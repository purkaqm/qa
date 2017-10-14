package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 01.06.2010
 * Time: 13:48:39
 */
public enum DatePickerLocators implements ILocatorable {
    CLASS_ATTR("class"),
    CLASS_ERROR_VALUE("dijitTextBoxError"),
    INPUT("//input[not(@type='hidden')]"), // for ie
    HIDDEN_INPUT("//input[@type='hidden']"),
    POPUP("//div[@class='dijitPopup' and contains(@dijitpopupparent, '" + LOCATOR_REPLACE_PATTERN + "')]"),
    POPUP_WIDGET_ID("widgetid"),
    POPUP_DATE_PICKER_TABLE("/table[@class='dijitCalendarContainer']"),

    POPUP_OPEN_ICON("//div[@class='dateIconDiv']"),

    POPUP_PREVIOUS_YEAR("//span[@dojoattachpoint='previousYearLabelNode']"),
    POPUP_CURREN_YEAR("//span[@dojoattachpoint='currentYearLabelNode']"),
    POPUP_NEXT_YEAR("//span[@dojoattachpoint='nextYearLabelNode']"),

    POPUP_TYPE_2_MONTH("//table[@class='calendarBodyContainer']//td[contains(@id, 'M" + LOCATOR_REPLACE_PATTERN + "')]"),
    POPUP_TYPE_1_SELECTED_MONTH("//div[@dojoattachpoint='monthLabelNode']"),
    POPUP_TYPE_1_MONTH_DROP_DOWN("//div[@dojoattachpoint='monthDropDown']"),
    POPUP_TYPE_1_MONTH("//div[@dojoattachpoint='monthDropDown']//div[@month='" + LOCATOR_REPLACE_PATTERN + "']"),

    POPUP_TYPE_1_INCREMENT_MONTH("//th[@dojoattachpoint='incrementMonth']"),
    POPUP_TYPE_1_DECREMENT_MONTH("//th[@dojoattachpoint='decrementMonth']"),

    POPUP_TYPE_1_DAY("//td[contains(@class, 'dijitCalendarCurrentMonth')]/span[text()='" + LOCATOR_REPLACE_PATTERN + "']"),;

    String locator;

    DatePickerLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    public String replace(int rep) {
        return replace(String.valueOf(rep));
    }

}
