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

    public class TST_HMC_008 : BaseWebAiiTest
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
        int yAfter;
    
        [CodedStep(@"Select Checkboxes for home page options")]
        public void CS_HMC_008_01()
        {
               string[] homePageOptions = {"My Executive Review",
                                        "My Dashboard",
                                        "My Projects",
                                        "My Ideas",
                                        "My Organizations",
                                        "My Folders",
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
        
        [CodedStep(@"Drag My Executive Review and move to anothher index")]
        public void CS_HMC_008_02()
        {
                                    
            string myExeRevLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            
            int yBefore =  myExeRevElement.BaseElement.GetRectangle().Y;

            string myFoldersLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Folders");
            HtmlDiv myFoldersElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myFoldersLocator);

            myExeRevElement.DragTo(myFoldersElement);

            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();

            HtmlDiv myExeRevElementAfter = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            yAfter =  myExeRevElementAfter.BaseElement.GetRectangle().Y;

            Assert.IsTrue(yAfter != yBefore, "My Executive Review position should change after drag operation");
                                       
        }
    
        [CodedStep(@"Click Save button")]
        public void CS_HMC_008_03()
        {
                                    
            Pages.PS_SettingsPreferencesPage.SaveBtn.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
                
        }
    
        [CodedStep(@"Verify that success message is displayed")]
        public void CS_HMC_008_04()
        {
                                 
            Pages.PS_SettingsPreferencesPage.MsgBoxDiv.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.MsgBoxDiv.Wait.ForVisible();
            string expectedMsg = "You have saved your preferences";
            Assert.IsTrue( Pages.PS_SettingsPreferencesPage.MsgBoxDiv.InnerText.Contains(expectedMsg) , "Successful save message should be displayed correctly");
        }       
    
        [CodedStep(@"Verify that changes are saved")]
        public void CS_HMC_008_05()
        {
         
            string myExeRevLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            
            int yAfterSave =  myExeRevElement.BaseElement.GetRectangle().Y;

            Assert.IsTrue(yAfterSave > yAfter, "My Executive Review position should change after save operation");
        
        }
        
        [CodedStep(@"Uncheck all options")]
        public void CS_HMC_008_06()
        {
           string[] homePageOptions = {"My Executive Review",
                                    "My Dashboard",
                                    "My Projects",
                                    "My Ideas",
                                    "My Organizations",
                                    "My Folders",
                                    };
        
           foreach (string e in homePageOptions) {
               string optLocator = string.Format(AppLocators.get("prefs_home_page_record_chkbx"),e);
               HtmlInputCheckBox chkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(optLocator);
               if(chkbox.Checked){
                   chkbox.Click();
                   System.Threading.Thread.Sleep(2000);
               }
           }    
           Pages.PS_SettingsPreferencesPage.SaveBtn.Wait.ForExists();
           Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
           ActiveBrowser.WaitUntilReady();
                                       
        }
    }
}
