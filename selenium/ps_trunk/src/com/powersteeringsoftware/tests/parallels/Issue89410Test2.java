package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 15.05.2014.
 */
public class Issue89410Test2 extends PSParallelTestDriver {
    private List<Work> works = Collections.synchronizedList(new ArrayList<Work>());
    private int worksSize;
    private List<Work> deletedList = Collections.synchronizedList(new ArrayList<Work>());
    public static final String WORK_PREFIX = "GP89410";
    protected boolean createNewOrg = false;
    private static final int ASYNC_TIMEOUT = 2000;

    private Integer max = 10;

    int getDeletionThreads() {
        return 1;
    }

    int getCheckerThreads() {
        return 5;
    }

    int getNumWorks() {
        return 10;
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

//        tmpl = getTestData().getSGTemplate89410();
//        org = getTestData().getOrg89410();
//        profile = getTestData().getPortfolio89410();
        tmpl = getTestData().getSGTemplate89410_2();
        org = getTestData().getOrg89410_2();
        profile = getTestData().getPortfolio89410_2();

        loginUser = CoreProperties.getDefaultUser();
        if (createNewOrg) {
            org = Work.createOrg(org.getName() + "-" + CoreProperties.getTestTemplate());
            WorkManager.createProject(org);
        }
        PortfoliosPage page1 = new PortfoliosPage();
        page1.open();
        PortfolioDetailsPage page2;
        if (createNewOrg) {
            EditPortfolioPage edit = page1.openPortfolio(profile.getName()).edit();
            edit.clearDescendedFrom();
            edit.setDescendedFrom(org);
            page2 = edit.submit();
        } else {
            page2 = PortfolioDetailsPage.openPage(profile.getId());
        }
        page2.more();
        List<Link> _links = page2.getNameLinks();
        Map<String, String> _nameIdMap = PSPage.getIdsFromLinkList(_links, true);
        for (String name : _nameIdMap.keySet()) {
            Work w = new GatedProject(name, tmpl);
            w.setId(_nameIdMap.get(name));
            w.setParent(org);
            works.add(w);
        }
        int i = 1;
        CoreProperties.setWaitForElementToLoad(120000);
        while (works.size() < getNumWorks()) {
            Work w = new GatedProject(getWorkPrefix() + i++, tmpl);
            w.setParent(org);
            WorkManager.createProject(w);
            works.add(w);
        }
        worksSize = works.size();
        PSLogger.info("Works: " + works);

        SummaryWorkPage.setWaitGrid(false);
        SummaryWorkPage.setDoCheck(true);
        EditWorkPage.setWaitLoading(true);
        PSPage.setCheckBlankPage(false);
        Page.setCheckJs(false);
        AbstractPage.setGetDocumentOnOpen(false);
        CoreProperties.setMakeScreenCapture(true);
        CoreProperties.setDoValidatePage(true);


        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        int count = getDeletionThreads() + getCheckerThreads();
        Object[][] res = new Object[count][0];
        for (int i = 0; i < getDeletionThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i, 1)};
        }
        for (int i = getDeletionThreads(); i < getCheckerThreads() + getDeletionThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i, 0)};
        }
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isWorkRemover()) {
            deletionThread(data);
        } else if (data.isChecker()) {
            checkerThread(data);
        }
    }

    private void deletionThread(Data d) throws InterruptedException {
        d.isRunning = true;
        list.add(d);
        int k = 1;
        try {
            BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
            Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
            while (!works.isEmpty()) {
                if (DeadlockParallelListener.hasFailure()) return;
                Work w = works.remove(PSTestData.getRandom().nextInt(works.size()));
                int index = worksSize - works.size();
                try {
                    deletedList.add(w);
                    check();
                    PSLogger.info("Delete. Iteration #" + (k++) + "/" + index + ". Work " + w);
                    WorkManager.deleteProject(w);
                    check();
                } catch (Throwable se) {
                    PSLogger.save("END Exception/Error on page during deleting project '" + w.getName() + "'");
                    processThrowable(se);
                } finally {
                    deletedList.remove(w);
                }
                if (max != null && index > max) break;
            }
        } finally {
            d.isRunning = false;
        }
    }

    protected void checkerThread(Data d) throws InterruptedException {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
        while (isAnyAlive()) {
            if (DeadlockParallelListener.hasFailure()) return;
            if (deletedList.isEmpty()) {
                Thread.sleep(1000);
                continue;
            }

            while (!deletedList.isEmpty()) {
                check();
                try {
                    Work w = PSTestData.getRandomFromList(deletedList);
                    PSLogger.info("Edit. Work " + w);
                    EditWorkPage.doNavigatePageEdit(w.getId()).submitChanges();
                } catch (Throwable se) {
                    PSLogger.save("Exception/Error on page during edit");
                    processThrowable(se);
                }
                check();
                if (!isAnyAlive()) return;
            }
        }
    }

    class Data extends AbstractData {

        private String id;
        private int type;

        private Data(User u, int id, int count) {
            this(u, String.valueOf(id), count);
        }

        private Data(User user, String id, Integer type) {
            this.user = user;
            this.id = id;
            this.type = type;
        }

        public boolean isWorkRemover() {
            return type == 1;
        }

        public boolean isChecker() {
            return type == 0;
        }

        public String toString() {
            return (isWorkRemover() ? "Work deletion thread" : "Checker thread") + "(" + id + ")";
        }
    }

}
