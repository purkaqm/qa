//USEUNIT IconMenu
//USEUNIT Logger
//USEUNIT Page
function getNavigationMenuPanel(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar;
}
function getNavigationMenuTitle(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar.NavigationMenuTitle;
}
//returns the menu item which contentText is equals to aux Pronject variable
function getMenuLink(){//to find this item should be specified the Project.Variables.aux variable
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar.panelMenuContent.menuContentPanel.panel.menuLink;
}
function getNavigationMenuContentPanel(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar.panelMenuContent.menuContentPanel/*.panel*/;
}
function getPinedNavigationMenuContentPanel(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar.menuPinContentPanel.panelPinFavs.panelPinContent.panel;
}
function getPinIcon(){
  return Aliases.browser.pageHome.panelBodyContainer.panelLeftMenuPane.panelMenuBar.Pin;
}
function isNavigationMenuDisplayed(){
  var nav=NavigationMenu.getNavigationMenuTitle();
  if(nav.Exists && nav.VisibleOnScreen){
    return true;
  }else{
    return false;
  }
}
//returns true if menu is pined, false if it is unpined and null if the menu is not displayed or if the pin icon is not correctly displayed
//it is susceptible to failures because it compares two images and images are displayed different in each browser.
function isMenuPined(){
  var title=NavigationMenu.getNavigationMenuTitle();
  if(title.Exists){
    title.HoverMouse();
    var pin=NavigationMenu.getPinIcon();
    //Log.Picture(Regions.panelMenuPined,"Pin Icon");
    if(pin.Exists){
      //it searches the stored region object named panelMenuPined(small 15x15 image) into the image displayed on screen (in FF this image is 21x21)
      if(Regions.FindRegion("panelMenuPined",pin,0,0,true,false,45)){//45 is the pixel tolerance /*Regions.Compare("panelMenuPined", pin, true, false, true, 200, 0)*/
        Log.Message("Pined");
        return true;
      }else{
        //it searches the stored region object named panelMenuUnpined(small 15x15 image) into the image displayed on screen (in FF this image is 21x21)
        if(Regions.FindRegion("panelMenuUnpined",pin,0,0,true,false,60)){//60 is the pixel tolerance/*Regions.Compare("panelMenuUnpined", pin, true, false, true, 200, 0)*/
          Log.Message("Unpined");
          return false;
        }else{
          //Log.Picture(pin.Picture(),"Pin Icon");
          Logger.logWarning("Pin icon does not match with stored pined and unpined icons.");
          return null;
        }
      }
    }else{
      Logger.logWarning("Pin Icon is not displayed.");
      return null;
    }
  }else{
    Logger.logWarning("Navigation Menu is not displayed.");
    return null;
  }
}
function isMenuPinedByClassName(){
  var bar=NavigationMenu.getNavigationMenuPanel();
  if(bar.Exists){
    if(!aqString.Compare(bar.ClassName,"active",true)){//className=active when Navigation Menu is pined
      return true;
    }else{
      if(!aqString.Compare(bar.ClassName,"",true)){//className="" when it is unpined
        return false;
      }else{
        Logger.logWarning("Navigation Menu ClassName = '"+bar.ClassName+"'");
        return null;
      }
    }
  }else{
    Logger.logWarning("Navigation Menu is not displayed.");
    return null;
  }
}
function mouseOverNavMenuTitleAndGetPinIcon(){
  NavigationMenu.getNavigationMenuTitle().HoverMouse();
  return NavigationMenu.getPinIcon();
}
//returns menu the first item if its name is equals to iName; if not returns null
function getItemFromNavigationMenuByName(iName){
  var propNames = Array("contentText", "ObjectType"); 
  var propValues = Array(iName, "Link"); 
  var res=NavigationMenu.getNavigationMenuContentPanel().FindChild(propNames, propValues, 100, true);
  if(res.Exists && res.VisibleOnScreen){
    Log.Message("Returning Navigation link item name ="+res.contentText);
    return res;
  }else{
    propNames = Array("contentText", "ObjectType", "className"); 
    propValues = Array(iName, "Panel", "level"); 
    res=NavigationMenu.getNavigationMenuContentPanel().FindChild(propNames, propValues, 100, true);//searches for two level items
    if(res.Exists && res.VisibleOnScreen){
      Log.Message("Returning Navigation two level item name ="+res.contentText);
      return res;
    }else{
        propNames = Array("contentText", "ObjectType", "ObjectIdentifier"); 
        propValues = Array(iName, "Panel", "ps_*"); 
        res=NavigationMenu.getNavigationMenuContentPanel().FindChild(propNames, propValues, 100, true);//searches for other panels
        if(res.Exists && res.VisibleOnScreen){
          Log.Message("Returning Other ps_ panel; item name ="+res.contentText);
          return res;
        }else{
          Log.Message("Menu Item name = '"+res.contentText+"' NOT found.");
          return null;
        }
    }
  }  
}  
//returns if exists menu item with name equals to iNmae
function existsNavigationMenuItemWithNameX(iName){
  if(NavigationMenu.getItemFromNavigationMenuByName(iName) == null){
    return false;
  }else{
    return true;
  }
}

