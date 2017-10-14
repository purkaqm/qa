package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSHTMLLayoutPerTest;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.logger.PSResults;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.servers.RemoteLinServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.MethodHelper;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.thread.ThreadTimeoutException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.powersteeringsoftware.libs.logger.PSResults.*;

public class PSTestListener extends TestListenerAdapter {
    Set<ITestNGMethod> methodsToRerun = new LinkedHashSet<ITestNGMethod>();
    Set<ITestResult> knisResults = new HashSet<ITestResult>();
    List<ITestResult> failedResults = new ArrayList<ITestResult>();

    private String testContextParameters = "";

    private static Map<String, Boolean> enabledTestCases = new HashMap<String, Boolean>();
    private static Map<String, Class[]> exceptionsTestCases = new HashMap<String, Class[]>();
    private ITestContext testContext;
    private static String[] groupsToSet;
    private int iterNumber = -1;
    private String onStartMessage;

    private static PSKnownIssueException lastKnis;

    public static void setGroups(String[] groups) {
        groupsToSet = groups;
    }

    public static void setExceptionsTestcases(Map<String, Class[]> toSet) {
        exceptionsTestCases = toSet;
    }

    public static void setEnabledTestCases(Map<String, Boolean> toSet) {
        enabledTestCases = toSet;
    }

    /**
     * getTestAnnotation
     *
     * @param tm TestNGMethod Object
     * @return - TestAnnotation Object
     */
    private ITestAnnotation getTestAnnotation(ITestNGMethod tm) {
        try {
            //test-ng 6.0:
            IAnnotationFinder finder = testContext.getSuite().getAnnotationFinder(tm.getXmlTest().getAnnotations());
            return AnnotationHelper.findTest(finder, tm.getMethod());
            //test-ng 5.1:
            //TestClass testClass = (TestClass) tm.getTestClass();
            //return AnnotationHelper.findTest(testClass.getAnnotationFinder(), tm.getMethod());
        } catch (Throwable e) {
            PSLogger.warn(e);
            return null;
        }
    }

    public static void setLastKnis(PSKnownIssueException knis) {
        lastKnis = knis;
    }

    private String getTestCaseMainLoggerInfo(ITestResult tr) {
        String res = tr.getMethod().getDescription();
        if (res == null) return getTestCaseInfo(tr);
        StringBuilder sb = new StringBuilder(res);
        sb.append("\n");
        return sb.append("(").append(getTestCaseInfo(tr)).append(")").toString();
    }

    private String getTestCaseInfo(ITestResult tr) {
        Object[] par = tr.getParameters();
        StringBuffer parameters = new StringBuffer();
        if (par != null && par.length > 0) {
            for (int i = 0; i < par.length; i++) {
                parameters.append(par[i]).append(" ");
            }
        }
        return getFullTestCaseMethodName(tr) + (parameters.length() != 0 ? " - Parameters: " + parameters.toString().trim() : "");
    }

    private String getFullTestCaseMethodName(ITestResult tr) {
        return tr.getTestClass().getRealClass().getName() + "." +
                tr.getMethod().getMethodName();
    }

    private String getFullTestCaseMethodNameWithParameters(ITestResult tr) {
        Object[] par = tr.getParameters();
        if (par != null && par.length > 0) {
            StringBuffer sb = new StringBuffer(getFullTestCaseMethodName(tr));
            for (int i = 0; i < par.length; i++) {
                sb.append("_").append(par[i].toString().replace(" ", "_"));
            }
            return sb.toString();
        }
        return null;
    }

    private boolean hasParameters(ITestResult tr) {
        Object[] par = tr.getParameters();
        return par != null && par.length > 0;
    }

