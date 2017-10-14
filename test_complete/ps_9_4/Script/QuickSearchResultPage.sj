//USEUNIT Page

function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageQuickSearch);
}

//Returns the search results table.
function getSearchResultsTable(){
  return Aliases.browser.pageQuickSearch.searchResultsTable;
}
//Returns true when table exists and false when not.
function existsResultsTable(){
  return QuickSearchResultPage.getSearchResultsTable().Exists;
}
