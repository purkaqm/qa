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

    public class TST_MET_HIS_003 : BaseWebAiiTest
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
    
        //[CodedStep(@"Click to open Metric template")]
        //public void TST_MET_HIS_004_CodedStep()
        //{
            //HtmlAnchor resMetric = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(AppLocators.get("metric_open_first_template"));
            //resMetric.Click();
            //SetExtractedValue("ResMetric",resMetric.InnerText);
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Verify user is navigated to Metric template page")]
        //public void TST_MET_HIS_004_CodedStep1()
        //{
           //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.IsVisible(),"Delete span should be visible");
           //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(),"Edit link should be visible");
           //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryResetLink.IsVisible(),"Reset link should be visible");
           //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryStartRectnLink.IsVisible(),"Start Recalculation link should be visible");
           //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.IsVisible(),"Table should be displayed");
        //}
    
        //[CodedStep(@"Click on Edit link")]
        //public void TST_MET_HIS_004_CodedStep2()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_004_CodedStep3()
        //{
            //if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Checked)
            //{
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessNoInput.Click();
                //ActiveBrowser.WaitUntilReady();
            //}
            //else
            //{
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Click();
                 //ActiveBrowser.WaitUntilReady();
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
                //ActiveBrowser.WaitUntilReady();
                
                //System.Threading.Thread.Sleep(1000);
                //HtmlInputRadioButton selProcess = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(AppLocators.get("metric_select_process_radio_button"));
                //selProcess.Click();
                
             //}
            
        //}
    
        //[CodedStep(@"Click on history tab")]
        //public void TST_MET_HIS_004_CodedStep4()
        //{
            //Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Click on submit")]
        //public void TST_MET_HIS_004_CodedStep5()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_004_CodedStep6()
        //{
            
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_004_CodedStep6()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string actionStr = "changed the \"Has Phases\" field in the  Metric Template";
            //string metricName = string.Format("{0}",GetExtractedValue("ResMetric"));
            //string updateStr1 = "from: No";
            //string updateStr2 = "to: Yes";
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            //Log.WriteLine(nameVerificationLocator);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Frequency setting change action should be logged in history");
            
                
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_004_CodedStep7()
        //{
            
        //}
    
        //[CodedStep(@"Click on Edit link")]
        //public void TST_MET_HIS_003_CodedStep()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
            
        //}
    
        //[CodedStep(@"Select Process option")]
        //public void TST_MET_HIS_003_CodedStep1()
        //{
            //if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Checked)
            //{
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessNoInput.Click();
                //ActiveBrowser.WaitUntilReady();
            //}
            //else
            //{
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Click();
                //ActiveBrowser.WaitUntilReady();
                //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessLink.Click();
                //HtmlInputRadioButton selProcess = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(AppLocators.get("metric_select_process_radio_button"));
                //selProcess.Click();
                //}
                
        //}
    
        //[CodedStep(@"Submit the changes")]
        //public void TST_MET_HIS_003_CodedStep2()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Click on history tab")]
        //public void TST_MET_HIS_003_CodedStep3()
        //{
            //Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Click on submit to see all history records")]
        //public void TST_MET_HIS_003_CodedStep4()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Go to Summary tab")]
        //public void TST_MET_HIS_003_CodedStep5()
        //{
            //Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        //}
    
        //[CodedStep(@"Delete newly created metric template")]
        //public void TST_MET_HIS_003_CodedStep6()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_003_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Update 'yes' to select process")]
        public void TST_MET_HIS_003_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Process link")]
        public void TST_MET_HIS_003_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Select a process")]
        public void TST_MET_HIS_003_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SoftwareProcessRadioBtn.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SoftwareProcessRadioBtn.Click();
            
        }
    
        [CodedStep(@"Click on submit button")]
        public void TST_MET_HIS_003_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_003_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Process link")]
        public void TST_MET_HIS_003_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Change process")]
        public void TST_MET_HIS_003_CodedStep7()
        {
            string oldProcessValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SoftwareProcessRadioBtn.Value;
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CiProjectProcessRadioBtn.Click();
            string newProcessValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CiProjectProcessRadioBtn.Value;
            SetExtractedValue("OldProcessValue",oldProcessValue);
            SetExtractedValue("NewProcessValue",newProcessValue);
            
        }
    
        [CodedStep(@"Click on submit button")]
        public void TST_MET_HIS_003_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on history tab ")]
        public void TST_MET_HIS_003_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on submit history link to see all events")]
        public void TST_MET_HIS_003_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click(); 
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify process change event is reflected in History")]
        public void TST_MET_HIS_003_CodedStep11()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the value of the tag Process associated with";
            string actionStr2 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from {0}",GetExtractedValue("OldProcessValue"));
            string updateStr2 = string.Format("to {0}",GetExtractedValue("NewProcessValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') ]",userName,actionStr1,actionStr2,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change process setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_003_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_003_CodedStep13()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
