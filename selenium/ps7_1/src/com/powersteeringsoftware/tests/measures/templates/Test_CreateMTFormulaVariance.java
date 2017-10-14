package com.powersteeringsoftware.tests.measures.templates;

import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.tests.measures.templates.utils.CreateAndTestMT;


/**
 * Create measure template with "Indicator Type" = "Formula" and "Data Collection" =
 * "Variance".<br>
 * For more details see test T-MF-98 in test suite T-MF-MT.
 *
 * @author selyaev_ag
 *
 */
public class Test_CreateMTFormulaVariance extends PSTest {

    public Test_CreateMTFormulaVariance(){
    	name = "Create measure: formula, variance";
    }

	public void run(){
		int dataCollection = 1;
		int indicatorType = 1;

		new CreateAndTestMT().execute(dataCollection, indicatorType);

//		MainMenuAdapter.clickBrowseMeasureLibrary();
//
//
//
//		String mtName = MeasureManager.createMeasureTemplateFormulaVariance();
//
//        DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
//
//        Assert.assertEquals(seleniumDriver.isTextPresent(mtName),
//                            true,
//                            "Measure Template with name "+mtName+" doesn't exist on the page 'Measure Template Library'.");
//
//        //open Measure Template for editing and check all fields
//        AbstractMeasureListPageAdapter.doActionEdit(mtName.toString());
//
//        String testedName = AbstractMeasureEditPageAdapter.getName();
//        Assert.assertEquals(StringUtils.equals(testedName,mtName),
//                            true,
//                            "Name "+testedName+" is different to typed on creating "+mtName);
//
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
