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

    public class TST_MAT_099_C992417 : BaseWebAiiTest
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
        
        string titleLink;
    
        [CodedStep(@"Click on Add new button")]
        public void TST_MAT_099_C992417_ClickAddNewButton()
        {
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Load the document and fill the rest of the details")]
        public void TST_MAT_099_C992417_FillDetails()
        {
            //Load file
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\TestUploadFile.docx";
            ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, newFilePath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
            Manager.Current.DialogMonitor.AddDialog(fileDialog);
            Manager.Current.DialogMonitor.Start();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_ManageImportantLinksPage.BrowseInput.Wait.ForExists();
            Pages.PS_ManageImportantLinksPage.BrowseInput.Focus();
            Pages.PS_ManageImportantLinksPage.BrowseInput.MouseClick();
            fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();

            //Enter title
            titleLink = "testDoc" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_ManageImportantLinksPage.TitleInputText,titleLink);
                                    
        }
    
        [CodedStep(@"Click on Add Document button")]
        public void TST_MAT_099_C992417_ClickAddDocBtn()
        {
            Pages.PS_ManageImportantLinksPage.AddDocumentButton.Click();
        }
    
        [CodedStep(@"Click on Done button")]
        public void TST_MAT_099_C992417_ClickDoneBtn()
        {
            Pages.PS_ManageImportantLinksPage.DoneButton.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.DoneButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify link is created")]
        public void TST_MAT_099_C992417_VerifyLink()
        {
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            HtmlAnchor impLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='titleColumnValue']/a[contains(.,'{0}')]",titleLink));   
            impLink.Wait.ForExists();
            Assert.IsTrue(impLink.IsVisible(),"Link should be visible");
                                    
        }
    
        [CodedStep(@"Delete the generated link")]
        public void TST_MAT_099_C992417_DeleteLink()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlInputCheckBox impLinkCkhbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(.,'{0}')]/td[@class='updateColumnValue last']//input",titleLink));
            impLinkCkhbox.Check(true);
            Pages.PS_ManageImportantLinksPage.DeleteBtn.Click();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ManageImportantLinksPage.DeleteYesButton.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.DeleteYesButton.Click();
            ActiveBrowser.WaitUntilReady();
                                                
        }
    }
}
