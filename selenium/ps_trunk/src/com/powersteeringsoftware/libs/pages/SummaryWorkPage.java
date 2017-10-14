package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.WorkTreePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.SummaryWorkPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 22:01:02
 * To change this template use File | Settings | File Templates.
 */
public class SummaryWorkPage extends AbstractWorkPage {

    private static boolean waitGrid = true;

    private SimpleGrid grid;

    private static SummaryWorkPage openLastVisited(Work work) {
        PSLogger.info("Open '" + work.getFullName() + "' using last visited");
        PSPage page = PSPage.getInstance().openLastVisitedPage(work.getName(), work.getId());
        if (page instanceof SummaryWorkPage) {
            return (SummaryWorkPage) page;
        }
        return null;
    }

    private static SummaryWorkPage openWorkTree(Work work) {
        PSLogger.info("Open '" + work.getFullName() + "' from Work Tree page");
        WorkTreePage wtp = new WorkTreePage();
        if (!wtp.checkUrl())
            wtp.open();
        return wtp.openWork(work);
    }


    public static SummaryWorkPage openWork(Work work, boolean doCheck) {
        PSLogger.debug("Open summary for project : " + work);
        SummaryWorkPage res = getInstance(doCheck, work.getId());
        initJsErrorChecker();
        if (useDirectLink() && work.getId() != null) {
            PSLogger.info("Open work '" + work.getFullName() + "' by direct link");
            res.open();
        } else {
            res = openLastVisited(work);
            if (res == null)
                res = openWorkTree(work);
            res.doCheckSummmary = doCheck;
            res.testUrl();
        }
        res.checkJSError();
        Assert.assertTrue(res.checkTitle(work), "Incorrect header after opening summary for " + work);
        if (!res.checkParent(work)) {
            if (work.hasParent()) {
                Assert.fail("Incorrect parent after opening summary for " + work);
            } else {
                PSLogger.warn("Parent is not specified?");
            }
        }

        if (work.getId() == null) {
            String id = res.getUrlId();
            PSLogger.debug("Project '" + work.getFullName() + "' id is " + id);
            work.setId(id);
        }
        WorkManager.checkSummarySaveButton(res, work);
        work.setCreated();
        return res;
    }

    public static SummaryWorkPage openWork(Work work) {
        return openWork(work, true);
    }

    public static void setWaitGrid(boolean b) {
        waitGrid = b;
    }

