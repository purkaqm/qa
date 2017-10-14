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

    public class TST_BRD_072 : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_072_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible(),"pallete color box should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_072_CodedStep1()
        {
            selectedColor = Data["Select_Color_Top_Nav_Icon_Hover"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.Click();
            Assert.IsTrue(!Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible());
            System.Threading.Thread.Sleep(1000); 
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_072_CodedStep2()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_072_CodedStep3()
        {
            Pages.PS_HomePage.HelpIconSpan.MouseHover();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.HelpIconSpan.GetComputedStyle("color").Value.ToString().Contains(selectedColor),"selected color should be displayed on top navigation icon hover");
        }
    }
}
