package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.04.11
 * Time: 13:36
 */
public enum PeopleManageTimePageLocators implements ILocatorable {
    EXPAND_LINK("//a[@href='javascript:toggleLine(" + LOCATOR_REPLACE_PATTERN + ");']"),
    TBODY("//tbody[@id='BRANCHCODE_" + LOCATOR_REPLACE_PATTERN + "']"),
    TBODY_HIDE_CLASS("hide"),
    TBODY_SHOW_CLASS("show"),
    TR(TBODY.locator + "/tr[" + LOCATOR_REPLACE_PATTERN_2 + "]"),
    TR_CHECKBOX(TR.locator + "//input[@type='checkbox']"),
    TR_LINK(TR.locator + "//a"),
    TR_TD_HOURS_TOTAL(TR.locator + "//td[12]"),
    TITLE_TOTAL_VALUE(TBODY.locator + "/parent::table//tr[2]//th[" + LOCATOR_REPLACE_PATTERN_2 + "]"),

    ACTION_COMBOBOX("//div[@id='widget_selectActions']"),
    ACTION_COMBOBOX_REJECT("Reject"),
    ACTION_COMBOBOX_APPROVE("Approve"),
    ACTION_SELECT("selectActions"),

    ACTION_APPLY("//input[@value='Apply']"),
    INPUT_START_DATE_PICKER("//div[@id='widget_startDatePicker']"),
    TIMESHEET_LINE_NUMBER_GETEVAL("window.dojo.query('#BRANCHCODE_" + LOCATOR_REPLACE_PATTERN + " tr').length;"),
    WINDOW_ID("RejectDialog"),
    TYPE_TEXTAREA_EXPLANATION("rejectText"),
    BUTTON_REJECT_RUN_SCRIPT("dojo.byId('rejectBtn').click();"),
    BUTTON_REJECT("//input[@class='btn' and @value='Reject']"),
    BUTTON_CANCEL_ID("RejectDialogHide"),

    APPROVED_TAB("link=Approved"),

    ACTION_COMBOBOX_UNAPPROVE("Unapprove"),;
    private String locator;

    PeopleManageTimePageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(int i1, int i2) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i1)).replace(LOCATOR_REPLACE_PATTERN_2, String.valueOf(i2));
    }

    public String replace(int i1) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i1));
    }

    public enum Columns {
        SUNDAY(4),
        MONDAY(5),
        TUESDAY(6),
        WEDNESDAY(7),
        THURSDAY(8),
        FRIDAY(9),
        SATURDAY(10),
        OVERALL(11);

        private int column;

        Columns(int column) {
            this.column = column;
        }

        public int getValue() {
            return column;
        }

    }

}
