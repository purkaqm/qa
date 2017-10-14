package com.powersteeringsoftware.libs.core;

import com.powersteeringsoftware.libs.enums.page_locators.HomePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.SAXReader;
import org.htmlcleaner.HtmlCleaner;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import java.io.File;
import java.io.StringReader;
import java.net.BindException;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.12.10
 * Time: 20:23
 */
class SeleniumRCDriver extends DefaultSelenium implements SeleniumDriver {

    public static final int SERVER_TIMEOUT = 10; // s
    public static final int SERVER_TIMEOUT_FOR_START = 1000; // ms
    public static final int SERVER_TIMEOUT_FOR_STOP = 20; // s
    public static final int SERVER_START_ITERATION_NUM = 20; // s

    private static int lastPort;
    private SeleniumServer theServer;
    private String baseUrl;
    private BrowserTypes browser;
    private String timeout;

    private static final String SELENIUM_SERVER_LOG_FILE = "./selenium.server.log";
    // critical exceptions for ie:
    private static final String SELENIUM_EXCEPTION_FRAME_CLOSED = "Current window or frame is closed!";
    private static final String SELENIUM_PERMISSION_DENIED = "Permission denied";
    // critical exception for safari:
    private static final String SELENIUM_EXCEPTION_SERVER = "ERROR Server Exception: unexpected command json";
    // timeout exception:
    private static final String SELENIUM_EXCEPTION_TIMEOUT = "SeleniumException ERROR: Command timed out";
    private static final String SELENIUM_EXCEPTION_OUT_OF_MEMORY = "The error message is: Out of memory";
    private static final String SELENIUM_EXCEPTION_UNKNOWN_RESULT = "result was neither 'true' nor 'false";
    private static final String SELENIUM_EXCEPTION_INVALID_ARGUMENT = "The error message is: Invalid argument.";
    // background for ff screenshots
    private static final String BACKGROUND_TO_SAVE_FOR_FF_CAPTURE_SCREENSHOOT = "background=#CCFFDD";

    private static final int DEFAULT_PORT = 4747;
    private static final int PORT_RANGE = 150;

    private String frame;

    private SeleniumRCDriver(BrowserTypes browser, String serverHost, int port, String url, long timeout) {
        super(serverHost, port, browser.getCommand(), url);
        this.browser = browser;
        this.timeout = String.valueOf(timeout);
    }

    /**
     * hotfix method (ie7,rc failure when init)
     *
     * @param server
     * @param port
     * @param browser
     * @param url
     * @param context
     * @param timeout
     * @return
     */
    static synchronized SeleniumRCDriver initDriver(String server, int port, BrowserTypes browser, String url, String context, long timeout) {
        try {
            return _initDriver(server, port, browser, url, context, timeout);
        } catch (Exception se) {
            PSLogger.error("Cannot init new rc-driver : " + se.getMessage()); // second iteration:
            PSLogger.saveFull();
            if (SeleniumDriverFactory.getNumberOfDrivers() == 0)
                LocalServerUtils.killBrowsers();
            return _initDriver(server, port, browser, url, context, timeout);
        }
    }

    /**
     * Setup Selenium, start and open browser
     *
     * @param server
     * @param port
     * @param browser
     * @param url
     * @param context
     * @return
     */
    static synchronized SeleniumRCDriver _initDriver(String server, int port, BrowserTypes browser, String url, String context, long timeout) {
        TimerWaiter.waitTime(SERVER_TIMEOUT_FOR_START);
        PSLogger.info("Selenium rc driver should be initialized.");
        browser.setRCDriver();
        SeleniumServer seleniumServer = null;
        if (isLocalServerSpecified(server)) {
            port = getAvailableServerPort(port > 0 ? port : DEFAULT_PORT);
            seleniumServer = initServer(port, browser, (int) (timeout / 1000));
        }
        PSLogger.info("Parameters for selenium have passed. Parameters are [server: " + server + "; port:" + port
                + "; browser:" + browser.getBrowser() + " url:" + url + "; context:" + context + "]");
        SeleniumRCDriver seleniumDriver = new SeleniumRCDriver(browser, server, port, url, timeout);
        seleniumDriver.theServer = seleniumServer;
        seleniumDriver.start();
        seleniumDriver.setTimeout(String.valueOf(timeout * 2));
        seleniumDriver.deleteAllVisibleCookies();
        // open domain:
        seleniumDriver.openWithoutCheckingResultAfter(url);
        try {
            seleniumDriver.windowMaximize();
            //seleniumDriver.allowNativeXpath("true");
        } catch (Exception e) {
            throw new SeleniumException(e);
        }
        if (context != null) {
            if (!browser.isFF()) // hotfix for ie and safari (to avoid errors like 'XHR ERROR'):
                seleniumDriver.openWithoutCheckingResultAfter(context);
            else
                seleniumDriver.open(context);
        }
        seleniumDriver.baseUrl = url + (context != null ? context : ""); // hotfix
        seleniumDriver.setTimeout(String.valueOf(timeout));
        return seleniumDriver;
    }

