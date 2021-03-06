//USEUNIT AcceptanceSettings
//USEUNIT AddNavigationMenu
//USEUNIT AdminNavigationMenu
//USEUNIT Assert
//USEUNIT Browser
//USEUNIT HeaderPanel
//USEUNIT HomeNavigationMenu
//USEUNIT Home
//USEUNIT IconMenu
//USEUNIT Logging
//USEUNIT NavigationMenu
//USEUNIT PersonSettingsMenu
//USEUNIT ReviewNavigationMenu
//USEUNIT HomeNavigationMenuController
//these test cases belong to the test suite: "https://upland.testrail.com/index.php?/suites/view/770"
//"User interface, Icon bar and navigation menu" group
function runTest(adminUser, adminPass){
  Logging.doLogin(adminUser, adminPass); //admin login
  //TC #1184452:
  Assert.assertValue(true, headerBarTest(AcceptanceSettings.USER_FULL_NAME), "TC #1184452: Testing the header bar (logo, Search Icon, Favorites Icon, Help Icon, userName, logOut).");
  Assert.assertValue(true, homePageTest(), "TC #1184452: Testing Home Page title.");
  //TC #1196367:
  Assert.assertValue(true, iconMenuTest(), "TC #1196367: Testing all IconMenu Icons (Home, Add, Admin, Review, Project, Inbox, Favirites, Important Links and History).");
  //TC #1184453:
  Assert.assertValue(true, addNavigationMenuTest(), "TC #1184453: Testing all Icons on Add Navigation Menu(Project, Idea, User, Organization, Other Work, Report Wizard and Create Reports).");
  //TC #1196368:
  Assert.assertValue(true, reviewNavigationMenuTest(), "TC #1196368: Testing all Icons on Review Navigation Menu.");
  //TC #1196369:
  Assert.assertValue(true, adminNavigationMenuTest(), "TC #1196369: Testing all Icons on Admin Navigation Menu.");
  //TC #1184454:
  Assert.assertValue(true, verifyPinNavMenu(), "TC #1184454: Pin function on Navigation Menu.");//this assert is susceptible to failures because it compares two images and images are displayed different in each browser.
  //TC #1184455:
  Assert.assertValue(true, verifyConfigureHomePage(), "TC #1184455: Open Configure Home page link and verify opened page.");
  Logging.doLogOut();
}

