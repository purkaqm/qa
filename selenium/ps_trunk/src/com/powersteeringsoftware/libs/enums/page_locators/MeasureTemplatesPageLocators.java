package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.05.2010
 * Time: 13:41:22
 * To change this template use File | Settings | File Templates.
 */
public enum MeasureTemplatesPageLocators implements ILocatorable {
    URL("/admin/measures/MeasureTemplates.page"),
    MEASURE_TEMPLATES_PAGE_FRAME_LOCATOR("relative=up"),
    SUBMIT_BUTTON_ELEMENT("//div[@id='content']//input[@value='Submit']"),
    CONFIRM_DIALOG_LOCATOR("confirmDialog"),
    CONFIRM_DIALOG_YES_LOCATOR("//input[@value='Yes']"),
    CONFIRM_DIALOG_NO_LOCATOR("//input[@value='No']"),
    CANCEL_TOP("//div[contains(@class,'box')][1]//input[@value='Cancel']"),
    CANCEL_BOTTOM("//div[contains(@class,'box')][2]//input[@value='Cancel']"),
    TABLE_MEASURE_ADD_EDIT_LINK("//a[@title='" + LOCATOR_REPLACE_PATTERN + "' and contains(@href, 'AddEditMeasure.epage')]"),
    TABLE_MEASURE_INSTANCE_LINK("//a[@title='" + LOCATOR_REPLACE_PATTERN + "' and contains(@href, 'Instance.epage')]"),
    ADD_NEW_BUTTON("//input[@value='Add New']"),

    VALIDATION_ERROR_1("You must enter a value for Library Owner."),

    MASS_ATTACH("link=Mass Attach");

    String locator;

    MeasureTemplatesPageLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    public enum MeasureLockedFieldsEnum {
        NAME(0, "checkbox"),
        DESCRIPTION(1, "checkbox_0"),
        UNITS(2, "checkbox_1"),
        DATA_COLLECTION(3, "checkbox_2"),
        FORMULA(4, "checkbox_3"),
        DISPLAY_FORMAT(6, "checkbox_5"),
        EFFECTIVE_DATES(7, "checkbox_6"),
        TEST_SCHEDULE(8, "checkbox_7"),
        HISTORY_SCHEDULE(9, "checkbox_8"),
        REMINDER_SHEDULE(10, "checkbox_8"),
        START_DATE(11, "checkbox_9"),
        TARGET_DATE(12, "checkbox_10"),
        TARGET_VALUE(13, "checkbox_11"),
        GOAL_MESSAGES(14, "checkbox_12"),
        GOAL_THRESHOLDS(15, "checkbox_13"),
        VARIANCE_MESSAGES(16, "checkbox_14"),
        VARIANCE_THRESHOLDS(17, "checkbox_15");

        private int index;
        private String fieldId;

        MeasureLockedFieldsEnum(int indx, String id) {
            fieldId = id;
            index = indx;
        }

        public int getIndex() {
            return index;
        }

        public String getFieldId() {
            return fieldId;
        }
    }
}
