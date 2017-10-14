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

    public class TST_HMC_018 : BaseWebAiiTest
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
    
        [CodedStep(@"Created one new user")]
        public void CS_HMC_018_01()
        {
            string username = "autouser"+Randomizers.generateRandomInt(10000,99999);
            string email = username + "@mail.com";
            string password = "Auto123#";
        
            //Enter firstname, last name, email and username
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserFirstNameText,"AutoFirst");
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserLastNameText,"AutoLast");
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserEmailText,email);
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserUserNameText,username);
     
            //click invite button
            Pages.PS_InviteCreateNewUserPage.AddUserInviteBtn.Click();
            ActiveBrowser.WaitUntilReady();
        
            //Go to user profile page to set password
            Pages.PS_InviteCreateNewUserPage.UserIDLinkDiv.Wait.ForExists();
            Pages.PS_InviteCreateNewUserPage.UserIDLinkDiv.MouseHover();
            Actions.Click(Pages.PS_InviteCreateNewUserPage.UserIDLinkDiv.BaseElement);
            Pages.PS_InviteCreateNewUserPage.UserIDLinkDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_InviteCreateNewUserPage.MoreLink.Wait.ForExists();
            Pages.PS_InviteCreateNewUserPage.MoreLink.Click();
            ActiveBrowser.WaitUntilReady();

            //Click edit user link
            Pages.PS_UserProfilePage.EmailTableHeader.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_SettingsProfilePage.UserNameInput.Wait.ForExists();
            Pages.PS_SettingsProfilePage.SaveBtn.Wait.ForVisible();
        
            //Set password for the user
            Actions.SetText(Pages.PS_SettingsProfilePage.NewPasswordInput,password);
            Actions.SetText(Pages.PS_SettingsProfilePage.ConfirmPasswordInput,password);
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        
            //set username and password
            SetExtractedValue("CreatedUserName", username);
            SetExtractedValue("CreatedUserPwd", password);       
                         
        }
    
        [CodedStep(@"Click Add new configuration link")]
        public void CS_HMC_018_02()
        {
           Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
           ActiveBrowser.WaitUntilReady();
                                                                                        
        }
    
        [CodedStep(@"Wait till user navigates to Add/Edit configuration page")]
        public void CS_HMC_018_03()
        {
           Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
           Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
           Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
           Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
                                                                                                            
        }
    
        [CodedStep(@"Enter Name, description and select few modules")]
        public void CS_HMC_018_04()
        {
            string configName = "AutoHMC"+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Actions.SetText(Pages.PS_AddEditHomePage.NewConfigNameInput,configName);
            SetExtractedValue("GeneratedHMCName",configName);
            
            Actions.SetText(Pages.PS_AddEditHomePage.NewConfigDescTextArea,"Sample Config Description");
            
             string[] homePageOptions = {"My Executive Review",
                                           "My Dashboard",
                                            "My Projects"
                                        
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
        public void CS_HMC_018_05()
        {
                                                  
            Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
            Pages.PS_AddEditHomePage.SaveButton.Click();
            ActiveBrowser.WaitUntilReady();
                                        
        }
    
        [CodedStep(@"Wait for Admin - Configuration - Home Page to be displayed")]
        public void CS_HMC_018_06()
        {
            Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Home Page Configurations");
                
                                                
        }
    
        [CodedStep(@"Verify newly created empty configuration is displayed in table")]
        public void CS_HMC_018_07()
        {
                            
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
            string configName = GetExtractedValue("GeneratedHMCName").ToString();
            string configNameLocator = string.Format(AppLocators.get("home_page_config_table_record"),configName);
            Log.WriteLine(configNameLocator);
            Log.WriteLine(ActiveBrowser.Find.AllByXPath(configNameLocator).Count.ToString());
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(configNameLocator).Count > 0 , "Newly created configuration should be displayed");
            
        }
    
        [CodedStep(@"Click Change home pages link")]
        public void CS_HMC_018_08()
        {
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Click();
              ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                            
        }
    
        [CodedStep(@"wait till change home page popup is displayed with home page and user options")]
        public void CS_HMC_018_09()
        {
              Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.UsersDropdown.Wait.ForExists(); 
              
                                                
        }
    
        [CodedStep(@"Change the newly created empty home page config")]
        public void CS_HMC_018_10()
        {
              Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.SelectByText(GetExtractedValue("GeneratedHMCName").ToString(), true);
              
        }
    
        [CodedStep(@"Select option from Users dropdowns")]
        public void CS_HMC_018_11()
        {
            Pages.PS_HomePageConfigurationsPage.UsersDropdown.SelectByValue("USER", true);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            ActiveBrowser.RefreshDomTree(); 
            Pages.PS_HomePageConfigurationsPage.UserSelectionBoxSpan.Wait.ForExists();
                         
        }
    
        [CodedStep(@"Choose newly created user for home page")]
        public void CS_HMC_018_12()
        {
            Pages.PS_HomePageConfigurationsPage.UserSelectionBoxSpan.Click();
            Pages.PS_HomePageConfigurationsPage.GoBtn.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.UserChooserInputBox.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.PopupUserChooserSaveBtn.Wait.ForVisible();
            
            
            
            Actions.SetText(Pages.PS_HomePageConfigurationsPage.UserChooserInputBox,GetExtractedValue("CreatedUserName").ToString());
            Pages.PS_HomePageConfigurationsPage.GoBtn.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree(); 
            
            
            Pages.PS_HomePageConfigurationsPage.SearchResultDiv.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.SearchResultDiv.Wait.ForVisible();
            Pages.PS_HomePageConfigurationsPage.SearchResultDiv.Click();
            
            Pages.PS_HomePageConfigurationsPage.AddedUserListItem.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.AddedUserListItem.Wait.ForVisible();
            
            Pages.PS_HomePageConfigurationsPage.PopupUserChooserSaveBtn.Click();
                          
        }
    
        [CodedStep(@"Click OK and Confirm Home Page Selection")]
        public void CS_HMC_018_13()
        {
                                     
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageOkBtn.Click();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Click();                                               
              ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Login with newly created user")]
        public void CS_HMC_018_14()
        {
            ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
            Manager.ActiveBrowser.WaitUntilReady();
         
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.UsernameTextField,GetExtractedValue("CreatedUserName").ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.PasswordTextField,GetExtractedValue("CreatedUserPwd").ToString());
            Pages.PS_LoginPage.SignInSubmitButton.Click();
        
            if(ActiveBrowser.Find.AllByXPath("//div[@id='popLicense']").Count > 0){
                Pages.PS_LicencePopup.ContinueButton.Wait.ForVisible();
                Pages.PS_LicencePopup.ContinueButton.Click();
            }
        
            ActiveBrowser.WaitUntilReady();     
            Pages.PS_HomePage.LogOutLink.Wait.ForExists();
            Pages.PS_HomePage.CenterContainerDiv.Wait.ForVisible();
        }
        
        [CodedStep(@"Verify selected modules are present on user's home page")]
        public void CS_HMC_018_15()
        {
            string myDashLocator = string.Format(AppLocators.get("home_page_module_item"),"Dashboard");
            string myExecRevLocator = string.Format(AppLocators.get("home_page_module_item"),"Executive Review");
            string myProjLocator = string.Format(AppLocators.get("home_page_module_item"),"Projects");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myDashLocator).Count > 0, "Dashboard should be present on home page");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myExecRevLocator).Count > 0, "My Executive Review should be present on home page");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myProjLocator).Count > 0, "My Projects should be present on home page");

        }
    
        [CodedStep(@"Delete created configuration")]
        public void CS_HMC_018_16()
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
