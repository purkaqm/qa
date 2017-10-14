package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.elements_locators.SingleStatusSelectorLocators.ITEM;

/**
 * class like a flat selector
 * User: szuev
 * Date: 17.08.2010
 * Time: 18:05:52
 */
public class SingleStatusSelector extends FlatTagChooser {

    private boolean useDEForContent;

    public SingleStatusSelector(Element e) {
        this(e, false, true);
    }

    public SingleStatusSelector(Element e, boolean howToOpen, boolean useDEForContent) {
        super(e, howToOpen);
        this.useDEForContent = useDEForContent;
    }

    public void waitForPopup() {
        getPopup().waitForVisible();
        searchForItems(ITEM);
    }

    public void set(String label) {
        Element item = getItem(label);
        if (item == null &&
                TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1) &&
                SeleniumDriverFactory.getDriver().getType().isIE(6)) {
            throw new PSKnownIssueException(74818);
        }
        Assert.assertNotNull(item, "Can't fing item '" + label + "'");
        item.click(false);
        item.mouseUp();
        if (getDriver().getType().isIE()) {
            new Element("//body").mouseDownAndUp(); // hotfix for ie
        }
        getPopup().waitForUnvisible();
    }

    public String getContent() {
        String res = null;
        if (useDEForContent) {
            res = SimpleGrid.getTextContent(this, getClass()); // works in 9.1-9.3 at least
        }
        return res == null ? StrUtil.trim(getText()) : res;
    }


    public void setLastLabel(String label) {
        PSLogger.debug("Select last item '" + label + "'");
        openPopup();
        Element item = getItem(label, false);
        item.click(false);
        item.mouseUp();
        getPopup().waitForUnvisible();
    }


}
