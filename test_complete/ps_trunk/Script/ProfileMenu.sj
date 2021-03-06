//USEUNIT PersonSettingsProfilePage
//USEUNIT ProxySwitchToUserPopUp
function getProfileMenuContainer(){
  Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelUser.panelUserNameLink.panelProfileTriangleControl.Click();
  //Aliases.browser.panelHeader.panelHeader.WaitSlApplication(500);//wait 0.5 second
  return Aliases.browser.paneUserProfileMenu;
}
//returns menu item if its name is equals to iName; if not returns null
function getMenuItemByName(iName){
  var container=getProfileMenuContainer();
  for(var i=0;i<container.ChildCount;i++){
    var item=container.Child(i);
    if(!aqString.Compare(item.contentText,iName,true)){//true if is case-sensitive
      Log.Message("'"+item.contentText+"'");
      return item;  //returns the first menu item with name equals to iName.
    }
  }
  return null;
}
//returns profile item from profile menu
function getProfileControlFromProfileMenu(){
  return getMenuItemByName("Profile");
}