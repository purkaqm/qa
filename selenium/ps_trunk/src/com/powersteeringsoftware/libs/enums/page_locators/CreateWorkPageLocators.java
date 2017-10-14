package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.05.2010
 * Time: 17:36:32
 * To change this template use File | Settings | File Templates.
 */
public enum CreateWorkPageLocators implements ILocatorable {
    URL("/project/CreateWork"),
    WORK_TYPE_SELECT("workType"),
    WORK_TYPE_SELECT_FOLDER_OPTION("Folder"),
    WORK_TYPE_SELECT_MILESTONE_OPTION("Milestone"),
    WORK_TYPE_SELECT_MSP_PROJECT_OPTION("MSP Project"),
    WORK_TYPE_SELECT_WORK_OPTION("Work"),
    FIRST_STEP_CONTINUE_BUTTON("next"),

    NAME_INPUT_FIELD("name"),
    START_DATE("//div[@id='widget_plannedStart']"),
    END_DATE("//div[@id='widget_plannedEnd']"),
    GATE_START_DP("//div[contains(@id, 'gatePlannedStart')]"),
    GATE_END_DP("//div[contains(@id, 'gatePlannedEnd')]"),
    GATE_DATE_LABEL("//label"),
    LOCATION_FIELD("popup_parentShow"),
    LOCATION_POPUP("//div[@id='popup_parent']"),
    SECOND_STEP_FINISH_BUTTON("finish"),

    ASSIGN_USER_SUFFIX("CREATEWORK"),

    STATUS("status"),
    PRIORITY("prioritySelect"),;

    String locator;

    CreateWorkPageLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String append(ILocatorable loc) {
        return append(loc.getLocator());
    }

    public String append(String loc) {
        return locator + loc;
    }

    public String replace(ILocatorable loc) {
        return replace(loc.getLocator());
    }

    public String replace(String loc) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, loc);
    }


}
