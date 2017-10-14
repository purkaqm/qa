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

    public class TST_MET_HIS_005 : BaseWebAiiTest
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
        
        
        
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_005_CodedStep()
        {
           
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_005_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_005_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.Wait.ForVisible();
            string oldViewName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.Value;
            string newViewName = "TestViewName";
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewNameInput.SetValue("value",newViewName);
            SetExtractedValue("OldViewName",oldViewName);
            SetExtractedValue("NewViewName",newViewName);
            System.Threading.Thread.Sleep(1000);
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_005_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_005_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_005_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_005_CodedStep6()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: "+GetExtractedValue("OldViewName").ToString();
            string updateStr2 = "to: "+GetExtractedValue("NewViewName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "veiw name change action should be logged in history");
       
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_005_CodedStep7()
        {
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "ACTUAL";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectByValue(s1,true);
            
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_005_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_005_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_005_CodedStep10()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: ESTIMATED";
            string updateStr2 = "to: ACTUAL";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change cost mapping setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Verify cost mapping select action is reflected in History")]
        public void TST_MET_HIS_005_CodedStep11()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: ACTUAL";
            string updateStr2 = "to: MANUAL";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change cost mapping setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Verify cost mapping select action is reflected in History")]
        public void TST_MET_HIS_005_CodedStep12()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: MANUAL";
            string updateStr2 = "to: ESTIMATED";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,metricName,updateStr1,updateStr2);
            Log.WriteLine("estm = "+nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "change cost mapping setting action in view should be logged in history");
            
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_005_CodedStep13()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on view tab")]
        public void TST_MET_HIS_005_CodedStep14()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
        }
    
        [CodedStep(@"Update cost mappinng to 'Actual'")]
        public void TST_MET_HIS_005_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "MANUAL";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectByValue(s1,true);
            SetExtractedValue("OldCostMappingValue2",oldName);
            SetExtractedValue("NewCostMappingValue2",s1);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_005_CodedStep16()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_005_CodedStep17()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on view tab")]
        public void TST_MET_HIS_005_CodedStep18()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
        }
    
        [CodedStep(@"Update cost mappinng to 'Manual'")]
        public void TST_MET_HIS_005_CodedStep19()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "ESTIMATED";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.SelectByValue(s1,true);
            SetExtractedValue("OldCostMappingValue3",oldName);
            SetExtractedValue("NewCostMappingValue3",s1);
            
        }

        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_005_CodedStep28()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
  
    }
}
