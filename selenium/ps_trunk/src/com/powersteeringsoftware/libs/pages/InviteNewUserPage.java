package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.UserMenu;

import static com.powersteeringsoftware.libs.enums.page_locators.InviteNewUserPageLocators.*;


/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class InviteNewUserPage extends PSPage {
    @Override
    public void open() {
        clickBrowseInviteNewUser();
    }

    public void setFirstName(String t) {
        new Input(FIRST_NAME).type(t);
    }

    public void setLastName(String t) {
        new Input(LAST_NAME).type(t);
    }

    public void setEmail(String t) {
        new Input(EMAIL).type(t);
    }

    public void submit() {
        new Button(SUBMIT).submit();
    }

    public UserMenu getUserMenu() {
        Element message = getMessageBox();
        if (!message.exists()) return null;
        return new UserMenu(message.getChildByXpath(USER_LINK));
    }

    public UserProfilePage toProfile() {
        UserMenu menu = getUserMenu();
        if (menu == null) return null;
        menu.open();
        menu.getMoreLink().click(true);
        return new UserProfilePage();
    }
}
