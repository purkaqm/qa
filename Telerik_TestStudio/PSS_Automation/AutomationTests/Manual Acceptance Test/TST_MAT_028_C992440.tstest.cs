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

    public class TST_MAT_028_C992440 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to work summary page")]
        public void TST_MAT_028_C992440_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Summary"),"page tiltle should contain 'Summary' text");
                                                                                                                                       
        }
    
        [CodedStep(@"Click to Copy/Move link on left navigatiom panel menu")]
        public void TST_MAT_028_C992440_ClickCopyMoveTab()
        {
            Pages.PS_AddToFavoritesPopup.CopyMoveDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Select New Location")]
        public void TST_MAT_028_C992440_NewLocation()
        {
            Pages.PS_CreateOtherWorkPage.LocationInWorkTreeSpan.Click();
            Pages.PS_CreateOtherWorkPage.LocationPopupTitleSpan.Wait.ForVisible();
            if(Data["LocationType"].ToString().Contains("Search")){
                Pages.PS_CreateOtherWorkPage.LocationPopupSearchTab.Click();
                Pages.PS_CreateOtherWorkPage.LocationPopUpFindInput.Wait.ForExists();
                Actions.SetText(Pages.PS_CreateOtherWorkPage.LocationPopUpFindInput,Data["NewLocationPath"].ToString());
                Pages.PS_CreateOtherWorkPage.LocationPopupGoBtn.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("create_other_work_location_search_result_div"),Data["NewLocationPath"].ToString()));
                resDiv.Wait.ForVisible();
                resDiv.MouseClick(MouseClickType.LeftClick);
            }
        }
    
        [CodedStep(@"Click to Move button")]
        public void TST_MAT_028_C992440_ClickMoveButton()
        {
            Pages.PS_CopyMovePage.MoveButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Projcet has been successfully moved to another location")]
        public void TST_MAT_028_C992440_VerifyPoject()
        {
            HtmlAnchor locationLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("summary_page_parent_location_link"),Data["NewLocationPath"].ToString()));
            locationLink.Wait.ForExists();
            Assert.IsTrue(locationLink.IsVisible(),"New location link should be visible");
        }
    }
}