    private static boolean isLocalServerSpecified(String url) {
        return url == null || url.equalsIgnoreCase("localhost");
    }

    /**
     * main method for initDriver server
     *
     * @param - server port
     */
    private static SeleniumServer initServer(int port, BrowserTypes browser, int _timeout) {
        PSLogger.debug2("Try to init server, port # " + port);
        RemoteControlConfiguration configuration = new RemoteControlConfiguration();
        configuration.setAvoidProxy(true);
        configuration.setSingleWindow(false);
        configuration.setTimeoutInSeconds(SERVER_TIMEOUT + _timeout);
        configuration.setDebugMode(false);
        configuration.setEnsureCleanSession(true);
        configuration.trustAllSSLCertificates();
        //configuration.setLogOutFile(new File(SELENIUM_SERVER_LOG_FILE));
        if (browser.getProfile() != null) {
            PSLogger.debug2("Use profile " + browser.getProfile());
            configuration.setFirefoxProfileTemplate(new File(browser.getProfile()));
        }
        for (int i = 0; i < SERVER_START_ITERATION_NUM; i++) {
            try {
                return initServer(configuration, port++);
            } catch (BindException e) {
                PSLogger.warn(e.getMessage());
            } catch (Exception e) {
                throw new SeleniumException(e);
            }
        }
        throw new SeleniumException("Can't init server");
    }

    private static SeleniumServer initServer(RemoteControlConfiguration configuration, int port) throws Exception {
        PSLogger.debug2("Start local selenium server, port # " + port);
        configuration.setPort(port);
        SeleniumServer theServer = new SeleniumServer(configuration);
        theServer.start();
        lastPort = port;
        java.util.logging.Logger.getLogger("org").setLevel(java.util.logging.Level.OFF);
        return theServer;
    }

    public void start() {
        if (theServer != null && !theServer.getServer().isStarted()) {
            PSLogger.debug2("Selenium server is not running, start it");
            try {
                theServer.start();
            } catch (Exception e) {
                throw new SeleniumException(e);
            }
        }
        super.start();
    }


    public void _stop() {
        PSLogger.debug2("stop this selenium rc client (" + this + ")");
        try {
            super.stop();
        } catch (SeleniumException ex) {
            // there was exception for safari: class com.thoughtworks.selenium.SeleniumException Internal Server Error
            PSLogger.warn(this + ".stop: " + ex);
        }
        if (theServer != null) {
            PSLogger.debug2("stop selenium server on port " + theServer.getPort());
            theServer.stop();
            long t = System.currentTimeMillis();
            while (LocalServerUtils.isServerPortBusy(theServer.getPort())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                if (System.currentTimeMillis() - t > SERVER_TIMEOUT_FOR_STOP) {
                    PSLogger.warn("Can't stop server during " + SERVER_TIMEOUT_FOR_STOP + " ms");
                    break;
                }
            }
        }
        PSLogger.saveFull();
    }

    /**
     * getAvailableServerPort
     *
     * @return int port
     */
    private static int getAvailableServerPort(int fromPort) {
        PSLogger.debug2("Get available server port");
        if (lastPort != 0) fromPort = lastPort + 1;
        int toPort = fromPort + PORT_RANGE;
        for (int port = fromPort; port < toPort; port++) {
            if (LocalServerUtils.isServerPortBusy(port)) {
                PSLogger.debug2("Port #" + port + " is busy");
            } else {
                return port;
            }
        }
        throw new SeleniumException("Couldn't find free port for Selenium server between " +
                fromPort + " and " + toPort);
    }


    /**
     * Stop selenium and close browser
     */
    public synchronized void stop() {
        PSLogger.info("Stop last selenium driver");
        theServer.getConfiguration().setTimeoutInSeconds(SERVER_TIMEOUT_FOR_STOP);
        try {
            _stop();
        } catch (Exception e) {
            PSLogger.error(this + ":" + e.getMessage());
            PSLogger.fatal(e);
        }
    }

    /*
     protected void waitForAjax() throws InterruptedException {
         selenium.waitForCondition("selenium.browserbot.getCurrentWindow().dojo.io.XMLHTTPTransport.inFlight.length == 0;", DEFAULT_WAIT_PERIOD);
     }
     */

