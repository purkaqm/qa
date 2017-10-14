package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.AssertionFailedError;
import ps5.services.state.UnitTestVisit;
import ps5.services.state.Visit;
import ps5.support.Util;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.beans.WorkBean;

import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.DependencyType;
import com.cinteractive.ps3.schedule.Schedules;
import com.cinteractive.ps3.scheduler.Constraint;
import com.cinteractive.ps3.scheduler.ScheduleData;
import com.cinteractive.ps3.scheduler.WorkSchedule;
import com.cinteractive.ps3.scheduler.calendar.DateCalculator;
import com.cinteractive.ps3.scheduler.calendar.DateCalculatorImpl;
import com.cinteractive.ps3.scheduler.newproblems.WorkScheduleProblem;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.types.WorkType;
import com.cinteractive.ps3.work.Milestone;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.scheduler.Duration;
import com.cinteractive.util.Log;

class TestSchedule {
    private WorkBean work;
    private Timestamp start;
    private Timestamp end;
    
    public TestSchedule(WorkBean work, String start, String end) {
        this.work = work;
        this.start = WorkScheduleTestUtils.formatDate(start);
        this.end = WorkScheduleTestUtils.formatDate(end);
    }
    
    protected WorkBean getWork() {
        return work;
    }
    protected Timestamp getStart() {
        return start;
    }
    protected Timestamp getEnd() {
        return end;
    }
}

class  Logger {
    private StringBuffer buffer = new StringBuffer(10000);
    private Log log = new Log(WorkScheduleTestUtils.class);
    
    void log(String string) {
        //getLogger().logprintln(string);
        buffer.append(string);
        buffer.append("\n");
        log.debug(log);
    }
    
    public String getBuffer() {
        return buffer.toString();
    }
    
    public void reset() {
        buffer = new StringBuffer(10000);
    }
}

public class WorkScheduleTestUtils {
    final private static String WORK_NAME = "AUTO_GEN_WORK_";
    static boolean USE_BEANS = true;
    private static User user = null;
    private DateCalculator calculator = null;
    private InstallationContext context = null;
    private Visit visit = null;
    private boolean enableConstraintTesting = true;
    private static Logger logger = new Logger();

    private final long ONE_DAY = 86400 * 1000;
    private final long currentTimeMillis = System.currentTimeMillis();
    private final Timestamp curTime = new Timestamp(currentTimeMillis);
    
    public static WorkScheduleTestUtils getInstance(InstallationContext context, User user, PSSession session) {
        return new WorkScheduleTestUtils(context, user, session);
    }
    
    private WorkScheduleTestUtils(InstallationContext context, User u, PSSession session) {
        this.context = context;
        user = u;
        calculator = new DateCalculatorImpl(getUser().getCalendar(), getUser().getTimeZone());
        visit = UnitTestVisit.createVisit(session);
    }
    
    private static Logger getLogger() {
        return logger;
    }

    public Visit getVisit() {
        return visit;
    }

    private InstallationContext getContext() {
        return context;
    }

    public static String getName(String name) {
        return WORK_NAME + name;
    }
    
    public static String getShortName(String name) {
        if (name.startsWith(WORK_NAME)) {
            name = name.substring(WORK_NAME.length());
        }
        return name;
    }

