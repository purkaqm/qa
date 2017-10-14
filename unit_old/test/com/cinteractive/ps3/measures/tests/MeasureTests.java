package com.cinteractive.ps3.measures.tests;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import ps5.pages.measures.DiagramBuilder;
import ps5.reports.schedule.MonthlySchedule;
import ps5.reports.schedule.NullReportSchedule;
import ps5.reports.schedule.QuarterlySchedule;
import ps5.reports.schedule.ReportSchedule;
import ps5.reports.schedule.ReportScheduleBuilder;
import ps5.reports.schedule.ReportScheduleException;

import com.cinteractive.ps3.documents.ReportDocument;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.measures.IndicatorType;
import com.cinteractive.ps3.measures.MeasureBase;
import com.cinteractive.ps3.measures.MeasureDef;
import com.cinteractive.ps3.measures.MeasureFieldPermission;
import com.cinteractive.ps3.measures.MeasureIndicatorHandler;
import com.cinteractive.ps3.measures.MeasureInstance;
import com.cinteractive.ps3.measures.MeasureRangeType;
import com.cinteractive.ps3.measures.MeasureTemplate;
import com.cinteractive.ps3.measures.MeasureValue;
import com.cinteractive.ps3.measures.ThresholdIndicator;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.test.PSTestBase;
import com.cinteractive.util.ImageUtil;

/** Measures test case.*/
public class MeasureTests extends PSTestBase {

    private String NAME_PREFIX = "AUTO_GEN_";
    private static String C0 = "color0";
    private static String C1 = "color1";
    private static String C2 = "color2";
    private static String C3 = "color3";
    private static String C4 = "color4";

    public MeasureTests(String name) { super(name); }

    private String getName(String name) {
        return NAME_PREFIX + name;
    }

    private String generateName(String name) {
        name += System.nanoTime();
        return getName(name);
    }
    
    public void testDiagram() {
        
        String userMail= "AUTO_GEN_USER_MAIL_5@test.test";
        User user = User.getByEmailAddress(userMail, getContext());
        if (user == null) {
            System.out.println("Creating a new user");
            user = User.createNewUser(userMail, getContext());
            user.setTimeZoneId(TimeZone.getDefault().getID());
            user.save();
        }

        PSSession session = PSSession.createAutoSession(user);
        
        
        List<MeasureInstance> instancies = MeasureInstance.getAllMeasureInstances(getContext());
        if (instancies.isEmpty()) {
            return;
        }
        
//        MeasureDef def = MeasureDef.createNew(getContext(), user);
//        def.setName("test");
//        def.setFormat(MeasureFormat.getInstance(MeasureDataType.INTEGER, def));
//        def.setTargetValue(100.d);
//        MeasureInstance inst = MeasureInstance.createNew("test", user, null);
//        inst.getMeasureDefHistory().add(def);
//        inst.save();
//        
//        
//        MeasureValue value = new MeasureValue();
//        value.setValue(100.d);
//        value.setDate(new Date());
//        value.setError(MeasureError.getWithCodeOK());
//        inst.getHistoryValueHolder().addValue(value);
        
        
        try {
            MeasureInstance instance = instancies.get(0);
            DiagramBuilder builder = new DiagramBuilder(instance, session, null, null, null);
            Image image = builder.createImage(1000, 500);
            
            FileOutputStream fo = new FileOutputStream("t:\\DiagramTest.png");
            ImageUtil.writeImage((BufferedImage)image, fo, "PNG");
            fo.close();
        } catch(Exception ex) {
            fail("Coundn't create or save image");
        }
    }
    
    public void testDeletion() {
        try {
            String name = generateName("Test2");
            MeasureTemplate base = MeasureTemplate.createNew(name, Nobody.get(getContext()));
            User nobody = Nobody.get(getContext());
            base.getMeasureDefHistory().add(MeasureDef.createNew(getContext(), nobody));
            MeasureDef lastDef = base.getLastDefinition();
            lastDef.setGoal(true);
            ThresholdIndicator ind = MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef);
            setData(ind, new Double[] { 1d, 2d }, new String[] { "A", "B" });
            MeasureIndicatorHandler.setIndicatorThresholds(ind, lastDef);

            ind = MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef);
            setData(ind, new Double[] { 3d, 4d }, new String[] { "C", "D" });
            MeasureIndicatorHandler.setIndicatorThresholds(ind, lastDef);
            base.setOwnerId(nobody.getId());
            List<User> users = User.getUserList(getContext());
            User user1 = null;
            User user2 = null;
            User user3 = null;
            int i = 0;
            for (User user : users) {
                if (user.isAdmin() && !user.isAccountExpired() && !user.equals(nobody)) {
                    switch (++i) {
                    case 1:
                        user1 = user;
                        break;
                    case 2:
                        user2 = user;
                        break;
                    case 3:
                        user3 = user;
                        break;
                    }
                }
                if (i == 3)
                    break;
            }
            base.save();

