
//returns the browse->Work_Tree link
function getWorkTreeLink(){
  Log.Message("exists wt="+Aliases.browser.popupAdminBrowse.panelBrowseMenu.workTreeControl.Exists);
  Log.Message("visible wt="+Aliases.browser.popupAdminBrowse.panelBrowseMenu.workTreeControl.Visible);
  Log.Message("popup_name ="+Aliases.browser.popupAdminBrowse.Name);
  return Aliases.browser.popupAdminBrowse.panelBrowseMenu.workTreeControl;
}
//returns the browse->Create_A_Project link
function getCreateNewProjectLink(){
  Log.Message("popup_name ="+Aliases.browser.popupAdminBrowse.Name);
  return Aliases.browser.popupAdminBrowse.panelBrowseMenu.createNewWorkControl;
}
