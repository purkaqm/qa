package com.powersteeringsoftware.libs.pages;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMElement;
import org.dom4j.tree.DefaultDocument;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getDriver;
import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.setDoWaitForReady;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 14.09.11
 * Time: 17:28
 */
public class XMLPage extends AbstractPage {
    private BrowserTypes browser;

    protected void open(String url) {
        SeleniumDriver driver = getDriver();
        browser = driver.getType();
        if (!browser.isWebDriver()) {
            PSSkipException.skip("Not supported for rc-driver : " + getClass().getSimpleName());
        }
        PSLogger.info("Open jsp by url " + url);
        try {
            if (browser.isFF())
                setDoWaitForReady(driver, false);
            document = null;
            super.open(url);
        } finally {
            if (browser.isFF())
                setDoWaitForReady(driver, true);
        }
        PSLogger.saveFull();
    }

    public Document getDocument() {
        if (document == null) {
            document = parseDocument();
        }
        return document;
    }

    private Document parseDocument() {
        Document doc = getDriver().getDocument();
        if (browser.isFF()) {
            return new DefaultDocument((Element) doc.selectSingleNode("//body/*"));
        }
        if (browser.isGoogleChrome()) {
            return new DefaultDocument((Element) doc.selectSingleNode("//div[@id='webkit-xml-viewer-source-xml']/*"));
        }
        Node d = doc.selectSingleNode("//body/div");
        StringBuilder sb = new StringBuilder();

        boolean classT = false;
        for (Object o : d.selectNodes("//div/*")) {
            DOMElement e = (DOMElement) o;
            if (e.getTagName().equals("a")) continue;
            String clazz = e.getAttribute("class");
            if (clazz == null) clazz = "";
            String txt = e.getText().replace("\u00A0", " ").trim();
            if (txt.isEmpty()) continue;
            if (classT) sb.append(" ");
            sb.append(txt);
            classT = clazz.equals("t");
        }
        return new DefaultDocument((Element) SeleniumDriverFactory.getDocument(sb.toString()).selectSingleNode("//body/*"));
    }

}
