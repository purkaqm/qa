package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.objects.works.Work;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.10.13
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class WorkOptionsComponent extends Element {
    private ILocatorable statusLoc;
    private ILocatorable priorityLoc;

    public WorkOptionsComponent(ILocatorable l1, ILocatorable l2) {
        statusLoc = l1;
        priorityLoc = l2;
    }

    public void setStatus(Work.Status status) {
        new SelectInput(statusLoc).selectValue(status.getValue());
    }

    public void setPriority(Work.Priority priority) {
        new SelectInput(priorityLoc).selectValue(String.valueOf(priority.getIndex()));
    }
}
