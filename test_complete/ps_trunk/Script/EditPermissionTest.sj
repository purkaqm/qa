//USEUNIT Assert
//USEUNIT WorkEditDetails

//edit permission positive case
function testEditPermissionPositive(pName){
  Log.Message("Edit permission Positive case starts");
  var linkPresent=workEditDetailsControlPresentTest(pName);
  Assert.assertValue(true,linkPresent,"testEditPermissionPositive-EditDetailsControlPresenceTest");
  if(linkPresent){
    Assert.assertValue(true,EditPermissionTest.editWorkDescriptionTest(pName),"testEditPermissionPositive-editWorkDescription"); //assert that the description be edited
  }
  Assert.assertValue(true,EditPermissionTest.editWorkDirectUrlAccess(),"testEditPermissionPositive-EditWorkDirectUrlAccess");
}
//edit permission negative case
function testEditPermissionNegative(pName){
  Log.Message("Edit permission Negative case starts");
  Assert.assertValue(false,EditPermissionTest.workEditDetailsControlPresentTest(pName),"testEditPermissionNegative-EditDetailsControlPresenceTest");
  Assert.assertValue(false,EditPermissionTest.editWorkDirectUrlAccess(),"testEditPermissionNegative-EditWorkDirectUrlAccess");
}

//verifies if the edit details link is present
function workEditDetailsControlPresentTest(pName){
  return WorkEditDetails.isWorkEditDetailsLinkPresent(pName);
}

//edits the work description
function editWorkDescriptionTest(pName){
  if(WorkEditDetails.editWorkDescription(pName,"description 1") && WorkEditDetails.editWorkDescription(pName, "description 2")){
    return true;
  }else{
    return false;
  }
}
function editWorkDirectUrlAccess(){
  return WorkEditDetails.openPageFromUrl(true);
}