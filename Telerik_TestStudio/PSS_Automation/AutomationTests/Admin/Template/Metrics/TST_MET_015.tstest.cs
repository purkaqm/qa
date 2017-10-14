using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.Drawing;
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

    public class TST_MET_015 : BaseWebAiiTest
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
    
        [CodedStep(@"Store Project 1 URL and Powersteering ID")]
        public void TST_MET_011_CodedStep1()
        {
                  string projUrl =  ActiveBrowser.Url;  
                  SetExtractedValue("ProjectURL1", projUrl);
                  SetExtractedValue("ProjectName1", GetExtractedValue("CreatedProjectName").ToString());
                  string projID = projUrl.Split(new string[] { "?sp=U" }, StringSplitOptions.None)[1];
                  SetExtractedValue("ProjectID1", projID);
        }      
    
     
    
     
    
        [CodedStep(@"Attach Metric to Project")]
        public void TST_MET_015_CodedStep()
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
                        
                         string attachedTemplate = GetExtractedValue("GeneratedMetricName").ToString();
                        Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogDropdown.SelectByText(attachedTemplate, true);
                        Pages.PS_ProjectManageMetricsPage.TemplateSelectDialogAttachBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        System.Threading.Thread.Sleep(3000);
                        ActiveBrowser.RefreshDomTree();
                        
                        string leftMenuItemLocator = string.Format(AppLocators.get("left_nav_menu_item_link_div"),GetExtractedValue("GeneratedMetricName").ToString());
                        HtmlControl menuItem = CustomUtils.waitForElementToExist(ActiveBrowser,leftMenuItemLocator,60);
                        menuItem.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, attachedTemplate, ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
                        Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
                        
                                         
        }
    
     
    
        [CodedStep(@"Store Project 2 URL and Powersteering ID")]
        public void TST_MET_015_CodedStep1()
        {
            
            string projUrl =  ActiveBrowser.Url;  
            SetExtractedValue("ProjectURL2", projUrl);
            string projID = projUrl.Split(new string[] { "?sp=U" }, StringSplitOptions.None)[1];
            SetExtractedValue("ProjectName2", GetExtractedValue("CreatedProjectName").ToString());
            SetExtractedValue("ProjectID2", projID);
        }
    
        [CodedStep(@"Create text file with all valid data")]
        public void TST_MET_015_CodedStep2()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\Generated\\validmetricloader.txt";
        
            string proj1Name = GetExtractedValue("ProjectName1").ToString();
            string proj1ID = GetExtractedValue("ProjectID1").ToString();
           
            string proj2Name = GetExtractedValue("ProjectName2").ToString();
            string proj2ID = GetExtractedValue("ProjectID2").ToString();
            
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            
            string viewName = Data["ViewName1"].ToString();
            string lineItemName = Data["LineItemName1"].ToString();
            
            string periodHeader = Data["PeriodStartDate"].ToString();
            
            string firstLine = string.Format("Project Name\tProject's Powersteering ID\tProject's Sequence\tMetric template\tView\tBeneficiary\tItem Name\t{0}",periodHeader);
            
            string secondLine = string.Format("{0}\t{1}\t\t{2}\t{3}\t\t{4}\t100",proj1Name,proj1ID, metricName,viewName, lineItemName);
            
            string thirdLine = string.Format("{0}\t{1}\t\t{2}\t{3}\t\t{4}\t100",proj2Name,proj2ID, metricName,viewName, lineItemName);
            
            string[] lines = { firstLine, secondLine, thirdLine };
       
            System.IO.File.WriteAllLines(newFilePath, lines);
            
            SetExtractedValue("ValidFilePath",newFilePath);
        }
    
        [CodedStep(@"Create text file with wrong metric name")]
        public void TST_MET_015_CodedStep3()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\Generated\\wrongtemplatenamemetricloader.txt";
            
            string proj1Name = GetExtractedValue("ProjectName1").ToString();
            string proj1ID = GetExtractedValue("ProjectID1").ToString();
           
           
            
            string metricName = "WrongTemplate";
            
            string viewName = Data["ViewName1"].ToString();
            string lineItemName = Data["LineItemName1"].ToString();
            
            string periodHeader = Data["PeriodStartDate"].ToString();
            
            string firstLine = string.Format("Project Name\tProject's Powersteering ID\tProject's Sequence\tMetric template\tView\tBeneficiary\tItem Name\t{0}",periodHeader);
            
            string secondLine = string.Format("{0}\t{1}\t\t{2}\t{3}\t\t{4}\t100",proj1Name,proj1ID, metricName,viewName, lineItemName);
            
           string[] lines = { firstLine, secondLine};
       
            System.IO.File.WriteAllLines(newFilePath, lines);
            
            SetExtractedValue("WrongTemplateFilePath",newFilePath);
        }
    
        [CodedStep(@"Create text file with invalid project id and line item name")]
        public void TST_MET_015_CodedStep4()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\Generated\\wrongprojidlineitemmetricloader.txt";
            
            string proj1Name = GetExtractedValue("ProjectName1").ToString();
            string proj1ID = "WrongProjID";
           
            
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            
            string viewName = Data["ViewName1"].ToString();
            string lineItemName = "WrongLineItem";
            
            string periodHeader = Data["PeriodStartDate"].ToString();
            
            string firstLine = string.Format("Project Name\tProject's Powersteering ID\tProject's Sequence\tMetric template\tView\tBeneficiary\tItem Name\t{0}",periodHeader);
            
            string secondLine = string.Format("{0}\t{1}\t\t{2}\t{3}\t\t{4}\t100",proj1Name,proj1ID, metricName,viewName, lineItemName);
            
                       
            string[] lines = { firstLine, secondLine };
       
            System.IO.File.WriteAllLines(newFilePath, lines);
            
            SetExtractedValue("WrongProjIDLineItemFilePath",newFilePath);
        }
    
        [CodedStep(@"Create empty file")]
        public void TST_MET_015_CodedStep5()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\Generated\\emptymetricloader.txt";
            
            
            
            string firstLine = "";
            
            string secondLine = "";
            
                       
            string[] lines = { firstLine, secondLine };
       
            System.IO.File.WriteAllLines(newFilePath, lines);
            
            SetExtractedValue("EmptyFilePath",newFilePath);
        }
        
        public void uploadFile(string filePath){
            
          
            ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, filePath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
            
       
            if(ActiveBrowser.BrowserType == BrowserType.Chrome){
                 ActiveBrowser.Window.SetFocus();
                Pages.PS_ReviewLoadFinancialsPage.BrowserBtn.Wait.ForExists();
                Pages.PS_ReviewLoadFinancialsPage.BrowserBtn.Focus();
                Pages.PS_ReviewLoadFinancialsPage.BrowserBtn.MouseHover();
                Manager.Desktop.Mouse.Click(MouseClickType.LeftClick,Pages.PS_ReviewLoadFinancialsPage.BrowserBtn.BaseElement.GetRectangle());
                
                fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
                Manager.Current.DialogMonitor.Stop();
                System.Threading.Thread.Sleep(5000);
                 Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.Wait.ForExists();
                Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.Focus();
                Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.MouseHover();
                Manager.Desktop.Mouse.Click(MouseClickType.LeftClick,Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.BaseElement.GetRectangle());
                ActiveBrowser.WaitUntilReady();
                System.Threading.Thread.Sleep(10000);
            }
            else{
              Actions.InvokeScript("document.querySelector('[value=\"Browse\"]').click();");  
              fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
              Manager.Current.DialogMonitor.Stop();
              System.Threading.Thread.Sleep(5000);
              Actions.InvokeScript("document.querySelector('[value=\"Load\"]').click();");  
              ActiveBrowser.WaitUntilReady();
              System.Threading.Thread.Sleep(10000);
            }
           
           
        }
    
        [CodedStep(@"Upload/Load text file with wrong metric name and verify that warning is displayed")]
        public void TST_MET_015_CodedStep6()
        {
            
            string wrongTemplateFilePath = GetExtractedValue("WrongTemplateFilePath").ToString();
            uploadFile(wrongTemplateFilePath);
            HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//td[@class='msgStrColumnValue last']", 60);
            Assert.IsNotNull(e,"Warning row should be displayed for wrong metric template");
            string problemStr = e.BaseElement.InnerText;
            Log.WriteLine(problemStr);
            Assert.IsTrue(problemStr.Contains("There is no metric template named \"WrongTemplate\""),"Problem text should be as expected for wrong metric tempate");
            
            
        }
    
        [CodedStep(@"Upload/Load text file with invalid project id and line item name and verify that warning is displayed")]
        public void TST_MET_015_CodedStep7()
        {
            string wrongProjIDLineItemFilePath = GetExtractedValue("WrongProjIDLineItemFilePath").ToString();
            uploadFile(wrongProjIDLineItemFilePath);
            HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//td[@class='msgStrColumnValue last']", 60);
            Assert.IsNotNull(e,"Warning row should be displayed for invalid project id");
            string problemStr = e.BaseElement.InnerText;
            Assert.IsTrue(problemStr.Contains("No project has PowerSteering ID \"WrongProjID\""),"Problem text should be as expected for invalid project id");
        }
    
        [CodedStep(@"Upload/Load an empty file and verify that warning is displayed")]
        public void TST_MET_015_CodedStep8()
        {
            string emptyFilePath = GetExtractedValue("EmptyFilePath").ToString();
            uploadFile(emptyFilePath);
            HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//td[@class='msgStrColumnValue last']", 60);
            Assert.IsNotNull(e,"Warning row should be displayed for empty file");
            string problemStr = e.BaseElement.InnerText;
            Assert.IsTrue(problemStr.Contains("File has no column header"),"Problem text should be as expected for empty file");
        }
    
        [CodedStep(@"Upload/Load text file with all valid data and verify that file uploads successfully")]
        public void TST_MET_015_CodedStep9()
        {
            string validFilePath = GetExtractedValue("ValidFilePath").ToString();
            uploadFile(validFilePath);
            HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//div[@class='container']/div[@class='box cool welcome']", 60);
            Assert.IsNotNull(e,"Success message element should be populated for valid file");
            string msg = e.BaseElement.InnerText;
            Assert.IsTrue(msg.Contains("Success") && msg.Contains("project have been affected"),"Success messsage with affected project information should be displayed for valid file upload");
            
            
        }
    
       
    
        [CodedStep(@"Set Metric Instance URL for Project 1")]
        public void TST_MET_015_CodedStep10()
        {
            string metricInstanceURL =  ActiveBrowser.Url;  
            SetExtractedValue("MetricInstanceURL1", metricInstanceURL);
        }
    
        [CodedStep(@"Set Metric Instance URL for Project 2")]
        public void TST_MET_015_CodedStep11()
        {
            string metricInstanceURL =  ActiveBrowser.Url;  
            SetExtractedValue("MetricInstanceURL2", metricInstanceURL);
        }
        
        public void verifyMetricInsanceContent(){
           Pages.PS_MetricInstancePage.MetricTemplateBoldName.Wait.ForVisible();
           HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//div[@id='mainArea']//tbody[2]//tr[1]/td[2]", 60);
           string cellValue = e.BaseElement.InnerText;
           Log.WriteLine(cellValue);
           Assert.IsTrue(cellValue.Contains("100"),"Text should be displayed as entered in the cell"); 
        }
    
        [CodedStep(@"Navigate to metric instance page for project 1 and verify uploaded file data is reflected")]
        public void TST_MET_015_CodedStep12()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("MetricInstanceURL1").ToString());
            ActiveBrowser.WaitUntilReady();
            verifyMetricInsanceContent();
            
        }
    
        [CodedStep(@"Navigate to metric instance page for project 2 and verify uploaded file data is reflected")]
        public void TST_MET_015_CodedStep13()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.NavigateTo(GetExtractedValue("MetricInstanceURL2").ToString());
            ActiveBrowser.WaitUntilReady();
            verifyMetricInsanceContent();
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
    
        [CodedStep(@"Get project 1 URL and open project summary page")]
        public void TST_MET_007_CodedStep17()
        {
                        ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL1").ToString());
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Get project 2 URL and open project summary page")]
        public void TST_MET_015_CodedStep14()
        {
              ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL2").ToString());
              ActiveBrowser.WaitUntilReady();
        }
    }
}
