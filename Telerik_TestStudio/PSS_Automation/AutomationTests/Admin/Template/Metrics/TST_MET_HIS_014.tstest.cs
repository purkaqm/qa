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

    public class TST_MET_HIS_014 : BaseWebAiiTest
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
        public void TST_MET_HIS_014_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_014_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep3()
        {
            string lineItemSeqLocator = string.Format(AppLocators.get("metric_line_item_edit_seq_input"));
            HtmlInputText lineItemSeq = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemSeqLocator);
            string oldValue = lineItemSeq.Value;
            lineItemSeq.SetValue("value",Data["LineItemSequence"].ToString());
            string newValue = Data["LineItemSequence"].ToString();
            SetExtractedValue("OldValue",oldValue);
            SetExtractedValue("NewValue",newValue);
            
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_014_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        //[CodedStep(@"Click on submit to see all history records")]
        //public void TST_MET_HIS_014_CodedStep6()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep7()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"Sequence\" field of the Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldValue"));
            string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Line item sequence action in spreadsheet should be logged in history");
            
        }
    
        //[CodedStep(@"Go to Summary tab")]
        //public void TST_MET_HIS_014_CodedStep8()
        //{
            //Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        //}
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_014_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep10()
        {
            string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            lineItemDataTypeSelector.SelectByValue("Numeric",true);
            
            string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
            HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
            string lineName = lineItemName.Value; 
            SetExtractedValue("LineName",lineName);
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_014_CodedStep11()
        //{
            //string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            //HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            //lineItemDataTypeSelector.SelectByValue("Separator",true);
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Update Line Item Data type to 'Seperator'")]
        //public void TST_MET_HIS_014_CodedStep11()
        //{
            //string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
            //HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
            //string lineName = lineItemName.Value; 
            //SetExtractedValue("LineName",lineName);
            
            //string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            //HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            //lineItemDataTypeSelector.SelectByValue("Separator",true);
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //System.Threading.Thread.Sleep(3000);
            
        //}
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_014_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        //[CodedStep(@"Click on Spreadsheet link")]
        //public void TST_MET_HIS_014_CodedStep12()
        //{
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Click on edit link to do changes")]
        //public void TST_MET_HIS_014_CodedStep13()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Wait for Line Items Tab")]
        //public void TST_MET_HIS_014_CodedStep14()
        //{
            //ActiveBrowser.RefreshDomTree();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        //}
    
        //[CodedStep(@"Submit the changes")]
        //public void TST_MET_HIS_014_CodedStep15()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Click on history tab")]
        //public void TST_MET_HIS_014_CodedStep16()
        //{
            //Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Click on submit to see all history records")]
        //public void TST_MET_HIS_014_CodedStep17()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep18()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Monetary\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0}",GetExtractedValue("LineName"));
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Line item sequence action in spreadsheet should be logged in history");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep19()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"scale\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0}",GetExtractedValue("LineName"));
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Line item sequence action in spreadsheet should be logged in history");
            
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_014_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Spreadsheet link")]
        public void TST_MET_HIS_014_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_014_CodedStep13()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Update Line Item Data type to 'Seperator'")]
        public void TST_MET_HIS_014_CodedStep14()
        {
            string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
            HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
            string lineName = lineItemName.Value; 
            SetExtractedValue("LineName",lineName);
            
            string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            lineItemDataTypeSelector.SelectByValue("Separator",true);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        //[CodedStep(@"Click on Spreadsheet link")]
        //public void TST_MET_HIS_014_CodedStep16()
        //{
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Click on edit link to do changes")]
        //public void TST_MET_HIS_014_CodedStep17()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        //[CodedStep(@"Wait for Line Items Tab")]
        //public void TST_MET_HIS_014_CodedStep20()
        //{
            //ActiveBrowser.RefreshDomTree();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep16()
        {
            string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
            HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
            string oldLineName = lineItemName.Value;
            string newLineName = Data["LineItemName"].ToString();
            SetExtractedValue("OldLineName",oldLineName);
            lineItemName.SetValue("value",newLineName);
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep17()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        //[CodedStep(@"Click on Spreadsheet link")]
        //public void TST_MET_HIS_014_CodedStep20()
        //{
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep21()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_014_CodedStep22()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep23()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"ItemName\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0}",GetExtractedValue("OldLineName"));
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem line name change event in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Click on Spreadsheet link")]
        public void TST_MET_HIS_014_CodedStep24()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep25()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_014_CodedStep26()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep27()
        {
            string lineItemConstraintLocator = string.Format(AppLocators.get("metric_line_item_edit_constraint_select"));
            HtmlSelect lineItemConstraintSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemConstraintLocator);
            lineItemConstraintSelector.SelectByText("Range", true);
           
            string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
            HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
            string lineName = lineItemName.Value; 
            SetExtractedValue("LineName",lineName);
            
             Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
             ActiveBrowser.WaitUntilReady();
             System.Threading.Thread.Sleep(3000);
            
             //click on Static/Constraint values
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            //set the range values
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LowerRange.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UpperRange.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultRange.Wait.ForVisible();
            string lowerRange = Data["LowerRange"].ToString();
            string upperRange = Data["UpperRange"].ToString();
            string defaultRange = Data["DefaultRange"].ToString();
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LowerRange.SetValue("value",lowerRange);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UpperRange.SetValue("value",upperRange);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultRange.SetValue("value",defaultRange);
            
            
                
            
                
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep28()
        {
             string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
             HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
             lineItemDataTypeSelector.SelectByValue("Monetary",true);
            
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep29()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Spreadsheet link")]
        public void TST_MET_HIS_014_CodedStep30()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep31()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"Wait for Line Items Tab")]
        //public void TST_MET_HIS_014_CodedStep32()
        //{
            //ActiveBrowser.RefreshDomTree();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep33()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep34()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Range\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0}",GetExtractedValue("LineName"));
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem constraint change event for range in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep20()
        {
           
               string lineItemDefaultLocator = string.Format(AppLocators.get("metric_line_item_edit_default_input"));
               HtmlInputText lineItemDefaultInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDefaultLocator);
               lineItemDefaultInput.SetValue("value",Data["LineItemDefault"].ToString());
                  
    }

        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep35()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"defaultValue\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["LineItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Behaviour change event for range in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep36()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Static/Constraint link")]
        public void TST_MET_HIS_014_CodedStep37()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep38()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LowerRange.Wait.ForVisible();
            string oldRangeValue = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LowerRange.Value;
            string lowerRange = Data["EditLowerRange"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LowerRange.SetValue("value",lowerRange);
            SetExtractedValue("OldRangeValue",oldRangeValue);
            SetExtractedValue("NewRangeValue",lowerRange);
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_014_CodedStep39()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep40()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Range\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["LineItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: [${0}",GetExtractedValue("OldRangeValue"));
            string updateStr2 = string.Format("to: [${0}",GetExtractedValue("NewRangeValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Range value change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_014_CodedStep41()
        {
            string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            lineItemDataTypeSelector.SelectByValue("Cost",true);
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_014_CodedStep32()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }}
