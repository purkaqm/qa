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

    public class TST_MUT_015 : BaseWebAiiTest
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
        
        /// \bug This uses quick search instead of advanced project search.
        
        [CodedStep(@"Set project name in Global variable")]
        public void TST_MUT_015_CodedStep()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MUT_015_ClickSearchIcon()
        {
            /// \bug Should be made into function, used in far too many places
            Pages.PS_HomePage.SearchLink.Click();
            /// \remarks Without this pause, the asserts fail, probably because the UI changes visually
            System.Threading.Thread.Sleep(1000);
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
        
        [CodedStep(@"Click on SearchTypeTraingle")]
        public void TST_MUT_015_SearchTypeTraingle()
        {            
            Pages.PS_HomePage.SearchDownTraingleSpan.Click();
            Pages.PS_HomePage.SearchDownTraingleSpan.InvokeEvent(ScriptEventType.OnMouseUp);
            Pages.PS_HomePage.ProjectsSpanSearchCategory.Wait.ForExists();
        }
        
        [CodedStep(@"Click on Projects to select search category")]
        public void TST_MUT_015_ProjectsSpan()
        {
            string backgroundValue = Pages.PS_HomePage.ProjectsSpanSearchCategory.BaseElement.InnerText;
            Pages.PS_HomePage.ProjectsSpanSearchCategory.Click();            
            Assert.IsTrue(Pages.PS_HomePage.SearchBackgroundSpan.BaseElement.InnerText.Equals(backgroundValue));           
            
        }
        
        [CodedStep(@"Type Search value in Serach area")]
        public void TST_MUT_015_TypeSearhValue()
        {
            /// \bug The search is a common feature. Should be made a method.            
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Pages.PS_HomePage.SearchInputText.Text = CustomUtils.locationValue; 
            Pages.PS_HomePage.SearchInputText.InvokeEvent(ScriptEventType.OnKeyUp);
            ActiveBrowser.WaitForAjax(30000);
            System.Threading.Thread.Sleep(5000);
        }
        
        [CodedStep(@"Verify search project is displayed in project section")]
        public void TST_MUT_015_VerifySearchProject()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlListItem searchItem = ActiveBrowser.Find.ByXPath<HtmlListItem>(string.Format(AppLocators.get("quick_search_result_project_section"),CustomUtils.locationValue));
            searchItem.Wait.ForExists();
            Assert.IsTrue(searchItem.IsVisible());
            
        }
     
    }
}
