//USEUNIT Logger
function getGroupsMainPanel(){
  return Aliases.browser.popUpUserGroups.panelMain;
}
function clickDoneControl(){
  return Aliases.browser.popUpUserGroups.panelToolbarBottom.panelLeft.doneControl.Click();
}
function checkValuesFromUserGroupsComboBox(values){
  var val, group, groupID, check;
  for(var i=0;i<values.length;i++){
    Log.Message("a"+i);
    val=values[i];
    group=UserGroupsComboBoxPanel.getGroupsMainPanel().FindChild(["ObjectType","contentText"], ["Cell",val], 10, true);
    if(group.Exists){
      groupID=group.parent.ObjectIdentifier;
      Log.Message(val+"="+groupID);
      check=UserGroupsComboBoxPanel.getGroupsMainPanel().FindChild(["ObjectType","ObjectIdentifier"], ["Checkbox","groupsId"+groupID], 10, true);
      check.ClickChecked(true);
      if(!check.checked){
        Logger.logWarning("User Groups checkBox value = "+val+" is not checked.");
        return false;
      }else{
        Log.Message("User Groups checkBox value = "+val+" has been checked.");
      }
    }else{
      Logger.logWarning("User Groups = "+val+" does not exist.");
      return false;
    }
  }
  if(values.length>0){
    UserGroupsComboBoxPanel.clickDoneControl();
    return true;
  }else{
    Logger.logWarning("NO User Groups specified to be checked");
    return false;
  }
}