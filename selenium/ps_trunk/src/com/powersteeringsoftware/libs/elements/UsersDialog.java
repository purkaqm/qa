package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.elements_locators.UsersDialogLocators.SAVE_BUTTON;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 28.06.2010
 * Time: 18:53:15
 */
public class UsersDialog extends AbstractUserDialog {

    public UsersDialog() {
        super("");
    }

    public UsersDialog(ILocatorable locator, ILocatorable popupLoc) {
        super(locator, popupLoc);
    }

    public void submit() {
        getPopup().getChildByXpath(SAVE_BUTTON).click(false);
        getPopup().waitForUnvisible();
        getPopup().setDynamic();
    }


    public void set(User user) {
        set(user.getFormatFullName());
    }

    public void set(String user) {
        set(user, true);
    }

    protected void set(String user, boolean doThrow) {
        Element res = getSearchResult(user);
        if (res != null) {
            res.click(false);
            PSLogger.save("owner found");
            return;
        }
        if (!doThrow) {
            PSLogger.warn("Can't find link for user '" + user + "'");
        } else {
            Assert.fail("Can't find link for user '" + user + "'");
        }
        PSLogger.save();
    }


}
