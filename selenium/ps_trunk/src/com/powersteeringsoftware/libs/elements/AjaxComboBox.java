package com.powersteeringsoftware.libs.elements;

import static com.powersteeringsoftware.libs.enums.elements_locators.AjaxComboBoxLocators.ARROW;
import static com.powersteeringsoftware.libs.enums.elements_locators.AjaxComboBoxLocators.LOADING;

/**
 * Created by admin on 24.10.2014.
 */
public class AjaxComboBox extends ComboBox {
    public AjaxComboBox(Element e) {
        super(e);
    }

    protected void waitForPopupLoading() {
        getPopup().getChildByXpath(LOADING).waitForDisapeared();
    }

    protected String getArrowLocator() {
        return arrow == null ? arrow = ARROW.getLocator() : arrow;
    }
}
