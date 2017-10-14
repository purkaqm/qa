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

    public class TST_MAT_053_C992366 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Save and Cancel button is present in Look and Feel tab")]
        public void TST_MAT_053_C992366_VerifySaveAndCancelButton()
        {
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.SaveBtn.IsVisible());
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.CancelBtn.IsVisible());
        }
    
        [CodedStep(@"Verify Company Logos with Section is present in Look and Feel tab ")]
        public void TST_MAT_053_C992366_VerifyCompanyLogos()
        {
            HtmlTableCell clearImgLocHL = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_h_logo_clear_image_table_cell"));
            HtmlTableCell clearImgLocHcon = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_h_logo_hcon_clear_image_table_cell"));
            HtmlTableCell clearImgLocPL = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_clear_image_print_logo_table_cell"));
            HtmlTableCell clearImgLocLL = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_login_logo_clear_image_td_cell"));
            //HtmlTableCell clearImgLocEL = ActiveBrowser.Find.ByXPath<HtmlTableCell>();
            if(!clearImgLocHL.IsVisible()){
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.HeaderLogoImg.IsVisible(),"Header logo image should be displayed");
            }
            if(!clearImgLocHcon.IsVisible()){
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.HeaderLogoHcImg.IsVisible(),"Header logo high contrast view image should be displayed");
            }
            if(!clearImgLocPL.IsVisible()){
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PrintLogoImg.IsVisible(),"Print logo image should be displayed");
            }
            if(!clearImgLocLL.IsVisible()){
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.LoginLogoImg.IsVisible(),"Email logo image should be displayed");
            }
            //Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ReportLogoImg.IsVisible(),"Report logo image should be displayed");
        }
    
        [CodedStep(@"Verify Color Scheme section is present in Look and Feel tab")]
        public void TST_MAT_053_C992366_Verify_Color_Schema()
        {
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ColorSchemeTable.IsVisible());
        }
    }
}
