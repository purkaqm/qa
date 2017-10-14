package com.powersteeringsoftware.tests.measures.instances;

import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Attach MT with all locked fields (formula, variance).</br>
 *
 * TODO: please check this test becouse of refactoring!!!
 *
 */
public class Test_AttachMTFormulaVariance extends PSTest {

	public Test_AttachMTFormulaVariance(){
		name = "Attach measure:formula, variance";
	}

	public void run() {
		String mtName =  MeasureManager.createMeasureTemplateFormulaVariance();
        MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);
        new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());
        new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()).doActionEdit(mtName);
		Assert.assertEquals(new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()).isDisabledAllLockedFields(),
				true, "Problem with validation");
	}
}
