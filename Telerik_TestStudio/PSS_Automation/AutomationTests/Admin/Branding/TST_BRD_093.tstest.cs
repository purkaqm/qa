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

    public class TST_BRD_093 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Custom Messages tab")]
        public void TST_BRD_093_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Help Desk Request Text link ")]
        public void TST_BRD_093_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtLink.Click();
        }
    
        [CodedStep(@"Checked Display on Page is On")]
        public void TST_BRD_093_CodedStep2()
        {
            
            if(!Pages.PS_BrandingCustomMessages.DisplayHelpDeskReqChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplayHelpDeskReqChkBox.Click();
            }
        }
    
        [CodedStep(@"Write message with html tag in message box area")]
        public void TST_BRD_093_CodedStep3()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "<b>PowerSteering<b>"+Randomizers.generateRandomInt(10000,99999);
            Actions.SetText(Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea,"");
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea,"");            
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Save the changes")]
        public void TST_BRD_093_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Help icon")]
        public void TST_BRD_093_CodedStep5()
        {
            Pages.PS_HomePage.HelpIconSpan.Wait.ForExists();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_HomePage.HelpIconSpan.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on Submit Help Desk Request")]
        public void TST_BRD_093_CodedStep6()
        {
            Pages.PS_HomePage.SubmitHelpDeskReqLink.Wait.ForExists();
            Pages.PS_HomePage.SubmitHelpDeskReqLink.Click();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_093_CodedStep7()
        {
            // Connect to pop-up window : '/help/HelpDesk.page'
            Manager.WaitForNewBrowserConnect("/help/HelpDesk.page", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify 'Custom text' with html tag used, is displayed on the resulting page")]
        public void TST_BRD_093_CodedStep8()
        {
            HtmlDiv customTxtLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("Custom_text_in_submit_help_desk_req"));
            customTxtLoc.Wait.ForExists();
            Assert.IsTrue(customTxtLoc.BaseElement.InnerMarkup.ToString().Contains(GetExtractedValue("MsgText").ToString()),"Custom message should be shown in submit help desk request page");
        }
    }
}
