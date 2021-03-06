//USEUNIT Assert
//USEUNIT WorkDocuments
//USEUNIT WorkViewDocument
//USEUNIT WorkDocumentDetailsPage
//USEUNIT WorkDocumentsPage
//USEUNIT WorkDownloadDocument
//View document permission positive case
function testViewDocumentPermissionPositive1(pName){
  Log.Message("View Document permission Positive 1st. case starts");
  Assert.assertValue(true,ViewDocumentPermissionTest.documentsControlPresentTest(pName),"testViewDocumentPermissionPositive-DocumentsControlPresenceTest");
}
function testViewDocumentPermissionPositive2(pName, docTitle){
   //Run add documents test before...
  Log.Message("View Document permission Positive 2nd. case starts");
  Assert.assertValue(true,ViewDocumentPermissionTest.documentsPageUrlAccess(),"testViewDocumentsPermissionPositive-DocumentsPageDirectUrlAccessTest");
  Assert.assertValue(true,ViewDocumentPermissionTest.seeDocumentDetailsTest(pName, docTitle), "testViewDocumentsPermissionPositive-SeeDocumentDetailsTest");
  Assert.assertValue(true,ViewDocumentPermissionTest.downloadDocumentControlPresentTest(pName, docTitle), "testViewDocumentsPermissionPositive-DownloadDocumentControlPresenceTest");
  Assert.assertValue(true,ViewDocumentPermissionTest.documentDetailsPageUrlAccess(),"testViewDocumentsPermissionPositive-DocumentDetailsPageDirectUrlAccessTest");
}
//View document permission negative case
function testViewDocumentPermissionNegative(pName){
  Log.Message("View Document permission Positive case starts");
  Assert.assertValue(false,ViewDocumentPermissionTest.documentsControlPresentTest(pName),"testViewDocumentPermissionNegative-DocumentsControlPresenceTest");
  Assert.assertValue(false,ViewDocumentPermissionTest.documentDetailsPageUrlAccess(),"testViewDocumentsPermissionNegative-DocumentDetailsPageDirectUrlAccessTest");
  Assert.assertValue(false,ViewDocumentPermissionTest.documentsPageUrlAccess(),"testViewDocumentsPermissionNegative-DocumentsPageDirectUrlAccessTest");
}

//Verifies if the Documents link is present on Work toolbar
function documentsControlPresentTest(pName){
  return WorkDocuments.isWorkDocumentsLinkPresent(pName);
}
//Verifies if the Download link of a given document is present 
function downloadDocumentControlPresentTest(pName, docTitle){
  return WorkDownloadDocument.isDocumentDownloadControlPresent(pName, docTitle);
}
//Verifies that the see details of a given document is present and opens that page and returns if successful.
function seeDocumentDetailsTest(pName, docTitle){
  return WorkViewDocument.isSeeDetailsLinkPrensentAndOpensPage(pName, docTitle);
}
//Opens project Documents page by direct url
function documentsPageUrlAccess(){
  return WorkDocumentsPage.openPageFromUrl(true);
}
//Opens project Document details page by direct url
function documentDetailsPageUrlAccess(){
  return WorkDocumentDetailsPage.openPageFromUrl(true);
}