//USEUNIT WorkSummary
//USEUNIT WorkSummaryPage
//USEUNIT Assert
//USEUNIT QuickSearch
//USEUNIT WorkTree
var pType="Projects";
//view Work Permission test positive case
function testViewPermissionPositive(pName,WTParents){
  Log.Message("View permission Positive case starts");
  Assert.assertValue(true,ViewPermissionTest.workTreeTest(pName,WTParents),"testViewPermissionPositive-WorkTreeTest"); 
  Assert.assertValue(true,ViewPermissionTest.simpleProjectSearchTest(pName),"testViewPermissionPositive-SimpleProjectSearchTest");
  Assert.assertValue(true,ViewPermissionTest.viewWorkDirectUrlAccess(),"testViewPermissionPositive-ViewWorkDirectUrlAccess"); 
}
//view Work Permission test negative case
function testViewPermissionNegative(pName,WTParents){
  Log.Message("View permission Negative case starts");
  Assert.assertValue(false,ViewPermissionTest.workTreeTest(pName,WTParents),"testViewPermissionNegative-WorkTreeTest"); 
  Assert.assertValue(false,ViewPermissionTest.simpleProjectSearchTest(pName,pType),"testViewPermissionNegative-SimpleProjectSearchTest");
  Assert.assertValue(false,ViewPermissionTest.viewWorkDirectUrlAccess(),"testViewPermissionNegative-ViewWorkDirectUrlAccess");
}

//Opens a project by direct url
function viewWorkDirectUrlAccess(){
  return WorkSummary.openPageFromUrl(true);
}
//searches a project by name on Simple Search
function simpleProjectSearchTest(pName){
  var wi=QuickSearch.searchProject(pName);
  return ViewPermissionTest.openProject(wi,"Simple Search");  
}
//searches a project by name on WorkTree
function workTreeTest(pName,parents){
  var wi=WorkTree.searchOnWorkTree(pName,parents);//when project has parents use ("pName",["My Company","..."]); when not use null
  return ViewPermissionTest.openProject(wi,"Work Tree");
}
//opens a project
function openProject(work,searchMethod){
  if(null!=work){//if found
    WorkSummary.openPageFromLink(work);
    if(WorkSummaryPage.isPageOpen()){
      Log.Message("Project was found on "+searchMethod+" and it was opened correctly.");
      return true;
    }else{
      Log.Error("Project was found on "+searchMethod+" but Summary page was NOT opened correctly.");
      return false;
    }
  }else{
    Log.Message("Project was not found on "+searchMethod+".");
    return false;
  }
}