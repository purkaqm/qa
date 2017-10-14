package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.tests.core.PSParallelListener;
import com.powersteeringsoftware.libs.util.ResinJMXManager;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 30.07.12
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class DeadlockParallelListener extends PSParallelListener {
    private static final boolean local = true;
    private static boolean hasFailure;
    private static boolean hasFinished;

    @Override
    public void onStart(ITestContext tc) {
        super.onStart(tc);
        getDeadlockChecker();
    }

    @Override
    public void onTestStart(ITestResult tr) {
        _onTestStart(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        hasFailure = true;
        super.onTestFailure(tr);
        String deadlock = getDeadlockChecker().getDeadLock();
        if (deadlock != null) {
            PSLogger.fatal("There is a Dead-lock!");
            PSLogger.error(deadlock);
        }
        hasFinished = true;
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        hasFinished = true;
    }

    public static boolean hasFailure() {
        return hasFailure;
    }

    public static boolean hasFinished() {
        return hasFinished;
    }

    private static DeadlockCheckerThread checker;

    public static DeadlockCheckerThread getDeadlockChecker() {
        if (checker != null) return checker;
        checker = new DeadlockCheckerThread(!local);
        Thread th = new Thread(checker);
        th.setName("Deadlock checker");
        th.setDaemon(true);
        th.start();
        return checker;
    }

    public static class DeadlockCheckerThread implements Runnable {
        private static final long TIMEOUT_MS = 30 * 1000;
        private Map<String, Date> result = new LinkedHashMap<String, Date>();
        private boolean useRemote;

        private DeadlockCheckerThread(boolean remote) {
            useRemote = remote;
        }

        @Override
        public void run() {
            while (true) {
                Date d = new Date();
                String res = useRemote ? ResinJMXManager.getDeadlock() : LocalServerUtils.getLocalServer().getDeadlock();
                if (res != null) {
                    if (!result.containsKey(res)) {
                        PSLogger.error("Found deadlock at " + d);
                        result.put(res, d);
                    }
                }
                try {
                    Thread.sleep(TIMEOUT_MS);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        public String getDeadLock() {
            if (result.isEmpty()) return null;
            Object[] keys = result.keySet().toArray();
            return (String) keys[keys.length - 1];
        }

        public boolean hasDeadlock() {
            return !result.isEmpty();
        }
    }
}
