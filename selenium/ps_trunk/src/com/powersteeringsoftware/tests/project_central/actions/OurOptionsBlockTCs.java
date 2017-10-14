package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Class for actions with Options
 * User: szuev
 * Date: 26.05.2010
 * Time: 17:03:47
 */
public class OurOptionsBlockTCs extends GeneralOptionsBlockTCs {

    public void testUncheckAllDisplayOptions(Work root) {
        WBSPage pc = WorkManager.openWBS(root.getChild(0));

        List<String> columns = pc.getGrid().getTableHeaderList();
        Assert.assertNotNull(pc.getGantt().checkHeader(), "Can't find gantt section before unchecking");

        PSLogger.info("before unchecking: " + columns);
        WBSPage.Columns display = pc.getOptions().getColumns();
        List<CheckBox> list = display.getCheckedCheckboxes();
        PSLogger.info("to uncheck: " + list);
        display.uncheckAll();
        display.apply();
        pc.hideGantt(); // this is for 9.0, for 8.2 gantt should be unselected before.

        columns = pc.getGrid().getTableHeaderList();
        PSLogger.info("after unchecking: " + columns);
        Assert.assertTrue(columns.isEmpty(), "Incorrect columns list, should be empty after unchecking");
        Assert.assertNull(pc.getGantt().checkHeader(), "There is a gantt section after unchecking");

        pc.getGrid().openWBSPageByDisplayFromParent(root.getChild(0));
        Assert.assertTrue(pc.getContainerHeader().equals(root.getName()), "Can't open parent");

        columns = pc.getGrid().getTableHeaderList();
        PSLogger.info("on parent: " + columns);
        Assert.assertTrue(columns.isEmpty(), "Incorrect columns list, should be empty on parent, after unchecking");
        Assert.assertNull(pc.getGantt().checkHeader(), "There is gantt section after unchecking on parent");

        pc.getGrid().openWBSPageByDisplayFromHere(root.getChild(0));
        Assert.assertTrue(pc.getContainerHeader().equals(root.getChild(0).getName()),
                "Can't open child");
        Assert.assertNull(pc.getGantt().checkHeader(), "There is gantt section after unchecking on child");

        PSLogger.info("Open again project central");
        pc = WorkManager.openWBS(root);
        columns = pc.getGrid().getTableHeaderList();
        PSLogger.info("on parnet " + root.getName() +
                ": " + columns);
        Assert.assertFalse(columns.isEmpty(), "There are not any columns");

    }

