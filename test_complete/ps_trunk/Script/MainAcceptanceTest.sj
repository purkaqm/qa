//USEUNIT AcceptanceNavigationMenuAndUITest
//USEUNIT AcceptanceUserTest
//USEUNIT AcceptancePreStepsTest
//USEUNIT AcceptanceSearchFunctionTest
//USEUNIT AcceptanceProjectTest
//USEUNIT AcceptanceGatedProjectTest
//USEUNIT Work
//these test cases belong to the test suite: "https://upland.testrail.com/index.php?/suites/view/770"
function runTest(){
  var randomId=Random.randomInt();
  var name="UserName_"+randomId;
  var lastName="UserLast_"+randomId;
  var email=randomId+"_testemail@test.comx";
  var login="TestNewUser_"+randomId;
  var defaultProject="TestAutomationProject";
  var projectName="AATestProject_"+randomId;
  var gatedProjectName="AATestGatedProject_"+randomId;
  //initial steps
  Browser.closeAllOpenBrowsers();//closes all open browsers before start testing.
  Browser.setBrowser(AcceptanceSettings.BROWSER);
  HomePage.setHomeUrl(AcceptanceSettings.BASE_URL+"/Home.page");
  Browser.navigateToPageInNewBrowser(HomePage.getHomeUrl());
  //Pre Steps start here with TC #1184389
  /*AcceptancePreStepsTest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_PASS, name, lastName, email);
  //TC #1184452 starts
  AcceptanceNavigationMenuAndUITest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_PASS);  
  //TC #1184390 starts
  AcceptanceUserTest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_NAME, AcceptanceSettings.ADMIN_USER_LAST_NAME, AcceptanceSettings.ADMIN_USER_PASS,name,lastName,email,login,"12345a",["Administrators"]);
  //TC #1184456 starts
  AcceptanceSearchFunctionTest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_PASS, defaultProject);
  //TC #1184376 starts
  AcceptanceProjectTest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_PASS, projectName, AcceptanceSettings.DESCENDANT_PROJECT_NAME, AcceptanceSettings.DEFAULT_PROJECT_NAME,[AcceptanceSettings.ADMIN_USER_LAST_NAME+", "+AcceptanceSettings.ADMIN_USER_NAME, AcceptanceSettings.SINGLE_USER_LAST_NAME+", "+AcceptanceSettings.SINGLE_USER_NAME],[AcceptanceSettings.USER_ROLE_1,AcceptanceSettings.USER_ROLE_2]);
  *///TC #1184382 starts
  AcceptanceGatedProjectTest.runTest(AcceptanceSettings.ADMIN_USER_LOGIN, AcceptanceSettings.ADMIN_USER_PASS, gatedProjectName, AcceptanceSettings.MY_COMPANY_NAME);
}