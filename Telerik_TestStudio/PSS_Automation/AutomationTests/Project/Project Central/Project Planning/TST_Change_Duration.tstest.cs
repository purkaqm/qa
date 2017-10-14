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

    public class TST_Change_Duration : BaseWebAiiTest
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
        public void TS_Change_Duration_CodedStep()
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
            
            string durationFieldLoc = string.Format(AppLocators.get("proj_plan_duration_field_div"), containerID,level,projectIndex);
            
            HtmlDiv durationField = ActiveBrowser.Find.ByXPath<HtmlDiv>(durationFieldLoc);
            durationField.Wait.ForVisible();
            string currentDuration = durationField.InnerText;
            currentDuration = currentDuration.Replace("d","");
            
            durationField.Click();
            
            string durationRangeStr = Randomizers.generateRandomInt(1,Int32.Parse(Data["Duration"].ToString()), Int32.Parse(currentDuration)).ToString();
            ActiveBrowser.RefreshDomTree();
            HtmlInputText firstEditableField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("proj_plan_editable_duration_input"));
            firstEditableField.Wait.ForVisible();
            Actions.SetText(firstEditableField, durationRangeStr);
            firstEditableField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            SetExtractedValue("durationRangeStr",durationRangeStr);
            SetExtractedValue("durationFieldLoc",durationFieldLoc);
            Log.WriteLine("Entered Duration : " + durationRangeStr);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
    
        
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Duration_CodedStep1()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Duration_CodedStep2()
        {
            
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Duration_CodedStep3()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv durationFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("durationFieldLoc").ToString());
            durationFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , durationFieldToVerify.InnerText.Contains(GetExtractedValue("durationRangeStr").ToString()),"Duration Update Failed");
            
            
        }
    
        
    }
}
