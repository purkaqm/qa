package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 03.09.11
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public enum AddDocumentsDialogLocators implements ILocatorable {

    SUFFIX("_ADD_DOCS"),
    LINK(LOCATOR_REPLACE_PATTERN + "_DLG_SHOW"),
    POPUP("//div[@id='" + LOCATOR_REPLACE_PATTERN + "_DLG']"),
    LOADING("//div[@id='" + LOCATOR_REPLACE_PATTERN + "_DLG']//img[contains(@src, 'loading.gif')]"),
    FILE_INPUT(LOCATOR_REPLACE_PATTERN + "_ADD_DOC_FORM_FILE"),
    FILE_TITLE(LOCATOR_REPLACE_PATTERN + "_ADD_DOC_FORM_TITLE"),
    FILE_DESCRIPTION(LOCATOR_REPLACE_PATTERN + "_ADD_DOC_FORM_DESCR"),
    ADD_DOCUMENT_BUTTON(LOCATOR_REPLACE_PATTERN + "_ADD_DOC"),
    ADD_DOCUMENT_BUTTON_CLASS("class"),
    ADD_DOCUMENT_BUTTON_CLASS_ATTR("btn btn-disabled"),
    DONE_BUTTON("//input[@value='Done']"),;

    String locator;

    AddDocumentsDialogLocators(String loc) {
        locator = loc;
    }

    public String getLocator(String prefix) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, prefix);
    }

    public String getLocator() {
        return locator;
    }
}
