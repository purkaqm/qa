package com.powersteeringsoftware.tests.project_central;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.UixFeature;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.PermissionsManager;
import com.powersteeringsoftware.libs.tests.actions.UixManager;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.project_central.actions.*;
import org.testng.annotations.*;

import java.io.IOException;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;

/**
 * Class for running tests on Project Central.
 * See <a href="https://svn.cinteractive.com/cgi-bin/trac.cgi/wiki/AutomatedTesting/FunctionalTesting/AATF/TestSummary/ProjectCentral">wiki</a> for more details.
 * See <a href="https://internal.psteering.com/confluence/display/autotests/Available+Automated+Tests">new wiki</a> for more details (all tests)
 * User: szuev
 * Date: 11.05.2010
 * Time: 12:44:15
 */
public class TestDriver extends PSTestDriver {

    private static final String ADD_NEW_WORK_GROUP = "add-new-subwork";
    private static final String FILTER_GROUP = "filter";
    private static final String FILTER_NAME_GROUP = "filter-name";
    private static final String BULK_EDIT_GROUP = "bulk-edit";
    private static final String BULK_EDIT_CONSTRAINT_GROUP = "bulk-edit-constraint";
    private static final String GANTT_GROUP = "gantt";

    protected static final String MAIN_WORK_GROUP = "main-work";
    private static final String BEFORE_MAIN_BULK_FILTER = "before-main-bulk-filter";
    private static final String MAIN_FILTER = "main-filter";
    private static final String MAIN_BULK = "main-bulk";
    private static final String SECOND_WORK_GROUP = "second-work";
    private static final String FIRST_WORK_GROUP = "first-work";
    private static final String GATED_GROUP_1 = "gated-work";
    private static final String GATED_GROUP_2 = TestSettings.PS_93_GROUP;
    private static final String ONLY_FF = TestSettings.FIREFOX_ONLY_GROUP; // for acceleration under ie
    private static final String TEAM_PANE_GROUP_1 = "team-pane-work";
    private static final String TEAM_PANE_GROUP_2 = TestSettings.PS_91_GROUP;
    private static final String GRID_RESOURCE = "resource";

    private OurAddSubWorkPopupTCs ourPopupActions = new OurAddSubWorkPopupTCs();
    protected OurGridTCs ourGridActions = new OurGridTCs();
    private OurGridBulkOperationsTCs ourGridBulkActions = new OurGridBulkOperationsTCs();
    private MyGridTCs myGridActions = new MyGridTCs();
    private MyAddSubWorkPopupTCs myPopupActions = new MyAddSubWorkPopupTCs();
    private OurOptionsBlockTCs ourOptionsActions = new OurOptionsBlockTCs();
    private MyOptionsBlockTCs myOptionsActions = new MyOptionsBlockTCs();
    private GatedProjectsTCs gatedProjectsActions = new GatedProjectsTCs();
    private TeamPaneTCs teamPaneActions = new TeamPaneTCs();

    protected TestData data;

    @Override
    public TestData getTestData() {
        if (data == null)
            data = new TestData();
        return data;
    }

    @BeforeClass
    public void beforeClass() {
        teamPaneActions.init(getTestData().getThirdMainWork(), getTestData().getAnotherUser(), getTestData().getSecondUser());
    }

    /**
     * prepare tests,
     * load properties,
     * set resource planning,
     * load test data from TestFile\project_central.xml config
     *
     * @param resourcePlanning    - parameter, if true then set resource planing to on, otherwise - off
     * @param immediateDelegation - parameter, if true then set immediate delegation to on, otherwise - off
     * @param resourceManager     - parameter, if true then set resource manager to on, otherwise - off
     * @param teamPane            - parameter, if true then set team pane to on, otherwise - off
     * @throws IOException - Exception
     */
    @BeforeTest(alwaysRun = true, timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    @Parameters({
            "resource_planning",
            "immediate_delegation",
            "resource_manager"})
    public void before(@Optional String resourcePlanning,
                       @Optional String immediateDelegation,
                       @Optional String resourceManager) {
        AuxiliaryTCs.setUixPageSettings(resourcePlanning, immediateDelegation, resourceManager);
        PSLogger.info("All projects in system : " + TestSession.getWorkList());
    }

    void setPermsAndStartNewSession(String permissions) throws IOException {
        PSLogger.task("Set custom permissions");
        User user = getTestData().getAnotherUser();
        if (permissions != null) {
            PermissionsManager.setAll(getTestData().getPermissionsSet(permissions));
        }
        PSLogger.info("Start new session for user " + user);
        BasicCommons.closeAll();
        SeleniumDriverFactory.initNewDriver();
        BasicCommons.logIn(user);
    }

    void closeNewSession() {
        PSLogger.task("Close new session");
        SeleniumDriverFactory.stopLastSeleniumDriver();
    }

    private boolean isThisDriver() {
        return getClass().equals(TestDriver.class);
    }

    /**
     * auxiliary testcase;
     * just create empty test work for testing (2 clicks),
     * disable manual scheduling.
     */
    @Test(testName = "Create Empty Work",
            alwaysRun = true,
            description = "Create empty work and go to Project Central Page",
            groups = {FIRST_WORK_GROUP})
    public void createEmptyWork() {
        AuxiliaryTCs.createWork(getTestData().getFirstMainWork());
    }


    /**
     * for empty parent work create 3 children using 'Add Under' new subwork popup;
     * before submitting change sequence of children using drag'n'drop;
     * children (name, type, constraints):
     * (A_*, Work, As Soon As Possible),
     * (B_*, MSP Project, As Soon As Possible),
     * (C_*, Work, Fixed Dates, {date}, {date + 5 days})
     *
     * @dependsOnMethods createEmptyWork
     */
    @Test(description = "Check 'Add Under' children and drag'and'drop in dialog",
            dependsOnMethods = {"createEmptyWork"},
            groups = {FIRST_WORK_GROUP, ADD_NEW_WORK_GROUP})
    public void addUnderAndDragAndDrop() {
        myPopupActions.addUnderAndDragAndDrop(getTestData().getFirstMainWork(), getTestData().getWork("A"), getTestData().getWork("B"), getTestData().getWork("C"));
    }

    /**
     * for first child (A_*, created by addUnderAndDragAndDrop tc) create 3 children using 'Add After' functionality;
     * works to add (name, type, constraints):
     * (D_*, Folder, Must Start On, {date}, null),
     * (E_*, Milestone, Must Finish On, null, {date}),
     * (F_*, Action Item, Start No Earlier Than, {date}, null),
     *
     * @dependsOnMethods addUnderAndDragAndDrop
     */
    @Test(testName = "Add After", description = "Check 'Add After' for first child in grid",
            dependsOnMethods = {"addUnderAndDragAndDrop"},
            groups = {FIRST_WORK_GROUP, ADD_NEW_WORK_GROUP})
    public void addAfterForFirstChild() {
        myPopupActions.addAfterForFirstChild(getTestData().getFirstMainWork(), getTestData().getWork("A"), getTestData().getWork("D"), getTestData().getWork("E"), getTestData().getWork("F"));
    }

    /**
     * Check drag'n'drop in grid.
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Check drag'n'drop in grid.",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP})
    public void checkDragAndDropInGrid() {
        myGridActions.dragAndDrop(getTestData().getFirstMainWork());
    }

    /**
     * validate that critical pathes are correct for all works (A,B,C,D,E,F and parent);
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Validate critical paths functionality for created before structure",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP})
    public void validateCriticalPaths() {
        myGridActions.validateCriticalPaths(getTestData().getFirstMainWork());
    }

    /**
     * validate that gantt is correct
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Compare gantt and scheduled dates",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, GANTT_GROUP})
    public void validateGanttDates() {
        myGridActions.validateGantt(getTestData().getFirstMainWork());
    }

    @Test(description = "Change works durations using drag'n'drop in gantt",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, GANTT_GROUP}, timeOut = 2000000)
    public void changeWorksDurationUsingGantt() {
        myGridActions.changeDurationInGantt(getTestData().getFirstMainWork());
        myGridActions.afterActionsInGantt();
    }

    @Test(description = "Move works in gantt using drag'n'drop",
            dependsOnMethods = {"changeWorksDurationUsingGantt"},
            groups = {FIRST_WORK_GROUP, GANTT_GROUP, ONLY_FF}, timeOut = 2000000)
    public void checkMovingWorksInGantt() {
        myGridActions.moveWorksInGantt(getTestData().getFirstMainWork());
        myGridActions.afterActionsInGantt();
    }

    @Test(description = "Make dependencies using gantt",
            dependsOnMethods = {"changeWorksDurationUsingGantt"},
            groups = {FIRST_WORK_GROUP, GANTT_GROUP, ONLY_FF}, timeOut = 2000000)
    public void makeDependenciesInGantt() {
        myGridActions.makeDependencyInGantt(getTestData().getFirstMainWork());
        myGridActions.afterActionsInGantt();
    }

    @Test(description = "Change statuses for children using gantt",
            dependsOnMethods = {"changeWorksDurationUsingGantt"},
            groups = {FIRST_WORK_GROUP, GANTT_GROUP, ONLY_FF}, timeOut = 2000000)
    public void changeStatusUsingGantt() {
        myGridActions.changeStatusInGantt(getTestData().getFirstMainWork());
        myGridActions.afterActionsInGantt();
    }


    /**
     * set status for several children, push save;
     * after it validate that all statuses are correct
     *
     * @dependsOnMethods validateStatuses, addAfterForFirstChild
     */
    @Test(description = "Set Statuses and Save",
            dependsOnMethods = {
                    "validateGanttDates",
                    "validateCriticalPaths",
                    "checkRunScheduler"},
            groups = {FIRST_WORK_GROUP},
            alwaysRun = true)
    public void checkSetStatuses() {
        myGridActions.setStatuses(getTestData().getFirstMainWork());
    }

    /**
     * set tags for several children, push save;
     * after it validate that all tags are correct
     *
     * @dependsOnMethods checkTagsDisplayOptions, addAfterForFirstChild
     */
    @Test(description = "Set Tags and Save",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP})
    public void checkSetTags() {
        myGridActions.setTags(getTestData().getFirstMainWork(), getTestData().getFirstTag(), getTestData().getSecondTag());
    }

