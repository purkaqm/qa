//USEUNIT PersonSettingsPreferencesPage
//USEUNIT PersonSettingsMenuPanel
function getPersonSettingsTabBar(){
  return PersonSettingsMenuPanel.getPersonSettingsTabBar();
}
function getPreferencesTab(){
  return PersonSettingsMenuPanel.getPreferencesTab();
}
function getProfileTab(){
  return PersonSettingsMenuPanel.getProfileTab();
}
function getPermissionsTab(){
  return PersonSettingsMenuPanel.getPermissionsTab();
}
function getAlertSubscriptionsTab(){
  return PersonSettingsMenuPanel.getAlertSubscriptionsTab();
}
function getPersonalRatesTab(){
  return PersonSettingsMenuPanel.getPersonalRatesTab();
}
function getHistoryTab(){
  return PersonSettingsMenuPanel.getHistoryTab();
}
function getFavoritesTab(){
  return PersonSettingsMenuPanel.getFavoritesTab();
}
function isPersonSettingsPreferencesPageOpen(){
  return (PersonSettingsPreferencesPage.isPageOpen(PAGE_IDENTIFIER) && PersonSettingsMenu.getPreferencesTab().Exists);
}