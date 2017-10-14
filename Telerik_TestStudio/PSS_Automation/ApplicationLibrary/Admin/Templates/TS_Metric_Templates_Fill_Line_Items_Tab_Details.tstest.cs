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

    public class TS_Metric_Templates_Fill_Line_Items_Tab_Details : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for View Tab")]
        public void TS_Metric_Templates_Fill_Line_Items_Tab_Details_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.LineItemDataTypeSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Enter View Details")]
        public void TS_Metric_Templates_Fill_Line_Items_Tab_Details_CodedStep1()
        {
            int lineItemCount = Int32.Parse(Data["TotalLineItems"].ToString()); 
            for(int i = 1; i <=lineItemCount;i++){
                Log.WriteLine("Filling data for line item : " + i);
                int index = Int32.Parse(Data["LineItemRecordRow"+i].ToString()) ;
            
                //Enter Line Item Name
                string lineItemNameLocator = string.Format(AppLocators.get("metric_line_item_name_input"),index);
                HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemNameLocator);
                lineItemName.SetValue("value",Data["LineItemName"+i].ToString());
                
                //Enter Line Item Sequence
                string lineItemSeqLocator = string.Format(AppLocators.get("metric_line_item_seq_input"),index);
                HtmlInputText lineItemSeq= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemSeqLocator);
                lineItemSeq.SetValue("value",Data["LineItemSequence"+i].ToString());
                
                //Enter Line Item Description
                string lineItemDescLocator = string.Format(AppLocators.get("metric_line_item_description_input"),index);
                HtmlInputText lineItemDesc= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDescLocator);
                lineItemDesc.SetValue("value",Data["LineItemDesc"+i].ToString());
                
                    
                //Select Line Item Data Type
                string dataType = Data["LineItemDataType"+i].ToString();
                string lineItemDataTypeLocator = string.Format(AppLocators.get("metric_line_item_data_type_select"),index);
                HtmlSelect lineItemDataTypeSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemDataTypeLocator);
                lineItemDataTypeSelector.SelectByValue(dataType,true);
                
                if(dataType.ToLower().Equals("separator")){
                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                    System.Threading.Thread.Sleep(3000);
                    continue;
                }
                
                //Select Line Item Scale
                string lineItemScaleLocator = string.Format(AppLocators.get("metric_line_item_scale_select"),index);
                HtmlSelect lineItemScaleSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemScaleLocator);
                lineItemScaleSelector.SelectByText(Data["LineItemScale"+i].ToString(),true);
                
                //Choose Line Item RollUp
                string lineItemRollUpLocator = string.Format(AppLocators.get("metric_line_item_rollup_checkbox"),index);
                HtmlInputCheckBox lineItemRollUpChkbox= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputCheckBox>(lineItemRollUpLocator);
                if(Data["LineItemRollUp"+i].ToString().ToLower().Equals("yes") && lineItemRollUpChkbox.BaseElement.GetAttribute("checked").ToString().Equals("false")){
                    lineItemRollUpChkbox.Click();
                }
                if(Data["LineItemRollUp"+i].ToString().ToLower().Equals("no") && lineItemRollUpChkbox.BaseElement.GetAttribute("checked").ToString().Equals("true")){
                    lineItemRollUpChkbox.Click();
                }
                
                
                if(dataType.ToLower().Equals("cost")){
                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                    System.Threading.Thread.Sleep(3000);
                    continue;
                }
                
               
                //Select Line Item Constraint
                string lineItemConstraintLocator = string.Format(AppLocators.get("metric_line_item_constraint_select"),index);
                HtmlSelect lineItemConstraintSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemConstraintLocator);
                lineItemConstraintSelector.SelectByText(Data["LineItemConstraint"+i].ToString(), true);
                
                //Select Line Item Behavior
                string lineItemBehaviorLocator = string.Format(AppLocators.get("metric_line_item_behavior_select"),index);
                HtmlSelect lineItemBehaviorSelector= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(lineItemBehaviorLocator);
                lineItemBehaviorSelector.SelectByText(Data["LineItemBehavior"+i].ToString(), true);
                
                //Enter Line Item Default
                string lineItemDefaultLocator = string.Format(AppLocators.get("metric_line_item_default_input"),index);
                if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlInputText>(lineItemDefaultLocator).Count > 0 ){
                    HtmlInputText lineItemDefaultInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(lineItemDefaultLocator);
                    lineItemDefaultInput.SetValue("value",Data["LineItemDefault"+i].ToString());
                }
               
                }
            }
            
            
        }
   
}
