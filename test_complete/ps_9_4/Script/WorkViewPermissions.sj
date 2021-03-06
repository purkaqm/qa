//USEUNIT ToolBarLinks
//USEUNIT WorkPermissionsPage
//is Work ViewPermissions link present?
function isWorkViewPermissionsLinkPresent(){
  if(ToolBarLinks.isToolBarItemPresent(ToolBarLinks.WORK_OPTIONS)){
    var pre=ToolBarLinks.isWorkOptionsSubItemPresent(ToolBarLinks.WO_VIEW_PERMISSIONS);
    return pre;
  }else{
    return false;
  }
}
//opens permissions page from link
function openPermissionsPageFromLink(){
  return WorkPermissionsPage.openViewPermissionsPageFromLink();
}
//opens permissions page from url. reOpen=boolean; if page is open should reOpen it?
function openPermissionsPageFromUrl(reOpen){
  return WorkPermissionsPage.openPageFromUrl(reOpen);
}
