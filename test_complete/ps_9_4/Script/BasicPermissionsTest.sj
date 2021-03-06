//USEUNIT Logging
//USEUNIT ViewPermissionTest
//USEUNIT EditPermissionTest
//USEUNIT EditPermissionsPermissionTest
//USEUNIT ViewPermissionsPermissionTest
//USEUNIT DeletePermissionTest
//USEUNIT DefineCategoriesTable
//USEUNIT HomePage
//USEUNIT Browser
//USEUNIT CreateNewProject
//USEUNIT WorkEditPermissions
//USEUNIT WorkEditDetails
//USEUNIT WorkUpdateDocument
//USEUNIT WorkDocuments
//USEUNIT AddDocumentsPermissionTest
//USEUNIT ViewDocumentPermissionTest
//USEUNIT EditDocumentPermissionTest

//this variables could be stored in a xml file or something like that. A issue for a near future.
var NEW_PROJECT_PREFFIX="PAProject";
var ADMIN_USER_LOGIN="PAUser1";
var ADMIN_USER_PASS="123a";
var USER_LOGIN="PAUser2";
var USER_PASS="123a";
var USER_NAME="User2_Last, User2_Name";//"LastName, Name"	  
var USER_GROUP_NAME="PAGroup";
var CATEGORY_NAME="PACat";
var CATEGORY_NONE="PANone";
var CORE_SET_EVERYBODY="Everybody";
var CORE_SET_LEVEL_HIGHER="Higher";
var CORE_SET_LEVEL_PROJECT="Project";
var CORE_SET_LEVEL_ALL="All";
var BROWSER="firefox";//"iexplore" or "firefox"
var WORK_TREE_PARENTS=null;
var ROLE_CONTRIBUTORS="Contributors";
var BASE_URL="http://192.168.0.60:8080/QARepTest";
var DOC_TITLE="PADocument";
var DOC_URL="url1";
var DOC_FILE_NAME="file.txt"
var DOC_FILE_PATH="C:/";
var VIEW="VIEW", EDIT="EDIT", DELETE="DELETE", EDIT_PERM="EDIT PERMISSIONS", VIEW_PERM="VIEW PERMISSIONS", VIEW_DOC="VIEW DOCUMENTS", ADD_DOC="ADD DOCUMENT", EDIT_DOC="EDIT DOCUMENT";//permission names/verbs
var cat1=[[VIEW,true], [EDIT,true],  [DELETE,false],[VIEW_PERM,true], [EDIT_PERM,true],[VIEW_DOC,true],[ADD_DOC,true],[EDIT_DOC,true]];  //VIEW_P - EDIT_P - EDIT_PERM_P - DELETE_N - VIEW_DOC_P - ADD_DOC_P - EDIT_DOC_P
var cat2=[[VIEW,true], [EDIT,false], [DELETE,false],[VIEW_PERM,false],[EDIT_PERM,false],[VIEW_DOC,false],[ADD_DOC,false],[EDIT_DOC,false]]; //VIEW_PERM_N - VIEW_DOC_N
var cat3=[[VIEW,false],[EDIT,false], [DELETE,false],[VIEW_PERM,false],[EDIT_PERM,false],[VIEW_DOC,false],[ADD_DOC,false],[EDIT_DOC,false]]; //VIEW_N
var cat4=[[VIEW,true], [EDIT,false], [DELETE,true], [VIEW_PERM,true], [EDIT_PERM,false],[VIEW_DOC,true],[ADD_DOC,false],[EDIT_DOC,false]]; //VIEW_P - EDIT_N - VIEW_PERM_P - EDIT_PERM_N - ADD_DOC_N - EDIT_DOC_N - DELETE_P

