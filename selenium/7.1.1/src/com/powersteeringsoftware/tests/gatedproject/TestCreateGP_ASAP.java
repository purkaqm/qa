package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;

/**
 * Create gated project using Gated project template ASAP<br>
 * Test Create GPT ASAP (TestCreateGPT_ASAP.java)  must be executed before this one.
 */
public class TestCreateGP_ASAP extends PSTest{

	public TestCreateGP_ASAP(){
		this.name = "Create GP:ASAP";
	}

	public void run(){
		GatedProject gatedProject = new GatedProject(
				TestDriver.GATED_PROJECT_NAME_ASAP,
				TestDriver.GATED_PROJECT_TEMPLATE_NAME_ASAP);

		gatedProject.addChampion(CoreProperties.getChampionName());
		new WorkItemCreateManager().createGatedProject(gatedProject);

		TestSession.putObject(TestSessionObjectNames.GATED_PROJECT_ASAP.getObjectKey(), gatedProject);
	}
}
