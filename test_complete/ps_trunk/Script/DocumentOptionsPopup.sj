//USEUNIT WorkDocumentsPage
  var DOC_SEE_DETAILS_OPTION=1;
  var DOC_UPDATE_OPTION=2;
  var DOC_DOWNLOAD_OPTION=3;
//returns the view document details link
function getSeeDetailsControl(){
  return Aliases.browser.popupDocumentOptions.seeDetailsCell;
}
//returns the update document link
function getUpdateControl(){
  return Aliases.browser.popupDocumentOptions.updateCell;
}  
//returns the download document link
function getDownloadControl(){
  return Aliases.browser.popupDocumentOptions.downloadCell;
} 
//returns if document options popup is displayen on the screen
function existsDocumentOptionsPopup(){
  return Aliases.browser.popupDocumentOptions.Exists;
}
//is document options subItem link present?
function isDocumentOptionsSubItemPresent(docLink, item){
  var link=DocumentOptionsPopup.getDocumentOptionsSubItem(docLink, item);
  if(null!=link){
    return link.Exists;
  }else{
    return false;
  }
}
function openDocumentOptionsPopupByLink(docLink){
  if(null != docLink){
    docLink.Click();
    return DocumentOptionsPopup.existsDocumentOptionsPopup();
  }else{
    Log.Error("Could NOT open Document Options Popup; document link is NULL.");
  }
}
//returns a work toolbar link. Logs an error and returns Null if item value is invalid
function getDocumentOptionsSubItem(docLink, item){
   /*if(!DocumentOptionsPopup.isDocumentOptionsPopupDisplayed()){
      docLink.Click();
   }*/
   if(DocumentOptionsPopup.openDocumentOptionsPopupByLink(docLink)){ 
    switch(item){
      case DocumentOptionsPopup.DOC_SEE_DETAILS_OPTION: return DocumentOptionsPopup.getSeeDetailsControl();
      case DocumentOptionsPopup.DOC_UPDATE_OPTION: return DocumentOptionsPopup.getUpdateControl();
      case DocumentOptionsPopup.DOC_DOWNLOAD_OPTION: return DocumentOptionsPopup.getDownloadControl()
      default: Log.Error("Document Options Item: #"+item+" not found."); return null;
    }
  }else{
    Log.Error("Document Options Popup does NOT opened. Error getting item #"+item);
    return null;
  }
  
}