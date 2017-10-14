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

    public class TST_BRD_111 : BaseWebAiiTest
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
        public void TST_BRD_111_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Self Registration link")]
        public void TST_BRD_111_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.SelfRegLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.SelfRegLink.Click();
        }
    
        [CodedStep(@"Checked Display on Page is off")]
        public void TST_BRD_111_CodedStep2()
        {
            if(Pages.PS_BrandingCustomMessages.DisplaySelfRegChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplaySelfRegChkBox.Click();
            }
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_111_CodedStep3()
        {
            ActiveBrowser.Window.SetFocus();
            string oldText = Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea.BaseElement.InnerText;
            SetExtractedValue("OldText",oldText);
            string msgText = "Welcome to PowerSteering";
            SetExtractedValue("MsgText",msgText);
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea,"");            
            Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_BRD_111_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify changes are reflected on Self Registration Text area")]
        public void TST_BRD_111_CodedStep5()
        {
            Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea.Wait.ForExists();
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea.BaseElement.InnerText.Contains(GetExtractedValue("MsgText").ToString()));
        }
    
        [CodedStep(@"Set default message and save the changes")]
        public void TST_BRD_111_CodedStep6()
        {
            Actions.SetText(Pages.PS_BrandingCustomMessages.SelfRegMessageTextArea,GetExtractedValue("OldText").ToString());
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
