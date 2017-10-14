package com.powersteeringsoftware.tests.measures;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.powersteeringsoftware.core.tc.Testable;
import com.powersteeringsoftware.tests.measures.instances.Test_Issue57270;
import com.powersteeringsoftware.tests.measures.instances.Test_AddMeasureManualVariance;
import com.powersteeringsoftware.tests.measures.instances.Test_EditMeasureInstance;
import com.powersteeringsoftware.tests.measures.instances.Test_AttachMeasureInstance;
import com.powersteeringsoftware.tests.measures.instances.Test_AttachMTManualVariance;
import com.powersteeringsoftware.tests.measures.instances.Test_AttachMTManualGoal;
import com.powersteeringsoftware.tests.measures.instances.Test_AttachMTFormulaVariance;
import com.powersteeringsoftware.tests.measures.instances.Test_AttachMTFormulaGoal;
import com.powersteeringsoftware.tests.measures.templates.Test_Issue57491;
import com.powersteeringsoftware.tests.measures.templates.Test_CreateMTManualGoal;
import com.powersteeringsoftware.tests.measures.templates.Test_DeleteMeasureTemplate;
import com.powersteeringsoftware.tests.measures.templates.Test_DetachMeasureTemplate;
import com.powersteeringsoftware.tests.measures.templates.Test_CopyMT;
import com.powersteeringsoftware.tests.measures.templates.Test_CreateMTManualVariance;
import com.powersteeringsoftware.tests.measures.templates.Test_CreateMTFormulaVariance;
import com.powersteeringsoftware.tests.measures.templates.Test_CreateMTFormulaGoal;


public class TestDriver{

	Logger log = Logger.getLogger("Driver.MeasureDriver");

	public TestDriver() {
	}

	@Test(groups = { "t_mf_31", "mf.mt" }, testName = "t_mf_31.Create MT(goal,manual)")
	public void Test_CreateMTManualGoal()  {
		Testable test = new Test_CreateMTManualGoal();
		test.execute();
	}

	@Test(groups = { "t_mf_33", "mf.mt" }, testName = "t_mf_33.Delete MT")
	public void Test_DeleteMeasureTemplate()  {
		Testable test = new Test_DeleteMeasureTemplate();
		test.execute();
	}

	@Test(groups = { "t_mf_34", "mf.mt" }, testName = "t_mf_34.Detach MT")
	public void Test_DetachMeasureTemplate()  {
		Testable test = new Test_DetachMeasureTemplate();
		test.execute();
	}

	@Test(groups = { "t_mf_36", "mf.mt" }, testName = "t_mf_36.Copy MT")
	public void Test_CopyMT()  {
		Testable test = new Test_CopyMT();
		test.execute();
	}

	@Test(groups = { "t_mf_96", "mf.mt" }, testName = "t_mf_96.Create MT(manual,variance)")
	public void Test_CreateMTManualVariance()  {
		Testable test = new Test_CreateMTManualVariance();
		test.execute();
	}

	@Test(groups = { "t_mf_97", "mf.mt" }, testName = "t_mf_97.Create MT(formula,variance)")
	public void Test_CreateMTFormulaVariance()  {
		Testable test = new Test_CreateMTFormulaVariance();
		test.execute();
	}

	@Test(groups = { "t_mf_98", "mf.mt" }, testName = "t_mf_98.Create MT(formula,variance)")
	public void Test_CreateMTFormulaGoal()  {
		Testable test = new Test_CreateMTFormulaGoal();
		test.execute();
	}

	@Test(groups = { "t_mf_103", "mf.mt" }, testName = "t_mf_103.issue#57491")
	public void Test_Issue57491()  {
		Testable test = new Test_Issue57491();
		test.execute();
	}

	@Test(groups = { "t_mf_114", "mf.mt" }, testName = "t_mf_114.issue#57270")
	public void Test_Issue57270()  {
		Testable test = new Test_Issue57270();
		test.execute();
	}

	@Test(groups = { "t_mf_125", "mf.mt" }, testName = "t_mf_125.Create Measure(manual,variance)")
	public void Test_AddMeasureInstanceManualVariance()  {
		Testable test = new Test_AddMeasureManualVariance();
		test.execute();
	}

	@Test(groups = { "t_mf_133", "mf.mt" }, testName = "t_mf_133.Edit Measure")
	public void Test_EditMeasureInstance()  {
		Testable test = new Test_EditMeasureInstance();
		test.execute();
	}

	@Test(groups = { "t_mf_189", "mf.mt" }, testName = "t_mf_189.Attach MT")
	public void Test_AttachMeasureInstance()  {
		Testable test = new Test_AttachMeasureInstance();
		test.execute();
	}

	@Test(groups = { "t_mf_210", "mf.mt" }, testName = "t_mf_210.Attach MT(manual,variance")
	public void Test_AttachMTManualVariance()  {
		Testable test = new Test_AttachMTManualVariance();
		test.execute();
	}

	@Test(groups = { "t_mf_212", "mf.mt" }, testName = "t_mf_212.Attach MT(manual,goal)")
	public void Test_AttachMTManualGoal()  {
		Testable test = new Test_AttachMTManualGoal();
		test.execute();
	}

	@Test(groups = { "t_mf_213", "mf.mt" }, testName = "t_mf_213.Attach MT(formula,variance)")
	public void Test_AttachMTFormulaVariance()  {
		Testable test = new Test_AttachMTFormulaVariance();
		test.execute();
	}

	@Test(groups = { "t_mf_214", "mf.mt" }, testName = "t_mf_214.Attach MT(formula,goal)")
	public void Test_AttachMTFormulaGoal()  {
		Testable test = new Test_AttachMTFormulaGoal();
		test.execute();
	}

}