            if (user1 != null) {
                base.delegate(user1);
                base.save();
                base.setDelegationAccepted("���", user1);
                base.save();
            }

            if (user2 != null) {
                base.addTeamMember(user2, nobody);
                base.save();
                base.setTeamMembershipAccepted(user2);
                base.save();
            }

            if (user3 != null) {
                base.addTeamMember(user3, nobody);
                base.save();
                base.setTeamMembershipAccepted(user3);
                base.save();
            }
            base.deleteSoft(nobody);
            base.deleteHard();
            System.out.println("deleted");
        } catch (Exception ex) {
            fail(ex.toString());
        }

    }

    
    public void testMonthlySchedule() throws ReportScheduleException {
        final ReportScheduleBuilder builder = new ReportScheduleBuilder(new NullReportSchedule());
        builder.setRecurrence(ReportDocument.SCHEDTYPE_MONTHLY);
        builder.setNextRun(null);
        builder.setTimeZone(TimeZone.getDefault());
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 20);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_YEAR, 5);
        cal.set(Calendar.YEAR, 2008);
        
        builder.setStartDate(cal.getTime());
        builder.setMonthlyOption(ReportDocument.SCHEDTYPE_DAYOFMONTH);
        builder.setMonthlyDayOfMonth(31);
        builder.setMonthlyNumMonths(1);
        
        ReportSchedule sched = builder.build();
        
        sched.calculateFirstRunDate(getContext().getCalendar(), getContext().getFiscalYearBean());
        
        System.out.println(sched.getNextRun());
        assertEquals(Timestamp.valueOf("2008-01-31 10:20:00.0").getTime(), 1000 *(sched.getNextRun().getTime()/1000));
        
        sched.process(getContext().getCalendar(), getContext().getFiscalYearBean());
        System.out.println(sched.getNextRun());
        assertEquals(Timestamp.valueOf("2008-02-29 10:20:00").getTime(), 1000*(sched.getNextRun().getTime()/1000));
    }

    public ReportSchedule getQuarterlyScheduler(int startDateDay, int startDateMonth, int everyDay, int everyQuarter) throws ReportScheduleException {
        final ReportScheduleBuilder builder = new ReportScheduleBuilder(new NullReportSchedule());
        builder.setRecurrence("quarterly");
        builder.setNextRun(null);
        builder.setTimeZone(TimeZone.getDefault());
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 20);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, startDateDay);
        cal.set(Calendar.MONTH, startDateMonth);
        cal.set(Calendar.YEAR, 2009);
        
        builder.setStartDate(cal.getTime());
        builder.setNextRun(cal.getTime());
        builder.setMonthlyOption(ReportDocument.SCHEDTYPE_DAYOFMONTH);
        builder.setQuarterlyDayOfQuarter(everyDay);
        builder.setQuarterlyNumQuarters(everyQuarter);
        builder.setQuarterlyOption("dayOfQuarter");

        return builder.build();
    } 
    
    public void testQuarterlyScheduler() throws ReportScheduleException {
        QuarterlySchedule sched = (QuarterlySchedule)getQuarterlyScheduler(20, 9, 99, 1);
        Date nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-12-31 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(20, 9, 92, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-12-31 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(1, 9, 31, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-10-31 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(20, 9, 99, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-12-31 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(20, 0, 89, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-03-30 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(20, 0, 89, 2);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-06-28 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(1, 0, 1, 3);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2009-10-01 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(1, 9, 1, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2010-01-01 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
        
        sched = (QuarterlySchedule)getQuarterlyScheduler(31, 11, 31, 1);
        nextRun = sched.computeNextRun(getContext().getCalendar(), getContext().getFiscalYearBean());
        assertEquals(Timestamp.valueOf("2010-01-31 00:00:00.0").getTime(), 1000 *(nextRun.getTime()/1000));
    }

    public void testMeasureScheduler(){
        List<MeasureBase> list = (List<MeasureBase>)MeasureInstance.getAllMeasureInstances(getContext());
        Iterator it = list.iterator();
        MeasureInstance instance = null;
        if (it.hasNext()){
            instance = (MeasureInstance) it.next();
        }
        instance.getLastDefinition().getEffectiveRange().setRangeType(MeasureRangeType.LIFE);
        if (instance != null){
            MeasureDef def = instance.getLastDefinition();
            Date date = def.getFrequency().getNextRun();
            def.getFrequency().process(getContext().getCalendar(), getContext().getFiscalYearBean());
            Date date1 = def.getFrequency().getNextRun();
            def.getFrequency().process(getContext().getCalendar(), getContext().getFiscalYearBean());
            Date date2 = def.getFrequency().getNextRun();
            System.out.println(date);
            System.out.println(date1);
            System.out.println(date2);
        }
    }

    public void testValueHolder(){
        List<MeasureBase> list = (List<MeasureBase>)MeasureInstance.getAllMeasureInstances(getContext());
        Iterator it = list.iterator();
        MeasureInstance instance = null;
        if (it.hasNext()){
            instance = (MeasureInstance) it.next();
        }
        TreeSet set = (TreeSet) instance.getHistoryValueHolder().getLastHistoryValues(1);
        MeasureValue val = (MeasureValue) set.first();
        instance.getHistoryValueHolder().getNextEarlier(val);
        instance.getHistoryValueHolder().getNextOlder(val);
    }

    private void setData(ThresholdIndicator indicator, Double [] thresholds, String [] descs) {
        int idx = 0;
        for (Double data : thresholds) {
            indicator.setThreshold(idx, data);
            ++idx;
        }

        idx = 0;
        for (String string : descs) {
            indicator.getDescs().set(idx, string);
            ++idx;
        }
    }

    private void assertIndicator(ThresholdIndicator indicator, Double [] thresholds, String [] descs) {
        int idx = 0;
        for (Double data : thresholds) {
            Double indData = indicator.getThreshold(idx);
            boolean equals = indData == null ? data == null : indData.equals(data);
            if (!equals) {
                assertTrue("Threshold: " + indData + ", expected: " + data, false);
            }
            ++idx;
        }

        idx = 0;
        for (String data : descs) {
            String indData = indicator.getDescs().get(idx);
            boolean equals = indData == null ? data == null : indData.equals(data);
            if (!equals) {
                assertTrue("Description: " + indData + ", expected: " + data, false);
            }
            ++idx;
        }
    }

    public void testCopyIndicator() {
        MeasureBase base = MeasureTemplate.createNew(generateName("Test2"), Nobody.get(getContext()));
        MeasureDef lastDef = base.getLastDefinition();
        lastDef.setGoal(false);
        ThresholdIndicator ind = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef);
        setData(ind, new Double[] {1d, 2d, 3d, 4d}, new String[]{"A", "B", "C", "D"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind, lastDef);
        base.save();

        MeasureBase base2 = MeasureTemplate.createNew(generateName("Test2-copy"), Nobody.get(getContext()));
        MeasureDef lastDef2 = base2.getLastDefinition();
        lastDef2.setGoal(false);
        ThresholdIndicator ind2 = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef2);
        setData(ind2, new Double[] {-1d, -2d, -3d, -4d}, new String[]{"Q", "W", "E", "R"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind2, lastDef2);
        base2.save();

        MeasureFieldPermission perms = MeasureFieldPermission.getInstance(MeasureFieldPermission.PERMISSIONED_NONE);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef), new Double[] {1d, 2d, 3d, 4d}, new String[]{"A", "B", "C", "D"});

        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_TARGET_INDICATOR_MESSAGES, true);
        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_TARGET_INDICATOR_VALUES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef), new Double[] {1d, 2d, 3d, 4d}, new String[]{"A", "B", "C", "D"});

        perms.set(MeasureFieldPermission.Field.FIELD_VARIANCE_INDICATOR_MESSAGES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef), new Double[] {1d, 2d, 3d, 4d}, new String[]{"Q", "W", "E", "R"});

        perms.set(MeasureFieldPermission.Field.FIELD_VARIANCE_INDICATOR_VALUES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, lastDef), new Double[] {-1d, -2d, -3d, -4d}, new String[]{"Q", "W", "E", "R"});


        // test goals
        base = MeasureTemplate.createNew(generateName("Test2"), Nobody.get(getContext()));
        lastDef = base.getLastDefinition();
        lastDef.setGoal(true);
        ind = MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef);
        setData(ind, new Double[] {1d, 2d}, new String[]{"A", "B"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind, lastDef);

        ind = MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef);
        setData(ind, new Double[] {3d, 4d}, new String[]{"C", "D"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind, lastDef);
        base.save();

        base2 = MeasureTemplate.createNew(generateName("Test2-copy"), Nobody.get(getContext()));
        lastDef2 = base2.getLastDefinition();
        lastDef2.setGoal(true);

        ind2 = MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef2);
        setData(ind2, new Double[] {-1d, -2d}, new String[]{"Q", "W"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind2, lastDef2);

        ind2 = MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef2);
        setData(ind2, new Double[] {-3d, -4d}, new String[]{"R", "T"});
        MeasureIndicatorHandler.setIndicatorThresholds(ind2, lastDef2);

        base2.save();

        perms = MeasureFieldPermission.getInstance(MeasureFieldPermission.PERMISSIONED_NONE);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {1d, 2d}, new String[]{"A", "B"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {3d, 4d}, new String[]{"C", "D"});

        perms.set(MeasureFieldPermission.Field.FIELD_VARIANCE_INDICATOR_MESSAGES, true);
        perms.set(MeasureFieldPermission.Field.FIELD_VARIANCE_INDICATOR_VALUES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {1d, 2d}, new String[]{"A", "B"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {3d, 4d}, new String[]{"C", "D"});

        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_TARGET_INDICATOR_MESSAGES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {1d, 2d}, new String[]{"Q", "W"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {3d, 4d}, new String[]{"C", "D"});


        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_PROGRESS_INDICATOR_MESSAGES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {1d, 2d}, new String[]{"Q", "W"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {3d, 4d}, new String[]{"R", "T"});

        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_TARGET_INDICATOR_VALUES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {-1d, -2d}, new String[]{"Q", "W"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {3d, 4d}, new String[]{"R", "T"});

        perms.set(MeasureFieldPermission.Field.FIELD_GOAL_PROGRESS_INDICATOR_VALUES, true);
        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, lastDef), new Double[] {-1d, -2d}, new String[]{"Q", "W"});

        MeasureIndicatorHandler.copyThresholdsWithPermission(lastDef, lastDef2, perms);
        assertIndicator(MeasureIndicatorHandler.getIndicator(IndicatorType.PROGRESS, lastDef), new Double[] {-3d, -4d}, new String[]{"R", "T"});
    }

    public void testCRUD() {

        List<MeasureTemplate> list = (List<MeasureTemplate>)MeasureTemplate.getAllMeasureTemplates(getContext());
        for (MeasureBase measure : list) {
            System.out.println(measure.getName() + " " + measure.getMeasureDefHistory().getLast().getName());
            assertNotNull(measure.getName());
        }

        User user = Nobody.get(getContext());
        String name = getName("test");

        MeasureBase base = MeasureTemplate.createNew(generateName("Test2"), Nobody.get(getContext()));
        MeasureDef lastDef = base.getLastDefinition();
        lastDef.setGoal(false);
        base.save();

        List objs = MeasureTemplate.getByName(MeasureTemplate.TYPE, base.getName(), false, null, null, getContext());
        assertTrue(!objs.isEmpty());

        base.deleteSoft(user);

        objs = MeasureTemplate.getByName(MeasureTemplate.TYPE, base.getName(), false, null, null, getContext());
        assertTrue(objs.isEmpty());


//        for (int i = 0; i <10; ++i) {
//            MeasureBase base = MeasureTemplate.createNew(name + i, Nobody.get(getContext()));
//            base.getMeasureDefHistory().getLast().setGoal(true);
//            base.save();
//        }
//
//        List<MeasureBase> newList = (List<MeasureBase>)MeasureTemplate.getAllMeasureTemplates(getContext());
//        assertTrue(list.size() == newList.size() - 1);
//        assertTrue(name.equals(newList.get(newList.size() - 1).getName()));
//        assertTrue(!newList.get(newList.size() - 1).getMeasureDefHistory().getLast().getGoal());
//
//        //base.deleteSoft(user);
//        newList.get(newList.size() - 1).deleteSoft(user);
//     //   base.save();
//
//        List<MeasureBase> newList2 = (List<MeasureBase>)MeasureTemplate.getAllMeasureTemplates(getContext());
//        assertTrue(list.size() == newList2.size());

    }

    public void testGoalsIndicators() {

        MeasureIndicatorHandler handler =  MeasureIndicatorHandler.getInstance();
        handler.setR0(25d);
        handler.setY0(50d);


        MeasureDef def = new MeasureDef();
        def.setGoal(true);
        def.setIndicatorHandler(handler);

        ThresholdIndicator achv = MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, def);

        assertTrue(achv.getSize() == 2);
        assertTrue(achv.getThreshold(0).doubleValue() == 25d);
        assertTrue(achv.getThreshold(1).doubleValue() == 50d);

        // thresholds in percents, other data is in absolute values
        assertTrue(achv.getDescription(-5.d).equals(C0));
        assertTrue(achv.getDescription(2.d).equals(C2));
        assertTrue(achv.getDescription(0.45).equals(C1));

        assertTrue(achv.getDescription(0.25).equals(C0));
        assertTrue(achv.getDescription(0.50).equals(C2));

    }
    
    public void testGoalsIndicatorsInverse() {

        MeasureIndicatorHandler handler =  MeasureIndicatorHandler.getInstance();
        handler.setR0(50d);
        handler.setY0(25d);


        MeasureDef def = new MeasureDef();
        def.setGoal(true);
        def.setIndicatorHandler(handler);

        ThresholdIndicator achv = MeasureIndicatorHandler.getIndicator(IndicatorType.ACHIEVEMENT, def);

        // thresholds in percents, other data is in absolute values
        assertTrue(achv.getDescription(-5.0).equals(C2));
        assertTrue(achv.getDescription(2.0).equals(C0));
        assertTrue(achv.getDescription(0.45).equals(C1));

        assertTrue(achv.getDescription(0.25).equals(C2));
        assertTrue(achv.getDescription(0.50).equals(C0));

    }

    public void testVariance() {
        MeasureIndicatorHandler handler = MeasureIndicatorHandler.getInstance(false);
        handler.setR0(-0.1d);
        handler.setY0(0.2d);
        handler.setY1(0.5d);
        handler.setR1(0.7d);

        MeasureDef def = new MeasureDef();
        def.setGoal(false);
        def.setIndicatorHandler(handler);
        
        ThresholdIndicator variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);
        variance.getDescs().set(0, C0);
        variance.getDescs().set(1, C1);
        variance.getDescs().set(2, C2);
        variance.getDescs().set(3, C3);
        variance.getDescs().set(4, C4);
        

        assertTrue(variance.getSize() == 4);
        assertTrue(variance.getThreshold(0).doubleValue() == -0.1d);
        assertTrue(variance.getThreshold(0).doubleValue() == 0.2d);
        assertTrue(variance.getThreshold(0).doubleValue() == 0.5d);
        assertTrue(variance.getThreshold(0).doubleValue() == 0.7d);

        assertTrue(variance.getDescription(-5.d).equals(C0));
        assertTrue(variance.getDescription(0.0).equals(C1));
        assertTrue(variance.getDescription(0.4d).equals(C2));
        assertTrue(variance.getDescription(0.6d).equals(C3));
        assertTrue(variance.getDescription(0.8d).equals(C4));

        assertTrue(variance.getDescription(-0.1d).equals(C0));
        assertTrue(variance.getDescription(0.2d).equals(C1));
        assertTrue(variance.getDescription(0.5d).equals(C3));
        assertTrue(variance.getDescription(0.7d).equals(C4));


        // set inverse order
        handler.setR0(0.7d);
        handler.setY0(0.5d);
        handler.setY1(0.2d);
        handler.setR1(-0.1d);

        variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);

        assertTrue(variance.getDescription(-5.d).equals(C4));
        assertTrue(variance.getDescription(0.0).equals(C3));
        assertTrue(variance.getDescription(0.4d).equals(C2));
        assertTrue(variance.getDescription(0.6d).equals(C1));
        assertTrue(variance.getDescription(0.8d).equals(C0));

        assertTrue(variance.getDescription(-0.1d).equals(C4));
        assertTrue(variance.getDescription(0.2d).equals(C3));
        assertTrue(variance.getDescription(0.5d).equals(C1));
        assertTrue(variance.getDescription(0.7d).equals(C0));

    }
  
    public void testOrder() {
        MeasureIndicatorHandler handler = MeasureIndicatorHandler.getInstance(false);
        handler.setR0(-0.1d);
        handler.setY0(0.2d);
        handler.setY1(0.5d);
        handler.setR1(0.7d);

        MeasureDef def = new MeasureDef();
        def.setGoal(false);
        def.setIndicatorHandler(handler);

        ThresholdIndicator variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);
        assertEquals(variance.getErrorThresholdNumber(), -1);

        handler.setR0(-0.1d);
        handler.setY0(0.2d);
        handler.setY1(0.7d);
        handler.setR1(0.7d);

        variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);
        assertEquals(variance.getErrorThresholdNumber(), -1);
        
        handler.setR0(0.1d);
        handler.setY0(0.1d);
        handler.setY1(0.1d);
        handler.setR1(0.1d);

        variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);
        assertEquals(variance.getErrorThresholdNumber(), -1);

        handler.setR0(0.1d);
        handler.setY0(0.d);
        handler.setY1(-0.1d);
        handler.setR1(0.1d);

        variance = MeasureIndicatorHandler.getIndicator(IndicatorType.VARIANCE, def);
        assertEquals(variance.getErrorThresholdNumber(), 3);
    }
    
    
}
