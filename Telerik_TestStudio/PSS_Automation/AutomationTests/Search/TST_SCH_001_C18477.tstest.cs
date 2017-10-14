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

    public class TST_SCH_001 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Search Icon.")]
        public void TST_SCH_001_VerifySearchIcon()
        {
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible());        
            Assert.AreEqual(0, String.Compare(Pages.PS_HomePage.SearchLink.Title.ToString(), "Search"));            
        }
    
        [CodedStep(@"Click Search Icon and Verify search components")]
        public void TST_SCH_001_ClickSearchIcon()
        {
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchBackgroundSpan.IsVisible());
            Assert.AreEqual(0, String.Compare(Pages.PS_HomePage.SearchBackgroundSpan.InnerText.ToString(), "Search..."));            
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchAdvancedDiv.IsVisible());
        }
    }
}
