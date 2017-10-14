package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;
import ps5.wbs.WBSTestHelper;
import ps5.wbs.beans.BeansTree;
import ps5.wbs.beans.Dependency;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.beans.WorkBean;
import ps5.wbs.model.DependencyColumn;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.DependencyType;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.scheduler.Duration;

class TestWork extends WorkBean {

    static class Predecessor {
        String name;
        Integer id;
        int lag;
        DependencyType depType;
        String getName() {
            return name;
        }
        void setName(String name) {
            this.name = name;
        }
        Integer getId() {
            return id;
        }
        void setId(Integer id) {
            this.id = id;
        }
        int getLag() {
            return lag;
        }
        void setLag(int lag) {
            this.lag = lag;
        }
        
        DependencyType getDepType() {
            return depType;
        }
        void setDepType(DependencyType depType) {
            this.depType = depType;
        }
        void setTypeAndLag(String relation) {
            if (relation != null && !relation.isEmpty()) {
                depType = DependencyColumn.getDependencyType(relation.substring(0, 2));
                String lg = relation.substring(2);
                if (lg.isEmpty()) {
                    lag = 0;
                } else if (lg.charAt(0) == '-') {
                    lag = -Integer.valueOf(lg.substring(1));
                } else {
                    lag = Integer.valueOf(lg.substring(0));
                }
            } else {
                depType = DependencyType.FS_DEPENDENCY;
                lag = 0;
            }
        }
        
    }
    
    static private List<Constraint> startPivot = Arrays.asList(new Constraint[]{ Constraint.MSO, Constraint.ASAP, Constraint.FD, Constraint.SNET, Constraint.SNLT});
    static private List<Constraint> endPivot = Arrays.asList(new Constraint[]{ Constraint.MFO, Constraint.ALAP, Constraint.FNET, Constraint.FNLT});
    
    private String parent = null; 
//    private DependencyType depType;
//    private int lag;
    private List<Predecessor> predecessors;
    private Work work = null;
    private WorkBean bean;

    // avoid to override constructor a lot of times
    static TestWork createFDWork(String name, String parent, Constraint constraint, String pivotDate, String pivotEndDate, Integer duration,
            String startDate, String endDate, Object predeccor) {
        TestWork work = new TestWork(name, parent, constraint, pivotDate, duration, startDate, endDate, predeccor, "");
        work.setPlannedEndDate(WorkScheduleTestUtils.formatDate(pivotEndDate));
        return work;
    }

    static TestWork createManual(String name, String parent, Constraint constraint, String plannedStart, String plannedEnd, Integer duration,
            String startDate, String endDate, Object predeccor) {
        TestWork work = new TestWork(name, parent, constraint, plannedStart, duration, startDate, endDate, predeccor, "");
       
        work.setPlannedStartDate(WorkScheduleTestUtils.formatDate(plannedStart));
        work.setPlannedEndDate(WorkScheduleTestUtils.formatDate(plannedEnd));

        work.setProjectControl(ProjectControl.MANUAL_SCHEDULING, true);
        return work;
    }

    public TestWork(String name, String parent, Constraint constraint, String pivotDate, Integer duration,
            String startDate, String endDate, Object predeccor) {
        this(name, parent, constraint, pivotDate, duration, startDate, endDate, predeccor, "");
    }

    public TestWork(PSType type, String name, String parent, Constraint constraint, String pivotDate, Integer duration,
            String startDate, String endDate, Object predeccor, String relation) {
        this(name, parent, constraint, pivotDate, duration, startDate, endDate, predeccor, relation);
        setPSType(type);
    }

