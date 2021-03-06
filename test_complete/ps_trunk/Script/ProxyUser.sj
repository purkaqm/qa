//USEUNIT HeaderPanel
//USEUNIT ProfileMenu
//USEUNIT Logger
function isProxyUser(){
    var icon=HeaderPanel.getProxyUserImagePanel();
    if(icon.Exists){
      //it searches the stored region object named panelProxyUser(small 14x14 image) into the image displayed on screen (in FF this image could be bigger)
      if(Regions.Compare("panelProxyUser",icon.Picture(),true,false,true,0)){//0 is the pixel tolerance
        if(HeaderPanel.getProxyUserReturnControl().Exists){
          return true
        }else{
          Log.Message("Return link is not displayed on Header panel. So, User is not a Proxy-User.");
          return false;
        }
        return true;
      }else{
        Logger.logWarning("Proxy icon does not match with stored icon.");
        return false;
      }
    }else{
      Log.Message("Proxy Icon is not displayed.");
      return false;
    }
}
function switchUserToX(name, lastName){
  var us=name+" "+lastName;
  var item=ProfileMenu.getMenuItemByName("Switch to User");
  var res=false;
  if(null!=item){
    if(item.Exists){
      item.HoverMouse();
      var user= ProxySwitchToUserPopUp.getSwitchToUserPopUpPanel().FindChild(["ObjectType","contentText"], ["Link",us], 100, true);//if there is only 1 result
      if(user.Exists){
        user.Click();
        if(ProxyUser.isProxyUser()){
          res=true;
        }else{
          Logger.logWarning("User UI does not change to Proxy-User UI.");
        }
      }else{
        Logger.logWarning("Proxy user '"+name+" "+lastName+"' is not displayed on ProxySwitchToUserPopUp.");
      }
    }else{
      Logger.logWarning("'Switch to User' link is not displayed on Profile menu.");
    }
  }else{
    Logger.logWarning("'Switch to User' link is null on Profile menu.");
  }
  var returnLink=HeaderPanel.getProxyUserReturnControl();
  if(returnLink.Exists){
    returnLink.Click();
  }
  return res;
}