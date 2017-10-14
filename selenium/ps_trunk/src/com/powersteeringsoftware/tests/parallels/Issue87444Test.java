package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.SimpleGrid;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 17.01.14
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class Issue87444Test extends PSParallelTestDriver {
    private static final int COUNT_1 = 6; // number of work-creation threads
    private static final int COUNT_2 = 3; // number of portfolio threads
    private static final int COUNT_1_1 = 20; // number of works in thread
    private List<String> checkedList = Collections.synchronizedList(new ArrayList<String>());
    private static boolean clear = true;
    private static final long time = 60 * 60 * 1000; // hour
    private static final int deleteParameter = 10;
    private static int listParameter = 4;

    @BeforeTest
    public void prepare() {
        PSPage.setCheckBlankPage(true);
        SummaryWorkPage.setWaitGrid(false);
        EditWorkPage.setWaitLoading(false);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        int count = COUNT_1 + COUNT_2 + (clear ? 1 : 0);
        Object[][] res = new Object[count][0];
        for (int i = 0; i < COUNT_1; i++) {
            res[i] = new Object[]{new Data(i, COUNT_1_1)};
        }
        for (int i = COUNT_1; i < COUNT_2 + COUNT_1; i++) {
            res[i] = new Object[]{new Data(i)};
        }
        if (clear)
            res[count - 1] = new Object[]{new Data(getTestData().getOrg87444())};
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isWorkGenerator()) {
            creationThread(data);
        } else if (data.isDelete()) {
            deletingThread(data);
        } else {
            portfolioThread(data);
        }
    }


    private void deletingThread(Data d) {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        do {
            if (DeadlockParallelListener.hasFailure()) return;
            List<Work> all = null;
            WBSPage pc = null;
            try {
                all = d.parent.getExistChildren();
            } catch (Exception e) {
                // just for case
            }
            if (pc == null) {
                pc = WorkManager.openWBS(d.parent);
            } else {
                pc.refresh();
            }
            if (all != null && all.size() > deleteParameter) {
                PSLogger.info("All works: " + all);
                bulkDelete(pc, d.parent);
            }
        } while ((isAnyAlive()));
        bulkDelete(null, d.parent);
    }

    private void bulkDelete(WBSPage pc, Work w) {
        try {
            pc = _bulkDelete(pc, w);
            if (pc != null) {
                SimpleGrid.ApplyButton save = pc.getGrid().getSaveButton();
                if (save.isEnabled()) {
                    save.submit();
                }
            }
        } catch (Exception e2) {
            PSLogger.error("Bulk delete exception2: " + e2);
        }
    }

    private WBSPage _bulkDelete(WBSPage pc, Work w) {
        try {
            if (pc == null)
                pc = WorkManager.openWBS(w);
            pc.getGrid().selectWorks();
            pc.bulkDelete().yes();
            SimpleGrid.ApplyButton save = pc.getGrid().getSaveButton();
            if (save.isEnabled()) {
                save.submit();
            }
        } catch (SeleniumException se) {
            PSLogger.error("Bulk delete exception0: " + se);
            return pc;
        } catch (Exception e) {
            PSLogger.error("Bulk delete exception1: " + e);
            return pc;
        }
        return null; // ok
    }

    private void creationThread(Data d) {
        d.isRunning = true;
        list.add(d);
        try {
            BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
            for (Work w : d.works) {
                if (DeadlockParallelListener.hasFailure()) return;
                try {
                    //WorkManager.createProject(w);
                    long t = (time - System.currentTimeMillis() + CoreProperties.getTestTemplate()) / 1000;
                    try {
                        w.setName("Test-" + t);
                        PSLogger.info("Creation " + w.getName());
                    } catch (NullPointerException ne) {
                        PSLogger.error(ne);
                    }
                    WorkManager.createDescendant(getTestData().getOrg87444(), w);
                    checkedList.add(w.getName());
                    check();
                    //WorkManager.deleteProject(sum1);
                    //w.setDeleted();
                    //check();
                } catch (SeleniumException se) {
                    PSLogger.save("Exception on page during creation");
                    PSLogger.fatal(se);
                }
            }
        } finally {
            d.isRunning = false;
        }
    }

    private void portfolioThread(Data d) {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        PortfolioDetailsPage page = PortfolioDetailsPage.openPage(getTestData().getPortfolio87444().getId());
        //page.more();
        while (isAnyAlive()) {
            if (DeadlockParallelListener.hasFailure()) return;
            try {
                List<Link> _links;
                List<String> links = toNames(_links = page.getNameLinks());
                while (links.isEmpty()) {
                    page.refresh();
                    links = toNames(_links = page.getNameLinks());
                    check();
                    if (!isAnyAlive()) return;
                }
                // operation with work
                links.removeAll(checkedList);
                if (links.isEmpty()) {
                    page.refresh();
                    continue;
                }
                String name = links.size() > listParameter ? PSTestData.getRandomFromList(links.subList(0, listParameter)) : links.get(0);//
                //links.get(links.size() - 1);//
                //PSTestData.getRandomFromList(links);
                checkedList.add(name);
                PSLogger.info("Open project '" + name + "'");
                SummaryWorkPage sum1;
                try {
                    PSLogger.info("EDIT!");
                    //sum1 = page.openWork(name, false, false);
                    //sum1 = sum1.edit().submitChanges();
                    String id = PSPage.getIdsFromLinkList(_links, true).get(name);
                    EditWorkPage.doNavigatePageEdit(id).submitChanges();
                } catch (SeleniumException se) {
                    PSLogger.save("Exception on page during edit");
                    PSLogger.error(se.getMessage());
                }
                check();
                page.open();
                //page.more();
                check();
            } catch (SeleniumException se) {
                PSLogger.save("Exception on page during edit");
                PSLogger.fatal(se);
            } catch (AssertionError ae) {
                PSLogger.save("AssertionError on page during delete/edit");
                PSLogger.fatal(ae);
            }
        }
    }

    private List<String> toNames(List<Link> links) {
        List<String> names = new ArrayList<String>();
        for (Link l : links) {
            String t;
            if (checkedList.contains(t = l.getName())) continue;
            names.add(t);
        }
        return names;
    }

    private class Data extends AbstractData {
        private List<Work> works;
        private String id;
        private Work parent;

        private Data(Work parent) {
            this((User) CoreProperties.getDefaultUser().clone(), "del", -1);
            this.parent = parent;
        }

        private Data(int id) {
            this((User) CoreProperties.getDefaultUser().clone(), String.valueOf(id), -1);
        }


        private Data(int id, int count) {
            this((User) CoreProperties.getDefaultUser().clone(), String.valueOf(id), count);
        }

        private Data(User user, String id, int worksCount) {
            this.user = user;
            this.id = id;
            if (worksCount > 0) {
                works = new ArrayList<Work>();
                for (int i = 0; i < worksCount; i++) { // for sorting
                    works.add(new MySGPGatedProject("Test-" + CoreProperties.getTestTemplate() + "-" + (worksCount - i)));
                    // + "-((" + id + "))"));
                }
            }
        }

        public boolean isWorkGenerator() {
            return works != null;
        }

        public boolean isDelete() {
            return parent != null;
        }

        public String toString() {
            return (isWorkGenerator() ? "Work generation thread" : parent != null ? "Deleting thread" : "Portfolio checker thread") + "(" + id + ")";
        }
    }


    private class MySGPGatedProject extends GatedProject {
        public MySGPGatedProject(String name) {
            super(name, getTestData().getSGPTemplate());
            setParent(getTestData().getOrg87444());
            setDeleted();
        }

        public boolean skipDates() {
            return true;
        }
    }


}
