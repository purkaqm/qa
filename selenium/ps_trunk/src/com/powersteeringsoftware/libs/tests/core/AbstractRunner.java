package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSHTMLLayoutPerTest;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.CommandLineOptions;
import com.powersteeringsoftware.libs.util.ContextDBManager;
import com.powersteeringsoftware.libs.util.ResinJMXManager;
import com.powersteeringsoftware.libs.util.bugtrackers.KnownIssue;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.mail.PSMailerManager;
import com.powersteeringsoftware.mail.PSResultProperties;
import org.apache.commons.cli.ParseException;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.02.12
 * Time: 17:38
 */
public abstract class AbstractRunner {

    private long start;
    private long end;

    protected TestNG testng;
    protected TestListenerAdapter tla;
    protected CommandLineOptions cli;


    public static final int EXIT_OK = 0;
    public static final int EXIT_FAIL = -1;

    public AbstractRunner() {
        testng = new TestNG();
        testng.addListener(getTLA());
    }

    protected abstract TestListenerAdapter getTLA();

    /**
     * Setup core properties. All Properties saved in instance of CoreProperties
     *
     * @param args - array of string. If array is empty used properties file by
     *             default
     * @return CommandlineOptions
     * @throws org.apache.commons.cli.ParseException
     * @throws java.io.IOException
     * @see com.powersteeringsoftware.libs.settings.CoreProperties
     */
    protected void setProperties(String[] args) {
        CommandLineOptions cli = getCLI();
        try {
            cli.parse(args);
            CoreProperties.setPropertiesFile(cli.getCorePropertiesFile());
            CoreProperties.loadProperties();
            CoreProperties.setRestartResin(cli.getDoRestartOption());
            CoreProperties.setRestoreDB(cli.getDoRestoreOption());
            CoreProperties.setKillResin(cli.getDoKillOption());
            CoreProperties.setTestNGFile(cli.getTestSuiteConfig());
            CoreProperties.setHudsonJobId(cli.getJobId());
            CoreProperties.setDoEmail(cli.getDoEmail());
            CoreProperties.setEmailList(cli.getEmailList());
            CoreProperties.setEmailPrefix(cli.getEmailPrefix());
            CoreProperties.setWebDriverFilename(cli.getWebDriverFilename());
            if (cli.useFFProfile())
                CoreProperties.setUseFFProfile();
            CoreProperties.setAny(args);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
            System.exit(2);
        }
    }

    protected abstract CommandLineOptions getCLI();

