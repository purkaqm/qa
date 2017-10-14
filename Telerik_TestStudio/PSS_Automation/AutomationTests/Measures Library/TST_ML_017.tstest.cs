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

    public class TST_ML_017 : BaseWebAiiTest
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
        public void TST_ML_017_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_017_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_017_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
        
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_017_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify page title is displayed correct")]
        public void TST_ML_017_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
        
        [CodedStep(@"Verify 'Currently Attached Measures' table is displayed with required columns and buttons")]
        public void TST_ML_017_CS05()
        {
            Pages.PS_ProjectMeasuresPage.TitleLink.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.DescriptionColumnTableCell.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.OnSummaryPageLink.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.FromLibraryLink.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.AttachNewButton.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.DefineNewButton.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.TitleLink.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.DescriptionColumnTableCell.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.OnSummaryPageLink.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.FromLibraryLink.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.AttachNewButton.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.DefineNewButton.IsVisible());
            
        }
    }
}