    /**
     * @param optionId    - config id
     * @param compareMode - if 0 then columns must exactly match chekboxes
     *                    - if 1 then number of columns must be greater than the number of chekboxes
     */
    public void testCheckDisplayOption(Work root, WBSEPageLocators optionId, int compareMode) {
        WBSPage pc = WorkManager.openWBS(root.getChild(0));

        WBSPage.Columns columns = pc.getOptions().getColumns();
        List<CheckBox> checkboxes = columns.getCheckboxes();
        PSLogger.info("All details checkboxes: " + checkboxes);
        CheckBox project = columns.getCheckbox(optionId);
        Assert.assertNotNull(project, "Can't find " + optionId + " checkbox");
        PSLogger.info("Uncheck all");
        columns.uncheckAll();
        PSLogger.info("Check on " + optionId.getLocator());
        project.click();

        List<CheckBox> checkedOn = columns.getCheckedCheckboxes();

        Assert.assertTrue(checkedOn.size() > 1, "There are less than " + 1 + " checked checkboxes: " + checkedOn);
        Assert.assertTrue(checkedOn.size() < OPTIONS_BLOCK_DISPLAY_CHEKBOXES_NUMBER.getInt() + 1,
                "There are more than " + OPTIONS_BLOCK_DISPLAY_CHEKBOXES_NUMBER.getInt() +
                        " chekced checkboxes: " + checkedOn.size() + ", " + checkedOn
        );

        List<String> before = pc.getGrid().getTableHeaderList();
        columns.apply();
        List<String> after = pc.getGrid().getTableHeaderList();

        Assert.assertFalse(before.equals(after), "List of columns is the same before and after apply: " + before);

        if (compareMode == 0) {
            Assert.assertEquals(after.size(), checkedOn.size() - 1,
                    "Incorrect list after apply, should be " + checkedOn + ", but really " + after);
            for (CheckBox ch : checkedOn) {
                if (ch.getName().equals(optionId.getLocator())) {
                    continue;
                }
                Assert.assertTrue(after.contains(ch.getName()), "Can't find " + ch.getName() + " in columns list");
            }
        } else if (compareMode == 1) {
            Assert.assertTrue(after.size() > checkedOn.size() - 1,
                    "Incorrect list after apply, should be " + checkedOn + ", but really " + after);
        }

        PSLogger.info("Validate on parent");
        pc.getGrid().openWBSPageByDisplayFromParent(root.getChild(0));
        Assert.assertTrue(pc.getContainerHeader().equals(root.getName()), "Can't open parent");
        List<String> after2 = pc.getGrid().getTableHeaderList();
        try {
            Assert.assertTrue(after.equals(after2),
                    "List of columns is incorrect on parent, should be " + after + ", but really " + after2);
        } catch (AssertionError e) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) && CoreProperties.getBrowser().isIE(8)) {
                throw new PSKnownIssueException(74085, e);
            } else {
                throw e;
            }
        }

        pc = WorkManager.openWBS(root.getChild(0));
        List<String> after3 = pc.getGrid().getTableHeaderList();
        Assert.assertFalse(after.equals(after3),
                "List of columns is the same after reopen project central: " + after);

    }

    public void testTagDisplayOption(Work root, PSTag t1, PSTag t2) {
        WBSPage pc = WorkManager.openWBS(root.getChild(0));
        List<String> before = pc.getGrid().getTableHeaderList();
        WBSPage.Columns columns = pc.getOptions().getColumns();

        List<CheckBox> checkboxes = columns.getCheckboxes();
        PSLogger.info("All details checkboxes: " + checkboxes);
        columns.uncheckAll();
        // search project chekboxes:
        CheckBox autoTag1;
        CheckBox autoTag2;
        String tgName1 = t1.isInResources() ? _OPTIONS_BLOCK_COLUMNS_TAG_WORK_NAME.replace(t1.getName()) : t1.getName();
        String tgName2 = t2.isInResources() ? _OPTIONS_BLOCK_COLUMNS_TAG_WORK_NAME.replace(t2.getName()) : t2.getName();
        Assert.assertNotNull(columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_TAGS), "Can't find " + OPTIONS_BLOCK_COLUMNS_TAGS.getLocator() + " checbox");
        Assert.assertNotNull(autoTag1 = columns.getCheckbox(tgName1), "Can't find " + tgName1 + " checkbox");
        Assert.assertNotNull(autoTag2 = columns.getCheckbox(tgName2), "Can't find " + tgName2 + " checkbox");
        PSLogger.info("Check '" + autoTag1.getName() + "'");
        columns.getCheckbox(autoTag1.getName()).click();
        columns.apply();

        List<String> after = pc.getGrid().getTableHeaderList();
        Assert.assertFalse(before.equals(after), "List of columns is the same before and after apply: " + before);
        Assert.assertTrue(after.size() == 1 && after.contains(autoTag1.getName()),
                "Can't find " + autoTag1.getName() + " in columns");

        columns = pc.getOptions().getColumns();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_TAGS).click();
        columns.apply();
        List<String> after2 = pc.getGrid().getTableHeaderList();
        Assert.assertFalse(after.equals(after2),
                "List of columns is the same before and after apply with selected all tags: " + after);
        Assert.assertTrue(after2.size() > 1 && after2.contains(autoTag1.getName()) && after2.contains(autoTag2.getName()),
                "Can't find " + autoTag1.getName() + " or " + autoTag2.getName() + " in columns");
    }

    public void testFilterNameExactly(Work root, List<String> t) {
        WBSEPageLocators.LocatorsList custom = null;
        WBSPage pc = WorkManager.openWBS(root);
        if (!CoreProperties.getDefaultUser().equals(BasicCommons.getCurrentUser())) {
            custom = MENU_POPUP_CUSTOM_DISABLED; // if it is not admin there is another list of items
        }

        List<String> fromPageList0 = pc.getGrid().getListTree();
        PSLogger.info("Check that no works on page if set " + t.get(0) + " to filter");
        WBSPage.OptionsBlock.Filter filter = pc.getOptions().getFilter();
        filter.setName(0, t.get(0));
        filter.apply();

        List<String> fromPageList1 = pc.getGrid().getListTree();
        Assert.assertTrue(fromPageList1.isEmpty(), "Found some works on page: " + fromPageList1);

        for (String toSearch : new String[]{t.get(1),
                t.get(2)}) {
            PSLogger.info("Search by '" + toSearch + "'");
            filter = pc.getOptions().getFilter();
            filter.setName(0, toSearch);
            filter.apply();
            List<String> fromPageList2 = pc.getGrid().getListTree();
            Assert.assertTrue(fromPageList2.size() == 1 && fromPageList2.contains(toSearch),
                    "Incorrect result of searching: " + fromPageList2);
            List<String> menuItems = pc.getGrid().callSubMenu(toSearch).getMenuItems();
            List<String> expected = MENU_POPUP_FILTER.get(custom);
            PSLogger.info("Menu items from page: " + menuItems);
            PSLogger.info("Menu items expected: " + expected);
            Assert.assertEquals(menuItems, expected, "Incorrect submenu items after filter: should be " + expected);
        }

        PSLogger.info("Check Reset");
        filter = pc.getOptions().getFilter();
        filter.reset();
        List<String> fromPageList3 = pc.getGrid().getListTree();
        Assert.assertEquals(fromPageList3, fromPageList0,
                "Incorrect result after resetting, should be " + fromPageList0);

        PSLogger.info("Check menu after resetting");
        List<String> menuItemsRoot = pc.getGrid().callSubMenu().getMenuItems();

        List<String> expected = MENU_POPUP_ROOT.get(custom);
        PSLogger.info("Menu root items from page: " + menuItemsRoot);
        PSLogger.info("Menu items expected: " + expected);
        Assert.assertEquals(menuItemsRoot, expected, "Incorrect submenu root items: should be " + expected);

        List<String> menuItemsChild1 = pc.getGrid().callSubMenu(root.getChild(0).getName()).getMenuItems();
        expected = MENU_POPUP_CHILD_1_1.get(custom);
        PSLogger.info("Menu child items from page: " + menuItemsChild1);
        PSLogger.info("Menu items expected: " + expected);
        Assert.assertEquals(menuItemsChild1, expected, "Incorrect submenu child items: should be " + expected);

        List<String> menuItemsChild2 = pc.getGrid().callSubMenu(root.getChild(1).getName()).getMenuItems();
        expected = MENU_POPUP_CHILD_1_2.get(custom);
        PSLogger.info("Menu child items from page: " + menuItemsChild2);
        PSLogger.info("Menu items expected: " + expected);
        Assert.assertEquals(menuItemsChild2, expected, "Incorrect submenu child items: should be " + expected);

    }

    public void testFilterNameAnyOf(Work root, List<String> t) {
        WBSPage pc = WorkManager.openWBS(root);

        List<String> fromPageList0 = pc.getGrid().getListTree();

        String text;
        PSLogger.info("Check that no works on page if set " + (text = t.get(0)) +
                " to any-of filter");
        WBSPage.OptionsBlock.Filter options = pc.getOptions().getFilter();
        options.setName(1, text);
        options.apply();
        List<String> list = pc.getGrid().getListTree();
        Assert.assertTrue(list.isEmpty(), "Found some works on page: " + list + ". Test string is " + text);

        PSLogger.info("Search by '" + (text = t.get(1)) + "'");
        options = pc.getOptions().getFilter();
        options.setName(1, text);
        options.apply();
        List<String> fromPageList1 = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + fromPageList1);
        List<String> expectedList1 = new ArrayList<String>(Arrays.asList(root.getChild(1).getName(),
                root.getChild(2).getName()));
        Assert.assertTrue(fromPageList1.equals(expectedList1),
                "Incorrect result of searching by " + text + ", should be " + expectedList1);

        PSLogger.info("Search by '" + (text = t.get(2)) + "'");
        options = pc.getOptions().getFilter();
        options.setName(1, text);
        options.apply();
        List<String> fromPageList2 = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + fromPageList2);
        List<String> expectedList2 = new ArrayList<String>(Arrays.asList(root.getName()));
        Assert.assertTrue(fromPageList2.equals(expectedList2),
                "Incorrect result of searching by " + text + ", should be " + expectedList2);

        PSLogger.info("Check Reset");
        pc.getOptions().getFilter().reset();
        List<String> fromPageList3 = pc.getGrid().getListTree();
        Assert.assertTrue(fromPageList3.equals(fromPageList0),
                "Incorrect result after resetting, should be " + fromPageList0);

    }


    public void testFilterNameAllOf(Work root, List<String> t) {
        WBSPage pc = WorkManager.openWBS(root);

        List<String> fromPageList0 = pc.getGrid().getListTree();

        for (String filter : new String[]{
                t.get(0),
                t.get(1)}) {
            PSLogger.info("Check that no works on page if set " + filter + " to all-of filter");
            WBSPage.OptionsBlock.Filter options = pc.getOptions().getFilter();
            options.setName(2, filter);
            options.apply();
            List<String> list = pc.getGrid().getListTree();
            Assert.assertTrue(list.isEmpty(), "Found some works on page after setting filter " + filter +
                    ": " + list);
        }

        PSLogger.info("Search by '" + t.get(2) + "'");
        WBSPage.OptionsBlock.Filter options = pc.getOptions().getFilter();
        options.setName(2, t.get(2));
        options.apply();
        List<String> fromPageList1 = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + fromPageList1);
        Assert.assertTrue(fromPageList1.size() == 1 &&
                        fromPageList1.contains(root.getChild(1).getName()),
                "Incorrect result of searching, should be " + root.getChild(1).getName()
        );

        PSLogger.info("Check Reset");
        pc.getOptions().getFilter().reset();
        List<String> fromPageList3 = pc.getGrid().getListTree();
        Assert.assertTrue(fromPageList3.equals(fromPageList0),
                "Incorrect result after resetting, should be " + fromPageList0);

    }

    public void testFilterPool(Work root, ResourcePool pool) {
        List<ResourcePool> pools = new ArrayList<ResourcePool>();
        pools.add(pool);
        testFilterByResource(root, pools);
    }

    public void testFilterRole(Work root, List<Role> roles) {
        testFilterByResource(root, roles);
    }

    /**
     * @param root
     * @param resource
     */
    public void testFilterByResource(Work root, List resource) {
        boolean isRole = resource.get(0) instanceof Role;
        Map<Object, ArrayList<String>> resourceWorks = new LinkedHashMap<Object, ArrayList<String>>();
        resourceWorks.put(null, new ArrayList<String>());
        for (Object role : resource) {
            resourceWorks.put(role, new ArrayList<String>());
        }
        List<Work> works = new ArrayList<Work>(root.getAllChildren(false, true));
        works.add(0, root);
        for (Work work : works) {
            Object role = isRole ? work.getResource().getRole() : work.getResource().getPool();
            String name = work.getName();
            resourceWorks.get(role).add(name);
            if (isRole && role != null && work.hasSplitResources()) {
                resourceWorks.get(null).add(name); //#90632
            }
        }

        PSLogger.info("Resource/Works : " + resourceWorks);
        WBSPage pc = WorkManager.openWBS(root);

        //to debug:
        /*WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_RESOURCE_ROLE).click(); // splits are displayed because of resource role.
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_RESOURCE_POOL).click();
        columns.apply();*/

        PSLogger.info("Search works with 'Any values'");
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        if (isRole)
            filters.setRole(2, null);
        else
            filters.setResourcePool(2, null);
        filters.apply();
        List<String> fromPage1 = pc.getGrid().getListTree();
        Collections.sort(fromPage1);
        List<String> expected1 = new ArrayList<String>();
        for (Object res : resourceWorks.keySet()) {
            Collections.sort(resourceWorks.get(res));
            if (res != null) expected1.addAll(resourceWorks.get(res));
        }
        Collections.sort(expected1);
        PSLogger.info("From page: " + fromPage1);
        PSLogger.info("Expected: " + expected1);
        Assert.assertEquals(fromPage1, expected1,
                "Incorrect result of searching by 'Any values', should be " + expected1);

        PSLogger.info("Search works with 'No values'");
        filters = pc.getOptions().getFilter();
        if (isRole)
            filters.setRole(1, null);
        else
            filters.setResourcePool(1, null);
        filters.apply();
        List<String> fromPage2 = pc.getGrid().getListTree();
        Collections.sort(fromPage2);
        List<String> expected2 = new ArrayList<String>(resourceWorks.get(null));
        Collections.sort(expected2);
        PSLogger.info("From page: " + fromPage2);
        PSLogger.info("Expected: " + expected2);
        Assert.assertEquals(fromPage2, expected2,
                "Incorrect result of searching by 'No values', should be " + expected2);

        PSLogger.info("Search works by 'Specified values'");
        for (Object res : resourceWorks.keySet()) {
            if (res == null) continue;
            filters = pc.getOptions().getFilter();
            if (isRole)
                filters.setRole(3, ((Role) res).getName());
            else
                filters.setResourcePool(3, ((ResourcePool) res).getName());
            filters.apply();
            List<String> fromPage3 = pc.getGrid().getListTree();
            Collections.sort(fromPage3);
            PSLogger.info("From page: " + fromPage3);
            PSLogger.info("Expected: " + resourceWorks.get(res));
            Assert.assertEquals(fromPage3, resourceWorks.get(res),
                    "Incorrect result of searching by '" + res +
                            "', should be " + resourceWorks.get(res)
            );
        }
        pc.getOptions().getFilter().reset();
    }

    public void simpleValidateLayouts(TestData data, Work root) {
        List<Work> works = new ArrayList<Work>(root.getAllChildren(false, true));
        works.add(0, root);
        simpleValidateLayouts(data,
                works.remove(TestData.getRandom().nextInt(works.size())),
                works.remove(TestData.getRandom().nextInt(works.size())),
                works.remove(TestData.getRandom().nextInt(works.size())),
                works.remove(TestData.getRandom().nextInt(works.size()))
        );
    }
}
