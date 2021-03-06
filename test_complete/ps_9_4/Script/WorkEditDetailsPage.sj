//USEUNIT ToolBarLinks
//USEUNIT Browser 
//USEUNIT Page
//USEUNIT WorkPage
  var _url=null;
  var PAGE_IDENTIFIER="project/EditWork";
  var ADD_TM_LEFT_TITLE_PANEL_HEIGHT=30;  //add team member/ left panel-title panel height
  var ADD_TM_LEFT_FIND_PANEL_HEIGHT=57;   //add team member/ left panel-find panel height
  var ADD_TM_LEFT_RESULT_BORDER_HEIGHT=5; //add team member/ left panel-result panel top border height
  var ADD_TM_LEFT_RESULT_ITEM_HEIGHT=21;  //add team member/ left panel-result items height
  var ADD_TM_LEFT_RESULT_ITEM_WIDTH=265;  //add team member/ left panel-result items width
  var ADD_TM_CENTER_ITEM_HEIGHT=27;       //add team member/ center panel-items = title height.
  var ADD_TM_DISTANCE_BETWEEN_LEFT_PANE_CENTER_AND_CENTER_PANE_CENTER=350; //add team member/ distance in px between the left pane center (member search) and the center pane center (roles)
  var SELECT_IN_GROUPS_VALUE="Groups, and show Members";
  var SELECT_IN_PEOPLE_VALUE="All people";
  var MAX_WAIT_TIME=60000;//max wait time in ms
  var FIRST_FIND_RESULT_NAME="textnode1";//first result textnode mapped name
    function wait(){//Waits until the browser loads the page.
      Page.wait(Aliases.browser.pageEditDetails);
    }
    
    function openPageFromUrl(reOpen){//navigates to EditDetails page
      Page.openFromUrl(Aliases.browser.pageEditDetails, _url, reOpen, WorkEditDetailsPage.isPageOpen());
      return WorkEditDetailsPage.isPageOpen();
    }
    
    function isPageOpen(){ //returns wheter the page is open
     return WorkPage.isPageOpenAndHasPermission(PAGE_IDENTIFIER); //if the current url contains "project/EditWork"
    }
    
//click Options->Edit_Details link and stores the new page url
function openPageFromLink(){
  ToolBarLinks.getWorkOptionsSubItem(ToolBarLinks.WO_EDIT_DETAILS).Click();
  WorkEditDetailsPage.wait();
  _url=Page.getUrl(WorkEditDetailsPage.isPageOpen());
  Log.Message("Setting EditDetailsPage URL = '"+_url+"'.");
}


//gets the description text area of edit details page 
function getDescriptionTextArea(){
  return Aliases.browser.pageEditDetails.panelContent.workDescriptionTextArea;
}
//sets the description text area of edit details page 
function setDescriptionTextArea(description){
  WorkEditDetailsPage.getDescriptionTextArea().select();
  WorkEditDetailsPage.getDescriptionTextArea().Keys(description);
}
//clicks the top save changes button
function clickSaveChanges(){
  Aliases.browser.pageEditDetails.saveChangesControl.ClickButton();
  WorkEditDetailsPage.wait();
}
//TeamMembersPanel- returns the panel(center panel) that contains the roles list on Team member Panel
function getRolesDropPane(){
  return Aliases.browser.pageEditDetails.teamMembersPanel.centerPanel.rolesPane2.rolesDropPane;
}
//TeamMembersPanel-returns the panel(left panel) that contains the search results panel
function getSearchResultsPanel(){
  return Aliases.browser.pageEditDetails.teamMembersPanel.leftPanel.teamMemberSearchResultPane;
}
//TeamMembersPanel-returns the "display" text box 
function getDisplayTeamMembers(){
  return Aliases.browser.pageEditDetails.teamMembersPanel.rightPanel.displayTextBox;
}
//TeamMembersPanel-sets "display" text textField
function setDisplayTeamMembersText(role){
  WorkEditDetailsPage.getDisplayTeamMembers().SetText(role);//Sets the role name
}
//TeamMembersPanel-selects "display" text textField text
function selectTextOfDisplayTeamMembersText(role){
  WorkEditDetailsPage.getDisplayTeamMembers().select();//Selects the text
}
//TeamMembersPanel-returns the panel(right panel) that contains the selected team members
function getSelectedRolesAndUsersList(){
  return Aliases.browser.pageEditDetails.teamMembersPanel.rightPanel.rolesAndUsersListPanel;
}
//TeamMembersPanel-sets "Find" text textField
function setFindTeamMembersText(name){
  Aliases.browser.pageEditDetails.teamMembersPanel.leftPanel.findTeamMemberTextBox.SetText(name);//Sets the user/group name
}
//TeamMembersPanel- clicks the go find button
function clickGoFindControl(){
  Aliases.browser.pageEditDetails.teamMembersPanel.leftPanel.goControl.ClickButton();
  WorkEditDetailsPage.waitUntilTeamMemberFindFinishes();
}
//TeamMembersPanel- selects the desired value from the "In" drop down list located on the TM left pane
function selectInSelector(item){
  Aliases.browser.pageEditDetails.teamMembersPanel.leftPanel.selectSearchIn.ClickItem(item);//Selects the desired item
}
//wait until the find operation finishes or timeOut
function waitUntilTeamMemberFindFinishes(){
  WorkEditDetailsPage.getSearchResultsPanel().WaitAliasChild(WorkEditDetailsPage.FIRST_FIND_RESULT_NAME,WorkEditDetailsPage.MAX_WAIT_TIME);
  WorkEditDetailsPage.getSearchResultsPanel().WaitSlApplication(2000);//wait 2 seconds more
  WorkEditDetailsPage.getSearchResultsPanel().Refresh();
  WorkEditDetailsPage.getSearchResultsPanel().Child(0).Refresh();

}