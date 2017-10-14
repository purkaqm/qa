package com.powersteeringsoftware.libs.enums;

/**
 * Interface presents classes that are allowed to keep html element locations
 * for using them in the Selenium methods such as type, isPresent etc
 */
public interface ILocatorable {
    String LOCATOR_REPLACE_PATTERN = "{}";
    String LOCATOR_REPLACE_PATTERN_2 = "##";
    String LOCATOR_REPLACE_PATTERN_3 = "$$";
    String EMPTY = "";

    String getLocator();

    String name();
}
