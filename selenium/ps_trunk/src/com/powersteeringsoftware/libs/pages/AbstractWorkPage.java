package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.LeftNavWorkLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.SummaryWorkPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 23.07.2010
 * Time: 17:42:43
 */
public abstract class AbstractWorkPage extends PSPage {

    private static boolean useDirectLink = true;

    protected String workId;

    protected boolean doCheckSummmary; // why?

    public static void setUseDirectLink(boolean b) {
        useDirectLink = b;
    }

    public static boolean useDirectLink() {
        return useDirectLink;
    }

    @Override
    public void open() {
        //do not nothing
    }

    @Override
    protected void open(String url) {
        super.open(url, !useDirectLink);
    }

    public boolean checkUrl() {
        if (!hasUrl())
            setUrl();
        return super.checkUrl() && checkId();
    }

    protected boolean checkId() {
        if (workId != null) {
            if (!url.contains(workId)) {
                PSLogger.debug("Can't find " + workId + " in url");
                return false;
            }
        }
        return true;
    }

    public boolean checkUrl(String id) {
        return checkUrl() && (id == null || url.contains(id));
    }

    public WBSPage openProjectPlanning() {
        return openProjectCentral(PROJECT_PLANING);
    }

    public WBSPage openResourcePlanning() {
        return openProjectCentral(RESOURCE_PLANING);
    }

    protected WBSPage openProjectCentral(ILocatorable name) {
        return openProjectCentral(name.getLocator());
    }

    public WBSPage openProjectCentral(String name) {
        try {
            return _openProjectCentral(name);
        } catch (Throwable e) {
            if (!getBrowser().isIE())
                throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
            // its hotfix, try again (once has null point in this case under ie-8 rc driver)
            if (e instanceof Wait.WaitTimedOutException) {
                throw new PSKnownIssueException(72196, e);
            }
            checkJSError();
            PSLogger.save();
            PSLogger.warn("openProjectCentral: " + e.getMessage());
            initJsErrorChecker(); // for catching bug under ie (8?)
            refresh();
            initJsErrorChecker();
            checkJSError();
            return _openProjectCentral(name);
        }
    }

