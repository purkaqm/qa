package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.06.2010
 * Time: 16:25:27
 */
public enum UnderlayNodeLocators implements ILocatorable {
    WAITING_UNDERLAY_NODE("//div[contains(@class,'waitingUnderlay')]"),
    LOADING_WIDGET_SCRIPT("var " + LOCATOR_REPLACE_PATTERN + "=false;" +
            "var ar=window.dojo.query('div.waitingUnderlay'); " +
            "for(var i=0;i<ar.length;i++){" +
            "if(window.dijit.byNode(ar[i]) && window.dijit.byNode(ar[i]).isShow())" +
            LOCATOR_REPLACE_PATTERN + "=true;" +
            "};" + LOCATOR_REPLACE_PATTERN),;
    private String locator;

    UnderlayNodeLocators(String s) {
        locator = s;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object rep) {
        String loc = getLocator();
        if (loc == null) return null;
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }

}
