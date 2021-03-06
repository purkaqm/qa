//USEUNIT QuickSearchResultsPopIn
//USEUNIT Table
//USEUNIT Logger
//USEUNIT WorkSummaryPage
//USEUNIT Header
//USEUNIT HeaderPanel
//searches a project
var SEARCH_TIMEOUT=20;
function searchProject(itemName){
  return search(itemName, HeaderPanel.PROJECT_SEARCH_TYPE, true);
}
//searches all types
function searchAll(itemName){
  return search(itemName, null, true);
}
//Searches a project by name an type and returns the link to the project if found, else returns null.
//equals=true verifies that the name be equals; if false verifies that contains the name
function search(itemName, itemTarget, equals){  //searches a project by name and opens it.
  if(!(equals==true || equals==false)){//if is not a boolean value
    Log.Error("equals parameter should have a valid boolean value");
  }
  QuickSearch.doSearch(itemName, itemTarget);
  var result=QuickSearchResultsPopIn.getQuickSearchResultsList();
  var timeCount=0;
  do{
    HeaderPanel.getSearchTextBox().WaitSlApplication(1000);//waits 1 second
    var searchedItem=(equals?itemName:"*"+itemName+"*");//if equals is false, it looks if the found item contains the itemName text
    var res=QuickSearchResultsPopIn.getQuickSearchResultsTable().FindChild(["ObjectType","innerText"], ["TextNode",searchedItem], 100, true);
    if(res.Exists){
      Log.Message("Returning result #1 from Quick Search ="+res.title);
      return res;
    }else{
      res=QuickSearchResultsPopIn.getQuickSearchResultsTable().FindChild(["ObjectType","innerText"], ["Cell",searchedItem], 100, true);//if there is only 1 result
      if(res.Exists){
        Log.Message("Returning result #2 from Quick Search ="+res.title);
        return res;
      }else{
        if(QuickSearchResultsPopIn.existsQuickSearchResultsList()){//if there is a search result
          Log.Message("Quick Search has results but not the expected = '"+searchedItem+"'");
          return null;
        }
      }
    }
    if(QuickSearchResultsPopIn.existsNotResultFoundMessage()){
      Log.Message("No results where found searching for '"+itemName+"'");
      return null;
    }
  }while(timeCount<SEARCH_TIMEOUT);
  Log.Error("QuickSearch Time-out exceeded searching "+itemName+".");
  return null;
}
//returns the search result table
function doSearch(itemName, itemTarget){
  Header.openSearchControlsIfNecesary();
  if(null != itemTarget){
    Header.selectSearchType(itemTarget);
  }
  HeaderPanel.setSearchText(itemName);
}