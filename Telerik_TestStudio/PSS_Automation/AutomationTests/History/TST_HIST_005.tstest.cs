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

    public class TST_HIST_005 : BaseWebAiiTest
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
    
        [CodedStep(@"Search 'Dashboard' in history")]
        public void CSHIST00501()
        {
            Actions.SetText(Pages.PS_ManageHistoryPage.SearchInputBox.BaseElement, "Dashboard");
            Pages.PS_ManageHistoryPage.GoButton.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ManageHistoryPage.HistorySearchFormDiv.Wait.ForVisible();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
        
        [CodedStep(@"Verify all serach results contain the word 'Dashboard'")]
        public void CSHIST00502()
        {
           IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
           IList<Element> descElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_desc_records"));
            for(int i=0; i<nameElements.Count;i++){
                Assert.IsTrue(nameElements[i].InnerText.Contains("Dashboard") || descElements[i].InnerText.Contains("Dashboard"), "user search results should be displayed in history table");
            }
        }
    }
}
