package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;

import static com.powersteeringsoftware.libs.enums.page_locators.MeasureInstancePageLocators.TABLE;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 30.08.12
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class MeasureInstancePage extends AbstractWorkPage {
    public boolean hasTable() {
        return new Element(TABLE).exists();
    }
}
