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

    public class TST_BRD_001 : BaseWebAiiTest
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
        public void TST_BRD_001_CodedStep()
        {
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.SaveBtn.IsVisible());
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.CancelBtn.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep1()
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep2()
        {
           Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ColorSchemeTable.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep3()
        {
           
            Pages.PS_BrandingLookAndFeelPage.HeaderLogoDelImgSpan.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
       
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep5()
        {
            
            HtmlTableCell clearImgLoc = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("branding_h_logo_clear_image_table_cell"));
            clearImgLoc.Wait.ForExists();
            Assert.IsTrue(clearImgLoc.IsVisible(),"Clear image locator should be visible");
        }
    
       
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep6()
        {
            Pages.PS_BrandingLookAndFeelPage.HeaderLogoDelImgSpan.MouseHover();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_001_CodedStep7()
        {
            string comDelSpanVal = Data["ToolTipMsg"].ToString();
            string DelSpanVal = Pages.PS_BrandingLookAndFeelPage.HeaderLogoDelImgSpan.BaseElement.GetAttribute("title").Value.ToString();
            Assert.IsTrue(comDelSpanVal.Equals(DelSpanVal));
        }
    }
}
