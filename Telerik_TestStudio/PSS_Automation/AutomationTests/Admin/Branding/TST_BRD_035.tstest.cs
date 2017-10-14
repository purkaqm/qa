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

    public class TST_BRD_035 : BaseWebAiiTest
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
        public void TST_BRD_035_CodedStep()
        {
           
             
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\upland_logo.png";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             ActiveBrowser.Window.SetFocus();
             Pages.PS_BrandingLookAndFeelPage.LoginLogoBrowseBtn.Click();          
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop(); 
             System.Threading.Thread.Sleep(3000);
             Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
             System.Threading.Thread.Sleep(3000);     
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_035_CodedStep1()
        {
            HtmlImage loginLogoLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("login_page_logo"));
            Assert.IsTrue(loginLogoLoc.IsVisible(),"Login logo should be diplayed");
        }
    
        
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_035_CodedStep2()
        {
             if(Pages.PS_BrandingLookAndFeelPage.LoginLogoImg.IsVisible()){
                Pages.PS_BrandingLookAndFeelPage.LoginLogoDelImgSpan.Click();
                System.Threading.Thread.Sleep(3000);
                Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
                System.Threading.Thread.Sleep(3000);
                Pages.PS_HomePage.LogOutLink.Click();
                ActiveBrowser.WaitUntilReady();  
                Assert.IsTrue(Pages.PS_HomePage.LoginPageLogoDefaultSpan.IsVisible(),"defualt logo should be displayed");                 
            }
        }
    }
}
