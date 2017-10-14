package com.powersteeringsoftware.libs.enums.page_locators;

import static com.powersteeringsoftware.libs.enums.page_locators.LeftNavLocators.Storage.*;

/**
 * since 11.0 ver
 */
public enum LeftNavMainMenuLocators implements LeftNavLocators {
    MENU_TITLE("//div[@class='title']"),
    MENU_BAR("MenuContentWrapper"),
    PLUS_CLASS("ps-ext-plus-circle"),
    MINUS_CLASS("ps-ext-minus-circle"),

    WORK_TREE(PROJECT, "Work Tree", toDivIdLinkLoc("ps_work_tree")),
    TEMPLATES("Templates", toDivTitleLoc("Templates")),
    WORK_TEMPLATES(ADMIN, TEMPLATES, "Work", toDivIdLinkLoc("ps_work_template")),
    METRIC_TEMPLATES(ADMIN, TEMPLATES, "Metrics", toDivIdLinkLoc("ps_metric_template")),
    CONFIGURATION("Configuration", toDivTitleLoc("Configuration")),
    CONFIGURATION_TAGS(ADMIN, CONFIGURATION, "Tags", toDivIdLinkLoc("ps_adm_tags")),
    CONFIGURATION_CUSTOM_FIELDS(ADMIN, CONFIGURATION, "Custom Fields", toDivIdLinkLoc("ps_adm_cf")),
    CONFIGURATION_PROCESSES(ADMIN, CONFIGURATION, "Processes", toDivIdLinkLoc("ps_adm_processes")),
    CONFIGURATION_AGENTS(ADMIN, CONFIGURATION, "Agents", toDivIdLinkLoc("ps_agents")),
    CONFIGURATION_OBJECT_TYPES(ADMIN, CONFIGURATION, "Object types", toDivIdLinkLoc("ps_adm_object_types")),

    PERMISSIONS("Permissions", toDivTitleLoc("Permissions")),
    PERMISSIONS_DEFAULTS(ADMIN, PERMISSIONS, "Defaults", toDivIdLinkLoc("ps_def_perms")),
    PERMISSIONS_DEFINE_CATEGORIES(ADMIN, PERMISSIONS, "Define Categories", toDivIdLinkLoc("ps_custom_perms")),

    ADD_REPORT("Report", toDivTitleLoc("Report")),
    CREATE_REPORTS(ADD, ADD_REPORT, "Create Reports", toDivIdLinkLoc("ps_add_report")),
    ADD_PROJECT(ADD, "Project", toDivIdLinkLoc("ps_create_work0")),//"id=createNewWorkLinkInBrowseMenu"),
    ADD_ORG(ADD, "Organization", toDivIdLinkLoc("ps_create_work1")),
    ADD_USER(ADD, "User", toDivIdLinkLoc("ps_add_user")),

    REVIEW_REPORTS("Reports", toDivTitleLoc("Reports")),
    MY_REPORTS(REVIEW, REVIEW_REPORTS, "My Reports", toDivIdLinkLoc("ps_reports_folder1")),
    PUBLIC_REPORTS(REVIEW, REVIEW_REPORTS, "Public Reports", toDivIdLinkLoc("ps_reports_folder2")),
    USER_MANAGEMENT("User Management", toDivTitleLoc("User Management")),
    MANAGE_TIME(REVIEW, USER_MANAGEMENT, "Manage Time", toDivIdLinkLoc("ps_awaiting")),
    PEOPLE(REVIEW, USER_MANAGEMENT, "Find A Person", toDivIdLinkLoc("ps_find_person")),
    RESOURCE_RATES(REVIEW, USER_MANAGEMENT, "Resource Rates", toDivIdLinkLoc("ps_people_resources")),
    RESOURCE_POOL(REVIEW, USER_MANAGEMENT, "Resource Pool", toDivIdLinkLoc("ps_res_pool")),
    MEASURE_LIBRARY(REVIEW, "Measure Library", toDivIdLinkLoc("measure_templates")),
    RESOURCE_REVIEW(REVIEW, "Resource Review", toDivIdLinkLoc("resource_review")),
    PORTFOLIOS(REVIEW, "Portfolios", toDivIdLinkLoc("portfolios")),
    IMPORT_EXPORT("Import/Export", toDivTitleLoc("Import/Export")),
    IMPORT_EXPORT_METRICS(REVIEW, IMPORT_EXPORT, "Metrics", toDivIdLinkLoc("ps_metric_load")),;
    private String locator;
    private String name;
    private Storage category;
    private LeftNavMainMenuLocators parent;

    private LeftNavMainMenuLocators(String name, String loc) {
        this(loc);
        this.name = name;
    }


    LeftNavMainMenuLocators(Storage category, String name, String loc) {
        this(name, loc);
        this.category = category;
    }

    LeftNavMainMenuLocators(Storage category, LeftNavMainMenuLocators parent, String name, String loc) {
        this(category, name, loc);
        this.parent = parent;
    }

    LeftNavMainMenuLocators(String loc) {
        locator = loc;
    }

    public Storage getStorage() {
        return category;
    }

    public LeftNavLocators getParent() {
        return parent;
    }

    public static String getParentOpenLocator(LeftNavLocators loc) {
        //String id = loc.getLocator().replace(Locators.ID_LOCATOR_PATTERN, "");
        //return toDivIdSpanLoc(id);
        return loc.getLocator() + "/span";
    }

    public String getName() {
        return (parent == null ? "" : (parent.getName() + ">")) + (name != null ? name : name().toLowerCase());
    }

    public String toString() {
        return category == null ? super.toString() : (category.getName() + ">" + getName());
    }

    @Override
    public String getLocator() {
        return locator;
    }

}
