package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.ObjectTypesJspPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 29.03.13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTypesJspPage extends Page {

    public void open() {
        super.open(URL.getLocator());
    }

    public View edit(String name) {
        Link link = new Link(LINK.replace(name));
        link.clickAndWaitNextPage();
        return new View();
    }

    public class View extends Page {

        public Edit copy() {
            Button copy = new Button(EDIT_COPY);
            copy.submit();
            return new Edit();
        }

        public ObjectTypesJspPage back() {
            Link back = new Link(EDIT_BACK);
            back.clickAndWaitNextPage();
            return new ObjectTypesJspPage();
        }
    }

    public class Edit extends Page {
        public void setName(String name) {
            new Input(EDIT_NAME).type(name);
        }

        public void setPluralName(String name) {
            new Input(EDIT_PLURAL_NAME).type(name);
        }


        public View submit() {
            Button view = new Button(EDIT_SUBMIT);
            view.submit();
            return new View();
        }
    }
}
