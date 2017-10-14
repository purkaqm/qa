package com.powersteeringsoftware.tests.acceptance;

import org.testng.annotations.Test;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.tc.PSTest;


/**
 * Create Basic WorkItems 
 * 
 * @author vsumenkov
 */

@Test(groups = { "create.Basics" })
public class TestCreateBasics  {
	
	// Public methods - for TestNG execution
	public void CreateWork() {
		new TestCreateBasics_Work().execute();
	}

	public void CreateFolder() {
		new TestCreateBasics_Folder().execute();
	}	
	
	public void CreateMSP_Project() {
		new TestCreateBasics_MSP_Project().execute();
	}	

	// Helper classes - implementation
	private class TestCreateBasics_Work extends PSTest {
		private static final String PROJECT_TEMPLATE_NAME = "Work";
		private static final String PROJECT_NAME = "TMP-BASIC - WORK";

		public TestCreateBasics_Work() {
			name = "Create Basics:Work";
		}

		public void run() {
			new WorkItemCreateManager().createProject(new WorkItem(PROJECT_NAME, PROJECT_TEMPLATE_NAME));
		}
	}	
	
	private class TestCreateBasics_Folder extends PSTest {
		private static final String PROJECT_TEMPLATE_NAME = "Folder";
		private static final String PROJECT_NAME = "TMP-BASIC - FOLDER";

		public TestCreateBasics_Folder() {
			name = "Create Basics:Folder";
		}

		public void run() {
			new WorkItemCreateManager().createProject(new WorkItem(PROJECT_NAME, PROJECT_TEMPLATE_NAME));
		}
	}	
	
	
	private class TestCreateBasics_MSP_Project extends PSTest {
		private static final String PROJECT_TEMPLATE_NAME = "MSP Project";
		private static final String PROJECT_NAME = "TMP-BASIC - MSP Project";

		public TestCreateBasics_MSP_Project() {
			name = "Create Basics:MSP Project";
		}	
		
		protected void run() {
			new WorkItemCreateManager().createProject(new WorkItem(PROJECT_NAME, PROJECT_TEMPLATE_NAME));

		}
	}
	
}
