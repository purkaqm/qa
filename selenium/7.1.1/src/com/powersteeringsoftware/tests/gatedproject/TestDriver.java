package com.powersteeringsoftware.tests.gatedproject;

import org.testng.annotations.Test;
import com.powersteeringsoftware.core.tc.Testable;

public class TestDriver {
	public static final String GATED_PROJECT_TEMPLATE_NAME_ASAP = "GatedProjectTemplate_ASAP";
	public static final String GATED_PROJECT_NAME_ASAP = "GATED PROJECT ASAP";

	public static final String GATED_PROJECT_TEMPLATE_NAME_FNLT = "Gate Project Template FNLT";
	public static final String GATED_PROJECT_NAME_FNLT = "GATED PROJECT FNLT";

	public static final String GATED_PROJECT_TEMPLATE_NAME_SNET = "Gate Project Template SNET";
	public static final String GATED_PROJECT_NAME_SNET = "GATED PROJECT SNET";

	public static final String GATED_PROJECT_TEMPLATE_NAME_FD = "Gate Project Template FD";
	public static final String GATED_PROJECT_NAME_FD = "GATED PROJECT FD";

	public static final String START_DATE = "02/02/2010";
	public static final String END_DATE = "02/02/2010";

	//public static final String CHAMPION_NAME = "Admin";

	@Test(groups = { "create.GPT.ASAP"})
	public void TestCreateGPT_ASAP(){
		Testable test = new TestCreateGPT_ASAP();
		test.execute();
	}

	@Test(groups = { "create.GPT.FD"})
	public void TestCreateGPT_FD(){
		Testable test = new TestCreateGPT_FD();
		test.execute();
	}

	@Test(groups = { "create.GPT.SNET"})
	public void TestCreateGPT_SNET(){
		Testable test = new TestCreateGPT_SNET();
		test.execute();
	}

	@Test(groups = { "create.GPT.FNLT"})
	public void TestCreateGPT_FNLT(){
		Testable test = new TestCreateGPT_FNLT();
		test.execute();
	}

	@Test(groups = { "create.GP.ASAP"}
		  ,dependsOnGroups="create.GPT.ASAP"
		  )
	public void TestCreateGP_ASAP(){
		Testable test = new TestCreateGP_ASAP();
		test.execute();
	}

	@Test(groups = { "create.GP.FNLT"}
	  ,dependsOnGroups="create.GPT.FNLT"
	)
	public void TestCreateGP_FNLT(){
		Testable test = new TestCreateGP_FNLT();
		test.execute();
	}

	@Test(groups = { "create.GP.SNET"}
	  ,dependsOnGroups="create.GPT.SNET"
	)
	public void TestCreateGP_SNET(){
		Testable test = new TestCreateGP_SNET();
		test.execute();
	}


	@Test(groups = { "create.GP.FD"}
	  ,dependsOnGroups="create.GPT.FD"
	)
	public void TestCreateGP_FD(){
		Testable test = new TestCreateGP_FD();
		test.execute();
	}

	@Test(groups = { "add.deliverables"}
	  ,dependsOnGroups="create.GP.ASAP"
	)
	public void testAddDeliverables(){
		Testable test = new TestAddDeliverables();
		test.execute();
	}

	@Test(groups = { "remove.deliverables"}
	  ,dependsOnGroups="add.deliverables"
	)
	public void testRemoveDeliverables(){
		Testable test = new TestRemoveDeliverables();
		test.execute();
	}


}
