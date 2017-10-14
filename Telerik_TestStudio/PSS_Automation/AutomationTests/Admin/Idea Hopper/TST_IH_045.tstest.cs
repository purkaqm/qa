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

    public class TST_IH_045 : BaseWebAiiTest
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
    
        [CodedStep(@"Set user for login")]
        public void TST_IH_045_CS01()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_045_CS02()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();
            Pages.PS_HomePage.EditPreferences.Wait.ForExists();
                        
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_045_CS03()
        {
            Pages.PS_HomePage.EditPreferences.Click();     
        }
    
        [CodedStep(@"Verify Preference page is open")]
        public void TST_IH_045_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Settings: Preferences"));
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.HighContViewCheckBox.IsVisible(),"'High Contrast view' checkbok should be present");
                        
        }
    }
}
