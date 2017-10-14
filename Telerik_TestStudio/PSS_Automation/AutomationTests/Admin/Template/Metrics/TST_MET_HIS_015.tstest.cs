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

    public class TST_MET_HIS_015 : BaseWebAiiTest
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
        public void TST_MET_HIS_015_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_015_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep3()
        {
      string lineItemConstraintLocator = string.Format(AppLocators.get("metric_line_item_edit_constraint_select"));
      HtmlSelect lineItemConstraintSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemConstraintLocator);
      lineItemConstraintSelector.SelectByText("Set", true);
      
      string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_edit_name_input"));
      HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
      string lineName = lineItemName.Value; 
      SetExtractedValue("LineName",lineName);
      
       Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
       ActiveBrowser.WaitUntilReady();
       System.Threading.Thread.Sleep(3000);
      
       //click on Static/Constraint values link
      
      Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
      Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
      ActiveBrowser.WaitUntilReady();
      
      //Enter Set Item Details
      
       
      
                        
          string itemName = Data["SetItemName"].ToString();
          int itemOptions = Int32.Parse(Data["SetItemOptions"].ToString()); 
          int defaultOpt = Int32.Parse(Data["SetDefaultOption"].ToString()); 
          for(int j=1;j<=itemOptions;j++){
              string itemSeqLoc = string.Format(AppLocators.get("metric_set_seq_input"),itemName,j);
              HtmlInputText itemSeqInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemSeqLoc);
              itemSeqInput.SetValue("value",Data[string.Format("SetOption{0}Seq",j)].ToString());
              
              System.Threading.Thread.Sleep(1000);
              string itemValLoc = string.Format(AppLocators.get("metric_set_value_input"),itemName,j);
              HtmlInputText itemValInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemValLoc);
              itemValInput.SetValue("value",Data[string.Format("SetOption{0}Value",j)].ToString());
              System.Threading.Thread.Sleep(1000);
              string itemLabelLoc = string.Format(AppLocators.get("metric_set_label_input"),itemName,j);
              HtmlInputText itemLabelInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemLabelLoc);
              itemLabelInput.SetValue("value",Data[string.Format("SetOption{0}Label",j)].ToString());
              System.Threading.Thread.Sleep(1000);
          }
          
          string defaultRadioLoc = string.Format(AppLocators.get("metric_set_default_radio_input"),itemName,defaultOpt);
          if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlInputRadioButton>(defaultRadioLoc).Count > 0){
              HtmlInputRadioButton itemDefaultRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(defaultRadioLoc);
              itemDefaultRadio.Click();
          }
      
      
      
                           }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_015_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_015_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep7()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Set\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0}",Data["SetItemName"].ToString());
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem constraint change event for range in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_015_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_015_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep10()
        {
            string lineItemBehaviorLocator = string.Format(AppLocators.get("metric_line_item_edit_behavior_select"));
            HtmlSelect lineItemBehaviorSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemBehaviorLocator);
            lineItemBehaviorSelector.SelectByText("Variant", true);
        }
    
        //[CodedStep(@"Click on Spreadsheet link")]
        //public void TST_MET_HIS_015_CodedStep11()
        //{
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"Wait for Line Items Tab")]
        //public void TST_MET_HIS_015_CodedStep13()
        //{
            //ActiveBrowser.RefreshDomTree();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep14()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep15()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Cumulative\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Behaviour change event for range in spreadsheet should be logged in history");
            
        }
    
        //[CodedStep(@"Click on Spreadsheet link")]
        //public void TST_MET_HIS_015_CodedStep16()
        //{
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.SpreadsheetLink.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep17()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_015_CodedStep18()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep19()
        {
            string lineItemBehaviorLocator = string.Format(AppLocators.get("metric_line_item_edit_behavior_select"));
            HtmlSelect lineItemBehaviorSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemBehaviorLocator);
            lineItemBehaviorSelector.SelectByText("Static", true);
            
            //Submit the changes
             Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
             ActiveBrowser.WaitUntilReady();
             System.Threading.Thread.Sleep(3000);
            
            //Click on Static/Constraint values link
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            //Set the values for static behaviour
            string itemName = Data["SetItemName"].ToString();
            string itemStaticValLoc = string.Format(AppLocators.get("metric_static_value_input"),itemName);
            HtmlInputText itemStaticValInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemStaticValLoc);
            itemStaticValInput.SetValue("value",Data["SetStaticValue"].ToString());
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep20()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep21()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = string.Format("added new static value ${0}.00",Data["SetStaticValue"].ToString());
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Behaviour change event for range in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Verify Line Item Scale action for 'K' is reflected in history ")]
        public void TST_MET_HIS_015_CodedStep11()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"scale\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr = string.Format("to: {0}",Data["LineItemScaleK"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')and contains(.,'{5}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Behaviour change event for range in spreadsheet should be logged in history");
            
        }
    
        //[CodedStep(@"Update  Line Item Scale to  'k'")]
        //public void TST_MET_HIS_015_CodedStep16()
        //{
                  //string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_edit_scale_select"));
                  //HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
                  //lineItemScaleSelector.SelectByText("K",true);
                  
                  //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
                  //ActiveBrowser.WaitUntilReady();
                  //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Update  Line Item Scale to  'k'")]
        //public void TST_MET_HIS_015_CodedStep16()
        //{
                  //string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_edit_scale_select"));
                  //HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
                  //lineItemScaleSelector.SelectByText(Data["LineItemScaleK"].ToString(),true);
                  
                  //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
                  //ActiveBrowser.WaitUntilReady();
                  //System.Threading.Thread.Sleep(3000);
        //}
    
        [CodedStep(@"Update  Line Item Scale to  'k'")]
        public void TST_MET_HIS_015_CodedStep16()
        {
                  string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_edit_scale_select"));
                  HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
                  lineItemScaleSelector.SelectByText(Data["LineItemScaleK"].ToString(),true);
                  
                  Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
                  ActiveBrowser.WaitUntilReady();
                  System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep22()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"Wait for Line Items Tab")]
        //public void TST_MET_HIS_015_CodedStep23()
        //{
            //ActiveBrowser.RefreshDomTree();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep23()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep24()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep25()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep26()
        {
            string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_edit_scale_select"));
            HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
            lineItemScaleSelector.SelectByText(Data["LineItemScaleM"].ToString(),true);
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep27()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"scale\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",Data["LineItemScaleK"].ToString());
            string updateStr2 = string.Format("to: {0}",Data["LineItemScaleM"].ToString());
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Scale change event for M in spreadsheet should be logged in history");
            
        }
    
    
        
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep28()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TST_MET_HIS_015_CodedStep29()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Update Line Item RollsUp field")]
        public void TST_MET_HIS_015_CodedStep30()
        {
                 string lineItemRollUpLocator = string.Format(AppLocators.get("metric_line_item_edit_rollup_checkbox"));
                 HtmlInputCheckBox lineItemRollUpChkbox = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputCheckBox>(lineItemRollUpLocator);
                 lineItemRollUpChkbox.Click();
                 
                 
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep31()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify Line Items RollsUp action is reflected in history")]
        public void TST_MET_HIS_015_CodedStep32()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"RollsUp\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr1,actionStr2,actionStr3,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem RollsUp change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep33()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Update Line Item Description field")]
        public void TST_MET_HIS_015_CodedStep34()
        {
                     string lineItemDescLocator = string.Format(AppLocators.get("metric_line_item_edit_description_input"));
                     HtmlInputText lineItemDesc= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDescLocator);
                     string oldDescValue = lineItemDesc.Value;
                     lineItemDesc.SetValue("value",Data["EditLineItemDesc"].ToString());
                     string newDescValue = Data["EditLineItemDesc"].ToString();
                     SetExtractedValue("OldDescValue",oldDescValue);
                     SetExtractedValue("NewDescValue",newDescValue);
                     
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep35()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify Line Items Description action is reflected in history")]
        public void TST_MET_HIS_015_CodedStep36()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Description\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: {0}",GetExtractedValue("OldDescValue"));
            string updateStr2 = string.Format("to: {0}",GetExtractedValue("NewDescValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Description change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep37()
        {
             string itemName = Data["SetItemName"].ToString();
             int j=1;
             string itemValLoc = string.Format(AppLocators.get("metric_set_value_input"),itemName,j);
             HtmlInputText itemValInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemValLoc);
             string oldSetValue = itemValInput.Value;
             string newSetValue = Data["EditSetOptionValue"].ToString();
             itemValInput.SetValue("value",Data["EditSetOptionValue"].ToString());
             SetExtractedValue("OldSetValue",oldSetValue);
             SetExtractedValue("NewSetValue",newSetValue);
             System.Threading.Thread.Sleep(1000);
             
             
        }
    
        [CodedStep(@"Click on edit link to do changes")]
        public void TST_MET_HIS_015_CodedStep38()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetEditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep39()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SpreadsheetStaticConstraiLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_015_CodedStep40()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Set\" field of the Metric Template";
            string actionStr2 = string.Format("Item: {0} in the",Data["SetItemName"].ToString());
            string actionStr3 = "Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = string.Format("from: [${0}",GetExtractedValue("OldSetValue"));
            string updateStr2 = string.Format("to: [${0}",GetExtractedValue("NewSetValue"));
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}') and contains(.,'{5}') and contains(.,'{6}')]",userName,actionStr1,actionStr2,actionStr3,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "LineItem Description change events in spreadsheet should be logged in history");
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_015_CodedStep41()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    }
    
}
