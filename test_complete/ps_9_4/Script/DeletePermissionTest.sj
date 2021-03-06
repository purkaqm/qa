//USEUNIT Assert
//USEUNIT ToolBarLinks
//USEUNIT WorkDelete
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
  if(ToolBarLinks.isToolBarItemPresent(ToolBarLinks.WORK_OPTIONS)){
    var pre=ToolBarLinks.isWorkOptionsSubItemPresent(ToolBarLinks.WO_DELETE);
    Log.Message("Delete Work presence test ="+pre);
    return pre;
  }else{
    Log.Message("Project options control and Delete Link are not displayed");
    return false;
  }
}

//deletes a work
function deleteWorkTest(pName, WTParents){
  Work.openWork(pName);
  return WorkDelete.deleteWork(pName, true, WTParents);
}