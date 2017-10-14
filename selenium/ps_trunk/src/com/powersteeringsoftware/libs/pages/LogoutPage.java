package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.LogoutPageLocators.LOGOUT_RESTART_SESSION_LINK_1;
import static com.powersteeringsoftware.libs.enums.page_locators.LogoutPageLocators.LOGOUT_RESTART_SESSION_LINK_2;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 14.01.12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class LogoutPage extends Page {

    public LogInPage doRestart() {
        // hotfix for web-driver:
        Link restart = new Link(getDriver().getType().isWebDriverIE() || getDriver().getType().isGoogleChromeGreaterThan(18) ?
                LOGOUT_RESTART_SESSION_LINK_2 : LOGOUT_RESTART_SESSION_LINK_1);
        if (getDriver().getType().isGoogleChrome()) {
            restart.waitForPresent(5000, false);
        }

        if (restart.exists()) {
            LogInPage login = new LogInPage();
            if (getDriver().getType().isGoogleChromeGreaterThan(10) || getDriver().getType().isWebDriverIE()) {
                login.open();
            } else {
                PSLogger.save("push restart link.");
                restart.clickAndWaitNextPage();
            }
            PSLogger.save("After restarting");
            if (login.checkUrl())
                return login;
        }
        PSLogger.save("Can't find link 'here'");
        return null;
    }
}
