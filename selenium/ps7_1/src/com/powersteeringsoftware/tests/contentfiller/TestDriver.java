package com.powersteeringsoftware.tests.contentfiller;

import org.testng.annotations.Test;
import com.powersteeringsoftware.core.tc.Testable;
/**
 * Content Filler Tests
 *
 * @author selyaev_ag
 *
 */
public class TestDriver {

//	/**
//	 * Process CDDRC
//	 */
//	public static String SESSION_KEY_PROCESS_CDDDRC = "test.contentfiller.process.cdddrc";
//	/**
//	 * Process DMAIC
//	 */
//	public static String SESSION_KEY_PROCESS_DMAIC = "test.contentfiller.process.dmaic";

	public TestDriver(){
	}

	@Test(groups = { "addTag"},
			testName = "Empty Content Filler Tests: add tag")
	public void executeTest_AddTag()  {
		Testable test = new TestAddTag();
		test.execute();
	}

	@Test(groups = { "createCustomField"},
			testName = "Empty Content Filler Tests: custom field")
	public void executeTest_CreateCustomField()  {
		Testable test = new TestCreateCustomField();
		test.execute();
	}

	@Test(groups = { "createProcess" }, testName = "Empty Content Filler Tests:Create Process")
	public void executeTest_CreateProcess() {
		Testable test = new TestCreateProcess();
		test.execute();
	}

	/**
	 * @deprecated by gated project tetsts (see package com.powersteeringsoftware.tests.gatedproject)
	 */
	@Test(dependsOnGroups={"createProcess"},
			groups = { "createGP"},
			testName = "Empty Content Filler Tests: create Gated Projects")
	public void executeTest_CreateGP()  {
		Testable test = new TestCreateGP();
		test.execute();
	}
}

