package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.StrUtil;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.DeliverablesListingPageLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.PSTablePageLocators.MORE;

public class DeliverablesListingPage extends AbstractWorkPage {

    @Override
    public void open() {
    }

    public class AddDeliverableDialog extends Dialog {

        public AddDeliverableDialog() {
            super(ADD_DELIVERABLE_POPUP_ID);
        }

        public void setWorkType(String workType) {
            new SelectInput(ADD_DELIVERABLE_POPUP_SELECT_WORK_TYPE).select(workType);
        }

        public void setGate(String gate) {
            new SelectInput(ADD_DELIVERABLE_POPUP_SELECT_GATE).select(gate);
        }

        public CreateWorkPage pushContinue() {
            Button bt = new Button(ADD_DELIVERABLE_POPUP_BUTTON_CONTINUE);
            bt.waitForVisible();
            bt.submit();
            return new CreateDeliverablePage();
        }

        public void pushCancel() {
            new Button(ADD_DELIVERABLE_POPUP_BUTTON_CANCEL).click(false);
            waitForUnvisible();
        }
    }

    public AddDeliverableDialog pushAddNew() {
        Button bt = new Button(BUTTON_ADD_NEW);
        bt.clickAndWaitForDialog(ADD_DELIVERABLE_POPUP_ID);
        return new AddDeliverableDialog();
    }

    public void select(String gate, String work) {
        PSLogger.info("Select deliverable " + work + " for gate " + gate);
        getDeleteButton().waitForVisible(); // for chrome12
        Element row = getRow(gate, work);
        Assert.assertNotNull(row, "Can't find row for deliverable '" + work + "' in table");
        new CheckBox(row.getChildByXpath(TABLE_CHECKBOX)).click();
    }

    public void more() {
        Link link = new Link(MORE);
        if (link.exists() && link.isVisible()) {
            PSLogger.info("Click 'More'");
            link.clickAndWaitNextPage();
            getDeleteButton().waitForVisible();
            document = null;
        }
    }

    public SummaryWorkPage openWork(String gate, String work) {
        PSLogger.info("Go to deliverable " + work + " for gate " + gate);
        getDeleteButton().waitForVisible();
        Element row = getRow(gate, work);
        Assert.assertNotNull(row, "Can't find row for deliverable '" + work + "' in table");
        SummaryWorkPage res = SummaryWorkPage.getInstance();
        new Link(row.getChildByXpath(TABLE_LINKS), res).openByHref();
        return res;
    }

    public Element getRow(String gateName, String workName) {
        more();
        Element table = getTable();
        if (table == null || !table.isDEPresent()) {
            PSLogger.debug("No table found");
            return null;
        }
        Element row = null;
        String sGate = null;
        for (Element r : Element.searchElementsByXpath(table, TABLE_ROWS)) {
            Element link = r.getChildByXpath(TABLE_LINKS);
            Element gate = r.getChildByXpath(TABLE_GATE);
            if (!link.isDEPresent() || !gate.isDEPresent()) continue;
            String work = StrUtil.trim(link.getDEText());
            String tmpGate = StrUtil.trim(gate.getDEText());
            if (tmpGate.isEmpty()) {
                tmpGate = sGate;
            } else {
                sGate = tmpGate;
            }
            PSLogger.debug("Found deliverable '" + work + "' for gate '" + tmpGate + "'");
            if (work.equals(workName) && tmpGate.equals(gateName)) {
                row = r;
            }
        }
        if (row == null) {
            PSLogger.debug("Table : " + table.asXML());
        }
        return row;
    }

    public boolean isDeliverablePresent(String gate, String name) {
        return getRow(gate, name) != null;
    }

    public void pushDelete() {
        getDeleteButton().waitForVisible();
        getDeleteButton().submit();
        waitForLoading();
        PSLogger.save("After deleting");
    }

    public Button getDeleteButton() {
        return new Button(BUTTON_DELETE);
    }

    public Element getTable() {
        return getElement(false, TABLE);
    }

    public void waitForLoading() {
        new Button(BUTTON_ADD_NEW).waitForVisible();
        Element table = new Element(TABLE);
        if (table.exists()) {
            table.waitForVisible();
            table.setDefaultElement(getDocument());
        } else {
            getDocument();
        }
    }

    public class CreateDeliverablePage extends CreateWorkPage {
        public AbstractWorkPage finish() {
            getFinishButton().submit();
            DeliverablesListingPage res = new DeliverablesListingPage();
            res.testUrl();
            return res;
        }
    }

}
