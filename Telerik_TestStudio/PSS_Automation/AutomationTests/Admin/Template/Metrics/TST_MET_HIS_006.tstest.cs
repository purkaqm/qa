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

    public class TST_MET_HIS_006 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_006_CodedStep()
        {
           
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on view tab")]
        public void TST_MET_HIS_006_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_006_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.Wait.ForVisible();
            string oldViewSeqValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.Value;
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.Focus();
            string newViewSeqValue = "4";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.SetValue("value",newViewSeqValue);
            SetExtractedValue("OldViewSeqValue",oldViewSeqValue);
            SetExtractedValue("NewViewSeqValue",newViewSeqValue);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_006_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_006_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_006_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_006_CodedStep6()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string actionStr = "changed the \"Sequence\" field of the view: TestView1 in the Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldViewSeqValue"));
            string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewViewSeqValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change View sequence setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_006_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_006_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
