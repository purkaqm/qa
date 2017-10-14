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

    public class TS_Metric_Tempates_Fill_Basic_Info_Tab_Details : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep()
        {
            string metricName = Data["MetricName"].ToString()+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.SetValue("value",metricName);
            //Manager.Desktop.KeyBoard.TypeText(metricName,0);
            SetExtractedValue("GeneratedMetricName",metricName);
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep1()
        {
            if(Data["Description"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateDescriptionTextArea.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateDescriptionTextArea.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateDescriptionTextArea.SetValue("value",Data["Description"].ToString());
            //Manager.Desktop.KeyBoard.TypeText(Data["Description"].ToString(),0);
            }
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep2()
        {
            if(Data["AllowRollUp"].ToString().ToLower().Contains("yes")){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.Click();
            }
            else{
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep3()
        {
            //TBD
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep4()
        {
            if(Data["Calendar"].ToString().Length > 0){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CalendarSelect.SelectByValue(Data["Calendar"].ToString(), true);
            
            }    
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep5()
        {
            if(Data["Frequency"].ToString().Length > 0){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FrequencySelect.SelectByValue(Data["Frequency"].ToString(), true);
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep6()
        {
            if(Data["Frequency"].ToString().Length > 0 && !Data["Frequency"].ToString().ToLower().Contains("infinitely")){
            
            if(Data["PeriodStartDate"].ToString().Length > 0 ){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.SetValue("value","");
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.SetValue("value",Data["PeriodStartDate"].ToString());
            //Manager.Desktop.KeyBoard.TypeText(Data["PeriodStartDate"].ToString(),0);
            }
            if(Data["PeriodEndDate"].ToString().Length > 0){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodEndsInput.SetValue("value","");
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodEndsInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodEndsInput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodEndsInput.SetValue("value",Data["PeriodEndDate"].ToString());
            //Manager.Desktop.KeyBoard.TypeText(Data["PeriodEndDate"].ToString(),0);
            }
            
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep7()
        {
            if(Data["SendReminder"].ToString().Length > 0 ){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SendRemindersSelect.SelectByText(Data["SendReminder"].ToString(), true);
            }    
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep8()
        {
            if(Data["VersionControl"].ToString().Length > 0 ) {
            
                
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.VersionControlSelect.SelectByValue(Data["VersionControl"].ToString(),true);
            
            }    
        }
    
        [CodedStep(@"Choose option for Allow RollUp")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep9()
        {
            if(Data["Views"].ToString().ToLower().Contains("yes")){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewsYesInput.Click();
            }
            else{
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewsNoInput.Click();
            }
        }
    
        [CodedStep(@"Choose option for Views")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep10()
        {
            if(Data["Process"].ToString().ToLower().Contains("yes")){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessYesInput.Click();
            }
            else{
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ProcessNoInput.Click();
            }
        }
    
        [CodedStep(@"Choose option for Process")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep11()
        {
            if(Data["VerticalTotal"].ToString().ToLower().Contains("yes")){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.VerticalTotalsYesInput.Click();
            }
            else{
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.VerticalTotalsNoInput.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Temp_Fill_Basic_Info_CodedStep12()
        {
            if(Data["WorkTypeAllocation"].ToString().ToLower().Contains("yes")){
                string[] workTypesArr = System.Text.RegularExpressions.Regex.Split(Data["WorkTypes"].ToString(), "---");
                for(int i=0; i<workTypesArr.Length;i++){
                    string locator = string.Format(AppLocators.get("metric_auto_work_type_checkbox"),workTypesArr[i]);
                    HtmlInputCheckBox workTypeCheckbox = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputCheckBox>(locator);
                    workTypeCheckbox.Click();
                }
                
            }
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Metric_Tempates_Fill_Basic_Info_Tab_Details_CodedStep()
        {
            if(Data["TagBreakDown"].ToString().ToLower().Equals("yes")){
                string optionToSelect;
                if(Data["TagName"].ToString().ToLower().Equals("randomizer")){
                    
                    IList<HtmlOption> options = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.Find.AllByXPath<HtmlOption>("//option");
                    int randomOption = Randomizers.generateRandomInt(0, (options.Count - 1));
                    optionToSelect = options[randomOption].BaseElement.InnerText;
                    
                    
                }else{
                    optionToSelect = Data["TagName"].ToString();
                }
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TagBreakDownSelect.SelectByText(optionToSelect);
                SetExtractedValue("TagBreakdown",optionToSelect);
                
                if(Data["AllocateBenefits"].ToString().ToLower().Contains("yes")){
                     Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioYes.Click();
                }
                else{
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllocateBenefitsRadioNo.Click();
                }
                
                if(Data["PercentageAllocation"].ToString().ToLower().Contains("yes")){
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PercentAllocationRadioYes.Click();
                }
                else{
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PercentAllocationRadioNo.Click();
                }
                SetExtractedValue("SelectedTagBreakDown",optionToSelect);
                
            }
        }
    }
}
