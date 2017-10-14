package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 28.04.12
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public enum MetricInstancePageLocators implements ILocatorable {
    URL("/metrics/Instance.epage"),
    URL_ID(URL.locator + "?sp=U" + LOCATOR_REPLACE_PATTERN),
    EDIT_PROPERTIES_LINK("id=editComponentShow", "//a[contains(text(), 'Edit Properties')]"),
    EDIT_PROPERTIES_DIALOG("editComponent"),
    EDIT_PROPERTIES_DIALOG_TAB("//span[@class='tabLabel' and contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),
    EDIT_PROPERTIES_DIALOG_TAB_TAGS_BREAKDOWN("Tags Breakdown", "Beneficiaries"),
    EDIT_PROPERTIES_DIALOG_TAB_TAGS_BREAKDOWN_SELECTOR("//div[contains(@class, 'dijitContentPane')]//div[@class='Selector']"),
    EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES("Properties"),
    EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES_LOCK("lockState"),
    EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES_READY_FOR_ROLLUP("Checkbox_2"),
    EDIT_PROPERTIES_DIALOG_UPDATE("//input[@value='Update']"),

    VIEW_ACTIVE("//li[@class='active']"),
    VIEW_INACTIVE("//li[@class='inactive']/a[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),

    GRID("//div[@id='AJAXMETRIC_METRIC_GRID']"),
    GRID_ROW_ID("rowid"),
    GRID_ROWS("//tr[@" + GRID_ROW_ID.locator + "]"),
    GRID_ROW("//tr[@" + GRID_ROW_ID.locator + "='" + LOCATOR_REPLACE_PATTERN + "']"),
    GRID_CELL_ID("cellid"),
    GRID_CELLS("//td[@" + GRID_CELL_ID.locator + "]"),
    GRID_HEADER("//div[@id='colHeader']"),
    GRID_HEADER_MONTH("//tr/th/nobr"),
    GRID_NAME_COLUMN("//div[@id='rowHeader']"),
    GRID_MAIN("//div[@id='mainArea']"),
    GRID_ROW_EXPAND("//img"),
    GRID_TXT("//nobr"),
    GRID_ACRONYM("//acronym"),
    GRID_ACRONYM_TITLE("title"),
    GRID_ROW_EXPAND_SRC("tree-expand.gif"),
    GRID_ROW_COLLAPSE_SRC("tree-collapse.gif"),
    GRID_CELL_NOT_EDITABLE("boldCell"),
    GRID_CELL_EDITABLE("editableCell"),
    GRID_CELL_MODIFIED("modifiedCell"),

    GRID_WAITER("css=div.pleaseWaitDiv"),
    GRID_WAITER_ERROR("Error occured while saving. Please reset and try again"),
    GRID_INPUT("//input[contains(@id,'ps_form_ValidationTextBox_')]"),

    GRID_SAVE("AJAX_SAVE_BTN"),
    GRID_RESET("RESET_BTN"),

    BENEFICIARY_GRID("//table[@id='beneficiariesTable']"),
    BENEFICIARY_GRID_ROW("//tr[contains(@id, 'row')]"),
    BENEFICIARY_GRID_ROW_NEW("row_new"),
    BENEFICIARY_GRID_ROW_SELECTOR("//td[contains(@class, 'selector')]"),
    BENEFICIARY_GRID_ROW_NUMBER_BOX("//td[contains(@class, 'numberBox')]"),
    BENEFICIARY_GRID_INPUT("//input[@id='beneficiaryPercent']"),
    BENEFICIARY_GRID_SUM("//th[@id='sum']"),

    EDIT_COMMENTS_DIALOG("editComments"),
    EDIT_COMMENTS_TEXTAREA("editComments_underlay"),
    EDIT_COMMENTS_OK("editCommentsHide"),
    EDIT_COMMENTS_CANCEL("editCommentsHideCancel"),
    EDIT_COMMENTS_LINK("//a[@id='editCommentsShow']"),
    EDIT_COMMENTS_TXT("commentsDisplay"),
    EDIT_COMMENTS_USER_LINK("//div[@class='link']"),

    INFO("link=info");

    String locator;

    MetricInstancePageLocators(String s) {
        locator = s;
    }

    MetricInstancePageLocators(String s, String s1) {
        this(TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ? s1 : s);
    }


    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
