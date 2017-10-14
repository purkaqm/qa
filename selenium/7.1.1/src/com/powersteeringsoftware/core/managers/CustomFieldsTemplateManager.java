package com.powersteeringsoftware.core.managers;

import java.util.*;
import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.objects.CustomFieldsTemplate;
import com.powersteeringsoftware.core.objects.CustomFieldsTemplate.PSFieldCheckboxes;
import com.powersteeringsoftware.core.objects.CustomFieldsTemplate.PSFieldYesNoButton;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.thoughtworks.selenium.DefaultSelenium;

public class CustomFieldsTemplateManager {

	//private Logger logger = Logger.getLogger(CustomFieldsTemplateManager.class);

	private CustomFieldsTemplate template;

	public CustomFieldsTemplateManager(){
	}

	enum CustomFieldsTemplateLocators implements ILocatorable{
		FRAME_CHILD("dom=window.frames['jspFrame']"),
		FRAME_PARENT("relative=parent"),

		NAME("dom=document.getElementsByName('mockup.name')[0]"),
		DESCRIPTION("dom=document.getElementsByName('mockup.description')[0]");

		String locator;
		public String getLocator() {
			return locator;
		}
		CustomFieldsTemplateLocators(String _locator){
			locator = _locator;
		}
	}

	public void create(CustomFieldsTemplate _template){
		template = _template;
		MainMenuAdapter.clickAdminCustomFields();

		selectFrameChild();

		pushAddNew();

		typeName();

		typeDescription();

		checkObjects();

		submitCreation();

		addYesNoList();

		doSMTH();

		//Go back to the main frame
		selectFrameParent();
	}

	private void doSMTH() {
		String jsQuery;
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		if (template.getFieldsCheckboxesList().size() > 0){
		    for (Iterator<PSFieldCheckboxes> it = template.getFieldsCheckboxesList().iterator(); it.hasNext(); ) {
		    	PSFieldCheckboxes checkboxes = it.next();

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++){if (/.*data_field_edit.jsp.*/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

				//logger.debug("Adding checkboxes '" + checkboxes.getName() + "' to the template '" + template.getName() + "'");

				driver.type("dom=document.getElementsByName('fieldName')[0]", checkboxes.getName());
				driver.click("dom=document.getElementsByName('fieldRequired')[1]");
				driver.click("dom=document.getElementsByName('fieldOnReport')[1]");
				driver.type("dom=document.getElementsByName('fieldDescription')[0]", checkboxes.getDescription());

				driver.select("dom=document.getElementById('windowedControl')", "value=check");
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

				String[] checkboxesNames = checkboxes.getCheckboxesNames();

				String checkboxesInputString = "";
				for (int i=0; i<checkboxesNames.length; i++){
					checkboxesInputString += checkboxesNames[i];
					if (i < checkboxesNames.length - 1)  checkboxesInputString += "\n";
				}

				driver.type("dom=document.getElementsByName('fieldCheck')[0]", checkboxesInputString);

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++)" +
							"{if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		    }
		}
	}

	private void addYesNoList() {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String jsQuery;
		if (template.getFieldsYesNoButtonsList().size() > 0){
		    for (Iterator<PSFieldYesNoButton> it = template.getFieldsYesNoButtonsList().iterator(); it.hasNext(); ) {
				PSFieldYesNoButton yesNoButton = it.next();

				//logger.debug("Adding Yes/No button '" + yesNoButton.getName() + "' to the template '" + template.getName() + "'");

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++){if (/.*data_field_edit.jsp.*/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

				driver.type("dom=document.getElementsByName('fieldName')[0]", yesNoButton.getName());
				driver.click("dom=document.getElementsByName('fieldRequired')[1]");
				driver.click("dom=document.getElementsByName('fieldOnReport')[1]");
				driver.type("dom=document.getElementsByName('fieldDescription')[0]", yesNoButton.getDescription());

				driver.select("dom=document.getElementById('windowedControl')", "value=radio");
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++)" +
							"{if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		    }
		}
	}

	private DefaultSelenium submitCreation() {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String jsQuery = "var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++)" +
					"{if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		return driver;
	}

	private void checkObjects() {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		if (template.getAssocWithEvents()) driver.check("dom=document.getElementsByName('object.type.ProjectEvent')[0]");
		if (template.getAssocWithMSPProjects()) driver.check("dom=document.getElementsByName('object.type.MSPContainer')[0]");
		if (template.getAssocWithMilestones()) driver.check("dom=document.getElementsByName('object.type.Milestone')[0]");
		if (template.getAssocWithTemplates()) driver.check("dom=document.getElementsByName('object.type.Template')[0]");
		if (template.getAssocWithFolders()) driver.check("dom=document.getElementsByName('object.type.FileFolder')[0]");
		if (template.getAssocWithWorkItems()) driver.check("dom=document.getElementsByName('object.type.Work')[0]");
		if (template.getAssocWithUnexpWorkItems()) driver.check("dom=document.getElementsByName('object.type.UnexpandedWork')[0]");
		if (template.getAssocWithGatedProjects()) driver.check("dom=document.getElementsByName('object.type.Tollgate')[0]");
		if (template.getAssocWithGates()) driver.check("dom=document.getElementsByName('object.type.Checkpoint')[0]");
		if (template.getAssocWithUsers()) driver.check("dom=document.getElementsByName('object.type.User')[0]");
		if (template.getAssocWithGroups()) driver.check("dom=document.getElementsByName('object.type.Group')[0]");
		if (template.getStatusReport()) driver.check("dom=document.getElementsByName('object.type.StatusReportHandler')[0]");
	}

	private void typeDescription() {
		if (null != template.getDescription()){
			SeleniumDriverSingleton.getDriver().type(CustomFieldsTemplateLocators.DESCRIPTION.getLocator(),
					template.getDescription());
		}
	}

	private void typeName() {
		SeleniumDriverSingleton.getDriver().type(CustomFieldsTemplateLocators.NAME.getLocator(),template.getName());
	}

	private void pushAddNew() {
		String jsQuery;

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*data_template_edit.jsp/.test(links[i].href)){links[i];break}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		SeleniumDriverSingleton.getDriver().waitForFrameToLoad(CustomFieldsTemplateLocators.FRAME_CHILD.getLocator(), CoreProperties.getWaitForElementToLoadAsString());
	}

	private void selectFrameChild() {
		SeleniumDriverSingleton.getDriver().selectFrame(CustomFieldsTemplateLocators.FRAME_CHILD.getLocator());
	}


	private void selectFrameParent() {
		SeleniumDriverSingleton.getDriver().selectFrame(CustomFieldsTemplateLocators.FRAME_PARENT.getLocator());
	}
}
