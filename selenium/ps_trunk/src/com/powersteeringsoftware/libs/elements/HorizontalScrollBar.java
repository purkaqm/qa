package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 25.01.11
 * Time: 17:52
 */
public class HorizontalScrollBar extends Element implements ScrollBar {
    private Integer left;

    public HorizontalScrollBar(ILocatorable locator) {
        super(locator);
    }

    public HorizontalScrollBar(Element e) {
        super(e);
    }

    /**
     * scroll left
     *
     * @param to int number of pixels to scrolling
     */
    public void doScroll(int to) {
        PSLogger.info("Scroll(x) to " + to);
        String loc = getDomLocator();
        getDriver().getEval("var d=" + loc + "; d.scrollLeft=" + to);
        left = to;
    }

    public int getScroll() {
        if (left != null) return left;
        String loc = getDomLocator();
        String pos = getDriver().getEval("var d=" + loc + "; d.scrollLeft");
        PSLogger.debug("Left scroll position is " + (left = Integer.parseInt(pos)));
        return left;
    }
}
