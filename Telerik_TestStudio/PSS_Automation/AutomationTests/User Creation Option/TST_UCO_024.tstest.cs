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

    public class TST_UCO_024 : BaseWebAiiTest
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
        public void TST_UCO_024_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Turn on Enforce required tags and required custom fields on Invite New User column")]
        public void TST_UCO_024_CS02()
        {
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Checked)
            {
                Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
        
        
        [CodedStep(@"Turn on Enforce required tags and required custom fields at Import New Users (spreadsheet) column")]
        public void TST_UCO_024_CS03()
        {
            if(!Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Checked)
            {
                Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsImportUserChkbox.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_024_CS04()
        {
            if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
            {
                Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
                ActiveBrowser.RefreshDomTree();
            }
        }
    
        
    
        [CodedStep(@"Click on AddNew tag button")]
        public void TST_UCO_024_CS05()
        {
            Pages.PS_TagsListPage.AddNewTagBtn.Click();
        }
    
        [CodedStep(@"Verify new tag is created")]
        public void TST_UCO_024_CS06()
        {
            HtmlTableRow tagRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_tag_row"),GetExtractedValue("CustTagName").ToString()));
            tagRow.Wait.ForExists();
            tagRow.IsVisible();
        }
    
        [CodedStep(@"Click on Review tab on left navigation bar")]
        public void TST_UCO_024_CS07()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Click();
        }
        
        [CodedStep(@"Select file to upload whose required tag column is missed")]
        public void TST_UCO_024_CS08()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string importFileToUploadPath = projectPath + "\\Data\\MissingTagNadCusFieldColumnImportFile.txt";
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
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify error meassage is displayed")]
        public void TST_UCO_024_CS09()
        {
            HtmlDiv errorMsg = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("import_new_users_error_msg_div"));
            errorMsg.Wait.ForExists();
            Assert.IsTrue(errorMsg.IsVisible(),"Error message should be displayed");
        }
    
        [CodedStep(@"Delete created custom tag")]
        public void TST_UCO_025_CS10()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_tag_image"),GetExtractedValue("CustTagName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            System.Threading.Thread.Sleep(2000);
            
            Pages.PS_TagsListPage.DeleteTagYesButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
