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

    public class TS_Create_Idea_On_Add_New_Idea_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Enter Idea Name")]
        public void TS_CS_01()
        {
            string ideaName = Data["IdeaName"].ToString() + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_AddSubmitNewIdeaPage.IdeaNameText,ideaName);
            SetExtractedValue("GeneratedIdeaName",ideaName);
        }
        
        [CodedStep(@"Enter User name")]
        public void TS_CS_02()
        {
            Actions.SetText(Pages.PS_AddSubmitNewIdeaPage.YourNameText,Data["UserName"].ToString());
        }
        
        [CodedStep(@"Enter Project type")]
        public void TS_CS_03()
        {
            HtmlDiv ProjectTypeDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("idea_hopper_create_idea_project_type_div"));
            ProjectTypeDiv.Wait.ForExists();
            ProjectTypeDiv.Click(true);
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AddSubmitNewIdeaPage.ProjectTypeDoneButton.Wait.ForExists();
            Pages.PS_AddSubmitNewIdeaPage.ProjectTypeDoneButton.IsVisible();
               
           string objectTypeStr = Data["ProjectType"].ToString();
           Log.WriteLine(objectTypeStr);
           string[] objectTypes = System.Text.RegularExpressions.Regex.Split(objectTypeStr,"---");
               
               for(int i=0; i < objectTypes.Length ; i++)
               {
                   System.Threading.Thread.Sleep(3000);
                   string projectTypeCheckboxLoc = string.Format(AppLocators.get("add_submit_idea_project_type_input"),objectTypes[i]);
                   Log.WriteLine(projectTypeCheckboxLoc);
                   if(ActiveBrowser.Find.AllByXPath(projectTypeCheckboxLoc).Count > 0)
                   {
                       HtmlInputCheckBox projectTypeChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(projectTypeCheckboxLoc);
                       projectTypeChkbx.Click(true);
                   }
                   else
                   {
                       Assert.IsTrue(false, objectTypes[i] + " Not present in project type list");
                   }
    
               }
               
               Pages.PS_AddSubmitNewIdeaPage.ProjectTypeDoneButton.Click();
               ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Enter Department")]
        public void TS_CS_04()
        {
            Pages.PS_SubmitAnIdeaPage.DepartmentDiv.Wait.ForExists();
            Pages.PS_SubmitAnIdeaPage.DepartmentDiv.Click(true);
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AddSubmitNewIdeaPage.DepartmentDoneButton.Wait.ForExists();
            Pages.PS_AddSubmitNewIdeaPage.DepartmentDoneButton.IsVisible();
               
           string objectTypeStr = Data["Department"].ToString();          
           string[] objectTypes = System.Text.RegularExpressions.Regex.Split(objectTypeStr, "---");
               
               for(int i=0; i < objectTypes.Length ; i++)
               {
                   System.Threading.Thread.Sleep(3000);
                   string departmentCheckboxLoc = string.Format(AppLocators.get("add_submit_idea_department_checkbox"),objectTypes[i]);
                   Log.WriteLine(departmentCheckboxLoc);
                   if(ActiveBrowser.Find.AllByXPath(departmentCheckboxLoc).Count > 0)
                   {
                       HtmlInputCheckBox departmentChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(departmentCheckboxLoc);
                       departmentChkbx.Check(true);
                       Log.WriteLine(departmentCheckboxLoc);
                   }
                   else
                   {
                       Assert.IsTrue(false, objectTypes[i] + " Not present in work items list");
                   }
               }
               
               Pages.PS_AddSubmitNewIdeaPage.DepartmentDoneButton.Click();
               ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
               System.Threading.Thread.Sleep(3000);
        }
        
        [CodedStep(@"Enter Functional Group")]
        public void TS_CS_05()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlDiv functionalGrpDiv = ActiveBrowser.Find.AllByXPath<HtmlDiv>(AppLocators.get("add_submit_idea_functional_grp_div"))[0];
            functionalGrpDiv.Wait.ForExists();
            functionalGrpDiv.Click(true);
 
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AddSubmitNewIdeaPage.FunctionalGrpDoneButton.Wait.ForExists();
            Pages.PS_AddSubmitNewIdeaPage.FunctionalGrpDoneButton.IsVisible();
               
           string objectTypeStr = Data["FunctionalGroup1"].ToString();          
           string[] objectTypes = System.Text.RegularExpressions.Regex.Split(objectTypeStr, "---");
               
               for(int i=0; i < objectTypes.Length ; i++)
               {
                   System.Threading.Thread.Sleep(3000);
                   string functionalGrpCheckboxLoc = string.Format(AppLocators.get("add_submit_idea_functional_group_chkbox"),objectTypes[i]);
                   Log.WriteLine(functionalGrpCheckboxLoc);
                   if(ActiveBrowser.Find.AllByXPath(functionalGrpCheckboxLoc).Count > 0)
                   {
                       HtmlInputCheckBox functionalGrpChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(functionalGrpCheckboxLoc);
                       functionalGrpChkbx.Check(true);
                       Log.WriteLine(functionalGrpCheckboxLoc);
                   }
                   else
                   {
                       Assert.IsTrue(false, objectTypes[i] + " Not present in functional group list");
                   }
               }
               
               Pages.PS_AddSubmitNewIdeaPage.FunctionalGrpDoneButton.Click(); 
        }
        
        [CodedStep(@"Enter Location")]
        public void TS_CS_06()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlDiv locationDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("idea_hopper_create_idea_location_type_div"));
            locationDiv.Wait.ForExists();
            locationDiv.Click(true);
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AddSubmitNewIdeaPage.LocationDoneButton.Wait.ForExists();
            Pages.PS_AddSubmitNewIdeaPage.LocationDoneButton.IsVisible();
               
           string objectTypeStr = Data["Location"].ToString();          
           string[] objectTypes = System.Text.RegularExpressions.Regex.Split(objectTypeStr, "---");
               
               for(int i=0; i < objectTypes.Length ; i++)
               {
                   System.Threading.Thread.Sleep(3000);
                   string locationCheckboxLoc = string.Format(AppLocators.get("add_submit_idea_location_checkbox"),objectTypes[i]);
                   Log.WriteLine(locationCheckboxLoc);
                   if(ActiveBrowser.Find.AllByXPath(locationCheckboxLoc).Count > 0)
                   {
                       HtmlInputCheckBox locationChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(locationCheckboxLoc);
                       locationChkbx.Check(true);
                       Log.WriteLine(locationCheckboxLoc);
                   }
                   else
                   {
                       Assert.IsTrue(false, objectTypes[i] + " Not present in work items list");
                   }
               }
               
               Pages.PS_AddSubmitNewIdeaPage.ProjectTypeDoneButton.Click();
        }
    
        [CodedStep(@"Enter Stratagic Alignment")]
        public void TS_CS_07()
        {
            string strategicValue = Data["Strategic Alignment"].ToString();
            ActiveBrowser.RefreshDomTree();
            HtmlDiv StratagicAlignmentDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("idea_hopper_create_idea_stratagic_alignment_type_div"));
            StratagicAlignmentDiv.Wait.ForExists();
            StratagicAlignmentDiv.Click(true);
            //Pages.PS_AddSubmitNewIdeaPage.StratagicAlignmentDiv.Click(true);
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("add_submit_idea_strategic_alignment_div"),strategicValue));
            HtmlDiv stratagicAlignmtValueDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("add_submit_idea_strategic_alignment_div"),strategicValue));
            stratagicAlignmtValueDiv.Wait.ForExists();
            stratagicAlignmtValueDiv.MouseClick(MouseClickType.LeftClick);
            
        }
        
        [CodedStep(@"Enter Project description")]
        public void TS_CS_08()
        {
            Actions.SetText(Pages.PS_AddSubmitNewIdeaPage.OpportunityStatementTextArea,"Generated by Automation script");
            Actions.SetText(Pages.PS_AddSubmitNewIdeaPage.GoalStatementTextArea,"Creating idea for testing purpose");
        }
    
        [CodedStep(@"Click Submit button")]
        public void TS_CS_09()
        {
            Pages.PS_AddSubmitNewIdeaPage.SubmitButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