    public TestWork(String name, String parent, Constraint constraint, String pivotDate, Integer duration,
            String startDate, String endDate, Object predecessor, String relation) {
        super(getIdByName(name));
        setName(name);
        setConstraintType(constraint);
        setPSType(Work.TYPE);
        
        //if (constraint.isApplicableDuration())
        if (duration != null) {
            setPlannedLaborTime(Duration.getDaysDuration(duration));
        }
        setSystemStartDate(WorkScheduleTestUtils.formatDate(startDate));
        setSystemEndDate(WorkScheduleTestUtils.formatDate(endDate));
        
        this.parent = parent;
        
        List<Predecessor> preds = null;
        if (predecessor instanceof String) {
            String predString = (String)predecessor;
            if (predString.indexOf(";") == -1) {
                if (relation == null || relation.isEmpty()) {
                    relation = "FS";
                }
                // support old style dependency
                predString = predString + ";" + relation;
            }
            
            String deps[] = predString.split(";");
            if (deps.length % 2 == 1) {
                throw new AssertionFailedError("Incorrect dependency: " + predecessor);
            }
            
            preds = new ArrayList<Predecessor>();
            for(int i = 0; i < deps.length; i += 2) {
                Predecessor pred = new Predecessor();
                pred.setName(deps[i]);
                if (i + 1 < deps.length) {
                    pred.setTypeAndLag(deps[i+1]);
                }
                preds.add(pred);
            }
        }

        if (predecessor instanceof Integer) {
            preds = new ArrayList<Predecessor>();
            Predecessor pred = new Predecessor();
            pred.setId((Integer)predecessor);
            pred.setTypeAndLag(relation);
            preds.add(pred);
        }
       
        this.predecessors = preds;
        
        Timestamp pivot = WorkScheduleTestUtils.formatDate(pivotDate);
        boolean startIncl = startPivot.contains(getConstraintType());
        setPlannedStartDate(startIncl ? (pivot == null ? getSystemStartDate() : pivot) : null);
        
        boolean endIncl = endPivot.contains(getConstraintType());
        setPlannedEndDate(endIncl ? (pivot == null ? getSystemEndDate() : pivot) : null);
    }

    public static ScheduleData createWorks(TestWork[] projects, User user) {
        if (WorkScheduleTestUtils.USE_BEANS) {
            return createWorkBeans(projects, user);
        } else {
            return createPSWorks(projects, user);
        }
    }
        
    private static ScheduleData createPSWorks(TestWork[] projects, User user) {
        
        //Work defParent = Work.getByName(Work.TYPE, ", isArchived, filter, comparator, context)
        Work works[] = new Work[projects.length];
        Map<String, Work> byName = new HashMap<String, Work>();
        int i = 0;
        for(TestWork w : projects) {
            Work work = WorkScheduleTestUtils.createWork(
                            w.getPSType(), 
                            WorkScheduleTestUtils.getName(w.getName()), 
                            null, 
                            w.getConstraintType(), 
                            w.getPlannedStartDate(), 
                            w.getPlannedEndDate(),
                            w.getPlannedLaborTime());
            
            work.getSchedules().setActualStartDate(w.getActualStartDate());
            work.getSchedules().setActualEndDate(w.getActualEndDate());
            work.setStatus(WorkStatus.DEFERRED);
            
            work.setManualScheduling(w.isManuallyScheduled());
            if (w.isManuallyScheduled()) {
                work.getSchedules().setPlannedStartDate(w.getPlannedStartDate());
                work.getSchedules().setPlannedEndDate(w.getPlannedEndDate());
            }
            
            w.setWork(work);
            w.setBean(null);
            works[i++] = work;
            byName.put(w.getName(), work);
        }
        
        i = 0;
        for (TestWork w : projects) {
            Work work = works[i];
            Work parent = byName.get(w.getParentWork());
            if (parent != null) {
                work.setParentWork(parent, user);
            }
            
            List<Predecessor> preds = w.getPredecessors();
            if (preds != null) {
                Work predWork = null;
                for (Predecessor p : preds) {
                    if (p.getName() != null) {
                        predWork = byName.get(p.getName());
                    } else {
                        predWork = works[p.getId()-1];
                    }
                    try {
                        work.getDependencies().addDependency(predWork, p.getLag(), p.getDepType(), user);
                    } catch (Exception ex) {
                        new AssertionFailedError("Could't add dependenices");
                    }
                }
            }
            
            work.setSequenceWithinParent(user, i);
            work.save();
            ++i;
        }
        return null;
    }
    
//    Timestamp getLatestStart() {
//        return latestStart;
//    }
//
//    void setLatestStart(String date) {
//        latestStart = WorkScheduleTestUtils.formatDate(date);
//    }
//    
    void setLatestDates(String start, String end, boolean isCritical) {
        setSystemLatestStartDate(WorkScheduleTestUtils.formatDate(start));
        setSystemLatestEndDate(WorkScheduleTestUtils.formatDate(end));
        setCriticalPath(isCritical);
    }

//    Timestamp getLatestEnd() {
//        return latestEnd;
//    }
//
//    void setLatestEnd(String date) {
//        latestEnd = WorkScheduleTestUtils.formatDate(date);
//    }
    
