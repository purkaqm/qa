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

    public class TST_FAV_003 : BaseWebAiiTest
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
        public void TST_Verify_Default_Pinned_Pages_CodedStep()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            
            string pinnedPage1MenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Configure Home Page");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(pinnedPage1MenuLocator).Count>0, "Configure Home Page should be listed in Pinned Pages Menu");
            string pinnedPage2MenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Create a Project");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(pinnedPage2MenuLocator).Count>0, "Create a Project page should be listed in Pinned Pages Menu");
            string pinnedPage3MenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Submit an Idea");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(pinnedPage3MenuLocator).Count>0, "Submit an Idea page should be listed in Pinned Pages Menu");
            string pinnedPage4MenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Run a Report");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(pinnedPage4MenuLocator).Count>0, "Run a Report page should be listed in Pinned Pages Menu");
            Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Home"),"Home icon on hover tool tip text should be 'Home'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Default_Pinned_Pages_CodedStep1()
        {
            HtmlAnchor configureHomeLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Configure Home Page"));
            configureHomeLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_SettingsPreferencesPage.PreferencesContainer.Wait.ForVisible();
            string pageTitle = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(pageTitle.Contains("Settings") && pageTitle.Contains("Preferences"),"User should be navigated to Settings : Preferences page");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Default_Pinned_Pages_CodedStep2()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            
            HtmlAnchor createProjectLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Create a Project"));
            createProjectLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add: Project");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Default_Pinned_Pages_CodedStep3()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            
            HtmlAnchor submitIdeaLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Submit an Idea"));
            Log.WriteLine(string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Submit an Idea"));
            submitIdeaLink.MouseHover();
            submitIdeaLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_SubmitAnIdeaPage.SubmitIdeaInputButton.Wait.ForExists();
            Pages.PS_SubmitAnIdeaPage.SubmitIdeaInputButton.Wait.ForVisible();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Submit an Idea");
            Pages.PS_SubmitAnIdeaPage.SubmitIdeaInputButton.Wait.ForExists();
            Pages.PS_SubmitAnIdeaPage.SubmitIdeaInputButton.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_SubmitAnIdeaPage.SubmitIdeaInputButton.IsVisible(),"User should be navigated to Submit an Idea page");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Default_Pinned_Pages_CodedStep4()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            //Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            
            HtmlAnchor runReportLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("left_nav_pinned_menu_item"),"Run a Report"));
            runReportLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MyReportsPage.AddNewReportInputButton.Wait.ForExists();
            //Pages.PS_MyReportsPage.AddNewReportInputButton.Wait.ForVisible();
            string pageTitle = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(pageTitle.Contains("My Reports | Reports"),"User should be navigated to My Reports | Reports page");         
                      
            
        }
    }
}
