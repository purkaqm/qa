package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import org.testng.Assert;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 31.05.2010
 * Time: 17:10:05
 */
public class MyOptionsBlockTCs extends GeneralOptionsBlockTCs {

    public void testFilterTypes(Work main) {
        Map<String, ArrayList<String>> list = new LinkedHashMap<String, ArrayList<String>>();

        for (Work ch : new Work[]{
                main,
                main.getChild("A"),
                main.getChild("B"),
                main.getChild("C"),
                main.getChild("D"),
                main.getChild("E"),
                main.getChild("F"),}) {
            String key = ch.getType();
            if (!list.containsKey(key)) {
                list.put(key, new ArrayList<String>());
            }
            list.get(key).add(ch.getName());
        }
        PSLogger.info("Check filter by types for: " + list);

        WBSPage pc = WorkManager.openWBS(main);
        List<String> init = pc.getGrid().getListTree();
        for (String type : list.keySet()) {
            testFilterType(pc, init, list.get(type), type);
        }

        PSLogger.info("Check bulk setting to filter by type");
        String[] types = new String[]{
                main.getChild("D").getType(),
                main.getChild("E").getType()};
        List<String> expectedList = new ArrayList<String>();
        for (String type : types) {
            expectedList.addAll(list.get(type));
        }
        testFilterType(pc, init, expectedList, types);
    }

    private void testFilterType(WBSPage pc, List<String> initList, List<String> expectedList, String... types) {
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setWorkType(types);
        filters.apply();
        List<String> listFromPage = pc.getGrid().getListTree();
        PSLogger.debug("list from page: " + listFromPage);
        Assert.assertTrue(expectedList.size() == listFromPage.size() &&
                listFromPage.containsAll(expectedList),
                "Incorrect list on page, should be " + expectedList);

        PSLogger.info("Check Reset");
        filters = pc.getOptions().getFilter();
        filters.reset();
        List<String> fromPageList = pc.getGrid().getListTree();
        PSLogger.info("List after resetting: " + fromPageList);
        PSLogger.info("Expected list after resetting: " + initList);
        Assert.assertEquals(fromPageList, initList, "Incorrect list after reset.");
    }

