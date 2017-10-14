package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Content;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Frame;
import com.powersteeringsoftware.libs.elements.Header;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.FrameLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;
import org.testng.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.08.11
 * Time: 14:43
 */
public abstract class Page extends AbstractPage {
    private static final String JS_ERROR_ATTRIBUTE = "JSError";
    private static final String BODY = "body";
    private static final String BODY_ELEMENT = "//" + BODY;
    private static final TimerWaiter SCROLLING_WAITER = new TimerWaiter(1000);
    private static boolean checkJs = true;

    private static boolean checkBlankPage;

    public static void setCheckBlankPage(boolean b) {
        checkBlankPage = b;
    }

    public static boolean isCheckBlankPage() {
        return checkBlankPage;
    }

    protected boolean isFreshDocument;

    public static void setCheckJs(boolean b) {
        checkJs = b;
    }

    public Document getDocument(boolean getNew) {
        if (getNew || document == null)
            return document = getDriver().getDocument();
        Assert.assertFalse(isBlankPage(document), "Blank page");
        return document;
    }

    protected static boolean isBlankPage(Document document) {
        return document.selectNodes("//body/*").size() - document.selectNodes("//body/script").size() == 0;
    }

    protected boolean isBlankPage(boolean newDoc) {
        return isBlankPage(getDocument(newDoc));
    }

    protected boolean isBlankPage() {
        return isBlankPage(true);
    }

    protected void checkBlankPage() {
        if (!checkBlankPage) return;
        if (isBlankPage()) {
            PSLogger.error("Blank page!");
            refresh();
        }
    }


    public boolean hasDocument() {
        return document != null;
    }

    public void setIsDocumentFresh() {
        setFreshDocument(true);
    }

    protected void setFreshDocument(boolean fresh) {
        PSLogger.debug("fresh:" + fresh);
        isFreshDocument = fresh;
    }

    public Document getDocument() {
        return getDocument(true);
    }

    public static void verticalScroll(int position) {
        String script = "window.scroll(0," + position + ")";
        SCROLLING_WAITER.waitTime();
        try {
            SeleniumDriverFactory.getDriver().getEval(script);
        } catch (SeleniumException se) { // exception under chrome 'Property 'scroll' of object [object DOMWindow] is not a function'
            PSLogger.warn(script + " : " + se.getMessage());
        }
        SCROLLING_WAITER.waitTime();
    }


    private static String _yWindowScript;

    public int getVerticalScrollPosition() {
        if (_yWindowScript != null) return getEvalAsInteger(_yWindowScript);
        String[] scripts = new String[]{"window.pageYOffset", "window.document.documentElement.scrollTop", "window.document.body.scrollTop"};
        for (String script : scripts) {
            try {
                String sRes = getDriver().getEval(script);
                Integer iRes = 0;
                if (sRes != null && sRes.matches("\\d+"))
                    iRes = Integer.parseInt(sRes);
                if (iRes != 0) {
                    _yWindowScript = script;
                    PSLogger.debug("Vertical position is " + iRes);
                    return iRes;
                }
            } catch (SeleniumException se) {
                PSLogger.warn(script + " : " + se.getMessage());
            }
        }
        return 0;
    }

    private int getEvalAsInteger(String script) {
        try {
            String sRes = getDriver().getEval(script);
            Integer iRes = 0;
            if (sRes != null && sRes.matches("\\d+"))
                iRes = Integer.parseInt(sRes);
            if (iRes != 0) return iRes;
        } catch (SeleniumException se) {
            PSLogger.warn(script + " : " + se.getMessage());
        }
        return 0;
    }

    public void scrollToEnd() {
        Integer[] c = getPageCoordinates();
        if (c != null) {
            PSLogger.debug("Page coords : " + Arrays.toString(c));
            verticalScroll(c[3]);
        }
    }

    public void scrollToTop() {
        verticalScroll(0);
    }

    public void scroll(Element toElement) {
        toElement.scrollTo();
    }

