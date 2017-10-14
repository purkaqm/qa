package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.core.managers.work.WorkBreakdownManager;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;

public class TestPredessors extends PSTest{

	private WorkBreakdownManager wBM;

	public TestPredessors(){
		name = "WBS:Predessesors";
	}

	public void run() {

		WorkItem _project = (WorkItem) TestSession.getObject(TestDriver.SESSION_KEY_WORK_ITEM_WORK);
		WorkItem child00001 = WorkItem.create("Child00001", _project.getTerm());
		WorkItem child00002 = WorkItem.create("Child00002", _project.getTerm());
		WorkItem[] children = { child00001, child00002 };

		wBM = new WorkBreakdownManager();
		try{
			wBM.addChildren(_project, children);
			wBM.indent(child00002);
			wBM.setPredecessor(child00001, child00002);
		}catch(Exception e){
			throw new SeleniumException("Error on testing predessesors",e);
		}
	}


}
