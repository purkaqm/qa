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

    public class TST_BRD_059 : BaseWebAiiTest
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
        public void TST_BRD_059_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdAcronymTag.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdAcronymTag.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdAcronymTag.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdAcronymTag.MouseHover();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_059_CodedStep1()
        {
            string redColor = Data["Red_Color"].ToString();
            HtmlDiv leftNavBkgdLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_left_nav_bkgd_color_left"));
            leftNavBkgdLoc.Wait.ForExists();
            leftNavBkgdLoc.Wait.ForVisible();
            Assert.IsTrue(leftNavBkgdLoc.BaseElement.GetAttribute("style").Value.ToString().Contains(redColor),"red color should be visiblie in top navigation background in preview frame");
            
            HtmlDiv leftNavBkgdLocR = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_left_nav_bkgd_color_right"));
            leftNavBkgdLocR.Wait.ForExists();
            leftNavBkgdLocR.Wait.ForVisible();
            Assert.IsTrue(leftNavBkgdLocR.BaseElement.GetAttribute("style").Value.ToString().Contains(redColor),"red color should be visiblie in top navigation background in preview frame");
        }
    }
}
