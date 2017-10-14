package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.UserProfilePageLocators.EDIT_USER_TAB;
import static com.powersteeringsoftware.libs.enums.page_locators.UserProfilePageLocators.URL;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.02.11
 * Time: 19:58
 */
public class UserProfilePage extends PSPage {

    public UserProfilePage(String profileId) {
        this.url = URL.getLocator() + profileId;
    }

    public UserProfilePage() {
        // empty
    }


    @Override
    public void open() {
        open(url);
    }

    public void open(String url) {
        if (url == null || !url.contains(URL.getLocator())) throw new IllegalArgumentException(
                "Incorrect url<" + url + "> specified for " + getClass().getSimpleName());
        super.open(url);
        checkUrl();
    }

    public ProfilePage edit() {
        new Link(EDIT_USER_TAB).clickAndWaitNextPage();
        ProfilePage page = new ProfilePage();
        page.getDocument();
        return page;
    }
}
