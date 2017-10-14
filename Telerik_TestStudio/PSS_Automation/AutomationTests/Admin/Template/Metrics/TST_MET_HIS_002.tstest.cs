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

    public class TST_MET_HIS_002 : BaseWebAiiTest
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
    
        
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_111_CodedStep1()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_111_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_111_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Display Tab")]
        public void TST_MET_HIS_111_CodedStep8()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_111_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
    
        
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_111_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_111_CodedStep11()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
       
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_111_CodedStep13()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        
    
        
    
      
    
        
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_111_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
            
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_111_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_111_CodedStep23()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_111_CodedStep24()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_111_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_111_CodedStep14()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_111_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_111_CodedStep17()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_111_CodedStep19()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep20()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultCurrentRadioBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_111_CodedStep21()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_111_CodedStep25()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_111_CodedStep26()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep27()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();; 
            string updateStr1 = "from: cdate";
            string updateStr2 = "to: fiscal";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD current date setting change action should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep28()
        {
            int oldValue1 = Int32.Parse(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultBackwardPeriodsText.Value);
            int newValue1 = Randomizers.generateRandomInt(1,4,oldValue1);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultBackwardPeriodsText.SetValue("value",newValue1);
            SetExtractedValue("OldValue1",oldValue1);
            SetExtractedValue("NewValue1",newValue1);
            
             int oldValue2 = Int32.Parse(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultBackwardPeriodsText.Value);
             int newValue2 = Randomizers.generateRandomInt(1,5,oldValue2);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DefaultForwardPeriodsText.SetValue("value",newValue2);
            SetExtractedValue("OldValue2",oldValue2);
            SetExtractedValue("NewValue2",newValue2);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep29()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "Default Backward Periods";
            string actionStr2 = "Default Forward Periods";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();; 
            
            string nameVerificationLocator1 = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'from: {3}') and contains(.,'to: {4}')]",userName,actionStr1,metricName,GetExtractedValue("OldValue1"),GetExtractedValue("NewValue1"));
            string nameVerificationLocator2 = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'from: {3}') and contains(.,'to: {4}')]",userName,actionStr2,metricName,GetExtractedValue("OldValue2"),GetExtractedValue("NewValue2"));
            
            Log.WriteLine(nameVerificationLocator1);
            Log.WriteLine(nameVerificationLocator2);
            
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator1).Count > 0,  "period background change action should be logged in history");
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator2).Count > 0, "forward background change action should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_111_CodedStep30()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_111_CodedStep31()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_111_CodedStep32()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
               
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_111_CodedStep35()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep34()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_111_CodedStep36()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        
    
        [CodedStep(@"Verify Display By Default setting for 'First Period with Data' reflected in History")]
        public void TST_MET_HIS_111_CodedStep33()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();;
            string updateStr1 = "from: prsdate";
            string updateStr2 = "to: fperdata";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD first period setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Verify Display By Default setting for 'Project Start Date' reflected in History")]
        public void TST_MET_HIS_111_CodedStep37()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();;
            string updateStr1 = "from: fperdata";
            string updateStr2 = "to: prsdate";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD project start date setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Verify Display By Default setting for 'Current Date' reflected in History")]
        public void TST_MET_HIS_111_CodedStep38()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();; 
            string updateStr1 = "from: prsdate";
            string updateStr2 = "to: cdate";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD current date setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Update Dispaly By Default option to 'First Period with Data'")]
        public void TST_MET_HIS_111_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "fperdata";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
                      
        }
    
        [CodedStep(@"Update Dispaly By Default option to 'Project Start Date'")]
        public void TST_MET_HIS_111_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "prsdate";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    
        [CodedStep(@"Update Dispaly By Default option to 'Current State'")]
        public void TST_MET_HIS_111_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "cdate";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    }
}
