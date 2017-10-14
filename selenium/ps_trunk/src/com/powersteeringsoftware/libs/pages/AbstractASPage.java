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

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.AdvancedSearchPageLocators.*;

/**
 * Class Description
 * <p/>
 * Date: 06/09/11
 *
 * @author karina
 */
public abstract class AbstractASPage extends PSPage {

    private static final int SEARCHING_ATTEMPTS = 15;
    private static final TimerWaiter SEARCHING_STEP_TIMEOUT = new TimerWaiter(5000);

    public AnythingASPage openAnythingTab() {
        openTab("Anything", ANYTHING_ADVANCED_SEARCH_LINK);
        return new AnythingASPage();
    }

    public ProjectsASPage openProjectsTab() {
        openTab("Projects", PROJECTS_ADVANCED_SEARCH_LINK);
        return new ProjectsASPage();
    }

    public PeopleASPage openPeopleTab() {
        openTab("People", PEOPLE_ADVANCED_SEARCH_LINK);
        return new PeopleASPage();
    }

    public IssuesASPage openIssuesTab() {
        openTab("Issues", ISSUES_ADVANCED_SEARCH_LINK);
        return new IssuesASPage();
    }

    public DocumentsASPage openDocumentsTab() {
        openTab("Documents", DOCUMENTS_ADVANCED_SEARCH_LINK);
        return new DocumentsASPage();
    }

    private void openTab(String name, ILocatorable tab) {
        PSLogger.info("Open " + name + " tab");
        Link link = new Link(tab);
        String clazz = link.getParent().getElementClass();
        if (SELECTED_TAB_CLASS.getLocator().equals(clazz)) {
            PSLogger.debug("Tab " + name + " is already open");
            return;
        }
        link.clickAndWaitNextPage();
    }

    /**
     * Make the search
     */
    public void search() {
        search(false);
    }

    public void search(boolean doWait) {
        PSLogger.info("Searching.");
        for (int i = 0; i < (doWait ? SEARCHING_ATTEMPTS : 1); i++) {
            if (doWait)
                PSLogger.info("Searching attempt #" + (i + 1));
            Element nothing = doSearch();
            if (nothing != null) {
                PSLogger.debug(nothing.getDEText());
            } else {
                return;
            }
            if (doWait) {
                expandCollapse(); // open again filters
                SEARCHING_STEP_TIMEOUT.waitTime();
            }
        }
    }

    private Element doSearch() {
        Button searchBut = new Button(SEARCH_BUTTON);
        Assert.assertTrue(searchBut.exists(), "Can't find search button");
        PSLogger.save("Before submitting search.");
        searchBut.submit();
        PSLogger.save("After submit searching");
        return getElement(true, NOTHING_FOUND); // get document hear
    }

    /*public static enum SearchCriteria { ALL(KEYWORD_TYPE_SELECT_ALL_OF_OPTION), ANY(KEYWORD_TYPE_SELECT_ANY_OF_OPTION),
        EXACTLY();
       public SearchCriteria(String keywordSelector) {

       }

       private String getKeywordSelector() {
           return null;
       }
    };

    public void selectSearchCriteria(SearchCriteria criteria) {
        selectKeyword(criteria.getKeywordSelector());
    }    */

    /**
     * Select Any of keyword in dropdownlist
     */
    public void selectAnyOfKeyword() {
        selectKeyword(KEYWORD_TYPE_SELECT_ANY_OF_OPTION);
    }

    /**
     * Select All of keyword in dropdownlist
     */
    public void selectAllOfKeyword() {
        selectKeyword(KEYWORD_TYPE_SELECT_ALL_OF_OPTION);
    }

    /**
     * Select Exactly keyword in dropdownlist
     */
    public void selectExactlyKeyword() {
        selectKeyword(KEYWORD_TYPE_SELECT_EXACTLY_OPTION);
    }

    /**
     * Select a keyword given a Locator
     *
     * @param ILocatorable
     */
    public void selectKeyword(ILocatorable type) {
        selectKeyword(type.getLocator());
    }

