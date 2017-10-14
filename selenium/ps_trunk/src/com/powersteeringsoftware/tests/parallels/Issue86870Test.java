package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.SimpleGrid;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by admin on 30.05.2014.
 */
public class Issue86870Test extends PSParallelTestDriver {

    private static final int COUNT = 30;
    private static final int WBS_COUNT = 4;
    private static final int ASYNC_TIMEOUT = 2000;

    protected MyWork work;

    private class MyWork extends GatedProject {
        private boolean advance;

        public MyWork(String name, WorkType term) {
            super(name, term);
        }

        public void setAdvance(boolean b) {
            advance = b;
        }
    }

    @BeforeTest
    public void prepare() {
        work = new MyWork("Test86870GP", getTestData().getSGPTemplate()); // advancing should be enabled on context, champion ia not required
        if (!WorkManager.exists(work)) {
            WorkManager.createProject(work);
        }

        //CoreProperties.setWaitForElementToLoad(1000 * 1000);

        SummaryWorkPage.setWaitGrid(false);
        SummaryWorkPage.setDoCheck(false);
        Page.setCheckJs(false);
        AbstractPage.setGetDocumentOnOpen(false);
        CoreProperties.setMakeScreenCapture(false);
        CoreProperties.setDoValidatePage(false);
        WBSPage.setCheckReload(false);

        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[2 + WBS_COUNT][0];
        res[0] = new Object[]{new Data((User) CoreProperties.getDefaultUser().clone(), 1)};
        res[1] = new Object[]{new Data((User) CoreProperties.getDefaultUser().clone(), 2)};
        for (int i = 0; i < WBS_COUNT; i++)
            res[2 + i] = new Object[]{new Data((User) CoreProperties.getDefaultUser().clone(), 0)};
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isAdvanceReactivate()) {
            advanceReactivate(data);
        } else if (data.isWBSSave()) {
            wbsSave(data);
        } else {
            advanceSearchPeople(data);
        }
    }

    private void advanceSearchPeople(Data d) {
        BasicCommons.logIn(d.user, false);
        PeopleASPage page = new PeopleASPage();
        page.open();
        String name = d.user.getLastName();
        page.setKeyword(name);
        page.search();
        int i = 1;
        while (isAnyAlive()) {
            try {
                PSLogger.info("Search #" + (++i));
                page.expandCollapse();
                page.search();
                check();
            } catch (Throwable t) {
                processThrowable(t);
                page.open();
            }
        }
    }

    private void wbsSave(Data d) throws InterruptedException {
        BasicCommons.logIn(d.user, false);
        Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
        WBSPage pc = WorkManager.openWBS(work);
        int i = 1;
        while (isAnyAlive()) {
            try {
                PSLogger.info("WBS-save #" + (++i));
                //String status = pc.getGrid().getStatus(work);
                pc.getGrid().setStatus(work, Work.Status.NEEDS_ATTENTION);
                check();
                pc.getGrid().setStatus(work, Work.Status.ON_TRACK);
                check();
                pc.saveArea();
                check();
            } catch (Throwable t) {
                SimpleGrid.ApplyButton save = pc.getGrid().getSaveButton();
                if (save.isEnabled()) save.submit();
                processThrowable(t);
                open(pc);
            }
        }
    }

    private void advanceReactivate(Data d) {
        d.isRunning = true;
        list.add(d);
        BasicCommons.logIn(d.user, false);
        try {
            SummaryWorkPage sum = WorkManager.open(work);
            for (int i = 0; i < COUNT; i++) {
                PSLogger.info("Iter #" + (i + 1));
                try {
                    SummaryWorkPage.GateSnapshot.AdvanceDialog d1 = sum.getGateSnapshot().pushAdvance();
                    d1.setText("Advance #" + i);
                    d1.doAdvance();
                    work.setAdvance(true);
                    check();
                } catch (Throwable t) {
                    processThrowable(t);
                    open(sum);
                }
                try {
                    if (!work.advance) continue;
                    SummaryWorkPage.GateSnapshot.ReactivateDialog d2 = sum.getGateSnapshot().pushReactivate(work.getChildren().get(0).getName());
                    d2.setText("Reactivate #" + i);
                    d2.doReactivate();
                    work.setAdvance(false);
                    check();
                } catch (Throwable t) {
                    processThrowable(t);
                    open(sum);
                }
            }
        } finally {
            d.isRunning = false;
        }
    }

    class Data extends AbstractData {

        private int type;
        private Object id;

        private Data(User user, Integer type) {
            this.user = user;
            this.type = type;
        }

        private Data(User user, Integer type, Object id) {
            this(user, type);
            this.id = id;
        }

        public boolean isAdvanceReactivate() {
            return type == 1;
        }

        public boolean isWBSSave() {
            return type == 0;
        }

        public boolean isUserSearch() {
            return type == 2;
        }

        public String toString() {
            return (isAdvanceReactivate() ? "Advance/Reassign thread" : isWBSSave() ? "WBS save thread" : "User search thread") + (id != null ? "(" + id + ")" : "");
        }
    }
}
