package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.02.11
 * Time: 19:35
 */
public enum ResourceReviewPageLocators implements ILocatorable {
    CONTAINER_HEADER("//div[@id='sub']//span"),

    TREE_ITEM_PARENT("//div[contains(@class, 'cell') and contains(@class, 'top_level')]/nobr"),
    TREE_ITEM("/div/div[contains(@class, 'cell')]/nobr"),
    TREE_ITEM_CONTENT("//div[@class='projectDiv']"),
    TREE_ITEM_CHILDREN_BOX("/parent::div/parent::div//div[@class='page']"),

    GRID("//div[@class='grid']"),
    GRID_TREE_DEMAND("//div[contains(@class, 'WorkTreeSection') and @id='RRDemandMasterSection']//div[contains(@class, 'workTree')]"),
    GRID_TREE_ALLOCATION("//div[contains(@class, 'WorkTreeSection') and @id='RRAllocationMasterSection']//div[contains(@class, 'workTree')]"),
    GRID_BODY(GRID.locator + "/div[@class='body']"),
    GRID_DEMAND("//div[@class='body']//div[@id='RRDemandSection']"),
    GRID_ALLOCATION("//div[@class='body']//div[@id='RRAllocationSection']"),
    GRID_HEADER("//th[@class='RRAllocationSection_rc']"),
    GRID_COLUMN("//div[contains(@class, 'column') and @columnid='" + LOCATOR_REPLACE_PATTERN + "']"),
    GRID_CELL("//div[contains(@class,'cellContent')]"),
    GRID_SCROLL_BAR("css=div.horizontalScrollArea>div[sectionid=\"RRAllocationSection\"]"),
    GRID_NUMBER_FORMAT_GROUPING_USED("true"),
    GRID_NUMBER_FORMAT_MAX_FRACTION_DIGITS(0),
    GRID_NUMBER_FORMAT_MIN_FRACTION_DIGITS(0),

    DISPLAY_NAME("Display"),
    LAYOUTS_NAME("Layout"),
    FILTER_NAME("Filter"),

    FILTER("//tr[@class='filtersColumn']"),
    FILTER_ROLE_EMPTY_VALUE("value=0"),
    FILTER_ROLE_ANY_VALUE("value=1"),
    FILTER_ROLE_SPECIFIED_VALUE("value=2"),
    FILTER_DEMAND_ROLE("roleOptions"),
    FILTER_DEMAND_ROLE_TAG_CHOOSER("//div[@id='dmnRoleListWrap']/div"),
    FILTER_ALLOCATION_ROLE("alcRoleOptions"),
    FILTER_ALLOCATION_ROLE_TAG_CHOOSER("//div[@id='alcRoleListWrap']/div"),

    DISPLAY("//tr[@class='filtersColumn']"),
    DISPLAY_PERIOD("//div[@id='widget_rollingDatePicker_period']"),
    DISPLAY_PERIOD_WEEKLY("Weekly"),
    DISPLAY_RANGE_ROLLING("rollingDatePicker_rollingRadio"),
    DISPLAY_RANGE_DATE("rollingDatePicker_dateRangeRadio"),
    DISPLAY_RANGE_BEGIN("//div[@id='widget_rollingDatePicker_beginRangeDate']"),
    DISPLAY_RANGE_END("//div[@id='widget_rollingDatePicker_endRangeDate']"),;


    ResourceReviewPageLocators(String s) {
        locator = s;
    }


    ResourceReviewPageLocators(int i) {
        value = i;
    }

    private Integer value;
    private String locator;


    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    public String replace(long to) {
        return replace(String.valueOf(to));
    }

    public int getInt() {
        return value;
    }

}
