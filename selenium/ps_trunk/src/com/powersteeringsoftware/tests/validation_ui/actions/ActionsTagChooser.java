package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 08.06.2010
 * Time: 13:52:40
 */
public class ActionsTagChooser {
    private TestData data;
    private UIPage ui;
    private static final int ITEMS_NUM = 2;

    public ActionsTagChooser(UIPage ui, TestData d) {
        this.ui = ui;
        this.data = d;
    }

    public void testSimpleTagChooser(String testId) {
        PSTag tag = data.getTag(testId);
        TagChooser tc = ui.getFirstTagChooser(tag.getName());
        testSimpleTagChooser(tag, tc);
    }

    public void testSimpleTagChooser(PSTag tag, TagChooser tc) {
        tc.openPopup();
        tc.closePopup();
        List<String> labels = tc.getAllLabels();
        validateTagChooserItems(labels, tag);

        tc.setAllLabels();
        List<String> fromPage = tc.getContent();
        PSLogger.info("From page: " + fromPage);
        PSLogger.save();
        Assert.assertEquals(fromPage, labels, "Incorrect list after select all");
        tc.setDefaultElement(ui.getDocument());

        tc.setNoneLabels();
        List<String> fromPage2 = tc.getContent();
        PSLogger.info("From page: " + fromPage2);
        PSLogger.save();
        Assert.assertTrue(fromPage2.size() == 0, "There are some selected items after set 'None'");

        String toSelect = labels.get(labels.size() / 2);
        tc.setLabel(toSelect);
        List<String> fromPage3 = tc.getContent();
        PSLogger.info("From page: " + fromPage3);
        PSLogger.save();
        Assert.assertTrue(fromPage3.size() == 1 && fromPage3.contains(toSelect),
                "incorect selected items after set '" + toSelect + "'");

        String toSelect2 = labels.get(0);
        String toSelect3 = labels.get(labels.size() - 1);
        if (SeleniumDriverFactory.getDriver().getType().isIE(9)) {
            ui.refresh();
        }
        tc.openPopup();
        tc.selectUnselect(false);
        tc.select(toSelect2, toSelect3);
        tc.done();
        List<String> fromPage4 = tc.getContent();
        PSLogger.info("From page: " + fromPage4);
        PSLogger.save();
        List<String> expected = Arrays.asList(toSelect2, toSelect3);
        Assert.assertEquals(fromPage4, expected,
                "incorect selected items after set '" + toSelect2 + "', '" + toSelect3 + "'");

        tc.focusOut();
        tc.openPopup();
        tc.cancel();
        List<String> fromPage5 = tc.getContent();
        PSLogger.info("From page: " + fromPage5);
        PSLogger.save();
        Assert.assertEquals(fromPage5, expected,
                "incorect selected items after cancel");
    }


    private void validateTagChooserItems(List<String> actual, PSTag tag) {
        List<String> expected = tag.getValues();
        PSLogger.info("Items from page: " + actual);
        PSLogger.info("Items expected: " + expected);
        Assert.assertFalse(actual.size() == 0, "No items in tag-chooser found");
        if (expected.size() < 10) {
            Assert.assertTrue(expected.equals(actual),
                    "Incorrect list of items in tag chooser. Should be : " + expected);
        }
        if (!expected.containsAll(actual)) {

            actual.removeAll(expected);
            Assert.fail("Incorrect list of items in tag chooser. There are excess items " + actual);
        }
        if (!actual.containsAll(expected)) {
            expected.removeAll(actual);
            List<String> loading = tag.getLoadingValues(); // may be not displayed until scroll
            Collections.sort(expected);
            Collections.sort(loading);
            if (!expected.equals(loading))
                Assert.fail("Incorrect list of items in tag chooser. Can't find items : " + expected);
        }
    }


