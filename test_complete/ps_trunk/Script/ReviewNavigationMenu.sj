//USEUNIT NavigationMenu
//USEUNIT IconMenu

//simple or one level menu items
function getDashboardControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Dashboard");
}
function getVisualPortalControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Visual Portal");
}
function getManageLayoutsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Manage layouts");
}
function getAddVisualPortalControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Add Visual Portal");
}
function getResourceReviewControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Resource Review");
}
function getExecutiveReviewControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Executive Review");
}
function getFinancialReviewControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Financial Review");
}
function getPortfoliosControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Portfolios");
}
function getMeasuresLibraryControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Measures Library");
}
function getMetricBulkActionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), null, "Metric Bulk Actions");
}

//Sub items of Reports
function getMyReportsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Reports", "My Reports");  
}
function getPublicReportsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Reports", "Public Reports"); 
}
function getManageReportsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Reports", "Manage Reports"); 
}
function getAdvancedReportsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Reports", "Advanced Reports"); 
}
//sub items of User Management
function getFindAPersonControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Find a Person"); 
}
function getGroupsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Groups");   
}
function getResourcePoolControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Resource Pool");   
}
function getCertificationControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Certification");   
}
function getManageTimeControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Manage Time");   
}
function getResourceRatesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "User Management", "Resource Rates");   
}
//sub items of Import/Export
function getMetricsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Import/Export", "Metrics");   
}
function getUsersControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Import/Export", "Users");    
}
function getTagsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Import/Export", "Tags");    
}
function getTimesheetsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Import/Export", "Timesheets");    
}
function getCostsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getReviewControl(), "Import/Export", "Costs");    
}