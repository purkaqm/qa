package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.core.SeleniumWebDriver;
import com.powersteeringsoftware.libs.elements.waiters.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.pages.Page;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.Locators;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.dom4j.tree.DefaultElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.powersteeringsoftware.libs.util.Locators.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 21:16:44
 * To change this template use File | Settings | File Templates.
 */
public class Element {
    private static final int DOM_LOCATOR_CHILD_MAXIMUM = 20;
    private static final int DOM_LOCATOR_CHILD_MAXIMUM_2 = 150;

    private SeleniumDriver driver;
    String locator;
    String initialLocator;
    String xpath;
    String node;
    protected DefaultElement defaultElement;
    protected Frame frame;
    private Element parent;

    private String id;

    public Element(ILocatorable locator) {
        this(locator.getLocator());
    }

    public Element(ILocatorable locator, boolean doSimplifyLoc) {
        this(locator.getLocator(), doSimplifyLoc);
    }

    public Element(String locator) {
        this(locator, true);
    }

    public Element(String locator, Frame frame) {
        this(locator, true);
        setFrame(frame);
    }

    Element() {
        //empty default constructor without any content
    }

    public Element(String locator, boolean doSimplifyLoc) {
        if (locator != null && locator.startsWith(ID_LOCATOR_PATTERN)) {
            id = locator.replace(ID_LOCATOR_PATTERN, "");
            xpath = Locators.id2xpath(id);
        }
        initialLocator = locator;
        BrowserTypes browser = getDriver().getType();
        /*if (browser.isWebDriverIE() && locator.startsWith(LINK_LOCATOR_PATTERN)) {
            //todo: its major workaround, see Link class
            this.locator = "//a[contains(text(), '" + locator.replace(LINK_LOCATOR_PATTERN, "") + "')]";
        } */
        if ((browser.isGoogleChrome() || browser.isZero()) && Locators.isCss(locator)) {
            //todo: its workaround for chrome-web-driver
            this.locator = initialLocator.replaceAll("^css=([A-Za-z]+):contains\\(([^\\)]+)\\)$", "//$1[contains(text(), $2)]");
        } else {
            this.locator = doSimplifyLoc ? simplifyLocator(locator, browser) : locator;
        }

    }

    public Element(Document doc, ILocatorable locator) {
        this(doc, locator.getLocator());
    }

    public Element(Document doc, String locator) {
        this(searchElementByXpath(doc, locator));
    }

    protected Element(Element e) {
        initFromOther(e);
    }

    private WebDriver webDriver;

    private boolean isWebDriver() {
        return getDriver().getType().isWebDriver();
    }

    private WebDriver getWebDriver() {
        if (!isWebDriver()) return null;
        if (webDriver != null) return webDriver;
        return webDriver = ((SeleniumWebDriver) getDriver()).getWebDriver();
    }

    private WebElement webElement;

    private WebElement getWebElement() {
        if (!isWebDriver()) return null;
        if (webElement != null) return webElement;
        return webElement = ((SeleniumWebDriver) getDriver()).findElement(locator);
    }

    public void initFromOther(Element e) {
        initialLocator = e.initialLocator;
        locator = e.locator;
        defaultElement = e.defaultElement;
        setFrame(e);
    }

    public void setDynamic() {
        PSLogger.debug2("Reset element '" + locator + "'");
        locator = initialLocator;
        defaultElement = null;
    }

    public boolean isSimpleLocator() {
        return locator.startsWith(MY_LOCATOR_PATTERN) ||
                locator.startsWith(ID_LOCATOR_PATTERN) ||
                locator.startsWith(NAME_LOCATOR_PATTERN) ||
                locator.startsWith(LINK_LOCATOR_PATTERN);
    }

