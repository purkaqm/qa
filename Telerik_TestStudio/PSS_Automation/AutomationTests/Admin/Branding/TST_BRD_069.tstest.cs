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

    public class TST_BRD_069 : BaseWebAiiTest
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
        public void TST_BRD_069_CodedStep()
        {
            string hexValue = Data["Enter_Hex_Dec_Value"].ToString();
            HtmlInputText hexColorLoc = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("branding_color_scheme_tnb_hex_dec_val_input"));
            hexColorLoc.Wait.ForExists();
            hexColorLoc.Wait.ForVisible();  
            hexColorLoc.ScrollToVisible();
            Actions.SetText(hexColorLoc,"20b2aa");
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            System.Threading.Thread.Sleep(4000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_069_CodedStep1()
        {
            string selectedColor = Data["Select_Color"].ToString();            
            HtmlDiv selColorLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_top_nav_bkgd_color_view"));
            selColorLoc.Wait.ForExists();
            selColorLoc.Wait.ForVisible();
            selColorLoc.ScrollToVisible();
            Assert.IsTrue( selColorLoc.BaseElement.GetAttribute("style").Value.ToString().Contains(selectedColor),"appropriate color should be visible in top navigation background field");
    
        }
    }
}
