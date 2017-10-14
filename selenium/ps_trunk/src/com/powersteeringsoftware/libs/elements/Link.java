package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.Locators;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getVersion;
import static com.powersteeringsoftware.libs.settings.CoreProperties.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 22:08:16
 * To change this template use File | Settings | File Templates.
 */
public class Link extends Element {
    private static final String JAVASCRIPT_HREF = "javascript:";
    private static final String IMG_HREF = ".gif";
    private static final String JSP_HREF = ".jsp";
    protected String name;
    private String link;
    private PSPage page;

    public Link(ILocatorable locator) {
        this(locator.getLocator());
    }

    public Link(ILocatorable locator, PSPage page) {
        this(locator);
        setResultPage(page);
    }

    public Link(String locator) {
        super(locator);
        reInit();
    }

    private void reInit() {
        // todo: its workaround.
        // investigate how it works on different browsers and seleniums
        if (link != null) return;
        if (Locators.isLink(initialLocator) && getVersion().isLinkBroken(getDriver())) { // ist it right now?
            link = getXpathLocator();
        } else if (Locators.isCss(initialLocator) && getVersion().isCssLinkBroken(getDriver())) {
            link = getXpathLocator();
        }
        if (link == null) return;
        //if (new Element(link).exists())
        locator = link;
    }

    public Link(Element e) {
        this(e, null);
    }

    public Link(Element e, PSPage page) {
        super(e);
        setResultPage(page);
    }

    public void setResultPage(PSPage p) {
        page = p;
    }

    public void clickAndWaitNextPage() {
        if (getDriver().getType().isWebDriverIE(9)) {
            // hotfix for ie 9
            String href = getHref();
            Assert.assertNotNull(href, "Can't find href for " + this);
            if (!href.endsWith(JSP_HREF) && !href.endsWith(IMG_HREF)) { // skip images and frames.
                openByHref(href);
                return;
            }
        }
        click();
    }

    public void click(boolean wait) {
        if (wait && page != null) {
            super.click(false);
            page.initJsErrorChecker();  // for catch js-error under ie.
            page.waitForPageToLoad();
            page.testUrl();
        } else {
            super.click(wait);
        }
    }

    public String getHref() {
        String attr = (defaultElement != null) ? defaultElement.attributeValue("href") : getAttribute("href");
        if (attr == null || attr.trim().equals("#")) return "";
        return attr;
    }

    public String getName() {
        if (name == null)
            name = defaultElement != null ? defaultElement.getTextTrim() : getText();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name != null ? name + "(" + super.toString() + ")" : super.toString();
    }

    public void openByHref() {
        openByHref(getHref());
    }

    private void openByHref(String href) {
        Assert.assertNotNull(href, "Can't find href for " + this);
        PSLogger.debug("Open by href '" + href + "'");
        if (href.startsWith(JAVASCRIPT_HREF)) {
            getDriver().runScript(StrUtil.urlToString(href));
            if (page != null) {
                page.initJsErrorChecker();
                page.waitForPageToLoad();
                page.testUrl();
            } else
                getDriver().waitForPageToLoad();
        } else {
            href = href.replace(":443/", "/");
            Assert.assertFalse(href.endsWith(IMG_HREF), "It is not a link: " + this);
            if (!href.startsWith(getURLServerHost())) {  // relative path:
                href = getURLServerWithContext() + href.replaceAll(".+" + getURLContext(), "");
            }
            try {
                getDriver().open(href);
                if (page != null) {
                    page.testUrl();
                }
            } catch (SeleniumException se) {
                if (se.getMessage().contains(SeleniumDriverFactory.SELENIUM_EXCEPTION_TIMEOUT)) {
                    PSLogger.saveFull();
                    throw new PSKnownIssueException(72196, se); // can't open after timeout -> problems with connections.
                }
                throw se;
            }
        }
    }

    public static Link toLink(ILocatorable loc) {
        return new Link(Locators.any2link(loc.getLocator()));
    }
}
