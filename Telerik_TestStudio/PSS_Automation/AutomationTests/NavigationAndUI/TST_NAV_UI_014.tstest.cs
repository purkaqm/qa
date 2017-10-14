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

    public class TST_NAV_UI_014 : BaseWebAiiTest
    {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

        
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
        public void TST_NAV_UI_014_CodedStep()
        {
            Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            string barColor = Data["Expected_Color"].ToString();
            Log.WriteLine(Pages.PS_HomePage.HomeLeftNavLink.GetComputedStyleValue("color"));
            Log.WriteLine(barColor);
            Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.GetComputedStyleValue("color").Equals(barColor),"color should be same as selected ");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_014_CodedStep1()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.ReviewLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            string barColor = Data["Expected_Color"].ToString();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.GetComputedStyleValue("color").Equals(barColor),"color should be same as selected ");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_014_CodedStep2()
        {
            Pages.PS_HomePage.AddLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.AddLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            string barColor = Data["Expected_Color"].ToString();
            Assert.IsTrue(Pages.PS_HomePage.AddLeftNavLink.GetComputedStyleValue("color").Equals(barColor),"color should be same as selected ");
            
        }
    
        [CodedStep(@"Select color for left nav icon highlighting")]
        public void TST_NAV_UI_014_CodedStep3()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            
            
            string selectedColor = Data["Expected_Color"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.Click();
            System.Threading.Thread.Sleep(3000);
            
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.RefreshDomTree();
          
        }
    }
}
