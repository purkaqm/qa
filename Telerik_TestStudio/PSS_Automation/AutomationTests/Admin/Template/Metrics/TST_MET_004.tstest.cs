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

    public class TST_MET_004 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify the financial page ")]
        public void TST_MET_002_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForVisible();
        }
    
        //[CodedStep(@"Enter text 'test' in 'ObjectNameText'")]
        //public void TST_MET_002_CodedStep2()
        //{
            
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_002_CodedStep6()
        //{
            //System.Threading.Thread.Sleep(5000);
        //}
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_002_CodedStep12()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            //ActiveBrowser.WaitUntilReady();
        //}
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_004_CodedStep()
        {
            
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
        public void TST_MET_004_CodedStep1()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_004_CodedStep2()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"Open attached template page from left navigation menu")]
        public void TST_MET_004_CodedStep3()
        {
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
            HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
            menuItem.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_004_CodedStep4()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep5()
        {
            Pages.PS_MetricInstancePage.EditPropertiesLink.Wait.ForVisible();
            Pages.PS_MetricInstancePage.EditPropertiesLink.Click();
            Pages.PS_MetricInstancePage.EditPropertiesPopupDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep6()
        {
            Pages.PS_MetricInstancePage.EditPropsBeneficiariesTabSpan.Wait.ForVisible();
            Pages.PS_MetricInstancePage.EditPropsBeneficiariesTabSpan.Click();
            Pages.PS_MetricInstancePage.SponsorTableHeader.Wait.ForVisible();
            Pages.PS_MetricInstancePage.BeneficiariesTableHeader.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep7()
        {
            Pages.PS_MetricInstancePage.EditPropsBeneficiariesSelectorDiv.Click();
            Pages.PS_MetricInstancePage.PrePopulatedTagHeader.Wait.ForVisible();
            
            
            Assert.IsTrue(Pages.PS_MetricInstancePage.PrePopulatedTagHeader.BaseElement.InnerText.Equals(GetExtractedValue("TagBreakdown").ToString()),"Selected Tag name should be displayed as option in Beneficiaries");
            
            Element labelItem = ActiveBrowser.Find.ByXPath("//div[@class='psMultiDiv']//div[@class='dijitTreeNode'][1]//label");
            string beneficiariesToSelect = labelItem.InnerText; 
            
            
            Pages.PS_MetricInstancePage.EditPropsSponsorInput.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.SponsorsListContainer.Wait.ForVisible();
            
            HtmlDiv sponsorDivElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format("//div[contains(@class,'lbl') and contains(.,'{0}')]",beneficiariesToSelect));
            sponsorDivElement.MouseClick(MouseClickType.LeftClick);
           
            Pages.PS_MetricInstancePage.EditPropsBeneficiariesSelectorDiv.Click();
            Pages.PS_MetricInstancePage.PrePopulatedTagHeader.Wait.ForVisible();
            
            HtmlInputCheckBox beneficiaryCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>("//div[@class='psMultiDiv']//div[@class='dijitTreeNode'][1]//input");
            beneficiaryCheckbox.Click();
            
            Pages.PS_MetricInstancePage.BeneficiariesDoneBtn.Click();
            
            SetExtractedValue("SelectedBeneficiaries",beneficiariesToSelect);
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep8()
        {
           Pages.PS_MetricInstancePage.EditPropsUpdateBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
        }
    

    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep9()
        {
            string metricGridRowLocator = string.Format("//td[@class='metric-child-tag' and contains(.,'{0}')]",GetExtractedValue("SelectedBeneficiaries").ToString());
            int rowCounter = ActiveBrowser.Find.AllByXPath(metricGridRowLocator).Count;
            Assert.IsTrue(rowCounter > 0, "Tag line items should be added in the metric grid");
            int expandableRowCount = ActiveBrowser.Find.AllByXPath("//div[@id='rowHeader']//img[contains(@src,'tree-expand.gif')]").Count;
            Assert.IsTrue(rowCounter ==  expandableRowCount, "Each line item should have + which can be expanded to display tab breakdown row");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_004_CodedStep10()
        {
            Pages.PS_MetricInstancePage.InfoLink.Click();
            
            Manager.WaitForNewBrowserConnect("/metrics/Template.epage", true,Manager.Settings.ElementWaitTimeout);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            
            
            Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Wait.ForVisible();
            Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_004_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            
        }
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_004_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.Close();
        }
    }
}
