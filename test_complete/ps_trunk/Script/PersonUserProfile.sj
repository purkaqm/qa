//USEUNIT PersonUserProfilePage
//USEUNIT Logger
function areGroupsSetToUser(groupNames){
  var allGroups=PersonUserProfilePage.getGroupsCell().contentText;
  for(var i=0;i<groupNames.length;i++){
    name=groupNames[i];
    if(aqString.Find(allGroups,name,0,true) < 0){
      Logger.logWarning("Group ="+name+" is not set in User Profile page.");
      return false;
    }else{
      Log.Message("Group = "+name+"is set in User Profile page.")
    }
  }
  if(groupNames.length>0){
    return true;
  }else{
    Logger.logWarning("There were NO User Groups specified to be checked in User Profile page.");
    return false;
  }
}
function clickUserLinkToOpenUserProfilePage(userLink){
  userLink.Click();
  PersonUserProfilePage.wait();
  return PersonUserProfilePage.isPageOpen();
}
function areProxyUsersSetToUser(proxyUserNames){
  var proxies=PersonUserProfilePage.getProxyUsersCell().contentText;
  for(var i=0;i<proxyUserNames.length;i++){
    name=proxyUserNames[i];
    if(aqString.Find(proxies,name,0,true) < 0){
      Logger.logWarning("Proxy User = '"+name+"' is not set in User Profile page.");
      return false;
    }else{
      Log.Message("Proxy User = '"+name+"' is set in User Profile page.")
    }
  }
  if(proxyUserNames.length>0){
    return true;
  }else{
    Log.Error("There were NO Proxy Users specified to be checked in User Profile page.");
    return false;
  }
}