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

    public class TST_BRD_068 : BaseWebAiiTest
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
        
        string selectedColor;
    
        [CodedStep(@"Click on pallete of top navigation background")]
        public void TST_BRD_068_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Verify pallete is displayed")]
        public void TST_BRD_068_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible(),"Pallete should be visible");
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Select color")]
        public void TST_BRD_068_CodedStep2()
        {
            selectedColor = Data["Select_Color"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.Click();
            Assert.IsTrue(!Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible());
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Verify star mark is displayed ")]
        public void TST_BRD_068_CodedStep3()
        {
            HtmlDiv starMark = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_star_mark"));
            starMark.Wait.ForExists();
            Assert.IsTrue(starMark.IsVisible(),"starmark should be visible");
        }
    
        [CodedStep(@"Cancel the changes")]
        public void TST_BRD_068_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Click();
            System.Threading.Thread.Sleep(4000);
        }
    
        [CodedStep(@"Verify that changes  are returned to default")]
        public void TST_BRD_068_CodedStep5()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.Wait.ForVisible();
            string whiteColor = Data["White_Color"].ToString();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.TopNavBkgdColorViewDiv.BaseElement.GetAttribute("style").Value.ToString().Contains(whiteColor),"Default white color is displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_068_CodedStep6()
        {
            Pages.PS_BrandingLookAndFeelPage.ResetBtn.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_068_CodedStep7()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            System.Threading.Thread.Sleep(4000);
        }
    }
}
