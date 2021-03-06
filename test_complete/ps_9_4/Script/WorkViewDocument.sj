//USEUNIT DocumentOptionsPopup
//USEUNIT WorkDocuments
//USEUNIT Table
//USEUNIT WorkDocumentDetailsPage
//returns the document see details link of the desired document
function getDocumentSeeDetailsControl(pName, docTitle){
  return DocumentOptionsPopup.getDocumentOptionsSubItem(WorkDocuments.getDocumentByTitle(pName, docTitle), DocumentOptionsPopup.DOC_SEE_DETAILS_OPTION);
}
//is Document X->See_Details link present?
function isDocumentSeeDetailsControlPresent(pName, docTitle){
  var res=DocumentOptionsPopup.isDocumentOptionsSubItemPresent(WorkDocuments.getDocumentByTitle(pName, docTitle), DocumentOptionsPopup.DOC_SEE_DETAILS_OPTION);
  Log.Message("Document See Details link is present = "+res);
  return (null != res ?true:false);
}

function isDocumentVersionUploaded(comments){
  return (Table.getRowIndex(WorkDocumentDetailsPage.getDocumentVersionsTable(), comments, WorkDocumentDetailsPage.COMMENTS_COLUMN_ID, true) >= 0?true:false);
}
  //opens document see details link and stores the documentDetailsPage url    
function isSeeDetailsLinkPrensentAndOpensPage(pName, docTitle){
  var exists=WorkViewDocument.isDocumentSeeDetailsControlPresent(pName, docTitle);
  if(true == exists){
    var link=WorkViewDocument.getDocumentSeeDetailsControl(pName, docTitle);
    if(aqString.Compare(link.contentText, WorkDocumentDetailsPage.SEE_DETAILS_OPTION_LABEL, true)){
      Log.Error("Link parameter is not the See details link.");
      return false;
    }
    var res=WorkDocumentDetailsPage.openDocumentOptionByLink(link, true);
    Log.Message("Open Document details page from see details link returns: "+res);
    return res;//true=store details page url
  }else{
    Log.Message("Document See Details link is not present.");
    return false;
  }
}