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

    public class TST_HIST_003 : BaseWebAiiTest
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
    
        [CodedStep(@"Click History tab on user profile page")]
        public void TST_HIST_003_CodedStep()
        {
            Pages.PS_SettingsProfilePage.HistoryTabLink.Wait.ForExists();
            Pages.PS_SettingsProfilePage.HistoryTabLink.Click();
            Pages.PS_ManageHistoryPage.HistorySearchFormDiv.Wait.ForVisible();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
    
        [CodedStep(@"Verify user is navigated to Manage History page")]
        public void TST_HIST_003_CodedStep1()
        {
             Pages.PS_ManageHistoryPage.GoButton.Wait.ForExists();
               Assert.IsTrue(Pages.PS_ManageHistoryPage.SearchInputBox.IsVisible(),"User should be navigated to History page");
                        Assert.IsTrue(Pages.PS_ManageHistoryPage.FromDateInputBox.IsVisible(),"User should be navigated to History page");
                        Assert.IsTrue(Pages.PS_ManageHistoryPage.GoButton.IsVisible(),"User should be navigated to History page");
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records")).Count > 0,"Records with name should be present");
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_desc_records")).Count > 0,"Records with description should be present");
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_url_records")).Count > 0,"Records with url should be present");
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_delete_icon_records")).Count > 0,"Records with delete icon should be present");
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_add_to_fav_icon_records")).Count > 0,"Records with add to fav icon should be present");
        
        }
    
        [CodedStep(@"Click Edit User link")]
        public void TST_HIST_003_CodedStep3()
        {
                        Pages.PS_UserProfilePage.EditUserLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        
                        
        }
    
        [CodedStep(@"Wait till user navigated to Edit Profle page")]
        public void TST_HIST_003_CodedStep4()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForExists();
                        Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    }
}
