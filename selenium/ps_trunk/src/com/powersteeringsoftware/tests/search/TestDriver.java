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
package com.powersteeringsoftware.tests.search;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.SearchEngine;
import com.powersteeringsoftware.libs.elements.SearchResult;
import com.powersteeringsoftware.libs.elements.UserDialog;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * Class to run Search tests
 * <p/>
 * Date: 12/08/11
 *
 * @author karina
 */
public class TestDriver extends PSTestDriver {

    public static final String SEARCH_GROUP = "search.test";
    public static final String DOCUMENTS_GROUP = "document.test";
    public static final String BEFORE = "prepare";

    /**
     * Advanced Search Tests - Create Issue
     */
    private void createIssue(String subject, IssueAddPage add) {
        add.setSubject(subject);
        add.setDescription("Issue message");
        add.submit();
    }

    /**
     * Advanced Search Tests - Create Issues
     */
    @Test(description = "Create Issues", groups = {TestSettings.PS_91_GROUP, BEFORE})
    public void testCreateIssuesAndReindex() {
        PSLogger.info("Create issues");
        Work w = getTestData().getRootWork();

        SummaryWorkPage summary = WorkManager.open(w);
        IssuesPage issues = summary.openIssues();

        Set<TestData.Data> searchIssuesValues = getTestData().getSearchValues("words-to-search-Issues-Tab");
        for (TestData.Data issueName : searchIssuesValues) {
            IssueAddPage add = issues.pushAddNew();
            createIssue(issueName.getValue(), add);
            issues = issues.openIssues();
        }
        // todo: remove it
        PSLogger.info("Check users and reindex");
        PeopleManageActivePage page1 = new PeopleManageActivePage();
        page1.open();
        page1.search();
        AgentsPage page2 = new AgentsPage();
        page2.open();
        page2.reindexBasicSearch();
    }

    private DocumentListingPage addDocument(FileAttachment fA, SummaryWorkPage page) {
        DocumentListingPage documents = page.openDocuments();
        addAttachment(fA, documents);
        return documents;
    }

    private void addAttachment(FileAttachment fA, DocumentListingPage page) {
        page.addAttachment(fA.getTitle(), fA.getPath(), fA.getDescription());
        Link title = page.getDocumentTitle(fA.getTitle());
        Assert.assertNotNull(title, "Can't find doc " + fA.getTitle() + " after attaching");
    }

    @Test(description = "Create Documents", groups = {TestSettings.FIREFOX_ONLY_GROUP, BEFORE})
    public void testCreateDocuments() {
        PSLogger.info("Create documents");
        Work w = getTestData().getRootWork();
        SummaryWorkPage summary = WorkManager.open(w);
        Set<TestData.Data> searchDocumentsValues = getTestData().getSearchValues("words-to-search-Documents-Tab");
        for (TestData.Data docName : searchDocumentsValues) {
            FileAttachment fA1 = FileAttachment.getFile(getTestData().getDocUrl(), docName.getValue());
            fA1.setDescription(docName + " search document");
            addDocument(fA1, summary);
        }
        AgentsPage page = new AgentsPage();
        page.open();
        page.reindexDocuments();
    }

    /**
     * Quick Search Tests - Search All
     */
    @Test(description = "Quick search test (All)", groups = TestSettings.PS_91_GROUP)
    public void testQuickSearchAll() {
        testQuickSearch(getTestData().getSearchValues("words-to-search-all"), SearchEngine.Type.ALL);
    }

    /**
     * Quick Search Tests - Search Projects
     */
    @Test(description = "Quick search test (Projects)", groups = TestSettings.PS_92_GROUP)
    public void testQuickSearchProjects() {
        testQuickSearch(getTestData().getSearchValues("words-to-search-projects"), SearchEngine.Type.PROJECTS);
    }

    /**
     * Quick Search Tests - Search People
     */
    @Test(description = "Quick search test (People)", groups = TestSettings.PS_92_GROUP, dependsOnMethods = "testCreateIssuesAndReindex")
    public void testQuickSearchPeople() {
        testQuickSearch(getTestData().getSearchValues("words-to-search-people"), SearchEngine.Type.PEOPLE);
    }

