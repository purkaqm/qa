//USEUNIT Browser
//USEUNIT HeaderPanel
//the general Page script. It contains all the common pages methods
var OBJECT_TYPE_LINK="Link";
var OBJECT_TYPE_TABLE="Table";
var OBJECT_TYPE_TEXTNODE="TextNode";
var OBJECT_TYPE_PANEL="Panel";

//navigates to home page
//page=Aliases.X page reference. url=current page url
//boolean reOpen; true=if page is open, it navigates to url, false, when page open it does not navigates to url.
//In some pages like summarypage, when user has no permission to view page, it opens summary page but with a "no permission massage" 
//therefor it is not possible to ask if page is open from page(page.Exists)
//if url has null value it returns undefined.
function openFromUrl(page, url, reOpen, isOpen){
  if(!(reOpen==true || reOpen==false) || !(isOpen==true || isOpen==false)){
    Log.Error("reOpen and isOpen parameter should have a valid boolean value; reOpen ='"+reOpen+"' and isOpen ='"+isOpen+"'.");
    return false;
  }
  if(null != url){
    if(!(false == reOpen && isOpen) || !isOpen){//if page is open and reOpen=false or then not navigate to url
      Browser.navigateToPageInCurrentBrowser(url);
      Page.wait(page);
    }
    return true;
  }else{
    Log.Warning("Page URL has NULL value. This may cause URL access problems.");
    return undefined;
  }
  
}
function wait(page){//Waits until the browser loads the page and is ready to accept user input.
  page.Wait();
}
function getUrl(isOpen){
  if(!(isOpen==true || isOpen==false)){
    Log.Error("isOpen parameter should have a valid boolean value; isOpen ='"+isOpen+"'.");
  }
  if(isOpen){
    var url=Browser.getCurrentPageUrl();
    return url;
  }else{
    Log.Error("Error getting page URL.");
    return null;
  }
}

function isPageOpen(pageIdentifier){
  var currUrl=Browser.getCurrentPageUrl();
  //Log.Message("Current Url="+currUrl);
  return (aqString.Find(currUrl,pageIdentifier,0)>=0?true:false) //if the current url contains the pageIdentifier
}
function isPageOpenAndHasPermission(pageIdentifier){ //returns wheter the page is open and has permission
  if(Page.isPageOpen(pageIdentifier)){ //if the current url contains the page identifier
    return HeaderPanel.getTitle().Exists;//if user has not permission to view work page, toolbar is not displayed
  }else{
    return false;
  }
}
function getActualPage(){
  return Aliases.browser.FindChild(["ObjectType"], ["Page"], 10, true);
}
function getActualPageContent(){
  return Aliases.browser.FindChild(["idStr"], ["PageContent"], 10, true);
}