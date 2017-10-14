package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.ConfigurationPageLocators.ENABLE;
import static com.powersteeringsoftware.libs.enums.page_locators.ConfigurationPageLocators.SUBMIT;

/**
 * Created by admin on 14.04.2014.
 */
public class ConfigurationPage extends ResourceRatesPage {

    public void open() {
        super.open();
        clickConfig(true);
    }

    public boolean setCosts(boolean v) {
        PSLogger.info((v ? "Enable" : "Disable") + " labor costs");
        return new CheckBox(ENABLE).doSelect(v);
    }

    public void save() {
        PSLogger.save("Save changes");
        new Button(SUBMIT).submit();
    }
}
