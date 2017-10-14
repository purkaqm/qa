package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 04.06.2010
 * Time: 20:50:57
 */
public class RadioButton extends CheckBox {
    public RadioButton(ILocatorable locator) {
        super(locator);
    }

    public RadioButton(String locator) {
        super(locator);
    }

    public RadioButton(Element e) {
        super(e);
    }

    public RadioButton(Document document, ILocatorable locator) {
        super(document, locator);
    }
}
