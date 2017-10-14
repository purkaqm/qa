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

    public class TST_BRD_047 : BaseWebAiiTest
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
        public void TST_BRD_047_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.ReportLogoDelImgSpan.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.ReportLogoDelImgSpan.MouseHover();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_047_CodedStep1()
        {
            string comDelSpanVal = "Delete image and reset to default";
            string DelSpanVal = Pages.PS_BrandingLookAndFeelPage.ReportLogoDelImgSpan.BaseElement.GetAttribute("title").Value.ToString();
            Assert.IsTrue(comDelSpanVal.Equals(DelSpanVal));
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_047_CodedStep2()
        {
            Pages.PS_BrandingLookAndFeelPage.ReportLogoDelImgSpan.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_047_CodedStep3()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(4000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_047_CodedStep4()
        {
            HtmlTableCell clearImgLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_report_logo_clear_img_td_cell"));
            clearImgLoc.Wait.ForExists();
            Assert.IsTrue(clearImgLoc.IsVisible(),"report logo clear image locator should be visible");
        }
    }
}
