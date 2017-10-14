//USEUNIT ToolBarLinks
function isProjectNameEqualsTo(pName){
  if(ToolBarLinks.existsProjectName()){
    if(!aqString.Compare(pName,ToolBarLinks.getProjectName().contentText,true)){
      return true;
    }else{
      return false;
    }
  }else{
    Log.Error("Project name was NOT found. ToolBar NOT displayed");
    return false;
  }
}