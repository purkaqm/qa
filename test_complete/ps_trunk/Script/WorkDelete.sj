//USEUNIT ProjectNavigationMenu
//USEUNIT DeleteWorkPopup
//USEUNIT WorkDeleteResultsPage
//USEUNIT WorkDeleteParentWorkPage
//USEUNIT ViewPermissionTest
//USEUNIT ProjectNavigationMenu
//USEUNIT Work

//removes a work. deleteDescendants if true removes that work and it descendants
//WTParents= work tree parents array (Project parents on work tree) if is a root project WTParents=null
function deleteWork(pName, deleteDescendants, WTParents){
  if(!(deleteDescendants==true || deleteDescendants==false)){//if is not a boolean value
    Log.Error("deleteDescendants parameter should have a valid boolean value.");
  }  
  var deleted = false;
  if(ProjectNavigationMenu.existsDeleteControl()){
   ProjectNavigationMenu.getDeleteControl().Click();
    DeleteWorkPopup.clickSubmitDeleteControl();
    //if the work to delete hasn't descendants
    if(WorkDeleteResultsPage.isPageOpen()){
      var message=WorkDeleteResultsPage.getResultsPanelText();
      if(aqString.Find(message,WorkDeleteResultsPage.SUCCESSFULL_DELETE_MESSAGE_SUFFIX_WITHOUT_CHILD,0)>=0){
        WorkDeleteResultsPage.clickOkControl();
        Log.Message("Work deleted successfully. Message: '"+message+"'.");
        deleted = true;//was return true before the deletion checking.
      }else{
        Log.Error("Delete confirmation message suffix: '"+WorkDeleteResultsPage.SUCCESSFULL_DELETE_MESSAGE_SUFFIX_WITHOUT_CHILD+"' NOT found on message: '"+message+"'.");
        //was return false before the deletion checking.
      }
    }else{
      //if the work to delete has descendants.
      if(WorkDeleteParentWorkPage.isPageOpen()){
         if(WorkDeleteParentWorkPage.existsDeleteAllChildItemsRadioButton() && WorkDeleteParentWorkPage.existsLeaveTheChildItemsWithoutParentRadioButton()){//when a work has asociated socuments it does not display this two options
           (deleteDescendants? WorkDeleteParentWorkPage.selectDeleteAllChildItemsRadioButton(): WorkDeleteParentWorkPage.selectLeaveTheChildItemsWithoutParentRadioButton());//according to deleteDescendants value, it selects one or the other radioButton.
         }
         WorkDeleteParentWorkPage.clickDeleteControl();
         var message=WorkDeleteResultsPage.getResultsPanelText();
         if(aqString.Find(message,WorkDeleteResultsPage.SUCCESSFULL_DELETE_MESSAGE_PREFIX_WITH_CHILD,0)>=0){
           WorkDeleteResultsPage.clickOkControl();
           deleted = true;//was return true before the deletion checking.
         }else{
           Log.Error("Delete confirmation message suffix: '"+WorkDeleteResultsPage.SUCCESSFULL_DELETE_MESSAGE_PREFIX_WITH_CHILD+"' NOT found. Delete Descendants = "+deleteDescendants);
           WorkDeleteResultsPage.clickOkControl();
           //was return false before the deletion checking.
         }
      }
    }
    //was return false before the deletion checking.
  }else{
    Log.Message("Delete work link NOT present.");
    //was return false before the deletion checking.
  }
  return deleted;
}
//is Work Delete link present?
function isWorkDeleteLinkPresent(){
  return ProjectNavigationMenu.existsDeleteControl();
}