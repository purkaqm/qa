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
package com.powersteeringsoftware.tests.reports;

import com.powersteeringsoftware.libs.elements.rw.RWColumnFilterControl;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators.RWTab;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.rw.RWChart;
import com.powersteeringsoftware.libs.objects.rw.RWChart.DataSeries;
import com.powersteeringsoftware.libs.objects.rw.RWColumn;
import com.powersteeringsoftware.libs.objects.rw.RWColumnGroup;
import com.powersteeringsoftware.libs.pages.ReportFiltersPage;
import com.powersteeringsoftware.libs.pages.ReportFolderPage;
import com.powersteeringsoftware.libs.pages.ReportWindow;
import com.powersteeringsoftware.libs.pages.rw.*;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class to run Search tests
 * <p/>
 * Date: 05/01/12
 *
 * @author igor & karina
 */
public class TestDriver {

    private static final String GENERAL_GROUP = "reports.test";
    private static final String MY_REPORTS_GROUP = "reports.test.my";
    private static final String FOLDER_GROUP = "reports.test.folder";

    @BeforeTest
    public void preparingToTest() throws IOException {
        CoreProperties.loadProperties();
        BasicCommons.logIn();
    }

    @Test(description = "Report folder creation test", groups = {GENERAL_GROUP, FOLDER_GROUP, TestSettings.PS_92_GROUP})
    public void testCreateReportFolder() {
        TestData.Folder folder = TestData.getFolder();
        ReportFolderPage rfp = new ReportFolderPage(folder.isMy());
        rfp.open();
        if (rfp.isFolderExist(folder.getName())) {
            rfp.removeFolder(folder.getName(), true);
        }
        rfp.createNewFolder(folder.getName());

        String msg = rfp.getErrorBoxMessage();
        Assert.assertNull(msg, "There are errors: " + msg);
        Assert.assertTrue(rfp.isFolderExist(folder.getName()), "The Folder '" + folder.getName() + "' hasn't been created!");
    }

    /* To run this test should be made infrastructure for creation a report, i.e. projects, metrics and so on. in the DB,
     taking into account the test data in report_folder.xml */
    //@Test(groups = GENERAL_GROUP, description = "Goes through all the RW tabs and sets test data")
    public void testFillingRWTabs() {
        TestData.Tabs data = TestData.getTabs("testFillingRWTabs");
        ReportWizardPage rwPage = ReportWizardPage.getPageInstance();
        rwPage.open();
        ReportWizardPage next = rwPage;
        do {
            getTester(rwPage, data).doTest();
            next = rwPage.doContinue();
        } while (next != null);
    }

    /**
     * depends on previous test-cases which create reports and folder.
     */
    @Test(description = "Click report name options", groups = {GENERAL_GROUP, TestSettings.PS_92_GROUP}, dependsOnGroups = {MY_REPORTS_GROUP, FOLDER_GROUP})
    public void testReportNameClick() {
        // should be created in previous test case in my reports folder:
        List<TestData.Tabs> dataList = TestData.getTabsList("testSaveFromAnyTab");
        if (dataList.size() < 3) return;
        TestData.Tabs dataForCopy = dataList.get(2); // this is for coping test
        TestData.Tabs dataForEdit = dataList.get(1); // this is for editing test

        testReportNameClick(dataForCopy, dataForEdit);
    }

    private void testReportNameClick(TestData.Tabs dataForCopy, TestData.Tabs dataForEdit) {
        ReportFolderPage publicReportsPage = new ReportFolderPage(false);
        publicReportsPage.open();


        if (ReportWindow.isBroken()) {
            PSLogger.skip("Report Window: Unsupported for this driver now.");
        } else {
            PSLogger.info("Test public reports > Run Option");
            checkReportWindow(publicReportsPage.clickReportNameForRun(null));
        }

        PSLogger.info("Test my reports > 'Edit details and schedule' option");
        ReportFolderPage myReportsPage = publicReportsPage.openMyReports();
        SaveRWPage savePage = myReportsPage.clickReportNameToEdit(dataForEdit.getName());
        TestData.Tabs dataEdited = TestData.getTabs("testReportNameClick1"); // has another location
        // and change location here:
        SaveTabTester tester = (SaveTabTester) getTester(savePage, dataEdited);
        tester.setCheckBanner(TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3));
        tester.doTest();
        publicReportsPage = tester.getRfpPage();
        Assert.assertTrue(publicReportsPage.isPublicReports(), "Incorrect reports page");

