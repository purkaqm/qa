/*
 * Copyright (c) PowerSteering Software 2011
 * All rights reserved.
 *
 * This software and documentation contain valuable trade secrets and proprietary information belonging to
 * PowerSteering Software Inc.  None of the foregoing material may be copied, duplicated or disclosed without the
 * express written permission of PowerSteering.  Reverse engineering, decompiling and disassembling are explicitly
 * prohibited. POWERSTEERING SOFTWARE EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR ANY
 * PARTICULAR PURPOSE, AND WARRANTIES OF NON-INFRINGEMENT OF INTELLECTUAL PROPERTY RIGHTS OF A
 * THIRD PARTY, PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE OF DEALING
 * OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED WITH RESPECT TO THE USE OF THE
 * SOFTWARE OR DOCUMENTATION.  Under no circumstances shall PowerSteering Software be liable for incidental,
 * special, indirect, direct or consequential damages or loss of profits, interruption of business, or related expenses which
 * may arise from use of software or documentation, including but not limited to those resulting from defects in software
 * and/or documentation, or loss or inaccuracy of data of any kind.
 */
package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.FrameLocators;
import com.powersteeringsoftware.libs.enums.page_locators.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.StalePageException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators.QuickSearch100Locators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.LeftNavMainMenuLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.MainMenuLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.ProblemPageLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.MESSAGE_ERROR_CONNECTION;

/**
 * Main class for any page.
 * User: szuev
 * Date: 08.05.2010
 * Time: 21:26:45
 */
public abstract class PSPage extends APSPage {

    public static final long SEARCH_ATTEMPTS = 5;
    public static final TimerWaiter SEARCH_TIMEOUT = new TimerWaiter(1000);

    protected static boolean check = true;

    public static void setDoCheck(boolean b) {
        check = b;
    }


    private static final long MENU_TIMEOUT = 30000; //ms
    private static final TimerWaiter MESSAGES_WAITER = new TimerWaiter(5000);
    private static final int MENU_ATTEMPT_NUM = 3;

    public abstract void open();

    private static Class lastPage;

    public PSPage() {
        if (!getClass().isAnonymousClass()) {
            lastPage = getClass();
        }
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public void testUrl() {
        if (check)
            doTestUrl();
    }

    public void doTestUrl() {
        Assert.assertTrue(checkUrl(), "Incorrect url for " + getName());
    }

    protected void open(String url) {
        doOpen(url);
    }

    @Deprecated
    protected void open(MainMenuLocators loc) {
        doOpen(loc);
    }

    protected void open(ILocatorable loc) {
        open(loc.getLocator());
    }

    /**
     * @param o
     */
    private void doOpen(Object o) {
        try {
            if (o instanceof MainMenuLocators) {
                clickSubmenus((MainMenuLocators) o);
                setUrl();
                document = null;
                if (getDocumentOnOpen)
                    getDocument();
            } else {
                super.open(String.valueOf(o));
            }
        } catch (SeleniumException se) {
            if (se.getMessage().contains(SeleniumDriverFactory.SELENIUM_EXCEPTION_TIMEOUT)) {
                PSLogger.saveFull();
                throw new PSKnownIssueException(72196, se); // can't open after timeout -> problems with connections.
            }
            throw se;
        }
    }

    public static PSPage getEmptyInstance() {
        return new PSPage() {
            @Override
            public void open() {
                // empty open
            }
        };
    }

    public static PSPage getInstance() {
        WBSPage external = WBSPage.getInstance();
        if (external.checkUrl()) {
            PSLogger.info("External WBS page is open : " + external.getUrl());
            return external;
        }
        return getEmptyInstance();
    }

    public void waitForPageToLoad() {
        // skipp validation for ie for quickening
        waitForPageToLoad(CoreProperties.doValidatePage() && !getDriver().getType().isRCDriverIE());
    }

    public void waitForPageToLoad(boolean doCheckPage) {
        waitForPageToLoad(doCheckPage, CoreProperties.getWaitForElementToLoad());
    }

    public void waitForPageToLoad(boolean doCheckPage, long timeout) {
        super.waitForPageToLoad(timeout);
        if (doCheckPage)
            validate();
    }

    public void doRefresh() {
        super.doRefresh(CoreProperties.getWaitForElementToLoad());
    }

    public void goBack() {
        super.goBack(CoreProperties.getWaitForElementToLoad());
    }


    public Link getLogoutLink() {
        return new Link(LOGOUT_COMMON_LINK);
    }

    public LogInPage doLogout() {
        Link link = getLogoutLink();
        if (!link.exists()) {
            checkBlankPage();
        }
        Assert.assertTrue(link.exists(), "Can't find logout link");
        link.clickAndWaitNextPage();
        PSLogger.save("After Log Out.");
        LogoutPage logoutPage = new LogoutPage();
        if (!logoutPage.checkUrl()) return null;
        LogInPage logInPage = logoutPage.doRestart();
        if (!getLogoutLink().exists())
            return logInPage;
        // try again
        PSLogger.save("Session is active after restarting");
        getLogoutLink().clickAndWaitNextPage();
        return logoutPage.doRestart();
    }

    public Menu getHelpMenu() {
        return new Menu(new Element(getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0) ? HELP_110 : HELP_100),
                getDriver().getType().isWebDriver());
    }

    public String getHelpMenuVersion() {
        try {
            Menu menu = getHelpMenu();
            menu.open();
            menu.call(HELP_VERSION);
            String alert = getDriver().getAlert();
            PSLogger.info("HELP VERSION: " + alert);
            return alert;
        } catch (Throwable t) {
            PSLogger.fatal(t.getMessage());
            return null;
        }
    }

