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

    public class TST_BRD_065 : BaseWebAiiTest
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
        public void TST_BRD_065_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible(),"pallete color box should be displayed");
            Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep2()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep3()
        {
            Pages.PS_BrandingLookAndFeelPage.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep5()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep6()
        {
            
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep7()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep8()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep9()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep10()
        {
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep11()
        {
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_065_CodedStep12()
        {
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    }
}
