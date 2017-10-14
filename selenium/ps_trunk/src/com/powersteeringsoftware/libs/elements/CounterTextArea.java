package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.CounterTextAreaLocators;
import com.powersteeringsoftware.libs.util.TimerWaiter;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.06.2010
 * Time: 17:37:52
 */
public class CounterTextArea extends TextArea {
    private static final TimerWaiter WAITER = new TimerWaiter(2000);

    public CounterTextArea(ILocatorable locator) {
        super(locator);
    }

    public CounterTextArea(String locator) {
        super(locator);
    }

    public void setText(String text) {
        super.setText(text);
        WAITER.waitTime();
        setDefaultElement();
    }

    public int getCounter() {
        String attr = getAttribute(CounterTextAreaLocators.MAXLENGHT);
        if (attr != null && attr.matches("\\d+")) {
            return Integer.parseInt(attr);
        }
        return -1;
    }

    public String getCounterFromSpan() {
        click(false);
        WAITER.waitTime();
        setDefaultElement();
        Element span = getChildByXpath(CounterTextAreaLocators.COUNTER_SPAN);
        span.waitForVisible();
        return span.getText();
    }
}
