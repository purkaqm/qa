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

    public class TST_BRD_015 : BaseWebAiiTest
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
        public void TST_BRD_015_CodedStep()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string dataToUploadPath = projectPath + "\\Data\\Images\\test_file.docx";
            ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, dataToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
            Manager.Current.DialogMonitor.AddDialog(fileDialog);
            Manager.Current.DialogMonitor.Start();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.HeaderLogoHconBrowseBtn.Click();          
            fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();            
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
            string errorCellName = AppLocators.get("branding_header_logo_hcon_error_table_cell").ToString();            
            HtmlTableCell errorCellLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(errorCellName);
            errorCellLoc.Wait.ForExists();
            Assert.IsTrue(errorCellLoc.IsVisible(),"error message should be visible"); 
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_015_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
