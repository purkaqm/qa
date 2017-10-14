package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.MetricsPageLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.PSTablePageLocators.*;

/**
 * Class for metric instances
 * example of usage:
 * <p>
 * SummaryWorkPage sum = SummaryWorkPage.openWork(work);
 * MetricsPage metrics = sum.openManageMetrics();
 * MetricsPage.AttachDialog attach = metrics.attach();
 * attach.select();
 * metrics = attach.submit();
 * List<String> res = metrics.getMetrics();
 * PSLogger.info(res);
 * MetricsPage.DeleteDialog delete = metrics.delete(res.get(0));
 * metrics = delete.ok();
 * List<String> res2 = metrics.getMetrics();
 * PSLogger.info(res2);
 * Assert.assertEquals(res2.size(), res.size() - 1, "The same list after deleting");
 * </p>
 * User: zss
 * Date: 03.10.11
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class MetricsPage extends AbstractWorkPage {

    @Override
    public MetricInstancePage openMetricInstancePage(String name) {
        Link link = new Link(INSTANCE_LINK.replace(name));
        Assert.assertTrue(link.exists(), "Can't find link " + name);
        link.clickAndWaitNextPage();
        MetricInstancePage res = new MetricInstancePage();
        res.setId();
        return res;
    }


    public AttachDialog attach() {
        Element link = new Element(ATTACH); // link or button.
        AttachDialog res = null;
        if (link.exists()) {
            link.click(false);
            res = new AttachDialog();
            res.getPopup().waitForVisible();
        }
        return res;
    }


    public class AttachDialog extends Dialog {
        public AttachDialog() {
            super("");
            setPopup(ATTACH_DIALOG);
        }

        public MetricsPage submit() {
            PSLogger.save("Before metric attach");
            Button bt = new Button(ATTACH_DIALOG_SUBMIT);
            bt.waitForVisible();
            bt.click(false);
            MetricsPage res = new MetricsPage();
            res.waitForPageToLoad();
            Assert.assertTrue(res.checkUrl(), "incorrect url after submitting");
            return res;
        }

        public void select(String label) {
            PSLogger.debug("Select metric to attach " + label);
            SelectInput s = findSelect();
            s.selectLabel(label);
        }

        public void select() {
            List<String> res = findSelect().getSelectOptions();
            Assert.assertFalse(res.size() == 0, "Can't find any items to select");
            select(res.get(0));
        }

        public SelectInput findSelect() {
            SelectInput si = new SelectInput(ATTACH_DIALOG_SELECT);
            si.waitForVisible();
            return si;
        }
    }

    public Element getRow(String name) {
        for (Element row : getRows()) {
            Element cell = row.getChildByXpath(TABLE_ROW_NAME);
            if (cell.getDEText().trim().equals(name)) {
                return row;
            }
        }
        return null;
    }

    public List<Element> getRows() {
        Element table = Element.searchElementByXpath(getDocument(), TABLE);
        List<Element> res = new ArrayList<Element>();
        for (Element row : Element.searchElementsByXpath(table, TABLE_ROW)) {
            Element cell = row.getChildByXpath(TABLE_ROW_NAME);
            if (cell.isDEPresent() && !cell.getDEText().trim().isEmpty()) {
                res.add(row);
            }
        }
        return res;
    }

    public List<String> getMetrics() {
        List<String> res = new ArrayList<String>();
        for (Element row : getRows()) {
            res.add(row.getChildByXpath(TABLE_ROW_NAME).getDEText());
        }
        return res;
    }

    public DeleteDialog delete(String name) {
        Element row = getRow(name);
        Assert.assertNotNull(row, "Can't find row for " + name + " metric");
        CheckBox ch = new CheckBox(row.getChildByXpath(TABLE_ROW_DELETE));
        ch.click();
        Button del = new Button(document, DELETE);
        String id = del.getDEAttribute("id");
        PSLogger.debug("Id is " + id);
        DeleteDialog dialog = new DeleteDialog(id.replace(DELETE_ID_SUFFIX.getLocator(), ""));
        del.click(false);
        dialog.getPopup().waitForVisible();
        return dialog;
    }

    public class DeleteDialog extends Dialog {

        private DeleteDialog(String id) {
            super("");
            setPopup(DELETE_DIALOG.replace(id));
        }

        public MetricsPage ok() {
            Button bt = new Button(getPopup().getChildByXpath(DELETE_OK));
            bt.click(false);
            MetricsPage res = new MetricsPage();
            res.waitForPageToLoad();
            Assert.assertTrue(res.checkUrl(), "incorrect url after deleting");
            return res;
        }
    }
}
