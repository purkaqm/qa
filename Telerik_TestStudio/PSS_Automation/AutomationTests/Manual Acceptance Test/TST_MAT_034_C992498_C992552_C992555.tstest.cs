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

    public class TST_MAT_034_C992498 : BaseWebAiiTest
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
        
        string projectName;
        
        // Add your test methods here...
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MAT_034_C992498_ClickSearchIcon()
        {
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchTypeImgIconSpan.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Set Search value")]
        public void TST_MAT_034_C992498_SetSearchValue()
        {
            projectName = "Project01";
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Focus();
            Pages.PS_HomePage.SearchInputText.MouseClick(MouseClickType.LeftClick);
            Manager.Desktop.KeyBoard.TypeText(projectName,10);
            ActiveBrowser.WaitForAjax(30000);
            System.Threading.Thread.Sleep(5000);
                        
        }
    
        [CodedStep(@"Click on Search value link")]
        public void TST_MAT_034_C992498_ClickSearchLink()
        {
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("quick_search_input"),CustomUtils.locationValue));
            HtmlListItem searchItem = ActiveBrowser.Find.ByXPath<HtmlListItem>(string.Format(AppLocators.get("quick_search_input"),CustomUtils.locationValue));
            searchItem.Wait.ForExists();
            searchItem.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for summary page to open")]
        public void TST_MAT_034_C992498_WaitSummaryPage()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
        }
    
        [CodedStep(@"Mouse hover to History icon")]
        public void TST_MAT_034_C992498_MouseHover()
        {
            Pages.PS_HomePage.HistoryLeftNavLink.MouseHover();
        }
    
        [CodedStep(@"Verify user is navigated to Manage History page")]
        public void TST_MAT_034_C992498_VerifyManageHistoryPage()
        {
            Pages.PS_ManageHistoryPage.GoButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ManageHistoryPage.SearchInputBox.IsVisible(),"User should be navigated to History page");
            Assert.IsTrue(Pages.PS_ManageHistoryPage.FromDateInputBox.IsVisible(),"User should be navigated to History page");
            Assert.IsTrue(Pages.PS_ManageHistoryPage.GoButton.IsVisible(),"User should be navigated to History page");
                                               
        }
    
        [CodedStep(@"Verify last visited page link was present on left navigation menu and Click to that link")]
        public void TST_MAT_034_C992498_VerifyLinkAndClick()
        {
            HtmlAnchor leftMenuProjectLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//span[@class='menuLevel root']/a[contains(.,'{0}')]",projectName));
            Assert.IsTrue(leftMenuProjectLink.IsVisible());
            leftMenuProjectLink.Click();
        }
    
        [CodedStep(@"Verify correct summary page was opened from History list")]
        public void TST_MAT_034_C992498_VerifySummaryPageHistory()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(string.Format( "{0} : Summary",projectName)));
        }
    
        [CodedStep(@"delete record and verify")]
        public void TST_MAT_034_C992498_DeleteRecord()
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