//returns pined menu the first item if its name is equals to iName; if not returns null
function getItemFromPinedNavigationMenuByName(iName){
  for(var i=0;i<NavigationMenu.getPinedNavigationMenuContentPanel().ChildCount;i++){
    var item=NavigationMenu.getPinedNavigationMenuContentPanel().Child(i);
    if(!aqString.Compare(item.contentText,iName,true)){//true if is case-sensitive
      return item;  //returns the first item from pined navigation menu name equals to iName.
    }
  }
  return null;
}  
//returns if exists pined menu item with name equals to iNmae
function existsPinedNavigationMenuItemWithNameX(iName){
  if(NavigationMenu.getItemFromPinedNavigationMenuByName(iName) == null){
    return false;
  }else{
    return true;
  }
}

//returns if exists menu title with name equals to iNmae
function existsNavigationMenuTitleWithNameX(iName){
  if(!aqString.Compare(getNavigationMenuTitle().contentText,iName,false)){
    return true;
  }else{
    return false;
  }
}
//returns navigation menu visible items count
function getVisibleMenuItemsCount(){
  var nav=NavigationMenu.getNavigationMenuContentPanel();
  var count=0;
  for(var i=0;i<nav.ChildCount;i++){
    var ch=nav.Child(i);
    if(nav.Child(i).VisibleOnScreen){
      count++;
    }
  }
  return count;
}
function expandTwoLevelMenuItemIfNecesary(item){
  var before=NavigationMenu.getVisibleMenuItemsCount();
  item.Click();
  var after=NavigationMenu.getVisibleMenuItemsCount();
  if(after<before){
    item.Click();//click again to expand
  }
}

function isProjectNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("PROJECT");
}
function isHomeNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("HOME");
}
function isAdminNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("ADMIN");
}
function isInBoxNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("INBOX");
}
function isAddNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("ADD");
}
function isReviewNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("REVIEW");
}
function isFavoritesNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("FAVORITES");
}
function isHistoryNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("HISTORY");
}
function isImportantLinksNavigationMenuDisplayed(){
  return existsNavigationMenuTitleWithNameX("IMPORTANT LINKS");
}
//iconMenu is the IconMenu object (ADD, REVIEW, ADMIN...)
//twoLevelItemName is the name of the two level item from nav menu (item with the + icon)
function getNavigationMenuSubItem(menuIcon, twoLevelItemName, itemName){
  if(menuIcon.Exists){
    if(!NavigationMenu.existsNavigationMenuTitleWithNameX(menuIcon.title)){
      menuIcon.Click();
      menuIcon.WaitSlApplication(500);
    }
    var item=NavigationMenu.getItemFromNavigationMenuByName(itemName);
    if(null != item){
      return item;
    }else{
      if(null != twoLevelItemName){//is null when it is not a two level item
        var twoLevelItem=NavigationMenu.getItemFromNavigationMenuByName(twoLevelItemName)
        if(null != twoLevelItem){
          twoLevelItem.Click();
          return NavigationMenu.getItemFromNavigationMenuByName(itemName);
        }else{
          return null;
        }
      }else{
        return null;
      }
    }
  }else{
    return null;
  }
}

function isBodyContainerProperlyAligned(){
  IconMenu.getIconBar().WaitSlApplication(3000);
  var iconMenuWidth= IconMenu.getIconBar().width;
  var navMenuWidth=0;
  var bar=NavigationMenu.getNavigationMenuContentPanel();
  if(bar.Exists){
    if(aqString.Compare(bar.ClassName,"hidden",true)){//if it is not hidden
      navMenuWidth=NavigationMenu.getNavigationMenuContentPanel().width;
    }
  }
  var bodyContainerWidth=Page.getActualPageContent().width;
  var pageWidth=Page.getActualPage().width;
  var sum=iconMenuWidth+navMenuWidth+bodyContainerWidth;
  Log.Message("iconMenuWidth="+iconMenuWidth+" navMenuWidth="+navMenuWidth+" bodyContainerWidth="+bodyContainerWidth+" pageWidth="+pageWidth+" sum="+sum);
  if(sum<=pageWidth){
    return true;
  }else{
    return false;
  }
}