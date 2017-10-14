package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.Wait;

public abstract class AbstractElementWaiter extends Wait {

    protected String locator = "Locator has not set";
    protected String errorMessage = "Error message has not set";

    protected AbstractElementWaiter(String locator, String errorMessage) {
        this.locator = locator;
        this.errorMessage = errorMessage;
    }



    public void defaultWait(long time) {
        try {
            wait(getErrorMessage(), time);
        } catch (WaitTimedOutException e) {
            String h = CoreProperties.getApplicationServerHost();
            if (!LocalServerUtils.ping(h)) {
                PSLogger.error("server '" + h + "' is not pingable");
            } else {
                PSLogger.warn("server '" + h + "' is pingable");
            }
            throw e;
        }
    }

    public void defaultWait() {
        defaultWait(CoreProperties.getWaitForElementToLoad());
    }

    protected String getLocator() {
        return locator;
    }

    protected String getErrorMessage() {
        return errorMessage;
    }

    public String toString() {
        return getErrorMessage();
    }


}
