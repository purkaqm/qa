package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.UserMenuLocators;
import com.powersteeringsoftware.libs.enums.page_locators.UserProfilePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.PeopleManageActivePageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.07.2010
 * Time: 14:06:10
 * Example of code:
 * PeopleManageActivePage page = new PeopleManageActivePage();
 * page.open();
 * page.search();
 * PSLogger.debug("Count " + page.getCount());
 * List<User> users = new ArrayList<User>();
 * do {
 * users.addAll(page.getUsers());
 * } while (page.next());
 */
public class PeopleManageActivePage extends PSPage {
    public void open() {
        clickBrowsePeople();
    }

    public void search() {
        new Button(SEARCH_BUTTON).submit();
        getDocument();
    }

    private Element getUserRowByEmail(String email) {
        String loc = USER_ROW_BY_EMAIL.replace(email);
        return getElement(false, loc);
    }

    public String[] getUserNameByEmail(String email) {
        PSLogger.info("Get user name by email " + email);
        Element row = getUserRowByEmail(email);
        row.setSimpleLocator();
        if (!row.exists()) {
            PSLogger.warn("Can't find user by email " + email + " on page");
            return null;
        }
        String txt = row.getChildByXpath(USER_NAME_COLUMN).getChildByXpath(USER_NAME_COLUMN_DIV).getText();
        PSLogger.debug("user full name: '" + txt + "'");
        return new String[]{txt.split(",")[1], txt.split(",")[0]};
    }

    public UserProfilePage goToUserProfilePage(User user) {
        Assert.assertNotNull(user.getFullName(), "User " + user + " has not full name");
        Element link = getElement(false, USER_ELEMENT.replace(user.getFormatFullName()));
        UserMenu menu = new UserMenu(link);
        menu.open();
        menu.getMoreLink().click(true);
        return new UserProfilePage();
    }

    public Integer getCount() {
        Element count = new Element(USERS_COUNT);
        if (!count.exists())
            return null;
        String txt = count.getText();
        PSLogger.debug("Count: " + txt);
        if (!txt.matches("\\s*\\[\\d+\\s+[^\\]]+\\]\\s*")) {
            return null;
        }
        return Integer.parseInt(txt.replaceAll("[^\\d]+", ""));
    }

    public boolean next() {
        return goTo(NEXT_IMG);
    }

    public boolean prev() {
        return goTo(PREV_IMG);
    }

    public boolean first() {
        return goTo(FIRST_IMG);
    }

    public boolean last() {
        return goTo(LAST_IMG);
    }

    public void goTo(int num) {
        Link link = new Link(PAGE.replace(num));
        if (!link.exists()) {
            PSLogger.warn("Can't find page #" + num);
            return;
        }
        PSLogger.debug("Go to page #" + num);
        link.clickAndWaitNextPage();
        getDocument();
    }

    public int getActivePageNumber() {
        Element e = new Element(PAGE_ACTIVE);
        if (!e.exists()) return -1;
        return Integer.parseInt(e.getText());
    }

    private List<Element> getRows() {
        return getElements(false, USER_ROW);
    }

    public List<User> getUsers() {
        List<User> res = new ArrayList<User>();
        for (Element row : getRows()) {
            Element nameCell = row.getChildByXpath(USER_NAME_COLUMN);
            if (!nameCell.isDEPresent()) continue;
            String name = nameCell.getChildByXpath(USER_NAME_COLUMN_DIV).getDEText();
            String email = row.getChildByXpath(USER_EMAIL_COLUMN).getDEText();
            Link more = new Link(nameCell.getChildByXpath(UserMenuLocators.USER_POPUP_MORE_LINK));
            String href = more.getHref();
            User us = new User();
            us.setFormatFullName(name);
            us.setEmail(email);
            us.setId(href.replaceAll(".*" + UserProfilePageLocators.URL.getLocator(), ""));
            if (row.getChildByXpath(USER_NO_ACCESS_IMG).isDEPresent()) {
                PSLogger.debug("Skip user {" + us + "}: has not access");
                continue;
            }
            res.add(us);
        }
        return res;
    }

    private boolean goTo(ILocatorable loc) {
        Img img = new Img(loc);
        if (!img.exists()) {
            PSLogger.debug("Can't find img " + loc);
            return false;
        }
        if (img.getAttribute(IMG_ALT).contains(IMG_ALT_DISABLED.getLocator())) {
            PSLogger.debug("Img " + loc + " is disabled");
            return false;
        }
        img.click(true);
        getDocument();
        return true;
    }

}
