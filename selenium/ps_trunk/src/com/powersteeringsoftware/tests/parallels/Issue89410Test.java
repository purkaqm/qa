package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.SimpleGrid;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Portfolio;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 23.01.14
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 * DB: sz_100_def_deadlocks
 */
public class Issue89410Test extends PSParallelTestDriver {

    protected List<String> okList = Collections.synchronizedList(new ArrayList<String>());
    protected List<String> completedList = Collections.synchronizedList(new ArrayList<String>());
    protected List<String> allList = Collections.synchronizedList(new ArrayList<String>());
    protected List<String> checkedList = Collections.synchronizedList(new ArrayList<String>());
    private static final long time = 60 * 60 * 1000; // hour
    protected static int listParameter = 5;
    protected static int deleteParameter = 5;
    public static final String WORK_PREFIX = "GP89410";
    protected boolean doDelete = true;
    protected boolean createNewOrg = true;
    protected boolean doAfter = true;
    protected boolean clear;

    int getCreationThreads() {
        return 4;
    }

    int getCheckerThreads() {
        return 4;
    }

    int getNumWorks() {
        return 25;
    }

    String getWorkPrefix() {
        return WORK_PREFIX + "-" + CoreProperties.getTestTemplate() + "-";
    }

    protected Work org;
    private Portfolio profile;
    protected Template tmpl;
    protected User loginUser;

