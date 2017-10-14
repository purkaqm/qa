package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.enums.page_locators.LogInPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.thoughtworks.selenium.SeleniumException;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.HomePageLocators.URL;
import static com.powersteeringsoftware.libs.enums.page_locators.LogInPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 14.01.12
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
public class LogInPage extends APSPage {

    public void open() {
        open(URL.getLocator());
    }

    public boolean checkUrl() {
        return new Input(LOGIN_PASSWORD_INPUTFIELD).exists();
    }

    public String getVersion() {
        String res = getLoginVersion(this, VERSION_V13);
        if (res == null) res = getLoginVersion(this, VERSION_V11);
        if (res == null) res = getLoginVersion(this, VERSION_V10);
        return res;
    }

    private static String getLoginVersion(LogInPage page, LogInPageLocators loc) {
        Element e = new Element(loc);
        String description;
        if (page.getDriver().getType().isWebDriver()) { // js error?
            e.setDefaultElement();
            description = e.getDEText();
        } else {
            description = e.getText();
        }
        PSLogger.info("Application description is '" + description + "'");
        return StringUtils.isEmpty(description) ? null : description.replaceAll("^v\\.\\s*", "");
    }

    public HomePage doLogIn(String name, String password) {
        Input in = new Input(LOGIN_PASSWORD_INPUTFIELD);
        if (!in.exists())
            checkBlankPage();
        in.type(password);
        new Input(LOGIN_NAME_INPUTFIELD).type(name);

        Button licenseButton = new Button(LICENSE_BUTTON);
        Button loginButton;
        if (licenseButton.exists()) {
            licenseButton.click(false);
            new Element(LICENSE_DIALOG).waitForVisible();
            loginButton = new Button(LICENSE_ACCEPT);
        } else {
            loginButton = new Button(LOGIN_SUBMIT_BOTTON);
        }
        loginButton.focus();
        loginButton.submit();

        HomePage home = new HomePage();
        BrowserTypes br = getDriver().getType();
        if (CoreProperties.doValidatePage() && !br.isGoogleChrome() &&
                !br.isRCDriverIE() && !br.isZero()) {
            home.validate();
        }
        /*home.waitForPageToLoad(CoreProperties.doValidatePage() && !getDriver().getType().isGoogleChrome() &&
                !getDriver().getType().isRCDriverIE());*/
        home.setLastClickSubmitTime(loginButton.getLastClickTime());
        String error;
        Assert.assertNull(error = home.getErrorBoxMessage(), "can't log in under (" + name + ":" + password +
                "), error: '" + error + "'");
        PSLogger.save("After submit");
        if (checkUrl()) {
            PSLogger.warn("failed to login...");
            //
            if (!getDriver().getType().isWebDriverIE()) throw new SeleniumException("failed to login");
            return null;
        }
        if (!home.checkUrl()) {
            class BlankPage extends Page {
                public boolean checkUrl() {
                    return checkUrl(BLANK_PAGE);
                }
            }
            if (new BlankPage().checkUrl()) {
                PSLogger.knis(75256);
                open();
            }
            LogoutPage logout = new LogoutPage();
            if (logout.checkUrl()) {
                PSLogger.save("Seems session expired");
                logout.doRestart();
            }
            return null;
        }
        return home;
    }

}
