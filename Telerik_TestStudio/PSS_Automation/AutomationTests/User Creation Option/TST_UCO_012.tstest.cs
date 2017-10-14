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

    public class TST_UCO_012 : BaseWebAiiTest
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
        public void TST_UCO_012_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Turn on At least one group must be specified on Invite New User column")]
        public void TST_UCO_012_CS02()
        {
            if(!Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Checked)
            {
            Pages.PS_UserCreationOptionsPage.AtleastGroupInviteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
            }
            
            if(Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Checked)
            {
            Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Click();
            ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_012_CS03()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
            ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_UCO_012_CS04()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_UCO_012_CS05()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Groups section is displayed with red dot")]
        public void TST_UCO_012_CS06()
        {
            Assert.IsTrue(Pages.PS_InviteCreateNewUserPage.AddUserGroupsRedDotImage.IsVisible(),"Groups section should be displayed with red dot");
        }
    }
}
