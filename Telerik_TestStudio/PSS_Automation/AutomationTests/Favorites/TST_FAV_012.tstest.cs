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

    public class TST_FAV_012 : BaseWebAiiTest
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
        public void TST_FAV_012_CodedStep()
        {
            Pages.PS_IssuesPage.NewIssueBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_AddEditIssuePage.IssueSubjectInput.Wait.ForVisible();
            Pages.PS_AddEditIssuePage.IssueSubmitBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_012_CodedStep1()
        {
            string issueNumber = Randomizers.generateRandomInt(1,1000).ToString();
            string msgText = "msg"+issueNumber;
            
            Actions.SetText(Pages.PS_AddEditIssuePage.IssueSubjectInput, issueNumber);
            
            Actions.InvokeScript(string.Format("window.frames[0].document.getElementById(\"tinymce\").innerHTML = '{0}';",msgText));
            
            Pages.PS_AddEditIssuePage.IssueSubmitBtn.Click();
            ActiveBrowser.WaitUntilReady();
            
            Pages.PS_IssueViewThreadPage.EditIssueLink.Wait.ForVisible();
            Pages.PS_IssueViewThreadPage.DeleteIssueLink.Wait.ForVisible();
            
            SetExtractedValue("IssueNumber",issueNumber);
        }
    
        [CodedStep(@"Verify Portfolio Name is pre populated in Name field ")]
        public void TST_FAV_012_CodedStep2()
        {
           // Element nameInput = ActiveBrowser.Find.ById("addToFavouritesName");
           string attr = ActiveBrowser.Actions.InvokeScript("document.getElementById(\"addToFavouritesName\").value;").ToString();
            //string attr = nameInput.getv("value").Value;
           string issueName = GetExtractedValue("IssueNumber").ToString();
           Assert.IsTrue(attr.Contains(string.Format("View Thread : {0}", issueName)), "Name field should be pre populated with Selected Issue Name");
           Pages.PS_HomePage.AddToFavDialogCancelButton.Click();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_012_CodedStep3()
        {
            Pages.PS_IssueViewThreadPage.DeleteIssueLink.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_IssueViewThreadPage.DeleteIssuePopupOkBtn.Wait.ForVisible();
            Pages.PS_IssueViewThreadPage.DeleteIssuePopupOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"Enter text 'test message' in 'TinymceBodyTag'")]
        //public void TST_FAV_012_CodedStep4()
        //{
            //// Enter text 'test message' in 'TinymceBodyTag'
            //Pages.SandboxTestArea.FrameMessageInputIfr.OwnerBrowser.Actions.SetText(Pages.SandboxTestArea.FrameMessageInputIfr.TinymceBodyTag, "test message");
            
        //}
    
        //[CodedStep(@"Enter text 'test message' in 'TinymceBodyTag'")]
        //public void TST_FAV_012_CodedStep4()
        //{
            //// Enter text 'test message' in 'TinymceBodyTag'
            //Actions.SetText(Pages.SandboxTestArea.FrameMessageInputIfr.TinymceBodyTag, "");
            //Pages.SandboxTestArea.FrameMessageInputIfr.TinymceBodyTag.ScrollToVisible(ArtOfTest.WebAii.Core.ScrollToVisibleType.ElementTopAtWindowTop);
            //ActiveBrowser.Window.SetFocus();
            //Pages.SandboxTestArea.FrameMessageInputIfr.TinymceBodyTag.Focus();
            //Pages.SandboxTestArea.FrameMessageInputIfr.TinymceBodyTag.MouseClick();
            //Manager.Desktop.KeyBoard.TypeText("test message", 50, 100, true);
            
        //}
    }
}
