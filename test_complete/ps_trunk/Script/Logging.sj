//USEUNIT HomePage 
//USEUNIT Browser 
//USEUNIT HeaderPanel
//sets the user name field
function setUserNameTextBox(userName){
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.userNameTextBoxPanel.focus();
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.userNameTextBoxPanel.Click();
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.userNameTextBoxPanel.userNameTextBox.SetText(userName);
}
//sets the password field
function setUserPassTextBox(passWord){
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.userPassTextBoxPanel.Click();
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.userPassTextBoxPanel.userPassTextBox.SetText(passWord);
}
//checks or unchecks the remember login cookie checkbox
function setRememberLoginCheckbox(value){
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.panelSignIn.panelRemember.rememberLoginCheckBox.ClickChecked(value);
}
//click login control
function submitLogIn(){
  Aliases.browser.panelLoginMain.panelLeft.panelLoginform.form.panelSignIn.panel.loginControl.Click();  //clicl login control
  HomePage.wait();
}
//click logout control
function submitLogOut(){
  HeaderPanel.getLogOutControl().Click(); //click logout link
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
  //HomePage.openPageFromUrl(false); //because this link is mapped under Aliases.browser.pageHome...
  Logging.submitLogOut();
  Logging.clickHereLink();
}