        Assert.assertFalse(publicReportsPage.isReportExist(dataForEdit.getName()), "There is " + dataForEdit.getName() + " after editing in public reports");
        myReportsPage = publicReportsPage.openMyReports();
        Assert.assertFalse(myReportsPage.isReportExist(dataForEdit.getName()), "There is " + dataForEdit.getName() + " after editing in my reports");

        PSLogger.info("Test my reports > 'Copy' option");
        savePage = myReportsPage.clickReportNameToCopy(dataForCopy.getName());
        String copiedReportName = savePage.getReportName();
        // Verify copied report name
        Assert.assertEquals(copiedReportName, SaveRWPage.getCopyName(dataForCopy.getName()), "Wrong report copy name");
        TestData.Tabs dataCopied = (TestData.Tabs) dataForCopy.clone();
        dataCopied.getSave().setReportName(copiedReportName);
        tester = (SaveTabTester) getTester(savePage, dataCopied);
        tester.setCheckBanner(TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3));
        tester.doSave();
        tester.validate();
        Assert.assertTrue(myReportsPage.isReportExist(dataForCopy.getName()), "Cannot find initial " + dataForCopy.getName() + " after copying");

        PSLogger.info("Test my reports > 'Edit filters' option");
        ReportFiltersPage filters = myReportsPage.clickReportNameToEditFilters(dataForCopy.getName());
        //filters.clickSave(); // todo ?
        myReportsPage = filters.clickCancel();
        Assert.assertTrue(myReportsPage.isMyReports(), "Incorrect reports page");

        PSLogger.info("Test my reports > 'Report Wizard' option");
        TestData.Tabs dataNew = TestData.getTabs("testReportNameClick2");
        ReportWizardPage rwp = myReportsPage.clickReportNameToGoToRW(dataForCopy.getName());
        savePage = rwp.doContinue().saveAs();
        getTester(savePage, dataNew).doTest();
    }


    @Test(description = "Save report from any tab", groups = {GENERAL_GROUP, MY_REPORTS_GROUP, TestSettings.PS_92_GROUP})
    public void testSaveFromAnyTab() {
        TestData.Tabs data = TestData.getTabs("testSaveFromAnyTab");
        ReportWizardPage rwPage = ReportWizardPage.getPageInstance();
        rwPage.open();
        List<RWTab> tabs = rwPage.getTabs();
        Assert.assertNotEquals(tabs.size(), 0, "Can't find any tab");
        Assert.assertEquals(tabs, Arrays.asList(RWTab.values()), "Incorrect tabs list");
        Assert.assertTrue(rwPage.isActive(), "Tab " + rwPage + " is not active");
        tabs.remove(0); // do not test first tab;

        for (RWTab tb : tabs) {
            PSLogger.info("Test save from " + tb.getTitle());
            ReportWizardPage page = rwPage.goToTab(tb);
            TestData.Tabs tmp = (TestData.Tabs) data.clone();
            String name = tmp.getSave().getReportName() + "(" + tb + ")";
            tmp.setId(data.getId() + tb);
            tmp.getSave().setReportName(name);
            if (!tb.equals(RWTab.Save))
                page = (ReportWizardPage) page.save();
            getTester(page, tmp).doTest();
            if (!tb.equals(RWTab.Save)) {
                rwPage.open(); // reopen report wizard page for next test
            }
        }
    }

    @Test(description = "Run report from any tab", groups = {GENERAL_GROUP, TestSettings.PS_92_GROUP})
    public void testRunOptions() {
        ReportWizardPage rwPage = ReportWizardPage.getPageInstance();
        rwPage.open();
        List<RWTab> tabs = rwPage.getTabs();
        PSLogger.info("Tabs: " + tabs);
        Assert.assertNotEquals(tabs.size(), 0, "Can't find any tab");
        Assert.assertEquals(tabs, Arrays.asList(RWTab.values()), "Incorrect tabs list");

        ReportWizardPage next = rwPage.doContinue();
        Assert.assertNotNull(next, "There is not continue on page");
        while (next != null) {
            PSLogger.info("Test tab '" + next + "'");
            checkReportWindow(next.clickRunAsHtmlOption());
            next = next.doContinue();
        }
    }

    @Test(description = "Save as a report", groups = {GENERAL_GROUP, TestSettings.PS_92_GROUP})
    public void testSaveAsReport() {
        TestData.Tabs data1 = TestData.getTabs("testSaveAsReport1");
        TestData.Tabs data2 = TestData.getTabs("testSaveAsReport2");
        TestData.Tabs data3 = TestData.getTabs("testSaveAsReport3");

        PSLogger.info("Create new report");
        ReportWizardPage rwPage = ReportWizardPage.getPageInstance();
        rwPage.open();

        ReportWizardPage next = rwPage.doContinue();
        // currentTab is Save tab
        SaveRWPage savePage = (SaveRWPage) next.save();
        SaveTabTester tester = (SaveTabTester) getTester(savePage, data1);
        tester.doTest();

        rwPage = tester.getRfpPage().clickEditLastReport();
        next = rwPage.doContinue();

        // Click save as button
        savePage = next.saveAs();
        String actualName = savePage.getReportName();
        // Verify copied report name
        Assert.assertEquals(actualName, SaveRWPage.getFirstEditedName(data1.getSave().getReportName()), "Wrong report name after first edit");

        // Save the report
        tester = (SaveTabTester) getTester(savePage, data2);
        tester.doTest();

        // Test create another report link from Banner
        next = tester.getRfpPage().clickCreateAnotherReport();
        savePage = (SaveRWPage) next.doContinue().save();
        tester = (SaveTabTester) getTester(savePage, data3);
        tester.doTest();
    }

    private void checkReportWindow(ReportWindow w) {
        if (ReportWindow.isBroken()) {
            PSLogger.skip("Report Window: Unsupported for this driver now");
            return;
        }
        if (w == null) return;
        try {
            String err = w.getErrorBoxMessage();
            if (err != null) {
                PSLogger.warn(err);
            } else {
                String info = w.getContentAsString();
                Assert.assertFalse(info.isEmpty(), "Empty window");
                PSLogger.info(info);
            }
        } finally {
            w.deselect();
        }
    }

    private TabTester getTester(ReportWizardPage page, TestData.Tabs data) {
        switch (page.getPageRWType()) {
            case Type:
                return new TypeTabTester(page, data);
            case Definition:
                return new DefinitionTabTester(page, data);
            case Columns:
                return new ColumnsTabTester(page, data);
            case Filter:
                return new FilterTabTester(page, data);
            case GroupSort:
                return new SortTabTester(page, data);
            case Summary:
                return new SummaryTabTester(page, data);
            case Layout:
                return new LayoutTabTester(page, data);
            case Chart:
                return new ChartTabTester(page, data);
            case Save:
                return new SaveTabTester(page, data);
        }
        return null;
    }

} // class TestDriver 


