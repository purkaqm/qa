package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.Locators;
import org.dom4j.tree.DefaultElement;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.FrameLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.12.2010
 * Time: 18:12:10
 */
public class Frame extends Element {
    private boolean simplified;
    private static final long LOADING_TIMEOUT = CoreProperties.getWaitForElementToLoad() / 2; // ms

    public Frame(ILocatorable loc) {
        this(loc.getLocator());
    }

    /**
     * constructor for first-level frame
     * has parent "relative=top"
     *
     * @param _this - locator for frame.
     */
    public Frame(String _this) {
        super(_this, false);
        if (!_this.equals(TOP.getLocator())) {
            init(TOP.getLocator());
        }
    }

    /**
     * constructor for nested frames.
     *
     * @param _parent - parent frame loc.
     * @param _this   - this frame loc.
     */
    private Frame(String _parent, String _this) {
        super(_this, false);
        init(_parent);
    }

    /**
     * constructor for nested frames.
     *
     * @param _parent - parent frame loc.
     * @param _this   - this frame loc.
     */
    public Frame(ILocatorable parent, ILocatorable _this) {
        this(parent.getLocator(), _this.getLocator());
    }

    private void init(String _parent) {
        setFrame(new Frame(_parent));
        reInit();
    }

    /**
     * @param locs - array with frames locators: parentN, ..., parent0, this
     */
    public Frame(ILocatorable... locs) {
        this(locs[locs.length - 2], locs[locs.length - 1]);
        getFrame().setFrame(locs.length > 3 ? new Frame(locs[locs.length - 3], locs[locs.length - 2]) : new Frame(locs[0]));
    }

    private boolean isRelative() {
        return initialLocator.startsWith(RELATIVE.getLocator());
    }

    private boolean isParent() {
        return getFrame() == null;
    }

    /**
     * select parent frame
     */
    public void selectUp() {
        if (getBrowser().isWebDriver()) { // unsupported relative=up :
            getFrame().select();
        } else {
            superSelect(UPPER.getLocator());
        }
    }

    private void selectTop() {
        superSelect(TOP.getLocator());
    }

    private void superSelect(String loc) {
        getDriver().selectFrame(loc);
    }

    /**
     * select from top level
     */
    public void select() {
        if (!isParent())
            getFrame().select();
        superSelect(locator);
    }

    public void setDefaultElement() {
        if (!isParent()) {
            getFrame().setDefaultElement();
            reInit(getFrame().defaultElement);
        }

        superSelect(locator);
        defaultElement = (DefaultElement) getDriver().getDocument(null).selectSingleNode("//body");
        xpath = "//" + defaultElement.getName();
    }

    private void reInit() {
        if (simplified) return;
        if (!getBrowser().isRCDriverIE()) return;
        if (Locators.isSimpleLocator(locator)) {
            locator = Locators.any2frame(locator);
        } else if (Locators.isXpath(locator)) {
            String res = Locators.xpath2simple(locator);
            if (res != null) locator = Locators.any2frame(res);
            else return;
        }
        simplified = true;
    }

    private void reInit(DefaultElement parent) {
        if (simplified) return;
        String res = parseLoc(parent);
        if (getBrowser().isRCDriverIE() && Locators.isSimpleLocator(res)) res = Locators.any2frame(res);
        locator = res;
        simplified = true;
    }

    private String parseLoc(DefaultElement parent) {
        // xpath to name, id or number
        if (Locators.isXpath(initialLocator)) {
            DefaultElement node = (DefaultElement) parent.selectSingleNode(initialLocator);
            for (String attrName : new String[]{"id", "name"}) {
                String attrVal;
                if ((attrVal = node.attributeValue(attrName)) != null && !attrVal.isEmpty()
                        && parent.selectNodes("//*[@" + attrName + "='" + attrVal + "']").size() == 1) {
                    return attrVal;
                }
            }
            // parse number. it is experimental
            if (getBrowser().isRCDriverIE())
                for (String fr : new String[]{FRAME.getLocator(), IFRAME.getLocator()}) {
                    List nodes = parent.selectNodes(fr);
                    int res = nodes.indexOf(node);
                    if (res != -1) {
                        return String.valueOf(res);
                    }
                }
        }
        // frame to name, id or number
        if (Locators.isFrame(initialLocator) && !getBrowser().isRCDriverIE()) {
            return Locators.frame2any(initialLocator);
        }
        return locator;
    }

    public boolean exists() {
        if (isRelative()) return true;
        return super.exists();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Frame)) return false;
        if (isParent() && ((Frame) o).isParent()) return true;
        return locator.equals(((Frame) o).locator);
    }

    public String toString() {
        return "frame[" + ((getFrame() != null ? getFrame().toString() + " > " : "") + initialLocator) + "]";
    }

    protected Element getElement(ILocatorable loc) {
        Element h = new Element(loc);
        h.setFrame(this);
        return h;
    }

    public void waitForLoad() {
        waitForLoad(LOADING_TIMEOUT);
    }

    public void waitForLoad(long timeout) {
        if (isParent()) return;
        getFrame().waitForLoad(timeout);
        getDriver().waitForFrameToLoad(locator, String.valueOf(timeout));
        select();
    }

    public void waitForReload(PSPage page) {
        if (getBrowser().isGoogleChrome() || getBrowser().isRCDriverIE()) {
            waitForLoad();
        } else {
            selectTop();
            page.waitForPageToLoad(false);
        }
        if (getBrowser().isWebDriver()) {
            waitForLoad(); // its for web-driver
        }
        select(); // its for web-driver
        PSLogger.save("After reload frame " + locator);
        //page.validate();
    }

    public void waitForUnLoad(PSPage page) {
        if (getBrowser().isWebDriverFF()) {
            selectUp();
            waitForDisapeared();
        }
        selectTop();
        page.waitForPageToLoad();
        PSLogger.save("After unload frame " + locator);
    }

    private BrowserTypes getBrowser() {
        return getDriver().getType();
    }
}
