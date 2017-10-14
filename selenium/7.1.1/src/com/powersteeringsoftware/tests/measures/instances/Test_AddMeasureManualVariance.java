package com.powersteeringsoftware.tests.measures.instances;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;


/**
 * Add new measure (manual and variance).
 *
 * @author selyaev_ag
 *
 */
public class Test_AddMeasureManualVariance extends PSTest {

	public Test_AddMeasureManualVariance(){
		name = "Add new measure:manual and variance";
	}

    public void run() {
    	/**
		 * Step 1: Create measure template
		 */
		String nameForDeleting = MeasureManager.createMeasureManualVariance(CoreProperties.getDefaultWorkItemIdWithPrefix());
		ManageMeasuresPageAdapter manageMeasuresAdapter = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		manageMeasuresAdapter.assertIsLoadedPageManageMeasure();

		Assert.assertEquals(manageMeasuresAdapter.existBySubstring(nameForDeleting),
				true,
				"Measure with name "+ nameForDeleting + " has not found");


		/**
		 * Step 2: Open for editing and check all edited fields
		 */
		manageMeasuresAdapter.doActionEdit(nameForDeleting);
		String testedName = new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()).getName();

		Assert.assertEquals(StringUtils.equals(testedName, nameForDeleting),
				true,
				"Name " + testedName + " is different to typed on creating " + nameForDeleting);

//		int dataCollectionFieldId = manageMeasuresAdapter.getDataCollectionType().getIndex();
//		Assert.assertEquals(dataCollectionFieldId,
//				dataCollection,
//				"Data Collection " + dataCollectionFieldId + " is different to selected on creating " + dataCollection);

//		int indicatorTypeFieldId = manageMeasuresAdapter.getIndicatorTypeType().getIndex();
//		Assert.assertEquals(indicatorTypeFieldId,
//				indicatorType,
//				"Indicator Type " + indicatorTypeFieldId + " is different to selected on creating " + indicatorType);
	}

}
