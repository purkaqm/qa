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

    public class TST_BRD_063 : BaseWebAiiTest
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
        public void TST_BRD_063_CodedStep()
        {
           Pages.PS_BrandingLookAndFeelPage.ContentBkgdAcronymTag.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdAcronymTag.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdAcronymTag.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdAcronymTag.MouseHover();
            ActiveBrowser.RefreshDomTree(); 
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_063_CodedStep1()
        {
            string redColor = Data["Red_Color"].ToString();
            HtmlDiv contentBkgdLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_content_bkgd_color"));
            contentBkgdLoc.Wait.ForExists();
            contentBkgdLoc.Wait.ForVisible();
            Assert.IsTrue(contentBkgdLoc.BaseElement.GetAttribute("style").Value.ToString().Contains(redColor),"red color should be visiblie in top navigation background in preview frame");
        }
    }
}
