//USEUNIT Page
//USEUNIT PersonUserProfilePage

var PAGE_IDENTIFIER="/InviteNewUser";

function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageInviteNewUser);
}
function getFirstNameTextBox(){
  return Aliases.browser.pageInviteNewUser.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.form.panelClearfix.panelLeft.tableSimpleSpaced.firstNameTextBox;
}
function getLastNameTextBox(){
  return Aliases.browser.pageInviteNewUser.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.form.panelClearfix.panelLeft.tableSimpleSpaced.lastNameTextBox;
}
function getEmailTextBox(){
  return Aliases.browser.pageInviteNewUser.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.form.panelClearfix.panelLeft.tableSimpleSpaced.emailTextBox;
}
function getInviteControl(){
  return Aliases.browser.pageInviteNewUser.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.form.panelBottomCenter.inviteControl;
}
function getInvitedUserNameLink(){
  return Aliases.browser.pageInviteNewUser.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.panelMsgBox.panelInvitedUserName;
}
function getMoreLinkFromInvitedUserNamePopUp(){
  return Aliases.browser.pageInviteNewUser.panelInvitedUserPopup.panelUI.panelPs.table.cell20.moreControl;
}
function isPageOpen(){
  return (Page.isPageOpen(PAGE_IDENTIFIER)); //if the page url contains "/InviteNewUser"
}
function setFirstName(name){
  InviteNewUserPage.getFirstNameTextBox().SetText(name);
}
function setLastName(lastName){
  InviteNewUserPage.getLastNameTextBox().SetText(lastName);
}
function setEmail(email){
  InviteNewUserPage.getEmailTextBox().SetText(email);
}
function clickInviteControl(){
  InviteNewUserPage.getInviteControl().Click();
  InviteNewUserPage.wait();
  return InviteNewUserPage.existsInvitedUserNameLink();
}
function existsInvitedUserNameLink(){
  return InviteNewUserPage.getInvitedUserNameLink().Exists;
}
function clickMoreLinkFromInvitedUserNamePopUp(){
  getMoreLinkFromInvitedUserNamePopUp().Click();
  PersonUserProfilePage.wait();
}