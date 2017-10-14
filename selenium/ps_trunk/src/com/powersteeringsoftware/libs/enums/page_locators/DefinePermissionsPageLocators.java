package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.libs.objects.PSPermissions.BasicTarget;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.07.2010
 * Time: 13:15:09
 */
public enum DefinePermissionsPageLocators implements ILocatorable {

    URL("admin/DefinePermissions"),
    SELECTOR("id=PropertySelection"),
    PROPERTIES("//table[@id='contentTable']//tr/td[1]//table//th", "//div[@id='DefinePermissions_Left']//tr/th/acronym"),
    ROLES("//table[@id='contentTable']//tr/td[2]//table//tr[1]//th/center", "//div[@id='containerId_Header']//th/center"),
    ROLE_LINK("//div[@class='link']"),
    CHECKBOX("//table[@id='contentTable']//tr/td[2]//tr[" + LOCATOR_REPLACE_PATTERN + "]/th[" + LOCATOR_REPLACE_PATTERN_2 + "]//input",
            "//div[@id='containerId']//tr[" + LOCATOR_REPLACE_PATTERN + "]/th[" + LOCATOR_REPLACE_PATTERN_2 + "]//input"),
    CHECKBOX_CHECKED_ATTR("checked"),
    CHECKBOX_CHECKED_VAL("checked"),
    UPDATE("//input[@value='Update']"),
    ADD_NEW_CATEGORY("//button[contains(text(), 'Add New')]", "//button[contains(text(), 'Add category column')]"),
    ADD_NEW_CATEGORY_TITLE("Add New Category"),
    ADD_NEW_CATEGORY_POPUP("id=AddNewCategory"),
    ADD_NEW_CATEGORY_POPUP_NAME("name"),
    ADD_NEW_CATEGORY_POPUP_OBJ_TYPE("PropertySelection_0"),
    ADD_NEW_CATEGORY_POPUP_DESCRIPTION("description"),
    ADD_NEW_CATEGORY_POPUP_SEQUENCE("sequence"),
    ADD_NEW_CATEGORY_POPUP_CANCEL("AddNewCategoryHide"),
    ADD_NEW_CATEGORY_POPUP_SUBMIT("//input[@value='Add']"),

    DIV_LINK("//div[@class='link']"),
    NEW_CATEGORY_LINK("//div[@class='link' and contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]"),
    NEW_CATEGORY_LINK_ID("Show"),
    NEW_CATEGORY_DELETE_IMG("//img"),

    DELETE_CATEGORY_POPUP("//div[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
    DELETE_CATEGORY_POPUP_TITLE("Deleting Permission Category"),
    DELETE_CATEGORY_POPUP_OK("//input[@value='Ok']"),
    DELETE_CATEGORY_POPUP_CANCEL("//input[@value='Cancel']"),

    VERTICAL_SCROLL_BAR_94("DefinePermissions_Left"),
    CONTAINER("containerId");
    private String loc;
    private static PowerSteeringVersions version;

    private static PowerSteeringVersions getVersion() {
        if (version != null) return version;
        return version = TestSession.isVersionPresent() ? TestSession.getAppVersion() : null;
    }

    DefinePermissionsPageLocators(String s) {
        loc = s;
    }

    DefinePermissionsPageLocators(String loc93, String loc94) {
        loc = PowerSteeringVersions._9_4.verGreaterThan(getVersion()) ? loc93 : loc94;
    }

    public String getLocator() {
        return loc;
    }

    public String replace(Object rep) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep));
    }

    public String replace(Object rep1, Object rep2) {
        return loc.replace(LOCATOR_REPLACE_PATTERN, String.valueOf(rep1)).replace(LOCATOR_REPLACE_PATTERN_2, String.valueOf(rep2));
    }

    public static interface IRow extends ILocatorable {
        public String name();
    }

    public static List<IRow> getPermissions() {
        List<IRow> res = new ArrayList<IRow>();
        res.addAll(Arrays.asList(WorkItemsRow.values()));
        res.addAll(Arrays.asList(ContextsRow.values()));
        res.addAll(Arrays.asList(ManageTimeAndBillingRow.values()));
        return res;
    }

    public static enum WorkItemsRow implements IRow {
        VIEW("VIEW"),
        EDIT("EDIT"),
        DELETE("DELETE"),
        VIEW_PROJECT_CENTRAL("VIEW PROJECT CENTRAL"),
        VIEW_PERMISSIONS("VIEW PERMISSIONS"),;
        String loc;

        WorkItemsRow(String loc) {
            this.loc = loc;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    public static enum ContextsRow implements IRow {
        SHARE_PORTFOLIOS("SHARE PORTFOLIOS"),
        MEASURE_TEMPLATE("MEASURE TEMPLATE"),
        CREATE_NON_TEMPLATE_WORK("CREATE NON-TEMPLATE WORK", "CREATE ORGANIZATION"),
        CREATE_ROOT_WORK("CREATE ROOT WORK"),;
        String loc;

        ContextsRow(String loc) {
            this.loc = loc;
        }

        ContextsRow(String loc1, String loc2) {
            this.loc = getVersion() != null ? (getVersion().verGreaterOrEqual(PowerSteeringVersions._9_3) ? loc2 : loc1) : null;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    public static enum ManageTimeAndBillingRow implements IRow {
        VIEW_ACCOUNTS("VIEW ACCOUNTS"),
        APPROVE_REJECT_TIMESHEETS("APPROVE/REJECT TIMESHEETS"),
        MANAGE_BILLING("MANAGE BILLING"),
        VIEW_RATE_TABLES("VIEW RATE TABLES"),
        CREATE_EDIT_RATE_TABLES("CREATE/EDIT RATE TABLES"),;
        String loc;

        ManageTimeAndBillingRow(String loc) {
            this.loc = loc;
        }

        @Override
        public String getLocator() {
            return loc;
        }
    }

    public enum SelectorOptions implements ILocatorable {
        USR("Users", BasicTarget.USR),
        CTX("Contexts", BasicTarget.CTX),
        WI("Work Items", BasicTarget.WI),
        GRP("Groups", BasicTarget.GRP),
        MT("Metric Templates", BasicTarget.MT),
        MTB("Manage Time and Billing", "Manage Time", BasicTarget.MTB),;
        private String locator;
        private BasicTarget tr;
        private static final String LABEL = "label=";

        SelectorOptions(String s, BasicTarget tr) {
            locator = s;
            this.tr = tr;
        }

        SelectorOptions(String s1, String s2, BasicTarget tr) {
            locator = PowerSteeringVersions._10_0.verGreaterThan(getVersion()) ? s1 : s2;
            this.tr = tr;
        }

        public String getLocator() {
            return LABEL + locator;
        }

        public String getName() {
            return locator;
        }

        public static SelectorOptions get(BasicTarget name) {
            for (SelectorOptions s : values()) {
                if (s.tr.equals(name)) return s;
            }
            throw new IllegalArgumentException("Can't find specified select option: " + name);
        }

        public static BasicTarget getByLabel(String label) {
            for (SelectorOptions s : values()) {
                if (s.locator.equalsIgnoreCase(label)) return s.tr;
            }
            Assert.fail("Can't find label " + label);
            return null;
        }


    }

}
