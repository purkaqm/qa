package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.enums.page_locators.DeletedJspPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DeletedJspPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 05.06.13
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
public class DeletedJspPage extends Page {

    public static final TimerWaiter TIMEOUT = new TimerWaiter(5 * 60 * 1000);

    public enum URL {
        DOCUMENTS(URL_DOCUMENTS),
        WORKS(URL_WORKS),;
        private String url;

        URL(DeletedJspPageLocators url) {
            this.url = url.getLocator();
        }
    }

    public void open() {
        open(URL.DOCUMENTS);
    }

    public void open(URL u) {
        super.open(u.url);
        getDocument();
    }

    public Document getDocument(boolean b) {
        if (b) rows = null;
        return super.getDocument(b);
    }

    private List<Row> rows;

    public List<Row> getRows() {
        if (rows != null) return rows;
        List<Row> rows = new ArrayList<Row>();
        for (Element e : getElements(false, CHECKBOX)) {
            Row r = new Row();
            r.ch = new CheckBox(e);
            Element parent = e.getParent(CHECKBOX_PARENT);
            Element label = parent.getChildByXpath(CHECKBOX_LABEL);
            if (!label.isDEPresent()) continue;
            r.label = label.getDEText();
            r.isDisabled = parent.getDEStyle().contains(CHECKBOX_PARENT_DELETED_SRYLE.getLocator());
            rows.add(r);
        }
        return this.rows = rows;
    }

    public List<String> getItems() {
        List<String> res = new ArrayList<String>();
        for (Row r : getRows()) {
            res.add(r.label);
        }
        return res;
    }

    public List<String> getDeletedItems() {
        List<String> res = new ArrayList<String>();
        for (Row r : getRows()) {
            if (r.isDisabled)
                res.add(r.label);
        }
        return res;
    }

    public void deleteAll() {
        delete(true);
    }

    public void deleteSelected() {
        delete(false);
    }

    private void delete(boolean all) {
        PSLogger.info("Delete " + (all ? "all" : "selected") + " items");
        Button b = new Button(all ? DELETE_ALL : DELETE_SELECTED);
        b.click(false);
        if (getDriver().isAlertPresent()) {
            PSLogger.info(getDriver().getAlert());
            return;
        }
        Element dialog = new Element(DIALOG);
        dialog.waitForVisible();
        PSLogger.save("deleting");
        PSLogger.info("Wait while deleting complete");
        Button ok = new Button(DIALOG_OK);
        ok.waitForVisible(TIMEOUT);
        ok.click(false);
        dialog.waitForUnvisible();
        getDocument();
        PSLogger.save("after");
    }

    public void select(String item) {
        PSLogger.info("Select item '" + item + "'");
        for (Row r : getRows()) {
            if (item.equals(r.label)) {
                r.ch.select();
                return;
            }
        }
        Assert.fail("Can't find item " + item);
    }

    private class Row {
        private CheckBox ch;
        private boolean isDisabled;
        private String label;
    }

}
