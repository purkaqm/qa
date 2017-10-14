package selenium.formholders;

import java.util.*;

public class TagHolder {
	String tagName;
	String tagDescription;

	Boolean propHierarchical = null;
	Boolean propEnableAlertsAndEventLogging = null;
	Boolean propRequired = null;
	Boolean propLocked = null;
	Boolean propApplyUsersPermissions = null;
	Boolean propMultiple = null;

	LinkedList<String> typesOfMessages = null;
	LinkedList<String> typesOfDocuments = null;
	LinkedList<String> typesOfPeople = null;
	LinkedList<String> typesOfWorks = null;

	LinkedList<TagValue> tagValues;

	public TagHolder(String tagName, String tagDescription){
		this.tagName = tagName;
		this.tagDescription = tagDescription;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public Boolean getPropHierarchical() {
		return propHierarchical;
	}

	public void setPropHierarchical(Boolean propHierarchical) {
		this.propHierarchical = propHierarchical;
	}

	public Boolean getPropEnableAlertsAndEventLogging() {
		return propEnableAlertsAndEventLogging;
	}

	public void setPropEnableAlertsAndEventLogging(
			Boolean propEnableAlertsAndEventLogging) {
		this.propEnableAlertsAndEventLogging = propEnableAlertsAndEventLogging;
	}

	public Boolean getPropRequired() {
		return propRequired;
	}

	public void setPropRequired(Boolean propRequired) {
		this.propRequired = propRequired;
	}

	public Boolean getPropLocked() {
		return propLocked;
	}

	public void setPropLocked(Boolean propLocked) {
		this.propLocked = propLocked;
	}

	public Boolean getPropApplyUsersPermissions() {
		return propApplyUsersPermissions;
	}

	public void setPropApplyUsersPermissions(Boolean propApplyUsersPermissions) {
		this.propApplyUsersPermissions = propApplyUsersPermissions;
	}

	public Boolean getPropMultiple() {
		return propMultiple;
	}

	public void setPropMultiple(Boolean propMultiple) {
		this.propMultiple = propMultiple;
	}

	public LinkedList<String> getTypesOfMessages() {
		return typesOfMessages;
	}

	public void addTypeOfMessages(String typeOfMessages) {
		if (null == this.typesOfMessages) this.typesOfMessages = new LinkedList<String>();
		this.typesOfMessages.add(typeOfMessages);
	}

	public LinkedList<String> getTypesOfDocuments() {
		return typesOfDocuments;
	}

	public void addTypeOfDocuments(String typeOfDocuments) {
		if (null == this.typesOfDocuments) this.typesOfDocuments = new LinkedList<String>();
		this.typesOfDocuments.add(typeOfDocuments);
	}

	public LinkedList<String> getTypesOfPeople() {
		return typesOfPeople;
	}

	public void addTypeOfPeople(String typeOfPeople) {
		if (null == this.typesOfPeople) this.typesOfPeople = new LinkedList<String>();
		this.typesOfPeople.add(typeOfPeople);
	}

	public LinkedList<String> getTypesOfWorks() {
		return typesOfWorks;
	}

	public void addTypeOfWorks(String typeOfWorks) {
		if (null == this.typesOfWorks) this.typesOfWorks = new LinkedList<String>();
		this.typesOfWorks.add(typeOfWorks);
	}

	public LinkedList<TagValue> getTagValues() {
		return tagValues;
	}

	public void addTagValue(String tagName) {
		if (null == this.tagValues) this.tagValues = new LinkedList<TagValue>();
		this.tagValues.add(new TagValue(tagName, null));
	}

	public void addTagValue(String tagName, String tagParent) {
		if (null == this.tagValues) this.tagValues = new LinkedList<TagValue>();
		this.tagValues.add(new TagValue(tagName, tagParent));
	}


}
