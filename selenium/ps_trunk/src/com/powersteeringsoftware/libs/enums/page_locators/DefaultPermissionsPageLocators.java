package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 25.05.2010
 * Time: 12:35:35
 * To change this template use File | Settings | File Templates.
 */
public enum DefaultPermissionsPageLocators implements ILocatorable {
    URL("admin/DefaultPermissions"),
    CONTENT("//div[@id='content']/div"),
    ADD_NEW_BUTTON("//button[contains(text(), 'Add New')]"),
    ADD_ROLE_POPUP_SAVE_BUTTON("//button[contains(text(), 'Save')]", "//div[contains(text(), 'Save')]"),
    ADD_ROLE_POPUP_CALCEL_BUTTON("AddConfigurabeRoleHide"),
    ADD_ROLE_POPUP("AddConfigurabeRole"),
    ADD_ROLE_POPUP_SEQUENCE("sequence"),
    ADD_ROLE_POPUP_NAME("displayName"),
    ADD_ROLE_POPUP_PLURAL_NAME("displayPName"),
    ADD_ROLE_POPUP_DESCRIPTION("//textarea[@id='description']"),
    ADD_ROLE_POPUP_WORKS("//div[@id='attachableToDisp']"),

    SET_TABLE_ROW("tr"),
    SET_TABLE_NAME_COLUMN("//" + SET_TABLE_ROW.locator + "/td[2]"),

    CORE_SET_TABLE("//div[@class='psGrid clearfix']//table"),
    CORE_SET_TD_LINK("/a"),
    CORE_SET_LINK("link=" + LOCATOR_REPLACE_PATTERN),
    CORE_SET_DELETE_IMG("//img[@alt='Delete Role']"),
    CORE_SET_SAVE_BUTTON("//div[@class='psGrid clearfix']//input[@value='Save']"),

    DELETE_CORE_SET_ROLE_POPUP("//div[@id='deleteRoleDialog']"),
    DELETE_CORE_SET_ROLE_POPUP_OK_BUTTON("//input[@value='Ok']"),

    ADD_NEW_CUSTOM_SET("//input[@value='Add New']"),

    CUSTOM_SET_TABLE("//div[@class='psGrid']//table"),
    CUSTOM_SET_SAVE_BUTTON("//div[@class='psGrid']//input[@value='Save']"),
    CUSTOM_SET_USERS_POPUP("//div[@id='popup_users']"),
    CUSTOM_SET_TD_LINK("/div[@class='link']"),
    CUSTOM_SET_LINK("//*[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    CUSTOM_SET_DELETE_IMG("//img[contains(@src, 'delete')]"),
    CUSTOM_SET_DELETE_IMG_LINK_ID("Show"),

    DELETE_CUSTOM_SET_ROLE_POPUP("//div[@id='dlgForJSAlert']"),
    DELETE_CUSTOM_SET_ROLE_POPUP_OK_BUTTON("//button[@id='dialogOkButton']"),
    DELETE_CUSTOM_SET_ROLE_POPUP_AFTER_SAVE("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    DELETE_CUSTOM_SET_ROLE_POPUP_AFTER_SAVE_OK_BUTTON("//input[@value='Ok']"),

    TABLE_HEADER(".//tr[1]//th"),
    TABLE_HEADER_CENTER("./center"),
    TABLE_HEADER_NAME("Name"),
    TABLE_HEADER_LEVEL("Level"),
    TABLE_COLUMNS("./td"),
    TABLE_INNER_TABLE_93("//td/div/table"),
    TABLE_ROWS("./tbody/tr"),
    CELL_INPUT(".//input"),
    CELL_LINK_1(".//div[@class='link']"),
    CELL_LINK_2("./a"),
    IMG("//img"),

    LINK("//a"),
    SET_TABLE_RIGHT_94("Right"),
    SET_TABLE_LEFT_94("Left"),
    CUSTOM_SET_TABLE_94("//div[@id='CustomSets_" + LOCATOR_REPLACE_PATTERN + "']//table"),
    CORE_SET_TABLE_94("//div[@id='CoreSets_" + LOCATOR_REPLACE_PATTERN + "']//table"),
    CORE_SET_RIGHT_DIV_94("//div[@id='CoreSets_" + SET_TABLE_RIGHT_94.locator + "']"),
    HEADER_94("//th/center"),
    INPUT_94("//td//input"),

    CUSTOM_SET_SAVE_BUTTON_94("//div[@class='psGrid']//input[@value='Save']"),
    ADD_NEW_CUSTOM_SET_94("//center/input[@class='btn-white']"),
    ADD_NEW_BUTTON_94("//center/button[@class='btn-white']"),;
    String locator;

    DefaultPermissionsPageLocators(String loc) {
        locator = loc;
    }

    DefaultPermissionsPageLocators(String loc81, String loc82) {
        if (TestSession.getAppVersion().equals(PowerSteeringVersions._8_1)) {
            locator = loc81;
        } else if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
            locator = loc82;
        }
    }


    public String getLocator() {
        return locator;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String replace(Object rep) {
        String s;
        if (rep instanceof ILocatorable) {
            s = ((ILocatorable) rep).getLocator();
        } else {
            s = String.valueOf(rep);
        }
        return locator.replace(LOCATOR_REPLACE_PATTERN, s);
    }

}
