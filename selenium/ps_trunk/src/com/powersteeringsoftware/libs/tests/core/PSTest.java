package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.TimerWaiter;

@Deprecated // use PSTestDriver
public abstract class PSTest {
    protected String name = "Test name has not been written yet.";

    public final void execute() {
        BasicCommons.logIn();
        //BasicCommons.loadHomePage();
        run();
        new TimerWaiter(1000).waitTime();
    }

    protected abstract void run();

}