//================ TabTester =========================
abstract class TabTester {
    protected ReportWizardPage rwPage;
    protected TestData.Tabs data;

    protected abstract void doSet();

    public void validate() {
    }

    public void doTest() {
        if (!data.hasTab(rwPage.getPageRWType().name())) {
            PSLogger.debug("Has not data in config for test page " + rwPage);
            return;
        }
        PSLogger.info("Test tab " + rwPage);
        doSet();
        validate();
    }

    TabTester(ReportWizardPage rwPage, TestData.Tabs data) {
        this.rwPage = rwPage;
        this.data = data;
    }

    TabTester(RWTab pageType, TestData.Tabs data) {
        this(ReportWizardPage.getPageInstance(pageType), data);
    }

    public ReportWizardPage getRWPage() {
        return this.rwPage;
    }

    public String getNameSuffix() {
        return CoreProperties.getTestTemplate() + " (" + getClass().getSimpleName() + "@" + toString().replaceAll(".*@", "") + ")";
    }

}// class TabTester


//================ TypeTabTester =========================
class TypeTabTester extends TabTester {
    TypeTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        int categoryAmount = getTypeRWPage().getCategoryAmount();
        for (int i = 1; i <= categoryAmount; i++) {
            getTypeRWPage().setCategoryByIndex(i);
            int typeAmount = getTypeRWPage().getTypeAmount();
            for (int j = 1; j <= typeAmount; j++) {
                getTypeRWPage().setTypeByIndex(j);
                //Assert.assertTrue(getTypeTab().isExampleChanged(), "Example info left the same after Category had been changed");
                //Assert.assertTrue(getTypeTab().isDescriptionChanged(), "Description info left the same after Category had been changed");
                getTypeRWPage().saveState();
            }
        }
        setDefaultValues();
    }

    private TypeRWPage getTypeRWPage() {
        return (TypeRWPage) getRWPage();
    }

    private void setDefaultValues() {
        getTypeRWPage().setCategoryByName(data.getType().getDefaultCategory());
        getTypeRWPage().setTypeByName(data.getType().getDefaultType());
    }

} // class TypeTabTester


