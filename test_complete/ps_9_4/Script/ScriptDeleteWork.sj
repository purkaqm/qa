//USEUNIT QuickSearch
//USEUNIT Table
//USEUNIT Browser
//USEUNIT HomePage
//USEUNIT Logging
//USEUNIT WorkSummaryPage
//USEUNIT WorkDelete
//USEUNIT QuickSearchPopup
function searchAndDeleteProjectsByName(iName,browser,baseUrl,adminUser,adminPass){
  Browser.setBrowser(browser);
  Browser.openEmptyBrowser();
  HomePage.setHomeUrl(baseUrl+"/Home.page");
  Logging.doLogin(adminUser, adminPass); //admin login
  var item = null;
  do{
    item=QuickSearch.search(iName, QuickSearchPopup.PROJECT_SEARCH_TYPE, false);
    if(null != item){
      WorkSummaryPage.openPageFromLink(item);
      if(!WorkDelete.deleteWork(true)){
        Log.Error("Project '"+item.contentText+"' NOT deleted");
      }else{
        Log.Message("Deleted = "+item.contentText);
      }
    }
  }while(null != item)
}