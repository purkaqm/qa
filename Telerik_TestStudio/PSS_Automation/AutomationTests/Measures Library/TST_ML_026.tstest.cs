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

    public class TST_ML_026 : BaseWebAiiTest
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
        
        string measureName, copyMeasureName, text;
    
        [CodedStep(@"Set Quick search item")]
        public void TST_ML_026_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_026_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_026_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_026_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
                                                        
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_026_CS04()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
    
        [CodedStep(@"Check 'X_measure_define01' is appeared in Currently Attached measures table")]
        public void TST_ML_026_CS05()
        {
            ActiveBrowser.RefreshDomTree();
            measureName = "X_measure_define01";
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
    
        [CodedStep(@"Click on the drop-down menu near 'X_measure_define01'")]
        public void TST_ML_026_CS06()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            measureNameTraingleImg.MouseClick();
        }
    
        [CodedStep(@"Select 'Copy'")]
        public void TST_ML_026_CS07()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlDiv copyDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("project_measure_library_copy"));
            copyDiv.Wait.ForExists();
            copyDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify page title")]
        public void TST_ML_026_CS08()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Copy Measure | " + CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify Measure page is displayed with all measure fields pre-populated")]
        public void TST_ML_026_CS09()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CancelSubmit.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CurrencyConversionNoRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CurrencyConversionYesRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.DescriptionTextArea.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.EffectiveDatesSelect.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.FormulaRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.GoalRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.GreenMsgTextInput.IsVisible());

            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.ManualRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NameTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NoneRadio.IsVisible());

            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.RedMessageTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.SubmitButton.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.TargetDateText.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.TargetStartDateText.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.TargetValueText.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalInd0Text.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalIndTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.UnitsOfMeasuresTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.VarianceRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.YellowMsgTextInput.IsVisible());
        }
        
        [CodedStep(@"Click on cancel button")]
        public void TST_ML_026_CS10()
        {
            Pages.PS_MeasureTemplatesNewPage.CancelSubmit.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify project measure page is invoked")]
        public void TST_ML_026_CS11()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.TitleLink.IsVisible(),"Title link should be displayed");
            Assert.IsTrue(Pages.PS_ProjectMeasuresPage.AttachNewButton.IsVisible(),"Attach new button should be displayed");
        }
    
        [CodedStep(@"Edit a field on 'Copy measure' page and submit the changes")]
        public void TST_ML_025_CS12()
        {
            text = "TestCopy" + Randomizers.generateRandomInt(1111,9999);
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.DescriptionTextArea,text);  
            Pages.PS_MeasureTemplatesNewPage.SubmitButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Copied measure is saved")]
        public void TST_ML_026_CS13()
        {
            ActiveBrowser.RefreshDomTree();
            copyMeasureName = measureName + "-Copy";
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),copyMeasureName)).Count>0);
            
        }
        
        [CodedStep(@"Delete copy of measure")]
        public void TST_ML_026_CS14()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),copyMeasureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),copyMeasureName));
            measureNameTraingleImg.MouseClick();
            System.Threading.Thread.Sleep(2000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell deleteCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("project_measure_library_delete"));
            deleteCell.Wait.ForExists();
            deleteCell.Click();
            Pages.PS_ProjectMeasuresPage.DeleteYesButton.Wait.ForExists();
            Pages.PS_ProjectMeasuresPage.DeleteYesButton.Click();
            ActiveBrowser.WaitUntilReady(); 
            
        }
        
    }
}
