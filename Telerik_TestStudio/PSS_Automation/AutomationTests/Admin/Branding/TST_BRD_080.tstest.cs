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

    public class TST_BRD_080 : BaseWebAiiTest
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
        public void TST_BRD_080_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_080_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.LoginLicMsgLink.Click();            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_080_CodedStep2()
        {            
            ActiveBrowser.Window.SetFocus();
            string msgText = "Test Message";
            ActiveBrowser.Actions.SetText(Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea,"");
            
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingCustomMessages.LoginLicMsgTxtArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(msgText);
                       
            ActiveBrowser.RefreshDomTree();
            //string acceptedName = Actions.InvokeScript<string>("document.getElementById(\"addToFavouritesName\").value");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_080_CodedStep3()
        {
            HtmlDiv starMark = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_login_lic_msg_starmark"));
            starMark.Wait.ForExists();
            Assert.IsTrue(starMark.IsVisible(),"Starmark is visible near the section name");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_080_CodedStep4()
        {
            Pages.PS_BrandingCustomMessages.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
