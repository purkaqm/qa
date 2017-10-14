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

    public class TST_MAT_086_C992803 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_086_C992803_WaitProjectSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                                   
        }
    
        [CodedStep(@"Click the 'here' link")]
        public void TST_MAT_086_C992803_ClickHereLink()
        {
            Pages.PS_Documents_Page.HereLink.Click(true);
        }
        
        [CodedStep(@"Click Add new button")]
        public void TST_MAT_086_C992803_ClickAddNewButton()
        {
            Pages.PS_Documents_Page.AddNewButton.Click(true);
        }
    
        [CodedStep(@"Load the document and fill the rest of the details")]
        public void TST_MAT_099_C992417_FillDetails()
        {
            //Load file
            ActiveBrowser.RefreshDomTree();
            string projectPath = this.ExecutionContext.DeploymentDirectory.ToString();
            string newFilePath = projectPath + "\\Data\\TestUploadFile.docx";
            ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog fileDialog = new ArtOfTest.WebAii.Win32.Dialogs.FileUploadDialog(Manager.Current.ActiveBrowser, newFilePath, ArtOfTest.WebAii.Win32.Dialogs.DialogButton.OPEN);
            Manager.Current.DialogMonitor.AddDialog(fileDialog);
            Manager.Current.DialogMonitor.Start();
            ActiveBrowser.Window.SetFocus();
            if ((ActiveBrowser.BrowserType == BrowserType.FireFox))
            {
                    ActiveBrowser.Window.SetFocus();
                    Pages.PS_AddDocumentPopup.BrowseFromFileInput.Wait.ForExists();
                    Pages.PS_AddDocumentPopup.BrowseFromFileInput.ScrollToVisible(ScrollToVisibleType.ElementTopAtWindowTop);
                    Pages.PS_AddDocumentPopup.BrowseFromFileInput.MouseClick();
            } else
            {
                    Pages.PS_AddDocumentPopup.BrowseFromFileInput.Wait.ForExists();
                    Pages.PS_AddDocumentPopup.BrowseFromFileInput.Click(false);
            }
            fileDialog.WaitUntilHandled(Manager.Settings.ElementWaitTimeout);
            Manager.Current.DialogMonitor.Stop();

            //Enter title
            titleLink = "TestDoc" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_AddDocumentPopup.TitleTextInput, titleLink);
                                                
        }
    
        [CodedStep(@"Click on Add Document button")]
        public void TST_MAT_099_C992417_ClickAddDocBtn()
        {
            Pages.PS_AddDocumentPopup.AddDocumentButton.Click(false);
        }
    
        [CodedStep(@"Click on Done button")]
        public void TST_MAT_099_C992417_ClickDoneBtn()
        {
            Pages.PS_AddDocumentPopup.DoneButton.Wait.ForExists(30000);
            Pages.PS_AddDocumentPopup.DoneButton.Wait.ForVisible(30000);
            System.Threading.Thread.Sleep(5000);
            Pages.PS_AddDocumentPopup.DoneButton.Click(false);
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify link is created")]
        public void TST_MAT_099_C992417_VerifyLink()
        {
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            HtmlAnchor documentLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='titleColumnValue']/a[contains(.,'{0}')]",titleLink));   
            documentLink.Wait.ForExists();
            Assert.IsTrue(documentLink.IsVisible(),"Link should be visible");
                                                
        }
    
        [CodedStep(@"Delete the generated link")]
        public void TST_MAT_099_C992417_DeleteLink()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlInputCheckBox documentLinkCkhbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(.,'{0}')]/td[@class='deleteColumnValue']//input",titleLink));
            documentLinkCkhbox.Check(true);
            Pages.PS_Documents_Page.DeleteButton.Click();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Wait.ForExists(30000);
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Click();
            ActiveBrowser.WaitUntilReady();
                                                            
        }
    
        [CodedStep(@"Delete the Document permanently through deleted_documents.jsp page")]
        public void TST_MAT_084_C992802_DeleteDocumentPermanently()
        {
            HtmlInputCheckBox deleteDocCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//div[@class='deleted'][contains(.,'{0}')]/input",titleLink));
            deleteDocCheckbox.Wait.ForExists();
            deleteDocCheckbox.ScrollToVisible();
            deleteDocCheckbox.Check(true);
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Click();
            Pages.PS_Deleted_Users_Page.PermanentlyDeleteUserProgressContainer.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForVisible();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Click();
        }
    
        
    }
}
