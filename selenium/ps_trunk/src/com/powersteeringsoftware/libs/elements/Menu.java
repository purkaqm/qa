package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.dom4j.tree.DefaultElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.elements_locators.MenuLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 22:41:15
 * To change this template use File | Settings | File Templates.
 */
public class Menu extends Element implements LinkMenu {

    protected Element popup;
    private Menu parent;
    protected static final int OPEN_ITERATIONS = 3;
    private Map<String, MenuItem> items = new LinkedHashMap<String, MenuItem>();
    private static final TimerWaiter WAITER = new TimerWaiter(2000);
    protected static final long CLOSE_TIMEOUT = 5000; // ms
    private static final long SEARCH_POPUP_TIMEOUT = 6000; // ms
    private boolean howToOpen;

    public Menu(ILocatorable locator) {
        super(locator);
    }

    public Menu(String locator) {
        super(locator);
    }

    public Menu(Element e) {
        super(e);
    }

    /**
     * @param e         - Element
     * @param howToOpen - if true open using click
     */
    public Menu(Element e, boolean howToOpen) {
        super(e);
        this.howToOpen = howToOpen;
    }

    public Menu(String loc, boolean howToOpen) {
        super(loc);
        this.howToOpen = howToOpen;
    }

    public Menu(ILocatorable loc, boolean flag) {
        this(loc.getLocator(), flag);
    }

    public void open() {
        for (int i = 0; i < OPEN_ITERATIONS; i++) { // for stabilization
            try {
                _open();
                return;
            } catch (AssertionError ae) {
                PSLogger.warn(getClass().getSimpleName() + ".open: " + ae);
            }
        }
        Assert.fail("Can't open " + getClass().getSimpleName() + " menu ");
    }

    protected void _open() {
        justOpen();
        setMenuElementAndItems();
        PSLogger.debug("all menu items: " + items.keySet());
    }

    private void justOpen() {
        PSLogger.debug("Open menu {" + this + "}");
        if (getFrame() != null) getFrame().select();
        focus();
        if (howToOpen)
            click(false);
        else
            mouseDownAndUp();
    }

    public void setHowToOpen(boolean flag) {
        howToOpen = flag;
    }

    public void close() {
        if (popup == null) {
            PSLogger.debug("Popup element not found to close");
            return;
        }
        PSLogger.debug("Close menu");
        Element parent = getParent();
        parent.focus(); // ?
        if (getDriver().getType().isWebDriverIE()) {
            parent.mouseDownAndUp();
        }
        try {
            popup.waitForUnvisible(CLOSE_TIMEOUT);
        } catch (Wait.WaitTimedOutException we1) {
            // ignore ex
            PSLogger.warn(we1);
            parent = parent.getVisibleParent();
            if (parent == null) return;
            PSLogger.debug(parent + "," + parent.isVisible());
            try {
                parent.click(false);
                popup.waitForUnvisible(SEARCH_POPUP_TIMEOUT);
            } catch (SeleniumException se) {
                PSLogger.error(se);
            } catch (Wait.WaitTimedOutException we2) {
                // ignore ex
                PSLogger.warn(we2);
            }
        }
        setDynamic();
    }

    public void setDynamic() {
        popup = null;
    }

    public boolean isMenuOpen() {
        return popup != null;
    }

    private void setMenuElementAndItems() {
        popup = waitPopup(1);
        Assert.assertNotNull(popup, "Can't find any popup");
        setMenuItemsMap();
    }

    /**
     * wait when popups are appeared
     *
     * @param expectedPopupsNumber
     * @return last popup Element
     */
    private Element waitPopup(int expectedPopupsNumber) {
        List<Element> pops;
        long s1 = System.currentTimeMillis();
        WAITER.waitTime();
        while ((pops = getMenuPopups(getDriver().getDocument(null))).size() < expectedPopupsNumber) {
            if (System.currentTimeMillis() - s1 > SEARCH_POPUP_TIMEOUT) {
                PSLogger.warn("Should be " + expectedPopupsNumber + " popups. Now " + pops.size() +
                        " (after " + SEARCH_POPUP_TIMEOUT + " ms)");
                return null;
            }
            WAITER.waitTime();
        }
        if (popup != null) {
            pops.remove(popup);
            --expectedPopupsNumber;
        }
        PSLogger.debug("There are " + pops.size() + " popups on page, expected " + expectedPopupsNumber);
        return pops.size() == 0 || pops.size() < expectedPopupsNumber ? null : pops.get(pops.size() - 1); // get last
    }


