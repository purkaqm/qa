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

    public class TST_ML_024 : BaseWebAiiTest
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
        public void TST_ML_024_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_024_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_024_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_024_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
                                
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_024_CS04()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
    
        [CodedStep(@"Check 'X_measure_define01' is appeared in Currently Attached measures table")]
        public void TST_ML_024_CS05()
        {
            ActiveBrowser.RefreshDomTree();
            measureName = "X_measure_define01";
            Log.WriteLine(string.Format(AppLocators.get("measure_library_name_link"),measureName));
            //Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
            
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
    
        [CodedStep(@"Click on the drop-down menu near 'X_measure_define01'")]
        public void TST_ML_024_CS06()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            measureNameTraingleImg.MouseClick();
        }
    
        [CodedStep(@"Select 'View properties'")]
        public void TST_ML_024_CS07()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlDiv viewMeasureDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("view_properties_tab_div"));
            viewMeasureDiv.Wait.ForExists();
            viewMeasureDiv.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify page title is displayed correct")]
        public void TST_ML_024_CS08()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Measure Templates: " + measureName + ": Properties | " + CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify page is displayed with read-only property")]
        public void TST_ML_024_CS09()
        {
            HtmlTableRow nameRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format("//tr[contains(.,'{0}')]",measureName));
            HtmlTableRow collectnRow= ActiveBrowser.Find.ByXPath<HtmlTableRow>("//tr[contains(.,'Collection Method:')]");
            HtmlTableRow indicatorRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>("//tr[contains(.,'Indicator Type:')]");
            nameRow.Wait.ForExists();
            collectnRow.Wait.ForExists();
            indicatorRow.Wait.ForExists();
            Assert.IsFalse(nameRow.Find.AllByTagName("input").Count>0);
            Assert.IsFalse(nameRow.Find.AllByTagName("select").Count>0);
            Assert.IsFalse(collectnRow.Find.AllByTagName("input").Count>0);
            Assert.IsFalse(collectnRow.Find.AllByTagName("select").Count>0);
            Assert.IsFalse(indicatorRow.Find.AllByTagName("input").Count>0);
            Assert.IsFalse(indicatorRow.Find.AllByTagName("select").Count>0);
        }
        
        [CodedStep(@"Click on Exit button")]
        public void TST_ML_024_CS10()
        {
            Pages.PS_MeasureTemplatesPropertiesPage.MeasurePropertiesPageExitButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify project measure page is invoked")]
        public void TST_ML_024_CS11()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.TitleLink.IsVisible(),"Title link should be displayed");
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.AttachNewButton.IsVisible(),"Attach new button should be displayed");
        }
    }
}
