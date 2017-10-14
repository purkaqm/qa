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

    public class TST_FAV_004 : BaseWebAiiTest
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
    
        [CodedStep(@"Enter all details, Check Pin this Page checkbox and add current page to favorites")]
        public void TST_Verify_Go_To_Fav_Manager_Checkbox_CodedStep()
        {
            string name = "Favorited New Project "+ Randomizers.generateRandomInt(1,1000).ToString();
            
                
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogNameText, name);
            
            Pages.PS_HomePage.AddToFavDialogGoToFavCheckBox.Click();
            Pages.PS_HomePage.AddToFavDialogAddButton.Click();
            ActiveBrowser.WaitUntilReady();
            
            SetExtractedValue("FavoritesName",name);
            
        }
    
        [CodedStep(@"Verify new page is added to pinned pages list")]
        public void TST_Verify_Go_To_Fav_Manager_Checkbox_CodedStep1()
        {
            ActiveBrowser.WaitUntilReady();
            Pages.PS_FavoritesPage.FavouriteGridDiv.Wait.ForVisible();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
    
        [CodedStep(@"Verify newly added page is present in the favorites list grid")]
        public void TST_Verify_Go_To_Fav_Manager_Checkbox_CodedStep2()
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
        public void TST_Verify_Go_To_Fav_Manager_Checkbox_CodedStep3()
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
