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

    public class TST_UCO_006 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Admin left navigation tab is present")]
        public void TST_UCO_006_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_006_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that in left navigation title 'admin' is displayed")]
        public void TST_UCO_006_CS03()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.BaseElement.InnerText.Contains("ADMIN"));
        }
    
        [CodedStep(@"Select all checkboxes")]
        public void TST_UCO_006_CS04()
        {
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Checked)               
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Checked) 
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Checked)
            Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.AtleastGroupImportUserChkbox.Checked)
            Pages.PS_UserCreationOptionsPage.AtleastGroupImportUserChkbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Checked)
            Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.RequireTreeLocatnImportUserChkbox.Checked)
            Pages.PS_UserCreationOptionsPage.RequireTreeLocatnImportUserChkbox.Click();
            if(!Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Checked)
            Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Save the changes")]
        public void TST_UCO_006_CS05()
        {
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
        }  
        
        [CodedStep(@"Verify all Checkboxes are marked")]
        public void TST_UCO_006_CS06()
        {
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.AtleastGroupImportUserChkbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.RequireTreeLocatnImportUserChkbox.Checked);
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Checked);
            
            //undo the changes
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Click();
            Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Click();
            Pages.PS_UserCreationOptionsPage.AtleastGroupImportUserChkbox.Click();
            Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Click();
            Pages.PS_UserCreationOptionsPage.RequireTreeLocatnImportUserChkbox.Click();
            Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Click();
            
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
        }
    }
}
