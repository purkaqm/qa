//USEUNIT Assert
//USEUNIT WorkViewPermissions
//USEUNIT WorkPermissions
//this test requires a project opened on summary page; run view test before

//ViewPermissions permission positive case
function testViewPermissionsPositive(){
  Log.Message("View Permissions permission Positive case starts");
  Assert.assertValue(true,ViewPermissionsPermissionTest.workViewPermissionsControlPresentTest(),"testViewPermissionsPermissionPositive-ViewPermissionsControlPresenceTest");
  Assert.assertValue(true,ViewPermissionsPermissionTest.viewPermissionsDirectUrlAccess(),"testViewPermissionsPermissionPositive-ViewPermissionsDirectUrlAccess");
}
function testViewPermissionsNegative(){
  Log.Message("View Permissions permission Negative case starts");
  Assert.assertValue(false,ViewPermissionsPermissionTest.workViewPermissionsControlPresentTest(),"testViewPermissionsPermissionNegative-ViewPermissionsControlPresenceTest");
  Assert.assertValue(false,ViewPermissionsPermissionTest.viewPermissionsDirectUrlAccess(),"testViewPermissionsPermissionNegative-ViewPermissionsDirectUrlAccess");
}

//verifies if the view permissions link is present
function workViewPermissionsControlPresentTest(){
    if(WorkViewPermissions.isWorkViewPermissionsLinkPresent()){
      WorkViewPermissions.openPermissionsPageFromLink();
      if(WorkPermissions.isPageOpen()){
        if(!WorkPermissions.isPageEditable()){
          Log.Message("View Permissions control presence test = TRUE"); 
          return true;
        }else{
          Log.Message("View Permissions control presence test = FALSE. Page is editable and should NOT be."); 
          return false;
        }
      }else{
        Log.Message("View Permissions control presence test = FALSE. Page is NOT open."); 
        return false;
      }
    }else{
      Log.Message("View Permissions control or Work options control link not present.");
      return false;
    }
}

function viewPermissionsDirectUrlAccess(){
  if(WorkViewPermissions.openPermissionsPageFromUrl(true)){
    if(!WorkPermissions.isPageEditable()){
      Log.Message("View Permissions control Direct URL test = TRUE. Page is not editable."); 
      return true; 
    }else{
      Log.Message("View Permissions control Direct URL test = FALSE. Page is editable."); 
      return false;
    }
  }else{
    Log.Message("View Permissions control Direct URL test = FALSE. Page is NOT open."); 
    return false;
  }
}