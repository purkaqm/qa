package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.WorkTreeElement;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.05.2010
 * Time: 15:13:21
 * To change this template use File | Settings | File Templates.
 */
public class WorkTreePage extends PSPage {
    private WorkTreeElement tree;
    private final static long TIMEOUT = CoreProperties.getWorkTreeTimeout();

    public WorkTreePage() {
        tree = new WorkTreeElement(25) {
            public void setDefaultElement() {
                super.setDefaultElement(getDocument());
            }
        };
        tree.setTimeout(TIMEOUT);
    }

    public void open() {
        open(true);
    }

    public void open(boolean doCheckTree) {
        try {
            clickBrowseWorkTree();
        } catch (Wait.WaitTimedOutException w) {
            if (!SeleniumDriverFactory.getDriver().getType().isIE()) throw w;
            PSLogger.warn(w.getMessage());
            PSLogger.saveFull();
            try {
                waitForPageToLoad(false, CoreProperties.getWaitForElementToLoad() * 2);
            } catch (Wait.WaitTimedOutException ww) {
                throw new PSKnownIssueException(72196, ww);
            }
        }
        tree.waitWhileLoading();
        if (doCheckTree)
            Assert.assertFalse(tree.getTreeNodes().size() == 0, "Empty work tree");
    }

    public SummaryWorkPage openWork(Work work) {
        Link link = tree.openTree(work);
        Assert.assertNotNull(link, "Cannot find tree link for '" + work.getFullName() + "'");
        SummaryWorkPage res = SummaryWorkPage.getInstance(false);
        link.setResultPage(res);
        link.clickAndWaitNextPage();
        return res;
    }

    public boolean isWorkPresent(Work work) {
        return tree.openTree(work) != null;
    }

    public void openTree() {
        tree.openTree();
    }

    public void openTree(String parent) {
        tree.openTree(parent);
    }

    public WorkTreeElement getWorkTree() {
        return tree;
    }

    public String toString() {
        return tree.print();
    }

}
