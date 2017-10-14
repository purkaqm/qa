package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import org.dom4j.Document;

import static com.powersteeringsoftware.libs.enums.elements_locators.NumberSpinnerLocator.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.06.2010
 * Time: 18:33:02
 */
public class NumberSpinner extends Element {
    private Input input;
    private Element up;
    private Element down;

    public NumberSpinner(ILocatorable locator) {
        super(locator);
    }

    public NumberSpinner(String locator) {
        super(locator);
    }

    public NumberSpinner(Element e) {
        super(e);
    }

    public NumberSpinner(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public Input getInput() {
        if (input == null) {
            input = new Input(getChildByXpath(INPUT));
            input.setRegExp(INPUT_REGEXP);
            input.setMax(INPUT_MAX);
            input.setMin(INPUT_MIN);
            input.setId(input.getAttribute("id"));
            input.setParentDiv(this);
        }
        return input;
    }

    public void increase() {
        if (up == null) {
            up = getChildByXpath(ARROW_UP);
            if (!CoreProperties.getBrowser().isWebDriver())
                up.setSimpleLocator();
        }
        if (!CoreProperties.getBrowser().isWebDriver()) up.mouseDownAndUp();
        else up.click(false);
    }

    public void decrease() {
        if (down == null) {
            down = getChildByXpath(ARROW_DOWN);
            if (!CoreProperties.getBrowser().isWebDriver())
                down.setSimpleLocator();
        }
        if (!CoreProperties.getBrowser().isWebDriver()) down.mouseDownAndUp();
        else down.click(false);
    }
}
