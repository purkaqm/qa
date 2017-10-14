package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 14.09.11
 * Time: 13:34
 */
public enum ApiExportPageLocators implements ILocatorable {
    URL_DIR("interchange"),
    URL_EXPORT("export_"),
    URL_IMPORT("import_"),
    URL_LOGIN("login"),
    URL_PASSWORD("pw"),
    URL_WORKS("works.jsp"),
    URL_USERS("users.jsp"),
    URL_METRIC_DATA("users.jsp"),;
    private String locator;

    ApiExportPageLocators(String loc) {
        locator = loc;
    }

    @Override
    public String getLocator() {
        return locator;
    }
}
