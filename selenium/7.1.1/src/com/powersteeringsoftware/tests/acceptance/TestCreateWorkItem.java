package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.core.managers.work.WorkItemCreateManager;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;

/**
 * <b>Summary:</b><br>
 * Create work item using Browse>>Create Work feature.<br>
 * <b>Dependencies:</b><br>
 * - file core.properties must have property project.term (i.e project.term = Work).<br>
 *
 *
 *
 */
public class TestCreateWorkItem extends PSTest{


	public TestCreateWorkItem(){
		name = "Create Work";
	}

	/**
	 * After work item has been created it's object will be put in the TestSession.
	 */
	public void run() {
		//log.info("Test create work item has started");

		WorkItemCreateManager nWM = new WorkItemCreateManager();
		WorkItem project = WorkItem.createTimeStamped("IACWork", CoreProperties.getProjectTerm());
		nWM.createProject(project);

		TestSession.putObject(TestDriver.SESSION_KEY_WORK_ITEM_WORK, project);

		//log.info("Test create work item has finished");
	}
}
