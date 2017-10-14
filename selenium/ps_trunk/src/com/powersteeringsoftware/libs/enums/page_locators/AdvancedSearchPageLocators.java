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
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Enum for Advanced Search Locators
 * <p/>
 * Date: 17/08/11
 *
 * @author karina
 */
public enum AdvancedSearchPageLocators implements ILocatorable {

    KEYWORD_TYPE_SELECT("PropertySelection"),
    KEYWORD_TYPE_SELECT_LABEL("label=regexp:\\s*"),
    KEYWORD_TYPE_SELECT_ANY_OF_OPTION("Any of"),
    KEYWORD_TYPE_SELECT_ALL_OF_OPTION("All of"),
    KEYWORD_TYPE_SELECT_EXACTLY_OPTION("Exactly"),
    KEYWORD_INPUT_FIELD("TextField"),

    TABLE("//table[@id='PSTable']"),
    TABLE_ROW_TH("/tbody/tr/th"),
    TABLE_ROW_TD("/tbody/tr/td"),
    TABLE_TD_DIV("/div"),
    TABLE_TD_A("/a"),

    GENERAL_NAME_COLUMN("Name"),
    ISSUE_NAME_COLUMN("Topic"),
    ANYTHING_TYPE_COLUMN("Type"),

    TABLE_ROW("//tr"),
    ISSUE_TABLE_ROW_NAME("//td[@class='issueColumnValue']//a"),
    TABLE_ROW_NAME("//td[@class='nameColumnValue']//a"),
    PEOPLE_TABLE_ROW_NAME("//td[@class='nameColumnValue']//div"),

    ADVANCED_SEARCH_CLEAR_LINK("//h5/a[text()='Clear']"),

    START_DATEPICKER("//div[@id='widget_PSDatePicker']"),
    END_DATEPICKER("//div[@id='widget_PSDatePicker_0']"),

    // Advanced Search tab locators
    ANYTHING_ADVANCED_SEARCH_LINK("//a[text()='Anything']", "//a[text()='All']"),
    PROJECTS_ADVANCED_SEARCH_LINK("//a[text()='Projects']"),
    PEOPLE_ADVANCED_SEARCH_LINK("//div//li//a[text()='People']"),
    ISSUES_ADVANCED_SEARCH_LINK("//a[text()='Discussions/Issues']", "//a[text()='Issues']"),
    DOCUMENTS_ADVANCED_SEARCH_LINK("//a[text()='Documents']"),
    SELECTED_TAB_CLASS("hl"),

    PROJECT_DESCENDED_FIELD("popup_projparShow"),
    PROJECT_DESCENDED_POPUP("//div[@id='popup_projpar']"),
    ISSUE_DESCENDED_FIELD("popup_issueparShow"),
    ISSUE_DESCENDED_POPUP("//div[@id='popup_issuepar']"),
    DOCUMENT_DESCENDED_FIELD("popup_docparShow"),
    DOCUMENT_DESCENDED_POPUP("//div[@id='popup_docpar']"),


    //TODO: ???
    DESCENDED_ROW_BY_NODE("//div[@class='link' and text()='" + LOCATOR_REPLACE_PATTERN + "']"), // ?
    DESCENDED_FROM_LINK("popup_projparShow"), // ?


    OWNER_FIELD("popup_projownShow"),
    OWNER_POPUP("//div[@id='popup_projown']"),

    STATUS_CHOOSER_FIELD("//div[@class='MultiSelectWrapper']"),

    EMAIL_INPUT_FIELD("TextField_0"),
    PHONE_INPUT_FIELD("TextField_1"),

    SEARCH_BUTTON("//form[@id='advsrch']//input[@value='Search']"),
    NOTHING_FOUND("//div[@class='box clearfix padded']"),
    EXPAND_COLLAPSE_FILTERS("//a[contains(@href, 'SearchFilters')]"),;

    /**
     * The constructor
     *
     * @param locator
     */
    private AdvancedSearchPageLocators(String locator) {
        this.locator = locator;
    }

    private AdvancedSearchPageLocators(String loc10, String loc11) {
        this(TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0) ? loc11 : loc10);
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
     * @return
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
     * @param loc
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