    private static List<Element> getMenuPopups(Document doc) {
        List<Element> res = new ArrayList<Element>();
        for (Element popup : Element.searchElementsByXpath(doc, MENU)) {
            String attr = popup.getDEStyle();
            if (!attr.toLowerCase().matches(STYLE_NONE) &&
                    !attr.toLowerCase().matches(STYLE_HIDDEN)) {
                res.add(new Element(popup) {
                    public boolean equals(Object o) {
                        return o != null && o instanceof Element &&
                                getDEAttribute(STYLE_WIDGET).equals(((Element) o).getDEAttribute(STYLE_WIDGET)) &&
                                getDEAttribute("id").equals(((Element) o).getDEAttribute("id"));
                    }
                });
            }
        }
        return res;
    }

    public void call(ILocatorable item) {
        call(item.getLocator());
    }

    public void call(String item) {
        PSLogger.info("Click on '" + item + "' in sub-menu");
        MenuItem e = checkItem(item);
        e.click(false);
        if (e.disabled) // disabled
            return;
        if (e.checkBox == null) { // has not checkbox:
            if (!e.hasSubmenu) {
                popup.waitForUnvisible(CLOSE_TIMEOUT);
                popup = null; // then current popup is closed
            }
            if (parent != null) {
                parent.popup.waitForUnvisible(CLOSE_TIMEOUT);
                parent.popup = null; // parent popup is closed too.
            }
        }
    }


    public boolean isSelected(String item) {
        checkItemCheckbox(item);
        return items.get(item).checkBox.getChecked();
    }

    public boolean isSelected(ILocatorable item) {
        return isSelected(item.getLocator());
    }

    public void select(ILocatorable item) {
        select(item.getLocator(), true);
    }

    public void unSelect(ILocatorable item) {
        select(item.getLocator(), false);
    }

    public void select(String item, boolean yes) {
        PSLogger.debug("Click on checkbox for '" + item + "' in sub-menu");
        checkItemCheckbox(item);
        items.get(item).checkBox.select(yes);
        PSLogger.save();
    }

    public boolean doSelect(String item, boolean yes) {
        checkItemCheckbox(item);
        return items.get(item).checkBox.doSelect(yes);
    }

    public boolean doSelect(ILocatorable item, boolean yes) {
        return doSelect(item.getLocator(), yes);
    }

    public Menu callWithSubmenu(ILocatorable item) {
        return callWithSubmenu(item.getLocator());
    }

    public Menu callWithSubmenu(String item) {
        PSLogger.debug("Call sub-menu for parent item '" + item + "'");
        Menu sub = null;
        for (int i = 0; i < OPEN_ITERATIONS; i++) { // for stabilization
            try {
                sub = new Menu(items.get(item));
                openSubPopup(sub);
                break;
            } catch (AssertionError ae) {
                PSLogger.warn(getClass().getSimpleName() + ".openSubPopup(" + item + "),#" + (i + 1) + ": " + ae);
                sub = null;
                if (popup == null || !popup.isVisible()) {
                    PSLogger.save("No parent menu!");
                    _open();
                }
            }
        }
        Assert.assertNotNull(sub, "Can't find any popups in second iteration.");
        sub.setMenuItemsMap();
        PSLogger.debug("Submenu items " + sub.items.keySet());
        return sub;
    }

    private void openSubPopup(Menu sub) {
        Assert.assertNotNull(popup, "Can't find any popup for parent menu.");
        sub.click(false);
        sub.popup = waitPopup(2);
        Assert.assertNotNull(sub.popup, "Can't find any popup for submenu.");
        sub.parent = this;
    }

/*
    public void closeSubMenus() {
        popup.click(false);
        waitPopups(1);
    }
*/

    public boolean containsItem(String item) {
        return items != null && items.containsKey(item);
    }

    public MenuItem checkItem(String item) {
        if (!containsItem(item)) {
            Assert.fail("Can't find item '" + item + "' or it is invisible. " + items.keySet());
        }
        return items.get(item);
    }

