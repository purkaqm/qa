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

    public class TST_NAV_UI_019 : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_019_CodedStep()
        {
            
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.ConfigureHPDiv.MouseHover();
            string eleColor = Pages.PS_HomePage.ConfigureHPDiv.GetComputedStyleValue("color");
            
            
            Pages.PS_HomePage.InboxLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.InboxAlertsDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.InboxAlertsDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.InboxStatusReportLeftNavDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.AddLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.AddProjectTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AddProjectTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AddIdeaTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AddIdeaTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AddUserTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AddUserTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AddOrganizationTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AddOrganizationTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AddOtherWorkTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AddOtherWorkTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.ReviewLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.ReviewDashboardTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ReviewDashboardTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.ReviewVisualPortalTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ReviewVisualPortalTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.ReviewManageLayoutTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ReviewManageLayoutTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.ReviewAddVisualPortalTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ReviewAddVisualPortalTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.AdminLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.AdminTemplatesTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AdminTemplatesTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AdminLayoutsTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AdminLayoutsTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AdminPermissionsTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AdminPermissionsTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AdminLocalizationTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AdminLocalizationTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.AdminConfigurationtab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.AdminConfigurationtab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            Pages.PS_HomePage.ProjectWorkTreeTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ProjectWorkTreeTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.ProjectProjectSummaryTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ProjectProjectSummaryTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.ProjectEditDetailsTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ProjectEditDetailsTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.FavoritesLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.ManageFavoritesLeftNavDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ManageFavoritesLeftNavDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            Pages.PS_HomePage.FavoritesAddToFavoritesTab.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.FavoritesAddToFavoritesTab.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.HistoryLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.ManageHistoryLeftNavDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ManageHistoryLeftNavDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
            
            Pages.PS_HomePage.ImportantLinksLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.ManageImpLinksLeftNavDiv.MouseHover();
            Assert.IsTrue(Pages.PS_HomePage.ManageImpLinksLeftNavDiv.GetComputedStyleValue("color").Equals(eleColor),"element background color should be white");
 
        }
    }
}
