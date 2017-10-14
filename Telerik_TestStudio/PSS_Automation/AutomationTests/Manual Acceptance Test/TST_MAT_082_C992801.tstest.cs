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

    public class TST_MAT_082_C992801 : BaseWebAiiTest
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
        public void TST_MAT_082_C992801_ClickAddTab()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_MAT_082_C992801_ClickAddUserTab()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify user is created and open user profile page")]
        public void TST_MAT_082_C992801_VerifyUser()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_InviteCreateNewUserPage.SuccessMsgBoxDiv.Wait.ForExists();
            HtmlDiv createdUserLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format("//div[contains(@id,'uid') and @class='link'][contains(.,'{0}')]",GetExtractedValue("FirstName").ToString())); 
            Assert.IsTrue(createdUserLink.IsVisible());
            createdUserLink.Click();
            Pages.PS_InviteCreateNewUserPage.MoreLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Delete created user")]
        public void TST_MAT_082_C992801_DeleteUser()
        {
            Pages.PS_UserProfilePage.DeleteLink.Click();
            Pages.PS_UserProfilePage.DeleteUserOkButton.Wait.ForExists();
            Pages.PS_UserProfilePage.DeleteUserOkButton.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Delete User"),"Page header title should changed to 'Delete user'");
        }
    
    
        [CodedStep(@"Delete the user permanently through deleted_user.jsp page")]
        public void TST_MAT_082_C992801_DeleteUserPermanently()
        {
            HtmlInputCheckBox userCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//div[@class='deleted'][contains(.,'{0}')]/input",GetExtractedValue("FirstName").ToString()));
            userCheckbox.Wait.ForExists();
            userCheckbox.ScrollToVisible();
            userCheckbox.Check(true);
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Click();
            Pages.PS_Deleted_Users_Page.PermanentlyDeleteUserProgressContainer.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForVisible();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Click();
        }
        
    }
}
