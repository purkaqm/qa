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

    public class TST_MET_HIS_013 : BaseWebAiiTest
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
        public void TST_MET_HIS_013_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on Delegate Owner link  ")]
        public void TST_MET_HIS_013_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TeamMembersLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Type 'user name' in search box")]
        public void TST_MET_HIS_013_CodedStep2()
        {
            string userName = Data["UserName"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerSearchInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerSearchInput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerSearchInput.SetValue("value",userName);
        }
    
        [CodedStep(@"Click on Search link ")]
        public void TST_MET_HIS_013_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerSearchLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click Delegate owner button for respective user")]
        public void TST_MET_HIS_013_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerUserLink.Wait.ForExists();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_013_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_013_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_013_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify Delegate Owner change event is reflected in History")]
        public void TST_MET_HIS_013_CodedStep8()
        {
            string addUserName =Data["UserName"].ToString();
            string actionStr1 = "user accepted your invitation";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string actionStr2 = "as Team Member";
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",addUserName,actionStr1,metricName,actionStr2);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Team Member add event should be logged in history");
            
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_013_CodedStep9()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_013_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on Delegate Owner link  ")]
        public void TST_MET_HIS_013_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TeamMembersLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on Delecate remove cancel link ")]
        public void TST_MET_HIS_013_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerRemoveLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DelegateOwnerRemoveLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MET_HIS_013_CodedStep13()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_013_CodedStep14()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MET_HIS_013_CodedStep15()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify Remove Delegate Owner change event is reflected in History")]
        public void TST_MET_HIS_013_CodedStep16()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string removeUserName = Data["UserName"].ToString();
            string actionStr1 = string.Format("removed {0} user",removeUserName);
            string actionStr2 = "from Metric Template";
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Remove team member change action should be logged in history");
            
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MET_HIS_013_CodedStep17()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_013_CodedStep18()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
