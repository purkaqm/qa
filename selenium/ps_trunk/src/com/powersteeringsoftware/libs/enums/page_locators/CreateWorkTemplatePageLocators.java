package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 07.10.12
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public enum CreateWorkTemplatePageLocators implements ILocatorable {
    NAME("name"),
    DESCRIPTION("description"),
    ROOT("rootWorkName"),
    TYPE("workTypeSelector"),
    TYPE_WORK("Work"),
    TYPE_GATED_PROJECT_ESG("Gated Project"),
    TYPE_GATED_PROJECT_NOT_ESG("Gated Project None Sequetial"), // from DB
    TYPE_FOLDER("Folder"),

    PROCESS_LIST("processesList"),

    PROCESS_LABEL_FOR("for"),
    PROCESS_LABEL("//label[@" + PROCESS_LABEL_FOR.loc + "]"),

    SUBMIT("//input[@value='Create']"),
    CANCEL("//input[@value='Cancel']");
    private String loc;

    CreateWorkTemplatePageLocators(String s) {
        loc = s;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
