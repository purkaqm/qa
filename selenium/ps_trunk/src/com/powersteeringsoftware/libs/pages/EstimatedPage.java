package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.objects.Cost;

import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.CostBasePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 20.11.13
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class EstimatedPage extends CostBasePage {

    public EstimatedPage() {
    }

    public EstimatedPage(String id) {
        workId = id;
    }

    public EstimatedAddDialog callAddDialog() {
        getAdd().click(false);
        EstimatedAddDialog res = new EstimatedAddDialog();
        res.waitForVisible();
        return res;
    }

    public class EstimatedAddDialog extends AddDialog {
        public DatePicker getEndDateDP() {
            return new DatePicker(DIALOG_END_DATE);
        }

        public void setEndDate(String time, boolean useInput) {
            if (useInput) {
                getEndDateDP().type(time);
            } else {
                getEndDateDP().set(time);
            }
        }

        public void setEndDate(String time) {
            setEndDate(time, false);
        }
    }

    protected CostRow toRow(Element tr) {
        CostRow res = super.toRow(tr);
        res.setEstimated(true);
        Element parent = tr.getParent();
        List<Element> trs = Element.searchElementsByXpath(parent, TR);
        String desc = null;
        for (int i = 0; i < trs.size(); i++) {
            if (trs.get(i).equals(tr)) {
                Element following = null;
                if (i < trs.size() - 1)
                    following = trs.get(i + 1).getChildByXpath(TR_EST_DESC);
                if (following != null && following.isDEPresent()) {
                    Element acronym = following.getChildByXpath(TR_EST_DESC_ACR);
                    if (acronym != null && acronym.isDEPresent())
                        following = acronym;
                    desc = following.getDEText();
                }
                break;
            }
        }
        res.setDescription(desc);
        String role = tr.getChildByXpath(TABLE_RESOURCE_ROLE).getDEText();
        String user = tr.getChildByXpath(TABLE_RESOURCE_ROLE_USER).getDEText();
        if (!role.isEmpty() || !user.isEmpty()) {
            res.setAddType(Cost.AddType.LABOR.getValue());
        }
        if (!role.isEmpty()) {
            res.setResourceRole(role);
        }
        if (!user.isEmpty()) {
            res.setResourceUser(user);
        }
        return res;
    }
}
