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

    public class TST_ML_020 : BaseWebAiiTest
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
    
        [CodedStep(@"Set Quick search item")]
        public void TST_ML_020_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_020_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_020_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_020_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_020_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
        
        [CodedStep(@"Click on 'Attach New' button")]
        public void TST_ML_020_CS05()
        {
            Pages.PS_ProjectMeasuresPage.AttachNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify required elemnts are displayed")]
        public void TST_ML_020_CS06()
        {
            Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.OwnerColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.LastEditedColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.TitleColumnLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.OwnerColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.LastEditedColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.IsVisible());
        }
    }
}
