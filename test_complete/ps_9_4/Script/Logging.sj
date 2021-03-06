//USEUNIT HomePage 
//USEUNIT Browser 
//sets the user name field
function setUserNameTextBox(userName){
  Aliases.browser.panelLogin.userNameTextBox.SetText(userName);
}
//sets the password field
function setUserPassTextBox(passWord){
  Aliases.browser.panelLogin.userPassTextBox.SetText(passWord);
}
//checks or unchecks the remember login cookie checkbox
function setRememberLoginCheckbox(value){
  Aliases.browser.panelLogin.rememberLoginCheckbox.ClickChecked(value);
}
//click login control
function submitLogIn(){
  Aliases.browser.panelLogin.loginControl.Click();  //clicl login control
  HomePage.wait();
}
//click logout control
function submitLogOut(){
  Aliases.browser.panelHeader.logoutControl.Click(); //click logout link
  Aliases.browser.pageLogout.Wait();  //Waits until the browser loads the page and is ready to accept user input.
}
//click here link in logout page
function clickHereLink(){
  Aliases.browser.pageLogout.hereControl.Click(); //Clicks the 'linkHere' control.
  HomePage.wait();
}

//login function
function doLogin(userName, passWord){
  Log.Message("User LogIn: "+userName);
  HomePage.openPageFromUrl(false);//because login is mapped under Aliases.browser.pageHome...
  Logging.setUserNameTextBox(userName);
  Logging.setUserPassTextBox(passWord);
  Logging.setRememberLoginCheckbox(false);
  Logging.submitLogIn();
}

//logout function
function doLogOut(){
  Log.Message("User LogOut");
  HomePage.openPageFromUrl(false); //because this link is mapped under Aliases.browser.pageHome...
  Logging.submitLogOut();
  Logging.clickHereLink();
}