//USEUNIT WorkDocumentsPage
//USEUNIT WorkPage
//USEUNIT TextArea
  var _url=null; 
  var PAGE_IDENTIFIER="/project/DocumentDetails,$PSForm.sdirect";
  function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageDocumentEditDetails);
  }

  function isPageOpen(){ //returns wheter the page is open
    return (Page.isPageOpenAndHasPermission(WorkDocumentEditDetailsPage.PAGE_IDENTIFIER)?true:false); //if the current url contains one of both possible urls and toolbar is visible and update panel is displayed
  }
  //set the page url to the current browser url
  function setPageUrl(){
    WorkDocumentEditDetailsPage._url=Page.getUrl(WorkDocumentEditDetailsPage.isPageOpen());
    Log.Message("Setting DocumentEditDetailsPage URL = '"+_url+"'.");
  }
//returns a reference to the title text box
function getTitleTextBox(){
  return Aliases.browser.pageDocumentEditDetails.titleTextBox;
}
//returns a reference to the description text area
function getDescriptionTextArea(){
  return Aliases.browser.pageDocumentEditDetails.descriptionTextArea;
}
//sets the description text area text
function setDescriptionText(description){
  TextArea.clearTextAreaContent( WorkDocumentEditDetailsPage.getDescriptionTextArea());
  /*do{
    WorkDocumentEditDetailsPage.getDescriptionTextArea().Keys("[Hold]^a[Release][Del]");//Ctrl-A= select all
  }while(aqString.GetLength(WorkDocumentEditDetailsPage.getDescriptionTextArea().contentText) > 0);//while content is not deleted*/
  WorkDocumentEditDetailsPage.getDescriptionTextArea().Keys(description);
}
//clicks save changes and redirects to document details page
//ATTENTION: this method does not waits until the next page is loaded.
function clickSaveChangesControl(){
  Aliases.browser.pageDocumentEditDetails.saveChangesControl.ClickButton();
 //I can not do WorkDocumentDetailsPage.wait here because in WorkDocumentDetailsPage is imported this page and they can not reference each other.
}
//returns the panel2 contentText. in Panel2 is displeyed the error message when this page could not be displayed.
function getPanel2ContentText(){
  return Aliases.browser.pageDocumentEditDetails.Content.Container.panel2.contentText;
}