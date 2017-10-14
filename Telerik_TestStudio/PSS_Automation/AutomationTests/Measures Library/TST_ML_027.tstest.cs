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

    public class TST_ML_027 : BaseWebAiiTest
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
        public void TST_ML_027_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_027_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_027_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_027_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
                                                                    
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_027_CS04()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
    
        [CodedStep(@"Check 'X_measure_delete_test' is appeared in Currently Attached measures table")]
        public void TST_ML_027_CS05()
        {
            ActiveBrowser.RefreshDomTree();
            measureName = "X_measure_delete_test";
            Log.WriteLine(string.Format(AppLocators.get("measure_library_name_link"),measureName));
            
            if(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0)
            {
                Log.WriteLine("Template is present");
                Log.WriteLine(string.Format(AppLocators.get("measure_library_name_link"),measureName));
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
            }
            else{
                
                // Create 'X_measure_define01' measure library
                
                    //Click on define new button
                    Pages.PS_ProjectMeasuresPage.DefineNewButton.Click();
                    ActiveBrowser.WaitUntilReady();
                
                    //Enter name of library                
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.NameTextInput,measureName);
                    SetExtractedValue("MeasureName",measureName);
                
                    //Set Threashold1
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalIndTextInput,"10");
                
                    //Set Threashold2
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalInd0Text,"20");
                
                    //Click on submit button
                    Pages.PS_MeasureTemplatesNewPage.SubmitButton.Click();
                    ActiveBrowser.WaitUntilReady();
                    System.Threading.Thread.Sleep(3000);
                
                }       
                                                                        
        }
    
        [CodedStep(@"Click on the drop-down menu near 'X_measure_delete_test'")]
        public void TST_ML_027_CS06()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            measureNameTraingleImg.MouseClick();
        }
    
        [CodedStep(@"Select 'Delete' and click 'No'")]
        public void TST_ML_026_CS07()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell deleteDiv = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("project_measure_library_delete"));
            deleteDiv.Wait.ForExists();
            deleteDiv.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectMeasuresPage.DeleteNoButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify measure is not deleted and measure page is invoked")]
        public void TST_ML_026_CS08()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.TitleLink.IsVisible(),"Title link should be displayed");
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.AttachNewButton.IsVisible(),"Attach new button should be displayed");
        }
        
        [CodedStep(@"Select 'Delete' and click 'Yes'")]
        public void TST_ML_026_CS09()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell deleteDiv = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("project_measure_library_delete"));
            deleteDiv.Wait.ForExists();
            deleteDiv.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectMeasuresPage.DeleteYesButton.Click();  
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify measure is deleted and measure page is invoked")]
        public void TST_ML_026_CS10()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsFalse(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.TitleLink.IsVisible(),"Title link should be displayed");
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.AttachNewButton.IsVisible(),"Attach new button should be displayed");
        }
    }
}
