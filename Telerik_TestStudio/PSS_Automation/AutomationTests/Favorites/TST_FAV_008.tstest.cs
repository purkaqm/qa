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

    public class TST_FAV_008 : BaseWebAiiTest
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
        public void TST_FAV_008_CodedStep()
        {
            HtmlInputCheckBox checkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("manage_fav_any_pin_col_checkbox"));
            checkbox.Click();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_008_CodedStep1()
        {
            string saveBtnAttr = Pages.PS_FavoritesPage.SaveButton.BaseElement.GetAttribute("class").Value;
            Assert.IsFalse(saveBtnAttr.Contains("btn-disabled"),"Save button should be enabled after making changes");
        }
    
        [CodedStep(@"Click home Link Left Panel")]
        public void TST_FAV_008_CodedStep2()
        {
            // Desktop command: LeftClick on ReviewLeftNavLink
            Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    }
}
