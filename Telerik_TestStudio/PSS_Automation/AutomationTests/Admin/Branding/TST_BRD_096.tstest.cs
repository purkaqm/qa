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

    public class TST_BRD_096 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Custom Messages tab ")]
        public void TST_BRD_096_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Help  Request Message link ")]
        public void TST_BRD_096_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.HelpReqLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.HelpReqLink.Click();
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_096_CodedStep2()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "PowerSterring";
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea,"");            
            Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_BRD_096_CodedStep3()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify changes are reflected on Help Request Message Text area")]
        public void TST_BRD_096_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.HelpReqMsgTextArea.BaseElement.InnerText.Equals(GetExtractedValue("MsgText")));
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_096_CodedStep5()
        {
            if(Pages.PS_BrandingCustomMessages.DisplayHelpReqMsgChkBox.Checked){
                Pages.PS_BrandingCustomMessages.DisplayHelpReqMsgChkBox.Click();
            }
        }
    }
}
