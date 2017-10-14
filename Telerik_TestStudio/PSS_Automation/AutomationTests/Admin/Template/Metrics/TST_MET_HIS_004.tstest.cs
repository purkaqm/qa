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

    public class TST_MET_HIS_004 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_222_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_222_CodedStep1()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        [CodedStep(@"Wait for Display Tab")]
        public void TST_MET_HIS_222_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }
    
        //[CodedStep(@"Update Dispaly By Default option to 'First Period with Data'")]
        //public void TST_MET_HIS_222_CodedStep3()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            //string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            //string s1 = "First Period with Data";
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByText(s1);
            //SetExtractedValue("OldName",oldName);
            
            
              
            
            
            
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_222_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_222_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_222_CodedStep6()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        //[CodedStep(@"Update Dispaly By Default option to 'Project Start Date'")]
        //public void TST_MET_HIS_222_CodedStep7()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            //string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            //string s1 = "Project Start Date";
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByText(s1);
            //SetExtractedValue("OldName",oldName);
            
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_222_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_222_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_222_CodedStep10()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        //[CodedStep(@"Update Dispaly By Default option to 'Current State'")]
        //public void TST_MET_HIS_222_CodedStep11()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeRadioBtn.Click();
            //string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectedOption.BaseElement.InnerText;
            //string s1 = "Current Date";
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayByDefaultRangeStartSelect.SelectByText(s1);
            //SetExtractedValue("OldName",oldName);
            
        //}
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_222_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
            
        }
    
        //[CodedStep(@"Click on history tab")]
        //public void TST_MET_HIS_222_CodedStep13()
        //{
            //Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_222_CodedStep14()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        //[CodedStep(@"Verify Display By Default setting for 'First Period with Data' reflected in History")]
        //public void TST_MET_HIS_222_CodedStep15()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            //string metricName = GetExtractedValue("GeneratedMetricName").ToString();;
            //string updateStr1 = "from: prsdate";
            //string updateStr2 = "to: fperdata";
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            //Log.WriteLine(nameVerificationLocator);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD first period setting change action should be logged in history");
            
        //}
    
        //[CodedStep(@"Verify Display By Default setting for 'Project Start Date' reflected in History")]
        //public void TST_MET_HIS_222_CodedStep16()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            //string metricName = GetExtractedValue("GeneratedMetricName").ToString();;
            //string updateStr1 = "from: fperdata";
            //string updateStr2 = "to: prsdate";
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD project start date setting change action should be logged in history");
            
        //}
    
        //[CodedStep(@"Verify Display By Default setting for 'Current Date' reflected in History")]
        //public void TST_MET_HIS_222_CodedStep17()
        //{
            //string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            //string actionStr = "changed the display setting \"refpoint\" in the  Metric Template";
            //string metricName = GetExtractedValue("GeneratedMetricName").ToString();; 
            //string updateStr1 = "from: prsdate";
            //string updateStr2 = "to: cdate";
            //string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "DBD current date setting change action should be logged in history");
            
        //}
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_222_CodedStep18()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_222_CodedStep19()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Total Displayed setting for 'Grand Total' reflected in History")]
        public void TST_MET_HIS_222_CodedStep20()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"Default Total Frequency\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: Never";
            string updateStr2 = "to: Infinitely";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Total display setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Verify Total Displayed setting for 'Never' reflected in History")]
        public void TST_MET_HIS_222_CodedStep21()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"Default Total Frequency\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: Infinitely";
            string updateStr2 = "to: Never";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Total display setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Verify Total Displayed setting for 'Displayed Range' reflected in History")]
        public void TST_MET_HIS_222_CodedStep22()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"Default Total Frequency\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: Never";
            string updateStr2 = "to: DisplayRange";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Total display setting change action should be logged in history");
            
        }
    
        [CodedStep(@"Update Total Displayed option to 'Grand Total'")]
        public void TST_MET_HIS_222_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "Infinitely";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
            
              
            
            
            
        }
    
        [CodedStep(@"Update Total Displayed option to 'Never'")]
        public void TST_MET_HIS_222_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "Never";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    
        [CodedStep(@"Update Total Displayed option to 'DisplayRange'")]
        public void TST_MET_HIS_222_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "DisplayRange";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_004_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_004_CodedStep1()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_004_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "Quarterly";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_004_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Click();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "Yearly";
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.SelectByValue(s1,true);
            SetExtractedValue("OldName",oldName);
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_004_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
            
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_004_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on  Display summary page")]
        public void TST_MET_HIS_004_CodedStep6()
        {
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_004_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
            
            
        }
    
        //[CodedStep(@"Click on Edit link")]
        //public void TST_MET_HIS_004_CodedStep8()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        //[CodedStep(@"Click on  Display summary page")]
        //public void TST_MET_HIS_004_CodedStep9()
        //{
            
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Wait.ForVisible();
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDisplayLink.Click();
            
            
        //}
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_004_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_004_CodedStep8()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"Default Total Frequency\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: DisplayRange";
            string updateStr2 = "to: Quarterly";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Total display setting change action should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_004_CodedStep9()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the display setting \"Default Total Frequency\" in the  Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string updateStr1 = "from: Quarterly";
            string updateStr2 = "to: Yearly";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}') and contains(.,'{4}')]",userName,actionStr,metricName,updateStr1,updateStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Total display setting change action should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_004_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FrequencySelect.SelectByValue("Monthly");
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_004_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    }
}
