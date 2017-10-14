package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 15:39:13
 */
public enum WorkTreeElementLocators implements ILocatorable {
    TREE_ROOT("//div[@containerid='root']"),
    WORK_ITEM_LINK("//a[@href]"),
    WORK_ITEM_NO_LINK("//span[@class='itemName']"),
    WORK_ITEM_LINK_POPUP("//div[contains(@class, 'link')]"),
    WORK_ITEM_LINK_POPUP_TIMESHEETS_100("./span"),
    WORK_ITEM_LINK_PARENT_CLASS("dijitTreeNode"),
    WORK_ITEM("//div[@class='treeNodeCnt']"),
    WORK_ITEM_ROOTS("./div/div[@class='" + WORK_ITEM_LINK_PARENT_CLASS.locator + "']"),
    WORK_ITEM_PARENTS("./div/div/div[@class='" + WORK_ITEM_LINK_PARENT_CLASS.locator + "']"),
    TREE_ITEM_POPUP("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']/parent::*/parent::div"),
    TREE_ITEM_ALT_POPUP("//div[contains(text(), '" + LOCATOR_REPLACE_PATTERN + "')]/parent::*/parent::div"),

    TREE_ITEM_IMG(".//img"),
    TREE_ITEM_EXPAND_COLLAPSE("//span[contains(@class, 'dijitTreeExpando')]"),
    TREE_ITEM_CLOSED_CLASS("dijitTreeExpandoClosed"),
    TREE_ITEM_LOADING_CLASS("dijitTreeExpandoLoading"),
    TREE_ITEM_OPENNED_CLASS("dijitTreeExpandoOpened"),
    TRRR_ITEM_LEAF_CLASS("dijitTreeExpandoLeaf"),
    TREE_ITEM_PLUS(".//span[@class='dijitTreeExpando " + TREE_ITEM_CLOSED_CLASS.getLocator() + "']"),
    TREE_ITEM_CHILD_LOC("/parent::div/parent::div//div[@class='dijitTreeNode']"),
    LOADING_ELEMENT_LOCATOR("//div[@class='dijitTreeExpando " + TREE_ITEM_LOADING_CLASS.getLocator() + "']"),
    POPUP_BROWSE_PANE("//div[contains(@widgetid,'browse') and contains(@class,'dijitContentPane')]");
    ;
    private String locator;

    WorkTreeElementLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }
}
