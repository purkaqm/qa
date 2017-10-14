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

    public class TST_MUT_014 : BaseWebAiiTest
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
        public void TST_MUT_014ClickSearchIcon()
        {
            /// \bug Make this a function, used too many times
            Pages.PS_HomePage.SearchLink.Click();
            System.Threading.Thread.Sleep(1000);
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Click on Advanced search link")]
        public void TST_MUT_014_SearchAdvanced()
        {
            Pages.PS_HomePage.SearchAdvancedDiv.Wait.ForExists();
            Pages.PS_HomePage.SearchAdvancedDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify Advanced search page is displayed with all required elements")]
        public void TST_MUT_014_VerifyAdvancedSearchElements()
        {
            /// \bug Missing asserts for search filters for individual tab
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.AllLink.IsVisible(),"All link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.ProjectsLink.IsVisible(),"Projects link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.OrganizationsLink.IsVisible(),"Organizations link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.WorksLink.IsVisible(),"Works link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.PeopleLink.IsVisible(),"People link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.IdeasTestLink.IsVisible(),"IdeaTest link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.DiscussionsLink.IsVisible(),"Discussions link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.IssuesLink.IsVisible(),"Issues link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.ActionItemsLink.IsVisible(),"Actions Items link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.DocumentsLink.IsVisible(),"Documents link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.ClearLink.IsVisible(),"Clear link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.SearchFiltersLink.IsVisible(),"Search Filters link should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.FunnelGifImage.IsVisible()," Funnel Image should be visible");
            //Assert.IsTrue(Pages.PS_AdvancedSearchPage.KeywordsPropertySelect.IsVisible(),"Property selcction should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.KeyboardInputTextField.IsVisible(),"Keywords TextField should be visible");
            Assert.IsTrue(Pages.PS_AdvancedSearchPage.SearchButton.IsVisible(),"Serach Button should be visible");
        }
    }
}
