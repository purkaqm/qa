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

    public class TST_MAT_050_C992487 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Home left navigation link")]
        public void TST_MAT_050_C992487_Click_Home_Icon()
        {
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
        
        [CodedStep(@"Select Dashboard from Home Show list")]
        public void TST_MAT_001_C992487_Select_Dashboard()
        {
            Pages.PS_SettingsPreferencesPage.MyDashboardCheckbox.Check(true);
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
        }
        
        [CodedStep(@"Verify that Dashboard is displayed on Home Page")]
        public void TST_MAT_001_C992487_Verify_Dashboard_On_Home_Page()
        {
            Pages.PS_HomePage.DashboardH2Tag.AssertContent().TextContent(ArtOfTest.Common.StringCompareType.Contains, "Dashboard");
            Pages.PS_HomePage.DashboardH2Tag.Wait.ForExists(30000);
            Assert.AreEqual(true, Pages.PS_HomePage.DashboardH2Tag.IsVisible());
        }
    }
}
