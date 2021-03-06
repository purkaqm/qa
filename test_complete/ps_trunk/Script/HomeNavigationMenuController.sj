//USEUNIT HomeNavigationMenu
//USEUNIT PersonSettingsPreferencesPage

//this script is implemented to contain only the click events of the HomeNavigationMenu and it can only be imported in test scripts (XxYyZzTest)
//If you want to call to the same click method from a page, implement it in HomeNavigationMenu script. This is the solution for circular reference error

function clickConfigureHomePageControl(){
  var item=HomeNavigationMenu.getConfigureHomePageControl();
  var res=false;
  if(item.Exists){
    item.Click();
    PersonSettingsPreferencesPage.wait();
    res=true;
    if(!PersonSettingsPreferencesPage.isPageOpen()){
      Logger.logWarning("Person Settings Preferences page is not opened after clicking Configure Home Page link from Home Navigation Menu.");
    }
  }else{
    Logger.logWarning("Configure Home Page link is not displayed on Home Navigation Menu.");
  }
  return res;
}