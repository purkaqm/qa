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

    public class TST_HMC_013 : BaseWebAiiTest
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
        public void CS_HMC_013_01()
        {
           Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
           ActiveBrowser.WaitUntilReady();
                                                
        }
        
        [CodedStep(@"Wait till user navigates to Add/Edit configuration page")]
        public void CS_HMC_013_02()
        {
           Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
           Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
           Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
           Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
                                                            
        }
    
        [CodedStep(@"Enter Name")]
        public void CS_HMC_013_03()
        {
           Actions.SetText(Pages.PS_AddEditHomePage.NewConfigNameInput,"Sample Config Name");
        }
        
        [CodedStep(@"Enter Description")]
        public void CS_HMC_013_04()
        {
           Actions.SetText(Pages.PS_AddEditHomePage.NewConfigDescTextArea,"Sample Config Description");
        }
        
        [CodedStep(@"Select Checkboxes")]
        public void CS_HMC_013_05()
        {
            string[] homePageOptions = {"My Executive Review",
                                    "My Dashboard",
                                    "My Projects",
                                    "My Ideas",
                                    "My Organizations",
                                    "My Folders",
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
        
        [CodedStep(@"Move modules")]
        public void CS_HMC_013_06()
        {
            string myExeRevLocator = string.Format(AppLocators.get("new_hmc_config_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
                        
            string myFoldersLocator = string.Format(AppLocators.get("new_hmc_config_drag_div"),"My Folders");
            HtmlDiv myFoldersElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myFoldersLocator);

            myExeRevElement.DragTo(myFoldersElement);

            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();

                        
        }
        
        [CodedStep(@"Click Cancel button")]
        public void CS_HMC_013_07()
        {
            
           Pages.PS_AddEditHomePage.CancelButton.Click();
           ActiveBrowser.WaitUntilReady();
            
        }
        
        [CodedStep(@"Verify user navigates back to home page configuration page")]
        public void CS_HMC_013_08()
        {
                        
            Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Wait.ForExists();
            Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Home Page Configurations");
            
                        
        }
    }
}
