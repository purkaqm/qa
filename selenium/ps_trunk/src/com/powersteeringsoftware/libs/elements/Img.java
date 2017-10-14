package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 04.08.11
 * Time: 14:15
 */
public class Img extends Element {
    public Img(ILocatorable locator) {
        super(locator);
    }

    public Img(String locator) {
        super(locator);
    }

    public Img(Document doc, ILocatorable locator) {
        super(doc, locator);
    }

    public Img(Document doc, String locator) {
        super(doc, locator);
    }

    public Img(Element e) {
        super(e);
    }

    public String getSrc() {
        return defaultElement != null ? getDEAttribute("src") : getAttribute("src");
    }

    public void waitForSrc(ILocatorable loc) {
        waitForSrc(loc.getLocator());
    }

    public void waitForSrc(String src) {
        waitForAttribute("src", src);
    }

}
