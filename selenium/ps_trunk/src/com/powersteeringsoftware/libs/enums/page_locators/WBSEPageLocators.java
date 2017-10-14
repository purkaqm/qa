package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.VersionLocator;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.VersionLocator.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 22:27:47
 * To change this template use File | Settings | File Templates.
 */
public enum WBSEPageLocators implements ILocatorable {
    URL("project/WBS.epage"),

    DIALOG("//div[@id='ps_ui_DialogBase_0']"),

    TREE_ITEM(_8_0("//div[@class='projectDiv']"), _9_1("//div[@class='projectDiv' or @class='subRowTitle']")),
    TREE_ITEM_ARROW_TYPE_NAME("subtype"),
    TREE_ITEM_ARROW_TYPE_VALUE("actionMenu"),
    TREE_ITEM_GENERAL_ARROW("//img[@" + TREE_ITEM_ARROW_TYPE_NAME.name() + "='" + TREE_ITEM_ARROW_TYPE_VALUE.name() +
            "']", TREE_ITEM_ARROW_TYPE_NAME, TREE_ITEM_ARROW_TYPE_VALUE),
    TREE_ITEM_WORK_LINK_PATTERN("link=" + LOCATOR_REPLACE_PATTERN),
    TREE_ITEM_FIRST_LEVEL("/div/div/div[contains(@class, 'cell')]//div[@class='projectDiv']"),

    //grid. all other settings see in com.powersteeringsoftware.libs.enums.elements_locators.GridLocators
    GRID_RESOURCE_SPLIT(_8_0("Resource split"), _12_0("Resource assignment")),
    GRID_TABLE_CELL_CHECKBOXES_ALL(_8_0("//th[@id='cb_wt']//input"), _9_2("//th[@columnid='cb_wt']//input")),
    GRID_TABLE_CELL_CHECKBOXES_COLUMN("//div[@columnid='cb_wt']"),
    GRID_TABLE_CELL_CHECKBOXES_COLUMN_CHILD("//input[@type='checkbox']"),
    GRID_TABLE_CELL_INDEX("//div[@columnid='numCol']//div[contains(@class, 'cellContent')]"),
    GRID_TABLE_CELL_CALCULATED_FIELD_ACRONYM("//acronym"),
    GRID_TABLE_TREE_NAME("Name"),
    GRID_TABLE_NAME("Name"),
    GRID_TABLE_STATUS("Status"),
    GRID_TABLE_OWNER("Owner"),
    GRID_TABLE_PERSON(_8_0("Person"), _12_0("Assignments")),
    GRID_TABLE_ROLE("Role"),
    GRID_TABLE_RESOURCE_POOL("Resource Pool"),
    GRID_TABLE_CONSTRAINT_TYPE("Constraint type"),
    GRID_TABLE_CONSTRAINT_START("Constraint Start"),
    GRID_TABLE_CONSTRAINT_END("Constraint End"),
    GRID_TABLE_CONSTRAINT_DURATION("Constraint Duration"),
    GRID_TABLE_PERCENT_COMPLETE("Percent Complete"),
    GRID_TABLE_DEPENDENCY("Dependency"),
    GRID_TABLE_DEPENDENCY_SEPARATOR(";\\s*"),
    GRID_TABLE_PRIORITY("Priority"),
    GRID_TABLE_STATUS_REPORTING("Status reporting"),
    GRID_TABLE_ACTUAL_START("Actual Start"),
    GRID_TABLE_ACTUAL_END("Actual End"),
    GRID_TABLE_SCHEDULED_START("Scheduled Start"),
    GRID_TABLE_SCHEDULED_END("Scheduled End"),
    GRID_TABLE_SCHEDULED_DURATION("Scheduled Duration"),
    GRID_TABLE_DURATION("Duration"),
    GRID_TABLE_ALLOCATION(_8_0("Allocation"), _9_2("% Allocation")),
    GRID_TABLE_EFFORT("Effort"),
    GRID_TABLE_CALCULATED_FIELD_V12("Calculated field"),
    GRID_TABLE_CALCULATED_FIELD_V12_DURATION("Duration"),
    GRID_TABLE_CALCULATED_FIELD_V12_EFFORT("Effort"),
    GRID_TABLE_CALCULATED_FIELD_V12_ALLOCATION("Allocation"),
    GRID_TABLE_CURRENCY("Currency"),
    GRID_TABLE_INHERIT_CALENDAR("Inherit Calendar"),
    GRID_TABLE_CALENDAR("Calendar"),
    GRID_TABLE_INHERIT_CONTROLS(_8_0("Inherit Controls"), _9_3("Inherit cost setting")),
    GRID_TABLE_CONTROL_COST("Control Cost"),
    GRID_TABLE_MANUAL_SCHEDULING(_8_0("Manual Scheduling"), _9_3("Schedule Type")),
    GRID_TABLE_RATE_TABLE("Rate table"),
    GRID_TABLE_INHERIT_RATE_TABLE("Inherit rate table"),
    GRID_TABLE_INHERIT_PERSONAL_RATE_RULE("Inherit personal rate rule"),
    GRID_TABLE_PERSONAL_RATE_RULE("Personal rate rule"),
    GRID_TABLE_ACTIVITY_TYPES("Activity Types"),
    GRID_TABLE_DEFAULT_ACTIVITY("Default Activity"),
    GRID_TABLE_PLAN_RESOURCES("Plan Resources"),
    GRID_TABLE_INCLUDE_ACTION_ITEMS("Include Action Items"),
    GRID_TABLE_INHERIT_PERMISSIONS("Inherit Permissions"),
    GRID_TABLE_TEXTBOX_INPUT("//input[contains(@id,'ps_form_ValidationTextBox_')]"),
    GRID_TABLE_TEXTBOX_INPUT_DIV("//div[contains(@id,'widget_ps_form_ValidationTextBox_')]"),
    GRID_TABLE_NUMBER_INPUT("//input[contains(@id,'ps_form_NumberTextBox_')]"),
    GRID_TABLE_TAG_WORK_NAME(LOCATOR_REPLACE_PATTERN + " (work)"),
    GRID_TABLE_TAG_RESOURCE_NAME(LOCATOR_REPLACE_PATTERN + " (res.)"),

