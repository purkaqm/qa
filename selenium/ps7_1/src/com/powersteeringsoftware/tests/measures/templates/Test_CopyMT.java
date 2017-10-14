package com.powersteeringsoftware.tests.measures.templates;

import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.TimeStampName;


/**
 * Create and copy measure template.
 *
 * @author selyaev_ag
 * @since 2009 January 20
 */
public class Test_CopyMT extends PSTest {

	public Test_CopyMT(){
		name = "Copy measure template";
	}
	public void run(){
		// create MT
		String mtName = MeasureManager.createMeasureTemplateManualGoal();

		// copy MT
		String mtNameCopy = new TimeStampName(mtName).getTimeStampedName();
		MeasuresLibraryPageAdapter mlAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
		MeasureTemplateEditPageAdapter mteAdapter = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
		mlAdapter.doActionCopy(mtName, mtNameCopy, mteAdapter);

		// test copy of MT
		mlAdapter.doActionEdit(mtNameCopy);
		String mtNameTest = mteAdapter.getName();
		Assert.assertEquals(mtNameCopy, mtNameTest,"Names are not equal!");
	}

}
