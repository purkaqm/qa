//USEUNIT Work
//USEUNIT WorkDocuments
//USEUNIT DocumentOptionsPopup

//returns the document download link of the desired document
function getDocumentDownloadControl(pName, docTitle){
  Work.openWork(pName);
  WorkDocumentsPage.openPageFromLink();
  return DocumentOptionsPopup.getDocumentOptionsSubItem(WorkDocuments.getDocumentByTitle(pName, docTitle), DocumentOptionsPopup.DOC_DOWNLOAD_OPTION);
}
//is Document X->Download link present?
function isDocumentDownloadControlPresent(pName, docTitle){
  return (null != WorkDownloadDocument.getDocumentDownloadControl(pName, docTitle)?true:false);
}