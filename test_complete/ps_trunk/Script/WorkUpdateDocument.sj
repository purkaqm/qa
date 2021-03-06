//USEUNIT DocumentOptionsPopup
//USEUNIT Work
//USEUNIT WorkDocuments
//USEUNIT WorkDocumentEditDetailsPage
//USEUNIT WorkDocumentDetailsPage
//USEUNIT WorkViewDocument
//returns the document update link of the desired document
function getDocumentUpdateControl(pName, docTitle){
  return DocumentOptionsPopup.getDocumentOptionsSubItem(WorkDocuments.getDocumentByTitle(pName, docTitle), DocumentOptionsPopup.DOC_UPDATE_OPTION);
}
//is Document X->Update link present?
function isDocumentUpdateControlPresent(pName, docTitle){
  var res=DocumentOptionsPopup.isDocumentOptionsSubItemPresent(WorkDocuments.getDocumentByTitle(pName, docTitle), DocumentOptionsPopup.DOC_UPDATE_OPTION);
  Log.Message("Document Update link is present = "+res);
  return res;
}
function isDocumentUpdateControlPresentAndOpensPage(pName, docTitle){
  if(true == WorkUpdateDocument.isDocumentUpdateControlPresent(pName, docTitle)){//if update control is present
    var res=WorkUpdateDocument.openDetailsPageFromUpdateLink(pName, docTitle)
    Log.Message("Open Document details page from update link returns: "+res);
    return res;
  }else{
    Log.Message("Document Update link is not present.");
    return false;
  }
}
function isEditDetailsControlPresentOnDocumentDetailsPage(pName, docTitle){
  if(false == WorkViewDocument.isSeeDetailsLinkPrensentAndOpensPage(pName, docTitle)){
    Log.Message("Document details page could NOT be opened to verify Edit details control presence.");
    return false;
  }else{
    if(WorkDocumentDetailsPage.existsEditDetailsControl()){
      Log.Message("Document Edit Details control is present.");
      return true;
    }else{
      Log.Message("Document Edit Details control is NOT present.");
      return false;
    }
  }
}
//opens the document update link and doesn't stores the details page url.
function openDetailsPageFromUpdateLink(pName, docTitle){//update page = details page.
  var link=WorkUpdateDocument.getDocumentUpdateControl(pName, docTitle);
  if(null!=link){
    if(aqString.Compare(link.contentText, WorkDocumentDetailsPage.UPDATE_OPTION_LABEL, true)){
      Log.Error("Link parameter is not the Update link.");
      return false;
    }
    var res= WorkDocumentDetailsPage.openDocumentOptionByLink(link, false);
    if(true == res){
      res = WorkDocumentDetailsPage.existsNewVersionPanel();
    }
    Log.Message("Open Document details page from Update link returns: "+res);
    return res;
  }else{
    Log.Error("Document Update link = NULL.");
    return false;
  }
}
function isUploadNewVersionPanelPresentOnDocumentDetailsPage(pName, docTitle){
  //Work.openWork(pName);
  if(false == WorkViewDocument.isSeeDetailsLinkPrensentAndOpensPage(pName, docTitle)){
    Log.Error("Document details page could NOT be opened to verify Upload New Version Panel presence.");
    return false;
  }else{
    if(WorkDocumentDetailsPage.existsNewVersionPanel()){
      Log.Message("Document Update new version Panel is present.");
      return true;
    }else{
      Log.Message("Document Update new version Panel is NOT present.");
      return false;
    }
  }
}
//updates document description
function editDocumentDetails(pName, docTitle, description){
  Work.openWork(pName);
  WorkUpdateDocument.openDetailsPageFromUpdateLink(pName, docTitle);
  WorkDocumentDetailsPage.clickEditDetailsControl();
  if(WorkUpdateDocument.isTitleTextBoxEqualsTo(docTitle)){
    WorkDocumentEditDetailsPage.setDescriptionText(description);
    WorkDocumentEditDetailsPage.clickSaveChangesControl();//it saves the details changes and redirects to document main page
    WorkDocumentDetailsPage.wait();//does a implicit wait because WorkDocumentEditDetailsPage and WorkDocumentDetailsPage cannot refer eachother
    if(!aqString.Compare(description,WorkDocumentDetailsPage.getDocumentDescriptionText(),true)){
      Log.Message("Successful Editing document details.");
      return true;
    }else{
      Log.Error("Error Editing document details. Description : '"+description+"' NOT equals to :'"+WorkDocumentDetailsPage.getDocumentDescriptionText()+"'.");
      return false;
    }
  }else{
    Log.Error("Document Title parameter is not equals to the opened document title textBox content. '"+docTitle+"' NOT EQUALS to '"+WorkDocumentEditDetailsPage.getTitleTextBox().Text);
    return false;
  }
}
function isTitleTextBoxEqualsTo(title){
  return (!aqString.Compare(title,WorkDocumentEditDetailsPage.getTitleTextBox().Text,true));
}
function uploadNewDocumentVersion(pName, title, comments, url, fileName, filePath){
  Log.Message("Uploading a new document copy to "+pName+". Document title = "+title+". Neew comments = "+comments+". Document url = "+url+". Local file name = "+fileName+". Local file path = "+filePath+".");
  if(null!=fileName && null!=filePath && null!=url){
    Log.Error("Url, fileName and filePath  paramethers can not have NULL values at the same time.");
    return false;
  }else{
    if(null == url && (null == fileName || null == filePath)){
      Log.Error("When Url paramether has null value, neither fileName nor filePath  paramethers can have NULL values.");
      return false;
    }
  }
  Work.openWork(pName);
  WorkUpdateDocument.openDetailsPageFromUpdateLink(pName, title);
  if(WorkDocumentDetailsPage.existsNewVersionPanel()){
    WorkDocumentDetailsPage.setNewVersionCommentsTextArea(comments);
    if(null != url){
      WorkDocumentDetailsPage.setNewVersionUrlText(url);
    }else{
      if(null != fileName && null != filePath){
        BrowseFile.browseFile(WorkDocumentDetailsPage.getNewVersionBrowseFileComponent(),filePath,fileName);
      }
    }
    WorkDocumentDetailsPage.clickUploadNewVersionControl();
    if(WorkViewDocument.isDocumentVersionUploaded(comments)){
      Log.Message("Document '"+title+"' new version added correctly.");
      return true;
    }else{
      Log.Error("Document '"+title+"' new version DO NOT added.");
      return false;
    }
  }else{
    Log.Error("Add new document version fails. Document NOT found with title = "+title);
    return false;
  }
}