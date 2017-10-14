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

    public class TST_FAV_001 : BaseWebAiiTest
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
        public void TST_Add_Page_To_Favorites_CodedStep()
        {
            Element nameInput = ActiveBrowser.Find.ById("addToFavouritesURL");
            string attr = nameInput.GetAttribute("value").Value;
            string currPageUrl = GetExtractedValue("PageURL").ToString();
            Assert.IsTrue(attr.Equals(currPageUrl), "Name field should be pre populated with current page title");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Add_Page_To_Favorites_CodedStep1()
        {
            SetExtractedValue("PageURL", ActiveBrowser.Url);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Add_Page_To_Favorites_CodedStep2()
        {
            string name = "Create New Project"+ Randomizers.generateRandomInt(1,1000).ToString();
            string desc = "this is favorited project description";
                
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogNameText, name);
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogDescTextArea, desc);  
            
            Pages.PS_HomePage.AddToFavDialogAddButton.Click();
            ActiveBrowser.WaitUntilReady();
            
            SetExtractedValue("FavoritesName",name);
            SetExtractedValue("FavoritesDesc",desc);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Add_Page_To_Favorites_CodedStep3()
        {
            string tabMenuLocator = string.Format(AppLocators.get("Favorites_menu_item_div"),GetExtractedValue("FavoritesName"));
            Log.WriteLine(tabMenuLocator);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(tabMenuLocator).Count>0, "Newly added page should be listed in Favorites navigation menu");
            HtmlDiv favTab = ActiveBrowser.Find.ByXPath<HtmlDiv>(tabMenuLocator);
            favTab.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.WaitForUrl(GetExtractedValue("PageURL").ToString(), false, Manager.Settings.ElementWaitTimeout);
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Add_Page_To_Favorites_CodedStep4()
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
            
            Element favoritedPageDescElement = ActiveBrowser.Find.ByXPath(AppLocators.get("manage_fav_grid_desc_items")+"["+gridItemIndex+"]");
            Assert.IsTrue(favoritedPageDescElement.InnerText.Contains(GetExtractedValue("FavoritesDesc").ToString()),"Favorited Page Description should be present in the grid");
            
            Element favoritedPageURLElement = ActiveBrowser.Find.ByXPath(AppLocators.get("manage_fav_grid_url_items")+"["+gridItemIndex+"]");
            Assert.IsTrue(favoritedPageURLElement.InnerText.Contains(GetExtractedValue("PageURL").ToString()),"Favorited Page URL should be present in the grid");
            
            SetExtractedValue("FavoritedIndex", gridItemIndex);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Add_Page_To_Favorites_CodedStep5()
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
    }
}
