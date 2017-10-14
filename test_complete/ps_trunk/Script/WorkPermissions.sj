//USEUNIT WorkPermissionsPage

//returns if permissions page is open
function isPageOpen(){
  return WorkPermissionsPage.isPageOpen();
}
//returns if permissions page is Editable. 
//If returns true, user has edit permissions, when false; user has view permissions
function isPageEditable(){
  return WorkPermissionsPage.isEditable();
}