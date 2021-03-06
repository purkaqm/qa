//USEUNIT WorkPermissionsPage
//USEUNIT Table
//USEUNIT Panel
//USEUNIT QuickSearch
//add a new custom set on the project permissions page
function addUserOrGroupToProjectCustomSet(name, catName){//name="LastName, Name"
  Log.Message("Custom Set - Setting Role '"+name+"' and category '"+catName+"' permissions");
  var csIndex=Table.getRowIndexWithSubString(WorkPermissionsPage.getAddedRolesTable(), name, 1, true); //1=column index
  if (csIndex < 0){
    WorkPermissionsPage.clickAddRoleControl();//opens the search role popup
    WorkPermissionsPage.setSearchTextBox(name);
    var bool=WorkPermissionsPage.getSearchResultsPanel().Exists;
    WorkPermissionsPage.clickGoControl();
    Log.Message("Searching "+name+" on the roles result panel");   
    var item=Panel.getSubObjectByNameTypeAndId(WorkPermissionsPage.getSearchResultsPanel(), name, Page.OBJECT_TYPE_PANEL, null, true);
    
    if(null!=item){  //if found on search
      item.Click();
      Log.Message("Custom Set - Searching "+name+" on the selected roles panel");
      if(null != Panel.getChildByName(WorkPermissionsPage.getSelectedRolesPanel(), name, true)){//verifies that the user/group is added to the selected roles panel(the first panel)
        Log.Message("Custom Set - Selecting '"+name+"' from the roles search results pane.");
        WorkPermissionsPage.clickSaveAddedRoleControl();//adds the custom set
        WorkPermissionsPage.wait();
        csIndex=Table.getRowIndexWithSubString(WorkPermissionsPage.getAddedRolesTable(), name, 1, true); //1=column index
      }else{
        Log.Error("Custom Set - Role '"+name+"' was not found or the search procedure took too long time.");
        WorkPermissionsPage.clickCancelAddedRoleControl();
        return false;
      } 
    }else{
      Log.Error("Custom Set - Role '"+name+"' NOT found on search Results.");
      return false;
    }
  }
  
  var catIndex=Table.getColumnIndex(WorkPermissionsPage.getCustomSetRadioButtonsTable(), catName, 0, true); //0=row index
  if(Table.selectRadioButtonAt(WorkPermissionsPage.getCustomSetRadioButtonsTable(),csIndex,catIndex)){
    Log.Message("Custom Set '"+name+"' checked correctly.");
    WorkPermissionsPage.clickSaveCustomSetControl();
    return true;
  }else{
    Log.Error("Custom Set '"+name+"' NOT checked correctly.");
    return false;
  }
}
