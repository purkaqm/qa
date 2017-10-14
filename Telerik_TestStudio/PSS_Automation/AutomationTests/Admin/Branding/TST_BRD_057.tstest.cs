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

    public class TST_BRD_057 : BaseWebAiiTest
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
        public void TST_BRD_057_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighAcronymTag.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighAcronymTag.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighAcronymTag.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighAcronymTag.MouseHover();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_057_CodedStep1()
        {
            string redColor = Data["Red_Color"].ToString();
            HtmlListItem topNavIconColorLoc = ActiveBrowser.Find.ByXPath<HtmlListItem>(AppLocators.get("branding_color_scheme_top_nav_icon_high"));
            topNavIconColorLoc.Wait.ForExists();
            topNavIconColorLoc.Wait.ForVisible();
            Assert.IsTrue(topNavIconColorLoc.GetComputedStyle("color").Value.ToString().Contains(redColor),"red color should be visiblie in top navigation background in preview frame");
        }
    }
}
