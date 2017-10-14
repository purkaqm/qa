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

    public class TST_MET_011 : BaseWebAiiTest
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
        public void TST_MET_011_CodedStep()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
            
            
        }
    
        [CodedStep(@"Store Project URL")]
        public void TST_MET_011_CodedStep1()
        {
            SetExtractedValue("ProjectURL", ActiveBrowser.Url);
        }
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_011_CodedStep2()
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
        public void TST_MET_011_CodedStep3()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
            Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(7000);
            ActiveBrowser.RefreshDomTree();
            
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_011_CodedStep4()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Log.WriteLine(elementLocator);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(leftMenuItemLocator).Count > 0,"Attached template should be displayed in the left navigation menu");
        }
    
        [CodedStep(@"Open attached template page from left navigation menu")]
        public void TST_MET_011_CodedStep5()
        {
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
            HtmlDiv menuItem = ActiveBrowser.Find.ByXPath<HtmlDiv>(leftMenuItemLocator);
            menuItem.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that user is navigated to template page")]
        public void TST_MET_011_CodedStep6()
        {
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricInstancePage.MetricTemplateBoldName.BaseElement.InnerText.Contains(attachedTemplate),"Template name should be displayed in Bold letters with info Link");
            
        }
    
        //[CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters x R y format data")]
        //public void TST_MET_011_CodedStep7()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //editableCell.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            //Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"10 R 3");
            
            ////verify save and reset buttons are grayed out(disabled)
            
        //}
    
        //[CodedStep(@"Verify that Save and Reset buttons are grayed out")]
        //public void TST_MET_011_CodedStep8()
        //{
            //string[] grayedSaveAttr = new String[]{"class","btn btn-disabled"};
            //string[] grayedResetAttr = new String[]{"class","btn-white btn-disabled"};
            //Pages.PS_MetricInstancePage.SaveChangesBtn.Wait.ForAttributes(grayedSaveAttr);
            //Pages.PS_MetricInstancePage.ResetBtn.Wait.ForAttributes(grayedResetAttr);
            
        //}
    
        //[CodedStep(@"Click anywhere else")]
        //public void TST_MET_011_CodedStep9()
        //{
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Verify that cell color is changed to yellow")]
        //public void TST_MET_011_CodedStep10()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            //string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            //editableCell.Wait.ForAttributes(attrToVerify);
            //editableCell.Wait.ForStyles(styleToVerify);
            
        //}
    
        //[CodedStep(@"Click Reset button and see that entered text is disappears")]
        //public void TST_MET_011_CodedStep11()
        //{
            //Pages.PS_MetricInstancePage.ResetBtn.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            //System.Threading.Thread.Sleep(5000);
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //Assert.IsTrue(editableCell.InnerText.Equals(""),"Text should be disappeared and nothing should be displayed in the cell");
        //}
    
        //[CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters x R y format data")]
        //public void TST_MET_011_CodedStep12()
        //{
            //HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //editableCell.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            //Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"10 R 3");
            
            ////verify save and reset buttons are grayed out(disabled)
            
        //}
    
        //[CodedStep(@"Click anywhere else")]
        //public void TST_MET_011_CodedStep13()
        //{
            //System.Threading.Thread.Sleep(3000);
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            //System.Threading.Thread.Sleep(3000);
        //}
    
        //[CodedStep(@"Click Save button and see that entered text is displayed into the cell")]
        //public void TST_MET_011_CodedStep14()
        //{
            //Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            //Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            //System.Threading.Thread.Sleep(7000);
            //ActiveBrowser.RefreshDomTree();
            
            //HtmlTableCell editableCell1 = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            //string cellValue1 = editableCell1.BaseElement.InnerText.ToString();
            //Assert.IsTrue(cellValue1.Contains("10"),"Text should be displayed as entered in the cell");
            
            //HtmlTableCell editableCell2 = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[3]");
            //string cellValue2 = editableCell2.BaseElement.InnerText.ToString();
            //Assert.IsTrue(cellValue2.Contains("10"),"Text should be displayed as entered in the cell");
            
            //HtmlTableCell editableCell3 = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            //string cellValue3 = editableCell3.BaseElement.InnerText.ToString();
            //Assert.IsTrue(cellValue3.Contains("10"),"Text should be displayed as entered in the cell");
        //}
    
        //[CodedStep(@"Verify that Show Versions link is displayed")]
        //public void TST_MET_011_CodedStep15()
        //{
            //Pages.PS_MetricInstancePage.ShowVersionsLink.Wait.ForExists();
            //Assert.IsTrue(Pages.PS_MetricInstancePage.ShowVersionsLink.IsVisible(), "Show versions link should be enabled");
            
            
        //}
    
        //[CodedStep(@"Click Show Versions link and check that version information is diplayed")]
        //public void TST_MET_011_CodedStep16()
        //{
            //Pages.PS_MetricInstancePage.ShowVersionsLink.Click();
            //Pages.PS_MetricInstancePage.MetricVersionsDiv.Wait.ForVisible();
            //string versionInfo = Pages.PS_MetricInstancePage.MetricVersionsDiv.InnerText;
            //Assert.IsTrue(versionInfo.Contains("Version #1"),"Versions should start reflecting with Version #1");
        //}
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_011_CodedStep17()
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
        public void TST_MET_011_CodedStep18()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            
        }
    
        [CodedStep(@"Delete given metric template and close the popup")]
        public void TST_MET_011_CodedStep19()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.Close();
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MET_011_CodedStep20()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    }
}
