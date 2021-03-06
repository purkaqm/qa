//USEUNIT HomePage
//USEUNIT AdminPopup
//USEUNIT BrowsePopup

var ADMIN_DEFINE_CATEGORIES=1;
var BROWSE_CREATE_NEW_PROJECT=2;
var BROWSE_WORK_TREE=3;
//get Browse control
function getBrowseControl(){
  HomePage.openPageFromUrl(false);//because this link is mapped under Aliases.browser.pageHome...
  return Aliases.browser.panelHeader.mainNav.showBrowsePopupControl;
}

//get Search link
function getSearchControl(){
  HomePage.openPageFromUrl(false); //because this link is mapped under Aliases.browser.pageHome...
  return Aliases.browser.panelHeader.mainNav.searchControl;
}

//get Admin control
function getAdminControl(){
  HomePage.openPageFromUrl(false);//because this link is mapped under Aliases.browser.pageHome...
  return Aliases.browser.panelHeader.mainNav.showAdminPopupControl;
}

//returns a admin sub option link. Logs an error and returns Null if item value is invalid
function getAdminSubItem(item){
    MainNavLinks.getAdminControl().Click();
    switch(item){
      case MainNavLinks.ADMIN_DEFINE_CATEGORIES: return AdminPopup.getDefineCategoriesLink();
      default: Log.Error("Admin Item: '"+item+"' not found."); return null;
    }
}
//returns a browse sub option link. Logs an error and returns Null if item value is invalid
function getBrowseSubItem(item){
    MainNavLinks.getBrowseControl().Click();
    switch(item){
      case MainNavLinks.BROWSE_CREATE_NEW_PROJECT: return BrowsePopup.getCreateNewProjectLink();
      case MainNavLinks.BROWSE_WORK_TREE: return BrowsePopup.getWorkTreeLink();
      default: Log.Error("Browse Item: '"+item+"' not found."); return null;
    }
}
