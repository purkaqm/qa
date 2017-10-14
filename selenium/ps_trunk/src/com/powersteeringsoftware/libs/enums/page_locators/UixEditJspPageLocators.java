package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.05.2010
 * Time: 19:03:44
 * To change this template use File | Settings | File Templates.
 */
public enum UixEditJspPageLocators implements ILocatorable {
    URL("admin/uix_key.jsp"),

    EDIT_LINK(toLinkHref("uix_edit.jsp")),
    SUBMIT_LINK(toLinkHref("submit")),
    CANCEL_LINK(toLinkHref("uix_key.jsp")),
    FEATURE("feature-" + LOCATOR_REPLACE_PATTERN),

    ROW("//tr"),
    ITEM_IMG("//td[2]/img"),
    ITEM_IMG_TITLE("title"),
    ITEM_DESC("//td[3]/div[1]/span"),
    ITEM_CODE("//td[3]/div[2]/span"),
    ;

    String locator;

    UixEditJspPageLocators(String loc) {
        locator = loc;
    }

    private static String toLinkHref(String loc) { // for ie10
        return "//a[contains(@href, '" + loc + "')]";
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }

    public enum Label implements ILocatorable{
        ON("On"),
        OFF("Off"),
        DEFAULT_ON("Default On"),
        DEFAULT_OFF("Default Off"),
        ;
        String loc;
        Label(String label) {
            loc = label;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    public enum Feature {
        RESOURCE_PLANING(),
        IMMEDIATE_DELEGATION(),
        TEAM_PANE("team.pane"),
        RESOURCE_MANAGER(),
        ;

        private String selector;
        private String code;

        Feature(String s) {
            code = s;
            FEATURE.replace(code);
        }

        Feature(String s1, String s2) {
            code = s1;
            selector = s2;
        }

        Feature() {
            code = name().toLowerCase().replace("_", ".");
            selector = FEATURE.replace(code);
        }


        public static String getLocator(String name) {
            for (Feature f : values()) {
                if (f.code.equals(name)) return "name=" + f.selector;
            }
            return "name=" + FEATURE.replace(name);
        }
    }

}
