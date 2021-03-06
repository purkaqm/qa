//USEUNIT Random
//USEUNIT Panel
//USEUNIT Page
//USEUNIT CreateNewProjectPage
//USEUNIT WorkSummaryPage
//USEUNIT NewLocationSelection
//returns a new Project Name
function getNewProjectName(preffix){
  return preffix+"_"+Random.randomInt();
}
//creates a project and if successful returns the name and null if not
function createNewProject(pName, parent, autoSuffix){
  CreateNewProjectPage.openPageFromLink();
  return CreateNewProject.createNewWork(CreateNewProjectPage.PROJECT_TYPE, pName, parent, autoSuffix, false);
}
//creates a gated project and if successful returns the name and null if not
function createNewGatedDMAICProject(pName, parent, autoSuffix){
  CreateNewProjectPage.openPageFromLink();
  return CreateNewProject.createNewWork(CreateNewProjectPage.GATED_DMAIC_PROJECT_TYPE, pName, parent, autoSuffix, false);
}
//adds a descendant and if successful returns the name and null if not
function createNewDescendant(dName, autoSuffix, descendantType){
  CreateNewProjectPage.openPageFromLink(WorkSummaryPage.getAddNewDescendantControl());
  return CreateNewProject.createNewWork(descendantType, dName, null, autoSuffix, true);
}
//creates a work and if successful returns the name and null if not
//type=work type. wName=workName. parent=parent name(null = without parent), autoSuffix = boolean
function createNewWork(type, wName, parent, autoSuffix, isDescendant){
  var name=wName;
  if(autoSuffix){
    name=CreateNewProject.getNewProjectName(wName);
  }
  CreateNewProjectPage.selectProjectType(type);
  CreateNewProjectPage.clickContinueControl();
  CreateNewProjectPage.setProjectName(name);
  if(null != parent){
    CreateNewProjectPage.clickOpenFindLocationPopup();
    NewLocationSelection.selectNewLocation(parent);
  }
  CreateNewProjectPage.clickFinishControl();
  WorkSummaryPage.setPageUrl();
  if(WorkSummaryPage.isProjectNameEqualsTo(name)){
    Log.Message("Project created. Name = '"+name+"'.");
    return name;
  }else{
    if(isDescendant){
      if(WorkSummaryPage.getDescendantsBodyPanel().FindChild(["ObjectType","contentText"], ["Link",name], 100, true).Exists){
        Log.Message("Descendant created. Name = '"+name+"'.");
        return name;
      }else{
        Log.Message("Descendant NOT created. Name = '"+name+"'.");
        return null;
      }
    }else{
      Log.Message("Project NOT created. Name = '"+name+"'.");
      return null;
    }
  }
}