//USEUNIT Random
//USEUNIT Panel
//USEUNIT Page
//USEUNIT CreateNewProjectPage
//USEUNIT WorkSummaryPage
//USEUNIT ToolBar

//returns a new Project Name
function getNewProjectName(preffix){
  return preffix+"_"+Random.randomInt();
}
//creates a project and if successful returns the name and null if not
function createNewProject(preffix, parent){
  return CreateNewProject.createNewWork(CreateNewProjectPage.PROJECT_TYPE, preffix, parent);
}
//creates a work and if successful returns the name and null if not
//type=work type. preffix=workNamePreffix. parent=parent name(null = without parent)
function createNewWork(type, preffix, parent){
  var name=CreateNewProject.getNewProjectName(preffix);
  CreateNewProjectPage.openPageFromLink();
  CreateNewProjectPage.selectProjectType(type);
  CreateNewProjectPage.clickContinueControl();
  CreateNewProjectPage.setProjectName(name);
  if(null != parent){
    CreateNewProject.setLocationThroughSearch(parent);
  }
  CreateNewProjectPage.clickFinishControl();
  WorkSummaryPage.setPageUrl();
  if(ToolBar.isProjectNameEqualsTo(name)){
    Log.Message("Project created. Name = '"+name+"'.");
    return name;
  }else{
    Log.Message("Project NOT created. Name = '"+name+"'.");
    return null;
  }
}
function setLocationThroughSearch(parent){
  CreateNewProjectPage.clickOpenFindLocationPopup();
  CreateNewProjectPage.clickSearchTab();
  CreateNewProjectPage.setFindLocationTextBox(parent);
  CreateNewProjectPage.clickGoFindControl();
  var pItem=Panel.getSubObjectByNameTypeAndId(CreateNewProjectPage.getSearchResultsPanel(), parent, Page.OBJECT_TYPE_PANEL, null, true);//searches a textnode with contentText = parent
  if(null != pItem){
    Log.Message("Parent project found. Name = '"+parent+"'.");
    pItem.Click(pItem.Width/2,pItem.Height/2);//selects the parent project from find results
    CreateNewProjectPage.wait();//not really necessary to wait
    return true;
  }else{
    Log.Error("Parent project NOT found. Name = '"+parent+"'.");
    return false;
  }
}