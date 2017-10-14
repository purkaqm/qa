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

    public class TST_ML_018 : BaseWebAiiTest
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
        public void TST_ML_018_CS00()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_ML_018_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Verify 'Manage Measures' and 'Available Measures' are displayed on Nav Menu")]
        public void TST_ML_018_CS02()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.AvailableMeasuresDiv.IsVisible());
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_ML_018_CS03()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify page title respect to selected project is displayed correct")]
        public void TST_ML_018_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue + " : Measures"));
        }
        
        [CodedStep(@"Click on 'Define New' button")]
        public void TST_ML_018_CS05()
        {
            Pages.PS_ProjectMeasuresPage.DefineNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify 'Add measure' with Project Name page title is displayed")]
        public void TST_ML_018_CS06()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Add Measure | " + CustomUtils.locationValue));
        }
        
        [CodedStep(@"Verify all required fields are present")]
        public void TST_ML_018_CS07()
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
        
    }
}
