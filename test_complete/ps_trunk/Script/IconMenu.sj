var ADD_MENU_ICON_NAME="ADD";
var REVIEW_MENU_ICON_NAME="REVIEW";
var ADMIN_MENU_ICON_NAME="ADMIN";
var HISTORY_MENU_ICON_NAME="HISTORY";
var PROJECT_MENU_ICON_NAME="PROJECT";
var IMPORTANT_LINKS_MENU_ICON_NAME="IMPORTANT LINKS";
var FAVORITES_MENU_ICON_NAME="FAVORITES";
var HOME_MENU_ICON_NAME="HOME";

/*function getIconMenuItemByTitle(title){
  var propNames = Array("title", "ObjectType"); 
  var propValues = Array(title, "Link"); 
  return IconMenu.getIconBar().FindChild(propNames, propValues, 10, false);
}
*/
function getIconBar(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar;
}
function getAddControl(){
  //return IconMenu.getIconMenuItemByTitle("Add");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.linkAdd;
}
function getAdminControl(){
  //return IconMenu.getIconMenuItemByTitle("Admin");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.adminControl;
}
function getFavoritesControl(){
  //return IconMenu.getIconMenuItemByTitle("Favorites");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.favoritesControl;
}
function getProjectControl(){
  //return IconMenu.getIconMenuItemByTitle("Project");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.projectControl;
}
function getReviewControl(){
  //return IconMenu.getIconMenuItemByTitle("Review");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.reviewControl;
}
function getHistoryControl(){
  //return IconMenu.getIconMenuItemByTitle("History");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.historyControl;
}
function getInboxControl(){
  //return IconMenu.getIconMenuItemByTitle("Inbox");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.inboxControl;
}
function getHomeControl(){
  //return IconMenu.getIconMenuItemByTitle("Home");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.homeControl;
}
function getImportantLinksControl(){
  //return IconMenu.getIconMenuItemByTitle("Important Links");
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelIconBar.importantLinksControl;
}