    public static Timestamp formatDate(Object date) {
        try {
            DateFormat df = new SimpleDateFormat ("MM.dd.yy");
            df.setTimeZone(getUser().getTimeZone());
            Timestamp ts = new Timestamp(df.parse((String)date).getTime());// + (long)(86400 * 1000L * Math.random()));
            DateCalculator dc = new DateCalculatorImpl(getUser().getCalendar(), getUser().getTimeZone());
            return dc.toDayBoundary(ts);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Work createWork(PSType wt, String name, Work parent, Constraint type, Timestamp start, Timestamp end, Duration dur) {
        Work w = Work.createNew(wt, name, getUser());
        try {
            w.setInheritControls(false);
            w.getSchedules().setConstraintType(type);
            
            if (type.isApplicableStartDate() || parent == null) {
                w.getSchedules().setPlannedStartDate(start);
            }
            
            if (type.isApplicableEndDate()) {
                w.getSchedules().setPlannedEndDate(end);
            }
             
            if (type.isApplicableDuration()) {
                w.getSchedules().setPlannedLaborTime(dur);
            }
        } catch (Exception ex) {
            throw new AssertionFailedError("Exception in createWork: " + ex.getMessage());
        }
        w.setParentWork(parent, getUser());
        w.save();
        return w;
    }
    
    public static WorkBean createWorkBean(WorkType wt, String name, Work parent, Constraint type, Timestamp start, Timestamp end, Duration dur) {
        WorkBean w = new TestWorkBean(name, Uuid.create(true));
        try {
            w.setConstraintType(type);
            w.setPSType(wt.getPSType());
            w.setScheduleFlag(true);
            if (type.isApplicableStartDate() || parent == null) {
                w.setPlannedStartDate(start);
            }
            
            if (type.isApplicableEndDate()) {
                w.setPlannedEndDate(end);
            }
             
            if (type.isApplicableDuration()) {
                w.setPlannedLaborTime(dur);
            }
        } catch (Exception ex) {
            throw new AssertionFailedError("Exception in createWork: " + ex.getMessage());
        }
        return w;
    }

    void removeWorks() {
//        List<Uuid> ids = (List) new JdbcQuery(getContext())
//        {
//            protected Object query(Connection conn)
//                throws SQLException {
//                WorkPeer p = (WorkPeer)WorkPeer.getInstance(conn);
//                return p.searchByName(conn, getContext(), WORK_NAME);
//            }
//        }.execute();

        HashSet<PSType> types = new HashSet<PSType>();
        PSType[] psTypes = new PSType[] {Work.TYPE,Tollgate.TYPE, Milestone.TYPE, Checkpoint.TYPE};
        types.addAll(Arrays.asList(psTypes));
        try {
            List list = (List)  PSObject.genericSearch(WORK_NAME, types, getContext(), null);
            for(Object obj : list ) {
                if (!(obj instanceof Work))
                    continue;

                Work w = (Work)obj;
               // w.delete(new DavUser(getContext(), getUser()));
                w.save();
            }
        } catch (Exception e) {
            //temporary
            List<Uuid> ids = (List<Uuid>)Work.findIdsByType(Work.TYPE, false, getContext());
            ids.addAll((List<Uuid>)Work.findIdsByType(Milestone.TYPE, false, getContext()));
            for(Uuid id : ids) {
                try {
                    Work w = (Work)Work.get(id.toString(), getContext());
                    if (w.getName().startsWith(WORK_NAME)) {
                      //  w.delete(new DavUser(getContext(), getUser()));
                        w.save();
                    }
                } catch (Exception ez) {
                    // TODO: handle exception
                }
            }
        }
    }

    private static String userDate(Timestamp t) {
        if (t == null) {
            return "";
        }
        return getUser().getDateFormat().format(t) + " : " + getUser().getTimeFormat().format(t);
    }
    
    public static void printWork(String name, Timestamp start, Timestamp end, Integer dur) {
        String s = String.format("%s\t%s (%s)\t%s (%s)\t%s", 
                                name, 
                                start, userDate(start), end, userDate(end), dur);
        getLogger().log(s);
    }

    private static void printWork(Work w) {
        Schedules sh = w.getSchedules();
        Timestamp start = sh.getSystemStartDate();
        Timestamp end = sh.getSystemEndDate();
        float dur = sh.getSystemLaborTime().toFloat()/60/8;
        String name = w.getName();
        name = getShortName(name);
        printWork(name, start, end, new Integer((int)dur));
    }
    
    private static void printWork(WorkBean w) {
        Timestamp start = w.getSystemStartDate();
        Timestamp end = w.getSystemEndDate();
        float dur = w.getSystemLaborTime() == null ? 0 : w.getSystemLaborTime().toFloat();// /60/8;
        String name = w.getName();
        name = getShortName(name);
        
        String s = String.format("%s\t%s (%s)\t%s (%s)\t%s\tLatest:\t%s (%s)\t%s (%s) %s", 
                name, 
                start, userDate(start), end, userDate(end), new Integer((int)dur),
                w.getSystemLatestStartDate(), userDate(w.getSystemLatestStartDate()),
                w.getSystemLatestEndDate(), userDate(w.getSystemLatestEndDate()),
                w.getCriticalPath()
                ); 
        getLogger().log(s);

        //printWork(name, start, end, new Integer((int)dur));
    }

    static private long compareWorks(Work from, Work to, DependencyType type) {
        Timestamp first = (DependencyType.FF_DEPENDENCY.equals(type) || DependencyType.FS_DEPENDENCY.equals(type))? from.getSchedules().getSystemEndDate() : from.getSchedules().getSystemStartDate();
        Timestamp second = (DependencyType.FF_DEPENDENCY.equals(type) || DependencyType.SF_DEPENDENCY.equals(type))? to.getSchedules().getSystemEndDate() : to.getSchedules().getSystemStartDate();
        return first.getTime() - second.getTime();
    }

    static private List<Work> sortWorks(Work parent, List<Work> works, Map<Work, Integer> levels) {
        List<Work> result = new ArrayList<Work>();
        Integer curLevel = levels.get(parent);
        if (curLevel == null) curLevel = new Integer(0);

        Collections.sort(works, new Comparator() {
            public int compare(Object x, Object y) {
                return ((Work)x).getName().compareTo(((Work)y).getName());
            }
        });
        for (Work w : works) {
            if ((parent != null && parent.equals(w.getParent())) || (parent == null && w.getParent() == null)) {
                result.add(w);
                levels.put(w, curLevel + 1);
                result.addAll(sortWorks(w, works, levels));
            }
        }
        return result;
    }

    List<WorkScheduleProblem> run(WorkSchedule ws, boolean enableResultsTesting) {
        List<WorkScheduleProblem> problems = ws.checkForProblems();

        if (!Util.isNullEmpty(problems)) {
            String result = "";
            for (WorkScheduleProblem prb : problems) {
                result += prb.getMessage();
            }
            
            throw new ScheduleProblem("Problem detected: " + result, problems);
        }

        problems.addAll(ws.run(enableResultsTesting));
        
        if (!USE_BEANS)
            ws.saveChanges();
        
        return problems;
    }
   
    void testWorks(TestWork[] works) {
        testWorks(works, true, null);
    }
    
    void testWorks(TestWork[] works, boolean throwException, WorkSchedule ws) {
        boolean error = false;
        getLogger().reset();
        for (TestWork w : works)  {
            
            if (ws != null && !USE_BEANS) {
                Work work = w.getWork();
                WorkBean bean = ws.getBean(work.getId());
                if (!Util.checkObjectsEquals(bean.getSystemStartDate(), work.getSchedules().getSystemStartDate())
                     || !Util.checkObjectsEquals(bean.getSystemEndDate(), work.getSchedules().getSystemEndDate())
                      || !Util.checkObjectsEquals(bean.getSystemLaborTime(), work.getSchedules().getSystemLaborTime())) {
                    throw new AssertionFailedError("WorkBean is different from Work");
                }
            }
            
            if (!testWork(w) || error) {
                if (!error) {
                    getLogger().log("\nResult is wrong");
                }
                
                if (w.getBean() == null) {
                    String parent = w.getWork().getParentWork() == null ? null : w.getWork().getParentWork().getName();
                    if (parent != null) {
                        parent = parent.substring(WORK_NAME.length());
                    }
                    getLogger().log("Work  :\tparent=" + parent + "\t"); printWork(w.getWork());
                    getLogger().log("Expect:\tparent=" + w.getParent() + "\t");printWork(w);
                } else {
                    getLogger().log("Work  :\t"); printWork(w.getBean());
                    getLogger().log("Expect:\t");printWork(w);
                }
                error=true;
            }
        }
        
        if (error && throwException) {
            throw new AssertionFailedError("Result is wrong\n" + getLogger().getBuffer());
        }
    }

    private boolean compareDates(Timestamp t1, Timestamp t2, boolean toStart) {
        t1 = getCalculator().toDayBoundary(t1);
        t2 = getCalculator().toDayBoundary(t2);
        return Util.checkObjectsEquals(t1, t2);
    }
    
    private boolean checkWorkDates(WorkBean work, Timestamp start, Timestamp end) {
        return compareDates(work.getSystemStartDate(), start, true) && compareDates(work.getSystemEndDate(), end, false);
    }
    
    private boolean testWork(TestWork w) {
        WorkBean work = w.getBean() != null ? w.getBean() : new TestWorkBean(w.getWork());
        
        boolean result = checkWorkDates(work, w.getSystemStartDate(), w.getSystemEndDate());
        
        if (w.getSystemLatestStartDate() != null) {
            result &= compareDates(w.getSystemLatestStartDate(), work.getSystemLatestStartDate(), true); 
        }
        
        if (w.getSystemLatestEndDate() != null) {
            result &= compareDates(w.getSystemLatestEndDate(), work.getSystemLatestEndDate(), false); 
        }
        
        if (w.getCriticalPath()) {
        	result &= Util.checkObjectsEquals(w.getCriticalPath(), work.getCriticalPath());
        }
        return result;
    }
    
    public void testWorks(Set works, List<TestSchedule> results) {
        boolean error = false;
        getLogger().reset();
        for (Iterator<TestSchedule> it = results.iterator(); it.hasNext();) {
            TestSchedule ts = it.next();
            if (!works.contains(ts.getWork())) {
                throw new AssertionFailedError("Work " + ts.getWork().getName() + " should be modified");
            }
            
            if (!checkWorkDates(ts.getWork(), ts.getStart(), ts.getEnd()) || error) {
                if (!error) {
                    getLogger().log("\nResult is wrong");
                }
                getLogger().log("Work  :\t"); printWork(ts.getWork());
                getLogger().log("Expect:\t");WorkScheduleTestUtils.printWork(ts.getWork().getName(), ts.getStart(), ts.getEnd(), 0);
                error=true;
            }
        }
        
        if (error) {
            throw new AssertionFailedError("Result is wrong\n" + getLogger().getBuffer());
        }
    }

    public void run(TestWork[] projects) {
        run(projects, true);
    }

    public void run(TestWork[] projects, boolean test) {
        WorkSchedule ws;
        ScheduleData data = TestWork.createWorks(projects, getUser());
        if (USE_BEANS) {
            ws = new WorkSchedule(data);
        } else {
            ws = new WorkSchedule(projects[0].getWork(), getUser());
        }
        
        List<WorkScheduleProblem> problems = run(ws, isEnableConstraintTesting());
        
        
        if (!Util.isNullEmpty(problems)) {
            String result = "";
            for (WorkScheduleProblem prb : problems) {
                result += prb.format(getUser());
            }
            // show results;
            getLogger().reset();
            testWorks(projects, false, ws);
            throw new ScheduleProblem("Problem detected: " + result + "\n" + getLogger().getBuffer(), problems);
        }
        
        if (test) {
            testWorks(projects, true, ws);
        }
    }

    public void testConstraints(Constraint constr, String validTypes, String ... params) {
        final String allTypes[] = {"Mile", "Leaf", "Sum", "MSsum"};
        Map typeMap = new HashMap();

        // wokrs[1] - is the testing work.
        typeMap.put("Mile", new TestWork[] {
                new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                new TestWork(Milestone.TYPE, "w2", "w1", Constraint.ASAP, null, null, null, null, null, null),
        });

        typeMap.put("Leaf", new TestWork[] {
                new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
        });

        typeMap.put("Sum", new TestWork[] {
                new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null),
                new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
        });

        typeMap.put("MSsum", new TestWork[] {
                new TestWork("w1", null, Constraint.ASAP, null, null, null, null, null),
                new TestWork("w2", "w1", Constraint.ASAP, null, null, null, null, null, null),
                new TestWork("w3", "w2", Constraint.ASAP, null, null, null, null, null),
        });
        ((TestWork[])typeMap.get("MSsum"))[1].setProjectControl(ProjectControl.MANUAL_SCHEDULING, true);

        boolean onlyForMS = (params.length != 0); //manual scheduling
        for(String type : allTypes) {
            TestWork[] works = (TestWork[])typeMap.get(type);
            works[1].setConstraintType(constr);
            boolean validType = (validTypes.indexOf(type) != -1);

            boolean exceptionThrown = false;
            try {
                run(works, false);
            } catch (ScheduleProblem ex) {
                exceptionThrown = true;
            }

            if (!((validType) ^ exceptionThrown)) {
                run(works, false);
                throw new AssertionFailedError(String.format("%s - %s : valid=%s, ms=%s, thrown=%s", constr, type, validType, false, exceptionThrown));
            }

//            works[1].manual = true;
//            exceptionThrown = false;
//            try {
//                run(works, false);
//            } catch (ScheduleProblem ex) {
//                exceptionThrown = true;
//            }
//            if (!(validType ^ exceptionThrown)) {
//                throw new AssertionFailedError(String.format("%s - %s : valid=%s, ms=%s, thrown=%s", constr, type, validType, true, exceptionThrown));
//            }

        }
    }
    
    public Timestamp add(Timestamp time, Duration dur) {
        return new Timestamp( getCalculator().add(time, dur).getTime() - (long)(dur.getAmount() == 0 ? 0 : ONE_DAY)); 
    }


    Timestamp toWorkTime(Timestamp time) {
        time = getCalculator().toDayBoundary(time);
        return getCalculator().toWorkTime(time, true);
    }

    void setEnableConstraintTesting(boolean b) {
        enableConstraintTesting = b;
    }
    
    public Timestamp getCurTime() {
        return curTime;
    }

    private static User getUser() {
        return user;
    }

    private DateCalculator getCalculator() {
        return calculator;
    }
    
    private boolean isEnableConstraintTesting() {
        return enableConstraintTesting;
    }
}
