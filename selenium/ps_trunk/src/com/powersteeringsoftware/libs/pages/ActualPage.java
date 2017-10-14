package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;

import static com.powersteeringsoftware.libs.enums.page_locators.CostBasePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 20.11.13
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class ActualPage extends CostBasePage {
    public ActualPage() {
    }

    public ActualPage(String id) {
        workId = id;
    }

    protected CostRow toRow(Element tr) {
        CostRow res = super.toRow(tr);
        res.setEstimated(false);
        String desc = tr.getChildByXpath(TABLE_DESC).getDEText();
        res.setDescription(desc == null && desc.isEmpty() ? null : desc);
        String t = tr.getChildByXpath(TABLE_ADD_TYPE).getDEText();
        res.setAddType(t);
        String h = tr.getChildByXpath(TABLE_HOURS).getDEText();
        res.setHours(h);
        return res;
    }
}

