package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;

import static com.powersteeringsoftware.libs.enums.page_locators.RateTableViewPageLocators.*;

/**
 * Created by admin on 11.04.2014.
 */
public abstract class AbstractRateCodesPage extends ResourceRatesPage {

    protected abstract class ARateDialog extends Dialog {
        protected ARateDialog() {
            super(DIALOG);
        }

        public void setDate(String t) {
            PSLogger.info("Set date " + t);
            DatePicker dp = new DatePicker(DIALOG_DATE);
            dp.type(t);
            dp.mouseDownAndUp();
        }

        public void setCodeDetails(Double amount, String currency) {
            PSLogger.info("Set value=" + amount + ", currency=" + currency);
            Input in = new DisplayTextBox(DIALOG_AMOUNT);
            in.type(amount.toString());
            in.mouseDownAndUp(); // for ie
            ComboBox cb = new ComboBox(DIALOG_CURRENCY);
            cb.select(currency);
            cb.mouseDownAndUp();
        }

        public void submit() {
            TimerWaiter.waitTime(5000);
            PSLogger.save("Before dialog submit");
            new Button(DIALOG_OK).click(false);
            waitForPageToLoad();
        }

        public abstract void setCode(String code);
    }

    protected abstract ARateDialog addNew();

    public Button getAddNew() {
        return new Button(NEW);
    }

}
