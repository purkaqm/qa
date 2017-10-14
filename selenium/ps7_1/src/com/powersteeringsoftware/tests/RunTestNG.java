package com.powersteeringsoftware.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import com.powersteering.mail.TestResultReporter;
import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.server.ContextDBManager;
import com.powersteeringsoftware.core.server.ResinJMXManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CLI;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;


/**
 * This class start Automatic Tests.</br>
 * Before invoking method start() you must:</br>
 * - configure log4j framework <i>PropertyConfigurator.configure(CoreProperties.LOG4J_PROP_FILE);</i> </br>
 * As example you can see method main() in class RunTestNG
 *
 * @author selyaev_ag
 *
 */
public class RunTestNG {

    private static Logger log = Logger.getLogger(RunTestNG.class.getName());

    private TestListenerAdapter tla;
    private TestNG testng;
    private String outputDir;

    public RunTestNG() {
        tla = new TestListenerAdapter();
        testng = new TestNG();
        testng.addListener(tla);
        outputDir=new String();
    }

    private boolean logResults() {

        FileWriter writer = null;

        try {
            String resultFile = CoreProperties.getLogMailerPath()
                    + File.separator + CoreProperties.getMailerFilename();
            log.debug("Log mailer file fullname is " + resultFile);
            writer = new FileWriter(new File(resultFile));
            writer.append(getStringResultIsOk());
            writer.append(getStringResultTestName());
            writer.append(getStringDateInMil());
            writer.append(getStringResultText());
            writer.append(getStringEnvironment());
            writer.close();
            return true;
        } catch (IOException ioe) {
            log.error("Error has accured while loggin results.");
            try {
                writer.close();
            } catch (IOException ioe2) {
                return false;
            }
            return false;
        }
    }

