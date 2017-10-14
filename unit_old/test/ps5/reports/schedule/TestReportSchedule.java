/*
 * Copyright (c) PowerSteering Software 2006
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
package ps5.reports.schedule;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.ps3.test.TestPS;

/**
 * 
 * @author amansuri (Jul 4, 2006)
 */
public class TestReportSchedule extends TestPS {

    private static final Date now;

    private static final Date yesterday;

    private static final Date tomorrow;

    private static final Date nextMonth;

    private static final Date lastDayOfYear;

    private static final Date firstDayNextOfYear;

    private static final Date lastDayOfNextFeb;

    private static final Date lastSunday;

    private static final Date nextSunday;
    static {
        // set up start dates
        Calendar cal = Calendar.getInstance();
        now = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        yesterday = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        tomorrow = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        nextMonth = cal.getTime();
        cal.set(Calendar.MONDAY, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        lastDayOfYear = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        firstDayNextOfYear = cal.getTime();
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, cal
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        lastDayOfNextFeb = cal.getTime();
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        lastSunday = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        nextSunday = cal.getTime();
    }

    private static final Date[] startDates = { lastDayOfYear, yesterday, now, tomorrow, nextMonth,
            firstDayNextOfYear, lastDayOfNextFeb, lastSunday, nextSunday };
    
    
    public TestReportSchedule(String name) {
        super(name);

    }

    public void testRecurrences() throws ReportScheduleException {
        ReportScheduleBuilder builder;
        TimeZone tz = TimeZone.getDefault();

        try {
            builder = new ReportScheduleBuilder();
            builder.setRecurrence(null);
            fail("should have thrown an exception on null");
        } catch (RuntimeException e) {
        }

        try {
            builder = new ReportScheduleBuilder();
            builder.setRecurrence("        ");
            fail("should have thrown an exception on empty");
        } catch (RuntimeException e) {
        }

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(tz);
        builder.setRecurrence("blah");
        assertTrue("expected null schedule",
                builder.build() instanceof NullReportSchedule);

        builder = new ReportScheduleBuilder();
        builder.setRecurrence(NullReportSchedule.RECURRENCE_TYPE);
        assertTrue("expected null schedule",
                builder.build() instanceof NullReportSchedule);

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(tz);
        builder.setRecurrence(DailySchedule.RECURRENCE_TYPE);
        builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);
        assertTrue("expected daily schedule",
                builder.build() instanceof DailySchedule);

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(tz);
        builder.setWeekDays(new boolean[7]);
        builder.setRecurrence(WeeklySchedule.RECURRENCE_TYPE);
        assertTrue("expected weekly schedule",
                builder.build() instanceof WeeklySchedule);

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(tz);
        builder.setRecurrence(MonthlySchedule.RECURRENCE_TYPE);
        builder.setMonthlyOption(MonthlySchedule.DAYOFMONTH_OPTION);
        assertTrue("expected monthly schedule",
                builder.build() instanceof MonthlySchedule);

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(tz);
        builder.setRecurrence(YearlySchedule.RECURRENCE_TYPE);
        builder.setYearlyOption(YearlySchedule.BY_MONTH_OPTION);
        assertTrue("expected yearly schedule",
                builder.build() instanceof YearlySchedule);

    }

    public void testScheduleEndCreate() throws ReportScheduleException {

        final String[] types = { DailySchedule.RECURRENCE_TYPE,
                WeeklySchedule.RECURRENCE_TYPE,
                MonthlySchedule.RECURRENCE_TYPE, YearlySchedule.RECURRENCE_TYPE };
        for (String recurrenceType : types) {

            ReportScheduleBuilder builder;
            ReportSchedule sched;

            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            builder.setRecurrence(recurrenceType);
            setupSpecificOptions(builder);
            builder.setEndType(null);
            assertTrue("expected NoScheduleEnd", builder.build().getScheduleEnd() instanceof NoScheduleEnd);

            try {
                builder = new ReportScheduleBuilder();
                builder.setTimeZone(TimeZone.getDefault());
                builder.setRecurrence(recurrenceType);
                builder.setEndType("haasdsdf");
                fail("Expected failure with bad end type");
            } catch (RuntimeException e) {
            }

            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            builder.setRecurrence(recurrenceType);
            setupSpecificOptions(builder);
            builder.setEndType(NoScheduleEnd.ID);
            assertTrue("expected NoScheduleEnd",
                    builder.build().getScheduleEnd() instanceof NoScheduleEnd);

            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            builder.setRecurrence(recurrenceType);
            setupSpecificOptions(builder);
            builder.setEndType(ScheduleEndByOccurrences.ID);
            assertTrue("expected ScheduleEndByOccurrences", builder.build()
                    .getScheduleEnd() instanceof ScheduleEndByOccurrences);
            sched = builder.build();
            ScheduleEndByOccurrences endOcc = (ScheduleEndByOccurrences) sched
                    .getScheduleEnd();
            assertTrue(
                    "By default end after occurrences must be greater than 0",
                    endOcc.getNumEndAfterOccurrences() > 0);
            assertTrue("By default num occurrences must be 0", endOcc
                    .getNumOccurrences() == 0);
            assertTrue("Not at end by default", !endOcc.isAtEnd(null));

            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            builder.setRecurrence(recurrenceType);
            setupSpecificOptions(builder);
            builder.setEndType(ScheduleEndByOccurrences.ID);
            builder.setEndAfterOccurrences(12);
            sched = builder.build();
            endOcc = (ScheduleEndByOccurrences) sched.getScheduleEnd();
            assertTrue("By default num occurrences must be 0", endOcc
                    .getNumOccurrences() == 0);
            assertTrue("End after should be 12", 12 == endOcc
                    .getNumEndAfterOccurrences());
            assertTrue("Not at end by default", !endOcc.isAtEnd(null));
            for (int i = 0; i < 11; i++) {
                endOcc.notifySkip();
                assertTrue("Not at end after " + i + " runs", !endOcc
                        .isAtEnd(null));
            }
            endOcc.notifySkip();
            assertTrue("Should be at end now", endOcc.isAtEnd(null));

            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            setupSpecificOptions(builder);
            builder.setRecurrence(recurrenceType);
            builder.setEndType(ScheduleEndByDate.ID);
            assertTrue("expected ScheduleEndByDate", builder.build()
                    .getScheduleEnd() instanceof ScheduleEndByDate);

        }
    }

