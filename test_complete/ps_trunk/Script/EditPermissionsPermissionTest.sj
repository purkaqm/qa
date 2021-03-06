//USEUNIT Assert
//USEUNIT WorkEditPermissions
//this test requires a project opened on summary page; run view test before

//EditPermissions permission positive case
function testEditPermissionsPositive(pName){
  Log.Message("Edit Permissions permission Positive case starts");
  var linkPresent=EditPermissionsPermissionTest.workEditPermissionsControlPresentTest();
  Assert.assertValue(true,linkPresent,"testEditPermissionsPermissionPositive-EditPermissionsControlPresenceTest");
  if(linkPresent){
    Assert.assertValue(true,EditPermissionsPermissionTest.editPermissionsTest(pName),"testEditPermissionsPermissionPositive-editPermissions");
  }
  Assert.assertValue(true,EditPermissionsPermissionTest.editPermissionsDirectUrlAccess(),"testEditPermissionsPermissionPositive-EditPermissionsDirectUrlAccess");
}
function testEditPermissionsNegative(){
  Log.Message("Edit Permissions permission Negative case starts");
  Assert.assertValue(false,EditPermissionsPermissionTest.workEditPermissionsControlPresentTest(),"testEditPermissionsPermissionNegative-EditPermissionsControlPresenceTest");
  Assert.assertValue(false,EditPermissionsPermissionTest.editPermissionsDirectUrlAccess(),"testEditPermissionsPermissionNegative-EditPermissionsDirectUrlAccess");
}

//verifies if the edit permissions link is present
function workEditPermissionsControlPresentTest(){
  return WorkEditPermissions.isWorkEditPermissionsLinkPresent();
}

//edit permissions test. Sets the Owner core set category to all
function editPermissionsTest(pName){
  return WorkEditPermissions.editWorkCoreSet(pName, "Owner","Project","All");
}
function editPermissionsDirectUrlAccess(){
  if(WorkPermissionsPage.openPageFromUrl(true)){
    if(WorkPermissionsPage.isEditable()){
      Log.Message("Edit Permissions control Direct URL test = TRUE. Page is editable."); 
      return true; 
    }else{
      Log.Message("Edit Permissions control Direct URL test = FALSE. Page is NOT editable."); 
      return false;
    }
  }else{
    Log.Message("Edit Permissions control Direct URL test = FALSE. Page is NOT open."); 
    return false;
  }
}