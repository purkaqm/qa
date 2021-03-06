//USEUNIT NewLocationPopUp
//USEUNIT Browser
function selectNewLocation(targetProject){
  NewLocationPopUp.getSearchTab().Click();
  NewLocationPopUp.setSearchTextBox(targetProject);
  NewLocationPopUp.clickGoControl();
  NewLocationPopUp.waitToSearchResults();
  var parent=NewLocationPopUp.getSearchResultsPanel().FindChild(["ObjectType","className","ContentText"], ["Panel","link",targetProject], 100, true);
  if(parent.Exists){
    parent.Click();
    Browser.getBrowser().WaitSlApplication(1000);
    return true;
  }else{
    return false;
  }
}