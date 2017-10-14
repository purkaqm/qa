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

    public class TST_UCO_022 : BaseWebAiiTest
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
        public void TST_UCO_022_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Turn On Display default work tree location if not required")]
        public void TST_UCO_022_CS02()
        {
            if(!Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Checked)
            {
                Pages.PS_UserCreationOptionsPage.DispalyDefaultLocatnInviteUserChkbox.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
        
        [CodedStep(@"Turn On Require default work tree location")]
        public void TST_UCO_022_CS03()
        {
            if(!Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Checked)
            {
                Pages.PS_UserCreationOptionsPage.RequireTreeLocatnInviteUserCkhbox.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_022_CS04()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
                Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_UCO_022_CS05()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_UCO_022_CS06()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Display default work tree location drop down list with label and description is displayed ")]
        public void TST_UCO_022_CS07()
        {
            Assert.IsTrue(Pages.PS_InviteCreateNewUserPage.DefaultLocationWorkTreeAcronymTag.IsVisible());
            Assert.IsTrue(Pages.PS_InviteCreateNewUserPage.DefaultLocWorkTreeFieldDiv.IsVisible());
        }
    }
}
