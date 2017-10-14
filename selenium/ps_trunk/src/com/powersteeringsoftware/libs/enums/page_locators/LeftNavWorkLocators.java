package com.powersteeringsoftware.libs.enums.page_locators;

import static com.powersteeringsoftware.libs.enums.page_locators.LeftNavLocators.Storage.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 11.10.13
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public enum LeftNavWorkLocators implements LeftNavLocators {
    SUMMARY("Project Summary", toDivIdLinkLoc("ps_summary")),
    EDIT("Edit Details", toDivIdLinkLoc("ps_edit_details")),
    PROJECT_СENTRAL("Project Central", toDivTitleLoc("Project Central")),
    PROJECT_CENTRAL_LAYOUT(PROJECT_СENTRAL, "Layout", toDivTitleLinkLoc(LOCATOR_REPLACE_PATTERN)),
    PROJECT_PLANNING(PROJECT_СENTRAL, "Project planning", toDivTitleLinkLoc("Project planning")),
    RESOURCE_PLANNING(PROJECT_СENTRAL, "Resource planning", toDivTitleLinkLoc("Resource planning")),
    //COLLABORATION("Collaboration", toDivTitleLoc("Collaboration")),
    DISCUSSIONS("Discussions", toDivIdLinkLoc("ps_discussions")),
    ISSUES("Issues", toDivIdLinkLoc("ps_issues")),
    TASKS("Action Items", toDivIdLinkLoc("ps_tasks")),
    RISKS("Risks", toDivIdLinkLoc("ps_risks")),
    COSTS("Costs", toDivTitleLoc("Costs")),
    BUDGET(COSTS, "Budget", toDivIdLinkLoc("ps_budget")),
    ESTIMATED(COSTS, "Estimated Costs", toDivIdLinkLoc("ps_estimated")),
    ACTUAL(COSTS, "Actual Costs", toDivIdLinkLoc("ps_actual")),
    COMPARE(COSTS, "Compare", toDivIdLinkLoc("ps_comparison")),
    METRICS("Metrics", toDivTitleLoc("Metrics")),
    MANAGE_METRICS(METRICS, "Manage Metrics", toDivIdLinkLoc("ps_manage_metrics")),
    METRIC_INSTANCE(METRICS, "Instance", toDivTitleLinkLoc(LOCATOR_REPLACE_PATTERN)),
    MEASURES("Measures", toDivTitleLoc("Measures")),
    MANAGE_MEASURES(MEASURES, "Manage Measures", toDivIdLinkLoc("ps_measures")),
    MEASURE_RECALCULATE(MEASURES, "Recalculate all", toDivTitleLinkLoc("Recalculate all")),
    AVAILABLE_MEASURES(MEASURES, "Available Measures", toDivTitleLinkLoc("Available Measures")),
    MEASURE_INSTANCE(AVAILABLE_MEASURES, "Instance", toDivTitleLinkLoc(LOCATOR_REPLACE_PATTERN)),
    REVERENCES("References", toDivTitleLoc("References")),
    HISTORY("History", toDivIdLinkLoc("ps_pro_history")),

    MORE("More", toDivTitleLoc("More")),
    EDIT_PERMISSIONS(MORE, "Edit Permissions", toDivIdLinkLoc("ps_project_permissions")),
    COPY_MOVE(MORE, "Copy / Move", toDivIdLinkLoc("ps_copy_move")),
    DELETE("Delete", toDivTitleLoc("Delete")),
    ARCHIVE(MORE, "Delete", toDivTitleLoc("Archive")),
    DOCUMENTS("Documents", toDivIdLinkLoc("ps_docs")),;

    LeftNavWorkLocators(LeftNavWorkLocators parent, String name, String loc) {
        this(name, loc);
        this.parent = parent;
    }

    LeftNavWorkLocators(String name, String loc) {
        this(loc);
        this.name = name;
    }

    private String locator;
    private String name;
    private LeftNavWorkLocators parent;

    LeftNavWorkLocators(String loc) {
        locator = loc;
    }

    public Storage getStorage() {
        return PROJECT;
    }

    public LeftNavLocators getParent() {
        return parent;
    }

    public String getName() {
        return (parent == null ? "" : (parent.getName() + ">")) + (name != null ? name : name().toLowerCase());
    }

    public String toString() {
        return PROJECT.getName() + ">" + getName();
    }

    @Override
    public String getLocator() {
        return locator.contains(LOCATOR_REPLACE_PATTERN) ? replace(name) : locator;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String replace(Object o) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(o));
    }
}
