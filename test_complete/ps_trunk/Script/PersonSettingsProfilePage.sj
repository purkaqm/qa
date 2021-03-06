//USEUNIT Page
//USEUNIT PersonUserProfilePage
//USEUNIT UserGroupsComboBoxPanel
//USEUNIT UserGroupsComboBoxPanel
function wait(){//Waits until the browser loads the page.
  Page.wait(PersonSettingsProfilePage.getPage());
}
function getPage(){
  return Aliases.browser.pagePersonSettingsProfile;
}
function getUserNameTextBox(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelSettings.panelClearFix.panelBox.tableSimple.cell11.UserNameTextBox;
}
function getNewPasswordTextBox(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelSettings.panelClearFix.panelBox.tableSimple.cell21.newPasswordTextBox;
}
function getConfirmPasswordTextBox(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelSettings.panelClearFix.panelBox.tableSimple.cell31.confirmPasswordTextBox;
}
function getSaveControl(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelClearfixButtons.saveControl;
}
function getGroupsTextBox(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelSettings.panelClearFixStep2.panelBox.tableSimple.cellGroups.panelMultiSelect.groupsPanel;
}
function getProxyUserComboBox(){
  return Aliases.browser.pagePersonSettingsProfile.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formMainform.panelSettings.panelClearFix.panelBox.tableSimple.proxyUserCell.panelSearchBrowse.panelClearfix.panelPopUpProxy;
}
function clickProxyUserComboBox(){
  PersonSettingsProfilePage.getProxyUserComboBox().Click();
}
function clickGroupsTextBox(){
  PersonSettingsProfilePage.getGroupsTextBox().Click();
}
function setUserName(login){
  PersonSettingsProfilePage.getUserNameTextBox().setText(login);
}
function setNewPassword(pass){
  PersonSettingsProfilePage.getNewPasswordTextBox().setText(pass);
}
function setConfirmPassword(pass){
  PersonSettingsProfilePage.getConfirmPasswordTextBox().setText(pass);
}
function clickSaveControl(){
  PersonSettingsProfilePage.getSaveControl().Click();
  PersonUserProfilePage.wait();
}
function checkValuesFromUserGroupsComboBox(values){
  PersonSettingsProfilePage.clickGroupsTextBox();
  return UserGroupsComboBoxPanel.checkValuesFromUserGroupsComboBox(values);
}