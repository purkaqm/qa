package com.powersteeringsoftware.tests.project_central.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.FiltersGridPage;
import com.powersteeringsoftware.libs.pages.ResourceReviewPage;
import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.actions.UserManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT;
import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.OPTIONS_BLOCK_COLUMNS_RESOURCE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.02.11
 * Time: 16:25
 */
@Deprecated
public class TeamPaneTCs {

    private static Work rootWork;
    private static UserRole parent;
    private static UserRole user;

    private static UserRole role;
    private static UserRole userContributor;
    private static UserRole unallocatedOwner;

    public void init(Work w, User u1, User u2) {
        rootWork = w;
        parent = new UserRole(null, null, rootWork);
        user = new UserRole(u1, null, rootWork);

        role = new UserRole(null, BuiltInRole.CONTRIBUTOR, rootWork);
        userContributor = new UserRole(u2, BuiltInRole.CONTRIBUTOR, rootWork);
        unallocatedOwner = new UserRole(null, BuiltInRole.OWNER, rootWork);
    }

    public static class UserRole extends WBSPage.TeamPaneItem implements Cloneable {
        private User user;
        private Role role;
        private PSCalendar calendar;
        private Work resource;
        private Work root;
        private List<Float> allocations = new ArrayList<Float>();
        private float capacity;
        private List<PSCalendar> periods = new ArrayList<PSCalendar>();
        private List<Integer> weeks = new ArrayList<Integer>();

        private Integer yellowLimit;
        private Integer redLimit;

        private UserRole(User u, Role r, Work root) {
            super(u, r);
            user = u;
            role = r;
            setParent(root);
        }


        public User getPerson() {
            return user;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
            this.roleName = role.getName();
        }

        /**
         * setter for newly splitted resource
         *
         * @param r resource (Work)
         */
        public void setResource(Work r) {
            resource = r;
            setParent(r.getParent());
            if (role != null)
                resource.getResource().setRole(role);
            if (user != null)
                resource.getResource().setPerson(user);
            resource.setAllocation(1);
        }


        /**
         * set allocation for this resource.
         *
         * @param v 0..1
         */
        public void setAllocation(float v) {
            if (resource != null)
                resource.setAllocation(v);
            allocations.clear();
        }

        public Float getAllocation() {
            Float res = resource != null ? resource.getAllocation() : null;
            if (res != null) {
                return res;
            }
            return 1F;
        }

        public Float getAllocation(PSCalendar week) {
            int index = getPeriods().indexOf(week);
            return getAllocations().get(index);
        }

        public List<Float> getAllocations() {
            if (!allocations.isEmpty()) return allocations;
            for (PSCalendar period : getPeriods()) {
                int d = getWeekDuration(period);
                allocations.add(d != 0 ? getAllocation() : 0);
            }
            return allocations;
        }

        public List<String> getVAValues() {
            List<String> res = new ArrayList<String>();
            for (PSCalendar period : getPeriods()) {
                res.add(WBSPage.toGridFormat(getVAValue(period)));
            }
            return res;
        }

        public List<Integer> getAvailabilities() {
            float teamPaneDemand = getCapacity() * getCalendar().getHoursPerWeek();
            List<Integer> hours = new ArrayList<Integer>();
            for (PSCalendar period : getPeriods()) {
                float al = getAllocation(period);
                int duration = getWeekDuration(period);
                float teamPaneAllocation = al * duration;
                Float teamPaneAvailability = teamPaneDemand - teamPaneAllocation;
                hours.add(teamPaneAvailability.intValue());
            }
            return hours;
        }

        public List<FiltersGridPage.TeamPaneCell> getTeamPaneValuesInHours() {
            float teamPaneDemand = getCapacity() * getCalendar().getHoursPerWeek();
            List<FiltersGridPage.TeamPaneCell> hours = new ArrayList<FiltersGridPage.TeamPaneCell>();
            for (PSCalendar period : getPeriods()) {
                float al = getAllocation(period);
                int duration = getWeekDuration(period);
                float teamPaneAllocation = al * duration;
                float teamPaneAvailability = teamPaneDemand - teamPaneAllocation;
                float teamPaneAllocationPercents = getTeamPaneAllocationInPercents(teamPaneDemand, al, duration);
                FiltersGridPage.TeamPaneCell cell = new FiltersGridPage.TeamPaneCell(teamPaneAllocation, teamPaneAvailability, false);
                cell.setColor(getColor(teamPaneDemand, teamPaneAllocationPercents, duration));
                hours.add(cell);
            }
            return hours;
        }

