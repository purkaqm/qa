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

    public class TST_MUT_013 : BaseWebAiiTest
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
        
        [CodedStep(@"Set search item")]
        public void TST_MUT_013_SetSearchItem()
        {
            /// \bug Is this mentioned in the data setup? Or is dynamic?
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MUT_013_ClickSearchIcon()
        {
            /// \bug Convert to a function
            Pages.PS_HomePage.SearchLink.Click();
            /// \remarks Without this pause, the asserts fail, probably because the UI changes visually
            System.Threading.Thread.Sleep(1000);
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Type Search value")]
        public void TST_MUT_013_TypeSearchValue()
        {
            /// \bug The search is a common feature. Should be made a method.            
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Pages.PS_HomePage.SearchInputText.Text = CustomUtils.locationValue; 
            Pages.PS_HomePage.SearchInputText.InvokeEvent(ScriptEventType.OnKeyUp);
            ActiveBrowser.WaitForAjax(30000);
            System.Threading.Thread.Sleep(5000);
        }
    
    
        [CodedStep(@"Verify Search item is displayed at list of results ")]
        public void TST_MUT_013_VerfyProject()
        {
            /// \bug XPath should be moved to central place
            ActiveBrowser.RefreshDomTree();
            IList<HtmlListItem> searchResultList = ActiveBrowser.Find.AllByXPath<HtmlListItem>("//ul[contains(@id,'SearchResults')]/li");
            Log.WriteLine(searchResultList.Count.ToString());
            bool flag = false;
            foreach(HtmlListItem listItem in searchResultList)
            {
                Log.WriteLine(listItem.BaseElement.InnerText);
                if(listItem.BaseElement.InnerText.Equals(CustomUtils.locationValue))
                {
                    flag= true;
                    break;
                }
            }
            Assert.IsTrue(flag,"Project is not present in list");
        }
    }
}
