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

    public class TST_FAV_007 : BaseWebAiiTest
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
        public void TST_FAV_007_CodedStep()
        {
            Pages.PS_HomePage.FavoritesLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            Assert.IsTrue(Pages.PS_HomePage.ManageFavoritesLeftNavDiv.IsVisible(),"Favorites left should be displayed on mouuse hover");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_007_CodedStep1()
        {
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.NavMenuPinDiv.IsVisible(),"Moving mouse away from left nav should hide the left nav when unpinned");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_007_CodedStep2()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            Assert.IsTrue(Pages.PS_HomePage.HomeLeftNavLink.Attributes.Single(x => x.Name == "title").Value.Equals("Home"),"Home icon on hover tool tip text should be 'Home'");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_007_CodedStep3()
        {
            Pages.PS_HomePage.CenterContainerDiv.MouseHover();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            Assert.IsTrue(Pages.PS_HomePage.ManageFavoritesLeftNavDiv.IsVisible(),"Favorites left should be displayed on mouuse hover");
        }
    }
}
