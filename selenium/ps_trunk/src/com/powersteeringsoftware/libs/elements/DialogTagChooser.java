package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.DialogTagChooserLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 06.09.13
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public class DialogTagChooser extends TagChooser {


    public DialogTagChooser(ILocatorable locator) {
        super(new Element(locator));

    }

    protected void searchForLabels() {
        items.clear();
        getPopup().setDefaultElement();
        List<Element> list = Element.searchElementsByXpath(getPopup(), LABEL);
        if (list.size() == 0) {
            PSLogger.warn("Can't find any labels on popup");
            PSLogger.save();
        }
        for (Element o : list) {
            String txt = o.getChildByXpath(LABEL_ACRONYM).getDEText();
            String id = o.getDEAttribute(LABEL_FOR);
            Item item = new Item(txt);
            item.set(new CheckBox(id));
            items.add(item);
        }
    }

    public List<String> getContent() {
        List<String> res = new ArrayList<String>();
        for (Element e : Element.searchElementsByXpath(this, LABEL_ACRONYM)) {
            res.add(e.getDEText());
        }
        return res;
    }

    public Link getSelectAllLink() {
        return all != null ? all : (all = new Link(getPopup().getChildByXpath(ALL)));
    }

    public Link getSelectNoneLink() {
        return none != null ? none : (none = new Link(getPopup().getChildByXpath(NONE)));
    }

}
