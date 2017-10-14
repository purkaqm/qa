package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public enum DocumentListingPageLocators implements ILocatorable {
    ADD_DOCUMENT_DIALOG_BUTTON("addNewDocumentsButton_ADD_DOCS", "addNewDocumentsButton_ADD_DOCS_" + LOCATOR_REPLACE_PATTERN),
    ADD_DOCUMENT_DIALOG_LINK("addNewDocumentsLink_ADD_DOCS", "addNewDocumentsLink_ADD_DOCS_" + LOCATOR_REPLACE_PATTERN),
    ROW("//table[@id='PSTable']//tr"),
    MORE("id=linkFwd"),
    TITLE_COLUMN("//td[@class='titleColumnValue']"),
    TITLE_COLUMN_LINK("/a"),
    CHECKBOX("//input[@type='checkbox']"),
    DELETE("actionButton"),
    DELETE_DIALOG("//div[@id='confirmationDialog']"),
    DELETE_DIALOG_YES("//input[@value='Yes']");
    private String locator;

    DocumentListingPageLocators(String loc93, String loc94) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? loc94 : loc93;
    }


    DocumentListingPageLocators(String loc) {
        locator = loc;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(Object r) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(r));
    }
}
