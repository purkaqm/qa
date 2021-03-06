//USEUNIT SearchUserDijitPopUp
//USEUNIT Logger
function selectUsers(users){//"LastName, Name"
  var user, user2, res, res2;
  for(var i=0;i<users.length;i++){
    user=users[i];
    SearchUserDijitPopUp.setFindText(user);
    var c=0;
    do{
      SearchUserDijitPopUp.clickGoControl();
      SearchUserDijit.waitToResults();
      res=SearchUserDijitPopUp.getSearchResultsPanel().FindChild(["ObjectType","contentText","idStr"], ["Panel","*"+user+"*",""], 10, true); //idStr="" due to there is another panel (searchResults) but it idStr is resultsDiv
      if(res.Exists){
        res.Click();
        res2=SearchUserDijitPopUp.getSelectedResultsPanel().FindChild(["ObjectType","contentText"/*,"childElementCount"*/], ["TextNode",user2/*,"0"*/], 10, true);
        if(!res2.Exists){
          Log.Message("User "+user+" could not be selected from results list in Users Dijit Dialog.");//It could not be selected when the user is already selected
          break;
        }
      }else{
        c++;
        SearchUserDijitPopUp.getSelectedResultsPanel().WaitSlApplication("3000");
      }
    }while(c<10)
    if(!res.Exists){
      Log.Error("User "+user+" could not be found in Users Dijit Dialog.");
    }
  }
  if(SearchUserDijitPopUp.getSelectedResultsPanel().ChildCount > 0){//if there are selected users
    SearchUserDijitPopUp.clickSaveControl();
    return true;
  }else{
    SearchUserDijitPopUp.clickCancelControl();
    return false;
  }
}

function waitToResults(){
  SearchUserDijitPopUp.getSearchResultsPanel().WaitAliasChild("panelResults",10000);//if user was found or the following message is displayed:"No matches found. Please try again."
}