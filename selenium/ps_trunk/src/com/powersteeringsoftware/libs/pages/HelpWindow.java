package com.powersteeringsoftware.libs.pages;


import java.util.List;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Frame;
import com.powersteeringsoftware.libs.elements.Img;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.HelpWindowLocators.*;

/**
 * Class fo Help.
 * example of code to debug:
 * <p>
 * HomePage page = BasicCommons.logIn();
 * HelpWindow help = page.getHelpMenuOnlineHelp();
 * String con1;
 * String con2;
 * PSLogger.info("content 1: " + (con1 = help.getContent()));
 * Assert.assertFalse(con1.isEmpty(), "Empty help home");
 * help.next();
 * PSLogger.info("content 2 : " + (con2 = help.getContent()));
 * Assert.assertFalse(con2.isEmpty(), "Empty help next");
 * help.close();
 * Assert.assertNotSame(con1, con2, "The same");
 * </p>
 * User: szuev
 * Date: 07.09.11
 * Time: 12:27
 */
public class HelpWindow extends Window {
    private ContentFrame content;
    private TopFrame top;

    public String getContent() {
        StringBuilder sb = new StringBuilder();
        getContentFrame().select();
        for (Element e : getContentFrame().getDivs()) {
            String clazz = e.getDEClass();
            if (!isHelpClass(clazz)) continue;
            append(sb, e);
            for (Element ch : Element.searchElementsByXpath(e)) {
                append(sb, ch);
            }
        }
        return sb.toString();
    }

    public void next() {
        getTopFrame().select();
        getTopFrame().next();
        getContentFrame().waitForLoad();
        getContentFrame().setDefaultElement();
    }

    public void waitForOpen() {
        super.waitForOpen();
        getContentFrame().waitForLoad();
        getContentFrame().setDefaultElement();
    }

    public ContentFrame getContentFrame() {
        if (content == null) {
            content = new ContentFrame();
        }
        return content;
    }

    public TopFrame getTopFrame() {
        if (top == null) {
            top = new TopFrame();
        }
        return top;
    }

    private static boolean isHelpClass(String clazz) {
        if (clazz == null || clazz.isEmpty()) return false;
        for (String cl : getClassPrefixes()) {
            if (clazz.startsWith(cl)) {
                return true;
            }
        }
        return false;
    }

    private static void append(StringBuilder sb, Element e) {
        String txt = e.getDEText().trim();
        if (!txt.isEmpty())
            sb.append(txt).append("\n");
    }

    private class ContentFrame extends Frame {

        private static final long LOADING_TIMEOUT = 30000; // ms

        public ContentFrame() {
            super(MAIN_FRAME, CONTENT_FRAME);
        }

        public void waitForLoad(long timeout) {
            super.waitForLoad(timeout);
            if (!getDriver().getType().isRCDriverIE())
                getHeader().waitForVisible(timeout);
        }

        public void waitForLoad() {
            waitForLoad(LOADING_TIMEOUT);
        }

        public Element getHeader() {
            select();
            return getElement(DIV_HEADER);
        }

        public List<Element> getDivs() {
            return Element.searchElementsByXpath(this, DIV);
        }

        public void select() {
            PSLogger.debug("Select " + this);
            super.select();
        }

    }

    private class TopFrame extends Frame {
        public TopFrame() {
            super(MAIN_FRAME, TOP_FRAME, CONTENT_FRAME);
        }

        public void next() {
            Img img = new Img(getElement(NEXT));
            img.click(false);
            selectUp();
        }
    }
}
