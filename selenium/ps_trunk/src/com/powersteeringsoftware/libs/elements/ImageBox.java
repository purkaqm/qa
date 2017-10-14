package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.elements_locators.ImageBoxLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 18.08.2010
 * Time: 16:02:50
 */
public class ImageBox extends Element {
    Element group = getChildByXpath(GROUP);
    Element img = getChildByXpath(IMAGE);

    public ImageBox(ILocatorable locator) {
        super(locator);
    }

    public ImageBox(String locator) {
        super(locator);
    }

    public ImageBox(Element e) {
        super(e);
    }

    public void next() {
        PSLogger.info("Click next on image box");
        String groupWas = group.getText();
        getChildByXpath(NEXT).click(false);
        PSLogger.save("after clicking next");
        group.waitForTextChanged(groupWas);
        img.waitForVisible();
        PSLogger.info("Next image box group is " + group.getText());
        PSLogger.save("After next");
    }

    public void prev() {
        PSLogger.info("Click prev on image box");
        String groupWas = group.getText();
        Element prev = getChildByXpath(PREV);
        prev.waitForVisible(5000);
        prev.click(false);
        PSLogger.save("after clicking prev");
        group.waitForVisible();
        group.waitForTextChanged(groupWas);
        img.waitForVisible();
        PSLogger.info("Prev image box group is " + group.getText());
        PSLogger.save("After prev");
    }

    public LightboxDialog getLightBoxDialog() {
        return new LightboxDialog(OPEN_LIGHT_BOX);
    }

    public String getTitle() {
        Element title = getChildByXpath(TITLE);
        if (title.exists()) return title.getText();
        return "";
    }

    public String getDescription() {
        Element desc = getChildByXpath(DESCRIPTION);
        if (desc.exists()) return desc.getText();
        return "";
    }

    public String getImage() {
        if (img.exists()) return img.getAttribute("src");
        PSLogger.warn("Img doesn't exist");
        PSLogger.debug(asXML());
        PSLogger.save();
        return "";
    }

    public String getGroup() {
        return group.getText();
    }

}