    protected void setEmailProperties() {
        // path to general logs:
        File logs = PSLogger.getMainLogsFile();
        // path to test-ng logs:
        File testNgLogs = getTestNGReportFile();
        // hudson urls:
        String hudsonBaseUrl = CoreProperties.getHudsonUrl();
        String commonTestsJsp = CoreProperties.getHudsonTestsList();
        String hudsonJobName = CoreProperties.getHudsonJobName();
        String hudsonUrl = CoreProperties.getHudsonArtifactSuffix();
        // hudson job id:
        String hudsonJobId = CoreProperties.getHudsonJobId();
        // new email list, if specified
        String list = CoreProperties.getEmailList();
        if (list != null) {
            PSResultProperties.MAIL_RECEIVER.setValue(list);
        }


        if (CoreProperties.getEmailPrefix() != null) {
            // set prefix to email and full mode:
            PSResultProperties.SUBJECT_PREFIX.setValue(CoreProperties.getEmailPrefix());
        }

        PSResultProperties.TEST_TEST_CASE_FAILED_NUMBER.appendValue(getFailedTestCaseNumber());
        PSResultProperties.TEST_TEST_CASE_PASSED_NUMBER.appendValue(getPassedTestCaseNumber());
        PSResultProperties.TEST_TEST_CASE_SKIPPED_NUMBER.appendValue(getSkippedTestCaseNumber());
        PSResultProperties.TEST_TEST_CASE_ALL_NUMBER.appendValue(getPassedTestCaseNumber() + getFailedTestCaseNumber());

        String description = "Tests Total: " + (
                PSResultProperties.TEST_TEST_CASE_PASSED_NUMBER.getInteger() +
                        PSResultProperties.TEST_TEST_CASE_SKIPPED_NUMBER.getInteger() +
                        PSResultProperties.TEST_TEST_CASE_FAILED_NUMBER.getInteger()) +
                "; Pass/Skip/Fail: " +
                PSResultProperties.TEST_TEST_CASE_PASSED_NUMBER.getInteger() + "/" +
                PSResultProperties.TEST_TEST_CASE_SKIPPED_NUMBER.getInteger() + "/" +
                PSResultProperties.TEST_TEST_CASE_FAILED_NUMBER.getInteger();
        PSResultProperties.TEST_SHORT_DESCRIPTION.setValue(description);

        String sRes = PSResultProperties.TEST_RESULT.getValue();
        boolean res = true;
        if (sRes != null && !sRes.isEmpty())
            res = Boolean.parseBoolean(sRes);
        PSResultProperties.TEST_RESULT.setValue(Boolean.toString(res && PSResultProperties.TEST_TEST_CASE_FAILED_NUMBER.getInteger() == 0 && PSResultProperties.TEST_TEST_CASE_PASSED_NUMBER.getInteger() != 0));

        if (start == 0) {
            start = Calendar.getInstance().getTimeInMillis();
            end = start;
        }
        if (end == 0) end = start;
        PSResultProperties.TEST_START_LONG.setValue(start);
        PSResultProperties.TEST_END_LONG.setValue(end);

        KnownIssue.addToKnises(PSResultProperties.TEST_KNOWN_ISSUE.getListListValues());
        KnownIssue.collectInfo();
        PSResultProperties.TEST_KNOWN_ISSUE.setValues(KnownIssue.getInfo());

        PSResultProperties.TEST_LOGS_FILE.setFile(logs);
        PSResultProperties.TEST_LOGS_TEST_NG_FILE.setFile(testNgLogs);

        PSResultProperties.ENVIRONMENT_TEST_CORE_VERSION.appendValue(SeleniumDriverFactory.getVersion() + ", " + CoreProperties.getDriverTypes());
        PSResultProperties.ENVIRONMENT_TEST_URL_BASE.setValue(hudsonBaseUrl);
        PSResultProperties.ENVIRONMENT_TESTS_LIST.setValue(commonTestsJsp);
        PSResultProperties.ENVIRONMENT_TEST_URL.setValue(hudsonUrl);
        PSResultProperties.ENVIRONMENT_TEST_URL_JOB_NAME.setValue(hudsonJobName);
        if (hudsonJobId != null)
            PSResultProperties.ENVIRONMENT_TEST_URL_JOB_ID.setValue(hudsonJobId);

        PSResultProperties.ENVIRONMENT_PS_VERSION.setValue(TestSession.getFullVersion());
        String ver = TestSession.getAppVersion().getValue();
        if (CoreProperties.getURLContext().contains("sp") && !ver.contains("sp")) { //todo: hotfix, remove it
            String verSp = CoreProperties.getURLContext().replaceAll(".*(sp\\d+).*", "$1");
            ver += verSp;
        }
        PSResultProperties.ENVIRONMENT_PS_VERSION_SHORT.setValue(ver);
        PSResultProperties.ENVIRONMENT_SERVER_URL.setValue(CoreProperties.getURLServerHost());
        PSResultProperties.ENVIRONMENT_SERVER_CONTEXT.setValue(CoreProperties.getURLContext());
        PSResultProperties.ENVIRONMENT_CLIENT_HOST.setValue(LocalServerUtils.getHostName());
        PSResultProperties.ENVIRONMENT_CLIENT_USER.setValue(LocalServerUtils.getHostUser());
    }