    /**
     * only for 9.3
     *
     * @return Element - first column
     */
    public SimpleGrid getGrid() {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) return null;
        return grid != null ? grid : (grid = new SimpleGrid(false) {
            public void setDefaultElement() {
                super.setDefaultElement(getDocument());
            }
        });
    }

    public GateSnapshot getGateSnapshot() {
        return new GateSnapshot();
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        waitForWBS();
    }

    protected void waitForWBS() {
        if (!waitGrid) {
            if (check && getGrid().exists()) {
                getGrid().checkForPopupError();
            }
            return;
        }
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3)) return;
        if (!isDescendantsExpanded()) {
            PSLogger.debug("Descendants is collapsed");
            return;
        }
        PSLogger.debug("Descendants is expanded, wait for grid loading");
        try {
            getGrid().waitForVisible();
        } catch (Throwable e) {
            checkJSError();
            throw new RuntimeException(e);
        }
    }

    public void testUrl() {
        if (doCheckSummmary)
            super.testUrl();
    }

    public boolean doCheckUrl() {
        if (!hasUrl())
            setUrl();
        return super.checkUrl(_URL);
    }

    @Override
    public void open() {
        if (workId == null) throw new NullPointerException("Id is empty");
        if (workId != null) {
            open(url = makeUrl(URL, workId));
        }

        PSLogger.debug("Summary page url is '" + url + "'");
        testUrl();
        waitForWBS();
    }

    public void refresh() {
        super.refresh(false);
        waitForWBS();
    }

    public static SummaryWorkPage getInstance(boolean doCheck, String id) {
        SummaryWorkPage res = new SummaryWorkPage();
        res.doCheckSummmary = doCheck;
        res.workId = id;
        return res;
    }

    public static SummaryWorkPage getInstance(boolean doCheck) {
        return getInstance(doCheck, null);
    }

    public static SummaryWorkPage getInstance() {
        return getInstance(true);
    }

    public static SummaryWorkPage getInstance(String id) {
        return getInstance(true, id);
    }

    private SummaryWorkPage() {
        //empty constructor.
    }

    public void openDetails() {
        new Link(DETAILS_LINK).click(false);
        new Element(DETAILS_BODY).waitForVisible();
    }

    public void openDescendants() {
        collapseExpandDescendants(true);
        waitForWBS();
    }

    public void openMeasure() {
        collapseExpandMeasure(true);
        getDocument(true);
    }

    public boolean isPlanResourcesEnabled() {
        String txt = new Element(PLAN_RESOURCES_INFO).getText();
        Assert.assertFalse(txt.isEmpty(), "Resource planning is not enabled, can't find plan resource in edit section");
        PSLogger.info("Plan Resources is '" + txt + "' for " + getContainerHeader());
        return !txt.contains(PLAN_RESOURCES_INFO_NO.getLocator());
    }

    public EditWorkPage edit() {
        EditWorkPage res = new EditWorkPage();
        new Link(EDIT_WORK_LINK).click(false);
        res.waitForPageToLoad();
        res.copySettings(this);
        return res;
    }

    /*public boolean hasEditDetailsLink() {
        return getDriver().isElementPresent(EDIT_WORK_LINK.getLocator());
    } */
    public boolean hasEditDetailsLink() {
        return new Link(EDIT_WORK_LINK).exists();
    }

    public List<String> getWorksWithCustomPermissions() {
        List<String> res = new ArrayList<String>();
        for (Element img : getElements(WorkTreePageLocators.CUSTOM_PERMISSIONS_IMG)) {
            res.add(img.getParent().getChildByXpath("//a").getDEText().trim());
        }
        return res;
    }

    public DeliverablesListingPage clickAddRemove() {
        Link link = null;
        Element e = getElement(false, LINK_ADD_REMOVE_DELIVERABLE_2);
        if (e != null && e.isDEPresent()) {
            link = new Link(e);
            link.openByHref();
        } else {
            link = new Link(LINK_ADD_REMOVE_DELIVERABLE);
            link.clickAndWaitNextPage();
        }

        DeliverablesListingPage res = new DeliverablesListingPage();
        res.waitForLoading();
        return res;
    }

    public boolean isDeliverableExist(String deliverableName) {
        return getChildLink(deliverableName) != null;
    }

    public Link getChildLink(String name) {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            if (!isDescendantsExpanded()) {
                openDescendants();
            }
            getGrid().expandTree();
            List<String> tree = getGrid().getListTree();
            PSLogger.debug("Summary Grid tree : " + tree);
            return getGrid().getItem(name);
        }
        Link res = new Link(GATE_LINK.replace(name));
        if (res.exists()) return res;
        return null;
    }

    public SummaryWorkPage openSummaryFor(String name) {
        SummaryWorkPage sum = getInstance(doCheckSummmary); // inherit
        Link link = getChildLink(name);
        link.setResultPage(sum);
        link.clickAndWaitNextPage();
        sum.getUrlId();
        return sum;
    }

    public boolean isDescendantsExpanded() {
        return isModuleExpanded(DESCENDANTS_IMG);
    }

    private boolean isModuleExpanded(ILocatorable loc) {
        Img img = new Img(loc);
        String alt = img.exists() ? img.getAttribute(IMG_ALT) : null;
        return IMG_ALT_COLLAPSE.getLocator().equalsIgnoreCase(alt);
    }

    private void collapseExpandModule(Link _link, ILocatorable img, boolean yes) {
        Img _img = new Img(img);
        //String alt = _img.getAttribute(IMG_ALT);
        String src = _img.getSrc();
        String txt = _link.getName();
        if (yes ^ src.endsWith(IMG_SRC_COLLAPSE.getLocator())) {
            //alt.equalsIgnoreCase(IMG_ALT_COLLAPSE.getLocator())) {
            PSLogger.info((yes ? "Expand" : "Collapse") + " " + _link.getName() + " module");
            _link.click(false);
            _img.waitForAttribute(IMG_ALT, yes ? IMG_ALT_COLLAPSE : IMG_ALT_EXPAND);
            new TimerWaiter(500).waitTime();
            PSLogger.save("After " + (yes ? "expanding" : "collapsing") + " module " + txt);
            return;
        }
        PSLogger.debug("module " + txt + " is already " + (yes ? "expanded" : "collapsed"));
    }

    private void collapseExpandDescendants(boolean yes) {
        collapseExpandModule(new Link(DESCENDANTS_LINK), DESCENDANTS_IMG, yes);
    }

    private void collapseExpandMeasure(boolean yes) {
        Link link = new Link(MEASURE_LINK);
        Assert.assertTrue(link.exists() && link.isDEVisible(), "Can't find Measures module");
        collapseExpandModule(link, MEASURE_IMG, yes);
    }

    public boolean hasMeasureModule() {
        return new Img(MEASURE_IMG).exists();
    }

    public Measures getMeasureModule() {
        openMeasure();
        Measures res = new Measures();
        if (!res.exists()) return null;
        res.setDefaultElement(getDocument(false));
        return res;
    }

    public CreateWorkPage addNewDescendant() {
        Link l1 = new Link(DESCENDANTS_ADD_NEW_LINK_2);
        if (l1.exists() && l1.isVisible()) {
            l1.clickAndWaitNextPage(); // hotfix for 9.4. can't find link using following way:
        } else {
            Element block = getBlock(DESCENDANTS_LINK);
            Link l2 = getLink(block, DESCENDANTS_ADD_NEW_LINK);
            Assert.assertNotNull(l2, "Can't find link " + DESCENDANTS_ADD_NEW_LINK.getLocator());
            l2.clickAndWaitNextPage();
        }
        return new CreateWorkPage();
    }

    public Element getBlock(ILocatorable loc) {
        Link l1 = new Link(loc);
        l1.setDefaultElement(getDocument(false));
        return l1.getParent(BLOCK_DIV, BLOCK_CLASS);
    }

    private Link getLink(Element block, ILocatorable txt) {
        for (Element e : Element.searchElementsByXpath(block, "//a")) {
            if (e.getDEText().equals(txt.getLocator())) return new Link(e);
        }
        return null;
    }

    public SingleStatusSelector getStatusSelector() {
        return new SingleStatusSelector(new Link(STATUS_LINK), true, false);
    }

    public String getStatus() {
        String res = getStatusSelector().getContent();
        PSLogger.debug("Summary Status=" + res);
        return res;
    }

    public void setStatus(Work.Status st) {
        setStatus(st.getValue());
    }

    public void setStatus(String status) {
        setStatus(status, true);
    }

    public void setStatus(String status, boolean wait) {
        // since 9.0 or 8.2 ?
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_0)) {
            PSLogger.skip("Can't set status!");
            return;
        }
        SingleStatusSelector st = getStatusSelector();
        String was = st.getContent();
        if (was.equals(status)) {
            PSLogger.debug("Status is already '" + status + "'");
            return;
        }
        PSLogger.info("Set status to '" + status + "'");
        st.setLabel(status);
        if (!wait) return;
        waitForPageToLoad();
        if (isCheckBlankPage() && isBlankPage(false)) {
            PSLogger.error("blank page!!");
            refresh();
        }
    }

    public Link getParentLink() {
        Link link = new Link(PARENT_LINK);
        if (hasDocument()) {
            link.setDefaultElement(getDocument(false));
            return link.isDEPresent() ? link : null;
        }
        return link.exists() ? link : null;
    }

    public String getParent() {
        Link res = getParentLink();
        if (res == null) {
            return "";
        }
        return res.isDEPresent() ? res.getDEText() : StrUtil.trim(res.getText());
    }

    public boolean checkTitle(Work work) {
        if (!check) return true;
        if (!doCheckSummmary) return true;
        String actualHeader = getContainerHeader();
        PSLogger.debug("Work page header is '" + actualHeader + "'");
        String expectedHeader = work.hasSummary() ? work.getName() : work.getParentName();
        return actualHeader.equals(expectedHeader);
    }

    public boolean checkParent(Work work) {
        if (!check) return true;
        if (!doCheckSummmary) return true;
        String actualParent = getParent();
        PSLogger.debug("Summary parent is '" + actualParent + "'");
        String expectedParent = "";
        if (work.hasParent()) {// && work.canView(BasicCommons.getCurrentUser())) { //TODO:
            expectedParent = work.hasSummary() ? work.getParentName() : work.getParent().getParentName();
        }
        return actualParent.equals(expectedParent);
    }

    public class Measures extends Element {
        private List<Row> rows;

        private Measures() {
            super(MEASURE_TABLE);
        }

        private class Row {
            private CheckBox ch;
            private Link link;
            private String name;
        }

        public List<Row> getRows() {
            if (rows != null) return rows;
            rows = new ArrayList<Row>();
            for (Element e : Element.searchElementsByXpath(this, MEASURE_TR)) {
                Element link = e.getChildByXpath(MEASURE_TD_NAME);
                if (!link.isDEPresent()) continue;
                Row row = new Row();
                row.link = new Link(link);
                row.name = link.getDEText();
                Element ch = e.getChildByXpath(MEASURE_TD_RECALCULATE);
                if (ch.isDEPresent()) {
                    row.ch = new CheckBox(ch);
                    row.ch.setName(row.name);
                }
                rows.add(row);
            }
            return rows;
        }

        public boolean hasLink(String name) {
            return getRow(name) != null;
        }

        public Row getRow(String name) {
            for (Row row : getRows()) {
                if (name.equals(row.name)) return row;
            }
            return null;
        }

        public CheckBox getRecalculate(String name) {
            return getRow(name).ch;
        }

        public void select(String name, boolean yes) {
            PSLogger.info((yes ? "Select" : "Unselect") + " checkbox for " + name);
            getRecalculate(name).select(yes);
        }

        public Link getLink(String name) {
            return getRow(name).link;
        }

        public MeasureInstancePage open(String name) {
            PSLogger.info("Open measure '" + name + "' from summary page");
            MeasureInstancePage res = new MeasureInstancePage();
            Link l = getLink(name);
            l.setResultPage(res);
            l.clickAndWaitNextPage();
            res.testUrl();
            return res;
        }

        public CheckBox getRecalculateAll() {
            Element e = Element.searchElementByXpath(this, MEASURE_RECALCULATE_ALL);
            if (e == null || !e.isDEPresent()) return null;
            return new CheckBox(e);
        }

        public Button getSubmit() {
            return new Button(MEASURE_SUBMIT);
        }

        public boolean hasSubmit() {
            return getSubmit().exists();
        }

        public void submit() {
            PSLogger.save("before recalculate measures");
            getSubmit().submit();
            setDefaultElement(getDocument());
        }

        public void setDefaultElement(Document d) {
            super.setDefaultElement(d);
            rows = null;
        }

    }

    public String getOwner() {
        return StrUtil.trim(new Element(OWNER).getText());
    }


    public class GateSnapshot {

        public AdvanceDialog pushAdvance() {
            PSLogger.info("Push 'Advance'");
            Button advance = new Button(GATE_SNAPSHOT_ADVANCE_BUTTON);
            AdvanceDialog res = new AdvanceDialog(advance);
            res.open();
            return res;
        }

        public boolean isGateActive(String gateName) {
            return new Link(GATE_SNAPSHOT_GATE_LINK.replace(gateName)).exists();
        }

        public ReactivateDialog pushReactivate(String gateName) {
            PSLogger.info("Click on '" + gateName + "' to reactivate");
            Link link = new Link(GATE_SNAPSHOT_GATE_LINK.replace(gateName));
            link.setId();
            String id = link.getId();
            PSLogger.debug2("link id=" + id);
            ReactivateDialog res = new ReactivateDialog(link, Integer.parseInt(id.replaceAll("[^\\d]+", "")));
            res.open();
            return res;
        }

        public class AdvanceDialog extends Dialog {
            private AdvanceDialog(Element e) {
                super(e);
                setPopup(GATE_SNAPSHOT_ADVANCE_DIALOG);
            }

            public void setText(String txt) {
                PSLogger.info("Set '" + txt + "' to advance dialog");
                TextArea textArea = new CounterTextArea(GATE_SNAPSHOT_ADVANCE_DIALOG_TEXTAREA);
                textArea.setText(txt);
            }

            public SummaryWorkPage doAdvance() {
                SummaryWorkPage res = getSummaryInstance();
                Button bt = new Button(getPopup().getChildByXpath(GATE_SNAPSHOT_ADVANCE_DIALOG_SUBMIT), res);
                PSLogger.save("Before Advance");
                bt.submit();
                return res;
            }
        }

        public class ReactivateDialog extends Dialog {
            private ReactivateDialog(Element e, int num) {
                super(e);
                setPopup(GATE_SNAPSHOT_REACTIVATE_DIALOG.replace(num));
            }

            public void setText(String txt) {
                PSLogger.info("Set '" + txt + "' to reactivation dialog");
                TextArea textArea = new CounterTextArea(GATE_SNAPSHOT_REACTIVATE_DIALOG_TEXTAREA);
                textArea.setText(txt);
            }

            public SummaryWorkPage doReactivate() {
                SummaryWorkPage res = getSummaryInstance();
                Button bt = new Button(getPopup().getChildByXpath(GATE_SNAPSHOT_REACTIVATE_DIALOG_SUBMIT), res);
                PSLogger.save("Before reactivation");
                bt.submit();
                return res;
            }
        }


    }
}
