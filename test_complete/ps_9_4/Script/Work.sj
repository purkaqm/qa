//USEUNIT WorkSummaryPage
//USEUNIT QuickSearch
//USEUNIT ToolBar
//verifies if project is opened an if not, searches and opensa that project
function openWork(pName){
  if(ToolBarLinks.existsProjectName()){
    if(ToolBar.isProjectNameEqualsTo(pName)){
      return true;
    }
  }
  //if not returns
  if(Work.searchAndOpenWork(pName)){
    return true;
  }else{
    Log.Error("Project "+pName+" NOT found or NOT opened.");
    return false;
  }
}
//searches a project by name and opens it
function searchAndOpenWork(pName){
  var work=QuickSearch.searchAll(pName);
  if(null!=work){//if found
    WorkSummaryPage.openPageFromLink(work);
    return ToolBar.isProjectNameEqualsTo(pName);//was work.contentText
  }else{
    return false;
  }
}