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

    public class TST_NAV_UI_015 : BaseWebAiiTest
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
    
        [CodedStep(@"Mouse hover on Favorites icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep1()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
       
        [CodedStep(@"Mouse hover on Favorites icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep2()
        {
            Pages.PS_HomePage.InboxLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"INBOX");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep3()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
        [CodedStep(@"Mouse hover on Inbox icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep4()
        {
            Pages.PS_HomePage.AddLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADD");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep5()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
        [CodedStep(@"Mouse hover on Inbox icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep6()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"REVIEW");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep7()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
        [CodedStep(@"Mouse hover on Inbox icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep8()
        {
            Pages.PS_HomePage.AdminLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADMIN");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep9()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
        [CodedStep(@"Mouse hover on Admin icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep10()
        {
            Pages.PS_HomePage.FavoritesLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep11()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
            
        }
    
        [CodedStep(@"Mouse hover on Favorites icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep12()
        {
            Pages.PS_HomePage.HistoryLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HISTORY");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep13()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    
        [CodedStep(@"Mouse hover on Favorites icon in left icon bar")]
        public void TST_NAV_UI_015_CodedStep14()
        {
            Pages.PS_HomePage.ImportantLinksLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"IMPORTANT LINKS");
            
        }
    
        [CodedStep(@"Move cursor away from menu and verify that left nav menu is hidden")]
        public void TST_NAV_UI_015_CodedStep15()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Equals("Home"),"page content title should be home");
        }
    }
}
