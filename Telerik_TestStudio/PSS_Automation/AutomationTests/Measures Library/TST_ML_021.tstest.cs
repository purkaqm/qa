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

    public class TST_ML_021 : BaseWebAiiTest
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
        
        string measureName;
    
        [CodedStep(@"Set Quick search item")]
        public void TST_ML_021_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_021_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_021_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_021_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_021_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
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
        
        [CodedStep(@"Check 'X_measure_template02' is present")]
        public void TST_ML_021_CS07()
        {
            //Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Click();
            //System.Threading.Thread.Sleep(3000);
            Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Click(true);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            measureName = "X_measure_template02";
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
        }
        
        [CodedStep(@"Click on attach button near 'X_measure_template02'")]
        public void TST_ML_021_CS08()
        {
            HtmlInputImage addImg = ActiveBrowser.Find.ByXPath<HtmlInputImage>(string.Format(AppLocators.get("add_measures_library_input"),measureName));
            addImg.Wait.ForExists();
            addImg.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify 'X_measure_template02' measure library is appeared in Currently Attached measures table")]
        public void TST_ML_021_CS09()
        {
            ActiveBrowser.RefreshDomTree();
            if(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0)
            {
                Log.WriteLine("Measure library is attached");
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
                
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
            HtmlTableCell onSummaryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_on_summary_page_column_status"),measureName));
            Assert.AreEqual("Yes", onSummaryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Verify 'From Library' column status is empty")]
        public void TST_ML_021_CS11()
        {
            HtmlTableCell fromLibraryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_from_library_column_status"),measureName));
            Assert.AreEqual("Yes", fromLibraryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Remove 'X_measure_template02' Measure library")]
        public void TST_ML_019_CS09()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            measureNameTraingleImg.MouseClick();
            System.Threading.Thread.Sleep(2000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell removeCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get( "project_measure_library_remove"));
            removeCell.Wait.ForExists();
            removeCell.Click();
            Pages.PS_ProjectMeasuresPage.RemoveYesButton.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.RemoveYesButton.Click();
            ActiveBrowser.WaitUntilReady();
              
        }
    }
}
