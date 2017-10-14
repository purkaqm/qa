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

    public class TST_UP_008 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on User Name link")]
        public void TST_UP_008_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_008_CS02()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit User tab")]
        public void TST_UP_008_CS03()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Profile tab is correct")]
        public void TST_UP_008_CS04()
        {
            string pageTitleName = string.Format("{0}: Settings: Profile",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
        
        [CodedStep(@"Click on Preference link")]
        public void TST_UP_008_CS05()
        {
            Pages.PS_SettingsProfilePage.PreferencesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Preference tab is correct")]
        public void TST_UP_008_CS06()
        {
            string pageTitleName = string.Format("{0}: Settings: Preferences",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    
        [CodedStep(@"Click on Permissions link")]
        public void TST_UP_008_CS07()
        {
            Pages.PS_SettingsProfilePage.PermissionsLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Permissions tab is correct")]
        public void TST_UP_008_CS08()
        {
            string pageTitleName = string.Format("{0}: Settings: Permissions",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    
        [CodedStep(@"Click on Alert Subcription link")]
        public void TST_UP_008_CS09()
        {
            Pages.PS_SettingsProfilePage.AlertLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Permissions tab is correct")]
        public void TST_UP_008_CS10()
        {
            string pageTitleName = string.Format("{0}: Settings: Alert Subscriptions",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    
        [CodedStep(@"Click on Personal Rates link")]
        public void TST_UP_008_CS11()
        {
            Pages.PS_SettingsProfilePage.PersonalRatesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Personal Rates tab is correct")]
        public void TST_UP_008_CS12()
        {
            string pageTitleName = string.Format("{0}: Settings: Personal Rates",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    
        [CodedStep(@"Click on History link")]
        public void TST_UP_008_CS13()
        {
            Pages.PS_SettingsProfilePage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for History tab is correct")]
        public void TST_UP_008_CS14()
        {
            string pageTitleName = string.Format("{0}: Settings: History",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    
        [CodedStep(@"Click on Favorites link")]
        public void TST_UP_008_CS15()
        {
            Pages.PS_SettingsProfilePage.FavoritesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Page title for Favorites tab is correct")]
        public void TST_UP_008_CS16()
        {
            string pageTitleName = string.Format("{0}: Settings: Favorites",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
        }
    }
}
