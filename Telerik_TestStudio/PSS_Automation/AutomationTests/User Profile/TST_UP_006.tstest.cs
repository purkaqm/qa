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

    public class TST_UP_006 : BaseWebAiiTest
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
        public void TST_UP_006_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_006_CS02()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit User tab")]
        public void TST_UP_006_CS03()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Delete the email info")]
        public void TST_UP_006_CS04()
        {
            Pages.PS_SettingsProfilePage.PrimaryEmailInput.Wait.ForExists();
            Actions.SetText(Pages.PS_SettingsProfilePage.PrimaryEmailInput,"");
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UP_006_CS05()
        {
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify warning message is displayed")]
        public void TST_UP_006_CS06()
        {
            HtmlDiv warningMsg = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("user_profile_setting_warning_msg")); 
            warningMsg.Wait.ForExists();
            Assert.IsTrue(warningMsg.IsVisible(),"Warning message should be displayed");
        }
    
        [CodedStep(@"Verify Red dot is present")]
        public void TST_UP_006_CodedStep()
        {
            HtmlImage redDot = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("User_profile_red_dot_img"));
            redDot.Wait.ForExists();
            Assert.IsTrue(redDot.IsVisible(),"Red dot should be present");
        }
    }
}
