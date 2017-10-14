package com.powersteeringsoftware.tests.measures.templates;

import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.tests.measures.templates.utils.CreateAndTestMT;


/**
 * Create measure template with "Data Collection" = "Manual" and "Indicator
 * Type" = "Variance" <br>
 * For more details see test T-MF-98 in test suite T-MF-MT.
 *
 * @author selyaev_ag
 *
 */
public class Test_CreateMTManualVariance extends PSTest {

    public Test_CreateMTManualVariance(){
    	name = "Create measure template:manual, variance";
    }

    public void run(){
    	int dataCollection = 0; //manual
		int indicatorType = 1; //variance

		new CreateAndTestMT().execute(dataCollection, indicatorType);
//
//		String mtName = MeasureManager.createMeasureTemplateManualVariance();
//        assertExistMT(mtName);
//
//        AbstractMeasureListPageAdapter.doActionEdit(mtName.toString());
//        assertName(mtName);
//		assertDataCollection(dataCollection);
//		assertIndicatorType(indicatorType);
//	}
//
//	private void assertExistMT(String mtName) {
//		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
//		Assert.assertEquals(seleniumDriver.isTextPresent(mtName),
//                            true,
//                            "Measure Template with name "+mtName+" hasn't been created exist on the page 'Measure Template Library'.");
//	}
//
//	private void assertIndicatorType(int indicatorType) {
//		int indicatorTypeFieldId = AbstractMeasureEditPageAdapter.getIndicatorTypeType().getIndex();
//		Assert.assertEquals(indicatorTypeFieldId,
//							indicatorType,
//							"Indicator Type "+ indicatorTypeFieldId+ " is different to selected on creating "+indicatorType);
//	}
//
//	private void assertDataCollection(int dataCollection) {
//		int dataCollectionFieldId = AbstractMeasureEditPageAdapter.getDataCollectionType().getIndex();
//		Assert.assertEquals(dataCollectionFieldId,
//							dataCollection,
//							"Data Collection "+dataCollectionFieldId+ " is different to selected on creating "+dataCollection);
//	}
//
//	private void assertName(String mtName) {
//		String testedName = AbstractMeasureEditPageAdapter.getName();
//        Assert.assertEquals(StringUtils.equals(testedName,mtName),
//                            true,
//                            "Name "+testedName+" is different to typed on creating "+mtName);
	}

}
