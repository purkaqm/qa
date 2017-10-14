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

    public class TST_MAT_105_C992446 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Review left navigation tab is present")]
        public void TST_MAT_092_C992807_VerifyReviewTab()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Review left navigation link")]
        public void TST_MAT_092_C992807_ClickReviewLink()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Add New button")]
        public void TST_MAT_092_C992807_ClickAddNewButton()
        {
            Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.Click(true);
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify 'Metric Template' page is opened")]
        public void TST_MAT_092_C992807_VerifyMetricTemplatePage()
        {
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NameTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.SubmitButton.IsVisible());
        }
    
        [CodedStep(@"Verify Measure library template  is created")]
        public void TST_MAT_092_C992807_VerifyTemplateCreated()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName"))).Count>0);
                            
        }
    
        [CodedStep(@"Verify Measures Library page is opened")]
        public void TST_MAT_092_C992807_VerifyMeasureLibraryPage()
        {
            Assert.IsTrue( Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Measures Library"));
        }
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_094_C992812_WaitProjectSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                                                                       
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_MAT_094_C992812_ClickManageMeasure()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Attach New' button")]
        public void TST_ML_021_CS05()
        {
            Pages.PS_ProjectMeasuresPage.AttachNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify required elemnts are displayed")]
        public void TST_ML_021_CS06()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.OwnerColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.LastEditedColLink.Wait.ForExists();
            //Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.TitleColumnLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.OwnerColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.LastEditedColLink.IsVisible());
            //Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.IsVisible());
        }
    
        [CodedStep(@"Check created measure template is present")]
        public void TST_ML_021_CS07()
        {
            //Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Click();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Click(true);
            //System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName").ToString())).Count > 0);
        }
    
        [CodedStep(@"Click on attach button near created measure template")]
        public void TST_ML_021_CS08()
        {
            HtmlInputImage addImg = ActiveBrowser.Find.ByXPath<HtmlInputImage>(string.Format(AppLocators.get("add_measures_library_input"),GetExtractedValue("MeasureName").ToString()));
            addImg.Wait.ForExists();
            addImg.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify created measure library is appeared in Currently Attached measures table")]
        public void TST_ML_021_CS09()
        {
            ActiveBrowser.RefreshDomTree();
            if(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName").ToString())).Count>0)
            {
                Log.WriteLine("Measure library is attached");
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName").ToString())).Count>0);
                
            }
            else
            {
                Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
                ActiveBrowser.WaitUntilReady();
                System.Threading.Thread.Sleep(3000);
            }
        }
    
        [CodedStep(@"Verify 'On Summary page' column status is Yes")]
        public void TST_ML_021_CS10()
        {
            HtmlTableCell onSummaryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_on_summary_page_column_status"),GetExtractedValue("MeasureName").ToString()));
            Assert.AreEqual("Yes", onSummaryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Verify 'From Library' column status is empty")]
        public void TST_ML_021_CS11()
        {
            HtmlTableCell fromLibraryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_from_library_column_status"),GetExtractedValue("MeasureName").ToString()));
            Assert.AreEqual("Yes", fromLibraryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Store Url of current page")]
        public void TST_MAT_105_C992446_CodedStep()
        {
            SetExtractedValue("ProjectUrl",ActiveBrowser.Url);
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_RW_004_CS42()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectUrl").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Delete created Measure library")]
        public void TST_ML_019_CS09()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),GetExtractedValue("MeasureName").ToString()));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),GetExtractedValue("MeasureName").ToString()));
            measureNameTraingleImg.MouseClick();
            System.Threading.Thread.Sleep(2000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell deleteCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("project_measure_library_delete"));
            deleteCell.Wait.ForExists();
            deleteCell.Click();
            Pages.PS_ReviewMeasureLibrary.DeletePopupDeleteAllRadio.Click();
            Pages.PS_ReviewMeasureLibrary.DeletePopupCommitButton.Click();  
            ActiveBrowser.WaitUntilReady();
                          
        }
    
        [CodedStep(@"Verify attached measure is deleted from project")]
        public void TST_MAT_105_C992446_CodedStep1()
        {
            Assert.IsFalse(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName").ToString())).Count>0);
        }
    }
}
