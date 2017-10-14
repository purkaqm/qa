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

    public class TST_SRT_002 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify all elements of status report templates page")]
        public void CS_SRT_002_01()
        {
            Pages.PS_StatusReportTemplatesPage.NameColumnHeader.Wait.ForExists();
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.NameColumnHeader.IsVisible(), "A table with Name column should be present");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.DescriptionColumnHeader.IsVisible(), "A table with Description column should be present");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AssociatedColumnHeader.IsVisible(), "A table with Associated With column should be present");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.ActiveColumnHeader.IsVisible(), "A table with Active column should be present");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewBtn.IsVisible(), "Add new button should be present on page");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("status_report_templ_records")).Count >= 2, "Atleast one default template should be present");
        }
    }
}