        public List<FiltersGridPage.TeamPaneCell> getTeamPaneValuesInPercents() {
            float teamPaneDemand = getCapacity() * getCalendar().getHoursPerWeek();
            List<FiltersGridPage.TeamPaneCell> percents = new ArrayList<FiltersGridPage.TeamPaneCell>();
            for (PSCalendar period : getPeriods()) {
                float al = getAllocation(period);
                int duration = getWeekDuration(period);
                float teamPaneAllocation = getTeamPaneAllocationInPercents(teamPaneDemand, al, duration);
                float teamPaneAvailability = getTeamPaneAvailabilityInPercents(teamPaneDemand, al, duration);
                FiltersGridPage.TeamPaneCell cell = new FiltersGridPage.TeamPaneCell(teamPaneAllocation, teamPaneAvailability, true);
                cell.setColor(getColor(teamPaneDemand, teamPaneAllocation, duration));
                percents.add(cell);
            }
            return percents;
        }

        private static float getTeamPaneAllocationInPercents(float teamPaneDemand, float periodAllocation, int periodDuration) {
            float teamPaneAllocation;
            if (teamPaneDemand == 0) {
                teamPaneAllocation = 0;
            } else {
                teamPaneAllocation = 100 * periodAllocation * periodDuration / teamPaneDemand;
            }
            return teamPaneAllocation;
        }

        private static float getTeamPaneAvailabilityInPercents(float teamPaneDemand, float periodAllocation, int periodDuration) {
            float teamPaneAvailability;
            if (teamPaneDemand == 0) {
                teamPaneAvailability = 0;
            } else {
                teamPaneAvailability = 100 * (teamPaneDemand - periodAllocation * periodDuration) / teamPaneDemand;
            }
            return teamPaneAvailability;
        }

        private FiltersGridPage.TeamPaneCell.Color getColor(float teamPaneDemand, float teamPaneAvailability, int periodDuration) {
            if (redLimit == null && yellowLimit == null) return null;
            if (teamPaneDemand == 0) { // unallocated role
                if (periodDuration == 0) return null; // non-work period
                if (redLimit != null)
                    return FiltersGridPage.TeamPaneCell.Color.RED;
                return FiltersGridPage.TeamPaneCell.Color.YELLOW;
            }
            if (redLimit != null && teamPaneAvailability >= redLimit) return FiltersGridPage.TeamPaneCell.Color.RED;
            if (yellowLimit != null && teamPaneAvailability >= yellowLimit)
                return FiltersGridPage.TeamPaneCell.Color.YELLOW;
            return null;
        }

        public void setLimits(Integer yellow, Integer red) {
            yellowLimit = yellow;
            redLimit = red;
        }

        public List<String> getRRValuesInHours() {
            List<String> res = new ArrayList<String>();
            for (PSCalendar period : getPeriods()) {
                res.add(ResourceReviewPage.toGridFormat(getVAValue(period)));
            }
            return res;
        }

        public float getVAValue(PSCalendar week) {
            int index = getPeriods().indexOf(week);
            return getAllocations().get(index) * getWeeks().get(index);
        }

        public void setVAValue(PSCalendar week, int val) {
            int index = getPeriods().indexOf(week);
            setAllocation(index, (float) val / getWeeks().get(index));
        }

        public void setAllocation(PSCalendar week, Float value) {
            int index = getPeriods().indexOf(week);
            setAllocation(index, value);
        }

        private void setAllocation(int index, Float value) {
            getAllocations().set(index, value);
            int sum = 0;
            float newAllocation = 0;
            for (int i = 1; i < allocations.size() - 1; i++) { // skip empty first and last week
                newAllocation += allocations.get(i);
                sum++;
            }
            float allocation = newAllocation / sum;
            if (resource != null)
                resource.setAllocation(allocation);

        }

        public void setCapacity(float c) {
            capacity = c;
        }

        public float getCapacity() {
            if (user != null && role != null)
                setCapacity(user.getRoleCapacity(role));
            return capacity;
        }

        public Work getResource() {
            if (user == null && role == null) { // its for empty parent:
                return root;
            }
            return resource;
        }

        public Work getParent() {
            if (root == null) setParent(resource.getParent());
            return root;
        }

        public void setParent(Work w) {
            root = w;
            setPeriods();
        }

        public PSCalendar getCalendar() {
            if (calendar != null) return calendar;
            if (user == null) {
                return calendar = PSCalendar.getEmptyCalendar();
            }
            return calendar = user.getCalendar();
        }

        public void setCalendar(PSCalendar calendar) {
            this.calendar = calendar;
            setTestWeeks();
        }

        public List<Integer> getWeeks() {
            if (weeks.isEmpty()) {
                setTestWeeks();
            }
            return weeks;
        }

        public Integer getWeekDuration(PSCalendar week) {
            int index = getPeriods().indexOf(week);
            return getWeeks().get(index);
        }

        private void setTestWeeks() {
            if (user == null && role == null) { // its for empty parent:
                for (PSCalendar c : getPeriods()) {
                    weeks.add(0);
                }
                return;
            }
            weeks = root.getWeekDurations(getCalendar());
            weeks.add(0, 0);
            weeks.add(0);
        }

        public List<PSCalendar> getPeriods() {
            if (periods.isEmpty()) {
                setPeriods();
            }
            return periods;
        }

        public PSCalendar getFirstWeek() {
            getPeriods();
            return periods.get(1);
        }

