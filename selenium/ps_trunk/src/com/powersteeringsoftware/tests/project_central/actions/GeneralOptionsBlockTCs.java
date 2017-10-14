package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.project_central.Layout;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.Assert;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 14.10.2010
 * Time: 17:01:44
 */
public class GeneralOptionsBlockTCs {
    /**
     * @param works - works to sort
     * @return - new sorted list
     */
    protected List<Work> getWorksSortedByRowIndex(Work... works) {
        List<Work> res = new ArrayList<Work>(Arrays.asList(works));
        Work.sortByRowIndex(res);
        return res;
    }

    /**
     * @param works - list of works
     * @param way   - way to sort: if true, then sort by end date; otherwise - by start date;
     * @return - new sorted list of works
     */
    public List<Work> getWorksSortedByStartOrEndDate(List<Work> works, final boolean way) {
        List<Work> res = new ArrayList<Work>();
        for (Work work : works) { // skip empty dates
            if (way && work.getConstraintEndDate() == null) continue;
            if (!way && work.getConstraintStartDate() == null) continue;
            res.add(work);
        }
        Collections.sort(res, new Comparator() {
            public int compare(Object o1, Object o2) {
                String d1;
                String d2;
                if (way) {
                    d1 = ((Work) o1).getConstraintEndDate();
                    d2 = ((Work) o2).getConstraintEndDate();
                } else {
                    d1 = ((Work) o1).getConstraintStartDate();
                    d2 = ((Work) o2).getConstraintStartDate();
                }
                return PSCalendar.compare(d1, d2);
            }
        });
        return res;
    }

    protected List<String> worksToString(List<Work> works) {
        List<String> res = new ArrayList<String>();
        for (Work work : works) {
            res.add(work.getName());
        }
        return res;
    }

    protected List<String> worksToString(Work parent) {
        List<Work> works = new ArrayList<Work>(parent.getChildren());
        works.add(0, parent);
        return worksToString(works);
    }

    protected boolean containsWork(Work parent, String name) {
        if (parent.getName().equals(name)) return true;
        for (Work ch : parent.getChildren()) {
            return containsWork(ch, name);
        }
        return false;
    }

    protected int getNumberOfWorksInTree(Work parent, String name) {
        String[] tree = parent.print().split(Work.TREE_NEW_LINE);
        int res = 0;
        for (String ch : tree) {
            if (ch.trim().equals(name)) res++;
        }
        return res;
    }

    /**
     * auxiliary method, get list of works by specified statuses list
     *
     * @param include - list of included statuses
     * @return - list of works
     */
    protected List<Work> getWorksIncludedStatuses(List<Work> works, String... statuses) {
        List<String> include = statusesAsList(statuses);
        List<Work> res = new ArrayList<Work>();
        for (Work ch : works) {
            if (include.contains(ch.getStatus().getValue())) res.add(ch);
        }
        return res;
    }

    /**
     * auxiliary method, get list of works by specified statuses list
     *
     * @param include - list of included statuses
     * @return - list of works
     */
    protected List<Work> getWorksExcludedStatuses(List<Work> works, String... statuses) {
        List<String> exclude = statusesAsList(statuses);
        List<Work> res = new ArrayList<Work>();
        for (Work ch : works) {
            if (!exclude.contains(ch.getStatus().getValue())) res.add(ch);
        }
        return res;
    }

    private static List<String> statusesAsList(String... statuses) {
        List<String> res = new ArrayList<String>();
        if (statuses == null) res.add("");
        else res.addAll(Arrays.asList(statuses));
        return res;
    }

    public void simpleValidateLayouts(TestData data, Work work1, Work work2, Work work3, Work work4) {
        PSLogger.info("Works to test layouts : " + Arrays.asList(work1, work2, work3, work4));
        Layout layout1 = new Layout("Layout_A_" + CoreProperties.getTestTemplate(), getRandomDisplayOptions(), work2.getName());
        Layout layout2 = new Layout("Layout_B_" + CoreProperties.getTestTemplate(), getRandomDisplayOptions(), work3.getName());
        PSLogger.debug(data.toString());

        WBSPage pc = WBSPage.openWBSPage(work4);
        prepareLayout(pc, layout1);
        pc = validateLayout(work2, layout1);

        prepareLayout(pc, layout2);
        pc = validateLayout(work3, layout2);

        pc.getOptions().overwriteLayout(layout1.set(layout2).getName());
        pc = validateLayout(work4, layout1);

        pc = WBSPage.openWBSPage(work1);
        pc.getOptions().deleteLayout(layout2.getName());
        pc.refresh(); // for v11
        Assert.assertFalse(pc.isLayoutPresent(layout2.getName()), layout2 + " is present after deleting");

        pc.getOptions().deleteLayouts();
        pc.refresh(); // for v11
        pc = WBSPage.openWBSPage(work4);
        Assert.assertFalse(pc.isLayoutPresent(layout1.getName()), layout1 + " is present after deleting all");
        Assert.assertFalse(pc.isLayoutPresent(layout2.getName()), layout2 + " is present after deleting all");
        Assert.assertFalse(checkLayoutOptions(pc.getGrid().getTableHeaderList(), layout1.getOptions()),
                "Incorrect options after deleting layouts");

    }

