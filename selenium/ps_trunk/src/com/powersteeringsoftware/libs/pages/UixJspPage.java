package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Img;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.UixFeature;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.UixEditJspPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.05.2010
 * Time: 19:09:13
 * To change this template use File | Settings | File Templates.
 */
public class UixJspPage extends Page {

    public void open() {
        super.open(URL.getLocator());
        load();
    }

    private void load() {
        for (Element e : getElements(ROW)) {
            Img img = new Img(e.getChildByXpath(ITEM_IMG));
            Element span1 = e.getChildByXpath(ITEM_DESC);
            Element span2 = e.getChildByXpath(ITEM_CODE);
            String label = img.getDEAttribute(ITEM_IMG_TITLE);
            String code = span2.getDEText();
            String name = span1.getDEText();
            if (code.isEmpty()) {
                continue;
            }
            UixFeature uix = new UixFeature(code);
            uix.setDescription(name);
            for (Label l : Label.values()) {
                if (l.getLocator().equals(label)) {
                    uix.setValue(l.ordinal());
                    break;
                }
            }
            uix.setCreated();
        }
    }

    public Edit edit() {
        Link edit = new Link(EDIT_LINK);
        if (edit.exists()) {
            edit.clickAndWaitNextPage();
        }
        return new Edit();
    }


    public boolean isOpened() {
        return url != null && url.endsWith(URL.getLocator());
    }

    public class Edit extends Page {
        public void set(UixFeature feature) {
            PSLogger.info("Set feature " + feature);
            SelectInput se = new SelectInput(Feature.getLocator(feature.getName()));
            Assert.assertTrue(se.exists(), "Can't find select for " + feature);
            Integer i = feature.getValue();
            if (i == null) throw new IllegalArgumentException("Value should be specified for feature " + feature.getName());
            se.selectLabel(Label.values()[i].getLocator());
        }

        public UixJspPage submit() {
            PSLogger.info("Save uix-features");
            new Link(SUBMIT_LINK).clickAndWaitNextPage();
            return UixJspPage.this;
        }


        public HomePage submitAndReturnToHome() {
            submit();
            HomePage page = new HomePage();
            if (!page.checkUrl())
                page.open();
            return page;
        }
    }
}
