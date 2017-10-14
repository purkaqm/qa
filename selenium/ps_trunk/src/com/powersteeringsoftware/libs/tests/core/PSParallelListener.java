package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSHTMLLayoutPerTest;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.logger.PSResults;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.HashSet;
import java.util.Set;

import static com.powersteeringsoftware.libs.logger.PSResults.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.02.12
 * Time: 16:33
 */
public class PSParallelListener extends TestListenerAdapter {

    @Override
    public void onStart(ITestContext tc) {
        CoreProperties.loadProperties();
        // for local use
        tc.getSuite().getXmlSuite().setDataProviderThreadCount(CoreProperties.DATA_PROVIDER_THREADS_MAX_NUMBER);
        PSLogger.createPerTestAppender();
        super.onStart(tc);
        ITestNGMethod[] ms = tc.getAllTestMethods();
        Set<String> classes = new HashSet<String>();
        for (ITestNGMethod m : ms) {
            classes.add(m.getRealClass().getSimpleName());
        }
        PSLogger.perTestInfo(tc.getSuite().getName() + " " + classes.toString(), PSHTMLLayoutPerTest.LOGS_LINK,
                PSLogger.getPerTestPlanLogsFile().getAbsolutePath(), TITLE);
    }

    @Override
    public void onTestStart(ITestResult tr) {
        _onTestStart(tr);
        SeleniumDriverFactory.initNewDriver();
    }

    protected void _onTestStart(ITestResult tr) {
        Thread th = Thread.currentThread();
        String fileName = PSLogger.getAllLogsFile().getAbsolutePath().replaceAll("(.*)\\.([^\\.]+)$",
                "$1." + PSLogger.getPerTestIndex() + "_" + th.getId() + ".$2");
        PSLogger.createPerThreadAppender(th, fileName);
        PSLogger.task("Test STARTED: " + getTestName(tr, th));
        super.onTestStart(tr);
    }

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        PSLogger.error("Configuration failure!");
        printStackTrace(tr);
        super.onConfigurationFailure(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        Thread th = Thread.currentThread();
        PSLogger.error("Test FAILED: " + getTestName(tr, th));
        if (SeleniumDriverFactory.isDriverInit()) {
            PSLogger.save("On failure");
        }
        printStackTrace(tr);
        super.onTestFailure(tr);
        Throwable thr = tr.getThrowable();
        PSResults res = ERROR;
        if (thr == null || !(thr.getCause() instanceof PSApplicationException)) {
            Boolean isServerAvailable = BasicCommons.isServerAvailable();
            Throwable validate = BasicCommons.validatePage();
            if (validate != null) {
                PSLogger.fatal(validate);
//                if (validate.getCause() == null) {
//                    PSLogger.saveDocument();
//                }
            } else if (isServerAvailable == null || isServerAvailable) {
                res = FATAL;
//                PSLogger.saveDocument();
            } else {
                PSLogger.error("503 Service Temporarily Unavailable");
            }
        }
        PSLogger.perTestInfo(getThreadTitle(th), getTestName(tr, th), PSLogger.getPerThreadLogsFile(th).getAbsolutePath(), res);
        PSLogger.removePerThreadAppender(th);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        Thread th = Thread.currentThread();
        PSLogger.error("Test SKIPPED: " + getTestName(tr, th));
        super.onTestSkipped(tr);
        PSLogger.perTestInfo(getThreadTitle(th), getTestName(tr, th), PSLogger.getPerThreadLogsFile(th).getAbsolutePath(), SKIP);
        PSLogger.removePerThreadAppender(th);

    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        Thread th = Thread.currentThread();
        PSLogger.perTestInfo(getThreadTitle(th), getTestName(tr, th), PSLogger.getPerThreadLogsFile(th).getAbsolutePath(), OK);
        PSLogger.removePerThreadAppender(th);
    }

    @Override
    public void onFinish(ITestContext tc) {
        PSLogger.removePerTestAppender();
        super.onFinish(tc);
    }

    private static String getThreadTitle(Thread th) {
        return "Thread-" + th.getId();
    }

    private static String getTestName(ITestResult tr, Thread th) {
        return th.getName() + "," + th.getId() + "(" + tr.getTestClass().getRealClass().getSimpleName() + "." +
                tr.getMethod().getMethodName() + ")";
    }

    private void printStackTrace(ITestResult tr) {
        Throwable err = tr.getThrowable();
        if (err != null) {
            PSLogger.fatal(err.getClass() + " " + err.getMessage());
            if (err instanceof UnsupportedOperationException) {
                PSLogger.fatal("UnsupportedOperationException occured");
            }
            PSLogger.fatal(err);
            if (err.getCause() != null) {
                PSLogger.error("Caused by:");
                PSLogger.fatal(err.getCause());
            }
        } else {
            PSLogger.error("Error in " + tr.getMethod().getMethodName());
        }
    }


}
