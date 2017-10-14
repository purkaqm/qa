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

    public class TS_Open_Financial_Review_Layouts_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Executive  Review Menu Item in Left Navigation Panel")]
        public void TS_Open_Financial_Review_Layouts_Page_CodedStep()
        {
            // Wait for Exists 'DashboardLeftNavDiv'
            Pages.PS_HomePage.AdminFinancialRevTabDiv.Wait.ForExists();
            
        }
    
        [CodedStep(@"Click on Executive Review tab")]
        public void TS_Open_Financial_Review_Layouts_Page_CodedStep1()
        {
            // Click on Dashboard
            Pages.PS_HomePage.AdminFinancialRevTabDiv.Click(false);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Executive Review layout page to be displayed")]
        public void TS_Open_Financial_Review_Layouts_Page_CodedStep2()
        {
           Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Financial Review Layouts");
           
           
            
        }
    }
}
