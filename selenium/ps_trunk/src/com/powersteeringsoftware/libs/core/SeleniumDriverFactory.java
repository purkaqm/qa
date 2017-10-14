package com.powersteeringsoftware.libs.core;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.htmlcleaner.HtmlCleaner;

import java.io.File;
import java.io.StringReader;
import java.util.*;

import static com.powersteeringsoftware.libs.settings.CoreProperties.*;


/**
 * @author selyaev_ag
 */
public class SeleniumDriverFactory {
    private static final String IE_META_HTTP_EQUIV = "X-UA-Compatible";
    private static final String IE_META_EMULATING_IE8 = "IE=EmulateIE8";

    // variable for checking hangs (for local selenium server rc)
    static long lastHandling = System.currentTimeMillis();
    private static Version seleniumVersion;

    private static Thread main = Thread.currentThread();
    private static boolean parallel;
    // selenium exception:
    public static final String SELENIUM_EXCEPTION_ALERT_WAS = "There was an unexpected Alert!";
    public static final String SELENIUM_EXCEPTION_TIMEOUT = "Timeout after ";

    public enum Version {
        _NULL("NULL"),
        _2_0_B3("2.0b3"),
        _2_0_RC3("2.0rc3"),
        _2_0_0("2.0.0"),
        _2_2_0("2.2.0"),
        _2_3_0("2.3.0"),
        _2_5_0("2.5.0"),
        _2_6_0("2.6.0"),
        _2_7_0("2.7.0"),
        _2_8_0("2.8.0"),
        _2_9_0("2.9.0"),
        _2_11_0("2.11.0"),
        _2_12_0("2.12.0"),
        _2_13_0("2.13.0"),
        _2_14_0("2.14.0"),
        _2_15_0("2.15.0"),
        _2_16_1("2.16.1"),
        _2_18_0("2.18.0"),
        _2_19_0("2.19.0"),
        _2_28_0("2.28.0"),
        _2_32_0("2.32.0"),
        _2_39_0("2.39.0"),
        _2_41_0("2.41.0"),
        _2_42_2("2.42.2"),
        _2_45_0("2.45.0");
        String ver;
        File file;

        Version(String s) {
            ver = s;
        }

        public boolean isTextAreaBroken(SeleniumDriver driver) {
            return driver.getType().isWebDriver() && greater(_2_0_RC3);
        }

        public boolean isSelectInputBroken(SeleniumDriver driver) { // its for chrome
            return driver.getType().isGoogleChromeGreaterThan(10) && inRange(_2_0_RC3, _2_2_0);
        }

        /**
         * for web-driver ff4.0
         *
         * @return
         */
        public boolean isDialogBroken(SeleniumDriver driver) {
            return driver.getType().isWebDriverFF(4) && inRange(_2_0_0, _2_7_0);
        }

        public boolean isCssLinkBroken(SeleniumDriver driver) {
            return driver.getType().isWebDriverIE(9) && greater(_2_0_B3);
        }

        public boolean isLinkBroken(SeleniumDriver driver) {
            return driver.getType().isWebDriverIE() && greater(_2_0_B3);
        }

        public boolean isDragAndDropBroken(SeleniumDriver driver) {
            return driver.getType().isWebDriver() && isDragAndDropBroken();
        }

        boolean isDragAndDropBroken() {
            return greater(_2_5_0); // e.g. broken d'n'd on CreateWorkPage
        }

        public boolean isAttachingBroken(SeleniumDriver driver) {
            return driver.getType().isRCDriver() && inRange(_2_12_0, _2_16_1); // 12-15
        }

        public boolean isInputBroken(SeleniumDriver driver) {
            //return driver.getType().isRCDriverIE() && greater(_2_9_0);
            // this is for debug
            return driver.getType().isRCDriverIE() && driver.getType().isLessThan(9) && inRange(_2_9_0, _2_19_0); // don't know where it fixed, but in 2.28.0 all ok.
        }

        private boolean inRange(Version t1, Version t2) {
            if (t1.compareTo(t2) > 0) {
                Version t0 = t2;
                t2 = t1;
                t1 = t0;
            }
            return compareTo(t1) >= 0 && compareTo(t2) <= 0;
        }

        public boolean greater(Version v) {
            Version[] vs = values();
            return inRange(v, vs[vs.length - 1]);
        }

        public boolean less(Version v) {
            return getVersion().ordinal() < v.ordinal();
        }

        @Override
        public String toString() {
            return ver;
        }

        public File getFile() {
            return file;
        }
    }

    static {
        // disable external logging
        Logger.getLogger("org").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }

    private static Map<Thread, List<SeleniumDriver>> seleniumDrivers = Collections.synchronizedMap(new LinkedHashMap<Thread, List<SeleniumDriver>>());