    /**
     * set new owner ('test test') for several works, push save;
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Set new owner when Immediate delegation is off, Save. " +
            "Accept and Reject questions under new user. Validate",
            dependsOnMethods = {
                    "addAfterForFirstChild",
                    "checkSetStatuses"
            },
            groups = {FIRST_WORK_GROUP},
            alwaysRun = true)
    public void checkSetOwner() {
        UixManager.setFeature(UixFeature.Code.IMMEDIATE_DELEGATION, false);
        myGridActions.setOwner(getTestData().getFirstMainWork(), getTestData().getAnotherUser());
    }

    /**
     * check that statuses are correct in information popup;
     *
     * @dependsOnMethods checkSetStatuses
     */
    @Test(description = "Check Statuses in Information Dialog after Saving",
            dependsOnMethods = {"checkSetStatuses"},
            groups = {FIRST_WORK_GROUP})
    public void checkInformationPopupStatuses() {
        myGridActions.testStatusInInformationPopup(getTestData().getFirstMainWork());
    }

    /**
     * check that owner, constraint, dates are correct in information popup;
     * change owner full name and validate again;
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Check other Information settings in popup",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP})
    public void checkInformationPopupSettings() {
        myGridActions.testInformationPopup(getTestData().getFirstMainWork(), getTestData().getNewDefaultUserSettings());
    }

    /**
     * search child works by type
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Check filter by type",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF}, timeOut = 1600000)
    public void checkFilterByType() {
        myOptionsActions.testFilterTypes(getTestData().getFirstMainWork());
    }


    /**
     * Check start date filter (constraint, is)
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Check start date filter (constraint, is)",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF})
    public void checkFilterByConstraintStartDate() {
        myOptionsActions.testFilterConstraintStartDate(getTestData().getFirstMainWork());
    }

    /**
     * Check end date filter (constraint, after)
     *
     * @dependsOnMethods addAfterForFirstChild
     */
    @Test(description = "Check end date filter (constraint, after)",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF})
    public void checkFilterByConstraintEndDate() {
        myOptionsActions.testFilterConstraintEndDate(getTestData().getFirstMainWork());
    }


    /**
     * Check status filter
     *
     * @dependsOnMethods checkSetStatuses
     */
    @Test(description = "Check status filter",
            dependsOnMethods = {"checkSetStatuses"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF})
    public void checkFilterByStatus() {
        myOptionsActions.testFilterStatus(getTestData().getFirstMainWork());
    }

    /**
     * Check filter by specified tags
     *
     * @dependsOnMethods checkSetTags
     */
    @Test(description = "Check tags filter, search works by specified tags",
            dependsOnMethods = {"checkSetTags"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF}, timeOut = 2000000)
    public void checkFilterByTagsWithSpecifiedOptions() {
        myOptionsActions.testFilterTag(getTestData().getFirstMainWork(), getTestData().getFirstTag(), getTestData().getSecondTag());
    }


    /**
     * Check filter tags with option 'No Value'
     *
     * @dependsOnMethods checkSetTags
     */
    @Test(description = "Check tags filter, search works with 'No Value' option",
            dependsOnMethods = {"checkSetTags"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP})
    public void checkFilterByTagsWithNoValueOption() {
        myOptionsActions.testFilterTagNoValues(getTestData().getFirstMainWork(), getTestData().getFirstTag(), getTestData().getSecondTag());
    }


    /**
     * Check filter tags with option 'Any Value'
     *
     * @dependsOnMethods checkSetTags
     */
    @Test(description = "Check tags filter, search works with 'Any Value' option",
            dependsOnMethods = {"checkSetTags"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF})
    public void checkFilterByTagsWithAnyValueOption() {
        myOptionsActions.testFilterTagAnyValues(getTestData().getFirstMainWork(), getTestData().getFirstTag(), getTestData().getSecondTag());
    }

    /**
     * Check filter owner with specified option
     *
     * @dependsOnMethods checkSetOwner
     */
    @Test(description = "Check owner filter, search works by specified owner",
            dependsOnMethods = {"checkSetOwner"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP, ONLY_FF})
    public void checkFilterByOwnerWithSpecifiedOptions() {
        myOptionsActions.testFilterOwner(getTestData().getFirstMainWork(), getTestData().getAnotherUser());
    }

    @Test(description = "Check 'Filters>Options>Display folders' checkbox",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP})
    public void checkDisplayFoldersOption() {
        myOptionsActions.testDisplayFoldersActionItemsDeliverables(getTestData().getFirstMainWork(), 0);
    }

    @Test(description = "Check 'Filters>Options>Display Action Items' checkbox",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP, FILTER_GROUP})
    public void checkDisplayActionItemsOption() {
        myOptionsActions.testDisplayFoldersActionItemsDeliverables(getTestData().getFirstMainWork(), 1);
    }

    @Test(description = "Check option 'Run Scheduler Automatically'",
            dependsOnMethods = {"addAfterForFirstChild"},
            groups = {FIRST_WORK_GROUP}, timeOut = 2000000)
    public void checkRunScheduler() {
        myGridActions.testRunSchedulerAutomatically(getTestData().getFirstMainWork());
    }

    /**
     * auxiliary testcase;
     * just create empty test works tree
     */
    @Test(testName = "Create test works tree (parent with two children)", alwaysRun = true,
            description = "Create test works tree",
            groups = {SECOND_WORK_GROUP})
    public void createEmptyWorksTree() {
        AuxiliaryTCs.checkResourcePlanningOn();
        AuxiliaryTCs.createWorks(getTestData().getSecondMainWork());
    }

    /**
     * create work tree using indent and outdent functionality in new sub work popup;
     */
    @Test(description = "Check indent and oudent functionality in popup",
            groups = {SECOND_WORK_GROUP, ADD_NEW_WORK_GROUP},
            dependsOnMethods = {"createEmptyWorksTree"}, timeOut = 1600000)
    public void checkIndentOutdentTreeInPopup() {
        myPopupActions.checkIndentOutdentTreeInPopup(getTestData().getSecondMainWork(), getTestData().getWork("I"));
    }

    /**
     * load tree using 'Shown level(s)' and 'Display from parent',
     * validate that tree is correct
     *
     * @dependsOnMethods checkIndentOutdentTreeInPopup
     */
    @Test(description = "Check 'Show level(s)' functionality in popup and validate tree from previous tc",
            dependsOnMethods = {"checkIndentOutdentTreeInPopup"},
            groups = {SECOND_WORK_GROUP})
    public void checkShowLevelAndValidateTree() {
        myGridActions.checkShowLevelAndValidateTree(getTestData().getSecondMainWork().getChild(2)); // I
    }

    /**
     * load tree using 'Display from here' and 'Display from parent';
     * validate that tree is correct
     *
     * @dependsOnMethods checkIndentOutdentTreeInPopup
     */
    @Test(description = "Check 'Display from parent', 'Display from here' functionality and validate tree from previous tc",
            dependsOnMethods = {"checkShowLevelAndValidateTree"},
            groups = {SECOND_WORK_GROUP})
    public void checkDisplayFromHereAndParentAndValidateTree() {
        Work w = getTestData().getSecondMainWork();
        try {
            myGridActions.checkDisplayFromHereAndParentAndValidateTree(w);
        } catch (Throwable t) {
            PSLogger.fatal(t);
            PSLogger.info("Try again");
            myGridActions.checkDisplayFromHereAndParentAndValidateTree(w);
        }
    }

    /**
     * check project central delete functionality;
     * remove work tree creating by checkIndentOutdentTreeInPopup tc;
     *
     * @dependsOnMethods checkDisplayFromHereAndParentAndValidateTree
     */
    @Test(description = "Delete some branch and validate tree",
            dependsOnMethods = {"checkDisplayFromHereAndParentAndValidateTree"},
            groups = {SECOND_WORK_GROUP})
    public void deleteChildBranchAndValidateTree() {
        myGridActions.deleteChildBranchAndValidateTree(getTestData().getSecondMainWork());
    }

    /**
     * check indent-outdent menu functionality;
     * make some child indent, than outdent
     *
     * @dependsOnMethods createEmptyWorksTree
     */
    @Test(description = "Check indent-outdent",
            dependsOnMethods = {"createEmptyWorksTree"},
            groups = {SECOND_WORK_GROUP})
    public void checkIndentOutdentInGrid() {
        myGridActions.testIndentOutdent(getTestData().getSecondMainWork());
    }

    /**
     * Rename several works
     *
     * @dependsOnMethods checkIndentOutdent
     */
    @Test(description = "Rename works using grid",
            dependsOnMethods = {"checkIndentOutdentInGrid"},
            groups = {SECOND_WORK_GROUP})
    public void renameInGrid() {
        myGridActions.renameWork("_re", getTestData().getSecondMainWork(),
                getTestData().getSecondMainWork().getChildren().get(1));
    }

    /**
     * Delete several works
     *
     * @dependsOnMethods checkIndentOutdent
     */
    @Test(description = "Check Bulk Delete for several works",
            dependsOnMethods = {"renameInGrid"},
            groups = {SECOND_WORK_GROUP})
    public void checkBulkDelete() {
        myGridActions.testBulkDelete(getTestData().getSecondMainWork());
    }


    /**
     * 'Add Under' cancel functionality;
     * Check that after push 'cancel' no new children appeared in Project Central
     */
    @Test(testName = "Check 'Add Under' canceling",
            description = "Check 'Add Under' children and Cancel",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void addUnderCancel() {
        ourPopupActions.addUnderCancel(getTestData().getRootWork(), getTestData().getWork("R"), getTestData().getWork("G"));
    }

    /**
     * 'Add After' cancel functionality;
     * Check that after push cancel there are not any new children in Project Central
     */
    @Test(testName = "Check 'Add After' canceling",
            description = "Check 'Add After' children and check Cancel",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void addAfterCancel() {
        ourPopupActions.addAfterCancel(getTestData().getRootWork(), getTestData().getWork("S"), getTestData().getWork("H"));
    }


    /**
     * validate that header for 'Add Under' new sub-work popup is correct;
     */
    @Test(testName = "Validate 'Add Under' Header",
            description = "Validate header for 'Add Under' popup",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP})
    public void validateAddUnderPopupHeader() {
        ourPopupActions.addUnderPopupHeader(getTestData().getRootWork());
    }

    /**
     * validate that header for 'Add After' new sub-work popup is correct;
     */
    @Test(testName = "Validate 'Add After' Header",
            description = "Validate header for 'Add After' dialog",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void validateAddAfterPopupHeader() {
        ourPopupActions.addAfterPopupHeader(getTestData().getRootWork());
    }

    /**
     * compare all available types in new sub-work popup;
     * should be: Work, MSP Project, Folder, Milestone, Action Item
     */
    @Test(description = "Validate list of available work types in 'Add Under' dialog",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void validateTypeColumn() {
        ourPopupActions.checkAllTypesFromPopup(getTestData().getRootWork());
    }

    /**
     * check behaviour of type column for 'Add Under' new subwork popup;
     *
     * @dependsOnMethods validateTypeColumn
     */
    @Test(testName = "Validate Add Under Type",
            description = "Validate 'Add Under' dialog (column 'Type')",
            dependsOnMethods = {"validateTypeColumn"},
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void validateAddUnderTypeColumn() {
        ourPopupActions.addUnderTypeColumn(getTestData().getRootWork());
    }

    /**
     * check behaviour of type column for 'Add After' new subwork popup;
     *
     * @dependsOnMethods validateTypeColumn
     */
    @Test(description = "Validate 'Add After' dialog (column 'Type')",
            dependsOnMethods = {"validateTypeColumn"},
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void validateAddAfterTypeColumn() {
        ourPopupActions.addAfterTypeColumn(getTestData().getRootWork());
    }

    /**
     * Check behaviour of 'Set predecessor' link in 'Add Under' new subwork popup;
     * Set several works in popup with indent and outdent options, then push 'Set predecessors'
     * and validate result in popup;
     */
    @Test(description = "Validate predecessors on 'Add Under' popup with indent-outdent for parent work",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP})
    public void checkPredecessorsWithIndentOputdentAddUnder() {
        ourPopupActions.validatePredecessorsWithIndentOputdentUnder(getTestData().getRootWork(), getTestData().getWork("I").copy());
    }

    /**
     * check behaviour of 'Set predecessor' link in 'Add After' new subwork popup,
     * set several works in popup with indent and outdent options, then push 'Set predecessors'
     * and validate result in popup
     */
    @Test(description = "Validate predecessors on 'Add After' popup with indent-outdent for child work",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void checkPredecessorsWithIndentOputdentAddAfter() {
        ourPopupActions.validatePredecessorsWithIndentOputdentAfter(getTestData().getRootWork(), getTestData().getWork("I").copy());
    }

    /**
     * check behaviour of 'Set predecessor' link in 'Add Under' new subwork popup for [arent work;
     * do not set indent-outdent options;
     */
    @Test(testName = "Validate Add Under Predecessor",
            description = "Validate predecessors on 'Add Under' popup without indent-outdent for parent work",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void checkPredecessorsAddUnder() {
        ourPopupActions.validatePredecessorsUnder(getTestData().getRootWork(), getTestData().getFirstMainWorkTree().copy());
    }

    /**
     * check behaviour of 'Set predecessor' link in 'Add After' new subwork popup for some child;
     * do not set indent-outdent options
     */
    @Test(testName = "Validate Add After Predecessor",
            description = "Validate predecessors on 'Add After' popup without indent-outdent for child work",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP, ONLY_FF})
    public void checkPredecessorsAddAfter() {
        ourPopupActions.validatePredecessorsAfter(getTestData().getRootWork(), getTestData().getFirstMainWorkTree().copy());
    }

    /**
     * validateDAEInAddNewWorkPopup;
     * Validate calculating duration, allocation, effort fields in 'Add Under' popup;
     */
    @Test(description = "Validate calculating duration, allocation, effort fields in 'Add Under' popup",
            groups = {MAIN_WORK_GROUP, ADD_NEW_WORK_GROUP})
    public void validateDAEInAddNewWorkPopup() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourPopupActions.testDurationAllocationEffort(getTestData().getRootWork());
    }

    /**
     * check 'View' functionality (click and validate work summary page);
     */
    @Test(testName = "Check View", description = "Check 'View' functionality",
            groups = {MAIN_WORK_GROUP})
    public void checkView() {
        ourGridActions.testView(getTestData().getRootWork());
    }


    /**
     * compare statuses in work grid statuses column with expected;
     * (see project_central.xml config)
     * for parent work set some statuses in work grid (main page), and then reset;
     * validate that statuses are correct before and after resetting
     */
    @Test(description = "Check set Statuses for works from 'ROOT_WORK_FOR_AUTOTESTS' and Reset",
            groups = {MAIN_WORK_GROUP, ONLY_FF})
    public void checkParentStatusesAndReset() {
        ourGridActions.checkRootStatusesAndReset(getTestData().getRootWork(), getTestData().getRandomStatuses());
    }

    /**
     * try to delete some work and than cancel.
     * check that work is not removed really
     */
    @Test(description = "Check Delete work and Cancel",
            groups = {MAIN_WORK_GROUP, ONLY_FF})
    public void checkDeleteCancel() {
        Work work = isThisDriver() ? getTestData().getFirstRootWorkWithDescendants() : getTestData().getRootWork();
        // for checking under custom user we use work with child and owner Admin.
        // otherwise it is possible to delete this work (because parent has owner current user and Permissions tests set WorkItems>Delete true for Edit.
        WBSPage pc = ourGridActions.testDeleteCancel(work);
        if (!isThisDriver()) { // this is to debug:
            WBSPage.Columns d = pc.getOptions().getColumns();
            d.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_PROJECT_OWNER).select(true);
            d.apply();
            pc.getOptions().standardScreen();
        }
    }


    /**
     * uncheck all chekboxes from display options and validate that in project central nothing in columns
     */
    @Test(description =
            "Uncheck all columns, validate that on project central there are not any columns anywhere",
            groups = {MAIN_WORK_GROUP})
    public void uncheckAllDisplayOptions() {
        ourOptionsActions.testUncheckAllDisplayOptions(getTestData().getRootWork());
    }

    /**
     * check all Project chekboxes, validate that columns are correct in project central
     */
    @Test(description = "Check all Projects checkboxes in Columns block",
            groups = {MAIN_WORK_GROUP})
    public void checkProjectDisplayOptions() {
        ourOptionsActions.testCheckDisplayOption(getTestData().getRootWork(), OPTIONS_BLOCK_COLUMNS_PROJECT, 0);
    }

    /**
     * check all Dates chekboxes, validate that columns are correct in project central
     */
    @Test(description = "Check all Dates checkboxes in Columns block",
            groups = {MAIN_WORK_GROUP, ONLY_FF})
    public void checkDatesDisplayOptions() {
        ourOptionsActions.testCheckDisplayOption(getTestData().getRootWork(), OPTIONS_BLOCK_COLUMNS_DATES, 1);
    }

    /**
     * check all Controls chekboxes in display options, validate that columns are correct in project central
     */
    @Test(description = "Check all Controls checkboxes in Columns block",
            groups = {MAIN_WORK_GROUP, ONLY_FF})
    public void checkControlsDisplayOptions() {
        ourOptionsActions.testCheckDisplayOption(getTestData().getRootWork(), OPTIONS_BLOCK_COLUMNS_CONTROLS, 0);
    }

    /**
     * check all Tags chekboxes in display options, validate that columns are correct in project central;
     * check single tag, check that columns are correct     *
     */
    @Test(description = "Check Tags checkboxes in Columns block",
            groups = {MAIN_WORK_GROUP, ONLY_FF})
    public void checkTagsDisplayOptions() {
        ourOptionsActions.testTagDisplayOption(getTestData().getRootWork(), getTestData().getTag("1"), getTestData().getTag("2"));
    }

    /**
     * search child work by name with option 'exactly'
     * validate changing in submenu
     */
    @Test(description = "Check filter by exactly name, validate work menu items in grid",
            groups = {MAIN_WORK_GROUP, MAIN_FILTER, FILTER_GROUP, FILTER_NAME_GROUP, ONLY_FF})
    public void checkFilterByNameWithExactly() {
        ourOptionsActions.testFilterNameExactly(getTestData().getRootWork(), getTestData().getFiltersByName("exactly"));
    }

    /**
     * search child work by name with option 'any-of'
     */
    @Test(description = "Check filter by any-of name",
            groups = {MAIN_WORK_GROUP, MAIN_FILTER, FILTER_GROUP, FILTER_NAME_GROUP, ONLY_FF})
    public void checkFilterByNameWithAnyOf() {
        ourOptionsActions.testFilterNameAnyOf(getTestData().getRootWork(), getTestData().getFiltersByName("any-of"));
    }

    /**
     * search child work by name with option 'all-of'
     */
    @Test(description = "Check filter by all-of name",
            groups = {MAIN_WORK_GROUP, MAIN_FILTER, FILTER_GROUP, FILTER_NAME_GROUP})
    public void checkFilterByNameWithAllOf() {
        ourOptionsActions.testFilterNameAllOf(getTestData().getRootWork(), getTestData().getFiltersByName("all-of"));
    }


    /**
     * check filter by resource pool in our grid
     *
     * @dependsOnMethods checkSetResourcePoolInOurGrid
     */
    @Test(description = "Check resource pool filter, search works with 'Any Value', " +
            "'No Values' and 'Select values...' options",
            dependsOnMethods = {"checkSetResourcePoolInOurGrid"},
            groups = {MAIN_WORK_GROUP, MAIN_FILTER, FILTER_GROUP, ONLY_FF})
    public void checkFilterByResourcePool() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourOptionsActions.testFilterPool(getTestData().getRootWork(), getTestData().getDefResourcePool());
    }

    /**
     * check filter by role in our grid
     *
     * @dependsOnMethods checkSetRoleInOurGrid
     */
    @Test(description = "Check role's filter, search works with 'Any Value', " +
            "'No Values' and 'Select values...' options",
            dependsOnMethods = {"checkSetRoleInOurGrid"},
            groups = {MAIN_WORK_GROUP, MAIN_FILTER, FILTER_GROUP, ONLY_FF})
    public void checkFilterByRole() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourOptionsActions.testFilterRole(getTestData().getRootWork(), TestSession.getRoleList());
    }


    /**
     * Check set statuses for some work from 'ROOT_WORK_FOR_AUTOTESTS'
     */
    @Test(description = "Check set statuses for work from 'ROOT_WORK_FOR_AUTOTESTS' project",
            groups = {MAIN_WORK_GROUP}, timeOut = 2500000)
    public void checkSetStatusesInOurGrid() {
        Work work = isThisDriver() ?
                getTestData().getTestingWork() : getTestData().getAnalysisWork();
        ourGridActions.setStatusesAndSave(work, getTestData().getRandomStatuses());
        PSKnownIssueException ex = toTestBug(getTestData().getRootWork());
        if (ex != null) throw ex;
    }

    /**
     * Check set tags for some work from 'ROOT_WORK_FOR_AUTOTESTS'
     */
    @Test(description = "Check set tags for work from 'ROOT_WORK_FOR_AUTOTESTS' project",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER})
    public void checkSetTagsInOurGrid() {
        Work work = isThisDriver() ?
                getTestData().getTestingWork() : getTestData().getAnalysisWork();
        ourGridActions.setTags(work, getTestData().getFirstTag(), getTestData().getSecondTag());
    }

    /**
     * Check set owner for some work from 'ROOT_WORK_FOR_AUTOTESTS'
     */
    @Test(description = "Check set owner for some work from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP})
    public void checkSetOwnerInOurGrid() {
        UixManager.setFeature(UixFeature.Code.IMMEDIATE_DELEGATION, true);
        Work work = isThisDriver() ?
                getTestData().getTestingWork() : getTestData().getAnalysisWork();
        User owner = isThisDriver() ?
                getTestData().getAnotherUser() :
                BasicCommons.getCurrentUser();
        ourGridActions.setOwner(work, owner);
    }

    /**
     * Check set resource pool for some work from 'ROOT_WORK_FOR_AUTOTESTS'
     */
    @Test(description = "Check set resource pool for some work from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER, GRID_RESOURCE},
            dependsOnMethods = {"checkSetControlCostInOurGrid"})
    public void checkSetResourcePoolInOurGrid() {
        AuxiliaryTCs.checkResourcePlanningOn();
        AuxiliaryTCs.createRateTable();
        Work work = getResourceWork1();
        ourGridActions.setResourcePool(work, getTestData().getDefResourcePool());
        ourGridActions.validateCosts(work);
    }

    /**
     * Check set roles for several works from 'ROOT_WORK_FOR_AUTOTESTS'
     */
    @Test(description = "Check set roles for several works from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER, GRID_RESOURCE},
            dependsOnMethods = {"checkSetControlCostInOurGrid", "checkSetStatusesUsingBulkEdit"})
    public void checkSetRoleInOurGrid() {
        AuxiliaryTCs.checkResourcePlanningOn();
        AuxiliaryTCs.createRateTable();
        Work work1 = getResourceWork1();
        Work work2 = getResourceWork2();
        ourGridActions.setResourceRole(getTestData().getRootWork(), work1, work2, BuiltInRole.OWNER, BuiltInRole.CONTRIBUTOR);
        ourGridActions.validateCosts(work1);
    }

    @Test(description = "Check Add Split for some work from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER, GRID_RESOURCE},
            dependsOnMethods = {"checkSetRoleInOurGrid", "checkSetResourcePoolInOurGrid"})
    public void checkAddSplit() {
        AuxiliaryTCs.checkResourcePlanningOn();
        Work root = getTestData().getRootWork();
        Work work = getResourceWork1();
        ourGridActions.addSplit(root, work);
    }

    @Test(description = "Check set person for some work from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER, GRID_RESOURCE},
            dependsOnMethods = {"checkAddSplit"})
    public void checkSetPersonInOurGrid() {
        AuxiliaryTCs.checkResourcePlanningOn();
        Work root = getTestData().getRootWork();
        Work work = getResourceWork1();
        User user = getTestData().getAnotherUser();
        ourGridActions.setResourcePerson(root, work, user);
        ourGridActions.validateCosts(work);
    }

    @Test(description = "Check set person for some work from 'ROOT_WORK_FOR_AUTOTESTS'",
            groups = {MAIN_WORK_GROUP, BEFORE_MAIN_BULK_FILTER})
    public void checkSetControlCostInOurGrid() {
        Work root = getTestData().getRootWork();
        Work work = getResourceWork1();
        ourGridActions.setControlCost(root, work);
        ourGridActions.validateCosts(work);
    }

    private Work getResourceWork1() {
        return isThisDriver() ?
                getTestData().getAnalysisWork() : getTestData().getDesignWork();
    }

    private Work getResourceWork2() {
        return isThisDriver() ?
                getTestData().getDesignWork() : getTestData().getAnalysisWork();
    }

    /**
     * Check set statuses for several works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set statuses for several works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP})
    public void checkSetStatusesUsingBulkEdit() {
        Work work1 = getResourceWork1();
        Work work2 = getResourceWork2();
        ourGridBulkActions.testSetStatusesUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getRandomStatusList(), work1, work2);
    }

    /**
     * Check set constraint type for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set constraint type for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, BULK_EDIT_CONSTRAINT_GROUP, ONLY_FF}, timeOut = 2000000)
    public void checkSetConstraintsTypesUsingBulkEdit() {
        ourGridBulkActions.testSetConstraintsUsingBulkEditOptionsBlock(getTestData().getCodingWork());
    }

    /**
     * checkSetStartEndDatesInOurGridUsingBulkEditBlock
     * Check set start and end dates work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set Constraint Start and End Dates type for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, BULK_EDIT_CONSTRAINT_GROUP, ONLY_FF})
    public void checkSetConstraintDatesUsingBulkEdit() {
        ourGridBulkActions.testSetStartEndDatesUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getCodingWork());
    }

    /**
     * Check set 'Percentage Complete' for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Percentage Complete' for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF},
            dependsOnMethods = "checkSetStatusesUsingBulkEdit")
    public void checkSetPercentageUsingBulkEdit() {
        Work work1 = getResourceWork1();
        //Work work2 = getResourceWork2();
        ourGridBulkActions.testSetPercentageUsingBulkEditOptionsBlock(work1, getTestData().getInputTestData());
    }

    /**
     * checkSetDependencyInOurGridUsingBulkEditBlock;
     * Check set 'Dependency' for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'     *
     */
    @Test(description = "Check set 'Dependency' for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF}, timeOut = 2500000)
    public void checkSetDependencyUsingBulkEdit() {
        ourGridBulkActions.testSetDependencyUsingBulkEditOptionsBlockAndReset(
                getTestData().getRootWork(),
                getTestData().getDesignWork(),
                getTestData().getTestingWork(),
                getTestData().getAnalysisWork(),
                getTestData().getCodingWork());
        ourGridBulkActions.testErrorDependencyUsingBulkEditOptionsBlockAndSave(getTestData().getRootWork(), getTestData().getTestingWork(), getTestData().getAnalysisWork());
    }

    /**
     * checkSetPriorityUsingBulkEditBlock;
     * Check set 'Priority' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Priority' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetPriorityUsingBulkEdit() {
        ourGridBulkActions.testSetPriorityUsingBulkEditOptionsBlock(getTestData().getRootWork());
    }

    /**
     * checkSetStatusReportingUsingBulkEditBlock;
     * Check set 'StatusReporting' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Status Reporting' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetStatusReportingUsingBulkEdit() {
        ourGridBulkActions.testSetStatusReportingUsingBulkEditOptionsBlock(getTestData().getRootWork());
    }

    /**
     * checkSetCurrencyUsingBulkEditBlock;
     * Check set 'Currency' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Currency' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP})
    public void checkSetCurrencyUsingBulkEdit() {
        ourGridBulkActions.testSetCurrencyUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getCurrencies());
    }

    /**
     * checkSetActualDatesUsingBulkEditBlock;
     * Check set 'Actual Start' and 'Actual End' dates for works from
     * 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Actual Start' and 'Actual End' dates for works from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetActualDatesUsingBulkEdit() {
        ourGridBulkActions.testSetActualDatesUsingBulkEditOptionsBlock(getTestData().getDesignWork());
    }


    /**
     * checkSetOwnerUsingBulkEditBlock
     * Check set Owner for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set Owner for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'. Reset.",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetOwnerUsingBulkEdit() {
        ourGridBulkActions.testSetOwnerUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getAnotherUser(), BasicCommons.getCurrentUser());
    }

    /**
     * checkSetDurationAllocationEffortUsingBulkEditBlock;
     * Check set Duration, Allocation, Effort for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set Duration, Allocation, Effort for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetDAEUsingBulkEdit() {
        AuxiliaryTCs.checkResourcePlanningOn();
        // this work should not have 'FD' as constraint 
        Work work = getTestData().getDesignWork();
        ourGridBulkActions.testSetDurationAllocationEffortUsingBulkEditOptionsBlock(work);
    }

    /**
     * checkSetRoleAndResourcePoolUsingBulkEditBlock;
     * Check set Role and Resource Pool for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     *
     * @dependsOnMethods checkSetRoleInOurGrid, checkSetResourcePoolInOurGrid
     */
    @Test(description = "Check set Role and Resource Pool for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            dependsOnMethods = {"checkSetRoleInOurGrid", "checkSetResourcePoolInOurGrid"},
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetRoleAndResourcePoolUsingBulkEdit() {
        ourGridBulkActions.testSetRoleAndResourcePoolUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getDefResourcePool(), TestSession.getRoleList());
    }

    /**
     * checkSetTagsUsingBulkEditBlock;
     * Check set Tags for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     *
     * @dependsOnMethods checkSetTagsInOurGrid
     */
    @Test(description = "Check set Tags for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            dependsOnMethods = {"checkSetTagsInOurGrid"},
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetTagsUsingBulkEdit() {
        ourGridBulkActions.testSetTagsUsingBulkEditOptionsBlockAndReset(getTestData().getRootWork(), getTestData().getFirstTag(), getTestData().getSecondTag());
    }

    /**
     * checkSetCalendarUsingBulkEdit;
     * Check set 'Calendar' and 'Inherit Calendar' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'.
     */
    @Test(description = "Check set 'Calendar' and 'Inherit Calendar' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetCalendarUsingBulkEdit() {
        ourGridBulkActions.testSetCalendarUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getCalendar("test"), getTestData().getCalendar("default"));
    }

    /**
     * checkSetControlCostUsingBulkEdit;
     * Check set 'Control Cost' and 'Inherit Controls' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Control Cost' and 'Inherit Controls' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetControlCostUsingBulkEdit() {
        ourGridBulkActions.testSetControlCostUsingBulkEditOptionsBlock(getTestData().getRootWork());
    }

    /**
     * checkSetManualSchedulingUsingBulkEdit;
     * Check set 'Manual Scheduling' and 'Inherit Controls' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     * TODO: temporary disable this tc for 93 and latter. create another tc
     */
    @Test(description = "Check set 'Manual Scheduling' and 'Inherit Controls' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, TestSettings.PS_92_GREATER_GROUP, ONLY_FF})
    public void checkSetManualSchedulingUsingBulkEdit() {
        ourGridBulkActions.testSetManualSchedulingUsingBulkEditOptionsBlock(getTestData().getRootWork());
    }

    /**
     * checkSetRateTableUsingBulkEdit;
     * Check set 'Rate Table' and 'Inherit Rate Table' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Rate Table' and 'Inherit Rate Table' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetRateTableUsingBulkEdit() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourGridBulkActions.testSetRateTableUsingBulkEditOptionsBlock(getTestData().getRootWork(), getTestData().getRateTable("custom").getName(),
                getTestData().getRateTable("default").getName());
    }


    /**
     * checkSetPersonalRateRuleUsingBulkEdit;
     * Check set 'Personal Rate Rule' and 'Inherit Personal Rate Rule' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Personal Rate Rule' and 'Inherit Personal Rate Rule' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetPersonalRateRuleUsingBulkEdit() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourGridBulkActions.testSetPersonalRateRuleUsingBulkEditOptionsBlock(getTestData().getRootWork());
    }

    /**
     * checkSetActivityTypesUsingBulkEdit;
     * Check set 'Activity Types' and other 'activity controls' for works from
     * 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     *
     * @dependsOnMethods checkSetControlCostUsingBulkEdit
     */
    @Test(description = "Check set 'Activity Types' and other 'activity controls' for works from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF},
            dependsOnMethods = {"checkSetControlCostUsingBulkEdit"})
    public void checkSetActivityTypesUsingBulkEdit() {
        ourGridBulkActions.testSetActivityTypesUsingBulkEditOptionsBlock(getTestData().getRootWork(),
                getTestData().getDesignWork(), getTestData().getActivityTypes());
    }

    /**
     * checkSetDefaultActivitiesUsingBulkEdit;
     *
     * @dependsOnMethods checkSetControlCostUsingBulkEdit
     */
    @Test(description = "Check set 'Default Activities' for works from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF},
            dependsOnMethods = {"checkSetControlCostUsingBulkEdit"})
    public void checkSetDefaultActivitiesUsingBulkEdit() {
        ourGridBulkActions.testSetDefaultActivitiesUsingBulkEditOptionsBlock(getTestData().getRootWork(),
                getTestData().getDesignWork(), getTestData().getActivity());
    }

    /**
     * checkSetIncludeActionItemsUsingBulkEditBlock;
     * Check set 'Include Action Items' and 'Plan Resources' for work from 'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'
     */
    @Test(description = "Check set 'Include Action Items' and 'Plan Resources' for work from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit'",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP})
    public void checkSetIncludeActionItemsUsingBulkEdit() {
        ourGridBulkActions.testSetPlanResourcesAndIncludeActionItemsUsingBulkEditOptionsBlock(
                getTestData().getRootWork(), getTestData().getDesignWork());
    }

    /**
     * checkSetInheritPermissionsUsingBulkEdit;
     */
    @Test(description = "Check set 'Inherit Permissions' for works from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Bulk Edit' and validate summary page",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP, ONLY_FF})
    public void checkSetInheritPermissionsUsingBulkEdit() {
        ourGridBulkActions.testSetInheritPermissionsUsingBulkEditOptionsBlock(getTestData().getRootWork(),
                getTestData().getDesignWork(),
                getTestData().getCodingWork());
    }

    /**
     * Check set 'Person' for works from 'ROOT_WORK_FOR_AUTOTESTS' using 'Allocate Resource' dialog
     */
    @Test(description = "Check set 'Person' for works from " +
            "'ROOT_WORK_FOR_AUTOTESTS' using 'Allocate Resource' dialog",
            groups = {MAIN_WORK_GROUP, MAIN_BULK, BULK_EDIT_GROUP})
    public void checkSetPersonUsingAllocateResourceDialog() {
        AuxiliaryTCs.checkResourcePlanningOn();
        ourGridBulkActions.testAllocateResource(getTestData().getRootWork(), getTestData().getAnotherUser(), BasicCommons.getCurrentUser());
    }


    /**
     * Delete some work
     *
     * @dependsOnMethods checkFilterByName*
     */
    @Test(description = "Check delete some work",
            groups = {MAIN_WORK_GROUP})
    public void checkDelete() {
        Work work = getTestData().getFirstRootWorkWithDescendants();
        PSKnownIssueException ex0 = toTestBug(work.getParent());
        try {
            ourGridActions.testDelete(work);
        } finally {
            PSKnownIssueException ex1 = toTestBug(work.getParent());
            if (ex1 != null) {
                throw ex1;
            }
            if (ex0 != null)
                throw ex0;
        }
    }

    private PSKnownIssueException toTestBug(Work parent) {   // TODO: remove it
        try {
            WBSPage pc = WBSPage.openWBSPage(parent);
            WBSPage.Columns cl = pc.getOptions().getColumns();
            cl.uncheckAll();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_PROJECT_STATUS).select();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_RESOURCE_ROLE).select();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_RESOURCE_POOL).select();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_DATES_SCHEDULED).select();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).select();
            cl.getCheckbox(WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_DATES_ACTUAL).select();
            cl.apply();
            pc.hideGantt();
            PSLogger.save("To debug");
            StringBuilder sb = new StringBuilder();
            for (Work w : parent.getChildren(true, true)) {
                String role = pc.getGrid().getRole(w);
                String pool = pc.getGrid().getResourcePool(w);
                PSLogger.info("Work " + w.getName() + ", role " + role + ", pool " + pool);
                if (!w.getResource().hasRole()) {
                    if (role == null)
                        PSLogger.debug("Work " + w.getName() + ": no roles as expected");
                } else if (role == null) {
                    w.getResource().removeRole();
                    PSLogger.warn("expect role " + role + " for work " + w.getName());
                    sb.append("expect role " + role + " for work " + w.getName()).append(";");
                }
                if (!w.getResource().hasPool()) {
                    if (pool == null)
                        PSLogger.debug("Work " + w.getName() + ": no pools as expected");
                } else if (pool == null) {
                    w.getResource().removePool();
                    PSLogger.warn("expect pool " + pool + " for work " + w.getName());
                    sb.append("expect pool " + pool + " for work " + w.getName()).append(";");
                }
            }
            if (sb.length() != 0) {
                return new PSKnownIssueException(81946, sb.toString());
            }
        } catch (Throwable t) {
            PSLogger.fatal(t);
        }
        return null;
    }

    /**
     * test resizing. for bug #70447
     */
    @Test(description = "Check resizing for table in grid",
            groups = {MAIN_WORK_GROUP})
    public void checkResizingColumnsInGrid() {
        ourGridActions.testResizing(getTestData().getRootWork());
    }

    @Test(description = "Check saving, deleting, overwriting layouts",
            groups = {MAIN_WORK_GROUP}, timeOut = 2000000)
    public void simpleCheckLayouts() {
        ourOptionsActions.simpleValidateLayouts(getTestData(), getTestData().getRootWork());
    }

    /**
     * Check dates for sequential gated project with 'Manual Scheduling' = 'Yes'
     */
    @Test(description = "Check constraint dates for sequential gated project", groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF})
    public void checkSetConstraintDatesForSGP() {
        gatedProjectsActions.testSetConstraintDatesForSequentialGatedProject(getTestData().getSGPWork());
    }

    /**
     * Check errors for sequential gated project
     */
    @Test(description = "Check errors for sequential gated project", groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF})
    public void checkErrorsWithConstraintDatesForSGP() {
        gatedProjectsActions.testErrorsWithConstraintDates(getTestData().getSGPWork(), getTestData().getCalendar());
    }

    /**
     * Validate scheduled dates for sequential gated project with 'Manual Scheduling' = 'Yes'
     *
     * @dependsOnMethods checkSetConstraintDatesForSGP
     */
    @Test(description = "Validate scheduled dates for sequential gated project",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF}, dependsOnMethods = {"checkSetConstraintDatesForSGP"})
    public void validateScheduledDatesForSGP() {
        gatedProjectsActions.testScheduledDatesForSequentialGatedProject(getTestData().getSGPWork());
    }

    /**
     * Validate available work types in 'Add Under' and 'Add After' dialogs
     * for non-sequential gated project
     */
    @Test(description = "Validate available work types in 'Add Under' and 'Add After' dialogs for non-sequential gated project",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF})
    public void validateAddWorksForGatedProject() {
        gatedProjectsActions.validateAddUnderAndAddAfterWorkTypesForGatedProject(getTestData().getNSGPWork(), getTestData().getWork("P"));
    }

    /**
     * Validate available work types in 'Add Under' and 'Add After' dialogs for non-sequential gated template
     */
    @Test(description = "Validate available work types in 'Add Under' and 'Add After' dialogs for non-sequential gated template",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF})
    public void validateAddWorksForGatedTemplate() {
        gatedProjectsActions.validateAddUnderAndAddAfterWorkTypesForGatedTemplate(getTestData().getNSGPTemplate(), getTestData().getWork("P").copy());
    }

    @Test(description = "Cancel sequential gated project",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF},
            dependsOnMethods = {"validateScheduledDatesForSGP"})
    public void cancelSequentialGatedProject() {
        gatedProjectsActions.makeProjectCanceled(getTestData().getSGPWork());
    }

    /**
     * validate setting actual start and end dates for canceled project with Enforce Sequential.
     * TODO: fix this tc. now not supported for 9.3 or later
     *
     * @dependsOnMethods validateScheduledDatesForSGP
     */
    @Test(description = "Check setting actual dates for canceled sequential gated project",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF, TestSettings.PS_92_GREATER_GROUP},
            dependsOnMethods = {"cancelSequentialGatedProject"})
    public void checkSetActualDatesForSGP() {
        gatedProjectsActions.testSetActualDatesForSequentialGatedProject(getTestData().getSGPWork());
    }

    /**
     * validate errors when setting actual dates for canceled project with Enforce Sequential.
     * TODO: fix this tc. now not supported for 9.3 or later
     */
    @Test(description = "Check errors in grid in case incorrect actual dates",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF, TestSettings.PS_92_GREATER_GROUP},
            dependsOnMethods = {"cancelSequentialGatedProject"}, timeOut = 2000000)
    public void checkErrorsWithActualDatesForSGP() {
        gatedProjectsActions.testGridErrors(getTestData().getSGPWork());
    }

    @Test(description = "Check set constraint types for gated project without Enforce Sequential (manual scheduling on)",
            groups = {GATED_GROUP_1, GATED_GROUP_2, ONLY_FF}, timeOut = 2000000)
    public void checkConstraintTypesForNonSGP() {
        gatedProjectsActions.testSetConstraintTypesForNonSequentialGatedProject(getTestData().getNSGPWork(), getTestData().getCalendar());
    }

    /**
     * auxiliary testcase;
     * just create empty test work for testing with constraint dates and enabled 'Plan Resources'.
     */
    @Test(alwaysRun = true,
            description = "Create empty work, enable 'Plan Resources', set constraint dates, go to Project Central page",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void createTeamPaneWork() {
        AuxiliaryTCs.checkResourcePlanningOn();
        AuxiliaryTCs.checkTeamPaneOn();
        AuxiliaryTCs.createWork(getTestData().getThirdMainWork());
    }


    @Test(description = "Check Show/Hide for Team Pane, Gantt and Variable Allocation",
            dependsOnMethods = "createTeamPaneWork",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneShowHide() {
        teamPaneActions.testShowHide();
    }

    @Test(description = "Validate Team Pane list after assign Role",
            dependsOnMethods = "validateTeamPaneShowHide",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneAssignRole() {
        teamPaneActions.testAssignRoleForWork();
    }

    @Test(description = "Check 'Add split', Validate Team Pane list after assign Person",
            dependsOnMethods = "validateTeamPaneShowHide",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneAssignPerson() {
        teamPaneActions.testAssignPersonForResource();
    }

    @Test(description = "Validate Team Pane list after assign Person and Role for resources, Save",
            dependsOnMethods = {"validateTeamPaneAssignPerson", "validateTeamPaneAssignRole"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void setTeamPaneResourcesAndSave() {
        teamPaneActions.testAssignPersonAndRoleForResourcesAndSave();
    }

    @Test(description = "Validate Allocation and Availability values in Team Pane (Project Central layout)",
            dependsOnMethods = "setTeamPaneResourcesAndSave",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneValues() {
        teamPaneActions.testAllocationAvailability();
    }

    @Test(description = "Set allocations for resources. Validate Team Pane values (Resource Planning page). Save",
            dependsOnMethods = "validateTeamPaneValues",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void setResourceAllocationAndSave() {
        teamPaneActions.testSetAllocationForResourcesAndSave();
    }

    @Test(description = "Change calendar for user to custom, validate team-pane, change it back and validate again",
            dependsOnMethods = "validateTeamPaneValues",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void checkChangeCalendar() {
        teamPaneActions.testChangeUserCalendarAndValidateAllocationAvailability();
    }

    @Test(description = "Validate colors for week periods (Red, Yellow, White). Reset",
            dependsOnMethods = "setTeamPaneResourcesAndSave",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneColors() {
        teamPaneActions.testTeamPaneColors();
    }

    @Test(description = "Validate Variable Allocation Grid.",
            dependsOnMethods = {"setTeamPaneResourcesAndSave", "setResourceAllocationAndSave"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateVariableAllocation() {
        teamPaneActions.testVariableAllocationGrid();
    }

    @Test(description = "Change allocation for resources on Resource Review page.",
            dependsOnMethods = {"checkChangeCalendar", "validateVariableAllocation"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void changeResourceReviewValues() {
        teamPaneActions.testChangeValuesOnResourceReviewPage();
    }

    @Test(description = "Validate that Team Pane information is correct after changes on Resource Review page.",
            dependsOnMethods = {"changeResourceReviewValues"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateTeamPaneAfterResourceReviewChanges() {
        teamPaneActions.testTeamPaneAfterResourceReviewChanges();
    }

    @Test(description = "Change Constraint Start and End Dates, validate values in Variable Allocation Grid. Reset",
            dependsOnMethods = {"validateTeamPaneAfterResourceReviewChanges"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void changeConstraintDatesAndValidateVA() {
        teamPaneActions.testChangeConstraintDatesAndValidateVA();
    }

    @Test(description = "Validate that Histogram information is correct",
            dependsOnMethods = {"changeResourceReviewValues"},
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateHistogram() {
        teamPaneActions.testHistogram();
    }

    @Test(description = "Validate menu for work ('Add split')",
            dependsOnMethods = "createTeamPaneWork",
            groups = {TEAM_PANE_GROUP_1, TEAM_PANE_GROUP_2})
    public void validateAddSplit() {
        teamPaneActions.testAddSplit();
    }

}
