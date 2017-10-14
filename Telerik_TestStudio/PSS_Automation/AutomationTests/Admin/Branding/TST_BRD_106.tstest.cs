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

    public class TST_BRD_106 : BaseWebAiiTest
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
        public void TST_BRD_106_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on System Invitation E-mail link")]
        public void TST_BRD_106_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.SystemInvEmailLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.SystemInvEmailLink.Click();
        }
    
        [CodedStep(@"Checked Display on Page is off")]
        public void TST_BRD_106_CodedStep2()
        {
            if(Pages.PS_BrandingCustomMessages.DisplaySystemInvEmailChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplaySystemInvEmailChkBox.Click();
            }
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_106_CodedStep3()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "PowerSteering";
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.SystemInvEmailMailSubTextArea,""); 
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.SystemInvEmailMailBodyTextArea,""); 
            Pages.PS_BrandingCustomMessages.SystemInvEmailMailSubTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.SystemInvEmailMailSubTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_BRD_106_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify changes are reflected on System Invitation E-mail Text area")]
        public void TST_BRD_106_CodedStep5()
        {
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.SystemInvEmailMailSubTextArea.Value.Contains(GetExtractedValue("MsgText").ToString()));
        }
    }
}
