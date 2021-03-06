//USEUNIT HeaderPanel
//USEUNIT Logger
//returns Favorites control
function getFavoritesControl(){
  return HeaderPanel.etFavoritesControl();
}
//returns search control
function getSearchControl(){
  return HeaderPanel.getSearchControl();
}
//returns help control
function getHelpControl(){
  return HeaderPanel.getHelpControl();
}
//returns User Name
function getUserName(){
  return HeaderPanel.getUserName();
}
//return logOut link
function getLogOutControl(){
  return HeaderPanel.getLogOutControl();
}
//returns page title
function getPageTitle(){
  return HeaderPanel.getPageTitle();
}
//returns page title
function getPageTitleProblemDetected(){
  return HeaderPanel.getPageTitleProblemDetected();
}
//returns the page title
function getTitle(){
  return HeaderPanel.getTitle();
}
//returns the logo
function getLogo(){
  return HeaderPanel.getLogo();
}
//returns search textField
function getSearchTextBox(){
  return HeaderPanel.getSearchTextBox();
}
//sets searched text textField
function setSearchText(itemName){
  return HeaderPanel.setSearchText(itemName);
}
//selects target drop-down
function selectTarget(target){
  return HeaderPanel.selectTarget(target);
}
//returns the control that should be clicked to see the search type selection table
function getOpenTypeSelectionControl(){
  return HeaderPanel.getOpenTypeSelectionControl();
}
//returns the Search Types table
function getQuickSearchTypesPopUpTable(){
  return HeaderPanel.getQuickSearchTypesPopUpTable();
}
//returns the Advanced Search link
function getAdvancedControlLink(){
  return HeaderPanel.getAdvancedControlLink();
}
//returns the return control visible when you are proxying an user
function getProxyUserReturnControl(){
  return HeaderPanel.getProxyUserReturnControl();
}
//returns the proxy user image
function getProxyUserImagePanel(){
  return HeaderPanel.getProxyUserImagePanel();
}
//selects target drop-down
function selectSearchType(type){
  var tab=HeaderPanel.getQuickSearchTypesPopUpTable();
  if(tab.Exists){
    var r=Table.getRowCount(tab);
    for(var rr=0;rr<r;rr++){
        var cell=tab.Cell(rr,2);
        if(!aqString.Compare(type,cell.contentText,true)){
          cell.Click();
          return true;
        }
    }
  }else{
    Logger.logWarning("QuickSearchTypesPopUpTable does not exist.");
  }
  return false;
}
function isUserNameAndLastNameEqualsToX(name, lastName){
  var user=HeaderPanel.getUserName().title;
  var fullName=name+" "+lastName;
  if(!aqString.Compare(fullName,user,true)){
    return true;
  }else{
    Log.Message("User Name '"+fullName+"' is not equals to '"+user+"'.");
    return false;
  }
}
function clickAdvancedSearchLink(){
  openSearchControlsIfNecesary();
  var advanced=HeaderPanel.getAdvancedControlLink();
  if(advanced.Exists){
    advanced.Click();
  }else{
    Logger.logWarning("Advanced Search link does not exist.");
  }
}
function openSearchControlsIfNecesary(){
  if(Header.getSearchControl().Exists){
    Header.getSearchControl().WaitSlApplication(2000);
    if(!(HeaderPanel.getAdvancedControlLink().Exists && HeaderPanel.getAdvancedControlLink().VisibleOnScreen)){
      HeaderPanel.getSearchControl().Click();
      Header.getSearchControl().WaitSlApplication(2000);
      if(HeaderPanel.getAdvancedControlLink().Exists && HeaderPanel.getAdvancedControlLink().VisibleOnScreen){
        return true;
      }else{
        Logger.logWarning("Search controls have not been displayed after clicking Search Icon on Header panel.");
        return false;
      }
    }else{
       return true;
    }
  }else{
    Logger.logWarning("Search Icon on Header Panel does not Exist.");
    return false;
  }
}