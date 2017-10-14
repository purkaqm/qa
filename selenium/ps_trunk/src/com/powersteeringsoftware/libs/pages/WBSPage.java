package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Dialog;
import com.powersteeringsoftware.libs.elements.Frame;
import com.powersteeringsoftware.libs.elements.Menu;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators;
import com.powersteeringsoftware.libs.enums.page_locators.LeftNavWorkLocators;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.WorkDependency;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;
import org.testng.SkipException;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.WorkChooserDialogLocators.LOCATOR_REPLACE_PATTERN;
import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 22:02:45
 * To change this template use File | Settings | File Templates.
 */
public class WBSPage extends AbstractWorkPage implements FiltersGridPage {

    private static final TimerWaiter GANTT_WAITER = new TimerWaiter(10000); // for gantt
    private static final TimerWaiter HISTOGRAM_WAITER = new TimerWaiter(5000); // for histogram

    private static boolean checkReload = true;

    private Grid grid;
    private TeamPane teamPane;
    private Gantt gantt;
    private VariableAllocation variableAllocation;
    private Histogram histogram;

    private List<String> errors; // grid errors

    static boolean waitGrid = true;

    private WBSPage() {
        init();
    }

    public static WBSPage getInstance() {
        return new WBSPage();
    }

    public static void setWaitGrid(boolean waitGrid) {
        WBSPage.waitGrid = waitGrid;
    }

    public static boolean isCheckReload() {
        return checkReload;
    }

    public static void setCheckReload(boolean b) {
        checkReload = b;
    }

    protected void open(String url) {
        init();
        if (!waitGrid) return;
        super.open(url);
        initJsErrorChecker();
        waitWhileLoading();
        testUrl();
    }

    public void open() {
        Assert.assertNotNull(url, "Url should be specified");
        PSLogger.debug("Open WBS page by direct url : " + url);
        open(url);
    }

    /**
     * TODO: move it to WorkManager
     *
     * @param work Work Object
     * @param isPC if true open WBS for PC, otherwise WBS for RP
     * @return WBSPage
     */
    public static WBSPage openWBSPage(Work work, boolean isPC) {
        WBSPage res = _openWBSPage(work, isPC);
        res.getGrid().setIndexes(work);
        return res;
    }

    private static WBSPage _openWBSPage(Work work, boolean isPC) {
        PSLogger.info("Open WBS page (" + (isPC ? "Project Central" : "Resource Planning") + " layout) for " + work);
        WBSPage page;
        String workUrl = isPC ? work.getWbsPCUrl() : work.getWbsRRUrl();
        if (useDirectLink() && workUrl != null) {
            PSLogger.debug("Open WBS page for work '" + work.getFullName() + "' by direct link");
            if (waitGrid)
                initJsErrorChecker();
            page = new WBSPage();
            page.open(workUrl);
            page.workId = work.getId();
            return page;
        }

        WBSPage external = new WBSPage();
        if (work.getWbsPCUrl() != null && external.checkUrl(URL) && external.getUrl().contains(work.getWbsPCUrl())) {
            PSLogger.debug("This WBS page is already open.");
            page = isPC ? external.openProjectPlanning() : external.openResourcePlanning();
        } else {
            SummaryWorkPage summary = WorkManager.open(work);
            page = isPC ? summary.openProjectPlanning() : summary.openResourcePlanning();
        }
        page.closeAllMenus(); // hotfix for ie8 (after opening using WorkTree there is a Browse popup on WBS page)
        String url = page.getUrl();
        PSLogger.debug("Page url is " + url);
        if (workUrl == null) {
            if (isPC)
                work.setWbsPCUrl(url);
            else
                work.setWbsRRUrl(url);
        }
        page.workId = work.getId();
        return page;
    }

    /**
     * Open Summary page, and then click on Project Central menu in top.
     *
     * @param work
     * @return
     */
    public static WBSPage openWBSPage(Work work) {
        return openWBSPage(work, true);
    }

    private void init() {
        grid = new Grid();
        if (waitGrid)
            teamPane = new TeamPane();
        gantt = new Gantt();
        variableAllocation = new VariableAllocation();
        histogram = new Histogram();
    }

    public GeneralActions getGeneralActions() {
        return new GeneralActions(this);
    }

    public void waitWhileLoading() {
        errors = null;
        if (!waitGrid) return;
        getGeneralActions().waitWhileLoading(true);
        getGantt().setDefaultElement(getDocument());
        if (teamPane != null) teamPane.setDefaultElement(getDocument(false));
    }

    public void checkForPopupError() {
        getGrid().checkForPopupError();
    }

    public void refresh() {
        super.refresh();
        waitWhileLoading();
    }

    public void goBack() {
        super.goBack();
        waitWhileLoading();
    }

    /**
     * method to avoid getting errors twice
     *
     * @return List of grid errors
     */
    @Override
    public List<String> getErrorMessagesFromTop() {
        if (errors != null) {
            List<String> res = new ArrayList<String>(errors);
            errors = null;
            return res;
        }
        return errors = super.getErrorMessagesFromTop();
    }

    private List<String> applySaveArea(SimpleGrid.ApplyButton button) {
        button.waitForEnabled();
        try {
            errors = null;
            button.submit();
            setIsDocumentFresh();
        } catch (Wait.WaitTimedOutException e) {
            getGeneralActions().checkReload(e);
            checkJSError();
        }
        initJsErrorChecker();
        return getErrorMessagesFromTop();
    }

    public void resetArea() {
        PSLogger.info("Push 'Reset' button in grid");
        List<String> res = applySaveArea(getGrid().getResetButton());
        if (res.size() != 0)
            PSLogger.warn(res);
    }

    public boolean isSaveAreaEnabled() {
        return getGrid().getSaveButton().isEnabled();
    }

    public boolean isResetAreaEnabled() {
        return getGrid().getResetButton().isEnabled();
    }

    public void saveArea() {
        PSLogger.info("Push 'Save' button in grid");
        SimpleGrid.ApplyButton bt = getGrid().getSaveButton();
        List<String> errors = applySaveArea(bt);
        Assert.assertTrue(errors.size() == 0, "Have errors on page : " + errors);
        Assert.assertFalse(bt.isEnabled(), "Save is enabled after pushing");
    }


    /**
     * close display menu by clicking on header.
     * this method is for 9.0
     */
    public void closeAnyMenus() {
        super.closeAllMenus();
        getTopElement().mouseDownAndUp();
    }

