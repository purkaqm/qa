package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.enums.page_locators.MoveWorkPageLocators;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 17.04.13
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class CopyMoveWorkPage extends MoveWorkPage {

    public CreateWorkPage copy() {
        new Button(MoveWorkPageLocators.COPY).submit();
        CreateWorkPage res = new CreateWorkPage();
        res.refreshBlankPage();
        return res;
    }
}
