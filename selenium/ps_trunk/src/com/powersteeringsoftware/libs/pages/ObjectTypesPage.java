package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.page_locators.ObjectTypesPageLocators;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.03.13
 * Time: 0:42
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTypesPage extends PSPage {
    @Override
    public void open() {
        clickAdminObjectTypes();
    }

    public EditObjectTypePage edit(String name) {
        Link link = new Link(ObjectTypesPageLocators.LINK.replace(name));
        link.clickAndWaitNextPage();
        EditObjectTypePage res = new EditObjectTypePage();
        res.refreshBlankPage();
        return res;
    }
}
