//USEUNIT Page
//USEUNIT Browser

  var _url=null;
  var PAGE_IDENTIFIER="/Home";
    
  function wait(){//Waits until the browser loads the page.
    Page.wait(Aliases.browser.pageHome);
  }
    
  function openPageFromUrl(reOpen){//navigates to home page.
    Page.openFromUrl(Aliases.browser.pageHome, _url, reOpen, HomePage.isPageOpen());
    return HomePage.isPageOpen();
  }
    
  function isPageOpen(){ //returns wheter the "My projects" label is displayed
    return Page.isPageOpen(PAGE_IDENTIFIER); //if the current url contains "/Home.page"
  }
    
  function setHomeUrl(url){//sets the _url variable
    _url=url;
  }
  function getHomeUrl(){
    return _url;
  }