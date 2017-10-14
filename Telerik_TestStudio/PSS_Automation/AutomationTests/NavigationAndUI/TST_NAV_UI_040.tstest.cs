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

    public class TST_NAV_UI_040 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify User is navigated to Create Other Work Page")]
        public void TST_NAV_UI_040_CodedStep()
        {
            Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.Wait.ForExists();
            //Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.IsVisible());
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkDescriptionDiv.IsVisible());
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkContinueBtn.IsVisible());
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkCancelBtn.IsVisible());
            
            
            
            
            
        }
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_NAV_UI_040_CodedStep1()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
            
            
        }
    
        //[CodedStep(@"Verify that long page tilte is truncated")]
        //public void TST_NAV_UI_040_CodedStep2()
        //{
            //string workName = Data["WorkName"].ToString();
            //if(workName.Length >= 90){
                //string textOverflowProperty = Pages.PS_HomePage.PageTitleDiv.GetComputedStyleValue("text-overflow");    
                //Assert.IsTrue(textOverflowProperty.Equals("ellipsis"),"Long page title should be truncated...");
            //}
            
        //}
    
        [CodedStep(@"Verify that it displays entire title on hovering the mouse on page title")]
        public void TST_NAV_UI_040_CodedStep3()
        {
            string workName = Data["WorkName"].ToString();
            string tabMenuLocator = string.Format(AppLocators.get("Favorites_menu_item_div"),Data["WorkName"].ToString());
            HtmlDiv favTab = ActiveBrowser.Find.ByXPath<HtmlDiv>(tabMenuLocator);
            favTab.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Assert.IsTrue(favTab.Attributes.Single(x => x.Name == "title").Value.Contains(workName),"On hovering the page title, it should display full page title");
        }
    
        [CodedStep(@"Enter all details and add current page to favorites")]
        public void TST_NAV_UI_040_CodedStep4()
        {
            Pages.PS_HomePage.AddToFavDialogAddButton.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify new page is added to favorites nav menu list and it navigates to correct page")]
        public void TST_NAV_UI_040_CodedStep5()
        {
            string tabMenuLocator = string.Format(AppLocators.get("Favorites_menu_item_div"),Data["WorkName"].ToString());
            Log.WriteLine(tabMenuLocator);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(tabMenuLocator).Count>0, "Newly added page should be listed in Favorites navigation menu");
            HtmlDiv favTab = ActiveBrowser.Find.ByXPath<HtmlDiv>(tabMenuLocator);
            
            string textOverflowProperty = favTab.GetComputedStyleValue("text-overflow");    
            Assert.IsTrue(textOverflowProperty.Equals("ellipsis"),"Long page title should be truncated in favorites sub menu...");
            
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_040_CodedStep2()
        {
            string workName = Data["WorkName"].ToString();
            string tabMenuLocator = string.Format(AppLocators.get("Favorites_menu_item_div"),Data["WorkName"].ToString());
            HtmlDiv favTab = ActiveBrowser.Find.ByXPath<HtmlDiv>(tabMenuLocator);
            favTab.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify newly added page is present in the favorites list grid")]
        public void TST_NAV_UI_040_CodedStep6()
        {
            int gridItemIndex = 0;
            
            IList<Element> favPageElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_fav_grid_name_items"));
            
            for(int i=1 ; i <= favPageElements.Count; i++){
                
                if(favPageElements[i-1].InnerText.Contains(Data["WorkName"].ToString())){
                 gridItemIndex = i;
                 break;   
                }
            }
            Assert.IsTrue(gridItemIndex !=0, "Favorited Page Name should be present in the grid");
            SetExtractedValue("FavoritedIndex", gridItemIndex);
        }
    
        [CodedStep(@"Delete given favorited page from list and verify")]
        public void TST_NAV_UI_040_CodedStep7()
        {
            HtmlSpan favoritedPageDeleteElement = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("manage_fav_grid_delete_icon_items")+"["+GetExtractedValue("FavoritedIndex").ToString()+"]//span[@title='Delete item']");
            favoritedPageDeleteElement.Click();
            
            Pages.PS_FavoritesPage.DelPopupDeleteButton.Wait.ForVisible();
            Pages.PS_FavoritesPage.DelPopupDeleteButton.Click();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            ActiveBrowser.WaitUntilReady();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"Click home Link Left Panel")]
        public void TST_NAV_UI_040_CodedStep8()
        {
            // Desktop command: LeftClick on ReviewLeftNavLink
            Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.HomeLeftNavLink.Focus();
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForExists();
            System.Threading.Thread.Sleep(5000);  
        }
    }
}
