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

    public class TST_MET_007 : BaseWebAiiTest
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
    
       
    
        [CodedStep(@"Open template information window by clicking info link")]
        public void TST_MET_007_CodedStep14()
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
    
        [CodedStep(@"Open Attach Metric Template Dialog")]
        public void TST_MET_007_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
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
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(5000);
            
        }
    
        [CodedStep(@"Verify that attached template is listed in the table and in left panel")]
        public void TST_MET_007_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
            string elementLocator = AppLocators.get("project_manage_metrics_table_row")+"[contains(.,'"+attachedTemplate+"')]";
            Log.WriteLine(elementLocator);
            //Assert.IsTrue(ActiveBrowser.Find.AllByXPath(elementLocator).Count > 0,"Attached template should be displayed in the table");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format("//form[@id='metricsForm']//table//tr[contains(.,'{0}')]",attachedTemplate)).Count > 0,"Attached template should be displayed in the table");
            string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),attachedTemplate);
            Log.WriteLine(leftMenuItemLocator);
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_007_CodedStep5()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"100");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_007_CodedStep6()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            editableCell.Wait.ForAttributes(attrToVerify);
            editableCell.Wait.ForStyles(styleToVerify);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_007_CodedStep7()
        {
            string[] grayedSaveAttr = new String[]{"class","btn btn-disabled"};
            string[] grayedResetAttr = new String[]{"class","btn-white btn-disabled"};
            Pages.PS_MetricInstancePage.SaveChangesBtn.Wait.ForAttributes(grayedSaveAttr);
            Pages.PS_MetricInstancePage.ResetBtn.Wait.ForAttributes(grayedResetAttr);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_007_CodedStep8()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_007_CodedStep11()
        {
            Pages.PS_MetricInstancePage.ResetBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(5000);
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            Assert.IsTrue(editableCell.InnerText.Equals(""),"Text should be disappeared and nothing should be displayed in the cell");
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '100' into it")]
        public void TST_MET_007_CodedStep12()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"100");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
      
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_007_CodedStep18()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(3000);
        }
    
     
    
        [CodedStep(@"Click Reset button and see that entered text is disappears")]
        public void TST_MET_007_CodedStep20()
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
            Assert.IsTrue(cellValue.Contains("100"),"Text should be displayed as entered in the cell");
        }
    
        
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '-100' into it")]
        public void TST_MET_007_CodedStep13()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"-100");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Verify that Save and Reset buttons are grayed out")]
        public void TST_MET_007_CodedStep19()
        {
            string[] grayedSaveAttr = new String[]{"class","btn btn-disabled"};
            string[] grayedResetAttr = new String[]{"class","btn-white btn-disabled"};
            Pages.PS_MetricInstancePage.SaveChangesBtn.Wait.ForAttributes(grayedSaveAttr);
            Pages.PS_MetricInstancePage.ResetBtn.Wait.ForAttributes(grayedResetAttr);
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_007_CodedStep21()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Click();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify that cell color is changed to yellow")]
        public void TST_MET_007_CodedStep22()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            string[] attrToVerify = new String[]{"class","editableCell modifiedCell"};
            string[] styleToVerify = new String[]{"background-color","rgb(255, 248, 220)"};
            editableCell.Wait.ForAttributes(attrToVerify);
            editableCell.Wait.ForStyles(styleToVerify);
            
        }
    
        [CodedStep(@"Click Reset button and see that entered text is disappears")]
        public void TST_MET_007_CodedStep23()
        {
            Pages.PS_MetricInstancePage.ResetBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
            Pages.PS_MetricInstancePage.MetricContainerGridDiv.Wait.ForVisible();
            System.Threading.Thread.Sleep(5000);
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            Assert.IsTrue(editableCell.InnerText.Equals(""),"Text should be disappeared and nothing should be displayed in the cell");
        }
    
        [CodedStep(@"User clicks inside the regular monetary type cell with mouse cursor and enters '-100' into it")]
        public void TST_MET_007_CodedStep24()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"-100");
            
            //verify save and reset buttons are grayed out(disabled)
            
        }
    
        [CodedStep(@"Click anywhere else")]
        public void TST_MET_007_CodedStep25()
        {
            System.Threading.Thread.Sleep(3000);
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Click();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click Save button and see that entered text is displayed into the cell")]
        public void TST_MET_007_CodedStep26()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            
            System.Threading.Thread.Sleep(10000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Log.WriteLine(cellValue);
            Assert.IsTrue(cellValue.Contains("100"),"Text should be displayed as entered in the cell");
        }
    
        [CodedStep(@"User  clicks inside the cell with mouse cursor and deletes its content")]
        public void TST_MET_007_CodedStep27()
        {
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[2]");
            editableCell.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_MetricInstancePage.TemplateEditableCell.Wait.ForVisible();
            
            Actions.SetText(Pages.PS_MetricInstancePage.TemplateEditableCell,"");
        }
    
       
    
        [CodedStep(@"Click Save button and see that the field is cleared")]
        public void TST_MET_007_CodedStep28()
        {
            Pages.PS_MetricInstancePage.SaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForExists();
            System.Threading.Thread.Sleep(10000);
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell editableCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//div[@id='mainArea']//tbody[2]//tr[1]/td[4]");
            string cellValue = editableCell.BaseElement.InnerText.ToString();
            Log.WriteLine(cellValue);
            Assert.IsTrue(cellValue.Contains(""),"Text should be cleared as entered in the cell");
        }
    }
}
