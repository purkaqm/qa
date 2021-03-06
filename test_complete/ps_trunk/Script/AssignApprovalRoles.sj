//USEUNIT AssignApprovalRolesPage
//USEUNIT ApprovalRolesPopUp
function assignApprovalRoles(gate, approvers){
  var i, j, g, a, a1, a2, a3, approver, label, checkBox;
  var exists=false;
  g=AssignApprovalRolesPage.getApprovalRolesTable().FindChild(["ObjectType","contentText","ColumnIndex"], ["Cell",gate,1], 10, true);
  if(g.Exists){
    a=AssignApprovalRolesPage.getApprovalRolesTable().FindChild(["ObjectType","RowIndex"], ["Cell",g.RowIndex], 10, true);
    if(a.Exists){
      a2=a.FindChild(["ObjectType", "className"], ["Panel", "Selector"], 10, true);
      if(a2.Exists){
        a2.Click();
        var popUpContent=ApprovalRolesPopUp.getProjectApprovalRolesPopUpContentPanel();
        for (i=0;i<popUpContent.ChildCount;i++){
          a3=popUpContent.Child(i);
          if(a3.Exists){
            label=a3.contentText;
            for (j=0;j<approvers.length;j++){
              approver=approvers[j];
              if(!aqString.Compare(label,approver,true)){//if approver should be selected
                exists=true;
                break;
              }
            }
            checkBox=popUpContent.FindChild(["ObjectType", "ObjectLabel"], ["Checkbox", label], 20, true);
            if(checkBox.Exists){
              checkBox.ClickChecked(exists);
            }else{
              Log.Warning("Approver's CheckBox with label '"+approver+"' was not found on Assign Approval Roles Page.");
              return false;
            }
            exists=false;
          }else{
            Log.Warning("Approver's Panel with id '"+i+"' was not found on Assign Approval Roles Page.");
            return false;
          }
        }
        ApprovalRolesPopUp.getProjectApprovalRolesPopUpDoneControl().Click();
        for (j=0;j<approvers.length;j++){
          if(aqString.Find(a2.contentText,approvers[j],0,true) < 0){
            Log.Warning("Approver '"+approvers[j]+"' was not found on the selected Approval Roles Combo Box in Assign Approval Roles Page.");
            return false;
          }
        }
        return true;
      }else{
        Log.Warning("Approver's Combo Box was not found on Assign Approval Roles Page.");
        return false;
      }
    }else{
      Log.Warning("Approver's Cell was not found on Assign Approval Roles Page.");
      return false;
    }
  }else{
    Log.Warning("Gate '"+gate+"' was not found on Assign Approval Roles Page.");
    return false;
  }
}