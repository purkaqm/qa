package com.cinteractive.ps3.scheduler;

/* Copyright � 2000-2001 Cambridge Interactive, Inc. All rights reserved.*/

import java.util.List;

import com.cinteractive.ps3.scheduler.newproblems.WorkScheduleProblem;

class ScheduleProblem extends RuntimeException {
    ScheduleProblem(String msg, List<WorkScheduleProblem> problems) {
        super(msg);
        this.problems = problems;
    }
    
    private List<WorkScheduleProblem> problems;

    public List<WorkScheduleProblem> getProblems() {
        return problems;
    }

    public void setProblems(List<WorkScheduleProblem> problems) {
        this.problems = problems;
    }
}
