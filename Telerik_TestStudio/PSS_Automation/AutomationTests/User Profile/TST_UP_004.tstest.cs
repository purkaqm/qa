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

    public class TST_UP_004 : BaseWebAiiTest
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
        public void TST_UP_004_CS01()
        {
             Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Verify drop down menu is displayed")]
        public void TST_UP_004_CS02()
        {
            Pages.PS_HomePage.PopupUserSettingsWindowDiv.Wait.ForExists();           
            Assert.IsTrue(Pages.PS_HomePage.PopupUserSettingsWindowDiv.IsVisible(),"Drop down menu should be displayed");
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_003_CS03()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify User Profile page is displayed correctly")]
        public void TST_UP_004_CodedStep()
        {
            Pages.PS_UserProfilePage.UserProfileListItemTab.Wait.ForExists();
            Pages.PS_UserProfilePage.DocumentsLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"Edit User link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.UserProfileListItemTab.IsVisible(),"User Profile tab should be present"); 
            
        }
    }
}
