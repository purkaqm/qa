package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.util.StrUtil;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.AgendaPageLocators.*;

/**
 * Created by admin on 06.05.2014.
 */
public abstract class AgendaPage extends PSTablePage {
    private String _url;

    public void open() {
        clickBrowseMyProfileAgenda();
        if (!getName().equals(getCurrentTabTxt())) {
            Link tab = getTab();
            Assert.assertTrue(tab.exists(), "Tab " + getName() + " is not enabled");
            tab.clickAndWaitNextPage();
            setUrl();
        }
        _url = getUrl();
    }

    public boolean checkUrl() {
        return getCurrentUrl().equals(_url);
    }

    public Link getTab() {
        return new Link(TAB_LINK.replace(getName()));
    }

    public static class Tasks extends AgendaPage {
        public String getName() {
            return TAB_TASKS.getLocator();
        }
    }

    public static class Issues extends AgendaPage {
    }

    public static class Risks extends AgendaPage {
    }

    public static class Ideas extends AgendaPage {
    }

    public static class Projects extends AgendaPage {
    }

    public String getCurrentTabTxt() {
        Element current = new Element(CURRENT);
        return StrUtil.trim(current.getText());
    }


}
