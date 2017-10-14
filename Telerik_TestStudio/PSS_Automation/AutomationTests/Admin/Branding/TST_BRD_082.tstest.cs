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

    public class TST_BRD_082 : BaseWebAiiTest
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
        public void TST_BRD_082_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Login Licence Message link")]
        public void TST_BRD_082_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Click();
        }
    
        [CodedStep(@"Write message in message box area")]
        public void TST_BRD_082_CodedStep2()
        {
            ActiveBrowser.Window.SetFocus();
            string prevMsg = Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.BaseElement.InnerText;
            SetExtractedValue("PrevMsg",prevMsg);
            string msgText = "Test Message";
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea,"");
            
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);           
        }
    
        [CodedStep(@"Cancel the changes")]
        public void TST_BRD_082_CodedStep3()
        {
            Pages.PS_BrandingCustomMessages.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify changes  are cancelled")]
        public void TST_BRD_082_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.BaseElement.InnerText.Equals(GetExtractedValue("PrevMsg")));
        }
    }
}
