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

    public class TS_Metric_Templates_Fill_Display_Tab_Details : BaseWebAiiTest
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
    
        //[CodedStep(@"New Coded Step")]
        //public void TS_Metric_Templates_Fill_Display_Tab_Details_CodedStep()
        //{
            
        //}
    
        [CodedStep(@"Choose option for Allow RollUp")]
        public void TS_Metric_Templates_Fill_Display_Tab_Details_CodedStep1()
        {
            if(Data["DisplayByDefault"].ToString().ToLower().Contains("yes")){
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
                if(Data["PeriodsStartPoint"].ToString().Length > 0 ){
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByValue(Data["PeriodsStartPoint"].ToString(), true);
                } 
                if(Data["PeriodsBack"].ToString().Length > 0 ){
                   // Actions.SetText(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultBackwardPeriodsText, Data["PeriodsBack"].ToString());
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultBackwardPeriodsText.SetValue("value",Data["PeriodsBack"].ToString());
                }
                 if(Data["PeriodsForward"].ToString().Length > 0 ){
                    // Actions.SetText(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultForwardPeriodsText, Data["PeriodsForward"].ToString());
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultForwardPeriodsText.SetValue("value",Data["PeriodsForward"].ToString());
                }
            }
            else{
                
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultCurrentRadioBtn.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Templates_Fill_Display_Tab_Details_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForExists();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForExists();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Templates_Fill_Display_Tab_Details_CodedStep2()
        {
            if(Data["TotalsDisplayed"].ToString().Length > 0 ){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(Data["TotalsDisplayed"].ToString(), true);
            }
        }
    }
}
