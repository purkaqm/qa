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

    public class TST_Create_Tag_With_Work_Type : BaseWebAiiTest
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
        
        string custTagName;
    
        [CodedStep(@"Wait till Add New Tag pop up is displayed")]
        public void TS_CTAG_00()
        {
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Wait.ForExists();           
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Wait.ForExists();
            Pages.PS_TagsListPage.AddNewTagsDailogAddTagButton.Wait.ForVisible();                                   
                                                                      
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CTAG_Name()
        {
            custTagName = Data["CustomTagName"].ToString()+Randomizers.generateRandomInt(1000,9999);
            SetExtractedValue("CustTagName",custTagName);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Click();
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Focus();
            Actions.SetText(Pages.PS_TagsListPage.AddNewTagDialogNameText, custTagName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CTAG_Descreption()
        {
            if(Data["CustomTagDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Click();
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Focus();
            Actions.SetText(Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea, Data["CustomTagDesc"].ToString());
            }
        }
    
        [CodedStep(@"Choose Work item")]
        public void TS_CTAG_WorkItem()
        {
                        
            if(!Data["TagWorkType"].ToString().ToLower().Contains("no"))
            {
                
                HtmlDiv worktypeChoose = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("select_work_item_for_tags"));
                worktypeChoose.Wait.ForExists();
                worktypeChoose.Click();
                ActiveBrowser.RefreshDomTree();
                Pages.PS_TagsListPage.AddNewTagDailogWorkTypesDoneButton.Wait.ForVisible();
                
                string workTypeStr = Data["TagWorkType"].ToString();
                
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
    
        [CodedStep(@"Click on 'Add Tag' button")]
        public void TS_CTAG_AddTag()
        {
            Pages.PS_TagsListPage.AddNewTagsDailogAddTagButton.Click();
            ActiveBrowser.WaitUntilReady();
                                    
        }
    }
}
