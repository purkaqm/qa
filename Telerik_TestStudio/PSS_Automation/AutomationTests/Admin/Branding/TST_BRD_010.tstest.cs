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

    public class TST_BRD_010 : BaseWebAiiTest
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
        public void TST_BRD_010_CodedStep()
        {
            
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\sample_img_tenMb.jpg";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             ActiveBrowser.Window.SetFocus();
             Pages.PS_BrandingLookAndFeelPage.HeaderLogoBrowseBtn.Click();          
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             System.Threading.Thread.Sleep(480000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_010_CodedStep1()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Click();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_010_CodedStep2()
        {
           
            HtmlTableCell logoErrorLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_header_logo_size_error_cell"));
            logoErrorLoc.Wait.ForExists();
            Assert.IsTrue(logoErrorLoc.IsVisible(),"Error message should be shown");
            
        }
    }
}
