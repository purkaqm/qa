//USEUNIT Page
var WITHOUT_PERMISSION_MESSAGE_TITLE="Problem Detected";
function isPageOpenAndHasPermission(pageIdentifier){ //returns wheter the page is open and has permission
  if(Page.isPageOpen(pageIdentifier)){ //if the current url contains the page identifier
    return Aliases.browser.paneToolBar.Exists;//if has not permission to view work page, toolbar is not displayed
  }else{
    return false;
  }
}