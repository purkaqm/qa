package com.powersteeringsoftware.libs.elements;

import org.testng.Assert;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;

/**
 * Created by IntelliJ IDEA.
 * User: Pavilion
 * Date: 21/09/11
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserDialog extends AbstractUserDialog {


    /**
     * The constructors
     *
     * @param locator
     */
    public UserDialog(ILocatorable loc, ILocatorable pop) {
        super(loc, pop);
    }

    public void selectResult(User user) {
        selectResult(user.getFormatFullName());
    }

    public void selectResult(String user) {
        PSLogger.debug("Select user '" + user + "'");
        Element res = getSearchResult(user);
        if (res != null) {
            res.click(false);
            getPopup().waitForUnvisible();
            getPopup().setDynamic();
            PSLogger.save("after selecting owner '" + user + "'");
            return;
        }
        Assert.fail("Can't find user '" + user + "'");
    }


}