//opens a browser, logs in and creates a project and returns it name
function init(){
  Browser.closeAllOpenBrowsers();//closes all open browsers before start testing.
  Browser.setBrowser(BROWSER);
  HomePage.setHomeUrl(BASE_URL+"/Home.page");
  Browser.navigateToPageInNewBrowser(HomePage.getHomeUrl());
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  //create a new project
  Log.Message("CREATING A NEW PROJECT");
  var projectName=CreateNewProject.createNewProject(NEW_PROJECT_PREFFIX, null);
  if(null==projectName){
    Log.Error("Project was NOT created.");
    return null;
  }else{
    return projectName;
  }
}
function finish(){
  Browser.closeCurrentBrowser();//close the browser
}
//sets project everybody core set and test everybody user permissions.
function runCoreSetEverybodyTests(){
  var projectName=init();
  if(!WorkEditPermissions.editWorkCoreSet(projectName, CORE_SET_EVERYBODY,CORE_SET_LEVEL_ALL,CATEGORY_NAME)){//set core set everybody to the desired category
    Log.Error("Project core set was NOT seted.");
  }
  BasicPermissionsTest.runTestSteps(projectName, WORK_TREE_PARENTS, true);
  finish();
}
//permissions tested with an user added to custom set.
function runUserCustomSetTests(){
  customSetTests(USER_NAME);
}
//permissions tested with a group added to custom set.
function runGroupCustomSetTests(){
  customSetTests(USER_GROUP_NAME);
}
//adds user/group to project custom sets and test it permissions.
function customSetTests(name){
  var projectName=init();
  if(!WorkEditPermissions.editWorkCoreSet(projectName, CORE_SET_EVERYBODY,CORE_SET_LEVEL_ALL,CATEGORY_NONE)){//set core set everybody to none category.
    Log.Error("Project core set was NOT seted.");
  }
  Log.Message("TESTING CUSTOM SET +name+ WITH PERMISSIONS.");
  if(!WorkEditPermissions.addWorkCustomSet(projectName, name, CATEGORY_NAME)){//add user to work custom set and sets the desired category
    Log.Error("Work custom set was NOT seted.");
  } 
  BasicPermissionsTest.runTestSteps(projectName, WORK_TREE_PARENTS, true);
  finish();
}

