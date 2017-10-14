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

    public class TST_MAT_040_C992428 : BaseWebAiiTest
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
        
        string mailId;
    
        [CodedStep(@"Set search item")]
        public void TST_MAT_040_C992428_SetSearchItem()
        {
                        
            CustomUtils.userName = "normal user";
        }
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MAT_040_C992428_ClickSearchIcon()
        {
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Type Search value")]
        public void TST_MAT_040_C992428_TypeSearchValue()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Manager.Desktop.KeyBoard.TypeText(CustomUtils.userName,1);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
                                                            
        }
    
        [CodedStep(@"Verify Search item is displayed at list of results")]
        public void TST_MAT_040_C992428_VerfyUser()
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
        public void TST_MAT_040_C992428_ClickSearchedItem()
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
        public void TST_MAT_040_C992428_VerifyUserPage()
        {
            Pages.PS_UserProfilePage.UserProfileListItemTab.Wait.ForExists();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("User Profile"));
                        
        }
    
        [CodedStep(@"Click on Edit user link")]
        public void TST_MAT_040_C992428_ClickEditUserLink()
        {
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify all required tabs on edit user page is displayed")]
        public void TST_MAT_040_C992428_VerifyEditUserTabs()
        {
            Pages.PS_SettingsProfilePage.ProfileListItem.Wait.ForExists();
            Pages.PS_SettingsProfilePage.PreferencesLink.Wait.ForExists();
            Pages.PS_SettingsProfilePage.PermissionsLink.Wait.ForExists();
            Pages.PS_SettingsProfilePage.AlertSubscriptionsLink.Wait.ForExists();
            Pages.PS_SettingsProfilePage.PersonalRatesLink.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_SettingsProfilePage.ProfileListItem.IsVisible(),"Profile tab should be displayed");
            Assert.IsTrue(Pages.PS_SettingsProfilePage.PreferencesLink.IsVisible(),"Preferences tab should be displayed");
            Assert.IsTrue(Pages.PS_SettingsProfilePage.PermissionsLink.IsVisible(),"Permissions tab should be displayed");
            Assert.IsTrue(Pages.PS_SettingsProfilePage.AlertSubscriptionsLink.IsVisible(),"Alert Subscriptions tab should be displayed");
            Assert.IsTrue(Pages.PS_SettingsProfilePage.PersonalRatesLink.IsVisible(),"Personal Rates tab should be displayed");
        }
        
    
        [CodedStep(@"Edit field(mail id) of user and save the changes")]
        public void TST_MAT_040_C992428_EditFieldAndSave()
        {
            mailId = "PM" + Randomizers.generateRandomInt(1000,9999) + "@gmail.com";
            Actions.SetText(Pages.PS_SettingsProfilePage.PrimaryEmailInput,mailId);
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify changes is reflected")]
        public void TST_MAT_040_C992428_VerifyChange()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath<HtmlTableRow>(string.Format("//div[@class='box']//tr[contains(.,'{0}')]",mailId)).Count > 0); 
        }
    }
}
