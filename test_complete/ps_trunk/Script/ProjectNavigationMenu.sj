//USEUNIT IconMenu
//USEUNIT NavigationMenu
//returns WorkTree link from Project's navigation menu
function getWorkTreeControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Work Tree");
}
//returns true if WorkTree link is displayed on Project's navigation menu
function existsWorkTreeControl(){
  if(null!=getWorkTreeControl()){
    return true;
  }else{
    return false;
  }
}

//returns Delete link from Project's navigation menu
function getDeleteControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Delete");
}
//returns true if Delete link is displayed on Project's navigation menu
function existsDeleteControl(){
  if(null!=getDeleteControl()){
    return true;
  }else{
    return false;
  }
}

//returns Summary link from Project's navigation menu
function getSummaryControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Project Summary");
}
//returns true if Summary link is displayed on Project's navigation menu
function existsSummaryControl(){
  if(null!=getSummaryControl()){
    return true;
  }else{
    return false;
  }
}

//returns Documents link from Project's navigation menu
function getDocumentsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Documents");
}
//returns true if Documents link is displayed on Project's navigation menu
function existsDocumentsControl(){
  if(null!=getDocumentsControl()){
    return true;
  }else{
    return false;
  }
}

//returns Discussions link from Project's navigation menu
function getDiscussionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Discussions");
}
//returns true ifDiscussions link is displayed on Project's navigation menu
function existsDiscussionsControl(){
  if(null!=getDiscussionsControl()){
    return true;
  }else{
    return false;
  }
}

//returns Issues link from Project's navigation menu
function getIssuesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Issues");
}
//returns true ifIssues link is displayed on Project's navigation menu
function existsIssuesControl(){
  if(null!=getIssuesControl()){
    return true;
  }else{
    return false;
  }
}

//returns Edit Details link from Project's navigation menu
function getEditDetailsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), null, "Edit details");
}
//returns true if Edit Details link is displayed on Project's navigation menu
function existsEditDetailsControl(){
  if(null!=getEditDetailsControl()){
    return true;
  }else{
    return false;
  }
}

//returns Edit Permissions link from Project's navigation menu
function getEditPermissionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), "More", "Edit Permissions");
}
//returns true if Edit Permissions link is displayed on Project's navigation menu
function existsEditPermissionsControl(){
  if(existsMoreControl()){
    if(null!=getEditPermissionsControl()){
      return true;
    }else{
      return false;
    }
  }else{
    return false;
  }
}

//returns View Permissions link from Project's navigation menu
function getViewPermissionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), "More", "View Permissions");
}
//returns true if View Permissions link is displayed on Project's navigation menu
function existsViewPermissionsControl(){
  if(existsMoreControl()){
    if(null!=getViewPermissionsControl()){
      return true;
    }else{
      return false;
    }
  }else{
    return false;
  }
}

//returns Copy / Move link from Project's navigation menu
function getCopyMoveControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getProjectControl(), "More", "Copy / Move");
}
//returns true if Copy / Move link is displayed on Project's navigation menu
function existsCopyMoveControl(){
  if(existsMoreControl()){
    if(null!=getCopyMoveControl()){
      return true;
    }else{
      return false;
    }
  }else{
    return false;
  }
}