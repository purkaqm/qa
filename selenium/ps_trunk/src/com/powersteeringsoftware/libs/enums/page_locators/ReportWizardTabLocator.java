package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

public enum ReportWizardTabLocator implements ILocatorable {

    TYPE_TAB_CATEGORY_SELECTOR("id=viewGroup"),
    TYPE_TAB_TYPE_SELECTOR("id=view"),
    TYPE_TAB_DESCRIPTION("id=wizardViewDescriptionDivId"),
    TYPE_TAB_EXAMPLE("id=wizardViewExampleDivId"),

    DEF_TAB_PORTFOLIO_SELECTOR("id=Select"),
    DEF_TAB_MORE_FILTER_SELECTOR("id=moreFilters"),
    DEF_TAB_ADD_MORE_FILTER_BUTTON("//input[@type='image' and @title='Add']"),
    DEF_TAB_RANGE_TYPE_SELECTOR("id=dateRangeTypeSelection"),
    DEF_TAB_FILTER_ELEMENT("//th[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    DEF_TAB_WORK_TYPE_LABEL_BY_LABEL("//label[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    DEF_TAB_WORK_TYPE_LABEL_BY_ACRONYM("//label/acronym[@title='" + LOCATOR_REPLACE_PATTERN + "']/parent::*"),
    DEF_TAB_WORK_TYPE_LABEL_BY_HOVER("//div[@class='hoverPopup' and text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    DEF_TAB_WORKSTATUS_SELECTOR("//td[text()='Status changed to']/following-sibling::td/select"),
    DEF_TAB_METRICFILTER_SELECTOR("//th[text()='Has metric']/following-sibling::td/select"),
    DEF_TAB_WORKCHOOSER_ELEMENT("//div[@class='MultiSelect' and @linksboxid]"),
    DEF_TAB_RELATED_WORKCHOOSER_ELEMENT("//th[text()='Related Work item']/following-sibling::td/div/div/div/div[@class='MultiSelect']"),

    COL_TAB_COLUMNGROUP_SELECTOR("id=addables"),
    COL_TAB_ADD_BUTTON("//select[@id='addables']/following-sibling::input[@title='Add']"),
    COL_TAB_CUSTOM_COLUMN_TYPE_SELECTOR("id=customColumns"),
    COL_TAB_ADD_CUSTOM_BUTTON("//select[@id='customColumns']/following-sibling::input[@title='Add']"),
    COL_TAB_CUSTOM_COLUMN_ELEMENT("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    COL_TAB_GROUP_ELEMENT("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    COL_TAB_CUSTOMCOLUMN_CHBOX("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']/input[@type='checkbox']"),
    COL_TAB_CUSTOMCOLUMN_EDIT_BUTTON("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']/input[@class='btn' and @value='edit']"),
    COL_TAB_CUSTOMCOLUMN_REMOVE_BUTTON("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']/input[@class='btn' and @value='remove']"),
    COL_TAB_GROUP_LINK("//a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    COL_TAB_COLUMN_CHBOX_BY_LABEL("//label[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    COL_TAB_COLUMN_CHBOX_BY_ACRONYM("//label/acronym[@title='" + LOCATOR_REPLACE_PATTERN + "']/parent::*"),
    COL_TAB_COLUMN_CHBOX_BY_ACRONYM2("//acronym[text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::label"),
    COL_TAB_COLUMN_CHBOX_BY_HOVER("//*[contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]"),

    FILTER_TAB_ADDITIONAL_FILTERS_ELEMENT("//div[@id='additional_filters']"),
    FILTER_TAB_COLUMNGROUP_ELEMENT("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    FILTER_TAB_COLUMNFILTER_CONTROL_ELEMENT("//th[text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::tr"),
    FILTER_TAB_ADDMORE_FILTER_LINK("//a[text()='Add more filters']"),
    FILTER_TAB_COLUMNGROUP_LINK("//a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    FILTER_TAB_ADDITIONALFILTERS_LINK("//a[text()='Additional Filters']"),
    FILTER_TAB_PREVIOUSLY_CHOSEN_COLUMNS_ELEMENT("//h4[text()='Which of your report columns would you like to filter on?']/following-sibling::div[2]"),
    FILTER_TAB_AF_COLUMNGROUP_ELEMENT("//div[@id='additional_filters']/div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    FILTER_TAB_COLUMN_LABEL("//td/label"),

    SORT_TAB_COLUMN_ELEMENT("//li[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    SORT_TAB_ADDSORT_ORDER_SELECTOR("//li[text()='" + LOCATOR_REPLACE_PATTERN + "']/select"),
    SORT_TAB_ADDSORT_COLUMN_SELECTOR("//select[1]"),
    SORT_TAB_ADDSORT_ADD_BUTTON("//input[@type='image' and @title='Add']"),
    SORT_TAB_GROUPBY_COLUMN_SELECTOR("//div[" + LOCATOR_REPLACE_PATTERN + "]/select[1]"),
    SORT_TAB_GROUPBY_ORDER_SELECTOR("//div[" + LOCATOR_REPLACE_PATTERN + "]/select[2]"),
    SORT_TAB_GROUPBY_CUSTOM_CHECKBOX("//div[" + LOCATOR_REPLACE_PATTERN + "]/input[@type='checkbox']"),
    SORT_TAB_GRPUPBY_ELEMENT("//div[text()='Group by']/parent::div"),
    SORT_TAB_ADDITIONALSORT_ELEMENT("//tr[@class='psGrid']/td[1]/div[2]"),

    SUM_TAB_COLUMN_ROW_ELMENT("//table/tbody/tr/td[1][text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::tr"),
    SUM_TAB_SUMMARY_PARAMETER_CONTROL("//td[" + LOCATOR_REPLACE_PATTERN + "]/input[@type='radio']"),

    LAYOUT_TAB_PAPERSIZE_SELECTOR("id=paperSize"),
    LAYOUT_TAB_TEMPLATE_SELECTOR("id=templateId"),
    LAYOUT_TAB_COLUMN_ROW_ELEMENT("//table[@id='PSDndSource']/tbody/tr/td[3][text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::tr"),
    LAYOUT_TAB_COLUMN_TITLE_INPUT("//td[4]/input[@type='text']"),

    CHART_TAB_NOCHART_RBUTTON("id=none"),
    CHART_TAB_BAR_RBUTTON("id=bar"),
    CHART_TAB_LINE_RBUTTON("id=line"),
    CHART_TAB_PIE_RBUTTON("id=pie"),
    CHART_TAB_AXIS_SELECTOR("id=xAxis"),
    CHART_TAB_BASE_SERIES_SELECTOR("id=yAxis"),
    CHART_TAB_ADDSERIES_LINK("//a[text()='Add data series']"),
    CHART_TAB_POSITION_SELECTOR("id=chartPos"),
    CHART_TAB_SIZE_SELECTOR("id=chartSize"),
    CHART_TAB_TEXT_FONT_SELECTOR("id=textSize"),
    CHART_TAB_LEGEND_POSITION("id=legendPosition"),
    CHART_TAB_LABEL_POSITION("id=labelPosition"),
    CHART_TAB_BACKGROUNDFADE_SELECTOR("//td/select"),
    CHART_TAB_FROM_COLOR_ELEMENT("//td/div[1]"),
    CHART_TAB_TO_COLOR_ELEMENT("//td/div[2]"),
    CHART_TAB_BACKGROUNDFADE_ELEMENT("//table/tbody/tr[last()]"),
    CHART_TAB_ADDITIONAL_SERIES_SELECTOR("dataSeries"),
    CHART_TAB_ADDITIONAL_SERIES_ASLINE_CHBOX("asLine"),
    CHART_TAB_ADDITIONAL_SERIES_NEWSCALE_CHBOX("newScale"),

    SAVE_TAB_REPORTNAME_INPUT("id=reportName"),
    SAVE_TAB_REPORTDESCRIPTION_AREA("id=TextArea"),
    SAVE_TAB_LOCATION_SELECTOR("id=folder"),
    MY_REPORTS_TAB("My Reports"),
    PUBLIC_REPORTS_TAB("Public Reports"),;

    ReportWizardTabLocator(String locator) {
        this.locator = locator;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    private String locator;
} // enum ReportWizardTabLocator 
