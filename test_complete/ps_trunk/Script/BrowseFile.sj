//USEUNIT BrowseFileWindow
//It clicks the browse component and selects a file by name and path from browse file window.
function browseFile(browseComponent, filePath, fileName){
  browseComponent.Click((browseComponent.width-40),10);
  BrowseFileWindow.openFile(filePath+fileName);
}