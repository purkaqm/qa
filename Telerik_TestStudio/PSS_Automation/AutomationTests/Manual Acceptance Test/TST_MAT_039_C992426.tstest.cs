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

    public class TST_MAT_039_C992426 : BaseWebAiiTest
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
        public void TST_MAT_039_C992426_SetSearchItem()
        {
            
            CustomUtils.userName = "normal user";
        }
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MAT_039_C992426_ClickSearchIcon()
        {
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Type Search value")]
        public void TST_MAT_039_C992426_TypeSearchValue()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Manager.Desktop.KeyBoard.TypeText(CustomUtils.userName,1);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
                                                
        }
    
        [CodedStep(@"Verify Search item is displayed at list of results")]
        public void TST_MAT_039_C992426_VerfyUser()
        {
            ActiveBrowser.RefreshDomTree();
            IList<HtmlListItem> searchResultList = ActiveBrowser.Find.AllByXPath<HtmlListItem>("//ul[contains(@id,'peopleSearchResults')]/li");
            Log.WriteLine(searchResultList.Count.ToString());
            bool flag = false;
            foreach(HtmlListItem listItem in searchResultList)
            {
                
                string userFirstName = listItem.BaseElement.InnerText.Split(',')[1];
                string userLastName = listItem.BaseElement.InnerText.Split(',')[0];
                Log.WriteLine(userFirstName);
                Log.WriteLine(userLastName);
                if(CustomUtils.userName.Contains(userFirstName) && CustomUtils.userName.Contains(userLastName))
                {
                    flag= true;
                    break;
                }
            }
            Assert.IsTrue(flag,"User is not present in list");
        }
        
        [CodedStep(@"Click on Search value link")]
        public void TST_MAT_039_C992426_ClickSearchedItem()
        {
            string firstname = CustomUtils.userName.Split(' ')[0];
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("quick_search_result_people_section"),firstname));
            HtmlListItem searchItem = ActiveBrowser.Find.ByXPath<HtmlListItem>(string.Format(AppLocators.get("quick_search_result_people_section"),firstname));
            searchItem.Wait.ForExists();
            searchItem.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is redirected to user profile page")]
        public void TST_MAT_039_C992426_VerifyUserPage()
        {
            Pages.PS_UserProfilePage.UserProfileListItemTab.Wait.ForExists();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("User Profile"));
            
        }
        
        [CodedStep(@"Verify all required tab is present in profile page")]
        public void TST_MAT_039_C992426_VerifyUserTabs()
        {
            Assert.IsTrue(Pages.PS_UserProfilePage.UserProfileListItemTab.IsVisible(),"User Profile tab should be visible");
            Assert.IsTrue(Pages.PS_UserProfilePage.DeleteLink.IsVisible(),"Delete tab should be displayed");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"Edit User tab should be visible");
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document tab should be displayed");
        }
    }
}
