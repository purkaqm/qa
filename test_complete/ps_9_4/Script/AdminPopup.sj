
//returns the admin->Define_Categories link
function getDefineCategoriesLink(){
  Log.Message("popup_name ="+Aliases.browser.popupAdminBrowse.Name);
  return Aliases.browser.popupAdminBrowse.panelAdminMenu.defineCategoriesControl;
}