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

    public class TST_NAV_UI_031 : BaseWebAiiTest
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
        public void TST_NAV_UI_031_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.LeftNavigationBarDiv.IsVisible(), "Icon Bar should be visible");
            Assert.IsTrue(Pages.PS_HomePage.NavPanelMenuBarDiv.IsVisible(),"Navigation Menu should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HeaderBarDiv.IsVisible(),"Header bar should be visible");
            Assert.IsTrue(Pages.PS_HomePage.PageContentDiv.IsVisible(),"Content Area should be visible");
        }
    
        [CodedStep(@"Verify Favorites, History and Important Links icons are displayed correctly in icon bar")]
        public void TST_NAV_UI_031_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_HomePage.FavoritesLeftNavLink.IsVisible(), "Favorites icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HistoryLeftNavLink.IsVisible(), "History icon should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ImportantLinksLeftNavLink.IsVisible(), "Important Links icon should be visible");
            
            
        }
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Home'")]
        //public void TST_NAV_UI_031_CodedStep2()
        //{
            //Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            //Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Home"),"Home icon on hover tool tip text should be 'Home'");
            
        //}
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Inbox'")]
        //public void TST_NAV_UI_031_CodedStep3()
        //{
            //Pages.PS_HomePage.InboxLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"INBOX");
            //Assert.IsTrue(Pages.PS_HomePage.InboxLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Inbox"),"Inbox icon on hover tool tip text should be 'Inbox'");
        //}
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Add'")]
        //public void TST_NAV_UI_031_CodedStep4()
        //{
            //Pages.PS_HomePage.AddLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADD");
            //Assert.IsTrue(Pages.PS_HomePage.AddLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Add"),"Add icon on hover tool tip text should be 'Add'");
            
        //}
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Review'")]
        //public void TST_NAV_UI_031_CodedStep5()
        //{
            
            //Pages.PS_HomePage.ReviewLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"REVIEW");
            //Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Review"),"Review icon on hover tool tip text should be 'Review'");
            
        //}
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Admin'")]
        //public void TST_NAV_UI_031_CodedStep6()
        //{
            
            //Pages.PS_HomePage.AdminLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADMIN");
            //Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Admin"),"Admin icon on hover tool tip text should be 'Admin'");
            
            
        //}
    
        //[CodedStep(@"Verify tooltip text and menu title for 'Project'")]
        //public void TST_NAV_UI_031_CodedStep7()
        //{
            //Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"PROJECT");
            //Assert.IsTrue(Pages.PS_HomePage.ProjectLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Project"),"Project icon on hover tool tip text should be 'Project'");
            
        //}
    
        [CodedStep(@"Verify tooltip text and menu title for 'Favorites'")]
        public void TST_NAV_UI_031_CodedStep8()
        {
            
            Pages.PS_HomePage.FavoritesLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            Assert.IsTrue(Pages.PS_HomePage.FavoritesLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Favorites"),"Favorites icon on hover tool tip text should be 'Favorites'");
            
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'History'")]
        public void TST_NAV_UI_031_CodedStep9()
        {
            
            Pages.PS_HomePage.HistoryLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HISTORY");
            Assert.IsTrue(Pages.PS_HomePage.HistoryLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("History"),"History icon on hover tool tip text should be 'History'");
            
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Important Links'")]
        public void TST_NAV_UI_031_CodedStep10()
        {
            
            Pages.PS_HomePage.ImportantLinksLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"IMPORTANT LINKS");
            Assert.IsTrue(Pages.PS_HomePage.ImportantLinksLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Important Links"),"Important Links icon on hover tool tip text should be 'Important Links'");
            
        }
    
        [CodedStep(@"Verify Left Panel Title is 'FAVORITES'")]
        public void TST_NAV_UI_031_CodedStep2()
        {
            // Verify 'TextContent' 'Contains' 'REVIEW' on 'NavPanelTitleDiv'
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            
        }
    
        [CodedStep(@"Verify History Panel Title is 'HISTORY'")]
        public void TST_NAV_UI_031_CodedStep3()
        {
            // Verify 'TextContent' 'Contains' 'REVIEW' on 'NavPanelTitleDiv'
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HISTORY");
            
        }
    
        [CodedStep(@"Verify Left Panel Title is 'IMPORTANT LINK'")]
        public void TST_NAV_UI_031_CodedStep4()
        {
            // Verify 'TextContent' 'Contains' 'REVIEW' on 'NavPanelTitleDiv'
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"IMPORTANT LINKS");
            
        }
    }
}
