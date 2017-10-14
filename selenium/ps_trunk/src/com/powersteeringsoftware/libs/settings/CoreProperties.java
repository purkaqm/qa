package com.powersteeringsoftware.libs.settings;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.enums.page_locators.DiscussionIssueViewPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Class is for getting core properties such as:</br>
 * selenium.server - selenium server host (e.g localhost)<br>
 * selenium.server.port - selenium server port (e.g 4444)<br>
 * selenium.waitforpagetoload - wait for page to load time in mils (e.g. 30000)<br>
 * browser.default - browser by default (e.g. *iehta,*chrome)<br>
 * application.url.serverhost - (e.g https://qa-solid.cinteractive.com)<br>
 * application.context - context (e.g. /quicktest/Home.page)<br>
 * user.main - user process for login<br>
 * user.main.password - user password for login<br>
 * application.url.homepage - home page URL without server hostname<br>
 * application.url.mt.chrary - page "Measure Template:Library" url without
 * server hostname<br>
 * pages.properties - page properties file<br>
 * prefix.project - prefix for new object Project<br>
 * prefix.organization- prefix for new object Organization<br>
 * prefix.measure - prefix for new object Measure<br>
 * prefix.attachedmeasure - prefix for new object Attached Measure <br>
 * prefix.measuretemplate - prefix for new object Measure Template<br>
 * etc<br>
 *
 * @author selyaev_ag
 * @since 2008 December 24
 */
public class CoreProperties {

    /**
     * VM parameters:
     */
    public static final String SYS_PROP_LOG_ROOT_PATH = "LOG_ROOT";
    public static final String SYS_PROP_CONFIG_PATH = "CONFIGS_DIR";
    public static final String SYS_PROP_PROPERTIES_FILE = "PROPERTIES_FILE";
    public static final String SYS_PROP_BROWSER = "BROWSER";
    private static final String SYS_PROP_USE_RC = "USE_RC";
    public static final String SYS_PROP_HOST = "HOST";
    private static final String SYS_PROP_CONTEXT = "CONTEXT"; // -DHOST=http://ps-05:8080/ -DCONTEXT=ppx_94/test/
    private static final String SYS_PROP_LOGS = "LOGS";// log dir
    private static final String SYS_PROP_USE_FF_PROFILE = "P";


    private static final String DEFAULT_LOG_ROOT = "./logs/";
    private static final String DEFAULT_CONFIG_PATH = "./conf/";
    public static final String THIS_WORK_DIR = System.getProperty("user.dir");
    public static final String CONFIG_PATH = getConfigPath();
    private static final String LOGS_FILES = "files"; // see log4j.properties, log.html should be in parent dir
    public static final String DEFAULT_IMAGE_FORMAT = "png";

    public static final String COMMON_PROPERTIES_FILE = "common.properties";
    public static final String DEFAULT_CORE_PROPERTIES_FILE = "core.properties";
    public static final String KNOWN_ISSUES_PROPERTIES_FILE = "knis.properties";
    public static final String KNOWN_ISSUES_PROPERTIES_FILE_DEF_VALUE = "knis_trunk.properties";
    public static final String DB_PROPERTIES_FILE = "context.db.properties";
    public static final String CLIENTS_PROPERTIES_FILE = "clients.properties";
    public static final int DEFAULT_SELENIUM_PORT = 4747;

    public static final long DEFAULT_WAIT_FOR_PAGE_TO_LOAD = Wait.DEFAULT_TIMEOUT;

    public static final String DATE_FORMAT = "application.date.format";
    public static final String DEFAULT_DATE_FORMAT_NO_YEAR = "MM/dd/";
    public static final String DEFAULT_DATE_FORMAT = DEFAULT_DATE_FORMAT_NO_YEAR + "yyyy";
    public static final String DEFAULT_DATE_FORMAT_PATTERN = "\\d{2}/\\d{2}/\\d{4}";
    public static final String DEFAULT_TIMEZONE = "EST5EDT";
    private static final String LOG_EXCEPTIONS = "application.server.log.exception.";
    private static final String LOG_WARNINGS = "application.server.log.warning.";
    private static final String POPUP_EXCEPTIONS = "popup.exception.";
    private static final String PAGE_EXCEPTIONS = "page.exception.";
    private static final String GROUP_EXCEPTION = "groups.";

    public static final long BEFORE_TEST_TIMEOUT = 15 * 60 * 1000;

    public static final int DATA_PROVIDER_THREADS_MAX_NUMBER = 20;

    private static Set driverTypeSet = new HashSet();

    private static Properties properties;
    private static Package pages;

    //private static Properties knisProperties;

    //if true then run tests, otherwise exit
    private static boolean doRun = true;

    private static BrowserTypes browser;

    private static User defaultUser;


    /**
     * Load properties from file core.properties
     * you can use VM option -DPROPERTIES_FILE=file to specifiy your properties
     *
     * @param doLoadAdditionalProperties if true then load additional properties
     */
    public static void loadProperties(boolean doLoadAdditionalProperties) {
        if (properties != null) return; // already loaded
        try {
            loadCommonProperties();
            loadProperties(getClientsPropertiesFile());
            if (doLoadAdditionalProperties) {
                loadProperties(getPropertiesFile());
                loadProperties(getKnisPropertiesFile());
                loadProperties(getDBPropertiesFile());
            }
        } catch (IOException io) {
            io.printStackTrace();
            System.exit(-2);
        }
    }


    public static void loadProperties() {
        loadProperties(true);
    }

    /**
     * do not log, logger should be init after this method
     *
     * @throws IOException - Exception
     */
    private static void loadCommonProperties() throws IOException {
        if (properties != null) return;
        File file = new File(CONFIG_PATH + File.separator + COMMON_PROPERTIES_FILE);
        if (!file.exists()) {
            throw new FileNotFoundException("Can't find file " + file.getAbsolutePath());
        }
        properties = new Properties();
        properties.load(new FileInputStream(file));
        //System.out.println(properties.toString());
    }

    private static void loadProperties(String str) throws IOException {
        if (str == null) return;
        File file = new File(str);
        if (!file.exists() && !(file = new File(CONFIG_PATH + File.separator + str)).exists()) {
            throw new FileNotFoundException("Can't find file " + file.getAbsolutePath());
        }
        properties.load(new FileInputStream(file));
    }

    public static Map<String, String> getKnownPopupExceptions() {
        return getKnownExceptions(POPUP_EXCEPTIONS);
    }

    public static Map<String, String> getKnownPageExceptions() {
        return getKnownExceptions(PAGE_EXCEPTIONS);
    }

    /**
     * @param clazz - simple name of PSPage class
     * @return map with knises
     */
    public static Map<String, String> getKnownPageExceptions(String clazz) {
        return getKnownExceptions(PAGE_EXCEPTIONS + clazz + ".");
    }


    public static Map<String, String> getKnownLogExceptions() {
        return getKnownExceptions(LOG_EXCEPTIONS);
    }

    public static Set<String> getKnownLogWarnings() {
        return getKnownExceptions(LOG_WARNINGS).keySet();
    }

    /**
     * @param prefix prefix to search. e.g page, popup, group
     * @return map. key - info (part of exception), value - issue number.
     */
    private static Map<String, String> getKnownExceptions(String prefix) {
        Map<String, String> res = new HashMap<String, String>();
        for (Object key : properties.keySet()) {
            if (key.toString().startsWith(prefix)) {
                res.put(String.valueOf(key).replace(prefix, ""), String.valueOf(properties.get(key)));
            }
        }
        return res;
    }

    /**
     * @param groups String[] name of group to search
     * @return List of String[] where first value is known issue number, second is additional info (e.g. exception or some string)
     */
    public static List<String[]> getKnownIssuesByGroup(String[] groups) {
        List<String[]> list = new ArrayList<String[]>();
        List<String> groupList = Arrays.asList(groups);
        Map<String, String> knises = getKnownExceptions(GROUP_EXCEPTION);
        for (String key : knises.keySet()) {
            if (key.isEmpty()) continue;
            String info = key.contains(".") ? key.replaceAll("^[^\\.]+\\.(.*)$", "$1") : null;
            String group = key.contains(".") ? key.replaceAll("^([^\\.]+)\\..*$", "$1") : key;
            if (!groupList.contains(group)) continue;
            list.add(new String[]{knises.get(key), info});
        }
        return list;
    }

    /**
     * parse known issue from properties file.
     * properties line should be in one of the following format:
     * full_browser_brand.full_package_name.test_class.test_method.test_exception_description bug_id
     * short_package_name.test_class.test_method bug_id
     * short_browser_brand.short_package_name.test_class.test_method.test_exception_description bug_id
     * <p/>
     * <p/>
     * for example:
     * project_central.TestDriver.validateAddWorksForGatedProject.AssertionError 75668
     * IE9.com.powersteeringsoftware.tests.validation_ui.TestDriver.checkImageBox 74020
     * IE.com.powersteeringsoftware.tests.project_central.TestDriver.checkResizingColumnsInGrid 75295
     * gatedproject.TestDriver.createGatedTemplate_GATED_PROJECT_ASAP.AssertionError\u003a\u0020Incorrect\u0020url\u0020specified 81703
     *
     * @param testMethod -
     *                   something like 'com.powersteeringsoftware.tests.project_central.TestDriver.setTeamPaneResourcesAndSave' or
     *                   'project_central.TestDriver.setTeamPaneResourcesAndSave'
     *                   or, if there is parameters,
     * @return List of String[] where first value is known issue number, second is additional info (e.g. exception or some string)
     */
    public static List<String[]> getKnownIssuesByMethod(String testMethod) {
        String br = getBrowser().name().toLowerCase();
        String toSearch = testMethod.replaceFirst("^.*((\\.[^\\.]+){3})$", "$1").replaceAll("^\\.", ""); // <- short_package_name.test_class.test_method
        String prefix = testMethod.replace(toSearch, ""); // <- something like 'com.powersteeringsoftware.tests.'
        List<String[]> list = new ArrayList<String[]>();
        for (Object key : properties.keySet()) {
            String skey = key.toString();
            if (skey.contains(toSearch)) {
                String[] found = skey.split(toSearch);
                String knis = properties.getProperty(skey);
                String[] res;
                // found.length equals 0 if matching is exactly
                // found.length equals 1 if skey ends with found string
                // found.length equals 2 if skey starts with found string or contains found string
                if (found.length == 0 || skey.equals(testMethod)) {
                    res = new String[]{knis, null};
                    list.add(res);
                    continue;
                }
                // parsing browser brand from string like 'IE.com.powersteering...' or 'IE9.package.TestDriver.method' :
                String brand = found[0].replace(prefix, "").replace(".", "").toLowerCase();
                // parsing description:
                String description = null;
                if (found.length > 1 && !found[1].isEmpty()) {
                    if (!found[1].startsWith(".")) continue;
                    description = found[1].replaceAll("^\\.", "");
                }
                res = new String[]{knis, description};
                if (brand.isEmpty() || brand.equals(br) || brand.equals(br.replaceAll("\\d+$", ""))) {
                    list.add(res);
                }
                // else ignore this knis
            }
        }
        return list;
    }

    public static List<Object[]> getKnownJsErrors() {
        List<Object[]> res = new ArrayList<Object[]>();
        for (Object key : properties.keySet()) {
            String sKey = key.toString();
            if (!sKey.startsWith("jserror.")) continue;
            String[] aKey = sKey.split("\\.");
            if (aKey.length < 3) continue;
            String page = aKey[1];
            String msg = sKey.replaceAll("^.*" + page + "\\.", "");
            String issue = properties.getProperty(sKey);
            if (issue == null) continue;
            Object[] el = new Object[3];
            if (pages == null) {
                for (Package pack : Package.getPackages()) {
                    if (pack.getName().contains(".pages")) {
                        pages = pack;
                        break;
                    }
                }
            }
            if (pages == null) return null;
            try {
                Class c = Class.forName(pages.getName() + "." + page);
                el[0] = c;
            } catch (ClassNotFoundException e) {
                PSLogger.warn(CoreProperties.class.getSimpleName() + ".getKnownJsErrors: " + e.getMessage());
                continue;
            }
            el[1] = msg;
            el[2] = issue;
            res.add(el);
        }
        return res;
    }

    public static String getInternalIssueUrlPrefix() {
        if (!properties.containsKey("internal.url.serverhost")) return "";
        if (!properties.containsKey("internal.url.context"))
            return properties.getProperty("internal.url.serverhost");
        return getInternalUrl() + getInternalContext() + DiscussionIssueViewPageLocators.REDIRECT_URL_PREF.getLocator();
    }

    public static String getJiraIssueUrlPrefix() {
        if (!properties.containsKey("jira.url.serverhost")) return "";
        return getJiraUrl() + getJiraContext() + properties.getProperty("jira.url.bug.prefix");
    }

    public static String getInternalUrl() {
        return getProperty("internal.url.serverhost");
    }

    public static String getInternalContext() {
        return getProperty("internal.url.context");
    }

    public static String getInternalUser() {
        return getProperty("internal.url.username");
    }

    public static String getInternalPassword() {
        return getProperty("internal.url.password");
    }

    public static String getJiraUrl() {
        return getProperty("jira.url.serverhost");
    }

    public static String getJiraContext() {
        String res = getProperty("jira.url.context");
        if (res == null) return "";
        return res;
    }

    public static String getJiraUser() {
        return getProperty("jira.url.username");
    }

    public static String getJiraPassword() {
        return getProperty("jira.url.password");
    }

    public static String getJiraIssuePrefix() {
        return getProperty("jira.bug.prefix");
    }

    /**
     * Get core parameter by key
     *
     * @param key - the parameter process (or key)
     * @return parameter as a string
     * @throws NullPointerException - in case properties are null and there is no parameters
     */
    public static String getProperty(String key) throws NullPointerException {
        if (null == properties) {
            throw new NullPointerException(
                    "Before using CoreProperties you should set it. Use loadProperties(...) methods.");
        }
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        if (properties != null)
            properties.setProperty(key, value);
    }

    public static void setBrowser(BrowserTypes br) {
        browser = br;
    }

    /**
     * main method for setting browser
     *
     * @param brand String (brand) see common.properties
     */
    public static void setBrowser(String brand) {
        if (brand == null) {
            setBrowser(BrowserTypes.NO_BROWSER);
            return;
        }
        BrowserTypes br;
        setBrowser(br = BrowserTypes.getBrowser(brand));
        br.setDriverType(!useSeleniumRC());
        if (!isLocalSeleniumServer()) {
            // set cmd to start from properties
            if (properties.containsKey("browser.default." + browser.name().toLowerCase())) {
                browser.setCommand(properties.getProperty("browser.default." + browser.name().toLowerCase()));
                browser.parsePath();
            }
        } else {
            // if browser.default is null set path and version from local server
            if (!properties.containsKey("browser.default")) {
                if (browser.isIE()) {
                    browser.setVersion(LocalServerUtils.getLocalServer().getIExploreVersion());
                    //browser.setPath(LocalServerUtils.getLocalServer().getIExplorePath());
                    String path = properties.getProperty("browser.default.ie.driver.path");
                    String name = properties.getProperty("browser.default.ie.driver.name");
                    if (path != null) {
                        File f = new File(SeleniumDriverFactory.getSeleniumLibDir() + File.separator + path);
                        if (f.exists()) {
                            // relative path
                            browser.setDriver(f.getAbsolutePath().replace(new File(THIS_WORK_DIR + File.separator).getAbsolutePath() + File.separator, ""),
                                    name);
                        }
                    }
                }
                if (browser.isFF()) {
                    Map<String, String> info = LocalServerUtils.getLocalServer().getFirefoxInfo();
                    for (String key : info.keySet()) {
                        String ver1 = key.replaceAll("[^\\d]", "");
                        String ver2 = browser.name().replaceAll("[^\\d]", "");
                        if (ver1.startsWith(ver2)) {
                            browser.setVersion(key);
                            browser.setPath(info.get(key));
                        }
                    }
                    String sp;
                    File fp;
                    if (useFFProfile() && (sp = getProperty("selenium.server.ffprofile")) != null && (fp = new File(sp)).exists()) {
                        browser.setProfile(fp.getAbsolutePath());
                    }
                }
                if (browser.isGoogleChrome()) {
                    Map<String, String> info = LocalServerUtils.getLocalServer().getGoogleChrome();
                    Object version = info.keySet().toArray()[0];
                    browser.setPath(info.get(version));
                    browser.setVersion((String) version);
                    //SeleniumDriverFactory.getVersion();
                    if (browser.isGoogleChromeGreaterThan(11)) {
                        String path = properties.getProperty("browser.default.gch.driver.path");
                        String name = properties.getProperty("browser.default.gch.driver.name");

                        if (path != null) {
                            File f = new File(SeleniumDriverFactory.getSeleniumLibDir() + File.separator + path);
                            if (f.exists()) {
                                // relative path
                                browser.setDriver(f.getAbsolutePath().replace(new File(THIS_WORK_DIR + File.separator).getAbsolutePath() + File.separator, ""),
                                        name);
                            }
                        }
                    }
                }
            } else { // otherwise set cmd to start
                browser.setCommand(properties.getProperty("browser.default"));
                browser.parsePath();
            }
        }
        PSLogger.debug("This browser is " + browser);
    }

    public static BrowserTypes getBrowser() {
        if (browser == null) setBrowser(System.getProperty(SYS_PROP_BROWSER));
        return browser;
    }

    public static String getURLContext() {
        if (System.getProperties().containsKey(SYS_PROP_CONTEXT)) {
            String tmp = System.getProperty(SYS_PROP_CONTEXT); // with starting slash:
            setProperty("application.url.context", tmp.startsWith("/") ? tmp : "/" + tmp);
            System.getProperties().remove(SYS_PROP_CONTEXT);
        }
        String res = getProperty("application.url.context");
        //return (res + "/" + HomePageLocators.URL.getLocator()).replaceAll("/{2,}", "/"); // workaround for #88766
        return res;
    }

    public static String getURLServerHost() {
        if (System.getProperties().containsKey(SYS_PROP_HOST)) {
            String tmp = System.getProperty(SYS_PROP_HOST); // without ending slash:
            setProperty("application.url.serverhost", tmp.endsWith("/") ? tmp.replaceAll("/$", "") : tmp);
            System.getProperties().remove(SYS_PROP_HOST);
        }
        return getProperty("application.url.serverhost");
    }

    public static String getURLServerWithContext() {
        return getURLServerHost() + getURLContext();
    }

    public static User getDefaultUser() {
        if (defaultUser != null) return defaultUser;
        String name = getProperty("application.default.user.login");
        String password = getProperty("application.default.user.password");
        String zone = getProperty("application.default.user.timezone");
        User us = new User(name, password);
        us.setIsAdmin(true);
        if (zone != null) us.setTimeZone(zone);
        return defaultUser = TestSession.putUser(us);
    }

    public static String getWaitForElementToLoadAsString() {
        return getProperty("selenium.page.timeout");
    }

    public static long getWaitForElementToLoad() {
        try {
            return Long.parseLong(getProperty("selenium.page.timeout"));
        } catch (NumberFormatException nfe) {
            return DEFAULT_WAIT_FOR_PAGE_TO_LOAD;
        }
    }

    public static void setWaitForElementToLoad(long l) {
        properties.setProperty("selenium.page.timeout", String.valueOf(l));
    }

    public static long getWorkTreeTimeout() {
        try {
            return Long.parseLong(getProperty("application.work-tree.timeout"));
        } catch (NumberFormatException nfe) {
            return DEFAULT_WAIT_FOR_PAGE_TO_LOAD;
        }
    }


    public static String getSeleniumServerPortAsString() {
        if (StringUtils.isEmpty(getProperty("selenium.server.port"))) {
            String port = String.valueOf(DEFAULT_SELENIUM_PORT);
            PSLogger.debug("Selenium port was empty. Set as default " + port);
            setSeleniumServerPort(port);
            return String.valueOf(port);
        }
        return getProperty("selenium.server.port");
    }

    public static void setSeleniumServerPort(String port) {
        if (port == null) {
            properties.remove("selenium.server.port");
        } else {
            setProperty("selenium.server.port", port);
        }
    }

    public static int getSeleniumServerPort() {
        try {
            return Integer.parseInt(getSeleniumServerPortAsString());
        } catch (NumberFormatException nfe) {
            PSLogger.warn("Was error while parsing Selenium port. Set as default " + DEFAULT_SELENIUM_PORT);
            return DEFAULT_SELENIUM_PORT;
        }
    }

    public static String getSeleniumServerHostname() {
        return getProperty("selenium.server.hostname");
    }

    public static boolean useSeleniumRC() {
        if (System.getProperties().containsKey(SYS_PROP_USE_RC)) {
            return Boolean.parseBoolean(System.getProperty(SYS_PROP_USE_RC));
        }
        return getProperty("selenium.rc") == null || getProperty("selenium.rc").equalsIgnoreCase("true");
    }

    public static void setUseSeleniumRC(boolean useRc) {
        System.setProperty(SYS_PROP_USE_RC, String.valueOf(useRc));
        driverTypeSet.add((useRc ? "rc" : "web") + "-driver");
    }

    public static void reSetSeleniumType() {
        System.getProperties().remove(SYS_PROP_USE_RC);
    }

    public static String getDriverTypes() {
        return driverTypeSet.toString().replace("[", "").replace("]", "");
    }

    public static String getTestNGFile() {
        return getProperty("testng.properties");
    }

    public static String getDefaultSuiteName() {
        if (properties != null && properties.containsKey("testng.name.default"))
            return getProperty("testng.name.default").toUpperCase();
        return "";
    }

    public static long getTestCaseTimeoutInMs() {
        if (properties != null && properties.containsKey("test.testcase.timeout"))
            return Long.parseLong(getProperty("test.testcase.timeout")) * 1000;
        return 0;
    }

    public static long getTestTimeoutInSec() {
        if (properties == null) throw new NullPointerException("core properties not specified");
        if (!properties.containsKey("test.timeout")) {
            properties.setProperty("test.timeout", String.valueOf(20 * 60 * 60));
        }
        return Long.parseLong(getProperty("test.timeout"));
    }

    public static String getTestNGReportFile() {
        return getProperty("testng.report.file"); // this is constant
    }

    public static String getTestNGReportDir() {
        return getProperty("testng.report.dir");
    }

    public static void setTestNGReportDir(String dir) {
        setProperty("testng.report.dir", dir);
    }

    public static Set<String> getTestsToExclude() {
        return getTestsToIncludeExclude("testng.exclude");
    }

    public static Set<String> getTestsToInclude() {
        return getTestsToIncludeExclude("testng.include");
    }

    private static Set<String> getTestsToIncludeExclude(String prop) {
        Set<String> res = new HashSet<String>();
        if (properties.containsKey(prop)) {
            res.addAll(Arrays.asList(properties.getProperty(prop).split("\\s*,\\s*")));
        }
        res.remove("");
        return res;
    }

    /**
     * @return true if pages should be validate after opening
     */
    public static boolean doValidatePage() {
        if (!properties.containsKey("test.page.validate")) {
            setDoValidatePage(Boolean.TRUE);
        }
        return Boolean.parseBoolean(properties.getProperty("test.page.validate"));
    }

    public static void setDoValidatePage(boolean b) {
        properties.setProperty("test.page.validate", String.valueOf(b));
    }

    public static void setTestsToExclude(String exclude) {
        setTestsToIncludeExclude(exclude, "testng.exclude");
    }

    public static void setTestsToInclude(String include) {
        setTestsToIncludeExclude(include, "testng.include");
    }


    private static void setTestsToIncludeExclude(String list, String prop) {
        if (list == null) return;
        if (properties == null) return;
        if (!properties.containsKey(prop) || properties.getProperty(prop).isEmpty()) {
            properties.setProperty(prop, list);
        } else {
            properties.setProperty(prop, properties.getProperty(prop) + "," + list);
        }
    }


    public static boolean isMultipleLogs() {
        return new Boolean(getProperty("logs.multiplefolders"));
    }

    public static String getLogMainPath() {
        if (System.getProperties().containsKey(SYS_PROP_LOGS)) {
            setProperty("logs.dir", System.getProperty(SYS_PROP_LOGS));
            System.getProperties().remove(SYS_PROP_LOGS);
        }
        if (properties.containsKey("logs.dir"))
            return getProperty("logs.dir");
        return DEFAULT_LOG_ROOT;
    }

    private static boolean useFFProfile() {
        if (System.getProperties().containsKey(SYS_PROP_USE_FF_PROFILE)) {
            setUseFFProfile();
            System.getProperties().remove(SYS_PROP_USE_FF_PROFILE);
        }
        if (properties.containsKey("ff.profile"))
            return Boolean.parseBoolean(getProperty("ff.profile"));
        return false;
    }

    public static void setUseFFProfile() {
        setProperty("ff.profile", String.valueOf(true));
    }

    public static String[] getLogDirParts() {
        if (properties.containsKey("logs.dir.prefix") && properties.containsKey("logs.dir.separator"))
            return new String[]{getProperty("logs.dir.prefix"), getProperty("logs.dir.separator")};
        return null;
    }


    public static void setLogMainPath(String newLogRoot) {
        setProperty("logs.dir", newLogRoot);
    }


    public static String getMailerProperties() {
        return getProperty("mailer.properties");
    }

    public static String getLog4jProperties() {
        return getProperty("logs.properties");
    }

    public static int getRunFailedCount() {
        return Integer.parseInt(getProperty("testng.runfailedcount"));
    }


    public static String getContextDBString() {
        String res = getProperty("context.db.connect.path");
        String n = getContextDBName();
        String t = getContextDBNameTemplate();
        return n != null && t != null ? res.replace(t, n) : res;
    }

    public static String getContextDBName() {
        return getProperty("context.db.name");
    }

    private static String getContextDBNameTemplate() {
        return getProperty("context.db.name.tmp");
    }

    public static String getContextDBBackupPath() {
        return getProperty("context.db.backup.path");
    }

    public static String getContextDBHost() {
        if (properties == null || !properties.containsKey("context.db.connect.path")) return null;
        return getProperty("context.db.connect.path").replaceAll("/[^/]+$", "").
                replaceAll(".*/", "").replaceAll(":\\d+", "");
    }

    public static Map<String, String> getContextDBBackupFiles() {
        Map<String, String> res = new HashMap<String, String>();
        if (getDBPropertiesFile() == null) return res;
        res.putAll(getContextDBBackupFile("mdf"));
        res.putAll(getContextDBBackupFile("ldf"));
        res.putAll(getContextDBBackupFile("search.txt"));
        res.putAll(getContextDBBackupFile("search.doc"));
        return res;
    }

    private static Map<String, String> getContextDBBackupFile(String name) {
        Map<String, String> res = new HashMap<String, String>();
        String k = getProperty("context.db." + name + ".name");
        String v = getProperty("context.db." + name + ".path");
        String n = getContextDBName();
        String t = getContextDBNameTemplate();
        if (k != null && v != null) {
            res.put(k, n != null && t != null ? v.replace(t, n) : v);
        }
        return res;
    }

    public static String getContextJMX() {
        return getProperty("context.jmx");
    }

    public static boolean doPingDatabase() {
        return properties.containsKey("context.pingdb") && properties.getProperty("context.pingdb").equals("true") && getContextDBHost() != null;
    }

    public static String getReportFileFolder() {
        return getProperty("report.file.folder");
    }

    public static boolean isRestoreDB() {
        return Boolean.parseBoolean(getProperty("application.restoredb"));
    }

    public static void setRestoreDB(boolean doRestore) {
        setProperty("application.restoredb", String.valueOf(doRestore));
    }

    public static void setTestNGFile(String testNG) {
        if (testNG != null)
            setProperty("testng.properties", testNG);
    }


    public static boolean isRestartResin() {
        return Boolean.parseBoolean(getProperty("application.restartresin"));
    }

    public static boolean isKillResin() {
        return Boolean.parseBoolean(getProperty("application.killresin"));
    }

    public static void setRestartResin(boolean doRestart) {
        setProperty("application.restartresin", String.valueOf(doRestart));
    }

    public static void setKillResin(boolean doRestart) {
        setProperty("application.killresin", String.valueOf(doRestart));
    }

    /**
     * format: -prop_name=prop_value
     *
     * @param args
     */
    public static void setAny(String[] args) {
        for (String arg : args) {
            if (!arg.startsWith("-") || !arg.contains("=")) continue;
            String key = arg.replaceAll("-(.*)=.*", "$1");
            String val = arg.replaceAll("-.*=(.*)", "$1");
            if (!properties.containsKey(key)) continue;
            properties.setProperty(key, val);
        }
    }

    /**
     * this method for getting individual for test parameter.
     *
     * @return - long from test.template property
     */
    public static long getTestTemplate() {
        if (!properties.containsKey("test.template")) {
            properties.setProperty("test.template", String.valueOf(System.currentTimeMillis()));
        }
        return Long.parseLong(properties.getProperty("test.template"));
    }

    public static void setTestTemplate(long template) {
        properties.setProperty("test.template", String.valueOf(template));
    }


    public static String getServerFolder() {
        if (isLocalSeleniumServer()) {
            return new File(THIS_WORK_DIR + File.separator + properties.getProperty("test.data.folder.local")).getAbsolutePath()
                    + File.separator;
        }
        return getProperty("test.data.folder.remote");
    }

    public static String getHudsonUrl() {
        return getProperty("hudson.url");
    }

    public static String getHudsonTestsList() {
        return getProperty("hudson.tests.list");
    }

    public static String getHudsonJobId() {
        return getProperty("hudson.url.job_id");
    }

    public static void setHudsonJobId(Integer id) {
        if (id == null) return;
        PSLogger.debug("Hudson job id is " + id);
        properties.setProperty("hudson.url.job_id", String.valueOf(id));
    }

    public static void setDoEmail(Boolean doSend) {
        if (doSend == null) return;
        properties.setProperty("mailer.do-send", String.valueOf(doSend));
    }

    public static boolean getDoEmail() {
        if (!properties.containsKey("mailer.do-send")) { // by default do not send emails. there is now hudson-email-daemon
            properties.setProperty("mailer.do-send", String.valueOf(Boolean.FALSE));
        }
        return Boolean.parseBoolean(properties.getProperty("mailer.do-send"));
    }


    public static void setEmailList(String list) {
        if (list == null) return;
        properties.setProperty("mailer.address.to", list);
    }

    public static void setEmailPrefix(String prefix) {
        if (prefix != null && !prefix.isEmpty() && !prefix.toUpperCase().startsWith(getDefaultSuiteName())) {
            if (properties.containsKey("mailer.subject.prefix")) {
                prefix += "," + properties.getProperty("mailer.subject.prefix");
            }
            properties.setProperty("mailer.subject.prefix", prefix.toUpperCase());
        }
    }

    public static String getEmailPrefix() {
        if (!properties.containsKey("mailer.subject.prefix")) return null;
        return properties.getProperty("mailer.subject.prefix");
    }

    public static void setWebDriverFilename(String file) {
        if (file != null)
            properties.setProperty("browser.default.gch.driver.path", file);
    }


    public static String getEmailList() {
        if (!properties.containsKey("mailer.address.to")) return null;
        String res = properties.getProperty("mailer.address.to");
        if (res.isEmpty()) return null;
        return res;
    }

    public static String getHudsonArtifactSuffix() {
        return getProperty("hudson.url.artifact");
    }

    public static String getHudsonJobName() {
        return StrUtil.stringToURL(new File(THIS_WORK_DIR).getParentFile().getName());
    }

    public static String getContextDBUser() {
        return getProperty("context.db.user");
    }

    public static String getContextDBPass() {
        return getProperty("context.db.pass");
    }

    public static String getRelativeLogFilesDir() {
        return LOGS_FILES;
    }

    public static String getPropertiesString() {
        StringBuffer propString = new StringBuffer("CoreProperties values:\n");
        for (Object key : CoreProperties.properties.keySet()) {
            propString.append(key + ":	");
            propString.append(CoreProperties.properties.getProperty((String) key));
            propString.append(";\n");
        }
        return propString.toString();
    }

    public static String getDateFormat() {
        if (StringUtils.isEmpty(getProperty(DATE_FORMAT))) {
            return DEFAULT_DATE_FORMAT;
        }
        return getProperty(DATE_FORMAT);
    }

    public static String getApplicationServerHost() {
        String res = getProperty("application.server.host");
        try {
            if (res == null)
                res = new URL(getURLServerHost()).getHost();
            setProperty("application.server.host", res);
        } catch (MalformedURLException e) {
            PSLogger.fatal(e);
        }
        return res;
    }

    public static TimeZone getApplicationServerTimeZone() {
        String res = getProperty("application.server.timezone");
        if (res == null)
            setProperty("application.server.timezone", res = DEFAULT_TIMEZONE);
        return TimeZone.getTimeZone(res);
    }

    public static String getApplicationServerUser() {
        return getProperty("application.server.build.user");
    }

    public static String getApplicationServerPort() {
        return getProperty("application.server.port");
    }

    public static String getApplicationServerPassword() {
        return getProperty("application.server.build.password");
    }

    public static String getApplicationServerLogs() {
        return getProperty("application.server.logpath");
    }

    public static String getApplicationServerLuceneDir() {
        return getProperty("application.server.lucene_dir");
    }

    public static String getApplicationTestServerUser() {
        return getProperty("application.server.pstest.user");
    }

    public static String getApplicationTestServerPassword() {
        return getProperty("application.server.pstest.password");
    }


    public static String getApplicationServerRestartCmd() {
        return getProperty("application.server.restart");
    }

    public static String getApplicationServerStopCmd() {
        return getProperty("application.server.stop");
    }

    public static String getApplicationServerStartCmd() {
        return getProperty("application.server.start");
    }

    public static String getApplicationServerClearLogsCmd() {
        return getProperty("application.server.clear");
    }

    private static String getConfigPath() {
        String confs;
        if (System.getProperties().containsKey(SYS_PROP_CONFIG_PATH)) {
            confs = System.getProperty(SYS_PROP_CONFIG_PATH);
        } else {
            confs = DEFAULT_CONFIG_PATH;
        }
        return confs + File.separator;
    }

    public static void setPropertiesFile(String file) {
        System.setProperty(SYS_PROP_PROPERTIES_FILE, file);
    }

    private static String getPropertiesFile() {
        String props;
        if (System.getProperties().containsKey(SYS_PROP_PROPERTIES_FILE)) {
            props = System.getProperty(SYS_PROP_PROPERTIES_FILE);
        } else {
            props = DEFAULT_CORE_PROPERTIES_FILE;
        }
        return props;
    }

    private static String getKnisPropertiesFile() {
        return properties == null ? null : properties.containsKey(KNOWN_ISSUES_PROPERTIES_FILE) ? properties.getProperty(KNOWN_ISSUES_PROPERTIES_FILE) : KNOWN_ISSUES_PROPERTIES_FILE_DEF_VALUE;
    }

    private static String getDBPropertiesFile() {
        return getProperty(DB_PROPERTIES_FILE);
    }

    private static String getClientsPropertiesFile() {
        return getProperty(CLIENTS_PROPERTIES_FILE);
    }

    /**
     * isLocalSeleniumServer
     *
     * @return true if it is local selenium server
     */
    public static boolean isLocalSeleniumServer() {
        if (getSeleniumServerHostname() == null ||
                getSeleniumServerHostname().isEmpty() ||
                getSeleniumServerHostname().equalsIgnoreCase("localhost")) {
            PSLogger.debug("Local selenium server");
            return true;
        }
        return false;
    }

    public static void setRun(boolean run) {
        doRun = run;
    }

    public static boolean getRun() {
        return doRun;
    }

    public static void setMakeScreenCapture(boolean on) {
        properties.setProperty("browser.default.make_screen_capture", String.valueOf(on));
    }

    public static boolean getMakeScreenCapture() {
        return Boolean.parseBoolean(getProperty("browser.default.make_screen_capture"));
    }

    public static String getImageFormat() {
        String prop = properties.getProperty("browser.default.image_format");
        if (prop != null) {
            return prop.toLowerCase();
        }
        return DEFAULT_IMAGE_FORMAT;
    }

    public static Map<String, Object> getClientUsers(String host) {
        Map<String, Object> res = new HashMap<String, Object>();
        for (Object key : properties.keySet()) {
            String u = ((String) key).toLowerCase();
            String p;
            if (!u.startsWith(host.toLowerCase() + ".user")) continue;
            String index = u.replaceAll(".*(\\d+)", "$1");
            if (!index.matches("\\d+")) continue;
            if (properties.containsKey(p = (host.toLowerCase() + ".password" + index))) {
                res.put(properties.getProperty(u), properties.getProperty(p));
            }
        }

        return res;
    }

    public static int getMetricItemMonetaryFormatMax() {
        if (properties.containsKey("application.metrics.item.monetary.number_format.max"))
            return Integer.parseInt(getProperty("application.metrics.item.monetary.number_format.max"));
        return 2;
    }

    public static int getMetricItemNumericFormatMax() {
        if (properties.containsKey("application.metrics.item.numeric.number_format.max"))
            return Integer.parseInt(getProperty("application.metrics.item.numeric.number_format.max"));
        return 5;
    }

    public static boolean getTestMoreVerbose() {
        if (!properties.containsKey("test.more-verbose")) {
            properties.setProperty("test.more-verbose", Boolean.FALSE.toString());
        }
        return Boolean.parseBoolean(properties.getProperty("test.more-verbose"));
    }
}
