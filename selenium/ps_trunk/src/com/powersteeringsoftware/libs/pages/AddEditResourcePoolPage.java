package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.TextArea;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.AddEditResourcePoolPageLocators.*;

/**
 * Created by admin on 29.04.2014.
 */
public class AddEditResourcePoolPage extends PSPage {
    @Override
    public void open() {
        //nothing
    }

    public void setName(String name) {
        new Input(NAME).type(name);
    }

    public void setDescription(String dsc) {
        new TextArea(DESCRIPTION).setText(dsc);
    }

    public ResourcePoolSummaryPage submit() {
        PSLogger.save("Before submit pool");
        new Button(SUBMIT).submit();
        PSLogger.save("After submit pool");
        return new ResourcePoolSummaryPage();
    }
}
