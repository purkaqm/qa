package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.FlatTagChooserLocators.*;

/**
 * Created by IntelliJ IDEA.
 * ToDo: this class is not ready
 * User: szuev
 * Date: 01.07.2010
 * Time: 12:34:19
 */
public class FlatTagChooser extends Element implements ITagChooser {
    //public static final String CLEAR_SELECT = "(clear)";
    private static final long OPEN_TIMEOUT = 30000; // ms
    private static final int ITERATIONS_NUM_TO_OPEN = 3;
    private static final TimerWaiter ITERATIONS_TIMEOUT = new TimerWaiter(1000);
    private static final TimerWaiter AFTER_TIMEOUT = new TimerWaiter(2000);

    private String name;
    private boolean howToOpen; // if false then click
    Element popup;
    Input in;
    List<TagChooser.Item> items = new ArrayList<TagChooser.Item>();

    public FlatTagChooser(ILocatorable locator) {
        this(locator.getLocator());
    }

    public FlatTagChooser(String locator) {
        super(locator);
    }

    public FlatTagChooser(Element e) {
        this(e, false);
    }

    public FlatTagChooser(Element e, boolean howToOpen) {
        super(e);
        this.howToOpen = howToOpen;
    }

    public FlatTagChooser(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public Input getInput() {
        if (in == null) return in = new Input(getChildByXpath(INPUT));
        return in;
    }

    public Element getPopup() {
        if (popup == null) {
            popup = new DijitPopup();
            //popup.setSimpleLocator();
        }
        return popup;
    }

    public void openPopup() {
        for (int i = 0; i < ITERATIONS_NUM_TO_OPEN; i++) {
            try {
                _openPopup();
                return;
            } catch (Wait.WaitTimedOutException ex) { // hotfix for ie
                PSLogger.warn(getClass().getSimpleName() + ".openPopup : " + ex);
                PSLogger.saveFull();
                mouseOut();
                ITERATIONS_TIMEOUT.waitTime();
            }
        }
        Assert.fail("Can't open tag-chooser popup");

    }

    /**
     * debug hotfix for ie.
     */
    private void _openPopup() {
        popup = null;
        focus();
        if (howToOpen) {
            mouseDownAndUp();
        } else {
            click(false);
        }
        // todo. mb we should do smth else to stable open the popup under ie-rc?
        waitForPopup();
    }

    public void closePopup() {
        set(getContent());
        /*
        mouseOut();
        getParent().click(false);
        try {
            getPopup().waitForUnvisible(OPEN_TIMEOUT);
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.warn(w);
        }*/
    }

    public void waitForPopup() {
        waitForPopup(OPEN_TIMEOUT);
        AFTER_TIMEOUT.waitTime();
    }

    public void waitForPopup(long timeout) {
        getPopup().waitForVisible(timeout);
        TagChooser.waitWhileLoading(this);
        searchForItems(ITEM);
    }

    void searchForItems(ILocatorable loc) {
        items.clear();
        for (Element element : Element.searchElementsByXpath(getPopup(), loc)) {
            //item.setSimpleLocator();
            String isVisible = element.getParent().getDefaultElement().attributeValue("style");
            if (isVisible == null)
                isVisible = element.getDefaultElement().attributeValue("style");
            if (isVisible == null || !isVisible.toLowerCase().contains("display: none")) {
                Element link = element.getChildByXpath(ITEM_LINK);
                String txt = link.isDEPresent() ? link.getDEText() : element.getDEText();
                TagChooser.Item item = new TagChooser.Item(txt);
                item.set(element);
                items.add(item);
            }
        }
        PSLogger.info("all items: " + items);
    }

    public List<String> getAllLabels() {
        List<String> res = new ArrayList<String>();
        for (TagChooser.Item item : items) {
            res.add(item.getName());
        }
        return res;
    }

    @Override
    public void setName(String label) {
        name = label;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name == null ? super.toString() : (name + "[" + FlatTagChooser.class.getSimpleName() + "]");
    }

    public List<String> getAllLabelsWithoutClear() {
        List<String> res = getAllLabels();
        res.remove(0);
        return res;
    }


    protected void _set(String label) {
        getItem(label).mouseDownAndUp();
        getPopup().waitForUnvisible();
    }

    public void set(String label) {
        _set(label);
        AFTER_TIMEOUT.waitTime();
    }

    Element getItem(String label) {
        return getItem(label, true);
    }

    Element getItem(String label, boolean searchFirst) {
        List<String> all = getAllLabels();
        int index = searchFirst ? all.indexOf(label) : all.lastIndexOf(label);
        if (index == -1) {
            PSLogger.warn("Can't find item '" + label + "': " + all);
            PSLogger.debug2("popup: " + getPopup().asXML());
            PSLogger.save();
            return null;
        }
        return items.get(index).get();
    }

    public void setLabel(ILocatorable label) {
        setLabel(label.getLocator());
    }

    public void setLabel(String label) {
        PSLogger.debug("Select item '" + label + "'");
        openPopup();
        if (!getPopup().isVisible() && getDriver().getType().isRCDriverIE()) { //hotfix for ie.
            PSLogger.warn("Popup is not visible, try to open again.");
            openPopup();
        }
        PSLogger.save("before selecting '" + label + "'");
        set(label);
    }

    public void setContent(String txt) {
        getInput().type(txt);
    }

    public String getContent() {
        return getInput().getValue();
    }

    public String getContentString() {
        return getContent();
    }

    @Deprecated
    public void search(String txt) {
        //todo: this method doesn't work correct!
        focus();
        getInput().type(txt);
        click(false);
        waitForPopup();
    }


}
