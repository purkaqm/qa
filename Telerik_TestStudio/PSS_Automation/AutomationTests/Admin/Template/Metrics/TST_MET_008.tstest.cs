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

    public class TST_MET_008 : BaseWebAiiTest
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
        public void TST_MET_008_CodedStep()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
            
            
        }
    
        [CodedStep(@"Store Project URL")]
        public void TST_MET_008_CodedStep1()
        {
            SetExtractedValue("ProjectURL", ActiveBrowser.Url);
        }
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_008_CodedStep2()
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
        public void TST_MET_008_CodedStep3()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(5000);
            
            
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_008_CodedStep4()
        {
            ActiveBrowser.RefreshDomTree();
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Log.WriteLine(elementLocator);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"Open attached template page from left navigation menu")]
        public void TST_MET_008_CodedStep5()
        {
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
            HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
            menuItem.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_008_CodedStep6()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '100' into it")]
        public void TST_MET_008_CodedStep7()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateSetListCell.Wait.ForExists();
            ActiveBrowser.RefreshDomTree();
            HtmlDiv setDropdown = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_form_ComboBox_0_popup']");
            setDropdown.Wait.ForVisible();
            int dropDownOptioncount = setDropdown.Find.AllByXPath("//div[@class='dijitReset dijitMenuItem']").Count;
            Log.WriteLine(dropDownOptioncount.ToString());
            int setItems = Int32.Parse(Data["SetItemOptions1"].ToString()); 
            Log.WriteLine(setItems.ToString());
            Assert.IsTrue(dropDownOptioncount == (setItems+1),"All set options with one additional option 'None' should be displayed in the list");
            
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Verify that Save and Reset buttons are grayed out")]
        public void TST_MET_008_CodedStep8()
        {
            HtmlDiv setDropdown = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_form_ComboBox_0_popup']");
            string optionLocator = string.Format("/div[contains(.,'{0}')]",Data["SetOptionToSelect"].ToString());
            HtmlDiv optionToSelect = setDropdown.Find.ByXPath<HtmlDiv>(optionLocator);
            Manager.Desktop.Mouse.Click(MouseClickType.LeftClick,optionToSelect.BaseElement.GetRectangle());
            //optionToSelect.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_008_CodedStep9()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(5000);
            
        }
    
        [CodedStep(@"Verify that cell color is changed to yellow")]
        public void TST_MET_008_CodedStep10()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            editableCell.Wait.ForAttributes(attrToVerify);
            editableCell.Wait.ForStyles(styleToVerify);
            
        }
    
        [CodedStep(@"Click Reset button and see that entered text is disappears")]
        public void TST_MET_008_CodedStep11()
        {
            Pages.PS_MetricInstancePage.ResetBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(5000);
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            Assert.IsTrue(editableCell.InnerText.Equals(""),"Text should be disappeared and nothing should be displayed in the cell");
        }
    
        //[CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '100' into it")]
        //public void TST_MET_008_CodedStep12()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //editableCell.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            //Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"100");
            
            ////verify save and reset buttons are grayed out(disabled)
            
        //}
    
        //[CodedStep(@"Click anywhere else")]
        //public void TST_MET_008_CodedStep13()
        //{
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Click();
            
        //}
    
        [CodedStep(@"Click Save button and see that entered text is displayed into the cell")]
        public void TST_MET_008_CodedStep14()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(10000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Log.WriteLine(cellValue);
            
            Assert.IsTrue(cellValue.Contains(Data["SetOptionToSelect"].ToString()) || cellValue.Contains("Ten"),"Text should be displayed as entered in the cell");
        }
    
        //[CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '-100' into it")]
        //public void TST_MET_008_CodedStep15()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //editableCell.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            //Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"-100");
            
            ////verify save and reset buttons are grayed out(disabled)
            
        //}
    
        //[CodedStep(@"Verify that Save and Reset buttons are grayed out")]
        //public void TST_MET_008_CodedStep16()
        //{
            //string[] grayedSaveAttr = new String[]{"class","btn btn-disabled"};
            //string[] grayedResetAttr = new String[]{"class","btn-white btn-disabled"};
            //Pages.PS_MetricInstancePage.SaveChangesBtn.Wait.ForAttributes(grayedSaveAttr);
            //Pages.PS_MetricInstancePage.ResetBtn.Wait.ForAttributes(grayedResetAttr);
            
        //}
    
        //[CodedStep(@"Click anywhere else")]
        //public void TST_MET_008_CodedStep17()
        //{
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Click();
            
        //}
    
        //[CodedStep(@"Verify that cell color is changed to yellow")]
        //public void TST_MET_008_CodedStep18()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            //string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            //editableCell.Wait.ForAttributes(attrToVerify);
            //editableCell.Wait.ForStyles(styleToVerify);
            
        //}
    
        //[CodedStep(@"Click Reset button and see that entered text is disappears")]
        //public void TST_MET_008_CodedStep19()
        //{
            //Pages.PS_MetricInstancePage.ResetBtn.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            //System.Threading.Thread.Sleep(5000);
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //Assert.IsTrue(editableCell.InnerText.Equals(""),"Text should be disappeared and nothing should be displayed in the cell");
        //}
    
        //[CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '100' into it")]
        //public void TST_MET_008_CodedStep20()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //editableCell.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            //Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"-100");
            
            ////verify save and reset buttons are grayed out(disabled)
            
        //}
    
        //[CodedStep(@"Click anywhere else")]
        //public void TST_MET_008_CodedStep21()
        //{
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Click();
            
        //}
    
        //[CodedStep(@"Click Save button and see that entered text is displayed into the cell")]
        //public void TST_MET_008_CodedStep22()
        //{
            //Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            //System.Threading.Thread.Sleep(10000);
            //ActiveBrowser.RefreshDomTree();
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //string cellValue = editableCell.BaseElement.InnerText.ToString();
            //Log.WriteLine(cellValue);
            //Assert.IsTrue(cellValue.Contains("100"),"Text should be displayed as entered in the cell");
        //}
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_008_CodedStep23()
        {
            Pages.PS_MetricInstancePage.InfoLink.Wait.ForVisible();
            Pages.PS_MetricInstancePage.InfoLink.Click();
            
            Manager.WaitForNewBrowserConnect("/metrics/Template.epage", true,Manager.Settings.ElementWaitTimeout);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            
            
            Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Wait.ForVisible();
            Pages.PS_MetricTemplateInfoPage.EditTemplateLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_008_CodedStep24()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            
        }
    
        [CodedStep(@"Delete given metric template and close the popup")]
        public void TST_MET_008_CodedStep25()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.Close();
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MET_008_CodedStep26()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click Create New link")]
        public void TST_MET_008_CodedStep27()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForVisible();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep28()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep29()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep30()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep31()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep32()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_008_CodedStep33()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Wait for Finish Tab ")]
        public void TST_MET_008_CodedStep34()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
        //[CodedStep(@"Verify Finish tab information displayed as a Preview")]
        //public void TST_MET_008_CodedStep35()
        //{
            //HtmlTableCell basicInfoTabDetails = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//table[@class='bgWhite']/tbody/tr[1]/td[1]");
            //string basicInfoDetailsStr = basicInfoTabDetails.InnerText;
            
            //Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["MetricName"].ToString().ToLower()), "Metric Name should be displayed in Preview under Finish Tab");
            //Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["Description"].ToString().ToLower()), "Metric Description should be displayed in Preview under Finish Tab");
            
            
            //HtmlTableCell rollUpCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Allow Rollup')]]");
            //Assert.IsTrue(rollUpCell.InnerText.ToLower().Contains(Data["AllowRollUp"].ToString().ToLower()), "Allow Rollup info should be displayed in Preview under Finish Tab");
            
             //HtmlTableCell frequencyCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Frequency Is')]]");
             //if(!Data["Frequency"].ToString().ToLower().Contains("infinitely")){
                     //Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains(Data["Frequency"].ToString().ToLower()), "Frequency info should be displayed in Preview under Finish Tab");
             //}else{
                    //Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains("no frequency"), "Frequency info should be displayed in Preview under Finish Tab");
             //} 
            
            //HtmlTableCell sendReminderCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Send Reminders')]]");
            //Assert.IsTrue(sendReminderCell.InnerText.ToLower().Contains(Data["SendReminder"].ToString().ToLower()), "Send Reminder info should be displayed in Preview under Finish Tab");
            
              //HtmlTableCell versionControlCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Version Control')]]");
                        //if(Data["VersionControl"].ToString().ToLower().Contains("change")){
                            //Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("version on change"), "Version Control info should be displayed in Preview under Finish Tab");
                        //}else{
                               //Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("none"), "Version Control info should be displayed in Preview under Finish Tab");
                        //}
            
            
            //IList<HtmlTableRow> lineItemRowsInPreview = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlTableRow>("//table[@class='bgWhite']/tbody/tr[2]//table[@class='bgDark']/tbody/tr");
            //int enteredLineItemCount = Int32.Parse(Data["TotalLineItems"].ToString());
            //Assert.IsTrue(lineItemRowsInPreview.Count == enteredLineItemCount + 1, "Totals line item count displayed should match with line items entered");
            //for(int j = 1;j<lineItemRowsInPreview.Count;j++){
                //string currentRowStr = lineItemRowsInPreview[j].InnerText.ToLower();
                //Log.WriteLine(currentRowStr);
                //Log.WriteLine(Data["LineItemName"+j].ToString().ToLower());
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemName"+j].ToString().ToLower()), "Line Item Name should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemDesc"+j].ToString().ToLower()), "Line Item Description should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemSequence"+j].ToString().ToLower()), "Line Item Sequence should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemDataType"+j].ToString().ToLower()), "Line Item Type should be displayed as Entered");
                //if(Data["LineItemDataType"+j].ToString().ToLower().Contains("cost") || Data["LineItemDataType"+j].ToString().ToLower().Contains("separator")){
                    //continue;
                //}
                //if(Data["LineItemRollUp"+j].ToString().ToLower().Contains("yes")){
                    //Assert.IsTrue(currentRowStr.Contains(Data["LineItemRollUp"+j].ToString().ToLower()), "Line Item RollUp should be displayed as Entered");
                //}
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemBehavior"+j].ToString().ToLower()), "Line Item Behavior should be displayed as Entered");
                //if(Data["LineItemBehavior"+j].ToString().ToLower().Contains("static") || Data["LineItemConstraint"+j].ToString().ToLower().Contains("none")){
                    //continue;
                //}
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemConstraint"+j].ToString().ToLower()), "Line Item Constraint should be displayed as Entered");
                              
            //}
            
        //}
    
        [CodedStep(@"Click Submit button")]
        public void TST_MET_008_CodedStep36()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
            
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_008_CodedStep37()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
        [CodedStep(@"Verify Metric Template Page is opened")]
        public void TST_MET_008_CodedStep38()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        }
    
        [CodedStep(@"Wait for Display Tab")]
        public void TST_MET_008_CodedStep39()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Wait for View Tab")]
        public void TST_MET_008_CodedStep40()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewAddLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Wait for Computation Tab")]
        public void TST_MET_008_CodedStep41()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ComputationLINameLabel.Wait.ForExists();
        }
    
        [CodedStep(@"User clicks inside a Set constraint cell and verify all set option with None options are displayed")]
        public void TST_MET_008_CodedStep15()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateSetListCell.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            HtmlDiv setDropdown = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_form_ComboBox_0_popup']");
            setDropdown.Wait.ForVisible();
            
        }
    
        [CodedStep(@"Select Any Option")]
        public void TST_MET_008_CodedStep16()
        {
            HtmlDiv setDropdown = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_form_ComboBox_0_popup']");
            string optionLocator = string.Format("/div[contains(.,'{0}')]",Data["SetOptionToSelect"].ToString());
            HtmlDiv optionToSelect = setDropdown.Find.ByXPath<HtmlDiv>(optionLocator);
           Manager.Desktop.Mouse.Click(MouseClickType.LeftClick,optionToSelect.BaseElement.GetRectangle());
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_008_CodedStep17()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(5000);
            
        }
    
        [CodedStep(@"Verify that cell color is changed to yellow")]
        public void TST_MET_008_CodedStep18()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            editableCell.Wait.ForAttributes(attrToVerify);
            editableCell.Wait.ForStyles(styleToVerify);
            
        }
    
        [CodedStep(@"User clicks inside a Set constraint cell")]
        public void TST_MET_008_CodedStep12()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateSetListCell.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            HtmlDiv setDropdown = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_form_ComboBox_0_popup']");
            setDropdown.Wait.ForVisible();
            
        }
    
        [CodedStep(@"Select Any Option")]
        public void TST_MET_008_CodedStep13()
        {
            Pages.PS_MetricInstancePage.TemplateSetListCell.MouseClick(MouseClickType.LeftClick);
            Pages.PS_MetricInstancePage.TemplateSetListCell.Focus();
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateSetListCell,"\n");
            
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_008_CodedStep19()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(5000);
            
        }
    
        [CodedStep(@"Verify that cell color is changed to yellow")]
        public void TST_MET_008_CodedStep20()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            editableCell.Wait.ForAttributes(attrToVerify);
            editableCell.Wait.ForStyles(styleToVerify);
            Assert.IsTrue(editableCell.InnerText.Equals(""),"None should be disappeared and nothing should be displayed in the cell");
        }
    }
}