    public void setSimpleLocator(boolean doReset) {
        String myLoc = MY_LOCATOR_PATTERN + System.currentTimeMillis();
        PSLogger.debug2("Assign id " + myLoc + " for element " + locator);
        try {
            getDriver().assignId(locator, myLoc);
            if (defaultElement != null && defaultElement instanceof DOMElement) {
                ((DOMElement) defaultElement).setAttribute("id", myLoc);
            }
            id = myLoc;
            xpath = null;
            this.locator = myLoc;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // ignore
            }
            if (doReset) initialLocator = null;
        } catch (SeleniumException e) {
            PSLogger.debug2("assignId:" + e);
        }
    }

    public void setSimpleLocator() {
        setSimpleLocator(false);
    }

    public String getId() {
        if (id == null && locator.startsWith(ID_LOCATOR_PATTERN)) id = locator.replace(ID_LOCATOR_PATTERN, "");
        return id;
    }

    public void setId(ILocatorable id) {
        setId(id.getLocator());
    }

    protected void setId(String id) {
        this.id = id;
    }

    public void setId() {
        if (defaultElement != null) {
            id = defaultElement.attributeValue("id");
        }
        if (id == null || id.isEmpty())
            id = getAttribute("id");
    }

    public String getLocator() {
        return initialLocator;
    }

    public String getXpathLocator() {
        if (xpath != null) return xpath;
        return xpath = findXpathLocator();
    }

    private String findXpathLocator() {
        String loc = findXpathLocator(locator);
        if (loc == null)
            loc = findXpathLocator(initialLocator);
        return loc;
    }

    private String findXpathLocator(String locator) {
        if (locator.startsWith(MY_LOCATOR_PATTERN))
            return id2xpath(locator, node);
        locator = locator.replace(IDENTIFIER_LOCATOR_PATTERN, ID_LOCATOR_PATTERN);
        if (locator.startsWith(ID_LOCATOR_PATTERN)) {
            return id2xpath(locator, node);
        }
        if (locator.startsWith(NAME_LOCATOR_PATTERN)) {
            return name2xpath(locator, node);
        }
        if (isXpath(locator)) {
            return locator.replace(XPATH_LOCATOR_PATTERN, "");
        }
        if (isLink(locator)) {
            return link2xpath(locator, getDriver().getType().isWebDriverIE());
        }
        if (isCss(locator)) {
            return css2xpath(locator);
        }
        if (locator.matches("\\w+")) { //wairning! for ie such loc doesn't work
            return idOrName2xpath(locator, node);
        }
        return null;
    }

    /**
     * simplify locators
     * todo: this is hotfix method
     *
     * @param locator - String locator
     * @return - String new locator
     */
    private static String simplifyLocator(String locator, BrowserTypes browser) {
        String newLocator = Locators.dom2any(locator);
        if (newLocator != null) {
            PSLogger.debug2("Replace locator " + locator + " with dom-locator : " + newLocator);
            return newLocator;
        }
        if (browser.isRCDriverIE() && Locators.isXpath(locator)) { // its for acceleration
            newLocator = Locators.xpath2css(locator, 7, false);
        }
        if (newLocator != null) {
            PSLogger.debug2("Replace locator " + locator + " with css-selector : " + newLocator);
            return newLocator;
        }
        return locator;
    }


    void select(ILocatorable value) {
        this.select(value.getLocator());
    }

    void select(String value) {
        PSLogger.debug("Select " + value + " for " + initialLocator);
        getDriver().select(locator, value);
    }


    void type(String text) {
        PSLogger.debug("Type '" + text + "' for " + initialLocator);
        getDriver().type(locator, text);
        if (SeleniumDriverFactory.getVersion().isInputBroken(getDriver())) {
            //TODO: find solution
            PSSkipException.skip("Sometimes incorrect behaviour after typing text for this selenium and browser");
        }
    }

    void click() {
        click(true);
    }

    public void clickAt(int x, int y) {
        PSLogger.debug("Click at " + initialLocator + ": " + x + "," + y);
        getDriver().clickAt(locator, x + "," + y);
    }

    public void fireEvent(String event) {
        getDriver().fireEvent(locator, event);
    }

    public void click(boolean wait) {
        PSLogger.debug("Click on " + initialLocator);
        getDriver().click(locator);
        if (wait) {
            getDriver().waitForPageToLoad();
        }
    }

    public String clickWithConfirmation(boolean wait) {
        PSLogger.debug("Click on " + initialLocator + " and wait confirmation");
        getDriver().chooseOkOnNextConfirmation();
        getDriver().click(locator);
        String conf = getDriver().getConfirmation();
        PSLogger.debug("Confirmation: " + conf);
        if (wait) {
            getDriver().waitForPageToLoad();
        }
        return conf;
    }

    public boolean exists() {
        PSLogger.debug("exists " + initialLocator);
        return getDriver().isElementPresent(locator);
    }

    public String getText() {
        PSLogger.debug("Get text for element '" + initialLocator + "'");
        // todo: may be use defaultElement if exists?
        return getDriver().getText(locator);
    }

    public String getParentText() {
        String thisText = getText();
        String parentText = getParent().getText();
        return parentText.replace(thisText, "");
    }


    public String getDEText() {
        return defaultElement != null ? StrUtil.trim(defaultElement.getTextTrim()) : "";
    }

    public String getDEAttribute(ILocatorable loc) {
        return getDEAttribute(loc.getLocator());
    }

    public String getDEStyle() {
        return hasDEAttribute("style") ? getDEAttribute("style").toLowerCase() : "";
    }

    public boolean isDEVisible() {
        return !getDEStyle().contains("display: none");
    }

    public Element getDEChild() {
        DefaultElement d;
        if (defaultElement == null || (d = (DefaultElement) defaultElement.selectSingleNode(".//*")) == null)
            return null;
        Element res = new Element(getXpathLocator() + "//*");
        res.defaultElement = d;
        res.parent = this;
        res.frame = frame;
        return res;
    }

    public String getDEClass() {
        return getDEAttribute("class");
    }

    public String getDEId() {
        return getDEAttribute("id");
    }

    public String getDEAttribute(String loc) {
        return defaultElement != null ? defaultElement.attributeValue(loc) : "";
    }

    public boolean hasDEAttribute(String loc) {
        return defaultElement != null && defaultElement.attribute(loc) != null;
    }

    public boolean hasDEAttribute(ILocatorable loc) {
        return hasDEAttribute(loc.getLocator());
    }


    public String getDENode() {
        return defaultElement != null ? defaultElement.getName() : null;
    }

    public String getDEInnerText(String... exclude) {
        if (defaultElement == null) return "";
        return getInnerText(exclude).replaceAll("<[^>]+>", " ");
    }

    public String getDEInnerText(boolean space) {
        if (defaultElement == null) return "";
        return getInnerText().replaceAll("<[^>]+>", space ? " " : "");
    }

    public String getDEInnerText(ILocatorable exclude) {
        return getInnerText(exclude.getLocator()).replaceAll("<[^>]+>", " ");
    }

    public String getInnerText(String... exclude) {
        List<Element> exList = new ArrayList<Element>();
        for (String l : new String[]{"//script", "//noscript"}) {
            exList.addAll(Element.searchElementsByXpath(this, l));
        }
        String res = StrUtil.replaceSpaces(asXML());
        for (String l : exclude) {
            exList.addAll(Element.searchElementsByXpath(this, l));
        }

        for (Element e : exList) {
            String r = StrUtil.replaceSpaces(e.asXML());
            res = res.replace(r, "");
        }
        return res.replaceFirst("^[^>]+>", "").replaceFirst("<[^<]+$", "");
    }


    public String getAttribute(ILocatorable attr) {
        return getAttribute(attr.getLocator());
    }

    /**
     * @param attr attribute name.
     * @return attribute value or null.
     */
    public String getAttribute(String attr) {
        if ("id".equalsIgnoreCase(attr)) {
            if (locator.startsWith(MY_LOCATOR_PATTERN)) return locator;
            if (locator.startsWith(ID_LOCATOR_PATTERN)) return locator.replace(ID_LOCATOR_PATTERN, "");
            if (id != null) return id;
        }
        if ("name".equalsIgnoreCase(attr)) {
            if (locator.startsWith(NAME_LOCATOR_PATTERN)) return locator.replace(NAME_LOCATOR_PATTERN, "");
        }
        PSLogger.debug("get attribute for " + initialLocator);
        return getDriver().getAttribute(locator, attr);
    }

    public String getElementClass() {
        return getAttribute("class");
    }

    public String getElementStyle() {
        return getAttribute("style");
    }

    /**
     * @return Array with x-y-w-h
     */
    public Integer[] getCoordinates() {
        PSLogger.debug("get coords for " + initialLocator);
        return getDriver().getCoordinates(locator);
    }

    public Integer[] getRelativeCoordinates(Element element) {
        PSLogger.debug("get relative coords for " + initialLocator);
        Integer[] iSrc = getCoordinates();
        Integer[] iDst = element.getCoordinates();
        int diffY = iDst[1] - iSrc[1];
        int diffX = iDst[0] - iSrc[0];
        return new Integer[]{diffX, diffY};
    }

    public int[] getAverageCoordinates() {
        Integer[] coords = getDriver().getCoordinates(locator);
        if (coords == null) return new int[]{0, 0};
        int x = coords[0] + coords[2] / 2;
        int y = coords[1] + coords[3] / 2;
        return new int[]{x, y};
    }

    public int getY() {
        return getAverageCoordinates()[1];
    }

    public int getX() {
        return getAverageCoordinates()[0];
    }

    public void dragAndDrop(Element to) {
        PSLogger.debug("Drag'n'drop " + initialLocator + " to " + to.initialLocator);
        getDriver().dragAndDropToElement(locator, to.locator);
    }

    public void dragAndDrop(int x, int y) {
        PSLogger.debug("Drag'n'drop " + initialLocator + " to " + x + "," + y);
        getDriver().dragAndDrop(locator, x + "," + y);
    }


    public void mouseDownAndUp() {
        PSLogger.debug("mouse down, mouse up for " + initialLocator);
        mouseDown();
        mouseUp();
    }

    public void mouseDownAndUp(boolean waitForAlert) {
        mouseDownAndUp(waitForAlert, CoreProperties.getWaitForElementToLoad());
    }

    public void mouseDownAndUp(boolean waitForAlert, long timeout) {
        try {
            mouseDownAndUp();
            if (!waitForAlert) return;
        } catch (SeleniumException ex) {
            if (waitForAlert && ex.getMessage().contains(SeleniumDriverFactory.SELENIUM_EXCEPTION_ALERT_WAS)) {
                PSLogger.info(ex.getMessage());
                return;
            } else {
                throw ex;
            }
        }
        PSLogger.debug("Now wait alert " + timeout + " ms");
        AlertWaiter.waitForAlert(timeout);
    }


    public void mouseDownAndUp(int x, int y) {
        PSLogger.debug("mouse down, mouse up for " + initialLocator + " on " + x + "," + y);
        mouseDownAt(x, y);
        mouseUpAt(x, y);
    }

    public void mouseDownAt(int x, int y) {
        getDriver().mouseDownAt(locator, x + "," + y);
    }

    public void mouseDownAt(int[] xy) {
        mouseDownAt(xy[0], xy[1]);
    }

    public void mouseDownAt() {
        int[] xy = getAverageCoordinates();
        mouseDownAt(xy);
    }


    public void mouseUpAt(int x, int y) {
        getDriver().mouseUpAt(locator, x + "," + y);
    }


    public void mouseDown() {
        getDriver().mouseDown(locator);
    }

    public void mouseUp() {
        getDriver().mouseUp(locator);
    }

    public void mouseOver() {
        PSLogger.debug2("Mouse-over " + locator);
        getDriver().mouseOver(locator);
    }

    public void mouseMove() {
        getDriver().mouseMove(locator);
    }

    public void mouseMoveAt(Element to) {
        Integer[] coords = getRelativeCoordinates(to);
        mouseMoveAt(coords[0], coords[1]);
    }

    public void _testDnD(Element dst) {
        Actions builder = new Actions(getWebDriver());
        Integer[] x = getRelativeCoordinates(dst);
        Locatable hoverItem = null;
        Mouse mouse = null;

        hoverItem = (Locatable) dst.getWebElement();
        mouse = ((HasInputDevices) getWebDriver()).getMouse();

        PSLogger.info("DND " + getWebElement() + "->" + dst.getWebElement() + ": " + Arrays.toString(x));
        PSLogger.info("DND> " + getWebElement().isDisplayed() + "->" + dst.getWebElement().isDisplayed());
        //builder.clickAndHold(getWebElement()).build().perform();
        mouse.mouseDown(((Locatable) getWebElement()).getCoordinates());
        TimerWaiter.waitTime(1500);
        //.moveToElement(dst.getWebElement()).pause(1000)
        //builder.moveByOffset(x[0], x[1]).moveToElement(dst.getWebElement()).build().perform();
        mouse.mouseMove(hoverItem.getCoordinates());
        //.release()
        //.build();
        TimerWaiter.waitTime(1500);
        mouse.mouseUp(hoverItem.getCoordinates());
        builder.moveToElement(dst.getWebElement()).build().perform();
        TimerWaiter.waitTime(1500);
        //act.perform();
    }

    public void mouseMoveAt(int x, int y) {
        PSLogger.debug("mouse move at " + x + "," + y + " for " + locator);
        try {
            getDriver().mouseMoveAt(locator, x + "," + y);
        } catch (SeleniumException se) {
            if (!getDriver().getType().isIE())
                throw se;
            PSLogger.warn(se.getMessage());
        }
    }

    public void mouseOut() {
        PSLogger.debug("Mouse out " + initialLocator);
        getDriver().mouseOut(locator);
    }

    public boolean isVisible() {
        return getDriver().isVisible(locator);
    }

    public String getValue() {
        return getDriver().getValue(locator);
    }

    public void setValue(String val) {
        String loc = getDomLocator();
        if (loc == null) {
            PSLogger.warn("Can't find dom locator for " + initialLocator);
            return;
        }
        getDriver().getEval(loc + ".value='" + val + "'");
    }

    public void focus() {
        try {
            getDriver().focus(locator);
        } catch (SeleniumException e) {
            PSLogger.warn("Exception on focus for " + CoreProperties.getBrowser().getName() + ": " + e.getMessage());
        }
    }


    public void waitForAttribute(ILocatorable name, ILocatorable value) {
        waitForAttribute(name.getLocator(), value.getLocator());
    }

    public void waitForAttribute(ILocatorable name, ILocatorable value, long timeout) {
        waitForAttribute(name.getLocator(), value.getLocator(), timeout);
    }

    public void waitForAttribute(String name, String value) {
        PSLogger.debug("wait until the attribute " + name + " does not accept the value of " + value +
                " for element " + locator);
        new ElementBecomeChangingWaiter(locator, name, value).defaultWait();
    }

    public void waitForAttribute(String name, String value, long timeout) {
        PSLogger.debug("wait " + timeout + " ms until the attribute " + name + " does not accept the value of " + value +
                " for element " + locator);
        new ElementBecomeChangingWaiter(locator, name, value).defaultWait(timeout);
    }


    public void waitForStyle(String value) {
        waitForAttribute("style", value);
    }

    public void waitForStyle(ILocatorable value) {
        waitForStyle(value.getLocator());
    }

    public void waitForClass(String value) {
        waitForAttribute("class", value);
    }

    public void waitForClass(ILocatorable value) {
        waitForClass(value.getLocator());
    }

    public void waitForClass(ILocatorable value, long timeout) {
        waitForAttribute("class", value.getLocator(), timeout);
    }

    public void waitForClass(ILocatorable value, TimerWaiter waiter) {
        waitForAttribute("class", value.getLocator(), waiter.getIntervalInMilliseconds());
    }

    public void waitForClassChanged(String value) {
        waitForAttributeChanged("class", value, CoreProperties.getWaitForElementToLoad());
    }

    public void waitForClassChanged(String value, long timeout) {
        waitForAttributeChanged("class", value, timeout);
    }

    public void waitForClassChanged(ILocatorable value) {
        waitForClassChanged(value.getLocator());
    }

    public void waitForClassChanged(ILocatorable value, long timeout) {
        waitForClassChanged(value.getLocator(), timeout);
    }

    public void waitForClassChanged(ILocatorable value, TimerWaiter waiter) {
        waitForClassChanged(value.getLocator(), waiter.getIntervalInMilliseconds());
    }

    public void waitForAttributeChanged(String name, String value, long timeout) {
        PSLogger.debug("wait until the attribute " + name + " has the value " + value +
                " for element " + locator);
        ElementBecomeChangingWaiter eWaiter = new ElementBecomeChangingWaiter(locator, name, value);
        eWaiter.setReverseLogic();
        eWaiter.defaultWait(timeout);
    }

    public void waitForAttributeChanged(String name, String value) {
        waitForAttributeChanged(name, value, CoreProperties.getWaitForElementToLoad());
    }

    public void waitForAttributeChanged(ILocatorable name, ILocatorable value) {
        waitForAttributeChanged(name.getLocator(), value.getLocator());
    }

    public void waitForText(String t, long d) {
        waitForText(t, null, d);
    }

    public void waitForText(String t) {
        waitForText(t, null, CoreProperties.getWaitForElementToLoad());
    }

    /**
     * @param text
     * @param mode if null then equals, if true ignore case, otherwise contains
     * @param time
     */
    protected void waitForText(final String text, final Boolean mode, long time) {
        PSLogger.debug("Wait " + time + "ms until text '" + text + "' does not appear");
        new Wait() {
            @Override
            public boolean until() {
                String t;
                if (exists() && (t = getText()) != null &&
                        (mode == null ? t.trim().equals(text) : mode ? t.trim().equalsIgnoreCase(text) : t.trim().contains(text)))
                    return true;
                return false;
            }
        }.wait("Element " + locator + " still has not text '" + text + "'", time, 2000);
    }

    public void waitForTextChanged() {
        PSLogger.debug("wait until the inner text changed");
        new ElementBecomeChangingWaiter(locator).defaultWait();
    }

    public void waitForTextChanged(String textWas) {
        PSLogger.debug("wait until the inner text changed, text was: " + textWas);
        new ElementBecomeChangingWaiter(locator, textWas).defaultWait();
    }

    public void waitForPresent() {
        PSLogger.debug("Wait until the element " + locator + " does not appear");
        new ElementBecomePresentWaiter(locator).defaultWait();
    }

    public void waitForPresent(long time) {
        waitForPresent(time, true);
    }

    public void waitForPresent(TimerWaiter waiter) {
        waitForPresent(waiter.getIntervalInMilliseconds(), true);
    }

    public void waitForPresent(long time, boolean doThrow) {
        PSLogger.debug("Wait " + time + "ms until the element " + locator + " does not appear");
        try {
            new ElementBecomePresentWaiter(locator).defaultWait(time);
        } catch (Wait.WaitTimedOutException e) {
            if (doThrow) {
                throw e;
            } else {
                PSLogger.debug(e.getMessage());
            }
        }
    }

    public void waitForDEPresent(long time, final Page page) {
        PSLogger.debug("Wait " + time + "ms until the DE element " + locator + " does not appear");
        new Wait() {
            @Override
            public boolean until() {
                if (isDEPresent()) return true;
                setDefaultElement(page.getDocument());
                return false;
            }
        }.wait("Can't find DE element for loc '" + locator + "'", time, 2000);
    }

    public void waitForDisapeared() {
        PSLogger.debug("Wait until the element " + locator + " does not disappear");
        new ElementBecomeDisappearedWaiter(locator).defaultWait();
    }

    public void waitForVisible() {
        PSLogger.debug("Wait while " + locator + " become visible");
        waitForVisible(CoreProperties.getWaitForElementToLoad());
    }

    public void waitForVisible(long time) {
        PSLogger.debug("Wait " + time + " ms while " + locator + " become visible");
        new ElementBecomeVisibleWaiter(locator).defaultWait(time);
    }

    public void waitForVisible(TimerWaiter waiter) {
        waitForVisible(waiter.getIntervalInMilliseconds());
    }

    public void waitForUnvisible() {
        waitForUnvisible(CoreProperties.getWaitForElementToLoad());
    }

    public void waitForUnvisible(long time) {
        PSLogger.debug("Wait " + time + " ms while " + locator + " become invisible");
        new ElementBecomeUnvisibleWaiter(locator).defaultWait(time);
    }

    public void waitForUnvisible(TimerWaiter time) {
        waitForUnvisible(time.getIntervalInMilliseconds());
    }

    public void waitForValue() {
        PSLogger.debug("Wait until the value of the element " + locator + " becomes not empty");
        new ElementBecomeValueWaiter(locator).defaultWait();
    }

    public void waitUntilPresent(long time) {
        PSLogger.debug("Wait " + time + " ms until " + locator + " is present");
        new AbstractElementWaiter(locator, "Element " + this + " still present after " + time + "ms") {
            @Override
            public boolean until() {
                return !getDriver().isElementPresent(locator);
            }
        }.defaultWait(time);
    }

    public void waitUntilPresent() {
        waitUntilPresent(CoreProperties.getWaitForElementToLoad());
    }

    /**
     * parent and child should have xpath locators
     *
     * @param loc
     * @return
     */
    public Element getChildByXpath(String loc) {
        if (loc == null)
            return null;
        if (!Locators.isXpath(loc)) {
            loc = findXpathLocator(loc);
        }
        if (loc == null)
            return null;
        Element res = null;
        if (isDEPresent()) {
            res = searchElementByXpath(this, loc);
        }
        if (res == null) { // dynamic way:
            PSLogger.debug2("Can't find DE for child element " + loc + " (parent:" + locator + ")");
            res = new Element(loc = (getXpathLocator() + loc));
        }
        //res.xpath = loc;  <- do not set xpath. why?
        res.setParent(this);
        return res;
    }

    public Element getChildByXpath(ILocatorable loc) {
        return getChildByXpath(loc.getLocator());
    }

    public static Element searchElementByXpath(Element parent, ILocatorable loc) {
        return searchElementByXpath(parent, loc.getLocator());
    }

    /**
     * this method is for ie. selenium can't find elements by locators like
     * "//th[@dojoattachpoint='incrementMonth']" under ie
     *
     * @param parent - Element parent
     * @param loc    - child xpath loc
     * @return - Element result
     */
    public static Element searchElementByXpath(Element parent, String loc) {
        if (parent == null) throw new NullPointerException("Element.searchElementByXpath : parent is null");
        DefaultElement thisEl = parent.getDefaultElement();
        if (thisEl == null) throw new NullPointerException("Element.searchElementByXpath : parent has not content");
        if (!loc.startsWith("/html") && !loc.startsWith(".")) loc = "." + loc;
        DefaultElement childEl = (DefaultElement) thisEl.selectSingleNode(loc);
        if (childEl == null) return null;
        Element res = getElementByDefaultElement(childEl, getUniqueXpath(parent, childEl));
        res.setParent(parent);
        return res;
    }

    public static Element searchElementByXpath(String loc) {
        return searchElementByXpath(SeleniumDriverFactory.getDriver().
                getDocument(), loc);
    }

    public static Element searchElementByXpath(ILocatorable loc) {
        return searchElementByXpath(loc.getLocator());
    }


    public static Element getElementByXpath(String loc) {
        return getElementByXpath(SeleniumDriverFactory.getDriver().
                getDocument(), loc);
    }

    public static Element getElementByXpath(Document doc, ILocatorable loc) {
        return getElementByXpath(doc, loc.getLocator());
    }

    public static Element getElementByXpath(Document doc, String loc) {
        Element res = searchElementByXpath(doc, loc);
        if (res != null) return res;
        return new Element(loc);
    }

    public static List<Element> searchElementsByXpath(String loc) {
        return searchElementsByXpath(SeleniumDriverFactory.getDriver().getDocument(), loc);
    }


    public static List<Element> searchElementsByXpath(Element parent, ILocatorable loc) {
        return searchElementsByXpath(parent, loc.getLocator());
    }

    public static List<Element> searchElementsByXpath(Element parent) {
        return searchElementsByXpath(parent, "//*");
    }

    public static List<Element> searchElementsByXpath(Element parent, String loc) {
        List<Element> res = new ArrayList<Element>();
        DefaultElement thisEl = parent.getDefaultElement();
        if (thisEl == null) {
            throw new SeleniumException("Can't find element " + loc + " in parent " + parent);
        }
        if (!loc.startsWith("/html") && !loc.startsWith(".")) loc = "." + loc;
        for (Object o : thisEl.selectNodes(loc)) {
            if (!(o instanceof DefaultElement)) continue;
            Element toAdd = getElementByDefaultElement((DefaultElement) o,
                    parent.getXpathLocator() + "/" + ((DefaultElement) o).getUniquePath(parent.getDefaultElement()));
            toAdd.setParent(parent);
            res.add(toAdd);
        }
        return res;
    }

    static Element getElementByDefaultElement(DefaultElement element, String defaultLoc) {
        String loc;
        String attr;
        String xpath = null;
        String node = element.getName();
        String id = null;
        if ((attr = getAttributeForDomElement(element, "id", null)) != null) {
            loc = ID_LOCATOR_PATTERN + attr;
            xpath = id2xpath(attr, node);
            id = attr;
        } else if ((attr = getAttributeForDomElement(element, "name", null)) != null) {
            loc = NAME_LOCATOR_PATTERN + attr;
            /*  // do not use locator with class because class can be changed while element loading
            } else if ((attr = getAttributeForDomElement(element, "class", getName(element, name))) != null) {
                if (attr.contains(" ")) {
                    loc = "css=" + getName(element, name) + "[class='" + attr + "']";
                } else {
                    loc = "css=" + getName(element, name) + "." + attr;
                }
            */
            xpath = name2xpath(attr, node);
        } else if ((attr = getAttributeForDomElement(element, "value", node)) != null) {
            String quote = attr.contains("'") ? "\"" : "'";
            loc = CSS_LOCATOR_PATTERN + node + "[value=" + quote + attr + quote + "]";
            xpath = css2xpath(loc);
        } else if ((attr = getAttributeForDomElement(element, "href", node)) != null) {
            String quote = attr.contains("'") ? "\"" : "'";
            loc = CSS_LOCATOR_PATTERN + node;
            if (attr.contains(CoreProperties.getURLContext())) {
                loc += "[href*=" + quote + attr.replaceAll(".*(" + CoreProperties.getURLContext() + ".*)", "$1") + quote + "]";
            } else {
                loc += "[href=" + quote + attr + quote + "]";
            }
            xpath = css2xpath(loc);
        } else {
            loc = calculateDomLocatorByTag(((DOMDocument) element.getDocument()), element, node);
            if (loc == null) {
                loc = defaultLoc;
            }
        }
        Element e = new Element(loc);
        e.id = id;
        e.defaultElement = element;
        e.initialLocator = defaultLoc;
        e.xpath = xpath != null ? xpath : getUniqueXpath(element, true);
        e.node = node;
        return e;
    }

    private static String getUniqueXpath(Element parent, DefaultElement child) {
        String relative = child.getUniquePath(parent.getDefaultElement());
        if (relative.startsWith("/html/body/") || relative.startsWith("/*[name()='html']/body/")) {
            return getUniqueXpath(child, false);
        }
        return parent.getXpathLocator() + "/" + relative;
    }

    /**
     * get unique xpath for specified dom4j element
     *
     * @param e          - DefaultElement
     * @param doSimplify - if true then simplify xpath to short
     * @return - String xpath
     */
    private static String getUniqueXpath(DefaultElement e, boolean doSimplify) {
        if (!doSimplify)
            return e.getUniquePath().replace("/html/body/", "//").replace("/*[name()='html']/body/", "//");
        List init = new ArrayList(Arrays.asList(e.getUniquePath().split("/")));
        if (init.size() < 4) {
            return "//html";
        }
        init.remove(0); // remove '/'
        init.remove(0); // remove "html" or "/*[name()='html']"
        init.remove(0); // remove 'body'

        List parts = new ArrayList(init);
        // simplify xpath:
        // xpath like '/html/body/div[11]/div/form/table/tbody[1]/tr[34]/td[2]/div/div' will be converted to
        // xpath like '//tr[34]/td[2]/div/div' if corresponding elements are equal
        Object first = null;
        while (isXpathUnique(e, parts)) {
            first = parts.remove(0);
        }
        if (parts.size() != 0 && first != null) {
            parts.add(0, first);
        } else {
            parts = init;
        }
        return list2xpath(parts);
    }

    private static boolean isXpathUnique(DefaultElement e, List parts) {
        if (parts.size() == 0) return false;
        List list = e.getDocument().selectNodes(list2xpath(parts));
        if (list.size() != 1) return false;
        return list.get(0) instanceof Node && e.getUniquePath().equals(((Node) list.get(0)).getUniquePath());
    }

    private static String list2xpath(List parts) {
        StringBuilder sb = new StringBuilder("//");
        for (Object o : parts) {
            sb.append(o).append("/");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static String calculateDomLocatorByTag(DOMDocument doc, DefaultElement element, String tagName) {
        NodeList nodes = doc.getElementsByTagName(tagName);
        int index = nodeListIndexOf(nodes, element);
        if (index != -1) {
            if (index < DOM_LOCATOR_CHILD_MAXIMUM)
                return DOM_LOCATOR_DOCUMENT_PATTERN + ".getElementsByTagName('" + tagName + "')[" + index + "]";
            else {
                DOMElement parent = getFirstElementWithId(element);
                if (parent == null) return null;
                String res = DOM_LOCATOR_DOCUMENT_PATTERN + ".getElementById('" + parent.getAttribute("id") + "')";
                nodes = parent.getElementsByTagName(tagName);
                index = nodeListIndexOf(nodes, element);
                if (index > DOM_LOCATOR_CHILD_MAXIMUM) {
                    Map map = getParentsMap(element, parent);
                    while (map.size() > 0) {
                        Integer firstParentIndex = (Integer) map.keySet().toArray()[0];
                        DOMElement firstParent = (DOMElement) map.get(firstParentIndex);
                        NodeList firstParentNodes = firstParent.getElementsByTagName(tagName);
                        index = nodeListIndexOf(firstParentNodes, element);
                        res += ".getElementsByTagName('" + firstParent.getName() + "')[" + firstParentIndex + "]";
                        if (index <= DOM_LOCATOR_CHILD_MAXIMUM_2) break;
                        map = getParentsMap(element, firstParent);
                    }
                }
                if (index != -1)
                    return res + ".getElementsByTagName('" + tagName + "')[" + index + "]";
            }
        }
        return null;
    }

    private String calculateDomLocator() {
        if (defaultElement == null) return null;
        return calculateDomLocatorByTag((DOMDocument) defaultElement.getDocument(), defaultElement, defaultElement.getName());
    }

    private static DOMElement getFirstElementWithId(DefaultElement element) {
        String id;
        if ((id = element.attributeValue("id")) != null &&
                element.getDocument().selectNodes("//*[@id='" + id + "']").size() == 1) {
            return (DOMElement) element;
        }
        DefaultElement parent = (DefaultElement) element.getParent();
        if (parent == null) {
            // can't find any parent with id on page
            return null;
        }
        return getFirstElementWithId(parent);
    }

    private static List<DOMElement> getParentsList(DefaultElement element, DOMElement parent) {
        List<DOMElement> res = new ArrayList<DOMElement>();
        DOMElement firstParent = (DOMElement) element.getParent();
        while (!firstParent.getUniquePath().equals(parent.getUniquePath())) {
            res.add(firstParent);
            firstParent = (DOMElement) firstParent.getParent();
        }
        return res;
    }

    private static Map<Integer, DOMElement> getParentsMap(DefaultElement element, DOMElement parent) {
        List<DOMElement> elements = getParentsList(element, parent);
        Map<Integer, DOMElement> res = new TreeMap<Integer, DOMElement>();
        for (DOMElement e : elements) {
            NodeList nodes = parent.getElementsByTagName(e.getName());
            int index = nodeListIndexOf(nodes, e);
            res.put(index, e);
        }
        return res;
    }

    public static int nodeListIndexOf(NodeList nodes, Node node) {
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).equals(node)) {
                return i;
            }
        }
        return -1;
    }

    private static String getAttributeForDomElement(DefaultElement element, String attrName, String name) {
        if (element.attribute(attrName) == null) return null;
        String val = element.attribute(attrName).getValue();
        if (val.isEmpty() || val.equals("null")) return null; // val.equals("null") due to ie behaviour
        if (attrName.equals("href")) {
            val = val.replaceAll(".*(" + CoreProperties.getURLContext() + ".*)", "$1");
        }
        //if (attrName.equals("id") && val.startsWith(MY_LOCATOR_PATTERN)) return null;
        String xpath = attribute2xpath(attrName, val, name);
        if (element.getDocument().selectNodes(xpath).size() != 1) return null;
        return val;
    }

    /**
     * please use Page.getElement from any instance of Page
     *
     * @param doc
     * @param loc
     * @return
     */
    public static Element searchElementByXpath(Document doc, ILocatorable loc) {
        return searchElementByXpath(doc, loc.getLocator());
    }

    /**
     * please use Page.getElement from any instance of Page
     *
     * @param doc org.dom4j.Document
     * @param loc String xpath-locator
     * @return Element instance
     */
    public static Element searchElementByXpath(Document doc, String loc) {
        if (doc == null) throw new NullPointerException("Element.searchElementsByXpath: no document specified");
        DefaultElement childEl = (DefaultElement) doc.selectSingleNode(loc);
        if (childEl == null) {
            if (!loc.startsWith("//html/parent")) {
                PSLogger.debug2("Can't find element " + loc + " in document");
                //PSLogger.debug2(doc.asXML());
            }
            return null;
        }
        return getElementByDefaultElement(childEl, loc);
    }

    /**
     * please use Page.getElements from any instance of Page
     *
     * @param doc org.dom4j.Document
     * @param loc String xpath-locator
     * @return List of Element instances
     */
    public static List<Element> searchElementsByXpath(Document doc, String loc) {
        List<Element> res = new ArrayList<Element>();
        for (Object o : doc.selectNodes(loc)) {
            if (!(o instanceof DefaultElement)) continue;
            res.add(getElementByDefaultElement((DefaultElement) o, getUniqueXpath((DefaultElement) o, false)));
        }
        return res;
    }

    /**
     * please use Page.getElements from any instance of Page
     *
     * @param doc
     * @param loc
     * @return
     */
    public static List<Element> searchElementsByXpath(Document doc, ILocatorable loc) {
        return searchElementsByXpath(doc, loc.getLocator());
    }

    /**
     * return direct parent Element (field this.parent can be distant ancestor)
     *
     * @return parent element.
     */
    public Element getParent() {
        String defaultLoc = getXpathLocator() + "/parent::*";
        Element res = null;
        if (defaultElement != null) {
            res = searchElementByXpath(getParentDoc(), defaultLoc);
            if (res != null && res.xpath != null)
                res.initialLocator = res.xpath;  // simplify initialLocator
        }
        if (res == null) res = new Element(defaultLoc);
        res.setFrame(this);
        return parent = res;
    }

    public Element getVisibleParent() {
        Element parent = getParent();
        if (!parent.exists()) return null;
        if (parent.isVisible()) return parent;
        return parent.getVisibleParent();
    }

    public Element getParent(int num) {
        Element res = this;
        for (int i = 0; i < num; i++) {
            res = res.getParent();
        }
        return res;
    }

    public Element getParent(ILocatorable name) {
        return getParent(name.getLocator().replaceAll("\\.*/+", ""));
    }

    public Element getParent(String name) {
        if (defaultElement == null) return null;
        return searchParent(this, name);
    }

    public Element getParent(String name, String clazz) {
        Element parent = getParent(name);
        if (parent == null) return null;
        String val = parent.getDEClass();
        if (val != null && val.contains(clazz)) return parent;
        return parent.getParent(name, clazz);
    }

    public Element getParent(ILocatorable name, ILocatorable clazz) {
        return getParent(name.getLocator(), clazz.getLocator());
    }

    private static Element searchParent(Element e, String name) {
        Element parent = e.getParent();
        DefaultElement de = parent.defaultElement;
        if (de == null) return null;
        if (de.getName().equals(name)) {
            return parent;
        } else {
            return searchParent(parent, name);
        }
    }


    public void setDefaultElement() {
        PSLogger.debug2("Set DE for " + locator);
        if (parent != null) {
            parent.setDefaultElement();
            if (parent.isDEPresent()) {
                setDefaultElement(parent.getParentDoc());
                return;
            } else {
                parent = null;
            }
        }
        setDefaultElement(getDriver().getDocument(null));
        if (defaultElement == null && frame != null) {
            frame.select();
            setDefaultElement(getDriver().getDocument(null));
        }
    }

    public void setDefaultElement(Document doc) {
        // reset xpath:
        defaultElement = (DefaultElement) doc.selectSingleNode(xpath == null ? xpath = findXpathLocator() : xpath);
        if (defaultElement != null) {
            Element e = getElementByDefaultElement(defaultElement, xpath);
            locator = e.locator;
            if (xpath.contains("parent::") && e.xpath != null) {
                xpath = e.xpath;
            }
        }
    }


    public DefaultElement getDefaultElement() {
        if (defaultElement == null) setDefaultElement();
        return defaultElement;
    }

    public Document getParentDoc() {
        return defaultElement == null ? null : defaultElement.getDocument();
    }

    public boolean isDEPresent() {
        return defaultElement != null;
    }

    public String asXML() {
        return getDefaultElement().asXML();
    }

    public void keyPress(char c) {
        PSLogger.debug("Press key '" + c + "'");
        getDriver().keyPress(locator, "\\" + (int) c);
    }

    public void keyPress(String st) {
        PSLogger.debug("Press key '" + st + "'");
        getDriver().keyPress(locator, st);
    }

    public void keyUp(char c) {
        PSLogger.debug("key down '" + c + "'");
        getDriver().keyUp(locator, "\\" + (int) c);
    }

    public void keyDown(char c) {
        PSLogger.debug("key up'" + c + "'");
        getDriver().keyDown(locator, "\\" + (int) c);
    }

    public void keyPressNative(int code) {
        getDriver().keyPressNative(String.valueOf(code));
    }

    public void setFrame(Frame fr) {
        frame = fr;
    }

    protected void setFrame(Element element) {
        if (element instanceof Frame) {
            setFrame((Frame) element);
        } else {
            frame = element.frame;
        }
    }

    private void setParent(Element e) {
        parent = e;
        setFrame(e);
    }

    public void setFrame(String loc) {
        frame = new Frame(loc);
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(ILocatorable loc) {
        setFrame(loc.getLocator());
    }

    /**
     * this method is only for elements with css locator or with id
     *
     * @return - Color object
     */
    public Color getBackgroundColor() {
        String query = getDomLocator().replaceAll("\\[0\\]$", "") + ".style('backgroundColor')";
        String color = getDriver().getEval(query);
        Color col = parseColor(color);
        PSLogger.debug("background color style for " + locator + " is '" + color + "'=[" + col + "]");
        return col;
    }

    public static Color parseColor(String str) {
        PSLogger.debug("Parse '" + str + "'");
        str = str.toLowerCase().replace("[", "").replace("]", "").replaceFirst(".*background-color:\\s*([^;]+)\\s*;*.*", "$1").replace(",transparent", "");
        //for ie: #043d70
        //for ff and safari: rgb(4\, 61\, 112)
        Color col = null;
        if (str.startsWith("#")) { //ie: COLOR: #ffffe0; BACKGROUND-COLOR: #ffffe0
            col = Color.decode(str.replace("#", "0x"));
        } else if (str.matches("rgb\\(.*\\).*")) { //ff,safari:background-color: rgb(139, 0, 0); color: rgb(139, 0, 0);
            String[] aCol = str.replaceAll("rgb\\(", "").replaceAll("\\)", "").split(",\\s*");
            col = new Color(Integer.parseInt(aCol[0].replaceAll("[^\\d]+", "")),
                    Integer.parseInt(aCol[1].replaceAll("[^\\d]+", "")),
                    Integer.parseInt(aCol[2].replaceAll("[^\\d]+", "")));
        }
        return col;
    }

    protected String getDomScrollTopScript() {
        String loc = getDomLocator();
        if (loc.startsWith(DOM_LOCATOR_DOCUMENT)) {
            loc = DOM_LOCATOR_WINDOW + "." + loc;
        }
        return loc + ".scrollTop";
    }

    public String getDomLocator() {
        String query = null;
        if (locator.startsWith(DOM_LOCATOR_DOCUMENT_PATTERN)) {
            query = locator.replace(DOM_LOCATOR_PATTERN, "");
        } else if (locator.startsWith(CSS_LOCATOR_PATTERN)) {
            query = css2dom(locator);
        } else {
            if (id == null) {
                if (locator.startsWith(MY_LOCATOR_PATTERN)) {
                    id = locator.replace(MY_LOCATOR_PATTERN, "");
                }
                if (locator.startsWith(ID_LOCATOR_PATTERN)) {
                    id = locator.replace(ID_LOCATOR_PATTERN, "");
                }
            }
            if (id != null) {
                query = id2dom(id);
            }
            String loc;
            if (query == null && (loc = calculateDomLocator()) != null) {
                query = loc.replace(DOM_LOCATOR_PATTERN, "");
            }
        }
        if (query == null) {
            query = xpath2dom(getXpathLocator());
        }
        return query;
    }

    public void scrollTo() {
        Integer[] coords = getCoordinates();
        if (coords == null) {
            if (getDriver().getType().isGoogleChrome()) {
                PSLogger.warn("Can't get coordinates for " + locator);
                return;
            }
            throw new SeleniumException("Can't get coordinates for " + locator);
        }
        int position = coords[1];
        PSPage.verticalScroll(position);
    }

    public String toString() {
        return defaultElement != null ? asXML() : initialLocator;
    }

    public void checkForUnexpectedAlert() {
        try {
            String alert = getDriver().getAlert();
            PSLogger.warn(alert);
        } catch (SeleniumException se) {
            PSLogger.debug(getClass().getSimpleName() + "(" + se.getMessage() + ")");
        }
    }


    public SeleniumDriver getDriver() {
        // warning! link to driver will be unavailable if restart or crash driver.
        if (driver == null || !SeleniumDriverFactory.isParallel())
            driver = SeleniumDriverFactory.getDriver();
        return driver;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) return true;
        if (o == null) return false;
        if (!(o instanceof Element)) return false;
        Element e = (Element) o;
        if (isDEPresent() && e.isDEPresent() && defaultElement.equals(e.defaultElement)) return true;
        return locator.equals(e.locator);
    }

}
