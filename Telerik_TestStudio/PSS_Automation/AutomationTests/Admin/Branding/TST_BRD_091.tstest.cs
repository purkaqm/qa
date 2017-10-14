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

    public class TST_BRD_091 : BaseWebAiiTest
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
        public void TST_BRD_091_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Help Desk Request Text link ")]
        public void TST_BRD_091_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtLink.Click();
        }
    
        [CodedStep(@"Write message with Html tag in message box area")]
        public void TST_BRD_091_CodedStep2()
        {
            ActiveBrowser.Window.SetFocus();
            string msgText = "<b>PowerSteering</b>";
            Actions.SetText(Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea,"");
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea,"");            
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Click on save button")]
        public void TST_BRD_091_CodedStep3()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify changes are reflected on Help Desk Request Text area")]
        public void TST_BRD_091_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.HelpDeskReqTxtTextArea.BaseElement.InnerText.Equals(GetExtractedValue("MsgText")));
        }
    }
}
