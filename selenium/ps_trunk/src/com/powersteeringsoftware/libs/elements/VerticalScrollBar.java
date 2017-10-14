package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 06.12.12
 * Time: 15:56
 * To change this template use File | Settings | File Templates.
 */
public class VerticalScrollBar extends Element implements ScrollBar {
    private Integer top;

    public VerticalScrollBar(ILocatorable locator) {
        super(locator);
    }

    public VerticalScrollBar(Element e) {
        super(e);
    }

    /**
     * scroll left
     *
     * @param to int number of pixels to scrolling
     */
    public void doScroll(int to) {
        PSLogger.info("Scroll(y) to " + to);
        String loc = getDomLocator();
        getDriver().getEval("var d=" + loc + "; d.scrollTop=" + to);
        top = to;
    }

    public int getScroll() {
        if (top != null) return top;
        String loc = getDomLocator();
        String pos = getDriver().getEval("var d=" + loc + "; d.scrollTop");
        PSLogger.debug("Top scroll position is " + (top = Integer.parseInt(pos)));
        return top;
    }
}
