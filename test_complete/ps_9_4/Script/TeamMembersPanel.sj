//USEUNIT WorkEditDetailsPage
//USEUNIT Panel
//USEUNIT Page
//USEUNIT Table
function addTeamMember(memberName, selectInValue, roleName, caseSensitive){
  WorkEditDetailsPage.setFindTeamMembersText(memberName); //sets find text
  WorkEditDetailsPage.selectInSelector(selectInValue);//selects the member name from the In drop down list
  WorkEditDetailsPage.clickGoFindControl();//clicks the do search button
  var memberIndex=Panel.getInverseChildIndexOfPanelSubObject(WorkEditDetailsPage.getSearchResultsPanel(), memberName, Page.OBJECT_TYPE_LINK,caseSensitive);  //gets the team member index on the search result list
  var roleIndex=Panel.getInverseChildIndexByName(WorkEditDetailsPage.getRolesDropPane(), roleName, caseSensitive);//gets the role index on the roles list
  roleIndex--;//because roles panes has 2 children object on the title "Roles (Drag user on role to assign)"
  var pxDiff=TeamMembersPanel.getVerticalDifferenceBetweenMemberAndRoleItemsInPx(memberIndex, roleIndex);//calculates the difference between the vertical position of both items
  var memberObject=getSubObjectByNameTypeAndId(WorkEditDetailsPage.getSearchResultsPanel(), memberName, Panel.OBJECT_TYPE_LINK, null, caseSensitive);//gets the member object to be dragged
  memberObject.Drag(WorkEditDetailsPage.ADD_TM_LEFT_RESULT_ITEM_WIDTH/10, WorkEditDetailsPage.ADD_TM_LEFT_RESULT_ITEM_HEIGHT/2, WorkEditDetailsPage.ADD_TM_DISTANCE_BETWEEN_LEFT_PANE_CENTER_AND_CENTER_PANE_CENTER, pxDiff);//drags the member object to the desired role. TestObj.Drag(ClientX, ClientY, toX, toY, Shift);
  var exists=TeamMembersPanel.isTeamMember(memberName, roleName, caseSensitive);//returns if added correctly
  if(exists){
    Log.Message("Saving - add Team Member. Member = "+memberName+" role = "+roleName);
    WorkEditDetailsPage.clickSaveChanges();
  }else{
    Log.Error("Can NOT Save - add Team Member. Member = "+memberName+" role = "+roleName);
  }
  return exists;
}
function getVerticalDifferenceBetweenMemberAndRoleItemsInPx(memberIndex, roleIndex){//returns the verticarl difference between searched TM item and role item. It substracts the role item center's verticarl position - TM item center's verticarl position in orden to drag and drop one to the other
  if(memberIndex >= 0 && roleIndex >= 0){
    var memberVerticalPX=WorkEditDetailsPage.ADD_TM_LEFT_TITLE_PANEL_HEIGHT+WorkEditDetailsPage.ADD_TM_LEFT_FIND_PANEL_HEIGHT+WorkEditDetailsPage.ADD_TM_LEFT_RESULT_BORDER_HEIGHT+(memberIndex*WorkEditDetailsPage.ADD_TM_LEFT_RESULT_ITEM_HEIGHT)+(WorkEditDetailsPage.ADD_TM_LEFT_RESULT_ITEM_HEIGHT/2);
    var roleVerticalPX=(roleIndex*WorkEditDetailsPage.ADD_TM_CENTER_ITEM_HEIGHT)+(WorkEditDetailsPage.ADD_TM_CENTER_ITEM_HEIGHT/2);//title has the same height than the other items
    return (roleVerticalPX-memberVerticalPX);//returns the vertical rusult in PX    
  }else{
    Log.Error("Error getting Vertical Difference Between Member And Role Items. One or both indexes are out of range. TMIndex = "+memberIndex+" and roleIndex = "+roleIndex);
    return null;
  }
}
//returns if a user/group is a team member. It searches that user/group in the roles list and returns if its present there.
function isTeamMember(member, role, caseSensitive){
  //It is not needed to select one role from the drop down list because SelectedRolesAndUsersList contains allways the complete list like "All Team Members" list
  var item=Panel.getSubObjectByNameTypeAndId(WorkEditDetailsPage.getSelectedRolesAndUsersList(), role, Page.OBJECT_TYPE_TEXTNODE, null, caseSensitive);
  var item2=Panel.getSubObjectByNameTypeAndId(WorkEditDetailsPage.getSelectedRolesAndUsersList(), member, Page.OBJECT_TYPE_TABLE, item.ObjectIdentifier, caseSensitive);
  if(null!=item2){
    Log.Message(member+" is a team member with role = "+role);
    return true;
  }else{
    Log.Message(member+" is NOT a team member with role = "+role);
    return false;
  }
}