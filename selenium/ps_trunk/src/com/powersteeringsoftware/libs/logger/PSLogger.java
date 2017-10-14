package com.powersteeringsoftware.libs.logger;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.bugtrackers.KnownIssue;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 21.04.2010
 * Time: 16:41:53
 * To change this template use File | Settings | File Templates.
 */
public class PSLogger {

    private static final String TXT_LOG_FILE = "logfile";
    private static final String PER_TESTCASE_FILE_APPENDER_NAME = "PSHTMLLayout_perTestCase";
    private static final String PER_TEST_FILE_APPENDER_NAME = "PSHTMLLayout_perTestPlan";
    private static final String ALL_LOGS_APPENDER_NAME = "PSHTMLLayout_All";//PSHTMLLayoutPerTestCase.class.getSimpleName();

    private static final String FULL_FILE_EXTENSION = ".full";
    private static int perTestIndex = 0;
    private static boolean printThread;

    static {
        init();
    }

    private static Logger log = Logger.getLogger(PSLogger.class.getName());

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    public static Logger getLogger(Class cl) {
        return Logger.getLogger(cl);
    }

    public static void setDoPrintThreadInfo(boolean b) {
        printThread = b;
        for (FileAppender fa : getFileAppenders()) {
            Layout la = fa.getLayout();
            if (la != null && la instanceof PSHTMLLayoutPerTestCase) {
                ((PSHTMLLayoutPerTestCase) la).setPrintThread(b);
            }
        }
    }

    public static void debug(Object msg) {
        log.debug(msg);
    }

    public static void debug2(Object msg) {
        if (!CoreProperties.getTestMoreVerbose()) return;
        log.log(PSLevel.DEBUG2, msg);
    }

    public static void fatal(String m, Object msg) {
        error(m);
        fatal(msg);
    }

    public static void fatal(Object msg) {
        if (msg instanceof Throwable) {
            String sMsg = ((Throwable) msg).getMessage();
            if (sMsg != null && !sMsg.contains(msg.getClass().getName()))
                sMsg = "[" + msg.getClass().getName() + "]:" + sMsg;
            log.fatal(sMsg);
            for (StackTraceElement stElement : ((Throwable) msg).getStackTrace()) {
                log.fatal(stElement.toString());
            }
        } else {
            log.fatal(msg);
        }
    }

    public static void error(Object message) {
        error(message, false);
    }

    public static void error(Object message, boolean doSave) {
        log.error(message);
        if (doSave)
            saveAll(message.toString());
    }

    public static void warn(Object message) {
        log.warn(message);
    }

    public static void info(Object message) {
        log.log(PSLevel.INFO, message);
    }

    /**
     * @param knis
     * @param testClass
     * @param testMethod
     */
    public static void knis(String knis, String testClass, String testMethod) {
        KnownIssue k = KnownIssue.addToKnises(knis, testClass, testMethod);
        _knis(k.getUrl());
    }

    /**
     * Warning! this method adds to knis without test-case info.
     * it will collected automatically
     *
     * @param id
     */
    public static void knis(String id) {
        KnownIssue k = KnownIssue.addToList(id);
        k.setTestMethod();
        _knis(k.getUrl());
    }

    public static void knis(int id) {
        knis(String.valueOf(id));
    }

    public static void _knis(String message) {
        log.log(PSLevel.KNIS, message);
    }


    public static void task(Object message) {
        final int length = 50;
        StringBuffer line = new StringBuffer();
        for (int i = 0; i < length; i++) {
            line.append("=");
        }
        log.log(PSLevel.TASK, line);
        log.log(PSLevel.TASK, message);
        log.log(PSLevel.TASK, line);
    }

    public static void skip(Object message) {
        log.log(PSLevel.SKIP, message);
    }

    public static Throwable saveOnFailure() {
        try {
            saveAll("Save on failure");
            debug("url is:" + PSPage.getCurrentUrl());
        } catch (Throwable e) {
            error(PSLogger.class.getSimpleName() + ".saveOnFailure:");
            fatal(e);
            return e;
        }
        return null;
    }

    public static void saveFull() {
        saveFull(getFileToSave());
    }

    private static void saveFull(String file) {
        if (CoreProperties.getBrowser().isNull()) return;
        if (LocalServerUtils.isDisplayEnabled() && LocalServerUtils.getLocalServer().isProcessPresent(CoreProperties.getBrowser().getName())) {
            String full = file + ".full." + CoreProperties.getImageFormat();
            LocalServerUtils.makeScreenCapture(full);
            debug("make full screen capture");
            log.log(PSLevel.SAVE, "Full screen capture " + pathToMessage(full));
        } else {
            debug("Can't save full " + CoreProperties.getImageFormat() + " (display is not enabled or there is no browser started)");
        }
    }

