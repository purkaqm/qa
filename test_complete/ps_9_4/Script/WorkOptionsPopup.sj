var WO_DELETE=11;
var WO_EDIT_DETAILS=12;
var WO_EDIT_PERMISSIONS=13;
var WO_VIEW_PERMISSIONS=14;

//returns the delete work link
function getDeleteControl(){
  return Aliases.browser.popupWorkOptions.deleteCell;
}
//returns the view permissions link
function getViewPermissionsControl(){
  return Aliases.browser.popupWorkOptions.viewPermissionsCell;
}
//returns the edit permissions link
function getEditPermissionsControl(){
  return Aliases.browser.popupWorkOptions.editPermissionsCell;
}
//returns the edit details link
function getEditDetailsControl(){
  return Aliases.browser.popupWorkOptions.editDetailsCell;
}