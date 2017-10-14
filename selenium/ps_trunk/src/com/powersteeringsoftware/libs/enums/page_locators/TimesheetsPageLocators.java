package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 18:33:10
 */
public enum TimesheetsPageLocators implements ILocatorable {
    URL("/person/Timesheets"),

    WORK_ITEM_DIV("shared_popup_id"),
    LOADING_PAGE("//div[@class='waitingUnderlay']"),
    DIALOG_CLEAR_LINK("id=callback_link_id"),
    WORK_ITEM_DIALOG_FAVORITES("Favorites"),
    WORK_ITEM_DIALOG_TAB_FAVORITES("dijit_layout__TabButton_0", "//div[@id='ps_ui_TabContainer_0']//span[text()='" +
            WORK_ITEM_DIALOG_FAVORITES.loc + "']"),
    WORK_ITEM_DIALOG_CONTAINER("//div[contains(@class, 'dijitTabPaneWrapper')]"),
    WORK_ITEM_DIALOG_LINK("link=" + LOCATOR_REPLACE_PATTERN,
            WORK_ITEM_DIALOG_CONTAINER.loc + "//div[text()='" + LOCATOR_REPLACE_PATTERN + "']",
            WORK_ITEM_DIALOG_CONTAINER.loc + "//span[contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]",
            WORK_ITEM_DIALOG_CONTAINER.loc + "//span[contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]",
            "//ul[@name='favouriteTab']//span[contains(@title,'" + LOCATOR_REPLACE_PATTERN + "')]"),
    WORK_ITEM_EDITABLE("popup_" + LOCATOR_REPLACE_PATTERN + "Show"),
    WORK_ITEM_SUBMITTED("//td[@id='work" + LOCATOR_REPLACE_PATTERN + "']/a"),
    SEARCH_WORK_TREE_100("//div[@id='searchTree']//div[@containerid='root']"),

    LOCATION_EDIT("locationLink" + LOCATOR_REPLACE_PATTERN),
    LOCATION_SUBMITTED("uid" + LOCATOR_REPLACE_PATTERN) {
        public String replace(String rep) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
                int i = Integer.valueOf(rep);
                if (i == 0) return "uid";
                return super.replace("_" + (i - 1));
            }
            return super.replace("_" + rep);
        }
    },

    // from AbstractTimesheetPageAdapter class:
    NEXT_WEEK("//img[@alt='Next Week']"),
    PREVIOUS_WEEK("//img[@alt='Previous Week']"),

    TIME_SHEET_TABLE("//div[@id='TimesheetsContainer']//table"),
    TIME_SHEER_TABLE_WORK_ITEM_ROW("//tr[@class='odd' or @class='even']"),
    TIME_SHEER_TABLE_LAST_ROW_CELL("//tr[@class='bottom']/th[" + LOCATOR_REPLACE_PATTERN + "]"),
    TIME_SHEET_TABLE_CELL_EDITABLE("//input[@id='value_" + LOCATOR_REPLACE_PATTERN + "_" + LOCATOR_REPLACE_PATTERN_2 + "']"),
    TIME_SHEET_TABLE_CELL_SUBMITTED("//span[@id='value_" + LOCATOR_REPLACE_PATTERN + "_" + LOCATOR_REPLACE_PATTERN_2 + "']"),
    TIME_SHEER_TABLE_ROW_TOTAL("//td[11]"),
    //#TimesheetsContainer table tr

    ACTIVITY_EDITABLE("//select[contains(@id, 'act" + LOCATOR_REPLACE_PATTERN + "_')]"),
    ACTIVITY_EDITABLE_LABEL("label=" + LOCATOR_REPLACE_PATTERN),
    ACTIVITY_SUBMITTED("//span[contains(@id, 'act" + LOCATOR_REPLACE_PATTERN + "_')]"),
    ACTIVITY_SUBMITTED_DIV("/div"),

    BILLING_CATEGORY("css=[id^=bil" + LOCATOR_REPLACE_PATTERN + "]"),
    BILLING_CATEGORY_LABEL("label=" + LOCATOR_REPLACE_PATTERN),

    INPUT_START_DATE_PICKER(null, "//div[@id='widget_PSDatePicker']", "//div[@id='widget_psTimesheetsDatePicker']"),

    BUTTON_GO("//div[@id='psTimesheetsToolbarTopLeft']/input"),

    BUTTON_ADD("//div[@class='right']/input[@value='Add']"),
    BUTTON_SAVE_SUBMIT("//input[@value='Save & Submit']"),
    BUTTON_SAVE("//input[@value='Save Changes']"),
    BUTTON_CANCEL("//input[@value='Cancel']"),
    LINK_COPY_LAST("link=Copy Last"),

    STATUS_SUBMITTED("Submitted"),
    STATUS_NOT_SUBMITTED("Not Submitted"),
    STATUS_APPROVED("Approved"),
    STATUS_REJECTED("Rejected"),
    STATUS_TOOLTIP("//img[@name='ToolTip']"),
    STATUS_TOOLTIP_ATTR("tooltip"),
    APPROVED_ROWS("//*[contains(@tooltip, 'Approved')]"),
    REJECTED_ROWS("//*[contains(@tooltip, 'Rejected')]"),;
    String loc;

    TimesheetsPageLocators(String l) {
        loc = l;
    }

    TimesheetsPageLocators(String loc7, String loc8) {
        this(loc7, loc8, null);
    }

    TimesheetsPageLocators(String loc7, String loc8, String loc94) {
        this(loc7, loc8, loc94, null);
    }

    TimesheetsPageLocators(String loc7, String loc8, String loc94, String loc100) {
        this(loc7, loc8, loc94, loc100, null);
    }

    TimesheetsPageLocators(String loc7, String loc8, String loc94, String loc100, String locV13) {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._13) && locV13 != null) {
            loc = locV13;
            return;
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._10_0) && loc100 != null) {
            loc = loc100;
            return;
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) && loc94 != null) {
            loc = loc94;
            return;
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_1)) {
            loc = loc8;
        } else {
            loc = loc7 != null ? loc7 : loc8;
        }
    }

    public String getLocator() {
        return loc;
    }

    public String toString() {
        return loc;
    }

    public String replace(String rep) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    public String replace(String rep, String rep2) {
        return replace(rep).replace(LOCATOR_REPLACE_PATTERN_2, rep2);
    }


    public String replace(int rep) {
        return replace(String.valueOf(rep));
    }

    public String replace(int rep1, int rep2) {
        return replace(String.valueOf(rep1), String.valueOf(rep2));
    }


}
