/*
 * Copyright (c) PowerSteering Software 2011
 * All rights reserved.
 *
 * This software and documentation contain valuable trade secrets and proprietary information belonging to
 * PowerSteering Software Inc.  None of the foregoing material may be copied, duplicated or disclosed without the
 * express written permission of PowerSteering.  Reverse engineering, decompiling and disassembling are explicitly
 * prohibited. POWERSTEERING SOFTWARE EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR ANY
 * PARTICULAR PURPOSE, AND WARRANTIES OF NON-INFRINGEMENT OF INTELLECTUAL PROPERTY RIGHTS OF A
 * THIRD PARTY, PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE OF DEALING
 * OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED WITH RESPECT TO THE USE OF THE
 * SOFTWARE OR DOCUMENTATION.  Under no circumstances shall PowerSteering Software be liable for incidental,
 * special, indirect, direct or consequential damages or loss of profits, interruption of business, or related expenses which
 * may arise from use of software or documentation, including but not limited to those resulting from defects in software
 * and/or documentation, or loss or inaccuracy of data of any kind.
 */
package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Enum for Simple Search Locators
 * <p/>
 * Date: 25/08/11
 *
 * @author karina
 */
public enum SimpleSearchPageLocators implements ILocatorable {

    SIMPLE_SEARCH_FIELD("TextField"),
    SIMPLE_SEARCH_RESULT_PEOPLE_ROW_NODE("//div[@class='link' and text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    SIMPLE_SEARCH_RESULT_ROW_NODE("//a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    SIMPLE_SEARCH_SEARCH_BUTTON("//form[@id='PSForm']//input[@value='Search']"),
    SIMPLE_SEARCH_CLEAR_BUTTON("//form[@id='PSForm']//input[@value='Clear']"),;

    /**
     * The constructor
     *
     * @param locator the locator
     */
    SimpleSearchPageLocators(String locator) {
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
     * @param loc the locator
     * @return the loc append to the locator
     */
    public String append(ILocatorable loc) {
        return append(loc.getLocator());
    }

    public String append(String loc) {
        return locator + loc;
    }

    /**
     * Replace the locator
     *
     * @param loc the input locator
     * @return the locator replaced
     */
    public String replace(ILocatorable loc) {
        return replace(loc.getLocator());
    }

    public String replace(String loc) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, loc);
    }

    private final String locator;
}
