using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

using ArtOfTest.Common.UnitTesting;
using ArtOfTest.WebAii.Core;
using ArtOfTest.WebAii.Controls.HtmlControls;
using ArtOfTest.WebAii.Controls.HtmlControls.HtmlAsserts;
using ArtOfTest.WebAii.Design;
using ArtOfTest.WebAii.Design.Execution;
using ArtOfTest.WebAii.ObjectModel;
using ArtOfTest.WebAii.Silverlight;
using ArtOfTest.WebAii.Silverlight.UI;

namespace PSS_Automation
{

    public class TST_Change_Status_Project_Planning : BaseWebAiiTest
    {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

        /// <summary>
        /// Gets the Pages object that has references
        /// to all the elements, frames or regions
        /// in this project.
        /// </summary>
        public Pages Pages
        {
            get
            {
                if (_pages == null)
                {
                    _pages = new Pages(Manager.Current);
                }
                return _pages;
            }
        }

        #endregion
        
        // Add your test methods here...
    
        [CodedStep(@"Click 'Change_Dates'")]
        public void TS_Change_Status_CodedStep()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string workTree = Data["Name"].ToString();
             string containerID = Data["containerID"].ToString();
             string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
             int level = workTreeArray.Length ;
             string lastChild = workTreeArray[level-1];
             int projectIndex = 1;
            
            IList<Element> projects = ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("proj_plan_node_list_div"),containerID,level));
            for(int i=1 ; i <= projects.Count; i++){
                 if(projects[i-1].InnerText.Contains(lastChild)){
                       projectIndex = i;
                       break;   
                 }
            }
            
            
            string statusFieldLoc = string.Format(AppLocators.get("proj_plan_status_field_div"), containerID,level,projectIndex);
            HtmlDiv statusField = ActiveBrowser.Find.ByXPath<HtmlDiv>(statusFieldLoc);
            statusField.Wait.ForVisible();
            string currentStatus = statusField.InnerText;
            statusField.Click();
            string newStatus;
            if(Data["Status1"].ToString().Equals(currentStatus)){
                newStatus = Data["Status2"].ToString();
            }
            else if(Data["Status2"].ToString().Equals(currentStatus)){
                newStatus = Data["Status1"].ToString();
            }
            else {
                newStatus = Data["Status1"].ToString();
            }
            SetExtractedValue("statusFieldLoc",statusFieldLoc);
            SetExtractedValue("newStatus",newStatus);
            
            
            
     
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Status_CodedStep1()
        {
            // Click 'Button0'
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
            Manager.ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Status_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv statusFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("statusFieldLoc").ToString());
            statusFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , statusFieldToVerify.InnerText.Contains(GetExtractedValue("newStatus").ToString()),"Status Update Failed");
            
            
        }
    
        [CodedStep(@"Click 'PsUiMenuItem2TextTableCell'")]
        public void TS_Change_Status_CodedStep3()
        {
            ActiveBrowser.RefreshDomTree();
            string newStatus = GetExtractedValue("newStatus").ToString();
            string findExpression = string.Format(AppLocators.get("proj_plan_new_status_icon_img"),newStatus);
            HtmlImage newStatusIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(findExpression);
            newStatusIcon.Click(false);
            Log.WriteLine("Entered Status : " + newStatus);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Status_Project_Planning_CodedStep()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    }
}