    public void testFilterConstraintStartDate(Work main) {
        List<Work> expectedList =
                getWorksSortedByStartOrEndDate(main.getChildren(), false);
        String date = expectedList.get(0).getConstraintStartDate();
        for (int i = 0; i < expectedList.size(); i++) {
            if (!expectedList.get(i).getConstraintStartDate().equals(date)) {
                while (expectedList.size() > i)
                    expectedList.remove(i);
                break;
            }
        }
        PSLogger.info("Filter is " + date);
        Work.sortByRowIndex(expectedList);
        WBSPage pc = WorkManager.openWBS(main);
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setStartDate(2, 1, date);
        filters.apply();
        List<String> fromPageList = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + fromPageList);
        Assert.assertEquals(fromPageList, worksToString(expectedList),
                "Incorrect List after filter, should be " + expectedList);
        pc.getOptions().getFilter().reset();
    }

    public void testFilterConstraintEndDate(Work main) {
        List<Work> expectedList =
                getWorksSortedByStartOrEndDate(main.getChildren(), true);
        String date = expectedList.get(0).getConstraintEndDate();
        Work.sortByRowIndex(expectedList);
        PSLogger.info("Filter after " + date);
        WBSPage pc = WorkManager.openWBS(main);
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setEndDate(2, 3, date);
        filters.apply();
        List<String> fromPageList = pc.getGrid().getListTree();
        PSLogger.info("List from page: " + fromPageList);
        Assert.assertEquals(fromPageList, worksToString(expectedList),
                "Incorrect List after filter, should be " + expectedList);
        pc.getOptions().getFilter().reset();
    }


    public void testFilterStatus(Work root) {
        List<Work> works = new ArrayList<Work>();

        works.add(root);
        works.addAll(root.getChildren());
        PSLogger.info(root + ", " + root.getStatus().getValue());
        for (Work ch : root.getChildren()) {
            PSLogger.info(ch + ", " + ch.getStatus().getValue());
        }

        String[] toSearch = new String[]{root.getStatus().getValue(),
                root.getChild("E").getStatus().getValue()};


        WBSPage pc = WorkManager.openWBS(root);
        List<String> listFromPage0 = pc.getGrid().getListTree(); // before

        PSLogger.info("1.Search by all statuses");
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setStatus(3, null);
        filters.apply();
        List<String> listFromPage1 = pc.getGrid().getListTree();
        List<Work> expected1 = getWorksExcludedStatuses(works, null);
        Work.sortByRowIndex(expected1);
        PSLogger.info("From page: " + listFromPage1);
        PSLogger.info("Expected: " + expected1);
        Assert.assertEquals(listFromPage1, worksToString(expected1),
                "Incorrect list after searching by all statuses");


        PSLogger.info("Search by none statuses");
        filters = pc.getOptions().getFilter();
        filters.setStatus(1);
        filters.apply();
        List<String> listFromPage2 = pc.getGrid().getListTree();
        List<Work> expected2 = getWorksIncludedStatuses(works, null);
        Work.sortByRowIndex(expected2);
        PSLogger.info("From page: " + listFromPage2);
        PSLogger.info("Expected: " + expected2);
        Assert.assertEquals(listFromPage2, worksToString(expected2),
                "Incorrect list after searching by none statuses");

        PSLogger.info("2.Search by any statuses");
        filters = pc.getOptions().getFilter();
        filters.setStatus(2);
        filters.apply();
        List<String> listFromPage3 = pc.getGrid().getListTree();
        List<Work> expected3 = getWorksExcludedStatuses(works, null);
        Work.sortByRowIndex(expected3);
        PSLogger.info("From page: " + listFromPage3);
        PSLogger.info("Expected: " + expected3);
        Assert.assertEquals(listFromPage3, worksToString(expected3),
                "Incorrect list after searching by any statuses");

        // add resetting due to ie6 behaviour
        PSLogger.info("3.Test reset");
        pc.getOptions().getFilter().reset();
        List<String> listFromPageReset = pc.getGrid().getListTree();
        Assert.assertEquals(listFromPageReset, listFromPage0, "Incorrect list after reset filters");

        PSLogger.info("4.Search by statuses " + toSearch);
        filters = pc.getOptions().getFilter();
        filters.setStatus(3, toSearch);
        filters.apply();
        List<String> listFromPage4 = pc.getGrid().getListTree();
        List<Work> expected4 = getWorksIncludedStatuses(works, toSearch);
        Work.sortByRowIndex(expected4);
        PSLogger.info("From page: " + listFromPage4);
        PSLogger.info("Expected: " + expected4);
        Assert.assertEquals(listFromPage4, worksToString(expected4),
                "Incorrect list after searching by " + Arrays.toString(toSearch) + " statuses");
    }


    public void testFilterTag(Work main, PSTag tg1, PSTag tg2) {
        Map<PSTag, ArrayList<String>> tagWorkMap = new LinkedHashMap<PSTag, ArrayList<String>>();

        for (Work ch : new Work[]{
                main,
                main.getChild("A"),
                main.getChild("B"),
                main.getChild("C"),
                main.getChild("D"),
                main.getChild("E"),
                main.getChild("F"),}) {
            for (PSTag tag : ch.getTags()) {
                if (!tagWorkMap.containsKey(tag)) {
                    tagWorkMap.put(tag, new ArrayList<String>());
                }
                tagWorkMap.get(tag).add(ch.getName());
            }
        }
        PSLogger.info(tagWorkMap);
        WBSPage pc = WorkManager.openWBS(main);
        for (PSTag tag : tagWorkMap.keySet()) {
            testFilterTag(tagWorkMap, tag, pc);
        }

        testFilterTags(tagWorkMap, tg1, pc, tg1.getChilds().get(1),
                tg1.getChilds().get(0));

        testFilterTags(tagWorkMap, tg2, pc, tg2.getChilds().get(1),
                tg2.getChilds().get(0));

        pc.getOptions().getFilter().reset();

    }

    private static void testFilterTags(Map<PSTag, ArrayList<String>> list, PSTag tag, WBSPage pc, PSTag... subtags) {
        Set<String> expectedList = new HashSet<String>();
        for (PSTag t : subtags) {
            expectedList.addAll(list.get(t));
        }
        PSLogger.info("Test searching by tags " + Arrays.toString(subtags));
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setTag(3, tag, subtags);
        filters.apply();
        Set<String> fromPageList = new HashSet<String>(pc.getGrid().getListTree());
        PSLogger.info("Expected: " + expectedList);
        PSLogger.info("From page: " + fromPageList);
        Assert.assertTrue(fromPageList.equals(expectedList),
                "Incorrect lists after filter by tags " + Arrays.toString(subtags));

    }

    private static void testFilterTag(Map<PSTag, ArrayList<String>> list, PSTag tag, WBSPage pc) {
        PSLogger.info("Test filter by tag " + tag);

        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setTag(3, tag.getParent(), tag);
        filters.apply();

        List<String> expectedList = list.get(tag);
        List<String> fromPage = pc.getGrid().getListTree();
        PSLogger.info("Expected: " + expectedList);
        PSLogger.info("From page: " + fromPage);
        Assert.assertEquals(fromPage, expectedList, "Incorrect lists after filter by tag " + tag);
    }

    public void testFilterTagNoValues(Work main, PSTag tg1, PSTag tg2) {
        Work[] worksList = new Work[]{
                main,
                main.getChild("A"),
                main.getChild("B"),
                main.getChild("C"),
                main.getChild("D"),
                main.getChild("E"),
                main.getChild("F"),};
        WBSPage pc = WorkManager.openWBS(main);
        testFilterTagNoOrAnyValues(worksList, tg1, pc, true);
        testFilterTagNoOrAnyValues(worksList, tg2, pc, true);
        pc.getOptions().getFilter().reset();
    }

    public void testFilterTagAnyValues(Work main, PSTag tg1, PSTag tg2) {

        Work[] worksList = new Work[]{
                main,
                main.getChild("A"),
                main.getChild("B"),
                main.getChild("C"),
                main.getChild("D"),
                main.getChild("E"),
                main.getChild("F"),};
        WBSPage pc = WorkManager.openWBS(main);
        testFilterTagNoOrAnyValues(worksList, tg1, pc, false);
        testFilterTagNoOrAnyValues(worksList, tg2, pc, false);
        pc.getOptions().getFilter().reset();
    }

    private static boolean hasThisWorkATag(Work work, PSTag parent) {
        for (PSTag t : work.getTags()) {
            if (t.getParent().equals(parent)) {
                return true;
            }
        }
        return false;
    }

    private static void testFilterTagNoOrAnyValues(Work[] list,
                                                   PSTag tag,
                                                   WBSPage pc,
                                                   boolean noOrAny) {
        WBSPage.OptionsBlock.Filter filters = pc.getOptions().getFilter();
        filters.setTag(noOrAny ? 1 : 2, tag);
        filters.apply();
        Set<String> expectedList = new HashSet<String>();
        for (Work work : list) {
            if (noOrAny && !hasThisWorkATag(work, tag))
                expectedList.add(work.getName());
            if (!noOrAny && hasThisWorkATag(work, tag))
                expectedList.add(work.getName());

        }
        Set<String> fromPageList = new HashSet<String>(pc.getGrid().getListTree());
        PSLogger.info("Expected: " + expectedList);
        PSLogger.info("From page: " + fromPageList);
        Assert.assertTrue(fromPageList.equals(expectedList),
                "Incorrect lists after filter by tag " + tag + " with option " + (noOrAny ? "'No Values'" : "'Any Values'"));
    }

    public void testFilterOwner(Work main, User us) {
        Work[] worksList = new Work[]{
                main,
                main.getChild("A"),
                main.getChild("B"),
                main.getChild("C"),
                main.getChild("D"),
                main.getChild("E"),
                main.getChild("F"),};
        WBSPage pc = WorkManager.openWBS(main);
        searchByOwner(worksList, pc, us);
        searchByOwner(worksList, pc, BasicCommons.getCurrentUser());
        searchByOwner(worksList, pc, BasicCommons.getCurrentUser(), us);
    }

    private static void searchByOwner(Work[] list, WBSPage pc, User... owners) {
        PSLogger.info("Search by owners " + Arrays.toString(owners));
        Set<String> expectedList = new HashSet<String>();
        for (Work work : list) {
            for (User owner : owners) {
                if (work.getOwner().equals(owner)) {
                    expectedList.add(work.getName());
                }
            }
        }
        WBSPage.OptionsBlock.Filter filter = pc.getOptions().getFilter();
        filter.setOwner(3, owners);
        filter.apply();

        Set<String> fromPageList = new HashSet<String>(pc.getGrid().getListTree());
        PSLogger.info("Expected: " + expectedList);
        PSLogger.info("From page: " + fromPageList);
        Assert.assertTrue(fromPageList.equals(expectedList),
                "Incorrect lists after searching by owners " + Arrays.toString(owners));
        filter = pc.getOptions().getFilter();
        filter.reset();
    }

}
