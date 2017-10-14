package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 23.07.2010
 * Time: 17:17:17
 */
public enum EditWorkPageLocators implements ILocatorable {
    URL("/project/EditWork.epage"),
    RADIO_BUTTON_NO("//input[contains(@id, 'radioNobooleanEdit')]"),
    RADIO_BUTTON_YES("//input[contains(@id, 'radioYesbooleanEdit')]"),
    RADIO_BUTTONS(RADIO_BUTTON_NO.locator + "/parent::div/parent::td/parent::tr"),
    RADIO_BUTTONS_LABEL("//th"),


    WORK_NAME("id=name"),
    WORK_OBJECTIVE("id=TextArea"),
    RESOURCE_PLANNING("Plan Resources:"),
    MANUAL_SCHEDULING("Manual Scheduling:"),
    MANUAL_SCHEDULING_93("Schedule type:"),
    MANUAL_SCHEDULING_LOWER_93("Manual schedule dependent works:"),
    INHERIT_PERMISSIONS("Inherit Permissions:"),
    INHERIT_CALENDAR("Inherit Calendar:"),
    INHERIT_COST_SETTING("Inherit cost setting:"),
    CONTROL_COST("Control Cost:"),
    CONTROL_COST_ENABLE("Enable"),
    CONTROL_COST_DISABLE("Disable"),
    CONTROL_COST_WARNING("controlCost" + LOCATOR_REPLACE_PATTERN + "Warning"),
    CONTROL_COST_WARNING_YES("controlCost" + LOCATOR_REPLACE_PATTERN + "WarningHide"),

    STATUS_REPORTING("selection_1"),
    STATUS_REPORTING_OFF("Status Reporting Off"),
    STATUS_REPORTING_UNSCHEDULED("Unscheduled"),
    STATUS_REPORTING_WEEKLY("Weekly"),
    STATUS_REPORTING_BI_WEEKLY("Bi-Weekly"),
    STATUS_REPORTING_MONTHLY("Monthly"),
    STATUS_REPORTING_QUARTERLY("Quarterly"),


    CONSTRAINT("constraintSelection"),
    CONSTRAINT_ASAP("As Soon As Possible"),
    CONSTRAINT_SNET("Start No Earlier Than"),
    CONSTRAINT_FNLT("Finish No Later Than"),
    CONSTRAINT_FD("Fixed Dates"),
    CONSTRAINT_ALAP("As Late As Possible"),
    CONSTRAINT_MSO("Must Start On"),
    CONSTRAINT_MFO("Must Finish On"),
    CONSTRAINT_SNLT("Start No Later Than"),
    CONSTRAINT_FNET("Finish No Earlier Than"),

    CONSTRAINT_START("//div[@id='widget_plannedStart']"),
    CONSTRAINT_END("//div[@id='widget_plannedEnd']"),

    SAVE("//input[@value='Save Changes']"),
    CANCEL("//input[contains(@onclick, 'form.events.cancel')]"),
    SAVE_DIALOG("//div[@id='propagatePlanResourcesDialog']"),
    SAVE_DIALOG_YES("//input[@value='Yes']"),

    ASSIGN_USER_SUFFIX("EDITWORK"),

    STATUS("EditWorkStatus"),
    PRIORITY("prioritySelect"),;

    private String locator;

    EditWorkPageLocators(String s) {
        locator = s;
    }

    EditWorkPageLocators(EditWorkPageLocators pattern, EditWorkPageLocators s) {
        this(pattern.replace(s));
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        if (o instanceof ILocatorable) o = ((ILocatorable) o).getLocator();
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
