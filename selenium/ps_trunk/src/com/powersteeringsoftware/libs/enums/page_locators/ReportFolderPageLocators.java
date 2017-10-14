package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

public enum ReportFolderPageLocators implements ILocatorable {
    URL("/reports/ReportFolder.epage"),
    TITLE("//div[@class='container']//div[@class='left']"),
    ADD_FOLDER_LINK("//a[@id='popOptions']"),
    ADD_FOLDER_MENU_ADD("Add Subfolder"),
    ADD_FOLDER_MENU_DELETE("Delete Folder"),
    LIST_FOLDER_LINK("//a[@id='BrowseFoldersShow']"),
    ADD_FOLDER_DIALOG("id=popAdd"),
    REMOVE_FOLDER_DIALOG("id=popDelete"),
    LIST_FOLDER_DIALOG("id=BrowseFolders"),
    LIST_FOLDER_CANCEL_BUTTON("id=BrowseFoldersHide"),
    FOLDER_NAME_INPUT("id=TextField"),
    FOLDER_ADD_BUTTON("//input[@value='Add']"),
    FOLDER_REMOVE_BUTTON("//input[@value='Delete']"),
    REPORT_TABLE_LOCATOR("id=PSTable"),
    REPORT_TABLE_ROWS_LOCATOR("//tr[@class]"),
    DELETE_COLUMN_HEADER("id=deleteColumnHeader"),
    DELETE_IMG("//img[@alt='Delete']"),
    FORM_LOCATOR("id=PSForm"),
    TB_FOLDER_REMOVE_BUTTON("//div[contains(@class, 'toolbar-top')]/div[last()]/input[@value='Delete']"),

    SAVE_BANNER("//div[contains(@class, 'BlueBoxBorderFull')]"),
    SAVE_BANNER_LAST_EDITED("popLastEditedReport"),
    SAVE_BANNER_RUN_IT_NOW("popRunLastEditedReport"),
    SAVE_BANNER_EDIT_REPORT("//a[contains(text(), 'edit it in the Report Wizard')]"),
    SAVE_BANNER_CREATE_ANOTHER_REPORT("//a[contains(text(), 'create another report')]"),

    //REPORT_NAME_LOCATOR("//table[@id='PSTable']/tbody/tr[2]/td/div"),
    LAST_LINK("linkLast"),
    MORE_LESS_IMG("paginateViewToggle"),
    REPORT_NAME_LOCATOR("//td[@class='nameColumnValue']/div"),
    CURRENT_TAB_LINK("//li[@class='hl']/a"),
    MY_REPORTS_TAB_NAME("My Reports"),
    PUBLIC_REPORTS_TAB_NAME("Public Reports"),
    TAB_LOCATOR("//li/a[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
    MY_REPORTS(TAB_LOCATOR.replace(MY_REPORTS_TAB_NAME.locator)),
    PUBLIC_REPORTS(TAB_LOCATOR.replace(PUBLIC_REPORTS_TAB_NAME.locator)),

    MENU_RUN_CSV("CSV"),
    MENU_RUN_EXEL("Excel"),
    MENU_RUN_EXEL2007("Excel 2007"),
    MENU_RUN_HTML("HTML"),
    MENU_RUN_PDF("PDF"),
    MENU_RUN_POWERPOINT2007("PowerPoint 2007"),
    MENU_RUN_RTF("RTF"),
    MENU_RUN_WORD2007("Word 2007"),
    MENU_COPY("Copy"),
    MENU_EDIT("Edit Details & Schedule"),
    MENU_EDIT_FILTERS("Edit Filters"),
    MENU_REPORT_WIZARD("Report Wizard"),

    // todo: do not use such locators
//    RUN_REPORT_9_2_LOCATOR("//div[14]/div/div/ul/li[2]/a"),
//    RUN_REPORT_LOCATOR("//div[14]/div/div/ul/li[5]/a"),
//    COPY_REPORT_LOCATOR("//div[14]/div/div/ul/li[9]/a"),
//    EDIT_DETAILS_LOCATOR("//div[14]/div/div/ul/li[10]/a"),
//    EDIT_FILTERS_LOCATOR("//div[14]/div/div/ul/li[11]/a"),
//    REPORT_WIZARD_LOCATOR("//div[14]/div/div/ul/li[12]/a"),

    ;

    ReportFolderPageLocators(String locator) {
        this.locator = locator;
    }

    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    private String locator;
} // enum ReportFolderPageLocators  
