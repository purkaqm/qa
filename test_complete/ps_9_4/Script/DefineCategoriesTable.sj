//USEUNIT DefineCategoriesPage
//USEUNIT Table

//sets work define permissions
function defineWorkItemPermissionsFromCategoryRole(catName, permValues){
  return DefineCategoriesTable.definePermissionsFromCategoryRole(catName, permValues, DefineCategoriesPage.WORK_ITEM_TYPE)
}
//sets the define permissions table checkboxes acording to a category role and an array of [permissionName,booleanValue]
//itemTarget=object type to define permissions (page selector)
function definePermissionsFromCategoryRole(catName, permValues, itemTarget){
  Log.Message("Defining permissions to the category '"+catName+"' with values "+permValues+".");
  DefineCategoriesPage.openPageFromLink();
  if(null != itemTarget){
    DefineCategoriesPage.selectTarget(itemTarget);
    DefineCategoriesPage.wait();
  }
  var catIndex=Table.getColumnIndex(DefineCategoriesPage.getCategorieRolesTable(),catName);
  var permIndex, permName, permValue,cell;
  var checked=false;
  for(var i=0;i<permValues.length;i++){
    permName=permValues[i][0];
    permValue=permValues[i][1];
    permIndex=Table.getRowIndex(DefineCategoriesPage.getPermissionNamesTable(),permName);
    if(Table.selectCheckBoxAt(DefineCategoriesPage.getCheckBoxesTable(),permIndex,catIndex,permValue)){
      Log.Message("Checking '"+permName+"' to "+permValue+".");
      checked=true;
    }else{
      checked=false;
      break;
    }
  }
  if(checked){
    DefineCategoriesPage.submitUpdate();
    Log.Message("All Permissions CheckBoxes were checked correctly.");
    return true;
  }else{
    Log.Error("One or more Permissions CheckBoxes were NOT checked correctly.");
    return false;
  }
  
}