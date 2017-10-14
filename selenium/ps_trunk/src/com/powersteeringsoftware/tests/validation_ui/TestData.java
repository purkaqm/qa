package com.powersteeringsoftware.tests.validation_ui;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 13:42:15
 */
public class TestData extends PSTestData {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final int ATTEMPTS_NUMBER = 5;

    public String getTooltip(String id) {
        return conf.getElementByTagAndAttribute("tooltip", "id", id).getText();
    }

    public List<String> getMenuItems(String id) {
        List<String> res = new ArrayList<String>();
        for (Config ch : conf.getElementByTagAndAttribute("menu", "id", id).getChilds()) {
            res.add(ch.getText());
        }
        return res;
    }

    public boolean hasMenuItemSubmenu(String menuId, int index) {
        List<Config> confs = conf.getElementByTagAndAttribute("menu", "id", menuId).getChilds();
        if (confs.size() > index) {
            Config ch = confs.get(index);
            if (ch.getAttribute("has-submenu").equals("true")) return true;
        }
        return false;
    }

    public String getMenuItemSubmenu(String menuId) {
        for (Config ch : conf.getElementByTagAndAttribute("menu", "id", menuId).getChilds()) {
            if (ch.getAttribute("has-submenu").equals("true")) return ch.getText();
        }
        return null;
    }

    public String getDialogTitle(String id) {
        return conf.getElementByTagAndAttribute("dialog", "id", id).getText("title");
    }

    public String getDialogBody(String id) {
        return conf.getElementByTagAndAttribute("dialog", "id", id).getText("body");
    }

    public List<String> getTextAreaData() {
        List<String> res = new ArrayList<String>();
        for (Config c : conf.getChByName("textarea").getChilds()) {
            if (!isDataForThisBrowser(c)) continue;
            String txt = c.getNotTrimText();
            res.add(txt);
        }
        return res;
    }

    /**
     * skipping some data for quickening tests
     *
     * @param c Config
     * @return
     */
    private boolean isDataForThisBrowser(Config c) {
        String excluded = c.getAttribute("not");
        if (excluded == null) return true;
        for (String br : excluded.split(",")) {
            if (SeleniumDriverFactory.getDriver().getType().getName().equalsIgnoreCase(br)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getTextboxData() {
        List<String> res = new ArrayList<String>();
        for (Config c : conf.getChByName("textbox").getChilds()) {
            if (!isDataForThisBrowser(c)) continue;
            res.add(c.getNotTrimText());
        }
        res.add(String.valueOf(Integer.MAX_VALUE / 2));
        res.add(String.valueOf(Long.MAX_VALUE));
        res.add(String.valueOf(Long.MIN_VALUE));
        return res;
    }

    public List<String> getComboBoxData(int id) {
        List<String> res = new ArrayList<String>();
        for (Config c : getComboBox(String.valueOf(id)).getChsByName("text")) {
            PowerSteeringVersions ver = PowerSteeringVersions.valueOf(c);
            if (ver == null || TestSession.getAppVersion().verGreaterOrEqual(ver)) {
                res.add(c.getText());
            }
        }
        return res;
    }

    public String getComboBoxDefaultValue(int id) {
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_1)) {
            // this checking is not supported on 8.2
            return null;
        }
        Config c = getComboBox(String.valueOf(id)).getChByName("default");
        if (c == null) return "";
        PowerSteeringVersions ver = PowerSteeringVersions.valueOf(c);
        if (ver == null || TestSession.getAppVersion().verGreaterOrEqual(ver)) {
            return c.getText();
        }
        return "";
    }

    private Config getComboBox(String id) {
        return conf.getElementByTagAndAttribute("combobox", "id", id);
    }

    public String getCheckboxValue(boolean onOff) {
        return conf.getChByName("checkbox").getText(String.valueOf(onOff));
    }

    public String getRadioButtonValue(String id, boolean onOff) {
        return conf.getElementByTagAndAttribute("radiobutton", "id", id).getText(String.valueOf(onOff));
    }

    public String getMessage(String type) {
        return conf.getChByName("messages").getText(type);
    }

    public String getLightboxDialogImage(String id) {
        Config c = conf.getElementByTagAndAttribute("lighbox-dialog", "id", id).getChByName("image");
        if (c == null) return "";
        return c.getNotTrimText();
    }

    public String getLightboxDialogTitle(String id) {
        Config c = conf.getElementByTagAndAttribute("lighbox-dialog", "id", id).getChByName("title");
        if (c == null) return "";
        return c.getNotTrimText();
    }

    public String getLightboxDialogDescription(String id) {
        Config c = conf.getElementByTagAndAttribute("lighbox-dialog", "id", id).getChByName("description");
        if (c == null) return "";
        return c.getNotTrimText();
    }

}