    public static synchronized SeleniumDriver getDriver() {
        if (getDriversList().size() == 0) {
            initNewDriver();
        }
        lastHandling = System.currentTimeMillis();
        List<SeleniumDriver> drivers = getDriversList();
        return drivers.get(drivers.size() - 1);
    }

    public static synchronized void initNewDriver() {
        initNewDriver(getBrowser(), getURLServerHost(), getURLContext());
    }

    public static synchronized void initNewDriver(String host, String context) {
        PSLogger.info("Selenium version is " + getVersion() + "; url=" + host + context);
        if (useSeleniumRC() && !getBrowser().isZero()) {
            getBrowser().setRCDriver();
        } else {
            getBrowser().setWebDriver();
        }
        initNewDriver(getBrowser(), host, context);
    }

    public static synchronized void initNewDriver(BrowserTypes browser, String host, String context) {
        SeleniumDriver driver;
        if (host == null || context == null) throw new NullPointerException("Initial host should be specified");
        fixFFProfile(browser);
        if (browser.isRCDriver()) {
            driver = SeleniumRCDriver.initDriver(
                    getSeleniumServerHostname(),
                    getSeleniumServerPort(),
                    browser,
                    host,
                    context,
                    getWaitForElementToLoad());
        } else {
            driver = SeleniumWebDriver.initDriver(
                    browser,
                    host,
                    context,
                    getWaitForElementToLoad());

        }
        addToDrivers(driver);
    }

    private static void fixFFProfile(BrowserTypes br) {
        if (!br.isFF() || br.getProfile() == null) return;
        List<String> toDel = Arrays.asList("extensions.cache", "extensions.ini", "extensions.rdf", "compatibility.ini");
        File dir = new File(br.getProfile());
        for (File f : dir.listFiles()) {
            PSLogger.debug2("Profile: " + f);
            if (toDel.contains(f.getName())) {
                PSLogger.debug("delete " + f + ":" + f.delete());
            }
        }
    }

    public static synchronized void initZeroDriver(String url, String context) {
        BrowserTypes br = BrowserTypes.ZERO;
        br.setWebDriver();
        initNewDriver(br, url, context);
    }

    public static synchronized void initNewDriver(BrowserTypes browser) {
        initNewDriver(browser, getURLServerHost(), getURLContext());
    }

    public static void setDoWaitForReady(SeleniumDriver driver, boolean waitForReady) {
        if (driver instanceof SeleniumRCDriver) {
            // not supported now
            return;
        }
        ((SeleniumWebDriver) driver).doWaitForReady = waitForReady;
    }