    public HelpWindow getHelpMenuOnlineHelp() {
        Menu menu = getHelpMenu();
        menu.open();
        menu.call(HELP_ONLINE_HELP);
        HelpWindow res = new HelpWindow();
        res.waitForOpen();
        return res;
    }

    public String getFullUserName() {
        Element us = new Element(getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0) ? USER_NAME_110 : USER_NAME_LINK_100);
        if (getDriver().getType().isWebDriver())
            us.waitForPresent();
        if (!us.exists()) checkBlankPage();
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            // 11.0
            return StrUtil.trim(us.getAttribute(USER_NAME_110_TITLE));
        } else {
            // 10.0
            return StrUtil.trim(us.getText());
        }
    }

    /**
     * if page has fresh document, then try to parse it to search messages.
     * otherwise use selenium access (by default)
     *
     * @return list of messages
     */
    public List<Element> getMessages() {
        List<Element> res = new ArrayList<Element>();
        if (isFreshDocument) { // static way:
            res = getElements(false, MESSAGE);
            setFreshDocument(false);
        } else {
            PSLogger.debug("Dynamic way");
            for (int i = 1; ; i++) {
                Element mes = new Element(MESSAGES.replace(i));
                if (mes.exists()) {
                    res.add(mes);
                } else {
                    break;
                }
            }
        }
        return res;
    }

    public void closeMessages() {
        Element close = new Element(MESSAGES_CLOSE);
        if (close.exists()) {
            PSLogger.debug("Close messages popup");
            if (getDriver().getType().isGoogleChromeGreaterThan(10)) {
                new Element(MESSAGES_CONSOLE).waitForStyle(MESSAGE_CONSOLE_READY);
            }
            close.click(false);
            close.waitForUnvisible();
            getDocument();
        }
    }

    public List<String> getMessagesFromTopAndClose() {
        List<String> res = getMessagesFromTop();
        closeMessages();
        return res;
    }

    public List<String> getMessagesFromTop() {
        List<String> res = new ArrayList<String>();
        for (Element mes : getMessages()) {
            res.add(mes.isDEPresent() ? mes.getDEText() : mes.getText());
        }
        checkMessagesForCritical(res);
        return res;
    }


    public List<String> getErrorMessagesFromTop() {
        // to debug:
        boolean isFresh = isFreshDocument;
        if (!isFresh)
            MESSAGES_WAITER.waitTime();
        List<String> res = new ArrayList<String>();
        for (Element mes : getMessages()) {
            Element ch = mes.getChildByXpath(MESSAGES_ERROR);
            String txt = null;
            if (ch.isDEPresent()) // static :
                txt = mes.getDEInnerText();
            else if (!isFresh && ch.exists()) // dynamic :
                txt = mes.getText();
            if (txt == null) continue;
            res.add(StrUtil.replaceSpaces(txt.replaceAll("\\s+", " ")).trim()); // for 9.3
        }
        if (res.size() != 0)
            PSLogger.debug("Page messages: " + res);
        checkMessagesForKnis(res);
        checkMessagesForCritical(res);
        return res;
    }

    public void checkForErrorMessagesFromTop() {
        List<String> messages = getErrorMessagesFromTop();
        if (messages.size() != 0) {
            PSLogger.warn(messages);
            PSLogger.save("There are errors on page!");
        }
    }

    private static void checkMessagesForCritical(List<String> mess) {
        for (String error : mess) {
            if (error.contains(MESSAGE_ERROR_CONNECTION.getLocator())) // hotfix
                throw new PSKnownIssueException("72196", mess.toString());
        }
    }

    private static void checkMessagesForKnis(List<String> mess) {
        Map<String, String> exceptions = CoreProperties.getKnownPageExceptions();
        for (String ex : exceptions.keySet()) {
            if (mess.toString().contains(ex)) {
                throw new PSKnownIssueException(exceptions.get(ex), mess.toString());
            }
        }
    }


    public String getErrorBoxMessage() {
        Element error = new Element(ERROR_BOX);
        if (!error.exists()) return null;
        StringBuilder sb = new StringBuilder();
        error.setDefaultElement(getDocument());
        sb.append(error.getDEText());
        List<Element> res = Element.searchElementsByXpath(error, ERROR_BOX_MESSAGE);
        for (Element e : res) {
            sb.append(e.getDEText()).append("\n");
        }
        PSLogger.debug(sb);
        return res.size() == 0 ? sb.toString() : sb.substring(0, sb.length() - 1);
    }

    public Element getMessageBox() {
        return new Element(MESSAGE_BOX);
    }

    /**
     * Open the Quick Search Popup
     *
     * @return a QuickSearchDialog
     */
    public SearchEngine openQuickSearch() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            QuickSearchInput in = new QuickSearchInput();
            in.open();
            return in;
        }
        QuickSearchDialog dialog = new QuickSearchDialog();
        dialog.open();
        return dialog;
    }

    public AnythingASPage openAdvancedSearch() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            QuickSearchInput in = (QuickSearchInput) openQuickSearch();
            in.getAdvancedLink().clickAndWaitNextPage();
        } else if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) {
            QuickSearchDialog dialog = (QuickSearchDialog) openQuickSearch();
            dialog.getAdvancedLink().clickAndWaitNextPage();
        } else {
            clickBrowseAdvancedSearch();
        }
        AnythingASPage res = new AnythingASPage();
        res.url = url;
        return res;
    }

    public PSPage openLastVisitedPage(String linkName) {
        return openLastVisitedPage(linkName, null);
    }


    public PSPage openLastVisitedPage(String linkName, String id) {
        Link link;
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            link = getLastVisitedLink(id);
            if (link == null) return null;
            SummaryWorkPage res = SummaryWorkPage.getInstance(false, id);
            link.setResultPage(res);
            link.clickAndWaitNextPage();
            return res;
        }
        link = getLastVisitedLink(linkName, id);
        if (link == null) {
            PSLogger.debug("Can't find link '" + linkName + "' in last visited links");
            return null;
        }
        PSLogger.info("Open last visited link '" + linkName + "'");
        Element popup = new Element(LAST_VISITED_SHOW_POPUP);
        if (!popup.exists()) return null;
        new Link(LAST_VISITED_SHOW_LINK).click(false);
        popup.waitForVisible();
        if (!CoreProperties.getBrowser().isIE())
            popup.waitForAttribute(MENU_STYLE, MENU_STYLE_OPACITY);
        PSLogger.save(); // recollect links:
        //link = getLastVisitedLink(linkName);
        link.setDefaultElement(getDocument());
        String txt = link.getText();
        PSLogger.debug("last visited link : " + link);
        Assert.assertEquals(txt, linkName, "Incorrect link found");
        PSPage res;
        if (LAST_VISITED_MORE.getLocator().equals(linkName))
            res = new HistoryPage();
        else
            res = SummaryWorkPage.getInstance(false, id);
        new Link(link, res).clickAndWaitNextPage();
        return res;
    }

    public HistoryPage openLastVisited() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            // todo
            return null;
        }
        HistoryPage res = new HistoryPage();
        res.open();
        return res;
    }

    public Link getLastVisitedLink(String id) {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            if (id == null) return null;
            Link link = new Link(new Element(LeftNavLocators.Storage.PROJECT).getParent());
            String href = link.getHref();
            if (href.contains(id)) {
                return link;
            }
            return null;
        }
        List<Link> lastVisitedLinks = collectLastVisitedLinks();
        if (lastVisitedLinks == null) return null;
        for (Link link : lastVisitedLinks) {
            if (link.getHref().contains(id)) {
                return link;
            }
        }
        return null;
    }

    private Link getLastVisitedLink(String linkName, String id) {
        List<Link> lastVisitedLinks = collectLastVisitedLinks();
        if (lastVisitedLinks == null) return null;
        for (Link link : lastVisitedLinks) {
            if (link.getName().equals(linkName) && (id == null || link.getHref().contains(id))) {
                return link;
            }
        }
        return null;
    }

    private List<Link> collectLastVisitedLinks() {
        List<Link> lastVisitedLinks = new ArrayList<Link>();
        Element popup = getElement(LAST_VISITED_SHOW_POPUP);
        if (popup == null) return null; // this is for uix_page e.g.
        List<Element> links = Element.searchElementsByXpath(popup, LAST_VISITED_LINKS);
        for (Element link : links) {
            // todo: hotfix. why dom locator is not working here for just created work?
            Link last =
//                    link.isDomLocator() ?
//                    new Link(link.getXpathLocator().
//                            replace(LAST_VISITED_LINKS_TO_REPLACE.getLocator(), "")) :
                    new Link(link);
            last.setName(link.getDEText());
            PSLogger.debug("Last visited : " + last.getName());
            lastVisitedLinks.add(last);
        }
        return lastVisitedLinks;
    }

    public void validate() {
        //if (getDriver().getType().isZero()) return; // skipp validating for empty driver.
        PSLogger.debug("validate page " + getName());
        String frameLoc = getDriver().getCurrentFrame();
        Frame frameWas = null;
        if (frameLoc != null) {
            frameWas = getFrame(frameLoc);
        }
        Frame up = getUpperFrame();
        up.select();
        if (frameWas != null && !frameWas.exists()) frameWas = null;
        checkStale(getStaleProblemFromPage(), "Stale page encountered");
        checkProblem(getProblemFromPage(), "There is problem on page");
        if (new Element(SERVLET_EXCEPTION_HEADER).exists()) {
            new Element(SERVLET_EXCEPTION_DETAILS_LINK).click(false);
            PSApplicationException.PSServletException ex = null;
            try {
                ex = PSApplicationException.PSServletException.parse(new Element(SERVLET_EXCEPTION_TRACE).getText());
                PSLogger.debug(ex);
            } catch (Exception e) {
                //ignore
            }
            throw new SeleniumException("There is servlet exception on page", ex);
        }
        if (new Element(ALERT_ERROR_HEADER).exists()) {
            new Element(ALERT_ERROR_DETAILS_LINK).click(false);
            PSApplicationException.PSServletException ex = null;
            try {
                Element exBody = new Element(ALERT_ERROR_BODY);
                exBody.setFrame(FrameLocators.IFRAME);
                ex = PSApplicationException.PSServletException.parse(exBody.getText());
                PSLogger.debug(ex);
            } catch (Exception e) {
                //ignore
            }
            checkProblem(ex, "There is alert error on page");
        }

        if (new Element(FF_FRAME_UNAVAILABLE_LOCATOR).exists()) {
            throw new SeleniumException("Frame is unavailable");
        }
        if (frameWas != null && !frameWas.equals(up)) {
            frameWas.select();
        }
    }

    private void checkStale(StalePageException s, String msg) {
        if (s == null) return;
        throw new SeleniumException(msg, s);
    }

    private void checkProblem(PSApplicationException problem, String msg) {
        if (problem == null) return;
        PSKnownIssueException knis = lastPage != null ? problem.toKnownIssueException(CoreProperties.getKnownPageExceptions(lastPage.getSimpleName())) : null;
        if (knis == null) knis = problem.toKnownIssueException(CoreProperties.getKnownPageExceptions());
        PSLogger.save("Problem!");
        throw knis != null ? knis : new SeleniumException(msg, problem);
    }

    public StalePageException getStaleProblemFromPage() {
        Element problem = getProblemHeader(STALE_PAGE);
        if (!problem.exists()) return null;
        return new StalePageException();
    }

    private Element getProblemHeader(ProblemPageLocators loc) {
        return new Element((getDriver().getType().isRCDriverIE() ? PROBLEM_HEADER_IE_RC : PROBLEM_HEADER).replace(loc));
    }

    public PSApplicationException getProblemFromPage() {
        Element problem = getProblemHeader(PROBLEM);
        if (!problem.exists()) return null;
        Element perms = new Element(PERMISSION_EXCEPTION);
        if (perms.exists()) {
            PSLogger.info("'Insufficient Permission' on page");
            return new PSApplicationException.PSPermissionException(perms.getText());
        }
        Element objectDeleted = new Element(OBJECT_DELETED_EXCEPTION);
        if (objectDeleted.exists()) {
            PSLogger.info("'Object deleted' on page");
            return new PSApplicationException.PSObjectDeletedException(objectDeleted.getText());
        }
        PSLogger.info("Exception on page");
        PSApplicationException exception = new PSApplicationException(problem.getText());
        Link details = new Link(PROBLEM_DETAILS_LINK);
        if (!details.exists()) {
            return exception;
        }
        details.click(false);
        Element table = getElement(PROBLEM_EXCEPTION_TABLE);
        StringBuffer ex = new StringBuffer();

        if (table != null) {
            for (Element td : Element.searchElementsByXpath(table, PROBLEM_EXCEPTION_TD)) {
                ex.append(td.getDefaultElement().getTextTrim()).append("\n");
                List<Element> stack = Element.searchElementsByXpath(td, PROBLEM_EXCEPTION_TD_STACK_TRACE);
                for (Element li : stack) {
                    String txt = li.getDEText();
                    ex.append(txt).append("\n");
                    exception.addStackTrace(txt);
                }
            }
        }
        PSLogger.debug(ex);
        PSLogger.save();
        return exception;
    }

    private void clickSubmenus(MainMenuLocators submenu) {
        Link link = new Link(submenu); // todo: see Link class
        // can't find link by css locator for ff and 2.2.0
        if (!link.exists()) {
            checkBlankPage();
        }
        Assert.assertTrue(link.exists(), "Can't find link " + submenu.getLocator());
        openMenuFor(submenu);
        link.waitForVisible(10000);
        link.clickAndWaitNextPage();
        closeAllMenus();
    }

    /* -------  "Admin" menu items   ------- */

    /**
     * Click main menu item Admin>>Templates:Work
     */
    public void clickAdminTemplatesWork() {
        clickMenuItem(ADMIN_TEMPLATES_WORK, WORK_TEMPLATES);
    }

    /**
     * Click main menu item Admin>>Configuration: Tags
     */
    public void clickAdminConfigurationsTags() {
        clickMenuItem(ADMIN_CONFIGURATION_TAGS, CONFIGURATION_TAGS);
    }

    public void clickAdminCustomFields() {
        clickMenuItem(ADMIN_CONFIGURATION_CUSTOM_FIELDS_LINK, CONFIGURATION_CUSTOM_FIELDS);
    }

    public void clickAdminProcesses() {
        clickMenuItem(ADMIN_CONFIGURATION_PROCESSES_LINK, CONFIGURATION_PROCESSES);
    }

    public void clickAdminDefaults() {
        clickMenuItem(ADMIN_DEFAULTS, PERMISSIONS_DEFAULTS);
    }

    public void clickAdminDefineCategories() {
        clickMenuItem(ADMIN_DEFINE_CATEGORIES, PERMISSIONS_DEFINE_CATEGORIES);
    }

    public void clickAdminAgents() {
        clickMenuItem(ADMIN_AGENTS_LINK, CONFIGURATION_AGENTS);
    }

    public void clickBrowseCreateReports() {
        clickMenuItem(BROWSE_CREATE_REPORTS_LINK, CREATE_REPORTS);
    }

    public void clickBrowseMyReports() {
        clickMenuItem(BROWSE_MY_REPORTS_LINK, MY_REPORTS);
    }

    public void clickBrowsePublicReports() {
        clickMenuItem(BROWSE_PUBLIC_REPORTS_LINK, PUBLIC_REPORTS);
    }
    /* -------   "Browse" menu items  -------- */

    /**
     * Click main menu item "Browse>>New work"
     */
    public void clickBrowseNewWork() {
        clickMenuItem(BROWSE_CREATE_NEW_WORK_LINK, ADD_PROJECT);
    }

    public void clickBrowseNewOrg() {
        clickMenuItem(BROWSE_CREATE_NEW_ORG_LINK, ADD_ORG);
    }

    /**
     * Click main menu item Browse>>Work Tree
     */
    public void clickBrowseWorkTree() {
        clickMenuItem(BROWSE_WORK_TREE_LINK, WORK_TREE);
    }

    /**
     * Click main menu item "Browse>>Advanced Search"
     */
    private void clickBrowseAdvancedSearch() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3))
            PSSkipException.skip("No 'Browse>Advanced Search' in 9.3 or greater"); // see #80626
        clickMenuItem(BROWSE_ADVANCED_SEARCH_LINK);
    }

    public void clickBrowseManageTime() {
        clickMenuItem(BROWSE_PEOPLE_MANAGE_TIME_LINK, MANAGE_TIME);
    }

    public void clickBrowseMeasureLibrary() {
        clickMenuItem(BROWSE_MEASURES_LIBRARY_LINK, MEASURE_LIBRARY);
    }

    public void clickBrowseInviteNewUser() {
        clickMenuItem(BROWSE_INVITE_NEW_USER, ADD_USER);
    }

    public void clickBrowsePeople() {
        clickMenuItem(BROWSE_PEOPLE_LINK, PEOPLE);
    }

    public void clickResourceRates() {
        clickMenuItem(BROWSE_RESOURCE_RATES, RESOURCE_RATES);
    }

    public void clickResourcePool() {
        clickMenuItem(BROWSE_RESOURCE_POOLS, RESOURCE_POOL);
    }

    public void clickResourceReview() {
        clickMenuItem(BROWSE_RESOURCE_REVIEW_LINK, RESOURCE_REVIEW);
    }

    public void clickPortfolios() {
        clickMenuItem(BROWSE_REVIEW_PORTFOLIOS, PORTFOLIOS);
    }

    public void clickBrowseMyProfileTimesheets() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            Link link = callUserMenu().getMenuLink(UserNameMenu110.ITEM_TIMESHEETS);
            link.setResultPage(this);
            link.clickAndWaitNextPage();
            return;
        }
        // 10.0
        clickMenuItem(BROWSE_MY_PROFILE_TIMESHEETS_LINK);
    }


    public void clickBrowseMyProfileAgenda() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            Link link = callUserMenu().getMenuLink(UserNameMenu110.ITEM_AGENDA);
            link.setResultPage(this);
            link.clickAndWaitNextPage();
            return;
        }
        // 10.0
        clickMenuItem(BROWSE_MY_PROFILE_AGENDA_LINK);
    }

    public LinkMenu callUserMenu() {
        LinkMenu menu;
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            menu = new UserMenu110();
        } else {
            menu = new Menu(USER_NAME_LINK_100);
        }
        menu.open();
        PSLogger.save("After calling user menu");
        return menu;
    }


    public class QuickSearchInput extends Element implements SearchEngine {
        public QuickSearchInput() {
            super(QuickSearch110Locators.ICON);
        }

        public void open() {
            if (getElementClass().contains(QuickSearch110Locators.ICON_OPEN_CLASS.getLocator())) {
                PSLogger.debug("Quick search already is opened");
                return;
            }
            click(false);
            try {
                waitForInput();
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.error(ww);
                PSLogger.debug(new Element(QuickSearch110Locators.QUICK_SEARCH).asXML());
                waitForInput();
            }
        }

        private void waitForInput() {
            waitForClass(QuickSearch110Locators.ICON_OPEN_CLASS);
            getInput().waitForVisible(MENU_TIMEOUT);
        }

        private Input getInput() {
            return new Input(QuickSearch110Locators.INPUT);
        }

        public QuickSearchResult type(String txt) {
            Input in = getInput();
            in.type(txt);
            in.keyDown(' ');
            in.keyUp(' ');
            if (txt.isEmpty()) return null;
            QuickSearchResult res = new QuickSearchResult();
            res.waitForVisible();
            res.setDefaultElement(getDocument());
            return res;
        }

        public void clear() {
            Input in = getInput();
            in.type("");
            in.keyDown(' ');
            in.keyUp(' ');
            getContainerHeaderElement().click(false);
            new QuickSearchResult().waitForUnvisible();
        }

        public Menu callTypeMenu() {
            Menu menu = new Menu(QuickSearch110Locators.TYPE_ICON);
            menu.open();
            return menu;
        }

        public Link getAdvancedLink() {
            return new Link(QuickSearch110Locators.ADVANCED_LINK);
        }

        @Override
        public SearchResult makeSearch(String txt, Type type) {
            PSLogger.info("Quick search (" + txt + "," + type + ")");
            open();
            if (type != null) {
                Menu menu = callTypeMenu();
                List<String> items = menu.getMenuItems();
                PSLogger.debug("Items " + items);
                String label = null;
                for (String i : items) {
                    if (i.equalsIgnoreCase(type.name())) {
                        label = i;
                        break;
                    }
                }
                Assert.assertNotNull(label, "Can't find type " + type);
                menu.call(label);
            }
            QuickSearchResult res = type(txt);
            PSLogger.save("After quick search (" + txt + "," + type + ")");
            return res;
        }
    }

    public class QuickSearchResult extends Element implements SearchResult {

        public QuickSearchResult() {
            super(QuickSearchResultLocators.TABLE);
        }

        public void waitForVisible(long timeout) {
            new Element(QuickSearchResultLocators.LOADING).waitForUnvisible(timeout);
            super.waitForVisible(timeout);
            getChildByXpath(QuickSearchResultLocators.ANY_ROW).waitForVisible(timeout);
            new TimerWaiter(1000).waitTime();
            PSLogger.debug(getParent().asXML());
        }

        public boolean isNoResults() {
            return new Element(QuickSearchResultLocators.NO_RESULTS).isVisible();
        }

        @Override
        public boolean verifyData(String txt, SearchEngine.Type type) {
            List<String> res = getResultList();
            PSLogger.info("First searching result: " + res);

            if (txt == null) {
                return res.isEmpty(); // should be empty list
            }
            int i = 0;
            while (res.isEmpty() && !isNoResults()) {
                if (i++ > SEARCH_ATTEMPTS) break;
                SEARCH_TIMEOUT.waitTime();
                setDefaultElement();
                res = getResultList();
                PSLogger.info("Searching result #" + i + ": " + res);
            }
            //todo: check type?
            for (String r : res) {
                if (txt.equalsIgnoreCase(r)) return true;
            }
            return false;
        }

        @Override
        public List<String> getResultList() {
            List<String> res = new ArrayList<String>();
            for (Result r : getResults()) {
                res.addAll(r.values());
            }
            return res;
        }

        @Override
        public List<Result> getResults() {
            List<Result> res = new ArrayList<Result>();
            for (Element tr : Element.searchElementsByXpath(this, QuickSearchResultLocators.ROW)) {
                if (!tr.isDEVisible()) continue;
                Element th = tr.getChildByXpath(QuickSearchResultLocators.TH);
                if (!th.isDEPresent()) continue;
                String k = th.getDEText();
                if (k.isEmpty()) continue;
                for (Element li : Element.searchElementsByXpath(tr, QuickSearchResultLocators.LI)) {
                    String html = li.getInnerText().replace("</b><b>", " ").replaceAll("<[^>]+>", "");
                    String v = StrUtil.trim(html.replace(", ", ",").replace(",", ", "));
                    Result r = new Result();
                    r.put(k, v);
                    res.add(r);
                }
            }
            return res;
        }
    }

    /**
     * Class to test Quick Search popup
     * <p/>
     * Date: 29/08/11
     *
     * @author karina
     */
    public class QuickSearchDialog extends Dialog implements SearchEngine {

        /**
         * The constructors
         *
         * @param link
         */
        protected QuickSearchDialog() {
            super(QUICK_SEARCH_LINK);
            setPopup(getLocator());
        }


        /**
         * Sets a text to the dialog
         *
         * @param txt
         */
        public void setSearchText(String txt) {
            new Input(QUICK_SEARCH_FIELD).type(txt);
        }

        /**
         * Clicks the dialog Search button
         */
        public void clickSearchButton() {
            new Button(QUICK_SEARCH_SEARCH_BUTTON).submit();
        }

        /**
         * Clicks the dialog Cancel button
         */
        public void clickCancelButton() {
            new Button(QUICK_SEARCH_CANCEL_BUTTON).submit();
        }

        /**
         * Clicks the Advanced Search link
         */
        public void clickAdvancedLink() {
            getAdvancedLink().click(false);
        }

        public Link getAdvancedLink() {
            return new Link(ADVANCED_SEARCH_LINK);
        }


        /**
         * Selects Projects Category
         */
        public void selectProjectsCategory() {
            selectCategory(CATEGORY_TYPE_SELECT_PROJECTS_OPTION);
        }

        /**
         * Select a category given category locator
         *
         * @param ILocatorable - the category locator
         */
        private void selectCategory(ILocatorable type) {
            selectCategory(type.getLocator());
        }

        /**
         * Select a category given category text
         *
         * @param String - the text
         */
        private void selectCategory(String type) {
            if (getAppVersion().verLessThan(PowerSteeringVersions._9_2)) {
                PSSkipException.skip(getName() + " : this functionality is not supported for " + getAppVersion());
            }
            new SelectInput(CATEGORY_TYPE_SELECT).select(CATEGORY_TYPE_SELECT_LABEL.append(type));
        }

        /**
         * Makes a search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchAll(String txt) {
            setSearchText(txt);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        /**
         * Makes projects search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchProjects(String txt) {
            setSearchText(txt);
            selectCategory(CATEGORY_TYPE_SELECT_PROJECTS_OPTION);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        /**
         * Makes people search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchPeople(String txt) {
            setSearchText(txt);
            selectCategory(CATEGORY_TYPE_SELECT_PEOPLE_OPTION);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        /**
         * Makes ideas search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchIdeas(String txt) {
            setSearchText(txt);
            selectCategory(CATEGORY_TYPE_SELECT_IDEAS_OPTION);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        /**
         * Makes issues search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchIssues(String txt) {
            setSearchText(txt);
            selectCategory(CATEGORY_TYPE_SELECT_ISSUES_OPTION);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        /**
         * Makes documents search
         *
         * @param txt - the word to search
         * @return a SimpleSearchPage with results
         */
        public SimpleSearchPage makeSearchDocuments(String txt) {
            setSearchText(txt);
            selectCategory(CATEGORY_TYPE_SELECT_DOCUMENTS_OPTION);
            clickSearchButton();
            return new SimpleSearchPage();
        }

        public SearchResult makeSearch(String txt, Type type) {
            switch (type) {
                case ALL:
                    return makeSearchAll(txt);
                case PROJECTS:
                    return makeSearchProjects(txt);
                case PEOPLE:
                    return makeSearchPeople(txt);
                case IDEAS:
                    return makeSearchIdeas(txt);
                case ISSUES:
                    return makeSearchIssues(txt);
                case DOCUMENTS:
                    return makeSearchDocuments(txt);
            }
            return null;
        }
    }

    public class UserMenu110 extends Element implements LinkMenu {
        private Element popup;
        private List<Link> links;

        private UserMenu110() {
            super(USER_NAME_LINK_110);
            popup = new Element(UserNameMenu110.POPUP);
        }

        @Override
        public void open() {
            click(false);
            popup.waitForVisible(MENU_TIMEOUT);
            if (links == null) {
                popup.setDefaultElement(getDocument());
                links = new ArrayList<Link>();
                for (Element e : Element.searchElementsByXpath(popup, UserNameMenu110.ITEM_LINK)) {
                    Link l = new Link(e) {
                        public void click(boolean w) {
                            if (!w) {
                                super.click(w); // ?
                                return;
                            }
                            //open by hred
                            openByHref();
                        }
                    };
                    String txt = l.getDEText();
                    if (txt.isEmpty()) continue;
                    l.setName(txt);
                    links.add(l);
                }
            }
        }

        @Override
        public List<String> getMenuItems() {
            if (links == null) return null;
            List<String> res = new ArrayList<String>();
            for (Link l : links) {
                res.add(l.getName());
            }
            return res;
        }

        @Override
        public Link getMenuLink(String lab) {
            if (links == null) return null;
            for (Link l : links) {
                if (lab.equals(l.getName())) return l;
            }
            return null;
        }

        @Override
        public Link getMenuLink(ILocatorable lab) {
            return getMenuLink(lab.getLocator());
        }
    }

    public void clickBrowseImportExportMetric() {
        clickMenuItem(BROWSE_IMPORT_EXPORT_METRIC_LINK, IMPORT_EXPORT_METRICS);
    }

    public void clickAdminMetricTemplates() {
        clickMenuItem(ADMIN_TEMPLATES_METRICS_LINK, METRIC_TEMPLATES);
    }

    public void clickAdminObjectTypes() {
        clickMenuItem(ADMIN_OBJECT_TYPES_LINK, CONFIGURATION_OBJECT_TYPES);
    }

    private void openMenuFor(MainMenuLocators submenu) {
        PSPage pc = getInstance();
        if (pc instanceof WBSPage) {
            ((WBSPage) pc).getOptions().standardScreen();
        }
        if (submenu.name().startsWith(BROWSE.name())) {
            openBrowse();
        } else if (submenu.name().startsWith(ADMIN.name())) {
            openAdmin();
        }
    }

    private void openBrowse() {
        PSLogger.info("Call 'Browse' menu");
        openMainMenu(BROWSE);
        PSLogger.save("after open 'Browse' menu");
    }

    private void openAdmin() {
        PSLogger.info("Call 'Admin' menu");
        openMainMenu(ADMIN);
        PSLogger.save("after open 'Admin' menu");
    }

    private void clickMenuItem(MainMenuLocators loc10, LeftNavLocators loc11) {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            try {
                openLeftNav(loc11);
            } catch (AssertionError ae) { // for ie9
                PSLogger.error(ae);
                getContainerHeaderElement().click(false);
                openLeftNav(loc11);
            }
        } else {
            clickMenuItem(loc10);
        }
    }

    /**
     * until 10.0
     *
     * @param loc
     */
    private void clickMenuItem(MainMenuLocators loc) {
        PSLogger.info("Go to '" + loc.getName() + "'");
        try {
            if (loc.isAdmin())
                openAdmin();
            else
                openBrowse();
            callLinkAndWait(loc);
        } catch (Wait.WaitTimedOutException we) {
            openMenu(we, loc.isAdmin(), loc);
        } catch (AssertionError ae) {
            openMenu(ae, loc.isAdmin(), loc);
        }
    }

    private void openMenu(Throwable we, boolean isAdmin, MainMenuLocators loc) {
        PSLogger.error("Can't open " + loc.name() + ":" + we);
        PSLogger.save();
        new HomePage().open();
        if (isAdmin)
            openAdmin();
        else
            openBrowse();
        callLinkAndWait(loc);
    }

    private void callLinkAndWait(MainMenuLocators loc) {
        Link link = new Link(loc);
        link.setResultPage(this);
        link.waitForVisible(MENU_TIMEOUT); // for chrome.
        if (getDriver().getType().isGoogleChrome()) { // for debug:
            new TimerWaiter(1500).waitTime();
            link.setDefaultElement(getDocument());
        }
        link.clickAndWaitNextPage();

        //Page.initJsErrorChecker();
        closeAllMenus();
        PSLogger.save("after opening page");
    }

    public void closeAllMenus() {
        Element popup = getTopMenuPopup();
        PSLogger.info("Close any top menus");
        if (popup == null) return;
        popup.mouseOut();
        getEmptyInstance().getTopElement().click(false);
        popup.waitForUnvisible();
    }

    private Element getTopMenuPopup() {
        for (String loc : new String[]{ADMIN_MENU.getLocator(), BROWSE_MENU.getLocator()}) {
            Element popup = new Element(loc);
            if (popup.exists() && popup.isVisible()) return popup;
        }
        return null;
    }


    private void openMainMenu(MainMenuLocators menu) {
        _openMenu(menu, true);
    }

    private void _openMenu(MainMenuLocators menu, boolean tryAgain) {
        Link topLink = new Link(menu);
        Wait.WaitTimedOutException w = null;
        for (int i = 0; i < (tryAgain ? MENU_ATTEMPT_NUM : 1); i++) {
            topLink.waitForVisible();
            topLink.click(false);
            try {
                waitMenu(menu);
                return;
            } catch (Wait.WaitTimedOutException e) {
                w = e;
                if (tryAgain) {
                    PSLogger.warn(e);
                    PSLogger.warn("Can't open menu, try again after refresh");
                    refresh();
                    PSLogger.save();
                }
            }
        }
        if (w != null) throw w;
    }

    private void waitMenu(MainMenuLocators menu) {
        Element popup = new Element(menu.getBody());
        popup.waitForVisible(MENU_TIMEOUT);
        if (!CoreProperties.getBrowser().isIE()) {
            try {
                popup.waitForAttribute(MENU_STYLE, MENU_STYLE_OPACITY, MENU_TIMEOUT);
            } catch (Wait.WaitTimedOutException w) { // have exception for chrome, 2.3.0, web-driver.
                PSLogger.warn(getName() + ".waitMenu: " + w.getMessage());
            }
        } else {
            new TimerWaiter(500).waitTime();
        }
    }

    public HomePage openHome() {
        Link link = new Link(HOME_LINK);
        link.clickAndWaitNextPage();
        return new HomePage();
    }

    public InboxPage openInbox() {
        Link link = new Link(INBOX_LINK);
        link.click(false);
        InboxPage res = new InboxPage();
        res.waitForPageToLoad();
        res.refreshBlankPage();
        return res;
    }

    protected String makeUrl(String id) {
        return makeUrl(getILocatorableUrl(), BasicCommonsLocators.URL_ID, id);
    }

    protected static String makeUrl(ILocatorable URL, String id) {
        return makeUrl(URL, BasicCommonsLocators.URL_ID, id);
    }

    protected static String makeUrl(ILocatorable URL, ILocatorable URL_ID, String id) {
        StringBuilder res = new StringBuilder("");
        String url = SeleniumDriverFactory.isDriverInit() ? SeleniumDriverFactory.getDriver().getBaseUrl() : CoreProperties.getURLServerWithContext();
        res.append(url);
        res.append(URL.getLocator());
        res.append(BasicCommonsLocators.URL_PREFIX.getLocator());
        if (!id.startsWith(URL_ID.getLocator()))
            res.append(URL_ID.getLocator());
        res.append(id);
        return res.toString().replaceAll("([^:])/+", "$1/");
    }

    public String getUrlId() {
        return _getUrlId();
    }

    protected String _getUrlId() {
        return getFirstIdFromUrl(getUrl());
    }

    public static String getFirstIdFromUrl(String url) {
        return url.replaceFirst(BasicCommonsLocators.URL_ID_PREFIX.getLocator(), "").
                replaceAll(BasicCommonsLocators.URL_ID_SUFFIX.getLocator(), "");
    }

    public void refreshBlankPage() {
        if (isCheckBlankPage() && isBlankPage()) {
            PSLogger.error("Blank page!");
            refresh();
        }
    }


    protected void openLeftNav(LeftNavLocators loc) {
        openLeftNav(this, loc);
    }

    protected void openLeftNav(PSPage resultPage, LeftNavLocators loc) {
        PSLogger.info("Go to '" + loc + "'");
        Link link = getLeftNavLink(loc);
        link.setResultPage(resultPage);
        Assert.assertNotNull(link, "Can't find link '" + loc + "'(" + loc.getLocator() + ")");
        link.click(resultPage != null);
    }


    private void openLeftNavLink(LeftNavLocators loc) {
        if (loc == null) return;
        openLeftNavLink(loc.getParent());
        String lOpen = LeftNavMainMenuLocators.getParentOpenLocator(loc);
        Element eOpen = new Element(lOpen);
        String clazz = eOpen.getElementClass();
        if (clazz != null && clazz.contains(MINUS_CLASS.getLocator())) {
            //already open.
            return;
        }
        Link link = new Link(loc);
        Assert.assertTrue(link.exists() && link.isVisible(), "Can't find link " + link);
        link.click(false);
        eOpen.waitForClass(MINUS_CLASS.getLocator());
    }

    protected Link getLeftNavLink(LeftNavLocators loc) {
        try {
            return _getLeftNavLink(loc);
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.error(we.getMessage() + ". Try again.");
            refresh();
            try {
                return _getLeftNavLink(loc);
            } catch (Wait.WaitTimedOutException we2) {
                if (getBrowser().isIE()) throw new PSSkipException("Unable open left nav", we2);
                throw we2;
            }
        }
    }

    private Link _getLeftNavLink(LeftNavLocators loc) {
        Element main = new Element(loc.getStorage());
        main.mouseOver();
        new Element(MENU_BAR).waitForVisible(MENU_TIMEOUT);
        new Element(MENU_TITLE).waitForText(loc.getStorage().getName().toUpperCase(), MENU_TIMEOUT * 2);
        new Element(loc.getStorage().getTitleLoc()).waitForPresent(MENU_TIMEOUT * 2);
        openLeftNavLink(loc.getParent());
        Link link = new Link(loc);
        if (link.exists() && link.isVisible()) return link;
        return null;
    }


    public String getContainerHeader() {
        Element e = getContainerHeaderElement();
        return e != null ? StrUtil.trim(getContainerHeaderElement().getText()) : null;
    }

    public String getContainerShortHeader() {
        return getContainerHeader().replaceAll("\\s+\\|\\s+.*$", "");
    }

    public Element getContainerHeaderElement() {
        if (getAppVersion().verGreaterOrEqual(PowerSteeringVersions._11_0)) {
            Element e = new Element(CONTAINER_HEADER_110);
            return e.exists() ? e : null;
        }
        Element e = new Element(CONTAINER_HEADER_100_1);
        if (e.exists()) return e;
        e = new Element(CONTAINER_HEADER_100_2);
        return e.exists() ? e : null;
    }

    public static Map<String, String> getIdsFromLinkList(List<Link> links, boolean reverse) {
        Map<String, String> res = new HashMap<String, String>();
        for (Link link : links) {
            String id = getFirstIdFromUrl(link.getHref());
            String name = link.getName();
            if (reverse)
                res.put(name, id);
            else
                res.put(id, name);
        }
        return res;
    }

}