    GRID_GANTT_HEADER(_8_0("//div[@id='GanttSection']"), _9_2("//div[@sectionid='GanttSection']")),
    GRID_GANTT_HEADER_TOP_ROW("//div[contains(@class,'topRow')]"),
    GRID_GANTT_HEADER_TOP_CELL_MONTH("//div[@class='monthHeaderRow topRow']/div"),
    GRID_GANTT_HEADER_BOTTOM_CELL_WEEK("//div[@class='weekHeaderRow bottomRow']/div"),
    GRID_GANTT_HEADER_BOTTOM_CELL_WEEK_FORMAT("M/dd"),
    GRID_GANTT_HEADER_TOP_CELL_MONTH_FORMAT("MMMM yyyy"),
    GRID_GANTT_SECTION("//div[@class='body']" + GRID_GANTT_HEADER.name(), GRID_GANTT_HEADER),
    GRID_GANTT_SECTION_HIDDEN("hidden"),
    GRID_GANTT_SECTION_CELL(_8_0("//div[@class='cellContent']"), _9_0("//div[@class='cell']")),
    GRID_GANTT_SECTION_CELL_BAR_WRAPPER("/div[1]"),
    GRID_GANTT_SECTION_CELL_BAR_WRAPPER_CRITICAL_PATH("criticalPath"),
    GRID_GANTT_SECTION_CELL_BASELINE("/div[2]"),
    GRID_GANTT_SECTION_CELL_STATUS("//table"),
    GRID_GANTT_SECTION_CELL_STATUS_COMPLETED("completed"),
    GRID_GANTT_SECTION_CELL_STATUS_ON_TRACK("on_track"),
    GRID_GANTT_SECTION_CELL_STATUS_PROPOSED("proposed"),
    GRID_GANTT_SECTION_CELL_PERCENTAGE("//div[@class='prCompleted']"),
    GRID_GANTT_SECTION_CELL_LEFT_TD("//tr[2]/td[1]"),
    GRID_GANTT_SECTION_CELL_RIGHT_TD("//tr[2]/td[3]"),
    GRID_GANTT_SECTION_CELL_MIDDLE_TD("//tr[2]/td[2]"),
    GRID_GANTT_SECTION_LINK_CANVAS_CLASS("linkCanvas"),
    GRID_GANTT_SECTION_LINK_CANVAS("//div[contains(@class,'" + GRID_GANTT_SECTION_LINK_CANVAS_CLASS.name() + "')]",
            GRID_GANTT_SECTION_LINK_CANVAS_CLASS),
    GRID_GANTT_SECTION_LINK_CANVAS_RESIZE_CLASS("resizeAction"),
    GRID_GANTT_SECTION_LINK_CANVAS_MOVE_CLASS("moveAction"),
    GRID_GANTT_SLIDER("//div[contains(@class,'dijitSliderBarContainerH')]"),
    GRID_GANTT_SLIDER_INCREMENT("//div[@class='dijitSliderIncrementIconH']"),
    GRID_GANTT_SLIDER_DECREMENT("//div[@class='dijitSliderDecrementIconH']"),
    GRID_GANTT_SCROLL_BAR("css=div.horizontalScrollArea>div[sectionid=\"GanttSection\"]"),

    VARIABLE_ALLOCATION_SECTION(GRID_GANTT_SECTION.name() + "/div[2]", GRID_GANTT_SECTION),
    VARIABLE_ALLOCATION_SECTION_4_CELL("/div[4]"),
    VARIABLE_ALLOCATION_COLUMN("'W'yyyy-MM-dd"),
    VARIABLE_ALLOCATION_NUMBER_FORMAT_GROUPING_USED("true"),
    VARIABLE_ALLOCATION_NUMBER_FORMAT_MAX_FRACTION_DIGITS(2),
    VARIABLE_ALLOCATION_NUMBER_FORMAT_MIN_FRACTION_DIGITS(0),

    //team pane for 9.1:
    TEAM_PANE("//div[contains(@class, 'body')][2]"),
    TEAM_PANE_EMPTY_CELL("//div[@class='cell empty']"),
    TEAM_PANE_GRID("//div[@id='TeamPane']"),
    TEAM_PANE_MASTER("//div[@id='TeamPaneMaster']"),
    TEAM_PANE_MASTER_TREE(TEAM_PANE_MASTER.name() + "//div[contains(@class,'workTree')]/div[@class='page']", TEAM_PANE_MASTER),
    TEAM_PANE_MASTER_TREE_LOADED("loaded"),
    TEAM_PANE_MASTER_TREE_LOADED_TRUE("true"),
    TEAM_PANE_MASTER_ROW("//div[@class='cellContent']"),
    TEAM_PANE_MASTER_ROW_UNALLOCATED_PREFIX("Unallocated"),
    TEAM_PANE_COLUMN_FIRST(TEAM_PANE_GRID.name() + "//div[@class='column number']", TEAM_PANE_GRID),
    TEAM_PANE_COLUMN_DATE_FORMAT_WEEK_AV("'W'yyyy-MM-dd'_AV'"),
    TEAM_PANE_COLUMN_DATE_FORMAT_WEEK_AL("'W'yyyy-MM-dd'_AL'"),
    TEAM_PANE_COLUMN("//div[@columnid='" + LOCATOR_REPLACE_PATTERN + "']"),
    TEAM_PANE_CELL_CONTENT("//div[@class='cellContent']"),
    TEAM_PANE_CELL_COLOR_RED("red"),
    TEAM_PANE_CELL_COLOR_YELLOW("yellow"),
    TEAM_PANE_ROLE_CELL("//div[@id='RolesSection']" + TEAM_PANE_MASTER_ROW.name(), TEAM_PANE_MASTER_ROW),
    TEAM_PANE_NUMBER_FORMAT_GROUPING_USED("true"),
    TEAM_PANE_NUMBER_FORMAT_MAX_FRACTION_DIGITS_HOURS(0),
    TEAM_PANE_NUMBER_FORMAT_MAX_FRACTION_DIGITS_PERCENTS(1),
    TEAM_PANE_NUMBER_FORMAT_MIN_FRACTION_DIGITS(0),

    //histogram locators this (9.1 only)
    HISTOGRAM_MASTER("//div[@id='HistogramMaster']//div[@class='legend']"),
    HISTOGRAM_LEGEND("//div[@class='legendRow']"),
    HISTOGRAM_LEGEND_COLOR("/div[@class='colorBox']"),
    HISTOGRAM_LEGEND_NAME("/div[@class='name']"),
    HISTOGRAM_BODY("//div[@class='body histogram']//div[@id='Histogram']"),
    HISTOGRAM_LABEL("/parent::div//div[@class='line']/div"),
    HISTOGRAM_DYNAMIC_FF("css=div.histogram svg rect"),
    HISTOGRAM_DYNAMIC_IE("css=div.histogram roundrect"),
    HISTOGRAM_COLUMN_FF("//rect"),
    HISTOGRAM_COLUMN_IE("//v:roundrect"),
    HISTOGRAM_COLUMN_COLOR_FF("fill"),
    HISTOGRAM_COLUMN_COLOR_IE("fillcolor"),
    HISTOGRAM_COLUMN_COLOR_IE_8_a(".*fillcolor\\s*=\\s*\"([^\"]+)\".*"),
    HISTOGRAM_COLUMN_COLOR_IE_8_b("$1"),
    HISTOGRAM_COLUMN_X_FF("x"),
    HISTOGRAM_COLUMN_Y_FF("y"),
    HISTOGRAM_COLUMN_TOOLTIP("tooltip"),
    HISTOGRAM_COLUMN_W_FF("width"),
    HISTOGRAM_COLUMN_H_FF("height"),
    HISTOGRAM_COLUMN_X_IE("left"),
    HISTOGRAM_COLUMN_Y_IE("top"),
    HISTOGRAM_COLUMN_W_IE("width"),
    HISTOGRAM_COLUMN_H_IE("height"),

