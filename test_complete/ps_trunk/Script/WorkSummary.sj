//USEUNIT WorkSummaryPage

//Opens a project by direct url
function openPageFromUrl(reOpen){
  return WorkSummaryPage.openPageFromUrl(reOpen);
}
//opens a work by link
function openPageFromLink(wLink){
  return WorkSummaryPage.openPageFromLink(wLink);
}
function isProjectNameEqualsTo(pName){
  return WorkSummaryPage.isProjectNameEqualsTo(pName);
}
function isParentProjectNameEqualsToX(parentName){
  var parent=WorkSummaryPage.getParentProject();
  if(parent.Exists){
    if(!aqString.Compare(parentName,parent.contentText,true)){
      return true;
    }else{
      return false;
    }
  }else{
    Log.Error("Parent Project '"+parentName+"' does NOT exist.");
    return false;
  }
}