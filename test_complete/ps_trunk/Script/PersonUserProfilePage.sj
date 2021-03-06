//USEUNIT Page
function wait(){//Waits until the browser loads the Person page.
  Page.wait(PersonUserProfilePage.getPage());
}
function getPage(){
  return Aliases.browser.pagePersonUserProfile;
}
function isPageOpen(){
  return (PersonUserProfilePage.getPage().Exists && PersonUserProfilePage.pageTitleContainsUserProfileText());
}
function getGroupsCell(){
  return Aliases.browser.pagePersonUserProfile.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelClearfixStep0.panelBox.panelLeft.tableSimple.groupsCell;
}
function getProxyUsersCell(){
  return Aliases.browser.pagePersonUserProfile.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelClearfixStep0.panelBox.panelLeft.tableSimple.proxyUsersCell;
}
function pageTitleContainsUserProfileText(){
  return HeaderPanel.pageTitleContainsText(": Profile : User Profile");
}
