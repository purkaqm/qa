package com.powersteeringsoftware.tests.acceptance;

import org.testng.annotations.Test;
import com.powersteeringsoftware.core.tc.Testable;


public class TestDriver {

	/**
	 * Work Item "Work"
	 */
	public static String SESSION_KEY_WORK_ITEM_WORK = "app.workitem.work";

	@Test(groups = { "createWork"},
			testName = "Internal Acceptance:create work")
	public void executeTest_CreateWorkItem()  {
		Testable test = new TestCreateWorkItem();
		test.execute();
	}

	@Test(groups = { "createIssue"},
			testName = "Internal Acceptance:create issue")
	public void executeTest_CreateIssue()  {
		//  run only in FF
		Testable test = new TestCreateIssue();
		test.execute();
	}

	@Test(groups = { "addAttachment"},
			dependsOnGroups= {"createIssue"},
			testName = "Internal Acceptance:add attachment")
	public void executeTest_AddAttachments()  {
		//  run only in FF
		Testable test = new TestAttachments();
		test.execute();
	}

	@Test(groups = { "updateAttachment"},
			dependsOnGroups= {"addAttachment"},
			testName = "Internal Acceptance:update attachment")
	public void executeTest_UpdateAttachment()  {
		//  run only in FF
		Testable test = new TestAttachmentUpdates();
		test.execute();
	}

	@Test(groups = { "testPredessors"},
			dependsOnGroups= {"createWork"},
			testName = "Internal Acceptance:test predessors")
	public void executeTest_TestPredessors()  {
		Testable test = new TestPredessors();
		test.execute();
	}

	@Test(groups = { "testIssue59872"},
			testName = "Internal Acceptance:test issue 59872")
	public void executeTest_TestIssue59872(){
		Testable test = new TestIssue59872();
		test.execute();
	}
}
