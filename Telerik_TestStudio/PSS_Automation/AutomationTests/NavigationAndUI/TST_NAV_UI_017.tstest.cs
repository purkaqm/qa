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

    public class TST_NAV_UI_017 : BaseWebAiiTest
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
        //public void TST_NAV_UI_017_CodedStep()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep()
        {
            Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardDiv.IsVisible(),"div should be present in dashboard page");
            Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardLeftPortfolioSelectorSelect.IsVisible(),"Portfolio selector list should be present in dashboard page");
            Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardLayoutSelectorSelect.IsVisible(),"Layout selector list should be present in dashboard page");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_ReviewVisualPortal.VisualPortalPropertySelectionSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewVisualPortal.VisualPortalLeftGoButtun.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_ReviewManageLayouts.ManageLayoutHelpLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewManageLayouts.ManageLayoutsAddVisualPortanButton.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep3()
        {
            Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.PortalSettingsTag.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.DefaultChartTag.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.PortalSettingNameText.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.EditVisualPortalCancelButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.EditVisualPortalSubmitButton.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewLayoutDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridFiltersDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridOptionsDisplayFilterDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridWhatCanDoDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridExportDiv.IsVisible());
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_017_CodedStep5()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep5()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Executive Review"));
            Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewPortfolioSelectSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewLayoutSelectSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewGoButton.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep6()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Financial Review"));
            Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewPortfolioSelectSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewGoButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewLayoutSelectSelect.IsVisible());
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_017_CodedStep7()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep7()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Portfolios"));
            Assert.IsTrue(Pages.PS_ReviewPortfolios.PortfoliosAddPortfolioButton.IsVisible());
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep8()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Measures Library"));
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep9()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("My Reports | Reports"));
            Assert.IsTrue(Pages.PS_MyReportsPage.AddNewReportInputButton.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep10()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Public Reports | Reports"));
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportAddNewButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportDeleteButton.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep11()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Manage Reports"));
            Assert.IsTrue(Pages.PS_ReviewManageReports.ManageLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewManageReports.ManageReportSearchButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewManageReports.ManageReportResetButton.IsVisible());
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep12()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Find a person: Active"));
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.ActiveLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.DeletedLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.LockedLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.OnlineLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.NonGroupedLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.NonRespondentsLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewFindAPerson.NoAccessUsersLink.IsVisible());
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep13()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Groups"));
            Assert.IsTrue(Pages.PS_ReviewGroupsPage.GroupsAddNewButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewGroupsPage.GroupsDeleteButton.IsVisible());
            
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep14()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Pools"));
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep15()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Certification"));
            /*if(Pages.PS_ReviewCertificationPage.FrameJspFrame.HideSearchLink.IsVisible())
            {
                Assert.IsTrue(Pages.PS_ReviewCertificationPage.FrameJspFrame.CertificateSearchLink.IsVisible());
            }*/
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep16()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Rates"));
            Assert.IsTrue(Pages.PS_ReviewResourceRatesPage.RateTablesLink.IsVisible());
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep17()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("From a File"));
            Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep18()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Import New Users"));
            Assert.IsTrue(Pages.PS_ReviewImportNewUsersPage.UploadFileLoadButton.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep19()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Load Tag From File"));
            Assert.IsTrue(Pages.PS_ReviewLoadTagsPage.TagsUploadAndContinueButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewLoadTagsPage.TagsCancelButton.IsVisible());
            
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_017_CodedStep20()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Bulk Actions"));
            Assert.IsTrue(Pages.PS_ReviewFinancialBulkActionsPage.FinancialBulkPortfolioSelect.IsVisible());
            
        }
    }
}
