package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 29.04.2010
 * Time: 19:30:00
 * To change this template use File | Settings | File Templates.
 */
public enum TagsSetEPageLocators implements ILocatorable {
    URL("/admin/tags/TagSets.epage"),
    URL2("/admin/tags/TagSets.page"),
    TABLE("//table[@id='PSTable']"),
    TABLE_ROW("//tr"),
    TABLE_ROW_IMG_TITLE("title"),
    TABLE_ROW_IMG("//img[@" + TABLE_ROW_IMG_TITLE.locator + "]"),
    TABLE_ROW_NAME_LINK("//td[@class='nameColumnValue']/a"),
    TABLE_ROW_DELETE_IMG("//td[@class='deleteTagSetColumnValue last']//img"),
    TABLE_ROW_TAG_LINK("link=" + LOCATOR_REPLACE_PATTERN),

    DELETE_POPUP_DIALOG("//div[@id='deleteTagDialog']"),
    DELETE_POPUP_DIALOG_YES("//input[@value='Yes']"),
    DELETE_POPUP_DIALOG_NO("//input[@value='No']"),
    ADD_TAG_SET_DIALOG("AddTagSet"),
    BUTTON_ADD_NEW_TAG("AddTagSetShow"),;
    String locator;

    TagsSetEPageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, toRep);
    }

    public enum AddTagSetDialogLocators implements ILocatorable {
        BUTTON_ADD_SUBMIT("//input[@value='Add Tag']"),
        BUTTON_EDIT_SUBMIT("//input[@value='Update Details']"),
        FIELD_NAME("tagSetName"),
        FIELD_DESCRIPTION("tagSetDescription"),
        FIELD_IS_HIERARCHICAL("tagsSetHierarchical"),
        FIELD_IS_APPLY_USERS_PERMISSIONS("tagsSetPermissions"),
        FIELD_IS_ENABLE_ALERTS_AND_EVENT_LOGGING("tagsSetAlertable"),
        FIELD_IS_LOCKED("tagsSetLock"),
        FIELD_IS_MULTIPLE("tagsSetMultiple"),
        FIELD_IS_MANDANATORY("tagsSetMandatory"),


        FIELD_MESSAGES("//div[@id='messageTypesDisp']"),
        FIELD_DOCUMENTS("//div[@id='documentTypesDisp']"),
        FIELD_PEOPLES("//div[@id='peopleTypesDisp']"),
        FIELD_WORKS("//div[@id='workTypesDisp']"),

        ASSOCIATE_WITH_MESSAGES_DISCUSSION_ITEMS("Discussion Items"),;


        String locator;

        AddTagSetDialogLocators(String _locator) {
            locator = _locator;
        }

        public String getLocator() {
            return locator;
        }
    }
}
