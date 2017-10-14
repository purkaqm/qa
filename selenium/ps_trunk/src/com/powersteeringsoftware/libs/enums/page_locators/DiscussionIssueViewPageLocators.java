package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 27.12.2010
 * Time: 18:58:17
 */
public enum DiscussionIssueViewPageLocators implements ILocatorable {
    URL("project/DiscussionIssueView"),
    REDIRECT_URL_PREF("DiscussionRedirect.svc?sp=S"),
    REDIRECT_URL(REDIRECT_URL_PREF.getLocator() + LOCATOR_REPLACE_PATTERN),
    BLOCK("//div[@class='block']"),
    TITLE("//span[@class='title']"),
    TITLE2("//span[@class='title2']"),
    TITLE_LINK("/a"),
    OPEN("//h5[@class='clearfix']//li[3]"),
    OPEN_YES("Close Issue"),

    POPUP_BUTTON_OK("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']//input[@value='Ok']"),

    ESCALATE("Escalate to Issue"),
    DEESCALATE("De-Escalate to Discussion"),
    DELETE("Delete"),
    DELETE_DEESCALATE_ID_SUFFIX("Show"),
    DELETE_DEESCALATE_DIALOG_OK("//input[@value='Ok']"),
    DELETE_DEESCALATE_DIALOG_CANCEL("//input[@value='Cancel']"),

    DELETE_DEESCALATE_DIALOG("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),

    REPLY("Reply"),
    CLOSE("Close Issue"),
    MOVE_LINK("link=Move Thread"),
    EDIT("Edit"),;

    private String locator;

    DiscussionIssueViewPageLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(String toRep) {
        return getLocator().replace(LOCATOR_REPLACE_PATTERN, toRep);
    }
}