    private void superOpen(String url, boolean skip) {
        PSLogger.debug2("Open url '" + url + "'");
        if (skip && getLocation().equals(url)) {
            PSLogger.debug2("skip opening");
        } else {
            try {
                PSLogger.debug2(browser);
                super.open(url);
            } catch (SeleniumException e) {
                if (!e.getMessage().contains(SELENIUM_EXCEPTION_TIMEOUT))
                    throw e;
                // press Enter to avoid popup blocking in next testcase
                // (example of popup: 'are you sure you want to navigate away from this page'):
                if (theServer != null)
                    LocalServerUtils.enter();
                //now should be waitForPageToLoad?
            }
        }
        String confirm = getConfirmation();
        if (confirm != null) {
            PSLogger.debug2("Confirmation: " + confirm);
        }
    }

    public void open(String url) {
        open(url, true);
    }

    public void open(final String url, final boolean skipIfOpen) {
        final SeleniumException[] exceptions = new SeleniumException[1];
        Thread thread = new Thread() {
            public void run() {
                try {
                    superOpen(url, skipIfOpen);
                } catch (SeleniumException e) {
                    exceptions[0] = new SeleniumException(e);
                }
            }
        };
        SeleniumDriverFactory.checkForPopup(getTimeout() / 2, thread, exceptions[0],
                "can't open " + url + (exceptions[0] != null ? " : " + exceptions[0].getMessage() : ""));
    }


    /**
     * do open without checking result page (according changes in Selenium RC 1.0.3)
     * i.e. ignore errors like 'Response_Code = 503 Error_Message = Service Temporarily Unavailable'
     *
     * @param url - url to open
     */
    public void openWithoutCheckingResultAfter(final String url) {
        PSLogger.debug2("Open with ignore response code " + url);
        open(url, "true");
    }

    static boolean isCriticalException(Throwable e) {
        if (!(e instanceof SeleniumException) && !(e instanceof RuntimeException)) return false;
        String msg = e.getMessage();
        if (msg == null) return false;
        if (msg.contains(SELENIUM_EXCEPTION_FRAME_CLOSED)
                || msg.contains(SELENIUM_EXCEPTION_SERVER)
                || msg.contains(SELENIUM_PERMISSION_DENIED)
                || msg.contains(SELENIUM_EXCEPTION_OUT_OF_MEMORY)
                || msg.contains(SELENIUM_EXCEPTION_UNKNOWN_RESULT)
                || msg.contains(SELENIUM_EXCEPTION_INVALID_ARGUMENT)) {
            PSLogger.debug2("Set driver to null");
            return true;
        }
        return false;
    }

    public String getConfirmation() {
        if (super.isConfirmationPresent()) {
            return super.getConfirmation();
        }
        return null;
    }

    public void selectFrame(String locator) {
        if (locator == null) return;
        try {
            if (locator.startsWith("relative=") || isElementPresent(locator)) {
                if (browser.isIE()) {
                    waitForReady();
                }
                PSLogger.debug2("selectFrame(" + locator + ")");
                super.selectFrame(locator);
                frame = locator;
            } else {
                PSLogger.debug2("Can't find frame " + locator);
            }
        } catch (SeleniumException e) {
            PSLogger.warn("selectFrame : " + e.getMessage());
        }
    }

    public String getCurrentFrame() {
        return frame;
    }


    public void click(String locator) {
        PSLogger.debug2("click(" + locator + ")");
        super.click(locator);
        SeleniumDriverFactory.lastHandling = System.currentTimeMillis();
    }


    public void waitForFrameToLoad(String frameAddress, String timeout) {
        PSLogger.debug2("waitForFrameToLoad " + frameAddress + " " + timeout);
        try {
            super.waitForFrameToLoad(frameAddress, timeout);
        } catch (SeleniumException e) {
            if (isCriticalException(e)) throw e;
            PSLogger.warn("waitForFrameToLoad:" + e.getMessage());
        }
    }

    public void waitForFrameToLoad(String loc) {
        waitForFrameToLoad(loc, timeout);
    }

    @Override
    public long getTimeout() {
        return Long.parseLong(timeout);
    }

    public void waitForPageToLoad() {
        waitForPageToLoad(timeout);
    }

    public void superWaitForPageToLoad(String timeout) {
        super.waitForPageToLoad(timeout);
    }

    public void waitForPageToLoad(String timeout) {
        PSLogger.debug2("waitForPageToLoad " + timeout);
        waitForPageToLoad(Long.parseLong(timeout));
        PSLogger.save(this);
    }

