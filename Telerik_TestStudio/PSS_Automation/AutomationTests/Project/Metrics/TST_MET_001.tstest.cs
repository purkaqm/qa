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

    public class TST_MET_001 : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
           
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("project_manage_metrics_table_row")).Count > 0){
                Pages.PS_ProjectManageMetricsPage.AttachNewBtn.Click();
            }
            else{
                Pages.PS_ProjectManageMetricsPage.ClickHereLink.Click();
            }
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDiv.Wait.ForVisible();
            //Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep1()
        {
            HtmlOption optiontoSelect = Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.Options[1];
            string attachedTemplate = optiontoSelect.BaseElement.InnerText;
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByValue("1", true);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
            ActiveBrowser.WaitUntilReady();
            SetExtractedValue("AttachedTemplate",attachedTemplate);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep2()
        {
            string attachedTemplate = GetExtractedValue("AttachedTemplate").ToString();
            ActiveBrowser.RefreshDomTree();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep3()
        {
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("AttachedTemplate").ToString());
            HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
            menuItem.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep4()
        {
            string attachedTemplate = GetExtractedValue("AttachedTemplate").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep5()
        {
            string attachedTemplate = GetExtractedValue("AttachedTemplate").ToString();
            HtmlTableRow rowElement = ActiveBrowser.Find.ByXPath<HtmlTableRow>(AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]");
            HtmlAnchor templateLink = rowElement.Find.ByXPath<HtmlAnchor>(AppLocators.get("project_manage_metrics_table_name_link"));
            templateLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_001_CodedStep6()
        {
            string attachedTemplate = GetExtractedValue("AttachedTemplate").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            
            //left section verification
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            string leftSection = Pages.PS_MetricInstancePage.TemplateDetailsLeftSection.InnerText;
            Assert.IsTrue(leftSection.Contains("Metric:"),"Metric: should be displayed in left part of detail section");
            Assert.IsTrue(leftSection.Contains("Financial Reps:"),"Financial Reps: should be displayed in left part of detail section");
            Assert.IsTrue(leftSection.Contains("Updated:"),"Updated: should be displayed in left part of detail section");
            Assert.IsTrue(leftSection.Contains("Project Start – End:"),"Project Start – End: should be displayed in left part of detail section");
            
            //right section verification
            Assert.IsTrue(Pages.PS_MetricInstancePage.EditPropertiesLink.IsVisible(),"Edit Properties link should be present");
            string rightSection = Pages.PS_MetricInstancePage.TemplateDetailsRightSection.InnerText;
            //Assert.IsTrue(rightSection.Contains("Ready for Rollup:"),"Ready for Rollup: should be displayed in right part of detail section");
            Assert.IsTrue(rightSection.Contains("Validated:"),"Validated: should be displayed in right part of detail section");
            
            //other buttons and link verification
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricContainerGridDiv.IsVisible(),"Metrig Grid Container in center should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.SaveChangesBtn.IsVisible(),"Save Changes button should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.ResetBtn.IsVisible(),"Reset button should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.PDFLink.IsVisible(),"PDF link should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.CSVLink.IsVisible(),"CSV link should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.ExcelLink.IsVisible(),"Excel link should be present");
            Assert.IsTrue(Pages.PS_MetricInstancePage.WordLink.IsVisible(),"Word link should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_001_CodedStep7()
        {
            string attachedTemplate = GetExtractedValue("AttachedTemplate").ToString();
            HtmlTableRow rowElement = ActiveBrowser.Find.ByXPath<HtmlTableRow>(AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]");
            HtmlInputCheckBox checkbox = rowElement.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("project_manage_metrics_table_delete_chkbx"));
            checkbox.Click();
            System.Threading.Thread.Sleep(5000);
            Pages.PS_ProjectManageMetricsPage.DeleteBtn.Click();
            //Pages.PS_ProjectManageMetricsPage.DeleteBtn.Click(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ProjectManageMetricsPage.RemoveDeleteTemplateDialogDiv.Wait.ForExists();
            Pages.PS_ProjectManageMetricsPage.RemoveDeleteTemplateDialogOKBtn.Wait.ForVisible();
            Pages.PS_ProjectManageMetricsPage.RemoveDeleteTemplateDialogOKBtn.Click();
            ActiveBrowser.WaitUntilReady();
            
            
            
        }
    }
}
