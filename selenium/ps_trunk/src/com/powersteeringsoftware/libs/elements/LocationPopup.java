package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.thoughtworks.selenium.SeleniumException;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.LocationPopupLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 22.10.12
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class LocationPopup extends DijitPopup {
    private Element link;
    private String popupId;
    private boolean isLink;

    public LocationPopup(String loc, boolean isLink) {
        super();
        link = (this.isLink = isLink) ? new Link(loc) : new Element(loc);
    }

    public void open() {
        PSLogger.info("Open location popup " + link.getLocator());
        if (isLink) {
            link.click(false);
        } else {
            link.focus();
            link.mouseDownAndUp();
        }
        waitForVisible();
        new Element(LOADING).waitForUnvisible();
        setDefaultElement();
    }

    public List<String> getLocation() {
        List<String> res = new ArrayList<String>();
        if (isLink) {
            Element location = null; //Element.searchElementByXpath(this, LOCATION);
            for (Element e : Element.searchElementsByXpath(this, TD_B)) {
                if (LOCATION_TXT.getLocator().equals(e.getDEText())) {
                    location = e;
                    break;
                }
            }
            if (location == null) {
                PSLogger.error(asXML());
                throw new NullPointerException("can't find " + LOCATION.getLocator());
            }
            Element td = location.getParent();
            String[] tdXmls = td.asXML().split("<img[^>]*/>");
            for (int i = 1; i < tdXmls.length; i++) {
                res.add(StrUtil.trim(tdXmls[i].replaceAll("<.*", "")));
            }
        } else {
            for (Element e : getLinks()) {
                res.add(e.getDEText());
            }
        }
        return res;
    }

    public List<Link> getLinks() {
        List<Link> res = new ArrayList<Link>();
        for (Element e : Element.searchElementsByXpath(this, LINK)) {
            res.add(new Link(e));
        }
        return res;
    }


    public void close() {
        PSLogger.info("Close location popup " + link.getLocator());
        Element div = getChildByXpath(DIV);
        if (!div.isDEPresent()) throw new SeleniumException("Can't find location popup content");
        popupId = div.getDEAttribute(WIDGET_ID);
        if (popupId == null || popupId.isEmpty()) throw new SeleniumException("Can't find location popup widget id");
        getDriver().getEval(CLOSE_SCRIPT.replace(popupId));
        waitForUnvisible();
    }
}
