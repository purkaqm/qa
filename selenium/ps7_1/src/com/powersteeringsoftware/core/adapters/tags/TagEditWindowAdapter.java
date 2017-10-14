package com.powersteeringsoftware.core.adapters.tags;

import java.util.LinkedList;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeDisappearedWaiter;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;

public class TagEditWindowAdapter {

	enum TagEditWindowLocators implements ILocatorable{
		
		DIV_ID("dom=window.dojo.byId('AddTagSet')"),
		BUTTON_SUBMIT("dom=window.dojo.byId('Submit_0')"),
		FIELD_NAME("dom=window.dojo.byId('tagSetName')"),
		FIELD_DESCRIPTIÎN("dom=window.dojo.byId('tagSetDescription')"),
		FIELD_IS_HIERARCHICAL("dom=window.dojo.byId('tagsSetHierarchical')"),
		FIELD_IS_APPLY_USERS_PERMISSIONS("dom=window.dojo.byId('tagsSetPermissions')"),
		FIELD_IS_ENABLE_ALERTS_AND_EVENT_LOGGING("dom=window.dojo.byId('tagsSetAlertable')"),
		FIELD_IS_LOCKED("dom=window.dojo.byId('tagsSetLock')"),
		FIELD_IS_MULTIPLE("dom=window.dojo.byId('tagsSetMultiple')"),
		FIELD_IS_MANDANATORY("dom=window.dojo.byId('tagsSetMandatory')"),
		FIELD_MESSAGES("dom=window.dojo.byId('messageTypesDisp')"),
		FIELD_DOCUMENTS("dom=window.dojo.byId('documentTypesDisp')"),
		FIELD_PEOPLES("dom=window.dojo.byId('peopleTypesDisp')"),
		FIELD_WORKS("dom=window.dojo.byId('workTypesDisp')");


		String locator;

		TagEditWindowLocators(String _locator){
			locator = _locator;
		}
		public String getLocator() {
			return locator;
		}
	}

	public static void submitEditing() {
		SeleniumDriverSingleton.getDriver().click(TagEditWindowLocators.BUTTON_SUBMIT.getLocator());
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public static void typeTagName(String tagName){
		SeleniumDriverSingleton.getDriver().type(TagEditWindowLocators.FIELD_NAME.locator, tagName);
	}

	public static void typeTagDescription(String tagDescription){
		SeleniumDriverSingleton.getDriver().type(TagEditWindowLocators.FIELD_DESCRIPTIÎN.getLocator(), tagDescription);
	}

	public static void setHierarchical(boolean isHierarhical) {
		if (isHierarhical){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_HIERARCHICAL.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_HIERARCHICAL.getLocator());
		}
	}

	public static void setApplyUsersPermissions(boolean isApplyUsersPermissions) {
		if (isApplyUsersPermissions){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_APPLY_USERS_PERMISSIONS.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_APPLY_USERS_PERMISSIONS.getLocator());
		}
	}

	public static void setAlertsAndEventLogging(boolean isEnableAlertsAndEventLogging) {
		if (isEnableAlertsAndEventLogging){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_ENABLE_ALERTS_AND_EVENT_LOGGING.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_ENABLE_ALERTS_AND_EVENT_LOGGING.getLocator());
		}
	}


	public static void setLocked(boolean isLocked) {
		if (isLocked){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_LOCKED.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_LOCKED.getLocator());
		}
	}


	public static void setMultiple(boolean isMultiple) {
		if (isMultiple){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_MULTIPLE.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_MULTIPLE.getLocator());
		}
	}


	public static void setRequired(boolean isRequired) {
		if (isRequired){
			SeleniumDriverSingleton.getDriver().check(TagEditWindowLocators.FIELD_IS_MANDANATORY.getLocator());
		} else {
			SeleniumDriverSingleton.getDriver().uncheck(TagEditWindowLocators.FIELD_IS_MANDANATORY.getLocator());
		}
	}


	public static void setMessageTypes(LinkedList<String> messageTypes) {
		if (!(null == messageTypes)) {
			SeleniumDriverSingleton.getDriver().focus(TagEditWindowLocators.FIELD_MESSAGES.getLocator());
			ElementBecomePresentWaiter.waitElementBecomePresent("dom=window.dojo.query('div.dijitPopup')[0]");

			int checkboxesCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = SeleniumDriverSingleton.getDriver().getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < messageTypes.size(); j++){
					if (messageTypes.get(j).equals(checkboxString))
						SeleniumDriverSingleton.getDriver().check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}

			SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('messageTypesDone')");
			ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.dijitPopup')[0]");
		}
	}


	public static void setDocumentTypes(LinkedList<String> documentTypes) {
		if (!(null == documentTypes)) {
			SeleniumDriverSingleton.getDriver().focus(TagEditWindowLocators.FIELD_DOCUMENTS.getLocator());
			ElementBecomePresentWaiter.waitElementBecomePresent("dom=window.dojo.query('div.dijitPopup')[0]");

			int checkboxesCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = SeleniumDriverSingleton.getDriver().getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < documentTypes.size(); j++){
					if (documentTypes.get(j).equals(checkboxString))
						SeleniumDriverSingleton.getDriver().check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}

			SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('documentTypesDone')");
			ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.dijitPopup')[0]");
		}
	}


	public static void setPeopleTypes(LinkedList<String> peopleTypes) {
		if (!(null == peopleTypes)) {
			SeleniumDriverSingleton.getDriver().focus(TagEditWindowLocators.FIELD_PEOPLES.getLocator());
			ElementBecomePresentWaiter.waitElementBecomePresent("dom=window.dojo.query('div.dijitPopup')[0]");

			int checkboxesCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = SeleniumDriverSingleton.getDriver().getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < peopleTypes.size(); j++){
					if (peopleTypes.get(j).equals(checkboxString))
						SeleniumDriverSingleton.getDriver().check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}
			SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('peopleTypesDone')");
			ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.dijitPopup')[0]");

		}
	}


	public static void setWorkTypes(LinkedList<String> workTypes) {
		if (!(null == workTypes)) {
			SeleniumDriverSingleton.getDriver().focus(TagEditWindowLocators.FIELD_WORKS.getLocator());
			ElementBecomePresentWaiter.waitElementBecomePresent("dom=window.dojo.query('div.dijitPopup')[0]");

			int checkboxesCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitPopup ul input').length"));
			for (int i=0; i < checkboxesCount; i++){
				String checkboxString = SeleniumDriverSingleton.getDriver().getText("dom=window.dojo.query('div.dijitPopup ul label')[" + i + "]").trim();
				for (int j=0; j < workTypes.size(); j++){
					if (workTypes.get(j).equals(checkboxString))
						SeleniumDriverSingleton.getDriver().check("dom=window.dojo.query('div.dijitPopup ul input')[" + i + "]");
				}
			}

			SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('workTypesDone')");
			ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.dijitPopup')[0]");
		}
	}

	public static String getTagUID(){
		String url = SeleniumDriverSingleton.getDriver().getLocation();
		url = url.substring(url.lastIndexOf("sp=")+3);
		return url;
	}
}

