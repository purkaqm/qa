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

    public class TST_MET_HIS_018 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Spreadsheet Link")]
        public void TST_MET_HIS_018_CS01()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@" Click on edit link to do changes")]
        public void TST_MET_HIS_018_CS02()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForExists();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
         [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_018_CS11()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Add some Line Items ")]
        public void TST_MET_HIS_018_CS03()
        {
                            
           int lineItemCount = Int32.Parse(Data["TotalExtraItems"].ToString()); 
           for(int index=1;index<=lineItemCount;index++){
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
    
        [CodedStep(@" Submit the changes")]
        public void TST_MET_HIS_018_CS04()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
        [CodedStep(@"Delete Line Item ")]
        public void TST_MET_HIS_018_CS05()
        {
            Manager.DialogMonitor.AddDialog(AlertDialog.CreateAlertDialog(ActiveBrowser, DialogButton.OK));
            Manager.DialogMonitor.Start();
            
            int rowIndex = Int32.Parse(Data["RowIndex"].ToString());
            string delItemLocator = string.Format(AppLocators.get("metric_line_item_delete_span"),rowIndex+1);
            HtmlSpan delItem = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSpan>(delItemLocator);
            delItem.Click();
            
            ActiveBrowser.WaitUntilReady();     
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_018_CS06()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();                       
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_018_CS07()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify Delete Line Item event is added in history")]
        public void TST_MET_HIS_018_CS08()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "deleted the";
            string actionStr2 = "Metric Template";
            string actionStr3 = string.Format("{0} in the",Data["LINameComputation"].ToString());
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}')]",userName,actionStr1,actionStr2,actionStr3,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Work Tags change events in spreadsheet should be logged in history");
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_018_CS09()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_018_CS10()
       {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
