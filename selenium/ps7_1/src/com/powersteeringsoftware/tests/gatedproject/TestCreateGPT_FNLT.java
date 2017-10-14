package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkTemplateFrameworkManager;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.tc.PSTest;


public class TestCreateGPT_FNLT extends PSTest{

	public TestCreateGPT_FNLT(){
		name = "Create GPT:FNLT";
	}

	public void run() {
		WorkTemplateContainer template = new WorkTemplateContainer(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_FNLT);

		template.setIsShowSelectChildrenStep(false);


		WorkTemplateFramework rgProject = new WorkTemplateFramework(
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_FNLT, null);
		rgProject.setDateConstraintFNLT(TestDriver.END_DATE);

		WorkTemplateFrameworkManager workManager = new WorkTemplateFrameworkManager();
		workManager.create(template, rgProject);
	}

}
