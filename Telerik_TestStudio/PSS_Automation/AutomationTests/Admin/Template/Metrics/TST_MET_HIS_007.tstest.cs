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

    public class TST_MET_HIS_007 : BaseWebAiiTest
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
        public void TST_MET_HIS_007_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_007_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.Wait.ForVisible();
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.SelectByIndex(2, true);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.Wait.ForVisible();
            string oldTagValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.SelectedOption.BaseElement.InnerText;
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.SelectByIndex(3, true);
            
            SetExtractedValue("OldTagValue",oldTagValue);
            string newTagValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.SelectedOption.BaseElement.InnerText;
            SetExtractedValue("NewTagValue",newTagValue);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_007_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_007_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_007_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_007_CodedStep5()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldTagValue"));
            string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewTagValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change tag breakdown tag setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_007_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_007_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_007_CodedStep8()
        {
            if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioNo.Checked)
            {   
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioYes.Click();
            string oldValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioNo.BaseElement.InnerText;
            string newValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioYes.BaseElement.InnerText;
            SetExtractedValue("OldValue",oldValue);
            SetExtractedValue("NewValue",newValue);  
            }
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_007_CodedStep9()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            //string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldTagValue"));
            //string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewTagValue"));
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change tag breakdown tag setting action in view should be logged in history");
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_007_CodedStep9()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldValue"));
            string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change tag breakdown tag setting action in view should be logged in history");
            
        }
    
        //[CodedStep(@"Click on Edit link")]
        //public void TST_MET_HIS_007_CodedStep10()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Update Allocate Benifits in Tag Breakdown ")]
        //public void TST_MET_HIS_007_CodedStep11()
        //{
            //if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioNo.Checked)
            //{   
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioYes.Click();
            //string oldValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioNo.BaseElement.InnerText;
            //string newValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioYes.BaseElement.InnerText;
            //SetExtractedValue("OldValue",oldValue);
            //SetExtractedValue("NewValue",newValue);  
            //}
            
        //}
    }
}
