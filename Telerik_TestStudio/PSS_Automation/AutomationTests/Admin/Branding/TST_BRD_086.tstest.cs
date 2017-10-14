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

    public class TST_BRD_086 : BaseWebAiiTest
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
        public void TST_BRD_086_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Login Licence Message link")]
        public void TST_BRD_086_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Click();
        }
    
        [CodedStep(@"Checked Display on page ")]
        public void TST_BRD_086_CodedStep2()
        {
            if(!Pages.PS_BrandingCustomMessages.DisplayLoginLicMsgChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplayLoginLicMsgChkBox.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_086_CodedStep3()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "Welcome to PowerSteering";
            Actions.SetText(Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea,"");
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea,"");
            
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_086_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Login ")]
        public void TST_BRD_086_CodedStep5()
        {
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.UsernameTextField,Data["Username"].ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.PasswordTextField,Data["Password"].ToString());
            Pages.PS_LoginPage.SignInSubmitButton.Click();
            Pages.PS_LicencePopup.ContinueButton.Wait.ForVisible();
            
        }

        [CodedStep(@"Verify message is shown in License Popup")]
        public void TST_BRD_086_CodedStep6()
        {
            HtmlDiv licPopUpTxtLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("login_licence_pop_up_txt_div"));
            Assert.IsTrue(licPopUpTxtLoc.InnerText.ToString().Contains(GetExtractedValue("MsgText").ToString()),"Correct message should be shown in licence popup");
        } 
    }
    
}
