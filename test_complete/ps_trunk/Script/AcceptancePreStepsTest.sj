//USEUNIT Logging
//USEUNIT User
//USEUNIT Assert
function runTest(adminLogin, adminPass, name, lastName, email){  
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184389
  Assert.assertValue(true, inviteUserTest(name, lastName, email), "TC #1184389: Testing the creation of a new User.");
  Logging.doLogOut();//admin user logout
}
function inviteUserTest(name, lastName, email){
  return User.inviteNewUser(name, lastName, email);
}