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

    public class TST_UCO_021 : BaseWebAiiTest
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
        public void TST_UCO_021_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_UCO_021_CS02()
        {
            Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Verify new 'Custom field' is created")]
        public void TST_UCO_021_CS03()
        {
            HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldRow.Wait.ForExists();
            Assert.IsTrue(customFieldRow.IsVisible());
        }
    
        [CodedStep(@"Turn on Enforce required tags and required custom fields on Invite New User column")]
        public void TST_UCO_021_CS04()
        {
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Checked)
            {
                Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_021_CS05()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
                Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
    
        [CodedStep(@"Click on User link on Field Manament page")]
        public void TST_UCO_021_CS06()
        {
            Pages.PS_FieldManagementPage.OtherUserLink.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Check Required and Locked aginst created tag")]
        public void TST_UCO_021_CS07()
        {
            HtmlInputCheckBox requiredCheckbox  = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("edit_field_user_required_checkbox"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            requiredCheckbox.Wait.ForExists();
            if(!requiredCheckbox.Checked)
                {
                    requiredCheckbox.Click();
                }
                
            HtmlInputCheckBox lockedCheckbox  = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("edit_field_user_locked_checkbox"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            lockedCheckbox.Wait.ForExists();
            if(!lockedCheckbox.Checked)
                {
                    lockedCheckbox.Click();
                }    
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_021_CS08()
        {
            if(Pages.PS_EditFieldsUserPage.SaveBtn.IsEnabled)
            {
                Pages.PS_EditFieldsUserPage.SaveBtn.Click(true);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();
                Pages.PS_EditFieldsUserPage.PopUpOkBtn.Click(true);
            }
        }
    
        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_UCO_021_CS09()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_UCO_021_CS10()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify require Custom Field is displayed without 'red dot'")]
        public void TST_UCO_021_CS11()
        {
            HtmlImage redDotCustumField = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("custom_field_red_dot_img"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            Assert.AreEqual<string>("",redDotCustumField.BaseElement.GetAttribute("alt").Value.ToString(),"Red dot should not be present");
        }
    
        [CodedStep(@"Verify requires Custom Field is 'Locked'")]
        public void TST_UCO_021_CS12()
        {
            HtmlDiv customFieldLocked = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("custom_field_locked_div"),GetExtractedValue("GeneratedCustomFieldName").ToString()));            
            Assert.IsNull(customFieldLocked,"Custom field should be locked");
        }
    
        [CodedStep(@"Delete created custom tag")]
        public void TST_UCO_021_CS13()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            
            Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
            ActiveBrowser.WaitUntilReady();          
        }
    }
}
