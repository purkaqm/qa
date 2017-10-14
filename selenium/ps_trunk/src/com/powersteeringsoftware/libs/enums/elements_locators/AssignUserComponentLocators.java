package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.10.13
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public enum AssignUserComponentLocators implements ILocatorable {
    SEARCH_PEOPLE_INPUT("inputBoxId_" + LOCATOR_REPLACE_PATTERN),
    SEARCH_PEOPLE_GO("//input[@value='Go' and contains(@onclick, 'inputBoxId_" + LOCATOR_REPLACE_PATTERN + "')]"),
    SEARCH_PEOPLE_RESULT("//div[@class='searchResults']//li"),
    SEARCH_PEOPLE_RESULT_LINK("//a"),
    SEARCH_PEOPLE_RESULT_MOUSE_OVER("dojoDndItemOver"),
    SEARCH_PEOPLE_WAIT("dnd-msg-loading"),
    SEARCH_PEOPLE_ERROR("dnd-msg-error"),

    ROLES_DND_PREFIX("drop_"),
    ROLES_("//h3[contains(@id, 'drop_')]/span"),
    ROLES_OWNER("owner"),
    ROLES_CHAMPION("champ"),
    ROLES_FINREP("finrep"),
    ROLES_CONTRIBUTOR("teammember"),
    ROLES_COLOR_GREEN("green"),
    ROLES_COLOR_PINK("pink"),
    TEAM_LINK("//li[@dndtype='" + LOCATOR_REPLACE_PATTERN + "']//a"),

    OLD_SEARCH_PEOPLE_HIDDEN_TEAM("hidden_team_champs", "hidden_team_all", null),
    OLD_SEARCH_PEOPLE_SEARCH("css=div.col1 input.dnd-search"),
    OLD_SEARCH_PEOPLE_SUBMIT("css=div.col1 input.btn-white"),
    OLD_SEARCH_PEOPLE_LOADING("css=div.col1 li.dnd-msg-loading"),
    OLD_SEARCH_PEOPLE_ITEM("css=div.col1 li.dojoDndItem"),
    OLD_SEARCH_PEOPLE_ITEM_ATTR("itemval"),
    OLD_SEARCH_PEOPLE_SET_VALUE("window.dojo.byId('" + OLD_SEARCH_PEOPLE_HIDDEN_TEAM.locator + "').value='" + LOCATOR_REPLACE_PATTERN + "'"),

    REMOVE("//span[@class='iRemove']"),
    REMOVE_LI("li"),
    REMOVE_LINK("//a"),
    TEAM_ALL("//ul[@id='drop_team_all']"),
    TEAM_MEM("//li"),
    TEAM_ROLE("//strong"),
    TEAM_USER_CLASS("dojoDndItem"),
    TEAM_USER("//a"),;
    String locator;

    AssignUserComponentLocators(String locator) {
        this.locator = locator;
    }

    AssignUserComponentLocators(String loc70, String loc71, String loc) {
        if (loc71 == null) loc71 = loc70;
        if (!TestSession.isVersionPresent()) return;
        locator = TestSession.getAppVersion().verLessThan(PowerSteeringVersions._7_1) ? loc70 :
                TestSession.getAppVersion().verGreaterThan(PowerSteeringVersions._7_1) ? loc : loc71;

    }

    public String getLocator() {
        return locator;
    }

    public String append(ILocatorable loc) {
        return append(loc.getLocator());
    }

    public String append(String loc) {
        return locator + loc;
    }

    public String replace(ILocatorable loc) {
        return replace(loc.getLocator());
    }

    public String replace(String loc) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, loc);
    }
}