    private void waitForPageToLoad(long timeout) {
        PSLogger.debug2("waitForPageToLoad " + timeout + " " + timeout);
        try {
            superWaitForPageToLoad(String.valueOf(timeout));
        } catch (SeleniumException e) {
            if (isCriticalException(e)) throw e;
            waitForReady();
            if (!browser.isSafari()) {
                boolean complete = false;
                try {
                    complete = super.getEval("document.readyState").equals("complete");
                } catch (SeleniumException e2) {
                    PSLogger.warn("getEval : " + e2.getMessage());
                }
                if (!complete)
                    PSLogger.error("waitForPageToLoad:" + e.getMessage());
            } else {
                PSLogger.warn("waitForPageToLoad:" + e.getMessage());
            }
        }
        if (browser.isIE()) {
            waitForReady(); // to do. its debug.
        }
    }

    public void waitForReady() {
        long s = System.currentTimeMillis();
        try {
            super.waitForCondition("document.readyState == 'complete'", timeout);
            PSLogger.debug("waitForReady : " + (System.currentTimeMillis() - s) + " ms");
        } catch (SeleniumException e) {
            PSLogger.warn("waitForReady(" + (System.currentTimeMillis() - s) + " ms) : " + e.getMessage());
        }

    }

    /**
     * todo: hotfix, sometimes selenium returns incorrect exception, so check 2 times
     *
     * @param locator
     * @return
     */
    public boolean isElementPresent(String locator) {
        try {
            return super.isElementPresent(locator);
        } catch (RuntimeException e) {
            if (isCriticalException(e)) throw e;
            PSLogger.warn(e.getMessage());
            return false;
        }
    }

    public boolean isVisible(String locator) {
        try {
            return super.isVisible(locator);
        } catch (SeleniumException e) {
            PSLogger.warn("isVisible(" + locator + ") : " + e.getMessage());
            return false;
        }
    }


    public String getEval(String script) {
        String res = SeleniumDriverFactory.doGetEval(this, script);
        if (res != null) return res;
        try {
            return super.getEval(script);
        } catch (SeleniumException e) {
            if (!getType().isIE(9))
                throw e;
            PSLogger.warn("getEval(" + script + "): " + e.getMessage());
        }
        return null;
    }

    public void type(String locator, String value) {
        PSLogger.debug2("type(" + locator + "," + value + ")");
        super.type(locator, value);
    }

    public void attachFile(String locator, String value) {
        SeleniumDriverFactory.attachFile(this, locator, value);
    }

    /**
     * @return Array with x-y-w-h coordinates of element related to <body/>
     */
    public final Integer[] getCoordinates(String locator) {
        return SeleniumDriverFactory.getCoordinates(this, locator);
    }


    public String getValue(String locator) {
        PSLogger.debug2("getValue(" + locator + ")");
        return super.getValue(locator);
    }

    public String getText(String locator) {
        PSLogger.debug2("getText(" + locator + ")");
        return superGetText(locator);
    }

    public void focus(String locator) {
        PSLogger.debug2("focus(" + locator + ")");
        super.focus(locator);
    }

    public String superGetText(String locator) {
        try {
            return super.getText(locator);
        } catch (SeleniumException e) {
            PSLogger.warn(e);
            return "";
        }
    }

    public String superGetValue(String locator) {
        return super.getValue(locator);
    }

    public String getAttribute(String locator, String attributeName) {
        locator += "@" + attributeName;
        PSLogger.debug2("getAttribute(" + locator + ")");
        return getAttribute(locator);
    }

    public String getAttribute(String locator) {
        try {
            return super.getAttribute(locator);
        } catch (SeleniumException e) {
            return null;
        }
    }

    public void dragAndDropToObject(String locatorOfObjectToBeDragged, String locatorOfDragDestinationObject) {
        PSLogger.debug2("DragAndDrop from " + locatorOfObjectToBeDragged + " to " + locatorOfDragDestinationObject);
        super.dragAndDropToObject(locatorOfObjectToBeDragged, locatorOfDragDestinationObject);
    }


    /**
     * @param locSrc - src element locator
     * @param locDst - dst element locator
     */
    public void dragAndDropToElement(String locSrc, String locDst) {
        SeleniumDriverFactory.dragAndDropToElement(this, locSrc, locDst);
    }

    /**
     * experimental
     *
     * @param locSrc
     * @param diffX
     * @param diffY
     */
    public void dragAndDropToCoords(String locSrc, int diffX, int diffY) {
        String diff = diffX + "," + diffY;
        PSLogger.debug2("Move on " + diff + " pixels");
        mouseDown(locSrc);
        mouseMoveAt(locSrc, diff);
        mouseUp(locSrc);
    }

