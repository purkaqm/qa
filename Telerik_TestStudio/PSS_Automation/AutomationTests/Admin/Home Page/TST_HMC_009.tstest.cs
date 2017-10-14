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

    public class TST_HMC_009 : BaseWebAiiTest
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
        public void CS_HMC_009_01()
        {
            Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify user navigates to Add/Edit configuration page")]
        public void CS_HMC_009_02()
        {
            Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
            Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
            Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
            
                        
        }
        
        [CodedStep(@"Verify all modules are present for Home Page options with drod down feature")]
        public void CS_HMC_009_03()
        {
            string[] homePageOptions = {"My Executive Review",
                                        "My Dashboard",
                                        "My Projects",
                                        "My Issues",
                                        "My Ideas",
                                        "My Organizations",
                                        "My Folders",
                                        "My Visual Portal"};
            
               foreach (string e in homePageOptions) {
                   string optLocator = string.Format(AppLocators.get("new_hmc_config_opts_record"),e);
                   Assert.IsTrue(ActiveBrowser.Find.AllByXPath(optLocator).Count > 0 ,e + " should be present as option for home page");
               }                                
        
    }
    }
}
