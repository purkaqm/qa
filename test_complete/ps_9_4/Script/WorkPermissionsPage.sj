//USEUNIT ToolBarLinks
//USEUNIT Browser 
//USEUNIT Page
//USEUNIT WorkPage
    var _url=null;
    var PAGE_IDENTIFIER="project/Permissions";
    var MAX_WAIT_TIME=60000;//max wait time in ms
    var FIRST_FIND_RESULT_WAIT_MESSAGE="One moment please...";//textNode0 element wait message. displayed while user/group search is running
    
    function wait(){//Waits until the browser loads the page.
      Page.wait(Aliases.browser.pagePermissions);
    }
    
    function openPageFromUrl(reOpen){//navigates to Edit_Permissions page
      Page.openFromUrl(Aliases.browser.pagePermissions, _url, reOpen, WorkPermissionsPage.isPageOpen());
      return WorkPermissionsPage.isPageOpen();
    }
    
    function isPageOpen(){ //returns wheter the page is open
      return WorkPage.isPageOpenAndHasPermission(PAGE_IDENTIFIER); //if the current url contains "project/Permissions"
    }
//click Options->View_Permissions link and stores the new page url
function openViewPermissionsPageFromLink(){
  ToolBarLinks.getWorkOptionsSubItem(ToolBarLinks.WO_VIEW_PERMISSIONS).Click();
  WorkPermissionsPage.wait();
  _url=Page.getUrl(WorkPermissionsPage.isPageOpen());
  Log.Message("Setting EditPermissionsPage URL = '"+_url+"'.");
}
function openEditPermissionsPageFromLink(){
  ToolBarLinks.getWorkOptionsSubItem(ToolBarLinks.WO_EDIT_PERMISSIONS).Click();
  WorkPermissionsPage.wait();
  _url=Page.getUrl(WorkPermissionsPage.isPageOpen());
  Log.Message("Setting EditPermissionsPage URL = '"+_url+"'.");
}
//return true if its editPermissions or false when its viewPermissions
function isEditable(){
  return WorkPermissionsPage.getSaveCustomSetControl().Exists;
}
//get the table that contains the project roles list
function getAddedRolesTable(){
  return Aliases.browser.pagePermissions.panelContent.addedRolesTable;
}

//click the add role button
function clickAddRoleControl(){
  Aliases.browser.pagePermissions.panelContent.addRoleControl.ClickButton();
}
//returns the table that contains the Custom Set radio buttons
function getCustomSetRadioButtonsTable(){
  return Aliases.browser.pagePermissions.panelContent.radioButtonsTable;
  //return Aliases.browser.pageEditPermissions.panelContent.panelContainer.formPermissions.panelPsgrid.panel2.table.radioButtonsCell.radioButtonsPanel.radioButtonsTable;
}
//gets the main custom set save button
function getSaveCustomSetControl(){
  return Aliases.browser.pagePermissions.panelContent.saveCustomSetControl;
}
//click the main custom set save button
function clickSaveCustomSetControl(){
  WorkPermissionsPage.getSaveCustomSetControl().ClickButton();
  WorkPermissionsPage.wait();
}
//search popup searched role textbox
function setSearchTextBox(role){
  return Aliases.browser.pagePermissions.roleSearchPopup.roleSearchTextBox.SetText(role);
}
//search popup go search button
function clickGoControl(){
  Aliases.browser.pagePermissions.roleSearchPopup.goRoleSearchControl.ClickButton();
  WorkPermissionsPage.waitUntilCustomSetSearchFinishes();
}
//search popup search results panel
function getSearchResultsPanel(){
  return Aliases.browser.pagePermissions.roleSearchPopup.roleSearchResultsPanel;
}
//search popup save that role and close search popup
function clickSaveAddedRoleControl(){
  Aliases.browser.pagePermissions.roleSearchPopup.saveRoleControl.ClickButton();
}
//search popup cancel and closes search popup
function clickCancelAddedRoleControl(){
  return Aliases.browser.pagePermissions.roleSearchPopup.cancelRoleControl
}
//search popup first panel that contains the selected roles
function getSelectedRolesPanel(){
  return Aliases.browser.pagePermissions.roleSearchPopup.selectedRolesPanel;
}
//returns the core set names table
function getCoreSetNamesTable(){
  return Aliases.browser.pagePermissions.panelContent.coreSetsTable;
}
//returns the table that contains the coreSet radio buttons
function getCoreSetRadioButtonsTable(){
  return Aliases.browser.pagePermissions.panelContent.coreSetRadioButtonsTable;
}
//click the main core set save button
function clickSaveCoreSetControl(){
  Aliases.browser.pagePermissions.panelContent.saveCoreSetControl.ClickButton();
  WorkPermissionsPage.wait();
}
//the first result or not found message
function getTextNode0(){
  return Aliases.browser.pagePermissions.roleSearchPopup.roleSearchResultsPanel.textNode0;
}
//wait until the find operation finishes or timeOut
function waitUntilCustomSetSearchFinishes(){
  /*WorkPermissionsPage.getSearchResultsPanel().WaitAliasChild(WorkPermissionsPage.FIRST_FIND_RESULT_NAME,WorkPermissionsPage.MAX_WAIT_TIME);
  WorkPermissionsPage.getSearchResultsPanel().WaitSlApplication(3000);//wait 1 second more (needed)
  WorkPermissionsPage.getSearchResultsPanel().Refresh();*/
  for(;;){
    if(WorkPermissionsPage.getTextNode0().Exists){
      if(aqString.Compare(WorkPermissionsPage.FIRST_FIND_RESULT_WAIT_MESSAGE, WorkPermissionsPage.getTextNode0().contentText,false)){
        //Log.Message("TextNode content ="+WorkPermissionsPage.getTextNode0().contentText);
        break;//if when wait message disappears
      }
    }
  }
}