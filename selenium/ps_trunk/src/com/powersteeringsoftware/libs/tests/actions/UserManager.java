package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.pages.InviteNewUserPage;
import com.powersteeringsoftware.libs.pages.PeopleManageActivePage;
import com.powersteeringsoftware.libs.pages.ProfilePage;
import com.powersteeringsoftware.libs.pages.UserProfilePage;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;


/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class UserManager {
    private static final String DAYS_OF_NAME = "my holidays!";

    public static ProfilePage openProfile(User user) {
        ProfilePage res;
        if (user.getId() != null) {
            res = new ProfilePage();
            res.open(user.getId());
            setUserInfo(user, res, false);
            user.setCreated();
            return res;
        }
        PeopleManageActivePage page = new PeopleManageActivePage();
        page.open();
        page.search();
        res = page.goToUserProfilePage(user).edit();
        user.setId(res.getUrlId());
        setUserInfo(user, res, false);
        user.setCreated();
        return res;
    }

    private static void setUserInfo(User user, ProfilePage page, boolean overwrite) {
        if (overwrite || user.getFirstName() == null) {
            user.setFirstName(page.getFirstName());
        }
        if (overwrite || user.getLastName() == null) {
            user.setLastName(page.getSecondName());
        }
    }

    public static void createUser(User u) {
        if (u.exist()) throw new IllegalArgumentException("User " + u + " already created");
        InviteNewUserPage invite = new InviteNewUserPage();
        invite.open();
        invite.setFirstName(u.getFirstName());
        invite.setLastName(u.getLastName());
        invite.setEmail(u.getEmail());
        invite.submit();
        UserProfilePage profile = invite.toProfile();
        Assert.assertNotNull(profile, "Some problem when create user " + u);
        ProfilePage edit = profile.edit();
        edit.setPassword(u.getPassword());
        if (u.getLogin() != null) {
            edit.setLogin(u.getLogin());
        } else {
            u.setLogin(edit.getLogin());
        }
        u.setId(edit.getUrlId());
        edit.submit();
        u.setCreated();
        u.setIsAdmin(false);
    }

    public static void doLogIn(User u) {
        BasicCommons.logIn(u, !TestSession.isVersionPresent());
    }

    public static void changeUserCalendar(User user, PSCalendar calendar) {
        PeopleManageActivePage page = new PeopleManageActivePage();
        page.open();
        page.search();
        ProfilePage profile = page.goToUserProfilePage(user).edit();
        profile.selectCalendar(calendar);
        profile.submit();
        user.setCalendar(calendar);
    }

    public static void setDaysOff(User user, String name, PSCalendar start, PSCalendar end) {
        PeopleManageActivePage page = new PeopleManageActivePage();
        page.open();
        page.search();
        ProfilePage profile = page.goToUserProfilePage(user).edit();
        profile.setDaysOff(name, start, end);
        profile.submit();
        user.addDaysOff(start, end);
    }

    public static void removeDaysOff(User user) {
        PeopleManageActivePage page = new PeopleManageActivePage();
        page.open();
        page.search();
        ProfilePage profile = page.goToUserProfilePage(user).edit();
        profile.removeAllDaysOff();
        profile.submit();
        user.removeDaysOff();
    }

    public static void setDaysOff(User user, PSCalendar start, PSCalendar end) {
        setDaysOff(user, DAYS_OF_NAME, start, end);
    }

    public static void setNewUserName(User newDefaultUser) {

        PSLogger.info("Set new settings for user " + BasicCommons.getCurrentUser() + ": " + newDefaultUser);
        ProfilePage profile = new ProfilePage();
        profile.open();
        profile.setNewUserName(newDefaultUser);
        BasicCommons.getCurrentUser().setFirstName(newDefaultUser.getFirstName());
        BasicCommons.getCurrentUser().setLastName(newDefaultUser.getLastName());

        for (User u : TestSession.getAllUsers()) {
            PSLogger.debug(u + "," + u.hashCode());
        }
        PSLogger.debug("Now: " + BasicCommons.getCurrentUser() + ", " + BasicCommons.getCurrentUser().hashCode());
    }
}
