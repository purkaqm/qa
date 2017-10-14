package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.VersionLocator;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;

import static com.powersteeringsoftware.libs.enums.VersionLocator.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 06.09.11
 * Time: 17:12
 */
public enum GridLocators implements ILocatorable {

    PROBLEM_DETECTED_DIALOG("//div[contains(@class, 'psDialog')]"),
    PROBLEM_DETECTED_DIALOG_TITLE("//div[@class='dijitDialogTitleCnt']"),
    PROBLEM_DETECTED_DIALOG_TITLE_TXT("Problem detected"),
    PROBLEM_DETECTED_DIALOG_DETAIL_LINK("//*[text()='Details']"),
    PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT("//div[@class='dijitDialogPaneContent']//div[@class='shaded']"),
    PROBLEM_DETECTED_DIALOG_DETAIL_CONTENTS("/*"),
    PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT_BLOCK("div"),
    PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT_BLOCK_SEPARATOR("\\s+at\\s+"),

    LOADING_ELEMENT_LOCATOR("//div[@class='loading']"),
    LOADING_HIDDEN("hidden"),
    LOADING_PLEASE_WAIT("css=div.waitingUnderlay"),
    LOADING_UNDERLAY("css=div.underlay"),
    LOADING_BOX("css=div.box"),
    LOADING_UNDERLAY_IMG("//img[contains(@src, 'loading_cycle.gif')]"),

    TREE(_8_0("//div[contains(@class, 'column workTree')]"), _13("//div[contains(@class, 'column treecol workTree')]")),
    TREE_ITEM("//div[@class='cellContent']"), // < summary for 9.3
    //TREE_ITEM(_8_0("//div[@class='projectDiv']"), _9_1("//div[@class='projectDiv' or @class='subRowTitle']")),
    TREE_ITEM_PARENT_DIV("div"),
    TREE_ITEM_PARENT_DIV_CLASS("treeChildrenContent"),
    TREE_ITEM_PARENT_DIV_LOC("//div[contains(@class, '" + TREE_ITEM_PARENT_DIV_CLASS.name() + "')]", TREE_ITEM_PARENT_DIV_CLASS),
    TREE_ITEM_PARENT_DIV_CLASS_HIDDEN("hidden"),
    TREE_ITEM_LINK("/a"),
    TREE_ITEM_NOBR("/nobr"),
    TREE_ITEM_IMG("//div[@class='projectDiv']/img"),
    TREE_ITEM_IMG_SRC("src"),
    TREE_ITEM_IMG_SRC_GATED("checkpt.gif"),
    TREE_ITEM_IMG_SRC_TOLLGATE("icon_tollgate.gif"),
    TREE_ITEM_WORK_LINK("//a"),
    TREE_ITEM_CLOSED("//div[contains(@class, 'dijitTreeExpandoClosed')]"),
    TREE_ITEM_OPENED("//div[contains(@class, 'dijitTreeExpandoOpened')]"),
    TREE_ITEM_FIRST_CLASS("withoutLeftPadding"),

    GRID("//div[@class='grid']"),
    GRID_BODY("/div[@class='body']"),
    GRID_SECTION_ID(_8_0("id"), _9_2("sectionid")),
    GRID_SCROLL_BAR("css=div.horizontalScrollArea>div[sectionid=\"WBSSection\"]"),
    GRID_TABLE_DIVIDER("//div[@class='divider']"),
    GRID_TABLE_RESIZING_UNDERLAY("//div[@id='ps_ui_DialogUnderlay_0']/div"),
    GRID_TABLE_FIRST_COLUMN("//div[@" + GRID_SECTION_ID.name() + "='WBSMasterSection']", GRID_SECTION_ID),
    GRID_TABLE_HEADER("//div[@" + GRID_SECTION_ID.name() + "='WBSSection']", GRID_SECTION_ID),
    GRID_TABLE_HEADER_PARENT(GRID_TABLE_HEADER.name() + "//th[not(contains(@class, 'hidden'))]", GRID_TABLE_HEADER),
    GRID_TABLE_HEADER_CELL(GRID_TABLE_HEADER_PARENT.name() + "/div", GRID_TABLE_HEADER_PARENT),
    GRID_TABLE_HEADER_CELL_TEXT_91("/nobr"),
    GRID_TABLE_HEADER_RESIZING_CLASS("resizingArea"),
    GRID_TABLE_HEADER_COLUMN_ID(_8_0("id"), _9_2("columnid")),
    GRID_TABLE_COLUMN("//div[@" + GRID_SECTION_ID.name() + "='WBSSection']//div[@columnid='" + LOCATOR_REPLACE_PATTERN + "']", GRID_SECTION_ID),
    GRID_TABLE_CELL_GENERAL("//div[contains(@class, 'cell')]"),
    GRID_TABLE_CELL_CONTENT("//div[contains(@class, 'cellContent')]"),
    GRID_TABLE_CELL_FIRST(GRID_TABLE_CELL_GENERAL.name(), GRID_TABLE_CELL_GENERAL),
    GRID_TABLE_CELL_TREE(TREE.name() + GRID_TABLE_CELL_GENERAL.name(), TREE, GRID_TABLE_CELL_GENERAL),
    GRID_TABLE_CELL_UNEDITABLE_CLASS_ATTR("uneditable"),
    GRID_TABLE_CELL_EDITED_CLASS_ATTR("edited"),
    GRID_TABLE_CELL_FIRST_CHILD("/div"),
    GRID_TABLE_TREE_NAME("Name"),
    GRID_TABLE_STATUS("Status"),
    GRID_TABLE_OWNER("Owner"),
    GRID_TABLE_NUMBER_INPUT("//input[contains(@id,'ps_form_NumberTextBox_')]"),
    GRID_TABLE_DATE_PICKER_INPUT("//input[contains(@id, 'ps_form_DatePicker_')]"),
    GRID_TABLE_STATUS_POPUP("//div[@columnid='STATUS']//div[contains(@class, 'cellContent')]"),
    GRID_TABLE_STATUS_POPUP_CHILD("//div[@class='treeChildrenContent']//div[" + LOCATOR_REPLACE_PATTERN + "]//div[contains(@class, 'cellContent')]"),

    GRID_TABLE_NAME_MIN_WIDTH(_8_0(60), _9_0(100)),
    GRID_TABLE_HEADER_MIN_WIDTH(_8_0(30), _9_2(20)),


    //grid buttons:
    APPLY_CHANGES_AREA("//div[@class='applyChangeArea']"),
    AREA_RESET_BUTTON_VALUE(_8_0("Reset"), _12_0("Cancel")),
    AREA_RESET_BUTTON(APPLY_CHANGES_AREA.name() + "/input[@value='" + AREA_RESET_BUTTON_VALUE.name() + "']", APPLY_CHANGES_AREA, AREA_RESET_BUTTON_VALUE),
    AREA_SAVE_BUTTON(APPLY_CHANGES_AREA.name() + "/input[@value='Save']", APPLY_CHANGES_AREA),
    AREA_SAVE_BUTTON_DISABLED("btn-disabled"),;

    private static PowerSteeringVersions current;
    VersionLocator locator;
    GridLocators[] replacements;

    GridLocators(String loc, GridLocators... reps) {
        this(loc);
        replacements = reps;
    }

    GridLocators(Object loc) {
        locator = _8_0(loc);
    }

    GridLocators(VersionLocator... locs) {
        locator = chooseLocator(locs);
    }

    @Override
    public String getLocator() {
        return locator.get(replacements);
    }

    public String replace(Object rep) {
        String loc = getLocator();
        if (loc == null) return null;
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }


}
