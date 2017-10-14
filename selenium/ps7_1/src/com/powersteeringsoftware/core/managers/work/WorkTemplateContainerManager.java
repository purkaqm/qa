package com.powersteeringsoftware.core.managers.work;

import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;

public class WorkTemplateContainerManager {

	public static final String SELECT_CHILDREN_STEP = "SelectChildrenInput";
	public static final String REQUIRE_GATE_END_DATES = "RequireDateEndDatesInput";

	private WorkTemplateContainer workTemplate;

	WorkTemplateContainerManager() {
	}

	public void createWorkTemplateContainer(WorkTemplateContainer _workTemplate){
		workTemplate = _workTemplate;

		MainMenuAdapter.clickAdminTemplatesWork();

		BasicCommons.selectAdminPageFrame();

		pushCreateNewWTContainer();

		typeWTContainerName();

		setSelectChildrenInput();

		setRequireGateEndDates();

		submitWTContainerCreating();

		BasicCommons.selectPageTopFrame();
	}

	private void setRequireGateEndDates(){
		if(workTemplate.getIsRequireGateEndDates()){
			enableRequireDateEndDatesInput();
		}
		disableRequireDateEndDatesInput();
	}

	private void setSelectChildrenInput(){
		if(workTemplate.getIsShowSelectChildrenStep() && workTemplate.getIsRequireGateEndDates()){
			enableSelectChildrenInput();
		}
		disableSelectChildrenInput();
	}


	private void pushCreateNewWTContainer() {
		String jsQuery="";
		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*new_wizard.jsp\\?objectType=Template/.test(links[i].href)){links[i];break}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void typeWTContainerName() {
		SeleniumDriverSingleton.getDriver().type("dom=document.getElementsByName('objectName')[0]", workTemplate.getName());
	}

	private void submitWTContainerCreating() {
		String jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString()); //finishcreate
	}

	private void enableSelectChildrenInput(){
		SeleniumDriverSingleton.getDriver().click("SelectChildrenInput");
	}

	private void disableSelectChildrenInput(){
		SeleniumDriverSingleton.getDriver().click("//input[@name='RequireDateEndDatesInput' and @value='false']");
	}

	private void enableRequireDateEndDatesInput(){
		SeleniumDriverSingleton.getDriver().click("RequireDateEndDatesInput");
	}

	private void disableRequireDateEndDatesInput(){
		SeleniumDriverSingleton.getDriver().click("//input[@name='RequireDateEndDatesInput' and @value='false']");
	}

}