    /**
     * Quick Search Tests - Search Ideas
     */
    @Test(description = "Quick search test (Ideas)", groups = TestSettings.PS_92_GROUP)
    public void testQuickSearchIdeas() {
        testQuickSearch(getTestData().getIdeasSearchValues(), SearchEngine.Type.IDEAS);
    }

    /**
     * Quick Search Tests - Search Issues
     */
    @Test(description = "Quick search test (Issues)", groups = {TestSettings.PS_92_GROUP, SEARCH_GROUP}, dependsOnMethods = "testCreateIssuesAndReindex")
    public void testQuickSearchIssues() {
        testQuickSearch(getTestData().getSearchValues("words-to-search-Issues-Tab"), SearchEngine.Type.ISSUES);
    }

    /**
     * Quick Search Tests - Search Documents
     */
    @Test(description = "Quick search test (Documents)", groups = {TestSettings.FIREFOX_ONLY_GROUP, DOCUMENTS_GROUP}, dependsOnMethods = "testCreateDocuments")
    public void testQuickSearchDocuments() {
        testQuickSearch(getTestData().getSearchValues("words-to-search-Documents-Tab"), SearchEngine.Type.DOCUMENTS);
    }

    private void testQuickSearch(Set<TestData.Data> searchAllValues, SearchEngine.Type type) {
        PSLogger.info("Search type " + type);
        PSPage page = PSPage.getEmptyInstance();
        page.refresh();
        // Get the set of test data for Search All dropdownlist value
        PSLogger.info("Search text : " + searchAllValues);
        // For each test data make a search and verify results
        for (TestData.Data value : searchAllValues) {
            _testQuickSearch(page, value, type);
        }
    }

    private void _testQuickSearch(PSPage page, TestData.Data value, SearchEngine.Type type) {
        AssertionError ae = null; //TODO: check and move to more suitable place
        for (int i = 0; i < PSPage.SEARCH_ATTEMPTS; i++) {
            try {
                __testQuickSearch(page, value, type);
                return;
            } catch (AssertionError _ae) {
                PSLogger.error(_ae);
                PSLogger.info("Try again, iteration number #" + (i + 1));
                SearchEngine quickSearch = page.openQuickSearch();
                if (quickSearch instanceof PSPage.QuickSearchInput) {
                    ((PSPage.QuickSearchInput) quickSearch).clear();
                }
                PSPage.SEARCH_TIMEOUT.waitTime();
                PSLogger.save((i + 1));
            }
        }
        if (ae != null) throw ae;
    }

    private void __testQuickSearch(PSPage page, TestData.Data value, SearchEngine.Type type) {
        SearchEngine quickSearch = page.openQuickSearch();
        SearchResult res = quickSearch.makeSearch(value.getValue(), type);
        // Verify Expected results
        Assert.assertTrue(res.verifyData(value.shouldBeFound() ? value.getValue() : null, type), "Result not found for value '" + value + "'");
    }

