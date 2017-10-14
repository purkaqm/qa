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

    public class TS_Open_Manage_Metric_Templates_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Resource Planning Menu Item in Left Navigation Panel")]
        public void TS_Open_Manage_Metric_Templates_Page_CodedStep()
        {
            // Wait for Exists 'DashboardLeftNavDiv'
            Pages.PS_ProjectSummaryPage.ProjectManageMetricsTemplatesTab.Wait.ForExists();
            
        }
    
        [CodedStep(@"Click on Resource Planning")]
        public void TS_Open_Manage_Metric_Templates_Page_CodedStep1()
        {
            // Click on Dashboard
            Pages.PS_ProjectSummaryPage.ProjectManageMetricsTemplatesTab.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Current Layout to be 'Resource planning'")]
        public void TS_Open_Manage_Metric_Templates_Page_CodedStep2()
        {
            // Wait for layout title as 'Project planning'
            //Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Financials", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(10000);
        }
    }
}
