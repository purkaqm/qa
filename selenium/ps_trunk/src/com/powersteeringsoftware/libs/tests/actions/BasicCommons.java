package com.powersteeringsoftware.libs.tests.actions;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.page_locators.ProblemPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.AgentsPage;
import com.powersteeringsoftware.libs.pages.HomePage;
import com.powersteeringsoftware.libs.pages.LogInPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.*;
import static com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators.*;

public class BasicCommons {

    private static Map<SeleniumDriver, User> currentUsers = new HashMap<SeleniumDriver, User>();

    /**
     * default first log in under user specified in properties
     *
     * @return true is success
     */
    public static HomePage logIn() {
        return logIn(true);
    }

    public static HomePage logIn(boolean getVersion) {
        CoreProperties.loadProperties();
        return logIn(CoreProperties.getDefaultUser(), getVersion);
    }

    public static HomePage logIn(User user) {
        return logIn(user, false);
    }

    public static HomePage logIn(String login, String pass, boolean getVersion) {
        return logIn(new User(login, pass), getVersion);
    }

    public static HomePage logIn(String login, String pass) {
        return logIn(login, pass, false);
    }

    public static HomePage logIn(User user, boolean parseVersion) {
        return logIn(user, parseVersion, null);
    }

    public static HomePage logIn(User user, boolean parseVersion, String context) {
        CoreProperties.loadProperties();
        if (!isDriverInit()) {
            initNewDriver(CoreProperties.getURLServerHost(),
                    context != null ? context : CoreProperties.getURLContext());
        }
        PSLogger.info("Try to log in under " + user.getLogin() + ":" + user.getPassword());
        if (isLoggedIn()) {
            PSPage page = PSPage.getEmptyInstance();
            PSLogger.save("Seems already log-inned");
            String name = page.getFullUserName();
            if (user.getFullName() == null) {
                user.setFullName(name);
            }
            if (name.equals(user.getFullName())) {
                if (getCurrentUser() == null)
                    putUserToTestSession(user);
                if (!TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION_NUM)) {
                    HomePage home = new HomePage();
                    if (!home.checkUrl())
                        home.open();
                    return home;
                }
            } else {
                PSLogger.warn("Seems incorrect user session : " + name + ", try again (" + user + ")");
            }
            logOut();
        }
        LogInPage logInPage = new LogInPage();
        if (parseVersion) parseVersion(logInPage);
        HomePage home = logInPage.doLogIn(user.getLogin(), user.getPassword());
        if (home != null) {
            String name = home.getFullUserName();
            if (user.getFullName() != null) {
                Assert.assertEquals(name, user.getFullName(), "Incorrect user session after log in");
            } else {
                user.setFullName(name);
            }
            putUserToTestSession(user);
            if (parseVersion) parseDBVersion(home);
            PSLogger.debug("Login has passed.");
            home.selectParentFrame();
            return home;
        }
        return logIn(user, parseVersion);
    }

    private static void parseVersion(LogInPage page) {
        if (!TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION)) return;
        PSLogger.debug("Parse version");
        /*
        * Extract the application version, store in TestSession for future use.
        * Assume the description looks like follows: PowerSteering / 7.1 - 3307b129
        */

        putVersion(page.getPageVersion());
        String description = page.getVersion();
        if (description != null) {
            TestSession.putObject(TestSession.Keys.APPLICATION_VERSION, description);
            // old way (ver < 10.0) :
            putVersion(description.replaceAll("^[A-Za-z]+\\s/*\\s*(([0-9\\.]+)|([A-Z]+))\\s(/|-)\\s*[\\w]+$", "$1"));
        } else {
            PSLogger.debug("Can't find full version on log-in page");
        }
        Assert.assertFalse(TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION_NUM),
                "Can not parse the application version by description: '" + description + "'");
    }

    private static void putVersion(String version) {
        if (!TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION_NUM)) return;
        for (PowerSteeringVersions ver : PowerSteeringVersions.values()) {
            if (ver.getValue().equals(version)) {
                TestSession.putObject(TestSession.Keys.APPLICATION_VERSION_NUM, ver.toString());
                return;
            }
        }
    }

    private static void parseDBVersion(HomePage home) {
        if (!TestSession.isObjectNull(TestSession.Keys.DB_VERSION)) return;
        PSLogger.debug("Parse DB version(full)");
        //if (!LocalServerUtils.getLocalServer().isWinServer() && !TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION)) {
        //    return;
        //}
        if (SeleniumDriverFactory.getDriver().getType().isZero()) return;
        String alert = home.getHelpMenuVersion();
        if (alert == null) alert = "Unknown";
        PSLogger.info(alert);
        if (TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION)) {
            String v = alert.replaceAll(".*:", "").replaceAll("\\s*-\\s*" + HELP_ALERT_DB_VERSION.getLocator() + ".*", "").trim();
            TestSession.putObject(TestSession.Keys.APPLICATION_VERSION, v);
        }
        String version = HELP_ALERT_DB_VERSION.getLocator() + " " + alert.replaceAll(".*\\s+(\\d+)$", "$1");
        TestSession.putObject(TestSession.Keys.DB_VERSION, version);
    }

    public static boolean isLoggedIn() {
        try {
            PSPage page = PSPage.getEmptyInstance();
            Link link = page.getLogoutLink();
            page.selectTopFrame();
            if (link.exists()) {
                return true;
            }
        } catch (SeleniumException e) {
            PSLogger.warn(e.getMessage());
            PSLogger.save();
        }
        PSLogger.debug("Seems no logged in");
        return false;
    }

    public static void logOut() {
        PSLogger.info("Try to log out (" + getCurrentUser() + ")");
        PSPage.getEmptyInstance().doLogout();
        currentUsers.remove(getDriver());
    }


    private static void putUserToTestSession(User user) {
        PSLogger.debug("Put user " + user + " to current");
        currentUsers.put(getDriver(), TestSession.putUser(user));
    }

    public static User getCurrentUser() {
        if (!isDriverInit()) return null;
        SeleniumDriver driver = getDriver();
        if (!currentUsers.containsKey(driver)) return null;
        return currentUsers.get(driver);
    }

    public static boolean reLogIn() {
        // do not push log out. just restart driver and log in:
        return reLogIn(false);
    }

    /**
     * method called in onTestFailure, so, try-catch
     *
     * @param doLogOut - if true make log out
     * @return true if ok
     */
    private static boolean reLogIn(boolean doLogOut) {
        boolean res = true;
        CoreProperties.loadProperties();
        PSLogger.debug("re-login");
        User currentUser = getCurrentUser();
        SeleniumDriver currentDriver = isDriverInit() ? getDriver() : null;
        if (currentDriver != null && doLogOut) {
            res &= logOutWithoutThrow();
        }
        restartLastSeleniumDriver();
        if (getDriver().getType().isWebDriver()) // its for ie-web-driver, log out.
            res &= logOutWithoutThrow();
        currentUsers.remove(currentDriver);
        if (currentUser != null)
            currentUsers.put(getDriver(), currentUser);
        res &= logInWithoutThrow();
        LocalServerUtils.resetFullScreen();
        return res;
    }

    public static void closeAll() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        currentUsers.clear();
    }

    /**
     * @return true if ok
     */
    public static boolean logOutWithoutThrow() {
        try {
            if (!isLoggedIn()) {
                PSLogger.debug("Can't log out.");
                return true;
            }
            logOut();
        } catch (Throwable t) {
            PSLogger.error("logOutWithoutThrow");
            PSLogger.fatal(t);
            return false;
        }
        return true;
    }

    /**
     * @return true if ok
     */
    public static boolean logInWithoutThrow() {
        try {
            if (getCurrentUser() != null) {
                // for relogin
                logIn(getCurrentUser(), false);
            } else {
                logIn();
            }
        } catch (Throwable e) {
            PSLogger.error("logInWithoutThrow:");
            PSLogger.fatal(e);
            try {
                PSLogger.save("logInWithoutThrow");
            } catch (Throwable t) {
                PSLogger.error(t);
            }
            return false;
        }
        return true;
    }

    /**
     * hotfix method for reindex basic search (qaautoserver02)
     */
    public static HomePage reindex() {
        try {
            AgentsPage page = new AgentsPage();
            page.open();
            page.reindexBasicSearch();
            HomePage home = new HomePage();
            home.open();
        } catch (SeleniumException se) {
            PSLogger.fatal(se);
        }
        return null;
    }


    public static Exception validatePage() {
        if (!isDriverInit()) return null;
        PSLogger.info("Check page");
        try {
            PSPage.getEmptyInstance().validate();
        } catch (Exception e) {
            PSLogger.error("validatePage error: " + e.getMessage());
            //PSLogger.fatal(e); this exception will catch in onTestFailure, so just error
            return e;
        }
        return null;
    }

    public static Boolean isServerAvailable() {
        if (!isDriverInit()) {
            PSLogger.warn("No driver init");
            return null;
        }
        try {
            PSPage page = PSPage.getEmptyInstance();
            Element problem = new Element(ProblemPageLocators.SERVER_UNAVAILABLE_LOCATOR);
            page.selectTopFrame();
            if (problem.exists()) {
                PSLogger.save();
                PSLogger.error("Service Temporarily Unavailable");
                return false;
            } else {
                return page.getBodyElement().exists() ?
                        true : // all ok
                        null;  // can't find problem and body. seems server is broken.
            }
        } catch (SeleniumException e) {
            PSLogger.warn(e.getMessage());
        } catch (Throwable e) {
            PSLogger.fatal("exception in BasicCommons::isServerAvailable");
            PSLogger.fatal(e);
        }
        return null;
    }

    public static boolean isContextNotReady() {
        Element waitMsg = new Element(CONTEXT_WAIT_ELEMENT);
        return waitMsg.exists() && waitMsg.getText().contains(CONTEXT_WAIT_MESSAGE.getLocator());
    }

}
