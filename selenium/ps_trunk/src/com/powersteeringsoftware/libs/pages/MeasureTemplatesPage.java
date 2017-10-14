package com.powersteeringsoftware.libs.pages;


import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Link;

import static com.powersteeringsoftware.libs.enums.page_locators.MeasureTemplatesPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 06.08.11
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class MeasureTemplatesPage extends PSPage implements MeasuresPage {

    public boolean isThisPage() {
        return super.checkUrl(URL);
    }

    @Override
    public void open() {
        clickBrowseMeasureLibrary();
        checkUrl();
    }

    public MeasureTemplatesMenu openMenu(String name) {
        MeasureTemplatesMenu res = new MeasureTemplatesMenu(name);
        res.open();
        return res;
    }

    public AddEditMeasureTemplatePage addNew() {
        Button bt = new Button(ADD_NEW_BUTTON);
        bt.waitForVisible(5000);
        bt.submit();
        return new AddEditMeasureTemplatePage();
    }

    public Link getMeasureLink(String name) {
        return AbstractMeasureMenu.getLink(name, this);
    }

    public class MeasureTemplatesMenu extends MeasureInstancesPage.AbstractMeasureMenu {

        private MeasureTemplatesMenu(String name) {
            super(name, MeasureTemplatesPage.this);
        }

        public AddEditMeasureTemplatePage copy() {
            callCopy();
            return new AddEditMeasureTemplatePage();
        }

        public AddEditMeasureTemplatePage edit() {
            callEdit();
            AddEditMeasureTemplatePage res = new AddEditMeasureTemplatePage();
            res.getContent().setEditPage(true);
            return res;
        }

    }

    public AttachPage massAttach() {
        new Link(MASS_ATTACH).clickAndWaitNextPage();
        return new AttachPage();
    }

}
