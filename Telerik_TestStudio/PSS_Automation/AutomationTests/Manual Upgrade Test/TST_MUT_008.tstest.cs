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

    public class TST_MUT_008 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MUT_008_ClickSearchIcon()
        {
            /// \bug Should be made into function, used in far too many places
            Pages.PS_HomePage.SearchLink.Click();
            /// \remarks Without this pause, the asserts fail, probably because the UI changes visually
            System.Threading.Thread.Sleep(1000);
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Set Search value")]
        public void TST_MUT_008_SetSearchValue()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Pages.PS_HomePage.SearchInputText.Text = Data["SearchItem"].ToString();
            Pages.PS_HomePage.SearchInputText.InvokeEvent(ScriptEventType.OnKeyUp);
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            System.Threading.Thread.Sleep(5000);               
        }
    
        
    
        [CodedStep(@"Verify results are shown respective to search key word")]
        public void TST_MUT_008_SearchResult()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("quick_search_input"),Data["SearchItem"].ToString())).Count > 0);
            
        }
        
        [CodedStep(@"Verify key words are in Bold")]
        public void TST_MUT_008_BoldKeyword()
        {
            ActiveBrowser.RefreshDomTree();
            IList<HtmlControl> searchResultList = ActiveBrowser.Find.AllByXPath<HtmlControl>(string.Format(AppLocators.get("quick_search_result_bold_element"),Data["SearchItem"].ToString()));
            Log.WriteLine(searchResultList.Count.ToString());
            foreach(HtmlControl listOption in searchResultList)
            {
                Assert.IsTrue(listOption.TagName.Equals("b"));
            }
        }
        
    }
}
