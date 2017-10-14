package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 14:04:40
 */
public enum WorkDependencyDialogLocators implements ILocatorable {
    DIALOG("//div[@class='dijitDialog psDialog wbsdep']"),
    ROW("//tr[@rowid]"),
    PRED("//td[@cellType='pred']//input"),
    TYPE("//td[@cellType='type']/div"),
    LAG("//td[@cellType='lag']//input"),
    LAG_REGEXP("[+-]?\\d*\\.?\\d{1,}([eE][-+]?\\d+)?"),
    LAG_MAX("8999999999999999"),
    LAG_MIN("-8999999999999999"),
    BUTTON_OK("//input[@value='Ok']"),
    BUTTON_CANCEL("//input[@value='Cancel']"),

    DEPENDENCY_REGEXP("^(\\d+)(([A-Za-z]+)((\\+|-)\\d+)*)*$"),
    DEPENDENCY_INDEX("$1"),
    DEPENDENCY_TYPE("$3"),
    DEPENDENCY_LAG("$4"),;
    private String locator;

    WorkDependencyDialogLocators(String s) {
        locator = s;
    }


    public String getLocator() {
        return locator;
    }


    public enum DependencyType implements ILocatorable {
        FS("Finish-to-start"),
        SS("Start-to-start"),
        FF("Finish-to-finish"),
        SF("Start-to-finish"),;
        String type;

        DependencyType(String s) {
            type = s;
        }

        @Override
        public String getLocator() {
            return type;
        }
    }
}
