package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 03.10.11
 * Time: 14:23
 * To change this template use File | Settings | File Templates.
 */
public enum MetricTemplatesPageLocators implements ILocatorable {
    CREATE_NEW("link=Create New"),
    FRAME("jspFrame"),

    START("id=START"),
    END("id=END"),
    BREAKDOWN_TAG("name=breakdownId"),
    NAME("name=objectName"),
    DESCRIPTION("name=description"),
    CALENDAR("id=windowedControl"),
    FREQUENCY("name=Period"),
    FREQUENCY_MONTHLY("Monthly"),
    FREQUENCY_QUARTERLY("Quarterly"),
    FREQUENCY_YEARLY("Yearly"),
    FREQUENCY_NO("No Frequency"),
    HAS_VIEW_YES("//input[@name='Has Views' and @value='true']"),
    HAS_VIEW_NO("//input[@name='Has Views' and @value='false']"),
    PERCENTAGE_ALLOCATION_YES("//input[@name='percent_allocation' and @value='true']"),
    PERCENTAGE_ALLOCATION_NO("//input[@name='percent_allocation' and @value='false']"),

    NEXT("link=Next"),

    CURRENT_FINANCIAL_YEAR("//input[@name='fiscal_year' and @value='true']"),
    DISPLAY_TOTAL_SELECTOR("//select[contains(@name, 'Default Total Frequency')]"),
    DISPLAY_TOTAL_SELECTOR_NEVER("Never"),
    DISPLAY_TOTAL_SELECTOR_QUARTERLY("Quarterly"),
    DISPLAY_TOTAL_SELECTOR_YEARLY("Yearly"),
    DISPLAY_TOTAL_SELECTOR_INFINITELY("Infinitely"),
    DISPLAY_TOTAL_SELECTOR_RANGE("DisplayRange"),

    CHECKBOX_WORK_ITEMS("com.cinteractive.ps3.metrics.MetricTemplate.associations.Work"),
    CHECKBOX_MSP_PROJECTS("com.cinteractive.ps3.metrics.MetricTemplate.associations.MSPContainer"),
    CHECKBOX_GP("com.cinteractive.ps3.metrics.MetricTemplate.associations.Tollgate"),
    CHECKBOX_GP_NOT_ESG("com.cinteractive.ps3.metrics.MetricTemplate.associations.Tollgate000_"),

    VIEW_NAME("//input[contains(@name, 'tagName.')]"),
    VIEW_SEQ("//input[contains(@name, 'tagSeq.')]"),
    VIEW_COST_MAPPING("//select[contains(@name, 'tagCostMap.')]"),
    VIEWS_MORE("//a[contains(@href, 'addLines')]"),

    ITEM_SEQ("//input[contains(@name, 'Sequence.')]"),
    ITEM_NAME("//input[contains(@name, 'ItemName.')]"),
    ITEM_TYPE("//select[contains(@name, 'datatype.')]"),
    ITEM_DESCRIPTION("//input[contains(@name, 'Description.')]"),
    ITEMS_MORE("//a[contains(@href, 'update')]"),

    CONSTRAINT_VALUES_COST_MAPPING("//select[contains(@name, '.viewCostMapping')]"),

    FORMULA_ITEMS("//td/span[@class='titleBoldLower']/a"),
    FORMULA_TEXT_MODE("//a[contains(text(), 'Text Mode')]"),
    //FORMULA_ITEM("//table[@class='bgDark']//a[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]"),
    FORMULA_ITEM("link=" + LOCATOR_REPLACE_PATTERN),
    FORMULA_TEXT("//textarea"),
    FORMULA_SAVE("//a[contains(@href, 'update')]"),

    SUBMIT("id=css-button-1"),;

    String loc;

    MetricTemplatesPageLocators(String l) {
        loc = l;
    }

    @Override
    public String getLocator() {
        return loc;
    }

    public String replace(Object o) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
