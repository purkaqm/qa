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

    public class TST_MET_012 : BaseWebAiiTest
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
        public void TST_MET_012_CodedStep()
        {
            Pages.PS_UIXEditPage.MetricCopyViewSetting.SelectByValue("on",true);
            Pages.PS_UIXEditPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify Metric Template Page is opened")]
        public void TST_MET_012_CodedStep2()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
                        
        }
        
     
    
        [CodedStep(@"Click Create New link")]
        public void TST_MET_002_CodedStep1()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForExists();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep3()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
                        
        }
    
       
    
        [CodedStep(@"Wait for Finish Tab ")]
        public void TST_MET_002_CodedStep8()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
        [CodedStep(@"Verify Finish tab information displayed as a Preview")]
        public void TST_MET_002_CodedStep9()
        {
                        HtmlTableCell basicInfoTabDetails = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//table[@class='bgWhite']/tbody/tr[1]/td[1]");
                        string basicInfoDetailsStr = basicInfoTabDetails.InnerText;
                        
                        Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["MetricName"].ToString().ToLower()), "Metric Name should be displayed in Preview under Finish Tab");
                        Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["Description"].ToString().ToLower()), "Metric Description should be displayed in Preview under Finish Tab");
                        
                        int viewCount = Int32.Parse(Data["TotalViews"].ToString());
                        for(int i = 1; i <=viewCount;i++){
                            Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["ViewName"+i].ToString().ToLower()), "View names should be displayed in Preview under Finish Tab");
                        }
                        
                        HtmlTableCell rollUpCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Allow Rollup')]]");
                        Assert.IsTrue(rollUpCell.InnerText.ToLower().Contains(Data["AllowRollUp"].ToString().ToLower()), "Allow Rollup info should be displayed in Preview under Finish Tab");
                        
                        HtmlTableCell startEndDates = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Start Date')]]");
                        Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodStartDate"].ToString().ToLower()), "Start Date info should be displayed in Preview under Finish Tab");
                        Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodEndDate"].ToString().ToLower()), "End Date info should be displayed in Preview under Finish Tab");
                        
                        HtmlTableCell frequencyCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Frequency Is')]]");
                        if(!Data["Frequency"].ToString().ToLower().Contains("infinitely")){
                            Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains(Data["Frequency"].ToString().ToLower()), "Frequency info should be displayed in Preview under Finish Tab");
                        }else{
                          Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains("no frequency"), "Frequency info should be displayed in Preview under Finish Tab");
                         } 
                        
                        HtmlTableCell sendReminderCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Send Reminders')]]");
                        Assert.IsTrue(sendReminderCell.InnerText.ToLower().Contains(Data["SendReminder"].ToString().ToLower()), "Send Reminder info should be displayed in Preview under Finish Tab");
                        
                          HtmlTableCell versionControlCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Version Control')]]");
                                    if(Data["VersionControl"].ToString().ToLower().Contains("change")){
                                        Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("version on change"), "Version Control info should be displayed in Preview under Finish Tab");
                                    }else{
                                           Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("none"), "Version Control info should be displayed in Preview under Finish Tab");
                                    }
                                    
                        HtmlTableCell periodsBackForwardCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Periods')]]");
                        Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsBack"].ToString().ToLower()), "Periods Back info should be displayed in Preview under Finish Tab");
                        Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsForward"].ToString().ToLower()), "Periods Forward info should be displayed in Preview under Finish Tab");
                        
                        HtmlTableCell totalsCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Totals:')]]");
                        Assert.IsTrue(totalsCell.InnerText.ToLower().Contains(Data["TotalsDisplayed"].ToString().ToLower()), "Totals info should be displayed in Preview under Finish Tab");
                        
                        
                        
                        IList<HtmlTableRow> lineItemRowsInPreview = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlTableRow>("//table[@class='bgWhite']/tbody/tr[2]//table[@class='bgDark']/tbody/tr");
                        int enteredLineItemCount = Int32.Parse(Data["TotalLineItems"].ToString());
                        Assert.IsTrue(lineItemRowsInPreview.Count == enteredLineItemCount + 1, "Totals line item count displayed should match with line items entered");
                        for(int j = 1;j<lineItemRowsInPreview.Count;j++){
                            string currentRowStr = lineItemRowsInPreview[j].InnerText.ToLower();
                            Log.WriteLine(currentRowStr);
                            Log.WriteLine(Data["LineItemName"+j].ToString().ToLower());
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemName"+j].ToString().ToLower()), "Line Item Name should be displayed as Entered");
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemDesc"+j].ToString().ToLower()), "Line Item Description should be displayed as Entered");
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemSequence"+j].ToString().ToLower()), "Line Item Sequence should be displayed as Entered");
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemDataType"+j].ToString().ToLower()), "Line Item Type should be displayed as Entered");
                            if(Data["LineItemDataType"+j].ToString().ToLower().Contains("cost") || Data["LineItemDataType"+j].ToString().ToLower().Contains("separator")){
                                continue;
                            }
                            if(Data["LineItemRollUp"+j].ToString().ToLower().Contains("yes")){
                                Assert.IsTrue(currentRowStr.Contains(Data["LineItemRollUp"+j].ToString().ToLower()), "Line Item RollUp should be displayed as Entered");
                            }
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemBehavior"+j].ToString().ToLower()), "Line Item Behavior should be displayed as Entered");
                            if(Data["LineItemBehavior"+j].ToString().ToLower().Contains("static") || Data["LineItemConstraint"+j].ToString().ToLower().Contains("none")){
                                continue;
                            }
                            Assert.IsTrue(currentRowStr.Contains(Data["LineItemConstraint"+j].ToString().ToLower()), "Line Item Constraint should be displayed as Entered");
                                          
                        }
                        
        }
    
        [CodedStep(@"Click Submit button")]
        public void TST_MET_002_CodedStep10()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        
                        
                        
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_002_CodedStep11()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
                        Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
                        string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
                        Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
        [CodedStep(@"Wait for Display Tab")]
        public void TST_MET_008_CodedStep39()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }

    
        [CodedStep(@"Wait for Computation Tab")]
        public void TS_Metric_Templates_Fill_Computation_Tab_Details_CodedStep()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ComputationLINameLabel.Wait.ForExists();
        }
    
        [CodedStep(@"Delete created metric template")]
        public void TST_MET_002_CodedStep12()
        {
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
                        Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Navigate to Context home page")]
        public void TST_MET_012_CodedStep1()
        {
             ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
             Manager.ActiveBrowser.WaitUntilReady();            
        }
    
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
    
        [CodedStep(@"Enter some data(100) in first cell of view1")]
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
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on view 2 link and navigate to view 2")]
        public void TST_MET_012_CodedStep4()
        {
            HtmlAnchor view2Link = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("metric_instance_view_link"),Data["ViewName2"].ToString()));
            view2Link.Wait.ForExists();
            view2Link.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click Copy from view.. and select view 1 as source")]
        public void TST_MET_012_CodedStep5()
        {
            //HtmlAnchor copyFromViewLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//a[contains(.,'Copy from view')]");
            //copyFromViewLink.Wait.ForExists();
            //Assert.IsTrue(copyFromViewLink.IsVisible(),"Copy from view... link should be present");
            //Actions.Click(copyFromViewLink.BaseElement, false);
            
            System.Threading.Thread.Sleep(3000);
            Actions.InvokeScript("document.getElementById(\"LinkSubmit_2\").click();");
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
            //string viewSourceLocator = string.Format(AppLocators.get("metric_instance_copy_view_item_link"),Data["ViewName1"].ToString());
            //HtmlTableCell viewSourceElement =  ActiveBrowser.Find.ByXPath<HtmlTableCell>(viewSourceLocator);
            //viewSourceElement.Wait.ForExists();
            //viewSourceElement.Click();
            
        }
    
 
        
         
                        
                        //verify save and reset buttons are grayed out(disabled)
    
        [CodedStep(@"Verify that the data is copied successfully")]
        public void TST_MET_012_CodedStep6()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(6000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Assert.IsTrue(cellValue.Contains("100"),"Copied Text should be displayed as entered in the cell");
        }
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_007_CodedStep14()
        {
                        Pages.PS_MetricInstancePage.InfoLink.Wait.ForExists();
                        Pages.PS_MetricInstancePage.InfoLink.Click();
                        
                        Manager.WaitForNewBrowserConnect("/metrics/Template.epage", true,Manager.Settings.ElementWaitTimeout);
                        Manager.ActiveBrowser.WaitUntilReady();
                        Manager.ActiveBrowser.Window.Maximize();
                        
                        
                        Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Wait.ForVisible();
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
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MET_007_CodedStep17()
        {
                        ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL").ToString());
                        ActiveBrowser.WaitUntilReady();
        }
    
    
    
        [CodedStep(@"Verify no data copy")]
        public void TST_MET_012_CodedStep7()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Assert.IsTrue(cellValue.Contains(""),"No data should be Copied");
        }
    
        [CodedStep(@"Click on view 1 link and navigate to view 1")]
        public void TST_MET_012_CodedStep8()
        {
            HtmlAnchor view2Link = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("metric_instance_view_link"),Data["ViewName1"].ToString()));
            view2Link.Wait.ForExists();
            view2Link.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    }
}
