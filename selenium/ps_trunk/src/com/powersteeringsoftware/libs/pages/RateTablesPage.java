package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.RateTablesPageLocators.*;

/**
 * Created by admin on 10.04.2014.
 */
public class RateTablesPage extends ResourceRatesPage {

    public class TablesDialog extends Dialog {
        public TablesDialog() {
            super(DIALOG_NEW);
        }

        public void setName(String n) {
            new Input(DIALOG_NEW_NAME).type(n);
        }

        public void setDescription(String d) {
            new TextArea(DIALOG_NEW_DESC).setValue(d);
        }

        public void submit() {
            new Button(DIALOG_NEW_SUBMIT).click(false);
            RateTablesPage.this.waitForPageToLoad();
        }
    }

    public TablesDialog addNew() {
        new Button(NEW).click(false);
        TablesDialog res = new TablesDialog();
        res.waitForVisible();
        return res;
    }

    protected class Row extends ARow {
        private Link name;
        private Menu menu;
        private String desc;
        private boolean isDef;

        public String toString() {
            return name.getName() + "," + desc + "," + isDef;
        }
    }

    public List<ARow> getRows() {
        if (rows != null) return rows;
        rows = new ArrayList<ARow>();
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
            r.desc = e.getChildByXpath(TABLE_ROW_DESCRIPTION).getDEText();
            r.isDef = e.getChildByXpath(TABLE_ROW_DEF).isDEPresent();
            rows.add(r);
        }
        return rows;
    }

    public List<String> getTables() {
        List<String> res = new ArrayList<String>();
        for (ARow _r : getRows()) {
            res.add(((Row) _r).name.getName());
        }
        return res;
    }

    public String getDefault() {
        List<String> res = new ArrayList<String>();
        for (ARow _r : getRows()) {
            if (((Row) _r).isDef)
                res.add(((Row) _r).name.getName());
        }
        Assert.assertTrue(res.size() == 1, "Should be one default table");
        return res.get(0);
    }

    public Row getTable(String name) {
        for (ARow _r : getRows()) {
            Row r = (Row) _r;
            if (name.equals(r.name.getName())) {
                return r;
            }
        }
        return null;
    }

    public RateTableViewPage openTable(String name) {
        PSLogger.info("Open rate table '" + name + "'");
        Row r = getTable(name);
        if (r != null) {
            r.name.clickAndWaitNextPage();
            RateTableViewPage res = new RateTableViewPage();
            res.reset();
            return res;

        }
        Assert.fail("Can't find rate table '" + name + "'");
        return null;
    }

    public Menu callSubMenu(String name) {
        PSLogger.info("Call sub-menu for rate table '" + name + "'");
        Row r = getTable(name);
        if (r != null) {
            r.menu.open();
            return r.menu;
        }
        Assert.fail("Can't find rate table '" + name + "'");
        return null;
    }

    public void setDefault(String name) {
        Menu menu = callSubMenu(name);
        menu.call(MENU_DEFAULT);
        SetDefaultDialog dialog = new SetDefaultDialog();
        dialog.waitForVisible();
        dialog.ok();
    }

    public class SetDefaultDialog extends Dialog {

        private SetDefaultDialog() {
            super(DIALOG_DEFAULT);
        }

        public void ok() {
            getChildByXpath(DIALOG_DEFAULT_OK).click(false);
            RateTablesPage.this.waitForPageToLoad();
        }

    }
}
