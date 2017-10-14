package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Create gated project using Gated project template FNLT</br>
 *
 * Before this test execution must be created process or executed test:
 * com.powersteeringsoftware.tests.contentfiller.TestCreateProcess.java
 *
 * @author selyaev_ag
 *
 */
public class TestCreateGP_FNLT extends PSTest{

	public TestCreateGP_FNLT(){
		name = "Create GP:FNLT";
	}

	public void run(){
		GatedProject gatedProject = new GatedProject(
				TestDriver.GATED_PROJECT_NAME_FNLT,
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_FNLT);

		gatedProject.addChampion(CoreProperties.getChampionName());

		new WorkItemCreateManager().createGatedProject(gatedProject);
	}
}
