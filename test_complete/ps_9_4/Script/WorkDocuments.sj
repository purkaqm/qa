//USEUNIT WorkDocumentsPage
//USEUNIT WorkDocumentDetailsPage
//USEUNIT ToolBarLinks
//USEUNIT BrowseFile
//USEUNIT Work
//USEUNIT Table
//add a new document. When you want to add a document by url, specifie the url and let fileName and filePath as null.
//if you want to add a local file, let url = null and specifie the file name and the file's directory path(where it is located in).
function addNewDocument(pName, title, description, url, fileName, filePath){
  Log.Message("Adding a new document to "+pName+". Document title = "+title+". Document description = "+description+". Document url = "+url+". Local file name = "+fileName+". Local file path = "+filePath+".");
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
  WorkDocumentsPage.openPageFromLink();
  WorkDocumentsPage.getNewDocumentGeneralControl().click();
  WorkDocumentsPage.setNewDocumentTitle(title);
  WorkDocumentsPage.setNewDocumentDescription(description);
  if(null != url){
    WorkDocumentsPage.setNewDocumentUrl(url);
  }else{
    if(null != fileName && null != filePath){
      BrowseFile.browseFile(WorkDocumentsPage.getFileBrowseComponent(),filePath,fileName);
    }
  }
  WorkDocumentsPage.clickAddDocumentDocumentControl();
  WorkDocumentsPage.clickDoneControl();
  if(WorkDocuments.isDocumentAddedAndOpened(pName, title)){
    Log.Message("Document '"+title+"' added correctly.");
    return true;
  }else{
    Log.Error("Document '"+title+"' DO NOT added.");
    return false;
  }
}
//searches a document by title and returns if found
function isDocumentAddedAndOpened(pName, docTitle){
  return (null != WorkDocuments.getDocumentByTitle(pName, docTitle)?true:false);
}
//exists the add new document control or here link.
function existsNewDocumentGeneralControl(pName){
  Work.openWork(pName);
  WorkDocumentsPage.openPageFromLink();
  return WorkDocumentsPage.existsNewDocumentGeneralControl();
}
//is Document link present?
function isWorkDocumentsLinkPresent(pName){
  Work.openWork(pName);
  return ToolBarLinks.isToolBarItemPresent(ToolBarLinks.DOCUMENTS);
}

//returns the document searched by title; null if not found
function getDocumentByTitle(pName, docTitle){
  Work.openWork(pName);
  WorkDocumentsPage.openPageFromLink();
  if(WorkDocumentsPage.existsDocumentsListTable()){
    var docIndex=Table.getRowIndex(WorkDocumentsPage.getDocumentsListTable(),docTitle,WorkDocumentsPage.DOCUMENTS_NAME_COLUMN_INDEX,true);
    if(docIndex >= 0){
      var cell=Table.getCellAt(WorkDocumentsPage.getDocumentsListTable(), docIndex, WorkDocumentsPage.DOCUMENTS_NAME_COLUMN_INDEX);
      if(null!=cell){
        Log.Message("Document is added to the current project. Document title='"+docTitle+"'.");
        return cell.Child(1);//child(1)=link  
      }else{
        Log.Error("Error occurred getting Document cell. Document title='"+docTitle+"'.");
        return null;
      }
    }else{
      Log.Message("Document is NOT added to the current project. Document title='"+docTitle+"'.");
      return null;
    }
  }else{
    Log.Message("There are NOT documents added to the current project. Document title='"+docTitle+"'. Or Documents table NOT found.");
    return null;
  }
}