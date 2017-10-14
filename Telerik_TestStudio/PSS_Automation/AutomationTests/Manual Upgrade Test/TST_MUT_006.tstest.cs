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

    public class TST_MUT_006 : BaseWebAiiTest
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
        public void TST_MUT_006_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
                                                                                                                                       
        }
    
        [CodedStep(@"Verify project has been successfully deleted")]
        public void TST_MUT_006_VerifyProjectDeleted()
        {
            string removeMsg = GetExtractedValue("CreatedProjectName").ToString() + " has been successfully deleted";
            Log.WriteLine(removeMsg);
            Log.WriteLine(Pages.PS_DeletionResultPage.DeleteConfirmationBoxDiv.BaseElement.InnerText);
            Assert.IsTrue(Pages.PS_DeletionResultPage.DeleteConfirmationBoxDiv.IsVisible());
            Assert.IsTrue(Pages.PS_DeletionResultPage.DeleteConfirmationBoxDiv.BaseElement.InnerText.Contains(removeMsg));
                        
        }
    }
}