    public void testFlatTagChooser(String testId) {
        PSTag tag = data.getTag(testId);
        FlatTagChooser tc = ui.getFlatTagChooser(tag.getName());
        tc.openPopup();
        List<String> labels = tc.getAllLabels();
        validateTagChooserItems(labels, tag);

        String clearSelect = labels.get(0);
        String toSelect1 = labels.get(labels.size() / 2);
        String toSelect2 = labels.get(1);
        String toSelect3 = labels.get(labels.size() - 1);

        for (String toSelect : new String[]{toSelect1, toSelect2, toSelect3}) {
            tc.setLabel(toSelect);
            String fromPage = tc.getContent();
            PSLogger.info("From page: " + fromPage);
            Assert.assertTrue(fromPage.equals(toSelect),
                    "incorect selected items after set '" + toSelect + "'");
        }

        tc.setLabel(clearSelect);
        String fromPage = tc.getContent();
        PSLogger.info("From page: " + fromPage);
        Assert.assertTrue(fromPage.isEmpty(),
                "after clear there is items on page: " + fromPage);

        //todo: search actions (don't work)
        /*
        List<String> allItems = tc.getAllLabels();
        List<String> itemsToTest = new ArrayList<String>();
        for (int i = 0; i < ITEMS_NUM; i++) {
            itemsToTest.add(allItems.get(rand.nextInt(allItems.size())));
        }
        PSLogger.info("Items to test searching: " + itemsToTest);
        for (String item : itemsToTest) {
            testPopupSearch(tc, item);
        }
        */
    }

    /**
     * todo: this method doesn't work correct
     *
     * @param tc
     * @param txt
     */
    private void testPopupSearch(FlatTagChooser tc, String txt) {
        PSLogger.info("Test searching in flat tag chooser by " + txt);
        tc.search(txt);

        List<String> fromPopup = tc.getAllLabelsWithoutClear();
        for (String item : fromPopup) {
            Assert.assertTrue(item.toLowerCase().contains(txt.toLowerCase()),
                    "Incorrect result after searching in popup: '" + item + "' doesn't contain '" + txt + "'");
        }
        Assert.assertFalse(fromPopup.size() == 0,
                "Can't find any items after searching by '" + txt + "'");
        String lab = fromPopup.get(TestData.getRandom().nextInt(fromPopup.size() - 1));
        PSLogger.info("Select label " + lab + " after searching by " + txt);
        tc.setLabel(lab);
        Assert.assertEquals(tc.getContent(), lab, "Incorrect label in input after selecting " + lab);

    }

    public void testMultipleTagChooser(String testId) {
        PSTag tag = data.getTag(testId);
        MultipleTagChooser tc = ui.getMultipleTagChooser(tag.getName());
        tc.focus();
        testSimpleTagChooser(tag, tc);
        List<String> items = tc.getAllLabels();
        Set<String> data = new HashSet<String>();
        while (data.size() < Math.min(ITEMS_NUM, items.size())) {
            data.add(items.get(TestData.getRandom().nextInt(items.size())));
        }
        for (String item : items) {
            if (item.startsWith("<")) { // add html to test data
                data.add(item);
                break;
            }
        }
        PSLogger.info("Items to test searching: " + data);
        for (String item : data) {
            testPopupSearch(tc, item);
        }
        //todo: test 'Show checked' and 'Display All'
    }

