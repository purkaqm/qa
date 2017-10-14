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

    public class TS_Open_Project_Planning_Page : BaseWebAiiTest
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
    
        //[CodedStep(@"Wait for Exists 'DashItemDiv'")]
        //public void TS_Open_Dashboard_Page_CodedStep()
        //{
            //// Wait for Exists 'DashboardLeftNavDiv'
            //Pages.PS_ProjectSummaryPage.ProjectPlanningLeftNavDiv.Wait.ForExists();
            
        //}
    
        //[CodedStep(@"Click 'DashItemDiv'")]
        //public void TS_Open_Dashboard_Page_CodedStep1()
        //{
            //// Click on Dashboard
            //Pages.PS_ProjectSummaryPage.ResourcePlanningTab.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Wait for 'TextContent' 'Contains' 'Dashboard' on 'DashboardDiv'")]
        //public void TS_Open_Dashboard_Page_CodedStep2()
        //{
            //// Wait for layout title as 'Project planning'
            //Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Project Central", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            //this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            //Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.Wait.ForVisible();
            //Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Resource planning", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            
        //}
    
        //[CodedStep(@"Wait for Project Planning Menu Item in Left Navigation Panel")]
        //public void TS_Open_Dashboard_Page_CodedStep()
        //{
            //// Wait for Exists 'DashboardLeftNavDiv'
            //Pages.PS_ProjectSummaryPage.ResourcePlanningTab.Wait.ForExists();
            
        //}
    
        [CodedStep(@"Wait for Project Planning Menu Item in Left Navigation Panel")]
        public void TS_Open_Project_Planning_Page_CodedStep()
        {
            // Wait for Exists 'DashboardLeftNavDiv'
            Pages.PS_ProjectSummaryPage.ProjectPlanningLeftNavDiv.Wait.ForExists();
            
        }
    
        [CodedStep(@"Click on Project Planning")]
        public void TS_Open_Project_Planning_Page_CodedStep1()
        {
            // Click on Dashboard
            Pages.PS_ProjectSummaryPage.ProjectPlanningTab.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Current Layout to be 'Project planning'")]
        public void TS_Open_Project_Planning_Page_CodedStep2()
        {
            // Wait for layout title as 'Project planning'
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Project Central", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.Wait.ForVisible();
            Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Project planning", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            
        }
    }
}
