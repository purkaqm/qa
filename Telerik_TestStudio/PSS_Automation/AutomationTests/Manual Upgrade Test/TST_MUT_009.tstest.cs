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

    public class TST_MUT_009 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify default layout contains icon bar, navigation bar, content area and header bar")]
        public void TST_MUT_009_DefaultLayoutIcons()
        {
            Assert.IsTrue(Pages.PS_HomePage.LeftNavigationBarDiv.IsVisible(), "Icon Bar should be visible");
            Assert.IsTrue(Pages.PS_HomePage.NavPanelMenuBarDiv.IsVisible(),"Navigation Menu should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HeaderBarDiv.IsVisible(),"Header bar should be visible");
            Assert.IsTrue(Pages.PS_HomePage.PageContentDiv.IsVisible(),"Content Area should be visible");
        }
    
        [CodedStep(@"Verify all module icons are displayed correctly in icon bar")]
        public void TST_MUT_009_LeftNavIcons()
        {
            Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.IsVisible(),"Home icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.InboxLeftNavLink.IsVisible(), "Inbox icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AddLeftNavLink.IsVisible(), "Add icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(), "Review icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ProjectLeftNavLink.IsVisible(), "Project icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(), "Admin icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.FavoritesLeftNavLink.IsVisible(), "Favorite icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HistoryLeftNavLink.IsVisible(), "History icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ImportantLinksLeftNavLink.IsVisible(), "Important icon should be visible");
                              
        }
        
        [CodedStep(@"Verify homepage header bar elements")]
        public void TST_MUT_009_HeaderIcons()
        {
            /// \bug Missing assert for Favorites, Where am I and profile dropdown
            Pages.PS_HomePage.PowerSteeringImage.Wait.ForExists();
            Pages.PS_HomePage.SearchLink.Wait.ForExists();
            Pages.PS_HomePage.HelpIconSpan.Wait.ForExists();
            Pages.PS_HomePage.EditDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.TextContent,"Home");
            Pages.PS_HomePage.LogOutLink.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible(),"SerachIcon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HelpIconSpan.IsVisible(),"HelpIcon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.IsVisible(),"");
            Assert.IsTrue(Pages.PS_HomePage.LogOutLink.IsVisible(),"Logout link should be visible");
        }
    }
}
