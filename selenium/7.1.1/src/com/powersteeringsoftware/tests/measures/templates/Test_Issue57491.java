package com.powersteeringsoftware.tests.measures.templates;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;


/**
 * Test issue 57491. For more details see issue 57491
 *
 *
 */
public class Test_Issue57491 extends PSTest{

	/**
	 * Error message text
	 */
	public static String ERRM_DAY_FIELD_IS_INCORRECT = "'Day' field is incorrect";

	/**
	 * Error value for the field day in any shedule
	 */
	public static int ERR_VALUE = 99999999;

	public Test_Issue57491(){
		name = "Issue 57491";
	}

	public void run() {
		String mtName = MeasureManager.createMeasureTemplateManualGoal();
		MeasuresLibraryPageAdapter mLibraryPageAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
		mLibraryPageAdapter.doActionEdit(mtName);
		MeasureTemplateEditPageAdapter mtEditAdapter = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
		mtEditAdapter.setScheduleReminderQuarterlyDate(ERR_VALUE, 1, "04:00 PM");
		mtEditAdapter.submitChangesWithUpperButtonForEditedM();
		Assert.assertEquals(
				SeleniumDriverSingleton.getDriver().isTextPresent(ERRM_DAY_FIELD_IS_INCORRECT), true,
				"Text " + ERRM_DAY_FIELD_IS_INCORRECT + " has not been found");
	}

}
