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

    public class TS_Create_Custom_Tag : BaseWebAiiTest
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
        
        protected string custTagName;
    
        [CodedStep(@"Wait till Add New Tag pop up is displayed")]
        public void TS_CTAG_00()
        {
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Wait.ForExists();           
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Wait.ForExists();
            Pages.PS_TagsListPage.AddNewTagsDailogAddTagButton.Wait.ForVisible();                                   
                                                          
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CTAG_01()
        {
            custTagName = Data["CustomTagName"].ToString()+Randomizers.generateRandomInt(1000,9999);
            SetExtractedValue("CustTagName",custTagName);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Click();
            Pages.PS_TagsListPage.AddNewTagDialogNameText.Focus();
            Actions.SetText(Pages.PS_TagsListPage.AddNewTagDialogNameText, custTagName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CTAG_02()
        {
            if(Data["CustomTagDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Click();
            Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea.Focus();
            Actions.SetText(Pages.PS_TagsListPage.AddNewTagDailogDescriptionTextArea, Data["CustomTagDesc"].ToString());
            }
        }
    
        [CodedStep(@"Add Administrators")]
        public void TS_CTAG_03()
        {
            //code for adding administrator
        }
        
        [CodedStep(@"Choose Checkbox for Hierarchical option")]
        public void TS_CTAG_04()
        {
            if(Data["HierarchicalValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagDialogHierarchicalChkkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for Locked option")]
        public void TS_CTAG_05()
        {
            if(Data["LockedValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogLockedChkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for 'Alert & Event Logging' option")]
        public void TS_CTAG_06()
        {
            if(Data["AlertValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogAlertableChkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for 'Apply Permission' option")]
        public void TS_CTAG_07()
        {
            if(Data["ApplyPermissionValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogPermissionsChkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for Required option")]
        public void TS_CTAG_08()
        {
            if(Data["RequiredValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogMandatoryChkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for 'Allow multiple values' option")]
        public void TS_CTAG_09()
        {
            if(Data["AllowMultipleValues"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogAllowMultipleChkBox.Click();
            }
           
        }
        
        [CodedStep(@"Choose Checkbox for 'Associate colors' option")]
        public void TS_CTAG_10()
        {
            if(Data["AssociatecolorsValue"].ToString().ToLower().Contains("yes")){
                Pages.PS_TagsListPage.AddNewTagsDailogAllowColorsChkBox.Click();
            }
           
        }
    
        [CodedStep(@"Choose Other Association Item 'People'")]
        public void TS_CTAG_11()
        {
            string peopleValue = Data["PeopleValue"].ToString();
            Pages.PS_TagsListPage.AddNewTagsDailogPeopleSpan.Click();
            HtmlInputCheckBox selectPeople = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("add_new_tag_dailog_people_input"),peopleValue));
            selectPeople.Wait.ForExists();
            selectPeople.Click();
            Pages.PS_TagsListPage.AddNewTagDailogPeopleDoneButton.Click();
            
                        
        }
        
        [CodedStep(@"Click on 'Add Tag' button")]
        public void TS_CTAG_12()
        {
            Pages.PS_TagsListPage.AddNewTagsDailogAddTagButton.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
    }
}
