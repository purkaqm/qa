//USEUNIT Browser 
//USEUNIT WorkSummaryPage
//USEUNIT WorkOptionsPopup
var WORK_OPTIONS=1;
var DOCUMENTS=2;
var WO_DELETE=11;
var WO_EDIT_DETAILS=12;
var WO_EDIT_PERMISSIONS=13;
var WO_VIEW_PERMISSIONS=14;
//get work options control.
function getWorkOptionsControl(){
  return Aliases.browser.paneToolBar.showOptionsPopupControl;
}
//get documents control.
function getDocumentsControl(){
  return Aliases.browser.paneToolBar.documentsControl;
}
//returns the project name displayed on work Summary
function getProjectName(){
  return Aliases.browser.paneToolBar.projectName;
}
//returns the project name displayed on work Summary
function existsProjectName(){
  return ToolBarLinks.getProjectName().Exists;
}

//is work tool bar link present?
function isToolBarItemPresent(item){
  var link=ToolBarLinks.getToolBarItem(item);
  if(null!=link){
    return link.Exists;
  }else{
    return false;
  }
}
//returns a work toolbar link. Logs an error and returns Null if item value is invalid
function getToolBarItem(item){
    if(!WorkSummaryPage.openPageFromUrl(false)){//this line is necesary to return from a page that user has not permissions to see.
      Log.Error("Work Summary NOT opened trying to get item #"+item);
    }
    switch(item){
      case ToolBarLinks.WORK_OPTIONS: return ToolBarLinks.getWorkOptionsControl();
      case ToolBarLinks.DOCUMENTS: return ToolBarLinks.getDocumentsControl();
      default: Log.Error("Work ToolBar Item: '"+item+"' not found."); return null;
    }
}

//is Options-> X link present?
function isWorkOptionsSubItemPresent(item){
  var link=ToolBarLinks.getWorkOptionsSubItem(item);
  if(null!=link){
    return link.Exists;
  }else{
    return false;
  }
}
//returns a work sub option link. Logs an error and returns Null if work options control or item link are not present or the item value is invalid
function getWorkOptionsSubItem(item){
  if(ToolBarLinks.isToolBarItemPresent(ToolBarLinks.WORK_OPTIONS)){
    ToolBarLinks.getToolBarItem(ToolBarLinks.WORK_OPTIONS).Click();
    switch(item){
      case ToolBarLinks.WO_DELETE: return WorkOptionsPopup.getDeleteControl();
      case ToolBarLinks.WO_VIEW_PERMISSIONS: return WorkOptionsPopup.getViewPermissionsControl();
      case ToolBarLinks.WO_EDIT_PERMISSIONS: return WorkOptionsPopup.getEditPermissionsControl();
      case ToolBarLinks.WO_EDIT_DETAILS: return WorkOptionsPopup.getEditDetailsControl();
      default: Log.Error("Work Options Item: '"+item+"' not found."); return null;
    }
  }else{
    Log.Error("Work Options Control does NOT exist.");
    return null;
  }
}