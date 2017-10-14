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

    public class TST_MUT_010 : BaseWebAiiTest
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

        /// \bug The list and the highlight should be checked.
        /// Can we simply do away with checking list and not every item in list?
        /// As list changes depending on user and the context (UIX).
    
        [CodedStep(@"Verify all module icons are displayed correctly in icon bar")]
        public void TST_MUT_010_LeftNavMenuIcons()
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
    
        [CodedStep(@"Verify tooltip text and menu title for 'Home' and list items is displayed")]
        public void TST_MUT_010_HomeIcon()
        {
            /// \bug Does things change on mouse hover and logic actually depends on it?
            Pages.PS_HomePage.HomeLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            Assert.IsTrue(Pages.PS_HomePage.ConfigureHPDiv.IsVisible(),"Configure home page link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Home"),"Home icon on hover tool tip text should be 'Home'");
                        
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Inbox' and list items is displayed ")]
        public void TST_MUT_010_InboxIcon()
        {
            Pages.PS_HomePage.InboxLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"INBOX");
            Assert.IsTrue(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.IsVisible(),"Question/Notification link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.InboxAlertsDiv.IsVisible(),"Alert link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.IsVisible(),"Status report due link  should be visible");
            Assert.IsTrue(Pages.PS_HomePage.InboxLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Inbox"),"Inbox icon on hover tool tip text should be 'Inbox'");
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Add' and list items is displayed")]
        public void TST_MUT_010_AddIcon()
        {
            Pages.PS_HomePage.AddLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADD");
            Assert.IsTrue(Pages.PS_HomePage.AddProjectTab.IsVisible(),"Project link should be visible");
            /// \bug Idea is a replaceable term, currently fails unless 'Idea Hopper'
            //Assert.IsTrue(Pages.PS_HomePage.AddIdeaDiv.IsVisible(),"Idea link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AddUserTab.IsVisible(),"User link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AddOrganizationTab.IsVisible(),"Organization link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AddOtherWorkTab.IsVisible(),"Other work link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.AddLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Add"),"Add icon on hover tool tip text should be 'Add'");
            
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Review' and list items is displayed")]
        public void TST_MUT_010_ReviewIcon()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"REVIEW");
            Assert.IsTrue(Pages.PS_HomePage.ReviewDashboardTab.IsVisible(),"Dashboard link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewVisualPortalTab.IsVisible(),"Visual portal link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewManageLayoutTab.IsVisible(),"Manage layout link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewAddVisualPortalTab.IsVisible(),"Add visual portal link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewExecutiveReviewTab.IsVisible(),"Executive review link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewFinancialReviewTab.IsVisible(),"Financial review link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewPortfoliosTab.IsVisible(),"Portfolio link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewMeasureLibraryTab.IsVisible(),"Measure library link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Review"),"Review icon on hover tool tip text should be 'Review'");
            
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Admin' and list items is displayed")]
        public void TST_MUT_010_AdminIcon()
        {
            Pages.PS_HomePage.AdminLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADMIN");
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Admin"),"Admin icon on hover tool tip text should be 'Admin'");
            
                        
        }
    
        [CodedStep(@"Verify tooltip text and menu title for 'Project' and list items is displayed")]
        public void TST_MUT_010_ProjectIcon()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"PROJECT");
            Assert.IsTrue(Pages.PS_HomePage.ProjectWorkTreeTab.IsVisible(),"Work tree link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ProjectLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Project"),"Project icon on hover tool tip text should be 'Project'");
            
        }
        
        [CodedStep(@"Verify tooltip text and menu title for 'Favorites' and list items is displayed")]
        public void TST_MUT_010_FavoritesIcon()
        {
            Pages.PS_HomePage.FavoritesLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            Assert.IsTrue(Pages.PS_HomePage.ManageFavoritesLeftNavDiv.IsVisible(),"Manage favorite link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.FavoritesAddToFavoritesTab.IsVisible(),"Add to favorite link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.FavoritesLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Favorites"),"Favorites icon on hover tool tip text should be 'Favorites'");
            
        }
        
        [CodedStep(@"Verify tooltip text and menu title for 'History' and list items is displayed")]
        public void TST_MUT_010_HistoryIcon()
        {
            Pages.PS_HomePage.HistoryLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HISTORY");
            Assert.IsTrue(Pages.PS_HomePage.ManageHistoryLeftNavDiv.IsVisible(),"Manage history link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.HistoryLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("History"),"History icon on hover tool tip text should be 'History'");
            
        }
        
        [CodedStep(@"Verify tooltip text and menu title for 'Important Links' and list items is displayed")]
        public void TST_MUT_010_ImportantLinksIcon()
        {
            Pages.PS_HomePage.ImportantLinksLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"IMPORTANT LINKS");
            Assert.IsTrue(Pages.PS_HomePage.ManageImpLinksLeftNavDiv.IsVisible(),"Manage important link should be visible");
            Assert.IsTrue(Pages.PS_HomePage.ImportantLinksLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Important Links"),"Important Links icon on hover tool tip text should be 'Important Links'");
            
        }
    }
}
