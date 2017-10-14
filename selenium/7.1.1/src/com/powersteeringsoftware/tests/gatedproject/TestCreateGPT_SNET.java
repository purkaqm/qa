package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkTemplateFrameworkManager;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.tc.PSTest;



/**
 * Test creating gated project with scheduler constraints = SNLT.<br>
 *
 * Any process must to be created before executing this test.
 */
public class TestCreateGPT_SNET extends PSTest{

	public TestCreateGPT_SNET(){
		name = "Create GPT:SNET";
	}

	public void run() {
		WorkTemplateContainer template = new WorkTemplateContainer(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_SNET);

		template.setIsShowSelectChildrenStep(false);

		WorkTemplateFramework rgProject = new WorkTemplateFramework(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_SNET, null);
		rgProject.setDateConstraintSNET(TestDriver.START_DATE);

		WorkTemplateFrameworkManager workManager = new WorkTemplateFrameworkManager();
		workManager.create(template, rgProject);
	}
}
