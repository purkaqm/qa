package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

public enum ReportWizardPageLocators implements ILocatorable {
    URL("/reports/pointandclick/Wizard"),
    TOP_BAR_LOCATOR("//div[@id='subtabnav']", "//div[@id='sub' and @ class='clearfix']"),
    TAB_BAR_LINK("//ul/li/*"),
    CURRENT_BAR_TAB_LINK("//ul/li[@class='active']/*", "//ul/li[@class='hl']/a"),
    PAGE_CONTINUE_BUTTON("//input[@class='btn' and contains(@value,'Continue')]"),
    PREVIEW_LINK("link=Preview"),
    RUN_LINK("//a[@id='runOptions']", "//input[@id='runOptions']"),
    MENU_RUN_CSV("CSV"),
    MENU_RUN_EXEL("Excel"),
    MENU_RUN_EXEL2007("Excel 2007"),
    MENU_RUN_HTML("HTML"),
    MENU_RUN_PDF("PDF"),
    MENU_RUN_POWERPOINT2007("PowerPoint 2007"),
    MENU_RUN_RTF("RTF"),
    MENU_RUN_WORD2007("Word 2007"),
    PAGE_SAVE_BUTTON("//input[@class='btn' and @value='Save']"),
    PAGE_CANCEL_BUTTON("//input[@value='Cancel' and @type='submit']"),
    PAGE_SAVE_AS_BUTTON("//input[@class='btn' and @value='Save as']"),
    TOP_PANE_LOCATOR("css=#content div.toolbar-top"),;

    ReportWizardPageLocators(String locator) {
        this.locator = locator;
    }

    ReportWizardPageLocators(String loc100, String loc110) {
        this(TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._10_0) ? loc100 : loc110);
    }


    @Override
    public String getLocator() {
        return locator;
    }

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

    private String locator;

    public enum RWTab implements ILocatorable {
        Type("Type"),
        Definition("Definition"),
        Columns("Columns"),
        Filter("Filter"),
        GroupSort("Group/Sort"),
        Summary("Summary"),
        Layout("Layout"),
        Chart("Chart"),
        Save("Save", "Details & Schedule"),;


        RWTab(String tabTitle) {
            this.tabTitle = tabTitle;
        }

        RWTab(String loc92, String loc93) {
            this(TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2) ? loc92 : loc93);
        }


        public String getTitle() {
            return tabTitle;
        }

        public String getLocator() {
            return "link=" + getTitle();
        }

        public static RWTab getByTitle(String tabTitle) {
            if (tabTitle == null) return null;
            for (RWTab tab : RWTab.values()) {
                if (tab.getTitle().equals(tabTitle)) return tab;
            }
            return null;
        }

        private String tabTitle;
    }
} // enum ReportWizardPageLocators