//================ DefinitionTabTester =========================
class DefinitionTabTester extends TabTester {

    DefinitionTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        getDefRWPage().setPortfolio(data.getDefinition().getDefaultPortfolio());
        getDefRWPage().setParentFolder(data.getDefinition().getDefaultParentFolder());
        Set<String> workTypes = data.getDefinition().getDefaultWorkTypes();
        for (String wType : workTypes) {
            getDefRWPage().setWorkTypeSelected(wType);
        }
        String filterName = data.getDefinition().getStatusFilter().getFilterName();
        getDefRWPage().addNewFilter(filterName);
        Assert.assertTrue(getDefRWPage().isFilterBlockPresent(filterName), "Filter '" + filterName + "' isn't visible");
        getDefRWPage().setStatusFilter(data.getDefinition().getStatusFilter().getStatus());
        getDefRWPage().setRangeType(data.getDefinition().getStatusFilter().getRangeType());

        filterName = data.getDefinition().getHasMetricFilter().getFilterName();
        getDefRWPage().addNewFilter(filterName);
        Assert.assertTrue(getDefRWPage().isFilterBlockPresent(filterName), "Filter '" + filterName + "' isn't visible");
        //getDefRWPage().setMetricFilter(TestData.DefTabTestData.HasMetricFilterTestData.getMetricName());

        filterName = data.getDefinition().getRelatedWorkFilter().getFilterName();
        //getDefRWPage().addNewFilter(filterName);
        //Assert.assertTrue( getDefRWPage().isFilterBlockPresent(filterName), "Filter '" +  filterName + "' isn't visible");
        //getDefTab().setRelatedWork( TestData.RelatedWorkFilterTestData.getRelatedWorkName()); // not present in the test plan
    }

    private DefinitionRWPage getDefRWPage() {
        return (DefinitionRWPage) getRWPage();
    }

} // class DefinitionTabTester


//================ ColumnsTabTester =========================
class ColumnsTabTester extends TabTester {

    ColumnsTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        Set<RWColumnGroup> allColumnGroups = data.getColumns().getAllGroups();

        Set<RWColumnGroup> projectGroups = data.getColumns().getProjectGroups(allColumnGroups);
        for (RWColumnGroup group : projectGroups) {
            String groupName = group.getName();
            if (!getColsRWPage().isColumnGroupPresent(groupName)) {
                String groupOption = group.getOption();
                getColsRWPage().addProjectColumnsGroup(groupOption);
                Assert.assertTrue(getColsRWPage().isColumnGroupPresent(groupName), "Column group '" + groupName + "' is not present");
            }
            if (!getColsRWPage().isColumnGroupVisible(groupName)) {
                getColsRWPage().openColumnsGroup(groupName);
                Assert.assertTrue(getColsRWPage().isColumnGroupVisible(groupName), "Column group '" + groupName + "' is not visible");
            }
            Set<RWColumn> projGroupCols = group.getColumns();
            //selectCols(projGroupCols);
        }

        RWColumnGroup customColGroup = data.getColumns().getCustomGroup(allColumnGroups);
        if (!getColsRWPage().isCustomColumnGroupVisible()) {
            getColsRWPage().openColumnsGroup(customColGroup.getName());
            Assert.assertTrue(getColsRWPage().isCustomColumnGroupVisible(), "Column group '" + customColGroup.getName() + "' is not visible");
        }

        Set<RWColumn> cols = customColGroup.getColumns();
        for (RWColumn column : cols) {
            String colName = column.getName();
            String colOption = column.getOption();
            String colExpression = column.getParameter("expr");
            getColsRWPage().createCustomColumn(colOption, colName, colExpression);
            Assert.assertTrue(getColsRWPage().isCustomColumnExist(colName), "Custom column '" + colName + "' was not created");
            colExpression = column.getParameter("expr2");
            getColsRWPage().editCustomColumn(colOption, colName, colExpression);
            Assert.assertTrue(getColsRWPage().isCustomColumnExist(colName), "Custom column '" + colName + "' was not changed");
            getColsRWPage().removeCustomColumn(colName);
            Assert.assertFalse(getColsRWPage().isCustomColumnExist(colName), "Custom column '" + colName + "' was not removed");
            colExpression = column.getParameter("expr");
            //getColsRWPage().createCustomColumn(colOption, colName, colExpression);
            //Assert.assertTrue(getColsRWPage().isCustomColumnExist(colName), "Custom column '" + colName + "' was not recreated");
        }
    }


    private void selectCols(Set<RWColumn> cols) {
        for (RWColumn col : cols) {
            getColsRWPage().selectColumn(col.getGroup(), col.getName());
        }
    }

    private ColumnsRWPage getColsRWPage() {
        return (ColumnsRWPage) getRWPage();
    }

} // class ColumnsTabTester


