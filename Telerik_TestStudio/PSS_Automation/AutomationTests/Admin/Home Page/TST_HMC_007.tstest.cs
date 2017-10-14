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

    public class TST_HMC_007 : BaseWebAiiTest
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
        
        int yBefore;
    
        [CodedStep(@"Drag My Executive Review and move to anothher index and verify the change")]
        public void CS_HMC_007_01()
        {
                        
            string myExeRevLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            
            yBefore =  myExeRevElement.BaseElement.GetRectangle().Y;
           
            string myFoldersLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Folders");
            HtmlDiv myFoldersElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myFoldersLocator);

            myExeRevElement.DragTo(myFoldersElement);

            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();

            HtmlDiv myExeRevElementAfter = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            int yAfter =  myExeRevElementAfter.BaseElement.GetRectangle().Y;
           
            Assert.IsTrue(yAfter != yBefore, "My Executive Review position should change after drag operation");
                                       
        }
    
        [CodedStep(@"Click Cancel button")]
        public void CS_HMC_007_02()
        {
                        
            Pages.PS_SettingsPreferencesPage.CancelBtn.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
        
        [CodedStep(@"Verify that user is navigated to Profile page")]
        public void CS_HMC_007_03()
        {
                     
            Pages.PS_UserProfilePage.EmailTableHeader.Wait.ForExists();
            Pages.PS_UserProfilePage.EmailTableHeader.Wait.ForVisible();
        }
    
        [CodedStep(@"Verify that cancelled changes are not saved")]
        public void CS_HMC_007_04()
        {
                     
            string myExeRevLocator = string.Format(AppLocators.get("prefs_home_page_drag_div"),"My Executive Review");
            HtmlDiv myExeRevElement = ActiveBrowser.Find.ByXPath<HtmlDiv>(myExeRevLocator);
            
            int yAfterCancel =  myExeRevElement.BaseElement.GetRectangle().Y;

            Assert.IsTrue(yAfterCancel == yBefore, "My Executive Review position should not change after cancel operation");
                
        }
    }
}
