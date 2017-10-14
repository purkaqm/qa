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
 * Attach measure template (manual, variance) with all locked fields.
 *
 *  @author selyaev_ag
 *
 */
public class Test_AttachMTManualVariance extends PSTest{

	public Test_AttachMTManualVariance(){
		name ="Attach measure:manual and variance";
	}

	public void run(){
			String mtName = MeasureManager.createMeasureTemplateManualVariance();
			MeasuresLibraryPageAdapter measureLibraryAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
			measureLibraryAdapter.doActionEdit(mtName);

			MeasureTemplateEditPageAdapter mtEditAdapter = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
			mtEditAdapter.lockAllFields();
			mtEditAdapter.submitChangesWithUpperButtonForEditedM();

			MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

			new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());
			new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()).doActionEdit(mtName);
			Assert.assertEquals(new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()).isDisabledAllLockedFields(), true,"Problem with validation");
	}

}
