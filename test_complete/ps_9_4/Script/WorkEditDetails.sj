//USEUNIT WorkEditDetailsPage
//USEUNIT WorkSummaryPage
//USEUNIT Panel
//USEUNIT TeamMembersPanel
//USEUNIT Work
//USEUNIT ToolBarLinks
//edits the work description
function editWorkDescription(pName, description){
  Work.openWork(pName);
  WorkEditDetailsPage.openPageFromLink();
  WorkEditDetailsPage.setDescriptionTextArea(description);
  WorkEditDetailsPage.clickSaveChanges();
  WorkSummaryPage.wait();
  var desc=WorkSummaryPage.getDescriptionValue();
  if(!aqString.Compare(description,desc,true)){
    Log.Message("Edit details process updates correctly the project description");
    return true;
  }else{
    Log.Error("Edit details process does not updates correctly the project description");
    return false;
  }
}
//adds a user to work team members
function addUserToWorkTeamMember(pName, memberName, roleName){
  Log.Message("Adding user to team member. User name = "+pName+" role = "+roleName);
  Work.openWork(pName);
  WorkEditDetailsPage.openPageFromLink();  //opens edit details page
  return TeamMembersPanel.addTeamMember(memberName, WorkEditDetailsPage.SELECT_IN_PEOPLE_VALUE, roleName, true);
}
//adds a group to work team members
function addGroupToWorkTeamMember(pName, groupName, roleName){
  Log.Message("Adding group to team member. Group name = "+groupName+" role = "+roleName);
  Work.openWork(pName);
  WorkEditDetailsPage.openPageFromLink();  //opens edit details page
  return TeamMembersPanel.addTeamMember(groupName, WorkEditDetailsPage.SELECT_IN_GROUPS_VALUE, roleName, true);
}
//Opens edit details page by direct url
function openPageFromUrl(reOpen){
  return WorkEditDetailsPage.openPageFromUrl(reOpen);
}
//is Edit details link present?
function isWorkEditDetailsLinkPresent(pName){
  Work.openWork(pName);
  if(ToolBarLinks.isToolBarItemPresent(ToolBarLinks.WORK_OPTIONS)){
    var pre=ToolBarLinks.isWorkOptionsSubItemPresent(ToolBarLinks.WO_EDIT_DETAILS);
    return pre;
  }else{
    return false;
  }
}
