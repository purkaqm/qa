package selenium.managers;

import java.util.*;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.*;
import selenium.objects.CustomFieldsTemplate.*;

public class CustomFieldsTemplateManager {
	private MySeleniumDriver driver;
	private Logger logger;

	public CustomFieldsTemplateManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void createFromTemplate(CustomFieldsTemplate template){
		driver.click("dom=window.dojo.query('div#AdminMenu a[href*=CustomFields]')[0]");
		driver.waitForPageToLoad("30000");

		//Work in the frame on jsp page
		driver.selectFrame("dom=window.frames['jspFrame']");

		String jsQuery;

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*data_template_edit.jsp/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		logger.info("Creating new Custom Fields Template '" + template.getName() + "'");

		driver.type("dom=document.getElementsByName('mockup.name')[0]", template.getName());
		if (null != template.getDescription()) driver.type("dom=document.getElementsByName('mockup.description')[0]", template.getDescription());
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

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++)" +
					"{if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		if (template.getFieldsYesNoButtonsList().size() > 0){
		    for (Iterator<PSFieldYesNoButton> it = template.getFieldsYesNoButtonsList().iterator(); it.hasNext(); ) {
				PSFieldYesNoButton yesNoButton = it.next();

				logger.info("Adding Yes/No button '" + yesNoButton.getName() + "' to the template '" + template.getName() + "'");

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++){if (/.*data_field_edit.jsp.*/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad("30000");

				driver.type("dom=document.getElementsByName('fieldName')[0]", yesNoButton.getName());
				driver.click("dom=document.getElementsByName('fieldRequired')[1]");
				driver.click("dom=document.getElementsByName('fieldOnReport')[1]");
				driver.type("dom=document.getElementsByName('fieldDescription')[0]", yesNoButton.getDescription());

				driver.select("dom=document.getElementById('windowedControl')", "value=radio");
				driver.waitForPageToLoad("30000");

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++)" +
							"{if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad("30000");
		    }
		}

		if (template.getFieldsCheckboxesList().size() > 0){
		    for (Iterator<PSFieldCheckboxes> it = template.getFieldsCheckboxesList().iterator(); it.hasNext(); ) {
		    	PSFieldCheckboxes checkboxes = it.next();

				jsQuery = 	"var links=document.getElementsByTagName('a');" +
							"for (var i=0; i<links.length; i++){if (/.*data_field_edit.jsp.*/.test(links[i].href)){links[i];break}}";
				driver.click("dom=" + jsQuery);
				driver.waitForPageToLoad("30000");

				logger.info("Adding checkboxes '" + checkboxes.getName() + "' to the template '" + template.getName() + "'");

				driver.type("dom=document.getElementsByName('fieldName')[0]", checkboxes.getName());
				driver.click("dom=document.getElementsByName('fieldRequired')[1]");
				driver.click("dom=document.getElementsByName('fieldOnReport')[1]");
				driver.type("dom=document.getElementsByName('fieldDescription')[0]", checkboxes.getDescription());

				driver.select("dom=document.getElementById('windowedControl')", "value=check");
				driver.waitForPageToLoad("30000");

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
				driver.waitForPageToLoad("30000");
		    }
		}

		//Go back to the main frame
		driver.selectFrame("relative=parent");
	}

}
