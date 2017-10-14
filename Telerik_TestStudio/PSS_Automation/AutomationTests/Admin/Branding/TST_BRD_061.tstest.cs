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

    public class TST_BRD_061 : BaseWebAiiTest
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
        public void TST_BRD_061_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighAcronymTag.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighAcronymTag.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighAcronymTag.ScrollToVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighAcronymTag.MouseHover();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_061_CodedStep1()
        {
            string redColor = Data["red_Color"].ToString();
            IList<HtmlListItem> leftNavIconLoc = ActiveBrowser.Find.AllByXPath<HtmlListItem>(AppLocators.get("branding_color_scheme_left_nav_txt_icon_color_left"));            
            for(int i=0;i<leftNavIconLoc.Count;i++)
            {   
                leftNavIconLoc[i].Wait.ForExists();
                leftNavIconLoc[i].Wait.ForVisible();
                Assert.IsTrue(leftNavIconLoc[i].GetComputedStyle("color").Value.ToString().Contains(redColor),"red color should be visiblie in top navigation background in preview frame");
            }
        }
    }
}
