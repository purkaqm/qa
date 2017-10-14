package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.Menu;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 13:56:54
 */
public class ActionsMenu {
    private UIPage ui;
    private TestData data;

    public ActionsMenu(UIPage ui, TestData data) {
        this.ui = ui;
        this.data = data;
    }

    public void testMenu(int id) {
        Menu menu = ui.getMenu(id);
        menu.open();
        List<String> items = menu.getMenuItems();
        PSLogger.info(items);
        Assert.assertEquals(items, data.getMenuItems(String.valueOf(id)),
                "Incorrect menu items, should be " + data.getMenuItems(String.valueOf(id)));
        for (String item : items) {
            menu.close();
            menu.open();
            menu.call(item);
        }
        for (int i = 0; i < items.size(); i++) {
            boolean expected = data.hasMenuItemSubmenu(String.valueOf(id), i);
            Assert.assertEquals(menu.hasSubmenu(items.get(i)), expected,
                    "Item " + items.get(i) + (expected ? " has " : " has not ") + " submenu");
        }
    }

    public void testSubmenu() {
        Menu parent = ui.getMenu(4);
        parent.open();
        Menu child = parent.callWithSubmenu(data.getMenuItemSubmenu("4"));
        List<String> items = child.getMenuItems();
        PSLogger.info(items);
        Assert.assertEquals(items, data.getMenuItems("submenu"),
                "Incorrect submenu items, should be " + data.getMenuItems("submenu"));
        for (String item : items) {
            child.call(item);
            parent.open();
            child = parent.callWithSubmenu(data.getMenuItemSubmenu("4"));
        }
        parent.close();
        for (int i = 0; i < items.size(); i++) {
            boolean expected = data.hasMenuItemSubmenu("submenu", i);
            Assert.assertEquals(child.hasSubmenu(items.get(i)), expected,
                    "Submenu item " + items.get(i) + (expected ? " has " : " has not ") + " own submenu");
        }
    }
}
