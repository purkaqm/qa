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

    public class TST_MAT_013_C992594 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_MAT_013_C992594_ClickAdminLink()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Verify Grid with tags is displayed")]
        public void TST_MAT_013_C992594_VerifyGrid()
        {
            Pages.PS_TagsListPage.AddNewTagBtn.Wait.ForExists();
            Assert.IsTrue(Pages.PS_CustomFieldsPage.GridDiv.IsVisible(),"Grid should be visible in tag page"); 
        }
    
        [CodedStep(@"Click on Dependencies tab")]
        public void TST_MAT_013_C992594_ClickDepTab()
        {
            Pages.PS_TagsListPage.DepedenciesLink.Wait.ForExists();
            Pages.PS_TagsListPage.DepedenciesLink.Click();
        }
    
        [CodedStep(@"Verify Grid with Dependencies is displayed")]
        public void TST_MAT_013_C992594_VerifyDepGrid()
        {
            Pages.PS_TagDependenciesPage.AddNewButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_TagDependenciesPage.AddNewButton.IsVisible(),"Add New button should be vissible");
            Assert.IsTrue(Pages.PS_CustomFieldsPage.GridDiv.IsVisible(),"Grid should be visible in dependencies page"); 
            Assert.IsTrue(Pages.PS_TagDependenciesPage.AllFieldsTable.IsVisible(),"Table with all dependencies should be displayed");
        }
    
        [CodedStep(@"Click on Add New button")]
        public void TST_MAT_013_C992594_ClickAddNewButton()
        {
            Pages.PS_TagDependenciesPage.AddNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Bank To List button")]
        public void TST_MAT_013_C992594_ClickBackToList()
        {
            Pages.PS_TagDependenciesSummaryPage.BackToListLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify new tag is created")]
        public void TST_MAT_013_C992594_VerifyNewTag()
        {
            HtmlTableRow tagRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_tag_row"),GetExtractedValue("DependenciesTagName").ToString()));
            tagRow.Wait.ForExists();
            tagRow.IsVisible();
        }
        
        [CodedStep(@"Delete Dependency Tag")]
        public void TST_MAT_013_C992594_DeleteDepTag()
        {
            HtmlImage downArrowImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("tag_dependencies_down_arrow_img"),GetExtractedValue("DependenciesTagName").ToString()));
            downArrowImg.Wait.ForExists();
            downArrowImg.MouseClick(MouseClickType.LeftClick);
            //downArrowImg.Click();
            ActiveBrowser.RefreshDomTree();
            
            HtmlTableCell deleteCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(AppLocators.get("tag_dependencies_delete_cell"));
            deleteCell.Wait.ForExists();
            deleteCell.Click();
            Pages.PS_TagsListPage.DeleteTagYesButton.Wait.ForExists();
            Pages.PS_TagsListPage.DeleteTagYesButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
