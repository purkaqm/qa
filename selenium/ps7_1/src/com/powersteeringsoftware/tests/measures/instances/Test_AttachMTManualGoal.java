package com.powersteeringsoftware.tests.measures.instances;

import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Attach MT with all locked fields (manual, goal)
 *
 */
public class Test_AttachMTManualGoal extends PSTest {

	public Test_AttachMTManualGoal(){
		name = "Attach Measure:manual and goal";
	}


	public void run() {
		String mtName = MeasureManager.createMeasureTemplateManualGoal();
		MeasuresLibraryPageAdapter mtAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
		mtAdapter.doActionEdit(mtName);

		MeasureTemplateEditPageAdapter mtEditAdapter = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
		mtEditAdapter.lockAllFields();
		mtEditAdapter.submitChangesWithUpperButtonForEditedM();

		MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

		new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());
		ManageMeasuresPageAdapter manageMAAdpater = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		manageMAAdpater.doActionEdit(mtName);
		Assert.assertEquals(new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()).isDisabledAllLockedFields(),
				true, "Problem with validation");
	}

}
