//USEUNIT WorkTreePage
//USEUNIT Panel
//Searches a project by name on WorTree and returns the link to the project if found, or  null when not.
//iName=wi to search, waitTime=work tree load time in ms
function searchItemOnWorkTreePanel(iName, panel){  //searches a project by name and opens it.
    var ch=Panel.getChildCount(panel);//was childElementCount;
    for(var i=0;i<ch;i++){
      if(!aqString.Compare(iName,panel.Child(i).contentText,true)){
        if(!aqString.Compare(iName,panel.Child(i).Child(0).Child(0).contentText,true)){//because in some cases panel.Child(i).Child(0).Child(1) is the project link
          return panel.Child(i).Child(0).Child(0);
        }else{
          return panel.Child(i).Child(0).Child(1);
        }
      }
    }
    return null;
}
//searches a item on WorkTree. iName= item name. parents=array of item parent nanmes. 
///waitTime=is the wait time in seconds to wait one item to be expanded.
function searchOnWorkTree(iName,parents){
//ATTENTION: This method could fail when there are more than one project/item with the same name in the same level or
            //when the parent project name is equals to his unique child
  Log.Message("searching work item '"+iName+"' on Work Tree.");
  WorkTreePage.openPageFromLink(WorkTreePage.getMaxWaitTime()*1000);  //wait for workTree loading
  if(WorkTreePage.existsWorkTree()){
    var item=WorkTreePage.getWorkTree();// the whole work tree
    if(null == parents){//if project has no parents
      item=WorkTree.searchItemOnWorkTreePanel(iName, item);
    }else{
      var found;
      for(var i=0;i<parents.length;i++){
        found= false;
        for(var j=0;j<Panel.getChildCount(item);j++){//was item.childElementCount
          if(!aqString.Compare(parents[i],item.Child(j).contentText,true)){
            item=WorkTree.expandItemAndWait(item.Child(j));
            if(null != item){
              found =true;
            }
            break;
          }
        }
        if(!found){
          return null;
        }
      }
      item=WorkTree.searchItemOnWorkTreePanel(iName, item);
    }
    Log.Message("Item '"+iName+"' "+(null == parents?"NOT ":"")+"found on Work Tree"+(null == parents?"":", under '"+parents+"'."));
    return item;
  }else{
    Log.Message("Project '"+iName+"' not found. Work tree does not exist.");
    return null;
  }
}
//expands a Work Tree item and waits until child panel is loaded and returns it. When wait time expires, logs error and return null
function expandItemAndWait(panel){
  var height=panel.Height;
  var parentOldContent=panel.contentText;//the parent name before expanding it
  panel.Click(panel.Height/2, 10);//expands the parent, it increases it height and adds 1 child; children=2
  var c=0;
  while(panel.Height == height){ //waits only 1 second if panel is not expanded
    Log.Message("Waiting 1 second until panel is loaded");
    panel.WaitSlApplication(1000);
    c++;
    if(c > WorkTreePage.getMaxWaitTime()){
      Log.Error("Wait time expired before item expand finishes. WorkTree item expand failure. Item name= '"+panel.contentText+"'.");
      return null;
    }
  }
  if(!aqString.Compare(panel.Child(0).contentText,parentOldContent,true)){//it is possible that be the same item than the parent project name before expanding it, then return child(1). 
  //ATTENTION:  It coul fail when there is only one childProject and the parent name is equals to the child project, then it can not recognize if the returned panel is the parent or the child.
  //because in some inexplicable situations the child project its contained in Child(1) but in most of the cases in Child(1). IT CAN CAUSE NOT FOUND ERROR
    Log.Message("Return 1 = "+panel.Child(1).Child(0).contentText);
    return panel.Child(1).Child(0);
  }else{
    Log.Message("Return 0 = "+panel.Child(0).Child(0).contentText);
    return panel.Child(0).Child(0);
  }
}