    public static void saveAll(String msg) {
        if (!CoreProperties.getMakeScreenCapture()) return;
        String file = getFileToSave();
        if (CoreProperties.isLocalSeleniumServer()) {
            save(msg, file, false);
        }
        if (!SeleniumDriverFactory.getDriver().getType().isRCDriverIE()) {
            saveFull(file);
        }
        //save(msg, file, true);
    }

    public static void save() {
        save(null, getFileToSave());
    }

    public static void save(Object message) {
        save(message, getFileToSave());
    }

    /**
     * @param testName - name for test
     * @param linkName - name for link
     * @param fileName - full path to file
     * @param result   - PSResult object
     */
    public static void perTestInfo(String testName, String linkName, String fileName, PSResults result, Long start, Long end) {
        log.log(PSLevel.UPPER_INFO, new Object[]{result, testName, linkName, fileName, start, end});
    }

    public static void perTestInfo(String testName, String linkName, String fileName, PSResults result) {
        log.log(PSLevel.UPPER_INFO, new Object[]{result, testName, linkName, fileName});
    }

    public static void perTestInfo(String message, PSResults result) {
        log.log(PSLevel.UPPER_INFO, new Object[]{result, message});
    }

    public static void perTestInfo(String message) {
        perTestInfo(message, PSResults.TITLE);
    }


    public static void save(Object message, String file) {
        save(message, file, !CoreProperties.isLocalSeleniumServer());
    }

    private static void save(Object message, String file, boolean asHtml) {
        if (!doSave()) {
            log.log(PSLevel.SAVE, message);
            return;
        }
        if (asHtml) {
            file += ".html";
            SeleniumDriverFactory.getDriver().captureScreenshotAsHtml(file);
        } else {
            file += "." + CoreProperties.getImageFormat();
            try {
                SeleniumDriverFactory.getDriver().captureScreenshot(file);
            } catch (Exception ex) {
                error("captureScreenshot" + ex);
                saveFull(file);
            }
        }
        String msg = "Save to " + pathToMessage(file) + (message != null ? ": " + message : "");
        if (!new File(file).exists()) {
            msg = "Can't save to file... " + file + (message != null ? ": " + message : "");
        }
        log.log(PSLevel.SAVE, msg);
    }

    public static void saveDocument() {
        try {
            log.log(PSLevel.SAVE, SeleniumDriverFactory.getDriver().getDocument().asXML());
        } catch (Exception e) {
            error("save:" + e.getMessage());
            return;
        }
    }

    private static boolean doSave() {
        if (!CoreProperties.getMakeScreenCapture()) return false;
        if (!SeleniumDriverFactory.isDriverInit()) {
            debug("selenium is not initialized");
            return false;
        }
        return !SeleniumDriverFactory.getDriver().getType().isNull();
    }

    public static void save(SeleniumDriver driver) {
        String file;
        if (!doSave()) return;
        driver.captureScreenshot(file = getFileToSave() + "." + CoreProperties.getImageFormat());
        log.log(PSLevel.SAVE, pathToMessage(file));
    }


