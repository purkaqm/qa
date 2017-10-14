package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import junit.framework.AssertionFailedError;
import ps5.wbs.beans.WorkBean;

import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.scheduler.newproblems.CyclicDependencyProblem;
import com.cinteractive.ps3.scheduler.newproblems.UnresolvedConstraintProblem;
import com.cinteractive.ps3.scheduler.newproblems.WorkScheduleProblem;

abstract class ThrowExceptionTest {

    ThrowExceptionTest(String workName, Class except) {
        this(workName, except, null);
    }
    
    ThrowExceptionTest(String workName, Class except, String toWorkName) {
        try {
            invoke();
        } catch (ScheduleProblem ex) {
            if (workName == null || except == null) {                
                return;
            }
            
            if (toWorkName == null) {
                toWorkName = "";
            }
            
            StringBuffer buf = new StringBuffer();
            for(WorkScheduleProblem p : ex.getProblems()) {
                WorkBean from = p.getFrom();
                String name = from.getName();
                String toName = "";
                if (except.equals(p.getClass())) {
                    if (p instanceof CyclicDependencyProblem) {
                        StringBuffer foundCycle = new StringBuffer();
                        for(WorkBean work : ((CyclicDependencyProblem)p).getCycle()) {
                            foundCycle.append(WorkScheduleTestUtils.getShortName(work.getName() + " "));
                        }
                        name = foundCycle.toString();
                        name = name.trim();
                    } else {
                        
                        if (p instanceof UnresolvedConstraintProblem) {
                            WorkBean to = ((UnresolvedConstraintProblem)p).getTo();
                            toName = to == null ? "" : to.getName();
                        }
                        
                        name = WorkScheduleTestUtils.getShortName(from.getName());
                    }

                    if ( (workName.equals(name) && toWorkName.equals(toName))
                            || (workName.equals(toName) && toWorkName.equals(name))) {
                        return;
                    } 
                  //  return;
                }
            
                buf.append(String.format("%s (%s)", p.getClass().getSimpleName(), name));
            }
            
            String descr = "Expected problem not found\n" +
                           "Expected: " + String.format("%s(%s)\n", except.getSimpleName(), workName) + 
                           "Problems: " + buf.toString();
            throw new AssertionFailedError(descr);
        } catch (AssertionFailedError ex) {
        }
        throw new AssertionFailedError("ScheduleProblem exception is not thrown");
    }
    abstract void invoke();
}
    