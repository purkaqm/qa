//USEUNIT HeaderPanel
//USEUNIT Page
var PAGE_IDENTIFIER="/person/settings/Preferences.epage";

function wait(){//Waits until the browser loads the Person page.
  Page.wait(Aliases.browser.pagePersonSettingsPreferences);
}
function isPageOpen(){
  return (Page.isPageOpen(PAGE_IDENTIFIER) && PersonSettingsPreferencesPage.pageTitleContainsPreferencesText()); //if the page url contains "/person/settings/"
}
function pageTitleContainsPreferencesText(){
  return HeaderPanel.pageTitleContainsText(": Settings : Preferences");
}
