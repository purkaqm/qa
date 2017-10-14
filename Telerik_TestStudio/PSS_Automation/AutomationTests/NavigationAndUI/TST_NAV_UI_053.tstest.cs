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

    public class TST_NAV_UI_053 : BaseWebAiiTest
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
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_053_CodedStep()
        //{
            //Pages.PS_HomePage.ProjectLeftNavLink.Click();
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_053_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectSummarySaveBtn.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectSummaryCancelButton.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_053_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.InnerText.Contains("PROJECT"),"In project menu tittle should be PROJECT");
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectIssuesTabDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectDiscussionsTabDiv.IsVisible());
            
        }
    }
}
