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

    public class TS_Metric_Templates_Create_Basic_Template : BaseWebAiiTest
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
    
      
    
        [CodedStep(@"Click Create New link")]
        public void TST_MET_002_CodedStep1()
        {
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
                                    ActiveBrowser.WaitUntilReady();
                                    ActiveBrowser.RefreshDomTree();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForExists();
        }
    
        [CodedStep(@"Click on Next button")]
        public void TST_MET_002_CodedStep3()
        {
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
                                    
        }
    
        [CodedStep(@"Wait for Display Tab to present")]
        public void TST_MET_008_CodedStep39()
        {
                                    ActiveBrowser.RefreshDomTree();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Wait for Computation Tab to present")]
        public void TS_Metric_Templates_Fill_Computation_Tab_Details_CodedStep()
        {
                                    ActiveBrowser.RefreshDomTree();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Wait.ForVisible();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ComputationLINameLabel.Wait.ForExists();
        }
    
        [CodedStep(@"Wait for Finish Tab to present")]
        public void TST_MET_002_CodedStep8()
        {
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
        [CodedStep(@"Click on Submit button")]
        public void TST_MET_002_CodedStep10()
        {
                                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
                                    ActiveBrowser.WaitUntilReady();
                                    
                                    
                                    
        }
    
        [CodedStep(@"Wait till Summary Page is displayed")]
        public void TS_Metric_Templates_Create_Sample_Template_CodedStep10()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
                        string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
                        Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
     
    }
}
