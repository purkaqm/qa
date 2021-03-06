//USEUNIT WorkPermissionsPage
//USEUNIT CoreSetsTable
//USEUNIT CustomSetsTable
//USEUNIT WorkPermissionsPage
//USEUNIT Work

//edits work core sets
function editWorkCoreSet(pName, coreSet, level, catName){
  Work.openWork(pName);
  WorkPermissionsPage.openEditPermissionsPageFromLink();
  if(CoreSetsTable.setProjectCoreSetValue(coreSet, level, catName)){
    Log.Message("Saving Core Set changes.");
    WorkPermissionsPage.clickSaveCoreSetControl();
    return true;
  }
  Log.Error("Core Set changes were NOT saved.");
  return false;
}
//adds a user or group to work custom set
function addWorkCustomSet(pName, name, catName){
  Work.openWork(pName);
  WorkPermissionsPage.openEditPermissionsPageFromLink();
  if(CustomSetsTable.addUserOrGroupToProjectCustomSet(name, catName)){
    Log.Message("Saving Custom Set changes.");
    WorkPermissionsPage.clickSaveCustomSetControl();
    return true;
  }
  Log.Error("Custom Set changes were NOT saved.");
  return false;
}
//is Work EditPermissions link present?
function isWorkEditPermissionsLinkPresent(){
  return ProjectNavigationMenu.existsEditPermissionsControl();
}