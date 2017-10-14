package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 23.04.2010
 * Time: 18:53:52
 * To change this template use File | Settings | File Templates.
 */
public enum BasicCommonsLocators implements ILocatorable {
    URL_PREFIX("?sp="),
    URL_ID_1("(U|S)"),
    URL_ID("U"),
    URL_ID_PREFIX(".*page\\?sp=" + URL_ID_1.locator),
    URL_ID_SUFFIX("&.*"),

    ERROR_BOX("//div[@class='MsgBox RedBoxBorder']"),
    MESSAGE_BOX("//div[@class='MsgBox BlueBoxBorder']"),
    ERROR_BOX_MESSAGE("/ul/li"),
    LOGOUT_COMMON_LINK("link=Log out"),

    CONTEXT_WAIT_ELEMENT("//td[@class='titleBold']"),
    CONTEXT_WAIT_MESSAGE("Please wait."),

    INBOX_LINK("InboxLink"),
    HOME_LINK("HomeLink"),

    HELP_100("popHelpLink"),
    HELP_110("helpIcon"),
    HELP_VERSION("PowerSteering Version"),
    HELP_ONLINE_HELP("View Online Help"),
    HELP_ALERT_DB_VERSION("DBVersion"),
    USER_NAME_LINK_100("popSettings"), //10.0
    USER_NAME_LINK_110("userNameLink"), //11.0
    USER_NAME_110("//div[@class='userNameFormat']"),
    USER_NAME_110_TITLE("title"),
    MESSAGE("//div[@class='message']"),
    MESSAGES(MESSAGE.locator + "[" + LOCATOR_REPLACE_PATTERN + "]"),
    MESSAGES_ERROR("//div[@class='error']"),
    MESSAGES_CONSOLE("//div[@class='console']"),
    MESSAGE_CONSOLE_READY("height: auto"),
    MESSAGES_CLOSE("//div[@class='messages compact']/following-sibling::div[@class='close']"),
    LAST_VISITED_SHOW_LINK("LastVisitedShow"),
    LAST_VISITED_SHOW_POPUP("//div[@id='LastVisited']"),
    LAST_VISITED_LINKS("//li//a"),
    LAST_VISITED_MORE("More..."),
    LAST_VISITED_LINKS_TO_REPLACE("table/tbody/tr/td"),

    // Locator for "Quick Search" popup
    QUICK_SEARCH_LINK("dlgSearchShow"),

    CONTAINER_HEADER_100_1("//div[@class='container']//span"),
    CONTAINER_HEADER_100_2("//div[@class='container']//div[@class='left']"),
    CONTAINER_HEADER_110("//div[@class='pageTitle']"),;

    public enum UserNameMenu110 implements ILocatorable {
        POPUP("//div[@id='popupUserSettingsWindow']"),
        ITEM_LINK("//a"),
        ITEM_AGENDA("Agenda"),
        ITEM_TIMESHEETS("Timesheets"),;
        private String loc;

        UserNameMenu110(String s) {
            loc = s;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    String locator;

    BasicCommonsLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }

    public enum QuickSearchResultLocators implements ILocatorable {
        LOADING("qs_loading_img"),
        TABLE("//table[@id='qs_searchResultsTable']"),
        ANY_ROW("//tr"),
        PEOPLE_TH("People"),
        ROW("//tr"),
        TH("//th"),
        LI("//li"),
        NO_RESULTS("qs_noResultsMessage"),;
        private String loc;

        QuickSearchResultLocators(String s) {
            loc = s;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    public enum QuickSearch110Locators implements ILocatorable {
        QUICK_SEARCH("//div[@id='quickSearch']"),
        ICON("searchIcon"),
        ICON_OPEN_CLASS(" headerIconSelected"),
        INPUT("qs_searchText"),
        ADVANCED_LINK("//div[@class='advancedSearch']"),
        TYPE_ICON("//span[contains(@class, 'qs_searchTypeTriangle')]"),;

        QuickSearch110Locators(String s) {
            loc = s;
        }

        private String loc;

        @Override
        public String getLocator() {
            return loc;
        }
    }

    /**
     * Enum for Quick Search locators
     * <p/>
     * Date: 29/08/11
     *
     * @author karina
     */
    public enum QuickSearch100Locators implements ILocatorable {
        POPUP("dlgSearch"),

        QUICK_SEARCH_FIELD("searchField"),
        CATEGORY_TYPE_SELECT("selection"),
        CATEGORY_TYPE_SELECT_LABEL("label=regexp:\\s*"),
        CATEGORY_TYPE_SELECT_PROJECTS_OPTION("Projects"),
        CATEGORY_TYPE_SELECT_PEOPLE_OPTION("People"),
        CATEGORY_TYPE_SELECT_IDEAS_OPTION("Ideas"),
        CATEGORY_TYPE_SELECT_ISSUES_OPTION("Issues"),
        CATEGORY_TYPE_SELECT_DOCUMENTS_OPTION("Documents"),
        QUICK_SEARCH_SEARCH_BUTTON("Submit"),
        QUICK_SEARCH_CANCEL_BUTTON("dlgSearchHide"),
        ADVANCED_SEARCH_LINK("//div[@id='" + POPUP.locator + "']//a");

        /**
         * The constructor
         *
         * @param locator
         */
        QuickSearch100Locators(String locator) {
            this.locator = locator;
        }

        /**
         * Gets the locator
         *
         * @return the locator
         */
        public String getLocator() {
            return locator;
        }

        /**
         * Append the locator
         *
         * @param loc
         * @return the loc append to the locator
         */
        public String append(ILocatorable loc) {
            return append(loc.getLocator());
        }

        public String append(String loc) {
            return locator + loc;
        }

        private final String locator;
    }
}
