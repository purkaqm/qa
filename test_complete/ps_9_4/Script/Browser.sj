
var _browser=null;
//set the browser variable
function setBrowser(browser){
  _browser=browser;
}
//opens a new browser process
function openEmptyBrowser(){
  if(null!=_browser){
    Browsers.Item(_browser).Run();
  }else{
    Log.Error("Browser is NULL, please call setBrowser() method first.");
  }
}
//close current browser
function closeCurrentBrowser(){
  Aliases.browser.Close();
}
//open page url in current browser
function navigateToPageInCurrentBrowser(url){
  if(null == Browsers.CurrentBrowser){
    Log.Error("Current browser object has NULL value");
  }
  Browsers.CurrentBrowser.Navigate(url);
}
//open page url in new browser. Pay atention at login session when browser change
function navigateToPageInNewBrowser(url){
  Browser.openEmptyBrowser();
  Browser.navigateToPageInCurrentBrowser(url);
}
//closes all open browsers
function closeAllOpenBrowsers(){
  while (Sys.WaitBrowser().Exists){// if (Sys.WaitBrowser().Exists)
    Sys.WaitBrowser().Close();
  }
}
//returns the reference to the current browser
//Aliases.browser
function getBrowser(){
  return Aliases.browser;
}

function getCurrentPageUrl(){
    return Aliases.browser.Page("*").URL;
}