    //for popup near work item:
    MENU_POPUP_VIEW_LABEL("View"),
    MENU_POPUP_ADD_UNDER_LABEL("Add under"),
    MENU_POPUP_ADD_AFTER_LABEL("Add after"),
    MENU_POPUP_INDENT_LABEL("Indent"),
    MENU_POPUP_OUTDENT_LABEL("Outdent"),
    MENU_POPUP_CAPTURE_BASELINES_LABEL("Capture baselines"),
    MENU_POPUP_ADD_SPLIT_LABEL(_8_0("Add split"), _12_0("Add assignment")),
    MENU_POPUP_DISPLAY_FROM_HERE_LABEL("Display from here"),
    MENU_POPUP_DISPLAY_FROM_PARENT_LABEL("Display from parent"),
    MENU_POPUP_INFORMATION_LABEL("Information"),
    MENU_POPUP_EXCLUDE_FROM_TOTALS_LABEL(_8_0(null), _9_1("Exclude from totals")),
    MENU_POPUP_DELETE_LABEL("Delete"),

    // for new works frame-popup:
    ADD_NEW_SUB_WORK_HEADER_NUMBER("#"),
    ADD_NEW_SUB_WORK_HEADER_TYPE("Type"),
    ADD_NEW_SUB_WORK_HEADER_NAME("Name"),
    ADD_NEW_SUB_WORK_HEADER_DAE_CALCULATED_FIELD(_8_0(null), _12_0("Calculated Field"), _13(null)),
    ADD_NEW_SUB_WORK_HEADER_DAE_DURATION("Duration"),
    ADD_NEW_SUB_WORK_HEADER_DAE_ALLOCATION("Allocation"),
    ADD_NEW_SUB_WORK_HEADER_DAE_EFFORT("Effort"),
    ADD_NEW_SUB_WORK_HEADER_SCHEDULED_TYPE(_8_0(null), _9_3("Scheduling Type")),
    ADD_NEW_SUB_WORK_HEADER_CONSTRAINT("Constraint"),
    ADD_NEW_SUB_WORK_HEADER_START_DATE("Start Date"),
    ADD_NEW_SUB_WORK_HEADER_END_DATE("End Date"),
    ADD_NEW_SUB_WORK_HEADER_PREDECESSOR(_8_0("Predecessor"), _10_0("Dependency")),

    ADD_NEW_SUB("//div[@id='ws_dlgNode']"),
    ADD_NEW_SUB_WORK_POPUP_NODE_SELECTOR(".//li"),
    ADD_NEW_SUB_WORK_HEADER("//table[@id='ws_tableNode']//th"),
    ADD_NEW_SUB_WORK_OUTDENT_ARROW("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[1]/img"),
    ADD_NEW_SUB_WORK_INDENT_ARROW("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[2]/img"),
    ADD_NEW_SUB_WORK_OUTDENT_ARROW_SRC_ATTR_OFF("left-arrow-off.gif"),
    ADD_NEW_SUB_WORK_INDENT_ARROW_SRC_ATTR_OFF("right-arrow-off.gif"),
    ADD_NEW_SUB_WORK_NUMBER("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[3]/div"),
    ADD_NEW_SUB_WORK_ITEM_NAME("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsName']//input"),
    ADD_NEW_SUB_WORK_TYPE("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsType']/div"),
    ADD_NEW_SUB_WORK_DURATION("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsDuration']/div"),
    ADD_NEW_SUB_WORK_ALLOCATION("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsAllocation']/div"),
    ADD_NEW_SUB_WORK_EFFORT("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsEffort']/div"),
    ADD_NEW_SUB_WORK_SCHEDULED_TYPE("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsSchedulingType']/div"),
    ADD_NEW_SUB_WORK_CONSTRAINT("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsCType']/div"),
    ADD_NEW_SUB_WORK_START_DATE_NAME("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsStart']/div"),
    ADD_NEW_SUB_WORK_END_DATE_NAME("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsFinish']/div"),
    ADD_NEW_SUB_WORK_PREDECESSOR("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsPred']//input"),
    ADD_NEW_SUB_WORK_SET_PREDECESSOR(
            _8_0("//table[@id='ws_tableNode']//div[text()='Set predecessors']"),
            _10_0("//table[@id='ws_tableNode']//div[text()='Set dependencies']"),
            _12_0("//input[@value='Set dependencies']")),
    ADD_NEW_SUB_WORK_CANCEL(_8_0("//table[@id='ws_tableNode']//div[text()='Cancel']"), _12_0("//table[@id='ws_tableNode']//input[@value='Cancel']")),
    ADD_NEW_SUB_WORK_SUBMIT("//input[@value='Submit' and @type='button']"),
    ADD_NER_SUB_WORK_ERROR("//div[@id='ws_errorNode']//li"),

    ADD_NEW_SUB_WORK_TOP_ACRONYM("/acronym"),
    ADD_NEW_SUB_WORK_TOP_DURATION_ACRONYM_V11("//th[@class='wsDuration']" + ADD_NEW_SUB_WORK_TOP_ACRONYM.name(), ADD_NEW_SUB_WORK_TOP_ACRONYM),
    ADD_NEW_SUB_WORK_TOP_ALLOCATION_ACRONYM_V11("//th[@class='wsAllocation']" + ADD_NEW_SUB_WORK_TOP_ACRONYM.name(), ADD_NEW_SUB_WORK_TOP_ACRONYM),
    ADD_NEW_SUB_WORK_TOP_EFFORT_ACRONYM_V11("//th[@class='wsEffort']" + ADD_NEW_SUB_WORK_TOP_ACRONYM.name(), ADD_NEW_SUB_WORK_TOP_ACRONYM),

    ADD_NEW_SUB_WORK_TOP_CALCULATED_FIELD_V12("//th[@class='wsCalculatedField']"),
    ADD_NEW_SUB_WORK_CALCULATED_FIELD_V12("//table[@id='ws_tableNode']//tr[" + LOCATOR_REPLACE_PATTERN + "]//td[@class='wsCalculatedField']/div"),

    ADD_NEW_SUB_WORK_DAE_INPUT("//input"),
    ADD_NEW_SUB_WORK_DAE_ACRONYM("/parent::td//acronym"),
    ADD_NEW_SUB_WORK_ROWS_NUM(5),

    //gated dave popup:
    GATE_SAVE_DIALOG_POPUP(DIALOG),
    GATE_SAVE_DIALOG_POPUP_OK("//input[@value='OK']"),
    GATE_SAVE_DIALOG_POPUP_CANCEL("//input[@value='Cancel']"),

    //delete popup:
    DELETE_DIALOG_POPUP_DELETE("//input[@value='Delete']"),
    DELETE_DIALOG_POPUP_CANCEL("//input[@value='Cancel']"),

