//USEUNIT Browser 
//USEUNIT Page
//USEUNIT AdminNavigationMenu
var WORK_ITEM_TYPE="Project Tasks";//work item selector value
function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageDefinePermissions);
}
//returns a table with the names of the category roles displayed in DefinePermissions page, the last column corresponds to the add new category role
function getCategorieRolesTable(){
  return Aliases.browser.pageDefinePermissions.categoriesTable;
}
//returns a table with the names of the permission(no permission verbs) displayed in DefinePermissions page; the first row is a empty row
function getPermissionNamesTable(){
  return Aliases.browser.pageDefinePermissions.permissionNamesTable;
}
//returns a table with the checkboxes displayed in DefinePermissions page
function getCheckBoxesTable(){
  return Aliases.browser.pageDefinePermissions.checkBoxesTable;
}

//click Admin->Define_Categories link
function openPageFromLink(){
  AdminNavigationMenu.getDefineCategoriesControl().Click();
  DefineCategoriesPage.wait();
}

//select one option from drop-down; wi,users,groups,context,etc.
function selectTarget(target){
  Aliases.browser.pageDefinePermissions.selectProperty.ClickItem(target);
}
//click update control
function submitUpdate(){
  Aliases.browser.pageDefinePermissions.updateControl.ClickButton();
  DefineCategoriesPage.wait();
}