        public PSCalendar getLastWeek() {
            getPeriods();
            return periods.get(periods.size() - 2);
        }

        private void setPeriods() {
            this.periods.clear();
            List<PSCalendar> periods = root.getWeekPeriods();
            int week = PSCalendar.getEmptyCalendar().getWeekDays().length;
            periods.add(0, periods.get(0).set(-week));
            periods.add(periods.get(periods.size() - 1).set(week));
            for (int i = 0; i < periods.size(); i++) {
                this.periods.add(periods.get(i));
            }
            PSLogger.info("Periods: " + periods);
        }

        private void setAllocationsAndWeeks() {
            getAllocations();
            getWeeks();
        }

        public void increaseRightBound(int weeksNum) {
            setAllocationsAndWeeks();
            for (int i = 0; i < weeksNum; i++) {
                int index = periods.size() - 2;
                periods.add(periods.get(periods.size() - 1).set(7));
                allocations.add(index, allocations.get(index));
                weeks.add(index, weeks.get(index));
            }
        }

        public void decreaseRightBound(int weeksNum) {
            setAllocationsAndWeeks();
            for (int i = 0; i < weeksNum; i++) {
                int index = periods.size() - 2;
                periods.remove(periods.size() - 1);
                allocations.remove(index);
                weeks.remove(index);
            }
        }

        public void decreaseLeftBound(int weeksNum) {
            setAllocationsAndWeeks();
            for (int i = 0; i < weeksNum; i++) {
                int index = periods.size() - 2;
                periods.add(0, periods.get(0).set(-7));
                allocations.add(index, allocations.get(index));
                weeks.add(index, weeks.get(index));
            }
        }

        public void increaseLeftBound(int weeksNum) {
            setAllocationsAndWeeks();
            for (int i = 0; i < weeksNum; i++) {
                int index = periods.size() - 2;
                periods.remove(0);
                allocations.remove(index);
                weeks.remove(index);
            }
        }

        @Override
        public Object clone() {
            UserRole clone = new UserRole(user, role, root);
            clone.resource = resource;
            clone.allocations.addAll(allocations);
            clone.weeks.addAll(weeks);
            clone.capacity = capacity;
            if (calendar != null)
                clone.calendar = new PSCalendar(calendar.getConfig());
            return clone;
        }
    }

    public void testShowHide() {
        WBSPage pc = WBSPage.openWBSPage(rootWork, false);
        Assert.assertTrue(pc.getTeamPane().isVisible(), "Can't find team pane");
        pc.hideTeamPane();
        Assert.assertFalse(pc.getTeamPane().isVisible(), "Team pane is visible after hiding");
        pc.showTeamPane();
        Assert.assertTrue(pc.getTeamPane().isVisible(), "Can't find team pane after showing");

        Assert.assertFalse(pc.getVariableAllocation().isVisible(), "Variable Allocation is visible");
        Assert.assertTrue(pc.getGantt().isVisible(), "Gantt is not visible");
        pc.showVariableAllocation();
        Assert.assertTrue(pc.getVariableAllocation().isVisible(), "Variable Allocation is not visible");
        Assert.assertFalse(pc.getGantt().isVisible(), "Gantt is visible");
        pc.showGantt();
        Assert.assertFalse(pc.getVariableAllocation().isVisible(), "Variable Allocation is visible");
        Assert.assertTrue(pc.getGantt().isVisible(), "Gantt is not visible");
        pc.showVariableAllocation();
        pc.hideVariableAllocation();
        Assert.assertFalse(pc.getVariableAllocation().isVisible(), "Variable Allocation is visible");
        Assert.assertFalse(pc.getGantt().isVisible(), "Gantt is visible");
        pc = WBSPage.openWBSPage(rootWork);
        Assert.assertFalse(pc.getTeamPane().isVisible(), "Team pane is present on Project Planning");
    }

    public void testAddSplit() {
        WBSPage pc = WBSPage.openWBSPage(rootWork);
        Assert.assertFalse(pc.getGrid().callSubMenu().isAddSplitPresent(), "There is 'Add split' before selecting resources");
        checkResourceColumns(pc);
        Assert.assertTrue(pc.getGrid().callSubMenu().isAddSplitPresent(), "Can't find 'Add split' after selecting resources");
    }

    public void testAssignRoleForWork() {
        WBSPage pc = WBSPage.openWBSPage(rootWork);
        pc.showTeamPane();
        checkResourceColumns(pc);

        List<WBSPage.TeamPaneItem> expected = new ArrayList<WBSPage.TeamPaneItem>();
        List<WBSPage.TeamPaneItem> before = pc.getTeamPane().getList();
        PSLogger.info("List before : " + before);
        PSLogger.info("List expected : " + expected);

        Assert.assertEquals(before, expected, "incorrect list of items before any actions");
        assignUserRole(pc, rootWork, role);
        List<WBSPage.TeamPaneItem> after = pc.getTeamPane().getList();
        PSLogger.info("List after : " + after);
        expected.add(role);
        Assert.assertEquals(after, expected, "Incorrect team-pane list after setting role " + role.getRole());
        pc.resetArea();
    }