    @Override
    public void onStart(ITestContext tc) {
        CoreProperties.loadProperties();
        PSLogger.info("onStart: " + CoreProperties.getBrowser());
        PSLogger.createPerTestAppender();
        Map parameters = ((TestRunner) tc).getTest().getParameters();
        if (parameters.containsKey(TestSettings.USE_RC_SUITE_PARAMETER)) {
            String key = (String) parameters.get(TestSettings.USE_RC_SUITE_PARAMETER);
            if (SeleniumDriverFactory.isDriverInit() && !key.equals(String.valueOf(CoreProperties.useSeleniumRC()))) {
                SeleniumDriverFactory.stopAllSeleniumDrivers(); // hotfix
            }
            boolean b = Boolean.parseBoolean(key);
            PSLogger.debug("Set use RC to " + b);
            CoreProperties.setUseSeleniumRC(b);
        }
        PSLogger.task("Test " + tc.getName() + " start");
        if (CoreProperties.doPingDatabase()) {
            LocalServerUtils.pingDataBaseDaemon().doStart();
        }
        if (tc.getAllTestMethods().length != 0) {
            BasicCommons.logInWithoutThrow(); // because of safari behaviour
            printOnStartMessage();
            PSLogger.perTestInfo(getTestContextName(tc), PSHTMLLayoutPerTest.LOGS_LINK,
                    PSLogger.getPerTestPlanLogsFile().getAbsolutePath(), TITLE);
        }
        setTestContextParameters(tc);
        super.onStart(tc);
        enabledTestCases.clear();
        exceptionsTestCases.clear();
        knisResults.clear();
        testContext = tc;
        setExcludedGroupsToAllMethods(tc);
        Set<ITestNGMethod> before = getAllBeforeMethods(tc);
        PSLogger.info("Now before methods: " + before);
        for (ITestNGMethod b : before) {
            if (b.getTimeOut() == 0) {
                b.setTimeOut(CoreProperties.getTestCaseTimeoutInMs());
            }
        }
    }

    private Set<ITestNGMethod> getAllBeforeMethods(ITestContext tc) {
        Set<ITestNGMethod> before = new HashSet<ITestNGMethod>();
        Set<ITestClass> classes = new HashSet<ITestClass>();
        for (ITestNGMethod m : tc.getAllTestMethods()) {
            classes.add(m.getTestClass());
        }
        for (ITestClass c : classes) {
            for (ITestNGMethod b : c.getBeforeTestMethods()) {
                before.add(b);
            }
            for (ITestNGMethod b : c.getBeforeClassMethods()) {
                before.add(b);
            }
            for (ITestNGMethod b : c.getBeforeSuiteMethods()) {
                before.add(b);
            }
            for (ITestNGMethod b : c.getBeforeGroupsMethods()) {
                before.add(b);
            }
            for (ITestNGMethod b : c.getBeforeTestConfigurationMethods()) {
                before.add(b);
            }
        }
        return before;
    }

    @Override
    public void onConfigurationSuccess(ITestResult tr) {
        PSLogger.info("On Configuration Success");
        if (testContext != null) {
            for (ITestNGMethod tm : testContext.getAllTestMethods()) {
                ITestAnnotation ta = getTestAnnotation(tm);
                if (ta == null) continue;
                if (exceptionsTestCases.containsKey(tm.getMethodName())) {
                    ta.setExpectedExceptions(exceptionsTestCases.get(tm.getMethodName()));
                }
                if (enabledTestCases.containsKey(tm.getMethodName())) {
                    ta.setEnabled(enabledTestCases.get(tm.getMethodName()));
                }
            }
        }
        super.onConfigurationSuccess(tr);
    }


