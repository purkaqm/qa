package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.Menu;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.AddNewReportPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.ReportFolderPage;
import com.powersteeringsoftware.libs.pages.ReportWindow;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators.*;

public abstract class ReportWizardPage extends PSPage {

    private boolean isCompleted;
    private Set<RWTab> rwTabLocators = new LinkedHashSet<RWTab>();
    private RWTab tabType;
    final static String HOVER = "Hover";
    private static PowerSteeringVersions version;

    protected ReportWizardPage(RWTab tabType) {
        this.tabType = tabType;
    }

    protected ReportWizardPage(RWTab tabType, boolean isCompleted) {
        this(tabType);
        setIsCompleted(isCompleted);
    }

    public void setIsCompleted(boolean x) {
        isCompleted = x;
    }

    public void open() {
        AddNewReportPage addNRPage = new AddNewReportPage();
        addNRPage.open();
        addNRPage.startReportWizard();
        getDocument();
        findRWTabs();
    }

    public static ReportWizardPage getPageInstance() {
        return getPageInstance(RWTab.Type);
    }

    public static ReportWizardPage getPageInstance(RWTab tabType) {
        ReportWizardPage rwPage = null;
        switch (tabType) {
            case Type:
                rwPage = new TypeRWPage();
                break;
            case Definition:
                rwPage = new DefinitionRWPage();
                break;
            case Columns:
                rwPage = new ColumnsRWPage();
                break;
            case Filter:
                rwPage = new FilterRWPage();
                break;
            case GroupSort:
                rwPage = new GroupSortRWPage();
                break;
            case Summary:
                rwPage = new SummaryRWPage();
                break;
            case Layout:
                rwPage = new LayoutRWPage();
                break;
            case Chart:
                rwPage = new ChartRWPage();
                break;
            case Save:
                rwPage = new SaveRWPage();
                break;
        }
        return rwPage;
    }