//================ FilterTabTester =========================
class FilterTabTester extends TabTester {

    FilterTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        Set<String> columns = new HashSet<String>(getFilterPage().getColumnsPresented());
        Set<String> columnsMustBePresented = data.getColumns().getFullNamedProjectColumns();
        Assert.assertTrue(columns.size() == columnsMustBePresented.size(),
                "'Filter' tab has different amount of columns than it was selected on the previous step 'Columns'");

        Assert.assertTrue(columnsMustBePresented.containsAll(columns), "'Filter' tab has a column which was not selected on the previous step 'Columns' ");
        getFilterPage().checkAllColumns(null); // get all the previously column checked

        if (!getFilterPage().isAdditionalFiltersVisible()) {
            getFilterPage().openAdditionalFilters();
            Assert.assertTrue(getFilterPage().isAdditionalFiltersVisible(), "Additional filters block is not visible");
        }
        //---------------- BEGIN STEP 1
        Set<RWColumnGroup> columnGroups = data.getFilter().getGroupsOfStep(1);
        doSubTest(columnGroups);
        //---------------- END STEP 1

//		_rwPage.clickPreview(); // TODO !!!

        getFilterPage().clickAddMoreFilters();

        if (!getFilterPage().isAdditionalFiltersVisible()) {
            getFilterPage().openAdditionalFilters();
            Assert.assertTrue(getFilterPage().isAdditionalFiltersVisible(), "Additional filters block is not visible");
        }
        //---------------- BEGIN STEP 2
        columnGroups = data.getFilter().getGroupsOfStep(2);
        doSubTest(columnGroups);
        //---------------- END STEP 2
    }


    private void doSubTest(Set<RWColumnGroup> columnGroups) {
        for (RWColumnGroup group : columnGroups) {
            if (!getFilterPage().isColumnGroupVisible(group.getId())) {
                if (group.isCustomColumnGroup()) continue; // TODO !
                getFilterPage().openColumnGroup(group.getName());
                Assert.assertTrue(getFilterPage().isColumnGroupVisible(group.getId()), "Column group '" + group.getName() + "' is not visible");
            }
            if (group.getAllColumnsSelected()) {
                getFilterPage().checkAllColumns(group.getId());
            } else {
                Set<String> alreadyCheckedColumns = data.getColumns().getFullNamedProjectColumns();
                Set<RWColumn> groupColumns = group.getColumns();
                for (RWColumn column : groupColumns) {
                    if (!alreadyCheckedColumns.contains(column.getFullColumnName())) {
                        getFilterPage().checkColumn(column);
                    }
                }
            }
        }
        getFilterPage().doContinue(); // we should stay on the same tab
        // set filters values up
        for (RWColumnGroup group : columnGroups) {
            Set<RWColumn> filteredColumns = data.getFilter().getFilteredColumns(group);
            setValueToColumnFilters(filteredColumns);
        }
    }


    private void setValueToColumnFilters(Set<RWColumn> columns) {
        for (RWColumn column : columns) {
            String fullColumnName = column.getFullColumnName();
            RWColumnFilterControl columnFilter = getFilterPage().getColumnFilterControl(column);
            Assert.assertTrue(columnFilter != null, "There is no RWColumnFilter instance for column '" + fullColumnName + "'");
            columnFilter.setValue(column.getFilterValue());
        }
    }

    private FilterRWPage getFilterPage() {
        return (FilterRWPage) getRWPage();
    }

} // class FilterTabTester


//================ SortTabTester =========================
class SortTabTester extends TabTester {

    SortTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        Set<RWColumn> groupBy = data.getSort().getGroupByColumns();
        int level = 1;
        for (RWColumn column : groupBy) {
            getSortRWPage().setGroupBy(column, level++);
        }

        Set<RWColumn> additionalSortColumns = data.getSort().getAdditionalColumnSort();
        for (RWColumn column : additionalSortColumns) {
            getSortRWPage().appendAdditionalSort(column);
            Assert.assertTrue(getSortRWPage().isAdditionalSortExist(column), "'Additional sort' column " + column.getFullColumnName() + " is not visible");
            getSortRWPage().setAdditionalSortValue(column);
        }
    }


    private GroupSortRWPage getSortRWPage() {
        return (GroupSortRWPage) getRWPage();
    }

} // class SortTabTester 


