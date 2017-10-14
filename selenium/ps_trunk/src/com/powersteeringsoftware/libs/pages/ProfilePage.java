package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.StrUtil;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.ProfilePageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.05.2010
 * Time: 13:29:25
 * To change this template use File | Settings | File Templates.
 */
public class ProfilePage extends PSPage {

    public void open() {
        PSLogger.info("Open ProfilePage for " + BasicCommons.getCurrentUser());
        super.open(URL.getLocator()); // open for current user
        /*
        new Link(USER_SETTINGS_LINK).click(false);
        new Link(EDIT_PROFILE_LINK).click(true);
        */
    }

    public void open(String id) {
        PSLogger.info("Open ProfilePage for " + id);
        String url = makeUrl(URL, id);
        super.open(url);
    }


    public String getLogin() {
        return new Input(LOGIN_INPUT).getValue();
    }

    public void setLogin(String s) {
        new Input(LOGIN_INPUT).type(s);
    }

    public void setPassword(String pass) {
        PSLogger.debug("Set password to " + pass);
        new Input(CONFIRM_PASSWORD_INPUT).type(pass);
        new Input(PASSWORD_INPUT).type(pass);
    }

    private void changeUserName(String firstName, String lastName) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        PSLogger.info("Set '" + u.getFormatFullName() + "' as usernmae for " + BasicCommons.getCurrentUser());
        getFirstNameInput().type(u.getFirstName());
        getSecondNameInput().type(u.getLastName());
        submit();
        String name = getFullUserName();
        Assert.assertEquals(name, u.getFullName(), "Incorrect user name after changing");
    }

    public Input getFirstNameInput() {
        return new Input(FIRST_NAME_INPUT);
    }

    public Input getSecondNameInput() {
        return new Input(LAST_NAME_INPUT);
    }

    public String getFirstName() {
        return StrUtil.trim(getFirstNameInput().getValue());
    }

    public String getSecondName() {
        return StrUtil.trim(getSecondNameInput().getValue());
    }

    public DatePicker getExpirationDatePicker() {
        return new DatePicker(EXPIRATION_DATE_PICKER);
    }

    public CheckBox getNoAccessCheckbox() {
        return new CheckBox(ACCESS_CHECKBOX);
    }

    public Boolean hasAccess() {
        CheckBox ch = getNoAccessCheckbox();
        if (!ch.exists()) return null;
        return !ch.getChecked();
    }

    public PSCalendar getExpirationDate() {
        String str = getExpirationDatePicker().get();
        if (str == null || str.isEmpty()) return null;
        return PSCalendar.getEmptyCalendar().set(str);
    }

    public void submit() {
        String error = doSubmit();
        Assert.assertNull(error, "Have an error on page " + error);
    }

    public String doSubmit() {
        PSLogger.info(getClass().getSimpleName() + ":submit");
        new Button(SUBMIT).submit();
        return getErrorBoxMessage();
    }

    public UserProfilePage cancel() {
        PSLogger.info(getClass().getSimpleName() + ":cancel");
        new Button(CANCEL).submit();
        UserProfilePage res = new UserProfilePage();
        return res;
    }

    public void setNewUserName(String firstName, String lastName) {
        changeUserName(firstName, lastName);
    }

    public void setNewUserName(User user) {
        setNewUserName(user.getFirstName(), user.getLastName());
    }

    public void selectCalendar(PSCalendar calendar) {
        selectCalendar(calendar.getName());
    }

    public void setDaysOff(String name, PSCalendar start, PSCalendar end) {
        NonWorkDaysSelector.RowItem item = new NonWorkDaysSelector.RowItem(name, start.toString(), end.toString());
        setDaysOff(item);
    }

    public void setDaysOff(NonWorkDaysSelector.RowItem... ranges) {
        List<ASelector.ARowItem> listToSet = new ArrayList<ASelector.ARowItem>(Arrays.asList(ranges));
        PSLogger.info("Set days off : " + listToSet);
        getNonWorkDaysSelector().setList(listToSet);
    }

    public void removeAllDaysOff() {
        NonWorkDaysSelector selector = getNonWorkDaysSelector(getDocument());
        List<ASelector.ARowItem> list;
        while ((list = selector.getList()).size() != 0) {
            PSLogger.debug("From page : " + list);
            PSLogger.info("Remove days off : " + list.get(0));
            selector.remove(0);
            selector = getNonWorkDaysSelector(getDocument());
        }
    }

    public NonWorkDaysSelector getNonWorkDaysSelector() {
        return getNonWorkDaysSelector(document);
    }

    private NonWorkDaysSelector getNonWorkDaysSelector(Document document) {
        return new NonWorkDaysSelector(document, DAYS_OFF_SELECTOR);
    }

    /**
     * resource planning should be on
     *
     * @param text calendar name
     */
    private void selectCalendar(String text) {
        PSLogger.info("Select calendar " + text);
        String label = CALENDAR_CHOOSER_LABEL.replace(text);
        SelectInput select = new SelectInput(CALENDAR_CHOOSER);
        Assert.assertTrue(select.exists(), "Can't find calendar selector");
        Assert.assertTrue(select.getSelectOptions().contains(text), "Can't find label " + label);
        select.select(label);
    }

    private TagsComponent comp;

    public TagsComponent getTagsComponent() {
        return comp != null ? comp : (comp = new TagsComponent(this));
    }

}
