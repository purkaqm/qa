package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkTemplateFrameworkManager;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.tc.PSTest;


/**
 * Create Root Gated Project with Date constraint = ASAP</br>
 *
 * Before this test execution must be created process or executed test:
 * com.powersteeringsoftware.tests.contentfiller.TestCreateProcess.java
 *
 * @author selyaev_ag
 */
public class TestCreateGPT_ASAP extends PSTest{

	public TestCreateGPT_ASAP(){
		name = "Create GPT:ASAP";
	}

	public void run() {

		WorkTemplateContainer template = new WorkTemplateContainer(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_ASAP);
		template.setIsShowSelectChildrenStep(false);

		WorkTemplateFramework rgProject = new WorkTemplateFramework(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_ASAP, null);
		rgProject.setDateConstraintASAP();

		WorkTemplateFrameworkManager workManager = new WorkTemplateFrameworkManager();
		workManager.create(template, rgProject);
	}

}
