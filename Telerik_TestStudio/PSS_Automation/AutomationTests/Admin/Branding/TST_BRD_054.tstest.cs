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

    public class TST_BRD_054 : BaseWebAiiTest
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
        public void TST_BRD_054_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);     
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep2()
        {
            string selectedColor = Data["Select_Color"].ToString();
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep3()
        {
            HtmlDiv starMark = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_star_mark"));
            starMark.Wait.ForExists();
            Assert.IsTrue(starMark.IsVisible(),"starmark should be visible");

        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep5()
        {
            Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
            ActiveBrowser.RefreshDomTree();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep6()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.Wait.ForVisible();
            string whiteColor = Data["White_Color"].ToString();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.BaseElement.GetAttribute("style").Value.ToString().Contains(whiteColor),"Default white color is displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_054_CodedStep7()
        {
            string selectedColor = Data["White_Color"].ToString();
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.MouseClick(MouseClickType.LeftClick);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
        }
    }
}
