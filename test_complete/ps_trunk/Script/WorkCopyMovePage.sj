//USEUNIT ProjectNavigationMenu
//USEUNIT WorkSummaryPage
var PAGE_IDENTIFIER="/project/CopyMove";

function getNewLocationComponent(){
  return Aliases.browser.pageCopyMove.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formPsForm.panelClearFixStep.panelBoxPadded.tableSimple.newLocationCell.panelSearchBrowse.panelClearFix.panelPopupParentShow;
}
function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageCopyMove);
}
function pageTitleContainsMoveText(){
  return HeaderPanel.pageTitleContainsText(" : Move");
}
function isPageOpen(){ //returns wheter the page is open
  return (Page.isPageOpenAndHasPermission(PAGE_IDENTIFIER) && WorkCopyMovePage.pageTitleContainsMoveText());
}
function getMoveControl(){
  return Aliases.browser.pageCopyMove.panelBodyContainer.panelPageContent.panelContent.panelContainerClearfix.formPsForm.panelBottom.panelBoxCenter.moveControl;
}
function clickMoveControl(){
  WorkCopyMovePage.getMoveControl().Click();
  WorkSummaryPage.wait();
}
//opens a Copy / Move page by link
function openPageFromLink(link){
  if(null==link){
    ProjectNavigationMenu.getCopyMoveControl().Click();
  }else{
    link.Click();
  }
  WorkCopyMovePage.wait();
  return WorkCopyMovePage.isPageOpen();
}