    public void checkItemCheckbox(String item) {
        checkItem(item);
        Assert.assertNotNull(items.get(item).checkBox, "Can't find checkbox for item with name '" + item + "'");
        Assert.assertFalse(items.get(item).disabled, "Item '" + item + "' is disabled");
    }


    public boolean containsItem(ILocatorable item) {
        return containsItem(item.getLocator());
    }

    public boolean hasSubmenu(String item) {
        return items.get(item).hasSubmenu;
    }

    public Element getMenuItem(String name) {
        Assert.assertTrue(items.containsKey(name), "Can't find menu item '" + name + "'");
        return items.get(name);
    }

    public Element getMenuItem(ILocatorable lab) {
        return getMenuItem(lab.getLocator());
    }

    public List<String> getMenuItems() {
        return new ArrayList<String>(items.keySet());
    }

    public Link getMenuLink(ILocatorable lab) {
        return getMenuLink(lab.getLocator());
    }

    public Link getMenuLink(String lab) {
        return ((MenuItem) getMenuItem(lab)).link;
    }

    public List<Element> getItemsList() {
        return new ArrayList<Element>(items.values());
    }

    public List<CheckBox> getCheckBoxesList() {
        List<CheckBox> res = new ArrayList<CheckBox>();
        for (String item : items.keySet()) {
            if (items.get(item).checkBox != null)
                res.add(items.get(item).checkBox);
        }
        return res;
    }

    public CheckBox getCheckBox(String name) {
        return ((MenuItem) getMenuItem(name)).checkBox;
    }

    private void setMenuItemsMap() {
        items.clear();
        for (Element row : Element.searchElementsByXpath(popup, ROW)) {
            String clazz = row.getDEClass();
            Element item = row.getChildByXpath(ITEM);
            if (!item.isDEPresent()) {
                // seems no items. it is separator.
                continue;
            }
            String attr = item.getParent().getDEAttribute(HAS_SUBMENU_ATTR);
            MenuItem myItem = new MenuItem(item,
                    searchInnerText(item.getDefaultElement()),
                    attr != null && attr.toLowerCase().equals(HAS_SUBMENU_TRUE));
            // dynamic checking
            /*if (myItem.isVisible()) {
                items.put(myItem.name, myItem);
            } else {
                //PSLogger.debug("Item is not visible: " + row.asXML());
                continue;
            }*/
            // static checking:
            if (!clazz.contains(ROW_HIDDEN.getLocator())) { // visible:
                items.put(myItem.name, myItem);
            } else {
                continue;
            }
            myItem.disabled = clazz != null && clazz.contains(ROW_DISABLED.getLocator());
            Element input = row.getChildByXpath(INPUT);
            if (input.isDEPresent()) { // has checkbox or radiobutton
                String inputType = input.getDEAttribute(INPUT_TYPE);
                String inputClass = input.getDEAttribute(INPUT_CLASS);
                if (inputType != null && inputType.equalsIgnoreCase("radio")) {
                    myItem.checkBox = new RadioButton(input);
                } else if (inputClass != null && inputClass.contains(INPUT_CLASS_DIJIT.getLocator())) {
                    myItem.checkBox = new DijitCheckBox(input);
                } else {
                    myItem.checkBox = new CheckBox(input);
                }
                myItem.checkBox.setName(myItem.name);
            }
            Element link = row.getChildByXpath(LINK);
            if (link.isDEPresent()) myItem.link = new Link(link);
        }
    }

    private static String searchInnerText(DefaultElement e) {
        String res = e.getTextTrim();
        if (res != null && !res.isEmpty()) return res;
        for (Object ch : e.elements()) {
            res = searchInnerText((DefaultElement) ch);
            if (res != null) return res;
        }
        return null;
    }

    public static class MenuItem extends Element {
        private boolean hasSubmenu;
        private String name;
        private CheckBox checkBox;
        private Link link;
        //private RadioButton radioButton;
        private boolean disabled;

        MenuItem(Element e, String name, boolean hasSubmenu) {
            super(e);
            this.name = name;
            this.hasSubmenu = hasSubmenu;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    public String asXML() {
        if (popup != null && popup.isDEPresent()) return popup.asXML();
        return super.asXML();
    }
}
