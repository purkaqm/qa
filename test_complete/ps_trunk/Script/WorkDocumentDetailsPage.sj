//USEUNIT Page
//USEUNIT WorkDocumentsPage
//USEUNIT WorkDocumentEditDetailsPage
  var _url=null;
  var PAGE_IDENTIFIER_1="/project/DocumentDetails.epage";//SEE DETAILS
  var PAGE_IDENTIFIER_2="/project/DocumentListing,$PSObjectLink_"; //UPDATE
  var PAGE_IDENTIFIER_3="/project/Summary1,$project$summary$Documents.$PSObjectLink";  //UPDATE
  var PAGE_IDENTIFIER_4="/project/DocumentDetails,$PSForm_0.sdirect"//after upload new version
  var PAGE_IDENTIFIER_5="/project/DocumentDetails.page"; //after edit details
  var COMMENTS_COLUMN_ID=5;
  var SEE_DETAILS_OPTION_LABEL="See details";
  var UPDATE_OPTION_LABEL="Update";
  function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageDocumentDetails);
  }
    
  function openPageFromUrl(reOpen){//navigates to Documents page
    Page.openFromUrl(Aliases.browser.pageDocumentDetails, _url, reOpen, WorkDocumentDetailsPage.isPageOpen());
    return WorkDocumentDetailsPage.isPageOpen();
  }
    
  function isPageOpen(){ //returns wheter the page is open
    return ((Page.isPageOpenAndHasPermission(WorkDocumentDetailsPage.PAGE_IDENTIFIER_1) 
          || Page.isPageOpenAndHasPermission(WorkDocumentDetailsPage.PAGE_IDENTIFIER_2)
          || Page.isPageOpenAndHasPermission(WorkDocumentDetailsPage.PAGE_IDENTIFIER_3) 
          || Page.isPageOpenAndHasPermission(WorkDocumentDetailsPage.PAGE_IDENTIFIER_4)
          || Page.isPageOpenAndHasPermission(WorkDocumentDetailsPage.PAGE_IDENTIFIER_5))?true:false); //if the current url contains "/project/DocumentListing" and toolbar is visible
  }
  //set the page url to the current browser url
  function setPageUrl(){//its called when a new document is added
    _url=Page.getUrl(WorkDocumentDetailsPage.isPageOpen());
    Log.Message("Setting DocumentDetailsPage URL = '"+_url+"'.");
  }

  //opens document details page by link(See details or Update) and depending on the boolean store parameter it stores or not the document details page url.   
function openDocumentOptionByLink(link, store){
    if(link.Exists){
      link.Click();
      WorkDocumentDetailsPage.wait();
      if(WorkDocumentDetailsPage.isPageOpen()){
        
        if(true == store){
          WorkDocumentDetailsPage.setPageUrl();
        }
        Log.Message("Project Details page Opened by link.");
        return true;        
      }else{
        Log.Error("Project Details page NOT Opened through Document Options link.");
        return false;
      }
    }else{
      Log.Error("Link is NOT displayed.");
      return false;
    }
}


//returns the description label.
function getDocumentDescriptionText(){
  return Aliases.browser.pageDocumentDetails.descriptionCell.contentText;
}
//returns the edit details link.
function getEditDetailsControl(){
  return Aliases.browser.pageDocumentDetails.editDetailsControl;
}
//esists edit details link? is it displayed?.
function existsEditDetailsControl(){
  return WorkDocumentDetailsPage.getEditDetailsControl().Exists;
}
//click the edit details link.
function clickEditDetailsControl(){
  WorkDocumentDetailsPage.getEditDetailsControl().Click();
  WorkDocumentEditDetailsPage.wait();
  WorkDocumentEditDetailsPage.setPageUrl();
}
//if exists the panel that contains the upload new version component.
function existsNewVersionPanel(){
  return Aliases.browser.pageDocumentDetails.newVersionPanel.Exists;
}
//sets the url textBox text.
function setNewVersionUrlText(url){
  Aliases.browser.pageDocumentDetails.newVersionPanel.newVersionUrlTextBox.SetText(url);
}
//returns the comments textArea.
function getNewVersionCommentsTextArea(){
  return Aliases.browser.pageDocumentDetails.newVersionPanel.newVersionCommentsTextArea;
}
//sets the comments text area.
function setNewVersionCommentsTextArea(comments){
  WorkDocumentDetailsPage.getNewVersionCommentsTextArea().Keys(comments);
}
//returns the browse file component.
function getNewVersionBrowseFileComponent(){
  return Aliases.browser.pageDocumentDetails.newVersionPanel.newVersionBrowseFileComponent;
}
//clicks the "Upload new version" link.
function clickUploadNewVersionControl(){
  Aliases.browser.pageDocumentDetails.newVersionPanel.uploadNewVersionControl.Click();
  WorkDocumentDetailsPage.wait();
}
//returns the document versions table.
function getDocumentVersionsTable(){
  return Aliases.browser.pageDocumentDetails.versionsPanel.documentVersionsTable;
}