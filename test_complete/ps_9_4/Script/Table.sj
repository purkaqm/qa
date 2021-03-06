
//returns the table cell position  at row, column indexes
function getCellAt(table, rowIndex, colIndex){
  if(Table.getRowCount(table) > rowIndex && Table.getColumnCount(table) > colIndex && rowIndex >= 0 && colIndex >= 0){
    return table.Cell(rowIndex, colIndex);
  }else{
    Log.Error("One or both indexes are out of range: 0=< "+rowIndex+" < "+Table.getRowCount(table)+" and 0=< "+colIndex+" < "+Table.getColumnCount(table));
    return null;
  }
}
//returns the table row count
function getRowCount(table){
  return table.RowCount;
}
//returns the table column count
function getColumnCount(table){
  return table.ColumnCount;
}
//returns the row index of a given row name
function getRowIndex(table, rowName, colIndex, keySensitive){
  for(var j=0;j<Table.getRowCount(table);j++){
    if(!aqString.Compare(rowName,table.Cell(j, colIndex).contentText,keySensitive)){
//      Log.Message("Returning row with name = '"+rowName+"'. Index= "+j);
      return j;  //returns the row index
    }
  }
  Log.Message("Row with name = '"+rowName+"' NOT FOUND");
  return -1;
}
//returns the row index of a given row name(if the first part of the row contentText matches with the rowName parameter returns it)
function getRowIndexWithSubString(table, rowName, colIndex, keySensitive){
  var content;
  for(var j=0;j<Table.getRowCount(table);j++){
    content=table.Cell(j, colIndex).contentText;
    if(content.length >= rowName.length){
      if(!aqString.Compare(rowName,aqString.SubString(content, 0 , rowName.length),keySensitive)){
//        Log.Message("Returning row with at least the first part of the name = '"+rowName+"'. Index= "+j);
        return j;  //returns the row index
      }
    }
  }
  Log.Message("Row with at least the first part of the name = '"+rowName+"' NOT FOUND");
  return -1;
}

//returns the column index of a given column name
function getColumnIndex(table, colName, rowIndex, keySensitive){
  for(var i=0;i<Table.getColumnCount(table);i++){
    if(!aqString.Compare(colName,table.Cell(rowIndex, i).contentText,keySensitive)){
//      Log.Message("Returning column with name ='"+colName+"'. Index= "+i);
      return i;  //returns the index
    }
  }
  Log.Message("Column with name ='"+colName+"' NOT FOUND");
  return -1;
}

function selectRadioButtonAt(table, rowIndex, colIndex){
  if(rowIndex >= 0 && colIndex >= 0){
    var radio=Table.getCellAt(table,rowIndex,colIndex).Child(0);//obtains the radioButton
    radio.ClickButton();
    if(radio.checked){
//      Log.Message("Returning RadioButton with identifier = '"+radio.ObjectIdentifier+"' selected. Position("+rowIndex+","+colIndex+").");
      return true;
    }else{
      Log.Error("RadioButton with identifier = '"+radio.ObjectIdentifier+"' NOT selected. Position("+rowIndex+","+colIndex+").");
      return false;
    }
  }else{
    Log.Error("Radiobutton at Position("+rowIndex+","+colIndex+"). Out of range");
    return false;
  }
}
function selectCheckBoxAt(table, rowIndex, colIndex, value){
  if(rowIndex >= 0 && colIndex >= 0){
    var cell=Table.getCellAt(table, rowIndex, colIndex);
    cell.Child(0).focus();
    cell.Child(0).ClickChecked(value);
    if(cell.Child(0).checked == value){
//      Log.Message("CheckBox value verified = "+value+".");
      return true;
    }else{
      Log.Error("CheckBox value does NOT verifies; expected = "+value+" current = "+cell.Child(0).checked+". Position("+rowIndex+","+colIndex+").");
      return false;
    }
  }else{
    Log.Error("CheckBox at Position("+rowIndex+","+colIndex+"). Out of range");
    return false;
  }
}