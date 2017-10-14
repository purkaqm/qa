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

    public class TS_Metric_Template_Fill_View_Tab_Details : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Display Tab")]
        public void TS_Metric_Template_Fill_View_Tab_Details_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewAddLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Template_Fill_View_Tab_Details_CodedStep1()
        {
            int viewCount = Int32.Parse(Data["TotalViews"].ToString()); 
            for(int i = 1; i <=viewCount;i++){
                
                int index = Int32.Parse(Data["ViewRecordRow"+i].ToString()) + 1;
            
                //Enter View Name
                string viewNameLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_name_input"),index);
                HtmlInputText viewName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(viewNameLocator);
                viewName.SetValue("value",Data["ViewName"+i].ToString());
                
                //Enter View Sequence
                string viewSeqLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_seq_input"),index);
                HtmlInputText viewSeq= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(viewSeqLocator);
                viewSeq.SetValue("value",Data["ViewSequence"+i].ToString());
                
                //Select Cost Mapping
                string costMappingSelectLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_cost_map_select"),index);
                HtmlSelect costMappingSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(costMappingSelectLocator);
                costMappingSelector.SelectByValue(Data["ViewCostMapping"+i].ToString(), true);
                
                //Select Work Type
                string workTypeSelectLocator = string.Format(AppLocators.get("metric_view_tab_tmpl_work_type_select"),index);
                HtmlSelect workTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(workTypeSelectLocator);
                workTypeSelector.SelectByText(Data["ViewWorkType"+i].ToString(), true);
                
                //Choose Cost Roll Ups
                if(Data["ViewCostRollUps"+i].ToString().ToLower().Contains("yes")){
                string costRollUpYesRadioLocator = string.Format(AppLocators.get("metric_view_tab_cost_rollup_yes"),index);
                HtmlInputRadioButton costRollUpYesRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(costRollUpYesRadioLocator);
                costRollUpYesRadio.Click();
                
                string rollUpWorkTypeSelectLocator = string.Format(AppLocators.get("metric_view_tab_cost_rollup_work_type_select"),index);
                HtmlSelect rollUpWorkTypeSelect= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(rollUpWorkTypeSelectLocator);
                rollUpWorkTypeSelect.SelectByText(Data["ViewRollUpWorkType"+i].ToString(),true);
            
            }
            }
            
            
        }
    }
}