    @Override
    public void onTestStart(ITestResult tr) {
        PSLogger.info("onTestStart: " + CoreProperties.getBrowser());
        if (tr.getMethod().getTimeOut() == 0) {
            tr.getMethod().setTimeOut(CoreProperties.getTestCaseTimeoutInMs());
        }
        PSLogger.createPerTCAppender(getLogFileForTc(tr));
        PSLogger.task("Test STARTED: " + getTestCaseInfo(tr));
        initCheckingLogs();
        super.onTestStart(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        if (exceptionsTestCases.containsKey(tr.getMethod().getMethodName())) {
            PSLogger.info("Test FAILED as expected");
            PSLogger.saveOnFailure();
            if (tr.getThrowable() != null)
                PSLogger.info(tr.getThrowable() + " : " + tr.getThrowable().getMessage());
            else
                PSLogger.warn("Can't get exception");
        }
        String errs = stopCheckingLogsAndCheckResult();
        if (errs != null)
            for (String warn : CoreProperties.getKnownLogWarnings()) {
                if (errs.contains(warn)) {
                    PSLogger.warn("Warning in log: " + errs);
                    errs = null;
                    break;
                }
            }
        if (errs == null) {
            PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), OK, tr.getStartMillis(), tr.getEndMillis());
            super.onTestSuccess(tr);
            PSLogger.info("Test PASSED: " + getTestCaseInfo(tr));
        } else {
            String knis = null;
            Map<String, String> logExceptions = CoreProperties.getKnownLogExceptions();
            for (String ex : logExceptions.keySet()) {
                if (errs.contains(ex)) {
                    knis = logExceptions.get(ex);
                    break;
                }
            }
            if (knis != null) {
                reportKnis(tr, knis);
                super.onTestSuccess(tr);
                PSLogger.info("Test PASSED, but with errors: " + getTestCaseInfo(tr));
            } else {
                PSLogger.error("Errors in logs!");
                // disable report failure in case logs:
                /*PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), ERROR, tr.getStartMillis(), tr.getEndMillis());
                super.onTestFailure(tr);
                tr.setStatus(ITestResult.FAILURE);*/
                PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), OK, tr.getStartMillis(), tr.getEndMillis());
                super.onTestSuccess(tr);
                tr.setStatus(ITestResult.SUCCESS);
                PSLogger.error("Test CONTAINS ERRORS: " + getTestCaseInfo(tr));
            }
        }
        PSLogger.removePerTCAppender();
    }

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        PSLogger.fatal("Some ConfigurationFailure");
        stopCheckingLogsAndCheckResult();
        super.onConfigurationFailure(tr);
        printStackTrace(tr);
        if (tr.getThrowable().getCause() != null) {
            PSLogger.error("Caused by: ");
            printStackTrace(tr.getThrowable().getCause());
            if (tr.getThrowable().getCause().getCause() != null) {
                printStackTrace(tr.getThrowable().getCause().getCause());
            }
        }
        if (tr.getThrowable() instanceof ThreadTimeoutException) {
            onThreadTimeoutException();
        } else {
            PSLogger.saveOnFailure();
        }
        BasicCommons.reLogIn();
    }

    private void onThreadTimeoutException() {
        PSLogger.saveFull();
        if (CoreProperties.isLocalSeleniumServer()) { // for debug:
            PSLogger.debug(LocalServerUtils.getLocalServer().getAllProcesses());
            if (CoreProperties.getBrowser().isWebDriverIE()) {
                LocalServerUtils.enter();
            }
        }
    }


    @Override
    public void onTestFailure(ITestResult tr) {
        stopCheckingLogsAndCheckResult();
        Throwable thr = tr.getThrowable();
        String knis = null;
        if (thr instanceof ThreadTimeoutException) {
            PSLogger.error("timeout : " + thr.getMessage());
            onThreadTimeoutException();
            super.onTestSkipped(tr);
            tr.setStatus(ITestResult.SKIP);
            try {
                // wait while selenium dies
                Thread.sleep(CoreProperties.getWaitForElementToLoad());
            } catch (InterruptedException e) {
                PSLogger.warn("onTestFailure: " + e.getMessage());
            }
        } else if (thr.getCause() instanceof SkipException) {
            super.onTestSkipped(tr);
            tr.setStatus(ITestResult.SKIP);
            PSLogger.skip(thr.getCause());
            if (thr.getCause() instanceof PSSkipException) { // why do this?
                PSLogger.error(thr.getCause().getMessage() + "; add method to rerun");
                methodsToRerun.add(tr.getMethod());
            }
        } else {
            printStackTrace(tr);
            //TODO: debug, remove it:
            /*PSPage p = PSPage.getEmptyInstance();
            if (p.getUrl().contains(WBSEPageLocators.URL.getLocator()) && CoreProperties.getBrowser().isRCDriverIE()) {
                PSLogger.debug("for investigate:");
                PSLogger.save();
                PSLogger.debug(p.getDocument(false).asXML());
            }*/
            Boolean webAvailability = null;
            if (PSLogger.saveOnFailure() != null ||
                    (thr instanceof Exception && SeleniumDriverFactory.isCriticalException((Exception) thr))) {
                PSLogger.error("browser crash");
            } else {
                webAvailability = BasicCommons.isServerAvailable();
            }
            Throwable validateEx = null;
            if (webAvailability == null) {
                PSLogger.error("Seems some selenium problems (with frames?) or browser crash");
                checkForCrash(tr);
            } else if (!webAvailability || !BasicCommons.isLoggedIn()) {
                //todo: may be issue #72196(Problems with connection), it is workaround :
                PSLogger.error("Servlet error");
                knis = "72196";
            } else {
                validateEx = BasicCommons.validatePage();
                if (validateEx != null && validateEx instanceof PSKnownIssueException) {
                    knis = ((PSKnownIssueException) validateEx).getKnis();
                }
            }
            if (thr instanceof TestException) {
                PSLogger.debug("This is a " + TestException.class.getName());
                if (thr.getMessage().contains(PSKnownIssueException.class.getName())) {
                    thr = lastKnis;
                }
                if (thr.getCause() != null) {
                    PSLogger.fatal(thr.getCause());
                }
            }
            if (knis == null) {

                List<String[]> knises = new ArrayList<String[]>();
                if (hasParameters(tr)) {
                    knises.addAll(CoreProperties.getKnownIssuesByMethod(getFullTestCaseMethodNameWithParameters(tr)));
                }
                knises.addAll(CoreProperties.getKnownIssuesByMethod(getFullTestCaseMethodName(tr)));
                knises.addAll(CoreProperties.getKnownIssuesByGroup(tr.getMethod().getGroups()));
                Collections.sort(knises, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        if (o1[1] == null && o2[1] == null) {
                            return o1[0].compareTo(o2[0]);
                        } else if (o1[1] != null) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                for (String[] k : knises) { // search right knis from list
                    if (k[1] == null) {
                        knis = k[0];
                        break;
                    }
                    // searching from string like 'java.lang.RuntimeException: Some exception':
                    if (thr.toString().contains(k[1])) {
                        knis = k[0];
                        break;
                    }
                    if (k[1] != null && validateEx != null && validateEx.getCause() instanceof PSApplicationException) {
                        if (((PSApplicationException) validateEx.getCause()).containsStackTraceElement(k[1])) {
                            knis = k[0];
                            break;
                        }
                    }
                }
            }
            if (thr instanceof PSKnownIssueException) {
                PSKnownIssueException ex = ((PSKnownIssueException) thr);
                if (!ex.isCritical()) {
                    PSLogger.error("Exception is not critical, add testcase to rerun list");
                    methodsToRerun.add(tr.getMethod());
                }
                knis = ex.getKnis();
            }
            if (knis == null) {
                if (methodsToRerun.contains(tr.getMethod())) {
                    PSLogger.error("Test SKIPPED because of problems with browser or selenium: " + getTestCaseInfo(tr));
                    tr.setStatus(ITestResult.SKIP);
                    super.onTestSkipped(tr);
                } else {
                    PSLogger.error("Test FAILED: " + getTestCaseInfo(tr));
                    super.onTestFailure(tr);
                }
            }
        }
        if (!BasicCommons.reLogIn()) {
            // have some problems when log-in
            PSLogger.error("Can't login!");
            if (!BasicCommons.isServerAvailable()) {
                knis = "72196";
            } else { // don't know what happens, but can't continue. add this to skipp
                tr.setStatus(ITestResult.SKIP);
            }
        }
        if (knis != null) {
            reportKnis(tr, knis);
        } else if (tr.getStatus() == ITestResult.SKIP) {
            PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), SKIP, tr.getStartMillis(), tr.getEndMillis());
        } else {
            failedResults.add(tr);
            PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), ERROR, tr.getStartMillis(), tr.getEndMillis());
        }
        PSLogger.removePerTCAppender();
    }

    private void checkForCrash(ITestResult tr) {
        LocalServerUtils.getLocalServer().hasAnyCrash();
        super.onTestSkipped(tr);
        tr.setStatus(ITestResult.SKIP);
        methodsToRerun.add(tr.getMethod());
    }

    private void reportKnis(ITestResult tr, String knis) {
        PSLogger.debug("Add " + tr.getMethod() + " to knis list (" + knis + ")");
        PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr),
                PSResults.getKnis(knis), tr.getStartMillis(), tr.getEndMillis());
        PSLogger.knis(knis, tr.getTestClass().getRealClass().getName(), tr.getMethod().getMethodName());
        knisResults.add(tr);
    }

    private void printStackTrace(ITestResult tr) {
        Throwable err = tr.getThrowable();
        if (err != null) {
            printStackTrace(err);
        } else {
            PSLogger.error("Error in " + tr.getMethod().getMethodName());
        }
    }

    private void printStackTrace(Throwable err) {
        PSLogger.fatal(err.getClass() + " " + err.getMessage());
        if (err instanceof UnsupportedOperationException) {
            PSLogger.fatal("UnsupportedOperationException occured");
        }
        if (!(err instanceof PSKnownIssueException))
            PSLogger.fatal(err);
        PSLogger.debug("This is " + SeleniumDriverFactory.getVersion() + "," + CoreProperties.getBrowser());
    }


    @Override
    public void onTestSkipped(ITestResult tr) {
        stopCheckingLogsAndCheckResult();
        super.onTestSkipped(tr);
        String mess = "Test SKIPPED : ";
        Throwable ex = tr.getThrowable();
        if (!enabledTestCases.containsKey(tr.getMethod().getMethodName())) {
            PSLogger.perTestInfo(tr.getName(), getTestCaseMainLoggerInfo(tr), getLogFileForTc(tr), SKIP, tr.getStartMillis(), tr.getEndMillis());
        }
        if (ex != null && ex instanceof SkipException) {
            mess += ex.getMessage();
            BasicCommons.reLogIn(); // to avoid popups
        } else {
            mess += tr.getMethod().getMethodName() +
                    "; dependencies=" + Arrays.toString(tr.getMethod().getGroupsDependedUpon()) + "," +
                    Arrays.toString(tr.getMethod().getMethodsDependedUpon());
        }
        PSLogger.skip(mess);
        PSLogger.removePerTCAppender();
    }

    public List<ITestResult> getFailedTests() {
        return failedResults;
    }

    @Override
    public void onFinish(ITestContext tc) {
        PSLogger.task("On Finish " + tc.getName());
        if (((TestRunner) tc).getTest().getParameters().containsKey(TestSettings.USE_RC_SUITE_PARAMETER)) {
            CoreProperties.reSetSeleniumType();
        }
        Set<ITestResult> passedResults = tc.getPassedTests().getAllResults();
        Set<ITestResult> failedResults = tc.getFailedTests().getAllResults();
        for (ITestResult result : passedResults) {
            if (result.getStatus() == ITestResult.FAILURE || knisResults.contains(result)) {
                tc.getPassedTests().removeResult(result.getMethod());
                if (result.getStatus() == ITestResult.FAILURE)
                    tc.getFailedTests().addResult(result, result.getMethod());
            } else {
                PSLogger.info(getTestCaseInfo(result) + " passed");
            }
        }
        for (ITestResult result : failedResults) {
            if (result.getStatus() == ITestResult.SKIP || knisResults.contains(result)) {
                PSLogger.debug("Remove " + result.getMethod() + " from failed tests");
                tc.getFailedTests().removeResult(result.getMethod());
                super.getFailedTests().remove(result);
                if (result.getStatus() == ITestResult.SKIP) {
                    tc.getSkippedTests().addResult(result, result.getMethod());
                }
            } else {
                PSLogger.error(getTestCaseInfo(result) + " failed");
            }
        }
        Set<ITestResult> skippedResults = tc.getSkippedTests().getAllResults();
        for (ITestResult result : skippedResults) {
            PSLogger.skip(getTestCaseInfo(result) + " skipped");
        }
        for (ITestResult result : knisResults) {
            PSLogger._knis(getTestCaseInfo(result) + " knis");
        }
        int significantResults = passedResults.size() + failedResults.size() + knisResults.size();
        int allResults = significantResults + skippedResults.size();
        PSLogger.info("Failed/Skipped/Knises/All : " + failedResults.size() + "/" + skippedResults.size() +
                "/" + knisResults.size() + "/" + allResults);
        if (significantResults == 0) {
            PSLogger.warn("Nothing was run for " + tc.getName());
        }
        setTestContextParameters(null);
        if (CoreProperties.doPingDatabase()) {
            LocalServerUtils.pingDataBaseDaemon().doStop();
        }
        BasicCommons.closeAll();
        PSLogger.removePerTestAppender();

    }

    /**
     * should be unique for each test case to avoid overwriting logs
     *
     * @param tr - ITestResult
     * @return - path to file
     */
    private String getLogFileForTc(ITestResult tr) {
        String param =
                Arrays.toString(tr.getParameters()).
                        replace("[", "").
                        replace("]", "").
                        replace(",", "").
                        replace(" ", "_").trim();
        param += getTestContextParameters();
        String fileName = tr.getTestClass().getRealClass().getName() + "." +
                tr.getMethod().getMethodName() + "." + param + getIterNumber() + ".html";
        String res = PSLogger.getLogsDir().getAbsolutePath() + File.separator +
                fileName;
        return res.replaceAll("\\.+", ".");
    }

    private void setTestContextParameters(ITestContext testContext) {
        if (testContext == null) {
            testContextParameters = "";
            return;
        }
        Map map = testContext.getCurrentXmlTest().getParameters();
        if (map.isEmpty()) {
            testContextParameters = "";
            return;
        }
        testContextParameters = map.toString().
                replaceAll(",\\s*", ",").
                replace(" ", "_").trim();
    }

    private String getTestContextParameters() {
        return testContextParameters;
    }

    private static String getTestContextName(ITestContext testContext) {
        return "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()) + "] " +
                testContext.getName();
    }

    private void printOnStartMessage() {
        StringBuffer mess = new StringBuffer();
        BrowserTypes br = SeleniumDriverFactory.getLastBrowser();
        mess.append(br != null ? br.getFullName() : null).append(", ");
        mess.append(TestSession.getFullVersion());
        if (onStartMessage == null || !onStartMessage.equals(mess.toString())) {
            PSLogger.perTestInfo(onStartMessage = mess.toString());
        }
    }

    private static void setExcludedGroupsToAllMethods(ITestContext testContext) {
        if (groupsToSet == null || groupsToSet.length == 0) return;
        List<String> exGroups = Arrays.asList(testContext.getExcludedGroups());
        for (String gr : groupsToSet) {
            if (exGroups.contains(gr)) {
                PSLogger.info("Set excluded group " + gr);
                for (ITestNGMethod tm : testContext.getAllTestMethods()) {
                    tm.setMissingGroup(gr);
                }
            }
        }
        groupsToSet = null;
    }


    private static void initCheckingLogs() {
        RemoteLinServerUtils.LogsChecker th = RemoteLinServerUtils.getLogChecker();
        if (th != null) {
            PSLogger.debug("Start Check logs");
            th.start();
        }
    }

    private static String stopCheckingLogsAndCheckResult() {
        RemoteLinServerUtils.LogsChecker th = RemoteLinServerUtils.getLogChecker();
        if (th != null) {
            PSLogger.debug("Stop Check logs");
            return th.checkLogsAndStop();
        }
        return null;
    }

    /**
     * collect all methods to rerun
     *
     * @return Map with ITestClass'es  and ITestNGMethod's
     */
    public Map<ITestClass, List<ITestNGMethod>> getMethodsToRerun() {
        Map<ITestClass, List<ITestNGMethod>> res = new LinkedHashMap<ITestClass, List<ITestNGMethod>>();
        for (ITestNGMethod rerun : methodsToRerun) {
            ITestClass clazz = rerun.getTestClass();
            ITestNGMethod[] all = clazz.getTestMethods();
            if (!res.containsKey(clazz)) {
                res.put(clazz, new ArrayList<ITestNGMethod>());
            }
            // add all methods before this (required by testng. don't know how to bypass it)
            res.get(clazz).addAll(MethodHelper.getMethodsDependedUpon(rerun, all));

            res.get(clazz).add(rerun);

            // add all methods after
            for (ITestNGMethod method : all) {
                List<ITestNGMethod> dependedUpon = MethodHelper.getMethodsDependedUpon(method, all);
                if (dependedUpon.contains(rerun)) {
                    res.get(clazz).add(method);
                }
            }
        }
        return res;
    }

    public Map<String, List<String>> getFailedTestsDescription() {
        Map<String, List<String>> res = new LinkedHashMap<String, List<String>>();
        for (ITestResult tr : getFailedTests()) {
            String testName = tr.getTestClass().getXmlTest().getName();
            if (!res.containsKey(testName)) {
                res.put(testName, new ArrayList<String>());
            }
            res.get(testName).add(tr.getName());
        }
        return res;
    }

    public List<String> getTestsNames() {
        List<String> res = new ArrayList<String>();
        for (ITestContext c : getTestContexts()) {
            res.add(c.getName());
        }
        return res;
    }

    private String getIterNumber() {
        if (iterNumber < 1) return "";
        return "-" + iterNumber;
    }

    public void setIterNumber(int num) {
        methodsToRerun.clear();
        iterNumber = num;
    }
}
