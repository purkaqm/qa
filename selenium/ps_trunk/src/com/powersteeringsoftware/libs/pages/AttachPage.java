package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.AttachPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 22.05.13
 * Time: 0:31
 * To change this template use File | Settings | File Templates.
 */
public class AttachPage extends PSPage {
    @Override
    public void open() {
        open(URL);
    }

    public SelectInput getPortfolioSelector() {
        return new SelectInput(PORTFOLIO_SELECTOR);
    }

    public SimpleTagChooser getTemplatesTagChooser() {
        return new SimpleTagChooser(TEMPLATES_TAG_CHOOSER);
    }

    public void selectMyProjects() {
        PSLogger.info("Select My Projects");
        getPortfolioSelector().selectLabel(PORTFOLIO_SELECTOR_MY_PROJECTS);
    }

    public void doAttach() {
        new Button(ATTACH).submit();
    }

    public void doPreview() {
        new Button(PREVIEW).submit();
    }

    private class SimpleTagChooser extends TagChooser {
        public SimpleTagChooser(ILocatorable locator) {
            super(new Element(locator));
        }

        protected void searchForLabels() {
            items.clear();
            getPopup().setDefaultElement();
            List<Element> list = Element.searchElementsByXpath(getPopup(), TEMPLATES_TAG_LABEL);
            if (list.size() == 0) {
                PSLogger.warn("Can't find any labels on popup");
                PSLogger.save();
            }
            for (Element o : list) {
                String txt = o.getDEText();
                String id = o.getDEAttribute(TEMPLATES_TAG_LABEL_FOR);
                Item item = new Item(txt);
                item.set(new CheckBox(id));
                items.add(item);
            }
        }

        public List<String> getContent() {
            List<String> res = new ArrayList<String>();
            for (String e : getDEInnerText().split("<br/>")) {
                if (e.trim().isEmpty()) continue;
                res.add(e.trim());
            }
            return res;
        }

        public void selectAll() {
            new Link(getPopup().getChildByXpath(TEMPLATES_TAG_ALL)).click(false);
        }

        public void done() {
            super.done();
            this.setDefaultElement();
        }

    }
}
