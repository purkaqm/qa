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

    public class TST_MET_009 : BaseWebAiiTest
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
        public void TST_MET_009_CodedStep()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
            
            
        }
    
        [CodedStep(@"Store Project URL")]
        public void TST_MET_009_CodedStep1()
        {
            SetExtractedValue("ProjectURL", ActiveBrowser.Url);
        }
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_009_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
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
        }
    
        [CodedStep(@"Select any Metric Template from drop down")]
        public void TST_MET_009_CodedStep3()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
            
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_009_CodedStep4()
        {
            ActiveBrowser.RefreshDomTree();
            //System.Threading.Thread.Sleep(3000);
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Log.WriteLine(elementLocator);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"Open attached template page from left navigation menu")]
        public void TST_MET_009_CodedStep5()
        {
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
            HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
            menuItem.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_009_CodedStep6()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '100' into it")]
        public void TST_MET_009_CodedStep7()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"150");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_009_CodedStep9()
        {
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            
        }
    
      
    
        [CodedStep(@"Click Save button and see that entered text is displayed into the cell")]
        public void TST_MET_009_CodedStep14()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(7000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Log.WriteLine(cellValue);
            Assert.IsTrue(cellValue.Contains("50"),"Text should be displayed as entered in the cell");
        }
    
       
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_009_CodedStep23()
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
        public void TST_MET_009_CodedStep24()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            
        }
    
        [CodedStep(@"Delete given metric template and close the popup")]
        public void TST_MET_009_CodedStep25()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.Close();
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MET_009_CodedStep26()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    
        
    
        [CodedStep(@"Verify Metric Template Page is opened")]
        public void TST_MET_009_CodedStep36()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        }
    
        [CodedStep(@"Click Create New link")]
        public void TST_MET_009_CodedStep39()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForVisible();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep40()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Wait for Display Tab")]
        public void TST_MET_009_CodedStep41()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.DisplayTabTemplateNameTD.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TotalsDisplayedSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep42()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Wait for View Tab")]
        public void TST_MET_009_CodedStep43()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewAddLineItemsLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewCostMappingSelect.Wait.ForVisible();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep44()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep45()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep46()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Wait for Computation Tab")]
        public void TST_MET_009_CodedStep47()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TextModeLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ComputationLINameLabel.Wait.ForExists();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_009_CodedStep48()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Wait for Finish Tab ")]
        public void TST_MET_009_CodedStep49()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
       
    
        [CodedStep(@"Click Submit button")]
        public void TST_MET_009_CodedStep51()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
               
        }
    
        [CodedStep(@"Verify Summary Page is displayed")]
        public void TST_MET_009_CodedStep52()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_009_CodedStep8()
        {
            Pages.PS_MetricInstancePage.WarningPopUp.Wait.ForVisible();
            string warningMessage = Pages.PS_MetricInstancePage.WarningPopUp.InnerText;
            Log.WriteLine(warningMessage);
            Log.WriteLine(Data["RangeStart1"].ToString());
            Log.WriteLine(Data["RangeEnd1"].ToString());
            
            Assert.IsTrue(warningMessage.Contains("must be between") && warningMessage.Contains(Data["RangeStart1"].ToString()) && warningMessage.Contains(Data["RangeEnd1"].ToString()),"Range indiation message pop up should be displayed");
            Pages.PS_MetricInstancePage.WarningPopUpOKBtn.Click();
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters value out of predefined range")]
        public void TST_MET_009_CodedStep10()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"xyz");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_009_CodedStep11()
        {
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            
        }
    
        [CodedStep(@"Verify that tooltip pop up is displayed with range information and click OK button")]
        public void TST_MET_009_CodedStep12()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            
            Pages.PS_MetricInstancePage.WarningPopUp.Wait.ForVisible();
            string warningMessage = Pages.PS_MetricInstancePage.WarningPopUp.InnerText;
            
            Assert.IsTrue(warningMessage.Contains("Invalid entry"),"Range indiation message pop up should be displayed");
            
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters invalid value")]
        public void TST_MET_009_CodedStep13()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"50");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_009_CodedStep15()
        {
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters valid value from predefined range")]
        public void TST_MET_009_CodedStep16()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_009_CodedStep17()
        {
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            
        }
    
        [CodedStep(@"Verify that tooltip pop up is displayed with invalid entry warning message")]
        public void TST_MET_009_CodedStep18()
        {
            
            
            Pages.PS_MetricInstancePage.WarningPopUp.Wait.ForVisibleNot();
            
        }
    }
}
