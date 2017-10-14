package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 14.09.11
 * Time: 17:18
 */
public abstract class AbstractPage {

    protected String url;
    protected Document document;
    protected long lastPageLoadingTime;
    private SeleniumDriver driver;
    private static final String ILOCATORS_CLASS = "com.powersteeringsoftware.libs.enums.page_locators." + ILocatorable.LOCATOR_REPLACE_PATTERN + "Locators";
    private static final String ILOCATORS_URL = "URL";
    protected static boolean getDocumentOnOpen = true;

    public static void setGetDocumentOnOpen(boolean b) {
        getDocumentOnOpen = b;
    }

    protected void open(String url, boolean skipIfAlreadyOpen) {
        String baseUrl = getDriver().getBaseUrl();
        if (!url.startsWith(baseUrl))
            url = baseUrl + "/" + url;
        url = url.replaceAll("([^:])/+", "$1/").replace(":443/", "/");
        PSLogger.info("Open '" + url + "'");
        try {
            getDriver().open(url, skipIfAlreadyOpen);
        } catch (SeleniumException se) {
            if (se.getMessage().contains(SeleniumDriverFactory.SELENIUM_EXCEPTION_TIMEOUT)) {
                PSLogger.saveFull();
                throw new PSKnownIssueException(72196, se);
            }
            throw se;
        }

        if (!hasUrl() || !skipIfAlreadyOpen)
            setUrl();
        if (document == null || !skipIfAlreadyOpen) {
            document = null;
            if (getDocumentOnOpen)
                getDocument();
        }
    }

    protected void open(String url) {
        open(url, true);
    }

    public abstract Document getDocument();

    public String getUrl() {
        if (url == null) setUrl();
        return url;
    }

    public boolean hasUrl() {
        return url != null;
    }

    public static String getCurrentUrl() {
        String res = SeleniumDriverFactory.getDriver().getLocation();
        if (res.isEmpty()) res = null;
        if (res != null) {
            res = res.replaceAll("([^:])/+", "$1/").replace(":443/", "/") // ssl
                    .replaceAll(";jsessionid=[A-Za-z0-9]+", ""); // resin's cookie
        }
        return res;
    }

    public void setUrl() {
        url = getCurrentUrl();
    }

    public boolean isCurrentUrl() {
        return isCurrentUrl(true);
    }

    public boolean isCurrentUrl(boolean debug) {
        String u = getCurrentUrl();
        if (debug)
            PSLogger.debug("current url : " + u);
        return u.replaceAll("([^:])/+", "$1/").equals(url != null ? url.replaceAll("([^:])/+", "$1/") : null);
    }

    protected boolean checkUrl(ILocatorable url) {
        String actual = getUrl().replaceAll("([^:])/+", "$1/");
        String expected = (getDriver().getBaseUrl() + "/" + url.getLocator()).replaceAll("([^:])/+", "$1/");
        PSLogger.debug("Expected url: " + expected + ", actual url: " + actual);
        return actual.startsWith(expected);
    }

    public boolean checkUrl() {
        ILocatorable i = getILocatorableUrl();
        if (i != null)
            return checkUrl(i);
        return true;
    }

    public ILocatorable getILocatorableUrl() {
        try {
            Class cl = Class.forName(ILOCATORS_CLASS.replace(ILocatorable.LOCATOR_REPLACE_PATTERN, getClass().getSimpleName()));
            return (ILocatorable) Enum.valueOf(cl, ILOCATORS_URL);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public boolean reopen() {
        if (isCurrentUrl()) return false;
        open(url);
        return true;
    }

    public void refresh() {
        refresh(true);
    }

    protected void refresh(boolean getDoc) {
        PSLogger.info("Refresh page " + getUrl());
        try {
            SeleniumDriverFactory.refreshWithPopup();
        } catch (SeleniumException se) { // todo workaround
            PSLogger.error("Ignore this error: " + se.getMessage());
        }
        document = null;
        if (getDoc && getDocumentOnOpen)
            getDocument();
    }

    public void doRefresh(long timeout) {
        PSLogger.info("Do refresh page " + getUrl());
        long s = System.currentTimeMillis();
        try {
            getDriver().refresh();
            getDriver().waitForPageToLoad(String.valueOf(timeout));
        } catch (SeleniumException se) {
            PSLogger.warn("doRefresh " + ((System.currentTimeMillis() - s) / 1000F) + "s");
            throw se;
        }
        lastPageLoadingTime = System.currentTimeMillis() - s;
        document = null;
    }

    public void goBack(long timeout) {
        PSLogger.debug("Go back from '" + getUrl() + "'");
        getDriver().goBack();
        waitForPageToLoad(timeout);
    }

    public void waitForPageToLoad(long timeout) {
        long start = System.currentTimeMillis();
        getDriver().waitForPageToLoad(String.valueOf(timeout));
        lastPageLoadingTime = System.currentTimeMillis() - start;
        document = null;
        url = null;
    }

    @Deprecated
    public void toEndPage() {
        if (CoreProperties.isLocalSeleniumServer()) {
            if (CoreProperties.getBrowser().isSafari()) {
                pageDown();
            } else {
                LocalServerUtils.ctrlEnd();
            }
        }
    }

    @Deprecated
    public void pageDown() {
        LocalServerUtils.pageDown();
    }

    @Deprecated
    public void toTopPage() {
        if (CoreProperties.isLocalSeleniumServer()) {
            if (CoreProperties.getBrowser().isSafari()) {
                LocalServerUtils.pageUp();
            } else {
                LocalServerUtils.ctrlHome();
            }
        }
    }

    public void F11() {
        LocalServerUtils.pushF11();
        LocalServerUtils.mouseMoveAt(0, 500);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignore
        }
        getDocument();
    }

    public void stop() {
        getDriver().runScript("window.stop()");
    }


    protected SeleniumDriver getDriver() {
        // warning! link to driver will be unavailable if restart or crash driver.
        if (driver == null || !SeleniumDriverFactory.isParallel())
            driver = SeleniumDriverFactory.getDriver();
        return driver;
    }

    protected BrowserTypes getBrowser() {
        return driver.getType();
    }
}
