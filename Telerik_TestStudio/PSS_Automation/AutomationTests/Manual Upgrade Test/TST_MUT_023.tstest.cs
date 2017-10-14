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

    public class TST_MUT_023 : BaseWebAiiTest
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
    
        [CodedStep(@"Set search item")]
        public void TST_MUT_023_SetSearchItem()
        {
            /// \bug Move to a central place
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_MUT_023_VerifySummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
        
        [CodedStep(@"Click on Attach New button")]
        public void TST_MUT_023_ClickAttachNewButton()
        {
            /// \bug Move to a central place
            if(ActiveBrowser.Find.AllByXPath<HtmlAnchor>("//a[@id='templateSelectDialogShow']").Count > 0)
            {
                HtmlAnchor hereLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//a[@id='templateSelectDialogShow']");
                hereLink.Click();
            }
            else
            {
                Pages.PS_ManageMetricsPage.AttachNewButton.Click();
                Pages.PS_ManageMetricsPage.AttachButton.Wait.ForExists();
            }
        }
        
        
        [CodedStep(@"Select the Metric Template wanted to be attached and Click on attach button")]
        public void TST_MUT_023_SelectMetricTemplateAndAttach()
        {
            Pages.PS_ManageMetricsPage.AttachMetricSelect.SelectByText(GetExtractedValue("GeneratedMetricName").ToString());
            Pages.PS_ManageMetricsPage.AttachButton.Click();
            ActiveBrowser.WaitUntilReady();    
        }
        
        [CodedStep(@"Verify attached metric is displayed on left navigation menu")]
        public void TST_MUT_023_VerifyAttachedMetric()
        {
            HtmlDiv attachedMetric = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("added_metric_template_left_nav_menu"),GetExtractedValue("GeneratedMetricName"))); 
            attachedMetric.Wait.ForExists();
            Assert.IsTrue(attachedMetric.IsVisible(),"Added Metric was not displayed");
        }
        
        [CodedStep(@"Discard attached metric template from project")]
        public void TST_MUT_023_DiscardMtericTemplate()
        {
            /// \bug Move to a central place
            HtmlInputCheckBox attachedMetricChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(.,'{0}')]//input",GetExtractedValue("GeneratedMetricName"))); 
            attachedMetricChkbox.Wait.ForExists();
            attachedMetricChkbox.Click();
            attachedMetricChkbox.Check(true);
            Pages.PS_ManageMetricsPage.DeleteButton.Click();
            Pages.PS_AdminDashboardLayoutsPage.DeleteDashbaordLayoutOkButton.Wait.ForExists();
            Pages.PS_AdminDashboardLayoutsPage.DeleteDashbaordLayoutOkButton.Click();
            ActiveBrowser.WaitUntilReady(); 
        }
        
        [CodedStep(@"MouseHover on Admin icon on left navigation bar")]
        public void TST_MUT_023_ClickAdminIcons()
        {
            /// \bug Why is mouse hover needed here?
            Pages.PS_HomePage.AdminLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
        }
        
        [CodedStep(@"Click on Created Metric template link")]
        public void TST_MUT_023_ClickMetricTemplate()
        {
            System.Threading.Thread.Sleep(4000);
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("metric_template_link"),GetExtractedValue("GeneratedMetricName").ToString()));
            HtmlAnchor createdMetricLink = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("metric_template_link"),GetExtractedValue("GeneratedMetricName").ToString()));
            createdMetricLink.Wait.ForExists();
            createdMetricLink.Click(true);
            
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MUT_023_DeleteMetricTemplate()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.ScrollToVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
     
    }
}
