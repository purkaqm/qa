package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.ClearCacheJspPage;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 06.05.13
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class PSParallelTestDriver extends PSTestDriver {
    protected List<AbstractData> list = Collections.synchronizedList(new ArrayList<AbstractData>());

    protected boolean skipCheckProblem;

    protected abstract class AbstractData {
        protected User user;
        protected Boolean isRunning;
    }

    protected void check() {
        String deadlock = DeadlockParallelListener.getDeadlockChecker().getDeadLock();
        if (deadlock != null) {
            PSLogger.error(deadlock);
            Assert.fail("Found Dead-lock!"); // application should be running local.
        }
    }


    public boolean hasDeadlock() {
        return DeadlockParallelListener.getDeadlockChecker().hasDeadlock();
    }

    protected boolean isAnyAlive() {
        for (AbstractData dt : list) {
            if (dt.isRunning != null && dt.isRunning) return true;
        }
        return false;
    }

    public void clearCache(boolean entire, boolean cycle) {
        ClearCacheJspPage cl = new ClearCacheJspPage();
        cl.open();
        if (cycle) {
            while (isAnyAlive() && !DeadlockParallelListener.hasFailure()) {
                if (entire)
                    cl.doClearEntire();
                else
                    cl.doClearPortfolios();
            }
            return;
        }
        if (entire)
            cl.doClearEntire();
        else
            cl.doClearPortfolios();
    }

    public void clearCache() {
        clearCache(true, false);
    }

    public HomePage openHome() {
        try {
            HomePage page = new HomePage();
            try {
                page.stop();
            } catch (Exception e) {
                PSLogger.error("Page.stop: " + e);
            }
            page.open();
            return page;
        } catch (Throwable th) {
            PSLogger.fatal(th);
        }
        return null;
    }

    protected void open(PSPage page) {
        if (page.checkUrl())
            page.refresh();
        else
            page.open();
    }

    public void stopLoading() {
        PSPage page = PSPage.getEmptyInstance();
        try {
            page.stop();
        } catch (Exception e) {
            PSLogger.error("Page.stop: " + e);
        }
    }

    protected void processThrowable(Throwable t) {
        check();
        if (SeleniumDriverFactory.isCriticalException(t)) {
            PSLogger.fatal(t);
            BasicCommons.reLogIn();
            return;
        }
        PSApplicationException p = null;
        try {
            p = skipCheckProblem ? null : PSPage.getEmptyInstance().getProblemFromPage();
        } catch (Exception ex) {
            PSLogger.error(ex);
        }
        if (p == null)
            PSLogger.fatal(t);
        else
            PSLogger.error(t);
        check();
    }

    private DeadlockTestData data;

    @Override
    public DeadlockTestData getTestData() {
        return data == null ? data = new DeadlockTestData() : data;
    }

}
