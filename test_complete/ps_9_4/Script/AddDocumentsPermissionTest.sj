//USEUNIT Assert
//USEUNIT Work
//USEUNIT WorkDocuments

//add document permission positive case
function testAddDocumentsPermissionPositive(pName, title, url, fileName, filePath){
  Log.Message("Add Documents permission Positive case starts");
  var linkPresent=AddDocumentsPermissionTest.addDocumentsControlPresentTest(pName);
  Assert.assertValue(true,linkPresent,"testAddDocumentsPermissionPositive-AddDocumentsControlPresenceTest");
  if(linkPresent){
    Assert.assertValue(true,AddDocumentsPermissionTest.addDocumentsTest(pName, title, url, fileName, filePath),"testAddDocumentsPermissionPositive-AddDocumentTest");
  }
}
function testAddDocumentsPermissionNegative(pName){
  Log.Message("Add Documents permission Negative case starts");
  Assert.assertValue(false,AddDocumentsPermissionTest.addDocumentsControlPresentTest(pName),"testAddDocumentsPermissionTest.PermissionNegative-AddDocumentsControlPresenceTest");
}

//verifies if the AddDocument link is present
function addDocumentsControlPresentTest(pName){
  return WorkDocuments.existsNewDocumentGeneralControl(pName);
}

//Adds 2 documents to a work. One local file and one file by url
function addDocumentsTest(pName, title, url, fileName, filePath){
  return (true == WorkDocuments.addNewDocument(pName, title, "description_"+title, url, fileName, filePath)?true:false);
}