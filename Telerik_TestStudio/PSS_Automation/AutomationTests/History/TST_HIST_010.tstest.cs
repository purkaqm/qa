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

    public class TST_HIST_010 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify user is navigated to Manage History page")]
        public void CSHIST01001()
        {
                                    Pages.PS_ManageHistoryPage.GoButton.Wait.ForExists();
                                    Assert.IsTrue(Pages.PS_ManageHistoryPage.SearchInputBox.IsVisible(),"User should be navigated to History page");
                                    Assert.IsTrue(Pages.PS_ManageHistoryPage.FromDateInputBox.IsVisible(),"User should be navigated to History page");
                                    Assert.IsTrue(Pages.PS_ManageHistoryPage.GoButton.IsVisible(),"User should be navigated to History page");
                                   
        }
    
        [CodedStep(@"verify history record with delete icon is present")]
        public void CSHIST01002()
        {
                                   
                                    
                                    IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
                                    Assert.IsTrue(nameElements[0].InnerText.Contains("Dashboard"),"Dashboard should be displayed in history table name coumn");
                                    IList<Element> delIconList = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_delete_icon_records"));
                                    Assert.IsTrue(nameElements.Count == delIconList.Count ,"Delete icon should be present for each record");
                                               
                                                
        }
    
        [CodedStep(@"delete record and verify")]
        public void CSHIST01003()
        {
                                   
                                    
                                    IList<Element> delIconList = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_delete_icon_records"));
                                    int countBeforeDelete = delIconList.Count;
            
                                    Actions.Click(delIconList[0]);       
                                    Pages.PS_ManageHistoryPage.DeletePopUp.Wait.ForExists();
                                    Pages.PS_ManageHistoryPage.DelPopUpDeleteBtn.Wait.ForVisible();
            
            
                                    Pages.PS_ManageHistoryPage.DelPopUpDeleteBtn.Click();
                                    ActiveBrowser.WaitUntilReady();
                                    System.Threading.Thread.Sleep(3000);
                                     this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
                                    IList<Element> delIconList2 = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_delete_icon_records"));
                                    int countAfterDelete = delIconList2.Count;
                                    Assert.IsTrue(countBeforeDelete == (countAfterDelete+1), "Record should be deleted successfully");           
        }
    }
}
