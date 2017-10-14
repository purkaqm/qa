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

    public class TST_Change_Dependency : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Dependency_CodedStep()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    
        [CodedStep(@"Click 'Change_Dates'")]
        public void TS_Change_Dependency_CodedStep1()
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
            
            string dependencyStartRangeLoc = string.Format(AppLocators.get("proj_plan_dep_start_range_div"), containerID,level);
            string dependencyEndRangeLoc = string.Format(AppLocators.get("proj_plan_dep_end_range_div"), containerID,level,projectIndex);
            
            HtmlDiv dependencyStartRangeField = ActiveBrowser.Find.ByXPath<HtmlDiv>(dependencyStartRangeLoc);
            HtmlDiv dependencyEndRangeField = ActiveBrowser.Find.ByXPath<HtmlDiv>(dependencyEndRangeLoc);
            
            
            string startIndexStr = dependencyStartRangeField.InnerText;
            string endIndexStr = dependencyEndRangeField.InnerText;
            
            int startIndex = Int32.Parse(startIndexStr); 
            int endIndex = (Int32.Parse(endIndexStr) - 1); 
            
            
                
            
            string dependencyFieldLoc = string.Format(AppLocators.get("proj_plan_dep_field_div"), containerID,level,projectIndex);
            HtmlDiv dependencyField = ActiveBrowser.Find.ByXPath<HtmlDiv>(dependencyFieldLoc);
            dependencyField.Wait.ForVisible();
            string currDep = dependencyField.InnerText;
            int currDependency = startIndex + 1;
            if(currDep.Length > 0){
                currDependency = Int32.Parse(currDep);
            }
            dependencyField.Click();
            
            string dependencyStr = Randomizers.generateRandomInt(startIndex,endIndex,currDependency).ToString();
            ActiveBrowser.RefreshDomTree();
            HtmlInputText firstEditableField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("proj_plan_editable_dep_input"));
            firstEditableField.Wait.ForVisible();
            Actions.SetText(firstEditableField, dependencyStr);
            firstEditableField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            SetExtractedValue("dependencyStr",dependencyStr);
            SetExtractedValue("dependencyFieldLoc",dependencyFieldLoc);
            Log.WriteLine("Entered Dependency : " + dependencyStr);
            
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
     
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Dependency_CodedStep2()
        {
            // Click 'Button0'
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Dependency_CodedStep3()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv dependencyFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("dependencyFieldLoc").ToString());
            dependencyFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , dependencyFieldToVerify.InnerText.Contains(GetExtractedValue("dependencyStr").ToString()),"Dependency Update Failed");
            
            
        }
    }
}
