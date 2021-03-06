//USEUNIT Logging
//USEUNIT CreateNewProject
//USEUNIT Logger
//USEUNIT Assert
//USEUNIT WorkSummary
//USEUNIT WorkEditDetails
//USEUNIT ProjectNavigationMenu
//USEUNIT WorkCopyMove
//USEUNIT WorkDelete
//USEUNIT QuickSearch
function runTest(adminLogin, adminPass, projectName, descendantName, parentProjectName, users, roles){  
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184376
  Assert.assertValue(true, projectCreationTest(projectName), "TC #1184376: Creating a project.");
  //TC #1184378
  Assert.assertValue(true, addDescendantTest(projectName, descendantName), "TC #1184378: Adding descendants.");
  //TC #1184379
  Assert.assertValue(true, addRolesTest(projectName, users, roles), "TC #1184379: Adding User Roles.");
  //TC #1184380
  Assert.assertValue(true, copyMoveProjectTest(projectName, parentProjectName), "TC #1184380: Copy/Move Project.");
  //TC #1184381
  Assert.assertValue(true, removeWorkTest(projectName, [parentProjectName]), "TC #1184381: Removing Project.");
  Logging.doLogOut();
}
function projectCreationTest(name){
  return (null!=CreateNewProject.createNewProject(name,null,false)?true:false);
}
function addDescendantTest(projectName, descendantName){
  if(isProjectXOpen(projectName)){
    return (null!=CreateNewProject.createNewDescendant(descendantName, false, CreateNewProjectPage.PROJECT_TYPE)?true:false);
  }else{
    Logger.logWarning("Project '"+projectName+"' NOT opened on addDescendantTest.");
    return false;
  }
}
function addRolesTest(projectName, users, roles){
  if(isProjectXOpen(projectName)){
    if(WorkEditDetails.addUsersToWorkTeamMembers(projectName, users, roles)){
      WorkEditDetails.openPageFromLink();
      if(WorkEditDetails.verifyUsersAndRolesOnAllTeamMembersListPanel(users, roles)){
        return true;
      }else{
        return false;
      }
    }else{
      return false;
    }
  }else{
    Logger.logWarning("Project '"+projectName+"' NOT opened on addRolesTest.");
    return false;
  }
}
function copyMoveProjectTest(projectName, targetProject){
  if(isProjectXOpen(projectName)){
    WorkCopyMove.openPageFromLink(null);
    return WorkCopyMove.setNewLocation(targetProject);
  }else{
    Logger.logWarning("Project '"+projectName+"' NOT opened on copyMoveProjectTest.");
    return false;
  }
}
//deletes a work
function removeWorkTest(pName, WTParents){
  if(isProjectXOpen(pName)){
    if(WorkDelete.deleteWork(pName, true, WTParents)){
      if(QuickSearch.searchProject(pName)){
        Log.Message("Work deletion fails. Project could be found after deletion.");
        return false;
      }else{
        Log.Message("Work deletion passed. Project not found after deletion.");
        return true;
      }
    }else{
      Log.Message("Work deletion fails.");
      return false;
    }
  }else{
    Logger.logWarning("Project '"+projectName+"' NOT opened on removeWorkTest.");
    return false;
  }
}