    protected void sendEmail() {
        String props = null;
        try {
            PSResultProperties.load(CoreProperties.getMailerProperties());
            // properties file:
            File pFile = new File(PSLogger.getRootLogDir().getAbsolutePath() + File.separator + PSResultProperties.MAIL_PROPERTIES_PREFIX.getValue() +
                    TestSession.getAppVersion().toString() + "_" + CoreProperties.getBrowser().name() + ".txt");
            props = pFile.getAbsolutePath();
            PSResultProperties.load(props);
            PSResultProperties.TEST_ROOT_LOGS_DIR.setFile(PSLogger.getRootLogDir());
        } catch (IOException e) {
            PSLogger.fatal(e);
            return;
        }

        setEmailProperties();
        boolean saved = false;
        try {
            PSResultProperties.save(props);
            saved = true;
        } catch (IOException e) {
            PSLogger.error("Error has occured while loggin results to properties file.", false);
            PSLogger.fatal(e);
        }

        if (CoreProperties.getDoEmail() && CoreProperties.getHudsonJobId() != null) { // send email if job-id specified.
            PSMailerManager manager = new PSMailerManager();
            try {
                manager.sendSingle();
                if (saved)
                    LocalServerUtils.renameFiles(PSResultProperties.MAIL_PROPERTIES_BACK.getValue(), props);
            } catch (Throwable e) {
                PSLogger.fatal(e);
            }
        }
    }

    /**
     * set links to header for main logs file.
     * set title.
     */
    protected void addLinksAndTitleToLog(String title) {
        File fullLogs = PSLogger.getFullAllLogsTxtFile();
        float bytes = fullLogs.length();
        float mb = bytes / 1024 / 1024;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        File testng = getTestNGReportFile();

        PSHTMLLayoutPerTest.setEncoding();
        PSHTMLLayoutPerTest.setTitle(title);
        PSHTMLLayoutPerTest.appendLinkToTop(fullLogs, "single file log(" + nf.format(mb) + "mb)", "full-log-link");
        PSHTMLLayoutPerTest.appendLinkToTop(testng, "test-ng report");
        PSHTMLLayoutPerTest.saveDoc();
    }

    private File getTestNGReportFile() {
        return new File(testng.getOutputDirectory() + "/" + CoreProperties.getTestNGReportFile());
    }


    protected void setup() throws Exception {
        if (CoreProperties.isRestartResin())
            ResinJMXManager.stopResin(CoreProperties.isKillResin());
        if (CoreProperties.isRestoreDB()) {
            ContextDBManager.getConnection(CoreProperties.getContextDBString()).killActiveConnectionAndRestoreDB(CoreProperties.getContextDBName(),
                    CoreProperties.getContextDBBackupPath(), CoreProperties.getContextDBBackupFiles());
            PSLogger.info("Database was restored");
        }
        if (CoreProperties.isRestartResin()) {
            ResinJMXManager.cleanupLuceneIndexes();
            ResinJMXManager.startResin();
        }
        onShutdown();
    }

    /**
     * Start running tests. If some test have failed then these tests will be run again
     * several times. Count of test re-running can be got invoking method
     * CoreProperties.getRunFailedCount()
     */
    public void start() {
        initTests();
        PSLogger.info("Tests are started.");
        File testNgDir = null;
        int index = 1;
        String dirTemplate = CoreProperties.getTestNGReportDir();
        while ((testNgDir = new File(PSLogger.getRootLogDir().getAbsolutePath() + File.separator + CoreProperties.getTestNGReportDir())).exists()) {
            CoreProperties.setTestNGReportDir(dirTemplate + "-" + index++);
        }
        testng.setOutputDirectory(testNgDir.getAbsolutePath());
        start = Calendar.getInstance().getTimeInMillis();
        runTests();
        end = Calendar.getInstance().getTimeInMillis();
        endTests();
    }

    protected abstract void endTests();

    protected abstract void runTests();

    protected abstract void initTests();


    public int getFailedTestCaseNumber() {
        return getTLA().getFailedTests().size();
    }

    public int getPassedTestCaseNumber() {
        return getTLA().getPassedTests().size();
    }

    public int getSkippedTestCaseNumber() {
        return getTLA().getSkippedTests().size();
    }

    private void onShutdown() {
        // stop all servers and browsers in case CTRL+C:
        Thread th = new Thread() {
            public void run() {
                SeleniumDriverFactory.stopAllSeleniumDrivers();
            }
        };
        th.setName("Shutdown thread");
        th.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(th);
    }

}
