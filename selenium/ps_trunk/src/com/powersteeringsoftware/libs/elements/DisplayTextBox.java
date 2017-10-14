package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.elements_locators.DisplayTextBoxLocators.ICON;
import static com.powersteeringsoftware.libs.enums.elements_locators.DisplayTextBoxLocators.ICON2;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 15:30:10
 */
public class DisplayTextBox extends Input {
    private Img icon;

    public DisplayTextBox(ILocatorable locator) {
        super(locator);
    }

    public DisplayTextBox(String locator) {
        super(locator);
    }

    public DisplayTextBox(Element e) {
        super(e);
    }

    public Img getIcon() {
        if (icon == null) {
            setDefaultElement();
            Element icon = new Element(getChildByXpath(ICON));
            if (!icon.isDEPresent())
                icon = new Element(getChildByXpath(ICON2));
            Assert.assertTrue(icon.isDEPresent(), "Can't find icon for display text box");
            this.icon = new Icon(icon);
        }
        icon.waitForVisible();
        return icon;
    }

    private class Icon extends Img {

        private Icon(Element e) {
            super(e);
        }

        public void click(boolean doWait) {
            waitForVisible(); // ?
            new TimerWaiter(500).waitTime(); // for debug ie-web-driver.
            if (getDriver().getType().isWebDriverIE()) {
                Element parent = getParent().getParent();
                PSLogger.debug("textbox : " + DisplayTextBox.this.asXML());
                PSLogger.debug("this : " + asXML());
                PSLogger.debug("parent : " + parent.asXML());
                parent.click(false);
                waitForVisible();
                new TimerWaiter(1500).waitTime(); // for debug ie-web-driver.
            }
            super.click(doWait);
        }
    }
}
