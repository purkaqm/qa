//USEUNIT WorkCopyMovePage
//USEUNIT NewLocationSelection
//USEUNIT WorkSummary
function setNewLocation(targetProject){
  WorkCopyMovePage.getNewLocationComponent().Click();
  NewLocationSelection.selectNewLocation(targetProject)
 /* NewLocationPopUp.getSearchTab().Click();
  NewLocationPopUp.setSearchTextBox(targetProject);
  NewLocationPopUp.clickGoControl();
  NewLocationPopUp.waitToSearchResults();
  var parent=NewLocationPopUp.getSearchResultsPanel().FindChild(["ObjectType","className","ContentText"], ["Panel","link",targetProject], 100, true);
  if(parent.Exists){
    parent.Click();
    WorkCopyMovePage.getNewLocationComponent().WaitSlApplication(1000);
  }*/
  if(!aqString.Compare(targetProject,WorkCopyMovePage.getNewLocationComponent().contentText,true)){
    WorkCopyMovePage.clickMoveControl();
    return WorkSummary.isParentProjectNameEqualsToX(targetProject);
  }else{
    Log.Warning("'"+targetProject+"'"+" could not be selected as New location.");
    return false;
  }
}
//opens a Copy / Move page by link
function openPageFromLink(link){
  return WorkCopyMovePage.openPageFromLink(link);
}
function moveProject(targetProject){
  WorkCopyMove.setNewLocation(targetProject);
}
