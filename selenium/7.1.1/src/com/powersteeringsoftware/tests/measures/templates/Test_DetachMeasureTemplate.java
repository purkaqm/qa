package com.powersteeringsoftware.tests.measures.templates;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Detach measure template.
 *
 * @author selyaev_ag
 *
 */
public class Test_DetachMeasureTemplate extends PSTest {
	public Test_DetachMeasureTemplate(){
		name = "Detach measure template";
	}

	public void run(){
		String mtName = MeasureManager.createMeasureTemplateFormulaGoal();

		MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

		// delete measure template
		MainMenuAdapter.clickBrowseMeasureLibrary();
		MeasuresLibraryPageAdapter mlAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
		mlAdapter.doActionDetach(mtName);

		// assert MT is deleted
        Assert.assertNotSame(mlAdapter.existBySubstring(mtName), true, "Measure template "+mtName+" hasn't been deleted.");

        //assert attached measure is deleted too
        new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());
        Assert.assertEquals(new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()).existBySubstring(mtName), true, "Attached Measure "+mtName+" has been deleted!");
	}

}