    /**
     * Advanced Search Tests - Search in Anything tab
     */
    @Test(description = "Advanced search test (Anything)", groups = TestSettings.PS_91_GROUP)
    public void testAdvancedSearchAnything() {

        // Create and open Advanced Search page
        ProjectsASPage projectASPage = new ProjectsASPage();
        projectASPage.open();
        // Change Advanced Search menu to Anything
        AnythingASPage aasp = projectASPage.openAnythingTab();

        Set<TestData.Data> searchAllValues = getTestData().getSearchValues("words-to-search-Anything-Tab");

        // Search with Any Of keyword
        // For each test data make a search and verify results
        for (TestData.Data value : searchAllValues) {
            aasp.selectAnyOfKeyword();
            aasp.setKeyword(value.getValue());
            aasp.search();
            // Verify Expected results
            Assert.assertTrue(aasp.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            aasp.clickClearLink();
        }

        // Search with All Of keyword
        for (TestData.Data value : searchAllValues) {
            aasp.selectAllOfKeyword();
            aasp.setKeyword(value.getValue());
            aasp.search();
            // Verify Expected results
            Assert.assertTrue(aasp.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            aasp.clickClearLink();
        }

        // Search with Exactly keyword
        for (TestData.Data value : searchAllValues) {
            aasp.selectExactlyKeyword();
            aasp.setKeyword(value.getValue());
            aasp.search();
            // Verify Expected results
            Assert.assertTrue(aasp.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            aasp.clickClearLink();
        }

    }

    /**
     * Advanced Search Tests - Search in Project tab
     */
    @Test(description = "Advanced search test (Project)", groups = TestSettings.PS_91_GROUP)
    public void testAdvancedSearchProject() {
        // Create and open Advanced Search page
        ProjectsASPage projectASPage = new ProjectsASPage();
        projectASPage.open();

        Set<TestData.Data> searchProjectsValues = getTestData().getSearchValues("words-to-search-Projects-Tab");
        Set<TestData.Data> descendedFromValues = getTestData().getSearchValues("descended-from");
        User projectOwner = BasicCommons.getCurrentUser();
        User falseProjectOwner = getTestData().getUser("user1");

        // Verify that search is not case-sensitive.
        for (TestData.Data value : searchProjectsValues) {
            projectASPage.selectAllOfKeyword();
            projectASPage.setKeyword(value.getValue().toUpperCase());
            // Select descended from
            projectASPage.selectDescendedFrom(descendedFromValues.iterator().next().getValue(), false);

            // Do Search
            projectASPage.search();

            // Verify Expected results
            Assert.assertTrue(projectASPage.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            projectASPage.clickClearLink();
        }

        // Search project with owner
        // Check that something found by project and 'Admin, Admin' (it is default owner)
        String projName = searchProjectsValues.iterator().next().getValue().toUpperCase();
        projectASPage.selectAllOfKeyword();
        projectASPage.setKeyword(projName);
        // Select owner
        UserDialog od = projectASPage.getOwnerDialog();
        od.open();
        od.search(projectOwner);
        od.selectResult(projectOwner);
        projectASPage.search();
        // Verify Expected results
        Assert.assertTrue(projectASPage.verifyData(projName), "Result not found for value '" + projName + "'");
        projectASPage.clickClearLink();

        // Search project with owner
        // Check that nothing found by project and 'test, test' as owner
        projName = searchProjectsValues.iterator().next().getValue().toUpperCase();
        projectASPage.selectAllOfKeyword();
        projectASPage.setKeyword(projName);
        // Select owner
        od = projectASPage.getOwnerDialog();
        od.open();
        od.search(falseProjectOwner);
        od.selectResult(falseProjectOwner);
        projectASPage.search();
        // Verify Expected results
        Assert.assertFalse(projectASPage.verifyData(projName), "Result found for value '" + projName + "'");
        projectASPage.clickClearLink();

        searchProjectsValues = getTestData().getSearchValues("words-to-search-Projects-Tab-Exactly");
        // For each test data make a search and verify results
        for (TestData.Data value : searchProjectsValues) {
            projectASPage.selectExactlyKeyword();
            projectASPage.setKeyword(value.getValue());
            projectASPage.search();
            // Verify Expected results
            Assert.assertTrue(projectASPage.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            projectASPage.clickClearLink();
        }

        // Search project with contributor
        projName = searchProjectsValues.iterator().next().getValue().toUpperCase();
        projectASPage.selectAllOfKeyword();
        projectASPage.setKeyword(projName);
        // Select contributor
        UserDialog contribDial = projectASPage.getOwnerDialog();
        contribDial.open();
        contribDial.search(projectOwner);
        contribDial.selectResult(projectOwner);
        projectASPage.search();
        // Verify Expected results
        Assert.assertTrue(projectASPage.verifyData(projName), "Result not found for value '" + projName + "'");
        projectASPage.clickClearLink();

        // Search with part of the project name
        searchProjectsValues = getTestData().getSearchValues("words-to-search-Projects-Tab-Any-Of");
        String anyOfKeyword = getTestData().getSearchValues("words-to-search-Projects-Tab-Any-Of-search").iterator().next().getValue();
        projectASPage.selectAnyOfKeyword();
        projectASPage.setKeyword(anyOfKeyword);
        projectASPage.search();
        // For each posible result data verify results
        for (TestData.Data value : searchProjectsValues) {
            // Verify Expected results
            Assert.assertTrue(projectASPage.verifyData(value.getValue()), "Result not found for value '" + value + "'");
        }
        projectASPage.clickClearLink();
    }

    /**
     * Advanced Search Tests - Search in People tab
     */
    @Test(description = "Advanced search test (People)", groups = TestSettings.PS_91_GROUP, dependsOnMethods = "testCreateIssuesAndReindex")
    public void testAdvancedSearchPeople() {
        // Create and open Advanced Search page
        ProjectsASPage projectASPage = new ProjectsASPage();
        projectASPage.open();

        PeopleASPage peopleASPage = projectASPage.openPeopleTab();

        String peopleName = BasicCommons.getCurrentUser().getLastName() + ", " + BasicCommons.getCurrentUser().getFirstName();
        peopleASPage.selectAnyOfKeyword();
        peopleASPage.setKeyword(peopleName);
        peopleASPage.search();
        // Verify Expected results
        Assert.assertTrue(peopleASPage.verifyData(peopleName), "Result not found for value '" + peopleName + "'");
        peopleASPage.clickClearLink();

        User user2 = getTestData().getUser("user2");
        peopleName = user2.getFormatFullName();
        peopleASPage.selectExactlyKeyword();
        peopleASPage.setKeyword(peopleName);
        peopleASPage.search();
        // Verify Expected results
        Assert.assertTrue(peopleASPage.verifyData(peopleName), "Result not found for value '" + peopleName + "'");
        peopleASPage.clickClearLink();
    }

    /**
     * Advanced Search Tests - Search in Issues tab
     */
    @Test(description = "Advanced search test (Issues)", groups = {TestSettings.PS_91_GROUP, SEARCH_GROUP}, dependsOnMethods = "testCreateIssuesAndReindex")
    public void testAdvancedSearchIssues() {

        // Create and open Advanced Search page
        ProjectsASPage projectASPage = new ProjectsASPage();
        projectASPage.open();

        IssuesASPage issuesASPage = projectASPage.openIssuesTab();
        Set<TestData.Data> searchIssuesValues = getTestData().getSearchValues("words-to-search-Issues-Tab");

        // For each test data make a search and verify results
        for (TestData.Data value : searchIssuesValues) {
            issuesASPage.selectExactlyKeyword();
            issuesASPage.setKeyword(value.getValue());
            issuesASPage.search(true); // do check in cycle hear.
            // Verify Expected results
            Assert.assertTrue(issuesASPage.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            issuesASPage.clickClearLink();
        }
    }



    /**
     * Advanced Search Tests - Search in Documents tab
     */
    @Test(description = "Advanced search test (Documents)", groups = {DOCUMENTS_GROUP, TestSettings.FIREFOX_ONLY_GROUP}, dependsOnMethods = "testCreateDocuments")
    public void testAdvancedSearchDocuments() {
        // Create and open Advanced Search page
        ProjectsASPage projectASPage = new ProjectsASPage();
        projectASPage.open();

        DocumentsASPage documentsASPage = projectASPage.openDocumentsTab();
        Set<TestData.Data> searchDocumentsValues = getTestData().getSearchValues("words-to-search-Documents-Tab");

        // For each test data make a search and verify results
        for (TestData.Data value : searchDocumentsValues) {
            documentsASPage.selectExactlyKeyword();
            documentsASPage.setKeyword(value.getValue());
            documentsASPage.search(true);
            // Verify Expected results
            Assert.assertTrue(documentsASPage.verifyData(value.getValue()), "Result not found for value '" + value + "'");
            documentsASPage.clickClearLink();
        }

    }


    private TestData data;

    @Override
    public TestData getTestData() {
        return data == null ? (data = new TestData()) : data;
    }
}
