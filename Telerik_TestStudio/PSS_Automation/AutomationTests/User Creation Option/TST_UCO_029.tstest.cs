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

    public class TST_UCO_029 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_029_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Turn on 'At least one group must be specified' at Import New Users (spreadsheet) column")]
        public void TST_UCO_029_CS02()
        {
                        
            Pages.PS_UserCreationOptionsPage.AtleastGroupImportUserChkbox.Check(true);
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_029_CS03()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
                Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Review tab on left navigation bar")]
        public void TST_UCO_029_CS04()
        {
                Pages.PS_HomePage.ReviewLeftNavLink.Click();
        }
    
        [CodedStep(@"Select file to upload whose required group value is missed")]
        public void TST_UCO_029_CS05()
        {
                string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
                string importFileToUploadPath = projectPath + "\\Data\\MissingColumnValueImportFile.txt";
                ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, importFileToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
                Manager.Current.DialogMonitor.AddDialog(fileDialog);
                Manager.Current.DialogMonitor.Start();
                ActiveBrowser.Window.SetFocus();
                Pages.PS_ReviewImportNewUsersPage.BrowseButton.Click();                      
                fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
                Manager.Current.DialogMonitor.Stop();
                System.Threading.Thread.Sleep(3000); 
                Pages.PS_ReviewImportNewUsersPage.UploadFileLoadButton.Click();
                ActiveBrowser.WaitUntilReady();
                System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify error meassage is displayed")]
        public void TST_UCO_029_CS06()
        {
                ActiveBrowser.RefreshDomTree();
                HtmlDiv errorMsg = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("import_new_users_error_msg_div"));
                errorMsg.Wait.ForExists();
                Assert.IsTrue(errorMsg.IsVisible(),"Error message should be displayed");
        }
    
        [CodedStep(@"Verify handeled message of error is shown")]
        public void TST_UCO_029_CS07()
        {
                string handleMsg = "Group field is required";
                HtmlTableRow errorHandleMsg = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("import_new_users_error_handler_missing_grp_col_val_msg_div"),handleMsg));
                errorHandleMsg.Wait.ForExists();
                Assert.IsTrue(errorHandleMsg.IsVisible(),"Error handler message should be displayed");
        }
    }
}
