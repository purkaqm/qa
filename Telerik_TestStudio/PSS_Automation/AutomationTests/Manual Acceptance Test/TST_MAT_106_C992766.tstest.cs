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

    public class TST_MAT_106_C992766 : BaseWebAiiTest
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
        public void TST_MAT_106_C992766_ClickEditLink()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
        [CodedStep(@"Update Allow Rollup option")]
        public void TST_MAT_106_C992766_ChangeRollUpOption()
        {
            if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.Checked){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.Click();
            string allowRollupOldInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.BaseElement.InnerText;
            string allowRollupNewInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.BaseElement.InnerText;    
            SetExtractedValue("AllowRollupOldInput",allowRollupOldInput);
            SetExtractedValue("AllowRollupNewInput",allowRollupNewInput);
            }
            else{
             Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.Click();
             string allowRollupOldInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.BaseElement.InnerText;
             string allowRollupNewInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.BaseElement.InnerText;  
             SetExtractedValue("AllowRollupOldInput",allowRollupOldInput);
             SetExtractedValue("AllowRollupNewInput",allowRollupNewInput);
            
            }
                        
                        
                        
        }
    
        [CodedStep(@"Submit the changes")]
        public void TST_MAT_106_C992766_SubmitChanges()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MAT_106_C992766_ClickHistoryTab()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
        
        [CodedStep(@"Click on submit to see all history records")]
        public void TST_MAT_106_C992766_ClickSubmitButton()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
                        
        }
    
        [CodedStep(@"Verify allow rollup setting is reflected in History")]
        public void TST_MAT_106_C992766_VerifyRollUpSettingChanged()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"Permits Rollups\" field in the  Metric Template";
            string updateStr1 = "from: "+GetExtractedValue("AllowRollupOldInput").ToString();;
            string updateStr2 = "to: "+GetExtractedValue("AllowRollupNewInput").ToString();;
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Frequency setting change action should be logged in history");
                        
        }
    
        [CodedStep(@"Go to Summary tab")]
        public void TST_MAT_106_C992766_OpenSummaryPage()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MAT_106_C992766_DelteTemplate()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
    }
}
