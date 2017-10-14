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

    public class TST_UP_005 : BaseWebAiiTest
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
        public void TST_UP_005_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_005_CS02()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit User tab")]
        public void TST_UP_005_CS03()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
    
        [CodedStep(@"Update Personal Info")]
        public void TST_UP_005_CS04()
        {
            string lName = Data["LastName"].ToString();
            SetExtractedValue("LastName",lName);
            string oldLastName = Pages.PS_SettingsProfilePage.LastNameInput.Value;
            SetExtractedValue("OldLastName",oldLastName);
            Actions.SetText(Pages.PS_SettingsProfilePage.LastNameInput,"");
            Actions.SetText(Pages.PS_SettingsProfilePage.LastNameInput,lName);
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UP_005_CS05()
        {
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is directed to User Profile page")]
        public void TST_UP_005_CS06()
        {
            Pages.PS_UserProfilePage.UserProfileListItemTab.Wait.ForExists();
            Pages.PS_UserProfilePage.DocumentsLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"Edit User link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.UserProfileListItemTab.IsVisible(),"User Profile tab should be present");
        }
    
        [CodedStep(@"Verify changes in personal info is saved")]
        public void TST_UP_005_CS07()
        {
            string firstLast = Pages.PS_HomePage.FirstNameLastNameDiv.TextContent;
            Assert.IsTrue(firstLast.Contains(GetExtractedValue("LastName").ToString()),"Last name should be updated");
        }
    
        [CodedStep(@"Set prevoius last name in Personal Info")]
        public void TST_UP_005_CS08()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            Actions.SetText(Pages.PS_SettingsProfilePage.LastNameInput,"");
            Actions.SetText(Pages.PS_SettingsProfilePage.LastNameInput,GetExtractedValue("OldLastName").ToString());
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