    /**
     * @param builder
     */
    private void setupSpecificOptions(ReportScheduleBuilder builder) {
        // if daily
        builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);
        // if weekly
        final boolean[] weekdays = new boolean[7];
        weekdays[0] = true;
        builder.setWeekDays(weekdays);
        builder.setNumWeeks(1);
        // if monthly
        builder.setMonthlyOption(MonthlySchedule.DAYOFMONTH_OPTION);
        // if yearly
        builder.setYearlyOption(YearlySchedule.BY_DAY_OPTION);
    }

    public void testNoEnd() throws ReportScheduleException {
        ReportScheduleBuilder builder;
        ReportSchedule sched;

        // NullReportSchedules are always at end and always compute null dates
        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(NullReportSchedule.RECURRENCE_TYPE);
        sched = builder.build();

        assertTrue("must be at end", sched.getScheduleEnd().isAtEnd(null));
        for (int i = 0; i < 1000; i++) {
            Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
            assertTrue("date must be null", null == date);
            assertTrue("must be at end", sched.getScheduleEnd().isAtEnd(date));
        }
    }

    public void testEndByXOccurrences() throws ReportScheduleException {

        ReportScheduleBuilder builder;
        ReportSchedule sched;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());

        // Daily, weekly, monthly, yearly depend on end type
        final String[] types = { DailySchedule.RECURRENCE_TYPE,
                WeeklySchedule.RECURRENCE_TYPE,
                MonthlySchedule.RECURRENCE_TYPE, YearlySchedule.RECURRENCE_TYPE };
        for (String recurrenceType : types) {
            final int LIMIT = recurrenceType
                    .equals(YearlySchedule.RECURRENCE_TYPE) ? 20 : 1000; // 20
            // years
            // is
            // plenty
            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            int month = 2;
            builder.setStartDate(new GregorianCalendar(2006, 2, 28).getTime());
            builder.setRecurrence(recurrenceType);
            if(MonthlySchedule.RECURRENCE_TYPE.equals(recurrenceType)) {
            	builder.setMonthlyNumMonths(month);
            }
            setupSpecificOptions(builder);
            // test run forever
            builder.setEndType(NoScheduleEnd.ID);
            sched = builder.build();
            assertTrue("must not be at end", !sched.getScheduleEnd().isAtEnd(null));
            for (int i = 0; i < LIMIT; i++) {
                Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                assertTrue("date must not be null", null != date);
                assertTrue("date must not be at end", !sched.getScheduleEnd()
                        .isAtEnd(date));
            }

            // test run X times
            final int[] TIMES = { 1, 2, 30, 31, 32, 500 };
            for (int times : TIMES) {
                builder.setEndType(ScheduleEndByOccurrences.ID);
                builder.setEndAfterOccurrences(times);
                sched = builder.build();
                assertTrue("must not be at end", !sched.getScheduleEnd().isAtEnd(
                        null));
                for (int i = 0; i < times - 1; i++) {
                    Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                    assertTrue("date must not be null", null != date);
                    assertTrue("date must not be at end", !sched.getScheduleEnd()
                            .isAtEnd(date));
                }
                Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                assertTrue("date must not be null", null != date);
                assertTrue("must be at end", sched.getScheduleEnd().isAtEnd(date));

                date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                assertTrue("date must be null", null == date);
                assertTrue("must be at end", sched.getScheduleEnd().isAtEnd(date));
            }

        }

    }

    public void testEndDate() throws ReportScheduleException {

        ReportScheduleBuilder builder;
        ReportSchedule sched;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());

        // set up start dates
        Calendar cal = Calendar.getInstance();
        // final Date now = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        // final Date yesterday = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 2);
        int currentMonth = cal.get(Calendar.MONTH);
        final Date tomorrow = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        final Date nextMonth = cal.getTime();
        cal.set(Calendar.MONDAY, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        final Date lastDayOfYear = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        final Date firstDayNextOfYear = cal.getTime();
        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
        cal.set(Calendar.DAY_OF_MONTH, cal
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        final Date lastDayOfNextFeb = cal.getTime();
        final Date[] endDates = { tomorrow, nextMonth, lastDayOfYear,
                firstDayNextOfYear, lastDayOfNextFeb };

        // Daily, weekly, monthly, yearly depend on end type
        final String[] types = { DailySchedule.RECURRENCE_TYPE,
                WeeklySchedule.RECURRENCE_TYPE,
                MonthlySchedule.RECURRENCE_TYPE, YearlySchedule.RECURRENCE_TYPE };
        for (String recurrenceType : types) {
            final int LIMIT = recurrenceType
                    .equals(YearlySchedule.RECURRENCE_TYPE) ? 20 : 1000; // 20
            // years
            // is
            // plenty
            builder = new ReportScheduleBuilder();
            builder.setTimeZone(TimeZone.getDefault());
            final Date startDate = new GregorianCalendar(2006, 2, 28, 0, 0, 0)
                    .getTime();
            builder.setStartDate(startDate);
            builder.setRecurrence(recurrenceType);
            setupSpecificOptions(builder);
            if(MonthlySchedule.RECURRENCE_TYPE.equals(recurrenceType)) {
            	builder.setMonthlyNumMonths(currentMonth);
            }
            // test run forever
            builder.setEndType(NoScheduleEnd.ID);
            sched = builder.build();
            assertTrue("must not be at end", !sched.getScheduleEnd().isAtEnd(null));
            for (int i = 0; i < LIMIT; i++) {
                Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                assertTrue("date must not be null", null != date);
                assertTrue("date must not be at end", !sched.getScheduleEnd()
                        .isAtEnd(date));
            }

            // test run X times
            final int[] TIMES = { 1, 2, 30, 31, 32, 500 };
            for (int times : TIMES) {
                for (Date endDate : endDates) {
                    builder.setEndType(ScheduleEndByDate.ID);
                    builder.setEndDate(endDate);
                    sched = builder.build();
                    if (startDate.after(endDate)) {
                        assertTrue("must be at end", sched.getScheduleEnd()
                                .isAtEnd(null));
                        continue;
                    } else {
                        assertTrue("must not be at end", !sched.getScheduleEnd()
                                .isAtEnd(null));
                    }
                    for (int i = 0; i < times; i++) {
                        Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                        // System.out.println("times " + times + " i " + i);
                        assertTrue("date " + date
                                + " must not be after end date " + endDate,
                                date == null || !date.after(endDate));
                        assertTrue("date must not be at end",
                                (date == null) == sched.getScheduleEnd().isAtEnd(
                                        date));
                    }
                }
            }

        }

    }

    /**
     * This test sets the interval to different interesting numbers and then
     * checks that the dates produced respect the established intervals.
     * @throws ReportScheduleException 
     * 
     */
    public void testDailyEveryXInterval() throws ReportScheduleException {

        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(DailySchedule.RECURRENCE_TYPE);
        builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);

        final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };
        final int[] dayIntervals = { 1, 2, 7, 30, 31, 365 };
        for (Date startDate : startDates) {
            for (int time : times) {
                for (int interval : dayIntervals) {
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);
                    startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                    startCal.set(Calendar.MINUTE, time % 100);
                    startCal.set(Calendar.SECOND, 0);
                    builder.setStartDate(startCal.getTime());
                    builder.setEndType(NoScheduleEnd.ID);
                    builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);
                    builder.setDayInterval(interval);

                    final ReportSchedule sched = builder.build();

                    Date date;
                    /*
                     * if (interval == 30) { System.out.println("here"); }
                     */
                    date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                    assertTrue("date must not be null", null != date);
                    assertTrue("date must not be at end", !sched.getScheduleEnd()
                            .isAtEnd(null));

                    assertTrue("date must equal start date "
                            + startCal.getTime() + " but was " + date, date
                            .equals(startCal.getTime()));
                    assertTrue("num ocurrences must be 1", sched.getScheduleEnd()
                            .getNumOccurrences() == 1);

                    date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                    assertTrue("date must not be null", null != date);
                    assertTrue("date must not be at end", !sched.getScheduleEnd()
                            .isAtEnd(date));

                    assertTrue("date must be after start date", date
                            .after(startCal.getTime()));
                    assertTrue("num ocurrences must be 2", sched.getScheduleEnd()
                            .getNumOccurrences() == 2);

                    assertTrue("interval should be " + interval + " days. Was "
                            + date + " startdate " + startCal.getTime(), ((date
                            .getTime() - startCal.getTime().getTime())
                            - (1000L * 3600L * interval * 24L) < 0.05));
                }
            }
        }

    }

    public void testDailyEveryWeekday() throws ReportScheduleException {
        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(DailySchedule.RECURRENCE_TYPE);
        builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);

        //outdated
        //final ResCalendar rcal = getContext().getResCalendar();
        //final List daysOff = rcal.getDaysOff();

        //assertTrue("incorrect to assume Sunday was a day off", daysOff.indexOf(Calendar.SUNDAY) != -1);

        final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };
        for (Date startDate : startDates) {
            for (int time : times) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                startCal.set(Calendar.MINUTE, time % 100);
                startCal.set(Calendar.SECOND, 0);
                builder.setStartDate(startCal.getTime());
                builder.setEndType(NoScheduleEnd.ID);
                builder.setDayOption(DailySchedule.DAYOPTION_EVERYWEEKDAY);

                final ReportSchedule sched = builder.build();

                Date date;
                date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                assertTrue("date must not be null", null != date);
                assertTrue("date must not be at end", !sched.getScheduleEnd()
                        .isAtEnd(null));

                assertTrue("date must equal start date " + startCal.getTime()
                        + " but was " + date, date.equals(startCal.getTime()));
                assertTrue("num ocurrences must be 1", sched.getScheduleEnd()
                        .getNumOccurrences() == 1);

                // ensure date is not a weekend
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                // assertTrue("date is not a weekend", -1 ==
                // daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));

                Date oldDate = startCal.getTime();
                for (int i = 2; i <= 1000; i++) {

                    date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                    assertTrue("date must not be null", null != date);
                    assertTrue("date must not be at end", !sched.getScheduleEnd()
                            .isAtEnd(date));

                    assertTrue("date must be after start date", date
                            .after(oldDate));
                    assertTrue("num ocurrences must be " + i, sched
                            .getScheduleEnd().getNumOccurrences() == i);

                    // ensure date is not a weekend
                    cal = Calendar.getInstance();
                    cal.setTime(date);
                    //assertTrue("date is not a weekend", -1 == daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));

                    oldDate = date;
                }
            }
        }

    }

    /**
     * This is a really huge test that tries out 32 million combinations dates
     * to try to find a bug in the week scheduler
     * 
     * (This test is very slow so we sometimes disable it by changing the method
     * name)
     * @throws ReportScheduleException 
     */
    public void disabledtestEveryXWeeks() throws ReportScheduleException {
        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(WeeklySchedule.RECURRENCE_TYPE);
        builder.setDayOption(DailySchedule.DAYOPTION_EVERYDAY);

        final boolean[][] weekDayOptions = new boolean[127][];
        // generate all 127 possible weekday options
        for (int i = 0; i < 127; i++) {
            weekDayOptions[i] = createWeekSelection(i + 1);
        }

        // final boolean[][] weekDayOptions = { {false, false, false, false,
        // false, false, true}};
        for (boolean[] selectedWeekdays : weekDayOptions) {
            builder.setWeekDays(selectedWeekdays);
            final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };
            for (Date startDate : startDates) {
                for (int time : times) {
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);
                    startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                    startCal.set(Calendar.MINUTE, time % 100);
                    startCal.set(Calendar.SECOND, 0);
                    builder.setStartDate(startCal.getTime());
                    builder.setEndType(NoScheduleEnd.ID);

                    final int[] recurrences = { 1, 2, 3, 4 };
                    for (int recurrence : recurrences) {
                        builder.setNumWeeks(recurrence);

                        final ReportSchedule sched = builder.build();
                        // discard first because it's weird
                        sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                        Date lastDate = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastDate);
                        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                        int lastYearMaxNumWeeks = cal
                                .getActualMaximum(Calendar.WEEK_OF_YEAR);
                        for (int i = 0; i < 1000; i++) {
                            final Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                            cal.setTime(date);

                            // ok.. don't exaggerate.. there could be Java bugs
                            // beyond 2037
                            if (cal.get(Calendar.YEAR) > 2037) {
                                // break out of repetitions loop
                                break;
                            }

                            if (isOnlyOneDay(selectedWeekdays)) {
                                long weekDifference = ((date.getTime() - lastDate
                                        .getTime()) / (1000L * 3600L * 24L * 7L));
                                // lookout for daylight savings changes (diff
                                // changes under these circumstances to
                                // plus or minus 1 hour)
                                if (cal.getTimeZone().inDaylightTime(date)
                                        && !cal.getTimeZone().inDaylightTime(
                                                lastDate)) {
                                    weekDifference = ((date.getTime() - lastDate
                                            .getTime()) / (1000L * 3600L * 24L * 7L - cal
                                            .getTimeZone().getDSTSavings()));
                                }
                                /*
                                 * System.out.print("Rec " + recurrence + " i " +
                                 * i + " date " + date + " selected days " +
                                 * selectedDays(selectedWeekdays)); System.out
                                 * .println(" lastdate " + lastDate + " diff " +
                                 * weekDifference + " detail " + (date.getTime() -
                                 * lastDate .getTime()));
                                 */
                                assertTrue("there should be a difference of "
                                        + recurrence + " instead of "
                                        + weekDifference,
                                        recurrence == weekDifference);
                            }
                            if (i == 76) {
                                // System.out.println(" here!");
                            }
                            assertTrue(
                                    "the week of the year cannot be more or less than than "
                                            + recurrence + " weeks difference",
                                    (0 == ((cal.get(Calendar.WEEK_OF_YEAR) - weekOfYear) % recurrence))
                                            // exception: the last day of the
                                            // year can be on the first day of
                                            // next year
                                            || ((cal.get(Calendar.WEEK_OF_YEAR) < weekOfYear) && (0 == ((cal
                                                    .get(Calendar.WEEK_OF_YEAR)
                                                    + lastYearMaxNumWeeks - weekOfYear) % recurrence))));
                            assertTrue("the day in " + date
                                    + " must always be a selected day "
                                    + selectedDays(selectedWeekdays),
                                    selectedWeekdays[cal
                                            .get(Calendar.DAY_OF_WEEK) - 1]);

                            lastDate = date;
                            cal.setTime(lastDate);
                            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
                            lastYearMaxNumWeeks = cal
                                    .getActualMaximum(Calendar.WEEK_OF_YEAR);
                        }
                    }
                }
            }
        }

    }

    public void testDayXOfEveryYMonths() throws ReportScheduleException {
        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(MonthlySchedule.RECURRENCE_TYPE);
        builder.setMonthlyOption(MonthlySchedule.DAYOFMONTH_OPTION);

        final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };
        for (Date startDate : startDates) {
            for (int time : times) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                startCal.set(Calendar.MINUTE, time % 100);
                startCal.set(Calendar.SECOND, 0);
                builder.setStartDate(startCal.getTime());
                builder.setEndType(NoScheduleEnd.ID);
                for (int dayOfMonth = 1; dayOfMonth <= 31; dayOfMonth++) {
                    builder.setMonthlyDayOfMonth(dayOfMonth);
                    for (int numMonths = 1; numMonths <= 4; numMonths++) {
                        builder.setMonthlyNumMonths(numMonths);

                        final ReportSchedule sched = builder.build();
                        // discard first because it's weird
                        sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                        Date lastDate = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastDate);
                        int lastMonth = cal.get(Calendar.MONTH);
                        int lastYear = cal.get(Calendar.YEAR);
                        for (int i = 0; i < 120; i++) {
                            final Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                            cal.setTime(date);

                            // ok.. don't exaggerate.. there could be Java bugs
                            // beyond 2037
                            if (cal.get(Calendar.YEAR) > 2037) {
                                // break out of repetitions loop
                                break;
                            }

                            // look for special cases
                            if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) < dayOfMonth) {
                                if (cal.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                                    assertTrue(
                                            "last day should be greatest day of february but was "
                                                    + date,
                                            cal.get(Calendar.DAY_OF_MONTH) == cal
                                                    .getActualMaximum(Calendar.DAY_OF_MONTH));
                                } else {
                                    assertTrue(
                                            "BUG IN TEST: oops expected dayOfMonth to be 31 but was "
                                                    + dayOfMonth,
                                            dayOfMonth == 31);
                                    assertTrue(
                                            "last day should be 30th of month but was "
                                                    + date,
                                            cal.get(Calendar.DAY_OF_MONTH) == 30);
                                }
                                // common case
                            } else {
                                assertTrue(
                                        "Expected day to be " + dayOfMonth
                                                + " for " + lastDate
                                                + " numMonths " + numMonths
                                                + " instead was " + date,
                                        cal.get(Calendar.DAY_OF_MONTH) == dayOfMonth);
                            }

                            if (cal.get(Calendar.YEAR) == lastYear) {
                                assertTrue("Month should have increased by "
                                        + numMonths + " but instead went from "
                                        + lastDate + " to " + date, cal
                                        .get(Calendar.MONTH) == lastMonth
                                        + numMonths);
                            } else {
                                assertTrue(
                                        "Month should have increased to "
                                                + (Calendar.JANUARY
                                                        + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))
                                                + " but went from " + lastDate
                                                + " to " + date,
                                        (cal.get(Calendar.YEAR) == lastYear + 1)
                                                && (cal.get(Calendar.MONTH) == (Calendar.JANUARY
                                                        + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))));
                            }

                            lastDate = date;
                            lastMonth = cal.get(Calendar.MONTH);
                            lastYear = cal.get(Calendar.YEAR);
                        }
                    }
                }
            }
        }
    }

    public void testXstPeriodOfEveryYMonths() throws ReportScheduleException {
        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(MonthlySchedule.RECURRENCE_TYPE);
        builder.setMonthlyOption(MonthlySchedule.NAMEDDAYOFMONTH_OPTION);

        //final ResCalendar rcal = getContext().getResCalendar();
        //final List daysOff = rcal.getDaysOff();

        final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };

        final String[] namedDays = { MonthlySchedule.NAMEDDAY_DAY,
                MonthlySchedule.NAMEDDAY_WEEKDAY,
                MonthlySchedule.NAMEDDAY_WEEKEND, "" + Calendar.SUNDAY,
                "" + Calendar.MONDAY, 
                "" + Calendar.TUESDAY,
                "" + Calendar.WEDNESDAY, "" + Calendar.THURSDAY,
                "" + Calendar.FRIDAY, "" + Calendar.SATURDAY,
                "" + Calendar.SUNDAY };

        for (Date startDate : startDates) {
            for (int time : times) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                startCal.set(Calendar.MINUTE, time % 100);
                startCal.set(Calendar.SECOND, 0);
                builder.setStartDate(startCal.getTime());
                builder.setEndType(NoScheduleEnd.ID);
                for (int numDay = 1; numDay <= 4; numDay++) {
                    builder.setMonthlyNumDay("" + numDay);

                    for (String namedDay : namedDays) {
                        builder.setMonthlyNamedDay(namedDay);

                        for (int numMonths = 1; numMonths <= 4; numMonths++) {
                            builder.setMonthlyNumMonths2(numMonths);

                            final ReportSchedule sched = builder.build();
                            // discard first because it's weird
                            sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                            Date lastDate = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());

                            if (numMonths == 1 && numDay == 2) {
                                // System.out.print("here");
                            }

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(lastDate);
                            int lastMonth = cal.get(Calendar.MONTH);
                            int lastYear = cal.get(Calendar.YEAR);
                            for (int i = 0; i < 120; i++) {
                                final Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                                cal.setTime(date);

                                // ok.. don't exaggerate.. there could be Java
                                // bugs
                                // beyond 2037
                                if (cal.get(Calendar.YEAR) > 2037) {
                                    // break out of repetitions loop
                                    break;
                                }

                                if (MonthlySchedule.NAMEDDAY_DAY
                                        .equals(namedDay)) {
                                    int month = cal.get(Calendar.MONTH);
                                    final Calendar scratch = Calendar
                                            .getInstance();
                                    scratch.setTime(cal.getTime());
                                    scratch.add(Calendar.DAY_OF_YEAR, -1
                                            * numDay);
                                    assertTrue(
                                            "Chosen day "
                                                    + date
                                                    + " is not the first day of the month",
                                            scratch.get(Calendar.MONTH) != month);

                                } else if (MonthlySchedule.NAMEDDAY_WEEKDAY
                                        .equals(namedDay)) {
                                    //assertEquals("Day " + date + " should be a weekday", -1, daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));
                                    int month = cal.get(Calendar.MONTH);
                                    final Calendar scratch = Calendar
                                            .getInstance();
                                    scratch.setTime(cal.getTime());
                                    int count = 1;
                                    do {
                                        scratch.add(Calendar.DAY_OF_YEAR, -1);
                                        // count days that aren't days off
                                        if ((scratch.get(Calendar.MONTH) == month)
                                                //&& -1 == daysOff
                                                //        .indexOf(scratch
                                                //                .get(Calendar.DAY_OF_WEEK))
                                                                ) {
                                            count++;
                                        }
                                        assertTrue(
                                                "Expected that any day previous to "
                                                        + date
                                                        + " in the same month be a weekend (i="
                                                        + i + ",numMonths="
                                                        + numMonths
                                                        + ",numDay=" + numDay
                                                        + ")",
                                                (scratch.get(Calendar.MONTH) != month)
                                                        || (count <= numDay));
                                        assertTrue(count < 32); // sanity test
                                    } while (scratch.get(Calendar.MONTH) == month);
                                    if (numDay != count) {
                                        System.out.println("here");
                                    }
                                    assertTrue("Expected day " + date
                                            + " to be the " + numDay
                                            + " weekday of the month",
                                            numDay == count);

                                } else if (MonthlySchedule.NAMEDDAY_WEEKEND
                                        .equals(namedDay)) {
                                    //assertTrue("Day " + date + " should be a weekEND", -1 != daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));
                                    int month = cal.get(Calendar.MONTH);
                                    final Calendar scratch = Calendar
                                            .getInstance();
                                    scratch.setTime(cal.getTime());
                                    int count = 1;
                                    do {
                                        scratch.add(Calendar.DAY_OF_YEAR, -1);
                                        // count days that are days off
                                        if ((scratch.get(Calendar.MONTH) == month)
                                                //&& -1 != daysOff
                                                //        .indexOf(scratch
                                                //                .get(Calendar.DAY_OF_WEEK))
                                                                ) {
                                            count++;
                                        }
                                        assertTrue(
                                                "Expected that any day previous to "
                                                        + date
                                                        + " in the same month is NOT a weekend (i="
                                                        + i + ",numMonths="
                                                        + numMonths
                                                        + ",numDay=" + numDay
                                                        + ")",
                                                (scratch.get(Calendar.MONTH) != month)
                                                        || (count <= numDay));
                                        assertTrue(count < 32); // sanity test
                                    } while (scratch.get(Calendar.MONTH) == month);
                                    assertTrue("Expected day to be the "
                                            + numDay
                                            + " weekend day of the month",
                                            numDay == count);

                                } else {
                                    // throws exception if value is unexpected
                                    int day = Integer.parseInt(namedDay);
                                    assertTrue(
                                            "Day " + namedDay
                                                    + " was expected but got "
                                                    + date,
                                            cal.get(Calendar.DAY_OF_WEEK) == day);

                                    int month = cal.get(Calendar.MONTH);
                                    final Calendar scratch = Calendar
                                            .getInstance();
                                    scratch.setTime(cal.getTime());
                                    scratch.add(Calendar.DAY_OF_YEAR, -7
                                            * numDay);
                                    assertTrue(
                                            "Chosen day "
                                                    + date
                                                    + " is not the first day of the month (i="
                                                    + i + ",numMonths="
                                                    + numMonths + ",numDay="
                                                    + numDay + ")",
                                            scratch.get(Calendar.MONTH) != month);
                                }

                                if (cal.get(Calendar.YEAR) == lastYear) {
                                    assertTrue(
                                            "Month should have increased by "
                                                    + numMonths
                                                    + " but instead went from "
                                                    + lastDate + " to " + date,
                                            cal.get(Calendar.MONTH) == lastMonth
                                                    + numMonths);
                                } else {
                                    assertTrue(
                                            "Month should have increased to "
                                                    + (Calendar.JANUARY
                                                            + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))
                                                    + " but went from "
                                                    + lastDate + " to " + date,
                                            (cal.get(Calendar.YEAR) == lastYear + 1)
                                                    && (cal.get(Calendar.MONTH) == (Calendar.JANUARY
                                                            + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))));
                                }

                                lastDate = date;
                                lastMonth = cal.get(Calendar.MONTH);
                                lastYear = cal.get(Calendar.YEAR);
                            }
                        }
                    }
                }
            }
        }
    }

    public void testLastPeriodOfEveryYMonths() throws ReportScheduleException {
        ReportScheduleBuilder builder;

        builder = new ReportScheduleBuilder();
        builder.setTimeZone(TimeZone.getDefault());
        builder.setRecurrence(MonthlySchedule.RECURRENCE_TYPE);
        builder.setMonthlyOption(MonthlySchedule.NAMEDDAYOFMONTH_OPTION);

        //final ResCalendar rcal = getContext().getResCalendar();
        //final List daysOff = rcal.getDaysOff();

        final int[] times = { 0, 100, 2300, 2359, 001, 1200, 1600 };

        final String[] namedDays = { MonthlySchedule.NAMEDDAY_DAY,
                MonthlySchedule.NAMEDDAY_WEEKDAY,
                MonthlySchedule.NAMEDDAY_WEEKEND, "" + Calendar.SUNDAY,
                "" + Calendar.MONDAY, "" + Calendar.TUESDAY,
                "" + Calendar.WEDNESDAY, "" + Calendar.THURSDAY,
                "" + Calendar.FRIDAY, "" + Calendar.SATURDAY,
                "" + Calendar.SUNDAY };

        for (Date startDate : startDates) {
            for (int time : times) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(startDate);
                startCal.set(Calendar.HOUR_OF_DAY, time / 100);
                startCal.set(Calendar.MINUTE, time % 100);
                startCal.set(Calendar.SECOND, 0);
                builder.setStartDate(startCal.getTime());
                builder.setEndType(NoScheduleEnd.ID);
                builder.setMonthlyNumDay("last");

                for (String namedDay : namedDays) {
                    builder.setMonthlyNamedDay(namedDay);

                    for (int numMonths = 1; numMonths <= 4; numMonths++) {
                        builder.setMonthlyNumMonths2(numMonths);

                        final ReportSchedule sched = builder.build();
                        // discard first because it's weird
                        sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                        Date lastDate = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastDate);
                        int lastMonth = cal.get(Calendar.MONTH);
                        int lastYear = cal.get(Calendar.YEAR);
                        for (int i = 0; i < 120; i++) {
                            final Date date = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
                            cal.setTime(date);

                            // ok.. don't exaggerate.. there could be Java
                            // bugs
                            // beyond 2037
                            if (cal.get(Calendar.YEAR) > 2037) {
                                // break out of repetitions loop
                                break;
                            }

                            assertTrue("Day " + date + " should be 20 or more", cal.get(Calendar.DAY_OF_MONTH) >= 20);

                            if (MonthlySchedule.NAMEDDAY_DAY.equals(namedDay)) {
                                int month = cal.get(Calendar.MONTH);
                                final Calendar scratch = Calendar.getInstance();
                                scratch.setTime(cal.getTime());
                                scratch.add(Calendar.DAY_OF_YEAR, 1);
                                assertTrue("Chosen day " + date
                                        + " is not the last day of the month",
                                        scratch.get(Calendar.MONTH) != month);

                            } else if (MonthlySchedule.NAMEDDAY_WEEKDAY
                                    .equals(namedDay)) {
                                //assertEquals("Day " + date + " should be a weekday", -1, daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));
                                int month = cal.get(Calendar.MONTH);
                                final Calendar scratch = Calendar.getInstance();
                                scratch.setTime(cal.getTime());
                                for (int j = 0; j < 10; j++) {
                                    scratch.add(Calendar.DAY_OF_YEAR, 1);
                                    assertTrue(
                                            "Chosen day "
                                                    + date
                                                    + " is not the last day weekday of the month "
                                                    + scratch.getTime() + " is",
                                            (scratch.get(Calendar.MONTH) != month));
                                                    // any other day must be a
                                                    // weekend
                                                    //|| -1 != daysOff.indexOf(scratch.get(Calendar.DAY_OF_WEEK)));
                                }

                            } else if (MonthlySchedule.NAMEDDAY_WEEKEND
                                    .equals(namedDay)) {
                                //it's not true
                                //assertTrue("Day " + date + " should be a weekEND", -1 != daysOff.indexOf(cal.get(Calendar.DAY_OF_WEEK)));
                                int month = cal.get(Calendar.MONTH);
                                final Calendar scratch = Calendar.getInstance();
                                scratch.setTime(cal.getTime());
                                for (int j = 0; j < 10; j++) {
                                    scratch.add(Calendar.DAY_OF_YEAR, 1);
                                    assertTrue(
                                            "Chosen day "
                                                    + date
                                                    + " is not the last day weekend day of the month",
                                            (scratch.get(Calendar.MONTH) != month));
                                                    // any other day must be a
                                                    // weekday
                                                    //|| -1 == daysOff
                                                    //        .indexOf(scratch
                                                    //               .get(Calendar.DAY_OF_WEEK)));
                                }

                            } else {
                                // throws exception if value is unexpected
                                int day = Integer.parseInt(namedDay);
                                assertTrue("Day " + namedDay
                                        + " was expected but got " + date, cal
                                        .get(Calendar.DAY_OF_WEEK) == day);

                                int month = cal.get(Calendar.MONTH);
                                final Calendar scratch = Calendar.getInstance();
                                scratch.setTime(cal.getTime());
                                scratch.add(Calendar.DAY_OF_YEAR, 7);
                                assertTrue("Chosen day " + date
                                        + " is not the last day of the month",
                                        scratch.get(Calendar.MONTH) != month);
                            }

                            if (cal.get(Calendar.YEAR) == lastYear) {
                                assertTrue("Month should have increased by "
                                        + numMonths + " but instead went from "
                                        + lastDate + " to " + date, cal
                                        .get(Calendar.MONTH) == lastMonth
                                        + numMonths);
                            } else {
                                assertTrue(
                                        "Month should have increased to "
                                                + (Calendar.JANUARY
                                                        + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))
                                                + " but went from " + lastDate
                                                + " to " + date,
                                        (cal.get(Calendar.YEAR) == lastYear + 1)
                                                && (cal.get(Calendar.MONTH) == (Calendar.JANUARY
                                                        + (numMonths - 1) - (Calendar.DECEMBER - lastMonth))));
                            }

                            lastDate = date;
                            lastMonth = cal.get(Calendar.MONTH);
                            lastYear = cal.get(Calendar.YEAR);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * This was created to reproduce a bug at Textron where the reports were running every half
     * hour instead of monthly. The data used here comes from the Textron db.
     * @throws ReportScheduleException 
     *
     */
    public void testIssue45977() throws ReportScheduleException {
        // HRT Monthly Financials
        {
            final Date lastRun = new Date(1162768972629L);
            final ReportScheduleBuilder builder = new ReportScheduleBuilder();
            builder.setTimeZone("EST");
            builder.setRecurrence("monthly");
            builder.setEndType("noEndDate");
            //lastrun = 1162768972629;
            builder.setMonthlyOption("namedDayOfMonth");
            final Date oldNextRun = new Date(1165186945588L);
            builder.setNextRun(oldNextRun);
            builder.setOccurrences(4);
            
            final Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            startCal.setTime(new Date(1157256000000L));
            startCal.set(Calendar.HOUR_OF_DAY, 830 / 100);
            startCal.set(Calendar.MINUTE, 830 % 100);
            startCal.set(Calendar.SECOND, 0);
            final Date startDate = startCal.getTime();
            builder.setStartDate(startDate);
            
            builder.setMonthlyNamedDay("1");
            builder.setMonthlyNumDay("1");
            builder.setMonthlyNumMonths2(1);
            
            final ReportSchedule sched = builder.build();
            
            Date nextRun = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
            assertTrue("Should not be null", null != nextRun);
            
            final Calendar testCal1 = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            testCal1.setTime(oldNextRun);
            
            final Calendar testCal2 = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            testCal2.setTime(nextRun);
            
            int diff = testCal2.get(Calendar.MONTH) - testCal1.get(Calendar.MONTH);
            if (testCal2.get(Calendar.MONTH) == Calendar.JANUARY && testCal1.get(Calendar.MONTH) == Calendar.DECEMBER) {
                diff = 1;
            }
            assertTrue("Month difference should be 1 but is " + diff, diff == 1); 
        }
        
        // Lycoming Monthly Financials
        {
            final Date lastRun = new Date(1162768737384L);
            final ReportScheduleBuilder builder = new ReportScheduleBuilder();
            builder.setTimeZone("EST");
            builder.setRecurrence("monthly");
            builder.setEndType("noEndDate");
            builder.setMonthlyOption("namedDayOfMonth");
            final Date oldNextRun = new Date(1165186880802L);
            builder.setNextRun(oldNextRun);
            builder.setOccurrences(4);
            
            final Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            startCal.setTime(new Date(1157256000000L));
            startCal.set(Calendar.HOUR_OF_DAY, 500 / 100);
            startCal.set(Calendar.MINUTE, 500 % 100);
            startCal.set(Calendar.SECOND, 0);
            final Date startDate = startCal.getTime();
            builder.setStartDate(startDate);
            
            builder.setMonthlyNamedDay("1");
            builder.setMonthlyNumDay("1");
            builder.setMonthlyNumMonths2(1);
            
            final ReportSchedule sched = builder.build();
            
            Date nextRun = sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
            assertTrue("Should not be null", null != nextRun);

            final Calendar testCal1 = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            testCal1.setTime(oldNextRun);
            
            final Calendar testCal2 = Calendar.getInstance(TimeZone.getTimeZone("EST"));
            testCal2.setTime(nextRun);
            
            int diff = testCal2.get(Calendar.MONTH) - testCal1.get(Calendar.MONTH);
            if (testCal2.get(Calendar.MONTH) == Calendar.JANUARY && testCal1.get(Calendar.MONTH) == Calendar.DECEMBER) {
                diff = 1;
            }
            assertTrue("Month difference should be 1 but is " + diff, diff == 1); 
        }        
        
    }

    private String selectedDays(boolean[] selectedWeekdays) {
        final StringBuilder builder = new StringBuilder();
        final String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday" };
        String sep = "";
        for (int i = 0; i < 7; i++) {
            if (selectedWeekdays[i]) {
                builder.append(sep + days[i]);
                sep = ",";
            }
        }
        return builder.toString();
    }

    private boolean[] createWeekSelection(int num) {
        assertTrue("num " + num + " out of range", num > 0 && num < 128);
        final boolean[] result = new boolean[7];
        // check leading 7 bits to see what numbers we come up with
        for (int i = 0; i < 7; i++) {
            result[i] = (num & (1 << i)) != 0;
        }
        return result;
    }

    private boolean isOnlyOneDay(boolean[] weekdays) {
        int count = 0;
        for (boolean b : weekdays) {
            count += (b ? 1 : 0);
        }
        return count == 1;
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        final Method[] methods = TestReportSchedule.class.getMethods();
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers())
                    && m.getName().startsWith("test")) {
                suite.addTest(new TestReportSchedule(m.getName()));
            }
        }

      //  suite.addTest(new
        //TestReportSchedule("testLastPeriodOfEveryYMonths"));
        return setUpHome(suite);
    }
}
