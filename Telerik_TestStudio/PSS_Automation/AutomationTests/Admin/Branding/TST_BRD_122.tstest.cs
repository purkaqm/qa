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

    public class TST_BRD_122 : BaseWebAiiTest
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
    

    
        [CodedStep(@"Click on PowerPoint Branding tab")]
        public void TST_BRD_122_CodedStep()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Upload ppt file")]
        public void TST_BRD_122_CodedStep1()
        {
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string fileToUploadPath = projectPath + "\\Data\\AutomationTest.ppt";
            string fileName = "AutomationTest.ppt";
            string fileVarificationLoc = string.Format("//a[contains(.,'{0}')]",fileName);
            SetExtractedValue("FileVarificationLoc",fileVarificationLoc);
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
    
        [CodedStep(@"Click on Delete icon ")]
        public void TST_BRD_122_CodedStep2()
        {
            if(ActiveBrowser.Find.AllByXPath(GetExtractedValue("FileVarificationLoc").ToString()).Count > 0){
                HtmlSpan delSpan = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("uploaded_ppt_file_del_span"));
                delSpan.MouseHover(); 
                delSpan.Click();
                ActiveBrowser.WaitUntilReady();
            }
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Popup is shown")]
        public void TST_BRD_122_CodedStep3()
        {
            Pages.PS_BrandingPowerPointPage.DeletePopUpDiv.Wait.ForExists();
            Pages.PS_BrandingPowerPointPage.DeletePopUpDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.DeletePopUpDiv.IsVisible(),"Popup should be displayed");
        }
    }
}
