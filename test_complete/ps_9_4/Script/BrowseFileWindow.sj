
function setFilePath(path){
  var progress32=Aliases.browser.windowFileUpload.navBar.ReBarWindow32.AddressBandRoot.msctls_progress32;
  progress32.BreadcrumbParent.ToolbarWindow32.Click(234, 11);
  progress32.ComboBoxEx32.SetText(path);
  progress32.ComboBoxEx32.ComboBox.Edit.Keys("[Enter]");
}
function setFileName(name){
  Aliases.browser.windowFileUpload.fileNameComboBox.ComboBox.fileName.SetText(name);
}
function clickOpenControl(){
  Aliases.browser.windowFileUpload.openControl.ClickButton();
}