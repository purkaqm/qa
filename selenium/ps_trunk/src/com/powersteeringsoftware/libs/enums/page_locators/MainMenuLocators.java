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
 * Locators for using in Selenium
 *
 * @author selyaev_ag
 */
public enum MainMenuLocators implements ILocatorable {

    /*
      * Locators for "Browse" menu items
      */
    BROWSE_MENU("id=BrowseMenu"),
    BROWSE("BrowseShow", BROWSE_MENU),
    //BROWSE_WORK_TREE("css=div#BrowseMenu a[href*='WorkTree.page']"),
    //BROWSE_MEASURES_LIBRARY("css=div#BrowseMenu [href*=MeasureTemplates]"),
    //BROWSE_MANAGE_TIMESHEET("css=div#BrowseMenu [href*=Awaiting]"),

    BROWSE_LINK("//div[@id='BrowseMenu']//a[contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]"),
    BROWSE_WORK_TREE_LINK("link=Work Tree", "link=Work tree"),
    BROWSE_CREATE_NEW_WORK_LINK("link=Create New Work", "link=Create a project"),
    BROWSE_CREATE_NEW_ORG_LINK("id=createOrganizationLinkInBrowseMenu"),
    BROWSE_MEASURES_LIBRARY_LINK("link=Measures Library"),
    BROWSE_PEOPLE_MANAGE_TIME_LINK("link=Manage Time", "link=Manage time"),
    BROWSE_PEOPLE_LINK("link=People", "css=div#BrowseMenu [href*=Active]"),
    BROWSE_RESOURCE_RATES("link=Resource rates"),
    BROWSE_RESOURCE_POOLS("link=Resource Pools"),
    BROWSE_RESOURCE_REVIEW_LINK("link=Resource Review", "link=Resource review"),
    BROWSE_MY_PROFILE_TIMESHEETS_LINK("css=div#BrowseMenu [href*=Timesheets]"),
    BROWSE_MY_PROFILE_AGENDA_LINK("link=Agenda"),
    //BROWSE_PUBLIC_REPORTS_LINK("css=div#BrowseMenu a:contains('Public Reports')", "css=div#BrowseMenu a:contains('Public reports')"),
    BROWSE_PUBLIC_REPORTS("Public Reports", "Public reports"),
    BROWSE_MY_REPORTS("My Reports", "My reports"),
    BROWSE_PUBLIC_REPORTS_LINK(BROWSE_LINK.locator.replace(LOCATOR_REPLACE_PATTERN, BROWSE_PUBLIC_REPORTS.locator)),
    BROWSE_MY_REPORTS_LINK(BROWSE_LINK.locator.replace(LOCATOR_REPLACE_PATTERN, BROWSE_MY_REPORTS.locator)),
    BROWSE_CREATE_REPORTS_LINK("link=Create Reports", "link=Create report"),
    BROWSE_IMPORT_EXPORT_METRIC_LINK("link=Metric"),
    BROWSE_ADVANCED_SEARCH_LINK("link=Advanced Search"),
    BROWSE_INVITE_NEW_USER("link=Invite New User", "link=Invite new user"),
    BROWSE_REVIEW_PORTFOLIOS("link=Portfolios"),


    /*
      * Locators for "Admin" menu items
      */
    ADMIN_MENU("id=AdminMenu"),
    ADMIN("AdminShow", ADMIN_MENU),
    ADMIN_TEMPLATES_WORK("css=div#AdminMenu a[href*=Work]"),
    ADMIN_CONFIGURATION_TAGS("css=div#AdminMenu a[href*=TagSets]"),
    ADMIN_CUSTOM_FIELDS("css=div#AdminMenu a[href*=CustomFields]"),
    ADMIN_PROCESSES("css=div#AdminMenu a[href*=Processes]"),
    ADMIN_DEFAULTS("css=div#AdminMenu a[href*=DefaultPermissions]"),
    ADMIN_DEFINE_CATEGORIES("css=div#AdminMenu a[href*=DefinePermissions]"),

    ADMIN_TEMPLATES_WORK_LINK("link=Work"),
    ADMIN_TEMPLATES_METRICS_LINK("link=Metrics"),
    ADMIN_CONFIGURATION_TAGS_LINK("link=Tags"),
    ADMIN_CONFIGURATION_CUSTOM_FIELDS_LINK("link=Custom Fields"),
    ADMIN_CONFIGURATION_PROCESSES_LINK("link=Processes"),
    ADMIN_PERMISSIONS_DEFAULTS_LINK("link=Defaults"),
    ADMIN_PERMISSIONS_DEFINE_CATEGORIES_LINK("link=Define Categories"),

    ADMIN_AGENTS_LINK("link=Agents"),

    ADMIN_OBJECT_TYPES_LINK("link=Object types"),

    MENU_STYLE("style"),
    MENU_STYLE_OPACITY("opacity: 1"), // only for ff and sf, not for ie.
    ;

    String locator;
    MainMenuLocators body;


    MainMenuLocators(String loc92, String loc93) {
        this(TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3) ? loc92 : loc93);
    }

    private MainMenuLocators(String locator) {
        this.locator = locator;
    }


    MainMenuLocators(String locator, MainMenuLocators body) {
        this(locator);
        this.body = body;
    }

    public String getLocator() {
        return locator;
    }

    public ILocatorable getBody() {
        return body;
    }

    public boolean isAdmin() {
        return name().startsWith("ADMIN");
    }

    public String getName() {
        return (isAdmin() ? "Admin>" : "Browse>") + name().replace("BROWSE_", "").replace("ADMIN_", "").replace("_LINK", "").toLowerCase().replace("_", " ");
    }


}
