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
    
     
    public class TST_BRD_077 : BaseWebAiiTest
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
        public void TST_BRD_077_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible(),"pallete color box should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_077_CodedStep1()
        {
            selectedColor = Data["Select_Color_Content_Backd"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.Click();
            System.Threading.Thread.Sleep(1000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_077_CodedStep2()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(4000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_077_CodedStep3()
        {
            HtmlDiv contBcKdLoc =ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_content_bckg_color_div"));
            Assert.IsTrue(contBcKdLoc.GetComputedStyle("background-color").Value.ToString().Contains(selectedColor));
        }
    }
}
