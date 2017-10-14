package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by admin on 21.05.2014.
 */
public class Issue91033Test extends PSParallelTestDriver {
    private static final int COUNT_THREADS = 10; // number of threads
    private static final int COUNT_ATTEMPTS = 8; // number of iterations
    private static final int ASYNC_TIMEOUT = 0 * 1000;

    private static final int WAITER_STEP = 5 * 1000;
    private static final long WAITER_TIMEOUT = 15 * 60 * 1000;
    private boolean waitForStatus = true;

    @BeforeTest
    public void prepare() {
        SummaryWorkPage.setWaitGrid(false);
        SummaryWorkPage.setDoCheck(false);
        EditWorkPage.setWaitLoading(false);
        PSPage.setCheckBlankPage(false);
        Page.setCheckJs(false);
        AbstractPage.setGetDocumentOnOpen(false);
        CoreProperties.setMakeScreenCapture(true);
        CoreProperties.setDoValidatePage(false);

        CoreProperties.setWaitForElementToLoad(WAITER_TIMEOUT / 2);

        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        work = getTestData().getSGProject91033_2();
        //skipCheckProblem = true;
    }

    private Work work;

    //@DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_THREADS][0];
        res[0] = new Object[]{new Data((User) getTestData().getDefaultUser().clone())};
        for (int i = 1; i < COUNT_THREADS; i++) {
            res[i] = new Object[]{new Data((User) getTestData().getDefaultUser().clone(), i)};
        }
        return res;
    }

    //@Test(dataProvider = "data")
    public void test(Data data) {
        Thread.currentThread().setName(data.toString());
        if (data.isCancel()) {
            cancel(data);
        } else {
            changeStatus(data);
        }
    }

    public void changeStatus(Data d) {
        BasicCommons.logIn(d.user, false);
        SummaryWorkPage sum = WorkManager.open(work);

        int j = 0;
        while (!DeadlockParallelListener.hasFinished()) {
            Work.Status newStatus = null;
            Work.Status st;
            try {
                if (!Work.Status.CANCELED.equals(st = work.getStatus(false))) {
                    PSLogger.debug("work status is " + st);
                    Thread.sleep(WAITER_STEP);
                    continue;
                }
                j++;
                do
                    newStatus = PSTestData.getRandomFromList(Work.Status.getActiveList());
                while (st.equals(newStatus));
                check();
                if (ASYNC_TIMEOUT > 0)
                    Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
                PSLogger.info("Go #" + j + ":" + newStatus);
                if (waitForStatus) {
                    sum.setStatus(newStatus);
                    sum.doTestUrl();
                } else {
                    sum.setStatus(newStatus.getValue(), false);
                    sum.stop();
                }
                check();
                PSLogger.info("Success #" + j + ":" + newStatus);
            } catch (Throwable e) {
                PSLogger.error("Fail #" + j + ":" + newStatus);
                processThrowable(e);
                open(sum);
            }
        }
    }

    public void cancel(Data d) {
        BasicCommons.logIn(d.user, false);
        SummaryWorkPage sum = WorkManager.open(work);
        for (int j = 1; j <= COUNT_ATTEMPTS; j++) {
            PSLogger.info("Cancel #" + j);
            check();
            try {
                if (!work.hasStatus()) {
                    work.setStatus(Work.Status.get(sum.getStatus()));
                }
                if (Work.Status.CANCELED.equals(work.getStatus(false))) {
                    PSLogger.info("Work is already canceled");
                } else {
                    PSLogger.info("doCancel(" + j + "/" + COUNT_ATTEMPTS + ")");
                    check();
                    sum.setStatus(Work.Status.CANCELED);
                    check();
                    sum.doTestUrl();
                    work.setStatus(Work.Status.CANCELED);
                    PSLogger.info("Success #" + j);
                }
                PSLogger.info("Wait while work is canceled");
                long s = System.currentTimeMillis();
                do {
                    if (System.currentTimeMillis() - s > WAITER_TIMEOUT) {
                        PSLogger.error("Reach timeout");
                        return;
                    }
                    Thread.sleep(WAITER_STEP);
                    //if (work.hasStatus() && !Work.Status.CANCELED.equals(work.getStatus(false))) break;
                    check();
                    open(sum);
                    sum.doTestUrl();
                    work.setStatus(Work.Status.get(sum.getStatus()));
                } while (!work.hasStatus() || Work.Status.CANCELED.equals(work.getStatus(false)));
                PSLogger.info("Work status now is '" + work.getStatus(false) + "'");
                PSLogger.debug(work.getConfig());
            } catch (Throwable e) {
                work.setStatus(null);
                PSLogger.error("Fail #" + j);
                processThrowable(e);
                j--;
                open(sum);
            }

        }
    }

    @Test(threadPoolSize = COUNT_THREADS,
            invocationCount = COUNT_THREADS, groups = TestSettings.FIREFOX_ONLY_GROUP)
    public void test() throws InterruptedException {
        String name;
        Thread.currentThread().setName(name = ("test-" + Thread.currentThread().getId()));

        BasicCommons.logIn(false);
        if (ASYNC_TIMEOUT > 0)
            Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
        SummaryWorkPage sum = WorkManager.open(work);

        for (int j = 1; j <= COUNT_ATTEMPTS; j++) {
            PSLogger.info("Iter.#" + j + "#");
            check();
            if (DeadlockParallelListener.hasFinished()) {
                PSLogger.info("break " + name);
                return;
            }
            Work.Status newSt = null;
            try {
                synchronized (work) { // sync to avoid exceptions
                    PSLogger.debug("START synchronized block (" + name + ")");
                    if (!work.hasStatus()) {
                        work.setStatus(Work.Status.get(sum.getStatus()));
                    }
                    if (!Work.Status.CANCELED.equals(work.getStatus(false))) {
                        PSLogger.info("Cancel.#" + j);
                        check();
                        sum.setStatus(Work.Status.CANCELED);
                        check();
                        sum.doTestUrl();
                        work.setStatus(Work.Status.CANCELED);
                        PSLogger.info("Success-Canceling.#" + j);
                    }
                    PSLogger.debug("END synchronized block (" + name + ")");
                }
                check();
                if (!Work.Status.CANCELED.equals(work.getStatus(false))) {
                    j--;
                    continue;
                }
                newSt = PSTestData.getRandomFromList(Work.Status.getActiveList());
                PSLogger.info("Iter.#" + j + ".Status=" + newSt + ".");
                sum.setStatus(newSt);
                check();
                sum.doTestUrl();
                work.setStatus(newSt);
                PSLogger.info("Success.#" + j + "(" + newSt + ")");
            } catch (Throwable e) {
                work.setStatus(null);
                PSLogger.error("Fail.#" + j + "(" + newSt + ")");
                processThrowable(e);
                open(sum);
                j--;
            }
        }
    }

    class Data extends AbstractData {
        private String id;

        private Data(User user, Object id) {
            this.user = user;
            this.id = id.toString();
        }

        private Data(User user) {
            this.user = user;
        }

        public boolean isCancel() {
            return id == null;
        }

        public String toString() {
            return isCancel() ? "Cancel thread" : ("Set Status thread #" + id);
        }
    }
}

