//USEUNIT Page
//USEUNIT ProjectNavigationMenu
//USEUNIT WorkPage
  var _url=null;
  var PAGE_IDENTIFIER="/project/DocumentListing.epage";
  var MAX_WAIT_TIME=600; //max wait time in seconds; 10 minutes
  var DOCUMENTS_NAME_COLUMN_INDEX=1;//the documents name column index

  function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageDocuments);
  }
    
  function openPageFromUrl(reOpen){//navigates to Documents page
    Page.openFromUrl(Aliases.browser.pageDocuments, _url, reOpen, WorkDocumentsPage.isPageOpen());
    return WorkDocumentsPage.isPageOpen();
  }
    
  function isPageOpen(){ //returns wheter the page is open
    return Page.isPageOpenAndHasPermission(WorkDocumentsPage.PAGE_IDENTIFIER); //if the current url contains "/project/DocumentListing" and toolbar is visible
  }
    
  //if documents page is not open clicks toolbar -> Documents link and stores the new page url
  function openPageFromLink(){
    if(!WorkDocumentsPage.isPageOpen()){
      ProjectNavigationMenu.getDocumentsControl().Click();
      WorkDocumentsPage.wait();
      _url=Page.getUrl(WorkDocumentsPage.isPageOpen());
      Log.Message("Setting DocumentsPage URL = '"+_url+"'.");
    }
  }
  //is the welcome panel prensent. It means that there are not documents added now...and the welcome paga exists.
  function existsDocumentsWelcomePanel(){
    return Aliases.browser.pageDocuments.panelContent.panelBoxWelcome.Exists;
  }
  //returns the "here" link displayed on documents welcome page(displayed when there aren't documents to display.) It displays the add document popup
  function getWelcomeHereControl(){
    return Aliases.browser.pageDocuments.panelContent.panelBoxWelcome.clickHereControl;
  }
  //returns "add new" button
  function getAddNewControl(){
    return Aliases.browser.pageDocuments.panelContent.panelContainer.addNewControl;
  }
  //sets the addDocumentPopup->Title value
  function setNewDocumentTitle(title){
    Aliases.browser.pageDocuments.popupAddDocument.titleTextBox.SetText(title);
  }
  //sets the addDocumentPopup->URL value
  function setNewDocumentUrl(url){
    Aliases.browser.pageDocuments.popupAddDocument.urlTextbox.SetText(url);
  }
  //returns a reference to addDocumentPopup->Description
  function getNewDocumentDescriptionTextArea(){
    return Aliases.browser.pageDocuments.popupAddDocument.descriptionTextArea;
  }
  //sets the addDocumentPopup->Description value
  function setNewDocumentDescription(description){
    //WorkDocumentsPage.getNewDocumentDescriptionTextArea().select();
    WorkDocumentsPage.getNewDocumentDescriptionTextArea().Keys(description);
  }
  //clicks the add documents popup "Add Document" button.
  function clickAddDocumentDocumentControl(){
    Aliases.browser.pageDocuments.popupAddDocument.addDocumentControl.ClickButton();
  }
  //clicks the add documents popup "Done" button and waits until document is saved.
  function clickDoneControl(){
    Aliases.browser.pageDocuments.popupAddDocument.doneControl.ClickButton();
    WorkDocumentsPage.wailtUntilDocumentIsSaved();
  }
  //returns the wait panel
  function getWaitPanel(){
    return Aliases.browser.pageDocuments.popupAddDocument.panelUploadWait.panelScrollText.PleaseWaitTextNode;
  }
  //exist the wait panel? (still loading?)
  function existsWaitPanel(){
    return WorkDocumentsPage.getWaitPanel().Exists;
  }
  function getDocumentsListTable(){
    return Aliases.browser.pageDocuments.panelContent.panelContainer.documentsListTable;
  }
  function existsDocumentsListTable(){
    return getDocumentsListTable().Exists;
  }
  //returns the add document control or here link, depending on if there are added documents
  function getNewDocumentGeneralControl(){
    if(WorkDocumentsPage.existsDocumentsWelcomePanel()){
      return WorkDocumentsPage.getWelcomeHereControl(); //only the first document on a project is added from here
    }else{
      return WorkDocumentsPage.getAddNewControl();
    }
  }
  //exists the add document control or here link, depending on if there are added documents
  function existsNewDocumentGeneralControl(){
    return WorkDocumentsPage.getNewDocumentGeneralControl().Exists;
  }
  //returns the file browse component and opens the file browse window
  function getFileBrowseComponent(){
    return Aliases.browser.pageDocuments.popupAddDocument.browseFileComponent;
  }
  //waits until document is saved
  function wailtUntilDocumentIsSaved(){
    var c=0;
    while(WorkDocumentsPage.existsWaitPanel() && c < WorkDocumentsPage.MAX_WAIT_TIME){
      Aliases.browser.pageDocuments.WaitSlApplication(1000);//wait 1 second
      c++;
    }
  }