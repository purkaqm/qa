//USEUNIT HomePage
//USEUNIT QuickSearchResultPage
var PROJECT_SEARCH_TYPE="Projects";
//sets searched text textField
function setSearchText(itemName){
  Aliases.browser.popupSearch.searchTextBox.SetText(itemName);//Sets the name
}
//selects target drop-down
function selectTarget(target){
  Aliases.browser.popupSearch.selectTarget.ClickItem(target);//Selects the desired item
}
//clicks search link to start searchi
function submitSearch(){
  Aliases.browser.popupSearch.searchControl.ClickButton();//Clicks the 'search submit button' control.
  HomePage.wait();
  QuickSearchResultPage.wait();
}