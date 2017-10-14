package com.powersteeringsoftware.libs.elements;

import static com.powersteeringsoftware.libs.enums.elements_locators.CheckBoxLocators.PARENT_DIV_CHECKED;
import static com.powersteeringsoftware.libs.enums.elements_locators.CheckBoxLocators.PARENT_DIV_CLASS;

/**
 * This class is workaround.
 * For ie6 and PS9.0 i see that getChecked() returns incorrect value for dijit chekbox
 * User: szuev
 * Date: 27.01.11
 * Time: 17:33
 */
public class DijitCheckBox extends CheckBox {
    private Boolean checked;

    public DijitCheckBox(Element e) {
        super(e);
    }

    public boolean getChecked() {
        if (checked != null) return checked;
        return checked = getDefaultElement().
                getParent().attributeValue(PARENT_DIV_CLASS.getLocator()).contains(PARENT_DIV_CHECKED.getLocator());
        //return checked = getDefaultElement().attributeValue("value").equalsIgnoreCase("on");
    }

    public void click() {
        super.click();
        checked = !getChecked();
    }

    public void check() {
        super.check();
        checked = true;
    }

    public void uncheck() {
        super.uncheck();
        checked = false;
    }

    @Override
    public String toString() {
        return getName();
    }

}
