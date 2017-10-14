package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.EditObjectTypePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.03.13
 * Time: 1:11
 * To change this template use File | Settings | File Templates.
 */
public class EditObjectTypePage extends PSPage {
    @Override
    public void open() {
        //todo
    }

    public void setName(String name) {
        new Input(NAME).type(name);
    }

    public void setPluralName(String name) {
        new Input(PLURAL_NAME).type(name);
    }

    public void copy() {
        new Button(COPY).submit();
        refreshBlankPage();
    }

    public void update() {
        new Button(UPDATE).submit();
        refreshBlankPage();
    }

    public ObjectTypesPage back() {
        new Link(BACK).clickAndWaitNextPage();
        ObjectTypesPage res = new ObjectTypesPage();
        res.refreshBlankPage();
        return res;
    }
}
