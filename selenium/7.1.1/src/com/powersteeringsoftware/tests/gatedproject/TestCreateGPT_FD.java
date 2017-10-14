package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkTemplateFrameworkManager;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.tc.PSTest;


/**
 * Test creating gated project with scheduler constraints = ASAP.<br>
 * Any process must to be created before executing this test.
 */
public class TestCreateGPT_FD extends PSTest{

	public TestCreateGPT_FD(){
		name = "Create GPT:FD";
	}
	public void run() {
		WorkTemplateContainer template = new WorkTemplateContainer(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_FD);

		template.setIsShowSelectChildrenStep(true);
		template.setIsRequireGateEndDates(true);

		WorkTemplateFramework rgProject = new WorkTemplateFramework(
				TestDriver.GATED_PROJECT_NAME_FD, null);
		rgProject.setDateConstraintFD(TestDriver.START_DATE,TestDriver.END_DATE);

		WorkTemplateFrameworkManager workManager = new WorkTemplateFrameworkManager();
		workManager.create(template, rgProject);
	}
}