    private WBSPage _openProjectCentral(String name) {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            synchronized (AbstractWorkPage.class) {
                LeftNavWorkLocators loc = LeftNavWorkLocators.PROJECT_CENTRAL_LAYOUT;
                loc.setTitle(name);
                Link link = getLeftNavLink(loc);
                if (link == null) throw new NullPointerException("Can't find left-nav link '" + name + "'");
                link.click(false);
            }
        } else {
            if (isMenuPresentOnProjectCentralIcon()) {
                Menu menu = openProjectCentralTopMenu();
                PSLogger.save("Before opening Project Central page"); // debug for ie rc (ie7).
                initJsErrorChecker(); // for catching bug
                menu.call(name);
            } else {
                PSLogger.debug("No menu on Project Central icon");
                new Link(PROJECT_CENTRAL_TOP_ICON_LOCATOR).click(false);
            }
        }
        waitForPageToLoad();
        WBSPage wbsPage = WBSPage.getInstance();
        wbsPage.copySettings(this);
        wbsPage.waitWhileLoading();
        return wbsPage;
    }

    /**
     * only for 10.0 or early
     *
     * @return
     */
    protected Menu openProjectCentralTopMenu() {
        Element top = new Element(PROJECT_CENTRAL_TOP_ICON_LOCATOR);
        top.waitForVisible();
        top.mouseOver();
        Menu menu = new Menu(top, true);
        menu.open();
        return menu;
    }


    public boolean isMenuPresentOnProjectCentralIcon() {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0)) return true;
        return new Link(PROJECT_CENTRAL_TOP_ICON_LOCATOR_RESOURCE_PLANNING_ON).exists();
    }

    /**
     * get name of work from title
     *
     * @return - String
     */
    public String getContainerHeader() {
        String res = super.getContainerHeader();
        if (res != null && TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            res = res.replaceAll("\\s:\\s.+$", "");
        }
        return res;
    }


    public DiscussionsPage openDiscussionsTab() {
        PSLogger.info("Open discussions tab");
        DiscussionsPage res = new DiscussionsPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.DISCUSSIONS);
        } else {
            Link link = new Link(DISCUSSIONS_TOP_ICON, res);
            link.waitForVisible(5000);
            link.clickAndWaitNextPage();
        }
        return res;
    }

    public SummaryWorkPage openSummaryPage() {
        SummaryWorkPage res = getSummaryInstance();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.SUMMARY);
        } else {
            new Link(SUMMARY_TOP_ICON, res).clickAndWaitNextPage();
        }
        return res;
    }

    private Link getSummaryLink() {
        return new Link(SUMMARY_TOP_ICON);
    }

    public HistoryWorkPage openHistory() {
        HistoryWorkPage res = new HistoryWorkPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.HISTORY);
        } else {
            new Link(HISTORY_TOP_ICON, res).clickAndWaitNextPage();
        }
        return res;
    }


    /**
     * Navigate page 'Manage Measures"<br>
     * Before using this method you must navigate summary page for any project.
     *
     * @return true - if loaded page 'Manage Measures', false - otherwise
     */
    public MeasureInstancesPage openManageMeasure() {
        PSLogger.info("Go to Measures>Manage");
        MeasureInstancesPage res = new MeasureInstancesPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.MANAGE_MEASURES);
        } else {
            Link link = new Link(MEASURES, res);
            String href = link.getHref();
            PSLogger.debug("href=[" + href + "]");
            if (href.isEmpty()) {
                Menu menu = new Menu(link);
                menu.open();
                menu.call(MEASURES_MANAGE);
                res.waitForPageToLoad();
            } else {
                link.clickAndWaitNextPage(); // 10.0
            }
        }
        PSLogger.save();
        res.testUrl();
        return res;
    }

    public MeasureInstancePage openMeasure(String name) {
        PSLogger.info("Go to Measures>" + name);
        MeasureInstancePage res = new MeasureInstancePage();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            synchronized (AbstractWorkPage.class) {
                LeftNavWorkLocators loc = LeftNavWorkLocators.MEASURE_INSTANCE;
                loc.setTitle(name);
                openLeftNav(res, loc);
            }
        } else {
            Menu menu = new Menu(MEASURES);
            menu.open();
            menu.call(name);
            res.waitForPageToLoad();
        }
        PSLogger.save("After open measure " + name);
        res.testUrl();
        return res;
    }

    public void recalculateMeasures() {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(LeftNavWorkLocators.MEASURE_RECALCULATE);
        } else {
            PSLogger.info("Click Measures>Recalculate all");
            Menu menu = new Menu(MEASURES);
            menu.open();
            menu.call(MEASURES_RECALCULATE_ALL);
            waitForPageToLoad();
        }
    }


    public MetricsPage openManageMetrics() {
        PSLogger.info("Go to Metric>Manage");
        MetricsPage res = new MetricsPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.MANAGE_METRICS);
        } else {
            Link link = new Link(getElement(false, METRIC), res); // use get document for ie* (incorrect href in some case)
            String href = link.getHref();
            if (href.isEmpty()) {
                Menu menu = new Menu(link);
                menu.open();
                menu.call(METRIC_MANAGE);
                res.waitForPageToLoad();
                PSLogger.save();
            } else {
                link.clickAndWaitNextPage();
            }
        }
        res.testUrl();
        return res;
    }

    public MetricInstancePage openMetricInstancePage(String name) {
        MetricInstancePage res = new MetricInstancePage();
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            synchronized (AbstractWorkPage.class) {
                LeftNavWorkLocators loc = LeftNavWorkLocators.METRIC_INSTANCE;
                loc.setTitle(name);
                openLeftNav(res, loc);
            }
        } else {
            Link link = new Link(getElement(false, METRIC), res); // use get document for ie* (incorrect href in some case)
            String href = link.getHref();
            Assert.assertTrue(href.isEmpty(), "Can't find menu");
            Menu menu = new Menu(link);
            menu.open();
            menu.call(name);
            res.waitForPageToLoad();
        }
        res.setId();
        PSLogger.save();
        return res;
    }

    public EstimatedPage openEstimatedCosts() {
        EstimatedPage res = new EstimatedPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.ESTIMATED);
        } else {
            PSLogger.info("Open estimated costs page");
            openCostsMenu().call(ESTIMATED);
            res.waitForPageToLoad();
        }
        return res;
    }

    public ActualPage openActualCosts() {
        ActualPage res = new ActualPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.ACTUAL);
        } else {
            PSLogger.info("Open actual costs page");
            openCostsMenu().call(ACTUAL);
            res.waitForPageToLoad();
        }
        return res;
    }


    private Menu openCostsMenu() {
        Link link = new Link(COSTS);
        Menu menu = new Menu(link);
        menu.open();
        return menu;
    }

    private Menu openEditMenu() {
        Link link = new Link(EDIT_WORK);
        Menu menu = new Menu(link);
        menu.open();
        return menu;
    }

    public EditWorkPage editDetails() {
        EditWorkPage res = new EditWorkPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.EDIT);
        } else {
            Menu menu = openEditMenu();
            menu.call(EDIT_DETAILS);
            res.waitForPageToLoad();
        }
        return res;
    }

    public PermissionsWorkPage editPermissions() {
        PermissionsWorkPage res = new PermissionsWorkPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.EDIT_PERMISSIONS);
        } else {
            Menu menu = openEditMenu();
            menu.call(EDIT_PERMISSIONS);
            res.waitForPageToLoad();
        }
        return res;
    }

    public MoveWorkPage moveWork() {
        MoveWorkPage res;
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            res = new CopyMoveWorkPage();
            openLeftNav(res, LeftNavWorkLocators.COPY_MOVE);
        } else {
            Menu menu = openEditMenu();
            List<String> items = menu.getMenuItems();

            if (items.contains(MOVE.getLocator())) {
                menu.call(MOVE);
                res = new MoveWorkPage();
            } else {
                menu.call(COPY_MOVE);  // copy only for tollgate
                res = new CopyMoveWorkPage();
            }
            res.waitForPageToLoad();
        }
        res.refreshBlankPage();
        return res;
    }

    public DeleteDialog callDelete() {
        Element delete;
        DeleteDialog res;
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            delete = getLeftNavLink(LeftNavWorkLocators.DELETE);
            res = new DeleteDialog(delete, DELETE_DIALOG_V11);
        } else {
            Menu menu = openEditMenu();
            delete = menu.getMenuItem(DELETE);
            res = new DeleteDialog(delete, DELETE_DIALOG);
        }
        res.open();
        return res;
    }

    public ArchiveDialog callArchive() {
        Element delete;
        ArchiveDialog res;
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            delete = getLeftNavLink(LeftNavWorkLocators.ARCHIVE);
            res = new ArchiveDialog(delete, ARCHIVE_DIALOG_V11);
        } else {
            Menu menu = openEditMenu();
            delete = menu.getMenuItem(ARCHIVE);
            res = new ArchiveDialog(delete, ARCHIVE_DIALOG);
        }
        res.open();
        return res;
    }

    public class DeleteDialog extends Dialog {
        private DeleteDialog(Element e, ILocatorable pop) {
            super(e);
            setPopup(pop);
        }

        public PSPage doDelete() {
            Button bt = new Button(getPopup().getChildByXpath(DELETE_DIALOG_YES));
            bt.click(false);
            waitForPageToLoad();
            PSPage res = new DeleteWorkPage();
            if (res.checkUrl())
                return res;
            res = ((DeleteWorkPage) res).getResultsInstance();
            if (res.checkUrl()) return res;
            return null;
        }
    }

    protected SummaryWorkPage getSummaryInstance() {
        return SummaryWorkPage.getInstance(doCheckSummmary, workId);
    }

    public class ArchiveDialog extends Dialog {
        private ArchiveDialog(Element e, ILocatorable pop) {
            super(e);
            setPopup(pop);
        }

        public SummaryWorkPage yes() {
            SummaryWorkPage res = getSummaryInstance();
            Button bt = new Button(getPopup().getChildByXpath(ARCHIVE_DIALOG_YES), res);
            bt.submit();
            return res;
        }
    }

    public IssuesPage openIssues() {
        IssuesPage res = new IssuesPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.ISSUES);
        } else {
            new Link(ISSUES_TOP_ICON, res).clickAndWaitNextPage();
        }
        return res;
    }

    public TasksPage openTasks() {
        TasksPage res = new TasksPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.TASKS);
        } else {
            new Link(TASKS_TOP_ICON, res).clickAndWaitNextPage();
        }
        return res;
    }

    public DocumentListingPage openDocuments() {
        DocumentListingPage res = new DocumentListingPage();
        res.copySettings(this);
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            openLeftNav(res, LeftNavWorkLocators.DOCUMENTS);
        } else {
            new Link(DOCUMENTS_TOP_ICON, res).clickAndWaitNextPage();
        }
        return res;
    }

    public String getUrlId() {
        if (workId != null) return workId;
        return workId = super.getUrlId();
    }

    protected void copySettings(AbstractWorkPage src) {
        workId = src.workId;
        doCheckSummmary = src.doCheckSummmary;
    }

}