    public void testAssignPersonForResource() {
        WBSPage pc = WBSPage.openWBSPage(rootWork, false);
        pc.getGrid().callSubMenu().addSplit();
        List<Work> resources = rootWork.splitResources();

        List<WBSPage.TeamPaneItem> expected = new ArrayList<WBSPage.TeamPaneItem>();
        List<WBSPage.TeamPaneItem> before = pc.getTeamPane().getList();
        PSLogger.info("List before : " + before);
        assignUserRole(pc, resources.get(0), user);
        List<WBSPage.TeamPaneItem> after = pc.getTeamPane().getList();
        PSLogger.info("List after : " + after);
        expected.addAll(before);
        UserRole user2 = (UserRole) user.clone();
        user2.setRole(BuiltInRole.CONTRIBUTOR); // now after assign person there is default role in grid
        expected.add(user2);
        Assert.assertEquals(after, expected, "Incorrect team-pane list after setting person " + user2.getPerson());
        pc.resetArea();
    }

    public void testAssignPersonAndRoleForResourcesAndSave() {
        WBSPage pc = WBSPage.openWBSPage(rootWork, false);
        pc.getGrid().callSubMenu().addSplit();
        List<Work> res = rootWork.splitResources();

        List<WBSPage.TeamPaneItem> before = pc.getTeamPane().getList();
        PSLogger.info("List before : " + before);
        assignUserRole(pc, res.get(0), unallocatedOwner);
        List<WBSPage.TeamPaneItem> after1 = pc.getTeamPane().getList();
        List<WBSPage.TeamPaneItem> expected = new ArrayList<WBSPage.TeamPaneItem>(before);
        expected.add(unallocatedOwner);
        Collections.sort(expected);
        Assert.assertEquals(after1, expected, "Incorrect team-pane list after setting  " + unallocatedOwner + " for " + res.get(0));

        assignUserRole(pc, res.get(1), userContributor);
        List<WBSPage.TeamPaneItem> after2 = pc.getTeamPane().getList();
        expected.add(userContributor);
        Collections.sort(expected);
        Assert.assertEquals(after2, expected, "Incorrect team-pane list after setting  " + userContributor + " for " + res.get(1));

        pc.saveArea();
        List<WBSPage.TeamPaneItem> after3 = pc.getTeamPane().getList();
        Assert.assertEquals(after3, expected, "Incorrect team-pane list after saving");
        setResources(res);
    }


    public void testAllocationAvailability() {
        WBSPage pc = WBSPage.openWBSPage(rootWork);
        pc.showTeamPane();
        validateTeamPaneValues(pc, true, null, unallocatedOwner, userContributor);
    }

    public void testSetAllocationForResourcesAndSave() {
        Work work = unallocatedOwner.getParent();
        Work res1 = unallocatedOwner.getResource();
        Work res2 = userContributor.getResource();

        float factorRes1 = 0.5f;
        float factorRes2 = 1.5f;

        WBSPage pc = WBSPage.openWBSPage(work, false);

        float allocation1 = pc.getGrid().getResourceAllocation(res1);
        float allocation2 = pc.getGrid().getResourceAllocation(res2);
        float allocation = pc.getGrid().getResourceAllocation(work);
        float allocationExpected = WBSPage.gridRound(allocation1 + allocation2);
        Assert.assertEquals(allocation, allocationExpected, "Incorrect total allocation for " + work);
        PSLogger.info("Allocation for " + res1 + " is " + allocation1);
        PSLogger.info("Allocation for " + res2 + " is " + allocation2);

        try {
            float newAllocation1 = allocation1 * factorRes1;
            float newAllocation2 = allocation2 * factorRes2;
            pc.getGrid().setResourceAllocation(res1, newAllocation1);
            pc.getGrid().setResourceAllocation(res2, newAllocation2);
            allocationExpected = WBSPage.gridRound(newAllocation1 + newAllocation2);
            allocation = pc.getGrid().getResourceAllocation(work);

            PSLogger.info("After setting work allocation is " + allocation);
            Assert.assertEquals(allocation, allocationExpected, "Incorrect total allocation for " + work);
            unallocatedOwner.setAllocation(newAllocation1);
            userContributor.setAllocation(newAllocation2);
            // do save :
            validateTeamPaneValues(pc, true, true, unallocatedOwner, userContributor);
            work.setAllocation(allocation);
        } catch (AssertionError e) {
            unallocatedOwner.setAllocation(allocation1);
            userContributor.setAllocation(allocation2);
            throw e;
        }
    }

