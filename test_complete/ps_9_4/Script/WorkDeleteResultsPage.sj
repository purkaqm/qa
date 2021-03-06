//USEUNIT HomePage
//USEUNIT Page

 var PAGE_IDENTIFIER="/project/DeleteWorkResults";
 var SUCCESSFULL_DELETE_MESSAGE_SUFFIX_WITHOUT_CHILD="has been successfully deleted";//remove successful message
 var SUCCESSFULL_DELETE_MESSAGE_PREFIX_WITH_CHILD="successfully deleted";//remove successful message
 
//returns the results panel message like: "ProjectX has been successfully deleted."
function getResultsPanelText(){
  return Aliases.browser.pageDeleteWorkResult.panelContent.panelContainer.resultsPanel.contentText;
}
//click the OK button and navigates to home page
function clickOkControl(){
  Aliases.browser.pageDeleteWorkResult.OkControl.ClickButton();
  HomePage.wait();
}
function isPageOpen(){ //returns wheter the page is open
  return Page.isPageOpen(WorkDeleteResultsPage.PAGE_IDENTIFIER); //if the current url contains "/project/DeleteWorkResults"
}
function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageDeleteWorkResult);
}