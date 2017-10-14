package com.powersteeringsoftware.libs.core;

import com.gargoylesoftware.htmlunit.WebClient;
import com.powersteeringsoftware.libs.enums.page_locators.HomePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.Locators;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.webdriven.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.dom4j.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getVersion;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.12.10
 * Time: 21:32
 */
public class SeleniumWebDriver extends WebDriverBackedSelenium implements SeleniumDriver {
    private static final String FIREFOX_BIN = "webdriver.firefox.bin";
    private static final String FIREFOX_PROFILE = "webdriver.firefox.profile";
    private static final String GOOGLE_CHROME_BIN = "webdriver.chrome.bin";
    private static final String GOOGLE_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String IE_CHROME_DRIVER = "webdriver.ie.driver";
    private static final int GOOGLE_CHROME_DRIVER_PORT = 9515;
    private static final long GOOGLE_CHROME_DRIVER_START_TIMEOUT = 30000;
    private static final String GOOGLE_CHROME_DRIVER_URL = "http://localhost:" + GOOGLE_CHROME_DRIVER_PORT;
    private static final String GOOGLE_CHROME_REP_PROFILE = "webdriver.reap_profile";
    private static final int DEBUG_INIT_ATTEMPTS = 2;
    public static final long WEB_DRIVER_STOP_TIMEOUT = 30000; // ms
    public static final long WEB_DRIVER_START_TIMEOUT = 60000; // s

    protected WebDriver webDriver;
    private String baseUrl;
    private BrowserTypes browser;
    private long timeout;
    private Integer port;
    boolean doWaitForReady;
    private static final int FRAMES_DEEP = 5;
    private List<Frame> frames = new ArrayList<Frame>();

    SeleniumWebDriver(WebDriver baseDriver, BrowserTypes br, String baseUrl, long timeout) {
        super(baseDriver, baseUrl);
        webDriver = baseDriver;
        browser = br;
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        setTimeout(String.valueOf(timeout));
        doWaitForReady = true;
        if (SeleniumDriverFactory.getVersion().isDragAndDropBroken()) {
            fixCommandProcessor(SeleniumDriverFactory.getWebDriverFireEventAtScript(), "mouseMoveAt", "mousemove");
            fixCommandProcessor(SeleniumDriverFactory.getWebDriverFireEventAtScript(), "mouseDownAt", "mousedown");
            fixCommandProcessor(SeleniumDriverFactory.getWebDriverFireEventAtScript(), "mouseUpAt", "mouseup");
        }
    }


    private static WebDriver createWebDriver(BrowserTypes br, int attempts, long timeout) {
        SeleniumException e = null;
        for (int i = 0; i < attempts; i++) {
            try {
                return createWebDriver(br, timeout);
            } catch (SeleniumException ex) {
                PSLogger.warn("Can't init web-driver in iteration #" + (i + 1) +
                        ": " + ex.getMessage());
                e = ex;
                try {
                    Thread.sleep(WEB_DRIVER_STOP_TIMEOUT);
                } catch (InterruptedException e1) {
                    // ignore.
                }
            }
        }
        throw e;
    }

