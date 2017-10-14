package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.ComboBox;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.FlatTagChooser;
import com.powersteeringsoftware.libs.elements.RadioButton;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.RateTableViewPageLocators.*;

/**
 * Created by admin on 07.04.14.
 */
public class RateTableViewPage extends AbstractRateCodesPage {
    @Override
    public void open() {
        // todo
    }

    private class Row extends ARow {
        private String role;
        private String activity;
        private String date;
        private String amount;
        private boolean isDef;

        public String toString() {
            return role + "," + activity + "," + date + "," + amount + "," + isDef;
        }
    }

    public List<ARow> getRows() {
        if (rows != null) return rows;
        rows = new ArrayList<ARow>();
        for (Element e : getElements(false, TABLE_ROW)) {
            Element r = e.getChildByXpath(TABLE_ROW_ROLE);
            if (!r.isDEPresent()) continue;
            Row row = new Row();
            row.role = r.getDEText();
            row.activity = e.getChildByXpath(TABLE_ROW_ACTIVITY).getDEText();
            row.date = e.getChildByXpath(TABLE_ROW_DATE).getDEText();
            row.amount = e.getChildByXpath(TABLE_ROW_AMOUNT).getDEText();
            row.isDef = e.getChildByXpath(TABLE_ROW_DEF).isDEPresent();
            rows.add(row);
        }
        return rows;
    }


    public RateDialog addNew() {
        getAddNew().click(false);
        RateDialog res = new RateDialog();
        res.waitForVisible();
        return res;
    }

    public class RateDialog extends ARateDialog {

        private RateDialog() {
            super();
        }

        public void setRole(String r) {
            PSLogger.info("Set role " + r);
            new ComboBox(DIALOG_ROLE).select(r);
        }

        public void setActivity(String a) {
            PSLogger.info("Set activity " + a);
            new FlatTagChooser(DIALOG_ACTIVITY).setLabel(a);
        }

        public void setCode(String code) {
            PSLogger.info("Set rate code " + code);
            new RadioButton(DIALOG_CHOOSE_CODE).select();
            new ComboBox(DIALOG_CODE).select(code);
        }

        public void setCodeDetails(Double amount, String currency) {
            new RadioButton(DIALOG_CUSTOME_CODE).select();
            super.setCodeDetails(amount, currency);
        }

    }


}
