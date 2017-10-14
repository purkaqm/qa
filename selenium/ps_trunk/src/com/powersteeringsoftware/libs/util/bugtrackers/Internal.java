package com.powersteeringsoftware.libs.util.bugtrackers;

import com.powersteeringsoftware.libs.pages.DiscussionIssueViewPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;

import java.util.List;

/**
 * Created by admin on 17.10.2014.
 */
public class Internal extends BugTracker {
    protected Internal(List<KnownIssue> knises) {
        super(knises);
    }

    @Override
    public boolean isJira() {
        return false;
    }

    @Override
    public String getUrl() {
        return CoreProperties.getInternalUrl();
    }

    @Override
    public String getContext() {
        return CoreProperties.getInternalContext();
    }

    @Override
    public String getLogin() {
        return CoreProperties.getInternalUser();
    }

    @Override
    public String getPassword() {
        return CoreProperties.getInternalPassword();
    }

    @Override
    public void doLogin() {
        BasicCommons.logIn(getLogin(), getPassword(), false);
    }

    @Override
    public void openIssue(KnownIssue kn) {
        DiscussionIssueViewPage page = new DiscussionIssueViewPage();
        page.open(kn.getId());
        kn.setSubject(page.getTitle());
        kn.setIsClosed(!page.getOpen());
    }
}
