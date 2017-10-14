package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 28.04.2010
 * Time: 18:45:04
 * To change this template use File | Settings | File Templates.
 */
public enum Processes93PageLocators implements ILocatorable {

    PROCESSES_PAGE_ADMIN_AREA_FRAME_LOCATOR("jspFrame"),
    ADD_BUTTON_LINK("link=Add"),
    NAME_INPUT_FIELD("tagname"),
    DESCRIPTION_TEXTAREA("tagdesc"),
    PHASES_SELECT("windowedControl"),
    PHASES_SELECT_VALUE("value=" + LOCATOR_REPLACE_PATTERN),
    PHASE_NAME_INPUT_FIELD_PREFIX("textfield" + LOCATOR_REPLACE_PATTERN),
    PHASE_PERCENTAGE_INPUT_FIELD_PREFIX("pcntcompleted" + LOCATOR_REPLACE_PATTERN),
    CANCEL_BUTTON_LINK("link=Cancel"),
    SUBMIT_BUTTON_LINK("link=Submit"),

    PARTICULAR_PROCESS_LINK("link=" + LOCATOR_REPLACE_PATTERN),
    BACK_TO_MAIN_PAGE_LINK("//img[@alt='Back']"),;

    String locator;

    Processes93PageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    public String replace(int rep) {
        return replace(String.valueOf(rep));
    }


}
