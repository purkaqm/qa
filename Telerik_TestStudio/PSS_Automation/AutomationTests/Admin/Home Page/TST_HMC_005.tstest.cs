using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using System.Collections;
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

    public class TST_HMC_005 : BaseWebAiiTest
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
        
        
        ArrayList homePageConfigs;
        
        // Add your test methods here...
        [CodedStep(@"Get all home page configs")]
        public void CS_HMC_005_01()
        {
            homePageConfigs = new ArrayList();
                IList<HtmlAnchor>   configElements = ActiveBrowser.Find.AllByXPath<HtmlAnchor>(AppLocators.get("home_page_config_records_name"));       
               foreach (HtmlAnchor e in configElements) {
                   homePageConfigs.Add(e.InnerText);
            }                                
        }
        
        
    
        [CodedStep(@"Click Change home pages link")]
        public void CS_HMC_005_02()
        {
             Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Click();
             ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                
        }
    
        [CodedStep(@"Wait till change home page popup is displayed with home page and user options")]
        public void CS_HMC_005_03()
        {
              Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.UsersDropdown.Wait.ForExists(); 
                          
                                                
        }
        
        [CodedStep(@"Verify all configurations displayed on Home page to user dropdown")]
        public void CS_HMC_005_04()
        {
              string homePageToUseOptions = Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.InnerText;
              foreach (string s in homePageConfigs) {
                Assert.IsTrue(homePageToUseOptions.Contains(s),s + " is not present in the drop down");
              }   
                                           
        }
        
        [CodedStep(@"Verify Specific users, Users in group and Users having role options are available for Users dropdown")]
        public void CS_HMC_005_05()
        {
              string usersOptions = Pages.PS_HomePageConfigurationsPage.UsersDropdown.InnerText;
              Assert.IsTrue(usersOptions.Contains("Specific users"),"Specific users is not present in the drop down");
              Assert.IsTrue(usersOptions.Contains("Users in group"),"Users in group is not present in the drop down");
              Assert.IsTrue(usersOptions.Contains("Users having role"),"Users having role is not present in the drop down");
                                                
        }
    }
}
