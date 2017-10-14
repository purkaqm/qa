package com.powersteeringsoftware.core.adapters.tags;

import java.util.LinkedList;
import com.powersteeringsoftware.core.objects.Tag.TagValue;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;

public class TagAddUpdatePageAdapter {

//	enum TagAddUpdateLocations implements Locatorable{
//		ID("");
//
//		String locator;
//
//		TagAddUpdateLocations(String _locator){
//			locator = _locator;
//		}
//
//		public String getLocator(){
//			return locator;
//		}
//	}


	public static void addNeedfullFieldCount(LinkedList<TagValue> tagValues) {
		//add enough fields
		if(!(tagValues==null)){
			int nameFieldsCount;
			do {
				nameFieldsCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('td.nameColumnValue input.txt').length"));
				if (nameFieldsCount < tagValues.size()){
					TagEditWindowAdapter.submitEditing(); //TODO!!!! must be checked !!!!!
				}
			} while (nameFieldsCount < tagValues.size());
		}
	}

	public static void fillNeedfullFields(LinkedList<TagValue> tagValues) {
		for (int i=0; i < tagValues.size(); i++){
			SeleniumDriverSingleton.getDriver().type("dom=window.dojo.query('td.nameColumnValue input.txt')[" + i + "]" ,
					tagValues.get(i).getTagValueName());
		}
	}

	public static void submitUpdating() {
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('Submit_1')");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	public static void setParents(LinkedList<TagValue> tagValues, boolean isHierarchical) {
		if (isHierarchical) {
			TagDetailsPageAdapter.pushButtonAddUpdate();//TODO: test it

			for (int i=0; i < tagValues.size(); i++){
				String tagValue = SeleniumDriverSingleton.getDriver().getValue("dom=window.dojo.query('td.nameColumnValue input.txt')[" + i + "]").trim();
				for (int j=0; j < tagValues.size(); j++){
					if (!(null == tagValues.get(j).getTagValueParent()))
						if (tagValues.get(j).getTagValueName().equals(tagValue))
							SeleniumDriverSingleton.getDriver().select("dom=window.dojo.query('td.parentColumnValue select')[" + i + "]" ,
									"label=" + tagValues.get(j).getTagValueParent());
				}
			}

			SeleniumDriverSingleton.getDriver().click("dom=document.getElementById('Submit_1')");
			SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		}
}
}
