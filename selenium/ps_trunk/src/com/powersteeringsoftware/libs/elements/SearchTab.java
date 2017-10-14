package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.ILocatorable.LOCATOR_REPLACE_PATTERN;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 30.09.2010
 * Time: 17:46:40
 */
public class SearchTab extends Element {
    private ILocatorable inputLoc;
    private ILocatorable submitLoc;
    private ILocatorable loadingLoc;
    private ILocatorable errorLoc;
    private ILocatorable linkLoc;

    private static final TimerWaiter SUBMIT_WAITER = new TimerWaiter(4000);

    public SearchTab(Element popup, ILocatorable... locators) {
        super(popup);
        inputLoc = locators[0];
        submitLoc = locators[1];
        loadingLoc = locators[2];
        errorLoc = locators[3];
        if (locators.length >= 5)
            linkLoc = locators[4];
    }

    public void doSearch(String toSearch) {

        new Input(getChildByXpath(inputLoc)).type(toSearch);
        submit();
    }

    public void doSearch() {
        new Input(getChildByXpath(inputLoc)).clear();
        submit();
    }

    public void submit() {
        new Button(getChildByXpath(submitLoc)).click(false);
        new Element(loadingLoc).waitForDisapeared();
        SUBMIT_WAITER.waitTime();
        setDefaultElement();
        PSLogger.save("After submitting in searching tab");
    }

    public String getErrorMessage() {
        Element error = new Element(errorLoc);
        if (error.exists() && error.isVisible()) {
            return error.getText();
        }
        return null;
    }

    public Link getLink(String name) {
        if (linkLoc == null) return null;
        String loc;
        Link link = new Link(getChildByXpath(loc = linkLoc.getLocator().replace(LOCATOR_REPLACE_PATTERN, name)));
        if (link.isDEPresent()) {
            PSLogger.debug("Element " + loc + " is present");
            if (link.exists())
                return link;
        }
        PSLogger.warn("Can't find de for " + loc);
        return null;
    }

    public void choose(String toSearch) {
        doSearch(toSearch);
        clickLink(toSearch);
    }

    public void clickLink(String name) {
        clickLink(name, true);
    }

    public void clickLink(String name, boolean doThrow) {
        String msg;
        if ((msg = getErrorMessage()) != null) {
            if (!doThrow) {
                PSLogger.warn("There is an error after searching : " + msg);
                PSLogger.save();
                return;
            }
            Assert.fail(msg);
        }
        Link link = getLink(name);
        if (link == null) {
            if (!doThrow) {
                PSLogger.warn("Can not find link for " + name);
                PSLogger.save();
                return;
            }
            Assert.fail("Can not find link for " + name);
        }
        link.click(false);
        waitForUnvisible();
    }
}
