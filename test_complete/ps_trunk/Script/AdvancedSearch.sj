//USEUNIT AdvancedSearchPage
//USEUNIT WorkSummaryPage
function wait(){//Waits until the browser loads the Person page.
  return AdvancedSearchPage.wait();
}
function getMenuItemByName(name){
  return AdvancedSearchPage.getAdvancedSearchMenuPanel().FindChild(["objectType","contentText"], ["Link",name], 10, true);
}
function areDisplayedAllTabsOnMenu(){
  var res=true;
  var x=AdvancedSearch.getAllTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("All tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getActionItemsTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Action Items tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getProjectsTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Projects tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getWorksTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Works tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getIdeasTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Ideas tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getIssuesTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Issues tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getDiscussionsTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Discussions tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  x=AdvancedSearch.getOrganizationsTabFromMenu();
  if(!x.Exists || !x.VisibleOnScreen){
    Log.Message("Organizations tab is not displayed on Advanced Search page's menu.");
    res= false;
  }
  return res;
}
function getAllTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("All");
}
function getProjectsTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Projects");
}
function getOrganizationsTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Organizations");
}
function getWorksTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Works");
}
function getPeopleTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("People");
}
function getIdeasTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Ideas");
}
function getDiscussionsTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Discussions");
}
function getIssuesTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Issues");
}
function getActionItemsTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Action Items");
}
function getDocumentsTabFromMenu(){
  return AdvancedSearch.getMenuItemByName("Documents");
}
function getKeyWordsTextBox(){
  return AdvancedSearchPage.getKeyWordsTextBox();
}
function setKeyWordsTextBox(text){
  AdvancedSearch.getKeyWordsTextBox().SetText(text);
}
function isPageOpen(){
  return (AdvancedSearchPage.isPageOpen() && AdvancedSearch.areDisplayedAllTabsOnMenu() && AdvancedSearch.isSearchControlDisplayed() && AdvancedSearch.isClearControlDisplayed() && AdvancedSearch.isSearchFilterTableDisplayed());
}
function isSearchControlDisplayed(){
  return (AdvancedSearchPage.getSearchControl().Exists  && AdvancedSearchPage.getSearchControl().VisibleOnScreen);
}
function isClearControlDisplayed(){
  return (AdvancedSearchPage.getClearControl().Exists  && AdvancedSearchPage.getClearControl().VisibleOnScreen);
}
function isSearchFilterTableDisplayed(){
  return (AdvancedSearchPage.getFiltersTable().Exists  && AdvancedSearchPage.getFiltersTable().VisibleOnScreen);
}
function pageTitleContainsAdvancedSearchText(){
  return AdvancedSearchPage.pageTitleContainsAdvancedSearchText();
}
function clickSearchControl(){
  AdvancedSearchPage.getSearchControl().Click();
  AdvancedSearchPage.getContainerPanel().WaitChild("advancedSearchResultsPanel");
}
function findSearchResultInResultsPanel(searchedItem){
  var res=AdvancedSearchPage.getAdvancedSearchResultsPanel().FindChild(["ObjectType","contentText"], ["Link",searchedItem], 100, true);
  if(res.Exists){
    return WorkSummaryPage.openPageFromLink(res);
  }else{
    return false;
  }
}