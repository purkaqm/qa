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

    public class TST_HMC_020 : BaseWebAiiTest
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
    
        [CodedStep(@"Click Add New Button")]
        public void CS_HMC_020_01()
        {
            Pages.PS_ReviewGroupsPage.GroupsAddNewButton.Wait.ForExists();
            Pages.PS_ReviewGroupsPage.GroupsAddNewButton.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_AddEditUserGroupPage.GroupNameInputText.Wait.ForExists();
            Pages.PS_AddEditUserGroupPage.SaveChangesButton.Wait.ForExists();
        }
    
        [CodedStep(@"Create new group with current user added")]
        public void CS_HMC_020_02()
        {
            
            string groupName = "AutoGRP"+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Actions.SetText(Pages.PS_AddEditUserGroupPage.GroupNameInputText,groupName);
            Actions.SetText(Pages.PS_AddEditUserGroupPage.GroupDescriptionInputTextArea,"Sample Group Description");
            
            
            Actions.SetText(Pages.PS_AddEditUserGroupPage.FindUserInputBox,GetExtractedValue("CurrentUsername").ToString());
            Pages.AddEditGroupProfile.GoButton.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree(); 
            
            
            Pages.PS_AddEditUserGroupPage.SearchedUserLink.Wait.ForExists();
            Pages.PS_AddEditUserGroupPage.SearchedUserLink.Wait.ForVisible();
            
            Pages.PS_AddEditUserGroupPage.SearchedUserLink.DragTo(Pages.PS_AddEditUserGroupPage.AddedUsersArea);
            System.Threading.Thread.Sleep(3000);
            
            Pages.PS_AddEditUserGroupPage.SaveChangesButton.Click();
            ActiveBrowser.WaitUntilReady();
            
            Pages.PS_GroupProfilePage.EditGroupLink.Wait.ForExists();
            
            SetExtractedValue("GeneratedGroupName",groupName);
    
        }
    
        [CodedStep(@"Click Add new configuration link")]
        public void CS_HMC_020_03()
        {
               Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
               ActiveBrowser.WaitUntilReady();
                                                                                                    
        }
    
        [CodedStep(@"Wait till user navigates to Add/Edit configuration page")]
        public void CS_HMC_020_04()
        {
               Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
               Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
               Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
               Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
                                                
                                                                                                                        
        }
    
        [CodedStep(@"Enter Name, description and select few modules")]
        public void CS_HMC_020_05()
        {
            string configName = "AutoHMC"+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Actions.SetText(Pages.PS_AddEditHomePage.NewConfigNameInput,configName);
            SetExtractedValue("GeneratedHMCName",configName);
            
            Actions.SetText(Pages.PS_AddEditHomePage.NewConfigDescTextArea,"Sample Config Description");
            
             string[] homePageOptions = {"My Dashboard",
                                            "My Organizations"
                                        };
            
               foreach (string e in homePageOptions) {
                   string optLocator = string.Format(AppLocators.get("new_hmc_config_record_chkbx"),e);
                   HtmlInputCheckBox chkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(optLocator);
                   if(!chkbox.Checked){
                       chkbox.Click();
                       System.Threading.Thread.Sleep(2000);
                   }
               }     
                                        
        }
    
        [CodedStep(@"Click Save button")]
        public void CS_HMC_020_06()
        {
                                                  
            Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
            Pages.PS_AddEditHomePage.SaveButton.Click();
            ActiveBrowser.WaitUntilReady();
                                                    
        }
    
        [CodedStep(@"Wait for Admin - Configuration - Home Page to be displayed")]
        public void CS_HMC_020_07()
        {
            Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Home Page Configurations");
                
                                
        }
    
        [CodedStep(@"Click Change home pages link")]
        public void CS_HMC_020_08()
        {
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Click();
              ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                            
        }
    
        [CodedStep(@"wait till change home page popup is displayed with home page and user options")]
        public void CS_HMC_020_09()
        {
              Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.UsersDropdown.Wait.ForExists(); 
          
                                                            
        }
    
        [CodedStep(@"Change the newly created empty home page config")]
        public void CS_HMC_020_10()
        {
             Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.SelectByText(GetExtractedValue("GeneratedHMCName").ToString(), true);
                          
        }
    
        [CodedStep(@"Select groups option from Users dropdowns")]
        public void CS_HMC_020_11()
        {
            Pages.PS_HomePageConfigurationsPage.UsersDropdown.SelectByValue("GROUP", true);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            ActiveBrowser.RefreshDomTree(); 
            Pages.PS_HomePageConfigurationsPage.UserGroupSelect.Wait.ForExists();
                                     
        }
        
        [CodedStep(@"Select newly created group")]
        public void CS_HMC_020_12()
        {
            Pages.PS_HomePageConfigurationsPage.UserGroupSelect.SelectByText(GetExtractedValue("GeneratedGroupName").ToString(), true);
            
        }
        
         [CodedStep(@"Click OK and Confirm Home Page Selection")]
        public void CS_HMC_020_13()
        {
                                     
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageOkBtn.Click();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Click();                                               
              ActiveBrowser.WaitUntilReady();
        }
        
        
    
        [CodedStep(@"Click home Link Left Panel")]
        public void CS_HMC_020_14()
        {
                                      
                Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
                Pages.PS_HomePage.HomeLeftNavLink.Click();
                ActiveBrowser.WaitUntilReady();
                                                            
        }
    
        [CodedStep(@"Verify selected modules are present for group users")]
        public void CS_HMC_020_15()
        {
                string myDashLocator = string.Format(AppLocators.get("home_page_module_item"),"Dashboard");
                string myOrgsLocator = string.Format(AppLocators.get("home_page_module_item"),"Organizations");
                
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myDashLocator).Count > 0, "Dashboard should be present on home page");
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myOrgsLocator).Count > 0, "My Organizations Review should be present on home page");
                
        }
    
        [CodedStep(@"Delete created configuration")]
        public void CS_HMC_020_16()
        {
                                                    
                System.Threading.Thread.Sleep(5000);
                string configName = GetExtractedValue("GeneratedHMCName").ToString();
                string deleteIconLoc = string.Format(AppLocators.get("home_page_config_record_delete_icon"),configName);
                HtmlImage deleteIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(deleteIconLoc);
                deleteIcon.Click();
                
                Pages.PS_HomePageConfigurationsPage.DeleteConfirmBtn.Wait.ForExists();
                Pages.PS_HomePageConfigurationsPage.DeleteConfirmBtn.Click();
                ActiveBrowser.WaitUntilReady();
                ActiveBrowser.RefreshDomTree();
        }
    }
}
