package com.powersteeringsoftware.tests.measures.templates.utils;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.thoughtworks.selenium.DefaultSelenium;

public class CreateAndTestMT {

	public CreateAndTestMT(){
	}

	public void execute(int dataCollection, int indicatorType){
		String mtName = "";
		if(dataCollection==0 & indicatorType==0){
			mtName =MeasureManager.createMeasureTemplateManualGoal();
		}

		if(dataCollection==0 & indicatorType==1){
			mtName =MeasureManager.createMeasureTemplateManualVariance();
		}

		if(dataCollection==1 & indicatorType==0){
			mtName =MeasureManager.createMeasureTemplateFormulaGoal();
		}

		if(dataCollection==1 & indicatorType==1){
			mtName =MeasureManager.createMeasureTemplateFormulaVariance();
		}



        DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();

        //check measure template is displayed on the page "Measure Template Library"
        Assert.assertEquals(seleniumDriver.isTextPresent(mtName),
                            true,
                            "Measure Template with name "+mtName+" doesn't exist on the page 'Measure Template Library'.");

		//open Measure Template for editing and check all fields
        MeasuresLibraryPageAdapter mlAdapter = new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver());
        mlAdapter.doActionEdit(mtName.toString());

        MeasureTemplateEditPageAdapter mtEdit = new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver());
        String testedName = mtEdit.getName();

        Assert.assertEquals(StringUtils.equals(testedName,mtName),
                            true,
                            "Name "+testedName+" is different to typed on creating "+mtName);

		int dataCollectionFieldId = mtEdit.getDataCollectionType().getIndex();
		Assert.assertEquals(dataCollectionFieldId,
							dataCollection,
							"Data Collection "+dataCollectionFieldId+ " is different to selected on creating "+dataCollection);

		int indicatorTypeFieldId = mtEdit.getIndicatorTypeType().getIndex();
		Assert.assertEquals(indicatorTypeFieldId,
							indicatorType,
							"Indicator Type "+ indicatorTypeFieldId+ " is different to selected on creating "+indicatorType);

	}
}
