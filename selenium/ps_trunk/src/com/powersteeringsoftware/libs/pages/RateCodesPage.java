package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.RateCodesPageLocators.*;

/**
 * Created by admin on 11.04.2014.
 */
public class RateCodesPage extends AbstractRateCodesPage {
    @Override
    public void open() {
        super.open();
        clickRateCodes(true);
    }

    private class Row extends ARow {
        String code;
        String rate;
        String date;

        public String toString() {
            return code + "," + rate + "," + date;
        }
    }

    public List<ARow> getRows() {
        if (rows != null) return rows;
        rows = new ArrayList<ARow>();
        for (Element e : getElements(false, TABLE_ROW)) {
            Element code = e.getChildByXpath(TABLE_ROW_CODE);
            if (!code.isDEPresent()) continue;
            String code1 = code.getDEText();
            String code2 = code.getChildByXpath("/a").getDEText();
            String rate1 = e.getChildByXpath(TABLE_ROW_AMOUNT).getDEText();
            String rate2 = e.getChildByXpath(TABLE_ROW_AMOUNT_FUTURE).getDEText();
            String date1 = e.getChildByXpath(TABLE_ROW_DATE).getDEText();
            String date2 = e.getChildByXpath(TABLE_ROW_DATE_FUTURE).getDEText();
            Row r = new Row();
            r.code = code1.isEmpty() ? code2 : code1;
            r.rate = rate1.isEmpty() ? rate2 : rate1;
            r.date = date1.isEmpty() ? date2 : date1;
            rows.add(r);
        }
        return rows;
    }

    public CodeDialog addNew() {
        getAddNew().click(false);
        CodeDialog res = new CodeDialog();
        res.waitForVisible();
        return res;
    }

    public class CodeDialog extends ARateDialog {

        private CodeDialog() {
            super();
        }

        public void setCode(String code) {
            PSLogger.info("Set rate code " + code);
            new Input(DIALOG_RATE).type(code);
        }

    }
}
