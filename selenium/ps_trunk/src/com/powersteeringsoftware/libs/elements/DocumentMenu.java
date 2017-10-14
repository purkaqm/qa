package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.settings.BrowserTypes;

import static com.powersteeringsoftware.libs.enums.elements_locators.DocumentMenuLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 04.09.11
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class DocumentMenu extends Menu {
    private static BrowserTypes browser = SeleniumDriverFactory.getDriver().getType();

    public DocumentMenu(String name) {
        super(LINK.replace(name), browser.isWebDriver());
    }

    public DocumentMenu(Link e) {
        super(e, browser.isWebDriver());
    }

    public Link download() {
        return getMenuLink(DOWNLOAD);
    }

    public Link update() {
        return getMenuLink(UPDATE);
    }

    public Link details() {
        return getMenuLink(SEE_DETAILS);
    }

    public Link unlock() {
        return containsItem(UNLOCK) ? getMenuLink(UNLOCK) : null;
    }
}
