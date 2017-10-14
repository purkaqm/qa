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

    public class TST_BRD_053 : BaseWebAiiTest
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
        public void TST_BRD_053_CodedStep()
        {
            if(Pages.PS_BrandingLookAndFeelPage.ReportLogoImg.IsVisible()){ 
             string prevImg = Pages.PS_BrandingLookAndFeelPage.ReportLogoImg.BaseElement.GetAttribute("src").Value.ToString();             
             SetExtractedValue("PrevImg",prevImg); 
             }
             else{
               string prevImg = "";
               SetExtractedValue("PrevImg",prevImg);  
             }
             string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
             string imgToUploadPath = projectPath + "\\Data\\Images\\sample_img_oneMb.jpg";
             ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, imgToUploadPath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
             Manager.Current.DialogMonitor.AddDialog(fileDialog);
             Manager.Current.DialogMonitor.Start();
             ActiveBrowser.Window.SetFocus();
             Pages.PS_BrandingLookAndFeelPage.ReportLogoBrowseBtn.Click();          
             fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
             Manager.Current.DialogMonitor.Stop();
             System.Threading.Thread.Sleep(50000); 
             Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
             ActiveBrowser.WaitUntilReady();
             System.Threading.Thread.Sleep(5000); 
             string currImg = Pages.PS_BrandingLookAndFeelPage.ReportLogoImg.BaseElement.GetAttribute("src").Value.ToString();
             string preImage = GetExtractedValue("PrevImg").ToString();
             if(preImage.Length > 0){
             Assert.IsTrue(!preImage.Equals(currImg), "report logo should be changed after upload...");  
             }
             else{
                 Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ReportLogoImg.IsVisible(),"new report logo should be visible");
             }       
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_053_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.ReportLogoDelImgSpan.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            System.Threading.Thread.Sleep(4000);
        }
    }
}
