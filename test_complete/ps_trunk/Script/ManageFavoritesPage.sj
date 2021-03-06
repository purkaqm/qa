//USEUNIT PersonSettingsProfilePage
//checkboxes on manage favorites page
function getFirstPinCheckBox(){
  return Aliases.browser.pageFavorites.panelBodyContainer.panelPagecontent.panelContent.panelContainerElastic.panelFavouritegridid.panelGrid.panelBody.panelSectionbodyFavmaster.panel.panelColumnCenter.panelPage.panel.panelCell.panelCellcontent.checkBox;
}
function getSecondPinCheckBox(){
  return Aliases.browser.pageFavorites.panelBodyContainer.panelPagecontent.panelContent.panelContainerElastic.panelFavouritegridid.panelGrid.panelBody.panelSectionbodyFavmaster.panel.panelColumnCenter.panelPage.panel_1.panelCell.panelCellcontent.checkbox;
}
function isFistPinCheckBoxChecked(){
  var sel=ManageFavoritesPage.getFirstPinCheckBox().checked;
  if(sel){
    Log.Message("First Check Box is selected.");
  }else{
    Log.Message("First Check Box is not selected.");
  }
  return sel;
}
function isSecondPinCheckBoxChecked(){
  var sel=ManageFavoritesPage.getSecondPinCheckBox().checked;
  if(sel){
    Log.Message("Second Check Box is selected.");
  }else{
    Log.Message("Second Check Box is not selected.");
  }
  return sel;
}


//home page name cell on manage favorites page
function getHomePageNameCell(){
  return Aliases.browser.pageFavorites.panelBodyContainer.panelPagecontent.panelContent.panelContainerElastic.panelFavouritegridid.panelGrid.panelBody.panelPsUiReordercontroller0.panel.panelColumnEditable.panelPage.panelHome.panelCellHome;
}
function existsHomePageFavoriteFavoriteOnExportTable(){
  var res=ManageFavoritesPage.getHomePageNameCell().Exists;
  if(res){
    Log.Message("Home page favorite exists on Export table.");
  }else{
    Log.Message("Home page favorite does not exist on Export table.");
  }
  return res;
}

//profile page name cell on manage favorites page
function getProfilePageNameCell(){
  return Aliases.browser.pageFavorites.panelBodyContainer.panelPagecontent.panelContent.panelContainerElastic.panelFavouritegridid.panelGrid.panelBody.panelPsUiReordercontroller0.panel.panelColumnEditable.panelPage.panelMyProfile.panelCellProfile;
}

function existsProfilePageFavoriteFavoriteOnExportTable(){
  var res=ManageFavoritesPage.getProfilePageNameCell().Exists;
  if(res){
    Log.Message("Profile page favorite exists on Export table.");
  }else{
    Log.Message("Profile page favorite does not exist on Export table.");
  }
  return res;
}

function getProfileLink(){
  return Aliases.browser.pageFavorites.panelBodyContainer.panelPagecontent.panelSub.panelContainerClearfix.textnodeClearfix.textnodeNl.profileControl;
}
function openProfilePage(){
  ManageFavoritesPage.getProfileLink().Click();
  PersonSettingsProfilePage.wait();
}





