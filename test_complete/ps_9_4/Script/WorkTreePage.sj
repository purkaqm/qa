//USEUNIT Browser 
//USEUNIT Page
//USEUNIT MainNavLinks
var _CHILD_NAME="Panel2";//the name of the panel that WorkTree parent should wail for when page is opened.
var _MAX_WAIT_TIME=180; //max wait time in seconds; 3 minutes
function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageWorkTree);
}

//wait until the work tree is loaded
function waitUntilWorkTreeIsLoaded(time){
  Aliases.browser.pageWorkTree.panelContent.panelContainer.panelBox.panelPsUiTree.panel.WaitAliasChild(_CHILD_NAME,time);
}
//Returns the search results table.
function getWorkTree(){
  return Aliases.browser.pageWorkTree.panelContent.panelContainer.panelBox.panelPsUiTree.panel.panel2.workTreePanel;
}
//click Browse->WorkTree link
function openPageFromLink(time){
  MainNavLinks.getBrowseSubItem(MainNavLinks.BROWSE_WORK_TREE).Click();
  WorkTreePage.wait();
  WorkTreePage.waitUntilWorkTreeIsLoaded(time);
}
//Returns true when table exists and false when not.
function existsWorkTree(){
  return WorkTreePage.getWorkTree().Exists;
}
function getMaxWaitTime(){
  return _MAX_WAIT_TIME;
}