    public static Version getVersion() {
        if (seleniumVersion == null) {
            //get version from jar
            Class clazz = Selenium.class;
            File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile());
            PSLogger.debug2("jar file : " + file);
            String version = file.getName().replaceAll("\\.jar$", "").replaceAll(".*-", "");
            for (Version v : Version.values()) {
                if (v.ver.equalsIgnoreCase(version)) {
                    v.file = file;
                    return seleniumVersion = v;
                }
            }

            PSLogger.warn("Can't determinate selenium version!");
            seleniumVersion = Version._NULL;
            seleniumVersion.file = file;
        }
        return seleniumVersion;
    }

    public static File getSeleniumLibDir() {
        return getVersion().file.getParentFile().getAbsoluteFile();
    }

    static String getWebDriverFireEventAtScript() {
        return new File(getSeleniumLibDir() + File.separator + "fireEventAt.js").getAbsolutePath();
    }

    private static synchronized void addToDrivers(SeleniumDriver driver) {
        Thread th = getThread();
        if (!seleniumDrivers.containsKey(th)) {
            seleniumDrivers.put(th, new ArrayList<SeleniumDriver>());
        }
        seleniumDrivers.get(th).add(driver);
    }

    private static synchronized List<SeleniumDriver> getDriversList() {
        Thread th = getThread();
        if (!seleniumDrivers.containsKey(th)) {
            seleniumDrivers.put(th, new ArrayList<SeleniumDriver>());
        }
        return seleniumDrivers.get(th);
    }

    private static Thread getThread() {
        if (parallel) {
            return Thread.currentThread();
        }
        return main;
    }

    public static void setParallel(boolean v) {
        parallel = v;
        PSLogger.setDoPrintThreadInfo(v);
    }

    public static boolean isParallel() {
        return parallel;
    }

    public static boolean isNewWindowAllowed() {
        if (!isDriverInit()) return false;
        BrowserTypes browser = getDriver().getType();
        return !(browser.isRCDriverFF() && browser.getProfile() != null) && !browser.isRCDriverIE();
    }

    public static int getNumberOfDrivers() {
        int size = 0;
        for (Object key : seleniumDrivers.keySet()) {
            size += seleniumDrivers.get(key).size();
        }
        return size;
    }

    public static long getLastHandling() {
        return lastHandling;
    }

    public static synchronized boolean isDriverInit() {
        return getDriversList().size() != 0;
    }

    public static BrowserTypes getLastBrowser() {
        if (!isDriverInit()) return null;
        return getDriver().getType();
    }

    /**
     * Stop selenium and close browser
     */
    public static synchronized void stopLastSeleniumDriver() {
        if (getDriversList().size() == 0) {
            PSLogger.debug2("No drivers to stop");
            return;
        }
        PSLogger.debug2("Stop last driver");
        getDriversList().remove(getDriversList().size() - 1).stop();
        if (getNumberOfDrivers() == 0) {
            LocalServerUtils.killBrowsers();
        }
        lastHandling = System.currentTimeMillis();
    }


    public static synchronized void restartLastSeleniumDriver() {
        PSLogger.debug2("Restart last selenium driver");
        stopLastSeleniumDriver();
        try {
            initNewDriver();
        } catch (SeleniumException e) {
            PSLogger.error("Can't restart selenium");
            PSLogger.fatal(e);
            PSLogger.saveFull();
            LocalServerUtils.killBrowsers();
        }
    }

    public static synchronized void stopAllSeleniumDrivers() {
        for (Thread th : seleniumDrivers.keySet()) {
            List<SeleniumDriver> list = seleniumDrivers.get(th);
            if (list.size() != 0)
                PSLogger.info("Stop all drivers");
            while (list.size() != 0) {
                list.remove(list.size() - 1).stop();
            }
        }
        LocalServerUtils.killBrowsers();
        BrowserTypes.Driver driver = CoreProperties.getBrowser().getDriver();
        if (driver == null) return; // hotfix for chrome:
        LocalServerUtils.killProcess(driver.getName());
    }

    public static boolean isCriticalException(Throwable e) {
        return SeleniumRCDriver.isCriticalException(e);
    }


    public static Document getDocument(String source) {
        PSLogger.debug2("getDocument");
        Document result = null;
        try {
            HtmlCleaner cleaner = new HtmlCleaner(source);
            cleaner.setAllowHtmlInsideAttributes(true);
            cleaner.setNamespacesAware(false);
            cleaner.clean();
            SAXReader reader = new SAXReader(DOMDocumentFactory.getInstance());
            result = reader.read(new StringReader(cleaner.getCompactXmlAsString()));
        } catch (Exception e) {
            PSLogger.warn("getDocument " + e.getMessage());
        }
        return result;
    }

    static Integer[] getCoordinates(Selenium driver, String locator) {
        try {
            PSLogger.debug2("getCoordinates(" + locator + ")");
            Integer[] res = new Integer[4];
            int height = driver.getElementHeight(locator).intValue();
            int width = driver.getElementWidth(locator).intValue();
            int left = driver.getElementPositionLeft(locator).intValue();
            int top = driver.getElementPositionTop(locator).intValue();
            res[3] = height;
            res[2] = width;
            res[0] = left;
            res[1] = top;
            return res;
        } catch (Exception e) {
            PSLogger.error("Can't get coordinates for " + locator + ". " + e.getMessage());
            return null;
        }
    }


    static void dragAndDropToElement(SeleniumDriver driver, String locSrc, String locDst) {
        Integer[] iSrc = driver.getCoordinates(locSrc);
        Integer[] iDst = driver.getCoordinates(locDst);
        int diffY = iDst[1] - iSrc[1];
        int diffX = iDst[0] - iSrc[0];
        String diff = diffX + "," + diffY;
        PSLogger.debug2("Move on " + diff + " pixels");
        driver.mouseDown(locSrc);
        driver.mouseMoveAt(locSrc, diff);
        driver.mouseUp(locDst);
    }

    static void waitForFrameToLoad(Selenium driver, String loc) {
        PSLogger.debug2("waitForFrameToLoad(" + loc + ", " + getWaitForElementToLoadAsString() + ")");
        driver.waitForFrameToLoad(loc, getWaitForElementToLoadAsString());
    }

    @Deprecated
    static void captureScreenshotAsHtml(SeleniumDriver driver, String file) {
        try { // this method is called from PSLogger
            captureScreenshotAsHtml(driver, getURLServerHost(), file);
        } catch (SeleniumException e) {
            PSLogger.fatal(e);
        }
    }

    static String doGetEval(SeleniumDriver driver, String script) {
        PSLogger.debug2("getEval(" + script + ") on page " + driver.getLocation());
        script = script.replaceAll(";$", "");
        try {
            if (!script.contains(";")) {
                if (script.endsWith(".value") && !script.contains(".attributes[0]")) {
                    return driver.getValue(script.replaceAll("\\.value$", ""));
                }
                /*if (script.endsWith(".innerHTML")) {
                    return getText(script.replaceAll("\\.innerHTML$", ""));
                }*/
                if (script.endsWith(".href")) {
                    return driver.getAttribute(script.replaceAll("\\.href$", ""), "href");
                }
            }
        } catch (SeleniumException e) {
            PSLogger.warn(e);
        }
        return null;
    }

    static void attachFile(SeleniumDriver driver, String locator, String file) {
        if (driver.getType().isFF()) {
            try {
                driver.type(locator, file);
            } catch (SeleniumException se) {
                // see http://code.google.com/p/selenium/issues/detail?id=2826
                if (getVersion().isAttachingBroken(driver)) {
                    PSSkipException.skip(se.getMessage());
                }
                throw se;
            }
        } else if (!isLocalSeleniumServer()) {
            PSSkipException.skip("Method attachFile is not supported for remote using and not ff");
/*        } else if (driver.getType().isIE()) {
            // for ie, not for sf:
            driver.focus(locator);
            LocalServerUtils.type(file);*/
        } else {
            PSSkipException.skip("Method attachFile is supported only for ff and ie");
        }
        //some waiting for tc com.powersteeringsoftware.tests.acceptance.TestDriver.addAttachments
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            //ignore
        }
    }

    private static void captureScreenshotAsHtml(SeleniumDriver driver, String url, String file) {
        if (driver.getType().isZero()) return;
        Document doc = driver.getDocument();
        if (doc == null) {
            PSLogger.warn("Can't get document");
            return;
        }
        for (Object script : doc.selectNodes("//script")) {
            ((Node) script).detach();
        }
        for (Object div : doc.selectNodes("//div")) {
            DefaultElement e = (DefaultElement) div;
            if (e.attribute("style") != null && e.attribute("style").getValue().toLowerCase().contains("display: none")) {
                e.detach();
            }
        }
        for (Object img : doc.selectNodes("//*")) {
            DefaultElement e = (DefaultElement) img;
            if (e.attribute("src") != null) {
                if (!e.attribute("src").getValue().startsWith(url))
                    e.attribute("src").setValue(url + e.attribute("src").getValue());
            }
            if (e.attribute("href") != null) {
                e.attribute("href").setValue(url + e.attribute("href").getValue());
            }
        }
        LocalServerUtils.saveDocument(doc, file);
    }

    public static void addMetaData(String httpEquiv, String content) {
        String script = "var head = document.getElementsByTagName('head')[0];\n" +
                "var firstChild = head.getElementsByTagName('meta')[0];\n" +
                "if (firstChild != null && firstChild.getAttribute('http-equiv') != '" + httpEquiv + "'){\n" +
                "var meta = document.createElement('meta');\n" +
                "meta.setAttribute('http-equiv','" + httpEquiv + "');\n" +
                "meta.content = '" + content + "';\n" +
                "head.insertBefore(meta, firstChild);\n" +
                "}";
        getDriver().runScript(script);
    }

    /**
     * its debug method.
     */
    public static void addIE9MetaHotFix() {
        addMetaData(IE_META_HTTP_EQUIV, IE_META_EMULATING_IE8);
    }


    static void checkForPopup(long timeout, Object... objects) {
        checkForPopup(timeout, (Thread) objects[0], (SeleniumException) objects[1], (String) objects[2]);
    }

    static void checkForPopup(long timeout, Thread thread, SeleniumException ex, String msg) {
        long t = System.currentTimeMillis();
        thread.start();
        boolean enterPressed = false;
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (!enterPressed && System.currentTimeMillis() - t > timeout) {
                PSLogger.warn(msg + " after " + timeout + " ms, seems popup; press enter");
                LocalServerUtils.enter();
                enterPressed = true;
                continue;
            }
            if (enterPressed && System.currentTimeMillis() - t > timeout * 2) {
                String msg2 = SELENIUM_EXCEPTION_TIMEOUT + timeout * 2;
                PSLogger.warn(msg2);
                throw ex == null ? new SeleniumException(msg + ": " + msg2) : ex;
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

    static Object[] getRefreshThread(final SeleniumDriver driver) {
        Object[] objects = new Object[3];
        final SeleniumException[] exceptions = new SeleniumException[1];
        Thread thread = new Thread() {
            public void run() {
                try {
                    driver.refresh();
                    driver.waitForPageToLoad();
                } catch (SeleniumException e) {
                    exceptions[0] = new SeleniumException(e);
                }
            }
        };
        objects[0] = thread;
        objects[1] = exceptions[0];
        objects[2] = "can't refresh" + (exceptions[0] != null ? " : " + exceptions[0].getMessage() : "");
        return objects;
    }

    public static void refreshWithPopup() {
        if (!isDriverInit()) {
            PSLogger.debug2("No driver init to refresh");
            return;
        }
        SeleniumDriver driver = getDriver();
        checkForPopup(driver.getTimeout() / 2, getRefreshThread(getDriver()));
    }

}
