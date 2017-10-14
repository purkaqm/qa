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

    public class TST_HMC_014 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify My Executive Review and My Organizations are present for Home Page options")]
        public void CS_HMC_014_01()
        {
                string[] homePageOptions = {"My Executive Review",
                                       
                                        "My Organizations",
                                       };
            
               foreach (string e in homePageOptions) {
                   string optLocator = string.Format(AppLocators.get("prefs_home_page_opts_record"),e);
                   Assert.IsTrue(ActiveBrowser.Find.AllByXPath(optLocator).Count > 0 ,e + " should be present as option for home page");
               }                                
        }
    
        [CodedStep(@"Select My Executive Review and My Org Checkboxes")]
        public void CS_HMC_014_02()
        {
               string[] homePageOptions = {"My Executive Review",
                                           "My Organizations"
                                        
                                        };
            
               foreach (string e in homePageOptions) {
                   string optLocator = string.Format(AppLocators.get("prefs_home_page_record_chkbx"),e);
                   HtmlInputCheckBox chkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(optLocator);
                   if(!chkbox.Checked){
                       chkbox.Click();
                       System.Threading.Thread.Sleep(2000);
                   }
               }      
                                                               
        }
    
        [CodedStep(@"Drag My Executive Review to Set Order on home page")]
        public void CS_HMC_014_03()
        {
                                                            
            string myExeRevLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
             
            string myFoldersLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Organizations");
            HtmlDiv myFoldersElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myFoldersLocator);
            
            myExeRevElement.DragTo(myFoldersElement);
            
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
                                                                 
        }
        
        [CodedStep(@"Click Save button")]
        public void CS_HMC_014_04()
        {
                                  
            Pages.PS_SettingsPreferencesPage.SaveBtn.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
                                    
        }
    
        [CodedStep(@"Click home Link Left Panel")]
        public void CS_HMC_014_05()
        {
                      
            Pages.PS_HomePage.HomeLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
                                    
        }
    

        [CodedStep(@"Verify My Organization and My Executive Review sections are present on Home Page")]
        public void CS_HMC_014_07()
        {
            string myOrgsLocator = string.Format(AppLocators.get("home_page_module_item"),"My Organizations");
            string myExecRevLocator = string.Format(AppLocators.get("home_page_module_item"),"Executive Review");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myOrgsLocator).Count > 0, "My Organizations should be present on home page");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(myExecRevLocator).Count > 0, "My Executive Review should be present on home page");

        }
        
         [CodedStep(@"Uncheck My Executive Review and My Org Checkboxes and Save")]
        public void CS_HMC_014_08()
        {
           string[] homePageOptions = {"My Executive Review",
                                       "My Organizations"
                                    
                                    };
           foreach (string e in homePageOptions) {
               string optLocator = string.Format(AppLocators.get("prefs_home_page_record_chkbx"),e);
               HtmlInputCheckBox chkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(optLocator);
               chkbox.Click();
               System.Threading.Thread.Sleep(2000);
            }  
           Pages.PS_SettingsPreferencesPage.SaveBtn.Wait.ForExists();
           Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
           ActiveBrowser.WaitUntilReady();
                                                               
        }
        
       
    }
}
