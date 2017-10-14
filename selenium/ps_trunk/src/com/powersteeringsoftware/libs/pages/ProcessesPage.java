package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.util.TimerWaiter;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 19.12.12
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProcessesPage extends PSPage {

    protected static final TimerWaiter WAITER = new TimerWaiter(1000);

    @Override
    public void open() {
        clickAdminProcesses();
        initJsErrorChecker();
        WAITER.waitTime();
    }
}
