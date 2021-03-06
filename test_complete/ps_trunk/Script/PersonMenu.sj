//USEUNIT PersonMenuPanel
//USEUNIT PersonSettingsProfilePage
function getPersonTabBar(){
  return PersonMenuPanel.getPersonTabBar();
}
function getUserProfileTab(){
  return PersonMenuPanel.getUserProfileTab();
}
function getAgendaTab(){
  return PersonMenuPanel.getAgendaTab();
}
function getWorkloadTab(){
  return PersonMenuPanel.getWorkloadTab();
}
function getTimesheetsTab(){
  return PersonMenuPanel.getTimesheetsTab();
}
function getDocumentsTab(){
  return PersonMenuPanel.getDocumentsTab();
}
function getEditUserTab(){
  return PersonMenuPanel.getEditUserTab();
}
function isUserProfileTabSelected(){
  if(!aqString.Compare(PersonMenu.getUserProfileTab().className,"hl nolink",true)){
    return true;
  }else{
    return false;
  }
}
function clickEditUserTab(){
  PersonMenu.getEditUserTab().Click();
  PersonSettingsProfilePage.wait();
}