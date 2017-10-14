package com.powersteeringsoftware.libs.elements.waiters;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.01.12
 * Time: 14:39
 */
public class ScriptWaiter extends AbstractElementWaiter {
    private String condition;

    protected ScriptWaiter(String script, String condition, String errorMessage) {
        super(script, errorMessage);
        this.condition = condition;
    }

    public ScriptWaiter(String script, boolean condition) {
        this(script, String.valueOf(condition), "Script (" + script + ") doesn't accept " + condition + " during timeout");
    }

    @Override
    public boolean until() {
        try {
            String res = SeleniumDriverFactory.getDriver().getEval(locator);
            return res != null && res.equals(condition);
        } catch (SeleniumException se) {
            PSLogger.warn(se);
        }
        return false;
    }
}
