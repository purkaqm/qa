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

    public class TST_BRD_018 : BaseWebAiiTest
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
        public void TST_BRD_018_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.HeaderLogoHConDelImgSpan.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_018_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_018_CodedStep2()
        {
            HtmlTableCell clearImgLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_h_logo_hcon_clear_image_table_cell"));
            clearImgLoc.Wait.ForExists();
            Assert.IsTrue(clearImgLoc.IsVisible(),"Clear image locator should be visible");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_018_CodedStep3()
        {
            HtmlSpan  defaultLogoSpanLoc = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("brandind_default_logo_span"));
            defaultLogoSpanLoc.Wait.ForExists();
            Assert.IsTrue(defaultLogoSpanLoc.IsVisible(),"Default powersteering image is shown in header");
        }
    }
}
