package com.powersteeringsoftware.libs.enums.page_locators;

import java.util.ArrayList;
import java.util.List;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.09.11
 * Time: 15:18
 */
public enum HelpWindowLocators implements ILocatorable {
    MAIN_FRAME("//frame[@name='mainFrame']"),
    CONTENT_FRAME("//frame[@src='blank.htm']"),
    CLASS_PREFIX_1("HC_"),
    CLASS_PREFIX_2("N_"),
    CLASS_PREFIX_3("TH_"),
    CLASS_PREFIX_4("LBS_"),
    CLASS_PREFIX_5("HRI_"),
    DIV_HEADER("//div[@class='HC_HeadingChapter']"),
    DIV("//div"),
    TOP_FRAME("//frame[@src='pagenav.htm']"),
    NEXT("//img[@name='WWHNextIcon']"),;
    private String locator;

    HelpWindowLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public static List<String> getClassPrefixes() {
        List<String> res = new ArrayList<String>();
        for (HelpWindowLocators v : values()) {
            if (v.name().startsWith("CLASS_PREFIX")) {
                res.add(v.getLocator());
            }
        }
        return res;
    }
}