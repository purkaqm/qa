function getQuickSearchResultsList(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.panelQs.panelQuickSearchResults.tableQuickSearchResults.resultCell;
}
function getQuickSearchResultsTable(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.panelQs.panelQuickSearchResults.tableQuickSearchResults;
}
function existsQuickSearchResultsList(){
  return getQuickSearchResultsList().Exists;
}
function existsNotResultFoundMessage(){
  return Aliases.browser.panelHeader.panelHeader.panelHeaderRight.panelHeaderRight2.panelSearchWrapper.panelSearch.panelQuickSearch.quickSearchForm.panelQs.panelQuickSearchResults.tableQuickSearchResults.resultNotFoundMessage.Exists;
}