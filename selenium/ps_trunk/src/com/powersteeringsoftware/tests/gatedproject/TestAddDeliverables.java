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

/**
 * Summary: Test add several deliverables on the summary page of gated project<br>
 * This test executing requires:
 * - executing test create Process
 * - executing test CreateGP ASAP.
 * <p/>
 * UNDER DEVELOPMENT - see TODOs
 */
public class TestAddDeliverables extends PSTest {
    private GatedProject gatedProject;
    private Work gate;
    private Work deliverable;

    public TestAddDeliverables(Work gate, Work toAdd) {
        name = "Add Deliverables";
        gatedProject = (GatedProject) gate.getParent();
        this.gate = gate;
        deliverable = toAdd;
    }

    protected void run() {
        DeliverablesListingPage page = WorkManager.addDeliverables(gatedProject, gate, deliverable);
        Assert.assertTrue(page.isDeliverablePresent(gate.getName(), deliverable.getName()), "Can't find deliverable " + deliverable + " on Deliverables page");

        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_3) || CoreProperties.getBrowser().isRCDriverFF()) {
            // can' expand summary grid tree under other browsers, skip this checking
            SummaryWorkPage sum = WorkManager.open(gatedProject);
            Assert.assertTrue(sum.isDeliverableExist(deliverable.getName()), "Can't find deliverable " + deliverable.getName());
        }

        WorkTreePage tree = new WorkTreePage();
        tree.open();
        Assert.assertTrue(tree.isWorkPresent(deliverable), "Can't find deliverable " + deliverable + " on WorkTree page");

    }


}
