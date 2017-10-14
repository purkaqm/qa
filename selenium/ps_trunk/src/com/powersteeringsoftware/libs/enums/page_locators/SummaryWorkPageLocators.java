package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.05.2010
 * Time: 18:42:34
 * To change this template use File | Settings | File Templates.
 */
public enum SummaryWorkPageLocators implements ILocatorable {

    _URL("/project/Summary1"),
    URL(_URL.locator + ".epage"),
    PROJECT_PLANING("Project planning"),
    RESOURCE_PLANING("Resource planning"),


    PARENT_LINK("//table[@class='simple']//a[@title]"),
    PROJECT_CENTRAL_TOP_ICON_LOCATOR("popProjectEditor"),
    PROJECT_CENTRAL_MENU_ITEM(".//td[contains(@class, 'dijitMenuItemLabel')]/div/a"),
    PROJECT_CENTRAL_TOP_ICON_LOCATOR_RESOURCE_PLANNING_ON("//a[@class='projecteditorpop']"),
    HISTORY_TOP_ICON("//a[@title='History']"),
    ISSUES_TOP_ICON("//a[@title='Issues']"),
    TASKS_TOP_ICON("//a[@title='Action Items']"),
    DOCUMENTS_TOP_ICON("//a[@title='Documents']"),

    STATUS_LINK("//th[text()='Status:']/parent::tr//a"),

    MEASURES("//*[@class='measures']"),
    EDIT_WORK("popOptions"),
    MEASURES_MANAGE("Manage"),
    MEASURES_RECALCULATE_ALL("Recalculate all"),

    COSTS("popCosts"),
    ESTIMATED("Estimated Costs"),
    ACTUAL("Actual Costs"),

    METRIC("//*[@title='Metrics']"),
    METRIC_MANAGE("Manage..."),
    EDIT_DETAILS("Edit Details"),
    EDIT_PERMISSIONS("Edit Permissions"),
    MOVE("Move"),
    COPY_MOVE("Copy/Move"),
    DELETE("Delete"),
    ARCHIVE("Archive"),

    //    PROJECT_CENTRAL_TOP_TD_LOCATOR("//td[text()='Project planning']/parent::tr"),
//    PROJECT_CENTRAL_TOP_TD_ID("id"),
//    RESOURCE_PLANNING_TOP_TD_LOCATOR("//td[text()='Resource planning']/parent::tr"),
    DISCUSSIONS_TOP_ICON("//a[@title='Discussions']"),
    SUMMARY_TOP_ICON("//a[@class='summary']"),

    EDIT_WORK_LINK("link=Edit"),
    DETAILS_LINK("link=Details"),
    DETAILS_BODY("DetailsBody"),
    PLAN_RESOURCES_INFO("//th[text()='Plan Resources:']/parent::tr/td"),
    PLAN_RESOURCES_INFO_NO("No"),

    LINK_ADD_REMOVE_DELIVERABLE("link=Add / Remove"),
    LINK_ADD_REMOVE_DELIVERABLE_2("//a[contains(text(), 'Add / Remove')]"),
    GATE_LINK("//div[@id='GatesAndDelivsTable']//a[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),

    BLOCK_DIV("div"),
    BLOCK_CLASS("block"),
    DESCENDANTS_LINK("link=Descendants"),
    DESCENDANTS_IMG("DescendantWork_img", "Descendants_img"),
    IMG_ALT("alt"),
    IMG_ALT_EXPAND("Expand"),
    IMG_ALT_COLLAPSE("Collapse"),
    IMG_SRC_EXPAND("arrow-right.gif"),
    IMG_SRC_COLLAPSE("arrow-down.gif"),
    DESCENDANTS_ADD_NEW_LINK("Add New"),
    DESCENDANTS_ADD_NEW_LINK_2("link=" + DESCENDANTS_ADD_NEW_LINK.locator),

    MODULE_LINK_HREF("boxToggleAndSave"),
    MEASURE_LINK("//a[contains(@href, 'measuresBody')]"),
    MEASURE_IMG("measuresBody_img"),
    MEASURE_TABLE("//div[@id='measuresBody']//table"),
    MEASURE_TR("//tr"),
    MEASURE_TD_RECALCULATE("//td[@class='colRecalcColumnValue']//input"),
    MEASURE_TD_NAME("//td[@class='colNameColumnValue']//a"),
    MEASURE_SUBMIT("//input[@value='Recalculate']"),
    MEASURE_RECALCULATE_ALL("//th[@class='colRecalcColumnHeader']//input"),

    DELETE_DIALOG("//div[@id='popDelete']"),
    DELETE_DIALOG_V11("//div[@id='popNavDelete']"),
    DELETE_DIALOG_YES("//input[@value='Delete']"),
    ARCHIVE_DIALOG("//div[@id='popArchive']"),
    ARCHIVE_DIALOG_V11("//div[@id='popNavArchive']"),
    ARCHIVE_DIALOG_YES("//input[@value='Archive Work']"),
    OWNER("//table[@class='simple']//td"),

    GATE_SNAPSHOT_GATE_LINK("//td/a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    GATE_SNAPSHOT_REACTIVATE_DIALOG("//div[@id='uid_" + LOCATOR_REPLACE_PATTERN + "']"),
    GATE_SNAPSHOT_REACTIVATE_DIALOG_TEXTAREA("id=reactivateGateDefineCommentsTextAreaId"),
    GATE_SNAPSHOT_REACTIVATE_DIALOG_SUBMIT("//input[@value='Reactivate']"),
    GATE_SNAPSHOT_ADVANCE_BUTTON("id=SkipSoftGateShow"),
    GATE_SNAPSHOT_ADVANCE_DIALOG("id=SkipSoftGate"),
    GATE_SNAPSHOT_ADVANCE_DIALOG_TEXTAREA("id=skipSoftGateCommentsTextAreaId"),
    GATE_SNAPSHOT_ADVANCE_DIALOG_SUBMIT("//input[@value='Advance']");
    String locator;

    SummaryWorkPageLocators(String locator) {
        this.locator = locator;
    }

    SummaryWorkPageLocators(String loc92, String loc93) {
        this(TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2) ? loc92 : loc93);
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }
}

