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

    public class TST_NAV_UI_043 : BaseWebAiiTest
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
    
        [CodedStep(@"Click Edit User link")]
        public void TST_NAV_UI_043_CodedStep()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait till user navigated to Edit Profle page")]
        public void TST_NAV_UI_043_CodedStep1()
        {
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForVisible();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"Upload new profile image in JPG format")]
        public void TST_NAV_UI_043_CodedStep2()
        {
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\profile_img.jpg";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             Pages.PS_SettingsProfilePage.UploadBoxAddImageFile.Click();
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             Pages.PS_SettingsProfilePage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
           
             
          
            
        }
    
        [CodedStep(@"Click Edit User link")]
        public void TST_NAV_UI_043_CodedStep3()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait till user navigated to Edit Profle page")]
        public void TST_NAV_UI_043_CodedStep4()
        {
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForVisible();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_043_CodedStep5()
        {
            Pages.PS_SettingsProfilePage.ProfileImgBox.MouseHover();
            Pages.PS_SettingsProfilePage.ProfileImgBox.Focus();
            Pages.PS_SettingsProfilePage.ProfileImgDeleteBtn.Wait.ForVisible();
            Pages.PS_SettingsProfilePage.ProfileImgDeleteBtn.Click();
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_UserProfilePage.EmailTableHeader.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_043_CodedStep6()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.HeaderProfileImage.Wait.ForVisible();
            string imgSource = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;
            Assert.IsTrue(imgSource.Contains("profileImgDefault.png"),"Profile image should be deleted and default image should be displayed");
        }
    }
}
