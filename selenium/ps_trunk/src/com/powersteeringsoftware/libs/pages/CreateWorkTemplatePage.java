package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.CreateWorkTemplatePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 07.10.12
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class CreateWorkTemplatePage extends PSPage {
    private static final TimerWaiter PROCESS_LIST_WAITER = new TimerWaiter(30000);

    @Override
    public void open() {
        WorkTemplates94Page page = new WorkTemplates94Page();
        page.open();
        page.doCreateNew();
    }

    public void setName(String name) {
        new Input(NAME).type(name);
    }

/*    public void setRootWorkName(String name) {
        new Input(ROOT).type(name);
    }*/

    public void setDescription(String dsc) {
        new TextArea(DESCRIPTION).setText(dsc);
    }

    public SelectInput getTypeSelector() {
        return new SelectInput(TYPE);
    }

    private Element getProcessListElement() {
        return new Element(PROCESS_LIST) {
            public void waitForVisible() {
                try {
                    super.waitForVisible(PROCESS_LIST_WAITER);
                } catch (Wait.WaitTimedOutException w) {
                    checkJSError();
                    throw w;
                }
            }
        };
    }

    public void setWork() {
        getTypeSelector().selectLabel(TYPE_WORK);
    }


    public void setFolder() {
        getTypeSelector().selectLabel(TYPE_FOLDER);
    }

    public void setGatedProjectESG() {
        getTypeSelector().selectLabel(TYPE_GATED_PROJECT_ESG);
        getProcessListElement().waitForVisible();
    }

    public void setGatedProjectNESG() {
        getTypeSelector().selectLabel(TYPE_GATED_PROJECT_NOT_ESG);
        getProcessListElement().waitForVisible();
    }


    public void setProcess(String name) {
        Element label = null;
        for (Element l : getElements(PROCESS_LABEL)) {
            if (l.getDEText().equals(name)) {
                label = l;
                break;
            }
        }
        Assert.assertNotNull(label, "Can't find process " + name);
        String id = label.getDEAttribute(PROCESS_LABEL_FOR);
        Assert.assertTrue(id != null && !id.isEmpty(), "Can't find id for label " + label);
        new RadioButton(id).click();
    }

    public WorkTemplates94Page submit() {
        PSLogger.save("Before submit");
        new Button(SUBMIT).submit();
        return new WorkTemplates94Page();
    }


}
