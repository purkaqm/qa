//USEUNIT IconMenu
//USEUNIT NavigationMenu

function getLogsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), null, "Logs");
}
function getAnnouncementsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), null, "Announcements");
}

//Permissions items
function getDefineCategoriesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Permissions", "Define Categories");
}
function getDefaultsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Permissions", "Defaults");
}
function getAdminGroupControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Permissions", "Admin Group");
}
//Templates items
function getWorkControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Templates", "Work");
}
function getMetricsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Templates", "Metrics");
}
function getPowerPointControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Templates", "Power Point");
}
function getTimesheetLoaderControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Templates", "Timesheet Loader");
}
function getCostLoaderControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Templates", "Cost Loader");
}
//Layouts items
function getDashboardControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Layouts", "Dashboard");
}
function getExecutiveReviewControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Layouts", "Executive Review");
}
function getFinancialReviewControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Layouts", "Financial Review");
}
//Localization items
function getWorkStatusNamesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Localization", "Work Status Names");
}
function getReplaceableTermsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Localization", "Replaceable Terms");
}
//Configuration items
function getAlertSubscriptionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Alert Subscriptions");
}
function getAgentsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Agents");
}
function getBrandingControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Branding");
}
function getChartsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Charts");
}
function getConditionsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Conditions");
}
function getCustomFieldsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Custom Fields");
}
function getExchangeRatesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Exchange Rates");
}
function getFieldManagementControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Field Management");
}
function getHelpControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Help");
}
function getHelpDeskRequestsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Help Desk Requests");
}
function getHomePageControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Home Page");
}
function getIdeaHoppersControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Idea Hoppers");
}
function getImportantLinksControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Important Links");
}
function getObjectTypesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Object Types");
}
function getPhaseAdvanceControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Phase Advance");
}
function getProcessesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Processes");
}
function getResourceCalendarControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Resource Calendar");
}
function getResourcePlanningControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Resource Planning");
}
function getRisksControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Risks");
}
function getSearchSynonymsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Search Synonyms");
}
function getStatusReportTemplatesControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Status Report Templates");
}
function getTagsControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getAdminControl(), "Configuration", "Tags");
}