package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.StrUtil;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.LinksMenuLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 25.05.12
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */
public class LinksMenu extends Element {
    private DijitPopup popup;
    private List<Link> items = new ArrayList<Link>();

    public LinksMenu(Element e) {
        super(e);
    }

    public void open() {
        mouseDownAndUp();
        popup = new DijitPopup();
        popup.waitForVisible();
        items.clear();
        for (Element e : Element.searchElementsByXpath(popup, LINK)) {
            items.add(new Item(e));
        }
        PSLogger.debug("All items: " + items);
    }

    public List<Link> getLinks() {
        return items;
    }

    public Link getLink(String parent, String name) {
        for (Link l : getLinks()) {
            Item i = (Item) l;
            if (parent != null && !i.parent.equals(parent)) continue;
            if (i.name.equals(name)) return l;
        }
        return null;
    }

    public Link getLink(ILocatorable loc) {
        return getLink(null, loc.getLocator());
    }


    private class Item extends Link {
        private String parent;

        public Item(Element e) {
            super(e);
            parent = "";
            name = StrUtil.trim(getDEText());
            Element li = getParent();
            if (INDENT.getLocator().equals(li.getDEClass())) {
                // has parent.
                List<Element> parents = Element.searchElementsByXpath(li.getParent(), PARENT);
                if (parents.size() != 0) {
                    parent = StrUtil.trim(parents.get(parents.size() - 1).getDEText());
                }
            }
        }

        public String toString() {
            return parent + name;
        }
    }
}
