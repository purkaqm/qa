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

    public class TST_ML_002 : BaseWebAiiTest
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
        public void TST_ML_002_CS01()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Review left navigation link")]
        public void TST_ML_002_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Click on Add New button")]
        public void TST_ML_002_CS03()
        {
            Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.Click(true);
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify all required elements are present on 'Measure Template' page")]
        public void TST_ML_002_CS04()
        {
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.AttachableToDiv.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.AutoAttachableDiv.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.AvailableToDiv.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CancelSubmit.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CurrencyConversionNoRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.CurrencyConversionYesRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.DescriptionTextArea.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.EffectiveDatesSelect.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.FormulaRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.GoalRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.GreenMsgTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.LockPropertyImage.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.ManualRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NameTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NoneRadio.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.OwnerDiv.IsVisible());
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
