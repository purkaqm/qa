//USEUNIT Page
//USEUNIT MainNavLinks
var PROJECT_TYPE="    Project";
var MAX_WAIT_TIME=60000;//max wait time in ms
var LOCATION_FIND_RESULT_PANEL_NAME="findLocationResultsPanel";//find results panel mapped name

//select the item type

function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageCreateNewProject);
}
function selectProjectType(iType){
  Aliases.browser.pageCreateNewProject.panelStep1.workTypeSelect.ClickItem(iType);
}
//click the continue button
function clickContinueControl(){
  Aliases.browser.pageCreateNewProject.panelStep1.continueControl.ClickButton();
  CreateNewProjectPage.wait();
}
//set the project name
function setProjectName(iName){
  Aliases.browser.pageCreateNewProject.panelStep2.projectNameTextBox.SetText(iName);
}
//click the finish and create control
function clickFinishControl(){
  Aliases.browser.pageCreateNewProject.panelStep2.finishAndCreateControl.ClickButton();
  CreateNewProjectPage.wait();
}

//click Browse->Create_A_Project
function openPageFromLink(){
  MainNavLinks.getBrowseSubItem(MainNavLinks.BROWSE_CREATE_NEW_PROJECT).Click();
  CreateNewProjectPage.wait();
}
//returns the open find location popup field
function getOpenFindLocationPopup(){
  return Aliases.browser.pageCreateNewProject.panelStep2.showLocationPopup;
}
//displays the location selection popup
function clickOpenFindLocationPopup(){
  //Click(x,y)
  CreateNewProjectPage.getOpenFindLocationPopup().Click(1,1);
}
//opens the search tab panel
function clickSearchTab(){
  Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.searchTab.Click(1,1);
}
//sets the find location text box. Parent=new project's parent
function setFindLocationTextBox(parent){
  Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.content.searchTabPanel.findLocationTextBox.SetText(parent);
}
//clicks the go find location button
function clickGoFindControl(){
  Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.content.searchTabPanel.findLocationGoControl.ClickButton();
  CreateNewProjectPage.waitUntilLocationFindFinishes();
}
//returns search tab panel
function getSearchTabPanel(){
  return Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.content.searchTabPanel;
}
//returns the search tab(searchTabPanel title)
function getSearchTab(){
  return Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.searchTab;
}
//find results panel
function getSearchResultsPanel(){
  return Aliases.browser.pageCreateNewProject.popupWorkTreeLocation.container.content.searchTabPanel.findLocationResultsPanel;
}
//wait until the find operation finishes or timeOut
function waitUntilLocationFindFinishes(){
  CreateNewProjectPage.getSearchTabPanel().WaitAliasChild(CreateNewProjectPage.LOCATION_FIND_RESULT_PANEL_NAME,CreateNewProjectPage.MAX_WAIT_TIME);
  CreateNewProjectPage.getSearchTabPanel().WaitSlApplication(1000);//wait 1 second more
  CreateNewProjectPage.getSearchTabPanel().Refresh();
}