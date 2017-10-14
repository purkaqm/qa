package com.powersteeringsoftware.libs.elements;

import static com.powersteeringsoftware.libs.enums.elements_locators.ContentLocators.ID;
import static com.powersteeringsoftware.libs.enums.elements_locators.ContentLocators.THIS;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.11.2010
 * Time: 18:39:36
 */
public class Content extends Element {
    public Content() {
        super(THIS);
        setId(ID);
    }
}
