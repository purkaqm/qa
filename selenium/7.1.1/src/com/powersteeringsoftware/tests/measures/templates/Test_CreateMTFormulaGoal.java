package com.powersteeringsoftware.tests.measures.templates;

import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.tests.measures.templates.utils.CreateAndTestMT;


/**
 * Create measure template with "Data Collection" = "Formula" and "Indicator
 * Type" = "Goal" .<br>
 *
 * @author selyaev_ag
 *
 */
public class Test_CreateMTFormulaGoal extends PSTest{

	public Test_CreateMTFormulaGoal(){
		name = "Create Measure Template: formula,goal";
	}

	public void run(){
		int dataCollection = 1; //formula
		int indicatorType = 0; // goal

		new CreateAndTestMT().execute(dataCollection, indicatorType);

//		//navigate page "Measure Template Library"
//		MainMenuAdapter.clickBrowseMeasureLibrary();
//
//
//		String mtName = MeasureManager.createMeasureTemplateFormulaGoal();
//
//        DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
//
//        //check measure template is displayed on the page "Measure Template Library"
//        Assert.assertEquals(seleniumDriver.isTextPresent(mtName),
//                            true,
//                            "Measure Template with name "+mtName+" doesn't exist on the page 'Measure Template Library'.");
//
//		//open Measure Template for editing and check all fields
//        MeasuresLibraryPageAdapter mlAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
//        mlAdapter.doActionEdit(mtName.toString());
//
//        MeasureTemplateEditPageAdapter mtEdit = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
//        String testedName = mtEdit.getName();
//        Assert.assertEquals(StringUtils.equals(testedName,mtName),
//                            true,
//                            "Name "+testedName+" is different to typed on creating "+mtName);
//
//		int dataCollectionFieldId = mtEdit.getDataCollectionType().getIndex();
//		Assert.assertEquals(dataCollectionFieldId,
//							dataCollection,
//							"Data Collection "+dataCollectionFieldId+ " is different to selected on creating "+dataCollection);
//
//		int indicatorTypeFieldId = mtEdit.getIndicatorTypeType().getIndex();
//		Assert.assertEquals(indicatorTypeFieldId,
//							indicatorType,
//							"Indicator Type "+ indicatorTypeFieldId+ " is different to selected on creating "+indicatorType);
	}
}