    public String getLocation() {
        try {
            return super.getLocation();
        } catch (SeleniumException e) {
            PSLogger.warn("getLocation:" + e);
            return "";
        }
    }

    public void captureScreenshot(String file) {
        try { // this method is called from PSTestListener, so try-cath
            if (browser.isFF()) {
                super.captureEntirePageScreenshot(file, BACKGROUND_TO_SAVE_FOR_FF_CAPTURE_SCREENSHOOT);
                LocalServerUtils.convertFromDefaultImage(file);
            } else {
                if (theServer == null) {
                    PSLogger.warn("captureScreenshot is not supported for external machine");
                } else {
                    super.captureScreenshot(file);
                    LocalServerUtils.convertFromDefaultImage(file);
                }
            }
        } catch (SeleniumException e) {
            PSLogger.fatal(e);
        }
    }

    public String getBaseUrl() {
        return baseUrl.replace(HomePageLocators.URL.getLocator(), ""); // workaround for #88766
    }

    public void captureScreenshotAsHtml(String file) {
        SeleniumDriverFactory.captureScreenshotAsHtml(this, file);
    }

    public void check(String locator) {
        try {
            super.check(locator);
        } catch (SeleniumException e) {
            throw e;
        }
    }

    /**
     * getDocument; for future, requred jaxen-1.1.1.jar, dom4j-1.6.1.jar, dom4j-1.6.1.jar
     *
     * @return dom4j Document
     */
    public Document getDocument() {
        return getDocument("relative=top");
    }

    public Document getFrameDocument(int index) {
        String frameLocator = "dom=window.frames[" + index + "].document.body.outerHTML";
        Document result = null;
        try {
            HtmlCleaner cleaner = new HtmlCleaner(getEval(frameLocator));
            cleaner.clean();
            SAXReader reader = new SAXReader(DOMDocumentFactory.getInstance());
            result = reader.read(new StringReader(cleaner.getCompactXmlAsString()));
        } catch (Exception e) {
            PSLogger.warn("getDocument " + e.getMessage());
        }
        return result;
    }

    public Document getDocument(String frameLocator) {
        if (frameLocator != null) {
            selectFrame(frameLocator);
        }
        // todo: under ie there is an error(2.28.0, ie8):
        //	class com.thoughtworks.selenium.SeleniumException ERROR: Command execution failure.
        // Please search the user group at https://groups.google.com/forum/#!forum/selenium-users for error details from the log window.
        // The error message is: Could not get the innerHTML property. Not enough storage is available to complete this operation.
        // todo: not enough memory?
        try {
            return SeleniumDriverFactory.getDocument(getHtmlSource());
        } catch (SeleniumException se) {
            PSLogger.fatal(se);
            System.gc();
            TimerWaiter.waitTime(5000);
            try {
                return SeleniumDriverFactory.getDocument(getHtmlSource());
            } catch (SeleniumException _se) {
                PSSkipException.skip(_se);
                return null;
            }
        }
    }


    public void mouseDown(String locator) {
        PSLogger.debug2("mouseDown(" + locator + ")");
        super.mouseDown(locator);
    }

    public void mouseOver(String locator) {
        PSLogger.debug2("mouseOver(" + locator + ")");
        super.mouseOver(locator);
    }

    public void mouseUp(String locator) {
        PSLogger.debug2("mouseUp(" + locator + ")");
        super.mouseUp(locator);
    }

    public void mouseDownAt(String locator, String coordString) {
        PSLogger.debug2("mouseDownAt(" + locator + "," + coordString + ")");
        super.mouseDownAt(locator, coordString);
    }

    public void mouseUpAt(String locator, String coordString) {
        PSLogger.debug2("mouseUpAt(" + locator + "," + coordString + ")");
        super.mouseUpAt(locator, coordString);
    }

    public void mouseMoveAt(String locator, String coordString) {
        PSLogger.debug2("mouseMoveAt(" + locator + "," + coordString + ")");
        super.mouseMoveAt(locator, coordString);
    }

    public void mouseMove(String locator) {
        PSLogger.debug2("mouseMove(" + locator + ")");
        super.mouseMove(locator);
    }

    public synchronized void assignId(String locator, String id) {
        PSLogger.debug2("assignId(" + locator + ", " + id + ")");
        super.assignId(locator, id);
    }

    public void mouseOut(String locator) {
        PSLogger.debug2("mouseOut(" + locator + ")");
        super.mouseOut(locator);
    }

    public BrowserTypes getType() {
        return browser;
    }

    public void runScript(String script) {
        PSLogger.debug2("runScript(" + script + ")");
        super.runScript(script);
    }

}
