//USEUNIT WorkEditDetailsPage
//USEUNIT WorkSummaryPage
//USEUNIT Panel
//USEUNIT TeamMembersPanel
//USEUNIT Work
//USEUNIT ProjectNavigationMenu
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
    Log.Message("Edit details process does not updates correctly the project description");
    return false;
  }
}
function addUsersToWorkTeamMembers(pName, userNames, roleNames){
  var user, role, i;
  for(i=0;i<userNames.length;i++){
    if(!addUserToWorkTeamMember(pName, userNames[i], roleNames[i])){
      Log.Message("User '"+userNames[i]+"' is not been added to '"+roleNames[i]+"' role");
      return false;
    }
  }
  return true;
}
//adds a user to work team members
function addUserToWorkTeamMember(pName, memberName, roleName){
  Log.Message("Adding user to team member. User name = "+pName+" role = "+roleName);
  Work.openWork(pName);
  WorkEditDetailsPage.openPageFromLink();  //opens edit details page
  return TeamMembersPanel.addTeamMember(memberName, WorkEditDetailsPage.SELECT_IN_PEOPLE_VALUE, roleName, true);
}
function verifyUsersAndRolesOnAllTeamMembersListPanel(users, roles){
  var list=WorkEditDetailsPage.getSelectedRolesAndUsersList();
  var role, user, i;
  for(i=0;i<users.length;i++){
    role=list.FindChild(["ObjectType","contentText"], ["TextNode",roles[i]], 100, true);
    if(role.Exists){
      user=list.FindChild(["ObjectType","contentText","ObjectIdentifier"], ["Table",users[i],role.ObjectIdentifier], 100, true);
      if(!user.Exists){
        Log.Warning("'"+users[i]+" user is NOT assigned to '"+roles[i]+"' role in All Team Members section.");
        return false;
      }else{
        Log.Message("'"+users[i]+" user is assigned to '"+roles[i]+"' role in All Team Members section.");
      }
    }else{
      Log.Warning("'"+roles[i]+" role does NOT have assigned Users in All Team Members section.");
      return false;
    }
  }
  return true;
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
function openPageFromLink(link){
  return WorkEditDetailsPage.openPageFromLink(link);
}
//is Edit details link present?
function isWorkEditDetailsLinkPresent(pName){
  Work.openWork(pName);
  return ProjectNavigationMenu.existsEditDetailsControl();
}
