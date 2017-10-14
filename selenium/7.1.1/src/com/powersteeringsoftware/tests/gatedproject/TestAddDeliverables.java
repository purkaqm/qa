package com.powersteeringsoftware.tests.gatedproject;

import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.workitems.CreateWorkAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WIDeliverablesAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.objects.PSProcess;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Summary: Test add several deliverables on the summary page of gated project<br>
 * This test executing requires:
 * - executing test create Process
 * - executing test CreateGP ASAP.
 *
 * UNDER DEVELOPMENT - see TODOs
 *
 */
public class TestAddDeliverables extends PSTest {

	public TestAddDeliverables() {
		name = "Add Deliverables";
	}


	protected void run() {
		navigateSummaryPage();

		WIDeliverablesAdapter adapterDeliverables = new WISummaryAdapter().clickAddRemove();
		adapterDeliverables.pushAddNew();

		PSProcess process = null;
		try{
			process = (PSProcess) TestSession.getObject(TestSessionObjectNames.PROCESS_DMAIC.getObjectKey());
		} catch (ClassCastException cce){
			throw new SeleniumException("There is no process in TestSession", cce);
		}
		adapterDeliverables.setParameters(CoreProperties.getProjectTerm(), process.getPhasesList().pop().getName());
		adapterDeliverables.pushContinue();

		CreateWorkAdapter createWorkAdapter = new CreateWorkAdapter(SeleniumDriverSingleton.getDriver());
		String timestampedName = new TimeStampName("NewWorkItem").getTimeStampedName();
		createWorkAdapter.typeWorkName(timestampedName);
		createWorkAdapter.clickFinishAndSave();

		navigateSummaryPage();

		Assert.assertEquals(true, new WISummaryAdapter().isDeliverableExist(timestampedName));

	}


	private void navigateSummaryPage() {
		GatedProject gatedProject = null;
		try{
			Object o = TestSession.getObject(TestSessionObjectNames.GATED_PROJECT_ASAP.getObjectKey());

			if(o==null) Assert.fail("Gated project ASAP has not been found ");

			gatedProject = (GatedProject)o;

		} catch (ClassCastException ñce){
			Assert.fail("Gated project ASAP has not been found ", ñce);
		}
		new WISummaryAdapter().navigatePageSummary(gatedProject.getUid());
	}

}
