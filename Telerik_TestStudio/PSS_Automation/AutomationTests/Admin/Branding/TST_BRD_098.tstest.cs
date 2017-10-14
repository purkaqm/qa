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

    public class TST_BRD_098 : BaseWebAiiTest
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
        public void TST_BRD_098_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Help Desk Request Text link ")]
        public void TST_BRD_098_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.HelpReqLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.HelpReqLink.Click();
        }
    
        [CodedStep(@"Checked Display on Page is On")]
        public void TST_BRD_098_CodedStep2()
        {
            if(!Pages.PS_BrandingCustomMessages.DisplayHelpReqMsgChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplayHelpReqMsgChkBox.Click();
            }
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_098_CodedStep3()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "PowerSteering"+Randomizers.generateRandomInt(10000,99999);
            Actions.SetText(Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea,"");
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea,"");            
            Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Save the changes")]
        public void TST_BRD_098_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Support link on the Login page")]
        public void TST_BRD_098_CodedStep5()
        {
            Pages.PS_LoginPage.SupportLink.Wait.ForExists();
            Pages.PS_LoginPage.SupportLink.Click();   
        }
    
        [CodedStep(@"Verify Custom text is displayed on the help page.")]
        public void TST_BRD_098_CodedStep6()
        {
            HtmlDiv customTxtLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("Custom_text_in_help_req_msg"));
            customTxtLoc.Wait.ForExists();
            Assert.IsTrue(customTxtLoc.InnerText.ToString().Contains(GetExtractedValue("MsgText").ToString()),"Custom message should be shown in help page");
        }
    }
}
