//USEUNIT BrowseFileWindow
//It clicks the browse component and selects a file by name and path from browse file window.
function browseFile(browseComponent, filePath, fileName){
  browseComponent.Click(160,10);
  BrowseFileWindow.setFilePath(filePath);
  BrowseFileWindow.setFileName(fileName);
  BrowseFileWindow.clickOpenControl();
}