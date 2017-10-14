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

    public class TST_UCO_019 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_019_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_UCO_019_CS02()
        {
            Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Verify new 'Custom field' is created")]
        public void TST_UCO_019_CS03()
        {
            HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldRow.Wait.ForExists();
            Assert.IsTrue(customFieldRow.IsVisible());
        }
        
        [CodedStep(@"Turn on Enforce required tags and required custom fields on Invite New User column")]
        public void TST_UCO_019_CS04()
        {
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Checked)
            {
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_019_CS05()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
            ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on User link on 'Field Management' page")]
        public void TST_UCO_019_CS06()
        {
            Pages.PS_FieldManagementPage.OtherUserLink.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Visibility link against created tag")]
        public void TST_UCO_019_CS07()
        {
            HtmlDiv visibilityLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("field_management_edit_page_custom_field_visibility_link"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            Log.WriteLine(string.Format(AppLocators.get("field_management_edit_page_custom_field_visibility_link"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            visibilityLink.Wait.ForExists();
            visibilityLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Connect to 'Formula Builder' window")]
        public void TST_UCO_019_CS08()
        {
            // Connect to pop-up window
            Manager.WaitForNewBrowserConnect("/eml/EMLBuilder.epage", true, 15000);
            
            Manager.ActiveBrowser.WaitUntilReady();  
        }
    
        [CodedStep(@"Enter value in Formula Builder")]
        public void TST_UCO_019_CS09()
        {
            string formulaValue = "#viewing";
            ActiveBrowser.Window.SetFocus();
            Pages.PS_FormulaBuilderPage.FormulaBuilderEditTextArea.Click();
            Manager.Desktop.KeyBoard.TypeText(formulaValue,2);
        }
    
        [CodedStep(@"Click on Ok button")]
        public void TST_UCO_019_CS10()
        {
            Pages.PS_FormulaBuilderPage.FormulaBuilderOkBtn.Click(true);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_019_CS11()
        {
            Pages.PS_EditFieldsUserPage.SaveBtn.Click(true);
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on pop-up ok button")]
        public void TST_UCO_019_CS12()
        {
            Pages.PS_EditFieldsUserPage.PopUpOkBtn.Click(true);
            
        }
    
        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_UCO_019_CS13()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_UCO_019_CS14()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
       
    
        [CodedStep(@"Verify Created custom field is not displayed on Invite New User page")]
        public void TST_UCO_019_CS15()
        {
            Log.WriteLine(string.Format(AppLocators.get("custom_field_not_found_cell"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            HtmlTableCell customField = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("custom_field_not_found_cell"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            Assert.IsNull(customField,"Custom Field should not be present on Invite New User Page");
        }
    
        [CodedStep(@"Delete created custom tag")]
        public void TST_UCO_019_CS16()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            
            Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
            ActiveBrowser.WaitUntilReady();          
        }
    
        
    }
}