    public boolean isLayoutPresent(String name) {
        if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) {
            synchronized (AbstractWorkPage.class) {
                LeftNavWorkLocators loc = LeftNavWorkLocators.PROJECT_CENTRAL_LAYOUT;
                loc.setTitle(name);
                return getLeftNavLink(loc) != null;
            }
        }
        return openProjectCentralTopMenu().containsItem(name);
    }

    protected Menu openProjectCentralTopMenu() {
        getOptions().standardScreen();
        return super.openProjectCentralTopMenu();
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + url;
    }

    public String getContainerHeader() {
        if (getAppVersion().verGreaterOrEqual(_9_1)) {
            // its for web-driver.
            getOptions().standardScreen();
        }
        return super.getContainerHeader();
    }

    public Grid getGrid() {
        return grid;
    }

    public List<String> getMessagesFromTop() {
        if (getDriver().getType().isIE())
            Grid.LOADING_GRID_WAITER_IE.waitTime();
        return super.getMessagesFromTop();
    }

    public Document getDocument() {
        super.getDocument();
        grid.setDefaultElement(document);
        if (getAppVersion().verGreaterOrEqual(_9_1) && teamPane != null)
            teamPane.setDefaultElement(document);
        return document;
    }

    public TeamPane getTeamPane() {
        if (getAppVersion().verLessThan(_9_1)) {
            throw new SkipException("not for this version");
        }
        return teamPane;
    }

    public Gantt getGantt() {
        return gantt;
    }

    public VariableAllocation getVariableAllocation() {
        return variableAllocation;
    }

    public Histogram getHistogram() {
        return histogram;
    }


    /**
     * @param initPc    WBSPage object
     * @param rootWork  - Work Object
     * @param goBackWay - of true use 'goBack', otherwise - 'Display from parent'
     * @param check     - if true then check header of page
     */
    private static void makeTree(WBSPage initPc, Work rootWork, boolean goBackWay, boolean check) {
        List<String> chs = initPc.getGrid().getFirstLevelListTree();
        //chs.remove(0);
        rootWork.setChildren(chs);
        PSLogger.debug("WBS tree list " + chs);
        for (Work ch : rootWork.getChildren()) {
            WBSPage pc = initPc.getGrid().openWBSPageByDisplayFromHere(ch.getName());
            if (check)
                Assert.assertEquals(pc.getContainerHeader(), ch.getName(), "Incorrect header after 'display from hear'");
            makeTree(pc, ch, goBackWay, check);
            if (goBackWay) {
                pc.goBack();
            } else {
                pc.getGrid().openWBSPageByDisplayFromParent(ch.getName());
            }
            if (check)
                Assert.assertEquals(pc.getContainerHeader(), rootWork.getName(),
                        "Incorrect header after go back/'display from parent'");
        }
    }

    /**
     * load tree dependencies to ChildWork,
     * use go back instead 'display from parent' and don't validate header
     *
     * @return ChildWork
     */
    public Work makeTree() {
        Work root = new VisibleWork(getGrid().getListTree().get(0));
        makeTree(this, root, true, false);
        return root;
    }

    /**
     * load tree dependencies to ChildWork using 'Display from parent' functionality
     *
     * @return ChildWork
     */
    public Work makeTreeByDisplayFromParent() {
        Work root = new VisibleWork(getGrid().getListTree().get(0));
        makeTree(this, root, false, true);
        return root;
    }

    public Work makeTreeByShowLevel() {
        int level = 1;
        List<Work> chs1 = getListTreeByShowLevel(level++);
        List<Work> chs2 = getListTreeByShowLevel(level++);

        List<Work> toReturn = new ArrayList<Work>(chs1);

        while (chs1.size() != chs2.size()) {
            int i1 = 0;
            int i2 = 0;
            while (i2 != chs2.size() - 1) {
                Work prev = chs1.get(i1++);
                while (!isWorkPresentInList(chs1, chs2.get(++i2))) {
                    prev.addChild(chs2.get(i2));
                    if (i2 == chs2.size() - 1) break;
                }
            }//~

            chs1 = chs2;
            chs2 = getListTreeByShowLevel(level++);
        }
        Work parent = toReturn.remove(0);
        parent.addChildren(toReturn);
        return parent;
    }

    private boolean isWorkPresentInList(List<Work> works, Work ch) {
        for (Work work : works) {
            if (work.getName().equals(ch.getName())) return true;
        }
        return false;
    }

    private List<Work> getListTreeByShowLevel(int num) {
        getOptions().showLevel(num);
        List<Work> chs = new ArrayList<Work>();
        for (String ch : getGrid().getListTree()) {
            chs.add(new VisibleWork(ch));
        }
        return chs;
    }

    public OptionsBlock getOptions() {
        OptionsBlock block = new OptionsBlock();
        if (getAppVersion().verGreaterOrEqual(_9_0)) {
            return block;
        }
        return (OptionsBlock) block.open(OPTIONS_BLOCK_OPTIONS_NAME);
    }

    public void showGantt() {
        //if (getGantt().getHeader().isVisible()) return;
        if (getGantt().isVisible()) return;
        PSLogger.info("Show gantt");
        if (getAppVersion().verGreaterOrEqual(_9_0)) {
            getDisplayMenu().select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_GANTT);
            closeAnyMenus();
            //getGantt().getHeader().waitForVisible();
            getGantt().waitForVisible();
            getDocument();
        } else {
            showHideOptions(true, true, OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_GANTT);
        }
    }

    public void hideGantt() {
        //if (!getGantt().getHeader().isVisible()) return;
        if (!getGantt().isVisible()) return;
        PSLogger.info("Hide gantt");
        if (getAppVersion().verGreaterOrEqual(_9_0)) {
            getDisplayMenu().unSelect(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_GANTT);
            closeAnyMenus();
            //getGantt().getHeader().waitForUnvisible();
            getGantt().waitForUnvisible();
            getDocument();
        } else {
            showHideOptions(true, false, OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_GANTT);
        }
    }

    public void showCriticalPath() {
        if (getAppVersion().verGreaterOrEqual(_9_0)) {
            getDisplayMenu().select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_CRITICAL_PATH);
            closeAnyMenus();
        } else {
            showHideOptions(true, true, OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_CRITICAL_PATH);
        }
    }

    public void showTeamPane() {
        if (getAppVersion().verGreaterOrEqual(_9_1)) {
            selectTeamPane(0);
        } else {
            getDisplayMenu().select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE);
        }
        closeAnyMenus();
    }

    public void showHistogram() {
        if (getAppVersion().verLessThan(_9_1)) PSSkipException.skip("unsupported");
        selectTeamPane(1);
        closeAnyMenus();
    }

    /**
     * it for 9.1 or greater
     *
     * @param loc locator for item
     */
    private void clickDisplayItemAndWaitWhileLoading(WBSEPageLocators loc, boolean show) {
        Menu menu = getDisplayMenu();
        if (show) {
            if (menu.isSelected(loc)) return;
            menu.select(loc);
        } else {
            if (!menu.isSelected(loc)) return;
            menu.unSelect(loc);
        }
        waitWhileLoading();
        closeAnyMenus();
    }


    public void showHideFolders(boolean show) {
        if (getAppVersion().verLessThan(_9_1)) {
            OptionsBlock.Filter filters = getOptions().getFilter();
            filters.setDisplayFolders(show);
            filters.apply();
            return;
        }
        clickDisplayItemAndWaitWhileLoading(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_FOLDERS, show);
    }

    public void showHideTasks(boolean show) {
        if (getAppVersion().verLessThan(_9_1)) {
            OptionsBlock.Filter filters = getOptions().getFilter();
            filters.setDisplayTasks(show);
            filters.apply();
            return;
        }
        clickDisplayItemAndWaitWhileLoading(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TASKS, show);

    }


    /**
     * this method is for 91
     *
     * @param option (0 - as list, 1 - as histogram, 2 - hide)
     */
    public void selectTeamPane(int option) {
        Menu display = getDisplayMenu();
        //Menu sub = display.callWithSubmenu(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE);
        if (option == 0)
            display.select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_LIST);
        else if (option == 1)
            display.select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_HISTOGRAM);
        else {
            display.unSelect(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_LIST);
            display.unSelect(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE_HISTOGRAM);
        }
    }

    public void showVariableAllocation() {
        if (getVariableAllocation().isVisible()) return;
        PSLogger.info("Show Variable Allocations");
        getDisplayMenu().select(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_VARIABLE_ALLOCATIONS);
        closeAnyMenus();
        getVariableAllocation().waitForVisible();
        getVariableAllocation().getForthCell().waitForVisible();
        getDocument();
    }

    public void hideVariableAllocation() {
        if (!getVariableAllocation().isVisible()) return;
        PSLogger.info("Hide Variable Allocations");
        getDisplayMenu().unSelect(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_VARIABLE_ALLOCATIONS);
        closeAnyMenus();
        getVariableAllocation().waitForUnvisible();
        getDocument();
    }

    public void hideTeamPane() {
        if (getAppVersion().verGreaterOrEqual(_9_1)) {
            selectTeamPane(2);
        } else {
            getDisplayMenu().unSelect(OPTIONS_BLOCK_DISPLAY_SHOW_HIDE_TEAM_PANE);
        }
        closeAnyMenus();
    }

    /**
     * this method for 8.2
     *
     * @param doApply  if true than apply
     * @param yes      if true then select
     * @param checkbox - locator for checkbox
     */
    private void showHideOptions(boolean doApply, boolean yes, WBSEPageLocators checkbox) {
        OptionsBlock options = getOptions();
        options.getColumns().getCheckbox(checkbox).select(yes);
        if (doApply)
            options.apply();
    }

    public void setIncludeIndependentWork() {
        setIncludeIndependentWork(true);
    }

    public void setIncludeIndependentWork(boolean yes) {
        if (getAppVersion().verGreaterOrEqual(_9_1)) {
            //now no 'include independent work'...#75154
            //selectFiltersOptions(yes, OPTIONS_BLOCK_OPTIONS_INDEPENDENT_WORK_LABEL);
        } else {
            selectFiltersOptions(yes, true, OPTIONS_BLOCK_OPTIONS_INDEPENDENT_WORK_CHECKBOX);
        }
    }

    public void setRunSchedulerAutomatically(boolean yes) {
        if (getAppVersion().verGreaterOrEqual(_9_1)) {
            selectFiltersOptions(yes, OPTIONS_BLOCK_OPTIONS_SCHEDULER_LABEL);
        } else {
            selectFiltersOptions(yes, true, OPTIONS_BLOCK_OPTIONS_RUN_SCHEDULER_AUTOMATICALLY);
        }
    }

    /**
     * this method for 9.1
     *
     * @param doApply  if true than apply
     * @param yes      if true then select
     * @param checkbox - locator for checkbox
     */
    private void selectFiltersOptions(boolean yes, ILocatorable label) {
        Menu options = getOptionsMenu();
        if (options.doSelect(label, yes)) {
            waitWhileLoading();
            checkJSError();
        }
        closeAnyMenus();
    }

    /**
     * this method for 9.0
     *
     * @param doApply  if true than apply
     * @param yes      if true then select
     * @param checkbox - locator for checkbox
     */
    private void selectFiltersOptions(boolean yes, boolean doApply, ILocatorable checkbox) {
        OptionsBlock.Filter filter = getOptions().getFilter();
        CheckBox ch = filter.getOptionsCheckbox(checkbox);
        ch.select(yes);
        if (doApply)
            filter.apply();
    }


    /**
     * open menu for display gantt etc
     * this method is for 9.0
     *
     * @return Menu
     */
    private Menu getDisplayMenu() {
        Element link = AbstractOptionsBlock.searchTopLink(document, OPTIONS_BLOCK_DISPLAY_NAME);
        Menu menu = new Menu(link, true);
        menu.open();
        return menu;
    }


    /**
     * open menu for options etc
     * this method is for 9.1
     *
     * @return Menu
     */
    private Menu getOptionsMenu() {
        Element link = AbstractOptionsBlock.searchTopLink(document, OPTIONS_BLOCK_OPTIONS_NAME);
        Menu menu = new Menu(link, true);
        menu.open();
        return menu;
    }


    public void applyBulkOperation() {
        getElement(false, BULK_OPERATIONS_APPLY).click(false);
        JS_WAITER.waitTime();
        getDocument();
        setFreshDocument(true);
    }

    public void chooseBulkOperation(String label) {
        PSLogger.info("Select '" + label + "' in bulk operations block");
        closeAnyMenus(); // workaround for ie and ps 9.0
        ComboBox c = new ComboBox(getElement(false, BULK_OPERATIONS_COMBOBOX_SELECTOR));
        c.select(label);
    }

    public void chooseBulkOperation(ILocatorable loc) {
        chooseBulkOperation(loc.getLocator());
    }

    public BulkEditBlock openBulkEditBlock() {
        chooseBulkOperation(BULK_OPERATIONS_EDIT_LABEL);
        applyBulkOperation();
        BulkEditBlock block = new BulkEditBlock();
        block.waitForVisible();
        getDocument();
        PSLogger.save("After opening bulk edit block");
        return block;
    }

    /**
     * @param confirm this is for 9.3 or latter
     * @return dialog
     */
    public AllocateResourceDialog openAllocateResourceDialog(boolean confirm) {
        chooseBulkOperation(BULK_OPERATIONS_ALLOCATE_RESOURCE_LABEL);
        applyBulkOperation();
        checkForPopupError();
        AllocateResourceDialog allocateResource = new AllocateResourceDialog(confirm);
        allocateResource.waitForPopup();
        PSLogger.save("After opening 'Allocate Resource' dialog");
        return allocateResource;
    }

    public AllocateResourceDialog openAllocateResourceDialog() {
        return openAllocateResourceDialog(false);
    }

    public void pushBulkDelete() {
        chooseBulkOperation(BULK_OPERATIONS_DELETE_LABEL);
        applyBulkOperation();
    }

    public DeleteDialog bulkDelete() {
        pushBulkDelete();
        DeleteDialog d = new DeleteDialog();
        d.waitForVisible();
        PSLogger.save("Bulk delete popup");
        return d;
    }

    public boolean isSchedulerIsUpToDate() {
        return !new Element(RUN_SCHEDULER_IMG).exists();
    }

    public void runScheduler() {
        new Element(RUN_SCHEDULER_IMG).click(false);
        new Element(RUN_SCHEDULER_UP_TO_DATE_IMG).waitForPresent();
        getDocument();
    }


    public Slider getGanttSlider() {
        return new Slider(getElement(false, GRID_GANTT_SLIDER));
    }

    public void incrementGantt() {
        PSLogger.info("Zoom gantt");
        Element header = getElement(false, GRID_GANTT_HEADER_TOP_ROW);
        String clazz = header.getElementClass();
        PSLogger.debug("Class for gantt header is " + clazz);
        getElement(false, GRID_GANTT_SLIDER_INCREMENT).mouseDownAndUp();
        header.waitForClassChanged(clazz);
        getDocument();
        PSLogger.save("After zooming gantt");
    }

    public void decrementGantt() {
        PSLogger.info("Reduce gantt");
        Element header = getElement(false, GRID_GANTT_HEADER_TOP_ROW);
        String clazz = header.getElementClass();
        PSLogger.debug("Class for gantt header is " + clazz);
        getElement(false, GRID_GANTT_SLIDER_DECREMENT).mouseDownAndUp();
        header.waitForClassChanged(clazz);
        getDocument();
        PSLogger.save("After reducing gantt");
    }

    public float getSliderValue() {
        return Float.parseFloat(getGanttSlider().getValue()) / 4;
    }


    public WBSPage openProjectPlanning() {
        return openProjectCentral(LAYOUTS_PROJECT_PLANING);
    }

    public WBSPage openResourcePlanning() {
        return openProjectCentral(LAYOUTS_RESOURCE_PLANING);
    }

    protected WBSPage openProjectCentral(ILocatorable name) {
        if (getAppVersion().verLessThan(_9_1) || getAppVersion().verGreaterOrEqual(_11_0)) {
            return super.openProjectCentral(name);
        }
        getOptions().getLayoutMenu().call(name);
        waitForPageToLoad();
        WBSPage wbsPage = new WBSPage();
        wbsPage.waitWhileLoading();
        return wbsPage;
    }

    /**
     * class for Grid Section
     */
    public class Grid extends SimpleGrid {

        /**
         * call submenu for root
         *
         * @return TreeWorkMenu
         */
        public TreeWorkMenu callSubMenu() {
            PSLogger.info("Call sub-menu for root work from page");
            TreeWorkMenu menu = new TreeWorkMenu();
            menu.waitForPresent(); // for ie
            if (getDriver().getType().isIE()) JS_WAITER.waitTime();
            menu.open();
            return menu;
        }

        /**
         * call submenu for specified child
         *
         * @param item
         * @return TreeWorkMenu
         */
        public TreeWorkMenu callSubMenu(String item) {
            PSLogger.info("Call sub-menu for '" + item + "'");
            int num = 3;
            Element img = null;
            for (int i = 0; i < num; i++) {
                img = getSubMenuImg(item);
                if (img != null) break;
                PSLogger.warn("Can't find img for sub-menu (iter #" + (i + 1) + ")");
                JS_WAITER.waitTime();
                getDocument();
            }
            TreeWorkMenu menu = new TreeWorkMenu(img);
            menu.open();
            checkJSError();
            return menu;
        }

        private Element getSubMenuImg(String item) {
            Element img;
            List<String> sList = getListTree();
            List<Element> eList = Element.searchElementsByXpath(getBody(), TREE_ITEM_GENERAL_ARROW);
            int index = sList.indexOf(item);
            if (eList.size() != sList.size() || sList.size() == 0) {
                return null;
            }
            img = eList.get(index);
            img.waitForPresent();
            if (!getElementsListTree(TREE_ITEM).get(index).getText().equals(item)) return null;
            String attr = img.getAttribute(TREE_ITEM_ARROW_TYPE_NAME);
            if (attr != null && attr.equals(TREE_ITEM_ARROW_TYPE_VALUE.getLocator()))
                return img;
            return null;
        }

        /**
         * @param work
         * @param type 0 - status, 1 - start date, 2 - end date
         * @return GateProjectDialog
         */
        public GateProjectDialog callGateProjectDialog(Work work, int type) {
            if (getAppVersion().verGreaterOrEqual(_9_3)) {
                PSSkipException.skip("Update Status is not supported in " + _9_3);
            }
            if (getAppVersion().verGreaterOrEqual(_9_2) && type == 0 && work.getParent() != null) {
                work = work.getParent().getChildren().get(0); // there are two status cells are editable on 9.2, but all on 9.1
            }
            Element cell;
            ILocatorable loc = null;
            if (type == 0) {
                loc = GRID_TABLE_STATUS;
            } else if (type == 1) {
                loc = GRID_TABLE_CONSTRAINT_START;
            } else if (type == 2) {
                loc = GRID_TABLE_CONSTRAINT_END;
            }
            PSLogger.info("Call Update-Status dialog for " + work.getName() + " and column " + loc.getLocator());
            cell = getCellByName(loc, work);
            if (!isCellEditable(cell)) {
                PSLogger.warn("Cell " + cell.getLocator() + " is not editable");
                return null;
            }
            PSLogger.info("Call gates project dialog for " + work);
            cell.click(false);
            waitForVisible();
            GateProjectDialog popup = new GateProjectDialog();
            popup.waitForVisible();
            return popup;

        }

        public boolean isWorkPresent(String name) {
            return new Link(TREE_ITEM_WORK_LINK_PATTERN.replace(name)).exists();
        }

        protected int getRowIndex(List<String> tree, Work work) {
            int i = work.getResourceRowIndex();
            int res = (i != -1) ? super.getRowIndex(tree, work.getParent()) + i : super.getRowIndex(tree, work);
            work.setRowIndex(res); // for sorting in tcs...
            return res;
        }

        public int getWorkIndex(Work work) {
            int res = getIndexesList().get(work.getRowIndex() - 1);
            work.setGeneralIndex(res);
            return res;
        }

        public List<Integer> getIndexesList() {
            List<Integer> res = new ArrayList<Integer>();
            for (Element e : Element.searchElementsByXpath(this, GRID_TABLE_CELL_INDEX)) {
                String txt = e.getDEText().trim();
                if (txt.matches("\\d+")) {
                    res.add(Integer.valueOf(txt));
                } else {
                    res.add(-1); // empty index. e.g. resource
                }
            }
            return res;
        }


        @Override
        protected ILocatorable getTreeItemLoc() {
            return TREE_ITEM;
        }

        public List<String> getFirstLevelListTree() {
            List<Element> contents = getTreeChildrenContents();
            if (contents.isEmpty()) return Collections.emptyList();
            List<Element> tree = Element.searchElementsByXpath(contents.get(0), TREE_ITEM_FIRST_LEVEL);
            return getListTree(tree);
        }

        public void setIndexes(Work parent) {
            if (!waitGrid) return;
            List<String> tree = getListTree();
            List<Integer> indexes = getIndexesList();
            List<Work> works = parent.getAllChildren();
            works.add(0, parent);
            for (Work work : works) {
                int i = getRowIndex(tree, work);
                if (i != -1 && work.getResourceRowIndex() == -1) {
                    work.setGeneralIndex(indexes.get(i - 1));
                }
            }
        }

        @Override
        protected void waitWhileChangesIsApplied() {
            errors = null;
            try {
                super.waitWhileChangesIsApplied();
                PSLogger.save("After changes");
            } catch (Wait.WaitTimedOutException w) {
                checkForErrorMessagesFromTop();
                getGeneralActions().checkReload(w);
                throw w;
            }
        }

        @Override
        public void setDefaultElement() {
            getDocument();
        }

        protected Element getCellByName(String name, Work work) {
            Element res = super.getCellByName(name, work);
            if (res == null) {
                setIsDocumentFresh();
                checkForErrorMessagesFromTop();
            }
            return res;
        }

        /**
         * for Status column
         *
         * @param work   ChildWork
         * @param status Status
         */
        public void setStatus(Work work, String status) {
            super.setStatus(work, status);
            setIsDocumentFresh(); // we have document after setting status. is it fresh?
            checkForErrorMessagesFromTop();
        }

        public boolean isTagCellEditable(Work work, PSTag tg) {
            String name = tg.isInResources() ? GRID_TABLE_TAG_WORK_NAME.replace(tg.getName()) : tg.getName();
            Element cell = getCellByName(name, work);
            return isCellEditable(cell);
        }

        /**
         * general method for set tags
         *
         * @param work
         * @param tags
         */
        public void setTags(Work work, PSTag... tags) {
            for (PSTag tag : tags) {
                PSTag parent = tag;
                if (tag.hasParent()) parent = tag.getParent();
                String name = parent.isInResources() ? GRID_TABLE_TAG_WORK_NAME.replace(parent.getName()) : parent.getName();

                Element cell = getCellByName(name, work);
                PSLogger.debug(work.getName() + "'s cell locator is " + cell.getLocator());
                Assert.assertTrue(cell.exists(),
                        "Can't find cell for work " + work.getName() + " and tag " + name);
                if (!isCellEditable(cell)) {
                    PSLogger.warn("Can't set tag " + name + " for " + work + ": cell is not editable");
                    continue;
                }

                if (parent.isMultiple()) { // use TagChooser element
                    TagChooser tc = new TagChooser(cell);
                    if (!tag.hasParent()) { // set all if this is general tag
                        PSLogger.info("Set all subtags for multiple tag " + parent.getFullName() + " for work " + work.getName());
                        tc.setAllLabels();
                    } else { // set only one child
                        PSLogger.info("Set multiple tag " + tag.getFullName() + " for work " + work.getName());
                        tc.setLabel(tag.getName());
                    }
                } else { // use Menu element, only for tags with parent
                    PSLogger.info("Set not multiple tag " + tag.getFullName() + " for work " + work.getName());
                    if (!tag.hasParent()) {
                        PSLogger.warn("Incorrect configuration, tag " + tag.getFullName() + " hasn't parent");
                    }
                    SingleStatusSelector tc = new SingleStatusSelector(cell);
                    tc.setLabel(tag.getName());
                }
                waitWhileChangesIsApplied();
            }
        }

        public void setPerson(Work work, User person) {
            setPerson(work, person.getFormatFullName());
        }

        public void setPerson(Work work, String person) {
            PSLogger.info("Set person '" + person + "' for work " + work);
            Element cell = getCellByName(GRID_TABLE_PERSON, work);
            cell.click(false);
            checkForPopupError();
            if (getAppVersion().verGreaterOrEqual(_12)) {
                Element click = getElement(false, ALLOCATE_RESOURCES_COMBOBOX_12);
                click.waitForVisible();
                ComboBox tc = new AjaxComboBox(click);
                //tc.select(ALLOCATE_RESOURCES_COMBOBOX_12_SEARCH);
                tc.waitForVisible();
                tc.set(ALLOCATE_RESOURCES_COMBOBOX_12_SEARCH.getLocator());
            }
            AllocateResourceDialog dialog = new AllocateResourceDialog(false);
            dialog.waitForPopup();
            PSLogger.save("After opening 'Allocate Resource' dialog");
            dialog.doSearch(person);
            waitWhileChangesIsApplied();
        }

        public void setResourcePool(Work work, String pool) {
            TagChooser tc = getTagChooser(GRID_TABLE_RESOURCE_POOL, work);
            if (!isCellEditable(tc)) {
                PSLogger.warn(GRID_TABLE_RESOURCE_POOL + " is not editable in grid for " + work);
                return;
            }
            tc.setLabel(pool);
            waitWhileChangesIsApplied();
        }

        public void setRole(Work work, String rool) {
            TagChooser tc = getTagChooser(GRID_TABLE_ROLE, work);
            if (!isCellEditable(tc)) {
                PSLogger.warn(GRID_TABLE_ROLE + " is not editable in grid for " + work);
                return;
            }
            tc.setLabel(rool);
            waitWhileChangesIsApplied();
        }

        public void setConstraintDuration(Work work, int val) {
            new GridInput(work, GRID_TABLE_CONSTRAINT_DURATION, GRID_TABLE_NUMBER_INPUT).set(String.valueOf(val));
        }

        public void setResourceDuration(Work work, int val) {
            new GridInput(work, GRID_TABLE_DURATION, GRID_TABLE_NUMBER_INPUT).set(String.valueOf(val));
        }

        /**
         * @param work
         * @param val  0..1
         */
        public void setResourceAllocation(Work work, float val) {
            PSLogger.info("Set Resource Allocation for " + work + " to " + val * 100);
            new GridInput(work, GRID_TABLE_ALLOCATION, GRID_TABLE_NUMBER_INPUT).set(String.valueOf(val * 100));
        }

        public void setName(Work work, String name) {
            new GridInput(work, GRID_TABLE_NAME, GRID_TABLE_TEXTBOX_INPUT, GRID_TABLE_TEXTBOX_INPUT_DIV).set(name);
        }

        public void setConstraintStart(Work work, String start) {
            PSLogger.debug("Set constraint start date for " + work.getName() + " to " + start);
            getConstraintStartDatePicker(work).set(start);
        }

        public void setConstraintEnd(Work work, String end) {
            PSLogger.debug("Set constraint end date for " + work.getName() + " to " + end);
            getConstraintEndDatePicker(work).set(end);
        }

        public void setConstraintType(Work work, Work.Constraint con) {
            setConstraint(work, con.getValue());
        }

        public boolean isConstraintTypeEditable(Work work) {
            return isCellEdited(GRID_TABLE_CONSTRAINT_TYPE, work);
        }

        /**
         * set constraint type both for gated and general children.
         *
         * @param work - ChildWork project
         * @param type String constraint type
         */
        public void setConstraint(Work work, String type) {
            PSLogger.info("Set constraint type '" + type + "' for project " + work.getName());
            SingleStatusSelector sss = getSingleStatusSelector(GRID_TABLE_CONSTRAINT_TYPE, work);
            if (!isCellEditable(sss)) {
                PSLogger.warn("Can't set constraint type for " + work + ": cell is not editable");
                return;
            }
            sss.openPopup();
            Assert.assertTrue(sss.getAllLabels().contains(type), "Can't find constraint " + type);
            sss.set(type);
            if (getAppVersion().verLessThan(_9_3)) {
                if (isProjectAGate(work)) { // gated
                    PSLogger.info("This is a gate : " + work.getName());
                    GateSaveDialog dialog = new GateSaveDialog();
                    dialog.waitForVisible();
                    dialog.ok();
                    waitWhileChangesIsApplied();
                } else {
                    PSLogger.info("This is not a gate : " + work.getName());
                }
            }

        }

        public String getName(Work work) {
            return getCellByName(GRID_TABLE_NAME, work).getText();
        }

        public List<PSTag> getTags(Work work, PSTag... parentTags) {
            List<PSTag> res = new ArrayList<PSTag>();
            for (PSTag tag : parentTags) {
                String name = tag.isInResources() ? GRID_TABLE_TAG_WORK_NAME.replace(tag.getName()) : tag.getName();
                Element cell = getCellByName(name, work);
                Assert.assertTrue(cell.exists(), "Can't find cell for work " + work.getName() +
                        " and tag " + name);
                String fromPage = cell.getText().trim();
                if (fromPage.isEmpty()) continue;
                PSLogger.debug("From cell: '" + fromPage + "'");
                for (String text : fromPage.split(",\\s*")) {
                    res.add(new PSTag(tag.getName() + PSTag.SEPARATOR + text));
                }
            }
            return res;
        }

        public String getPerson(Work work) {
            return getCellByName(GRID_TABLE_PERSON, work).getText();
        }

        public String getConstraint(Work work) {
            return getCellByName(GRID_TABLE_CONSTRAINT_TYPE, work).getText();
        }

        public Work.Constraint getConstraintType(Work work) {
            return Work.Constraint.getByName(getConstraint(work));
        }

        public String getPercentComplete(Work work) {
            return getCellByName(GRID_TABLE_PERCENT_COMPLETE, work).getText().replace("%", "");
        }

        public String getResourcePool(Work work) {
            String res = getCellByName(GRID_TABLE_RESOURCE_POOL, work).getText();
            return res.isEmpty() ? null : res;
        }

        public String getRole(Work work) {
            String res = getCellByName(GRID_TABLE_ROLE, work).getText();
            return res.isEmpty() ? null : res;
        }

        public int getResourceDuration(Work work) {
            return getDuration(GRID_TABLE_DURATION, work);
        }

        public String getResourceDurationAsString(Work work) {
            return getResourceDuration(work) + DAYS_SUFFIX.getLocator();
        }

        public String getAllocation(Work work) {
            return getCellByName(GRID_TABLE_ALLOCATION, work).getText().replace("%", "");
        }

        /**
         * @param work
         * @return 0..1
         */
        public float getResourceAllocation(Work work) {
            String res = getAllocation(work);
            if (!res.matches("\\d+\\.\\d+")) return -1;
            return Float.parseFloat(res) / 100;
        }

        public Work.CalculatedField getCalculatedField(Work work) {
            if (getAppVersion().inRange(_12, _12_1)) {
                String txt = getCellByName(GRID_TABLE_CALCULATED_FIELD_V12, work).getText();
                if (GRID_TABLE_CALCULATED_FIELD_V12_ALLOCATION.getLocator().equals(txt))
                    return Work.CalculatedField.ALLOCATION;
                if (GRID_TABLE_CALCULATED_FIELD_V12_EFFORT.getLocator().equals(txt)) return Work.CalculatedField.EFFORT;
                if (GRID_TABLE_CALCULATED_FIELD_V12_DURATION.getLocator().equals(txt))
                    return Work.CalculatedField.DURATION;
            } else {
                boolean allocation = isCalculatedField(work, GRID_TABLE_ALLOCATION);
                boolean effort = isCalculatedField(work, GRID_TABLE_EFFORT);
                boolean duration = isCalculatedField(work, GRID_TABLE_DURATION);
                if (allocation && !effort && !duration) return Work.CalculatedField.ALLOCATION;
                if (effort && !allocation && !duration) return Work.CalculatedField.EFFORT;
                if (duration && !allocation && !effort) return Work.CalculatedField.DURATION;
            }
            return null;
        }

        private boolean isCalculatedField(Work work, ILocatorable loc) {
            return getCellByName(loc, work).
                    getChildByXpath(GRID_TABLE_CELL_CALCULATED_FIELD_ACRONYM).exists();
        }

        public String getEffort(Work work) {
            return getCellByName(GRID_TABLE_EFFORT, work).getText();
        }

        public String getDependency(Work work) {
            return getCellByName(GRID_TABLE_DEPENDENCY, work).getText();
        }

        public List<WorkDependency> getDependencyAsList(Work work) {
            String fromPage = getDependency(work);
            List<WorkDependency> res = new ArrayList<WorkDependency>();
            for (String part : fromPage.split(GRID_TABLE_DEPENDENCY_SEPARATOR.getLocator())) {
                WorkDependency dep = WorkDependencyDialog.parse(part);
                if (dep != null)
                    res.add(dep);
            }
            return res;
        }

        public String getConstraintStartDate(Work work) {
            return getDatePicker(GRID_TABLE_CONSTRAINT_START, work).get();
        }

        public String getConstraintEndDate(Work work) {
            return getDatePicker(GRID_TABLE_CONSTRAINT_END, work).get();
        }

        public int getConstraintDuration(Work work) {
            return getDuration(GRID_TABLE_CONSTRAINT_DURATION, work);
        }

        public int getScheduledDuration(Work work) {
            return getDuration(GRID_TABLE_CONSTRAINT_DURATION, work);
        }

        private int getDuration(ILocatorable loc, Work work) {
            String txt = getCellByName(loc, work).getText().trim().
                    replace(DAYS_SUFFIX.getLocator(), "");
            if (txt.matches("\\d+"))
                return Integer.parseInt(txt);
            return -1;
        }

        public String getActualStartDate(Work work) {
            return getDatePicker(GRID_TABLE_ACTUAL_START, work).get();
        }

        public String getActualEndDate(Work work) {
            return getDatePicker(GRID_TABLE_ACTUAL_END, work).get();
        }

        public String getScheduledStartDate(Work work) {
            return getCellByName(GRID_TABLE_SCHEDULED_START, work).getText();
        }

        public String getScheduledEndDate(Work work) {
            return getCellByName(GRID_TABLE_SCHEDULED_END, work).getText();
        }

        public DatePicker getActualStartDatePicker(Work work) {
            DatePicker dp = getDatePicker(GRID_TABLE_ACTUAL_START, work);
            if (isCellEditable(dp)) return dp;
            PSLogger.info("'Actual Start' is not editable for " + work);
            return null;
        }

        public DatePicker getActualEndDatePicker(Work work) {
            DatePicker dp = getDatePicker(GRID_TABLE_ACTUAL_END, work);
            if (isCellEditable(dp)) return dp;
            PSLogger.info("'Actual End' is not editable for " + work);
            return null;
        }

        public DatePicker getConstraintStartDatePicker(Work work) {
            DatePicker dp = getDatePicker(GRID_TABLE_CONSTRAINT_START, work);
            if (isCellEditable(dp)) return dp;
            PSLogger.info("'Constraint Start' is not editable for " + work);
            return null;
        }

        public DatePicker getConstraintEndDatePicker(Work work) {
            DatePicker dp = getDatePicker(GRID_TABLE_CONSTRAINT_END, work);
            if (isCellEditable(dp)) return dp;
            PSLogger.info("'Constraint End' is not editable for " + work);
            return null;
        }

        public String getPriority(Work work) {
            return getSingleStatusSelector(GRID_TABLE_PRIORITY, work).getContent();
        }

        @Override
        public WBSDatePicker getDatePicker(ILocatorable loc, Work work) {
            Element cell = getCellByName(loc, work);
            return new WBSDatePicker(cell, work) {
                public void set(String date) {
                    super.set(date);
                    setIsDocumentFresh(); // we have document after setting status. is it fresh?
                    checkForErrorMessagesFromTop();
                }
            };
        }

        public TagChooser getTagChooser(ILocatorable loc, Work work) {
            Element cell = getCellByName(loc, work);
            return new TagChooser(cell);
        }

        public void dragAndDrop(Work from, Work to) {
            List tree = getListTree();
            int iFrom = getRowIndex(tree, from);
            int iTo = getRowIndex(tree, to);
            PSLogger.info("Try to move works (" + iFrom + "->" + iTo + ") : " + from.getName() + " to " + to.getName());
            Element fromCell = getCellByName(from);
            if (iFrom > iTo) iTo--;
            Element toCell = getCell(iTo);
            fromCell.dragAndDrop(toCell);
            waitForVisible();
        }

        public List<CheckBox> getWorksCheckboxes(Work... works) {
            List tree = getListTree();
            Element column = Element.searchElementByXpath(this, GRID_TABLE_CELL_CHECKBOXES_COLUMN);
            List<Element> list = Element.searchElementsByXpath(column, GRID_TABLE_CELL_CHECKBOXES_COLUMN_CHILD);
            List<CheckBox> res = new ArrayList<CheckBox>();
            for (Work work : works) {
                CheckBox ch = new CheckBox(list.get(getRowIndex(tree, work) - 1));
                ch.setName(work.getName());
                res.add(ch);
            }
            return res;
        }

        public void selectWorks() {
            PSLogger.info("Click select all projects");
            new CheckBox(GRID_TABLE_CELL_CHECKBOXES_ALL).click();
        }

        public void unSelectWorks() {
            CheckBox ch = new CheckBox(GRID_TABLE_CELL_CHECKBOXES_ALL);
            PSLogger.info("Unselect all projects");
            if (ch.getChecked()) ch.click();
        }

        public void selectWorks(Work... works) {
            for (CheckBox ch : getWorksCheckboxes(works)) {
                PSLogger.info("Select checkbox for " + ch.getName());
                ch.select(true);
            }
        }

        public void unSelectWorks(Work... works) {
            for (CheckBox ch : getWorksCheckboxes(works)) {
                PSLogger.info("Unselect checkbox for " + ch.getName());
                ch.select(false);
            }
        }

        public void selectWorks(List<Work> works) {
            selectWorks(works.toArray(new Work[]{}));
        }

        public void unSelectWorks(List<Work> works) {
            unSelectWorks(works.toArray(new Work[]{}));
        }

        public SummaryWorkPage openSummaryPage(String name) {
            String loc = TREE_ITEM_WORK_LINK_PATTERN.getLocator().
                    replace(LOCATOR_REPLACE_PATTERN, name);
            Link link = new Link(loc);
            if (!link.exists()) {
                PSLogger.warn("Can't find item '" + name + "' in tree on page");
                return null;
            }

            SummaryWorkPage res = getSummaryInstance();
            link.setResultPage(res);
            link.clickAndWaitNextPage();
            return res;
        }

        public SummaryWorkPage openSummaryPageByView(String name) {
            return callSubMenu(name).view();
        }

        private WBSPage openWBSPageByDisplayFromHere(String name) {
            PSLogger.info("Open child for '" + name + "'");
            return callSubMenu(name).displayFromHere();
        }

        private WBSPage openWBSPageByDisplayFromParent(String name) {
            PSLogger.info("Open parent for '" + name + "'");
            return callSubMenu(name).displayFromParent();
        }

        public WBSPage openWBSPageByDisplayFromHere(Work work) {
            WBSPage res = openWBSPageByDisplayFromHere(work.getName());
            res.getGrid().setIndexes(work);
            return res;
        }

        public WBSPage openWBSPageByDisplayFromParent(Work work) {
            WBSPage res = openWBSPageByDisplayFromParent(work.getName());
            res.getGrid().setIndexes(work);
            return res;
        }

        private class GateSaveDialog extends Dialog {
            public GateSaveDialog() {
                super(GATE_SAVE_DIALOG_POPUP);
                setPopup(GATE_SAVE_DIALOG_POPUP);
            }

            public void ok() {
                getChildByXpath(GATE_SAVE_DIALOG_POPUP_OK).click(false);
                waitForUnvisible();
                PSLogger.save("after pressing 'OK'");
            }

            public void no() {
                getChildByXpath(GATE_SAVE_DIALOG_POPUP_CANCEL).click(false);
                waitForUnvisible();
            }
        }

    }

    public interface Columns {
        public List<CheckBox> getCheckboxes();

        public CheckBox getCheckbox(WBSEPageLocators name);

        public CheckBox getCheckbox(String name);

        public void uncheckAll();

        public List<CheckBox> getCheckedCheckboxes();

        public void apply();
    }

    private static Map<String, List<String>> columnsMenuItems = new LinkedHashMap<String, List<String>>();

    /**
     * class for Options block. It contains Display and Filter classes
     */
    public class OptionsBlock extends AbstractOptionsBlock {
        private Columns display;
        private Filter filter;

        OptionsBlock() {
            super(WBSPage.this);
        }

        public Columns getColumns() {
            if (getAppVersion().verGreaterOrEqual(_9_2)) {
                ColumnsMenu menu = new ColumnsMenu();
                menu.open();
                return display = menu;
            }
            display = new Display();
            if (getAppVersion().verGreaterOrEqual(_9_0)) {
                open(OPTIONS_BLOCK_COLUMNS_NAME);
            }
            return display;
        }

        public Filter getFilter() {
            filter = new Filter();
            if (getAppVersion().verGreaterOrEqual(_9_0)) {
                open(OPTIONS_BLOCK_FILTERS_NAME);
            }
            return filter;
        }


        /**
         * open menu for layouts
         * for 8.2 open project central top menu
         *
         * @return Menu
         */
        public Menu getLayoutMenu() {
            if (getAppVersion().verLessThan(_9_0)) {
                return openProjectCentralTopMenu();
            }
            Menu menu = new Menu(AbstractOptionsBlock.searchTopLink(getDocument(false), OPTIONS_BLOCK_LAYOUTS_NAME), true);
            try {
                menu.open();
                return menu;
            } catch (RuntimeException re) { // its for ie:
                if (!getDriver().getType().isIE()) throw re;
                PSLogger.warn("getLayoutMenu: " + re.getMessage());
                closeAnyMenus();
                menu.open();
                return menu;
            }
        }

        /**
         * this class is for 9.2 or latter
         */
        private class ColumnsMenu extends Menu implements Columns {
            public ColumnsMenu() {
                super(searchTopLink(getDocument(false), OPTIONS_BLOCK_COLUMNS_NAME), true);
            }

            public void open() {
                super.open();
                document = getParentDoc();
            }

            @Override
            public List<CheckBox> getCheckboxes() {
                if (!isMenuOpen()) open();
                List<CheckBox> res = new ArrayList<CheckBox>();
                for (CheckBox item : getCheckBoxesList()) {
                    String name = item.getName();
                    if (!columnsMenuItems.containsKey(name)) {
                        columnsMenuItems.put(name, new ArrayList<String>());
                    } else {
                        columnsMenuItems.get(name).clear();
                    }
                    res.add(item);
                    Menu subMenu = callWithSubmenu(name);
                    for (CheckBox subItem : subMenu.getCheckBoxesList()) {
                        columnsMenuItems.get(name).add(subItem.getName());
                        res.add(subItem);
                    }
                    //closeSubMenus();
                }
                return res;
            }

            @Override
            public CheckBox getCheckbox(WBSEPageLocators name) {
                return getCheckbox(name.getLocator());
            }

            @Override
            public CheckBox getCheckbox(String name) {
                if (!isMenuOpen()) open();
                for (String item : getMenuItems()) {
                    if (item.equals(OPTIONS_BLOCK_COLUMNS_SELECTED.getLocator())) continue;
                    if (!columnsMenuItems.containsKey(item)) {
                        columnsMenuItems.put(item, new ArrayList<String>());
                    }
                    if (item.equals(name)) {
                        return getCheckBox(item);
                    }
                    if (columnsMenuItems.get(item).contains(name)) {
                        Menu sub = callWithSubmenu(item);
                        columnsMenuItems.get(item).clear();
                        columnsMenuItems.get(item).addAll(sub.getMenuItems());
                        return sub.getCheckBox(name);
                    }
                }
                PSLogger.debug("Search for check-box '" + name + "'");
                for (String item : columnsMenuItems.keySet()) {
                    Menu sub = callWithSubmenu(item);
                    columnsMenuItems.get(item).clear();
                    columnsMenuItems.get(item).addAll(sub.getMenuItems());
                    if (columnsMenuItems.get(item).contains(name)) {
                        return sub.getCheckBox(name);
                    }
                }
                PSLogger.warn("Can't find checkbox '" + name + "'");
                return null;
            }

            @Override
            public void uncheckAll() {
                if (!isMenuOpen()) open();
                List<CheckBox> chs = getCheckedCheckboxes();
                PSLogger.debug("All selected check-boxes : " + chs);
                for (CheckBox checkBox : chs) {
                    checkBox.click();
                }
                //closeSubMenus();
            }

            @Override
            public List<CheckBox> getCheckedCheckboxes() {
                if (!isMenuOpen()) open();
                List<CheckBox> res = new ArrayList<CheckBox>();
                for (CheckBox ch : getCheckBoxesList()) {
                    if (ch.getChecked()) res.add(ch);
                }
                res.addAll(callWithSubmenu(OPTIONS_BLOCK_COLUMNS_SELECTED).getCheckBoxesList());
                return res;
            }

            @Override
            public void apply() {
                PSLogger.debug("Apply columns");
                getGrid().mouseMoveAt(0, 0);
                getGrid().mouseDownAndUp();
                waitWhileLoading();
                setDynamic();
            }
        }

        /**
         * this class is for 8.1 - 9.1
         */
        private class Display extends AbstractFilter implements Columns {
            private Display() {
                super(OPTIONS_BLOCK_DISPLAY);
            }

            /**
             * @return List of all checkboxes from display block
             */
            public List<CheckBox> getCheckboxes() {
                List<CheckBox> res = new ArrayList<CheckBox>();
                for (Element e : Element.searchElementsByXpath(this, OPTIONS_BLOCK_DISPLAY_CHECKBOX_INPUT)) {
                    String label = Element.searchElementByXpath(e, OPTIONS_BLOCK_DISPLAY_CHECKBOX_LABEL).getDEText();
                    CheckBox ch = new CheckBox(e);
                    ch.setName(label);
                    res.add(ch);
                }
                return res;
            }

            public CheckBox getCheckbox(WBSEPageLocators name) {
                return getCheckbox(name.getLocator());
            }

            public CheckBox getCheckbox(String name) {
                for (CheckBox ch : getCheckboxes()) {
                    if (ch.getName().trim().equals(name.trim())) return ch;
                }
                PSLogger.warn("Can't find checkbox " + name);
                return null;
            }

            public void uncheckAll() {
                for (CheckBox ch : getCheckedCheckboxes()) {
                    ch.click();
                }
            }

            public List<CheckBox> getCheckedCheckboxes() {
                return getCheckboxes(getCheckboxes(), true);
            }

            private List<CheckBox> getCheckboxes(List<CheckBox> list, boolean isChecked) {
                List<CheckBox> res = new ArrayList<CheckBox>();
                for (CheckBox ch : list) {
                    if (isChecked == ch.getChecked()) {
                        res.add(ch);
                    }
                }
                return res;
            }
        }

        public class Filter extends AbstractFilter {

            Filter() {
                super(OPTIONS_BLOCK_FILTERS);
            }


            /**
             * options block should be open
             *
             * @param type  - can be 0,1,2 - i.e for 'exactly', 'any of' and 'all of'
             * @param toSet - String to search
             */
            public void setName(int type, String toSet) {
                PSLogger.info("Set " + toSet + " in work filter");
                SelectInput select = new SelectInput(OPTIONS_BLOCK_FILTERS_NAME_SELECT);
                if (type == 0)
                    select.select(OPTIONS_BLOCK_FILTERS_NAME_SELECT_EXACTLY);
                else if (type == 1)
                    select.select(OPTIONS_BLOCK_FILTERS_NAME_SELECT_ANY_OF);
                else if (type == 2)
                    select.select(OPTIONS_BLOCK_FILTERS_NAME_SELECT_ALL_OF);
                new Input(OPTIONS_BLOCK_FILTERS_NAME_INPUT_WORK).type(toSet);
            }

            /**
             * for options block, set type to filter
             *
             * @param types - array with types
             */
            public void setWorkType(String... types) {
                PSLogger.info("Set work types " + Arrays.asList(types) + " to filter");
                TagChooser tc = new TagChooser(OPTIONS_BLOCK_FILTERS_TYPES_INPUT);
                tc.openPopup();
                PSLogger.debug("All items : " + tc.getAllLabels());
                tc.selectUnselect(false);
                tc.select(types);
                tc.done();
            }

            /**
             * method to set data to start-date filter
             *
             * @param firstType  - can be 1,2,3,4
             * @param secondType - can be 1,2,3,4
             * @param date       - correct date, e.g "12/11/1912"
             */
            public void setStartDate(int firstType, int secondType, String date) {
                PSLogger.info("Set " + firstType + ", " + secondType + ", " + date + " to start date filter");
                setDate(OPTIONS_BLOCK_FILTERS_DATE_SELECT_START,
                        OPTIONS_BLOCK_FILTERS_DATE_SELECT_START_INTERVAL,
                        OPTIONS_BLOCK_FILTERS_DATE_SELECT_START_PICKER, firstType, secondType, date);
            }

            /**
             * method to set data to end-date filter
             *
             * @param firstType  - can be 1,2,3,4
             * @param secondType - can be 1,2,3,4
             * @param date       - correct date, e.g "12/11/1912"
             */
            public void setEndDate(int firstType, int secondType, String date) {
                PSLogger.info("Set " + firstType + ", " + secondType + ", " + date + " to end date filter");
                setDate(OPTIONS_BLOCK_FILTERS_DATE_SELECT_END,
                        OPTIONS_BLOCK_FILTERS_DATE_SELECT_END_INTERVAL,
                        OPTIONS_BLOCK_FILTERS_DATE_SELECT_END_PICKER, firstType, secondType, date);
            }

            /**
             * auxiliary method
             *
             * @param firstLoc   - locator for first select
             * @param secondLoc  - locator for second select
             * @param thirdLoc   - locator for date picker element
             * @param firstType  - can be 1,2,3,4
             * @param secondType - can be 1,2,3,4
             * @param date       - correct date, e.g "12/11/1912"
             */
            private void setDate(ILocatorable firstLoc,
                                 ILocatorable secondLoc,
                                 ILocatorable thirdLoc,
                                 int firstType,
                                 int secondType,
                                 String date) {
                ILocatorable firstSelectOption;
                ILocatorable secondSelectOption;
                switch (firstType) {
                    case 1:
                        firstSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_ANY;
                        break;
                    case 2:
                        firstSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_CONSTRAINT;
                        break;
                    case 3:
                        firstSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_ACTUAL;
                        break;
                    case 4:
                        firstSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_SCHEDULED;
                        break;
                    default:
                        PSLogger.warn("Incorrect type specified for first locator");
                        return;
                }
                switch (secondType) {
                    case 1:
                        secondSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_IS;
                        break;
                    case 2:
                        secondSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_BEFORE;
                        break;
                    case 3:
                        secondSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_AFTER;
                        break;
                    case 4:
                        secondSelectOption = OPTIONS_BLOCK_FILTERS_DATE_SELECT_INTERVAL_BETWEEN;
                        break;
                    default:
                        PSLogger.warn("Incorrect type specified for second locator");
                        return;
                }
                SelectInput firstSelect = new SelectInput(firstLoc);
                firstSelect.select(firstSelectOption);
                SelectInput secondSelect = new SelectInput(secondLoc);
                secondSelect.select(secondSelectOption);
                DatePicker dp = new DatePicker(thirdLoc);
                dp.useDatePopup(true);
                dp.set(date);
                PSLogger.save("save filter-date settings");
            }

            /**
             * @param parent  - parent tag
             * @param type    0,1,2,3 (Emty, No Values, Any Values, Selected Values)
             * @param subtags list
             */
            public void setTag(int type, PSTag parent, PSTag... subtags) {
                int numberOfAllTags = 10;
                SelectInput firstSelect = new SelectInput(OPTIONS_BLOCK_FILTERS_TAG_SELECT);
                if (parent == null) {
                    firstSelect.select(OPTIONS_BLOCK_FILTERS_TAG_SELECT_EMTY);
                    return;
                }
                firstSelect.select(OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED.
                        getLocator().replace(LOCATOR_REPLACE_PATTERN, parent.getName()));
                ILocatorable optionValue = getOptionValueForSelects(type);
                SelectInput select = new SelectInput(OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED_SELECT);
                select.select(optionValue);
                if (!optionValue.equals(OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE)) {
                    return;
                }
                TagChooser tc = null;
                for (int i = 1; i < numberOfAllTags; i++) {
                    tc = new TagChooser(
                            OPTIONS_BLOCK_FILTERS_TAG_SELECT_SPECIFIED_SELECT_MULTI.getLocator().
                                    replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i))
                    );
                    if (!tc.exists()) break;
                    if (tc.isVisible()) break;
                }
                tc.openPopup();
                if (subtags == null) {
                    // select all
                    tc.selectAll();
                } else if (subtags.length == 0) {
                    // select none
                    tc.selectNone();
                } else {
                    String[] labels = new String[subtags.length];
                    for (int i = 0; i < subtags.length; i++) {
                        labels[i] = subtags[i].getName();
                    }
                    tc.selectUnselect(false);
                    tc.select(labels);
                }
                tc.done();
                PSLogger.info("Selected: " + tc.getContent());
            }


            /**
             * @param type   0,1,2,3 (Emty, No Values, Any Values, Selected Values)
             * @param owners - array with owners
             */
            public void setOwner(int type, User... owners) {
                setFilterWithUserDialogPopup(OPTIONS_BLOCK_FILTERS_OWNER_SELECT,
                        OPTIONS_BLOCK_FILTERS_OWNER_SELECT_MULT_WRAPPER,
                        OPTIONS_BLOCK_FILTERS_OWNER_ASSIGN_USER_POPUP,
                        type, owners);
            }

            /**
             * set status to options filter
             *
             * @param type     - can be 0, 1, 2, 3 (for empty, 'No value', 'Any value', 'Select values...')
             * @param statuses - Array with statuses; if empty than select none, if null than select all, otherwise select specified
             */
            public void setStatus(int type, String... statuses) {
                setFilterWithTagChooser(
                        OPTIONS_BLOCK_FILTERS_STATUS_SELECT,
                        OPTIONS_BLOCK_FILTERS_STATUS_SELECT_TAG_CHOOSER,
                        type, statuses);
            }

            /**
             * @param type  0,1,2,3 (Emty, No Values, Any Values, Selected Values)
             * @param pools - resources pools
             */
            public void setResourcePool(int type, String... pools) {
                setFilterWithTagChooser(
                        OPTIONS_BLOCK_FILTERS_POOL_SELECT,
                        OPTIONS_BLOCK_FILTERS_POOL_SELECT_TAG_CHOOSER,
                        type, pools);
            }

            /**
             * @param type  0,1,2,3 (Emty, No Values, Any Values, Selected Values)
             * @param pools - resources pools
             */
            public void setRole(int type, String... rools) {
                setFilterWithTagChooser(
                        OPTIONS_BLOCK_FILTERS_ROLE_SELECT,
                        OPTIONS_BLOCK_FILTERS_ROLE_SELECT_TAG_CHOOSER,
                        type, rools);
            }

            private void setFilterWithUserDialogPopup(ILocatorable elementLocator,
                                                      ILocatorable callDialogLocator,
                                                      ILocatorable popupDialogLocator,
                                                      int type,
                                                      User... toSearch) {
                ILocatorable optionValue = getOptionValueForSelects(type);
                SelectInput select = new SelectInput(elementLocator);
                if (!optionValue.equals(OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE)) {
                    select.select(optionValue);
                    return;
                }
                select.select(OPTIONS_BLOCK_FILTERS_SELECT_EMPTY_VALUE);
                select.select(optionValue);
                UsersDialog dialog = new UsersDialog(callDialogLocator, popupDialogLocator);
                if (dialog.exists() && dialog.isVisible()) {
                    dialog.click(false);
                }
                dialog.getPopup().waitForVisible();
                for (User user : toSearch) {
                    dialog.search(user.getFullName());
                    dialog.set(user);
                }
                dialog.submit();
            }

            void setFilterWithTagChooser(ILocatorable elementLocator,
                                         ILocatorable tagChooserLocator,
                                         int type,
                                         String... toSearch) {
                ILocatorable optionValue = getOptionValueForSelects(type);
                SelectInput select = new SelectInput(elementLocator);
                if (!optionValue.equals(OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE)) {
                    select.select(optionValue);
                    return;
                }
                StatusChooser tc = new StatusChooser(tagChooserLocator);
                select.select(OPTIONS_BLOCK_FILTERS_SELECT_EMPTY_VALUE);
                tc.waitForUnvisible();
                select.select(optionValue);
                //tc.setSimpleLocator();
                tc.waitForPopup();
                if (tc.getAllLabels().size() == 0) {  // its debug for ie6
                    PSLogger.save("Can't find any items. Popup is closed?");
                    tc.openPopup();
                }
                checkJSError();
                if (toSearch == null) {
                    // select all
                    tc.selectAll();
                } else if (toSearch.length == 0) {
                    // select none
                    tc.selectNone();
                } else {
                    tc.selectUnselect(false);
                    tc.select(toSearch);
                }
                tc.done();
                PSLogger.info("Selected: " + tc.getContent());
            }

            /**
             * @param type
             * @return
             */
            private ILocatorable getOptionValueForSelects(int type) {
                switch (type) {
                    case 0:
                        return OPTIONS_BLOCK_FILTERS_SELECT_EMPTY_VALUE;
                    case 1:
                        return OPTIONS_BLOCK_FILTERS_SELECT_NO_VALUE;
                    case 2:
                        return OPTIONS_BLOCK_FILTERS_SELECT_ANY_VALUE;
                    case 3:
                        return OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE;
                    default:
                        PSLogger.warn("Incorrect type specified for selector in filter options");
                        return null;
                }
            }

            public void setDisplayFolders(boolean flag) {
                // its for 9.0 or below
                getOptionsCheckbox(OPTIONS_BLOCK_OPTIONS_FOLDERS_DISPLAY).select(flag);
            }

            public void setDisplayTasks(boolean flag) {
                // its for 9.0 or below
                getOptionsCheckbox(OPTIONS_BLOCK_OPTIONS_TASKS_DISPLAY).select(flag);
            }


            private CheckBox getOptionsCheckbox(ILocatorable checkbox) {
                CheckBox ch = new CheckBox(checkbox);
                ch.setId(checkbox);
                ch.setName(new Element(OPTIONS_BLOCK_OPTIONS_LABEL.getLocator().
                        replace(LOCATOR_REPLACE_PATTERN, ch.getId())).getText());
                return ch;
            }

            public void checkTeamPaneHours() {
                new RadioButton(OPTIONS_BLOCK_TEAM_PANE_HOURS).select(true);
            }

            public void checkTeamPanePercentage() {
                new RadioButton(OPTIONS_BLOCK_TEAM_PANE_PERCENTAGE).select(true);
            }

            public void setTeamPaneYellow(int value) {
                seValueToInput(OPTIONS_BLOCK_TEAM_PANE_YELLOW_INPUT, String.valueOf(value));
            }

            public void setTeamPaneRed(int value) {
                seValueToInput(OPTIONS_BLOCK_TEAM_PANE_RED_INPUT, String.valueOf(value));
            }

            private void seValueToInput(ILocatorable loc, String value) {
                Input in = new Input(getElement(false, loc));
                in.type(value);
                if (in.isWrongInput()) {
                    PSLogger.warn("Input is wrong. its a bug");
                    PSLogger.save();
                }
            }

        }
    }

    /**
     * class for bulk edit block
     */
    public class BulkEditBlock extends Element {

        public BulkEditBlock() {
            super(BULK_EDIT_BLOCK);
            getDocument();
        }

        public void update() {
            PSLogger.save("Before updating!");
            Button update = new Button(getChildByXpath(BULK_EDIT_UPDATE));
            update.focus();
            update.click(false);
            waitWhileLoading();
            Assert.assertFalse(isVisible(), "Bulk Edit block still visible");
            setIsDocumentFresh();
            checkForErrorMessagesFromTop();
        }

        public void cancel() {
            Element cancel = getChildByXpath(BULK_EDIT_CANCEL);
            cancel.focus(); // for ie and ps90
            cancel.click(false);
            waitForUnvisible();
        }

        public boolean isOpened() {
            return isVisible();
        }

        private Map<String, Element> getControls() {
            Map<String, Element> res = new LinkedHashMap<String, Element>();
            setDefaultElement(document);
            for (Element child : Element.searchElementsByXpath(this, BULK_EDIT_BLOCK_CHILD)) {
                Element label = Element.searchElementByXpath(child, BULK_EDIT_BLOCK_CHILD_LABEL);
                Element nobr;
                if ((nobr = label.getChildByXpath(BULK_EDIT_BLOCK_CHILD_LABEL_INNER)).isDEPresent()) {
                    label = nobr;
                }
                String sLabel = label.getDEInnerText().replaceAll("\\s*:\\s*$", "").trim();
                Element control = Element.searchElementByXpath(child, BULK_EDIT_BLOCK_CHILD_ELEMENT);
                res.put(sLabel, control);
            }
            return res;
        }

        private Element getControl(String loc) {
            PSLogger.debug("search for control " + loc);
            loc = loc.replaceAll("\\s*:\\s*$", "").trim();
            Map<String, Element> controls = getControls();
            PSLogger.debug("controls: " + controls.keySet());
            if (!controls.containsKey(loc)) {
                Assert.fail("Can't find control '" + loc + "' in bulk edit block");
            }
            return controls.get(loc);
        }

        private Element getControl(ILocatorable loc) {
            return getControl(loc.getLocator());
        }

        private Input getControlInput(ILocatorable loc) {
            return new BulkEditInput(getControl(loc));
        }

        private class BulkEditInput extends Input {

            public BulkEditInput(Element e) {
                super(e.getChildByXpath(BULK_EDIT_BLOCK_CHILD_INPUT));
            }

            /**
             * this is workaround for ie and ps9.0
             *
             * @param txt
             */
            public void type(String txt) {
                super.type(txt);
                BulkEditBlock.this.focus(this);
            }
        }

        /**
         * this method for ie and ps9*
         *
         * @param e
         */
        private void focus(Element e) {
            e.focus();
            mouseDownAndUp();
        }

        public SingleStatusSelector getSingleStatusSelector(ILocatorable loc) {
            return new SingleStatusSelector(getControl(loc));
        }

        public void setStatus(String status) {
            SingleStatusSelector selector = getSingleStatusSelector(BULK_EDIT_CONTROL_STATUS_LABEL);
            selector.setLabel(status);
        }

        public void setConstraint(String type) {
            SingleStatusSelector selector = getSingleStatusSelector(BULK_EDIT_CONTROL_CONSTRAINT_LABEL);
            selector.setLabel(type);
        }

        public void setConstraint(Work.Constraint type) {
            setConstraint(type.getValue());
        }

        public void setPriority(String label) {
            SingleStatusSelector selector = getSingleStatusSelector(BULK_EDIT_CONTROL_PRIORITY_LABEL);
            selector.setLabel(label);
        }

        public List<String> getPriorityList() {
            SingleStatusSelector selector = getSingleStatusSelector(BULK_EDIT_CONTROL_PRIORITY_LABEL);
            selector.openPopup();
            return selector.getAllLabels();
        }

        public void setStartDate(String time, boolean howToSet, boolean wayToSet) {
            setStartEndDate(BULK_EDIT_CONTROL_CONSTRAINT_START_LABEL, time, howToSet, wayToSet);
        }

        public void setEndDate(String time, boolean howToSet, boolean wayToSet) {
            setStartEndDate(BULK_EDIT_CONTROL_CONSTRAINT_END_LABEL, time, howToSet, wayToSet);
        }

        public void setActualStartDate(String time, boolean howToSet, boolean wayToSet) {
            setStartEndDate(BULK_EDIT_CONTROL_ACTUAL_START_LABEL, time, howToSet, wayToSet);
        }

        public void setActualEndDate(String time, boolean howToSet, boolean wayToSet) {
            setStartEndDate(BULK_EDIT_CONTROL_ACTUAL_END_LABEL, time, howToSet, wayToSet);
        }

        private void setStartEndDate(ILocatorable loc, String time, boolean howToSet, boolean wayToSet) {
            DatePicker dp = new DatePicker(getControl(loc));
            dp.useDatePopup(howToSet);
            dp.useDatePopup(wayToSet);
            dp.set(time);
            mouseDownAndUp(); //according changes in 9.0, for ie
        }

        public void setOwner(String owner) {
            Element control = getControl(BULK_EDIT_CONTROL_OWNER_LABEL);
            new UserChooser(control).set(owner);
            focus(control);
        }

        public void setRole(String role) {
            TagChooser tc = new TagChooser(getControl(BULK_EDIT_CONTROL_ROLE_LABEL));
            tc.setLabel(role);
        }

        public void setResourcePool(String pool) {
            TagChooser tc = new TagChooser(getControl(BULK_EDIT_CONTROL_RESOURCE_POOL_LABEL));
            tc.setLabel(pool);
        }

        public Input getPercentCompleteInput() {
            Input res = getControlInput(BULK_EDIT_CONTROL_PERCENT_COMPLETE_LABEL);
            res.setRegExp(BULK_EDIT_CONTROL_PERCENT_COMPLETE_REGEXP);
            res.setMax(BULK_EDIT_CONTROL_PERCENT_COMPLETE_MAX);
            res.setMin(BULK_EDIT_CONTROL_PERCENT_COMPLETE_MIN);
            return res;
        }

        public Input getAllocationInput() {
            Input res = getControlInput(BULK_EDIT_CONTROL_ALLOCATION_LABEL);
            res.setRegExp(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_REGEXP);
            res.setMax(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MAX);
            res.setMin(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MIN);
            return res;
        }

        public Input getEffortInput() {
            Input res = getControlInput(BULK_EDIT_CONTROL_EFFORT_LABEL);
            res.setRegExp(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_REGEXP);
            res.setMax(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MAX);
            res.setMin(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MIN);
            return res;
        }

        public Input getResourceDurationInput() {
            Input res = getControlInput(BULK_EDIT_CONTROL_RESOURCE_DURATION_LABEL);
            res.setRegExp(BULK_EDIT_CONTROL_RESOURCE_DURATION_REGEXP);
            res.setMax(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MAX);
            res.setMin(BULK_EDIT_CONTROL_EFFORT_ALLOCATION_DURATION_MIN);
            return res;
        }

        public void setEffort(float val) {
            Input input = getEffortInput();
            input.type(String.valueOf(val));
            Assert.assertFalse(input.isWrongInput(), "Incorrect input after setting effort " + val);
        }

        public void setAllocation(float val) {
            Input input = getAllocationInput();
            // todo. its hotfix.
            int _val = (int) val;
            input.type(String.valueOf(_val));
            Assert.assertFalse(input.isWrongInput(), "Incorrect input after setting allocation " + val);
        }

        public void setResourceDuration(int val) {
            Input input = getResourceDurationInput();
            input.type(String.valueOf(val));
            Assert.assertFalse(input.isWrongInput(), "Incorrect input after setting duration " + val);
        }


        /**
         * @param type: BULK_EDIT_CONTROL_CALCULATED_FIELD_EFFORT,
         *              BULK_EDIT_CONTROL_CALCULATED_FIELD_ALLOCATION,
         *              BULK_EDIT_CONTROL_CALCULATED_FIELD_RESOURCE_DURATION
         */
        public void selectCalculatedField(ILocatorable loc) {
            SelectInput select = new SelectInput(getControl(BULK_EDIT_CONTROL_CALCULATED_FIELD_LABEL));
            select.select(loc);
            JS_WAITER.waitTime();
        }

        /**
         * @param i 0,1,2 (effort, allocation, duration)
         */
        public void selectCalculatedField(int i) {
            if (i == 0)
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_EFFORT);
            if (i == 1)
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_ALLOCATION);
            if (i == 2)
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_RESOURCE_DURATION);
        }

        public void setEffortAllocationDuration(Float effort, Float allocation, Integer duration) {
            if (duration == null) {
                PSLogger.info("Set allocation " + allocation + ", effort " + effort);
                setAllocation(allocation);
                setEffort(effort);
                PSLogger.info("Select duration as 'Calculated field'");
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_RESOURCE_DURATION);
            }
            if (allocation == null) {
                PSLogger.info("Set duration " + duration + ", effort " + effort);
                setEffort(effort);
                setResourceDuration(duration);
                PSLogger.info("Select allocation as 'Calculated field'");
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_ALLOCATION);
            }
            if (effort == null) {
                PSLogger.info("Set duration " + duration + ", allocation " + allocation);
                setAllocation(allocation);
                setResourceDuration(duration);
                PSLogger.info("Select effort as 'Calculated field'");
                selectCalculatedField(BULK_EDIT_CONTROL_CALCULATED_FIELD_EFFORT);
            }
            PSLogger.save("after setting effort/allocation/duration");
        }

        public DisplayTextBox getDependencyInput() {
            return new DisplayTextBox(getControlInput(BULK_EDIT_CONTROL_DEPENDENCY_LABEL));
        }

        public WorkDependencyDialog getDependencyDialog() {
            return new WorkDependencyDialog(getDependencyInput());
        }

        public List<WorkDependency> getDependencyAsList() {
            String fromPage = getDependencyInput().getValue();
            List<WorkDependency> res = new ArrayList<WorkDependency>();
            for (String part : fromPage.split(BULK_EDIT_CONTROL_DEPENDENCY_SEPARATOR.getLocator())) {
                WorkDependency dep = WorkDependencyDialog.parse(part);
                if (dep != null)
                    res.add(dep);
            }
            return res;
        }

        public void setDependency(WorkDependency... toSet) {
            setDependency(Arrays.asList(toSet));
        }

        public void setDependency(List<WorkDependency> toSetList) {
            WorkDependencyDialog dep = getDependencyDialog();
            PSLogger.info("Set dependencies: " + toSetList);
            dep.setDependency(toSetList);
            PSLogger.save("After setting deps");
            // vvvvvv, its workaround for bug 75290:
            int num = 10;
            TimerWaiter step = new TimerWaiter(1000);
            int i = 0;
            List<WorkDependency> fromPage = null;
            for (; i < num; i++) {
                step.waitTime();
                fromPage = getDependencyAsList();
                if (toSetList.size() == fromPage.size())
                    break;
            }
            PSLogger.debug("From page : " + fromPage);
        }

        public void setTag(PSTag... tags) {
            PSTag parent = tags[0].getParent();
            if (parent == null) {
                PSLogger.warn("Can't find parent for tag " + tags[0].getFullName());
                return;
            }
            String name = parent.isInResources() ? BULK_EDIT_CONTROL_TAG_WORK_NAME.replace(parent.getName()) : parent.getName();
            Element div = getControl(name + ":");
            if (parent.isMultiple()) {
                TagChooser tc = new TagChooser(div);
                try {
                    tc.openPopup();
                    for (PSTag tag : tags) {
                        tc.select(tag.getName());
                    }
                } catch (AssertionError ae) {
                    if (getAppVersion().verGreaterOrEqual(_9_1) &&
                            getDriver().getType().isIE(6)) {
                        throw new PSKnownIssueException(74818, ae);
                    }
                    throw ae;
                }
                tc.done();
            } else {
                SingleStatusSelector tc = new SingleStatusSelector(div);
                //FlatTagChooser tc = new FlatTagChooser(div);
                tc.setLabel(tags[0].getName());
            }
        }

    }

    /**
     * Class for work menu
     */
    public class TreeWorkMenu extends Menu {
        private TreeWorkMenu() {
            super(TREE_ITEM_GENERAL_ARROW, true);
        }

        private TreeWorkMenu(Element e) {
            super(e, true);
        }

        public void waitForPresent() {
            try {
                super.waitForPresent();
            } catch (Wait.WaitTimedOutException e) {
                String jsError = getJsError();
                if (jsError != null) {
                    PSLogger.error(jsError);
                }
                throw e;
            }
        }

        public void open() {
            for (int i = 0; i < OPEN_ITERATIONS; i++) { // for stabilization
                try {
                    if (getDriver().getType().isIE()) JS_WAITER.waitTime();
                    PSLogger.debug("Open work tree iteration #" + i);
                    super._open();
                    PSLogger.save("After open");
                    return;
                } catch (AssertionError ae) {
                    PSLogger.warn(getClass().getSimpleName() + ".open: " + ae);
                    PSLogger.debug(getAttribute(TREE_ITEM_ARROW_TYPE_NAME));
                    setDefaultElement(getDocument());
                }
            }
            Assert.fail("Can't open tree menu");
        }

        /**
         * click on link Add Under in menu for specified work
         *
         * @return AddSubWorkDialog dialog
         */
        public AddSubWorkDialog addUnder() {
            try {
                return _addUnder();
            } catch (Wait.WaitTimedOutException we) {
                PSLogger.warn("addUnder: " + we); // try again for ie
                PSLogger.save("addUnder!");
                return _addUnder();
            }
        }

        private AddSubWorkDialog _addUnder() {
            call(MENU_POPUP_ADD_UNDER_LABEL);
            AddSubWorkDialog popup = new AddSubWorkDialog();
            popup.waitForVisible();
            return popup;
        }


        /**
         * click 'Add After'
         *
         * @return AddSubWorkDialog dialog
         */
        public AddSubWorkDialog addAfter() {
            call(MENU_POPUP_ADD_AFTER_LABEL);
            AddSubWorkDialog popup = new AddSubWorkDialog();
            popup.waitForVisible();
            return popup;
        }

        public void addSplit() {
            call(MENU_POPUP_ADD_SPLIT_LABEL);
            waitWhileLoading();
        }

        public boolean isAddSplitPresent() {
            return isItemPresent(MENU_POPUP_ADD_SPLIT_LABEL);
        }

        /**
         * push 'Delete' and wait popup
         */
        public DeleteDialog delete() {
            call(MENU_POPUP_DELETE_LABEL);
            DeleteDialog d = new DeleteDialog();
            d.waitForVisible();
            PSLogger.save("Delete popup");
            return d;
        }

        /**
         * push 'Information' item
         *
         * @return information dialog
         */
        public InformationDialog information() {
            call(MENU_POPUP_INFORMATION_LABEL);
            InformationDialog dialog = new InformationDialog();
            dialog.waitForVisible();
            return dialog;
        }

        public void indent() {
            call(MENU_POPUP_INDENT_LABEL);
            waitWhileLoading();
        }

        public void outdent() {
            call(MENU_POPUP_OUTDENT_LABEL);
            waitWhileLoading();
        }

        public WBSPage displayFromHere() {
            Element link = checkItem(MENU_POPUP_DISPLAY_FROM_HERE_LABEL.getLocator());
            PSLogger.info("Display from here: " + link.asXML());
            //call(MENU_POPUP_DISPLAY_FROM_HERE_LABEL);
            link.click(false);
            return waitNewWBSPage();
        }

        public WBSPage displayFromParent() {
            call(MENU_POPUP_DISPLAY_FROM_PARENT_LABEL);
            return waitNewWBSPage();
        }

        public SummaryWorkPage view() {
            SummaryWorkPage res = getSummaryInstance();
            call(MENU_POPUP_VIEW_LABEL);
            res.waitForPageToLoad();
            return res;
        }

        private WBSPage waitNewWBSPage() {
            waitForPageToLoad();
            getGeneralActions().waitWhileLoading(false);
            WBSPage page = new WBSPage();
            // todo: its for debug:
            try {
                new Element(TREE_ITEM_GENERAL_ARROW).waitForVisible();
                JS_WAITER.waitTime();
            } catch (Wait.WaitTimedOutException e) {
                checkJSError();
                checkForPopupError();
            }
            page.getDocument();
            return page;
        }

        private boolean isItemPresent(ILocatorable item) {
            boolean res = containsItem(item);
            close();
            return res;
        }

        @Override
        public void close() {
            if (SeleniumDriverFactory.getDriver().getType().isFF()) {
                PSLogger.info("Close tree item menu"); // hotfix for 9.3
                clickBody();
                popup.waitForUnvisible(CLOSE_TIMEOUT);
            } else {
                super.close();
            }
        }

        public boolean isIndentPresent() {
            return isItemPresent(MENU_POPUP_INDENT_LABEL);
        }

        public boolean isOutdentPresent() {
            return isItemPresent(MENU_POPUP_OUTDENT_LABEL);
        }

        public List<String> getMenuItems() {
            List<String> res = super.getMenuItems();
            close();
            return res;
        }
    }


    /**
     * class for delete dialog
     */
    public class DeleteDialog extends SimpleDialog {

        public DeleteDialog() {
            super(DELETE_DIALOG_POPUP_DELETE, DELETE_DIALOG_POPUP_CANCEL, WBSPage.this);
        }
    }

    /**
     * class for information Dialog
     */
    public class InformationDialog extends Dialog {

        private InformationDialog() {
            super(INFORMATION_DIALOG_POPUP);
            setPopup(INFORMATION_DIALOG_POPUP);
        }

        public void waitForVisible() {
            super.waitForVisible();
            getChildByXpath(INFORMATION_DIALOG_POPUP_TABLE).waitForVisible(); // first row
            new TimerWaiter(1500).waitTime();
            setDefaultElement(getDocument());
        }

        public Map<String, String> getInfo() {
            Map<String, String> info = new HashMap<String, String>();
            for (Element row : Element.searchElementsByXpath(this, INFORMATION_DIALOG_POPUP_TR)) {
                Element th = row.getChildByXpath(INFORMATION_DIALOG_POPUP_TH);
                Element td = row.getChildByXpath(INFORMATION_DIALOG_POPUP_TD);
                if (!th.isDEPresent() || !td.isDEPresent()) continue;
                info.put(th.getDEText().replace(":", "").trim(), td.getDEText());
            }
            return info;
        }

        public List<String> getWorkInfo() {
            List<String> res = new ArrayList<String>();
            Map<String, String> info = getInfo();
            for (String key : WBSEPageLocators.getInformationTable()) {
                if (info.get(key) != null)
                    res.add(info.get(key));
            }
            return res;
        }

        public void ok() {
            PSLogger.save("before OK in information");
            getChildByXpath(INFORMATION_DIALOG_POPUP_OK).click(false);
            waitForUnvisible();
        }
    }

    /**
     * class for Add New Sub Work popup (add under or add after)
     */
    public class AddSubWorkDialog extends Dialog {
        private final TimerWaiter TIMEOUT = new TimerWaiter(60000);

        public AddSubWorkDialog() {
            super(ADD_NEW_SUB);
            setPopup(ADD_NEW_SUB);
        }

        public void close() {
            PSLogger.info("Close Add new project dialog");
            super.close();
        }

        public void waitForVisible() {
            try {
                super.waitForVisible();
            } catch (Wait.WaitTimedOutException w) { // check for connection and errors.
                getDocument();
                setIsDocumentFresh();
                checkForErrorMessagesFromTop();
                checkJSError();
                getGrid().checkForPopupError();
                throw w;
            }
        }

        public void submit() {
            PSLogger.save("Saving before submit");
            new Button(ADD_NEW_SUB_WORK_SUBMIT).click(false);
            try {
                waitForUnvisible(TIMEOUT);
            } catch (Wait.WaitTimedOutException w) {
                setDefaultElement(getDocument());
                StringBuilder sb = new StringBuilder();
                for (Element e : Element.searchElementsByXpath(this, ADD_NER_SUB_WORK_ERROR)) {
                    sb.append(StrUtil.trim(e.getDEText())).append("\n");
                }
                if (sb.length() == 0) throw w;
                Assert.fail("There are errors : " + sb);
            }
            waitWhileLoading();
            setIsDocumentFresh();
        }

        public void cancel() {
            PSLogger.save("Before cancel");
            new Element(ADD_NEW_SUB_WORK_CANCEL).click(false);
            waitForUnvisible();
        }

        public void setPredecessor() {
            new Element(ADD_NEW_SUB_WORK_SET_PREDECESSOR).click(false);
            new TimerWaiter(500).waitTime();
            setDefaultElement(getDocument());
        }

        /**
         * add child projects to tree
         *
         * @param list ChildWork list's with settings
         */
        public void setChildWorks(Work... list) {
            for (int i = 1; i < list.length + 1; i++) {
                PSLogger.debug("Set child #" + i + ": " + list[i - 1]);
                setChildWork(list[i - 1], i);
            }
        }

        public void setChildTree(Work parent) {
            setChildWorks(parent.getAllChildren().toArray(new Work[]{}));
        }

        public void setType(String type, int row) {
            ComboBox c = getTypeInput(row);
            c.select(type);
        }

        public String getNumber(int row) {
            return getElement(ADD_NEW_SUB_WORK_NUMBER, row).getDEText();
        }

        private Input getNameInput(int row) {
            return new Input(getElement(ADD_NEW_SUB_WORK_ITEM_NAME, row));
        }

        public void setName(String name, int row) {
            Input in = getNameInput(row);
            in.focus();
            in.type(name);
        }

        public void setConstraint(Work w, int row) {
            PSLogger.info("Set constraint " + w.getConstraint() + " for work " + w);
            ComboBox c = new ComboBox(getElement(ADD_NEW_SUB_WORK_CONSTRAINT, row));
            if (w.getConstraint().getName().equals(c.getValue())) {
                PSLogger.debug("Do not set constraint " + w.getConstraint() + " for " + w.getName() + ": already seted");
                return;
            }
            Assert.assertEquals(c.isDisabled(), w.isConstraintsDisabled(), "Constraint combobox should be " + (w.isConstraintsDisabled() ? "disabled" : "enabled"));
            if (getAppVersion().verGreaterOrEqual(_9_3) && w.isConstraintsDisabled()) {
                PSLogger.warn("Cannot set constraint type for " + w.getName() + ": functionality disabled");
                return;
            }
            c.select(w.getConstraint().getValue());
        }

        public void setConstraint(String constraint, int row) {
            ComboBox c = new ComboBox(getElement(ADD_NEW_SUB_WORK_CONSTRAINT, row));
            c.select(constraint);
        }

        public void setStartDate(String start, int row) {
            DatePicker dp = new DatePicker(getElement(ADD_NEW_SUB_WORK_START_DATE_NAME, row));
            dp.waitForVisible();
            dp.useDatePopup(false);
            dp.set(start);
        }

        public void setEndDate(String end, int row) {
            DatePicker dp = new DatePicker(getElement(ADD_NEW_SUB_WORK_END_DATE_NAME, row));
            dp.waitForVisible();
            dp.useDatePopup(false);
            dp.set(end);
        }

        public void setOutdent(int outdent, int row) {
            // set outdent
            PSLogger.info("Set " + Math.abs(outdent) + " outdent for row #" + row);
            Element img = getElement(ADD_NEW_SUB_WORK_OUTDENT_ARROW, row);
            img.setSimpleLocator();
            if (img.getAttribute("src").contains(ADD_NEW_SUB_WORK_OUTDENT_ARROW_SRC_ATTR_OFF.getLocator())) {
                PSLogger.warn("Outdent arrow is disabled");
            } else {
                for (int i = 0; i < Math.abs(outdent); i++) {
                    img.mouseDownAndUp();
                }
            }
        }

        public void setIndent(int indent, int row) {
            // set indent
            PSLogger.info("Set " + indent + " indent for row #" + row);
            Element img = getElement(ADD_NEW_SUB_WORK_INDENT_ARROW, row);
            img.setSimpleLocator();
            if (img.getAttribute("src").contains(ADD_NEW_SUB_WORK_INDENT_ARROW_SRC_ATTR_OFF.getLocator())) {
                PSLogger.warn("Indent arrow is disabled");
            } else {
                for (int i = 0; i < indent; i++) {
                    img.mouseDownAndUp();
                }
            }
        }

        public void setDAE(String name, WBSEPageLocators loc, String toSet, int row) {
            if (toSet == null) return;
            if (!isResourcePlanning()) {
                PSLogger.warn("Can't find allocation in header.");
                return;
            }
            PSLogger.info("Set " + name + " " + toSet + " for row #" + row);
            Element cell = getElement(loc, row);
            Element acronym = cell.getChildByXpath(ADD_NEW_SUB_WORK_DAE_ACRONYM);
            Input input = new Input(cell.getChildByXpath(ADD_NEW_SUB_WORK_DAE_INPUT));
            if (!getAppVersion().inRange(_12, _12_1) && acronym.exists() && acronym.isVisible()) {
                acronym.click(false);
            }
            input.type(toSet);
            input.focus();
            click(false);
        }

        public void setDuration(String toSet, int row) {
            setDAE("Duration", ADD_NEW_SUB_WORK_DURATION, toSet, row);
        }

        public void setAllocation(String toSet, int row) {
            setDAE("Allocation", ADD_NEW_SUB_WORK_ALLOCATION, toSet, row);
        }

        public void setEffort(String toSet, int row) {
            setDAE("Effort", ADD_NEW_SUB_WORK_EFFORT, toSet, row);
        }


        public void setChildWork(WorkType ch, int num) {
            PSLogger.info("Set work type " + ch);
            setType(ch.getName(), num);
            afterSetLine(num);
            Work.IConstraint constraint = Work.Constraint.ASAP;
            boolean disabled = false;
            //validate constraint:
            Work tmp = null;
            if (ch instanceof Template) {
                tmp = ((Template) ch).getStructure();
            } else if (ch instanceof Work.Type) {
                tmp = ((Work.Type) ch).toWork();
            }
            constraint = tmp.getDummyConstraint(); // TODO: #81747
            disabled = tmp.isConstraintsDisabled();

            ComboBox c = new ComboBox(getElement(ADD_NEW_SUB_WORK_CONSTRAINT, num));

            Assert.assertEquals(c.isDEVisible() ? c.getValue() : GatedProject.EMPTY_CONSTRAINT.getName(), constraint.getName(), "Incorrect constraint for type " + ch);
            if (c.isDEVisible())
                Assert.assertEquals(c.isDisabled(), disabled, "Constraint for type " + ch + " should be " + (disabled ? "disabled" : "enabled"));
        }

        /**
         * set child work in add new subwork popup
         *
         * @param ch  ChildWork Object
         * @param num number of row
         */
        public void setChildWork(Work ch, int num) {
            if (ch.getType() != null)
                setType(ch.getType(), num);

            if (ch.getName() != null)
                setName(ch.getName(), num);

            if (ch.getConstraint() != null) {
                setConstraint(ch, num);
            }

            if (ch.getConstraintStartDate() != null) {
                setStartDate(ch.getConstraintStartDate(), num);
            }
            if (ch.getConstraintEndDate() != null) {
                setEndDate(ch.getConstraintEndDate(), num);
            }

            if (num != 0) {
                int indent = ch.getIndent();
                if (indent < 0) {
                    setOutdent(indent, num);
                }
                if (indent > 0) {
                    setIndent(indent, num);
                }
            }
            if (getAppVersion().inRange(_12, _12_1) && ch.hasDAE()) {
                if (ch.getAllocation() == null) {
                    makeAllocationCalculated(num);
                } else if (ch.getResourceDuration() == null) {
                    makeDurationCalculated(num);
                } else {
                    makeEffortCalculated(num);
                }
            }
            setEffort(ch.getEffort() == null ? null : String.valueOf(ch.getEffort()), num);
            setDuration(ch.getResourceDuration() == null ? null : String.valueOf(ch.getResourceDuration()), num);
            setAllocation(ch.getAllocation() == null ? null : String.valueOf(ch.getAllocation()), num);
            ch.setIndex(getElement(ADD_NEW_SUB_WORK_NUMBER, num).getText());
            afterSetLine(num);
        }

        private void afterSetLine(int num) {
            getNameInput(num).focus();
            JS_WAITER.waitTime(); // wait because sometimes can't find index cell in ie?
            setDefaultElement(getDocument());
        }

        private void makeDAECalculated(ILocatorable header, ILocatorable name, Integer num) {
            PSLogger.info("Make " + name.getLocator() + " calculated" + (num == null ? "" : (" for row#" + num)));
            if (getAppVersion().inRange(_12, _12_1)) {
                Element res = getElement(ADD_NEW_SUB_WORK_CALCULATED_FIELD_V12, num == null ? 1 : num);
                Assert.assertNotNull(res, "Can't find calculated field cell");
                ComboBox comboBox = new ComboBox(res);
                comboBox.select(name);
                comboBox.focus();
                click(false);
                return;
            }
            Element acronym = getChildByXpath(header);
            if (!acronym.isDEPresent() || !acronym.isVisible()) {
                PSLogger.warn("Can't find acronym for " + name.getLocator() + " in header");
            } else {
                acronym.click(false);
                setDefaultElement(getDocument());
            }
            PSLogger.save();
        }

        public boolean isResourcePlanning() {
            if (getAppVersion().inRange(_12, _12_1)) {
                Element calcField = getChildByXpath(ADD_NEW_SUB_WORK_TOP_CALCULATED_FIELD_V12);
                return calcField.isDEPresent() && calcField.isVisible();
            }
            Element acronym = getChildByXpath(ADD_NEW_SUB_WORK_TOP_ALLOCATION_ACRONYM_V11);
            //new Element(ADD_NEW_SUB_WORK_TOP_ALLOCATION_ACRONYM_V11);
            //return acronym.exists() && acronym.isVisible();
            return acronym.isDEPresent() && acronym.isVisible();
        }

        public void makeAllocationCalculated(Integer num) {
            makeDAECalculated(ADD_NEW_SUB_WORK_TOP_ALLOCATION_ACRONYM_V11, ADD_NEW_SUB_WORK_HEADER_DAE_ALLOCATION, num);
        }

        public void makeEffortCalculated(Integer num) {
            makeDAECalculated(ADD_NEW_SUB_WORK_TOP_EFFORT_ACRONYM_V11, ADD_NEW_SUB_WORK_HEADER_DAE_EFFORT, num);
        }

        public void makeDurationCalculated(Integer num) {
            makeDAECalculated(ADD_NEW_SUB_WORK_TOP_DURATION_ACRONYM_V11, ADD_NEW_SUB_WORK_HEADER_DAE_DURATION, num);
        }

        public void makeDurationCalculated() {
            makeDurationCalculated(null);
        }

        /**
         * make expected tree from popup before submiting
         *
         * @param list - list of children from popuop
         * @return - tree in String
         */
        public String makeStringTree(final Work... list) {
            String title = getTitle();
            title = title.replace(EXPECTED_TITLE_POPUP_AFTER_PREFIX.getLocator(), "").
                    replace(EXPECTED_TITLE_POPUP_UNDER_PREFIX.getLocator(), "");
            return Work.TREE_NEW_LINE + title + getStringTree(list);
        }

        public String makeStringTreeForParent(Work parent) {
            return makeStringTree(parent.getAllChildren().toArray(new Work[]{}));
        }

        /**
         * load tree from add new subwork popup
         *
         * @param list array of ChildWorks
         * @return tree, String
         */
        private String getStringTree(final Work... list) {
            setDefaultElement(getDocument());
            StringBuffer res = new StringBuffer();
            if (list.length == 0) return "";
            Map<String, Integer> nameAndX = new LinkedHashMap<String, Integer>();
            for (int i = 1; i < list.length + 1; i++) {
                Element in = getElement(ADD_NEW_SUB_WORK_ITEM_NAME, i);
                Integer[] coords = in.getCoordinates(); //xywh
                nameAndX.put(list[i - 1].getName(), coords[0]);
            }
            SortedSet<Integer> set = new TreeSet<Integer>();
            set.addAll(nameAndX.values());
            for (String name : nameAndX.keySet()) {
                res.append(Work.TREE_NEW_LINE);
                res.append(indexOfAndGetTabs(set, nameAndX.get(name)));
                res.append(name);
            }
            return res.toString();
        }

        private Element getElement(WBSEPageLocators loc, int row) {
            getDefaultElement();
            return Element.searchElementByXpath(this, loc.replace(row));
        }

        private ComboBox getTypeInput(int row) {
            Element res = getElement(ADD_NEW_SUB_WORK_TYPE, row);
            if (res == null) return null;
            return new ComboBox(res);
        }

        /**
         * @param positionIndexFrom - row table index first
         * @param positionIndexTo   - row table index second
         */
        public void dragAndDropRow(int positionIndexFrom, int positionIndexTo) {
            PSLogger.debug("move row " + positionIndexFrom + " to " + positionIndexTo + " position");
            Element row1 = getElement(ADD_NEW_SUB_WORK_NUMBER, positionIndexFrom);
            Element row2 = getElement(ADD_NEW_SUB_WORK_NUMBER, positionIndexTo);
            row1.dragAndDrop(row2);
        }

        private String indexOfAndGetTabs(SortedSet<Integer> set, int X) {
            int index = 0;
            for (int x : set) {
                if (x == X) {
                    StringBuffer res = new StringBuffer();
                    for (int i = 0; i < Math.pow(2, index); i++) {
                        res.append(Work.TREE_TAB);
                    }
                    return res.toString();
                }
                index++;
            }
            return null;
        }

        public WorkType[] getAllWorkTypes() {
            ComboBox typesPopup = getTypeInput(1); // first cell
            typesPopup.open();
            List<String> options = typesPopup.getOptions();
            PSLogger.info(options);
            WorkType[] childs = new WorkType[options.size()];
            int i = 0;
            for (String name : options) {
                childs[i++] = Work.toWorkType(name);
            }
            typesPopup.close();
            return childs;
        }

        public List<String> getValuesFromTypesColumn() {
            List<String> res = new ArrayList<String>();
            for (int i = 1; ; i++) {
                ComboBox cb = getTypeInput(i);
                if (cb == null) break;
                if (!cb.getInput().exists()) break;
                res.add(cb.getValue());
            }
            return res;
        }

        public List<String> getPredecessors() {
            List<String> res = new ArrayList<String>();
            for (int i = 1; ; i++) {
                Element in = getElement(ADD_NEW_SUB_WORK_PREDECESSOR, i);
                if (in == null || !in.isDEPresent()) break;
                String value = in.getValue();
                res.add(value);
            }
            return res;
        }

        public List<Integer> getDuration(Work... list) {
            List<Integer> res = new ArrayList<Integer>();
            for (Float o : getDAE(ADD_NEW_SUB_WORK_DURATION, list)) {
                res.add(o.intValue());
            }
            return res;
        }

        public List<Float> getAllocation(Work... list) {
            return getDAE(ADD_NEW_SUB_WORK_ALLOCATION, list);
        }

        public List<Float> getEffort(Work... list) {
            return getDAE(ADD_NEW_SUB_WORK_EFFORT, list);
        }

        private List<Float> getDAE(WBSEPageLocators loc, Work... list) {
            List<Float> res = new ArrayList<Float>();
            if (!isResourcePlanning()) {
                PSLogger.warn("Can't find allocation");
                return res;
            }
            for (int i = 1; i <= list.length; i++) {
                Element cell = getElement(loc, i);
                Element acronym = cell.getChildByXpath(ADD_NEW_SUB_WORK_DAE_ACRONYM);
                Input in = new Input(cell.getChildByXpath(ADD_NEW_SUB_WORK_DAE_INPUT));
                String txt;
                if (acronym.isDEPresent() && acronym.isVisible()) {
                    txt = acronym.getDEText();
                } else {
                    txt = in.getValue();
                }
                res.add(Float.parseFloat(txt));
            }
            return res;
        }

        /**
         * get Popup header with skipping empty
         *
         * @return list
         */
        public List<String> getTableHead() {
            List<String> res = new ArrayList<String>();
            setDefaultElement(getDocument());
            for (Element o : Element.searchElementsByXpath(this, ADD_NEW_SUB_WORK_HEADER)) {
                //DefaultElement e = o.getDefaultElement();
                if (!o.isVisible()) {
                    continue;
                }
                Element c = o.getDEChild();
                if (c != null && !c.isVisible()) {
                    continue;
                }
                String txt;
                Element ch;
                if ((ch = o.getChildByXpath(ADD_NEW_SUB_WORK_TOP_ACRONYM)).isDEPresent()) {
                    txt = ch.getDEText();
                } else {
                    txt = o.getDEText();
                }
                if (!txt.isEmpty()) {
                    res.add(txt);
                }
            }
            return res;
        }
    }

    /**
     * class for Gate Project dialog-popup (dates and statuses cells)
     */
    public class GateProjectDialog extends Dialog {
        private Frame frame;

        public GateProjectDialog() {
            super(GATE_PROJECT_POPUP);
            setPopup(GATE_PROJECT_POPUP);
        }

        public Frame getIFrameElement() {
            if (frame == null) {
                frame = WBSPage.super.getFrame(GATE_PROJECT_POPUP_FRAME);
                frame.setDefaultElement();
            } else {
                frame.select();
            }
            return frame;
        }

        public void submit() {
            PSLogger.save("Saving before submit");
            Frame frame = getIFrameElement();
            frame.getChildByXpath(GATE_PROJECT_POPUP_SUBMIT).click(false);
            frame.waitForLoad();
            String mes = getErrorMessage();
            if (mes != null) {
                PSLogger.warn(mes);
                return;
            }
            selectTopFrame();
            if (!getDriver().getType().isIE() || getDriver().getType().isIE(6))
                waitForPageToLoad();
            else
                JS_WAITER.waitTime();
            waitWhileLoading();
        }


        public String getErrorMessage() {
            Element error = getIFrameElement().getChildByXpath(BasicCommonsLocators.ERROR_BOX);
            if (!error.exists()) return null;
            if (!error.isVisible()) return null;
            return error.getText();
        }

        public void cancel() {
            getIFrameElement().getChildByXpath(GATE_PROJECT_POPUP_CANCEL).click(false);
            selectTopFrame();
            waitForUnvisible();
            getDocument();
        }

        public void close() {
            selectTopFrame();
            close(false);
            waitForUnvisible();
            getDocument();
        }

        private List<Element> getRows() {
            return Element.searchElementsByXpath(getIFrameElement(), GATE_PROJECT_POPUP_TABLE_ROW);
        }

        private Element getRow(String name) {
            for (Element row : getRows()) {
                Element link = row.getChildByXpath(GATE_PROJECT_POPUP_TABLE_ROW_LINK);
                if (link.getDEText().equals(name)) return row;
            }
            PSLogger.warn("Can't find row for '" + name + "'");
            return null;
        }

        public DatePicker getEndDatePicker(String name) {
            Element td = getRow(name).getChildByXpath(GATE_PROJECT_POPUP_TABLE_ROW_END_DATE);
            if (td == null) return null;
            return new DatePicker(td);
        }

        public void setEndDatePicker(String name, String date) {
            DatePicker dp = getEndDatePicker(name);
            dp.useDatePopup(false);
            dp.set(date);
        }

        public DatePicker getStartDatePicker(String name) {
            Element td = getRow(name).getChildByXpath(GATE_PROJECT_POPUP_TABLE_ROW_START_DATE);
            if (td == null) return null;
            return new DatePicker(td);
        }

        public SelectInput getStatusSelect(String name) {
            Element td = getRow(name).getChildByXpath(GATE_PROJECT_POPUP_TABLE_ROW_STATUS);
            if (td == null || !td.exists()) return null;
            return new SelectInput(td);
        }

        public void cancelGate(String name) {
            PSLogger.info("Cancel gate " + name);
            setStatus(name, GATE_PROJECT_POPUP_TABLE_ROW_STATUS_CANCELED.getLocator());
        }

        public void setStatus(String name, String status) {
            PSLogger.debug("Set status " + status + " for gate " + name);
            getStatusSelect(name).select(status);
        }

        public void setProjectStartDate(String time) {
            DatePicker dp = getProjectStartDatePicker();
            dp.useDatePopup(false);
            dp.set(time);
        }

        public DatePicker getProjectStartDatePicker() {
            return new DatePicker(getIFrameElement().getChildByXpath(GATE_PROJECT_POPUP_PROJECT_START_DATE));
        }

        public String getProjectEndDate() {
            return getIFrameElement().getChildByXpath(GATE_PROJECT_POPUP_PROJECT_END_DATE).getText();
        }

        public String getTitle() {
            selectParentFrame();
            String res = new Element(GATE_PROJECT_POPUP_TITLE).getText();
            getIFrameElement().select();
            return res;
        }

        public boolean isVisible() {
            selectParentFrame();
            boolean res = super.exists() && super.isVisible();
            if (res) {
                getIFrameElement().select();
            }
            return res;
        }

    }

    public class AllocateResourceDialog extends TabsDialog {
        private SearchTab searchTab;
        private ByQualificationsTab byQualificationsTab;
        private boolean doConfirm;

        public AllocateResourceDialog(boolean confirm) {
            super();
            doConfirm = confirm;
            setPopup(ALLOCATE_RESOURCES_POPUP);
        }

        private void waitForPopup() {
            getPopup().waitForVisible();
            JS_WAITER.waitTime();
        }

        public void openSearchTab() {
            openTab(ALLOCATE_RESOURCES_POPUP_TAB_SEARCH);
            searchTab = new ResourceAllocationSearchTab(ALLOCATE_RESOURCES_POPUP_TAB_SEARCH_INPUT, ALLOCATE_RESOURCES_POPUP_TAB_SEARCH_SUBMIT);
        }

        private class ResourceAllocationSearchTab extends SearchTab {

            public ResourceAllocationSearchTab(ILocatorable input, ILocatorable submit) {
                super(getPopup(), input, submit, ALLOCATE_RESOURCES_POPUP_TAB_LOADING, ALLOCATE_RESOURCES_POPUP_TAB_ERROR);
            }

            @Override
            public Link getLink(String name) {
                //a[contains(text(),'" + LOCATOR_REPLACE_PATTERN + "')]/parent::td/parent::tr//img
                for (Element e : Element.searchElementsByXpath(this, ALLOCATE_RESOURCES_POPUP_TAB_LINK)) {
                    if (!name.equals(e.getDEText())) continue;
                    Element parent = e.getChildByXpath(ALLOCATE_RESOURCES_POPUP_TAB_LINK_PARENT);
                    if (!parent.isDEPresent()) continue;
                    Element img = parent.getChildByXpath(ALLOCATE_RESOURCES_POPUP_TAB_LINK_IMG);
                    if (!img.isDEPresent()) continue;
                    if (img.exists())
                        return new Link(img);
                }
                PSLogger.warn("Can't find link '" + name + "'");
                PSLogger.debug(asXML());
                return null;
            }
        }

        public SearchTab getSearchTab() {
            return searchTab;
        }

        public ByQualificationsTab getByQualificationsTab() {
            return byQualificationsTab;
        }


        public void openByQualificationsTab() {
            openTab(ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS);
            byQualificationsTab = new ByQualificationsTab(ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SUBMIT);
        }

        public void openBestMatchesTab() {
            openTab(ALLOCATE_RESOURCES_POPUP_TAB_BEST_MATCHES);
        }

        public void doSearch(String user) {
            PSLogger.info("Search and set user " + user + " using resource allocation dialog, search tab");
            openSearchTab();
            int num = 5;
            for (int i = 0; i < num; i++) {
                try {
                    getSearchTab().choose(user);
                    waitWhileLoading();
                    return;
                } catch (AssertionError a) {
                    String msg = a.getMessage() == null ? "" : a.getMessage();
                    if (getAppVersion().verLessThan(_9_1)) throw a;
                    if (msg.contains(SimpleGrid.PROBLEM_MESSAGE_PREFIX)) throw a;
                    if (!msg.contains(ALLOCATE_RESOURCES_POPUP_NOT_FOUND_MSG.getLocator())) throw a;
                    PSLogger.warn(msg);
                    //PSLogger.knis(75724); // try again.
                    getSearchTab().doSearch();
                    getSearchTab().clickLink(user);
                }
            }
            throw new PSKnownIssueException(75724);
        }

        public void doSearch(User user) {
            doSearch(user.getFormatFullName());
        }

        /**
         * @param roleType : 0,1,2,3 (Empty, No Values, Any Values, Selected Values)
         * @param role     : role to Search
         * @param poolType : type 0,1,2,3 (Empty, No Values, Any Values, Selected Values)
         * @param pool     : pool to Search
         * @param user     : user to click
         */
        public void doSearchByQualifications(int roleType, String role, int poolType, String pool, String user) {
            PSLogger.info("Search and set user '" + user + "' using resource allocation dialog, qualifications tab " + (pool == null ? "" : ", pool is " + pool) + (role == null ? "" : ", role is " + role));
            openByQualificationsTab();
            getByQualificationsTab().selectRole(roleType, role);
            getByQualificationsTab().selectPool(poolType, pool);
            PSLogger.save("Before submit searching");
            getByQualificationsTab().submit();
            getByQualificationsTab().clickLink(user);
            waitWhileLoading();
        }

        public void waitWhileLoading() {
            if (getAppVersion().verGreaterOrEqual(_9_3)) {
                SimpleDialog confirmation = new SimpleDialog(ALLOCATE_RESOURCES_CONFIRMATION_YES, ALLOCATE_RESOURCES_CONFIRMATION_NO, WBSPage.this);
                confirmation.waitForVisible();
                if (doConfirm) {
                    confirmation.yes();
                } else {
                    confirmation.no();
                }
            }
            WBSPage.this.waitWhileLoading();
        }

        /**
         * search by qualifications with 'No Values' for role and pool
         *
         * @param user
         */
        public void doSearchByQualifications(String user) {
            doSearchByQualifications(1, null, 1, null, user);
        }

        private class ByQualificationsTab extends ResourceAllocationSearchTab {
            public ByQualificationsTab(ILocatorable submit) {
                super(null, submit);
            }

            /**
             * @param type 0,1,2,3 (Empty, No Values, Any Values, Selected Values)
             * @param role - String role name;
             */
            public void selectRole(int type, String role) {
                selectRoleOrPool(type,
                        role,
                        ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_ROLES,
                        ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_ROLES_SELECT_VALUE);
            }

            /**
             * @param type 0,1,2,3 (Empty, No Values, Any Values, Selected Values)
             * @param pool - String pool name;
             */
            public void selectPool(int type, String pool) {
                selectRoleOrPool(type,
                        pool,
                        ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_POOLS,
                        ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_POOLS_SELECT_VALUE);
            }


            private void selectRoleOrPool(int type, String value, ILocatorable firstSelect, ILocatorable secondSelect) {
                openByQualificationsTab();
                ILocatorable loc = getOptionValueForSelects(type);
                SelectInput select1 = new SelectInput(firstSelect);
                select1.select(loc);
                if (!loc.equals(OPTIONS_BLOCK_FILTERS_SELECT_SPECIFIED_VALUE)) return;

                SelectInput select2 = new SelectInput(secondSelect);
                List<String> options = select2.getSelectOptions();
                PSLogger.debug(options);
                if (options.contains(value))
                    select2.select(value);
                else {
                    PSLogger.warn("Can't find role or pool '" + value + "' in options");
                    PSLogger.save();
                }
            }

            /**
             * @param type
             * @return
             */
            private ILocatorable getOptionValueForSelects(int type) {
                switch (type) {
                    case 0:
                        return ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_EMPTY_VALUE;
                    case 1:
                        return ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_NO_VALUE;
                    case 2:
                        return ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_ANY_VALUE;
                    case 3:
                        return ALLOCATE_RESOURCES_POPUP_TAB_BY_QUALIFICATIONS_SELECT_SPECIFIED_VALUE;
                    default:
                        PSLogger.warn("Incorrect type specified for selector in allocate resource dialog");
                        return null;
                }
            }
        }
    }


    public class VariableAllocation extends AbstractPeriodGrid {
        public VariableAllocation() {
            super(GRID_GANTT_SECTION, WBSPage.this);
        }

        @Override
        public Element getDynamicElement() {
            return new Element(VARIABLE_ALLOCATION_SECTION);
        }

        public Element getForthCell() {
            return getDynamicElement().getChildByXpath(VARIABLE_ALLOCATION_SECTION_4_CELL);
        }

        protected Column[] initColumns(PSCalendar week) {
            Column column = initColumnElement(week, VARIABLE_ALLOCATION_COLUMN);
            if (column == null) return null;
            return new Column[]{column};
        }
    }

    public class Histogram extends AbstractGanttSection {
        private Element master;
        private List<Column> columns = new ArrayList<Column>();
        private List<Legend> legends = new ArrayList<Legend>();
        private List<WeekPeriod> weeks = new ArrayList<WeekPeriod>();
        private boolean isIE = getDriver().getType().isIE();

        protected Histogram() {
            super(HISTOGRAM_BODY, WBSPage.this);
            master = new Element(HISTOGRAM_MASTER);
        }

        @Override
        public Element getDynamicElement() {
            return new Element(isIE ? HISTOGRAM_DYNAMIC_IE : HISTOGRAM_DYNAMIC_FF);
        }

        public void waitForVisible() {
            super.waitForVisible();
            HISTOGRAM_WAITER.waitTime();
            load();
        }

        private void load() {
            master.setDefaultElement(getDocument());
            legends.clear();
            for (Element e : Element.searchElementsByXpath(master, HISTOGRAM_LEGEND)) {
                legends.add(new Legend(e));
            }
            Integer[] header = getHeader().getCoordinates();
            weeks.clear();
            List<MonthPeriod> months = new ArrayList<MonthPeriod>();
            for (Element e : getElements(false, GRID_GANTT_HEADER_TOP_CELL_MONTH)) {
                months.add(new MonthPeriod(e));
            }
            for (Element e : getElements(false, GRID_GANTT_HEADER_BOTTOM_CELL_WEEK)) {
                weeks.add(new WeekPeriod(e, header[0], months));
            }
            loadColumns(document);
        }

        private void loadColumns(Document doc) {
            setDefaultElement(doc);
            List<Label> labels = new ArrayList<Label>();
            for (Element e : Element.searchElementsByXpath(this, HISTOGRAM_LABEL)) {
                labels.add(new Label(e));
            }
            columns.clear();
            for (Element e : Element.searchElementsByXpath(this, isIE ? HISTOGRAM_COLUMN_IE : HISTOGRAM_COLUMN_FF)) {
                Column c = new Column(e);
                columns.add(c);
                c.setLabels(labels);
                c.setPeriod(weeks);
            }
        }

        public void scrollTo(PSCalendar period) {
            CellElement week = null;
            for (int i = 0; i < weeks.size() - 2; i++) {
                PSCalendar s = weeks.get(i).week;
                PSCalendar e = weeks.get(i + 1).week;
                if (s.lessOrEqual(period) && period.lessOrEqual(e)) {
                    PSLogger.debug("scroll to week " + week);
                    week = new CellElement(weeks.get(i));
                    break;
                }
            }
            if (week == null) {
                PSLogger.warn("Can't find period " + period + " in gantt");
                return;
            }
            scrollTo(week);
            HISTOGRAM_WAITER.waitTime();
            loadColumns(getDocument());
        }

        public List<Column> getColumnsForRole(Role role) {
            Color color = null;
            for (Legend l : legends) {
                if (l.name.equals(role.getName())) {
                    color = l.color;
                    break;
                }
            }
            if (color == null) {
                PSLogger.warn("Can't find role " + role + " in histogram");
                return null;
            }
            List<Column> res = new ArrayList<Column>();
            for (Column c : columns) {
                if (c.color.equals(color)) {
                    res.add(c);
                }
            }
            return res;
        }

        public List<Integer> getAvailabilities(Role role, PSCalendar week1, PSCalendar week2) {
            scrollTo(week1);
            Map<PSCalendar, Integer> list = new LinkedHashMap<PSCalendar, Integer>();
            for (WeekPeriod w : weeks) {
                if (week1.lessOrEqual(w.week) && w.week.lessOrEqual(week2)) {
                    list.put(w.week, 0);
                }
            }
            PSLogger.debug("list of periods for histogram: " + list.keySet());
            List<Column> columns = getColumnsForRole(role);
            if (columns == null) return null;
            List<Integer> res = new ArrayList<Integer>();
            for (Column c : columns) {
                if (list.containsKey(c.period))
                    list.put(c.period, c.getAvailability());
            }
            res.addAll(list.values());
            return res;
        }

        private class Label extends Element {
            private Integer val;
            private int y;

            public Label(Element e) {
                super(e);
                String txt = getDEText();
                PSLogger.debug("label: " + txt);
                if (txt.matches(".*\\d+.*"))
                    val = Integer.parseInt(txt.replaceAll("[^\\d]+", ""));
                else
                    val = 0;
                Integer[] coords = getCoordinates();
                this.y = coords[1];
            }
        }

        private class MonthPeriod extends Element {
            private int x;
            private int w;
            private String month;

            private MonthPeriod(Element e) {
                super(e);
                month = getDEText();
                PSLogger.debug("month period: " + month);
                Integer[] coords = getCoordinates();
                x = coords[0];
                w = coords[2];
            }
        }

        private class WeekPeriod extends Element {
            private int x;
            private int w;
            private PSCalendar week;

            private WeekPeriod(Element e, int X, List<MonthPeriod> months) {
                super(e);
                parse(X, months);
            }

            private void parse(int X, List<MonthPeriod> months) {
                String sweek = getDEText();
                PSLogger.debug("week period: " + sweek);
                Integer[] coords = getCoordinates();
                this.x = coords[0];
                this.w = coords[2];
                Date date = null;
                String separator = ";";
                String format = GRID_GANTT_HEADER_TOP_CELL_MONTH_FORMAT.getLocator() + separator + GRID_GANTT_HEADER_BOTTOM_CELL_WEEK_FORMAT.getLocator();
                for (MonthPeriod m : months) {
                    if (m.x <= x && m.x + m.w >= x) {
                        try {
                            date = new SimpleDateFormat(format, Locale.ENGLISH).parse(m.month + separator + sweek);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
                this.x -= X;
                if (date != null) {
                    this.week = PSCalendar.getEmptyCalendar().set(date);
                }

            }
        }

        public class Column extends Element {
            private Color color;
            private String tooltip;
            private float x;
            private float y;
            private float w;
            private float h;
            private Integer label_1;
            private Integer label_2;
            private PSCalendar period;

            private Column(Element e) {
                super(e);
                parse();
            }

            private void parse() {
                String sc;
                String sx = null;
                String sw = null;
                String sy = null;
                String sh = null;
                String xml = asXML();
                PSLogger.debug(xml);
                if (isIE) {
                    sc = getDEAttribute(HISTOGRAM_COLUMN_COLOR_IE);
                    String style = getDEStyle();
                    for (String s : style.split(";\\s*")) {
                        String d = s.replaceAll("[^\\d]+", "");
                        if (s.startsWith(HISTOGRAM_COLUMN_X_IE.getLocator())) {
                            sx = d;
                        }
                        if (s.startsWith(HISTOGRAM_COLUMN_Y_IE.getLocator())) {
                            sy = d;
                        }
                        if (s.startsWith(HISTOGRAM_COLUMN_W_IE.getLocator())) {
                            sw = d;
                        }
                        if (s.startsWith(HISTOGRAM_COLUMN_H_IE.getLocator())) {
                            sh = d;
                        }
                    }
                    sc = getDEAttribute(HISTOGRAM_COLUMN_COLOR_IE);
                    if (sc == null || sc.isEmpty()) { // its for ie8:
                        sc = xml.replaceAll(HISTOGRAM_COLUMN_COLOR_IE_8_a.getLocator(),
                                HISTOGRAM_COLUMN_COLOR_IE_8_b.getLocator());
                    }
                } else {
                    sx = getDEAttribute(HISTOGRAM_COLUMN_X_FF);
                    sw = getDEAttribute(HISTOGRAM_COLUMN_W_FF);
                    sy = getDEAttribute(HISTOGRAM_COLUMN_Y_FF);
                    sh = getDEAttribute(HISTOGRAM_COLUMN_H_FF);
                    sc = getDEAttribute(HISTOGRAM_COLUMN_COLOR_FF);
                }
                PSLogger.debug("histogram column : " + sx + "," + sy + "," + sw + "," + sh + "; " + sc);
                x = Float.parseFloat(sx);
                w = Float.parseFloat(sw);
                y = Float.parseFloat(sy);
                h = Float.parseFloat(sh);
                color = Element.parseColor(sc);
                tooltip = getDEAttribute(HISTOGRAM_COLUMN_TOOLTIP);
            }

            private void setLabels(List<Label> labels) {
                for (int i = 0; i < labels.size() - 2; i++) {
                    Label i1 = labels.get(i);
                    Label i2 = labels.get(i + 1);
                    if (i2.y >= y && i1.y <= y) {
                        label_1 = (Math.abs(y - i1.y) > Math.abs(y - i2.y) ? i2 : i1).val;
                    }
                    if (i2.y >= y + h && i1.y <= y + h) {
                        label_2 = (Math.abs(y + h - i1.y) > Math.abs(y + h - i2.y) ? i2 : i1).val;
                    }
                    if (label_1 != null && label_2 != null) break;
                }
            }

            private void setPeriod(List<WeekPeriod> periods) {
                for (WeekPeriod p : periods) {
                    Integer i1 = p.x;
                    Integer i2 = p.w + i1;
                    if (i1 <= x && i2 >= x + w) {
                        period = p.week;
                        break;
                    }
                }
            }

            public int getAvailability() {
                return label_1 - label_2;
            }

            @Override
            public String toString() {
                return period + ":" + getAvailability();
            }
        }

        private class Legend extends Element {
            private Color color;
            private String name;

            protected Legend(Element e) {
                super(e);
                name = getChildByXpath(HISTOGRAM_LEGEND_NAME).getDEText();
                String sc = getChildByXpath(HISTOGRAM_LEGEND_COLOR).getDEStyle();
                PSLogger.debug("legend: " + name + ", " + sc);
                color = Element.parseColor(sc);
            }
        }
    }

    public static String toGridFormat(float a) {
        if (a == 0) return "";
        return TeamPaneCell.getNumberFormatter(VARIABLE_ALLOCATION_NUMBER_FORMAT_GROUPING_USED,
                VARIABLE_ALLOCATION_NUMBER_FORMAT_MAX_FRACTION_DIGITS,
                VARIABLE_ALLOCATION_NUMBER_FORMAT_MIN_FRACTION_DIGITS).format(a);
    }

    public class Gantt extends AbstractGanttSection {

        public Gantt() {
            super(GRID_GANTT_SECTION, WBSPage.this);
        }

        private boolean useRobot = (getDriver().getType().isFF() || getDriver().getType().isIE(8)) &&
                LocalServerUtils.isDisplayEnabled(false);

        public boolean isRobot() {
            return useRobot;
        }

        @Override
        public Element getHeader() {
            return new Element(GRID_GANTT_HEADER) {
                @Override
                public boolean isVisible() {
                    if (getAppVersion().verGreaterOrEqual(_9_2) && getDriver().getType().isIE(6)) {
                        return !getElementClass().contains(GRID_GANTT_SECTION_HIDDEN.getLocator());
                    }
                    return super.isVisible();
                }
            };
        }

        public boolean isVisible() {
            if (getAppVersion().verGreaterOrEqual(_9_2) && getDriver().getType().isIE(6)) {
                String clazz = getElementClass();
                return clazz != null && !clazz.contains(GRID_GANTT_SECTION_HIDDEN.getLocator());
            }
            return super.isVisible();
        }

        public void waitForUnvisible() {
            if (getAppVersion().verGreaterOrEqual(_9_2) && getDriver().getType().isIE(6)) {
                waitForClass(GRID_GANTT_SECTION_HIDDEN);
                return;
            }
            super.waitForUnvisible();
        }

        public void waitForVisible() {
            if (getAppVersion().verGreaterOrEqual(_9_2) && getDriver().getType().isIE(6)) {
                waitForClassChanged(GRID_GANTT_SECTION_HIDDEN);
                return;
            }
            super.waitForVisible();
        }

        private class Cell extends CellElement {

            protected Cell(Element e) {
                super(e);
            }

            /**
             * get window horizontal coordinates for cell
             *
             * @param cell cell Element
             * @return array with x,w
             */
            private int[] getPosition() {
                Integer[] coords;
                coords = getCoordinates();
                Integer[] coords1 = getMiddleElment().getCoordinates();
                int x = coords[0];
                int y = coords1[2];
                return new int[]{x, y};
            }

            private CellElement getStatusElement() {
                return getElement(GRID_GANTT_SECTION_CELL_STATUS);
            }

            private CellElement getRightElment() {
                return getElement(GRID_GANTT_SECTION_CELL_RIGHT_TD);
            }

            private CellElement getLeftElment() {
                return getElement(GRID_GANTT_SECTION_CELL_LEFT_TD);
            }

            private CellElement getMiddleElment() {
                return getElement(GRID_GANTT_SECTION_CELL_MIDDLE_TD);
            }

            private CellElement getPercentageElement() {
                return getElement(GRID_GANTT_SECTION_CELL_PERCENTAGE);
            }

            private CellElement getBaseLineElement() {
                return new CellElement(getParent().getChildByXpath(GRID_GANTT_SECTION_CELL_BASELINE));
            }

            private CellElement getElement(ILocatorable loc) {
                return new CellElement(getChildByXpath(loc));
            }
        }

        private Element getLinkCanvas() {
            setDefaultElement(document);
            return Element.searchElementByXpath(this, GRID_GANTT_SECTION_LINK_CANVAS);
        }

        public Element getDynamicElement() {
            return new Element(GRID_GANTT_SECTION_LINK_CANVAS);
        }

        /**
         * debug method. maybe it is not required really
         */
        private void waitWhileLoading() {
            WBSPage.this.waitWhileLoading();
            setDefaultElement(document);
        }


        public List<Cell> getCells() {
            List<Cell> chs = new ArrayList<Cell>();
            setDefaultElement(document);
            for (Element cell : Element.searchElementsByXpath(this, GRID_GANTT_SECTION_CELL)) {
                Element barWrapper = cell.getChildByXpath(GRID_GANTT_SECTION_CELL_BAR_WRAPPER);
                if (barWrapper.isDEPresent())
                    chs.add(new Cell(barWrapper));
            }
            return chs;
        }

        public Cell getCell(Work work) {
            int index = getGrid().getRowIndex(work);
            return getCells().get(index - 1);
        }

        public boolean isCriticalPath(Work work) {
            return getCell(work).getElementClass().
                    contains(GRID_GANTT_SECTION_CELL_BAR_WRAPPER_CRITICAL_PATH.getLocator());
        }

        /**
         * @param work - ChildWork
         * @return - array, first cell - x, second - w
         */
        public int[] getPosition(Work work) {
            return getCell(work).getPosition();
        }


        /**
         * get status of work in gantt
         *
         * @param work ChildWork
         * @return -1 if no status, 0 if proposed, 1 if on track, 2 if copmleted, otherwise 100
         */
        public int getStatus(Work work) {
            Cell cell = getCell(work);
            String clazz = cell.getStatusElement().getElementClass();
            PSLogger.debug(work.getName() + "'s class is " + clazz);
            if (clazz.isEmpty()) return -1;
            if (GRID_GANTT_SECTION_CELL_STATUS_PROPOSED.getLocator().equals(clazz)) return 0;
            if (GRID_GANTT_SECTION_CELL_STATUS_ON_TRACK.getLocator().equals(clazz)) return 1;
            if (GRID_GANTT_SECTION_CELL_STATUS_COMPLETED.getLocator().equals(clazz)) return 2;
            return 100;
        }

        /**
         * get percentage of work
         *
         * @param work ChildWork work
         * @return float (0..1)
         */
        public float getPercentageCompleted(Work work) {
            Cell cell = getCell(work);
            String style = cell.getPercentageElement().getElementStyle().toLowerCase();
            PSLogger.debug(work.getName() + "'s style is " + style);
            if (style.isEmpty()) return 0;
            float pc = Float.parseFloat(style.replaceAll(".*width:\\s*(\\d+)%.*", "$1"));
            return pc / 100F;
        }

        public boolean isBaselinePresent(Work work) {
            Element baseline = getCell(work).getBaseLineElement();
            return baseline != null && baseline.isVisible();
        }

        /**
         * @param linkCanvas - Element
         * @param x          - relative x
         * @param y          - relative y
         * @param newX       - new relative x
         */
        private void changeCellUsingSelenium(Element linkCanvas, int x, int y, int newX, int newY, ILocatorable clazz) {
            Integer[] linkCanvasCoords = linkCanvas.getCoordinates();
            x -= linkCanvasCoords[0];
            y -= linkCanvasCoords[1];
            newX -= linkCanvasCoords[0];
            newY -= linkCanvasCoords[1];
            callDivider(linkCanvas, x, y, clazz, 3);
            linkCanvas.mouseDownAt(x, y);
            linkCanvas.mouseMoveAt(newX, newY);
            linkCanvas.mouseUp();
        }

        /**
         * @param linkCanvas - Element
         * @param x          - absolute x
         * @param y          - absolute y
         * @param newX       - new absolute x
         */
        private void changeCellUsingRobot(Element linkCanvas, int x, int y, int newX, int newY, ILocatorable clazz) {
            if (!useRobot) {
                PSSkipException.skip("Method 'changeCellUsingRobot' is only for display mode");
            }
            int[] absoluteXY = getAbsolutePageCoordinates();
            x += absoluteXY[0];
            y += absoluteXY[1];
            newX += absoluteXY[0];
            newY += absoluteXY[1];
            try {
                callDivider(linkCanvas, x, y, clazz, 3);
            } catch (Wait.WaitTimedOutException ww) {
                PSSkipException.skip(ww);
            }
            LocalServerUtils.mouseDown();
            LocalServerUtils.mouseMoveAt(newX, newY);
            LocalServerUtils.mouseUp();
        }

        /**
         * auxiliary method. it is hotfix
         *
         * @param linkCanvas Element //div[@class='linkCanvas']
         * @param x          - absolute or relative x
         * @param y          - absolute or relative y
         * @param clazz      - accepted class (resizeAction, moveAction)
         * @param num        - number of attempts
         */
        private void callDivider(Element linkCanvas, int x, int y, ILocatorable clazz, int num) {
            Wait.WaitTimedOutException e = null;
            for (int i = 0; i < num; i++) {
                try {
                    PSLogger.debug("class was " + linkCanvas.getElementClass());
                    if (useRobot) {
                        LocalServerUtils.mouseMoveAt(x, y);
                    } else {
                        linkCanvas.mouseMoveAt(x, y);
                    }
                    linkCanvas.waitForClass(clazz, GANTT_WAITER);
                    return;
                } catch (Wait.WaitTimedOutException ex) {
                    clickBody();
                    linkCanvas.setDefaultElement(WBSPage.this.getDocument());
                    String elementClass = linkCanvas.getElementClass();
                    PSLogger.debug("class is " + elementClass);
                    Assert.assertTrue(elementClass.contains(GRID_GANTT_SECTION_LINK_CANVAS_CLASS.getLocator()));
                    PSLogger.debug(linkCanvas.asXML());
                    checkJSError();
                    PSLogger.warn("Iteration #" + i + " : " + ex.getMessage());
                    PSLogger.saveFull();
                    e = ex;
                }
            }
            if (e != null)
                throw e;
        }

        public void scrollToCenter() {
            PSLogger.info("Scroll to center");
            scrollTo(getCells().get(0));
        }

        private void robotActions() {
            if (useRobot)
                F11();
            else {
                LocalServerUtils.mouseMoveAt(0, 0);
                GANTT_WAITER.waitTime();
            }
        }


        private void changeCell(Element linkCanvas, int x, int y, int newX, int newY, ILocatorable clazz) {
            if (useRobot) {
                // absolute coordinates:
                changeCellUsingRobot(linkCanvas, x, y, newX, newY, clazz);
            } else {
                // relative coordinates:
                changeCellUsingSelenium(linkCanvas, x, y, newX, newY, clazz);
            }
            // todo: this is workaround. investigate..
            LocalServerUtils.mouseMoveAt(0, 500);
            getTopElement().mouseDownAndUp();
            getTopElement().click(false);
            try {
                linkCanvas.waitForClassChanged(clazz, GANTT_WAITER);
            } catch (Wait.WaitTimedOutException e) {
                PSLogger.warn(e.getMessage());
                PSLogger.save();
                JS_WAITER.waitTime();
            }
            waitWhileLoading();
            PSLogger.save("After changes");
        }

        /**
         * this is a checker.
         * i see js error
         * on Win 6.1 (7,pro,x64), Release: 9.0 - Build 4973 on 02/11/11 - DBVersion 1094, ie 8.0.76000.16385
         */
        public void validate() {
            PSLogger.info("Validate for JS error");
            int index = 0;
            for (Cell cell : getCells()) {
                PSLogger.debug("Click on " + index);
                cell.click(false);
                checkJSError();
            }
        }

        public void resize(Work work, int newWidth) {
            if (newWidth == 0) {
                PSLogger.skip("Incorrect new width for cell.");
                return;
            }
            robotActions();
            PSLogger.info("Resize cell for " + work.getName() + " in gantt, newWidth is " + newWidth);
            Cell cell = getCell(work);
            CellElement td = cell.getRightElment();
            Element linkCanvas = getLinkCanvas();
            Integer[] tdCoords = td.getCoordinates();

            int[] cellCoords = cell.getPosition();

            int newX = newWidth + cellCoords[0];
            int x = tdCoords[0];
            int y = tdCoords[1] + tdCoords[3] / 2;

            PSLogger.debug("Coords for " + work.getName() + " are " + x + "," + y);
            changeCell(linkCanvas, x, y, newX, y, GRID_GANTT_SECTION_LINK_CANVAS_RESIZE_CLASS);
            PSLogger.save("After changing duration in gantt");

        }

        public void resize(Work work, float factor) {
            int w = getPosition(work)[1];
            resize(work, (int) (w * factor));
        }


        public void move(Work work, int newPosition) {
            robotActions();

            PSLogger.info("Move cell for " + work.getName() + " in gantt, newPosition is " + newPosition);

            Cell cell = getCell(work);
            Element td = cell.getMiddleElment();
            Element linkCanvas = getLinkCanvas();
            Integer[] tdCoords = td.getCoordinates();

            int x = tdCoords[0] + tdCoords[2] / 2;
            int y = tdCoords[1] + tdCoords[3] / 2;
            int newX = newPosition + x;

            changeCell(linkCanvas, x, y, newX, y, GRID_GANTT_SECTION_LINK_CANVAS_MOVE_CLASS);
            PSLogger.save("After moving in gantt");
        }

        public void makeDependency(Work work1, Work work2) {
            robotActions();

            PSLogger.info("Make dependency in gantt between " + work1.getName() + " and " + work2.getName());

            Cell firstCell = getCell(work1);
            Cell secondCell = getCell(work2);
            Element linkCanvas = getLinkCanvas();
            Integer[] firstCoords = firstCell.getMiddleElment().getCoordinates();
            Integer[] secondCoords = secondCell.getMiddleElment().getCoordinates();

            int x = firstCoords[0] + firstCoords[2] / 2;
            int y = firstCoords[1] + firstCoords[3] / 2;
            int newX = secondCoords[0] + secondCoords[2] / 2;
            int newY = secondCoords[1] + secondCoords[3] / 2;

            changeCell(linkCanvas, x, y, newX, newY, GRID_GANTT_SECTION_LINK_CANVAS_MOVE_CLASS);
            PSLogger.save("After making dependency in gantt");

        }

        public void setPercentageCompletion(Work work, float percentage) {
            robotActions();

            PSLogger.info("Change status for work " + work.getName() + " using gantt, percentage: " + percentage);

            Cell cell = getCell(work);
            Element td = cell.getLeftElment();
            Element linkCanvas = getLinkCanvas();
            Integer[] tdCoords = td.getCoordinates();

            int[] cellCoords = cell.getPosition();

            int x0 = cell.getPercentageElement().getCoordinates()[2];
            int x = tdCoords[0];
            int y = tdCoords[1] + tdCoords[3] / 2;
            int newX = x + (int) (cellCoords[1] * percentage);
            x += x0;

            PSLogger.debug("Coords for " + work.getName() + " are " + x + "," + y);
            changeCell(linkCanvas, x, y, newX, y, GRID_GANTT_SECTION_LINK_CANVAS_RESIZE_CLASS);
            PSLogger.save("After changing status in gantt");
        }

    }


    /**
     * class for tree item
     */
    public static class TeamPaneItem implements Comparable {
        protected String roleName;
        protected String personName;

        public TeamPaneItem(String person, String role) {
            this.personName = TEAM_PANE_MASTER_ROW_UNALLOCATED_PREFIX.getLocator().equals(person) ? null : person;
            this.roleName = role != null && role.isEmpty() ? null : role;
        }

        public TeamPaneItem(User person, Role role) {
            this(person != null ? person.getFullName() : null, role != null ? role.getName() : null);
        }

        public String toString() {
            StringBuffer res = new StringBuffer("[");
            res.append("role=").append("'").append(roleName).append("'");
            res.append(";");
            res.append("person=");
            if (personName == null) res.append(TEAM_PANE_MASTER_ROW_UNALLOCATED_PREFIX.getLocator());
            else res.append("'").append(personName).append("'");
            res.append("]");
            return res.toString();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof TeamPaneItem)) return false;
            if (roleName == null) {
                if (((TeamPaneItem) o).roleName != null)
                    return false;
            } else if (!roleName.equals(((TeamPaneItem) o).roleName)) return false;
            if (personName == null) {
                return ((TeamPaneItem) o).personName == null;
            }
            return personName.equals(((TeamPaneItem) o).personName);
        }

        private String getPerson() {
            return personName == null ? TEAM_PANE_MASTER_ROW_UNALLOCATED_PREFIX.getLocator() : personName;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof TeamPaneItem)) return -1;
            TeamPaneItem item = (TeamPaneItem) o;
            int res = getPerson().compareToIgnoreCase(item.getPerson());
            if (res != 0) return res;
            return roleName.compareToIgnoreCase(item.roleName);
        }
    }

    public class TeamPane extends Element {
        private TeamPaneTable grid;
        private static final long TREE_LOADING_TIMEOUT = 10000; // ms

        public TeamPane() {
            super(TEAM_PANE);
            grid = new TeamPaneTable(getChildByXpath(TEAM_PANE_GRID));
        }

        public void waitForVisible() {
            if (!getDriver().getType().isIE()) {
                Element tree = getTreeBody(null);
                tree.waitForVisible(TREE_LOADING_TIMEOUT);
                tree.waitForAttribute(TEAM_PANE_MASTER_TREE_LOADED, TEAM_PANE_MASTER_TREE_LOADED_TRUE, TREE_LOADING_TIMEOUT);
            }
            grid.waitForVisible(TREE_LOADING_TIMEOUT);
            setDefaultElement(getDocument());
        }

        private Element getTreeBody(Document doc) {
            Element res = new Element(TEAM_PANE_MASTER_TREE);
            if (doc != null) {
                res.setDefaultElement(doc);
            }
            return res;
        }

        private Element getEmptyCell() {
            return getChildByXpath(TEAM_PANE_EMPTY_CELL);
        }

        private boolean isEmpty() {
            Element cell = getEmptyCell();
            if (cell.exists() && cell.isVisible()) {
                PSLogger.info(cell.getText());
                return true;
            }
            return false;
        }

        private List<String> getListTree(Document doc) {
            List<String> res = new ArrayList<String>();
            for (Element e : Element.searchElementsByXpath(getTreeBody(doc), TREE_ITEM)) {
                res.add(SimpleGrid.getWorkNameByElement(e));
            }
            return res;
        }


        public List<TeamPaneItem> getList() {
            if (isEmpty()) return new ArrayList<TeamPaneItem>();
            waitForVisible(); // expected that there is a list
            List<TeamPaneItem> res = new ArrayList<TeamPaneItem>();
            setDefaultElement(document);
            List<String> names = getListTree(document);
            List<Element> roles = getElements(false, TEAM_PANE_ROLE_CELL);
            for (int i = 0; i < roles.size(); i++) {
                String role = roles.get(i).getDEText();
                String name = names.get(i);
                res.add(new TeamPaneItem(name, role));
            }
            return res;
        }

        public TeamPaneTable getTable() {
            return grid;
        }

    }

    /**
     * the formula is:
     * Duration * 8(hours) * allocation / 100(%) = effort (hour)
     *
     * @param effort     - Float
     * @param allocation - Float
     * @param duration   - Integer
     * @return String in format as on WBS page
     */
    public static String calculateEffortAllocationDuration(Float effort, Float allocation, Integer duration) {
        if (duration == null) {
            float res = calculateDuration(effort, allocation);
            float diff = res - (int) res;
            if (diff < 0.125 && diff > 0) {
                return (int) res + DAYS_SUFFIX.getLocator();
            }
            return (int) Math.ceil(res) + DAYS_SUFFIX.getLocator();
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.ROOT);
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP); // !!
        if (effort == null) {
            float res = calculateEffort(allocation, duration);
            return nf.format(res);
        }
        if (allocation == null) {
            float res = duration != 0 ? calculateAllocation(effort, duration) : 0;
            return nf.format(res);
        }
        return null;
    }

    public static float calculateDuration(Float effort, Float allocation) {
        return effort * 100 / allocation / 8;
    }

    public static float calculateEffort(Float allocation, Integer duration) {
        return duration * 8 * allocation / 100;
    }

    public static float calculateAllocation(Float effort, Integer duration) {
        return effort * 100 / duration / 8;
    }

    public static float gridRound(float a) {
        return (float) round(a, 4);
    }

    private static double round(double a, int points) {
        BigDecimal bd = new BigDecimal(a).setScale(points, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }


    public HomePage openHome() {
        getOptions().standardScreen();
        return super.openHome();
    }

    public PSPage openLastVisitedPage(String linkName, String id) {
        getOptions().standardScreen();
        return super.openLastVisitedPage(linkName, id);
    }

    private static class VisibleWork extends Work {
        public VisibleWork(String name) {
            super(name);
        }

        public boolean exist() {
            return true;
        }

        public void setCreated() {
        }

        public void setDeleted() {
        }
    }
}
