package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.04.2010
 * Time: 13:37:13
 * To change this template use File | Settings | File Templates.
 */
public enum CustomFields81PageLocators implements ILocatorable {

    CUSTOM_FIELDS_PAGE_ADMIN_AREA_FRAME_LOCATOR("jspFrame"),
    ADD_BUTTON_LINK("link=Add"),
    DELETE_BUTTON_LINK_LOCATOR("//a[@title='Delete Data Template']"),
    NAME_INPUT_FIELD("mockup.name"),
    DESCRIPTION_TEXTAREA("mockup.description"),

    CANCEL_BUTTON_LINK("link=Cancel"),
    SUBMIT_BUTTON_LINK("link=Submit"),

    EVENTS_CHECKBOX("object.type.ProjectEvent"),
    MSPCONTAINER_CHECKBOX("object.type.MSPContainer"),
    MILESTONE_CHECKBOX("object.type.Milestone"),
    TEMPLATE_CHECKBOX("object.type.Template"),
    FILE_FOLDER_CHECKBOX("object.type.FileFolder"),
    WORK_CHECKBOX("object.type.Work"),
    UNEXPANDED_WORK_CHECKBOX("object.type.UnexpandedWork"),
    TOLLGATE_CHECKBOX("object.type.Tollgate"),
    CHECKPOINT_CHECKBOX("object.type.Checkpoint"),
    USER_CHECKBOX("object.type.User"),
    GROUP_CHECKBOX("object.type.Group"),
    STATUS_REPORT_HANDLER_CHECKBOX("object.type.StatusReportHandler"),

    ADD_CUSTOM_DATA_FIELD_BUTTON_LINK("//a[contains(@href,'data_field_edit.jsp')]"),
    CUSTOM_DATA_FIELD_NAME_INPUT_FIELD("fieldName"),
    CUSTOM_DATA_FIELD_DESCRIPTION_TEXTAREA("fieldDescription"),
    CUSTOM_DATA_FIELD_REQUERED_RADIOBUTTON_NO("//input[@name='fieldRequired' and @value='false']"),
    CUSTOM_DATA_FIELD_ON_REPORT_RADIOBUTTON_NO("//input[@name='fieldOnReport' and @value='false']"),
    CUSTOM_DATA_FIELD_CHECKBOXES_DESCRIPTION_TEXTAREA("fieldCheck"),
    CUSTOM_DATA_FIELD_DATA_TYPE_SELECT("windowedControl"),
    CUSTOM_DATA_FIELD_DATA_TYPE_SELECT_VALUE_PREFIX("value="),
    CUSTOM_DATA_FIELD_SUBMIT_LINK_BUTTON("link=Submit");

    String locator;

    CustomFields81PageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

}
