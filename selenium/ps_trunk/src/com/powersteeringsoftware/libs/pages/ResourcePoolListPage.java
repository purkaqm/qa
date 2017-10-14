package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.Menu;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.ResourcePoolListPageLocators.*;

/**
 * Created by admin on 29.04.2014.
 */
public class ResourcePoolListPage extends PSPage {
    @Override
    public void open() {
        clickResourcePool();
    }

    public AddEditResourcePoolPage addNew() {
        new Button(NEW).click(false);
        AddEditResourcePoolPage res = new AddEditResourcePoolPage();
        res.waitForPageToLoad();
        return res;
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        reset();
    }

    protected void reset() {
        document = null;
        rows = null;
        PSLogger.debug("Rows: " + getRows());
    }

    public Row getPool(String name) {
        for (Row r : getRows()) {
            if (name.equals(r.name.getName())) {
                return r;
            }
        }
        return null;
    }

    public List<String> getPools() {
        List<String> res = new ArrayList<String>();
        for (Row _r : getRows()) {
            res.add(_r.name.getName());
        }
        return res;
    }

    public Menu callSubMenu(String name) {
        PSLogger.info("Call sub-menu for pool '" + name + "'");
        Row r = getPool(name);
        if (r != null) {
            r.menu.open();
            return r.menu;
        }
        Assert.fail("Can't find pool '" + name + "'");
        return null;
    }

    public List<Row> getRows() {
        if (rows != null) return rows;
        rows = new ArrayList<Row>();
        for (Element e : getElements(false, TABLE_ROW)) {
            Link link = null;
            Menu menu = null;
            for (Element _e : Element.searchElementsByXpath(e, TABLE_ROW_NAME)) {
                Link _l = new Link(_e);
                String text = _l.getDEText();
                if (link == null && !text.isEmpty()) {
                    link = _l;
                    link.setName(text);
                }
                if (menu == null && TABLE_ROW_NAME_MENU_ARROW_HREF.getLocator().equals(_l.getHref())) {
                    menu = new Menu(_l.getChildByXpath(TABLE_ROW_NAME_MENU_ARROW), false);
                }
            }
            if (link == null) continue;
            Row r = new Row();
            r.name = link;
            r.menu = menu;
            r.desc = e.getChildByXpath(TABLE_ROW_DESC).getDEText();
            Element man = e.getChildByXpath(TABLE_ROW_MANAGER);
            if (man != null && man.isDEPresent())
                r.manager = man.getDEText();
            rows.add(r);
        }
        return rows;
    }

    protected List<Row> rows;

    public class Row {
        private Link name;
        private Menu menu;
        String desc;
        String manager;
    }
}
