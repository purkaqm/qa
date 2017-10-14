package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;


/**
 * Create gated project using Gated project template SNET</br>
 * 
 * Before this test execution must be created process or executed test:
 * com.powersteeringsoftware.tests.contentfiller.TestCreateProcess.java
 * 
 * @author selyaev_ag
 *
 */
public class TestCreateGP_SNET extends PSTest{

	public TestCreateGP_SNET() {
		name = "Create GP:SNET";
	}

	public void run(){
		GatedProject gatedProject = new GatedProject(
				TestDriver.GATED_PROJECT_NAME_SNET,
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_SNET);

		//gatedProject.addChampion(TestDriver.CHAMPION_NAME);
		gatedProject.addChampion(CoreProperties.getChampionName());

		new WorkItemCreateManager().createGatedProject(gatedProject);
	}
}