    @BeforeTest
    public void prepare() {
        tmpl = getTestData().getSGTemplate89410();
        org = getTestData().getOrg89410();
        profile = getTestData().getPortfolio89410();
        SummaryWorkPage.setWaitGrid(false);
        EditWorkPage.setWaitLoading(true);
        loginUser = CoreProperties.getDefaultUser();
        if (createNewOrg) {
            org = Work.createOrg(org.getName() + "-" + CoreProperties.getTestTemplate());
            WorkManager.createProject(org);
            //BasicCommons.reindex();
            PortfoliosPage page = new PortfoliosPage();
            page.open();
            EditPortfolioPage edit = page.openPortfolio(profile.getName()).edit();
            edit.clearDescendedFrom();
            edit.setDescendedFrom(org);
            edit.submit();
        }
        PSPage.setCheckBlankPage(true);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    @AfterTest
    public void after() {
        check();
        if (!doAfter) return;
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(false);
        BasicCommons.logIn();
        if (createNewOrg) {
            WorkManager.deleteProject(org);
            return;
        }
        int i = 0;
        WBSPage pc = null;
        while (i++ < deleteParameter) {
            try {
                PSLogger.info("Bulk delete iteration #" + i);
                pc = WorkManager.openWBS(org);
                pc.getGrid().selectWorks();
                pc.bulkDelete().yes();
                return;
            } catch (Exception e) {
                PSLogger.error("Bulk delete exception: " + e);
            } finally {
                if (pc == null) continue;
                SimpleGrid.ApplyButton save = pc.getGrid().getSaveButton();
                if (save.isEnabled()) {
                    try {
                        save.submit();
                    } catch (AssertionError ae) {
                        PSLogger.fatal(ae);
                    }
                }

            }
        }
    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        int count = getCreationThreads() + getCheckerThreads() + (clear ? 1 : 0);
        Object[][] res = new Object[count][0];
        for (int i = 0; i < getCreationThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i, getNumWorks())};
        }
        for (int i = getCreationThreads(); i < getCheckerThreads() + getCreationThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i)};
        }
        if (clear)
            res[count - 1] = new Object[]{new Data(CoreProperties.getDefaultUser())};
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isWorkGenerator()) {
            creationThread(data);
        } else if (data.isChecker()) {
            checkerThread(data);
        } else {
            clearThread(data);
        }
    }

    private void creationThread(Data d) {
        d.isRunning = true;
        list.add(d);
        int k = 0;
        try {
            BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
            for (Work w : d.works) {
                if (DeadlockParallelListener.hasFailure()) return;
                try {
                    check();
                    long t = (time - System.currentTimeMillis() + CoreProperties.getTestTemplate()) / 1000;
                    try {
                        String name = getWorkPrefix() + t;
                        allList.add(name);
                        w.setName(name);
                    } catch (NullPointerException ne) {
                        PSLogger.error(ne);
                    }
                    PSLogger.info("Create Iteration #" + ++k);
                    PSLogger.info("START creation of '" + w.getName() + "'");
                    w.setParent(org);
                    if (!doDelete)
                        w.addUser(BuiltInRole.CONTRIBUTOR, d.user);
                    SummaryWorkPage sum1 = (SummaryWorkPage) WorkManager.createProject(w);
                    PSLogger.info("END creation of '" + w.getName() + "'");
                    check();
                    if (doDelete) {
                        WorkManager.deleteProject(sum1);
                        w.setDeleted();
                        check();
                    }
                    okList.add(w.getName());
                } catch (Exception se) {
                    PSLogger.save("END exception on page during creation '" + w.getName() + "'");
                    PSLogger.fatal(se);
                    try {
                        PSPage.getEmptyInstance().getProblemFromPage();
                    } catch (Exception ex) {
                        PSLogger.error(ex);
                    }
                    openHome();
                } catch (AssertionError ae) {
                    PSLogger.save("END assertionError on page during creation project '" + w.getName() + "'");
                    PSLogger.fatal(ae);
                    if (hasDeadlock()) return;
                    try {
                        PSPage.getEmptyInstance().getProblemFromPage();
                    } catch (Exception ex) {
                        PSLogger.error(ex);
                    }
                    openHome();
                } finally {
                    completedList.add(w.getName());
                }
            }
        } finally {
            d.isRunning = false;
        }
    }

    protected void clearThread(Data d) {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        clearCache(true, true);
    }

    protected void checkerThread(Data d) {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        PortfolioDetailsPage page = PortfolioDetailsPage.openPage(profile.getId());
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
                //links.removeAll(okList);
                links.removeAll(checkedList);
                if (links.isEmpty()) {
                    page.refresh();
                    continue;
                }
                String name = links.size() > listParameter ? PSTestData.getRandomFromList(links.subList(0, listParameter)) : links.get(0);

                PSLogger.info("Open project '" + name + "'");
                try {
                    checkedList.add(name);
                    String id = PSPage.getIdsFromLinkList(_links, true).get(name);
                    do { //todo?
                        try {
                            PSLogger.info("EDIT!");
                            EditWorkPage.doNavigatePageEdit(id).submitChanges();
                        } catch (SeleniumException se) {
                            PSLogger.save("Exception on page during edit");
                            PSLogger.error(se.getMessage());
                            page.open();
                            break;
                        }
                        check();
                    } while (!completedList.contains(name));
                } finally {
                    checkedList.remove(name);
                }
                page.open();
                //page.more();
                check();
            } catch (SeleniumException se) {
                PSLogger.save("Exception on page during edit");
                PSLogger.fatal(se);
                page.open();
            } catch (AssertionError ae) {
                PSLogger.save("AssertionError on page during delete/edit");
                PSLogger.fatal(ae);
                if (hasDeadlock()) return;
                page.open();
            }
        }
    }

    List<String> toNames(List<Link> links) {
        List<String> names = new ArrayList<String>();
        for (Link l : links) {
            if (!l.getName().matches(getWorkPrefix() + "\\d+")) continue;
            String t;
            if (okList.contains(t = l.getName())) continue;
            if (!names.contains(t))
                names.add(t);
        }
        return names;
    }

    class Data extends AbstractData {
        private List<Work> works;
        private String id;

        private Data(User u, int id) {
            this(u, String.valueOf(id), -1);
        }

        private Data(User u) {
            this(u, null, null);
        }

        private Data(User u, int id, int count) {
            this(u, String.valueOf(id), count);
        }

        private Data(User user, String id, Integer worksCount) {
            this.user = user;
            this.id = id;
            if (worksCount != null && worksCount > 0) {
                works = new ArrayList<Work>();
                for (int i = 0; i < worksCount; i++) { // for sorting
                    works.add(new MySGPGatedProject(
                            getWorkPrefix() + CoreProperties.getTestTemplate() + "-" + (worksCount - i), tmpl, org));
                }
            }
        }

        public boolean isWorkGenerator() {
            return works != null;
        }

        public boolean isChecker() {
            return works == null && id != null;
        }

        public boolean isClear() {
            return !isWorkGenerator() && !isChecker();
        }

        public String toString() {
            if (isClear()) return "Clear thread";
            return (isWorkGenerator() ? "Work generation thread" : "Checker thread") + "(" + id + ")";
        }
    }


    class MySGPGatedProject extends GatedProject {
        public MySGPGatedProject(String name, Template tmpl, Work org) {
            super(name, tmpl);
            setParent(org);
            setDeleted();
        }

        public boolean skipDates() {
            return true;
        }
    }


}

