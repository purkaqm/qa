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

    public class TS_Select_Portfolio : BaseWebAiiTest
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
    
        [CodedStep(@"Select 'ByValue' option '18403dg0000l12kusksg000000' on 'DashboardToolbarIdLeftPortfolioSelectorSelect'")]
        public void TS_Select_Portfolio_CodedStep()
        {
          
            Pages.PS_ReviewDashboardPage.DashboardLeftPortfolioSelectorSelect.SelectByText(GetExtractedValue("Portfolio").ToString(), true);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_ReviewDashboardPage.DashboardLeftPortfolioSelectorSelect.AssertSelect().SelectedText(ArtOfTest.Common.StringCompareType.Contains, GetExtractedValue("Portfolio").ToString());
            
        }
    
        //[CodedStep(@"Verify selection 'ByText' is 'My Projects' on 'DashboardToolbarIdLeftPortfolioSelectorSelect'")]
        //public void TS_Select_Portfolio_CodedStep1()
        //{
            
            
        //}
    }
}
