package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;

import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.SelectInputLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 08.05.2010
 * Time: 21:40:06
 * To change this template use File | Settings | File Templates.
 */
public class SelectInput extends Element {

    private List<Element> options;

    public SelectInput(ILocatorable locator) {
        super(locator);
    }

    public SelectInput(String locator) {
        super(locator);
    }

    public SelectInput(String locator, Frame fr) {
        super(locator, fr);
    }

    public SelectInput(Element e) {
        super(e);
    }

    public SelectInput(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void select(ILocatorable value) {
        this.select(searchLabelByRegexp(value.getLocator()));
    }

    public void select(String option) {
        try {
            super.select(option);
        } catch (SeleniumException se) { // happens for chrome web-driver in case option has '>'
            PSLogger.warn(initialLocator + ":" + se);
            for (Element op : getOptions()) {
                if (StrUtil.htmlToString(op.getDEText()).trim().equals(option.trim())) {
                    super.select(VALUE.replace(op.getDEAttribute(VALUE_ATTR)));
                    return;
                }
            }
            throw se;
        }
    }

    public void selectLabel(String txt) {
        if (!txt.startsWith(LABEL_PREFIX.getLocator())) txt = LABEL.replace(txt);
        select(txt);
    }

    public void selectLabel(ILocatorable txt) {
        select(txt.getLocator());
    }

    public void selectValue(ILocatorable txt) {
        selectValue(txt.getLocator());
    }

    public void selectValue(String txt) {
        if (!txt.startsWith(VALUE_PREFIX.getLocator())) txt = VALUE.replace(txt);
        super.select(txt);
    }

    public void select(int index) {
        Element elmToChoose = Element.searchElementByXpath(this, OPTION.replace(index));
        if (elmToChoose == null) {
            PSLogger.warn("Can't find option with index '" + index + "'");
            return;
        }
        super.select(elmToChoose.getText());
    }

    public void selectNotTrim(String txt) {
        select(LABEL_SPACE.replace(txt));
    }


    private String searchLabelByRegexp(String label) {
        if (!label.contains(LABEL_PREFIX_REGEXP.getLocator())) return label;
        if (CoreProperties.getBrowser().isWebDriverIE()) return label; // hotfix for ie.
        for (String l : getSelectOptions()) {
            String pattern = label.replace(LABEL_PREFIX_REGEXP.getLocator(), "");
            if (StrUtil.replaceSpaces(l).matches(pattern)) {
                return LABEL.replace(l);
            }
        }
        PSLogger.warn("Can't find label '" + label + "'");
        return null;
    }

    public List<String> getSelectedLabels() {
        return Arrays.asList(getDriver().getSelectedLabels(locator));
    }


    public int getOptionAmount() {
        return getOptions().size();
    }

    public List<Element> getOptions() {
        if (options != null) return options;
        return options = Element.searchElementsByXpath(this, OPTIONS);
    }

    public String getSelectedLabel() {
        String res = getDriver().getSelectedLabel(locator);
        if (res == null && CoreProperties.getBrowser().isGoogleChrome()) {
            if (isDEPresent()) { // its workaround for chrome web-driver.
                for (Element option : getOptions()) {
                    if (option.getAttribute("selected") != null) { // dynamic way
                        return option.getDEText();
                    }
                }
            } else {
                res = getDriver().getSelectedValue(locator); // it is workaround for chrome.
            }
        }
        return res;
    }

    public List<String> getSelectOptions() {
        return Arrays.asList(getDriver().getSelectOptions(locator));
    }

    public boolean hasOption(String label) {
        List<String> labels = getSelectOptions();
        if (labels.contains(label)) return true;
        for (String lab : labels) {
            if (StrUtil.trim(lab).equals(label.trim())) return true;
        }
        PSLogger.debug("Can't find option " + label + ", available options : " + labels);
        return false;
    }

    public String getSelectedValue() {
        return getDriver().getSelectedValue(locator);
    }

    public String getDESelectedLabel() {
        if (!isDEPresent()) return null;
        for (Element e : getOptions()) {
            if (e.hasDEAttribute(SELECTED_ATTR)) {
                return StrUtil.htmlToString(e.getDEText()).trim();
            }
        }
        return "";
    }

}
