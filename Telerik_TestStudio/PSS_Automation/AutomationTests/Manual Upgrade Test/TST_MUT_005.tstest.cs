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

    public class TST_MUT_005 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MUT_005_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
                                                                                                                                       
        }
    
        [CodedStep(@"Delete Gated project")]
        public void TST_MUT_005_DeleteProject()
        {
            Pages.PS_ProjectSummaryPage.DeleteTabDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.DeleteTabDiv.Click();
            ActiveBrowser.WaitUntilReady();
            
            Pages.PS_ProjectSummaryPage.DeletePopupDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.DeletePopupDeleteBtn.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.DeletePopupDeleteBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ProjectDeletePage.DeleteButton.Wait.ForExists();
            Pages.PS_ProjectDeletePage.DeleteButton.Click();
            Pages.PS_DeletionResultPage.DeleteConfirmationBoxDiv.Wait.ForExists();
        }
    }
}