    protected WBSPage validateLayout(Work work, Layout layout) {
        PSLogger.info("Validate " + layout + " for project " + work.getName());
        WBSPage pc = WorkManager.open(work).openProjectCentral(layout.getName());
        try {
            Assert.assertTrue(checkLayoutOptions(pc.getGrid().getTableHeaderList(), layout.getOptions()),
                    "Incorrect options for " + layout);
        } catch (AssertionError e) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0) && CoreProperties.getBrowser().isIE(8)) {
                throw new PSKnownIssueException(74085, e);
            } else {
                throw e;
            }
        }
        if (layout.getFilterNameExactly() != null) {
            PSLogger.info("Filter name exactly " + layout.getFilterNameExactly());
            List<String> treeFromPage = pc.getGrid().getListTree();
            PSLogger.info("Tree from page : " + treeFromPage);
            List<String> treeExpected = new ArrayList<String>();
            for (int i = 0; i < getNumberOfWorksInTree(work, layout.getFilterNameExactly()); i++) {
                treeExpected.add(layout.getFilterNameExactly());
            }
            PSLogger.info("Expected tree : " + treeExpected);
            Assert.assertEquals(treeFromPage, treeExpected, "Incorrect tree for " + layout);
        }
        return pc;
    }

    private static boolean checkLayoutOptions(List<String> fromPage, List<String> expected) {
        Collections.sort(fromPage);
        Collections.sort(expected);
        PSLogger.info("From page : " + fromPage);
        PSLogger.info("Expected : " + expected);
        return fromPage.equals(expected);
    }

    protected void prepareLayout(WBSPage pc, Layout layout) {
        PSLogger.info("Set for " + layout);
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        PSLogger.info("Options to set " + layout.getOptions());
        for (String option : layout.getOptions()) {
            columns.getCheckbox(option).click();
        }
        columns.apply();

        WBSPage.OptionsBlock.Filter filter = pc.getOptions().getFilter();
        PSLogger.info("Set filter name exactly is " + layout.getFilterNameExactly());
        filter.setName(0, layout.getFilterNameExactly());
        filter.apply();

        pc.getOptions().saveLayout(layout.getName());
    }

    public static List<String> getRandomDisplayOptions() {
        int randSize = 10;
        List<String> chs = new ArrayList<String>(WBSEPageLocators.getColumnsProject());
        //chs.addAll(TestData.getDisplayCheckboxesByCategory("dates"));
        chs.addAll(WBSEPageLocators.getColumnsResource());
        chs.addAll(WBSEPageLocators.getColumnsControls());
        while (chs.size() > randSize) {
            chs.remove(TestData.getRandom().nextInt(chs.size()));
        }
        return chs;
    }


    /**
     * @param parent - root
     * @param flag:  0 - check 'Display Folders',
     *               1 - check 'Display Action Items',
     *               2 - check 'Display deliverables'
     */
    public void testDisplayFoldersActionItemsDeliverables(Work parent, int flag) {
        String name = null;
        if (flag == 0) name = "Display Folder";
        if (flag == 1) name = "Display Action Items";

        WBSPage pc = WBSPage.openWBSPage(parent, false);
        List<Work> works = parent.getChildren(true, true);
        works.add(0, parent);
        Work.sortByRowIndex(works);


        if (flag == 0) {
            pc.showHideFolders(false);
        } else if (flag == 1) {
            pc.showHideTasks(false);
        }

        List<String> after = new ArrayList<String>();
        for (Work work : works) {
            if (flag == 0 && work.isFolder()) continue;
            if (flag == 1 && work.isActionItem()) continue;
            after.add(work.getName());
        }
        List<String> fromPage1 = pc.getGrid().getListTree();
        PSLogger.info("From page : " + fromPage1);
        PSLogger.info("Expected : " + after);
        Assert.assertEquals(fromPage1, after, "Incorrect list in grid after checking " + name);

        if (flag == 0) {
            pc.showHideFolders(true);
        } else if (flag == 1) {
            pc.showHideTasks(true);
        }

        List<String> fromPage2 = pc.getGrid().getListTree();
        List<String> after2 = worksToString(works);
        PSLogger.info("From page : " + fromPage2);
        PSLogger.info("Expected : " + after2);
        Assert.assertEquals(fromPage2, after2,
                "Incorrect list in grid after unchecking " + name);
    }
}
