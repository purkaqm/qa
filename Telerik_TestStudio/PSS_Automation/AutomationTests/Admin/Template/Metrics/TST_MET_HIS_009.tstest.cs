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

    public class TST_MET_HIS_009 : BaseWebAiiTest
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
        public void TST_MET_HIS_009_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        //[CodedStep(@"Click on view tab")]
        //public void TST_MET_HIS_009_CodedStep1()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"Click on view tab")]
        public void TST_MET_HIS_009_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Adding additional 'View' ")]
        public void TST_MET_HIS_009_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewSequenceInput.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewAddLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Wait.ForVisible();
            
            int index = Int32.Parse(Data["ViewRecordRow"].ToString());
            
                //Enter View Name
                string viewNameLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_name_input"),index);
                HtmlInputText viewName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(viewNameLocator);
                viewName.SetValue("value",Data["ViewName"].ToString());
                
                //Enter View Sequence
                string viewSeqLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_seq_input"),index);
                HtmlInputText viewSeq= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(viewSeqLocator);
                viewSeq.SetValue("value",Data["ViewSequence"].ToString());
                
                //Select Cost Mapping
                string costMappingSelectLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_cost_map_select"),index);
                HtmlSelect costMappingSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(costMappingSelectLocator);
                costMappingSelector.SelectByValue(Data["ViewCostMapping"].ToString(), true);
                
                //Select Work Type
                string workTypeSelectLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_work_type_select"),index);
                HtmlSelect workTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(workTypeSelectLocator);
                workTypeSelector.SelectByText(Data["ViewWorkType"].ToString(), true);
                
                //Choose Cost Roll Ups
                if(Data["ViewCostRollUps"].ToString().ToLower().Contains("yes")){
                string costRollUpYesRadioLocator = string.Format(AppLocators.get("metric_view_tab_cost_rollup_yes"),index);
                HtmlInputRadioButton costRollUpYesRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(costRollUpYesRadioLocator);
                costRollUpYesRadio.Click();
                
                string rollUpWorkTypeSelectLocator = string.Format(AppLocators.get("metric_view_tab_cost_rollup_work_type_select"),index);
                HtmlSelect rollUpWorkTypeSelect= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(rollUpWorkTypeSelectLocator);
                rollUpWorkTypeSelect.SelectByText(Data["ViewRollUpWorkType"].ToString(),true);
            
            }
        }
    
        //[CodedStep(@"Submit the changes")]
        //public void TST_MET_HIS_009_CodedStep3()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Click on history tab")]
        //public void TST_MET_HIS_009_CodedStep4()
        //{
            //Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Click on submit to see all history records")]
        //public void TST_MET_HIS_009_CodedStep5()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_009_CodedStep6()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "added the View: TestAdditionalView to the Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}')]",userName,actionStr,metricName);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change tag breakdown tag setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_009_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_009_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_009_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_009_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_009_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    }
}
