package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.ComboBoxLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.06.2010
 * Time: 15:58:09
 */
public class ComboBox extends Element {
    private static final String EMPTY = "";
    private static final long ALERT_TIMEOUT = 10000; // ms
    private static final long TIMEOUT_FOR_CLASS = 3000;
    Element popup;
    private List<Element> items = new ArrayList<Element>();
    protected String arrow;
    private boolean hasAlert;
    private boolean useOldWay;
    private Boolean useClick;

    public ComboBox(ILocatorable locator) {
        super(locator);
    }

    public ComboBox(String locator) {
        super(locator);
    }

    public ComboBox(Element e) {
        super(e);
    }

    public void open() {
        PSLogger.info("Open combobox");
        Assert.assertFalse(isDisabled(), "Combobox is disabled");
        checkForUnexpectedAlert();
        Element open = getChildByXpath(getArrowLocator());
        if (howToOpen()) {
            open.click(false);
        } else {
            open.mouseDownAndUp();
        }
        waitForVisible();
    }

    public void setLocatorToOpen(String loc) {
        arrow = loc;
    }

    protected String getArrowLocator() {
        return arrow == null ? arrow = ARROW.getLocator() : arrow;
    }

    protected boolean howToOpen() {
        return useClick == null ? useClick = getDriver().getType().isWebDriver() : useClick;
    }

    public void waitForVisible() {
        if (popup == null) {
            popup = new DijitPopup();
        }
        popup.waitForVisible();
        waitForPopupLoading();
        popup.setDefaultElement();
        popup.setId();
        PSLogger.info(popup.getId());
        PSLogger.debug(popup.asXML());
        if (items.size() != 0) return;
        searchItems();
        setDefaultElement(popup.getParentDoc()); // set element from DijitPopup
    }

    public DijitPopup getPopup() {
        return (DijitPopup) popup;
    }

    protected void waitForPopupLoading() {
    }

    public void searchItems() {
        items.clear();
        items.addAll(Element.searchElementsByXpath(popup, useOldWay ? ITEM.getAlternativeLocator() : ITEM.getLocator()));
    }

    public void close() {
        if (popup == null) {
            PSLogger.warn("Combobox popup not found");
            return;
        }
        PSLogger.info("Close combobox popup");
        if (!CoreProperties.getBrowser().isIE())
            mouseDownAndUpWithAlert(getParent(), hasAlert);
        else
            getParent().mouseDownAndUp();
        popup.waitForUnvisible();
    }

    public void select(ILocatorable label) {
        this.select(label.getLocator());
    }

    public void select(String label) {
        PSLogger.info("Select item " + label);
        open();
        set(label);
    }

    public void set(String label) {
        for (Element item : items) {
            if (getItemText(item).equals(label)) {
                mouseDownAndUpWithAlert(item, hasAlert);
                popup.waitForUnvisible();
                return;
            }
        }
        PSLogger.warn("Can't find item " + label + " in combobox");
        PSLogger.save();
    }

    public List<String> getOptions() {
        List<String> res = new ArrayList<String>();
        for (Element e : items) {
            res.add(getItemText(e));
        }
        return res;
    }

    private static String getItemText(Element item) {
        String res = item.getDefaultElement().getText();
        if (res.equals(EMPTY_ITEM.getLocator())) return EMPTY;
        return res;
    }

    public Input getHiddenInput() {
        return new Input(getChildByXpath(VALUE_INPUT));
    }

    public Input getInput() {
        return new Input(getChildByXpath(INPUT));
    }

    public void type(String txt) {
        PSLogger.info("Set text '" + txt + "' to combobox " + getLocator());
        getInput().type(txt);
        getInput().keyPress(" ");
    }

    public String getValue() {
        return getInput().getValue();
    }

    public String getSelectedLabel() {
        return getChildByXpath(INPUT).getValue();
    }

    // todo: don't work for safari

    public boolean isWrongInput() {
        if (CoreProperties.getBrowser().isIE()) {
            mouseDownAndUp();
        }
        if (CoreProperties.getBrowser().isChromeOrSafari()) {
            getInput().keyPress(" ");
        }
        mouseDownAndUpWithAlert(getParent(), hasAlert);
        try {
            waitForClass(CLASS_ERROR_VALUE, TIMEOUT_FOR_CLASS);
            return true;
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.debug("Class is " + getElementClass());
            return false;
        }
    }

    protected void mouseDownAndUpWithAlert(Element e, boolean hasAlert) {
        try {
            e.mouseDownAndUp(hasAlert, ALERT_TIMEOUT);
        } catch (Wait.WaitTimedOutException ex) {
            PSLogger.warn(ComboBox.class.getSimpleName() + " : " + ex.getMessage()); // ignore if there is not alert. it is only for stupid UI page
        }
    }

    public void setAlert(ILocatorable hasAlert) {
        this.hasAlert = Boolean.parseBoolean(hasAlert.getLocator());
    }

    public void setOldWay(boolean useOldWay) {
        this.useOldWay = useOldWay;
    }

    /**
     * for 9.3
     *
     * @return
     */
    public boolean isDisabled() {
        return getElementClass().contains(CLASS_DISABLE_VALUE.getLocator());
    }
}
