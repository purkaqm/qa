package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Class for waiting for attribute or inner text
 * User: szuev
 * Date: 19.05.2010
 * Time: 19:57:21
 * To change this template use File | Settings | File Templates.
 */
public class ElementBecomeChangingWaiter extends AbstractElementWaiter {
    public static final String ERROR_MESSAGE_CHANGING_1 =
            "Element doesn't change attribute. Element locator = ";
    public static final String ERROR_MESSAGE_CHANGING_2 =
            "Element doesn't change inner text. Element locator = ";
    private String attributeName;
    private String pendingAttributeValue;
    private String elementTextWas;
    private boolean isCorrectLogic = true;

    public ElementBecomeChangingWaiter(String locator, String attributeName, String newAttributeValue) {
        super(locator, ERROR_MESSAGE_CHANGING_1 + locator);
        this.attributeName = attributeName;
        pendingAttributeValue = newAttributeValue;
    }

    public ElementBecomeChangingWaiter(String locator, String txt) {
        super(locator, ERROR_MESSAGE_CHANGING_2 + locator);
        elementTextWas = txt;
    }

    public ElementBecomeChangingWaiter(String locator) {
        super(locator, ERROR_MESSAGE_CHANGING_2 + locator);
        try {
            elementTextWas = SeleniumDriverFactory.getDriver().getText(locator);
        } catch (SeleniumException e) {
            elementTextWas = "";
        }
    }

    public void setReverseLogic() {
        isCorrectLogic = false;
    }

    public boolean until() {
        if (isCorrectLogic) return _until();
        return !_until();
    }


    private boolean _until() {
        if (elementTextWas != null) {
            String txt;
            try {
                txt = SeleniumDriverFactory.getDriver().superGetText(locator);
                if (txt == null) return false;
            } catch (SeleniumException e) {
                return false;
            }
            return !elementTextWas.equals(txt);
        }
        String value = SeleniumDriverFactory.getDriver().getAttribute(locator, attributeName);
        if (value == null || value.isEmpty()) return false;
        return value.contains(pendingAttributeValue);
    }
}
