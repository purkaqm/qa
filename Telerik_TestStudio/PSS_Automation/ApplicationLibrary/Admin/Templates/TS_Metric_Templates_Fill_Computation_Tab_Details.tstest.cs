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

    public class TS_Metric_Templates_Fill_Computation_Tab_Details : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Static Constraint Items Tab")]
        public void TS_Metric_Templates_Fill_Computation_Tab_Details_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ComputationLINameLabel.Wait.ForExists();
        }
    
        [CodedStep(@"Enter Static and Constraint values")]
        public void TS_Metric_Templates_Fill_Computation_Tab_Details_CodedStep1()
        {
            
            //Enter Computation Formula Details
            int formulaLineItemCount = Int32.Parse(Data["ComputationCount"].ToString()); 
            for(int i = 1; i <=formulaLineItemCount;i++){
                
                string itemName = Data["LINameComputation"+i].ToString();
                string itemNameLinkLocator = string.Format(AppLocators.get("metric_computation_line_item_link"),itemName);
                HtmlAnchor lineItemNameLink= Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(itemNameLinkLocator);
                lineItemNameLink.Click();
                ActiveBrowser.WaitUntilReady();
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Wait.ForVisible();
                
                
                string formulaMode = Data["FormulaModeComputation"+i].ToString();
                string formulaStr = Data["FormulaComputation"+i].ToString();
                if(formulaMode.ToLower().Equals("text")){
                   
                    string actualFormula=formulaStr.Replace("---","");
                    if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.IsVisible()){
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.Wait.ForVisible();
                    }
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaTextModeArea.SetValue("value",actualFormula);
                    Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Click();
                }
                if(formulaMode.ToLower().Equals("builder")){
                    string[] formulaItemsArray = System.Text.RegularExpressions.Regex.Split(formulaStr, "---");
                    if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaBuilderLink.IsVisible()){
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaBuilderLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaAddMoreItemsLink.Wait.ForVisible();
                        for(int k=0;k<formulaItemsArray.Length;k++){
                            string currentValue = formulaItemsArray[k].Replace("[","").Replace("]","");
                            string selectLoc = string.Format(AppLocators.get("metric_computation_formula_select"),k+1);
                            HtmlSelect selectOption = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlSelect>(selectLoc);
                            selectOption.SelectByText(currentValue,true);
                        }
                     
                    }
                }
               Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FormulaSaveLink.Click();
               ActiveBrowser.WaitUntilReady();
               System.Threading.Thread.Sleep(3000);
            }
            Actions.InvokeScript("document.getElementById(\"PageContent\").scrollTop = 0;");
            System.Threading.Thread.Sleep(3000);
            
            
            
        }
    }
}
