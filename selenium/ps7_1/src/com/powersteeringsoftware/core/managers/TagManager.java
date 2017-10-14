package com.powersteeringsoftware.core.managers;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagAddUpdatePageAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagDetailsPageAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagEditWindowAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagListPageAdapter;
import com.powersteeringsoftware.core.objects.Tag;

public class TagManager {

	private Tag tag;

	public TagManager(){
	}

	public void addTag(Tag _tag) {
		tag = _tag;
		MainMenuAdapter.clickAdminConfigurationsTags();
		TagListPageAdapter.pushButtonAddNewTag();
		processInTagCreateWindow();
		tag.setUid(TagEditWindowAdapter.getTagUID());
		TagDetailsPageAdapter.pushButtonAddUpdate();
		processInTagDetailsPage();
	}

	private void processInTagDetailsPage() {
		TagAddUpdatePageAdapter.addNeedfullFieldCount(tag.getTagValues());
		TagAddUpdatePageAdapter.fillNeedfullFields(tag.getTagValues());
		TagAddUpdatePageAdapter.submitUpdating();
		TagAddUpdatePageAdapter.setParents(tag.getTagValues(), tag.getPropHierarchical());
	}


	private void processInTagCreateWindow() {
		TagEditWindowAdapter.typeTagName(tag.getTagName());
		TagEditWindowAdapter.typeTagDescription(tag.getTagDescription());
		TagEditWindowAdapter.setHierarchical(tag.getPropHierarchical());
		TagEditWindowAdapter.setApplyUsersPermissions(tag.getPropApplyUsersPermissions());
		TagEditWindowAdapter.setAlertsAndEventLogging(tag.getPropEnableAlertsAndEventLogging());
		TagEditWindowAdapter.setLocked(tag.getPropLocked());
		TagEditWindowAdapter.setMultiple(tag.getPropMultiple());
		TagEditWindowAdapter.setRequired(tag.getPropRequired());
		TagEditWindowAdapter.setMessageTypes(tag.getTypesOfMessages());
		TagEditWindowAdapter.setDocumentTypes(tag.getTypesOfDocuments());
		TagEditWindowAdapter.setPeopleTypes(tag.getTypesOfPeople());
		TagEditWindowAdapter.setWorkTypes(tag.getTypesOfWorks());
		TagEditWindowAdapter.submitEditing();
	}
}
