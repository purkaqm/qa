//USEUNIT IconMenu
//USEUNIT NavigationMenu

function getReportWizardControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), "Report", "Report Wizard"); 
}
function getCreateReportsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), "Report", "Create Reports");
}
function getUserControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), null, "User");
}
function getOrganizationControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), null, "Organization");
}
function getOtherWorkControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), null, "Other Work");
}
function getIdeaControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), null, "Idea");
}
function getProjectControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAddControl(), null, "Project");
}