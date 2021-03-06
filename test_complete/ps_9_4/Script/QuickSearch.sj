//USEUNIT QuickSearchResultPage
//USEUNIT MainNavLinks
//USEUNIT QuickSearchPopup
//USEUNIT Table
//USEUNIT WorkSummaryPage

//searches a project
function searchProject(itemName){
  return search(itemName, QuickSearchPopup.PROJECT_SEARCH_TYPE, true);
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
  if(QuickSearchResultPage.existsResultsTable()){
    var cols=Table.getColumnCount(QuickSearchResultPage.getSearchResultsTable());
    var rows=Table.getRowCount(QuickSearchResultPage.getSearchResultsTable());
    var colIndex=cols-5;//because sometimes the 1st column contines the range(then the table has 6 columns and the project column is the #1), and others is the project name in 1st place(then the table has 5 columns and the project column is the #0).
    for(var i=1;i<rows;i++){ //i=1 because row1=header.
      var item=Table.getCellAt(QuickSearchResultPage.getSearchResultsTable(),i,colIndex).Child(0); 
      var iName=item.contentText;
      if(equals){
        if(!aqString.Compare(itemName,iName,true)){
          return item;  //returns the link to the project.
        }
      }else{
        if(aqString.Find(iName,itemName,0) >=0){
          return item;  //returns the link to the project.
        }
      }
      
    }
    Log.Message("Project '"+itemName+"' not found in search function");
    return null;
  }else{
    Log.Message("Project '"+itemName+"' NOT found");
    return null;
  }
}
//returns the search result table
function doSearch(itemName, itemTarget){  //searches a project by name and opens it.
  MainNavLinks.getSearchControl().Click();
  QuickSearchPopup.setSearchText(itemName);
  if(null != itemTarget){
    QuickSearchPopup.selectTarget(itemTarget);
  }
  QuickSearchPopup.submitSearch();
}
