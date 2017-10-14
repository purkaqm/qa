package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.LoadAlternativeCalendarsPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 13.12.12
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class LoadAlternativeCalendarsPage extends Page {

    public void open() {
        super.open(URL.getLocator());
    }

    public void attach(String file) {
        setFile(file);
        submit();
        PSLogger.save("After attach");
    }

    public void setFile(String file) {
        new Input(INPUT).attachFile(file);
    }

    public void submit() {
        new Button(SUBMIT).submit();
    }
}
