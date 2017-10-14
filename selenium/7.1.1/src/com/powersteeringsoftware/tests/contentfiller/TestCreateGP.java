package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.managers.work.WorkTemplateFrameworkManager;
import com.powersteeringsoftware.core.objects.PSProcess;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Test creating gated project This test MUST be executed after test
 * TestProcessCreating has been executed, because this test uses session objects
 * (TestSession class) that are imported while TestProcessCreating executing.
 *
 * @deprecated see tests in com.powersteeringsoftware.tests.gatedproject
 *
 */
public class TestCreateGP extends PSTest {

	TestCreateGP(){
		name = "Create Gate Project";
	}


	public void run() {
		WorkTemplateContainer DMAICTemplate = createWorkTemplateDMAIC();
		WorkTemplateContainer CDDDRCTemplate = createWorkTemplateCDDDRC();
		createWorkDMAIC(DMAICTemplate);
		createWorkCDDDRC(CDDDRCTemplate);
	}

	private WorkTemplateContainer createWorkTemplateDMAIC() {
		PSProcess DMAIC = (PSProcess) TestSession
				.getObject(TestSessionObjectNames.PROCESS_DMAIC.getObjectKey());
		WorkTemplateFrameworkManager wTM = new WorkTemplateFrameworkManager();
		WorkTemplateContainer DMAICTemplate = new WorkTemplateContainer(
				"DMAIC Work Template");
		WorkTemplateFramework DMAICRoot = new WorkTemplateFramework(
				"DMAIC Work Template Root", DMAIC.getName());
		DMAICRoot
				.setStatusReporting(WorkTemplateFramework.StatusReportingFrequency.NO_FREQUENCY);
		DMAICRoot.setInheritPermissions(true);
		DMAICRoot.setInheritControls(true);
		DMAICRoot.setWebFolder(false);
		DMAICRoot.setControlCost(true);
		wTM.create(DMAICTemplate, DMAICRoot);

		return DMAICTemplate;
	}

	private WorkTemplateContainer createWorkTemplateCDDDRC() {
		PSProcess CDDDRC = (PSProcess) TestSession
				.getObject(TestSessionObjectNames.PROCESS_CDDDRC.getObjectKey());

		WorkTemplateContainer CDDDRCTemplate = new WorkTemplateContainer(
				"CDDDRC Work Template");
		WorkTemplateFramework CDDDRCRoot = new WorkTemplateFramework(
				"CDDDRC Work Template Root", CDDDRC.getName());

		CDDDRCRoot
				.setStatusReporting(WorkTemplateFramework.StatusReportingFrequency.NO_FREQUENCY);
		CDDDRCRoot.setInheritPermissions(true);
		CDDDRCRoot.setInheritControls(true);
		CDDDRCRoot.setWebFolder(false);
		CDDDRCRoot.setControlCost(false);

		new WorkTemplateFrameworkManager().create(CDDDRCTemplate, CDDDRCRoot);

		return CDDDRCTemplate;
	}

	private void createWorkDMAIC(WorkTemplateContainer DMAICTemplate) {
		GatedProject dmaicGP = GatedProject.create("DMAIC test project",
				DMAICTemplate.getName());
		dmaicGP.addChampion("Admino");
		try {
			new WorkItemCreateManager().createGatedProject(dmaicGP);
		} catch (Exception e) {
			throw new SeleniumException("Error on creating DMAIC test project", e);
		}
	}

	private void createWorkCDDDRC(WorkTemplateContainer CDDDRCTemplate) {
		GatedProject cdddrcGP = GatedProject.create("CDDDRC test project",
				CDDDRCTemplate.getName());
		cdddrcGP.addChampion("Admino");
		try {
			new WorkItemCreateManager().createGatedProject(cdddrcGP);
		} catch (Exception e) {
			throw new SeleniumException("Error on creating CDDDRC test project", e);
		}
	}
}
