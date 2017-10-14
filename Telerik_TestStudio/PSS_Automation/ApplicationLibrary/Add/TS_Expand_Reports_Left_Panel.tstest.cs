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

    public class TS_Expand_Reports_Left_Panel : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Expand_Project_Central_Left_Panel_CodedStep()
        {
            /// \bug Is there any reason why we have this feature as separate test?
            /// Can it not be absorbed in open report wizard common test?
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("report_leftnav_plus_span")).Count > 0){
                HtmlSpan plusIcon = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("report_leftnav_plus_span"));
                plusIcon.Click();
                Pages.PS_HomePage.AddCreateReportsTab.Wait.ForExists();
            }
        }
    }
}
