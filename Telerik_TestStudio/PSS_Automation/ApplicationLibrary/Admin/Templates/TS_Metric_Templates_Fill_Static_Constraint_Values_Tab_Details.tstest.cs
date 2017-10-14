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

    public class TS_Metric_Templates_Fill_Static_Constraint_Values_Tab_Details : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Line Items Tab")]
        public void TS_Metric_Templates_Fill_Static_Constraint_Values_Tab_Details_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell contstraintTab = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[contains(.,'Static/Constraint Values')]");
            contstraintTab.Wait.ForExists();
            contstraintTab.Wait.ForAttributes("class","tabOn");
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.MoreItemsLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.StaticItemInput.Wait.ForExists();
        }
    
        [CodedStep(@"Enter Line Items Details")]
        public void TS_Metric_Templates_Fill_Static_Constraint_Values_Tab_Details_CodedStep1()
        {
            
            //Enter Static Item Details
            int staticItemsCount = Int32.Parse(Data["StaticValuesCount"].ToString()); 
            for(int i = 1; i <=staticItemsCount;i++){
                
                string itemName = Data["StaticItemName"+i].ToString();
                string itemInputLocator = string.Format(AppLocators.get("metric_static_value_input"),itemName);
                HtmlInputText lineItemName= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemInputLocator);
                lineItemName.SetValue("value",Data["StaticItemValue"+i].ToString());
            }
            
            //Enter Range Item Details
            int rangeItems = Int32.Parse(Data["RangeValuesCount"].ToString()); 
            for(int i = 1; i <=rangeItems;i++){
                
                string itemName = Data["RangeItemName"+i].ToString();
                string rangeMinValueLocator = string.Format(AppLocators.get("metric_range_value_min_input"),itemName);
                HtmlInputText rangeMinVal= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(rangeMinValueLocator);
                rangeMinVal.SetValue("value",Data["RangeStart"+i].ToString());
                
                string rangeMaxValueLocator = string.Format(AppLocators.get("metric_range_value_max_input"),itemName);
                HtmlInputText rangeMaxVal= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(rangeMaxValueLocator);
                rangeMaxVal.SetValue("value",Data["RangeEnd"+i].ToString());
                
                string rangeDefaultValueLocator = string.Format(AppLocators.get("metric_range_value_default_input"),itemName);
                if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlInputText>(rangeDefaultValueLocator).Count > 0){
                    HtmlInputText rangeDefaultVal= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(rangeDefaultValueLocator);
                    rangeDefaultVal.SetValue("value",Data["RangeDefault"+i].ToString());
                }
            }
            
            
             //Enter Set Item Details
            int setItems = Int32.Parse(Data["SetValuesCount"].ToString()); 
            for(int i = 1; i <=setItems;i++){
                              
                string itemName = Data["SetItemName"+i].ToString();
                int itemOptions = Int32.Parse(Data["SetItemOptions"+i].ToString()); 
                int defaultOpt = Int32.Parse(Data["SetDefaultOption"+i].ToString()); 
                for(int j=1;j<=itemOptions;j++){
                    string itemSeqLoc = string.Format(AppLocators.get("metric_set_seq_input"),itemName,j);
                    HtmlInputText itemSeqInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemSeqLoc);
                    itemSeqInput.SetValue("value",Data[string.Format("SetOption{0}Seq{1}",j,i)].ToString());
                    
                    System.Threading.Thread.Sleep(1000);
                    string itemValLoc = string.Format(AppLocators.get("metric_set_value_input"),itemName,j);
                    HtmlInputText itemValInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemValLoc);
                    itemValInput.SetValue("value",Data[string.Format("SetOption{0}Value{1}",j,i)].ToString());
                    System.Threading.Thread.Sleep(1000);
                    string itemLabelLoc = string.Format(AppLocators.get("metric_set_label_input"),itemName,j);
                    HtmlInputText itemLabelInput= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputText>(itemLabelLoc);
                    itemLabelInput.SetValue("value",Data[string.Format("SetOption{0}Label{1}",j,i)].ToString());
                    System.Threading.Thread.Sleep(1000);
                }
                
                string defaultRadioLoc = string.Format(AppLocators.get("metric_set_default_radio_input"),itemName,defaultOpt);
                if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlInputRadioButton>(defaultRadioLoc).Count > 0){
                    HtmlInputRadioButton itemDefaultRadio= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlInputRadioButton>(defaultRadioLoc);
                    itemDefaultRadio.Click();
                }
            }
            
        }
    
    
    }
}
