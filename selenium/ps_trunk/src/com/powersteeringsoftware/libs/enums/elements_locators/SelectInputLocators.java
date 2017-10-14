package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.04.12
 * Time: 16:37
 */
public enum SelectInputLocators implements ILocatorable {
    VALUE_ATTR("value"),
    VALUE_PREFIX(VALUE_ATTR.locator + "="),
    VALUE(VALUE_PREFIX.locator + LOCATOR_REPLACE_PATTERN),
    LABEL_PREFIX("label="),
    LABEL(LABEL_PREFIX.locator + LOCATOR_REPLACE_PATTERN),
    LABEL_PREFIX_REGEXP("label=regexp:"),
    LABEL_PREFIX_SPACE(LABEL_PREFIX_REGEXP.locator + "\\s*"),
    LABEL_SPACE(LABEL_PREFIX_SPACE.locator + LOCATOR_REPLACE_PATTERN),
    OPTIONS("/option"),
    OPTION("/option[" + LOCATOR_REPLACE_PATTERN + "]"),
    SELECTED_ATTR("selected");
    String locator;

    SelectInputLocators(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }

}
