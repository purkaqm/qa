package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.AssignUsersComponent;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.EditWorkPage;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 07.05.2014.
 */
public class Issue90169Test extends Issue89410Test {

    private Integer iterationNum;

    @BeforeTest
    public void prepare() {
        tmpl = getTestData().getSGTemplate90169();
        org = getTestData().getOrg90169();
        loginUser = getTestData().getFirstUser();
        SummaryWorkPage.setWaitGrid(false);
        EditWorkPage.setWaitLoading(false);
        iterationNum = null;
        doDelete = false;
        doAfter = false;
        clear = false;
        if (createNewOrg) {
            org = Work.createOrg(org.getName() + "-" + CoreProperties.getTestTemplate());
            org.addUser(BuiltInRole.CONTRIBUTOR, loginUser);
            WorkManager.createProject(org);
            //BasicCommons.reindex();
        }
        PSPage.setCheckBlankPage(true);
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    int getCreationThreads() {
        return 2;
    }

    int getCheckerThreads() {
        return 4;
    }

    int getNumWorks() {
        return 5;
    }

    String getWorkPrefix() {
        return "GP90169" + "-" + CoreProperties.getTestTemplate() + "-";
    }

    private Long start;
    private static final Long TIMEOUT_AFTER = 2 * 60 * 1000L;

    protected boolean isAnyAlive() {
        if (super.isAnyAlive()) return true;
        if (start == null) start = System.currentTimeMillis();
        return System.currentTimeMillis() - start < TIMEOUT_AFTER;
    }

    protected void checkerThread(Data d) {
        HomePage page = BasicCommons.logIn(d.user, !TestSession.isVersionPresent());
        int k = 0;
        //AgendaPage.Projects page = new AgendaPage.Projects();
        while (isAnyAlive()) {
            if (DeadlockParallelListener.hasFailure()) return;
            try {
                List<Link> _links;
                open(page);
                List<String> links = toNames(_links = page.getNameLinks());
                while (links.isEmpty()) {
                    open(page);
                    links = toNames(_links = page.getNameLinks());
                    check();
                    if (!isAnyAlive()) return;
                    if (links.isEmpty()) {
                        continue;
                    }
                }
                links.removeAll(checkedList);
                if (links.isEmpty()) {
                    continue;
                }
                String name = links.size() > listParameter ? PSTestData.getRandomFromList(links.subList(0, listParameter)) : links.get(0);
                checkedList.add(name);
                try {
                    String id = PSPage.getIdsFromLinkList(_links, true).get(name);
                    PSLogger.info("Edit Iteration #" + ++k);
                    if (iterationNum == null) {
                        edit(id, name);
                    } else {
                        int j = 0;
                        do {
                            edit(id, name);
                        } while (!completedList.contains(name) || j++ < iterationNum);
                    }
                } finally {
                    checkedList.remove(name);
                }
                check();
            } catch (Throwable se) {
                PSLogger.save("Exception on page during checkThread");
                PSLogger.fatal(se);
                if (hasDeadlock()) return;
                page = openHome();
            }
        }
    }

    private PSPage edit(String id, String name) {
        check();
        PSPage res = null;
        try {
            PSLogger.info("START: edit project '" + name + "'");
            EditWorkPage edit = EditWorkPage.doNavigatePageEdit(id);
            AssignUsersComponent auc = edit.getAssignUserComponent();
            Map<String, List<String>> map = auc.getUsers();
            String suser = map.get(BuiltInRole.OWNER.getName()).get(0);
            User user = null;
            List<User> _users = getUsers();
            PSTestData.mixList(_users);
            for (User u : _users) {
                if (!u.getFormatFullName().equals(suser)) {
                    user = u;
                    break;
                }
            }
            PSLogger.info("User: " + user);
            auc.doAssign(user, BuiltInRole.OWNER);
            res = edit.submitChanges();
            PSLogger.info("END: finish editing '" + name + "', user=" + user.getFullName());
        } catch (Exception se) {
            PSLogger.save("END: exception on page during edit '" + name + "'");
            PSLogger.error(se.getMessage());
            res = openHome();
        } catch (AssertionError ae) {
            PSLogger.save("AssertionError on page during delete/edit");
            PSLogger.fatal(ae);
            if (!hasDeadlock())
                res = openHome();
        }
        check();
        return res;
    }

    private List<User> users;

    private List<User> getUsers() {
        if (users != null) return users;
        users = new ArrayList<User>();
        users.add(getTestData().getUser2());
        users.add(getTestData().getUser3());
        users.add(getTestData().getUser4());
        users.add(getTestData().getUser5());
        return users;
    }
}
