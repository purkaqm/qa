//USEUNIT WorkDeleteResultsPage
//USEUNIT WorkDeleteParentWorkPage
//clicks the delete button
function clickSubmitDeleteControl(){
  Aliases.browser.popupDeleteWork.submitDeleteControl.ClickButton();
  DeleteWorkPopup.waintUntilDeletePageAppears();
}

//waits until one of the confirm delete page appears
function waintUntilDeletePageAppears(){
  for(;;){
    if(WorkDeleteResultsPage.isPageOpen()){
      WorkDeleteResultsPage.wait();
      break;
    }else{
      if(WorkDeleteParentWorkPage.isPageOpen()){
        WorkDeleteParentWorkPage.wait();
        break;
      }
    }
  }
}