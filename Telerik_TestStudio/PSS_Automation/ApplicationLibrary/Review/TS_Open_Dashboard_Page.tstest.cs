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

    public class TS_Open_Dashboard_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Exists 'DashItemDiv'")]
        public void TS_Open_Dashboard_Page_CodedStep()
        {
            // Wait for Exists 'DashboardLeftNavDiv'
            Pages.PS_HomePage.ReviewDashboardTab.Wait.ForExists();
            
        }
    
        [CodedStep(@"Click 'DashItemDiv'")]
        public void TS_Open_Dashboard_Page_CodedStep1()
        {
            // Click on Dashboard
            Pages.PS_HomePage.ReviewDashboardTab.Click(false);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for 'TextContent' 'Contains' 'Dashboard' on 'DashboardDiv'")]
        public void TS_Open_Dashboard_Page_CodedStep2()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Dashboard");
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            
        }
    }
}