    //information popup:
    INFORMATION_DIALOG_POPUP(DIALOG),
    INFORMATION_DIALOG_POPUP_OK("//input[@value='Ok']"),
    INFORMATION_DIALOG_POPUP_TABLE("//table"),
    INFORMATION_DIALOG_POPUP_TR("//tr"),
    INFORMATION_DIALOG_POPUP_TH("/th"),
    INFORMATION_DIALOG_POPUP_TD("/td"),
    INFORMATION_DIALOG_POPUP_TH_OWNER("Owner"),
    INFORMATION_DIALOG_POPUP_TH_STATUS("Status"),
    INFORMATION_DIALOG_POPUP_TH_CONSTRAINT("Constraint"),
    INFORMATION_DIALOG_POPUP_TH_START_DATE("Start"),
    INFORMATION_DIALOG_POPUP_TH_END_DATE("End"),

    //options
    OPTIONS_BLOCK_LINK("//div[@class='link' and text()='Options']"),
    OPTIONS_BLOCK_DIV(_8_0("//div[@class='options']/div"), _9_1("//div[@class='options']/div[contains(@class,'body')]")),
    OPTIONS_BLOCK_DIV_STYLE("style"),
    OPTIONS_BLOCK_DIV_STYLE_AUTO("auto"),
    OPTIONS_BLOCK_DISPLAY(_8_0("//td[@class='filtersColumn'][1]"), _9_0("//tr[@class='filtersColumn']")),
    OPTIONS_BLOCK_DISPLAY_CHECKBOX_INPUT(".//input[@type='checkbox']"),
    OPTIONS_BLOCK_DISPLAY_CHECKBOX_INPUT_ID_PREFIX("WBS_Grid"),
    OPTIONS_BLOCK_DISPLAY_CHECKBOX_LABEL("./following::label"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_GANTT("Gantt"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_CRITICAL_PATH("Critical Path"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE(_9_0("Team pane"), _9_1("Team")),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_VARIABLE_ALLOCATIONS("Variable Allocation"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_LIST("List"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_HISTOGRAM("Histogram"),
    //OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_HIDE("Hide team"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_FOLDERS("Folders"),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TASKS(_9_1("Tasks"), _10_0("Action Items")),
    OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_DELIVERABLES("Deliverables"),

    // options for 8.2 or 9.1 and latter
    OPTIONS_BLOCK_OPTIONS_NAME("Options"),
    // options for 90 or latter:
    OPTIONS_BLOCK_DISPLAY_NAME("Display"),
    OPTIONS_BLOCK_LAYOUTS_NAME("Layouts"),
    OPTIONS_BLOCK_COLUMNS_NAME("Columns"),
    OPTIONS_BLOCK_FILTERS_NAME("Filters"),
    OPTIONS_BLOCK_GENERAL_LINK_1(_8_0("//div[@class='link']"), _9_0("//div[@class='item link']")),
    OPTIONS_BLOCK_GENERAL_LINK_2(_8_0("//div[@class='link' and contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),
            _9_0("//div[@class='item link' and contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]")),

    //for 9.0:
    OPTIONS_BLOCK_OPTIONS_INDEPENDENT_WORK_CHECKBOX("independentWorkOption"),
    OPTIONS_BLOCK_OPTIONS_DELIVERABLES_DISPLAY_CHECKBOX("deliverablesDisplayOption"),
    OPTIONS_BLOCK_OPTIONS_FOLDERS_DISPLAY("foldersDisplayOption"),
    OPTIONS_BLOCK_OPTIONS_TASKS_DISPLAY("tasksDisplayOption"),
    OPTIONS_BLOCK_OPTIONS_RUN_SCHEDULER_AUTOMATICALLY("autoScheduleOption"),
    OPTIONS_BLOCK_OPTIONS_LABEL("//label[@for='" + LOCATOR_REPLACE_PATTERN + "']"),
    //for 9.1:
    OPTIONS_BLOCK_OPTIONS_INDEPENDENT_WORK_LABEL("Include independent work"),
    OPTIONS_BLOCK_OPTIONS_SCHEDULER_LABEL("Run scheduler automatically"),

    OPTIONS_BLOCK_TEAM_PANE_HOURS("asHoursOption"),
    OPTIONS_BLOCK_TEAM_PANE_PERCENTAGE("asPercentageOption"),
    OPTIONS_BLOCK_TEAM_PANE_YELLOW_INPUT("//input[@id='yellowThreshold']"),
    OPTIONS_BLOCK_TEAM_PANE_RED_INPUT("//input[@id='redThreshold']"),

    OPTIONS_BLOCK_APPLY("//input[@value='Apply options']"),
    OPTIONS_BLOCK_RESET_NAME(_8_0("Reset"), _12_0("Cancel")),
    OPTIONS_BLOCK_RESET("//input[@value='" + OPTIONS_BLOCK_RESET_NAME.name() + "']", OPTIONS_BLOCK_RESET_NAME),
    OPTIONS_BLOCK_APPLY_DIALOG_YES("//input[@value='Save and continue']"),
    OPTIONS_BLOCK_APPLY_DIALOG_NO("//input[@value='Cancel']"),
    OPTIONS_BLOCK_FILTERS(_8_0("//td[@class='filtersColumn'][2]"), _9_0("//tr[@class='filtersColumn']")),
    OPTIONS_BLOCK_FILTERS_NAME_SELECT(_8_0("PropertySelection"), _12_0("PropertySelection_0"), _12_1("PropertySelection")),
    OPTIONS_BLOCK_FILTERS_NAME_SELECT_EXACTLY("value=EXACTLY"),
    OPTIONS_BLOCK_FILTERS_NAME_SELECT_ANY_OF("value=ANY_OF"),
    OPTIONS_BLOCK_FILTERS_NAME_SELECT_ALL_OF("value=ALL_OF"),
    OPTIONS_BLOCK_FILTERS_NAME_INPUT_WORK("workNames"),
    OPTIONS_BLOCK_FILTERS_TYPES_INPUT("//div[@class='Selector']"),
    // date filters:
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_START(_8_0("PropertySelection_2_0"), _12_0("PropertySelection_5"), _12_1("PropertySelection_4")),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_END(_8_0("PropertySelection_3_0"), _12_0("PropertySelection_6"), _12_1("PropertySelection_5")),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_CONSTRAINT("Constraint"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_ANY("Any"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_ACTUAL("Actual"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_SCHEDULED("Scheduled"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_START_INTERVAL("startDateInterval"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_END_INTERVAL("endDateIntervalType"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_IS("is"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_BEFORE("before"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_AFTER("after"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_BETWEEN("between"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_START_PICKER("//span[@id='filterStartDate1']/div"),
    OPTIONS_BLOCK_FILTERS_DATE_SELECT_END_PICKER("//span[@id='filterEndDate1']/div"),
    // general options:
    OPTIONS_BLOCK_FILTERS_SELECT_EMPTY_VALUE("value=0"),
    OPTIONS_BLOCK_FILTERS_SELECT_NO_VALUE("value=1"),
    OPTIONS_BLOCK_FILTERS_SELECT_ANY_VALUE("value=2"),
    OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE("value=3"),
    // tags:
    OPTIONS_BLOCK_FILTERS_TAG_SELECT(_8_0("PropertySelection_1"), _12_0("PropertySelection_3_0")),
    OPTIONS_BLOCK_FILTERS_TAG_SELECT_EMTY("value=none"),
    OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED("label=" + LOCATOR_REPLACE_PATTERN),
    OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED_SELECT("workTagOptionSelect"),
    OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED_SELECT_MULTI("//tr[@id='workTagFilterTagContainer']/td/div[" + LOCATOR_REPLACE_PATTERN + "]"),
    // owner:
    OPTIONS_BLOCK_FILTERS_OWNER_SELECT("ownerOptions"),
    OPTIONS_BLOCK_FILTERS_OWNER_SELECT_MULT_WRAPPER("//div[@id='SearchBrowseMultWrapper']"),
    OPTIONS_BLOCK_FILTERS_OWNER_SELECT_MULT_WRAPPER_REMOVE("/img[@alt='Remove']"),
    OPTIONS_BLOCK_FILTERS_OWNER_ASSIGN_USER_POPUP("//div[@id='popup_fowner']"),
    //status filters:
    OPTIONS_BLOCK_FILTERS_STATUS_SELECT("statusOptions"),
    OPTIONS_BLOCK_FILTERS_STATUS_SELECT_TAG_CHOOSER("//div[@id='statusFilter']/div"),
    // pool:
    OPTIONS_BLOCK_FILTERS_POOL_SELECT("poolOptions"),
    OPTIONS_BLOCK_FILTERS_POOL_SELECT_TAG_CHOOSER("//div[@id='poolFilter']/div"),
    // rool:
    OPTIONS_BLOCK_FILTERS_ROLE_SELECT("roleOptions"),
    OPTIONS_BLOCK_FILTERS_ROLE_SELECT_TAG_CHOOSER("//div[@id='roleFilter']/div"),
    // Columns. since 9.0 :
    OPTIONS_BLOCK_COLUMNS_DATES("Dates"),
    OPTIONS_BLOCK_COLUMNS_DATES_ACTUAL("Actual"),
    OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT("Constraint"),
    OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT_TYPE("Constraint type"),
    OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED("Scheduled"),
    OPTIONS_BLOCK_COLUMNS_PROJECT("Project"),
    OPTIONS_BLOCK_COLUMNS_PROJECT_NAME("Name"),
    OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER("Owner"),
    OPTIONS_BLOCK_COLUMNS_PROJECT_STATUS("Status"),
    OPTIONS_BLOCK_COLUMNS_PROJECT_PRIORITY("Priority"),
    OPTIONS_BLOCK_COLUMNS_PROJECT_DEPENDENCY("Dependency"),
    OPTIONS_BLOCK_COLUMNS_RESOURCE("Resource"),
    OPTIONS_BLOCK_COLUMNS_RESOURCE_PERSON(_8_0("Person"), _12_0("Assignments")),
    OPTIONS_BLOCK_COLUMNS_RESOURCE_ROLE("Role"),
    OPTIONS_BLOCK_COLUMNS_RESOURCE_POOL("Resource Pool"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS("Controls"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_STATUS_REPORTING("Status reporting"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_CURRENCY("Currency"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CALENDAR("Inherit Calendar"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_CALENDAR("Calendar"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_CONTROLS(_8_0("Inherit Controls"), _9_3("Inherit cost setting")),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_CONTROL_COST("Control Cost"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_MANUAL_SCHEDULING(_8_0("Manual Scheduling"), _9_3("Schedule Type")),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_PERSONAL_RATE_RULE("Inherit personal rate rule"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_PERSONAL_RATE_RULE("Personal rate rule"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_RATE_TABLE("Inherit rate table"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_RATE_TABLE("Rate table"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_DEFAULT_ACTIVITY("Default Activity"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_DEFAULT_ACTIVITY("Inherit Default Activity"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_ACTIVITY_TYPES("Activity Types"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_ALL_ACTIVITIES("All Activities"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_ACTIVITY_TYPES("Inherit Activity Types"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_PLAN_RESOURCES("Plan Resources"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INCLUDE_ACTION_ITEMS("Include Action Items"),
    OPTIONS_BLOCK_COLUMNS_CONTROLS_INHERIT_PERMISSIONS("Inherit Permissions"),
    OPTIONS_BLOCK_COLUMNS_TAGS("Tags"),
    // we have not resource tags in db in v13:
    _OPTIONS_BLOCK_COLUMNS_TAG_WORK_NAME(GRID_TABLE_TAG_WORK_NAME.getLocator()),
    _OPTIONS_BLOCK_COLUMNS_TAG_RESOURCE_NAME(GRID_TABLE_TAG_RESOURCE_NAME.getLocator()),
    // its for 9.2 or latter
    OPTIONS_BLOCK_COLUMNS_SELECTED("Selected"),

    // for bulk operations:
    BULK_OPERATIONS_COMBOBOX_SELECTOR("//div[@class='actionsArea top']//div[contains(@id,'widget_ps_form_ComboBox_')]"),
    BULK_OPERATIONS_APPLY("//div[@class='actionsArea top']//input[@value='Apply']"),

    BULK_OPERATIONS_EDIT_LABEL("Bulk edit"),
    BULK_OPERATIONS_DELETE_LABEL("Delete"),
    BULK_OPERATIONS_ALLOCATE_RESOURCE_LABEL("Allocate resource"),
    BULK_OPERATIONS_CAPTURE_BASELINES_LABEL("Capture baselines"),

    BULK_EDIT_BLOCK("//div[@class='bulkEdit']"),
    BULK_EDIT_BLOCK_CHILD(_8_0("//div[@class='controlDiv']"), _9_1("//span//tr[not(contains(@style, 'none'))]")),
    BULK_EDIT_BLOCK_CHILD_LABEL(_8_0("/div[1]"), _9_1("/td[@class='controlLabel']")),
    BULK_EDIT_BLOCK_CHILD_LABEL_INNER(_8_0("/nobr"), _9_2("/div")),
    BULK_EDIT_BLOCK_CHILD_ELEMENT(_8_0("/div[2]/*[not(contains(@class, 'hidden'))]"),
            _9_1("/td[@class='controlDiv']/div/*[not(contains(@class, 'hidden'))]")),
    BULK_EDIT_BLOCK_CHILD_INPUT("//input"),
    BULK_EDIT_CONTROL_STATUS_LABEL("Status:"),
    BULK_EDIT_CONTROL_CONSTRAINT_LABEL("Constraint type:"),
    BULK_EDIT_CONTROL_CONSTRAINT_START_LABEL("Constraint Start:"),
    BULK_EDIT_CONTROL_CONSTRAINT_END_LABEL("Constraint End:"),
    BULK_EDIT_CONTROL_PERCENT_COMPLETE_LABEL("Percent Complete:"),
    BULK_EDIT_CONTROL_DEPENDENCY_LABEL("Dependency:"),
    BULK_EDIT_CONTROL_DEPENDENCY_SEPARATOR(GRID_TABLE_DEPENDENCY_SEPARATOR.name(), GRID_TABLE_DEPENDENCY_SEPARATOR),
    BULK_EDIT_CONTROL_PRIORITY_LABEL("Priority:"),
    BULK_EDIT_CONTROL_STATUS_REPORTING_LABEL("Status reporting:"),
    BULK_EDIT_CONTROL_ACTUAL_START_LABEL("Actual Start:"),
    BULK_EDIT_CONTROL_ACTUAL_END_LABEL("Actual End:"),
    BULK_EDIT_CONTROL_OWNER_LABEL("Owner:"),
    BULK_EDIT_CONTROL_ALLOCATION_LABEL(_8_0("Allocation:"), _9_2("% Allocation")),
    BULK_EDIT_CONTROL_EFFORT_LABEL("Effort:"),
    BULK_EDIT_CONTROL_RESOURCE_DURATION_LABEL("Duration:"),
    BULK_EDIT_CONTROL_ROLE_LABEL("Role:"),
    BULK_EDIT_CONTROL_RESOURCE_POOL_LABEL("Resource Pool:"),
    BULK_EDIT_CONTROL_CALCULATED_FIELD_LABEL("Calculated field:"),
    BULK_EDIT_CONTROL_CURRENCY_LABEL("Currency:"),
    BULK_EDIT_CONTROL_CALENDAR_LABEL("Calendar:"),
    BULK_EDIT_CONTROL_INHERIT_CALENDAR_LABEL("Inherit Calendar:"),
    BULK_EDIT_CONTROL_MANUAL_SCHEDULING_LABEL(_8_0("Manual Scheduling:"), _9_3("Schedule Type")),
    BULK_EDIT_CONTROL_CONTROL_COST_LABEL("Control Cost:"),
    BULK_EDIT_CONTROL_INHERIT_CONTROLS_LABEL(_8_0("Inherit Controls:"), _9_3("Inherit cost setting")),
    BULK_EDIT_CONTROL_RATE_TABLE_LABEL("Rate table:"),
    BULK_EDIT_CONTROL_INHERIT_RATE_TABLE_LABEL("Inherit rate table:"),
    BULK_EDIT_CONTROL_INHERIT_PERSONAL_RATE_RULE_LABEL("Inherit personal rate rule:"),
    BULK_EDIT_CONTROL_PERSONAL_RATE_RULE_LABEL("Personal rate rule:"),
    BULK_EDIT_CONTROL_INHERIT_ACTIVITY_TYPES_LABEL("Inherit Activity Types:"),
    BULK_EDIT_CONTROL_INHERIT_DEFAULT_ACTIVITY_LABEL("Inherit Default Activity:"),
    BULK_EDIT_CONTROL_ALL_ACTIVITIES_LABEL("All Activities:"),
    BULK_EDIT_CONTROL_ACTIVITY_TYPES_LABEL("Activity Types:"),
    BULK_EDIT_CONTROL_DEFAULT_ACTIVITY_LABEL("Default Activity:"),
    BULK_EDIT_CONTROL_PLAN_RESOURCES_LABEL("Plan Resources:"),
    BULK_EDIT_CONTROL_INCLUDE_ACTION_ITEMS_LABEL("Include Action Items:"),
    BULK_EDIT_CONTROL_INHERIT_PERMISSIONS_LABEL("Inherit Permissions:"),
    BULK_EDIT_CONTROL_TAG_WORK_NAME(LOCATOR_REPLACE_PATTERN + " (work)"),
    BULK_EDIT_CONTROL_TAG_RESOURCE_NAME(LOCATOR_REPLACE_PATTERN + " (res.)"),

    CONTROL_YES("Yes"),
    CONTROL_NO("No"),
    CONTROL_MANUAL_SCHEDULING_YES("Manual"),
    CONTROL_MANUAL_SCHEDULING_NO("Automatic"),
    BULK_EDIT_CONTROL_CALCULATED_FIELD_EFFORT("value=EFFORT"),
    BULK_EDIT_CONTROL_CALCULATED_FIELD_ALLOCATION("value=ALLOCATION"),
    BULK_EDIT_CONTROL_CALCULATED_FIELD_RESOURCE_DURATION("value=RESOURCE_DURATION"),
    BULK_EDIT_CONTROL_EFFORT_ALLOCATION_REGEXP("\\d*\\.?\\d{1,}([eE][-+]?\\d+)?"),
    BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MAX("8999999999999999"),
    BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MIN("0"),
    BULK_EDIT_CONTROL_RESOURCE_DURATION_REGEXP("\\d+"),
    BULK_EDIT_CONTROL_PERCENT_COMPLETE_REGEXP("\\d{1,}"),
    BULK_EDIT_CONTROL_PERCENT_COMPLETE_MAX("100"),
    BULK_EDIT_CONTROL_PERCENT_COMPLETE_MIN("0"),

    BULK_EDIT_UPDATE("//input[@value='Update']"),
    BULK_EDIT_CANCEL("//input[@value='Cancel']"),

    ALLOCATE_RESOURCES_COMBOBOX_12("//div[contains(@id, 'widget_ps_ui_FilteredAjaxCombobox_')]"),
    ALLOCATE_RESOURCES_COMBOBOX_12_SEARCH("Search"),
    ALLOCATE_RESOURCES_COMBOBOX_12_MATCHES("Best Matches"),
    ALLOCATE_RESOURCES_COMBOBOX_12_QUALIFICATIONS("By Qualifications"),

    ALLOCATE_RESOURCES_POPUP("//div[@id='userAssignDialog']"),
    ALLOCATE_RESOURCES_POPUP_NOT_FOUND_MSG("No matches found"),
    ALLOCATE_RESOURCES_POPUP_TAB_BEST_MATCHES("Best Matches"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS("By Qualifications"),
    ALLOCATE_RESOURCES_POPUP_TAB_SEARCH("Search"),
    ALLOCATE_RESOURCES_POPUP_TAB_SEARCH_INPUT("//input[@type='text' and contains(@class, 'dnd-search')]"),
    ALLOCATE_RESOURCES_POPUP_TAB_SEARCH_SUBMIT("//input[contains(@onclick, 'peoplePicker.sendSearchByName')]"),
    ALLOCATE_RESOURCES_POPUP_TAB_LOADING("//tr[@class='dnd-msg-loading']"),
    ALLOCATE_RESOURCES_POPUP_TAB_ERROR("//tr[@class='dnd-msg-error']"),
    ALLOCATE_RESOURCES_POPUP_TAB_LINK("//a"),
    ALLOCATE_RESOURCES_POPUP_TAB_LINK_PARENT("./parent::td/parent::tr"),
    ALLOCATE_RESOURCES_POPUP_TAB_LINK_IMG("//img"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_ROLES("roles_opt"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_ROLES_SELECT_VALUE("role_chQ"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_POOLS("pools_opt"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_POOLS_SELECT_VALUE("pool_chQ"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SUBMIT("//input[contains(@onclick, " +
            "'peoplePicker.prepareAndMakeQSearch')]"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_EMPTY_VALUE("value=0"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_ANY_VALUE("value=1"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_NO_VALUE("value=2"),
    ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_SPECIFIED_VALUE("value=3"),

    //for 9.3
    ALLOCATE_RESOURCES_CONFIRMATION_YES("//input[@value='Yes']"),
    ALLOCATE_RESOURCES_CONFIRMATION_NO("//input[@value='No']"),


    GATE_PROJECT_POPUP(DIALOG),
    GATE_PROJECT_POPUP_TITLE("//span[@id='ps_ui_DialogBase_0_title']"),
    GATE_PROJECT_POPUP_FRAME("//iframe[@id='iframe_tollgate_popup']"),
    GATE_PROJECT_POPUP_SUBMIT("//input[@id='Submit']"),
    GATE_PROJECT_POPUP_CANCEL("//input[@id='cancelButton']"),
    //GATE_PROJECT_POPUP_ERROR("//div[@class='MsgBox RedBoxBorder']"),
    GATE_PROJECT_POPUP_TABLE_ROW("//td[@class='first']/a/parent::td/parent::tr"),
    GATE_PROJECT_POPUP_TABLE_ROW_LINK("//td[1]/a"),
    GATE_PROJECT_POPUP_TABLE_ROW_STATUS("//td[2]/select"),
    GATE_PROJECT_POPUP_TABLE_ROW_STATUS_CANCELED("Canceled"),
    GATE_PROJECT_POPUP_TABLE_ROW_STATUS_DELAYED("Delayed"),
    GATE_PROJECT_POPUP_TABLE_ROW_START_DATE("//td[3]/div"),
    GATE_PROJECT_POPUP_TABLE_ROW_END_DATE("//td[4]/div"),
    GATE_PROJECT_POPUP_PROJECT_START_DATE("//div[@class='bxTable']//tr[1]/td[2]/div"),
    GATE_PROJECT_POPUP_PROJECT_END_DATE("//div[@class='bxTable']//tr[2]/td[2]"),

    SHOW_LEVELS_IMG("//img[@alt='Expand/Collapse']"),
    SHOW_LEVELS_IMG_2("//img[contains(@alt, 'Collapse')]"),

    FULL_SCREEN_IMG(_8_0("//img[@alt='Fullscreen']"), _9_1("//div[text()='Maximize']")),
    STANDARD_SCREEN_IMG(_8_0("//img[@alt='Standard']"), _9_1("//div[text()='Restore']")),

    RUN_SCHEDULER_IMG("//img[@alt='Run scheduler']"),
    RUN_SCHEDULER_UP_TO_DATE_IMG("//img[@alt='Schedule is up-to-date']"),
    //SHOW_LEVEL_LABEL("Show 1 level"),
    //SHOW_LEVELS_TEMPLATE("Show " + LOCATOR_REPLACE_PATTERN + " levels"),


    LAYOUTS_SAVE_LAYOUT("Save layout"),
    LAYOUTS_DELETE_LAYOUT("Delete layout"),
    LAYOUTS_PROJECT_PLANING("Project planning"),
    LAYOUTS_RESOURCE_PLANING("Resource planning"),

    //save and delete dialog prefix (different for 82 and 90)
    SAVE_DELETE_LAYOUT_PREFIX(_8_0("ps_ui_wbs_WBSLayouts"), _9_0("ps_grid_Layouts")),
    // save layout menu and dialog:
    SAVE_LAYOUT_DIALOG(SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_saveDlg", SAVE_DELETE_LAYOUT_PREFIX),  //div[contains(@id, 'ps_ui_wbs_WBSLayouts')]
    SAVE_LAYOUT_DIALOG_INPUT_NEW("//input[@id='" + SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_saveDialogNew']", SAVE_DELETE_LAYOUT_PREFIX),
    SAVE_LAYOUT_DIALOG_COMBOBOX_EXISTING("//div[@id='widget_" + SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_saveDialogList']", SAVE_DELETE_LAYOUT_PREFIX),
    SAVE_LAYOUT_DIALOG_COMBOBOX_EXISTING_DIABLED("dijitDisabled"),
    SAVE_LAYOUT_DIALOG_SUBMIT(SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_saveDialogSubmit", SAVE_DELETE_LAYOUT_PREFIX),
    // delete layout menu and dialog:
    DELETE_LAYOUT_DIALOG("//div[@id='" + SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_deleteDlg']", SAVE_DELETE_LAYOUT_PREFIX),
    DELETE_LAYOUT_DIALOG_CHECKBOX("//input[@type='checkbox']"),
    DELETE_LAYOUT_DIALOG_COMBOBOX("//div[@id='widget_" + SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_deleteDialogList']", SAVE_DELETE_LAYOUT_PREFIX),
    DELETE_LAYOUT_DIALOG_SUBMIT(SAVE_DELETE_LAYOUT_PREFIX.name() + "_0_deleteDialogSubmit", SAVE_DELETE_LAYOUT_PREFIX),

    // constants (no selectors):
    GRID_GANTT_ZERO_WIDTH(0), //pcs
    GRID_GANTT_MILESTONE_WIDTH(14), //pcs
    GRID_DEFAULT_DURATION(1), //d
    GRID_DEFAULT_ALLOCATION(100F),
    GRID_DEFAULT_EFFORT(8F),
    POPUP_DEFAULT_ROWS_NUMBER(5),
    OPTIONS_BLOCK_DISPLAY_CHEKBOXES_NUMBER(25),
    DAYS_SUFFIX("d"),
    EXPECTED_TITLE_POPUP_UNDER_PREFIX("Add New Work Under: "),
    EXPECTED_TITLE_POPUP_AFTER_PREFIX("Add New Work After: "),
    //messages and errors:
    MESSAGE_CHANGE_OWNER(LOCATOR_REPLACE_PATTERN + " will be invited to become owner of " + LOCATOR_REPLACE_PATTERN_2 +
            " when changes are saved."),
    MESSAGE_ERROR_BULK_OPERATION_READ_ONLY_TEMPLATE(_8_0("Can not change read-only " + LOCATOR_REPLACE_PATTERN + " for: "),
            _9_0(LOCATOR_REPLACE_PATTERN + " could not be set for: ")),
    MESSAGE_ERROR_CONNECTION("The connection to the server has been lost. Try to reload page."),
    MESSAGE_BULK_OPERATION("Please select some rows"),
    MESSAGE_BULK_OPERATION_CAPTURE_BASELINES_TEMPLATE("You have overwritten existing baselines: "),
    _MESSAGE_ERROR_MOVE("Task(s) " + LOCATOR_REPLACE_PATTERN + " has a constraint or is linked to a task that cannot move."),
    MESSAGE_ERROR_MOVE(_8_0(_MESSAGE_ERROR_MOVE),
            _9_0("Task(s) " + LOCATOR_REPLACE_PATTERN + " have a constraint or is linked to a task that cannot move."),
            _9_4(_MESSAGE_ERROR_MOVE)),
    MESSAGE_ERROR_DEPENDENCY("Dependencies: " + LOCATOR_REPLACE_PATTERN),
    MESSAGE_ERROR_MOVE_DEPENDENCY_PATTERN(MESSAGE_ERROR_MOVE.name() + " " + MESSAGE_ERROR_DEPENDENCY.name(),
            MESSAGE_ERROR_MOVE, MESSAGE_ERROR_DEPENDENCY),;

    public static final LocatorsList MENU_POPUP_ROOT = new LocatorsList(
            MENU_POPUP_VIEW_LABEL,
            MENU_POPUP_ADD_UNDER_LABEL,
            MENU_POPUP_CAPTURE_BASELINES_LABEL,
            MENU_POPUP_INFORMATION_LABEL,
            getCurrentVersion() != null && getCurrentVersion().verLessThan(PowerSteeringVersions._8_2) ? null : MENU_POPUP_DELETE_LABEL); // due to 68645
    public static final LocatorsList MENU_POPUP_CHILD_1_1 = new LocatorsList(
            MENU_POPUP_VIEW_LABEL,
            MENU_POPUP_ADD_UNDER_LABEL,
            MENU_POPUP_ADD_AFTER_LABEL,
            MENU_POPUP_CAPTURE_BASELINES_LABEL,
            MENU_POPUP_DISPLAY_FROM_HERE_LABEL,
            MENU_POPUP_INFORMATION_LABEL,
            MENU_POPUP_EXCLUDE_FROM_TOTALS_LABEL,
            getCurrentVersion() != null && getCurrentVersion().verLessThan(PowerSteeringVersions._8_2) ? null : MENU_POPUP_DELETE_LABEL); // due to 68645
    public static final LocatorsList MENU_POPUP_CHILD_1_2 = new LocatorsList(
            MENU_POPUP_VIEW_LABEL,
            MENU_POPUP_ADD_UNDER_LABEL,
            MENU_POPUP_ADD_AFTER_LABEL,
            MENU_POPUP_INDENT_LABEL,
            MENU_POPUP_CAPTURE_BASELINES_LABEL,
            MENU_POPUP_DISPLAY_FROM_HERE_LABEL,
            MENU_POPUP_INFORMATION_LABEL,
            MENU_POPUP_EXCLUDE_FROM_TOTALS_LABEL,
            getCurrentVersion() != null && getCurrentVersion().verLessThan(PowerSteeringVersions._8_2) ? null : MENU_POPUP_DELETE_LABEL); // due to 68645
    public static final LocatorsList MENU_POPUP_FILTER = new LocatorsList(
            MENU_POPUP_VIEW_LABEL,
            MENU_POPUP_CAPTURE_BASELINES_LABEL,
            MENU_POPUP_DISPLAY_FROM_HERE_LABEL,
            MENU_POPUP_INFORMATION_LABEL,
            MENU_POPUP_EXCLUDE_FROM_TOTALS_LABEL,
            getCurrentVersion() != null && getCurrentVersion().verLessThan(PowerSteeringVersions._10_0) ? null : MENU_POPUP_DELETE_LABEL); // due to 85292
    public static final LocatorsList MENU_POPUP_CUSTOM_DISABLED = new LocatorsList(
            MENU_POPUP_INDENT_LABEL,
            MENU_POPUP_ADD_UNDER_LABEL,
            MENU_POPUP_ADD_AFTER_LABEL,
            MENU_POPUP_CAPTURE_BASELINES_LABEL,
            MENU_POPUP_DELETE_LABEL);


    VersionLocator[] locators;
    WBSEPageLocators[] replacements;

    WBSEPageLocators(Object loc) {
        locators = new VersionLocator[]{_8_0(loc)};
    }

    WBSEPageLocators(String loc, WBSEPageLocators... reps) {
        this(loc);
        replacements = reps;
    }

    WBSEPageLocators(VersionLocator... locs) {
        locators = locs;
    }

    public String getLocator() {
        if (replacements != null) { // parse composite locators
            return locators[0].get(replacements);
        }
        Object loc = chooseLocator(locators).get();
        if (loc instanceof String) {
            return (String) loc;
        } else if (loc instanceof ILocatorable) {
            return ((ILocatorable) loc).getLocator();
        }
        return null;
    }

    public int getInt() {
        Object loc = chooseLocator(locators).get();
        if (loc instanceof Integer)
            return (Integer) loc;
        return -1;
    }

    public float getFloat() {
        Object loc = chooseLocator(locators).get();
        if (loc instanceof Float)
            return (Float) loc;
        return -1;
    }


    public String replace(int num) {
        return replace(String.valueOf(num));
    }

    public String replace(String rep) {
        String loc = getLocator();
        if (loc == null) return null;
        return getLocator().replace(LOCATOR_REPLACE_PATTERN, rep);
    }


    public String replace(String rep1, String rep2) {
        String loc = getLocator();
        if (loc == null) return null;
        return getLocator().replace(LOCATOR_REPLACE_PATTERN, rep1).replace(LOCATOR_REPLACE_PATTERN_2, rep2);
    }

    public String replace(ILocatorable rep) {
        return replace(rep.getLocator());
    }


    public boolean contains(ILocatorable loc2) {
        String loc = getLocator();
        if (loc == null) return false;
        return loc.contains(loc2.getLocator());
    }

    public String toPattern() {
        String loc = getLocator();
        if (loc == null) return null;
        return loc.replace(LOCATOR_REPLACE_PATTERN, "[0-9]+").replace("(", "\\(").replace(")", "\\)");
    }

    public static class LocatorsList extends ArrayList<WBSEPageLocators> {

        public LocatorsList(WBSEPageLocators... locs) {
            for (WBSEPageLocators loc : locs) {
                if (loc != null)
                    add(loc);
            }
        }

        public List<String> get() {
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < super.size(); i++) {
                String sLoc = super.get(i).getLocator();
                if (sLoc != null) {
                    list.add(sLoc);
                }
            }
            return list;
        }

        public List<String> get(LocatorsList toExclude) {
            List<String> res = get();
            if (toExclude == null) return res;
            res.removeAll(toExclude.get());
            return res;
        }
    }

    public static List<String> getLocators(String prefix) {
        List<String> res = new ArrayList<String>();
        for (WBSEPageLocators loc : values()) {
            if (loc.name().startsWith(prefix.toUpperCase())) {
                if (loc.getLocator() != null)
                    res.add(loc.getLocator());
            }
        }
        return res;
    }

    private static List<String> getColumns(String category) {
        return getLocators("OPTIONS_BLOCK_COLUMNS_" + category.toUpperCase() + "_");
    }

    public static List<String> getAddSubWorkHeader(boolean panResource) {
        List res = getLocators("ADD_NEW_SUB_WORK_HEADER_");
        if (!panResource) {
            res.removeAll(getLocators("ADD_NEW_SUB_WORK_HEADER_DAE"));
            res.removeAll(getLocators("ADD_NEW_SUB_WORK_HEADER_SCHEDULED_TYPE"));
        }
        return res;
    }

    public static List<String> getInformationTable() {
        return getLocators("INFORMATION_DIALOG_POPUP_TH_");
    }

    public static List<String> getColumnsDates() {
        return getColumns("dates");
    }

    public static List<String> getColumnsProject() {
        return getColumns("project");
    }

    public static List<String> getColumnsResource() {
        return getColumns("resource");
    }

    public static List<String> getColumnsControls() {
        return getColumns("controls");
    }

}
