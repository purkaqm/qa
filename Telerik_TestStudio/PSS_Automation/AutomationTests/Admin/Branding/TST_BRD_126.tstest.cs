using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.IO;
using ArtOfTest.WebAii.Win32.Dialogs;
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

    public class TST_BRD_126 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on PowerPoint branding tab")]
        public void TST_BRD_126_01()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    

        [CodedStep(@"Upload ppt file and Verify")]
        public void TST_BRD_126_02()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string fileToUploadPath = projectPath + "\\Data\\AutomationTest.ppt";
            string fileName = "AutomationTest.ppt";
            string fileVarificationLoc = string.Format("//a[contains(.,'{0}')]",fileName); 
            if(ActiveBrowser.Find.AllByXPath(fileVarificationLoc).Count == 0){
            ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, fileToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
            Manager.Current.DialogMonitor.AddDialog(fileDialog);
            Manager.Current.DialogMonitor.Start();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingPowerPointPage.AddNewTempBtn.MouseClick(MouseClickType.LeftClick);         
            fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();
            System.Threading.Thread.Sleep(3000);             
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(fileVarificationLoc).Count > 0, "File should be uploaded"); 
            }
        }
        
        [CodedStep(@"Template download to the user drive folder")]
        public void TST_BRD_126_03()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string fileToDownloadPath = projectPath + "\\Data\\downlaoded_file.ppt";
            string fileName = "AutomationTest.ppt";
            string fileVarificationLoc = string.Format("//a[contains(.,'{0}')]",fileName); 
            HtmlAnchor fileLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(fileVarificationLoc);

            ArtOfTest.WebAii.Win32.Dialogs.DownloadDialogsHandler dialog = new DownloadDialogsHandler (Manager.ActiveBrowser, DialogButton.SAVE, fileToDownloadPath, Manager.Desktop); 
            Manager.DialogMonitor.Start();
            fileLink.Click();
            dialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();
            
        }
    
        [CodedStep(@"Verify Downloaded file is Present in Default Folder")]
        public void TST_BRD_126_04()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string fileToDownloadPath = projectPath + "\\Data\\downlaoded_file.ppt";
            Assert.IsTrue(System.IO.File.Exists(fileToDownloadPath),"Ppt file should be present in directory");
            File.Delete(fileToDownloadPath);           
        }
    }
}
