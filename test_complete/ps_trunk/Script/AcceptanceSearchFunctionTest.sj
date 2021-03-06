//USEUNIT Logging
//USEUNIT Header
//USEUNIT Logger
//USEUNIT Assert
//USEUNIT QuickSearch
//USEUNIT WorkSummary
//USEUNIT AdvancedSearch
function runTest(adminLogin, adminPass, projectName){  
  Logging.doLogin(adminLogin, adminPass); //admin login
  //TC #1184456
  Assert.assertValue(true, verifySearchIconTest(), "TC #1184456: Verify Search icon.");
  //TC #1184457
  Assert.assertValue(true, quickSearchProjectTest(projectName), "TC #1184457: Project Quick Search.");
  //TC #1184458
  Assert.assertValue(true, advancedSearchUITest(), "TC #1184458: Advanced Search UI.");
  //TC #1184459
  Assert.assertValue(true, projectAdvancedSearchUITest(projectName), "TC #1184459: Project Advanced Search.");
  Logging.doLogOut();
}

function projectAdvancedSearchUITest(projectName){//advanced search page should be open
  AdvancedSearch.getProjectsTabFromMenu().Click();
  AdvancedSearch.setKeyWordsTextBox(projectName);
  AdvancedSearch.clickSearchControl();
  return AdvancedSearch.findSearchResultInResultsPanel(projectName);
}

function verifySearchIconTest(){
  if(Header.openSearchControlsIfNecesary()){
    if(Header.getAdvancedControlLink().Exists){
      return true;
    }else{
      Logger.logWarning("Advanced Search link on Header Panel does not Exist.");
      return false;
    }
  }else{
    return false;
  }
}
function quickSearchProjectTest(name){
  var project=QuickSearch.search(name, HeaderPanel.PROJECT_SEARCH_TYPE,true);
  if(null != project){
    if(project.Exists){
      return WorkSummary.openPageFromLink(project);
    }else{
      Logger.logWarning("Project link on search panel does not exist.");
      return false;
    }
  }else{
    Logger.logWarning("Project link on search panel is null.");
    return false;
  }
}
function advancedSearchUITest(){
  Header.clickAdvancedSearchLink();
  AdvancedSearch.wait();
  return AdvancedSearch.isPageOpen();
}