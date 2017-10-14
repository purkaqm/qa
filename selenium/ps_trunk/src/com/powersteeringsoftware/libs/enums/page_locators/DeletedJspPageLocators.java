package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 05.06.13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public enum DeletedJspPageLocators implements ILocatorable {
    URL_DOCUMENTS("admin/deleted_documents.jsp"),
    URL_WORKS("admin/deleted_work.jsp"),
    CHECKBOX("//input[@type='checkbox']"),
    CHECKBOX_PARENT("div"),
    CHECKBOX_PARENT_DELETED_SRYLE("line-through"),
    CHECKBOX_LABEL("//span[@class='deletedItem']"),
    DELETE_SELECTED("//input[@value='Permanently delete checked items']"),
    DELETE_ALL("//input[@value='Permanently delete all items']"),
    DIALOG("//td[@class='progressContainer']"),
    DIALOG_OK("progressBarOkBtn"),;
    private String loc;

    DeletedJspPageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
