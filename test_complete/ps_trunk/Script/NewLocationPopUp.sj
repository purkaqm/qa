//Component to select a partent project or project's new location
function clickGoControl(){
  return Aliases.browser.dialogPopupParent.panelDijitDialogPaneContent.panelDlgSearchBrowse.paneTabcontainer.panelWapper.panelSearchTabPopupParentShow.tableSimpleMiddle.searchCell.goControl.Click();
}
function getSearchTab(){
  return Aliases.browser.dialogPopupParent.panelDijitDialogPaneContent.panelDlgSearchBrowse.paneTabcontainer.paneSearchTab;
}
function setSearchTextBox(newLocation){
  return Aliases.browser.dialogPopupParent.panelDijitDialogPaneContent.panelDlgSearchBrowse.paneTabcontainer.panelWapper.panelSearchTabPopupParentShow.tableSimpleMiddle.searchCell.parentSearchTextBox.SetText(newLocation);
}
function getOneMomentPleasePanel(){
  return Aliases.browser.dialogPopupParent.panelDijitDialogPaneContent.panelDlgSearchBrowse.paneTabcontainer.panelWapper.panelSearchTabPopupParentShow.panelResultsDiv.textNodeOneMomentPlease;
}
function getSearchResultsPanel(){
  return Aliases.browser.dialogPopupParent.panelDijitDialogPaneContent.panelDlgSearchBrowse.paneTabcontainer.panelWapper.panelSearchTabPopupParentShow.panelResultsDiv;
}
function waitToSearchResults(){
  NewLocationPopUp.getSearchResultsPanel().WaitChild("firstResult");
}