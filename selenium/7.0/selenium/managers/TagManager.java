package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.Tag;

public class TagManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public TagManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void addTag(Tag tag) throws InterruptedException{
		logger.info("Creating new tag '" + tag.getTagName() + "'");

		driver.click("dom=window.dojo.query('div#AdminMenu a[href*=TagSets]')[0]");
		driver.waitForPageToLoad("30000");

		driver.click("dom=window.dojo.byId('AddTagSetShow')");
		driver.waitForElementToBecomeVisible("dom=window.dojo.byId('AddTagSet')", "30000");
		driver.type("dom=window.dojo.byId('tagSetName')", tag.getTagName());
		driver.type("dom=window.dojo.byId('tagSetDescription')", tag.getTagDescription());

		if (!(null == tag.getPropHierarchical()))
			if (tag.getPropHierarchical()){
				driver.check("dom=window.dojo.byId('tagsSetHierarchical')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetHierarchical')");
			}

		if (!(null == tag.getPropApplyUsersPermissions()))
			if (tag.getPropApplyUsersPermissions()){
				driver.check("dom=window.dojo.byId('tagsSetPermissions')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetPermissions')");
			}

		if (!(null == tag.getPropEnableAlertsAndEventLogging()))
			if (tag.getPropEnableAlertsAndEventLogging()){
				driver.check("dom=window.dojo.byId('tagsSetAlertable')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetAlertable')");
			}

		if (!(null == tag.getPropLocked()))
			if (tag.getPropLocked()){
				driver.check("dom=window.dojo.byId('tagsSetLock')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetLock')");
			}

		if (!(null == tag.getPropMultiple()))
			if (tag.getPropMultiple()){
				driver.check("dom=window.dojo.byId('tagsSetMultiple')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetMultiple')");
			}

		if (!(null == tag.getPropRequired()))
			if (tag.getPropRequired()){
				driver.check("dom=window.dojo.byId('tagsSetMandatory')");
			} else {
				driver.uncheck("dom=window.dojo.byId('tagsSetMandatory')");
			}

		if (!(null == tag.getTypesOfMessages())) {
			driver.focus("dom=window.dojo.byId('messageTypesDisp')");
			driver.waitForElementToAppear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
			int checkboxesCount = Integer.valueOf(driver.getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = driver.getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < tag.getTypesOfMessages().size(); j++){
					if (tag.getTypesOfMessages().get(j).equals(checkboxString))
						driver.check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}
			driver.click("dom=window.dojo.byId('messageTypesDone')");
			driver.waitForElementToDisappear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
		}

		if (!(null == tag.getTypesOfDocuments())) {
			driver.focus("dom=window.dojo.byId('documentTypesDisp')");
			driver.waitForElementToAppear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
			int checkboxesCount = Integer.valueOf(driver.getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = driver.getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < tag.getTypesOfDocuments().size(); j++){
					if (tag.getTypesOfDocuments().get(j).equals(checkboxString))
						driver.check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}
			driver.click("dom=window.dojo.byId('documentTypesDone')");
			driver.waitForElementToDisappear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
		}

		if (!(null == tag.getTypesOfPeople())) {
			driver.focus("dom=window.dojo.byId('peopleTypesDisp')");
			driver.waitForElementToAppear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
			int checkboxesCount = Integer.valueOf(driver.getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = driver.getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < tag.getTypesOfPeople().size(); j++){
					if (tag.getTypesOfPeople().get(j).equals(checkboxString))
						driver.check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}
			driver.click("dom=window.dojo.byId('peopleTypesDone')");
			driver.waitForElementToDisappear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
		}

		if (!(null == tag.getTypesOfWorks())) {
			driver.focus("dom=window.dojo.byId('workTypesDisp')");
			driver.waitForElementToAppear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
			int checkboxesCount = Integer.valueOf(driver.getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = driver.getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < tag.getTypesOfWorks().size(); j++){
					if (tag.getTypesOfWorks().get(j).equals(checkboxString))
						driver.check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}
			driver.click("dom=window.dojo.byId('workTypesDone')");
			driver.waitForElementToDisappear("dom=window.dojo.query('div.dijitPopup')[0]", "30000");
		}

		driver.click("dom=window.dojo.byId('Submit_0')");
		driver.waitForPageToLoad("30000");

		driver.click("dom=window.dojo.query('form#tagValuesForm a')[0]");
		driver.waitForPageToLoad("30000");

		//add enough fields

		int nameFieldsCount;
		do {
			nameFieldsCount = Integer.valueOf(driver.getEval("window.dojo.query('td.nameColumnValue input.txt').length"));
			if (nameFieldsCount < tag.getTagValues().size()){
				driver.click("dom=window.dojo.byId('Submit_0')");
				driver.waitForPageToLoad("30000");
			}
		} while (nameFieldsCount < tag.getTagValues().size());

		for (int i=0; i < tag.getTagValues().size(); i++){
			driver.type("dom=window.dojo.query('td.nameColumnValue input.txt')[" + i + "]" ,
					tag.getTagValues().get(i).getTagValueName());
		}

		driver.click("dom=window.dojo.byId('Submit_1')");
		driver.waitForPageToLoad("30000");

		//set parents if hierarchical

		if (!(null == tag.getPropHierarchical()))
			if (tag.getPropHierarchical()) {
				driver.click("dom=window.dojo.query('form#tagValuesForm a')[0]");
				driver.waitForPageToLoad("30000");

				for (int i=0; i < tag.getTagValues().size(); i++){
					String tagValue = driver.getValue("dom=window.dojo.query('td.nameColumnValue input.txt')[" + i + "]").trim();
					for (int j=0; j < tag.getTagValues().size(); j++){
						if (!(null == tag.getTagValues().get(j).getTagValueParent()))
							if (tag.getTagValues().get(j).getTagValueName().equals(tagValue))
								driver.select("dom=window.dojo.query('td.parentColumnValue select')[" + i + "]" ,
										"label=" + tag.getTagValues().get(j).getTagValueParent());
					}
				}

				driver.click("dom=document.getElementById('Submit_1')");
				driver.waitForPageToLoad("30000");

			}

	}

}
