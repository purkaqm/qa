using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using ArtOfTest.WebAii.Win32.Dialogs;
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

    public class TST_MET_HIS_017 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Spreadsheet link")]
        public void TST_MET_HIS_017_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
       
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpeardsheetComputatnLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpeardsheetComputatnLink.Click();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_017_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep1()
        {
            string itemName = Data["LINameComputation"].ToString();
            string itemNameLinkLocator = string.Format(AppLocators.get("metric_computation_line_item_link"),itemName);
            HtmlAnchor lineItemNameLink= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(itemNameLinkLocator);
            lineItemNameLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Wait.ForVisible();
            
            string formulaStr = Data["FormulaComputation"].ToString();
            string actualFormula=formulaStr.Replace("---","");
            if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.IsVisible()){
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.Wait.ForVisible();
                    }
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.SetValue("value",actualFormula);
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Click();
        }
    
       
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_017_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_017_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_017_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep8()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Equation\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["LINameComputation"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Formula change events in computation should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_017_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_017_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep4()
        {
                
            
           
            for(int index=1;index<=2;index++){
                //Enter Line Item Name
                string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_name_input"),index);
                HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
                lineItemName.SetValue("value",Data["AddLineItemName"+index].ToString());
                
                //Enter Line Item Sequence
                string lineItemSeqLocator = string.Format(AppLocators.get("metric_line_item_seq_input"),index);
                HtmlInputText lineItemSeq= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemSeqLocator);
                lineItemSeq.SetValue("value",Data["AddLineItemSequence"+index].ToString());
                
                //Enter Line Item Description
                string lineItemDescLocator = string.Format(AppLocators.get("metric_line_item_description_input"),index);
                HtmlInputText lineItemDesc= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDescLocator);
                lineItemDesc.SetValue("value",Data["AddLineItemDesc"+index].ToString());
            }
        }
    
        
    
        
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep11()
        {
            string itemName = Data["LINameComputation"].ToString();
            string itemNameLinkLocator = string.Format(AppLocators.get("metric_computation_line_item_link"),itemName);
            HtmlAnchor lineItemNameLink= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(itemNameLinkLocator);
            lineItemNameLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Wait.ForVisible();
            string formulaStr = Data["EditFormulaComputation"].ToString();
            string actualFormula=formulaStr.Replace("---","");
            SetExtractedValue("NewFormulaValue",actualFormula);
            if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.IsVisible()){
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.Wait.ForVisible();
                    }
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.SetValue("value",actualFormula);
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Click();
        
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep12()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Equation\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["LINameComputation"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 1, "Line Item change events in computation should be logged in history");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_017_CodedStep13()
        {
            OnBeforeUnloadDialog dialog = OnBeforeUnloadDialog.CreateOnBeforeUnloadDialog(ActiveBrowser, DialogButton.OK);
            Manager.DialogMonitor.AddDialog(dialog);
            
            int rowIndex = Int32.Parse(Data["RowIndex"].ToString());
            string delItemLocator = string.Format(AppLocators.get("metric_line_item_delete_span"),rowIndex+1);
            HtmlSpan delItem = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSpan>(delItemLocator);
            delItem.Click();
            
            dialog.WaitUntilHandled(5000);
            ActiveBrowser.WaitUntilReady();     
        }
    }
}
