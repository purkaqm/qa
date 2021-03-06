//USEUNIT Page
//USEUNIT Browser
//USEUNIT NavigationMenu
  var _url=null;
  var PAGE_IDENTIFIER="/Home";
    
  function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageHome);
  }
    
  function openPageFromUrl(reOpen){//navigates to home page.
    Page.openFromUrl(Aliases.browser.pageHome, _url, reOpen, HomePage.isPageOpen());
    return HomePage.isPageOpen();
  }
    
  function isPageOpen(){
    return Page.isPageOpen(PAGE_IDENTIFIER); //if the current url contains "/Home.page"
  }
  
  function isPageTitleCorrect(){
    if(HeaderPanel.getTitle().Exists){
      if(HeaderPanel.compareTitleContentText("Home")){
        if(HeaderPanel.getTitle().VisibleOnScreen){
          Log.Message("Home page title is correct.");
          return true;
        }else{
          Log.Message("Home page title is not visible on screen.");
          return false;
        }
      }else{
        Log.Message("Home page title is incorrect.");
        return false;
      }
    }else{
      Log.Message("Home page title does not exist.");
      return false;
    }
  }  
  
  function setHomeUrl(url){//sets the _url variable
    _url=url;
  }
  function getHomeUrl(){
    return _url;
  }