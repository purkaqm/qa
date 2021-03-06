//USEUNIT Assert
//USEUNIT ProjectNavigationMenu
//USEUNIT WorkDelete
//USEUNIT ViewPermissionTest
//this test requires a project opened on summary page; run view test before

//delete permission positive case
//delete work true removes the opened work
function testDeletePermissionPositive(pName, deleteWork, WTParents){
  Log.Message("Delete permission Positive case starts");
  var linkPresent=workDeleteControlPresentTest();
  Assert.assertValue(true,linkPresent,"testDeletePermissionPositive-WorkDeleteControlPresenceTest");
  if(linkPresent && deleteWork){
    Assert.assertValue(true,DeletePermissionTest.deleteWorkTest(pName,WTParents),"testDeletePermissionPositive-deleteWork"); //assert the work deletion
  }
}
function testDeletePermissionNegative(){
  Log.Message("Delete permission Negative case starts");
  Assert.assertValue(false,workDeleteControlPresentTest(),"testDeletePermissionNegative-WorkDeleteControlPresenceTest");
}
//verifies if the delete link is present
function workDeleteControlPresentTest(){
  return WorkDelete.isWorkDeleteLinkPresent();
}
//deletes a work
function deleteWorkTest(pName, WTParents){
  Work.openWork(pName);
  var deleted= WorkDelete.deleteWork(pName, true, WTParents);
  if(true == deleted){
    var wt=ViewPermissionTest.workTreeTest(pName,WTParents);
    var qs=ViewPermissionTest.simpleProjectSearchTest(pName);
    var du=ViewPermissionTest.viewWorkDirectUrlAccess();
    if(true == wt || true == qs|| true == du){
      Log.Message("Work deletion fails. Project could be found after deletion. WorkTree: "+wt+" QuickSearch: "+qs+"DirectUrl: "+du);
      return false;
    }else{
      Log.Message("Work deletion passed. Project not found after deletion.");
      return true;
    }
  }else{
    Log.Message("Work deletion fails.");
    return false;
  }
}