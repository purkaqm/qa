package com.powersteeringsoftware.tests.measures.templates;

import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.tests.measures.templates.utils.CreateAndTestMT;


/**
 * Create measure template with and "Data Collection" = "Manual" and "Indicator
 * Type" = "Goal" .<br>
 * For more details see test T-MF-98 in test suite T-MF-MT.
 *
 * @author selyaev_ag
 *
 */
public class Test_CreateMTManualGoal extends PSTest{

    public Test_CreateMTManualGoal(){
    	name = "Create measure template: manual,goal";
    }

	public void run(){
		int dataCollection = 0;
		int indicatorType = 0;

		new CreateAndTestMT().execute(dataCollection, indicatorType);

//		//navigate page "Measure Template Library"
//		AbstractMeasureListPageAdapter.clickBrowseMeasureLibrary();
//		AbstractMeasureListPageAdapter.assertIsLoadedPageMTLibrary();
//
//		String mtName = AbstractMeasureEditPageAdapter.getNewAutoName();
//		String mtDescr = AbstractMeasureEditPageAdapter.getAutoDescriptionForMT(mtName);
//
//		MeasureManager.createMeasureTemplateManualGoal(mtName,mtDescr);
//		//MeasureListPagesCommons.createSimpleM(mtName, mtDescr,dataCollection,indicatorType);
//
//        DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
//        //check measure template has displayed on the page "Measure Template Library"
//        Assert.assertEquals(seleniumDriver.isTextPresent(mtName),
//                            true,
//                            "Measure Template with name "+mtName+" doesn't exist on the page 'Measure Template Library'.");
//
//
//		//open Measure Template for editing and check all fields
//        AbstractMeasureListPageAdapter.doActionEdit(mtName.toString());
//
//        String testedName = AbstractMeasureEditPageAdapter.getName();
//        Assert.assertEquals(StringUtils.equals(testedName,mtName),
//                            true,
//                            "Name "+testedName+" is different to typed on creating "+mtName);
//
//        String testedDescr = AbstractMeasureEditPageAdapter.getDescription();
//        Assert.assertEquals(StringUtils.equals(testedDescr,mtDescr),
//                            true,
//                            "Descriptions "+testedDescr+" is different to typed on creating"+mtDescr);
//
//		int dataCollectionFieldId = AbstractMeasureEditPageAdapter.getDataCollectionType().getIndex();
//		Assert.assertEquals(dataCollectionFieldId,
//							dataCollection,
//							"Data Collection "+dataCollectionFieldId+ " is different to selected on creating "+dataCollection);
//
//		int indicatorTypeFieldId = AbstractMeasureEditPageAdapter.getIndicatorTypeType().getIndex();
//		Assert.assertEquals(indicatorTypeFieldId,
//							indicatorType,
//							"Indicator Type "+ indicatorTypeFieldId+ " is different to selected on creating "+indicatorType);
	}
}
