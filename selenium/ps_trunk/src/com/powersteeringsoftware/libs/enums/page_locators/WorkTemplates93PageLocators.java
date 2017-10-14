package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.04.2010
 * Time: 15:50:55
 * To change this template use File | Settings | File Templates.
 */
public enum WorkTemplates93PageLocators implements ILocatorable {
    URL("/admin/WorkTemplates"),

    //WORK_TEMPLATES_PAGE_PARENT_FRAME_LOCATOR("relative=parent"),
    //WORK_TEMPLATES_PAGE_ADMIN_AREA_FRAME_LOCATOR("jspFrame"),
    CREATE_NEW_BUTTON_LINK("link=Create New"),
    NAME_INPUT_FIELD("objectName"),
    PROJECT_ID_INPUT_FIELD("peerId"),
    OBJECTIVE_TEXTAREA("objective"),
    SELECT_CHILDREN_INPUT_RADIOBUTTON_YES("SelectChildrenInput"),
    SELECT_CHILDREN_INPUT_RADIOBUTTON_NO("//input[@name='RequireDateEndDatesInput' and @value='false']"),
    REQUIRED_DATE_END_DATES_INPUT_RADIOBUTTON_YES("RequireDateEndDatesInput"),
    REQUIRED_DATE_END_DATES_INPUT_RADIOBUTTON_NO("//input[@name='RequireDateEndDatesInput' and @value='false']"),
    SUBMIT_LINK_BUTTON("//a[contains(text(),'Submit')]"),
    ERROR("//div[@class='bgError']/li"),

    RELATED_PROJECTS_CREATE_BUTTON_LINK("link=Create"),
    SELECT_WORK_TYPE_BASIC("//a[contains(@href, \"javascript:selectType('basic','" + LOCATOR_REPLACE_PATTERN + "')\")]"),
    SELECT_WORK_TYPE_GATED_PROJECT_ESG("Tollgate"),
    SELECT_WORK_TYPE_GATED_PROJECT_NOT_ESG("Tollgate000_"),
    SELECT_WORK_TYPE_WORK("Work"),
    SELECT_WORK_TYPE_FOLDER("FileFolder"),
    SELECT_WORK_TYPE_LABEL("//span[@class='titleBoldlower']"),
    SELECTION_PROCESSES_LABEL_LOCATOR("//input[@name='tag_sequence']//parent::td/parent::tr/td[2]"),
    SELECTION_PROCESSES_RADIOBUTTON_LOCATOR("//input[@name='tag_sequence']"),
    SELECTION_PROCESSES_RADIOBUTTONS(SELECTION_PROCESSES_RADIOBUTTON_LOCATOR.locator + "[" + LOCATOR_REPLACE_PATTERN + "]"),

    INHERIT_PERMISSIONS_RADIOBUTTON_YES("inheritPermissions"),
    INHERIT_PERMISSIONS_RADIOBUTTON_NO("//input[@name='inheritPermissions' and @value='false']"),
    INHERIT_CONTROLS_RADIOBUTTON_YES("inheritControls"),
    INHERIT_CONTROLS_RADIOBUTTON_NO("//input[@name='inheritControls' and @value='false']"),
    CONTROL_COST_RADIOBUTTON_YES("controlCostYes"),
    CONTROL_COST_RADIOBUTTON_NO("controlCostNo"),
    MANUAL_SHEDULING_RADIOBUTTON_YES("manualSchedulingYes"),
    MANUAL_SHEDULING_RADIOBUTTON_NO("manualSchedulingNo"),
    WEB_FOLDER_RADIOBUTTON_YES("webdav.is_resourceYes"),
    WEB_FOLDER_RADIOBUTTON_NO("webdav.is_resourceNo"),

    NAME_DATE_CONSTRAINT_TYPE("ps3.work.schedule.Constraint.type"),
    NAME_DATE_CONSTRAINT_END("ps3.work.schedule.Constraint.end"),
    NAME_DATE_CONSTRAINT_START("ps3.work.schedule.Constraint.start"),

    STATUS_REPORTING_CHOOSER("com.cinteractive.ps3.work.UpdateFrequency.isAvailable"),
    STATUS_REPORTING_SELECT("updateFrequency_options"),
    STATUS_REPORTING_NO_FREQUENCY("value=No Frequency"),
    STATUS_REPORTING_WEEKLY("value=Weekly"),
    STATUS_REPORTING_BIWEEKLY("value=Bi-Weekly"),
    STATUS_REPORTING_MONTHLY("value=Monthly"),
    STATUS_REPORTING_QUARTERLY("value=Quarterly"),

    TEMPLATE_DETAILS_LINK_IMG("//div/a/img"),
    TEMPLATE_DETAILS_LINK_PARENT_NAME("//span[@class='titleBoldLower']"),
    TEMPLATE_STRUCTURE_MESSAGE("//div[@class='titleLower']"),;

    String locator;

    WorkTemplates93PageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(toRep));
    }


}
