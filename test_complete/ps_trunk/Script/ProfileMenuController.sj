//USEUNIT ProfileMenu
//USEUNIT PersonSettingsProfilePage
//USEUNIT PersonUserProfilePage

//this script is implemented to contain only the click events of the ProfileMenu and it can only be imported in test scripts (XxYyZzTest)
//If you want to call to the same click method from a page, implement it in ProfileMenu script. This is the solution for circular reference error

function clickSettingsControl(){
  var item=ProfileMenu.getMenuItemByName("Settings");
  var res=false;
  if(null!=item){
    if(item.Exists){
      item.Click();
      PersonSettingsProfilePage.wait();
      res=true;
      if(!PersonSettingsProfilePage.isPageOpen()){
        Logger.logWarning("Person Settings Preferences page is not opened after clicking 'Settings' link from User Profile Menu.");
      }
    }else{
      Logger.logWarning("'Settings' link is not displayed on User Profile menu.");
    }
  }else{
      Logger.logWarning("'Settings' link on User Profile Menu is null.");
    }
  return res;
}
function clickProfileControl(){
  var item=ProfileMenu.getMenuItemByName("Profile");
  var res=false;
  if(null!=item){
    if(item.Exists){
      item.Click();
      PersonUserProfilePage.wait();
      res=true;
      if(!PersonUserProfilePage.isPageOpen()){
        Logger.logWarning("Person User Profile page is not opened after clicking 'Profile' link from User Profile Menu");
      }
    }else{
      Logger.logWarning("'Profile' link is not displayed on User Profile menu.");
    }
  }else{
      Logger.logWarning("'Profile' link on User Profile Menu is null.");
    }
  return res;
}