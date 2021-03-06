//USEUNIT HeaderPanel
//USEUNIT Page
var PAGE_IDENTIFIER="/search/advanced/";

function wait(){//Waits until the browser loads the Person page.
  Page.wait(Aliases.browser.pageAdvancedSearch);
}
function getAdvancedSearchMenuPanel(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelSub.panelContainer;
}
function getClearControl(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelBlockClosed.linkClear;
}
function getFiltersTable(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelBlockClosed.panelSearchfilters.form.filterTable;
}
function getKeyWordsTextBox(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelBlockClosed.panelSearchfilters.form.filterTable.keyWordsTextBox;
}
function getSearchControl(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer.panelBlockClosed.panelSearchfilters.form.searchControl;
}
function getContainerPanel(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer;
}
function getAdvancedSearchResultsPanel(){
  return Aliases.browser.pageAdvancedSearch.panelBodyContainer.panelPageContent.panelContent.panelContainer.advancedSearchResultsPanel;
}
function isPageOpen(){
  return (Page.isPageOpen(PAGE_IDENTIFIER) && AdvancedSearchPage.pageTitleContainsAdvancedSearchText());
}
function pageTitleContainsAdvancedSearchText(){
  return HeaderPanel.pageTitleContainsText("Advanced Search");
}