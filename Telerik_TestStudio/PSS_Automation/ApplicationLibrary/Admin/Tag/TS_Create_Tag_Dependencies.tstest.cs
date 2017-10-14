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

    public class TS_Create_Tag_Dependencies : BaseWebAiiTest
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
        
        string depTagName;
    
        [CodedStep(@"Wait till Add New Tag Dependencies pop up is displayed")]
        public void TS_CTagDep_00()
        {
            Pages.PS_TagDependenciesPage.UpdateDetailsButtonTagDependencies.Wait.ForExists();           
            Pages.PS_TagDependenciesPage.DescriptionTextAreaTagDependencies.Wait.ForExists();
            Pages.PS_TagDependenciesPage.NameTextTagDependencies.Wait.ForVisible();                                   
                                                                                  
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CTagDep_Name()
        {
            depTagName = Data["DependenciesTagName"].ToString()+Randomizers.generateRandomInt(1000,9999);
            SetExtractedValue("DependenciesTagName",depTagName);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagDependenciesPage.NameTextTagDependencies.Click();
            Pages.PS_TagDependenciesPage.NameTextTagDependencies.Focus();
            Actions.SetText(Pages.PS_TagDependenciesPage.NameTextTagDependencies, depTagName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CTagDep_Descreption()
        {
            if(Data["DepedenciesTagDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagDependenciesPage.DescriptionTextAreaTagDependencies.Click();
            Pages.PS_TagDependenciesPage.DescriptionTextAreaTagDependencies.Focus();
            Actions.SetText(Pages.PS_TagDependenciesPage.DescriptionTextAreaTagDependencies, Data["DepedenciesTagDesc"].ToString());
            }
        }
    
        [CodedStep(@"Choose Work item")]
        public void TS_CTagdep_WorkItem()
        {
                                    
            if(!Data["DependenciesTagWorkType"].ToString().ToLower().Contains("no"))
            {
                
                HtmlDiv worktypeChoose = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("select_work_item_for_tags"));
                worktypeChoose.Wait.ForExists();
                worktypeChoose.Click();
                ActiveBrowser.RefreshDomTree();
                Pages.PS_TagsListPage.AddNewTagDailogWorkTypesDoneButton.Wait.ForVisible();
                
                string workTypeStr = Data["DependenciesTagWorkType"].ToString();
                
                string[] workType = System.Text.RegularExpressions.Regex.Split(workTypeStr,"---");
                
                for(int i=0; i < workType.Length; i++)
                {
                 string workTypeCheckboxLocator = string.Format("//input[@name='{0}']",workType[i]);
                 Log.WriteLine(workTypeCheckboxLocator);   
                 if(ActiveBrowser.Find.AllByXPath(workTypeCheckboxLocator).Count > 0)
                 {
                   HtmlInputCheckBox workItemChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(workTypeCheckboxLocator);
                   workItemChkbx.Click();
                 }
                 else
                 {
                   Assert.IsTrue(false, workType[i] + " Not present in work items list");
                 }
                }
                
                Pages.PS_TagsListPage.AddNewTagDailogWorkTypesDoneButton.Click();
                ActiveBrowser.RefreshDomTree();
            }
                                    
                                    
        }
    
        [CodedStep(@"Click on 'Update Details' button")]
        public void TS_CTagDep_AddTag()
        {
            Pages.PS_TagDependenciesPage.UpdateDetailsButtonTagDependencies.Click();
            ActiveBrowser.WaitUntilReady();
                                                
        }
    }
}
