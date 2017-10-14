package com.powersteeringsoftware.core.objects;

import java.util.*;

public class Tag {
	String tagName;
	String tagDescription;

	boolean propHierarchical;
	boolean propEnableAlertsAndEventLogging;
	boolean propRequired;
	boolean propLocked;
	boolean propApplyUsersPermissions;
	boolean propMultiple;

	LinkedList<String> typesOfMessages = null;
	LinkedList<String> typesOfDocuments = null;
	LinkedList<String> typesOfPeople = null;
	LinkedList<String> typesOfWorks = null;

	LinkedList<TagValue> tagValues;

	private String uid;

	public Tag(String tagName, String tagDescription){
		this.tagName = tagName;
		this.tagDescription = tagDescription;
	}

	public String getTagName() {
		return tagName;
	}

	public String getTagDescription() {
		return tagDescription;
	}

	public boolean getPropHierarchical() {
		return propHierarchical;
	}

	public void setPropHierarchical(boolean propHierarchical) {
		this.propHierarchical = propHierarchical;
	}

	public boolean getPropEnableAlertsAndEventLogging() {
		return propEnableAlertsAndEventLogging;
	}

	public void setPropEnableAlertsAndEventLogging(
			boolean propEnableAlertsAndEventLogging) {
		this.propEnableAlertsAndEventLogging = propEnableAlertsAndEventLogging;
	}

	public boolean getPropRequired() {
		return propRequired;
	}

	public void setPropRequired(boolean propRequired) {
		this.propRequired = propRequired;
	}

	public boolean getPropLocked() {
		return propLocked;
	}

	public void setPropLocked(boolean propLocked) {
		this.propLocked = propLocked;
	}

	public boolean getPropApplyUsersPermissions() {
		return propApplyUsersPermissions;
	}

	public void setPropApplyUsersPermissions(boolean propApplyUsersPermissions) {
		this.propApplyUsersPermissions = propApplyUsersPermissions;
	}

	public boolean getPropMultiple() {
		return propMultiple;
	}

	public void setPropMultiple(boolean propMultiple) {
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


	public class TagValue {
		String tagValueName;
		String tagValueParent;

		protected TagValue(String tagValueName, String tagValueParent){
			this.tagValueName = tagValueName;
			this.tagValueParent = tagValueParent;
		}

		public String getTagValueName() {
			return tagValueName;
		}

		public String getTagValueParent() {
			return tagValueParent;
		}
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
