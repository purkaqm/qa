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
using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System.Security.AccessControl;
using System.Security.Principal;
using System.IO;

namespace PSS_Automation.AutomationTests.Admin.Template.Metrics
{

    public class TST_MET_016 : BaseWebAiiTest
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

        [CodedStep(@" Wait for user to be navigated to newly created work summary page")]
        public void TST_MET_011_CodedStep()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();

        }

        [CodedStep(@"Store Project 1 URL and Powersteering ID")]
        public void TST_MET_011_CodedStep1()
        {
            string projUrl = ActiveBrowser.Url;
            SetExtractedValue("ProjectURL1", projUrl);
            SetExtractedValue("ProjectName1", GetExtractedValue("CreatedProjectName").ToString());
            string projID = projUrl.Split(new string[] { "?sp=U" }, StringSplitOptions.None)[1];
            SetExtractedValue("ProjectID1", projID);
        }

        [CodedStep(@"Create a text file without metric template attached")]
        public void TST_MET_015_CodedStep3()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\Generated\\templatenoattachmetricloader.txt";

            string proj1Name = GetExtractedValue("ProjectName1").ToString();
            string proj1ID = GetExtractedValue("ProjectID1").ToString();



            string metricName = GetExtractedValue("GeneratedMetricName").ToString();

            string viewName = Data["ViewName1"].ToString();
            string lineItemName = Data["LineItemName1"].ToString();

            string periodHeader = Data["PeriodStartDate"].ToString();

            string firstLine = string.Format("Project Name\tProject's Powersteering ID\tProject's Sequence\tMetric template\tView\tBeneficiary\tItem Name\t{0}", periodHeader);

            string secondLine = string.Format("{0}\t{1}\t\t{2}\t{3}\t\t{4}\t100", proj1Name, proj1ID, metricName, viewName, lineItemName);

            string[] lines = { firstLine, secondLine };

            System.IO.File.WriteAllLines(newFilePath, lines);

            SetExtractedValue("NoAttachTemplateFilePath", newFilePath);
        }

         public void uploadFile(string filePath){
            
          grantAccess(filePath);
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
                 System.Threading.Thread.Sleep(10000);
                Manager.Current.DialogMonitor.Stop();
                 System.Threading.Thread.Sleep(10000);
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
               System.Threading.Thread.Sleep(10000);
              Manager.Current.DialogMonitor.Stop();
              System.Threading.Thread.Sleep(10000);
              Actions.InvokeScript("document.querySelector('[value=\"Load\"]').click();");  
              ActiveBrowser.WaitUntilReady();
              System.Threading.Thread.Sleep(10000);
            }
           
           
        }
        


        private bool grantAccess(string fullPath)
        {
            DirectoryInfo dInfo = new DirectoryInfo(fullPath);
            DirectorySecurity dSecurity = dInfo.GetAccessControl();
            dSecurity.AddAccessRule(new FileSystemAccessRule(new SecurityIdentifier(WellKnownSidType.WorldSid, null), FileSystemRights.FullControl, InheritanceFlags.ObjectInherit | InheritanceFlags.ContainerInherit, PropagationFlags.NoPropagateInherit, AccessControlType.Allow));
            dInfo.SetAccessControl(dSecurity);
            return true;
        }

        [CodedStep(@"Upload/Load text file and verify that warning is displayed")]
        public void TST_MET_015_CodedStep6()
        {

            string proj1Name = GetExtractedValue("ProjectName1").ToString();
            string metricName = GetExtractedValue("GeneratedMetricName").ToString();
            string noAttachTemplateFilePath = GetExtractedValue("NoAttachTemplateFilePath").ToString();
            
            uploadFile(noAttachTemplateFilePath);
            HtmlControl e = CustomUtils.waitForElementToExist(ActiveBrowser, "//td[@class='msgStrColumnValue last']", 60);
            Assert.IsNotNull(e, "Warning row should be displayed for wrong metric template");
            string problemStr = e.BaseElement.InnerText;
            Log.WriteLine(problemStr);
            string expectedStr = string.Format("template \"{0}\" is not attached to project \"{1}\"", metricName, proj1Name);
            Log.WriteLine(expectedStr);
            Assert.IsTrue(problemStr.Contains(expectedStr), "Problem text should be as expected for wrong metric tempate");

        }

        [CodedStep(@"Get project 1 URL and open project summary page")]
        public void TST_MET_007_CodedStep17()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectURL1").ToString());
            ActiveBrowser.WaitUntilReady();
        }
        
        // Add your test methods here...
    }
}
