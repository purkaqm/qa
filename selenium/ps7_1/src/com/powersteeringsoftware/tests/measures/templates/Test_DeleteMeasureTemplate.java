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
 * Delete Measure Template.<br>
 *
 *
 */
public class Test_DeleteMeasureTemplate extends PSTest{

	public Test_DeleteMeasureTemplate(){
		name = "Delete Measure Template";
	}

    public void run(){
    	String mtName = MeasureManager.createMeasureTemplateManualGoal();

        MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

        // delete measure template
        MainMenuAdapter.clickBrowseMeasureLibrary();
        MeasuresLibraryPageAdapter mlAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
        mlAdapter.doActionDelete(mtName);

        // assert MT is deleted
        Assert.assertNotSame(mlAdapter.existBySubstring(mtName), true, "Measure template "+mtName+" hasn't been deleted.");

        //assert attached measure is deleted too
        new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());

        Assert.assertEquals(new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()).existBySubstring(mtName), false, "Attached Measure "+mtName+" has not been deleted.");
    }
}
