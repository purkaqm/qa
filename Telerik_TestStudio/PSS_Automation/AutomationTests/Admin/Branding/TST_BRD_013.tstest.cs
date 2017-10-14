using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.IO;
using ArtOfTest.Common.UnitTesting;
using ArtOfTest.WebAii.Core;
using ArtOfTest.WebAii.Controls.HtmlControls;
using ArtOfTest.WebAii.Controls.HtmlControls.HtmlAsserts;
using ArtOfTest.WebAii.Design;
using ArtOfTest.WebAii.Design.Execution;
using ArtOfTest.WebAii.ObjectModel;
using ArtOfTest.WebAii.Silverlight;
using ArtOfTest.WebAii.Silverlight.UI;
using ArtOfTest.WebAii.Win32.Dialogs;

namespace PSS_Automation
{

    public class TST_BRD_013 : BaseWebAiiTest
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
        public void TST_BRD_013_CodedStep()
        {
             HtmlTableCell clearImgLocHL = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_h_logo_hcon_clear_image_table_cell"));
             if(clearImgLocHL.IsVisible()){
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\upland_logo.png";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             ActiveBrowser.Window.SetFocus();
             Pages.PS_BrandingLookAndFeelPage.HeaderLogoHconBrowseBtn.Click();          
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             System.Threading.Thread.Sleep(3000);
             Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
             System.Threading.Thread.Sleep(3000);
            }
            
            string projectPath1 = this.ExecutionContext.DeploymentDirectory.ToString();
            string imgToDownloadPath = projectPath1 + "\\Data\\Images\\Header_logo_hcon.png";
            ArtOfTest.WebAii.Win32.Dialogs.DownloadDialogsHandler dialog = new DownloadDialogsHandler (Manager.ActiveBrowser, DialogButton.SAVE, imgToDownloadPath, Manager.Desktop); 
            Manager.DialogMonitor.Start();
            Pages.PS_BrandingLookAndFeelPage.HeaderLogoHcDownloadLink.Click();
            dialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_013_CodedStep1()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string imgToDownloadPath = projectPath + "\\Data\\Images\\Header_logo_hcon.png";
            Assert.IsTrue(System.IO.File.Exists(imgToDownloadPath),"Image file should be present in directory");
            File.Delete(imgToDownloadPath);
        }
    }
}
