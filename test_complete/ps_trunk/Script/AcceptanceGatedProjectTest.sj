//USEUNIT Logging
//USEUNIT CreateNewProject
//USEUNIT Logger
//USEUNIT Assert
//USEUNIT WorkEditDetails

function runTest(adminLogin, adminPass, projectName, parentProjectName){  
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184382
  Assert.assertValue(true, gatedProjectCreationTest(projectName, parentProjectName), "TC #1184382: Creating a gated project.");
  //TC #1184383
  Assert.assertValue(true, assignApprovalsRolesTest(projectName, parentProjectName), "TC #1184383: Assign Approvals Roles to a gated project.");
  Logging.doLogOut();
}
function gatedProjectCreationTest(name, parentProjectName){
  return (null!=CreateNewProject.createNewGatedDMAICProject(name, parentProjectName,false)?true:false);
}
function assignApprovalsRolesTest(projectName, user, role){
  if(isProjectXOpen(projectName)){
    if(WorkEditDetails.addUsersToWorkTeamMembers(projectName, [user], [role])){
      WorkEditDetails.openPageFromLink();
      if(WorkEditDetails.verifyUsersAndRolesOnAllTeamMembersListPanel([user], [role])){
        
      
      
      
      
      
      
      }else{
        return false;
      }
    }else{
      return false;
    }
  }else{
    Logger.logWarning("Project '"+projectName+"' NOT opened on assignApprovalsRolesTest.");
    return false;
  }
}