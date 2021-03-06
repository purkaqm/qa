//USEUNIT InviteNewUserPage
//USEUNIT Logger
//USEUNIT AddNavigationMenu
function inviteNewUser(name, lastName, email){
  Log.Message("Inviting new User name = '"+name+"' last_name = '"+lastName+"' email = '"+email+"'.");
  var item=AddNavigationMenu.getUserControl();
  if(item.Exists){
    item.Click();
    InviteNewUserPage.wait();
    InviteNewUserPage.setFirstName(name);
    InviteNewUserPage.setLastName(lastName);
    InviteNewUserPage.setEmail(email);
    if(!InviteNewUserPage.clickInviteControl()){
      Logger.logWarning("Confirmation message after inviting a new User is not displayed.");
      return false;
    }else{
      InviteNewUserPage.getInvitedUserNameLink().Click();
      if(getMoreLinkFromInvitedUserNamePopUp().Exists){
        clickMoreLinkFromInvitedUserNamePopUp();
        return true
      }else{
        Logger.logWarning("Confirmation message after inviting a new User is not displayed.");
        return false;
      }
    }
  }else{
    Logger.logWarning("Invite New User page item is not displayed on Navigation Menu.");
    return false;
  }
}