function verifyConfigureHomePage(){
  if(HomeNavigationMenuController.clickConfigureHomePageControl()){
    var res=true;
    if(!PersonSettingsPreferencesPage.isPageOpen()){
      res=false;
      Logger.logWarning("Person Settings Preferences page is not open.");
    }
    //preferences tab verification is included in this first verification...PersonSettingsPreferencesPage.isPageOpen()
    
    if(!PersonSettingsMenu.getAlertSubscriptionsTab().Exists){
      res=false;
      Logger.logWarning("Alert Subscriptions tab is not displayed on Person Settings' page Menu.");
    }
    if(!PersonSettingsMenu.getFavoritesTab().Exists){
      res=false;
      Logger.logWarning("Favorites tab is not displayed on Person Settings' page Menu.");
    }
    if(!PersonSettingsMenu.getHistoryTab().Exists){
      res=false;
      Logger.logWarning("History tab is not displayed on Person Settings' page Menu.");
    }
    if(!PersonSettingsMenu.getPermissionsTab().Exists){
      res=false;
      Logger.logWarning("Permissions tab is not displayed on Person Settings' page Menu.");
    }
    if(!PersonSettingsMenu.getPersonalRatesTab().Exists){
      res=false;
      Logger.logWarning("Personal Rates tab is not displayed on Person Settings' page Menu.");
    }
    if(!PersonSettingsMenu.getProfileTab().Exists){
      res=false;
      Logger.logWarning("Profile tab is not displayed on Person Settings' page Menu.");
    }
    return res;
  }else{
    Logger.logWarning("Configure Home page item is not displayed on Navigation Menu.");
    return false;
  }
}
//verifies if nav menu is pined by default and then verifies that the pin functionality works
function verifyPinNavMenu(){
  if(NavigationMenu.isNavigationMenuDisplayed()){
    var pined=NavigationMenu.isMenuPined();
    if(null == pined){
      Logger.logWarning("Pin icon #1 is null.");
      return false;
    }
    if(pined){//should be pined
      HeaderPanel.getFavoritesControl().HoverMouse();//it goes out to the nav menu
      if(NavigationMenu.isNavigationMenuDisplayed()){
        if(!NavigationMenu.isBodyContainerProperlyAligned()){
          Logger.logWarning("BodyContainer is not properly aligned. Body Container is hidden behind the Navigation Menu. Error #1");
          return false;
        }
        NavigationMenu.getNavigationMenuTitle().HoverMouse();
        NavigationMenu.getPinIcon().Click();//unpin
        pined=NavigationMenu.isMenuPined();
        if(null == pined){
          Logger.logWarning("Pin icon #2 is null.");
          return false;
        }
        if(! pined){//should not be pined
          HeaderPanel.getFavoritesControl().HoverMouse();//it goes out to the nav menu
          if(!NavigationMenu.isNavigationMenuDisplayed()){
            if(!NavigationMenu.isBodyContainerProperlyAligned()){
              Logger.logWarning("BodyContainer is not properly aligned. Body Container is hidden behind the Navigation Menu. Error #2");
              return false;
            }
            return true;
          }else{
            Logger.logWarning("Navigation menu is fixed when the pin icon is unpined.");
            return false;
          }
        }else{
          Logger.logWarning("Pin icon does not change from pined to unpined.");
          return false;
        }
      }else{
        Logger.logWarning("Navigation menu is not fixed.");
        return false;
      }
    }else{
      Logger.logWarning("Navigation menu is not pined by default.");
      return false;
    }
  }else{
    Logger.logWarning("Navigation menu is not fixed by default.");
    return false;
  }
} 
function adminNavigationMenuTest(){
   var res= true;
   if(IconMenu.getAdminControl().Exists){
     var link=AdminNavigationMenu.getLogsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Logs")){
       res=false;
     }
     link=AdminNavigationMenu.getAnnouncementsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Announcements")){
       res=false;
     }

     //Configuration items
     link=AdminNavigationMenu.getAlertSubscriptionsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Alert Subscriptions")){
       res=false;
     }
     link=AdminNavigationMenu.getAgentsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Agents")){
       res=false;
     }
     link=AdminNavigationMenu.getBrandingControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Branding")){
       res=false;
     }
     link=AdminNavigationMenu.getChartsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Charts")){
       res=false;
     }
     link=AdminNavigationMenu.getConditionsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Conditions")){
       res=false;
     }
     link=AdminNavigationMenu.getCustomFieldsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Custom Fields")){
       res=false;
     }
     link=AdminNavigationMenu.getExchangeRatesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Exchange Rates")){
       res=false;
     }
     link=AdminNavigationMenu.getFieldManagementControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Field Management")){
       res=false;
     }
     link=AdminNavigationMenu.getHelpControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Help")){
       res=false;
     }
     link=AdminNavigationMenu.getHelpDeskRequestsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Help Desk Requests")){
       res=false;
     }
     link=AdminNavigationMenu.getHomePageControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Home Page")){
       res=false;
     }
     link=AdminNavigationMenu.getIdeaHoppersControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Idea Hoppers")){
       res=false;
     }
     link=AdminNavigationMenu.getImportantLinksControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Important Links")){
       res=false;
     }
     link=AdminNavigationMenu.getObjectTypesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Object Types")){
       res=false;
     }
     link=AdminNavigationMenu.getPhaseAdvanceControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Phase Advance")){
       res=false;
     }
     link=AdminNavigationMenu.getProcessesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Processes")){
       res=false;
     }
     link=AdminNavigationMenu.getResourceCalendarControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Resource Calendar")){
       res=false;
     }
     link=AdminNavigationMenu.getResourcePlanningControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Resource Planning")){
       res=false;
     }
     link=AdminNavigationMenu.getRisksControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Risks")){
       res=false;
     }
     link=AdminNavigationMenu.getSearchSynonymsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Search Synonyms")){
       res=false;
     }
     link=AdminNavigationMenu.getStatusReportTemplatesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Status Report Templates")){
       res=false;
     }
     link=AdminNavigationMenu.getTagsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Tags")){
       res=false;
     }

     //Templates items
     link=AdminNavigationMenu.getWorkControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Work")){
       res=false;
     }
     link=AdminNavigationMenu.getMetricsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Metrics")){
       res=false;
     }
     link=AdminNavigationMenu.getPowerPointControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Power Point")){
       res=false;
     }
     link=AdminNavigationMenu.getTimesheetLoaderControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Timesheet Loader")){
       res=false;
     }
     link=AdminNavigationMenu.getCostLoaderControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Cost Loader")){
       res=false;
     }
   
     //Layouts items
     link=AdminNavigationMenu.getDashboardControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Dashboard")){
       res=false;
     }
     link=AdminNavigationMenu.getExecutiveReviewControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Executive Review")){
       res=false;
     }
     link=AdminNavigationMenu.getFinancialReviewControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Financial Review")){
       res=false;
     }
   
     //Permissions items
     link=AdminNavigationMenu.getDefaultsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Defaults")){
       res=false;
     }
     link=AdminNavigationMenu.getDefineCategoriesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Define Categories")){
       res=false;
     }
     link=AdminNavigationMenu.getAdminGroupControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Admin Group")){
       res=false;
     }
   
     //Localization items
     link=AdminNavigationMenu.getWorkStatusNamesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Work Status Names")){
       res=false;
     }
     link=AdminNavigationMenu.getReplaceableTermsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Admin", "Replaceable Terms")){
       res=false;
     }
   }else{
     res=false;
     Logger.logWarning("Admin Item is not displayed on Icon Menu.");
   }
   return res;
}
function reviewNavigationMenuTest(){
   var res= true;
   if(IconMenu.getReviewControl().Exists){
     var link=ReviewNavigationMenu.getDashboardControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Dashboard")){
       res=false;
     }
     link=ReviewNavigationMenu.getVisualPortalControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Visual Portal")){
       res=false;
     }
     link=ReviewNavigationMenu.getManageLayoutsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Manage Layouts")){
       res=false;
     }
     link=ReviewNavigationMenu.getAddVisualPortalControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Add Visual Portal")){
       res=false;
     }
     link=ReviewNavigationMenu.getResourceReviewControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Resource Review")){
       res=false;
     }
     link=ReviewNavigationMenu.getExecutiveReviewControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Executive Review")){
       res=false;
     }
     link=ReviewNavigationMenu.getFinancialReviewControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Financial Review")){
       res=false;
     }
     link=ReviewNavigationMenu.getPortfoliosControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Portfolios")){
       res=false;
     }
     link=ReviewNavigationMenu.getMeasuresLibraryControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Measures Library")){
       res=false;
     }
     link=ReviewNavigationMenu.getMyReportsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "My Reports")){
       res=false;
     }
     link=ReviewNavigationMenu.getPublicReportsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Public Reports")){
       res=false;
     }
     link=ReviewNavigationMenu.getManageReportsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Manage Reports")){
       res=false;
     }
     link=ReviewNavigationMenu.getAdvancedReportsControl();  //not in local server
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Advanced Reports")){
       res=false;
     }
     link=ReviewNavigationMenu.getFindAPersonControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Find A Person")){
       res=false;
     }
     link=ReviewNavigationMenu.getGroupsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Groups")){
       res=false;
     }
     link=ReviewNavigationMenu.getResourcePoolControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Resource Pool")){
       res=false;
     }
     link=ReviewNavigationMenu.getCertificationControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Certification")){
       res=false;
     }
     link=ReviewNavigationMenu.getManageTimeControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Manage Time")){
       res=false;
     }
     link=ReviewNavigationMenu.getResourceRatesControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Resource Rates")){
       res=false;
     }
     link=ReviewNavigationMenu.getMetricsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Metrics")){
       res=false;
     }
     link=ReviewNavigationMenu.getUsersControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Users")){
       res=false;
     }
     link=ReviewNavigationMenu.getTagsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Tags")){
       res=false;
     }
     link=ReviewNavigationMenu.getTimesheetsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Timesheets")){
       res=false;
     }
     link=ReviewNavigationMenu.getCostsControl();//not in local server
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Costs")){
       res=false;
     }  
     link=ReviewNavigationMenu.getMetricBulkActionsControl();
     if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Review", "Metric Bulk Actions")){
       res=false;
     }
   }else{
     res=false;
     Logger.logWarning("Review Item is not displayed on Icon Menu.");
   }
   return res;
}