    public void testChangeUserCalendarAndValidateAllocationAvailability() {
        Work work = parent.getParent();
        Work res1 = unallocatedOwner.getResource();
        Work res2 = userContributor.getResource();

        assignCalendarToUser(userContributor, work.getCalendar());

        WBSPage pc = WBSPage.openWBSPage(work, false);
        float allocation1was = pc.getGrid().getResourceAllocation(res1);
        float allocation2was = pc.getGrid().getResourceAllocation(res2);
        unallocatedOwner.setAllocation(allocation1was);
        userContributor.setAllocation(allocation2was);
        validateTeamPaneValues(pc, true, null, unallocatedOwner, userContributor);

        assignCalendarToUser(userContributor, work.getCalendar());

        pc = WBSPage.openWBSPage(work, false);
        float allocation1now = pc.getGrid().getResourceAllocation(res1);
        float allocation2now = pc.getGrid().getResourceAllocation(res2);
        unallocatedOwner.setAllocation(allocation1now);
        userContributor.setAllocation(allocation2now);
        Assert.assertEquals(allocation1now, allocation1was, "Incorrect allocation for " + res1);
        Assert.assertNotSame(allocation2now, allocation2was, "Incorrect allocation for " + res2);
        validateTeamPaneValues(pc, true, null, unallocatedOwner, userContributor);
    }

    public void testTeamPaneColors() {
        Work parent = userContributor.getParent();
        Set<Float> percents = new TreeSet<Float>();
        for (FiltersGridPage.TeamPaneCell cell : userContributor.getTeamPaneValuesInPercents()) {
            percents.add(cell.getAllocation());
        }
        PSLogger.debug("Percents to filter from page : " + percents);
        if (percents.size() < 2) PSSkipException.skip("No values to test");

        Integer redLimit = ((Float) percents.toArray()[percents.size() - 1]).intValue() - 1;
        Integer yellowLimit = ((Float) percents.toArray()[percents.size() - 2]).intValue() - 1;
        PSLogger.debug("Test values : " + yellowLimit + "," + redLimit);

        WBSPage pc = WBSPage.openWBSPage(parent, false);
        WBSPage.OptionsBlock.Filter filter = pc.getOptions().getFilter();
        filter = pc.getOptions().getFilter();
        filter.setTeamPaneYellow(yellowLimit);
        filter.setTeamPaneRed(redLimit);
        filter.apply();
        try {
            userContributor.setLimits(yellowLimit, redLimit);
            unallocatedOwner.setLimits(yellowLimit, redLimit);
            validateTeamPaneValues(pc, true, null, userContributor, unallocatedOwner);
        } finally {
            userContributor.setLimits(null, null);
            unallocatedOwner.setLimits(null, null);
        }
    }

    public void testVariableAllocationGrid() {
        Work work = parent.getParent();
        Work res1 = unallocatedOwner.getResource();
        Work res2 = userContributor.getResource();

        WBSPage pc = WBSPage.openWBSPage(work, false);
        float allocation1 = pc.getGrid().getResourceAllocation(res1);
        float allocation2 = pc.getGrid().getResourceAllocation(res2);
        unallocatedOwner.setAllocation(allocation1);
        userContributor.setAllocation(allocation2);
        pc.showVariableAllocation();

        validateVariableAllocationValues(pc, parent, unallocatedOwner, userContributor);

        PSCalendar period = userContributor.getFirstWeek();

        float allocationWas = userContributor.getAllocation(period);
        int toSet = 30;
        try {
            userContributor.setVAValue(period, toSet);
            PSLogger.info("Set new value for work " + res2 + " and period " + period + " : " + toSet);
            pc.getVariableAllocation().setValueForPeriod(period, res2, toSet);

            validateVariableAllocationValues(pc, parent, unallocatedOwner, userContributor);

            validateTeamPaneValues(pc, false, null, unallocatedOwner, userContributor);

            float exAllocation0 = WBSPage.gridRound(userContributor.getAllocation() + allocation1);
            float exAllocation2 = WBSPage.gridRound(userContributor.getAllocation());

            float allocation0 = pc.getGrid().getResourceAllocation(work);
            allocation2 = pc.getGrid().getResourceAllocation(res2);

            Assert.assertEquals(allocation2, exAllocation2,
                    "Incorrect allocation for " + res2 +
                            " in grid after setting new variable allocation (" + toSet + ")"
            );
            Assert.assertEquals(allocation0, exAllocation0,
                    "Incorrect allocation for " + work +
                            " in grid after setting new variable allocation (" + toSet + ")"
            );
            pc.resetArea();
        } finally {
            userContributor.setAllocation(period, allocationWas);
        }
    }