//================ SummaryTabTester =========================
class SummaryTabTester extends TabTester {

    SummaryTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        Set<RWColumn> columns = data.getSummary().getColumnToSetSummaryValues();
        for (RWColumn column : columns) {
            Assert.assertTrue(getSummaryRWPage().isSummaryTypeControlExist(column),
                    "Parameter '" + column.getSummaryValue().getTitle() + "' cannot be applied on Summary tab for column '" + column.getFullColumnName() + "'");
            getSummaryRWPage().setColumnSummaryValue(column);
        }
    }


    private SummaryRWPage getSummaryRWPage() {
        return (SummaryRWPage) getRWPage();
    }

} // class SummaryTabTester


//================ LayoutTabTester =========================
class LayoutTabTester extends TabTester {

    LayoutTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        getLayoutRWPage().setPaperSize(data.getLayout().getPaperSize());
    }

    private LayoutRWPage getLayoutRWPage() {
        return (LayoutRWPage) getRWPage();
    }

} // class LayoutTabTester


//================ ChartTabTester =========================
class ChartTabTester extends TabTester {

    ChartTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
    }

    @Override
    protected void doSet() {
        RWChart chart = data.getChart().getChartData();
        getChartRWPage().setChartType(chart.getType());
        getChartRWPage().setAxis(chart.getAxis());

        List<DataSeries> dataSeries = chart.getDataSeries();
        if (chart.getDataSeries().size() > 0) getChartRWPage().setBaseDataSeries(dataSeries.get(0));

        getChartRWPage().setSize(chart.getSize());
    }


    private ChartRWPage getChartRWPage() {
        return (ChartRWPage) getRWPage();
    }

} // class ChartTabTester

//================ SaveTabTester =========================
class SaveTabTester extends TabTester {
    private ReportFolderPage rfpPage;
    private boolean checkBanner;

    SaveTabTester(ReportWizardPage page, TestData.Tabs data) {
        super(page, data);
        checkBanner = true;
    }

    public void setCheckBanner(boolean x) {
        checkBanner = x;
    }

    @Override
    protected void doSet() {
        String repName = data.getSave().getReportName();
        String repDescription = data.getSave().getReportDescription();
        TestData.Folder repLocation = data.getSave().getReportLocation();

        PSLogger.info("Set report name to " + repName);
        getSaveRWPage().setReportName(repName);
        if (repDescription != null) {
            PSLogger.info("Set report description to " + repDescription);
            getSaveRWPage().setReportDesription(repDescription);
        }
        if (repLocation.getName() != null) {
            PSLogger.info("Set report location to " + repLocation.getName());
            getSaveRWPage().setReportLocation(repLocation.getName());
        } else {
            getSaveRWPage().setReportLocation(repLocation.isMy());
        }
        doSave();
    }

    public void doSave() {
        rfpPage = (ReportFolderPage) getSaveRWPage().save();
    }

    @Override
    public void validate() {
        if (rfpPage == null) return;
        TestData.Folder loc = data.getSave().getReportLocation();
        if (loc.isMy()) {
            Assert.assertTrue(rfpPage.isMyReports(), "Incorrect page after submit. Should be my reports");
            rfpPage.setIsMy(true);
        } else {
            Assert.assertTrue(rfpPage.isPublicReports(), "Incorrect page after submit. Should be public reports");
            rfpPage.setIsMy(false);
        }
        if (loc != null && loc.getName() != null) {
            String title = rfpPage.getFolderName();
            Assert.assertEquals(title, loc.getName(), "Incorrect location after submitting '" + data.getName() + "'");
        }
        if (checkBanner) {
            Assert.assertTrue(rfpPage.checkSaveBannerValues(), "Save banner not found for report saved.");
            String savedBannerName = rfpPage.getReportNameFromBanner();
            Assert.assertEquals(savedBannerName, data.getName(), "Incorrect name after saving in banner");
        }
        List<String> reports = rfpPage.getReportsNames();
        PSLogger.debug("All reports: " + reports);
        Assert.assertTrue(reports.contains(data.getName()), "Cannot find report '" + data.getName() + "'");
    }

    private SaveRWPage getSaveRWPage() {
        return (SaveRWPage) getRWPage();
    }

    public ReportFolderPage getRfpPage() {
        return rfpPage;
    }

} // class SaveTabTester 
