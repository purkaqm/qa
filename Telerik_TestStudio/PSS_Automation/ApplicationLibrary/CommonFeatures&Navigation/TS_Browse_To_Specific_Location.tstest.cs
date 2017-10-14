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

    public class TS_Browse_To_Project_Location : BaseWebAiiTest
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
        public void TS_CS01()
        {
            Pages.PS_HomePage.SearchLink.Click();
            /// \remarks Without this pause, the asserts fail, probably because the UI changes visually
            //System.Threading.Thread.Sleep(1000);
            Pages.PS_HomePage.SearchTypeImgIconSpan.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
        
        [CodedStep(@"Set Search value")]
        public void TS_CS02()
        {
            /// \remarks See if this fixes the failing search.
            /// GIve indexer time to index.
            /// Better thing would have been to wait after creation and
            /// not before search. That way, search, which can happen many times
            /// need not wait unnecessarily.
            System.Threading.Thread.Sleep(5 * 1000);
            
            ActiveBrowser.RefreshDomTree();

            Pages.PS_HomePage.SearchInputText.Focus();
            
            Pages.PS_HomePage.SearchInputText.Text = CustomUtils.locationValue;
            Pages.PS_HomePage.SearchInputText.InvokeEvent(ScriptEventType.OnKeyUp);            
            
            ActiveBrowser.WaitForAjax(30000);
            System.Threading.Thread.Sleep(5000);
            
        }
        
        [CodedStep(@"Click on Search value link")]
        public void TS_CS03()
        {
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("quick_search_input"),CustomUtils.locationValue));
            HtmlListItem searchItem = ActiveBrowser.Find.ByXPath<HtmlListItem>(string.Format(AppLocators.get("quick_search_input"),CustomUtils.locationValue));
            /// \bug Should we be doing Assert for null here? Currently, null searchItem throws exception
            searchItem.Wait.ForExists();
            
            searchItem.Click();
            searchItem.InvokeEvent(ScriptEventType.OnMouseUp);
            
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    }
}
