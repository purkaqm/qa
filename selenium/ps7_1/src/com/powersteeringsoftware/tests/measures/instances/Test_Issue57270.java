package com.powersteeringsoftware.tests.measures.instances;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;

/**
 * Test issue 57270
 *
 *
 */
public class Test_Issue57270 extends PSTest {

	public Test_Issue57270(){
		name = "Issue 57270";
	}

	public void run() {
		//step-1: create measure template
		String mtName = MeasureManager.createMeasureTemplateFormulaGoal	();
		String copiedMName = new TimeStampName(mtName).getTimeStampedName();

		//step-2: lock all fields in the measure template
		MeasuresLibraryPageAdapter mlAdpater = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
		mlAdpater.doActionEdit(mtName);
		MeasureTemplateEditPageAdapter mtAdapter = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
		mtAdapter.lockAllFields();

		//step-3: attach measure template
		MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

		//step-4: copy attached measure template
		ManageMeasuresPageAdapter mmAdapter = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		MeasureInstanceEditPageAdapter mEditAdapter = new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver());
		mmAdapter.doActionCopy(mtName, copiedMName,mEditAdapter);

		//step-5: assert all fields are enabled for copied measure
		mmAdapter.doActionEdit(copiedMName);
		mEditAdapter.isEnabledAllLockedFields();

		//step-6: change measure template
		MainMenuAdapter.clickBrowseMeasureLibrary();
		mmAdapter.doActionEdit(mtName);

		//step-7: assert all fields in the copied measure hasn't changed
		//TODO: continue creating bugs
		Assert.assertEquals(true, false,"Special assertion");
	}
}
