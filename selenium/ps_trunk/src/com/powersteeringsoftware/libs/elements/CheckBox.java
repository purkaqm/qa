package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import org.dom4j.Document;

import static com.powersteeringsoftware.libs.enums.elements_locators.CheckBoxLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 26.05.2010
 * Time: 17:17:02
 */
public class CheckBox extends Element {

    private Button middleStateButton;
    private String label;

    public CheckBox(ILocatorable locator) {
        super(locator);
    }

    public CheckBox(String locator) {
        super(locator);
    }

    public CheckBox(Element e) {
        super(e);
    }

    public CheckBox(Document document, ILocatorable locator) {
        super(document, locator);
    }


    public String getName() {
        return label;
    }

    public void setName(String name) {
        label = name;
    }

    public void setName(Element element) {
        setName(element.getText());
    }

    public void setName(ILocatorable loc) {
        Element label;
        if (defaultElement != null) {
            label = Element.searchElementByXpath(defaultElement.getDocument(), loc);
        } else {
            label = Element.searchElementByXpath(loc.getLocator());
        }
        setName(label);
    }


    public String toString() {
        return getName() + "[" + getLocator() + "]";
    }

    public boolean getChecked() {
        //PSLogger.debug("ischecked (" + this + ")");
        return getDriver().isChecked(locator);
    }


    public boolean isEditable() {
        return getDriver().isEditable(locator);
    }

    public void check() {
        PSLogger.debug("check (" + this + ")");
        getDriver().check(locator);
    }

    public void uncheck() {
        PSLogger.debug("uncheck (" + this + ")");
        getDriver().uncheck(locator);
    }

    public void select(boolean flag) {
        doSelect(flag);
    }

    public void select() {
        doSelect(true);
    }

    public boolean doSelect(boolean flag) {
        if (getChecked() ^ flag) {
            PSLogger.debug("Check " + (flag ? "on " : "off ") + "'" + this + "'");
            click();
            return true;
        } else {
            PSLogger.debug("Checkbox '" + this + "' is already checked " + (flag ? "on" : "off"));
            return false;
        }
    }


    public void click() {
        try {
            super.click(false);
        } catch (SeleniumException e) {
            if (CoreProperties.getBrowser().isWebDriverIE()) {
                PSLogger.debug("Have an exception : " + e.getMessage());
                getParent().click(false);
            } else {
                throw e;
            }
        }
    }

    private void setMiddleStateButton(Button bt) {
        middleStateButton = bt;
    }

    public void setMiddleStateButton() {
        Button bt = new Button(getParent("div").getParent().getChildByXpath("//button"));
        setMiddleStateButton(bt);
    }

    public void setMiddleStateButton(ILocatorable loc) {
        Element button;
        if (isDEPresent()) {
            button = Element.searchElementByXpath(getParentDoc(), loc);
        } else {
            button = Element.searchElementByXpath(loc.getLocator());
        }
        setMiddleStateButton(new Button(button));
    }


    public void setMiddleState() {
        if (middleStateButton == null) return;
        PSLogger.debug("Set ch " + this + " to middle state");
        middleStateButton.click(false);
    }

    public boolean isMiddleStateSetted() {
        Element parent = getChildByXpath(PARENT_DIV);
        return middleStateButton != null && parent.
                getAttribute(PARENT_DIV_CLASS).contains(PARENT_DIV_MIDDLE_ATTR.getLocator());
    }

}
