//USEUNIT Logging
//USEUNIT Assert
//USEUNIT HomePage
//USEUNIT Browser
//USEUNIT Header
//USEUNIT Panel
//USEUNIT FavoritesPopIn
//USEUNIT ManageFavoritesPage
//USEUNIT PersonSettingsProfilePage
//USEUNIT NavigationMenu
//variables could be taken from a XML
var ADMIN_USER_LOGIN="User1";
var ADMIN_USER_PASS="12345a";
var BROWSER="iexplore";  //"iexplore" or "firefox"
var BASE_URL="http://localhost:8080/QARepTest";
function init(){
  Browser.closeAllOpenBrowsers();//closes all open browsers before start testing.
  Browser.setBrowser(BROWSER);
  HomePage.setHomeUrl(BASE_URL+"/Home.page");
  Browser.navigateToPageInNewBrowser(HomePage.getHomeUrl());
  Logging.doLogin(ADMIN_USER_LOGIN, ADMIN_USER_PASS); //admin login
  Header.getFavoritesControl().Click();
  FavoritesPopIn.setFavoritesName("Home page");
  FavoritesPopIn.setFavoritesDescription("Home page description");
  //FavoritesPopIn.setPinCheckbox(false);
  FavoritesPopIn.setGoToFavoritesManagerCheckbox(true);
  FavoritesPopIn.clickAddButton(); 
  Assert.assertValue(true,NavigationMenu.existsNavigationMenuItemWithNameX("Home page"),"Home page favorite presence on navigation menu. Should exist.");
  Assert.assertValue(false,NavigationMenu.existsPinedNavigationMenuItemWithNameX("Home page"),"Home page favorite presence on pined favorite section. Should not exist.");
  Assert.assertValue(true,ManageFavoritesPage.existsHomePageFavoriteFavoriteOnExportTable(),"Home page favorite presence on Export table. Should exist.");
  Assert.assertValue(false,ManageFavoritesPage.isFistPinCheckBoxChecked(),"Home page favorite's checkbox checked on Export table. Should not be checked.");
  
  ManageFavoritesPage.openProfilePage();
  Header.getFavoritesControl().Click();
  FavoritesPopIn.setFavoritesName("My Profile");
  FavoritesPopIn.setFavoritesDescription("Profile page description");
  FavoritesPopIn.setPinCheckbox(true);
  FavoritesPopIn.setGoToFavoritesManagerCheckbox(true);
  FavoritesPopIn.clickAddButton();
  Assert.assertValue(false,NavigationMenu.existsNavigationMenuItemWithNameX("My Profile"),"Profile page favorite presence on navigation menu. Should not exist.");
  Assert.assertValue(true,NavigationMenu.existsPinedNavigationMenuItemWithNameX("My Profile"),"Profile page favorite presence on pined favorite section. Should exist.");
  Assert.assertValue(true,ManageFavoritesPage.existsProfilePageFavoriteFavoriteOnExportTable(),"Profile page favorite presence on Export table. Should exist.");
  Assert.assertValue(true,ManageFavoritesPage.isSecondPinCheckBoxChecked(),"Profile page favorite's checkbox checked on Export table. Should be checked.");
  Logging.doLogOut();
}