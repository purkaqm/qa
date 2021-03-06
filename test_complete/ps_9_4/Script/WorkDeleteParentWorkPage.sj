//USEUNIT HomePage
//USEUNIT Page
//USEUNIT WorkDeleteResultsPage
 var PAGE_IDENTIFIER="/project/DeleteWork.epage";//.epage is important to differentiate this page with workDeleteResultsPage
 
 function isPageOpen(){ //returns wheter the page is open
    return Page.isPageOpen(WorkDeleteParentWorkPage.PAGE_IDENTIFIER); //if the current url contains "/project/DeleteWork.epage"
 }
 
 function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageDeleteParentWork);
 }
 //selects the first radiobutton
 function selectDeleteAllChildItemsRadioButton(){
    WorkDeleteParentWorkPage.getDeleteAllChildItemsRadioButton().ClickButton();
    if(WorkDeleteParentWorkPage.getDeleteAllChildItemsRadioButton().checked){
      return true;
    }else{
      Log.Error("RadioButton DeleteAllChildItems NOT selected.");
      return false;
    }
 }
 //selects the second radiobutton
 function selectLeaveTheChildItemsWithoutParentRadioButton(){
   WorkDeleteParentWorkPage.getLeaveTheChildItemsWithoutParentRadioButton().ClickButton();
    if(WorkDeleteParentWorkPage.getLeaveTheChildItemsWithoutParentRadioButton().checked){
      return true;
    }else{
      Log.Error("RadioButton LeaveTheChildItemsWithoutParent NOT selected.");
      return false;
    }
 }
 //returns the first radiobutton
 function getDeleteAllChildItemsRadioButton(){
  return Aliases.browser.pageDeleteParentWork.deleteAllChildItemsRadioButton;
 }
 //returns the second radiobutton
 function getLeaveTheChildItemsWithoutParentRadioButton(){
  return Aliases.browser.pageDeleteParentWork.leaveTheChildItemsWithoutParentRadioButton;
 }
  //exists the first radiobutton?
 function existsDeleteAllChildItemsRadioButton(){
  return WorkDeleteParentWorkPage.getDeleteAllChildItemsRadioButton().Exists;
 }
 //exists the second radiobutton?
 function existsLeaveTheChildItemsWithoutParentRadioButton(){
  return WorkDeleteParentWorkPage.getLeaveTheChildItemsWithoutParentRadioButton().Exists;
 }
  //clicks the delete button
 function clickDeleteControl(){
  Aliases.browser.pageDeleteParentWork.deleteControl.ClickButton();
  WorkDeleteResultsPage.wait();
 }