    private CharSequence getStringEnvironment() {
        StringBuilder environment = new StringBuilder(
                TestResultReporter.PROP_NAME_RESULT_ENVIRONMENT);
        environment.append(" ");
        environment.append(TestSession.getObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey()));
        environment.append("\n");
        return environment;
    }

    private CharSequence getStringDateInMil() {
        StringBuilder dateInMil = new StringBuilder(
                TestResultReporter.PROP_NAME_RESULT_DATE);
        dateInMil.append(" ");
        dateInMil.append(String.valueOf(Calendar.getInstance()
                .getTimeInMillis()));
        dateInMil.append("\n");
        return dateInMil;
    }

    private CharSequence getStringResultText() {
        StringBuilder resultText = new StringBuilder();
        resultText.append(TestResultReporter.PROP_NAME_RESULT_MAILSUBTEXT);
        resultText.append(" ");
        resultText.append(getFailedDescription());
        resultText.append("\n");
        return resultText;
    }

    private CharSequence getStringResultTestName() {
        StringBuilder resultTestName = new StringBuilder(
                TestResultReporter.PROP_NAME_RESULT_TESTNAME);
        resultTestName.append(" ");
        resultTestName.append(CoreProperties.getMailerTestName());
        resultTestName.append("\n");
        return resultTestName;
    }

    private StringBuilder getStringResultIsOk() {
        StringBuilder resultIsOk = new StringBuilder(
                TestResultReporter.PROP_NAME_RESULT_ISOK);
        resultIsOk.append(" ");
        resultIsOk.append((tla.getFailedTests().size() == 0) ? "1" : "0");
        resultIsOk.append("\n");
        return resultIsOk;
    }

    private String setOutputDirectory(){
        String loginMainPath = CoreProperties.getLogMainPath();
        StringBuilder outputBuildDirectory = new StringBuilder(loginMainPath);
        outputBuildDirectory.append(File.separatorChar);
        outputBuildDirectory.append("log");

        if (CoreProperties.isMultipleLogs()) {
            outputBuildDirectory.append("_");
            outputBuildDirectory.append(new SimpleDateFormat(CoreProperties
                    .getMultipleFolderFormatter()).format(new Date()));
        }

        this.outputDir = outputBuildDirectory.toString();

        return this.outputDir;
    }

    private String getOutputDir(){
        return this.outputDir;
    }

    private String getOutputFile(){
        return getOutputDir() + File.separatorChar + CoreProperties.FAILED_TESTS_XML;
    }


    /**
     * Start running tests. If test have failed failed tests will be run again
     * several times. Count of test re-running can be got invoking method
     * CoreProperties.getRunFailedCount()
     *
     */
    public void start(){
        log.debug("Tests are started.");
        if(!setupTestExecution()) return;
        if(!run(CoreProperties.getTestNGFile(), getOutputDir())) return;
        if(!stop()) return;
    }

    private boolean stop() {
        try{
        log.debug("Check failed.");
        checkForFailed();
        log.debug("Log results.");
        logResults();
        log.debug("Stop selenium.");
        SeleniumDriverSingleton.stop();
        log.debug("Tests have finished.");
        return true;
        } catch (Exception e){
            log.error("Error while stop test execution");
            return false;
        }
    }

    private boolean setupTestExecution() {
        try{
            testng.setOutputDirectory(setOutputDirectory());
            log.debug("Try to start Selenium.");
            SeleniumDriverSingleton.setDriverUsingCoreProperties();
            log.debug("Try to kill and restore DB.");
            killActiveConnectionAndRestoreDB();
            log.debug("Restart Resin.");
            restartResin();
            log.debug("Login PS.");
            BasicCommons.logIn();
            log.debug("Run tests.");
            return true;
        } catch (Exception e){
            stop();
            log.error("Error while starting tests.");
            return false;
        }
    }

    /**
     * Check if there are failed tests and try run them again.
     *
     */
    private void checkForFailed() {
        int failedCount = CoreProperties.getRunFailedCount();
        int runFailedCount = 1;
        if (tla.getFailedTests().size() > 0 && 0 != failedCount--) {
            log.info("Run failed tests:");
            log.info(getFailedDescription());
            run(getOutputFile(), this.outputDir + runFailedCount++);
        }
    }


    /**
     * Run TestNG tests
     *
     * @param testNGPropFile -
     *            testNG property file
     */
    private boolean run(String testNGPropFile, String outputDir) {
        try {
            tla = new TestListenerAdapter();
            testng = new TestNG();
            testng.addListener(tla);
            testng.setOutputDirectory(outputDir);

            List<java.lang.String> suites = new ArrayList<java.lang.String>();
            suites.add(testNGPropFile);
            testng.setTestSuites(suites);

            testng.run();
            return true;
        } catch (Exception e) {
            log.error("Error while run tests");
            stop();
            return false;
        }
    }


    /**
     * Get failed tests description
     *
     * @return A String with descripton of fail
     */
    private String getFailedDescription() {
        StringBuilder failed = new StringBuilder("Tests Failed: ");
        failed.append(tla.getFailedTests().size());

        if (tla.getFailedTests().size() > 0) {
            failed.append("(");
            for (ITestResult itemResult : tla.getFailedTests()) {
                failed.append(itemResult.getName());
                failed.append(";");
            }
            failed.append(")");
        }

        return failed.toString();
    }

    /**
	 * Kill all active connection and restore DB.It is necessary restore DB
	 * backup and restart Resin before executing new tests
	 *
	 * @throws java.sql.SQLException
	 * @throws ClassNotFoundException
	 */
    private void killActiveConnectionAndRestoreDB() throws java.sql.SQLException, ClassNotFoundException {
        if (CoreProperties.isRestoreDB()) {
            ContextDBManager.getConnection(CoreProperties.getContextDBString())
                    .killActiveConnectionAndRestoreDB(
                            CoreProperties.getContextDBName(),
                            CoreProperties.getContextDBBupath());
        }
    }
    /**
	 * Restart Resin Server. It is better before invoking this method invoke
	 * method killActiveConnectionAndRestoreDB
	 *
	 * @throws Exception
	 */
    private void restartResin()	throws Exception {
        if (CoreProperties.isRestartResin()) {
            ResinJMXManager.getConnection(CoreProperties.getContextJMX())
                    .restart().waitUntilServerIsReady(
                            CoreProperties.getURLServerWithContext());
        }
    }

    /**
	 * Setup core properties. All Properties saved in instance of CoreProperties
	 *
	 * @see com.powersteeringsoftware.core.util.CoreProperties
	 * @param args -
	 *            array of string. If array is empty used properties file by
	 *            default
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private void setupCoreProperties(String[] args) throws FileNotFoundException, IOException{
        CLI cli = new CLI(args);
        if (cli.parse()) {
            CoreProperties.loadProperties(cli.getCorePropertiesFile());
        } else {
            CoreProperties.loadProperties(CoreProperties.DEFAULT_CORE_PROPERTIES_FILE);
        }
        log.info(CoreProperties.getPropertiesString());
    }

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(CoreProperties.LOG4J_PROP_FILE);
        log.debug("Start main method");
        RunTestNG runTestNG = new RunTestNG();
        log.debug("RunTestNG is created");
        runTestNG.setupCoreProperties(args);
        log.debug("RunTestNG is setuped");
        runTestNG.start();
    }

}
