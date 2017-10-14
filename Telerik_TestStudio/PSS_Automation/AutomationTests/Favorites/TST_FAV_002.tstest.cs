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

    public class TST_FAV_002 : BaseWebAiiTest
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
    
        //[CodedStep(@"Get Page URL for reference")]
        //public void TST_Pin_A_Favorited_Page_CodedStep()
        //{
            //SetExtractedValue("PageURL", ActiveBrowser.Url);
        //}
    
        //[CodedStep(@"Verify Page title is pre populated in Name field ")]
        //public void TST_Pin_A_Favorited_Page_CodedStep1()
        //{
            //Element nameInput = ActiveBrowser.Find.ById("addToFavouritesURL");
            //string attr = nameInput.GetAttribute("value").Value;
            //string currPageUrl = GetExtractedValue("PageURL").ToString();
            //Assert.IsTrue(attr.Equals(currPageUrl), "Name field should be pre populated with current page title");
        //}
    
        [CodedStep(@"Enter all details and add current page to favorites")]
        public void TST_Pin_A_Favorited_Page_CodedStep2()
        {
            string name = "Pinned New Project "+ Randomizers.generateRandomInt(1,1000).ToString();
            string desc = "this is favorited project description";
                
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogNameText, name);
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogDescTextArea, desc);  
            Pages.PS_HomePage.AddToFavDialogPinnedCheckBox.Click();
            Pages.PS_HomePage.AddToFavDialogAddButton.Click();
            ActiveBrowser.WaitUntilReady();
            
            SetExtractedValue("FavoritesName",name);
            SetExtractedValue("FavoritesDesc",desc);
        }
    
        [CodedStep(@"Verify new page is added to favorites nav menu list and it navigates to correct page")]
        public void TST_Pin_A_Favorited_Page_CodedStep3()
        {
            string pinnedPageMenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),GetExtractedValue("FavoritesName"));
            HtmlAnchor pinnedElement = ActiveBrowser.Find.ByXPath<HtmlAnchor>(pinnedPageMenuLocator);
            Assert.IsTrue(pinnedElement.IsVisible(), "Newly added page should be listed in Pinned Pages Menu");
          
            HtmlDiv child = pinnedElement.Find.ByXPath<HtmlDiv>("//div");
            child.MouseHover();
            string expectedMouseHoverStr = GetExtractedValue("FavoritesName").ToString();
            Assert.IsTrue(child.BaseElement.GetAttribute("title").Value.Equals(expectedMouseHoverStr),"Mouse hover on pinned page should display description");
            
        }
    
        [CodedStep(@"Verify newly added page is present in the favorites list grid")]
        public void TST_Pin_A_Favorited_Page_CodedStep4()
        {
            int gridItemIndex = 0;
            
            IList<Element> favPageElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_fav_grid_name_items"));
            
            for(int i=1 ; i <= favPageElements.Count; i++){
                
                if(favPageElements[i-1].InnerText.Contains(GetExtractedValue("FavoritesName").ToString())){
                 gridItemIndex = i;
                 break;   
                }
            }
            Assert.IsTrue(gridItemIndex !=0, "Favorited Page Name should be present in the grid");
            
            
            
            SetExtractedValue("FavoritedIndex", gridItemIndex);
        }
    
        [CodedStep(@"Delete given favorited page from list and verify")]
        public void TST_Pin_A_Favorited_Page_CodedStep5()
        {
            HtmlSpan favoritedPageDeleteElement = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("manage_fav_grid_delete_icon_items")+"["+GetExtractedValue("FavoritedIndex").ToString()+"]//span[@title='Delete item']");
            favoritedPageDeleteElement.Click();
            
            Pages.PS_FavoritesPage.DelPopupDeleteButton.Wait.ForVisible();
            Pages.PS_FavoritesPage.DelPopupDeleteButton.Click();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            ActiveBrowser.WaitUntilReady();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
            
            Element favoritedPageNameElement = ActiveBrowser.Find.ByXPath(AppLocators.get("manage_fav_grid_name_items")+"["+GetExtractedValue("FavoritedIndex").ToString()+"]");
            Assert.IsTrue(!favoritedPageNameElement.InnerText.Contains(GetExtractedValue("FavoritesName").ToString()),"Favorited Page Should be deleted from the grid");
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_FAV_002_CodedStep()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_FAV_002_CodedStep()
        {
            string pinnedPageMenuLocator = string.Format(AppLocators.get("left_nav_pinned_menu_item"),GetExtractedValue("FavoritesName"));
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(pinnedPageMenuLocator).Count>0, "Newly added page should be listed in Pinned Pages Menu");
        }
    }
}
