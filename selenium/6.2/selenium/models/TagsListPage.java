package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class TagsListPage {

	private MySeleniumDriver mydriver;

	public TagsListPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisTagsPage() {
		return mydriver.getTitle().contains("Tags List");
	}

	public void addNewTag(TagHolder tagHolder) throws Exception {
		mydriver.click("dom=document.getElementById('AddTagSetShow')");
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('AddTagSet')", "30000");
		mydriver.type("dom=document.getElementById('tagSetName')", tagHolder.getTagName());
		mydriver.type("dom=document.getElementById('tagSetDescription')", tagHolder.getTagDescription());

		if (!(null == tagHolder.getPropHierarchical()))
			if (tagHolder.getPropHierarchical())
				mydriver.click("dom=document.getElementById('tagsSetHierarchical')");

		if (!(null == tagHolder.getPropApplyUsersPermissions()))
			if (tagHolder.getPropApplyUsersPermissions())
				mydriver.click("dom=document.getElementById('tagsSetPermissions')");

		if (!(null == tagHolder.getPropEnableAlertsAndEventLogging()))
			if (tagHolder.getPropEnableAlertsAndEventLogging())
				mydriver.click("dom=document.getElementById('tagsSetAlertable')");

		if (!(null == tagHolder.getPropLocked()))
			if (tagHolder.getPropLocked())
				mydriver.click("dom=document.getElementById('tagsSetLock')");

		if (!(null == tagHolder.getPropMultiple()))
			if (tagHolder.getPropMultiple())
				mydriver.click("dom=document.getElementById('tagsSetMultiple')");

		if (!(null == tagHolder.getPropRequired()))
			if (tagHolder.getPropRequired())
				mydriver.click("dom=document.getElementById('tagsSetMandatory')");

		if (!(null == tagHolder.getTypesOfMessages())) {
			mydriver.focus("dom=document.getElementById('messageTypesDisp')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('messageTypesUID_multidiv').parentNode", "30000");
			int checkboxesCount =
				Integer.valueOf(mydriver.getEval("window.document.getElementById('messageTypesAllNone').getElementsByTagName('input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = mydriver.getText("dom=document.getElementById('messageTypesAllNone').getElementsByTagName('label')[" +
						String.valueOf(i) + "]").trim();
				for (int j=0; j < tagHolder.getTypesOfMessages().size(); j++){
					if (tagHolder.getTypesOfMessages().get(j).equals(checkboxString))
						mydriver.click("dom=document.getElementById('messageTypesAllNone').getElementsByTagName('input')[" +
								String.valueOf(i) + "]");
				}
			}
			mydriver.click("dom=document.getElementById('messageTypesDone')");
			mydriver.waitForElementToBecomeInvisible("dom=document.getElementById('messageTypesUID_multidiv').parentNode", "30000");
		}

		if (!(null == tagHolder.getTypesOfDocuments())) {
			mydriver.focus("dom=document.getElementById('documentTypesDisp')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('documentTypesUID_multidiv').parentNode", "30000");
			int checkboxesCount =
				Integer.valueOf(mydriver.getEval("window.document.getElementById('documentTypesAllNone').getElementsByTagName('input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = mydriver.getText("dom=document.getElementById('documentTypesAllNone').getElementsByTagName('label')[" +
						String.valueOf(i) + "]").trim();
				for (int j=0; j < tagHolder.getTypesOfDocuments().size(); j++){
					if (tagHolder.getTypesOfDocuments().get(j).equals(checkboxString))
						mydriver.click("dom=document.getElementById('documentTypesAllNone').getElementsByTagName('input')[" +
								String.valueOf(i) + "]");
				}
			}
			mydriver.click("dom=document.getElementById('documentTypesDone')");
			mydriver.waitForElementToBecomeInvisible("dom=document.getElementById('documentTypesUID_multidiv').parentNode", "30000");
		}

		if (!(null == tagHolder.getTypesOfPeople())) {
			mydriver.focus("dom=document.getElementById('peopleTypesDisp')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('peopleTypesUID_multidiv').parentNode", "30000");
			int checkboxesCount =
				Integer.valueOf(mydriver.getEval("window.document.getElementById('peopleTypesAllNone').getElementsByTagName('input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = mydriver.getText("dom=document.getElementById('peopleTypesAllNone').getElementsByTagName('label')["
						+ String.valueOf(i) + "]").trim();
				for (int j=0; j < tagHolder.getTypesOfPeople().size(); j++){
					if (tagHolder.getTypesOfPeople().get(j).equals(checkboxString))
						mydriver.click("dom=document.getElementById('peopleTypesAllNone').getElementsByTagName('input')[" +
								String.valueOf(i) + "]");
				}
			}
			mydriver.click("dom=document.getElementById('peopleTypesDone')");
			mydriver.waitForElementToBecomeInvisible("dom=document.getElementById('peopleTypesUID_multidiv').parentNode", "30000");
		}

		if (!(null == tagHolder.getTypesOfWorks())) {
			mydriver.focus("dom=document.getElementById('workTypesDisp')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('workTypesUID_multidiv').parentNode", "30000");
			int checkboxesCount =
				Integer.valueOf(mydriver.getEval("window.document.getElementById('workTypesAllNone').getElementsByTagName('input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = mydriver.getText("dom=document.getElementById('workTypesAllNone').getElementsByTagName('label')[" +
						String.valueOf(i) + "]").trim();
				for (int j=0; j < tagHolder.getTypesOfWorks().size(); j++){
					if (tagHolder.getTypesOfWorks().get(j).equals(checkboxString))
						mydriver.click("dom=document.getElementById('workTypesAllNone').getElementsByTagName('input')[" +
								String.valueOf(i) + "]");
				}
			}
			mydriver.click("dom=document.getElementById('workTypesDone')");
			mydriver.waitForElementToBecomeInvisible("dom=document.getElementById('workTypesUID_multidiv').parentNode", "30000");
		}

		mydriver.click("dom=document.getElementById('Submit_0')");
		mydriver.waitForPageToLoad("30000");
	}
}
