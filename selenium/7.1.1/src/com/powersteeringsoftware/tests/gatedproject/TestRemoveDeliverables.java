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

public class TestRemoveDeliverables extends PSTest{

	private String deliverableName = new TimeStampName("NewDeliverable").getTimeStampedName();
	private WIDeliverablesAdapter adapterDeliverables;
	public TestRemoveDeliverables(){
		this.name = "Remove Deliverables";
	}


	protected void run() {
		addDeliverable();
		deleteDeliverable();
		assertActions();
	}


	private void assertActions() {
		Assert.assertEquals(false, adapterDeliverables.isDeliverableExistByName(deliverableName));

		navigateSummaryPage();
		new WISummaryAdapter().isDeliverableExist(deliverableName);
	}

	private void deleteDeliverable(){
		adapterDeliverables.checkDeleteCheckBoxByWorkName(deliverableName);
		adapterDeliverables.pushDelete();
	}

	private void addDeliverable(){
		navigateSummaryPage();

		adapterDeliverables = new WISummaryAdapter().clickAddRemove();
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
		createWorkAdapter.typeWorkName(deliverableName);
		createWorkAdapter.clickFinishAndSave();

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
