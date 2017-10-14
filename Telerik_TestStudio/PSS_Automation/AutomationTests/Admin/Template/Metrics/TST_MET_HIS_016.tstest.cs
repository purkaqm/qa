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

    public class TST_MET_HIS_016 : BaseWebAiiTest
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
        public void TST_MET_HIS_016_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_016_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_016_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_HIS_016_CodedStep3()
        //{
                  //string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_edit_scale_select"));
                  //HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
                  //lineItemScaleSelector.SelectByText("K",true);
                  
                  //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
                  //ActiveBrowser.WaitUntilReady();
                  //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep4()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"View Cost Mapping\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from:";
            string updateStr2 = string.Format("to: {0}",Data["ViewMapping"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem View Mapping change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_016_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_016_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_016_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_016_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_016_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"Update Line Item RollsUp field")]
        //public void TST_MET_HIS_016_CodedStep10()
        //{
                     //string lineItemDescLocator = string.Format(AppLocators.get("metric_line_item_edit_description_input"));
                     //HtmlInputText lineItemDesc= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDescLocator);
                     //string oldDescValue = lineItemDesc.Value;
                     //lineItemDesc.SetValue("value",Data["LineItemDesc"].ToString());
                     //string newDescValue = Data["LineItemDesc"].ToString();
                     //SetExtractedValue("OldDescValue",oldDescValue);
                     //SetExtractedValue("NewDescValue",newDescValue);
                     
        //}
    
        [CodedStep(@"Update Line Items Data Type to 'Cost(Monetary)'")]
        public void TST_MET_HIS_016_CodedStep3()
        {
            string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_edit_data_type_select"));
            HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
            lineItemDataTypeSelector.SelectByValue("Cost",true);
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_016_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Static/Constraint link")]
        public void TST_MET_HIS_016_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_016_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep13()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetViewMappingSel.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetViewMappingSel.Click();
            string selViewMap = Data["ViewMapping"].ToString().ToUpper();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetViewMappingSel.SelectByValue(selViewMap);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep14()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetWorkTagsSel.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetWorkTagsSel.Click();
            string selWorkTags = Data["WorkTags"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetWorkTagsSel.SelectByText(selWorkTags);
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(1000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetActivitySel.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetActivitySel.Click();
            string selActivity = Data["Activity"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetActivitySel.SelectByValue(selActivity);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep16()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetCostTagSel.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetCostTagSel.Click();
            string selCostTag = Data["CostTag"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetCostTagSel.SelectByValue(selCostTag);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep17()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Work Tags Filter\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from:";
            string updateStr2 = string.Format("to: {0}",Data["WorkTags"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Work Tags change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep18()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Activity Filter\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            //string updateStr1 = "from:";
            //string updateStr2 = string.Format("to: {0}",Data["Activity"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Work Tags change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep19()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"User Cost Tags Filter\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Work Tags change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_016_CodedStep20()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Static/Constraint link")]
        public void TST_MET_HIS_016_CodedStep21()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep22()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadshtWrkTgFilterSel.IsVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadshtWrkTgFilterSel.SelectByIndex(2);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_016_CodedStep23()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_016_CodedStep24()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Work Tags Filter\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",Data["WorkTags"].ToString());
            string updateStr2 = string.Format("to: {0}: {1}",Data["WorkTags"].ToString(),Data["EditWorkTagFilterVal"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Work Tags change events in spreadsheet should be logged in history");
            
        }
    }
}
