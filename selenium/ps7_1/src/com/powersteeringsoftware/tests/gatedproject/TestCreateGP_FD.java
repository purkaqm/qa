package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Create gated project using Gated project template FD</br>
 *
 * Before this test execution must be created process or executed test:
 * com.powersteeringsoftware.tests.contentfiller.TestCreateProcess.java
 *
 * @author selyaev_ag
 *
 */
public class TestCreateGP_FD extends PSTest{

	public TestCreateGP_FD(){
		name = "Create GP:FD";
	}

	public void run(){
		GatedProject gatedProject = new GatedProject(
				TestDriver.GATED_PROJECT_NAME_FD,
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_FD);

		gatedProject.addChampion(CoreProperties.getChampionName());


		new WorkItemCreateManager().createGatedProject(gatedProject);
	}
}
