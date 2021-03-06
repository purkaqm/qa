//USEUNIT Table
var PROJECT_SEARCH_TYPE="Projects";
var USER_SEARCH_TYPE="People";
//returns Favorites control
function getFavoritesControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelFavoritesIconShadow.favoritesControl;
}
//returns search control
function getSearchControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchIconShadow.searchControl;
}
//returns help control
function getHelpControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelHelpIconShadow.helpControl;
}
//returns User Name
function getUserName(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelUser.panelUserNameLink.panelUser;
}
//return logOut link
function getLogOutControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelUser.panelLogout.logoutForm.logOutControl;
}
//returns page title
function getPageTitle(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderTitle.panelPageTitle.title;
}
//returns page title
function getPageTitleProblemDetected(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderTitle.panelPageTitle.titleProblemDetected;
}
//returns the page title
function getTitle(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderTitle.panelPageTitle.title;
}
//returns the logo
function getLogo(){
  return Aliases.browser.panelHeader.panelHeader.panelLogoWrap;
}
//returns search textField
function getSearchTextBox(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.panelQs.searchTextBox;
}
//sets searched text textField
function setSearchText(itemName){
  getSearchTextBox().Click();
  getSearchTextBox().Keys(itemName);//Sets the name
}
//selects target drop-down
function selectTarget(target){
  Aliases.browser.popupSearch.selectTarget.ClickItem(target);//Selects the desired item
}
//returns the control that should be clicked to see the search type selection table
function getOpenTypeSelectionControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.panelQs.panelQuickSearchType;
}
//returns the Search Types table
function getQuickSearchTypesPopUpTable(){
  getOpenTypeSelectionControl().Click();
  return Aliases.browser.panelMultiUsePopUp.tableQuickSearchType;
}
//returns the Advanced Search link
function getAdvancedControlLink(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.advancedSearchControl;
}
//returns the return control visible when you are proxying an user
function getProxyUserReturnControl(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelUser.panelLogout.returnControl;
}
//returns the proxy user image
function getProxyUserImagePanel(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelUserimageIconwrapper.panelUserfoursquare.ProxyUserImageId;
}
//returns if title is equals to tName
function compareTitleContentText(tName){
  if(!aqString.Compare(getTitle().contentText,tName,true)){
    return true;
  }else{
    return false;
  }
}
//returns true if the page contains the specified text
function pageTitleContainsText(text){
  if(aqString.Find(getTitle().contentText,text,0,true) >= 0){
    return true;
  }else{
    return false;
  }
}
