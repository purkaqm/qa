package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.AssignUsersComponent;
import com.powersteeringsoftware.libs.elements.Link;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 18.05.2014.
 */
public class Issue90169Test2 extends PSParallelTestDriver {
    private List<Work> works = Collections.synchronizedList(new ArrayList<Work>());
    public static final String WORK_PREFIX = "GP90169";
    protected boolean createNewOrg = true;
    protected int collectorType = 0; // 0 - home, 1 - portfolio, 2 - summary
    protected boolean clear = false;
    private static final int ASYNC_TIMEOUT = 2000;
    private static final long TIME = 60 * 60 * 1000; // hour

    int getCreationThreads() {
        return 1;
    }

    int getCheckerThreads() {
        return 4;
    }

    int getNumWorks() {
        return getCreationThreads() * 2;
    }

    String getWorkPrefix() {
        return WORK_PREFIX + "-" + CoreProperties.getTestTemplate() + "-";
    }

    protected Work org;
    protected Template tmpl;
    protected Portfolio profile;
    protected User loginUser;

    @BeforeTest
    public void prepare() {
//        tmpl = getTestData().getSGTemplate90169();
        tmpl = getTestData().getSGPTemplate90169_2();
        org = getTestData().getOrg90169();
        profile = getTestData().getPortfolio90169();

        loginUser = collectorType != 0 ? CoreProperties.getDefaultUser() : getTestData().getFirstUser();

        if (createNewOrg) {
            org = Work.createOrg(org.getName() + "-" + CoreProperties.getTestTemplate());
            if (collectorType == 0)
                org.addUser(BuiltInRole.CONTRIBUTOR, loginUser); // full perm for contributor
            WorkManager.createProject(org);
            if (collectorType == 1) {
                PortfoliosPage page = new PortfoliosPage();
                page.open();
                EditPortfolioPage edit = page.openPortfolio(profile.getName()).edit();
                edit.clearDescendedFrom();
                edit.setDescendedFrom(org);
                edit.submit();
            }
        }

        CoreProperties.setWaitForElementToLoad(1000 * 1000);

        SummaryWorkPage.setWaitGrid(true);
        SummaryWorkPage.setDoCheck(false);
        EditWorkPage.setWaitLoading(false);
        PSPage.setCheckBlankPage(false);
        Page.setCheckJs(false);
        AbstractPage.setGetDocumentOnOpen(false);
        CoreProperties.setMakeScreenCapture(false);
        CoreProperties.setDoValidatePage(false);


        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        int count = getCreationThreads() + getCheckerThreads() + (clear ? 2 : 1);
        Object[][] res = new Object[count][0];
        for (int i = 0; i < getCreationThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i, 1)};
        }
        for (int i = getCreationThreads(); i < getCheckerThreads() + getCreationThreads(); i++) {
            res[i] = new Object[]{new Data((User) loginUser.clone(), i, 0)};
        }
        res[count - 1] = new Object[]{new Data((User) loginUser.clone(), null, 2)};
        if (clear)
            res[count - 2] = new Object[]{new Data((User) loginUser.clone(), null, 3)};
        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isWorkGenerator()) {
            workGeneratorThread(data);
        } else if (data.isChecker()) {
            checkerThread(data);
        } else if (data.isCollect()) {
            collector(data);
        } else {
            BasicCommons.logIn(data.user, !TestSession.isVersionPresent());
            clearCache(false, true);
        }
    }

    protected void collector(Data d) throws InterruptedException {
        PSPage page = BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        if (collectorType == 1) {
            page = PortfolioDetailsPage.openPage(profile.getId());
        } else if (collectorType == 2) {
            page = WorkManager.open(org);
        }
        while (isAnyAlive()) {
            List<Link> _links = page instanceof PSTablePage ? ((PSTablePage) page).getNameLinks() : ((SummaryWorkPage) page).getGrid().getWorkLinks();

            Map<String, String> _nameIdMap = PSPage.getIdsFromLinkList(_links, true);
            PSLogger.info("Links: " + _nameIdMap.keySet());
            for (Work w : works) {
                if (w.getId() != null) continue;
                String name = w.getName();
                if (!_nameIdMap.containsKey(name)) continue;
                PSLogger.info("Work=" + name + ",id=" + _nameIdMap.get(name));
                try {
                    w.setId(_nameIdMap.get(name));
                } catch (Exception e) {
                    PSLogger.error(e);
                }
            }
            PSLogger.info("Works: " + works);
            Thread.sleep(100);
            //clearCache(false, false);
            try {
                open(page);
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
        }
    }

    private void workGeneratorThread(Data d) throws InterruptedException {
        d.isRunning = true;
        list.add(d);
        int k = 1;
        try {
            BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
            Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
            while (works.size() < getNumWorks()) {
                if (DeadlockParallelListener.hasFailure()) return;
                long t = (TIME - System.currentTimeMillis() + CoreProperties.getTestTemplate()) / 1000;
                Work w = new MyGatedProject(getWorkPrefix() + t, collectorType == 0 ? null : d.user);
                try {
                    check();
                    PSLogger.info("START creation of '" + w.getName() + "'.Create Iteration #" + k++);
                    works.add(w);
                    WorkManager.createProject(w);
                    check();
                } catch (Throwable se) {
                    PSLogger.save("END Exception/Error on  page during creation project '" + w.getName() + "'");
                    processThrowable(se);
                } finally {
                    PSLogger.info("Set work " + w + " created.");
                    w.setCreated();
                }
            }
        } finally {
            d.isRunning = false;
        }
    }

    private class MyGatedProject extends GatedProject {
        private boolean exist;
        private String id;
        private String name;

        public MyGatedProject(String name, User contributor) {
            super(name, tmpl);
            this.name = name;
            setParent(org);
            if (contributor != null)
                addUser(BuiltInRole.CONTRIBUTOR, contributor);
            setStatus(Status.ON_TRACK);
        }

        public synchronized boolean exist() {
            return exist;
        }

        /*public boolean hasSecondCreateWorkPage() {
            return true;
        }*/

        public synchronized void setCreated() {
            exist = true;
        }

        public void setDeleted() {
            exist = false;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return "[" + name + "][" + id + "][" + exist + "]";
        }
    }

    private static List<Work> getNotReadyWorks(List<Work> works) {
        List<Work> _works = new ArrayList<Work>();
        for (Work w : works) {
            if (w.exist()) continue;
            if (w.getId() == null) continue;
            _works.add(w);
        }
        return _works;
    }

    protected void checkerThread(Data d) throws InterruptedException {
        BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        Thread.sleep(PSTestData.getRandom().nextInt(ASYNC_TIMEOUT));
        while (isAnyAlive()) {
            check();
            if (DeadlockParallelListener.hasFailure()) return;
            List<Work> _works = getNotReadyWorks(works);
            if (_works.isEmpty()) {
                Thread.sleep(100);
                continue;
            }
            PSLogger.info("All works: " + works);
            PSLogger.info("Works to check: " + _works);
            check();
            try {
                Work w = PSTestData.getRandomFromList(_works);
                if (w.exist()) continue;
                PSLogger.info("Edit. Work " + w);
                edit(w);
            } catch (Throwable se) {
                PSLogger.save("Exception/Error on page during edit");
                processThrowable(se);
            }
            check();
        }
    }

    private void edit(Work w) throws InterruptedException {
        PSLogger.info("START: edit project '" + w + "'");
        EditWorkPage edit = EditWorkPage.doNavigatePageEdit(w.getId());
        if (w.exist()) {
            PSLogger.info("Stop editing " + w);
            return;
        }
        AssignUsersComponent auc = edit.getAssignUserComponent();
        Map<String, List<String>> map = auc.getUsers();
        String oldOwner = map.get(BuiltInRole.OWNER.getName()).get(0);
        User user = null;
        List<User> _users = getTestData().getUsers();
        PSTestData.mixList(_users);
        for (User u : _users) {
            if (!u.getFormatFullName().equals(oldOwner)) {
                user = u;
                break;
            }
        }
        PSLogger.info("User to delegate : " + user);
        if (w.exist()) {
            PSLogger.info("Stop editing " + w);
            return;
        }
        auc.doAssign(user, BuiltInRole.OWNER);
        if (w.exist()) {
            PSLogger.info("Cancel editing " + w);
            edit.clickCancelChanges(false);
            return;
        }
        PSLogger.info("Before finish editing " + w);
        edit.clickSubmitChanges(false);
        Thread.sleep(1000);
        PSLogger.info("END: finish editing '" + w + "', user=" + user.getFullName());

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

        public boolean isWorkGenerator() {
            return type == 1;
        }

        public boolean isChecker() {
            return type == 0;
        }

        public boolean isCollect() {
            return type == 2;
        }

        public boolean isClear() {
            return type == 3;
        }

        public String toString() {
            return (isWorkGenerator() ? "Work creation thread" : isChecker() ? "Checker thread" : isCollect() ? "Collector" : "Clear cache") + "(" + id + ")";
        }
    }


}