    public void testChangeValuesOnResourceReviewPage() {
        int toSet1 = 70;
        int toSet2 = 90;
        Work work = parent.getParent();

        Work res1 = userContributor.getResource();
        Work res2 = unallocatedOwner.getResource();

        List<PSCalendar> periods = userContributor.getPeriods();

        Work expected = ResourceReviewPage.getQuasiWorkTree(work);

        ResourceReviewPage rr = new ResourceReviewPage();
        rr.open();
        ResourceReviewPage.OptionsBlock.Filter filter = rr.getOptions().getRRFilter();
        filter.setAllocationRoles(unallocatedOwner.getRole().getName(), userContributor.getRole().getName());
        filter.setDemandRoles(userContributor.getRole().getName(), unallocatedOwner.getRole().getName());
        filter.apply();

        ResourceReviewPage.OptionsBlock.Display display = rr.getOptions().getRRDisplay();
        display.selectWeekly();
        display.checkDateRange();
        display.setStartDate(periods.get(0).getStartWeekDay().toString());
        display.setEndDate(periods.get(periods.size() - 1).getEndWeekDay().toString());
        display.apply();

        Work actual = rr.makeTreeByShowLevel();

        String treeFromPage = actual.print();
        PSLogger.info("From page : " + treeFromPage);
        PSLogger.info("Expected : " + expected.print());
        Assert.assertEquals(treeFromPage, expected.print(), "Incorrect grid structure");

        // validate RR values:
        validateResourceReviewValues(rr, unallocatedOwner, userContributor);


        PSCalendar week1 = parent.getFirstWeek();
        PSCalendar week2 = parent.getLastWeek();
        float allocationWas1 = userContributor.getAllocation(week1);
        float allocationWas2 = unallocatedOwner.getAllocation(week2);
        // set new values:
        try {
            PSLogger.info("Set new value for work " + res1 + " and period " + week1 + " : " + toSet1);
            userContributor.setVAValue(week1, toSet1);
            setResourceReviewValue(rr, userContributor, week1, toSet1);

            PSLogger.info("Set new value for work " + res2 + " and period " + week2 + " : " + toSet2);
            unallocatedOwner.setVAValue(week2, toSet2);
            setResourceReviewValue(rr, unallocatedOwner, week2, toSet2);

            // validate RR values after setting:
            validateResourceReviewValues(rr, userContributor, unallocatedOwner);

            rr.getGrid().getSaveButton().submit();
        } catch (AssertionError ae) {
            userContributor.setAllocation(week1, allocationWas1);
            unallocatedOwner.setAllocation(week2, allocationWas2);
        }
    }

    public void testTeamPaneAfterResourceReviewChanges() {
        Work work = unallocatedOwner.getParent();
        WBSPage pc = WBSPage.openWBSPage(work, false);
        validateTeamPaneValues(pc, true, null, unallocatedOwner, userContributor);
        checkResourcesSequence(pc, unallocatedOwner.getResource(), userContributor.getResource());
    }

    public void testHistogram() {
        Work work = unallocatedOwner.getParent();
        WBSPage pc = WBSPage.openWBSPage(work, false);
        pc.showHistogram();
        pc.getHistogram().waitForVisible();
        validateHistogramValues(pc, unallocatedOwner, userContributor);
    }


    public void testChangeConstraintDatesAndValidateVA() {
        int weeksToIncrease = 2;
        int weeksToDecrease = 1;

        Work work = unallocatedOwner.getParent();
        UserRole us1 = (UserRole) unallocatedOwner.clone();
        UserRole us2 = (UserRole) userContributor.clone();
        UserRole us0 = (UserRole) parent.clone();

        WBSPage pc = WBSPage.openWBSPage(work, false);
        WBSPage.Columns display = pc.getOptions().getColumns();
        display.getCheckbox(OPTIONS_BLOCK_COLUMNS_DATES_CONSTRAINT).click();
        display.apply();
        pc.showVariableAllocation();
        checkResourcesSequence(pc, us1.getResource(), us2.getResource());

        PSCalendar end = PSCalendar.getEmptyCalendar().set(work.getConstraintEndDate());
        PSCalendar start = PSCalendar.getEmptyCalendar().set(work.getConstraintStartDate());

        PSCalendar endToSet = end.set(7 * weeksToIncrease);
        PSLogger.info("Test setting indent constraint end date " + endToSet);
        pc.getGrid().getConstraintEndDatePicker(work).set(endToSet.toString());
        moveEndConstraintDate(weeksToIncrease, us0, us1, us2);
        validateVariableAllocationValues(pc, us0, us1, us2);

        PSCalendar startToSet = start.set(-7 * weeksToIncrease);
        PSLogger.info("Test setting outdent constraint start date " + startToSet);
        pc.getGrid().getConstraintStartDatePicker(work).set(startToSet.toString());
        moveStartConstraintDate(-weeksToIncrease, us0, us1, us2);
        validateVariableAllocationValues(pc, us0, us1, us2);

        validateTeamPaneValues(pc, false, null, us1, us2);

        pc.resetArea();
        us0 = (UserRole) parent.clone();
        us1 = (UserRole) unallocatedOwner.clone();
        us2 = (UserRole) userContributor.clone();

        endToSet = end.set(-7 * weeksToDecrease);
        PSLogger.info("Test setting outdent constraint end date " + endToSet);
        pc.getGrid().getConstraintEndDatePicker(work).set(endToSet.toString());
        moveEndConstraintDate(-weeksToDecrease, us0, us1, us2);
        validateVariableAllocationValues(pc, us0, us1, us2);

        startToSet = start.set(7 * weeksToDecrease);
        PSLogger.info("Test setting indent constraint start date " + startToSet);
        pc.getGrid().getConstraintStartDatePicker(work).set(startToSet.toString());
        moveStartConstraintDate(weeksToDecrease, us0, us1, us2);
        validateVariableAllocationValues(pc, us0, us1, us2);

        validateTeamPaneValues(pc, false, null, us1, us2);
        pc.resetArea();
    }

