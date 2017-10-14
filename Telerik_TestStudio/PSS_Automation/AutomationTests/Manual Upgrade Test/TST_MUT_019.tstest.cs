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

    public class TST_MUT_019 : BaseWebAiiTest
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
        
        int count;
        string nameFavLink;
    
        [CodedStep(@"Set search item")]
        public void TST_MUT_019_SetSearchItem()
        {
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify summary page is opened")]
        public void TST_MUT_019_VerifySummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(CustomUtils.locationValue));
        }
    
        [CodedStep(@"Click on Favorite icon on header")]
        public void TST_MUT_019_ClickFavoriteIcon()
        {
            Pages.PS_HomePage.HeaderFavoriteIconSpan.Click();
                        
        }
    
        [CodedStep(@"Fill the details")]
        public void TST_MUT_019_FillDetails()
        {
            nameFavLink = CustomUtils.locationValue + Randomizers.generateRandomInt(1000,9999);
            Pages.PS_AddToFavoritesPopup.AddButton.Wait.ForExists();
            Pages.PS_AddToFavoritesPopup.AddToFavouritesNameText.Wait.ForExists();
            Actions.SetText(Pages.PS_AddToFavoritesPopup.AddToFavouritesNameText,nameFavLink);
        }
    
        [CodedStep(@"Click on Add button on 'Add to favorite' popup")]
        public void TST_MUT_019_ClickAddButton()
        {
            Pages.PS_AddToFavoritesPopup.AddButton.Wait.ForExists();
            Pages.PS_AddToFavoritesPopup.AddButton.Click();
            Pages.PS_AddToFavoritesPopup.AddButton.Wait.ForVisibleNot();
            ActiveBrowser.Refresh(); /// \bug Refresh and not just DOM?
        }
        
         [CodedStep(@"Click on Favorite link on left navigation menu")]
        public void TST_MUT_019_ClickFavoriteLink()
        {
            Pages.PS_HomePage.FavoritesLeftNavLink.Click();
            Pages.PS_HomePage.ManageFavoritesLeftNavDiv.Wait.ForExists();
                        
        }
        
        [CodedStep(@"Click on Manage Favorite link")]
        public void TST_MUT_019_ClickManageFavLink()
        {          
            Pages.PS_HomePage.ManageFavoritesLeftNavDiv.Click();
            ActiveBrowser.WaitUntilReady(); 
            
        }
        
        [CodedStep(@"Verify table with Favorite list is displayed")]
        public void TST_MUT_019_VerifyTable()
        {    
            Pages.PS_FavoritesPage.FavouriteGridDiv.Wait.ForExists();
            Pages.PS_FavoritesPage.NameDiv.Wait.ForExists();
            Pages.PS_FavoritesPage.NameDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_FavoritesPage.FavouriteGridDiv.IsVisible());
            System.Threading.Thread.Sleep(4000);
        }
        
        [CodedStep(@"Verify project link is displayed in Favorite list items")]
        public void TST_MUT_019_VerifyProjectLink()
        {    
            ActiveBrowser.RefreshDomTree();
            IList<HtmlDiv> favLinks = ActiveBrowser.Find.AllByXPath<HtmlDiv>(AppLocators.get("favorite_list"));
            Log.WriteLine(favLinks.Count.ToString());
            count = 0;
            bool flag = false;
            foreach(HtmlDiv favLinkElement in favLinks)
            {
                count++;
                Log.WriteLine(favLinkElement.BaseElement.InnerText);
                if(favLinkElement.BaseElement.InnerText.Contains(nameFavLink))
                { 
                    flag = true;
                    break;
                }
            }
            Log.WriteLine(count.ToString());
            Assert.IsTrue(flag,nameFavLink + " link is not present in list");
        }
    
        [CodedStep(@"Delete a created project favorite link")]
        public void TST_MUT_019_DeleteLink()
        {
            HtmlSpan delFavLink = ActiveBrowser.Find.ByXPath<HtmlSpan>(string.Format(AppLocators.get("delete_favorite_link_span"),count.ToString()));
            delFavLink.Wait.ForExists();
            delFavLink.Click();
            Pages.PS_WorkGenerationTemplatesPage.DeleteButton.Click();
            System.Threading.Thread.Sleep(4000);
            ActiveBrowser.WaitUntilReady();
            Pages.PS_FavoritesPage.NameDiv.Wait.ForVisible();
        }
    }
}