    /**
     * for tag chooser with searching popup
     *
     * @param tc
     * @param txt
     */
    private void testPopupSearch(MultipleTagChooser tc, String txt) {
        PSLogger.info("Test searching in multiple tag chooser by " + txt);
        tc.openPopup();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
            tc.selectNone();
        }
        tc.search(txt);
        List<String> fromPopup = tc.getAllLabels();
        PSLogger.info("After search: " + fromPopup);
        for (String item : fromPopup) {
            Assert.assertTrue(item.toLowerCase().contains(txt.toLowerCase()),
                    "Incorrect result after searching in popup: '" + item + "' doesn't contain '" + txt + "'");
        }
        Assert.assertFalse(fromPopup.size() == 0,
                "Can't find any items after searching by '" + txt + "'");
        tc.clickAllDisplayed();
        tc.done();
        List<String> fromPage = tc.getContent();
        PSLogger.info("After submit: " + fromPage);
        Assert.assertEquals(fromPage, fromPopup, "Incorrect list in tag chooser after searching for '" + txt + "'");

    }


    public void testHierarchicalTagChooser(String id) {

        PSTag tag = data.getTag(id);
        HierarchicalTagChooser tc = ui.getHierarchicalTagChooser(tag.getName());

        tc.openPopup();
        List<String> labels = tc.getAllLabels();
        PSLogger.info(labels);
        tc.openAll();
        labels = tc.getAllLabels();
        PSLogger.info(labels);
        List<String> tree = tc.getTree();
        List<String> shouldBe = tag.getTreeList();
        PSLogger.info("from popup: " + tree);
        PSLogger.info("should be: " + shouldBe);
        Assert.assertEquals(tree, shouldBe, "Incorrect list from popup");
        if (tc.isSingleSelector()) {
            Assert.assertFalse(tc.getSelectAllLink().exists() && tc.getSelectAllLink().isVisible(),
                    "There is All link in popup");
        }
        tc.closePopup();
        Assert.assertFalse(labels.size() == 0, "Nothing on first popup");

        PSLogger.info("Validate several items");
        tc.openPopup();
        tc.closeAll();
        List<String> shouldBe2 = tag.getValues();
        tree = tc.getTree();
        PSLogger.info("From popup: " + tree);
        PSLogger.info("Should be: " + shouldBe2);
        Assert.assertEquals(tree, shouldBe2, "Incorrect list from popup after closing all");

        List<PSTag> chainToOpen = tag.getFirstLongestChain();
        PSTag toSelect = chainToOpen.get(chainToOpen.size() - 1);
        PSLogger.info("Test chain to open: " + toSelect);
        for (int i = 1; i < chainToOpen.size() - 1; i++) {
            tc.openBranch(chainToOpen.get(i).getName());
        }
        List<String> shouldBe3 = PSTag.getOpenedTreeList(toSelect);

        tree = tc.getTree();
        PSLogger.info("from popup: " + tree);
        PSLogger.info("should be: " + shouldBe3);
        Assert.assertEquals(tree, shouldBe3, "Incorrect list from popup after closing all and opening first child");
        tc.closePopup();

        tc.setLabel(toSelect.getName());
        List<String> fromPage = tc.getContent();
        PSLogger.info("Expected " + toSelect.getName());
        PSLogger.info("from page " + fromPage);
        Assert.assertTrue(fromPage.size() == 1 && fromPage.contains(toSelect.getName()),
                "Incorrect result after selecting some child");

        if (!tc.isSingleSelector()) {
            tc.setAllLabels();
            fromPage = tc.getContent();
            PSLogger.info("from page after selecting all : " + fromPage);
            Assert.assertEquals(fromPage, labels, "Incorrect result after selecting all, should be " + labels);
        }
        tc.setNoneLabels();
        fromPage = tc.getContent();
        PSLogger.info("from page after selecting none : " + fromPage);
        Assert.assertTrue(fromPage.isEmpty(), "Incorrect result after selecting none, should be empty list");
    }


    public void testTagsWithDependencies(TagsDependencies td, String id) {
        //todo: make ConfigObject for TD.
        PSTag tag = data.getTag(id);
        // skip for safari...
        // by some reason can't find selects
        // todo: investigate
        if (SeleniumDriverFactory.getDriver().getType().isSafari())
            PSSkipException.skip("Skip for safari");

        List<PSTag> toTest = tag.getFirstLongestChain();
        toTest.remove(0); // remove root.
        List<String> shouldBe = tag.getTreeList();
        List<String> tree = td.getTree();
        PSLogger.info(tree);
        PSLogger.info(shouldBe);
        Assert.assertEquals(tree, shouldBe, "Incorrect tree in tags with dependencies");
        PSLogger.info("Sequence to test : " + toTest);
        for (int i = 0; i < toTest.size(); i++) {
            PSTag item = toTest.get(i);
            PSTag parent = i != 0 ? item.getParent() : null;
            String name = item.getName();

            td.getItems().get(i).select(name);

            String help = td.getItems().get(i).getToolTipInfo();
            String expectedHelp = (parent == null ? "" : parent.getFullName()).replace(tag.getName() + PSTag.SEPARATOR, "").
                    replace(PSTag.SEPARATOR, TagsDependencies.HELP_SEPARATOR);
            PSLogger.info("help tooltip: '" + help + "'");
            PSLogger.info("expected: '" + expectedHelp + "'");
            Assert.assertEquals(help.replaceAll(TagsDependencies.HELP_STRING_PATTERN, ""), expectedHelp, "Incorrect info for " + name);

        }
    }

}
