package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;

import static com.powersteeringsoftware.libs.enums.elements_locators.LightboxDialogLocators.CLOSE;
import static com.powersteeringsoftware.libs.enums.elements_locators.LightboxDialogLocators.POPUP;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.06.2010
 * Time: 18:47:07
 */
public class LightboxDialog extends Element implements IDialog {
    private ImageBox popup = new ImageBox(POPUP);
    private static final long TIMEOUT_FOR_OPENING = 15000; //ms

    public LightboxDialog(ILocatorable locator) {
        super(locator);
    }

    public LightboxDialog(String locator) {
        super(locator);
    }

    public void open() {
        PSLogger.info("Open light-box dialog");
        mouseDownAndUp();
        click(false);
        try {
            waitForPopup();
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.warn("Failure: " + w);
            click(false);
            waitForPopup();
        }
        waitForLightBox();
    }

    private void waitForPopup() {
        popup.waitForVisible(TIMEOUT_FOR_OPENING);
        TimerWaiter.waitTime(500); // debug
    }

    void waitForLightBox() {
        popup.group.waitForVisible();
        popup.img.waitForVisible();
        PSLogger.save("Dialog is opened");
    }

    public void close() {
        PSLogger.info("Close light-box dialog");
        Element close = popup.getChildByXpath(CLOSE);
        close.waitForVisible(5000);
        close.click(false);
        popup.waitForUnvisible();
        TimerWaiter.waitTime(500); // debug
        PSLogger.save("After closing " + getClass().getName());
    }

    public ImageBox getImageBox() {
        return popup;
    }
}
