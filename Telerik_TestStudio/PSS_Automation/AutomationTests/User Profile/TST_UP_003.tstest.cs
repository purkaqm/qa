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

    public class TST_UP_003 : BaseWebAiiTest
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
        public void TST_UP_003_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Verify drop down menu is displayed")]
        public void TST_UP_003_CS02()
        {
            Pages.PS_HomePage.PopupUserSettingsWindowDiv.Wait.ForExists();           
            Assert.IsTrue(Pages.PS_HomePage.PopupUserSettingsWindowDiv.IsVisible(),"Drop down menu should be displayed");
        }
    
        [CodedStep(@"Verify All link respected to Admin is present")]
        public void TST_UP_003_CS03()
        {
            Pages.PS_HomePage.SwitchUserLinkDiv.Wait.ForExists();
            Pages.PS_HomePage.ProfileDiv.Wait.ForExists();
            Pages.PS_HomePage.SettingsLinkDiv.Wait.ForExists();
            Pages.PS_HomePage.EditPreferences.Wait.ForExists();
            Pages.PS_HomePage.EditPermissionLinkDiv.Wait.ForExists();
            Pages.PS_HomePage.EditAlertLinkDiv.Wait.ForExists();
            Pages.PS_HomePage.EditPersonalLinkDiv.Wait.ForExists();
            Pages.PS_HomePage.EditHistoryDropdownLink.Wait.ForExists();
            Pages.PS_HomePage.EditFavoritesLinkDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.SwitchUserLinkDiv.IsVisible(),"Switch to user link should be present");
            Assert.IsTrue(Pages.PS_HomePage.ProfileDiv.IsVisible(),"Profile link should be present");
            Assert.IsTrue(Pages.PS_HomePage.SettingsLinkDiv.IsVisible(),"Settings link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditPreferences.IsVisible(),"Edit Prefrences link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditPermissionLinkDiv.IsVisible(),"Edit Permission link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditAlertLinkDiv.IsVisible(),"Edit Alert subscription  link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditPersonalLinkDiv.IsVisible(),"Edit personal rates link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditHistoryDropdownLink.IsVisible(),"Edit History link should be present");
            Assert.IsTrue(Pages.PS_HomePage.EditFavoritesLinkDiv.IsVisible(),"Edit Favorites link should be present");
           
        }
    

        [CodedStep(@"Click on Profile link")]
        public void TST_UP_003_CS04()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify User Profile page is displayed")]
        public void TST_UP_005_CS05()
        {
            string pageTitleName = string.Format("{0}: Profile: User Profile",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
            Pages.PS_UserProfilePage.DocumentsLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document link should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"EditUser link should be present");
        }
    
        [CodedStep(@"Click on Settings link")]
        public void TST_UP_003_CS06()
        {
            Pages.PS_HomePage.SettingsLinkDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Settings page is displayed")]
        public void TST_UP_003_CodedStep()
        {
            string pageTitleName = string.Format("{0}: Settings: Profile",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
            Assert.IsTrue(Pages.PS_SettingsProfilePage.UserNameInput.IsVisible(),"User name input should be present");           
        }
    }
}