    private static WebDriver createWebDriver(BrowserTypes browser, long timeout) {
        long start = System.currentTimeMillis();
        InitWebDriverThread thread = new InitWebDriverThread(browser, timeout);
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (System.currentTimeMillis() - start > WEB_DRIVER_START_TIMEOUT) {
                String msg;
                PSLogger.fatal(msg = "Can't init web-driver during " + WEB_DRIVER_START_TIMEOUT + " ms");
                throw new SeleniumException(msg);
            }
        }
        if (thread.ex != null) {
            PSLogger.fatal("Exception while starting driver : " + thread.ex.getMessage());
            throw new SeleniumException(thread.ex);
        }
        return thread.driver;
    }

    private static class HtmlUnitDriverFF extends HtmlUnitDriver {

        public WebClient getWebClient() {
            return super.getWebClient();
        }
    }


    private static class InitWebDriverThread extends Thread {
        private WebDriver driver;
        private Exception ex;
        private BrowserTypes br;
        private int timeout;

        public InitWebDriverThread(BrowserTypes browser, long t) {
            br = browser;
            timeout = (int) t;
        }

        public void run() {
            try {
                if (br.isIE()) {
                    if (br.getDriver() != null) {
                        PSLogger.debug("Set ie driver to " + br.getDriver());
                        System.setProperty(IE_CHROME_DRIVER, br.getDriver().getPath());
                    }
                    driver = new InternetExplorerDriver();
                } else if (br.isFF()) {
                    if (br.getPath() == null) throw new NullPointerException("Path to browser should be specified");
                    PSLogger.debug2("Set path to " + br.getPath());
                    System.setProperty(FIREFOX_BIN, br.getPath());
                    FirefoxProfile firefoxProfile = null;
                    if (br.getProfile() != null) {
                        PSLogger.debug("Use firefox  profile " + br.getProfile());
                        System.setProperty(FIREFOX_PROFILE, br.getProfile());
                        firefoxProfile = new FirefoxProfile(new File(br.getProfile()));
                    } else {
                        firefoxProfile = new FirefoxProfile();
                    }
                    firefoxProfile.setAcceptUntrustedCertificates(true);
                    if (br.isFFGreaterThan(280)) {
                        firefoxProfile.setPreference("network.http.response.timeout", timeout);
                    }
                    driver = new FirefoxDriver(firefoxProfile);
                } else if (br.isGoogleChrome()) {
                    if (br.getPath() == null) throw new NullPointerException("Path to browser should be specified");
                    PSLogger.debug2("Set path to " + br.getPath());
                    System.setProperty(GOOGLE_CHROME_BIN, br.getPath());
                    if (br.getDriver() != null) {
                        PSLogger.debug("Set chrome driver to " + br.getDriver());
                        System.setProperty(GOOGLE_CHROME_DRIVER, br.getDriver().getPath());
                        //TODO: this is workaround
                        startDriverProcess(br.getDriver(), GOOGLE_CHROME_DRIVER_PORT, GOOGLE_CHROME_DRIVER_START_TIMEOUT);
                        driver = new RemoteWebDriver(new URL(GOOGLE_CHROME_DRIVER_URL), DesiredCapabilities.chrome());
                    } else {
                        driver = new ChromeDriver();
                    }
                } else if (br == null || br.isZero()) {
                    HtmlUnitDriver dr = new HtmlUnitDriverFF();
                    dr.setJavascriptEnabled(true);
                    driver = dr;
                } else {
                    //todo: this is for safari, opera, android, iphone etc
                }
            } catch (Exception e) {
                ex = e;
            }

        }

        private static void startDriverProcess(BrowserTypes.Driver driver, int port, long timeout) throws Exception {
            if (driver == null) return;
            if (LocalServerUtils.getLocalServer().isProcessPresent(driver.getName())) {
                PSLogger.info("Process for driver " + driver + " is already running");
                return;
            }
            long start = System.currentTimeMillis();
            LocalServerUtils.getLocalServer().exec(driver.getPath(), String.valueOf(port));
            long step = timeout / 5;
            do {
                PSLogger.debug2("Wait for " + driver + " " + step + " ms");
                Thread.sleep(step);
                if (System.currentTimeMillis() - start > timeout) {
                    throw new SeleniumException("Can't start driver '" + driver +
                            "' server during " + timeout + " ms");
                }
            } while (!LocalServerUtils.isServerPortBusy(port));

        }
    }


    static SeleniumWebDriver initDriver(BrowserTypes browser, String url, String context, long timeout) {
        PSLogger.info("Selenium web driver (" + browser.getBrowser() + ") should be initialized.");
        browser.setWebDriver();
        WebDriver driver = createWebDriver(browser, DEBUG_INIT_ATTEMPTS, timeout);
        if (!browser.isGoogleChrome())
            driver.manage().deleteAllCookies(); // delete cookies before
        PSLogger.info(driver + ".get(" + url + context + ")");
        driver.get(url + context);
        driver.manage().deleteAllCookies(); // delete cookies before
        SeleniumWebDriver seleniumDriver = new SeleniumWebDriver(driver, browser, url + context, timeout);
        try {
            if (!browser.isZero())
                seleniumDriver.windowMaximize();
            else {
                driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MICROSECONDS);
            }
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
            throw new SeleniumException(e);
        }
        if (browser.isGoogleChrome()) {
            try {
                Thread.sleep(WEB_DRIVER_STOP_TIMEOUT); // << todo: debug
            } catch (Exception ex) {
            }
        }
        return seleniumDriver;
    }

    private WebDriverCommandProcessor getCommandProcessor() {
        return (WebDriverCommandProcessor) commandProcessor;
    }

    private void fixCommandProcessor(final String script, final String type1, final String type2) {
        final JavascriptLibrary js = new JavascriptLibrary();
        String res = null;
        try {
            res = String.format("function() { return (%s).apply(null, arguments);}", LocalServerUtils.readFile(script));
        } catch (IOException e) {
            throw new SeleniumException(e);
        }
        final String fire = "return (" + res + ").apply(null, arguments);";
        ((WebDriverCommandProcessor) commandProcessor).addMethod(type1, new SeleneseCommand<Void>() {
                    @Override
                    protected Void handleSeleneseCommand(WebDriver driver, String locator, String value) {
                        WebElement element = findElement(locator);

                        js.executeScript(driver, fire, element, type2, value);

                        return null;
                    }
                }
        );
    }

    private HttpCommandExecutor getExecutor() {
        if (webDriver instanceof HtmlUnitDriver) return null;
        CommandExecutor ex = ((RemoteWebDriver) webDriver).getCommandExecutor();
        if (ex instanceof HttpCommandExecutor)
            return (HttpCommandExecutor) ex;
        return null;
    }

    private Integer getServerPort() {
        if (port != null) return port;
        HttpCommandExecutor executor = getExecutor();
        if (executor != null) return port = executor.getAddressOfRemoteServer().getPort();
        return null;
    }

    public void open(String url) {
        open(url, true);
    }

    public void open(String url, boolean skipIfOpen) {
        PSLogger.debug2("Open url '" + url + "'");
        if (skipIfOpen && getLocation().equals(url)) {
            PSLogger.debug2("skip opening");
            return;
        }
        super.open(url);
        waitForReady();
    }

    public void stop() {
        PSLogger.info("Close and stop last selenium web driver");
        final RuntimeException[] exceptions = new RuntimeException[3];
        Thread thread = new Thread() {
            public void run() {
                Integer port = SeleniumWebDriver.this.getServerPort();
                PSLogger.debug2("Server-Port is " + port);

                try { // its for google chrome:
                    PSLogger.debug2("close");
                    SeleniumWebDriver.super.close();
                } catch (Exception e) {
                    exceptions[0] = new RuntimeException(e);
                }
                try {
                    if (!browser.isZero())
                        sleep(WEB_DRIVER_STOP_TIMEOUT / 2); // debug for chrome 12
                } catch (InterruptedException e) {
                    //ignore
                }
                try { // its for google chrome:
                    PSLogger.debug2("stop");
                    SeleniumWebDriver.super.stop();
                } catch (Exception e) {
                    exceptions[1] = new RuntimeException(e);
                }
                try {
                    if (!browser.isZero())
                        sleep(WEB_DRIVER_STOP_TIMEOUT / 2);
                } catch (InterruptedException e) {
                    //ignore
                }
                if (port != null && LocalServerUtils.isServerPortBusy(port)) {
                    PSLogger.warn("Server Port is busy : " + port);
                }
            }
        };
        long t = System.currentTimeMillis();
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (System.currentTimeMillis() - t > timeout) {
                PSLogger.warn("Can't stop server after " + timeout + " ms");
                break;
            }
        }
        for (Exception e : exceptions) {
            if (e != null)
                PSLogger.fatal(e);
        }
    }

    public void windowMaximize() {
        webDriver.manage().window().maximize();
    }

    public void selectFrame(String locator) {
        if (locator == null) return;
        try {
            selectFrameInThread(locator);
        } catch (SeleniumException e) {
            PSLogger.warn(e);
        }
    }

    private void selectFrameInThread(final String loc) {
        final SeleniumException[] exceptions = new SeleniumException[1];
        Thread thread = new Thread() {
            public void run() {
                try {
                    // its for google chrome:
                    if (browser.isChromeOrSafari()) {
                        sleep(500);
                    }
                    Frame fr = findFrame(loc);
                    PSLogger.debug2("selectFrame(" + fr.frameLoc + ")");
                    if (fr.top) {
                        webDriver.switchTo().defaultContent();
                    } else if (fr.we != null) {
                        webDriver.switchTo().frame(fr.we);
                    } else if (fr.index != 0) {
                        webDriver.switchTo().frame(fr.index);
                    } else {
                        throw new SeleniumException("Can't find frame " + fr.loc);
                    }
                    addToFrames(fr);
                } catch (Exception e) {
                    exceptions[0] = new SeleniumException(e);
                }
            }
        };
        long t = System.currentTimeMillis();
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (System.currentTimeMillis() - t > timeout) {
                String msg;
                PSLogger.warn(msg = "Can't select frame " + loc + " after " + timeout + " ms");
                throw new RuntimeException(msg);
            }
        }
        if (exceptions[0] != null)
            throw exceptions[0];
    }


    private class Frame {
        private WebElement we;
        private String loc;
        private String frameLoc;
        private int index;
        private boolean parent;
        private boolean top;
    }

    /**
     * @param locator
     * @return
     */
    private Frame findFrame(String loc) {
        Frame fr = new Frame();
        fr.loc = loc;
        fr.frameLoc = loc;
        if (loc.startsWith("relative=")) {
            Frame previous;
            if (loc.endsWith("up") && (previous = getLastFrame()) != null) { // if relative=up, choose previous frame
                fr.frameLoc = previous.frameLoc;
                fr.we = previous.we;
                fr.top = previous.top;
                fr.parent = true;
            } else { // ignore relative=up. unsupported.
                fr.top = true;
                fr.frameLoc = "relative=top";
            }
        } else if (Locators.isFrame(loc)) {
            String res = Locators.frame2any(loc);
            if (res.matches("\\d+")) {
                fr.index = Integer.parseInt(res);
            } else {
                fr.frameLoc = res;
            }
        }
        if (!fr.top && !fr.parent)
            fr.we = findElement(fr.frameLoc);
        if (fr.we != null && !Locators.isSimpleLocator(fr.frameLoc)) {
            try {
                String name = fr.we.getAttribute("name");
                String id = fr.we.getAttribute("id");
                if (name != null && !name.isEmpty()) {
                    fr.frameLoc = name;
                } else if (id != null && !id.isEmpty()) {
                    fr.frameLoc = id;
                }
            } catch (Exception e) {
                PSLogger.warn(getClass().getSimpleName() + ".findFrame: " + e.getMessage());
            }

        }
        return fr;
    }

    public String getCurrentFrame() {
        Frame fr = getLastFrame();
        if (fr == null) return "relative=top";
        return fr.loc;
    }

    private Frame getLastFrame() {
        if (frames.size() == 0) return null;
        return frames.get(frames.size() - 1);
    }

    private void addToFrames(Frame fr) {
        if (fr.parent) return;
        if (fr.top) {
            frames.clear();
            frames.add(fr);
            return;
        }
        frames.add(fr);
        if (frames.size() > FRAMES_DEEP) frames.remove(0);
    }

    public BrowserTypes getType() {
        return browser;
    }

    public String getBaseUrl() {
        return baseUrl.replace(HomePageLocators.URL.getLocator(), ""); // workaround for #88766
    }

    public void captureScreenshotAsHtml(String file) {
        SeleniumDriverFactory.captureScreenshotAsHtml(this, file);
    }

    public void captureScreenshot(String file) {
        if (webDriver instanceof TakesScreenshot && !browser.isGoogleChrome()) {
            File scrFile = null;
            try {
                scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            } catch (Throwable e) {
                PSLogger.fatal(getClass().getSimpleName() + ".captureScreenshot", e);
                return;
            }
            try {
                FileUtils.copyFile(scrFile, new File(file));
                LocalServerUtils.convertFromDefaultImage(file);
            } catch (IOException e) {
                PSLogger.fatal(getClass().getSimpleName() + ".captureScreenshot", e);
            }
        } else {
            LocalServerUtils.makeScreenCapture(file);
        }
    }

    public Document getDocument(String frameLocator) {
        if (frameLocator != null)
            selectFrame(frameLocator);
        Document doc = SeleniumDriverFactory.getDocument(webDriver.getPageSource());
        documentSetNamespaces(doc.getRootElement().content(), Namespace.NO_NAMESPACE);
        return doc;
    }

    public Document getDocument() {
        if (getType().isGoogleChromeGreaterThan(10)) {
            // todo: this is for investigating.
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // ignore.
            }
        }
        return getDocument("relative=top");
    }


    /**
     * Sets the namespace of the element to the given namespace
     */
    private static void documentSetNamespace(Element elem, Namespace ns) {
        elem.setQName(QName.get(elem.getName(), ns,
                elem.getQualifiedName()));
    }

    /**
     * Recursively sets the namespace of the element and all its children.
     */
    private static void documentSetNamespaces(Element element, Namespace ns) {
        documentSetNamespace(element, ns);
        documentSetNamespaces(element.content(), ns);
    }

    /**
     * Recursively sets the namespace of the List and all children if the
     * current namespace is match
     */
    private static void documentSetNamespaces(List list, Namespace ns) {
        for (Object o : list) {
            Node n = (Node) o;
            if (n.getNodeType() == Node.ATTRIBUTE_NODE)
                ((Attribute) n).setNamespace(ns);
            if (n.getNodeType() == Node.ELEMENT_NODE)
                documentSetNamespaces((Element) n, ns);
        }
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


    public Integer[] getCoordinates(String locator) {
        return SeleniumDriverFactory.getCoordinates(this, locator);
    }

    public void dragAndDropToElement(String locSrc, String locDst) {
        SeleniumDriverFactory.dragAndDropToElement(this, locSrc, locDst);
    }

    public void dragAndDrop(String s, String s1) {
        try {
            super.dragAndDrop(s, s1);
        } catch (UnsupportedOperationException e) {
            // for selenium version 2.0b3
            if (browser.isGoogleChrome()) {
                PSSkipException.skip(e.getMessage());
            } else {
                throw e;
            }
        } catch (SeleniumException se) {
            // for selenium version 2.0rc3
            if (browser.isWebDriverFF(5)) {
                PSSkipException.skip(se.getMessage());
            } else {
                throw se;
            }
        }
    }

    @Override
    public void mouseMoveAt(String locator, String coords) {
        PSLogger.debug2("mouseMoveAt(" + locator + "," + coords + ")");
        //TODO:
       /* if (SeleniumDriverFactory.getVersion().isDragAndDropBroken(this)) {
            PSSkipException.skip(getClass().getSimpleName() + ".mouseMoveAt(" + locator + ", '" + coords + "'): doesn't work");
        }*/
        super.mouseMoveAt(locator, coords);
    }

    public void mouseDown(String locator) {
        PSLogger.debug2("mouseDown(" + locator + ")");
        super.mouseDown(locator);
    }

    public void mouseMove(String locator) {
        PSLogger.debug2("mouseMove(" + locator + ")");
        super.mouseMove(locator);
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

    public synchronized void assignId(String locator, String id) {
        PSLogger.debug2("assignId(" + locator + ", " + id + ")");
        super.assignId(locator, id);
    }

    public void mouseOut(String locator) {
        PSLogger.debug2("mouseOut(" + locator + ")");
        super.mouseOut(locator);
    }

    public String getEval(String script) {
        String res = SeleniumDriverFactory.doGetEval(this, script);
        if (res != null) return res;
        try {
            return super.getEval(script);
        } catch (SeleniumException e) {
            throw e;
        }
    }

    public void waitForPageToLoad(String timeout) {
        try {
            PSLogger.debug2("waitForPageToLoad(" + timeout + ")");
            _waitForPageToLoad(Long.parseLong(timeout));
            // hotfix for web-driver (chrome, safari)
        } catch (Exception e) {
            PSLogger.warn("waitForPageToLoad : " + e.getMessage());
            // ignore exception
        } finally {
            //if (browser.isGoogleChrome() || browser.isSafari()) {
            waitForReady();
            //}
        }
    }

    private void _waitForPageToLoad(final long timeout) {
        final RuntimeException[] exceptions = new RuntimeException[1];
        Thread thread = new Thread() {
            public void run() {
                try {
                    SeleniumWebDriver.super.waitForPageToLoad(String.valueOf(timeout));
                } catch (Exception e) {
                    exceptions[0] = new RuntimeException(e);
                }
            }
        };
        long t = System.currentTimeMillis();
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (System.currentTimeMillis() - t > timeout) {
                PSLogger.warn("Page loading after " + timeout + " ms");
                throw new SeleniumException("Page loading after " + timeout + " ms");
            }
        }
        if (exceptions[0] != null) {
            throw exceptions[0];
        }
    }

    public void waitForPageToLoad() {
        waitForPageToLoad(String.valueOf(timeout));
    }

    public void waitForFrameToLoad(String loc) {
        Frame res = findFrame(loc); // WebDriver does not support this functionality
        SeleniumDriverFactory.waitForFrameToLoad(this, res.frameLoc);
        waitForReady();
    }

    public WebElement findElement(String loc) {
        ElementFinder finder;
        try {
            //todo: due to selenium-2.0b2 changes:
            finder = new ElementFinder(new JavascriptLibrary());
            //finder = new ElementFinder();
            return finder.findElement(webDriver, loc);
        } catch (SeleniumException se) {
            return null;
        }
    }

    public void select(String selector, String option) {
        PSLogger.debug2("select(" + selector + "," + option + ")");
        if (getVersion().isSelectInputBroken(this)) {
            throw new SkipException("Method select doesn't work correct for this selenium and browser.");
        }
        super.select(selector, option);
        PSLogger.debug2("after select(" + selector + "," + option + ")");
    }

    public void click(String loc) {
        PSLogger.debug2("click(" + loc + ")");
        long s = System.currentTimeMillis();
        try {
            super.click(loc);
        } catch (Exception e) {
            PSLogger.warn(e.getMessage() + ": time " + ((System.currentTimeMillis() - s) / 1000F) + "s");
            throw new SeleniumException(e);
        }
        SeleniumDriverFactory.lastHandling = System.currentTimeMillis();
    }

    public void check(String loc) {
        PSLogger.debug2("check(" + loc + ")");
        super.check(loc);
        SeleniumDriverFactory.lastHandling = System.currentTimeMillis();
    }

    public boolean isElementPresent(String loc) {
        PrintStream errStream = getVersion().equals(SeleniumDriverFactory.Version._2_0_0) ? System.err : null;
        PrintStream outStream = getVersion().greater(SeleniumDriverFactory.Version._2_16_1) && browser.isZero() ? System.out : null;
        try {
            if (errStream != null)
                System.setErr(new PrintStream(new NullOutputStream()));
            if (outStream != null)
                System.setOut(new PrintStream(new NullOutputStream()));
            return super.isElementPresent(loc);
        } finally {
            if (errStream != null)
                System.setErr(errStream);
            if (outStream != null)
                System.setOut(outStream);
        }
    }

    public boolean isVisible(String loc) {
        PSLogger.debug2("isVisible(" + loc + ")");
        return super.isVisible(loc);
    }

    public void type(String loc, String txt) {
        PSLogger.debug2("type(" + loc + "," + txt + ")");
        super.type(loc, txt);
    }

    public String getText(String loc) {
        PSLogger.debug2("getText(" + loc + ")");
        try {
            return super.getText(loc);
        } catch (SeleniumException se) {
            PSLogger.warn(se);
            return "";
        }
    }

    public String getValue(String loc) {
        return super.getValue(loc);
    }

    public void runScript(String script) {
        PSLogger.debug2("runScript(" + script + ")");
        super.runScript(script);
    }

    public boolean isReady() {
        try {
            return browser.isZero() || Boolean.parseBoolean(super.getEval("document['readyState'] == 'complete'"));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isReady(int num, long time) {
        boolean res = isReady();
        for (int i = 0; i < num; i++) {
            if (res) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                }
                res &= isReady();
            } else return false;
        }
        return res;
    }

    public void waitForReady() {
        frames.clear();
        if (!doWaitForReady) return;
        PSLogger.debug2("waitForReady");
        long s1 = System.currentTimeMillis();
        long s2 = s1;
        while (!isReady(2, 100)) {
            s2 = System.currentTimeMillis();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ignore
            }
            if (s2 - s1 > timeout) {
                PSLogger.warn("Refresh. Timeout after " + timeout + " ms");
                return;
            }
        }
        PSLogger.debug2("waitForReady: " + (s2 - s1) + "ms");
    }

    public String superGetValue(String locator) {
        return getValue(locator);
    }

    public String superGetText(String locator) {
        return getText(locator);
    }

    public void attachFile(String locator, String value) {
        if (!browser.isIE()) {
            SeleniumDriverFactory.attachFile(this, locator, value);
            return;
        }
        if (browser.isIE(9)) {
            PSSkipException.skip("Not supported for " + browser.getName());
        }
        // attaching for ie:
        PSLogger.debug2("Web-driver attaching : " + locator + ", " + value);
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(value);
        try {
            Thread.sleep(2000); // some debug waiting
        } catch (Exception e) {
            //ignore
        }
    }

    @Override
    public long getTimeout() {
        return timeout;
    }

    public String[] getAllWindowNames() {
        try {
            return super.getAllWindowNames();
        } catch (UnsupportedOperationException u) {
            PSLogger.warn("getAllWindowNames:" + u.getClass().getSimpleName());
            return null;
        }
    }

    public String[] getAllWindowIds() {
        try {
            return super.getAllWindowIds();
        } catch (UnsupportedOperationException u) {
            PSLogger.warn("getAllWindowIds:" + u.getClass().getSimpleName());
            return null;
        }
    }

    public boolean isChecked(String loc) {
        try {
            return super.isChecked(loc);
        } catch (SeleniumException se) {
            PSLogger.warn("isChecked(" + loc + "):" + se.getMessage());
            return false;
        }
    }

    public void refresh() {
        super.refresh();
        /*
        WebClient client = null;
        if (webDriver instanceof HtmlUnitDriverFF36) { // hotfix for HtmlUnit Driver
            client = ((HtmlUnitDriverFF36) webDriver).getWebClient();
        }
        try {
            if (client != null) client).setThrowExceptionOnScriptError(false);
            super.refresh();
        } finally {
            if (client != null) client.setThrowExceptionOnScriptError(true);
        }
        */
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}

