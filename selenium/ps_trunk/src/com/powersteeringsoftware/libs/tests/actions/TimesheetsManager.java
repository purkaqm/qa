package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Timesheets;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AbstractTimesheetsPage;
import com.powersteeringsoftware.libs.pages.PeopleManageTimePage;
import com.powersteeringsoftware.libs.pages.TimesheetsPage;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 30.09.12
 * Time: 21:19
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetsManager {

    public static TimesheetsPage open() {
        //UixManager.setResourcePlanningOn();
        TimesheetsPage page = new TimesheetsPage();
        page.open();
        return page;
    }

    public static AbstractTimesheetsPage open(Date date) {
        AbstractTimesheetsPage page = open();
        return page.setStartDateAndGo(date);
    }


    /**
     * @param t    Timesheets ConfigObject
     * @param save - if null then cancel, if true then save and submit, otherwise just save
     */
    public static void setTimesheets(Timesheets t, Boolean save) {
        AbstractTimesheetsPage _page;
        if (t.hasDate()) {
            _page = open(t.getDDate());
        } else {
            _page = open();
            t.setSDate(_page.getStartDate());
        }
        Assert.assertTrue(_page instanceof TimesheetsPage, "Should be editable page. some timesheets already seted for date " + t.getPSDate());
        TimesheetsPage page = (TimesheetsPage) _page;
        List<Timesheets.Line> lines = t.getLines();
        int rows = page.getTimesheetLinesNumber();
        PSLogger.debug("There are " + rows + " on page");
        if (rows < lines.size()) {
            page.pushAdd();
            int rows2 = page.getTimesheetLinesNumber();
            Assert.assertNotSame(rows, rows2, "Number of rows the same after adding new items");
        }

        for (int i = 0; i < lines.size(); i++) {
            Timesheets.Line line = lines.get(i);
            addLine(i, line, page);
            setLine(i, line, page);
        }
        AbstractTimesheetsPage res = save(save, page, t);
        if (save == null) return;
        Collections.sort(lines);
        // validate totals:
        for (int i = 0; i < lines.size(); i++) {
            Double total = res.getRowTotal(i);
            PSLogger.info("Total for line " + lines.get(i) + " is " + total);
            Timesheets.Line line = lines.get(i);
            Assert.assertEquals(total, line.getTotal(), "Incorrect total for line " + lines.get(i));
        }

        for (Timesheets.DayOfWeek day : Timesheets.DayOfWeek.values()) {
            Double total = res.getColumnTotalValue(day.ordinal());
            PSLogger.info("Total for day " + day + " is " + total);
            Assert.assertEquals(total, t.getTotal(day), "Incorrect total for day " + day);
        }
        Double total = res.getTotalValue();
        PSLogger.info("General total is " + total);
        Assert.assertEquals(total, t.getTotal(), "Incorrect total for timesheets " + t);
    }

    private static AbstractTimesheetsPage save(Boolean save, TimesheetsPage page, Timesheets t) {
        List<Timesheets.Line> lines = t.getLines();
        Timesheets.Status st;
        AbstractTimesheetsPage res = page;
        if (save == null) {
            page.pushCancel();
            return res;
        } else if (save) {
            res = page.pushSaveSubmit();
            st = Timesheets.Status.SUBMITTED;
        } else {
            page.pushSaveChanges();
            st = Timesheets.Status.NOT_SUBMITTED;
        }

        for (Timesheets.Line line : lines) {
            line.setStatus(st);
        }
        t.setCreated();
        return res;
    }

    private static void addLine(int i, Timesheets.Line line, TimesheetsPage page) {
        Work w = line.getWork();
        TimesheetsPage.WorkItemDialog dialog = page.openItemDialog(i);
        switch (line.howToSetWork()) {
            case FAVORITES:
                dialog.openFavorites();
                dialog.chooseWorkOnFavoritesTab(w);
                break;
            case BROWSE:
                dialog.openBrowseTab();
                dialog.chooseWorkOnBrowseTab(w);
                break;
            case SEARCH:
                dialog.openSearchTab();
                dialog.chooseWorkOnSearchTab(w);
                break;
            default:
                throw new IllegalArgumentException("How to set?");
        }

    }

    private static void setLine(int i, Timesheets.Line line, TimesheetsPage page) {
        for (Timesheets.DayOfWeek day : Timesheets.DayOfWeek.values()) {
            Double val = line.getValue(day);
            if (val == null) continue;
            page.fillTimeSheetCell(i, day.ordinal(), String.valueOf(val));
        }
        if (line.getActivity() != null)
            page.selectActivityOption(line.getActivity(), i);
        if (line.getBillingCategory() != null)
            page.selectBillingCategoryOption(line.getBillingCategory(), i);
    }

    public static Timesheets load(Date date) {
        AbstractTimesheetsPage page = open(date);
        return load(date, page);
    }

    private static Timesheets load(Date date, AbstractTimesheetsPage page) {
        Timesheets res = new Timesheets(date);
        for (int i = 0; ; i++) {
            Work w = getWork(i, page);
            if (w == null) break;
            Timesheets.Line line = res.addLine(w);
            String activity = page.getActivity(i);
            if (!activity.isEmpty()) line.setActivity(activity);
            for (Timesheets.DayOfWeek day : Timesheets.DayOfWeek.values()) {
                String val = page.getTimeSheetCellValue(i, day.ordinal());
                if (val.isEmpty()) continue;
                line.setValue(day, Double.parseDouble(val));
            }
            line.setStatus(page.getStatus(i));
        }
        return res;
    }

    private static Work getWork(int i, AbstractTimesheetsPage page) {
        String wName = page.getWorkItemName(i);
        if (wName == null || wName.isEmpty()) return null;

        Work w = null;
        String wFullName = page.getLocation(i);
        PSLogger.info("Full Project path is " + wFullName);
        for (Work _w : TestSession.getWorkList()) {
            if (_w.getFullName().equals(wFullName)) {
                w = _w;
                break;
            }
        }
        if (w == null) {
            PSLogger.warn("Can't find work with full name " + wFullName);
            w = new Work(wName);
        }
        return w;
    }

    public static void approveAll(Timesheets ts) {
        PeopleManageTimePage.AwaitingApproval page = new PeopleManageTimePage.AwaitingApproval();
        page.open();
        page.navigateStartDate(ts.getDDate());
        page.expandTimesheet(0);
        validateTotals(page, ts, 0);
        page.checkAllStatuses(0);
        page.pushApprove();
        for (Timesheets.Line line : ts.getLines()) {
            line.setStatus(Timesheets.Status.APPROVED);
        }
    }

    public static void rejectAll(Timesheets ts, String desc) {
        PeopleManageTimePage.AwaitingApproval page = new PeopleManageTimePage.AwaitingApproval();
        page.open();
        page.navigateStartDate(ts.getDDate());
        page.expandTimesheet(0);
        validateTotals(page, ts, 0);
        page.checkAllStatuses(0);
        page.pushReject(desc);
        for (Timesheets.Line line : ts.getLines()) {
            line.setStatus(Timesheets.Status.REJECTED);
        }
    }

    public static void unApproveAll(Timesheets ts) {
        PeopleManageTimePage.Approved page = new PeopleManageTimePage.Approved();
        page.open();
        page.navigateStartDate(ts.getDDate());
        page.expandTimesheet(0);
        validateTotals(page, ts, 0);
        page.checkAllStatuses(0);
        page.pushUnApprove();
        for (Timesheets.Line line : ts.getLines()) {
            line.setStatus(Timesheets.Status.SUBMITTED);
        }
    }

    private static void validateTotals(PeopleManageTimePage page, Timesheets ts, int user) {
        List<Timesheets.Line> lines = ts.getLines();
        Collections.sort(lines);
        // validate totals:
        for (int i = 0; i < lines.size(); i++) {
            Double total = page.getWorkTotalForUser(user, i);
            PSLogger.info("Total for line " + lines.get(i) + " is " + total);
            Timesheets.Line line = lines.get(i);
            Assert.assertEquals(total, line.getTotal(), "Incorrect total for line " + lines.get(i));
        }

        for (Timesheets.DayOfWeek day : Timesheets.DayOfWeek.values()) {
            Double total = page.getColumnTotal(user, day.ordinal());
            PSLogger.info("Total for day " + day + " is " + total);
            Assert.assertEquals(total, ts.getTotal(day), "Incorrect total for day " + day);
        }
        Double total = page.getOverallTotal(user);
        PSLogger.info("General total is " + total);
        Assert.assertEquals(total, ts.getTotal(), "Incorrect total for timesheets " + ts);

    }

    public static void validate(Timesheets expected, Timesheets actual) {
        Assert.assertEquals(expected.getDate(), actual.getDate(), "Incorrect date for " + actual);
        List<Timesheets.Line> lines1 = expected.getLines();
        List<Timesheets.Line> lines2 = actual.getLines();
        Assert.assertEquals(lines1.size(), lines2.size(), "Incorrect timesheets lines for " + actual);
        Collections.sort(lines1);
        Collections.sort(lines2);
        for (int i = 0; i < lines1.size(); i++) {
            Timesheets.Line line1 = lines1.get(i);
            Timesheets.Line line2 = lines2.get(i);
            Assert.assertEquals(line1.getName(), line2.getName(), "Incorrect line #" + i + " for " + actual);
            Assert.assertEquals(line1.getStatus(), line2.getStatus(), "Incorrect status #" + i + " for " + line2);
            Assert.assertEquals(line1.getActivity(), line2.getActivity(), "Incorrect activity #" + i + " for " + line2);
            for (Timesheets.DayOfWeek day : Timesheets.DayOfWeek.values()) {
                Assert.assertEquals(line1.getValue(day), line2.getValue(day), "Incorrect value #" + i + " for " + line2 + " and day " + day);
            }
        }
    }

    public static void copyLast(Timesheets was, Timesheets toSet, Boolean save) {
        if (!was.hasDate()) throw new IllegalArgumentException("Timesheets was should has a date");
        PSLogger.info("Timesheets was: " + was);
        AbstractTimesheetsPage _page = open();
        Assert.assertTrue(_page instanceof TimesheetsPage, "Should be editable page on first open.");
        TimesheetsPage page = (TimesheetsPage) _page;
        page.clickCopyLast();

        Timesheets ex = new Timesheets();
        List<Timesheets.Line> linesWas = was.getLines();

        for (Timesheets.Line l : linesWas) {
            ex.addLine(l.getWork()).setActivity(l.getActivity());
        }
        PSCalendar c = was.getPSDate().set(7); // <-- next week
        ex.setSDate(c.toString());

        String d = page.getStartDate();
        Date date = was.getPSDate().set(d).getDate();
        Timesheets act = load(date, page);
        validate(ex, act);

        toSet.setSDate(ex.getDate());

        List<Timesheets.Line> lines = toSet.getLines();
        for (int i = 0; i < lines.size(); i++) {
            Work w = getWork(i, page);
            if (!lines.get(i).getWork().equals(w)) addLine(i, lines.get(i), page);
            setLine(i, lines.get(i), page);
            lines.get(i).setStatus(Timesheets.Status.NOT_SUBMITTED);
        }
        save(save, page, toSet);
    }
}
