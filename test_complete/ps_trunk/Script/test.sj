//USEUNIT Browser
//USEUNIT Header
//USEUNIT HeaderPanel
//USEUNIT QuickSearch
//USEUNIT WorkSummaryPage
//USEUNIT WorkDeleteParentWorkPage
//USEUNIT AddNavigationMenu
//USEUNIT AdminNavigationMenu
//USEUNIT WorkPermissionsPage
//USEUNIT ProjectNavigationMenu
//USEUNIT WorkDocumentEditDetailsPage
//USEUNIT AcceptanceNavigationMenuAndUITest
//USEUNIT AddNavigationMenu
//USEUNIT IconMenu
//variables could be taken from a XML
var ADMIN_USER_LOGIN="User2";
var ADMIN_USER_PASS="12345a";
var BROWSER="iexplore";  //"iexplore" or "firefox"
var BASE_URL="http://localhost:8080/QARepTest";
function init(){
  //IconMenu.getHomeControl().Click();
  /*for (var i=0;i<10;i++){
    NavigationMenu.getNavigationMenuPanel().WaitSlApplication(1000);
    Log.Message(NavigationMenu.isMenuPinedByClassName());
  }*/
  Log.Message(Header.isProxyUser());
  
}