function addNavigationMenuTest(){
   var res= true;
   var link=AddNavigationMenu.getProjectControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Project")){
     res=false;
   } 
   
   link=AddNavigationMenu.getIdeaControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Idea")){
     res=false;
   } 
   
   link=AddNavigationMenu.getUserControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "User")){
     res=false;
   } 
   
   link=AddNavigationMenu.getOrganizationControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Organization")){
     res=false;
   } 
   
   link=AddNavigationMenu.getOtherWorkControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Other Work")){
     res=false;
   } 
   
   link=AddNavigationMenu.getReportWizardControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Report Wizard")){
     res=false;
   } 
   
   link=AddNavigationMenu.getCreateReportsControl();
   if(!logWarningIfNavigationMenuLinkDoesNotExist(link, "Add", "Create Reports")){
     res=false;
   } 
   return res;
}
function homePageTest(){
  return (Home.isPageTitleCorrect());
}
function headerBarTest(userName){
   var res= true;
   if(!HeaderPanel.getLogo().Exists || !HeaderPanel.getLogo().VisibleOnScreen){
     res=false;
     Log.Warning("Logo is not displayed on Header Bar.");
   } 
   if(!HeaderPanel.getFavoritesControl().Exists){
     res=false;
     Log.Warning("Favorites Icon is not displayed on Header Bar.");
   } 
   if(!HeaderPanel.getHelpControl().Exists){
     res=false;
     Log.Warning("Help Icon is not displayed on Header Bar.");
   } 
   if(!HeaderPanel.getSearchControl().Exists){
     res=false;
     Log.Warning("Help Icon is not displayed on Header Bar.");
   } 
   if(!HeaderPanel.getLogOutControl().Exists){
     res=false;
     Log.Warning("LogOut link is not displayed on Header Bar.");
   } 
   if(!HeaderPanel.getUserName().Exists){
     res=false;
     Log.Warning("UserName is not displayed on Header Bar.");
   }
   return res;
}
function iconMenuTest(){
   var res= true;
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getHomeControl(),"Home")){
     res=false;
   } 
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getAddControl(),"Add")){
     res=false;
   }
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getAdminControl(),"Admin")){
     res=false;
   }
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getReviewControl(),"Review")){
     res=false;
   }
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getProjectControl(),"Project")){
     res=false;
   } 
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getInboxControl(),"Inbox")){
     res=false;
   } 
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getFavoritesControl(),"Favorites")){
     res=false;
   }
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getImportantLinksControl(),"Important Links")){
     res=false;
   }
   if(!logWarningIfIconMenuLinkDoesNotExist(IconMenu.getHistoryControl(),"History")){
     res=false;
   }
   return res;
}
function logWarningIfNavigationMenuLinkDoesNotExist(link, menuName, itemName){
   if(null != link){
     if(!link.Exists || !link.VisibleOnScreen){
       Log.Warning(itemName+" link is not visible on "+menuName+" Navigation Menu.");
       return false;
     }else{
       return true;
     } 
   }else{
       Log.Warning(itemName+" link is not displayed on "+menuName+" Navigation Menu.");
       return false;
   }
}
function logWarningIfIconMenuLinkDoesNotExist(iconLink, iconName){
   if(null != iconLink){
     if(!iconLink.Exists || !iconLink.VisibleOnScreen){
       Log.Warning(iconName+" icon is not visible on Icon Menu.");
       return false;
     }else{
       return true;
     } 
   }else{
       Log.Warning(iconName+" icon is not displayed on Icon Navigation Menu.");
       return false;
   }
}