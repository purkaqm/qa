//USEUNIT QuickSearch
//USEUNIT WorkSummaryPage
//USEUNIT Header
//verifies if project is opened an if not, searches and opensa that project
function openWork(pName){
  if(WorkSummaryPage.existsProjectName()){
    if(WorkSummaryPage.isProjectNameEqualsTo(pName)){
      return true;
    }
  }
  //if not returns
  if(Work.searchAndOpenWork(pName)){
    return true;
  }else{
    Log.Warning("Project "+pName+" NOT found or NOT opened.");
    return false;
  }
}
//searches a project by name and opens it
function searchAndOpenWork(pName){
  var work=QuickSearch.searchAll(pName);
  if(null!=work){//if found
    WorkSummaryPage.openPageFromLink(work);
    return WorkSummaryPage.isProjectNameEqualsTo(pName);
  }else{
    return false;
  }
}
function isProjectXOpen(pName){
  var title=HeaderPanel.getTitle();
  if(title.Exists){
    if(aqString.Find(title.contentText, pName+" : ",0,true) == 0){
      return true;
    }else{
      Log.Message("Text '"+pName+" : ' was NOT found on the title.");
      return false;
    }
  }else{
    Log.Message("Title was NOT found. Project Name NOT displayed");
    return false;
  }
}