    private static void moveStartConstraintDate(int weeksNum, UserRole... teams) {
        for (UserRole team : teams) {
            if (weeksNum < 0) {
                team.decreaseLeftBound(-weeksNum);
            } else {
                team.increaseLeftBound(weeksNum);
            }
        }
    }

    private static void moveEndConstraintDate(int weeksNum, UserRole... teams) {
        for (UserRole team : teams) {
            if (weeksNum < 0) {
                team.decreaseRightBound(-weeksNum);
            } else {
                team.increaseRightBound(weeksNum);
            }
        }
    }

/*

    public void testTeamPaneWithDaysOff() {
        // todo: mnot ready method
        Work work = rootWork;
        List<PSCalendar> periods = UserRole.getPeriods(work);
        AuxiliaryTCs.setDaysOff(user1, periods.get(periods.size() - 2), periods.get(periods.size() - 1));

        WBSPage pc = AuxiliaryTCs.openWBSPage(work, false);
        pc.getGrid().callSubMenu().addSplit();
        Work res3 = work.initSplitResource(3);
        PSLogger.info("Grid List before : " + pc.getGrid().getListTree());
        List<WBSPage.TeamPaneItem> before = pc.getTeamPane().getList();
        PSLogger.info("Team Pane List before : " + before);
        pc.getGrid().setPerson(res3, user1);
        String person = pc.getGrid().getPerson(res3);
        Assert.assertEquals(person, user1.getFullName(), "Incorrect person after setting for work " + res3);
        List<WBSPage.TeamPaneItem> after = pc.getTeamPane().getList();
        PSLogger.info("Team Pane List after : " + after);
        pc.resetArea();
        AuxiliaryTCs.removeDaysOff(user1);
    }

*/

    private static void validateHistogramValues(WBSPage pc, UserRole... teams) {
        WBSPage.Histogram h = pc.getHistogram();
        for (UserRole team : teams) {
            List<PSCalendar> periods = team.getPeriods();
            PSCalendar first = periods.get(0);
            PSCalendar last = periods.get(periods.size() - 1);
            Role role = team.getRole();
            List<Integer> expected = team.getAvailabilities();
            List<Integer> fromPage = h.getAvailabilities(role, first, last);
            PSLogger.info("From page : " + fromPage);
            PSLogger.info("Expected : " + expected);
            Assert.assertEquals(fromPage, expected, "Incorrect Histogram values for " + team);
        }

    }

    private void checkResourcesSequence(WBSPage pc, Work res1, Work res2) {
        String roleFromPage = pc.getGrid().getRole(res2);
        if (roleFromPage.equals(res2.getResource().getRole().getName())) return;
        PSLogger.knis(73558);
        int index2 = res2.getResourceRowIndex();
        int index1 = res1.getResourceRowIndex();
        res2.setResourceRowIndex(index1);
        res1.setResourceRowIndex(index2);
    }

    private static void setTeamPanePercentage(WBSPage pc, Boolean doSave) {
        WBSPage.OptionsBlock.Filter filter = pc.getOptions().getFilter();
        filter.checkTeamPanePercentage();
        if (doSave != null)
            filter.apply(doSave);
        else
            filter.apply();
    }


    private static void validateVariableAllocationValues(WBSPage pc, UserRole... teams) {
        for (UserRole team : teams) {
            Work work = team.getResource();
            List<String> expected = team.getVAValues();
            List<String> fromPage = getVariableAllocationValues(pc, work, team.getPeriods());
            Assert.assertEquals(fromPage, expected, "Incorrect VA list for " + work);
        }
    }

    private static List<String> getVariableAllocationValues(WBSPage pc, Work work, List<PSCalendar> periods) {
        List<String> res = new ArrayList<String>();
        for (PSCalendar calendar : periods) {
            String value = pc.getVariableAllocation().getValueForPeriod(calendar, work);
            PSLogger.info(calendar + "(" + work.getName() + ") : " + value);
            res.add(value);
        }
        return res;
    }

    private static List<String> getResourceReviewValues(ResourceReviewPage rr, Work work, List<PSCalendar> periods) {
        List<String> res = new ArrayList<String>();
        for (PSCalendar period : periods) {
            String value = rr.getTable().getAllocationCell(period, work).getText();
            PSLogger.info(period + "(" + work.getName() + ") : " + value);
            res.add(value);
        }
        return res;
    }

    private static void setResourceReviewValue(ResourceReviewPage rr, UserRole user, PSCalendar week, float toSet) {
        rr.getTable().getAllocationCell(week, rr.getWorkRow(user.getParent(), user.getRole(), user.getPerson())).set(String.valueOf(toSet));
    }

