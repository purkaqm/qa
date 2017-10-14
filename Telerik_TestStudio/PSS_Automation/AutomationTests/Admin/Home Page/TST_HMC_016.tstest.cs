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

    public class TST_HMC_016 : BaseWebAiiTest
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
    
        [CodedStep(@"Click Add new configuration link")]
        public void CS_HMC_016_01()
        {
               Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
               ActiveBrowser.WaitUntilReady();
                                                                            
        }
    
        [CodedStep(@"Wait till user navigates to Add/Edit configuration page")]
        public void CS_HMC_016_02()
        {
               Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
               Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
               Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
               Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
                                                
                                                                                    
        }
    
        [CodedStep(@"Enter Name")]
        public void CS_HMC_016_03()
        {
                string configName = "AutoHMC"+Randomizers.generateRandomInt(10000,99999);
                ActiveBrowser.Window.SetFocus();
                Actions.SetText(Pages.PS_AddEditHomePage.NewConfigNameInput,configName);
                SetExtractedValue("GeneratedHMCName",configName);
        }
    
        [CodedStep(@"Enter Description")]
        public void CS_HMC_016_04()
        {
                
                Actions.SetText(Pages.PS_AddEditHomePage.NewConfigDescTextArea,"Sample Config Description");
                       
        }
    
        [CodedStep(@"Click Save button")]
        public void CS_HMC_016_05()
        {
                                              
                Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
                Pages.PS_AddEditHomePage.SaveButton.Click();
                ActiveBrowser.WaitUntilReady();
                                    
        }
    
        [CodedStep(@"Wait for Admin - Configuration - Home Page to be displayed")]
        public void CS_HMC_016_06()
        {
                Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Wait.ForExists();
                Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Wait.ForExists();
                Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Home Page Configurations");
                
                        
        }
    
        [CodedStep(@"Verify newly created empty configuration is displayed in table")]
        public void CS_HMC_016_07()
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
        public void CS_HMC_016_08()
        {
                  Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Click();
                  ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                        
        }
    
        [CodedStep(@"wait till change home page popup is displayed with home page and user options")]
        public void CS_HMC_016_09()
        {
                  Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.Wait.ForExists();
                  Pages.PS_HomePageConfigurationsPage.UsersDropdown.Wait.ForExists(); 
                                                    
        }
    
        [CodedStep(@"Change the newly created empty home page config")]
        public void CS_HMC_016_10()
        {
                  Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.SelectByText(GetExtractedValue("GeneratedHMCName").ToString(), true);
                  
        }
    
        [CodedStep(@"Click home Link Left Panel")]
        public void CS_HMC_016_11()
        {
                          
                Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
                Pages.PS_HomePage.HomeLeftNavLink.Click();
                ActiveBrowser.WaitUntilReady();
                                                
        }
    
        [CodedStep(@"Verify No module displayed on home page")]
        public void CS_HMC_016_12()
        {
                  Assert.IsTrue(ActiveBrowser.Find.AllByXPath("\\h2").Count == 0, "No module should be present on home page");
              
        }
    
        [CodedStep(@"Delete created configuration")]
        public void CS_HMC_016_13()
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
        
        [CodedStep(@"Select option from Users dropdowns ")]
        public void CS_HMC_016_14()
        {
            Pages.PS_HomePageConfigurationsPage.UsersDropdown.SelectByValue("USER", true);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            ActiveBrowser.RefreshDomTree(); 
            Pages.PS_HomePageConfigurationsPage.UserSelectionBoxSpan.Wait.ForExists();
                         
        }
        
        [CodedStep(@"Choose current user for home page")]
        public void CS_HMC_016_15()
        {
            Pages.PS_HomePageConfigurationsPage.UserSelectionBoxSpan.Click();
            Pages.PS_HomePageConfigurationsPage.GoBtn.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.UserChooserInputBox.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.PopupUserChooserSaveBtn.Wait.ForVisible();
            
            
            
            Actions.SetText(Pages.PS_HomePageConfigurationsPage.UserChooserInputBox,GetExtractedValue("CurrentUsername").ToString());
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
        public void CS_HMC_016_16()
        {
                         
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageOkBtn.Click();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageConfirmYesBtn.Click();                                               
              ActiveBrowser.WaitUntilReady();
        }
    }
}
