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

    public class TST_MET_HIS_011 : BaseWebAiiTest
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
        public void TST_MET_HIS_011_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on view tab")]
        public void TST_MET_HIS_011_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_011_CodedStep2()
        {
            
                string costRollUpYesRadioLocator = string.Format(AppLocators.get("metric_view_tab_edit_cost_rollup_yes"));
                HtmlInputRadioButton costRollUpYesRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(costRollUpYesRadioLocator);
                string costRollUpNoRadioLocator = string.Format(AppLocators.get("metric_view_tab_edit_cost_rollup_no"));
                HtmlInputRadioButton costRollUpNoRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(costRollUpYesRadioLocator);
                
                costRollUpYesRadio.Click();
                
                string rollUpWorkTypeSelectLocator = string.Format(AppLocators.get("metric_view_tab_edit_cost_rollup_work_type_select"));
                HtmlSelect rollUpWorkTypeSelect= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(rollUpWorkTypeSelectLocator);
                rollUpWorkTypeSelect.SelectByText("Work".ToString(),true);
        
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_011_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_011_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_011_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        //[CodedStep(@"Verify work type action is reflected in History")]
        //public void TST_MET_HIS_011_CodedStep6()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            //string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldWorkTypeValue"));
            //string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewWorkTypeValue"));
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,metricName,updateStr1,updateStr2);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change work type action setting action in view should be logged in history");
            
        //}
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_011_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_011_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_011_CodedStep6()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Rollup Costs\" field of the view: TestView1 in the";
            string actionStr2 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change work type action setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_011_CodedStep9()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Rollup Work Types\" field of the view: TestView1 in the";
            string actionStr2 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change work type action setting action in view should be logged in history");
            
        }
    }
}
