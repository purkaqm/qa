package com.powersteeringsoftware.tests.measures.templates;

import com.powersteeringsoftware.libs.tests.core.PSTest;


/**
 * Test issue 57491. For more details see issue 57491
 */
@Deprecated
public class Test_Issue57491 extends PSTest {

    /**
     * Error message text
     */
    public static String ERRM_DAY_FIELD_IS_INCORRECT = "'Day' field is incorrect";

    /**
     * Error value for the field day in any shedule
     */
    public static int ERR_VALUE = 99999999; //9999999999

    public Test_Issue57491() {
        name = "Issue 57491";
    }

    public void run() {
/*
        String mtName = MeasureManager.createMeasureTemplateManualGoal();
        MeasureTemplatesPage mLibraryPageAdapter = new MeasureTemplatesPage();
        mLibraryPageAdapter.doActionEdit(mtName);
        AddEditMeasureTemplatePage mtEditAdapter = new AddEditMeasureTemplatePage();
        //todo: i remove this method. need to create new.
        //mtEditAdapter.setScheduleReminderQuarterlyDate(ERR_VALUE, 1, "04:00 PM");
        mtEditAdapter.getContent().submitWithDialog();
        Assert.assertEquals(
                SeleniumDriverFactory.getDriver().isTextPresent(ERRM_DAY_FIELD_IS_INCORRECT), true,
                "Text " + ERRM_DAY_FIELD_IS_INCORRECT + " has not been found");
*/
    }

}
