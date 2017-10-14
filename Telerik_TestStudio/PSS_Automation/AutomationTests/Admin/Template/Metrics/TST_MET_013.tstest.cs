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

    public class TST_MET_013 : BaseWebAiiTest
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
        public void TST_MET_007_CodedStep9()
        {
                        Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
                        Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
                        
                        
        }
    
        [CodedStep(@"Store Project URL")]
        public void TST_MET_007_CodedStep10()
        {
                        SetExtractedValue("ProjectURL", ActiveBrowser.Url);
        }
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_007_CodedStep()
        {
                        Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
                        if(ActiveBrowser.Find.AllByXPath(AppLocators.get("project_manage_metrics_table_row")).Count > 0){
                            Pages.PS_ProjectManageMetricsPage.AttachNewBtn.Click();
                        }
                        else{
                            Pages.PS_ProjectManageMetricsPage.ClickHereLink.Click();
                        }
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        ActiveBrowser.RefreshDomTree();
                        System.Threading.Thread.Sleep(3000);
                        Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDiv.Wait.ForVisible();
                        //Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"Select any Metric Template from drop down")]
        public void TST_MET_007_CodedStep1()
        {
                        string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
                        Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
                        Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        System.Threading.Thread.Sleep(7000);
                        ActiveBrowser.RefreshDomTree();
                        
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_007_CodedStep2()
        {
                        string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
                        string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
                        Log.WriteLine(elementLocator);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
                        string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"Click on work type link and go to module wizard page")]
        public void TST_MET_013_CodedStep()
        {
            string workTypeLinkLocator = string.Format(AppLocators.get("admin_modulels_work_type_link"),Data["ProjectWorkType"].ToString());
            HtmlAnchor workTypeLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(workTypeLinkLocator);
            workTypeLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ModuleWizardPage.OPTIONSLink.Wait.ForExists();
            
        }
    
        [CodedStep(@"Click on OPTIONS tab")]
        public void TST_MET_013_CodedStep1()
        {
            Pages.PS_ModuleWizardPage.OPTIONSLink.Wait.ForVisible();
            Pages.PS_ModuleWizardPage.OPTIONSLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ModuleWizardPage.SearchLink.Wait.ForExists();
            Pages.PS_ModuleWizardPage.SearchMetricText.Wait.ForVisible();
        }
    
        [CodedStep(@"Click on search button to open select metric window")]
        public void TST_MET_013_CodedStep2()
        {
            Pages.PS_ModuleWizardPage.SearchLink.Click();
            Manager.WaitForNewBrowserConnect("/modules/select_metric.jsp", true,Manager.Settings.ElementWaitTimeout);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            Pages.PS_ModulesSelectMetricPage.PopUpSearchLink.Wait.ForExists();
            Pages.PS_ModulesSelectMetricPage.PopUpSearchMetricText.Wait.ForVisible();
        }
    
        [CodedStep(@"Search for metric then select metric, its period, view and line items. Hit submit")]
        public void TST_MET_013_CodedStep3()
        {
            
            // search for metric
            
             string metricName = GetExtractedValue("GeneratedMetricName").ToString();
             Actions.SetText(Pages.PS_ModulesSelectMetricPage.PopUpSearchMetricText, metricName);
             Pages.PS_ModulesSelectMetricPage.PopUpSearchLink.Click();
             ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
             System.Threading.Thread.Sleep(5000);
            
            //select searched metric for further options
            string metricSelectLinkLocator = string.Format(AppLocators.get("admin_modules_met_search_res_select_link"),metricName);
            HtmlAnchor metricSelectLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(metricSelectLinkLocator);
            metricSelectLink.Wait.ForExists();
            metricSelectLink.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_ModulesSelectMetricPage.PopUpSubmitLink.Wait.ForExists();
            Pages.PS_ModulesSelectMetricPage.PopUpTrackPeriodSelect.Wait.ForExists();
            
            
            
            
            //select track period, view and line items
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("admin_modules_met_view_radio")).Count > 0){
                Pages.PS_ModulesSelectMetricPage.PopUpViewRadio.Click();
            }
            
            IList<HtmlInputCheckBox> lineItems = ActiveBrowser.Find.AllByXPath<HtmlInputCheckBox>(AppLocators.get("admin_module_met_line_item_checkbox"));
            
            for(int i=0 ; i < lineItems.Count; i++){
                lineItems[i].Click();
                System.Threading.Thread.Sleep(2000);
             }
            
            //click submit
            Pages.PS_ModulesSelectMetricPage.PopUpSubmitLink.Click(true);
            //Actions.InvokeScript("document.getElementsByClassName(\"submit\")[0].click();");
            System.Threading.Thread.Sleep(5000);
            Manager.WaitForNewBrowserConnect("/admin/module_wizard.jsp", true,Manager.Settings.ElementWaitTimeout);
            
            Manager.ActiveBrowser.WaitUntilReady();
            Pages.PS_ModuleWizardPage.OptionsSubmitLink.Wait.ForExists();
            
            //submit OPTIONS Tab 
            Pages.PS_ModuleWizardPage.OptionsSubmitLink.Click();
            Manager.ActiveBrowser.WaitUntilReady();
            Pages.PS_AdminModulesPage.ModulesListContainerTable.Wait.ForExists();
            Pages.PS_AdminModulesPage.ModuleTypeTableCell.Wait.ForVisible();
        
            
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MET_007_CodedStep17()
        {
                        ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL").ToString());
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that metric details are displayed on summary page with entered line item values")]
        public void TST_MET_013_CodedStep4()
        {
                        //verify financial summary links
                        Pages.PS_ProjectSummaryPage.FinancialSummaryLink.Wait.ForExists();
                        Pages.PS_ProjectSummaryPage.MetricSummaryIconImg.Wait.ForVisible();
                    
                        //click financial summary link
                        Pages.PS_ProjectSummaryPage.FinancialSummaryLink.Click();
            
                        //verify that financial summary section is visible
                        Pages.PS_ProjectSummaryPage.MetricSummaryDiv.Wait.ForExists();
                        Pages.PS_ProjectSummaryPage.MetricSummaryDiv.Wait.ForVisible();
                        
                        //verify all metric details like metric name, view, line items etc...                    
                        string financialSummaryStr = Pages.PS_ProjectSummaryPage.MetricSummaryDiv.InnerText;
                        Log.WriteLine(financialSummaryStr);
            
                        string metricName = GetExtractedValue("GeneratedMetricName").ToString();
                        Assert.IsTrue(financialSummaryStr.Contains(metricName),"Financial Summary should contain metric name");
            
                        string viewName = Data["ViewName1"].ToString();
                        Assert.IsTrue(financialSummaryStr.Contains(viewName),"Financial Summary should contain View information");
            
                        Assert.IsTrue(financialSummaryStr.Contains("Grand Total"),"Financial Summary should contain track record information");
            
                        int lineItemCount = Int32.Parse(Data["TotalLineItems"].ToString()); 
                        for(int i = 1; i <=lineItemCount;i++){
               
                            string lineItemName = Data["LineItemName"+i].ToString();
                            Assert.IsTrue(financialSummaryStr.Contains(lineItemName),"Financial Summary should contain Line Item "+lineItemName);
                            
                        }
                        
                        Assert.IsTrue(financialSummaryStr.Contains("100"),"Financial Summary should contain entered line item value");
            
        }
    
        [CodedStep(@"Open attached template page from left navigation menu")]
        public void TST_MET_007_CodedStep3()
        {
                        string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
                        HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
                        menuItem.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_007_CodedStep4()
        {
                        string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
                        Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
                        Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
                        Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
                        
        }
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_007_CodedStep14()
        {
                        Pages.PS_MetricInstancePage.InfoLink.Wait.ForExists();
                        Pages.PS_MetricInstancePage.InfoLink.Click();
                        
                        Manager.WaitForNewBrowserConnect("/metrics/Template.epage", true,Manager.Settings.ElementWaitTimeout);
                        Manager.ActiveBrowser.WaitUntilReady();
                        Manager.ActiveBrowser.Window.Maximize();
                        
                        
                        Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Wait.ForExists();
                        Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        
                        
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_007_CodedStep15()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
                        
        }
    
        [CodedStep(@"Delete given metric template and close the popup")]
        public void TST_MET_007_CodedStep16()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.Close();
        }
    
        [CodedStep(@"Remove metric from module wizard page")]
        public void TST_MET_013_CodedStep5()
        {
            
            HtmlAnchor metricSelectLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//td[@class='titleBold']//a");
            metricSelectLink.Wait.ForExists();
            metricSelectLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
            //submit OPTIONS Tab 
            Pages.PS_ModuleWizardPage.OptionsSubmitLink.Click();
            Manager.ActiveBrowser.WaitUntilReady();
            Pages.PS_AdminModulesPage.ModulesListContainerTable.Wait.ForExists();
            Pages.PS_AdminModulesPage.ModuleTypeTableCell.Wait.ForVisible();
        }
    
        [CodedStep(@" Enter some data(100) in first cell and save")]
        public void TST_MET_012_CodedStep3()
        {
                        HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
                        editableCell.Click();
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
                        
                        Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"100");
                        
                        System.Threading.Thread.Sleep(3000);
                        Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
                        System.Threading.Thread.Sleep(3000);
                        
                        Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
                       // Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
                        System.Threading.Thread.Sleep(3000);
        }
    }
}
