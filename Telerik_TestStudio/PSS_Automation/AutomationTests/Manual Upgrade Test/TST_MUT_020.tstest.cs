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

    public class TST_MUT_020 : BaseWebAiiTest
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
    
        [CodedStep(@"MouseHover on Home icon on left navigation bar")]
        public void TST_MUT_020_MouseHover()
        {
            /// \bug I think the test step was misunderstood, there is no need for hover
            //Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
        }
        
        [CodedStep(@"Click on Configure Home Page on navigation menu")]
        public void TST_MUT_020_ClickConfigureHomePage()
        {
           Pages.PS_HomePage.ConfigureHPDiv.Click();
           ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Enable 'My Dashboard' checkbox")]
        public void TST_MUT_020_EnableChechbox()
        {
           Pages.PS_SettingsPreferencesPage.MyDashboardCheckbox.Check(true);
           Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
           ActiveBrowser.WaitUntilReady(); 
        }
        
        [CodedStep(@"Click on Home icon on left navigation bar")]
        public void TST_MUT_020_ClickHomeIcon()
        {
           Pages.PS_HomePage.HomeLeftNavLink.Click();
           ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify that Dashboard is displayed properly on Home page")]
        public void TST_MUT_020_VerifyDashboard()
        {
            Pages.PS_HomePage.DashboardGridColumnsDiv.Wait.ForExists();
            Pages.PS_HomePage.DashboardGridExportDiv.Wait.ForExists();
            Pages.PS_HomePage.DashboardGridOptionsDiv.Wait.ForExists();
            Pages.PS_HomePage.DashboardGridPlanningDiv.Wait.ForExists();
            Pages.PS_HomePage.DashboardH2Tag.Wait.ForExists();
            Pages.PS_HomePage.DashboardLayoutSelect.Wait.ForExists();
            Pages.PS_HomePage.DashboardPortfolioSelect.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_HomePage.DashboardGridColumnsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardGridExportDiv.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardGridOptionsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardGridPlanningDiv.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardH2Tag.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardLayoutSelect.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.DashboardPortfolioSelect.IsVisible());
        }
    }
}
