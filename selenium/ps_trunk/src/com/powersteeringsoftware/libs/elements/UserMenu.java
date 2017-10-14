package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.elements_locators.UserMenuLocators;

/**
* Created with IntelliJ IDEA.
* User: zss
* Date: 11.09.12
* Time: 21:58
* To change this template use File | Settings | File Templates.
*/
public class UserMenu extends DijitPopup {
    private Element link;

    public UserMenu(Element link) {
        super();
        this.link = link;
    }

    public void open() {
        link.focus();
        link.mouseDownAndUp();
        waitForVisible();
        setDefaultElement();
    }

    public Element getMoreLink() {
        return Element.searchElementByXpath(this, UserMenuLocators.USER_POPUP_MORE_LINK);
    }
}