    public Frame getFrame(ILocatorable frame) {
        return getFrame(frame.getLocator());
    }

    public Frame getFrame() {
        return getFrame(FrameLocators.IFRAME);
    }

    protected Frame getFrame(String loc) {
        return new Frame(loc);
    }

    public void selectParentFrame() {
        getUpperFrame().select();
    }

    public void selectTopFrame() {
        getFrame(FrameLocators.TOP).select();
    }

    protected Frame getUpperFrame() {
        return getFrame(FrameLocators.UPPER);
    }

    public int[] getAbsolutePageCoordinates() {
        Color color = getTopElement().getBackgroundColor();
        int[] res = LocalServerUtils.getHorizontalAndVerticalDifference(color);
        PSLogger.debug("Browser coordinates are " + Arrays.toString(res));
        if (res == null) {
            res = new int[2];
            res[0] = res[1] = 0;
        }
        return res;
    }

    public Integer[] getPageCoordinates() {
        return getBodyElement().getCoordinates();
    }

    public Element getTopElement() {
        Header header = new Header();
        if (header.isVisible()) return header;
        return new Content();
    }

    public Element getBodyElement() {
        return new Element(BODY_ELEMENT);
    }

    public List<Element> getElements(boolean newDoc, String xpath) {
        return Element.searchElementsByXpath(getDocument(newDoc), xpath);
    }

    public Element getElement(boolean newDoc, String xpath) {
        return Element.searchElementByXpath(getDocument(newDoc), xpath);
    }

    public Element getElement(String xpath) {
        return getElement(true, xpath);
    }

    public Element getElement(ILocatorable xpath) {
        return getElement(true, xpath.getLocator());
    }

    public Element getElement(boolean newDoc, ILocatorable xpath) {
        return getElement(newDoc, xpath.getLocator());
    }

    public List<Element> getElements(String xpath) {
        return getElements(true, xpath);
    }

    public List<Element> getElements(ILocatorable xpath) {
        return getElements(true, xpath.getLocator());
    }

    public List<Element> getElements(boolean newDoc, ILocatorable xpath) {
        return getElements(newDoc, xpath.getLocator());
    }

    public void clickBody() {
        try {
            getBodyElement().click(false);
        } catch (SeleniumException e) {
            PSLogger.warn(e.getMessage());
        }
    }

    public static void initJsErrorChecker() {
        if (!checkJs) return;
        if (SeleniumDriverFactory.getDriver().getType().isWebDriverIE()) {
            // do not check js for ie web-driver
            return;
        }
        try {
            SeleniumDriverFactory.getDriver().runScript("window.onerror=function(msg)" +
                    "{" +
                    "var e=window.dojo.query('" + BODY + "')[0];" +
                    "e.setAttribute('" + JS_ERROR_ATTRIBUTE + "', msg);" +
                    "}");
        } catch (SeleniumException e) {
            PSLogger.warn("Can't init js-error-checker : " + e.getMessage());
            PSLogger.saveFull();
        }
    }

    public String getJsError() {
        if (!checkJs) return null;
        Element body = getBodyElement();
        String attr = body.getAttribute(JS_ERROR_ATTRIBUTE);
        return attr != null && attr.isEmpty() ? null : attr;
    }

    public void checkJSError() {
        if (!checkJs) return;
        if (getDriver().getType().isWebDriverIE()) {
            // do not check js for ie web-driver
            return;
        }
        String jsError = getJsError();
        if (jsError == null) return;
        for (Object[] error : CoreProperties.getKnownJsErrors()) {
            if (!((Class) error[0]).isInstance(this)) {
                continue;
            }
            String msg = (String) error[1];
            if (!jsError.contains(msg)) continue;
            throw new PSKnownIssueException((String) error[2], "JS Error : " + jsError);
        }
        Assert.fail("JS Error : " + jsError);
    }

    public String getAlert() {
        if (getDriver().isAlertPresent()) {
            return getDriver().getAlert();
        }
        return null;
    }

    public String getCookie() {
        return getDriver().getCookie();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }
}
