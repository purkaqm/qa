package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 19.12.12
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */
public enum Processes94PageLocators implements ILocatorable {
    TD_NAME("//td[@class='nameColumnValue']"),
    ADD("//input[@value='Add new process']"),
    DIALOG("ps_ui_DialogBase_0"),
    NAME("ps_form_TextBox_0"),
    DESCRIPTION("//textarea"),
    PHASE_NAME("//td[@type='string']"),
    PHASE_NUMBER("//td[@type='number']"),
    UPDATE("//input[@value='Update']"),
    INPUT("//input[@type='text']");

    String loc;

    Processes94PageLocators(String loc) {
        this.loc = loc;
    }

    @Override
    public String getLocator() {
        return loc;
    }
}
