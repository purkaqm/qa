package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 17:29:25
 */
public enum WorkChooserDialogLocators implements ILocatorable {
    POPUP("//div[@id='popup_depWCPopup']"),
    TAB_BROWSE("Browse"),
    TAB_SEARCH("Search"),
    TAB_SEARCH_INPUT("//input[@type='text']"),
    TAB_SEARCH_SUBMIT("//input[@type='button']"),
    TAB_SEARCH_LOADING("//li[@class='dnd-msg-loading']"),
    TAB_SEARCH_ERROR("//li[@class='dnd-msg-error']"),
    TAB_SEARCH_LINK("//div[@id='searchTree' or @id='resultsDiv']//div"),
    DLG_CLEAR_LINK("//div/a[text()='Clear']"),
    DLG_SAVE_BUTTON("//input[@value='Save']"),
    DLG_CANCEL_BUTTON("//input[@value='Cancel']");
    private String locator;

    WorkChooserDialogLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

}
