package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.PSTablePageLocators.*;

/**
 * Created by admin on 07.05.2014.
 */
public abstract class PSTablePage extends PSPage {
    public void more() {
        Link link = new Link(MORE);
        if (link.exists() && link.isVisible()) {
            PSLogger.info("Click 'More'");
            link.clickAndWaitNextPage();
            document = null;
        }
    }

    public List<Link> getNameLinks() {
        List<Element> names = getElements(false, NAME_COLUMN);
        List<Link> res = new ArrayList<Link>();
        for (Element n : names) {
            Element l = n.getChildByXpath(LINK);
            if (l != null && l.isDEPresent()) {
                String txt = l.getDEText();
                if (txt != null && !txt.isEmpty()) {
                    Link link = new Link(l);
                    link.setName(txt);
                    res.add(link);
                }
            }
        }
        return res;
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
}
