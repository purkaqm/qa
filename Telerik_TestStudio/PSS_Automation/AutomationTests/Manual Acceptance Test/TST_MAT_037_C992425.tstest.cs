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

    public class TST_MAT_037_C992425 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_MAT_037_C992425_ClickAddTab()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_MAT_037_C992425_ClickAddUserTab()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is added")]
        public void TST_MAT_037_C992425_VerifyUser()
        {
            Pages.PS_InviteCreateNewUserPage.AddedUserMsgBoxDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_InviteCreateNewUserPage.AddedUserMsgBoxDiv.IsVisible());
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
        [CodedStep(@"Set username")]
        public void TST_MAT_037_C992425_SetUserName()
        {
            ActiveBrowser.RefreshDomTree();
            CustomUtils.locationValue = GetExtractedValue("FirstName").ToString();
        }
    
        [CodedStep(@"Delete created user")]
        public void TST_MAT_037_C992425_DeleteUser()
        {
            Pages.PS_UserProfilePage.DeleteLink.Click();
            Pages.PS_UserProfilePage.DeleteUserOkButton.Wait.ForExists();
            Pages.PS_UserProfilePage.DeleteUserOkButton.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Delete User"),"Page header title should changed to 'Delete user'");
        }
    }
}
