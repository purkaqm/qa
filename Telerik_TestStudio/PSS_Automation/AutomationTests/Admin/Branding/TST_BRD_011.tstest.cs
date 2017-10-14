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

    public class TST_BRD_011 : BaseWebAiiTest
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
    
        [CodedStep(@"Mousehover to Trashcan icon")]
        public void TST_BRD_011_CodedStep6()
        {
                        Pages.PS_BrandingLookAndFeelPage.HeaderLogoHConDelImgSpan.MouseHover();
        }
    
        [CodedStep(@"Verify Delete image and reset to default message is shown after hovering on Trashcan icon")]
        public void TST_BRD_011_CodedStep7()
        {
                        string comDelSpanVal = "Delete image and reset to default";
                        string DelSpanVal = Pages.PS_BrandingLookAndFeelPage.HeaderLogoHConDelImgSpan.BaseElement.GetAttribute("title").Value.ToString();
                        Assert.IsTrue(comDelSpanVal.Equals(DelSpanVal));
        }
    
        [CodedStep(@"Click on Trashcan icon to delete header logo")]
        public void TST_BRD_011_CodedStep3()
        {
                       
                        Pages.PS_BrandingLookAndFeelPage.HeaderLogoHConDelImgSpan.Click();
                        ActiveBrowser.WaitUntilReady();
                        System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Save the changes")]
        public void TST_BRD_011_CodedStep4()
        {
                        Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        System.Threading.Thread.Sleep(6000);
        }
    
        [CodedStep(@"Verify Image Preview Frame is empty ")]
        public void TST_BRD_011_CodedStep5()
        {
                        
                        HtmlTableCell clearImgLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_clear_image_h_logo_hcon_table_cell"));
                        clearImgLoc.Wait.ForExists();
                        Assert.IsTrue(clearImgLoc.IsVisible(),"Clear image locator should be visible");
        }
    }
}
