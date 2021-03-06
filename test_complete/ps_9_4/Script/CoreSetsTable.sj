//USEUNIT WorkPermissionsPage
//USEUNIT Table

//sets the project core set to de desired category
//coreSet=core set name. level=CS level. catName=category name
function setProjectCoreSetValue(coreSet, level, catName){
  Log.Message("Core Set - Setting core set '"+coreSet+"', level '"+level+"' and category '"+catName+"' permissions");
  var rows=Table.getRowCount(WorkPermissionsPage.getCoreSetNamesTable());
  var cell, cell2, c;//c is the core set array elements counter
  var csIndex=-1;
  for(c=0;c<rows;c++){//generates an array with core set name and level. When name is a big row, it generates two rows for that name
    cell=Table.getCellAt(WorkPermissionsPage.getCoreSetNamesTable(),c,0);
    if(!aqString.Compare(coreSet,cell.contentText,true)){
      cell2=Table.getCellAt(WorkPermissionsPage.getCoreSetNamesTable(),c,1);
      if(!aqString.Compare(level,cell2.contentText,true)){
        csIndex=c;
      }else{
        if(cell.rowSpan == 2){//if its a big row
          cell2=Table.getCellAt(WorkPermissionsPage.getCoreSetNamesTable(),(c+1),0);
          if(!aqString.Compare(level,cell2.contentText,true)){
            csIndex=c+1;
          }else{
            Log.Error("Core Set '"+coreSet+"' Level '"+level+"' NOT foud #1.");
          }
        }else{
          Log.Error("Core Set '"+coreSet+"' Level '"+level+"' NOT foud #2.");
        }
      }   
      break;
    }
  }
  var catIndex=Table.getColumnIndex(WorkPermissionsPage.getCoreSetRadioButtonsTable(), catName, 0, true); //0=row index
  var found=false;
  if(catIndex >= 0 && csIndex >= 0){
    if(Table.selectRadioButtonAt(WorkPermissionsPage.getCoreSetRadioButtonsTable(),csIndex,catIndex)){
      Log.Message("Core Set '"+coreSet+"' checked correctly.");
      return true;
    }else{
      Log.Error("Core Set '"+coreSet+"' NOT checked correctly.");
      return false;
    }
  }else{
    Log.Error("Category '"+catName+"' or Core Set '"+coreSet+" -> "+level+"' NOT found.");
    return false;
  }
}

