
function openFile(path){
  var str=aqString.Replace(path, "/", "\\");
  Aliases.browser.windowFileUpload.OpenFile(str);
}