    private static void init() {
        CoreProperties.loadProperties();
        System.out.println("log4j config path and dir : " + CoreProperties.getLog4jProperties() + ", " + CoreProperties.getLogMainPath());
        //after load common properties:
        System.setProperty(CoreProperties.SYS_PROP_LOG_ROOT_PATH, CoreProperties.getLogMainPath());
        System.out.println(Logger.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        System.out.println(System.getProperty(CoreProperties.SYS_PROP_LOG_ROOT_PATH));
        PropertyConfigurator.configure(CoreProperties.getLog4jProperties());
        System.out.println("load");
    }

    public static void setNewLogsRoot(String newLogRoot) {
        CoreProperties.setLogMainPath(newLogRoot);
        init();
    }

    private static List<FileAppender> getFileAppenders() {
        List<FileAppender> res = new ArrayList<FileAppender>();
        Enumeration en = Logger.getRootLogger().getAllAppenders();
        while (en.hasMoreElements()) {
            Object o = en.nextElement();
            if (o instanceof FileAppender)
                res.add((FileAppender) o);
        }
        return res;
    }

    /**
     * get file name to save png
     *
     * @return file name of png picture
     */
    private static synchronized String getFileToSave() {
        File dir = getLogsDir();
        String file = String.valueOf(System.currentTimeMillis());
        if (dir == null) return file;
        dir = new File(dir.getAbsolutePath());// + File.separator + CoreProperties.getRelativeLogFilesDir());
        if (!dir.exists()) dir.mkdir();
        return dir.getAbsolutePath() + File.separator + file;
    }


    public static FileAppender getFileAppender(Class layoutClazz) {
        for (FileAppender fa : PSLogger.getFileAppenders()) {
            if (fa.getLayout().getClass().equals(layoutClazz)) {
                return fa;
            }
        }
        return null;
    }

    public static FileAppender getFileAppender(String name) {
        for (FileAppender fa : getFileAppenders()) {
            if (fa.getName().equals(name)) {
                return fa;
            }
        }
        return null;
    }

    public static FileAppender getPerTCAppender() {
        return getFileAppender(PER_TESTCASE_FILE_APPENDER_NAME);
    }

    /**
     * appender for main.html
     *
     * @return
     */
    public static FileAppender getPerTestAppender() {
        return getFileAppender(PER_TEST_FILE_APPENDER_NAME);
    }

    /**
     * @return directory that contains main.html, log.txt and files dir
     */
    public static File getRootLogDir() {
        return getMainLogsFile().getParentFile();
    }

    /**
     * @return directory with log.html and saved files (files dir)
     */
    public static File getLogsDir() {
        FileAppender fa = getFileAppender(ALL_LOGS_APPENDER_NAME);
        if (fa != null) return new File(fa.getFile()).getParentFile();
        return null;
    }

    /**
     * @return log.html file
     */
    public static File getAllLogsFile() {
        return new File(getFileAppender(ALL_LOGS_APPENDER_NAME).getFile());
    }

    public static File getAllLogsTxtFile() {
        return new File(getFileAppender(TXT_LOG_FILE).getFile());
    }


    public static File getFullAllLogsFile() {
        return getFullAllLogsFile(getAllLogsFile());
    }

    public static File getFullAllLogsTxtFile() {
        return getFullAllLogsFile(getAllLogsTxtFile());
    }

    private static File getFullAllLogsFile(File log) {
        File res;
        String fullLogsName = log.getName().replaceAll("\\.([^\\.]+)$", FULL_FILE_EXTENSION + ".$1");
        if ((res = new File(fullLogsName)).exists()) {
            return res;
        }
        List<File> files = new ArrayList<File>();
        int index = 1;
        while ((res = new File(log.getAbsolutePath() + "." + index++)).exists()) {
            files.add(res);
        }
        if (files.size() == 0) return log;
        files.add(log);
        for (File f : files) { // to debug
            LocalServerUtils.copy(f, f.getAbsolutePath() + ".bac.txt");
        }
        File toDelete = null;
        do {
            if (toDelete != null) {
                toDelete.delete();
            }
            LocalServerUtils.copy(files.get(1), files.get(0), true);
            toDelete = files.remove(1);
        } while (files.size() != 1);
        return LocalServerUtils.rename(files.get(0), fullLogsName);
    }

    public static File getPerTestPlanLogsFile() {
        return new File(getPerTestAppender().getFile());
    }

    public static File getPerThreadLogsFile(Thread th) {
        FileAppender fa = getFileAppender(PER_TESTCASE_FILE_APPENDER_NAME + "_" + th.getId());
        return new File(fa.getFile());
    }

    public static File getMainLogsFile() {
        return new File(getFileAppender(PSHTMLLayoutPerTest.class).getFile());
    }

    public static void createPerThreadAppender(final Thread th, String file) {
        try {
            FileAppender fa = new FileAppender(new PSHTMLLayoutPerTestCase(printThread, th), file, false);
            fa.setName(PER_TESTCASE_FILE_APPENDER_NAME + "_" + th.getId());
            Logger.getRootLogger().addAppender(fa);
        } catch (IOException e) {
            log.fatal(e.getMessage(), e);
        }
    }

    public static void removePerThreadAppender(Thread th) {
        FileAppender fa = getFileAppender(PER_TESTCASE_FILE_APPENDER_NAME + "_" + th.getId());
        if (fa == null) return;
        Logger.getRootLogger().removeAppender(fa);
    }


    public static void createPerTCAppender(String file) {
        try {
            FileAppender fa = new FileAppender(new PSHTMLLayoutPerTestCase(printThread), file, false);
            fa.setName(PER_TESTCASE_FILE_APPENDER_NAME);
            Logger.getRootLogger().addAppender(fa);
        } catch (IOException e) {
            log.fatal(e.getMessage(), e);
        }
    }

    public static void removePerTCAppender() {
        FileAppender fa = getPerTCAppender();
        if (fa != null) {
            Logger.getRootLogger().removeAppender(fa);
        }
    }

    public static void createPerTestAppender() {
        try {
            String all = getAllLogsFile().getAbsolutePath();
            String f;
            int count = 100;
            int i = 0;
            while (new File(f = all.replace(".html", "." + perTestIndex + ".html")).exists() && i++ < count) {
                ++perTestIndex;
            }
            if (i > count) {
                throw new FileNotFoundException("Can't make per test appender");
            }
            FileAppender fa = new FileAppender(new PSHTMLLayoutPerTestCase(printThread), f, false);
            fa.setName(PER_TEST_FILE_APPENDER_NAME);
            Logger.getRootLogger().addAppender(fa);
        } catch (IOException e) {
            log.fatal(e.getMessage(), e);
        }
    }

    public static int getPerTestIndex() {
        return perTestIndex;
    }

    public static void removePerTestAppender() {
        FileAppender fa = getPerTestAppender();
        if (fa != null) {
            Logger.getRootLogger().removeAppender(fa);
        }
    }

    static String pathToMessage(String path) {
        return "file=[" + path + "]";
    }

}
