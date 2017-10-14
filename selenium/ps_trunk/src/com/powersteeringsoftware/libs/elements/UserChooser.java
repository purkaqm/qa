package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;

import static com.powersteeringsoftware.libs.enums.elements_locators.UserChooserLocators.GRID_TABLE_OWNER_INPUT;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 19.09.11
 * Time: 15:25
 */
public class UserChooser extends Element {
    public UserChooser(Element e) {
        super(e);
    }

    public void set(String user) {
        click(false);
        // not a child:
        Input in = new Input(GRID_TABLE_OWNER_INPUT);
        in.waitForPresent();
        in.type(user.toLowerCase().substring(0, 2));
        in.keyPress(' ');
        ComboBox cb = new ComboBox(in);
        cb.setOldWay(true);
        cb.waitForVisible();
        PSLogger.info("UserChooser options: " + cb.getOptions());
        cb.set(user);
        PSPage.getEmptyInstance().getTopElement().mouseDownAndUp();
    }
}
