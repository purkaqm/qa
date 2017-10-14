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

    public class TST_Change_Actual_Dates : BaseWebAiiTest
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
        public void TS_Change_Actual_Dates_CodedStep()
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
            
            
            string startDateFieldLoc = string.Format(AppLocators.get("proj_plan_act_start_date_field_div"), containerID,level,projectIndex);
            
            HtmlDiv startDateField = ActiveBrowser.Find.ByXPath<HtmlDiv>(startDateFieldLoc);
            startDateField.Wait.ForVisible();
            string currStartDate = startDateField.InnerText;
            DateTime currDateVal = DateTime.Today;
            if(currStartDate.Length > 0){
                currDateVal = Convert.ToDateTime(currStartDate);
            }
                        
            startDateField.Click();
          
            
            int month = Int32.Parse(Data["StartMonth"].ToString());
            DateTime randomStartDate = Randomizers.generateRandomDate(DateTime.Today,month*30,currDateVal); 
            string startDateToEnter = randomStartDate.ToString("MM/dd/yyyy");
            ActiveBrowser.RefreshDomTree();
            HtmlInputText firstEditableDateField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("proj_plan_editable_date_field_input"));
            firstEditableDateField.Wait.ForVisible();
            Actions.SetText(firstEditableDateField, startDateToEnter);
            firstEditableDateField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            
            
            string endDateFieldLoc = string.Format(AppLocators.get("proj_plan_act_end_date_field_div"), containerID,level,projectIndex);
            
            HtmlDiv endDateField = ActiveBrowser.Find.ByXPath<HtmlDiv>(endDateFieldLoc);
            endDateField.Wait.ForVisible();
            endDateField.Click();
            
            DateTime randomEndDate = Randomizers.generateRandomDate(randomStartDate,Int32.Parse(Data["EndDays"].ToString()));
            string endDateToEnter = randomEndDate.ToString("MM/dd/yyyy");
            
            HtmlInputText secondEditableDateField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("proj_plan_editable_date_field_input"));
            secondEditableDateField.Wait.ForVisible();
            Actions.SetText(secondEditableDateField, endDateToEnter);
            secondEditableDateField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            
            SetExtractedValue("startDateFieldLoc",startDateFieldLoc);
            SetExtractedValue("startDateToEnter",startDateToEnter);
            
            SetExtractedValue("endDateFieldLoc",endDateFieldLoc);
            SetExtractedValue("endDateToEnter",endDateToEnter);
            
            Log.WriteLine("Entered Start Date : " + startDateToEnter);
            Log.WriteLine("Entered End Date : " + endDateToEnter);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
     
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Actual_Dates_CodedStep1()
        {
            // Click 'Button0'
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
             this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Actual_Dates_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv startDateFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("startDateFieldLoc").ToString());
            startDateFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , startDateFieldToVerify.InnerText.Contains(GetExtractedValue("startDateToEnter").ToString()),"Start Date Update Failed");
            
            
            HtmlDiv endDateFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("endDateFieldLoc").ToString());
            endDateFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , endDateFieldToVerify.InnerText.Contains(GetExtractedValue("endDateToEnter").ToString()),"End Date Update Failed");
            
                       
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Actual_Dates_CodedStep3()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    }
}
