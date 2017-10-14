package com.powersteeringsoftware.core.objects.permissions.define_categories.workitems;

import java.util.HashMap;
import com.powersteeringsoftware.core.objects.permissions.PermissionCategoriesEnum;
import com.powersteeringsoftware.core.objects.permissions.define_categories.DefinePermissionLine;
import com.powersteeringsoftware.core.objects.permissions.define_categories.ICategoryPermissions;
import com.powersteeringsoftware.core.tc.PSTestCaseException;

public class WorkItemCategoryPermissions implements ICategoryPermissions {
	private HashMap<WorkItemsPermissionsEnum, DefinePermissionLine> permissionTable;

	public PermissionCategoriesEnum getPermissionCategory() {
		return PermissionCategoriesEnum.WORK_ITEMS;
	}

	public void setPermissionLine(WorkItemsPermissionsEnum permission, DefinePermissionLine line){
		permissionTable.put(permission, line);
	}

	public void setPermissionTable(DefinePermissionLine line){
		if(null==line) throw new PSTestCaseException("DefinePermissionLine objects can't be null.");

		permissionTable = new HashMap<WorkItemsPermissionsEnum, DefinePermissionLine>(70);
		setAddActionItem(line);
		setAddChild(line);
		setAddDiscussionItem(line);
		setAddDocument(line);
		setAddMetric(line);
		setAddTeamMember(line);
		setApproveRejectBestPracticeNomination(line);
		setApproveRejectIdea(line);
		setApproveRejectTime(line);
		setArchive(line);
		setAssignChampion(line);
		setAssignUsersToResourceNeeds(line);
		setChangePhase(line);
		setCopyAttachMeasure(line);
		setCreateProjectReports(line);
		setCreateRisks(line);
		setDelegate(line);
		setDelete(line);
		setDeleteDocument(line);
		setEdit(line);
		setEditActualSchedule(line);
		setEditConstraintDates(line);
		setEditControls(line);
		setEditCost(line);
		setEditDocument(line);
		setEditDocumentApprovalSettings(line);
		setEditElectronicSignatureSettings(line);
		setEditMeasureHistory(line);
		setEditPermissions(line);
		setEditPriority(line);
		setEditResourceNeeds(line);
		setEditResourcesAllocation(line);
		setEditRisks(line);
		setEditStatus(line);
		setEditTags(line);
		setEditWorkBreakdown(line);
		setExpandIdea(line);
		setLockMetrics(line);
		setMakeAnIssue(line);
		setMassDelegateWork(line);
		setMove(line);
		setOvverideBaselineDates(line);
		setReActivateGate(line);
		setRemoveIssues(line);
		setRemoveMetric(line);
		setRemoveTeamMember(line);
		setRequestApprovalOfIdea(line);
		setDeliverableFlag(line);
		setReadyForRollUp(line);
		setUpdateMetricNumbers(line);
		setView(line);
		setViewAssociatedMetrics(line);
		setViewCosts(line);
		setViewDiscussion(line);
		setViewDocuments(line);
		setViewHistory(line);
		setViewMetricOnWorkSummary(line);
		setViewPermissions(line);
		setViewProjectReports(line);
		setViewRisks(line);
		setStatusReports(line);
		setViewWorkBreakdown(line);
	}

