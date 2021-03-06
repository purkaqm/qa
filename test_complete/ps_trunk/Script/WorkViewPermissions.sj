//USEUNIT ProjectNavigationMenu
//USEUNIT WorkPermissionsPage
//is Work ViewPermissions link present?
function isWorkViewPermissionsLinkPresent(){
  return ProjectNavigationMenu.existsViewPermissionsControl();
}
//opens permissions page from link
function openPermissionsPageFromLink(){
  return WorkPermissionsPage.openViewPermissionsPageFromLink();
}
//opens permissions page from url. reOpen=boolean; if page is open should reOpen it?
function openPermissionsPageFromUrl(reOpen){
  return WorkPermissionsPage.openPageFromUrl(reOpen);
}
