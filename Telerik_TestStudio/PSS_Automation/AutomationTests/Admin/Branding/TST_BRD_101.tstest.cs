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

    public class TST_BRD_101 : BaseWebAiiTest
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
        public void TST_BRD_101_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Request Submittal Page Disclaimer link")]
        public void TST_BRD_101_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.ReqSubPageDisclaimerLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.ReqSubPageDisclaimerLink.Click();
        }
    
        [CodedStep(@"Checked Display on page")]
        public void TST_BRD_101_CodedStep2()
        {
            if(!Pages.PS_BrandingCustomMessages.DisplayDisclaimerMsgChkBox.Checked){
            Pages.PS_BrandingCustomMessages.DisplayDisclaimerMsgChkBox.Click();
            }
        }
    
        [CodedStep(@"Leave message field empty in Request Submittal Page Disclaimer section")]
        public void TST_BRD_101_CodedStep3()
        {
            Actions.SetText(Pages.PS_BrandingCustomMessages.RequestDisclaimerMsgTxtArea,"");
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_BRD_101_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify warning is displayed")]
        public void TST_BRD_101_CodedStep5()
        {
            HtmlDiv warningMsgLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_empty_txt_warning_msg"));
            warningMsgLoc.Wait.ForExists();
            Assert.IsTrue(warningMsgLoc.IsVisible(),"Warning meassage should be displayed");
        }
    
        [CodedStep(@"Click on Cancel button")]
        public void TST_BRD_101_CodedStep6()
        {
            Pages.PS_BrandingCustomMessages.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
