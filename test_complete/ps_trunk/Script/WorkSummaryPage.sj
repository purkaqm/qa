//USEUNIT Browser 
//USEUNIT Page
//USEUNIT WorkPage
//USEUNIT HeaderPanel
//USEUNIT NavigationMenu
    var _url=null;
    var PAGE_IDENTIFIER="/project/Summary";
    function wait(){//Waits until the browser loads the page.
      Page.wait(Aliases.browser.pageSummary);
    }
    
    function openPageFromUrl(reOpen){//navigates to summary page
      Page.openFromUrl(Aliases.browser.pageSummary, _url, reOpen, WorkSummaryPage.isPageOpen());
      return WorkSummaryPage.isPageOpen();
    }
    function pageTitleContainsPreferencesText(){
      return HeaderPanel.pageTitleContainsText(" : Summary");
    }
    function isPageOpen(){ //returns wheter the page is open
      return (Page.isPageOpenAndHasPermission(PAGE_IDENTIFIER) && WorkSummaryPage.pageTitleContainsPreferencesText());//if the current url contains "/project/Summary" and the title contains " : Summary"
    }
    //set the page url to the current browser url
    function setPageUrl(){//its called when a new project is created
      _url=Page.getUrl(WorkSummaryPage.isPageOpen());
      Log.Message("Setting SummaryPage URL = '"+_url+"'.");
    }
//opens the project and stores the summary page url    
function openPageFromLink(projectLink){
  projectLink.Click();
  WorkSummaryPage.wait();
  WorkSummaryPage.setPageUrl();
  return WorkSummaryPage.isPageOpen();
}
function getParentProject(){
  return Aliases.browser.pageSummary.panelContent.panelContainer.panelBlockClearfixSummary.table.cellFirst.tableSimple.cellParent.parentProject;
}
//returns the summary page reference
//Aliases.browser.pageSummary
function getPageSummary(){
  return Aliases.browser.pageSummary;
}

//returns a reference to sys.browser.pageSummary.panelContent.panelContainer.panelBlock.panelDetailsbody.tableSimple.cell.panelLeft.descriptionPanel
function getDescription(){
  return Aliases.browser.pageSummary.panelContent.descriptionPanel;
}
function getAddNewDescendantControl(){
  return Aliases.browser.pageSummary.panelContent.panelContainer.descendantsBlock.panelRight.addNewDescendantControl;
}
function getDescendantsBodyPanel(){
  return Aliases.browser.pageSummary.panelContent.panelContainer.descendantsBlock.panelDescendants.panelDescendantsGridID.panelGrid.panelBody;
}
//returns the value of the work summary description
function getDescriptionValue(){
  var tit=WorkSummaryPage.getDescription().Child(0).contentText;//the description title
  var full=WorkSummaryPage.getDescription().contentText;//the whole description with title
  var desc = aqString.SubString(full, (tit.length + 1) , (full.length - tit.length)); //+1 because \n is the char next to the title
  Log.Message("Project Description = '"+desc+"'.");
  return desc;
}

function existsProjectName(){
  if(HeaderPanel.getTitle().Exists && NavigationMenu.isProjectNavigationMenuDisplayed()){
    return true;
  }else{
    return false;
  }
}
function isProjectNameEqualsTo(pName){
  var title=HeaderPanel.getTitle();
  if(title.Exists){
    if(!aqString.Compare(pName+" : Summary",title.contentText,true)){
      return true;
    }else{
      return false;
    }
  }else{
    Log.Error("Project name was NOT found. Project Name NOT displayed");
    return false;
  }
}
