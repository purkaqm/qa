function GeneralEvents_OnLogWarning(Sender, LogParams){//called by GeneralEvents
  Log.Picture(Sys.Desktop,"Warning ScreenShot");//logs a screenshot when a warning message is logged.
}

function logCheckPoint(message){
  Log.Checkpoint(message, clOlive)
}

function logWarning(message){
  Log.Warning(message, clRed)
}