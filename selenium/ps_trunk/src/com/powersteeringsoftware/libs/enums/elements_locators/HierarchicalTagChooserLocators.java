package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 13.07.2010
 * Time: 16:11:49
 */
public enum HierarchicalTagChooserLocators implements ILocatorable {
    POPUP_LABEL_PARENT_NAME("div"),
    POPUP_LABEL_PARENT_CLASS("treeNodeCnt"),
    POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT("//span"),
    POPUP_LABEL_OPEN_CLOSE_BRANCH_LOADING("//div[@class='dijitTreeExpando dijitTreeExpandoLoading']"),
    POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_CLOSED("dijitTreeExpandoClosed"),
    POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_OPENED("dijitTreeExpandoOpened"),
    POPUP_LABEL_OPEN_BRANCH_ELEMENT("//span[@class='dijitTreeExpando " + POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_CLOSED.locator + "']"),
    POPUP_LABEL_CLOSE_BRANCH_ELEMENT("//span[@class='dijitTreeExpando " + POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_OPENED.locator + "']"),
    POPUP_LABEL_VALUE("value"),
    POPUP_LABEL_CONTAINER("//div[@containerid='" + LOCATOR_REPLACE_PATTERN + "']"),
    POPUP_LABEL_CHILDS("/div/div[@class='dijitTreeNode']"),
    POPUP_CLEAR("//div[@action='Clear']"),;
    private String locator;

    HierarchicalTagChooserLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object r) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(r));
    }
}
