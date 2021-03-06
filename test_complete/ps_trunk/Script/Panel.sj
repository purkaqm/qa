var OBJECT_TYPE_LINK="Link";
//invests the panel child index and returns it. -1 if fail. index=actual object/pane index. size=# of panes/objects in that parent panel
function getInverseIndex(index,size){
  if(index >=0){
    Log.Message("Investing index = "+index+", size = "+size+".Returns = "+(size-index-1));
    return (size-index-1);//invest the index
  }else{
    return -1;
  }
}
//returns the child if found; null if not. panel=parent panel name=childName. caseSensitive if search is caseSensitive
function getChildByName(panel, name, caseSensitive){
  var ch=Panel.getChildIndexByName(panel, name, caseSensitive);
  if(ch >=0){
    return panel.Child(ch);
  }else{
    return null;
  }
}
//searches sub item by object type and name, objectType and objectIdentifier; returns the child object if found; null if not. panel=parent panel name=childName. objectType=child Object. objectId = the object identifier value. caseSensitive if search is caseSensitive
//if objectId or name are null it does not search by this property
function getSubObjectByNameTypeAndId(panel, name, objectType, objectId, caseSensitive){
  var item;
  for(var i=0;i<Panel.getChildCount(panel);i++){
    item=panel.Child(i);
    if(!aqString.Compare(objectType, item.ObjectType,true)){
      if(!aqString.Compare(name, item.contentText,caseSensitive)){
        if(null != objectId){
          if(item.ObjectIdentifier == objectId){
            Log.Message("Object type '"+objectType+"', name '"+name+"' and Identifier '"+objectId+"' found.");
            return item;
          }
        }else{//if objectIdentifier is null returns the item
          Log.Message("Object type '"+objectType+"' and name '"+name+"' found.");
          return item;
        }
      }
    }
  }
  Log.Message("Object type '"+objectType+"' and name '"+name+"' and Identifier '"+objectId+"' NOT  found.")
  return null;
}
//returns the index of the child panel when child items are counted from bottom to top if found; -1 if not. panel=parent panel name=childName. caseSensitive if search is caseSensitive
function getInverseChildIndexByName(panel, name, caseSensitive){
 //returns the sub panel index, cou. 
  return Panel.getInverseIndex(Panel.getChildIndexByName(panel, name, caseSensitive), Panel.getChildCount(panel)); //was panel.childElementCount
}
//returns the child index if found; -1 if not
function getChildIndexByName(panel, name, caseSensitive){
  for(var i=0;i<Panel.getChildCount(panel);i++){
    if(!aqString.Compare(name,panel.Child(i).contentText, caseSensitive)){
      Log.Message("Child '"+name+"' found. Child index ="+i);
      return i;
    }
  }
  Log.Message("Child '"+name+"' NOT  found.")
  return -1;
}

//returns the panel child index of an object when child objects are counted from bottom to top if found. It counts only the object types specified by objectType; -1 if not found
//panel=parent panel name=childName. objectType=child Object Type. caseSensitive if search is caseSensitive
function getInverseChildIndexOfPanelSubObject(panel, name, objectType, caseSensitive){
  var index=0;
  var objCount=Panel.getObjectChildCount(panel, objectType);
  var item;
  for(var i=0;i<Panel.getChildCount(panel);i++){
    item=panel.Child(i);
    if(!aqString.Compare(objectType, item.ObjectType,true)){
      if(!aqString.Compare(name, item.contentText,caseSensitive)){
        index=Panel.getInverseIndex(index,objCount);
        Log.Message("Object type '"+objectType+"' and name '"+name+"' found. Index ="+index);
        return index;
      }else{
        index++;
      }
    }
  }
  Log.Message("Object type '"+objectType+"' and name '"+name+"' NOT  found.")
  return -1;
}
//returns the panel object child count for the specified objectType. panel=parent panel. objectType=child Object Type.
function getObjectChildCount(panel, objectType){
  var index=0;
  for(var i=0;i<Panel.getChildCount(panel);i++){
    if(!aqString.Compare(objectType, panel.Child(i).ObjectType,true)){
      index++;
    }
  }
  return index;
}
//returns the panel ChildCount
function getChildCount(panel){
  return panel.ChildCount;
}