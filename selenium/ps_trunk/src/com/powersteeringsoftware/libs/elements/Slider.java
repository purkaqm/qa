package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.dom4j.Document;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.elements_locators.SliderLocators.INPUT;
import static com.powersteeringsoftware.libs.enums.elements_locators.SliderLocators.ROLLER;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 09.06.2010
 * Time: 20:26:31
 */
public class Slider extends Element {
    private static final float ALLOWED_DIFFERENCE = 5F; //%
    private static final TimerWaiter WAITER = new TimerWaiter(1000); // for chrome, ie.

    public Slider(ILocatorable locator) {
        super(locator);
    }

    public Slider(Element e) {
        super(e);
    }

    public Slider(Document doc, ILocatorable locator) {
        super(doc, locator);
    }

    public Slider(String locator) {
        super(locator);
    }

    public Element getRunner() {
        return getChildByXpath(ROLLER);
    }

    public void clickAt(int percentage) {
        PSLogger.info("Set slider to " + percentage + "%");
        Integer[] xywhSlider = getRunner().getCoordinates();
        Integer[] xywhBar = getCoordinates();
        Assert.assertFalse(xywhBar == null || xywhSlider == null, "Can't determinate slider/bar coordinates");
        int x;
        int y;
        if (xywhBar[2] > xywhBar[3]) { //horizontal
            x = xywhBar[0] + (int) (xywhBar[2] * percentage / 100f);
            y = xywhBar[1] + xywhBar[3] / 2;
        } else { //vertical
            x = xywhBar[0] + xywhBar[2] / 2;
            y = xywhBar[1] + (int) (xywhBar[3] * percentage / 100f);
        }
        x -= xywhSlider[0];
        y -= xywhSlider[1];
        getRunner().focus();
        getRunner().dragAndDrop(x, y);
        WAITER.waitTime();
        PSLogger.save();
    }

    public void scrollTo() {
        super.scrollTo();
        WAITER.waitTime();
    }

    public String getValue() {
        String value = getChildByXpath(INPUT).getValue();
        PSLogger.debug("Slider value is " + value);
        return value;
    }

    public boolean compare(int expectedValue) {
        return compare(expectedValue, Float.parseFloat(getValue()));
    }

    public static boolean compare(int expectedValue, float value) {
        int iVal = Math.round(value);
        double diff = Math.abs(expectedValue - iVal);
        PSLogger.debug("Diff is " + diff);
        return diff <= ALLOWED_DIFFERENCE;
    }

}
