//USEUNIT Assert
//USEUNIT HeaderPanel
//USEUNIT Logger
//USEUNIT Logging
//USEUNIT PersonMenu
//USEUNIT PersonSettingsProfile
//USEUNIT PersonUserProfile
//USEUNIT QuickSearch
//USEUNIT ProfileMenuController
//USEUNIT PersonMenuController
//USEUNIT SearchUserDijit
//USEUNIT ProxyUser
//USEUNIT Random
//USEUNIT User
function runTest(adminLogin, adminName, adminLastName, adminPass, name, lastName, email, login, newPass, groups){
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184389 and the first part of TC #1184391(opening the More... link):
  //Assert.assertValue(true, inviteUserTest(name, lastName, email), "TC #1184389: Testing the creation of a new User.");
  //TC #1184390:
  HeaderPanel.getTitle().WaitSlApplication(10000);
  Assert.assertValue(true, quickSearchUserTest(lastName+", "+name), "TC #1184390: Testing search an user.");
  //TC #1184391(Once User Profile page is open):
  Assert.assertValue(true, userProfilePageTest(), "TC #1184391: Opening User profile page from More... link");
  //TC #1184392 and #1184393
  Assert.assertValue(true, editUserTest(login, newPass, groups), "TC #1184392 and #1184393: Editing an User.");
  //TC #1184393
  Logging.doLogOut();//admin user logout
  Logging.doLogin(login, newPass); //new user login
  Assert.assertValue(true, newUserLoginTest(name, lastName), "TC #1184393: Log in as Newly added User and verify Admin Permissions.");
  Logging.doLogOut();
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184394:
  Assert.assertValue(true, proxyUserTestSetUp(name, lastName), "TC #1184394: Testing proxy user set-up.");
  Logging.doLogOut();
  Logging.doLogin(login, newPass);
  Assert.assertValue(true, proxyUserLogIn(adminName, adminLastName), "TC #1184394: Testing proxy user Log-In.");
  Logging.doLogOut();
}
function proxyUserLogIn(name, lastName){
  return ProxyUser.switchUserToX(name, lastName)
}
function proxyUserTestSetUp(proxyName, proxyLastName){
  ProfileMenuController.clickProfileControl();
  PersonMenuController.clickEditUserTab();
  PersonSettingsProfile.clickProxyUserComboBox();
  SearchUserDijit.selectUsers([proxyLastName+", "+proxyName]);
  PersonSettingsProfile.clickSaveControl();
  return (PersonUserProfile.areProxyUsersSetToUser([proxyName+" "+proxyLastName]) && PersonUserProfilePage.isPageOpen() && PersonMenu.isUserProfileTabSelected());
}
/*function inviteUserTest(name, lastName, email){
  return User.inviteNewUser(name, lastName, email);
}*/
function editUserTest(login, pass, groups){//person page should be open before starting this test
  if(PersonMenu.getEditUserTab().Exists){
    PersonMenu.clickEditUserTab();
    PersonSettingsProfile.setUserName(login);
    PersonSettingsProfile.setNewPassword(pass);
    PersonSettingsProfile.setConfirmPassword(pass);
    PersonSettingsProfile.checkValuesFromUserGroupsComboBox(groups);
    PersonSettingsProfile.clickSaveControl();
    return (PersonUserProfile.areGroupsSetToUser(groups) && PersonUserProfilePage.isPageOpen() && PersonMenu.isUserProfileTabSelected());
  }else{
    Logger.logWarning("Edit User tab is not displayed on Person Menu.");
    return false;
  }
}
function newUserLoginTest(name, lastName){
   if(Header.isUserNameAndLastNameEqualsToX(name, lastName)){
     var adminIcon=IconMenu.getAdminControl();
     if(null != adminIcon){
       if(!adminIcon.Exists || !adminIcon.VisibleOnScreen){
         Log.Warning("Admin icon is not visible on Icon Menu.");
         return false;
       }else{
         return true;
       } 
     }else{
         Log.Warning("Admin icon is not displayed on Icon Navigation Menu.");
         return false;
     }
   }else{
     Logger.logWarning("Logged user does not match with the given user name = '"+name+" "+lastName+"'.")
     return false;
   }
}

function userProfilePageTest(){
  var res=true;
  if(!PersonMenu.getUserProfileTab().Exists){
    res=false;
    Logger.logWarning("User Profile tab is not displayed on Person's page Menu.");
  }
  if(!PersonMenu.getAgendaTab().Exists){
    res=false;
    Logger.logWarning("Agenda tab is not displayed on Person's page Menu.");
  }
  if(!PersonMenu.getDocumentsTab().Exists){
    res=false;
    Logger.logWarning("Documents tab is not displayed on Person's page Menu.");
  }
  if(!PersonMenu.getTimesheetsTab().Exists){
    res=false;
    Logger.logWarning("Timesheets tab is not displayed on Person's page Menu.");
  }
  if(!PersonMenu.getWorkloadTab().Exists){
    res=false;
    Logger.logWarning("Workload tab is not displayed on Person's page Menu.");
  }
  if(!PersonMenu.getEditUserTab().Exists){
    res=false;
    Logger.logWarning("Edit User tab is not displayed on Person's page Menu.");
  }
  return res;
}
function quickSearchUserTest(name){
  var user=QuickSearch.search(name, HeaderPanel.USER_SEARCH_TYPE,true);
  if(null != user){
    return PersonUserProfile.clickUserLinkToOpenUserProfilePage(user);
  }else{
    return false;
  }
}