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

    public class TST_HIST_007 : BaseWebAiiTest
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
    
        [CodedStep(@"Click Export Link")]
        public void CSHIST00701()
        {
            Pages.PS_ManageHistoryPage.ExportLinkDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            ActiveBrowser.RefreshDomTree();
        }
        
         [CodedStep(@"Verify PDF, Excel, RTF and CSV export options are displayed ")]
        public void CSHIST00702()
        {
            Pages.PS_ManageHistoryPage.PDFExportImg.Wait.ForExists();
            Pages.PS_ManageHistoryPage.CSVExportImg.Wait.ForVisible();
            Pages.PS_ManageHistoryPage.RTFExportImg.Wait.ForVisible();
            Pages.PS_ManageHistoryPage.ExcelExportImg.Wait.ForVisible();
        }
    }
}
