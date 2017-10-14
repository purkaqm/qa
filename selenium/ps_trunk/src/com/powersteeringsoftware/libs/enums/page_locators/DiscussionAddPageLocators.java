package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 21.05.2010
 * Time: 11:03:38
 * To change this template use File | Settings | File Templates.
 */
public enum DiscussionAddPageLocators implements ILocatorable {
    URL("project/DiscussionAdd.epage"),
    ADDED_DOCUMENT_LINK("//div[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    ADD_DOCUMENT_DIALOG("addDocuments_ADD_DOCS", "addDocuments_ADD_DOCS_" + LOCATOR_REPLACE_PATTERN),
    DEFAULT_ISSUE_SUBJECT_PATTERN("RE: " + LOCATOR_REPLACE_PATTERN),
    ISSUE_SUBJECT("subjectInput"),
    ISSUE_SUBMIT("//input[@value='Submit' and @name='Submit']"),
    ISSUE_FRAME("messageInput_ifr"),
    ISSUE_DESCRIPTION("window.tinyMCE.getInstanceById('mce_editor_0').setHTML('" + LOCATOR_REPLACE_PATTERN + "')",
            "window.tinyMCE.getInstanceById('ta_messageInput').setContent('" + LOCATOR_REPLACE_PATTERN + "');",
            "window.tinyMCE.getInstanceById('messageInput').setContent('" + LOCATOR_REPLACE_PATTERN + "');"),
    ISSUE_DESCRIPTION_CONTENT("window.tinyMCE.getInstanceById('messageInput').getContent();");

    String locator;

    DiscussionAddPageLocators(String loc93, String loc94) {
        locator = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) ? loc94 : loc93;
    }

    DiscussionAddPageLocators(String locOld, String loc71, String loc82) {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._7_1_1)) {
            locator = locOld;
        } else if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_0)) {
            locator = loc82;
        } else {
            locator = loc71;
        }
    }

    DiscussionAddPageLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object toRep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(toRep));
    }

}