    /**
     * Select a keyword given the text
     *
     * @param String - the input
     */
    public void selectKeyword(String type) {
        SelectInput select = new SelectInput(KEYWORD_TYPE_SELECT);
        if (select.exists()) {
            select.select(KEYWORD_TYPE_SELECT_LABEL.append(type));
            return;
        }
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._14)) {
            throw new PSKnownIssueException("PS-4026", "Can't find selector All/Any/Exactly");
        }
        Assert.fail("Can't find selector All/Any/Exactly");
    }

    /**
     * Set a text into keyword textbox
     *
     * @param txt - the text
     */
    public void setKeyword(String txt) {
        PSLogger.info("Set keywords '" + txt + "'");
        Input in = new Input(KEYWORD_INPUT_FIELD);
        in.waitForVisible();
        in.type(txt);
        String _txt = in.getValue();
        Assert.assertEquals(_txt, txt, "Incorrect keywords value");
    }

    public boolean verifyData(String str) {
        return verifyData(str, false);
    }

    public boolean verifyData(String str, boolean ignoreCase) {
        List<String> res = getResultList();
        PSLogger.debug("Search result is: " + res);
        for (String r : res) {
            if (ignoreCase && r.equalsIgnoreCase(str)) return true;
            if (!ignoreCase && r.equals(str)) return true;
        }
        return false;
    }

    /**
     * Search the text in the result list
     *
     * @param elementLocator the element locator
     * @return the list of elements
     */
    private List<Element> getRows(ILocatorable elementLocator) {
        Element table = Element.searchElementByXpath(getDocument(false), TABLE); // do not get document if exist
        List<Element> res = new ArrayList<Element>();
        if (table != null) {
            for (Element row : Element.searchElementsByXpath(table, TABLE_ROW)) {
                Element cell = row.getChildByXpath(elementLocator);
                if (cell.isDEPresent() && !cell.getDEText().isEmpty()) {
                    res.add(row);
                }
            }
        }
        return res;
    }

    protected List<String> getResultList(ILocatorable name) {
        List<String> res = new ArrayList<String>();
        for (SearchResult.Result r : getResults()) {
            res.add(r.get(name.getLocator()));
        }
        return res;
    }

    public List<String> getResultList() {
        return getResultList(GENERAL_NAME_COLUMN);
    }

    public List<SearchResult.Result> getResults() {
        List<SearchResult.Result> res = new ArrayList<SearchResult.Result>();
        Element table = Element.searchElementByXpath(getDocument(false), TABLE); // do not get document if exist
        if (table == null) return res;
        List<String> ths = new ArrayList<String>();
        List<String> tds = new ArrayList<String>();
        for (Element th : Element.searchElementsByXpath(table, TABLE_ROW_TH)) {
            String txt = getText(th);
            if (txt != null) ths.add(txt);
        }
        for (Element td : Element.searchElementsByXpath(table, TABLE_ROW_TD)) {
            tds.add(getText(td));
        }

        while (tds.size() > 0) {
            SearchResult.Result r = new SearchResult.Result();
            res.add(r);
            for (String th : ths) {
                r.put(th, tds.remove(0));
            }
        }
        return res;
    }

    private static String getText(Element td) {
        String res = td.getDEText();
        if (!res.isEmpty()) return res;
        for (ILocatorable tag : new ILocatorable[]{TABLE_TD_DIV, TABLE_TD_A}) {
            for (Element a : Element.searchElementsByXpath(td, tag)) {
                res = a.getDEText();
                if (!res.isEmpty()) return res;
            }
        }
        return null;
    }


    public void clickClearLink() {
        PSLogger.info("Click Clear link");
        new Link(ADVANCED_SEARCH_CLEAR_LINK).clickAndWaitNextPage();
    }

    public void expandCollapse() {
        PSLogger.debug("Expand/collapse filters");
        new Link(EXPAND_COLLAPSE_FILTERS).click(false);
    }

}

