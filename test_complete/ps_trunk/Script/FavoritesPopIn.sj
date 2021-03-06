function getFavoritesNameTextBox(){
  return Aliases.browser.panelAddToFavorites.panelContent.panel.panel2.table.cell01.favoritesNameTextBox;
}
function getFavoritesDescriptionTextArea(){
  return Aliases.browser.panelAddToFavorites.panelContent.panel.panel2.table.cell11.favoritesDescriptionTextArea;
}
function getPinCheckBox(){
  return Aliases.browser.panelAddToFavorites.panelContent.panel.panel2.table.cell21.pinCheckBox;
}
function getGoToFavoritesManagerCheckBox(){
  return Aliases.browser.panelAddToFavorites.panelContent.panel.panel2.table.cell31.goToFavoritesManagerCheckBox;
}
function getAddButton(){
  return Aliases.browser.panelAddToFavorites.panelContent.panel.panelButtons.addControl;
}

function setPinCheckbox(value){
  FavoritesPopIn.getPinCheckBox().ClickChecked(value);
}
function setGoToFavoritesManagerCheckbox(value){
  FavoritesPopIn.getGoToFavoritesManagerCheckBox().ClickChecked(value);
}
function clickAddButton(){
  FavoritesPopIn.getAddButton().Click();
}
function setFavoritesName(name){
  FavoritesPopIn.getFavoritesNameTextBox().SetText(name);
}
function setFavoritesDescription(description){
  FavoritesPopIn.getFavoritesDescriptionTextArea().zselect();
  FavoritesPopIn.getFavoritesDescriptionTextArea().Keys(description);
}