	public void setAddActionItem(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_ACTION_ITEM, line);
	}

	public void setAddChild(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_CHILD, line);
	}

	public void setAddDiscussionItem(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_DISCUSSION_ITEM, line);
	}

	public void setAddDocument(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_DOCUMENT, line);
	}

	public void setAddMetric(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_METRIC, line);
	}

	public void setAddTeamMember(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ADD_TEAM_MEMBER, line);
	}

	public void setApproveRejectBestPracticeNomination(
			DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.APPROVE_REJECT_BEST_PRACTICE_NOMINATION, line);
	}

	public void setApproveRejectIdea(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.APPROVE_REJECT_IDEA, line);
	}

	public void setApproveRejectTime(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.APPROVE_REJECT_TIME, line);
	}

	public void setArchive(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ARCHIVE, line);
	}

	public void setAssignChampion(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ASSIGN_CHAMPION, line);
	}

	public void setAssignUsersToResourceNeeds(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.ASSIGN_USERS_TO_RESOURCE_NEEDS, line);
	}

	public void setChangePhase(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.CHANGE_PHASE, line);
	}

	public void setCopyAttachMeasure(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.COPY_ATTACH_MEASURE, line);
	}

	public void setCreateProjectReports(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.CREATE_PROJECT_REPORTS, line);
	}

	public void setCreateRisks(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.CREATE_RISKS, line);
	}

	public void setDelegate(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.DELEGATE, line);
	}

	public void setDelete(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.DELETE, line);
	}

	public void setDeleteDocument(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.DELETE_DOCUMENT, line);
	}

	public void setEdit(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT, line);
	}

	public void setEditActualSchedule(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_ACTUAL_SCHEDULE, line);
	}

	public void setEditConstraintDates(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_CONSTRAINT_DATES, line);
	}

	public void setEditControls(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_CONTROLS, line);
	}

	public void setEditCost(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_COST, line);
	}

	public void setEditDocument(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_DOCUMENT, line);
	}

	public void setEditDocumentApprovalSettings(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_DOCUMENT_APPROVAL_SETTINGS, line);
	}

	public void setEditElectronicSignatureSettings(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_ELECTRONIC_SIGNATURE_SETTINGS, line);
	}

	public void setEditMeasureHistory(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_MEASURE_HISTORY, line);
	}

	public void setEditPermissions(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_PERMISSIONS, line);
	}

	public void setEditPriority(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_PRIORITY, line);
	}

	public void setEditResourceNeeds(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_RESOURCE_NEEDS, line);
	}

	public void setEditResourcesAllocation(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_RESOURCES_ALLOCATION, line);
	}

	public void setEditRisks(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_RISKS, line);
	}

	public void setEditStatus(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_STATUS, line);
	}

	public void setEditTags(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_TAGS, line);
	}

	public void setEditWorkBreakdown(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EDIT_WORK_BREAKDOWN, line);
	}

	public void setExpandIdea(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.EXPAND_IDEA, line);
	}

	public void setLockMetrics(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.LOCK_METRICS, line);
	}

	public void setMakeAnIssue(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.MAKE_AN_ISSUE, line);
	}

	public void setMassDelegateWork(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.MASS_DELEGATE_WORK, line);
	}

	public void setMove(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.MOVE, line);
	}

	public void setOvverideBaselineDates(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.OVERRIDE_BASELINE_DATES, line);
	}

	public void setReActivateGate(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.RE_ACTIVATE_GATE, line);
	}

	public void setRemoveIssues(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.REMOVE_ISSUES, line);
	}

	public void setRemoveMetric(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.REMOVE_METRIC, line);
	}

	public void setRemoveTeamMember(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.REMOVE_TEAM_MEMBER, line);
	}

	public void setRequestApprovalOfIdea(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.REQUEST_APPROVAL_OF_IDEA, line);
	}

	public void setDeliverableFlag(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.SET_DELIVERABLE_FLAG, line);
	}

	public void setReadyForRollUp(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.SET_READY_FOR_ROLL_UP, line);
	}

	public void setUpdateMetricNumbers(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.UPDATE_METRIC_NUMBERS, line);
	}

	public void setView(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW, line);
	}

	public void setViewAssociatedMetrics(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_ASSOCIATED_METRICS, line);
	}

	public void setViewCosts(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_COST, line);
	}

	public void setViewDiscussion(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_DISCUSSION, line);
	}

	public void setViewDocuments(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_DOCUMENTS, line);
	}

	public void setViewHistory(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_HISTORY, line);
	}

	public void setViewMetricOnWorkSummary(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_METRIC_ON_WORK_SUMMARY, line);
	}

	public void setViewPermissions(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_PERMISSIONS, line);
	}

	public void setViewProjectReports(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_PROJECT_REPORTS, line);
	}

	public void setViewRisks(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_RISKS, line);
	}

	public void setStatusReports(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_STATUS_REPORTS, line);
	}

	public void setViewWorkBreakdown(DefinePermissionLine line) {
		permissionTable.put(WorkItemsPermissionsEnum.VIEW_WORK_BREAKDOWN, line);
	}

}