    @Override
    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        setDocument();
    }


    public void setDocument() {
        super.getDocument(true);
    }

    public Element searchElement(String xPath) {
        return Element.searchElementByXpath(getPageDocument(), xPath);
    }

    public List<Element> searchElements(String xPath) {
        return Element.searchElementsByXpath(getPageDocument(), xPath);
    }

    public Element searchElement(ILocatorable loc) {
        return Element.searchElementByXpath(getPageDocument(), loc);
    }

    protected Document getPageDocument() {
        return getDocument(false);
    }


    public RWTab getPageRWType() {
        return this.tabType;
    }

    public String toString() {
        return getPageRWType().name();
    }

    public Element getTopBar() {
        return getElement(false, TOP_BAR_LOCATOR);
    }

    private int findRWTabs() {
        List<Element> repWizardTabs = Element.searchElementsByXpath(getTopBar(), TAB_BAR_LINK);
        for (Element tabElm : repWizardTabs) {
            String title = tabElm.getDEText();
            RWTab tab = RWTab.getByTitle(title);
            Assert.assertTrue((tab != null), "Report wizard tab " + title + " was not found");
            rwTabLocators.add(tab);
        }
        return repWizardTabs.size();
    }


    private RWTab clickContinue() {
        Button continueBtn = new Button(PAGE_CONTINUE_BUTTON);
        if (!continueBtn.exists()) return null;
        PSLogger.save("Before continue");
        continueBtn.click(true);
        return getCurrentTab();
    }

    public ReportWizardPage doContinue() {
        RWTab tab = clickContinue();
        if (tab == null) {
            PSLogger.debug("Cannot find 'continue' button");
            return null;
        }
        ReportWizardPage res = getPageInstance(tab);
        res.setDocument();
        res.setIsCompleted(isCompleted);
        return res;
    }

    public RWTab getCurrentTab() {
        Element currentRepWizardTab = new Element(CURRENT_BAR_TAB_LINK);
        String tabTitle = currentRepWizardTab.getText();
        return RWTab.getByTitle(tabTitle);
    }

    public void clickPreview() {
        getPreviewLink().click(false); // ????
    }

    /**
     * this link available only for copies or just created reports
     */
    public SaveRWPage saveAs() {
        if (!isCompleted) {
            return null; //todo. no button for this page
        }
        Button saveBtn = new Button(PAGE_SAVE_AS_BUTTON);
        Assert.assertTrue(saveBtn.exists(), "Cannot find 'save as' button");
        saveBtn.click(true);
        String error;
        Assert.assertNull(error = getErrorBoxMessage(), "There is an error : " + error);
        return checkSaveTab();
    }

    public PSPage save() {
        return save(true);
    }

    protected void doSave(boolean doValidate) {
        PSLogger.info("Do save");
        Button saveBtn = new Button(PAGE_SAVE_BUTTON);
        saveBtn.waitForVisible(5000);
        PSLogger.save("Before saving");
        saveBtn.click(true);
        if (doValidate) {
            String error;
            Assert.assertNull(error = getErrorBoxMessage(), "There is an error : " + error);
        }
        PSLogger.save("After saving");
    }

    public PSPage save(boolean doValidate) {
        doSave(doValidate);
        if (isCompleted) {
            Assert.assertTrue(checkUrl(), "Incorrect page after saving completed page");
            Assert.assertTrue(isActive(), this + " tab is not active");
            return this;
        }
        if (getPageRWType().equals(RWTab.Save)) {
            ReportFolderPage res = new ReportFolderPage();
            Assert.assertTrue(res.checkUrl(), "Incorrect page after saving");
            return res;
        }
        return checkSaveTab();
    }

    private static SaveRWPage checkSaveTab() {
        ReportWizardPage res = ReportWizardPage.getPageInstance(RWTab.Save);
        Assert.assertTrue(res.checkUrl(), "Incorrect page after saving, expected report wizard");
        res.setDocument();
        Assert.assertTrue(res.isActive(), "Save tab is not active");
        return (SaveRWPage) res;
    }

    public ReportWindow clickRunAsHtmlOption() {
        Link runLink = new Link(RUN_LINK);

        Menu menu = new Menu(runLink);
        menu.open();
        Assert.assertTrue(menu.containsItem(MENU_RUN_HTML), "Cannot find item " + MENU_RUN_HTML.getLocator());
        PSLogger.debug(menu.asXML());
        ReportWindow res = new ReportWindow(true);
        // TODO: temporary disable calling new window. fix for all browsers types and uncomment
        menu.call(MENU_RUN_HTML);
        waitForPageToLoad();
        res.waitForOpen();
        return res;
    }

    public boolean isActive() {
        return getPageRWType().equals(getCurrentTab());
    }

    public int getTabsNum() {
        return rwTabLocators.size();
    }

    public List<RWTab> getTabs() {
        return new ArrayList<RWTab>(rwTabLocators);
    }

    public RWTab getFirstTab() {
        Iterator<RWTab> tabItr = rwTabLocators.iterator();
        return tabItr.hasNext() ? tabItr.next() : null;
    }

    public RWTab getNextTab(RWTab currentTab) {
        boolean next = false;
        for (RWTab tab : rwTabLocators) {
            if (next) {
                return tab;
            }
            next = (tab == currentTab);
        }
        return null;
    }

    public ReportWizardPage goToTab(RWTab tab) {
        if (isActive() && getPageRWType().equals(tab)) {
            PSLogger.debug("Tab " + this + " is active");
            return this;
        }
        PSLogger.info("Go to tab " + tab);
        ReportWizardPage res = getPageInstance(tab);
        new Link(tab).click(false);
        res.waitForPageToLoad();
        res.setIsCompleted(isCompleted);
        return res;
    }


    private Link getPreviewLink() {
        return new Link(PREVIEW_LINK);
    }

} // class RepotWizardPage