    private static PersistentKey getIdByName(String name) {
        return new StringPersistentKey(name);
    }
    
    private static WorkBean getByName(BeansTree tree, String name) {
        return (WorkBean)tree.getById(getIdByName(name));
    }

    private static WorkBean createBean(TestWork work) {
        WorkBean w = new TestWorkBean(work.getName(), work.getId());
        w.setConstraintType(work.getConstraintType());
        w.setPSType(work.getPSType());
        w.setScheduleFlag(true);
        if (work.getConstraintType().isApplicableStartDate() || work.getParentWork() == null) {
            w.setPlannedStartDate(work.getPlannedStartDate());
        }
        
        if (work.getConstraintType().isApplicableEndDate()) {
            w.setPlannedEndDate(work.getPlannedEndDate());
        }
         
        if (work.getConstraintType().isApplicableDuration()) {
            w.setPlannedLaborTime(work.getPlannedLaborTime());
        }
        
        w.setActualStartDate(work.getActualStartDate());
        w.setActualEndDate(work.getActualEndDate());
        w.setProjectControl(ProjectControl.MANUAL_SCHEDULING, work.isManuallyScheduled());
        if (work.isManuallyScheduled()) {
            w.setPlannedStartDate(work.getPlannedStartDate());
            w.setPlannedEndDate(work.getPlannedEndDate());
        }
        
        work.setBean(w);
        return w;
    }
    
    public static ScheduleData createWorkBeans(TestWork[] projects, User user) {
        WorkBean root = createBean(projects[0]);
        BeansTree tree = new BeansTree(root);
        for(int i = 1; i < projects.length; ++i) {
            if (tree.getById(projects[i].getId()) != null) {
                projects[i].setId(new StringPersistentKey(projects[i].getId().toString() + i));
            }
            WorkBean work = createBean(projects[i]);
            
            WorkBean parent = getByName(tree, projects[i].getParentWork());
            if (parent == null) {
                throw new IllegalArgumentException("Incorrect parent of work " + work.getName());
            }
            tree.addChild(parent, work);
        }
        
        tree.reorder(1);
        for(int i = 0; i < projects.length; ++i) {
            TestWork work = projects[i];
            WorkBean w = (WorkBean)tree.getById(work.getId());
            WorkBean predWork = null;
            List<Predecessor> preds = work.getPredecessors();
            if (preds != null) {
                for (Predecessor p : preds) {
                    if (p.getName() != null) {
                        predWork = getByName(tree, p.getName());
                    } else {
                        predWork = (WorkBean)tree.getByIndex(p.getId());
                    }
                    tree.addDependency(new Dependency(predWork, w, p.getDepType(), p.getLag()));
                }
            }
        }
        
        //WBSTestUtils.printTree(tree);
        return new ScheduleData(tree, user, WBSTestHelper.getVisit());
    }

    protected String getParentWork() {
        return parent;
    }
    private List<Predecessor> getPredecessors() {
        return predecessors;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }
    
    protected WorkBean getBean() {
        return bean;
    }

    protected void setBean(WorkBean bean) {
        this.bean = bean;
    }

    public void setActualStart(String time) {
        setActualStartDate(WorkScheduleTestUtils.formatDate(time));
    }
    
    public void setActualEnd(String time) {
        setActualEndDate(WorkScheduleTestUtils.formatDate(time));
    }
//
//    Boolean getIsCriticalPath() {
//        return isCriticalPath;
//    }
//
//    void setIsCriticalPath(Boolean isCriticalPath) {
//        this.isCriticalPath = isCriticalPath;
//    }
}
