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

    public class TST_Change_Percentage_Completed : BaseWebAiiTest
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
        public void TS_Change_Percentage_Completed_CodedStep()
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
            
            
            string percentageCompFieldLoc = string.Format(AppLocators.get("proj_plan_perc_comp_field_div"), containerID,level,projectIndex);
            
            HtmlDiv percentageCompField = ActiveBrowser.Find.ByXPath<HtmlDiv>(percentageCompFieldLoc);
            percentageCompField.Wait.ForVisible();
            string currPercentage = percentageCompField.InnerText;
            currPercentage = currPercentage.Replace("%","");
            
            percentageCompField.Click();
             
            string percentageCompStr = Randomizers.generateRandomInt(1,100, Int32.Parse(currPercentage)).ToString();
            ActiveBrowser.RefreshDomTree();
            HtmlInputText firstEditableField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("proj_plan_editable_perc_input"));
            firstEditableField.Wait.ForVisible();
            Actions.SetText(firstEditableField, percentageCompStr);
            firstEditableField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            SetExtractedValue("percentageCompStr",percentageCompStr);
            SetExtractedValue("percentageCompFieldLoc",percentageCompFieldLoc);
            Log.WriteLine("Entered Percentage : " + percentageCompStr);
            
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
     
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Percentage_Completed_CodedStep1()
        {
            // Click 'Button0'
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Percentage_Completed_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv percentageCompFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("percentageCompFieldLoc").ToString());
            percentageCompFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , percentageCompFieldToVerify.InnerText.Contains(GetExtractedValue("percentageCompStr").ToString()),"Percentage Completed Update Failed");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Percentage_Completed_CodedStep3()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    }
}
