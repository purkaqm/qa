package com.powersteeringsoftware.core.adapters.issues;

import org.apache.commons.lang.StringUtils;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;

public class IssueEditPageAdapter {

	public static void selectTagValue_Select(String tagUID, String value){
		String shortUID = tagUID.substring(1);
		if (isExistTagByUID(shortUID)
				&& StringUtils.isNotEmpty(shortUID)
				&& StringUtils.isNotEmpty(value)) {
			SeleniumDriverSingleton.getDriver().select(shortUID, value);
		}
	}

	public static boolean isExistTagByUID(String tagUID){
		return SeleniumDriverSingleton.getDriver().isElementPresent(tagUID);
	}

	public static void typeName(String _name){
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.byId('subjectInput')", _name);
	}

	public static void typeDescription(String _desription){
		//SeleniumDriverSingleton.getDriver().runScript("window.dijit.byId('messageInput_editor').setValue('"+_desription+"')");
		SeleniumDriverSingleton.getDriver().runScript("window.tinyMCE.getInstanceById('mce_editor_0').setHTML('" + _desription + "')");
	}

	public static void submitWithUpperButton(){
		//SeleniumDriverSingleton.getDriver().mouseUp("dom=window.dojo.query('[type=submit]>>[value=Submit]')[0];");
		//SeleniumDriverSingleton.getDriver().mouseUp("Submit_3");
		//SeleniumDriverSingleton.getDriver().click("Submit_5");

		//SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('[name=Submit]>>[value=Submit]')[0]");

		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('[name=Submit]>>[value=Submit]')[0]");
		//SeleniumDriverSingleton.getDriver().mouseUp("dom=window.dojo.query('[name=Submit]>>[value=Submit]')[0]");

		//SeleniumDriverSingleton.getDriver().click("Submit_3");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

	}
}
