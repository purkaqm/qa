package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ps5.wbs.WBSTestHelper;
import ps5.wbs.beans.ProjectControl;
import ps5.wbs.beans.ResourceBean;
import ps5.wbs.beans.WorkBean;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.ps3.contexts.ContextWorkStatus;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.newsecurity.PSPermission;
import com.cinteractive.ps3.schedule.Schedules;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;

public class TestWorkBean extends WorkBean {
      
    public TestWorkBean(String name, PersistentKey id) {
        super(id);
        setName(name);
        setConstraintType(Constraint.ASAP);
        setPSType(Work.TYPE);
        setStatus(WorkStatus.PROPOSED);
        setPermission(PSPermission.ALL);
        setScheduleFlag(true);
        long date = 100;
        setSystemStartDate(new Timestamp(date));
        setSystemEndDate(new Timestamp(date));
        setProjectControl(ProjectControl.PLAN_RESOURCES, true);
        
        List<ContextWorkStatus> statuses = new ArrayList<ContextWorkStatus>();
        for(WorkStatus status : WorkStatus.values())
            statuses.add(WBSTestHelper.getContext().getWorkStatus(status));

        setValidWorkStatuses(statuses);
        setInitialized();
    }
    
    public TestWorkBean(String name, String id) {
        this(name, new StringPersistentKey(id));
    }
    
    public TestWorkBean(WorkBean bean) {
        super(bean.getId());
        create(bean);
    }
    
    private void create(WorkBean bean) {
        setName(bean.getName());
        // TODO
        
        
        setPlannedStartDate(bean.getPlannedStartDate());
        setPlannedEndDate(bean.getPlannedEndDate());
        setPlannedLaborTime(bean.getPlannedLaborTime());
        setSystemStartDate(bean.getSystemStartDate());
        setSystemEndDate(bean.getSystemEndDate());
        setSystemLaborTime(bean.getSystemLaborTime());
        setActualStartDate(bean.getActualStartDate());
        setActualEndDate(bean.getActualEndDate());
        setConstraintType(bean.getConstraintType());
        setProjectControl(ProjectControl.MANUAL_SCHEDULING, bean.isManuallyScheduled());
        
        List<ResourceBean> needs = bean.getResources();
        if (needs != null && !needs.isEmpty()) {
            for (ResourceBean need : needs) {
                User user = need.getPerson();
                if (user != null) {
//                    addDateCalculator(new DateCalculatorImpl(user.getCalendar(), user.getTimeZone()));
                }
            }
        }
        
        if (getConstraintType() == null) {
            setConstraintType(Constraint.ASAP);
        }
        
        //TODO
//        if (getDateCalculators().isEmpty()) {
//            addDateCalculator(new DateCalculatorImpl(work.getCalendar(), work.getOwner().getTimeZone()));
//        }
    }
    
    public TestWorkBean(Work work) {
        super(work.getId());
        WorkBean bean = new WorkBean(work.getId());
        
        Schedules s = work.getSchedules();
        bean.setName(work.getName());
        setConstraintType(Constraint.ASAP);
        
        //TODO
        bean.setPlannedStartDate(s.getPlannedStartDate());
        bean.setPlannedEndDate(s.getPlannedEndDate());
        bean.setPlannedLaborTime(s.getPlannedLaborTime());
        bean.setSystemStartDate(s.getSystemStartDate());
        bean.setSystemEndDate(s.getSystemEndDate());
        bean.setSystemLaborTime(s.getSystemLaborTime());
        bean.setActualStartDate(s.getActualStartDate());
        bean.setActualEndDate(s.getActualEndDate());
        bean.setConstraintType(s.getConstraintType());
        
        create(bean);
        setProjectControl(ProjectControl.MANUAL_SCHEDULING, work.isManuallyScheduled());
      
//        if (getDateCalculators().isEmpty()) {
//            addDateCalculator(new DateCalculatorImpl(work.getCalendar(), work.getOwner().getTimeZone()));
//        }
        
    }
}