    private void validateResourceReviewValues(ResourceReviewPage rr,
                                              UserRole... teams) {
        for (UserRole team : teams) {
            Work work = rr.getWorkRow(team.getParent(), team.getRole(), team.getPerson());
            List<String> expected = team.getRRValuesInHours();
            List<String> fromPage = getResourceReviewValues(rr, work, team.getPeriods());
            Assert.assertEquals(fromPage, expected, "Incorrect RR list for " + work);
        }
    }


    private static List<List> collectTeamPaneInfo(WBSPage pc, List<PSCalendar> periods) {
        pc.getTeamPane().getTable().scrollTo(periods.get(1));
        List<WBSPage.TeamPaneItem> list = pc.getTeamPane().getList();
        PSLogger.info("List from page : " + list); // to wait?
        List<List> res = new ArrayList<List>();
        for (PSCalendar period : periods) {
            List<FiltersGridPage.TeamPaneCell> fromPage = pc.getTeamPane().getTable().getColumnContent(period);
            PSLogger.info("Team Pane Values (Allocation/Availability) from page for period " + period + " : " +
                    fromPage);
            if (res.size() == 0) {
                for (int j = 0; j < fromPage.size(); j++) {
                    res.add(new ArrayList<FiltersGridPage.TeamPaneCell>());
                }
            }
            for (int j = 0; j < fromPage.size(); j++) {
                Assert.assertTrue(res.size() > j, "Incorrect column for period " + period + ": " + fromPage + ", expected " + res.size() + " elements");
                res.get(j).add(fromPage.get(j));
            }
        }
        return res;
    }

    private static void validateTeamPaneValues(WBSPage pc,
                                               Boolean checkPercentage,
                                               Boolean doSave,
                                               UserRole... teams) {
        if (teams.length == 0) return;
        List<List> fromPage = collectTeamPaneInfo(pc, teams[0].getPeriods());
        Assert.assertEquals(teams.length, fromPage.size(), "Incorrect number of rows specified");
        List<UserRole> rows = new ArrayList<UserRole>();
        for (int i = 0; i < teams.length; i++) {
            rows.add(teams[i]);
        }
        Collections.sort(rows);
        Assert.assertEquals(rows.size(), fromPage.size(), "Incorrect team-pane rows size");

        for (int i = 0; i < rows.size(); i++) {
            List<FiltersGridPage.TeamPaneCell> expected = rows.get(i).getTeamPaneValuesInHours();
            List<FiltersGridPage.TeamPaneCell> actual = fromPage.get(i);
            Assert.assertEquals(actual, expected, "Incorrect team-pane hours for row " + rows.get(i));
        }
        if (!checkPercentage) return;
        setTeamPanePercentage(pc, doSave);
        fromPage = collectTeamPaneInfo(pc, teams[0].getPeriods());
        Assert.assertEquals(teams.length, fromPage.size(), "Incorrect number of rows was specified");
        for (int i = 0; i < rows.size(); i++) {
            List<FiltersGridPage.TeamPaneCell> expected = rows.get(i).getTeamPaneValuesInPercents();
            List<FiltersGridPage.TeamPaneCell> actual = fromPage.get(i);
            Assert.assertEquals(actual, expected, "Incorrect team-pane percentage for row " + rows.get(i));
        }
    }

    private static void assignUserRole(WBSPage pc, Work work, UserRole team) {
        if (team == null) return;
        if (team.getRole() != null) {
            PSLogger.info("Set Role " + team.getRole() + " for child " + work);
            pc.getGrid().setRole(work, team.getRole().getName());
            String role = pc.getGrid().getRole(work);
            Assert.assertEquals(role, team.getRole().getName(), "Incorrect role after setting for work " + work);
        }
        if (team.getPerson() != null) {
            PSLogger.info("Set Person " + team.getPerson() + " for child " + work);
            pc.getGrid().setPerson(work, team.getPerson());
            String person = pc.getGrid().getPerson(work);
            Assert.assertEquals(person, team.getPerson().getFullName(), "Incorrect person after setting for work " + work);
        }
        //todo: checking that no other cells changed
    }

    private static void assignCalendarToUser(UserRole us, PSCalendar c) {
        UserManager.changeUserCalendar(us.getPerson(), c);
        us.setCalendar(c);
    }


    private void checkResourceColumns(WBSPage pc) {
        WBSPage.Columns columns = pc.getOptions().getColumns();
        columns.uncheckAll();
        columns.getCheckbox(OPTIONS_BLOCK_COLUMNS_RESOURCE).click();
        columns.apply();
    }

    private static void setResources(List<Work> res) {
        if (res == null)
            res = rootWork.splitResources();
        rootWork.addChild(res.get(0));
        rootWork.addChild(res.get(1));
        unallocatedOwner.setResource(res.get(0));
        userContributor.setResource(res.get(1));
    }

    public static List<UserRole> splitResources() {
        setResources(null);
        return Arrays.asList(unallocatedOwner, userContributor);
    }

}
