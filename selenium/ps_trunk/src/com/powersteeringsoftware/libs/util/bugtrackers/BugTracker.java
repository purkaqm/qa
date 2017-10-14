package com.powersteeringsoftware.libs.util.bugtrackers;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 17.10.2014.
 */
public abstract class BugTracker {

    private List<KnownIssue> _knises;

    protected BugTracker(List<KnownIssue> knises) {
        _knises = new ArrayList<KnownIssue>();
        for (KnownIssue k : knises) {
            if (isJira() == k.isJira()) _knises.add(k);
        }
        Collections.sort(_knises);
    }

    public boolean hasKnises() {
        return !_knises.isEmpty();
    }

    public List<KnownIssue> getKnises() {
        return _knises;
    }

    public abstract boolean isJira();

    public abstract String getUrl();

    public abstract String getContext();

    public abstract String getLogin();

    public abstract String getPassword();

    public void initZeroDriver() {
        PSLogger.info("Init Zero bug-treker");
        SeleniumDriverFactory.initZeroDriver(getUrl(), getContext());
    }

    public void init() {
        PSLogger.info("Init bug-treker");
        SeleniumDriverFactory.initNewDriver(CoreProperties.getBrowser(), getUrl(), getContext());
    }

    public abstract void doLogin();

    public abstract void openIssue(KnownIssue knownIssue);


}