//core set higher and project permission tested with a user added to team members.
function runUserCoreSetHigherAndProjectTests(){
  coreSetHigherAndProjectTests(USER_NAME, true);
}
//core set higher and project permission tested with a group added to team members.
function runGroupCoreSetHigherAndProjectTests(){
  coreSetHigherAndProjectTests(USER_GROUP_NAME, false);
}
//adds a group or user to project team members and runs the permissions test with project and higher perms.
//name=user or group name. isUser=true when name=user name and false if name=group name.
function coreSetHigherAndProjectTests(name, isUser){
  var projectName=init();
  Log.Message("TESTING CORE SET USER: CONTRIBUTORS - LEVEL: PROJECT  WITH PERMISSIONS.");
  if(!WorkEditPermissions.editWorkCoreSet(projectName, CORE_SET_EVERYBODY, CORE_SET_LEVEL_ALL, CATEGORY_NONE)){//set core set everybody to none category.
    Log.Error("Project core set was NOT seted.");
  }
  if(!WorkEditPermissions.editWorkCoreSet(projectName, ROLE_CONTRIBUTORS,CORE_SET_LEVEL_PROJECT,CATEGORY_NAME)){//set permissions to core set Project level
    Log.Error("Project core set was NOT seted.");
  }
  if(isUser){//if the name is a user name
    if(!WorkEditDetails.addUserToWorkTeamMember(projectName, name, ROLE_CONTRIBUTORS)){//add a user to team members
      Log.Error("New team member was NOT seted.");
    }
  }else{
    if(!WorkEditDetails.addGroupToWorkTeamMember(projectName, name, ROLE_CONTRIBUTORS)){//add a group to team members
      Log.Error("New team member was NOT seted.");
    }
  }
  BasicPermissionsTest.runTestSteps(projectName, WORK_TREE_PARENTS, false);
  
  Log.Message("TESTING CORE SET USER: CONTRIBUTORS STARTS. ALL PERMISSIONS WILL BE RETRIEVED FIRST.");
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  if(!WorkEditPermissions.editWorkCoreSet(projectName, ROLE_CONTRIBUTORS,CORE_SET_LEVEL_PROJECT,CATEGORY_NONE)){//set core set everybody to none category.
    Log.Error("Project core set was NOT seted.");
  }
  Log.Message("CREATING CHILD PROJECT");
  var childProjectName=CreateNewProject.createNewProject(NEW_PROJECT_PREFFIX, projectName);//sets projectName as Parent
  if(null==childProjectName){
    Log.Error("Child Project was NOT created.");
  }
  if(!WorkEditPermissions.editWorkCoreSet(childProjectName, ROLE_CONTRIBUTORS, CORE_SET_LEVEL_HIGHER, CATEGORY_NONE)){//set None permissions to core set Higher level.
    Log.Error("Project core set was NOT seted.");
  }
  Log.Message("TESTING CORE SET USER: CONTRIBUTORS - LEVEL: HIGHER WITH PERMISSIONS. WITH CHILD PROJECT DELETION BEFORE FINISH.");
  if(!WorkEditPermissions.editWorkCoreSet(childProjectName, ROLE_CONTRIBUTORS,CORE_SET_LEVEL_HIGHER,CATEGORY_NAME)){//set permissions to core set Higher level
    Log.Error("Project core set was NOT seted.");
  }
  BasicPermissionsTest.runTestSteps(childProjectName, null, true);//run test steps and deletes child project before finishing. parents=null because user has not view permission on Parent Project and it will appear on WorkTree rool, not under it parent project.
  finish();    
}
//runs all the permission tests
function main(){
  runCoreSetEverybodyTests();
  runUserCoreSetHigherAndProjectTests();
  runGroupCoreSetHigherAndProjectTests();
  runUserCustomSetTests();
  runGroupCustomSetTests();
}
//run the permission test steps
//deleteWork=try to delete the work before finishing this method
function runTestSteps(project, parents, deleteWork){
  Log.Message("TEST STEPS STARTS.");
  //category 1
  if(DefineCategoriesTable.defineWorkItemPermissionsFromCategoryRole(CATEGORY_NAME,cat1)){
    Logging.doLogOut();//admin logout
    Logging.doLogin(USER_LOGIN, USER_PASS); //user login
     ViewPermissionTest.testViewPermissionPositive(project, parents);
     EditPermissionTest.testEditPermissionPositive(project);
     EditPermissionsPermissionTest.testEditPermissionsPositive(project);
     DeletePermissionTest.testDeletePermissionNegative();
     ViewDocumentPermissionTest.testViewDocumentPermissionPositive1(project);
     AddDocumentsPermissionTest.testAddDocumentsPermissionPositive(project, DOC_TITLE, DOC_URL, null, null);//creates document1     EditDocumentPermissionTest.testEditDocumentPermissionPositive(project, DOC_TITLE, DOC_URL, null, null);//Edits it
     AddDocumentsPermissionTest.testAddDocumentsPermissionPositive(project, DOC_TITLE+"_2", null, DOC_FILE_NAME, DOC_FILE_PATH);//creates document2
     EditDocumentPermissionTest.testEditDocumentPermissionPositive(project, DOC_TITLE+"_2", null, DOC_FILE_NAME, DOC_FILE_PATH);//Edits it
     ViewDocumentPermissionTest.testViewDocumentPermissionPositive2(project, DOC_TITLE);//verifies newsly added document's prensence.
    Logging.doLogOut(); //user logout
  }else{
    Log.Error("Category Role #1 Permissions cold NOT be setted");
  }
  //category 2 view_permission_NEGATIVE
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  if(DefineCategoriesTable.defineWorkItemPermissionsFromCategoryRole(CATEGORY_NAME,cat2)){
    Logging.doLogOut();//admin logout
    Logging.doLogin(USER_LOGIN, USER_PASS); //user login
    ViewPermissionsPermissionTest.testViewPermissionsNegative();
    ViewDocumentPermissionTest.testViewDocumentPermissionNegative(project);
    Logging.doLogOut(); //user logout
  }else{
    Log.Error("Category Role #2 Permissions cold NOT be setted");
  }
  //category 3 = none
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  if(DefineCategoriesTable.defineWorkItemPermissionsFromCategoryRole(CATEGORY_NAME,cat3)){
    Logging.doLogOut();//admin logout
    Logging.doLogin(USER_LOGIN, USER_PASS); //user login
    ViewPermissionTest.testViewPermissionNegative(project, parents);
    Logging.doLogOut(); //user logout
  }else{
    Log.Error("Category Role #3 Permissions cold NOT be setted");
  }
  //category 4
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  if(DefineCategoriesTable.defineWorkItemPermissionsFromCategoryRole(CATEGORY_NAME,cat4)){
    Logging.doLogOut();//admin logout
    Logging.doLogin(USER_LOGIN, USER_PASS); //user login
     //ViewPermissionTest.testViewPermissionPositive(project, parents);
     EditPermissionTest.testEditPermissionNegative(project);
     ViewPermissionsPermissionTest.testViewPermissionsPositive();
     EditPermissionsPermissionTest.testEditPermissionsNegative();
     AddDocumentsPermissionTest.testAddDocumentsPermissionNegative(project);
     EditDocumentPermissionTest.testEditDocumentPermissionNegative(project, DOC_TITLE);
     DeletePermissionTest.testDeletePermissionPositive(project, deleteWork, parents);
    Logging.doLogOut(); //user logout
  }else{
    Log.Error("Category Role #4 Permissions cold NOT be setted");
  }
}