package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.CLI;
import com.powersteeringsoftware.libs.util.ResinJMXManager;
import com.powersteeringsoftware.libs.util.bugtrackers.KnownIssue;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.mail.PSResultProperties;
import org.apache.commons.cli.ParseException;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.xml.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.powersteeringsoftware.libs.tests.core.TestSettings.*;


/**
 * This class start Automatic Tests.</br>
 * Before invoking method start() you must:</br>
 * - configure log4j framework <i>PropertyConfigurator.configure(CoreProperties.LOG4J_PROP_FILE);</i> </br>
 * As example you can see method main() in class RunTestNG
 *
 * @author selyaev_ag
 */
public class RunTestNG extends AbstractRunner {

    private boolean isBrokenRun;

    private String getOutputDir() {
        StringBuilder res = new StringBuilder(CoreProperties.getLogMainPath());
        res.append(File.separatorChar);
        if (CoreProperties.isMultipleLogs()) {
            String[] parts = CoreProperties.getLogDirParts();
            res.append(parts[0]);
            res.append(TestSession.getAppVersion().toString());
            res.append(parts[1]);
            res.append(CoreProperties.getBrowser().name());
        }
        return res.toString();
    }

    protected void endTests() {
        try {
            KnownIssue.collectInfo();
            PSLogger.info(KnownIssue.getReport());
            PSLogger.debug("Stop selenium.");
            SeleniumDriverFactory.stopAllSeleniumDrivers();
            PSLogger.debug("Log results.");
            sendEmail();
            String title = "AutoTests Result. PS" + TestSession.getAppVersion().getValue() + ", " + CoreProperties.getBrowser().name();
            addLinksAndTitleToLog(title);
            PSLogger.debug("Tests have finished.");
            System.exit(EXIT_OK);
        } catch (Exception e) {
            PSLogger.error("Error while stop test execution: " + e, false);
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
    }

    /**
     * Run TestNG tests
     *
     * @param suites list of XmlSuite
     * @return true if nothing to rerun
     */
    private boolean run(List<XmlSuite> suites) {
        try {
            testng.setXmlSuites(suites);
            testng.run();
        } catch (Throwable e) {
            isBrokenRun = true;
            PSLogger.error("Error while run tests. Error: " + e, false);
            PSLogger.fatal(e);
            PSLogger.saveFull();
            e.printStackTrace();
        } finally {
            PSLogger.warn("Methods to rerun: " + getTLA().getMethodsToRerun());
            return getTLA().getMethodsToRerun().size() == 0;
        }
    }

    protected void runTests() {
        int num = 0;
        while (!run(getXmlSuites(num)) && num < CoreProperties.getRunFailedCount()) {
            PSLogger.task("Run failed tests, ITER #" + ++num + ":");
        }
    }

    public PSTestListener getTLA() {
        if (tla == null) tla = new PSTestListener();
        return (PSTestListener) tla;
    }


    private List<XmlSuite> getXmlSuites(int iterNum) {
        List<XmlSuite> allSuites;
        if (iterNum == 0) {
            allSuites = getGeneralSuites();
        } else {
            allSuites = getFailedSuites(iterNum);
        }
        getTLA().setIterNumber(iterNum);
        for (XmlSuite x : allSuites) {
            PSLogger.debug(x.toXml());
        }
        return allSuites;
    }

    /**
     * generate XmlSuites for run failed tests
     *
     * @param iterNum - rerun index
     * @return ArrayList<XmlSuite>
     */
    private List<XmlSuite> getFailedSuites(int iterNum) {
        String FAILED = "failed ";
        List<XmlSuite> allSuites = new ArrayList<XmlSuite>();
        XmlSuite suite = new XmlSuite();

        suite.setName(FAILED + iterNum);
        allSuites.add(suite);
        Map<ITestClass, List<ITestNGMethod>> classes = getTLA().getMethodsToRerun();
        for (ITestClass clazz : classes.keySet()) {
            String testName = clazz.getXmlTest().getName().
                    replaceAll("\\(" + FAILED + "\\d+\\)", "") + "(" + FAILED + iterNum + ")";
            XmlTest test = new XmlTest(suite);
            test.setName(testName);
            for (XmlTest t : suite.getTests()) {
                if (t.getName().equals(testName)) {
                    test = t;
                    break;
                }
            }
            test.setIncludedGroups(clazz.getXmlTest().getIncludedGroups());
            test.setExcludedGroups(clazz.getXmlTest().getExcludedGroups());
            XmlClass xmlClazz = new XmlClass(clazz.getName());
            test.getXmlClasses().add(xmlClazz);
            xmlClazz.getIncludedMethods();
            List<XmlInclude> includes = new ArrayList<XmlInclude>();
            for (ITestNGMethod m : classes.get(clazz)) {
                includes.add(new XmlInclude(m.getMethodName()));
            }
            xmlClazz.getIncludedMethods().addAll(includes);
        }
        return allSuites;
    }

    /**
     * generate XmlSuites for run general tests
     *
     * @return ArrayList<XmlSuite>
     */
    private List<XmlSuite> getGeneralSuites() {
        PowerSteeringVersions version = null;
        String suiteNameSuffix = null;
        List<XmlSuite> allSuites = new ArrayList<XmlSuite>();
        String file = null;
        try {
            version = TestSession.getAppVersion();
            suiteNameSuffix = "(PS ver. " + version.getValue() + ", " + CoreProperties.getBrowser().name() + ")";
            allSuites = new Parser(file = CoreProperties.getTestNGFile()).parseToList();
        } catch (Throwable e) {
            PSLogger.error("Exception when parsing file : " + file);
            PSLogger.fatal(e);
        }
        for (XmlSuite suite : allSuites) {
            parseXmlSuite(suite, suiteNameSuffix, version);
        }
        return allSuites;

    }

    /**
     * Modify the test suite according to app. version and browser.
     * Skipp some tests according properties or command line.
     *
     * @param suite           - XmlSuite
     * @param suiteNameSuffix - String suffix for suite name
     * @param version         - PowerSteeringVersions Object
     */
    private void parseXmlSuite(XmlSuite suite, String suiteNameSuffix, PowerSteeringVersions version) {
        CoreProperties.setEmailPrefix(suite.getName());
        suite.setName(suite.getName() + suiteNameSuffix);
        List<XmlTest> tests = suite.getTests();
        Set<String> testsToExclude = CoreProperties.getTestsToExclude();
        Set<String> testsToInclude = CoreProperties.getTestsToInclude();
        PSLogger.info("Tests to exclude/include: " + testsToExclude + "/" + testsToInclude);
        if (testsToInclude.size() != 0) { // parse include tests
            for (int i = 0; i < tests.size(); i++) {
                XmlTest test = tests.get(i);
                String prefix = test.getName().matches("^([^:]+):.*$") ? test.getName().replaceAll("^([^:]+):.*", "$1") : "";
                if (!testsToInclude.contains(prefix)) {
                    PSLogger.debug("Exclude test " + test.getName());
                    testsToExclude.add(prefix);
                    tests.remove(i--); // skipp exclude test
                    continue;
                }
                test.setName(test.getName().replace(prefix + ":", ""));
            }
            PSLogger.debug("Tests to exclude : " + testsToExclude);
        }
        if (testsToExclude.size() != 0) { // parse exclude tests
            for (int i = 0; i < tests.size(); i++) {
                XmlTest test = tests.get(i);
                String prefix = test.getName().matches("^([^:]+):.*$") ? test.getName().replaceAll("^([^:]+):.*", "$1") : "";
                if (testsToExclude.contains(prefix)) {
                    PSLogger.debug("Exclude test " + test.getName());
                    tests.remove(i--); // skipp exclude test
                    continue;
                }
                test.setName(test.getName().replace(prefix + ":", ""));
                testsToInclude.add(prefix);
            }
            PSLogger.debug("Tests to include : " + testsToInclude);
        }

        for (XmlTest test : tests) {
            PSLogger.debug("Include test " + test.getName());
            List<String> excluded = test.getExcludedGroups();
            for (PowerSteeringVersions v : PowerSteeringVersions.values()) {
                if (v.verGreaterThan(version)) {
                    excluded.add(v.name() + TestSettings._LESS);
                }
                if (v.verLessThan(version)) {
                    excluded.add(v.name() + TestSettings._GREATER);
                }
            }
            if (!CoreProperties.getBrowser().isIE()) {
                excluded.add(IE_ONLY_GROUP);
            }
            if (!CoreProperties.getBrowser().isFF()) {
                excluded.add(FIREFOX_ONLY_GROUP);
            }
            if (!CoreProperties.getBrowser().isSafari()) {
                excluded.add(SAFARI_ONLY_GROUP);
            }
            if (!CoreProperties.getBrowser().isGoogleChrome()) {
                excluded.add(CHROME_ONLY_GROUP);
            }
            excluded.add(DEVELOPMENT_GROUP);
            excluded.add(BROKEN_ALL_GROUP);
            excluded.add(BROKEN_GROUP_PREFIX + version.getValue());
            excluded.add(BROKEN_GROUP_PREFIX + CoreProperties.getBrowser().getName() + "." +
                    String.valueOf(CoreProperties.getBrowser().isWebDriver()));
            test.setExcludedGroups(excluded);

            if (SeleniumDriverFactory.isDriverInit()) {
                Map parameters = test.getParameters();
                parameters.putAll(_workaround(SeleniumDriverFactory.getDriver(), test.getIncludedGroups()));
                test.setParameters(parameters);
            }
        }
    }

    protected void initTests() {
        try {
            PSLogger.info("Init tests");
            setup();
            //restartResin();
            if (!CoreProperties.getRun()) {
                PSLogger.info("Do not run tests");
                System.exit(EXIT_OK);
            }
            // kill browsers
            LocalServerUtils.killBrowsers();
            // Setup Selenium driver
            ResinJMXManager.refresh();
            BasicCommons.logIn();
            BasicCommons.reindex();
            moveOtherLogsToTestNGLogDirAndReinitLogger(CoreProperties.getLogMainPath(), getOutputDir());
        } catch (Throwable e) {
            PSLogger.error("Error while starting tests: ");
            e.printStackTrace();
            PSLogger.fatal(e);
            PSLogger.saveFull();
            if (CoreProperties.getRun())
                endTests();
            System.exit(EXIT_FAIL);
        }
    }

    /**
     * Setup core properties. All Properties saved in instance of CoreProperties
     *
     * @param args - array of string. If array is empty used properties file by
     *             default
     * @throws ParseException
     * @throws IOException
     * @see com.powersteeringsoftware.libs.settings.CoreProperties
     */
    protected void setProperties(String[] args) {
        super.setProperties(args);
        CLI cli = getCLI();
        CoreProperties.setUseSeleniumRC(cli.getUseRcOption());
        if (cli.getBrowser() != null) {
            CoreProperties.setBrowser(cli.getBrowser());
        } else {
            CoreProperties.setRun(false);
            CoreProperties.setMakeScreenCapture(false);
        }
        CoreProperties.setTestsToExclude(cli.getTestsToExclude());
        CoreProperties.setTestsToInclude(cli.getTestsToInclude());
        PSLogger.info("Browser: " + CoreProperties.getBrowser());
    }

    protected CLI getCLI() {
        if (cli == null) cli = new CLI();
        return (CLI) cli;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("run tests with args " + Arrays.toString(args));
        RunTestNG runner = new RunTestNG();
        try {
            runner.setProperties(args);
            PSLogger.info(CoreProperties.getPropertiesString());
        } catch (Exception e) {
            e.printStackTrace();
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
        runner.start();
    }

    protected void setEmailProperties() {
        super.setEmailProperties();
        if (isBrokenRun) {
            PSResultProperties.SUBJECT_RESULT.setValue(PSResultProperties.SUBJECT_CRASH);
        }
        if (CoreProperties.getEmailPrefix() != null) {
            PSResultProperties.setType(PSResultProperties.Type.HTML);
        } else {
            PSResultProperties.setType(PSResultProperties.Type.SHORT_HTML);
        }
        PSResultProperties.TEST_ALL_TESTS_INFO.setValues(getTLA().getTestsNames());
        PSResultProperties.TEST_FAILED_TEST_CASES_INFO.setValue(getTLA().getFailedTestsDescription());

        PSResultProperties.ENVIRONMENT_CLIENT_BROWSER.setValue(CoreProperties.getBrowser().getFullName());
        PSResultProperties.ENVIRONMENT_CLIENT_BROWSER_SHORT.setValue(CoreProperties.getBrowser().name());
        PSResultProperties.ENVIRONMENT_CLIENT_BROWSER_BRAND.setValue(CoreProperties.getBrowser().getName());
    }

    /**
     * copy all *txt, *html logs to test NG log dir after this dir init
     *
     * @param was
     * @param now
     */
    private void moveOtherLogsToTestNGLogDirAndReinitLogger(String was, String now) {
        if (!CoreProperties.isMultipleLogs()) return; // do not move in this case
        String[] parts = CoreProperties.getLogDirParts();
        LocalServerUtils.copyAllFilesFromDirToDir(
                new File(was).getAbsolutePath(),
                new File(now).getAbsolutePath(), parts[0] + ".*");
        PSLogger.setNewLogsRoot(now);
    }


}
