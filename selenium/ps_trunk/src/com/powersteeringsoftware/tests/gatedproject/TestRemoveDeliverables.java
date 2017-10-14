package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.DeliverablesListingPage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.pages.WorkTreePage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

public class TestRemoveDeliverables extends PSTest {

    private GatedProject gatedProject;
    private Work gate;
    private Work deliverable;

    public TestRemoveDeliverables(Work gate, Work toAdd) {
        this.name = "Remove Deliverables";
        gatedProject = (GatedProject) gate.getParent();
        this.gate = gate;
        deliverable = toAdd;
    }


    protected void run() {
        SummaryWorkPage summary = WorkManager.open(gatedProject);

        DeliverablesListingPage page = summary.clickAddRemove();
        Assert.assertTrue(page.isDeliverablePresent(gate.getName(), deliverable.getName()), "Can't find deliverable " + deliverable + " on Deliverables page");
        page.select(gate.getName(), deliverable.getName());
        page.pushDelete();
        Assert.assertFalse(page.isDeliverablePresent(gate.getName(), deliverable.getName()), "There is deliverable " + deliverable + " on Deliverables page after deleting");

        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3) || CoreProperties.getBrowser().isRCDriverFF()) {
            // can' expand summary grid tree under other browsers, skip this checking
            SummaryWorkPage sum = WorkManager.open(gatedProject);
            Assert.assertFalse(sum.isDeliverableExist(deliverable.getName()), "There is deliverable " + deliverable.getName());
        }

        WorkTreePage tree = new WorkTreePage();
        tree.open();
        Assert.assertFalse(tree.isWorkPresent(deliverable), "There is deliverable " + deliverable + " on WorkTree page");
    }


}
