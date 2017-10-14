function getSaveControl(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panelBottom.saveControl;
}
function getCancelControl(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panelBottom.cancelControl;
}
function getFindTextBox(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panel2.tableSimpleMiddle.findCell.findTextBox;
}
function getGoControl(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panel2.tableSimpleMiddle.findCell.goControl;
}
function getSearchResultsPanel(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panel2;
}
function getSelectedResultsPanel(){
  return Aliases.browser.dialogUserSearch.panelContent.panelPopUp.panel1.selectedPanel;
}
function setFindText(text){
  SearchUserDijitPopUp.getFindTextBox().setText(text);
}
function clickGoControl(){
  SearchUserDijitPopUp.getGoControl().ClickButton();
}
function clickSaveControl(){
  SearchUserDijitPopUp.getSaveControl().ClickButton();
}
function clickCancelControl(){
  SearchUserDijitPopUp.getCancelControl().ClickButton();
}