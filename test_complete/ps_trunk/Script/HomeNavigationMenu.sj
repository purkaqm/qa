//USEUNIT IconMenu
//USEUNIT NavigationMenu

function getConfigureHomePageControl(){
  return NavigationMenu.getNavigationMenuSubItem(IconMenu.getHomeControl(), null, "Configure Home Page");
}
