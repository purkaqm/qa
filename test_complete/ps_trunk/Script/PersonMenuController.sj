//USEUNIT PersonMenu
//USEUNIT PersonSettingsProfilePage

//this script is implemented to contain only the click events of the PersonMenu and it can only be imported in test scripts (XxYyZzTest)
//If you want to call to the same click method from a page, implement it in PersonMenu script. This is the solution for circular reference error

function clickEditUserTab(){
  PersonMenu.getEditUserTab().Click();
  PersonSettingsProfilePage.wait();
}