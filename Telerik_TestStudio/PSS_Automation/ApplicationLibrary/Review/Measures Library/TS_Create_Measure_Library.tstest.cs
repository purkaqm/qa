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

    public class TS_Create_Measure_Library : BaseWebAiiTest
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
        
        [CodedStep(@"Enter Name")]
        public void TS_Create_Measure_Library_CS01()
        {
            string measureName = "     aTest_library" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.NameTextInput,measureName);
            SetExtractedValue("MeasureName",measureName.Trim());
        }
        
        [CodedStep(@"Set Data Collection")]
        public void TS_Create_Measure_Library_CS02()
        {
            Pages.PS_MeasureTemplatesNewPage.ManualRadio.Click(true);
        }
        
        [CodedStep(@"Set Display Format")]
        public void TS_Create_Measure_Library_CS03()
        {
            Pages.PS_MeasureTemplatesNewPage.DisplayFormatSelect.SelectByText("Integer");
        }
        
        [CodedStep(@"Set Effective dates")]
        public void TS_Create_Measure_Library_CS04()
        {
            Pages.PS_MeasureTemplatesNewPage.EffectiveDatesSelect.SelectByText("Always");
        }
        
        [CodedStep(@"Set Indicator type")]
        public void TS_Create_Measure_Library_CS05()
        {
            Pages.PS_MeasureTemplatesNewPage.GoalRadio.Click();
        }
        
        [CodedStep(@"Set Threshold1")]
        public void TS_Create_Measure_Library_CS06()
        {
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalIndTextInput,"10");
        }
        
        [CodedStep(@"Set Threshold2")]
        public void TS_Create_Measure_Library_CS07()
        {
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalInd0Text,"20");
        }
        
        [CodedStep(@"Click on Submit button")]
        public void TS_Create_Measure_Library_CS08()
        {
            Pages.PS_MeasureTemplatesNewPage.SubmitButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
