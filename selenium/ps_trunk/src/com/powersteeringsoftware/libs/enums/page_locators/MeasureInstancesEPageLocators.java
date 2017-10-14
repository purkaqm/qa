package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 25.05.2010
 * Time: 13:09:02
 * To change this template use File | Settings | File Templates.
 */
public enum MeasureInstancesEPageLocators implements ILocatorable {
    URL("/measures/MeasureInstances"),
    ITEM_ARROW("//a[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]/parent::td//img"),
    MENU_POPUP("//div[@class='dijitPopup']"),
    ATTACH_NEW_BUTTON("//input[@value='Attach New']"),
    DEFINE_NEW_BUTTON("//input[@value='Define New']"),
    MENU_POPUP_DELETE_ITEM("ps_widget_psMenuItem_3", "ps_ui_MenuItem_3"),
    MENU_POPUP_EDIT_ITEM("ps_widget_psMenuItem_1", "ps_ui_MenuItem_1"),
    DELETE_DETACH_DIALOG("//div[@class='dijitDialog psDialog']"),
    DELETE_DIALOG_DETACH_RADIOBUTTON("//input[@name='RadioGroup' and @value='0']"),
    DELETE_DIALOG_DELETE_RADIOBUTTON("//input[@name='RadioGroup' and @value='1']"),
    DELETE_DIALOG_COMMIT_BUTTON("//input[@value='Commit']"),
    REMOVE_YES_BUTTON("//a[@id='yesButtonAddHoc']/input[@value='Yes']"),
    DETACH_YES_BUTTON("//a[@id='yesButtonAttached']/input[@value='Yes']"),
    REMOVE_TITLE("Delete"),
    DETACH_TITLE("Detach"),
    ATTACH_BUTTON("//a[@title='" + LOCATOR_REPLACE_PATTERN + "']/parent::td/parent::tr//input[contains(@id,'ImageSubmit')]"),

    TABLE_COUNT("//span[@class='paginationMsg']"),
    TITLE_CELLS("//td[@class='titleColumnValue']"),
    TITLE_CELL_LINK("//a"),
    TITLE_CELL_IMG("//img"),
    ROW("//tr"),
    NEXT("id=linkFwd"),
    ATTACH_CELL_IMG("//input[contains(@id, 'ImageSubmit')]"),

    MENU_VIEW_PROPERTIES("View properties"),
    MENU_VIEW_MEASURE("View measure"),
    MENU_EDIT("Edit"),
    MENU_COPY("Copy"),
    MENU_DELETE("Delete"),
    MENU_DETACH("Remove"),
    MENU_DO_NOT_SHOW("Do not show on summary page"),
    MENU_OFFLINE("Offline"),;


    String locator;

    MeasureInstancesEPageLocators(String loc) {
        locator = loc;
    }

    MeasureInstancesEPageLocators(String loc80, String loc81) {
        locator = loc80;
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_1)) {
            locator = loc81;
        }

    }


    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

}
