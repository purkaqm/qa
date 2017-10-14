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

    public class TST_MAT_090_C992806 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_090_C992806_WaitProjectSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                                               
        }
    
        [CodedStep(@"Click on Attach New button")]
        public void TST_MAT_090_C992806_ClickAttachNewButton()
        {
            ActiveBrowser.RefreshDomTree();
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
        public void TST_MAT_090_C992806_SelectMetricTemplateAndAttach()
        {
            Pages.PS_ManageMetricsPage.AttachMetricSelect.SelectByText(GetExtractedValue("GeneratedMetricName").ToString());
            Pages.PS_ManageMetricsPage.AttachButton.Click();
            ActiveBrowser.WaitUntilReady();    
        }
    
        [CodedStep(@"Verify attached metric is displayed on left navigation menu")]
        public void TST_MAT_090_C992806_VerifyAttachedMetric()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            HtmlDiv attachedMetric = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("added_metric_template_left_nav_menu"),GetExtractedValue("GeneratedMetricName"))); 
            attachedMetric.Wait.ForExists();
            Assert.IsTrue(attachedMetric.IsVisible(),"Added Metric was not displayed");
        }
    
        [CodedStep(@"Click on Created Metric template link")]
        public void TST_MAT_090_C992806_ClickMetricTemplate()
        {
            System.Threading.Thread.Sleep(4000);
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("metric_template_link"),GetExtractedValue("GeneratedMetricName").ToString()));
            HtmlAnchor createdMetricLink = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("metric_template_link"),GetExtractedValue("GeneratedMetricName").ToString()));
            createdMetricLink.Wait.ForExists();
            createdMetricLink.Click(true);
                        
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MAT_090_C992806_DeleteMetricTemplate()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.ScrollToVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Delete the Metric Template permanently through deleted_metrics.jsp page")]
        public void TST_MAT_090_C992806_DeleteMetricTemplatePermanently()
        {

            HtmlInputCheckBox deleteMetricInstanceCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//div[@class='deleted'][contains(.,'{0}')]/input",GetExtractedValue("GeneratedMetricName").ToString()));
            deleteMetricInstanceCheckbox.Wait.ForExists();
            deleteMetricInstanceCheckbox.ScrollToVisible();
            deleteMetricInstanceCheckbox.Check(true);
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Click();
            Pages.PS_Deleted_Users_Page.PermanentlyDeleteUserProgressContainer.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForVisible();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Click();
        }
    }
}
