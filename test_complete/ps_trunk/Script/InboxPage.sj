//USEUNIT Page
//USEUNIT IconMenu
function wait(){//Waits until the browser loads the page.
  Page.wait(Aliases.browser.pageInbox);
}
function getAlertsContentPanel(){
  return Aliases.browser.pageInbox.panelBodyContainer.panelPagecontent.panelContent.panelContainer.alertsContentPanel;
}
