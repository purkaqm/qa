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

    public class TST_NAV_UI_042 : BaseWebAiiTest
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
        public void TST_NAV_UI_042_CodedStep()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_042_CodedStep1()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForExists();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_042_CodedStep2()
        {
             string prevImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;
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
             string currImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;
             Assert.IsTrue(!prevImg.Equals(currImg), "profile image should be changed after upload...");
            
        }
    
        [CodedStep(@"Click Edit User link")]
        public void TST_NAV_UI_042_CodedStep3()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait till user navigated to Edit Profle page")]
        public void TST_NAV_UI_042_CodedStep4()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForExists();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"Upload new profile image in jpg format")]
        public void TST_NAV_UI_042_CodedStep5()
        {
             string prevImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;    
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\profile_img.png";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             Pages.PS_SettingsProfilePage.UploadBoxAddImageFile.Click();
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             Pages.PS_SettingsProfilePage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
             string currImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;
             Assert.IsTrue(!prevImg.Equals(currImg), "profile image should be changed after upload...");
           
             
           //  System.Drawing.Bitmap btmp1 = (System.Drawing.Bitmap)System.Drawing.Image.FromFile(imgToUploadPath);
            //System.Drawing.Bitmap btmp2 = Pages.PS_HomePage.HeaderProfileImage.Capture();
            
            
            //ArtOfTest.Common.PixelMap expected = ArtOfTest.Common.PixelMap.FromBitmap(btmp1);
            //ArtOfTest.Common.PixelMap actual = ArtOfTest.Common.PixelMap.FromBitmap(btmp2);
            
          
         //   Assert.IsTrue(expected.Compare(actual, 50.0));
            
        }
    
        [CodedStep(@"Click Edit User link")]
        public void TST_NAV_UI_042_CodedStep6()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait till user navigated to Edit Profle page")]
        public void TST_NAV_UI_042_CodedStep7()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForExists();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"Upload new profile image in png format")]
        public void TST_NAV_UI_042_CodedStep8()
        {
             string prevImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;        
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\profile_img.gif";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             Pages.PS_SettingsProfilePage.UploadBoxAddImageFile.Click();
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             Pages.PS_SettingsProfilePage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
             string currImg = Pages.PS_HomePage.HeaderProfileImage.BaseElement.GetAttribute("src").Value;
             Assert.IsTrue(!prevImg.Equals(currImg), "profile image should be changed after upload...");
           
             
           //  System.Drawing.Bitmap btmp1 = (System.Drawing.Bitmap)System.Drawing.Image.FromFile(imgToUploadPath);
            //System.Drawing.Bitmap btmp2 = Pages.PS_HomePage.HeaderProfileImage.Capture();
            
            
            //ArtOfTest.Common.PixelMap expected = ArtOfTest.Common.PixelMap.FromBitmap(btmp1);
            //ArtOfTest.Common.PixelMap actual = ArtOfTest.Common.PixelMap.FromBitmap(btmp2);
            
          
         //   Assert.IsTrue(expected.Compare(actual, 50.0));
            
        }
    }
}
